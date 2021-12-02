---
layout: post
title: 'bug现场谜之进程为什么消失了'
subtitle: '谜之bug现场：进程好好的不见了，找不到他杀的任何证据，总不能是自杀吧。复活它之后某一个时刻消失，再复活再消失...'
background: '/img/posts/bug-scene-of-disappeared.jpg'
comment: false
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 真相浮出水面](#3)
- [4. 总结](#4)

---

<h3 id="1">1. bug现场情况</h3>

2021年11月，客户现场某业务系统挂掉，运维发现线程没了，看日志也没有任何报错信息。

运维重启服务，系统运行正常，第二天或者隔几天又挂掉...

值得注意的现象：周五重启的服务，周末基本没人用服务，系统周末正常运行，周一上班，大家一用系统就又挂了。

<h3 id="2">2. 尝试破案</h3>

因为没有任何错误日志输出，首先想到的是由于操作系统资源限制导致的，比如内存超过设置的最大内存、CPU、socket、打开文件数等。如果是这样，操作系统级别会杀死进程，会记录相关日志。

**1. 查看系统级别的日志**

查看系统日志：`cat /var/log/messages |grep -i 'kill'`

查看内核级别的日志：`dmesg |grep -i 'kill'`

没有查到任何java、OOM、或者kill相关的系统级记录。

**2. 查看JVM crash日志**

系统级别的日志没有，可能是JVM本身bug造成的crash，这样JVM在crash的时候会生成`hs_err_pid_xxx.log`，该文件默认生成在JVM运行的工作目录中，也可以在JVM启动的时候指定路径：`-XX:ErrorFile=/var/log/hs_err_pid<pid>.log`

工作目录中没有找到JVM crash日志。

**3. 查看OOM Heap Dump日志**

系统启动的时候如果设定了JVM参数`-XX:+HeapDumpOnOutOfMemoryError`和`-XX:HeapDumpPath=*/java.hprof`并且是由OOM导致的crash，可以在对应的路径上找到heap dump文件，使用`jvisualvm`这种工具分析dump文件也可以定位问题。

该系统有设定对应的JVM参数，对应路径上没有dump日志输出。

什么日志都没有，会不会是什么问题导致日志没有生成呢？因为周一客户一旦使用就发生宕机，和怀疑是内存问题导致的，监控一下内存吧：

**4. 监控OS进程内存、JVM堆内存**

使用`top`命令监控OS进程内存脚本：

```shell
#!/bin/bash

while true
do
  datetime=$(date '+%Y-%m-%d %H:%M:%s')
  echo "$datetime" >> record_new3.txt 2>&1
  top -d 1 -b -n1 |grep $PID >> record_new3.txt 2>&1
  sleep 60
done
```

使用`jmap`命令监控JVM堆内存：

```shell
#!/bin/bash

while true
do
  datetime=$(date '+%Y-%m-%d %H:%M:%s')
  echo "$datetime" >> record_jmap_new3.txt 2>&1
  /home/jdk8u282-b08/bin/jmap -heap $PID >> record_jmap_new3.txt 2>&1
  sleep 120
done
```

分析日志发现：

- JVM堆内存从3G到接近14G（JVM设置的是最大堆内存16G），然后JVM会不时进行GC，内存就会降下来，没有出现堆内存溢出情况。
- java进程的内存不断增大，最后维持在16G左右（系统内存是256G，还非常充裕）。
    - 当JVM进行GC的时候JVM堆内存是回收了一部分，但是对于分配给操作系统JAVA进程的内存不会回收，在JVM堆中GC时，释放的内存只是标记为空间可用。所以这也是为什么系统级别JAVA进程内存一直增大，最后维持一个较大的值不变（这种是堆外内存正常的情况，一些情况下堆外内存持续增加最终导致进程被OS杀死，说明是一些其他原因造成堆外内存异常增长，也要考虑这种情况）。

内存确实没有问题：堆内存增加到一定程度后JVM GC会将下来，系统进程内存最后在一个稳定的值。

---
