---
layout: post
title: 'JVM年轻代Minor GC详解及相关JVM调优'
subtitle: 'JVM为什么要将堆分为不同的Generation？JVM年轻代的GC是如何进行的？这些与JVM性能调优有什么关系？我们深入探讨...'
background: '/img/posts/jvm-minor-gc-algorithm.jpg'
comment: true
---

# 目录

- [1. 为什么年轻代如此重要](#1)
- [2. 年轻代Minor GC是如何进行的](#2)
- [3. 年轻代相关的JVM性能调优参数](#3)

---

<h3 id="1">为什么年轻代如此重要</h3>

如果仅仅从JVM的实现功能来说，JVM并不需要一个年轻代，对象的创建回收仅仅在`Heap`当中进行就可以了。将`Heap`分出来一个年轻代是为了进行更好的GC回收性能优化。具体来说将`Heap`分为`Yong Generation`和`Old Generation`有两个方面的主要目的：

- 简化了创建对象时分配内存空间的复杂性(仅仅在`Yong Generation`中为新生成的对象分配空间)。
- 根据`Yong Generation`和`Old Generation`的不同特点，可以分别使用不同的GC算法。

大量的数据表明，Java应用程序都遵循一个共同的特征：

1. 大部分的Java对象在很年轻的时候就死了。也就是说，一个Java对象在创建之后，在这个应用运行过程中，很多都不会再被引用。
2. 很少有新生成的对象被已经存活很久的老对象引用。

基于上述两方面的考虑，JVM需要有一个很快速的方法能够获取新生成的对象，比如说一个专门的空间来存这些新的对象。这样，`Heap`分为`Yong Generation`和`Old Generation`，新生成的对象都在`Yong Generation`中，`Old Generation`中存放的是长时间存活的对象，JVM的GC算法就可以在`Yong Generation`中快速回收死亡的对象，而不必要遍历`Heap`上所有对象一遍。

ORACLE HotSpot JVM进了一步将`Yong Generation`分为三个子区域：一个相对较大的`Enden`区域和两个较小的`Suvivor`区域(分别作为`From`和`To`)。

> 下图是`Heap`区域的整个内存划分：

![Heap Memory Model](/img/posts/java-heap-model.png "Heap Memory Model")

---

<h3 id="2">年轻代Minor GC是如何进行的</h3>

基于上述的`Yong Generation`的内存模型，新创建的对象是在`Enden`上分配内存的(如果新生成的对象足够大，不足以在`Enden`上分配，该对象会被直接分配到`Old Generation`中)，在第一次Minor GC(对`Yong Generation`的GC称为Minor GC)中，`Enden`区存活的对象(Live Object)会被复制到`Suvivor`区域中，这些对象一直存活在那里直到达到一定的年龄(这里所谓的年龄是指在它们被创建后经历了对少次的GC)，然后仍然存活的对象会被复制到`Old Generation`中。`Suvivor`区域的目的是让哪些新生成的对象在第一次GC之后存活的稍微长时间一点，这样以便于在它们死的时候能够被回收。

基于大部分的新生对象都会在GC的过程中被回收，JVM对`Yong Generation`区域采取一种COPY策略(复制算法)：

1. 最开始，`Enden`、`From`和`To`区域都是空的，新生成的对象会分配在`Enden`中。
2. 新生成的对象多了以后，第一次Minor GC会将`Enden`区域中存活的对象复制到`From`区域中，复制到`Suvivor`区域中的对象会记录该对象的经历GC的次数，以便标记该对象存活的年龄。
3. 接下来会触发多次GC，每一次GC会将`Enden`中存活的对象复制到`To`区域。`From`区域中存活对象会根据其年龄决定其去向：如果年龄没有达到GC规定的年龄，则对象也会被复制到`To`区域中；反之会被复制到`Old Generation`中。这时候`Enden`和`From`区域可以被清空了(因为里面的对象全部是死对象)，只有`To`去中存在存活的对象(这里面的对象有不同的年龄(或者说经历的不同的GC次数))。
4. 最后，`From`和`To`区域会互换角色，这样再下次GC中同样会从`Enden`和`From`区域中复制对象，`To`区域始终保持为空，等待下次GC时接受存活的对象。

> 下图表示一次Minor GC过程。绿色表示未使用空间，红色表示存活的对象，黄色表示死亡对象。这个示例中假设`Suvivor`有足够的区域来存放活着的对象。

![一次Minor GC过程](young_gc.png "一次Minor GC过程")

总结一下`Yong Generation`中对象的生命历程：对象的创建一定发生在`Enden`区中(除非对象太大，`Enden`区放不下)，接下来生命力顽强的对象会在`Suvivor`去中被复制来复制去，经历了多次GC之后，如果还存活会被复制到`Old Generation`中，当然,过程中可能会被GC回收。

---

<h3 id="2">年轻代相关的JVM性能调优参数</h3>

通过上述内容，我们知道`Yong Generation`的大小对于JVM性能来说是至关重要的：如果`Yong Generation`过小，新生成的对象很容易就进入到`Old Generation`中，需要更大的成本来进行GC回收；如果`Yong Generation`过大，新生成的对象会再`Suvivor`区域中被复制来复制去，同样会影响JVM的性能。很不幸的是，没有一个固定的标准是最优的，最优策略需要根据Java应用的特点进行不同的测试得出来，这个时候JVM参数就很有用处了。

**-XX:NewSize and -XX:MaxNewSize**

与设定整个`Heap`大小的参数(`-Xmx & -Xms`)一样，同样可以通过参数来设定`Yong Generation`大小的最大和最小限制。当我们设定`-XX:MaxNewSize`大小的时候，我们要考虑到`Yong Generation`是`Heap`的一部分，它的大小应该小于`Old Generation`的大小，这是因为在最坏的情况下，我们会将`Yong Generation`的所有对象复制到`Old Generation`中，因此`-Xmx/2`是`-XX:MaxNewSize`的上限。

出于性能方面的考虑，我们可以用参数`-XX:NewSize`指定`Yong Generation`的初始化大小。这种方法很有效，尤其是当我们经过测试知道新生代与老生代分配地址比例的时候。

**-XX:NewRation**

`-XX:NewRation`可以设定`Yong Generation`和`Old Generation`的大小比例关系。

如果混合使用了初始化、上限和比例的参数，例如下面的场景：

`java -XX:NewSize=32M -XX:MaxNewSize=512M -XX:NewRation=3 MyApp`

在这样设定的情况下，JVM会试图让`Yong Generation`是`Old Generation`大小的1/3，但是不会让`Yong Generation`的大小低于32M或者高于512M。

**-XX:SuvivorRatio**

`-XX:SuvivorRatio`可以设定`Enden`和`From`或者`To`的比例。例如`-XX:SuvivorRatio=10`表示`Enden`占`Yong Generation`的10/12，`From`和`To`都占`Yong Generation`的1/12。

**-XX:+PrintTenuringDistibution**

当设定`-XX:+PrintTenuringDistibution`时，就是告诉JVM打印出`Suvivor`区域所有对象年龄分布(age distribution)。例如：

    Desired survivor size 75497472 bytes, new threshold 15 (max 15)
    - age   1:   19321624 bytes,   19321624 total
    - age   2:      79376 bytes,   19401000 total
    - age   3:    2904256 bytes,   22305256 total

> 第一行表示`Suvivor To`区域中大约的容量是75M，对象经历GC的阈值(threshold)是15次。紧接着第二行表示`Suvivor To`区域中有大约19M对象经历了1次GC，这时候`Suvivor TO `区域中中也有大约19M对象，说明第一次GC所有的对象都存活过来，并且被复制到了`Suvivor From`中。最后一行表示`Suvivor To`区域中有大约2M的对象经历3次GC，这时候`Suvivor TO`区域中大约有22M对象，没有超过第一行的容量75M。

还有很多参数：`-XX:InitalTenuringThreadhold`、`-XX:MaxTenuringThreadhold`、`-XX:TargetSuvivorRatio`、`-XX:+NeverTenure`、`-XX:+AlwaysTenure`等。

总之，`Yong Generation`的大小在整个应用JVM性能调优中起着很重要的作用，在设定`Yong Generation`的参数时，一定要兼顾`Old Generation`的带下，才能很好的起到作用。
