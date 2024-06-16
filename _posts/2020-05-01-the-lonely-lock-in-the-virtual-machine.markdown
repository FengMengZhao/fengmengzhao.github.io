---
layout: post
title: '虚拟世界中孤独的锁'
subtitle: 'Java在语言级别支持多线程编程是Java重要的特性之一，通过不同类型的锁(互斥锁和信号锁)实现了两点：一，为了保护共享资源安全的线程之间的互斥性；二、为了完成同一个目标线程之间的协作性。锁，看不见、摸不着，但确确实实存在。本文从锁在JVM内部实现的角度，试图理解锁的概念，弄明白锁的内部实现和语言级别的特性。'
background: '/img/posts/lock-in-jvm.jpg'
comment: true
---

# 目录

- [1. 锁的概念](#1)
- [2. 锁在JVM内部的实现](#2)

---

<h3 id="1">锁的概念</h3>

在Java多线程中，最基本的概念就是锁。每一个Java对象都有与之相关联的锁。我们也会经常听说`Monitor`的概念，每一个Java对象都有与之相关联的`Monitor`，Java是通过`Monitor`来实现线程之间的互斥和协作的。

那么，锁(`lock`)和`Monitor`有什么区别呢？

锁可以理解为Java对象数据的一部分，在JVM内部实现中有专一的一块数据是用来实现锁机制的。

`Monitor`可以理解为一个逻辑概念，分为两种：`互斥Monitor`和`信号Monitor`。`互斥Monitor`通过锁来保证线程之间的互斥性；`信号Monitor`通过Java对象的`wait()`和`notify()`方法来实现线程之间的协作性，而wait和notify方法的内部也是通过Java对象与之相关联的一份数据实现的。

我们可以把`Monitor`比作一个大建筑，这个大建筑里面有一块数据区域称之为`lock`。线程进入这个大建筑称之为`Enter Monitor`；线程获取大建筑里面的`lock`称之为`Acquire Monitor`；线程占有大建筑里面的`lock`称之为`Owning Monitor`；线程释放大建筑里面的`lock`称之为`Release Monitor`；线程退出大建筑称之为`Exit Monitor`。

接下来详细描述`Monitor`在JVM内部的实现及怎么做到线程之间的互斥和同步。

---

<h3 id="2">锁在JVM内部的实现</h3>

简单看一下Java对象在JVM堆内存中的数据结构：

![java-object-structure-in-heap](/img/posts/java-object-structure-in-heap.png "Java对象在堆内存中的数据表示")

上图只是某一个特定的JVM实现对堆中对象数据结构的表示，不同的JVM实现会不尽相同。

对象数据中最上面的一个引用`ptr to class data`是指向方法区的类型信息(`class data`)，用于对象在运行时能够获取类的相关信息，比如类的名称、类型、接口、静态变量、方法等。

接下来的几个引用都是对象的实例数据(`instance data`)，也就是我们在类中定义的实例属性。

实际上在上图中有没有展示出来的一部分数据，这部分数据逻辑上属于堆中对象数据的一部分，称之为对象的锁(`lock`)。JVM中的每一个对象都有与之相关联的一个锁，通过这个锁能够实现多线程协作访问同一个对象。同一时间仅仅由一个线程可以拥有一个对象的锁。如果一个对象的锁被一个线程占用，只有该线程可以访问该对象的实例数据，所有其他试图获取该对象锁的线程都会被等待，直到拥有该对象锁的线程释放这个对象的锁。一旦一个线程拥有了一个对象的锁，那么该线程可以多次获取并拥有该对象的锁；当该线程释放该锁时，也需要释放多次。

许多Java对象在它整个的生命周期中都不会被一个线程请求锁，用于实现锁的与Java对象相关联的数据实际上只有当线程请求锁的时候才需要创建。这样，像上图所示，许多JVM实现在对象数据中都不会有一个指向锁数据的引用，而是当线程第一次请求该对象锁时创建一份必要的数据来表示锁。

和Java对象都有一个相关联的锁数据类似，每一个Java对象都还有与之相关联的一份数据用来实现`wait set`，也就是对`java.lang.object`中`wait()`和`notify()`方法的实现。`wati set`数据在JVM的实现中也是当线程调用`wait()`或者`notify`方法是才会在内存中创建。锁用来帮助线程间访问同一份数据时不会相互干扰，避免造成数据的不一致性；而`wait set`帮助线程间为完成共同的目标能够协同工作。

总结来说，与Java对象相关联的数据有两种：一种是锁数据，另一种是`wait set`。前者就是通常意义上的`lock`，用来保证线程之间独立工作，不相互干扰；后者是通常意义上说的`wait()`和`notify()`方法，用来保证线程之间能够协作完成目标。把这两份数据的实现用一个逻辑概念来表示，就是`Monitor`。

`Monitor`除了有与之相关联的数据之外，与之相关联还有代码，称之为临界区。临界区的代码一次只能由一个独立的线程来执行，也就是说一个线程必须从临界区的开始执行到结束，这期间不会有拥有相同`Monitor`的其他线程的并发执行。`Monitor`保证了同一时间只有一个线程执行临界区代码。只有当一个线程执行到临界区代码的时候才能够`Enter Monitor`，也只有当线程`Acquire Monitor`之后才能后在临界区中继续执行代码。

当一个线程执行代码到达临界区时，线程会被放入与`Monitor`相关联的一个`entry set`中，如果`entry set`中没有其他线程正在拥有Monitor，该线程就会请求Monitor并进一步执行临界区的代码。当线程执行完临界区的代码后，该线程会退出(并释放)Monitor。

当一个线程执行代码到达临界区的锁已经被另外一个线程拥有，新进入`entry set`的线程必须等待。当拥有Monitor的线程退出Monitor之后，新进入的线程必须和在`wait set`中等待的其他线程一起进行竞争，只有一个线程能够成功获取刚刚释放的Monitor。

上述Monitor实现的并发，我们称之为`互斥Monitor`。这种并发模式保证了多线程共享同一份数据时，同一时间只有一个线程能够执行临界区的代码，保护了共享数据的安全性。Monitor实现的并发除了保证线程之间的互斥性，还有一种方式是保证线程之间能够朝着同一个目标协作执行，这种Monitor称之为`协作Monitor`。

当一个线程在一个特定对象处于特定状态的时候索取数据，而另一个线程负责让该特定对象处于特定状态时，线程之间的协作就尤为重要。例如，一个写线程负责从一个缓冲区中写数据，而另一个线程要从该缓冲区读数据。读线程在读取数据之前需要保证该缓冲区不为空，同样写线程在写数据之前需要保证该缓冲区没有写满。如果读线程发现缓冲区没有数据时必须要进行等待，直到写线程写入数据之后，读线程才能做一些读取数据的操作。

这种`协作Monitor`又称为`Wait and Notify Monitor`。在这种`Monitor`中，对象可以执行`wait()`或者`notify()`方法让当前线程挂起并释放锁，进入到`wait set`中。该被挂起的线程会一直会处于挂起状态直到另外一个线程执行相同Monitor的`notify`方法。当某个特定线程执行相同的Monitor的`notify`方法之后，该特定线程继续拥有Monitor并执行临界区代码，直到执行完临界区代码或者调用`wait`方法时，才会退出Monitor。特定线程执行`notify`方法并退出Monitor之后，在`wait set`中等待的线程会被激活并重新竞争Monitor。

`Wait and Notify Monitor`又被称为`Singnal and Continue Monitor`，这是因为当一个线程`notify`一个信号之后，该线程将保持持有`Monitor`并继续执行临界区中代码。一段时间之后`Notifying`线程释放了锁，`Waiting`线程被激活重新参与竞争。试想一下，当一个线程进入临界区获取锁之后，发现并不能做一些有意义的事情；而另一个线程获取锁后执行的代码就是上一个线程所需要的，这时候这种`Wait and Notify`机制就非常重要。它解决了两个方面的问题：1，通过这种方式，线程之间的关系是协作的，而不是"恶性竞争"的，这样就避免的资源的浪费；2，JVM的线程Model是基于线程优先级抢占式的，高优先级的线程会优先于低优先级线程执行。当高优先级的线程block或者dead时，低优先级的线程才能有机会执行，如果在低优先级线程执行的过程中，高优先级的线程从block变为了runnable，JVM会中断低优先级线程的执行继续执行高优先级线程。针对优先级不同的线程之间的合作，使用这种模式就能够避免高优先级的线程一直执行，而低低优先级的线程没有机会。

举例来说，还是上述的一个读线程和一个写线程分别从缓冲区中读写数据的示例，其中缓冲区是被`Monitor`保护着。当读线程进入被保护的Monitor，它会检查这个缓冲区是否为空。如果不为空，读线程从缓冲区中读取(或者删除)一些数据，退出Monitor；如果缓冲区为空，读线程会执行一个`wait()`方法，一旦读线程执行了`wait()`方法，该线程会立刻被挂起并且放到`wait set`中，同时读线程会释放Monitor，让Monitor资源对其他线程可用。一段时间之后，写线程进入了Monitor，调用了Notify方法并执行了临界区的代码后退出了Monitor。当读线程执行`notify()`方法之后，被挂起的读线程被标记后续会被激活，当写线程真正退出Monitor之后，读线程被激活并真正参与到Monitor的竞争中。当读线程竞争得胜再次获取Monitor之后，必须要再次检查缓冲区是否为空(因为在这之前可能有其他过来的读线程消费了缓冲区的数据)，如果缓冲区不为空，则读线程消费数据，如果为空，读线程再次执行`wait`方法，进入`wait set`中等待notify信号。

用一张图来解释这个过程：

![read-and-write-thread-example.png](/img/posts/read-and-write-thread-example.png "读写线程示例")

上图所示一些需要和Monitor进行交互必须通过的`"门"`，当新入线程进入临界区开始时，该线程会从最左边的那个`"门1"`进入`entry set`，如果当前状态下`锁`没有被占有并且在`entry set`没有其他等待(Waiting)线程，该线程就会立刻从`"门2"`通过，进而拥有Monitor并继续执行临界区的代码；如果当前状态下锁已经被占用，新到的线程必须在`entry set`(`entry set`中可能也有其他正在等待的线程)中等待并被阻塞，不会执行任何临界区的代码。

上图所示在`entry set`和`wait set`中分别有三个和四个线程被挂起，这些被挂起的线程将一直处于挂起状态直到当前活跃线程释放Monitor。当前活跃线程可以通过两种方式释放Monitor：1，执行完临界区的代码退出；2，调用对象的`wait()`方法。如果活跃线程继续执行并执行完临界区代码，则该线程通过图中下面的`"门5"`退出Monitor；如果该线程执行了`wait()`方法，则它会通过`"门3"`进入到`wait set`中。

如果活跃线程在退出Monitor之前没有执行`notify()`方法，则只会有`entry set`中的线程参与新一轮Monitor的竞争；如果该线程在退出Monitor之前执行了`notify()`方法，则`entry set`和`wait set`中的线程都会参与新一轮Monitor的竞争。在新一轮的Monitor竞争中，如果`entry set`中的线程获取到了Monitor，该线程会穿过`"门2"`进而拥有Monitor；如果是`wait set`中线程获取到了Monitor，该线程会退出`wait set`并穿过`"门4"`拥有Monitor。`"门3"`和`"门4"`是线程进入或者退出`entry set`仅有的途径，如果当前线程拥有Monitor，它可以通过执行`wait()`方法进入`wait set`，进入`wait set`之后，也只有相同Monitor的对象执行了`notify()`方法之后，该线程带可能退出`wait set`。

总结来说，锁可以理解为Java对象数据的一部分，分为两种：1，`entry set`；2，`wait set`。这两部分数据都是当线程获取`锁`或者执行`wait()`、`notify()`方法时在堆中创建的一份必要的数据来表示，逻辑上属于Java对象数据的一部分。Monitor可以理解为一个逻辑概念，与锁数据相对应，也分为两种：1，`互斥Monitor`；2，`协作Monitor`。表示多线程之间互斥访问和写作方访问的一种管理机制。

---

**参考文章:**

- [https://www.artima.com/insidejvm/ed2/threadsynch.html](https://www.artima.com/insidejvm/ed2/threadsynch.html "Inside the Java Virtual Machine by Bill Venners")
- [https://www.artima.com/insidejvm/ed2/jvm.html](https://www.artima.com/insidejvm/ed2/jvm.html "Inside the Java Virtual Machine by Bill Venners")

**了解更多:**

- [https://fengmengzhao.github.io/2017/11/10/java-essential.html#4.5](https://fengmengzhao.github.io/2017/11/10/java-essential.html#4.5 "冯兄话吉-java-essential")
- [https://fengmengzhao.github.io/](https://fengmengzhao.github.io/ "冯兄话吉-博客")
