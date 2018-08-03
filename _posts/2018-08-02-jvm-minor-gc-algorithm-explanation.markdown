---
layout: post
title: 'JVM年轻代Minor GC详解及相关JVM调优'
subtitle: 'JVM为什么要将堆分为不同的Generation？JVM年轻代的GC是如何进行的？这些与JVM性能调优有什么关系？我们深入探讨...'
background: '/img/posts/jvm-minor-gc-algorithm.jpg'
comment: true
---

#目录

- [1. 为什么年轻代如此重要](#1)
- [2. 年轻代Minor GC是如何进行的](#2)
- [3. 年轻代相关的JVM性能调优参数](#3)

---

<h3 id="1">为什么年轻代如此重要</h3>

如果仅仅从JVM的实现功能来说，JVM并不需要一个年轻代，对象的创建回收在`Heap`当中进行就可以了。
