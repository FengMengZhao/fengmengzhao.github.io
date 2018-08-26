---
layout: post
title: Java Essential
---

> Java的基本思想、概念

## 目录

- [1. Java思想](#1)
    - [1.1 面向对象 VS 面向过程](#1.1)
    - [1.2 抽象(Abstraction),封装(Encapsulation),协作(Cohesion)](#1.2)
- [2. Java类集框架](#2)
- [3. Java I/O流体系](#3)
    - [3.1 字节流(Byte Stream)](#3.1)
    - [3.2 字符流(Character Stream)](#3.2)
    - [3.3 Buffered Stream](#3.3)
    - [3.4 Java NIO](#3.4)
- [4. Java多线程](#4)
    - [4.1 进程 VS 线程](#4.1)
    - [4.2 线程对象](#4.2)
    - [4.3 同步(Synchronization)](#4.3)
    - [4.4 死锁](#4.4)
    - [4.5 Guarded Block](#4.5)
    - [4.6 不可更改(Immutable)对象](#4.6)
    - [4.7 并发进阶](#4.7)
    - [4.8 并发安全策略](#4.8)
    - [4.9 Java并发实践(干货-读取并入库large-size XML文件)](#4.9)
- [5. JVM内存模型](#5)
    - [5.1 JVM内存模型](#5.1)
    - [5.2 Java垃圾回收](#5.2)
    - [5.3 Java类加载](#5.3)
        - [5.3.1 Tomcat类加载](#5.3.1)
- [6. Heap dump Anylysis](#6)
    - [6.1 内存溢出实例](#6.1)
    - [6.2 通过工具进行Heap dump](#6.2)
    - [6.3 Java hasCode and equals method](#6.3)

---

<h3 id="1">1. Java思想</h3>

Java是一个面向对象的编程语言.面向对象是一种看待世界的世界观,这种世界观通过抽象(类和对象的形式)来对真实的世界进行建模.一个Java应用程序是一系列对象的合集,一个对象能够传递消息,接受消息和处理数据,对象之间通过传递消息的方式来请求服务.面向对象的编程范式旨在提高程序的灵活性和可扩展性.

<h4 id="1.1">1.1 面向对象 VS 面向过程</h4>

面向对象的程序是由模块组成,模块可以进行分别开发和测试,然后整合到整个应用或者从整个应用中去除.

面向过程的编程方式也被称为`自上而下`的设计.当你设计自己模型时,首先会有一个主要的问题,这个问题可能以要有一些子问题来解决,直到某个子问题可能独立的完成任务为止.这种编程模式的主要缺点是软件的维护成本大而且费时.当我们主问题的逻辑发生改变的时候,这种逻辑会传递性的影响到金字塔格局中的各个子问题.

面向过程与面向对象最大的区别：面向过程主要的关注点在`functionality`；面向对象的主要关注点在`data`.

**面向过程示例:**

- 主流电商网站爬虫(php)
    - 模拟http请求,得到HTML页面
    - 解析HTML页面,匹配正则表达式,提取关键目标标签信息
    - 依据关键目标信息,重新向相关页面做http请求,得到完整信息
    - 将完整信息插入数据

- 弊端
    - 各电商反爬虫策略不同,比如说cookie验证,验证码校验等
    - 电商页面标签和分页规则不尽一致,匹配的正则表达式要相应的改变
    - 有些关键信息动态生成等,还需要动态请求js信息

- 应用面向对象
    - 将HttpRequest抽象为一个类
        - 针对电商反爬虫策略,有不同的实现,如CookieHttpRequest,PeriodHttpRequest
    - 将ParsePage抽象为一个类,封装正则规则和Parse策略
        - 如SimpleLoopParse,WithJsPase等

<h4 id="1.2">1.2 抽象(Abstraction),封装(Encapsulation),协作(Cohesion)</h4>

面向对象语言的三要素:

- 封装
- 继承
- 多态

这是语言级别的.更广泛一点,可以理解为:

- 抽象
    - 保留事物的本质特征，去除事物的差异化细节，有`generalization`的概念
    - 对外提供`API`接口
- 封装
    - 将数据和对数据的操作绑定在一个实体当中
    - 控制外部对数据的访问权限
- 复用

对应到现实世界,可以理解为:

- 角色
    - 抽象(Abstraction): 是一个一般化(generalization)的过程.代表了事物的必要信息,而没有具体的实现细节.抽象是一种实现的隐藏(Implementation hiding).
- 职责
    - 封装(Encapsulation): 将事物的状态和对状态的操作整合为一个对象,并且严格限制外部对对象某些特征的访问.也就是说,将事物的内在表示从外部世界对它的定义中隐藏起来.封装是一种信息的隐藏(Informatica hiding).
- 协作
    - 复用: 角色间有继承,关联和依赖等关系,相互间约束和交流,有序的进行
    - 抽象和封装是多多态的基石,多态(Polymorphism)是复用的一种

---

<h3 id="2">2. Java类集框架</h3>

**Java类集框架UML类图**

![Java类集框架UML类图](/img/posts/java-collections-framework.png)

---

<h3 id="3">3. Java IO流体系</h3>

I/O分别表示输入源(InputStream)和输出头(OutputStream),I/O可以表示各个不同类型的源和头,包括磁盘文件,设备,其他应用程序和数组等.

I/O流可以支持多种类型的数据: 字节(byte),基本数据类型,本地字符和对象.一些流仅仅使用来传递数据,而一些是操控并传输这些数据.

不管流的内部事怎么样的实现,`InputStream`从输入源读取数据,`OutputStream`将流输出到输出头中.

<h4 id="3.1">3.1 字节流(Byte Stream)</h4>

字节流用来读取和输出8-bit的字节(byte).所有的字节流都继承自`InputStream`和`OutputStream`.

*使用字节流读并写文件:*

    package org.fmz.io;

    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;

    public class CopyBytes {
        public static void main(String[] args) throws IOException {

            FileInputStream in = null;
            FileOutputStream out = null;

            try {
                in = new FileInputStream("xanadu.txt");
                out = new FileOutputStream("outagain.txt");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }
    }

> 流读取或者输出完毕,不用时,一定要记得关闭流(finally);当文件不存在或者无法打开文件时,流仍为`null`,所以在关闭流前要做一个非空判断.<br><br>
字节流代表了一种比较底层的I/O流,像上述读取字符文件,我们最好不用字节流,而是用字符流.

在实际的应用中，使用上述代码来进行文件的copy效率非常低下，通常我们会定义一个缓冲区，当缓冲区满了再一次性写文件:

    package org.fmz.io;

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];//缓冲区
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            
            }
        
        } finally {
            is.close();
            os.close();
        
        }

    }

在nio中我们也可以使用`FileChannel`来实现文件的copy：

    package com.fmz.io;

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
           
        }finally{
               sourceChannel.close();
               destChannel.close();
           
        }

    }

> Apache的common-io包中的`FileUtils#copyFile`也是使用了`FileChannel`的方法实现的文件的copy。

<h4 id="3.2">3.2 字符流(Character Stream)</h4>

Java语言的默认编码方式是:`UTF-16`.字符流自动将本地字符进行内部转化或者由内部转为本地字符.所有的字符流都继承自`Reader`和`Writter`

*使用字符流读并写文件:*

    package org.fmz.io;

    import java.io.FileReader;
    import java.io.FileWriter;
    import java.io.IOException;

    public class CopyCharacters {
        public static void main(String[] args) throws IOException {

            FileReader inputStream = null;
            FileWriter outputStream = null;

            try {
                inputStream = new FileReader("xanadu.txt");
                outputStream = new FileWriter("characteroutput.txt");

                int c;
                while ((c = inputStream.read()) != -1) {
                    outputStream.write(c);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }

字符用常常是字节流的封装,将字节流转化为字符用常用的方式是:`InputStreamReader`和`OutputStreamWriter`.

> 当使用`FileReader`或者`FileWriter`时，其实现使用平台默认的编码方式(Windows:GBK; Linux:UTF-8)，这不是一个好的实现。如果需要用指定的编码格式，可以使用`new InputStream(new FileInputStreamReader(pathToFile, <encoding>))`

如果一个Windows的默认GBK文件copy到Linux系统上进行文件的复制，就会出现问题：

<h4 id="3.3">3.3 Buffered Stream</h4>

字节流和字符流都是非Buffered Stream,也就是说每一次读和写的操作都直接由底层操作系统实现,这样可以使得程序的效率很低,因为操作系统的每一读和写都要调动磁盘网络和其他一些花销很大的操作.

为了解决这种问题,Java实现了buffered I/O流.Buffered输入流从一块内存(buffer)中读取数据,当buffer为空时,调用本地input API;Buffered输出流往一块内存(buffer)中写数据,当buffer满时,调动本地的output API.

*使用Buffered Stream读并写文件:*

    import java.io.FileReader;
    import java.io.FileWriter;
    import java.io.BufferedReader;
    import java.io.PrintWriter;
    import java.io.IOException;

    public class CopyLines {
        public static void main(String[] args) throws IOException {

            BufferedReader inputStream = null;
            PrintWriter outputStream = null;

            try {
                inputStream = new BufferedReader(new FileReader("xanadu.txt"));
                outputStream = new PrintWriter(new FileWriter("characteroutput.txt"));

                String l;
                while ((l = inputStream.readLine()) != null) {
                    outputStream.println(l);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }

除了这三种流之外,Java还提供了DataStream和ObjectStream,分别用来处理基本数据类型和序列化操作.

<h4 id="3.4">3.4 Java NIO</h4>

**参考：**

- [I/O那些事](https://fengmengzhao.github.io/2018/07/25/what-I-am-suffering-from-io.html "I/O那些事")

---

<h3 id="4">4. Java多线程</h3>

<h4 id="4.1">4.1 进程 VS 线程</h4>

**进程:**

一个进程有独立的运行环境.通常情况下,一个进程具有完全私有的基本运行时资源.尤其是,每一个进程有自己独立的内存.

进程也通常被称为程序或应用的代名词.然而,人们普遍认为的进程实际上是相互合作的进程集.操作系统通过管道和套接字等IPC(Inter Process Communication)资源实现同一个系统或不同系统间的交流协作.

**线程:**

线程被称为轻量级的进程.线程和进程都提供程序运行环境,相对于进程,但是创建一个新的线程需要更少的资源.

线程依附于进程存在:每一个进程都至少有一个线程.线程分享进程的资源,包括内存和打开文件.这使得共享更加有效,同时也带来潜在的问题.

Java支持多线程环境是必要的.如果你认为内存管理和信号处理是一个线程,每一个Java应用都至少有一个或者多个线程.但是从开发者的角度,你仅仅从一个线程开始,这个线程是主线程.这个主线程有能力创建其他线程.

<h4 id="4.2">4.2 线程对象</h4>

使用Thread Object的两种方式:

- 手动创建线程对象
    - extends Thread 
    - new Thread(new Runnable)
- 将线程的管理抽象,将应用的tasks创递给一个executor

Thread主要方法:

- java.lang.Thread
    - class method
        - Thread.currentThread()
        - Thread.sleep()
        - Thread.interrupted(): 当前线程是否被interrupted
        - Threa.yield(): 当前线程暗示可以交出CPU
    - instance method
        - new Thread().isInterrupted(): this线程是否被interrupted
        - new Thread().interrupt(): interrupt this线程
        - new Thread().join: 当前线程等待this线程,直到this线程结束

**join()方法示例:**

    package org.fmz.thread;
    
    public class ThreadJoin{
        private static class PrintInteger implements Runnable{
            
            @Override
            public void run(){
                for(int i = 0; i < 5; i++){
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " --> " + (i+1));
                }
            }
        }

        public static void main(String args[]){

            Thread t1 = new Thread(new PrintInteger());
            Thread t2 = new Thread(new PrintInteger());
            Thread t3 = new Thread(new PrintInteger());
            t1.start();
            try{
                t1.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            t2.start();
            t3.start();
        }
    }

**使用join让多线程顺序执行:**

    package org.fmz.thread;

    public class ThreadSequence{
        private static class PrintInteger implements Runnable{
            
            @Override
            public void run(){
                for(int i = 0; i < 5; i++){
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " --> " + (i+1));
                }
            }
        }

        public static void main(String args[]){

            Thread t1 = new Thread(new PrintInteger());
            Thread t2 = new Thread(new PrintInteger());
            Thread t3 = new Thread(new PrintInteger());
            t1.start();
            try{
                t1.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            t2.start();
            try{
                t2.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            t3.start();
        }
    }

**join让一个线程等待过个线程执行完成后执行**

    package com.fmz.thread;

    public class ThreadJoinWaitOneThread {

        public static void main(String args[]){
            Thread[] threads = new Thread[4];
            for(int i = 0; i < 4; i++){
                threads[i] = new Thread(new Runnable(){
                    
                    @Override
                    public void run(){
                        System.out.println(Thread.currentThread().getName() + "will sleep for:: 3 secodes");
                        try{
                            Thread.sleep(3000L);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }, "thead::" + i);
                threads[i].start();
                // 如果在这里让线程等待，则会变为线程顺序执行
                /*
                try{
                    threads[i].join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                */
            }

            /*
                这个是统计线程，等待
                0 1 2 3线程执行完毕后执行
            */
            new Thread(new Runnable(){
                @Override
                public void run(){
                    for(Thread thread : threads){
                        try{
                            thread.join();
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    System.out.println("0 1 2 3 线程执行完毕后执行统计线程!");
                    //执行一些统计工作
                    // compute()
                }
            }, "统计线程").start();

            //main不会受到统计线程等待的影响，正常执行
            System.out.println("这是main线程在运行！");
        }
    }

> 统计线程等待0 1 2 3线程完成后执行统计工作,main不受此等待的影响,正常执行。

**线程的状态**

先上一个图：

![java-thread-state](/img/posts/java-thread-state.jpg)

Java的线程状态一共可以分为6种。

- 初始态(`Thread.State.NEW`)
    - 创建一个Thread对象，还未调用`start()`方法时，线程处于初始态
- 运行态(`Thread.State.RUNNABLE`)
    - 就绪态
        - 线程已经获取执行所需要的资源，只需要CPU分配执行权就能执行
        - 就绪线程存放在就绪队列中
    - 运行态
        - 获得CPU执行权，正在执行
        - 由于一个CPU同时只能执行一个线程，因此每个CPU每时每刻只有一条执行线程
- 阻塞态(`Thread.State.BLOCKED`)
    - 当一个线程请求资源失败时，就会进入阻塞态
    - 在Java中阻塞态专指请求锁失败进入的状态
    - 阻塞队列存放说有的阻塞态进程
    - 处于阻塞态的进程会不断请求资源，一旦请求成功，就会进入就绪队列，等待执行
    - PS: 锁、IO、Socket等都是资源
- 等待态(`Thread.State.WAITING`)
    - 当前线程中调用`wait()、join()`等函数时，当前线程会进入等待态
    - 线程进入等待态表示它需要等待其他线程的指示才能继续运行
    - 和阻塞态的区别是，等待态不是因为请求不到资源，而是主动进入的，并且进入后需要其他线程唤醒
    - 进入等待态的线程会释放CPU执行权和资源(如：锁)
- 超时等待态(`Thread.State.TIMED_WAITING`)
    - 当运行中线程调用`sleep(sleeptime)、wait(timeout)、join(timeout)`等函数，线程就会进入超时等待状态
    - 进入该状态后释放CPU和占有的资源
    - 与等待态区别：到了超时时间后自动进入阻塞队列，开始竞争锁
- 终止态(`Thread.State.TERMINATED`)
    - 线程执行结束后的状态

> - wait()方法会释放CPU执行权和占有的锁
- sleep(long)方法仅释放CPU使用权，锁仍然占用；线程被放入超时等待队列，与yield相比，它会使线程较长时间得不到运行。
- yield()方法仅释放CPU执行权，锁仍然占用，线程会被放入就绪队列，会在短时间内再次执行。
- wait和notify必须配套使用，即必须使用同一把锁调用；wait和notify必须放在一个同步块中调用wait和notify的对象必须是他们所处同步块的锁对象。<br><br>
线程阻塞态和线程等待态的区别：阻塞态是线程请求不到资源时被动(被JVM)进入的一种状态，进入阻塞态的线程不再消耗CPU(具体阻塞态是怎么实现的可能有不同的方式，可以理解为：1. 资源获取完成后，通知阻塞线程，但是这里的通知和等待态的通知(`Notify`)是不同的；2. 线程会隔一段时间来尝试获取资源，如果有资源到达，则获取成功，否则继续隔一段时间尝试获取)；等待态是资源对象显示调用`Object#wait()`方法时，当前对象对应的线程放弃锁资源，并且进入的一种状态，这种状态只有当同一个资源对象调用`Object#notify()`方法时，等待该资源对象锁的线程会被唤醒，进入阻塞态。

<h4 id="4.3">4.3 同步(Synchronization)</h4>

线程间的交流主要是通过共享域和引用对象的访问完成.这种方式的交流非常有效,同时也会带来两种方面的问题:**线程干扰(thread interference)**和**内存一致性(memory consistency errors)**.要解决这些问题,就需要用到同步.

当两个或者多个线程同时访问相同的资源的时候,同步会引起线程竞争(thread contention),这样会导致JVM执行线程速度变慢,甚至挂起运行.饿死(Stavation)和活锁(livelock)是两种常见的线程竞争的形式.

Java提供了两种基本的同步用法:

- synchronize method
- synchronize statement

Synchronized同步方法能够阻止线程干扰和内存一致性的问题.

**内置锁(intrinsic lock)**

每一个对象都有一个内置锁与之相关联.通常,一个线程如果想长期独占对一个对象的访问,则这个线程必须首先获得这个对象的内置锁,并且在结束的时候释放锁.从线程获取一个对象的锁开始到释放这个锁的过程叫做:这个对象拥有这个锁.只要一个线程拥有一个对象的内置锁,其他线程没有办法获得该对象的内置锁,而试图获取该对象内置锁的线程就会处于`Thread.State.BLOCKED`状态.

> Java的内置锁实际上是Java对象数据的一部分，大部分的对象在其生命周期中可能都不用用到锁，所以根据实现的不同，Java对象锁data在线程第一次要获取锁时生成(或者用其他数据结构能够查找到)，一个线程获取一个对象的锁之后，其他线程如果同样要获取这个对象的锁，就会被JVM挂起线程，线程处于`Threae.State.BLOCKED`状态，直到这个对象的锁释放，处于`BLOCKED`状态的线程才可能获取锁，访问对象的同步数据。<br><br>
和Java内置锁时Java数据一部分一样，Java同样有相关联的另一部分数据`wait set`，当一个对象调用`Object#wait()`方法时，该对象相关联的`wait set`能够被找到，当前线程主动挂起和释放锁，并且会被加入到`wait set`中，当同一个对象调用`Object#notify()|notifyAll()`方法时，`wait set`中的线程会被随意的选择一个，进入`Thread.State.BLOCKED`状态，如果该线程能够获取到锁，就能够进入`Thread.State.RUNNABLE`状态。

**原子访问(Atomic Access)**

在程序中,原子操作一次性执行完成: 原子操作不会中途停止,要么完全执行,要么没有执行.

常见原子操作:

- 对引用变量及大部分基本数据类型(long,double除外)的读和写操作是原子性的
- 对所有`volatile`修饰的所有变量(包括long,double)的读和写操作是原子性的

<h4 id="4.4">4.4 死锁(deadlock)</h4>

**死锁示例:**

    package org.fmz.thread;

    public class DeadLock{

        static class Friend{
            private String name;

            public Friend(String name){
                this.name = name;
            }

            public String getName(){
                return name;
            }
            
            public synchronized void bow(Friend another){
                System.out.println(name + "向" + another.getName() + "鞠躬.");
                another.bowBack(this);
            }

            public synchronized void bowBack(Friend another){
                System.out.println(name + "向" + another.getName() + "回礼.");
            }
        }

        public static void main(String args[]){
            final Friend f1 = new Friend("张三");
            final Friend f2 = new Friend("李四");
            new Thread(new Runnable(){
                public void run(){
                    f1.bow(f2);
                }
            }).start();
            new Thread(new Runnable(){
                public void run(){
                    f2.bow(f1);
                }
            }).start();
        }
    }

> 张三和李四是懂礼貌的好朋友,当他们碰到对方时要向对方鞠躬并且直到对方做出回应方可起身.可是,当他们同时向对方鞠躬的时候,死锁就发生了.

> 要尽量避免在同步块中执行执行其他对象的方法,以为这样容易引起Liveness问题.

- Liveness
    - 饿死(Starvation): 当一个线程不能够获得对共享数据的访问,并且不能有所进展的情形.
    - 活锁(Livelock): 当一个线程回应另一个线程的同时,另一个线程同时回应前一个线程.

> 活锁和死锁一样,程序不能够继续执行,但不同的是活锁中的线程处于繁忙状态,并没有处于block状态.

<h4 id="4.5">4.5 Guarded Block</h4>

线程之间需要协作对方的行为.最常用的一种协作方式是`Guarded Block`,这个block循环一个条件,当这个条件为`true`时,block才能继续执行.

    package org.fmz.thread;

    public void guardedJoy() {
        // Simple loop guard. Wastes
        // processor time. Don't do this!
        while(!joy) {}
        System.out.println("Joy has been achieved!");
    }

这样的代码虽然满足了`Guarded Block`的要求,但是浪费资源.在`while`循环中,线程在等待时还一直处于工作状态(消耗CPU).

通常我们用同步中`wait`和`notify`解决上述问题:

    package org.fmz.thread;

    public synchronized void guardedJoy() {
        // This guard only loops once for each special event, which may not
        // be the event we're waiting for.
        while(!joy) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        System.out.println("Joy and efficiency have been achieved!");
    }

> 为什么`wait`和`notify`是`java.lang.Object`中的方法,而不放入`java.lang.Thread`中?<br><br>
这是因为: 不同的线程是通过获取对象的内置锁的方式来进行独占访问的;对象在线程间进行共享,线程彼此之间并不知道对方的信息,通过对象锁状态的改变可以让线程等待和通知竞争线程,而一个线程无法通知另一个线程.<br><br>
举例来说:火车上的人上厕所,当人发现指示灯是绿色的时候会尝试开门进入厕所,而指示灯是红色的时候,会坐回原位置等待.这里的人相当于线程,厕所相当于对象,指示灯相当于对象的内置锁.指示灯颜色的转变是通过厕所(对象)门反锁与否来完成的,是对象发出的行为,而不是线程(人)发出的行为.

**Wait & Notify实现Producer-Consumer Model:**

*示例一:同一个对象中有两个同步分发,该对象锁实现wait-notify*

    package org.fmz.thread;

    import java.util.Vector;

    public class Producer extends Thread{

        static final int MAXQUEUE = 5;
        private Vector<String> messages = new Vector<String>();

        @Override
        public void run(){
            try{
                while(true){
                    putMessage();
                }
            }catch(InterruptedException e){
               e.printStackTrace();
            }
        }

        private synchronized void putMessage() throws InterruptedException{
            while(messages.size() == MAXQUEUE){
                wait();
            }
            messages.addElement(new java.util.Date().toString());
            System.out.println( Thread.currentThread().getName() + " --> put messages");
            notify();
        }

        public synchronized String getMessage() throws InterruptedException{
            notify();
            while(messages.size() == 0){
                wait();
            }
            String message = messages.firstElement();
            messages.removeElement(message);
            return message;
        }

    }

> 生产者线程类,有产生消息的方法,也有消费消息的方法.生产和消费消息的方法都是同步的.生产者类线程负责生产消息.

    package org.fmz.thread;

    public class Consumer extends Thread{
        Producer producer;

        public Consumer(Producer p){
            producer = p;
        }

        @Override
        public void run(){
            try{
                while(true){
                    String message = producer.getMessage();
                    System.out.println(Thread.currentThread().getName() + " --> Got message: " + message);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        

        public static void main(String args[]) throws InterruptedException{
            Producer producer =  new Producer();
            producer.start();
            new Consumer(producer).start();
        }
    }

> 消费者类,持有一个生产者对象,消费者线程负责消费消息.二者能够完成消息的自动生产和消费.

**日常生活示例：排队上厕所**

    package org.fmz.thread;

    public class Toilet {

        private boolean available = true;

        public void setOccupied() {
            available = false;
            System.out.println("Warning: The toilet has been occupied by " + Thread.currentThread().getName() + "!");
        
        }

        public void setAvailable() {
            available = true;
            System.out.println("Warning: The toilet has been available by " + Thread.currentThread().getName() + "!");
        
        }

        public boolean getAvaiable(){
            return available;
        
        }

        public synchronized void getInToilet() {
            while(!available){
                System.out.println(Thread.currentThread().getName() + " --> is waiting for the toilet...");
                try{
                    wait();
                
                }catch(InterruptedException e){
                    e.printStackTrace();
                
                }
            
            }
            System.out.println(Thread.currentThread().getName() + "--> the toilet will be closed!");
            setOccupied();
            System.out.println(Thread.currentThread().getName() + "--> the toilet has been closed and will last for 3 seconds!");
            try{
                Thread.sleep(3000);
            
            }catch(InterruptedException e){
                e.printStackTrace();
            
            }

            //notify();
        
        }

        public synchronized void getOutToilet() {
            notify();
            System.out.println(Thread.currentThread().getName() + "--> the toilet will be opened now!");
            setAvailable();
        
        }

    }

> 厕所对象。厕所对象是共享资源类，有`synchronized`方法，负责指示共享资源的状态(厕所是否有人)。

    package org.fmz.thread;

    public class IntendToilet extends Thread {

        private Toilet toilet;

        public  IntendToilet(Toilet t, String threadName){
            super(threadName);
            toilet = t;  
        
        }
        
        @Override
            public void run(){
            toilet.getInToilet();
            toilet.getOutToilet();
        
            }

        public static void main(String args[]){
            Toilet toilet = new Toilet();
            for(int i = 0; i < 10; i++){
                new IntendToilet(toilet, "Person--" + i).start();
            
            }
        
        }

    }

> 消费线程。每一个线程先进厕所，然后再出厕所。

*示例二:生产者和消费者持有Message对象,Message对象有生产和消费消息的同步方法,该对象实现wait-notify*

    package org.fmz.thread;

    import java.util.Vector;

    public class Message{

        private Vector<String> messages;
        private int size;

        public Message(Vector<String> msgs, int size){
            messages = msgs;
            this.size = size;
        }

        public synchronized void putMsg(String msg) throws InterruptedException{ 
            while(messages.size() == size){
                System.out.println("Vector is full, " + Thread.currentThread().getName() + " is waiting, size: " + messages.size());
                wait();
            }
            messages.addElement(msg);
            System.out.println(Thread.currentThread().getName() + " --> put message: " + msg + ";vector: " + messages);
            notify();
        }

        public synchronized String getMsg() throws InterruptedException{
            notify();
            while(messages.size() == 0){
                System.out.println("Vector is empty, " + Thread.currentThread().getName() + " is waiting, size: " + messages.size());
                wait();
            }
            String message = messages.firstElement();
            messages.removeElement(message);
            System.out.println(Thread.currentThread().getName() + " --> get message: " + message + ";vector: " + messages);
            return message;
        }
    }

> Message类,有生产和消费消息的同步方法,用次对象完成消息的生成和消费.

    package org.fmz.thread;

    import java.util.Vector;

    public class ProducerMessage extends Thread{
        
        private Message message;

        public ProducerMessage(Message msg){
            message = msg;
        }

       @Override 
       public void run(){
           try{
               for(int i = 0; i < 10; i++){
                   message.putMsg("" + i);
               }
           }catch(InterruptedException e){
               e.printStackTrace();
           }
       }
    }

> 生产者线程,负责生产消息.

    package org.fmz.thread;    

    import java.util.Vector;

    public class ConsumerMessage extends Thread{
        
        private Message message;

        public ConsumerMessage(Message msg){
            message = msg;
        
        }

       @Override 
           public void run(){
               try{
                   for(int i = 0; i < 10; i++){
                   String msg = message.getMsg();
               
                   }
           
               }catch(InterruptedException e){
               e.printStackTrace();
           
               }
       
           }

    }

> 消费者线程类,负责消费消息.

    package org.fmz.thread;

    import java.util.Vector;

    public class ProducerConsumer{

        public static void main(String args[]){
            Message msg = new Message(new Vector<String>(), 5);
            ProducerMessage pm = new ProducerMessage(msg);
            ConsumerMessage cm = new ConsumerMessage(msg);
            pm.start();
            cm.start();
            ProducerMessage pm2 = new ProducerMessage(msg);
            ConsumerMessage cm2 = new ConsumerMessage(msg);
            pm2.start();
            cm2.start();
        }
    }

> main()方法类

> 举一个生活中的例子来理解，生产者消费者模型和`wait()`和`notify()`的概念：<br><br>
师傅在平底锅上做烤冷面，平底锅只能容下方5个烤冷面；路人不断聚集过来买烤冷面；每当师傅放在平底锅上一个烤冷面或者路人付完款后都会有报时器警告：可支配烤冷面还有多少个<br><br>
在这里<br><br>
师傅是生产者线程，当报时器报的数字是5时，等待(停下来休息下)；其余数字继续做烤冷面<br><br>
路人是消费者线程，当报时器报的数字是0时，等待(没有现成的，等一等吧)；其余数字可以付款<br><br>
烤冷面是生产者-消费者协作的内容，报时器是`wait()`和`notify()`的信号<br><br>
为什么烤冷面的操作:1,师傅做烤冷面(putMsg())和2,付款(getMsg())要是线程安全的？<br><br>
设想一个，这时候刚好报时器报告警告只剩下1个烤冷面，如果路人甲由于网络问题付款没有成功，(如果方法没有同步)这时候路人乙也开始付款了，而路人乙成功了，这时候路人甲的网络好了，完成了付款，但是烤冷面已经没了(假设刚好是最后一个)

*示例三:对集合进行同步,完成wait-notify*

    package org.fmz.thread;

    import java.util.Vector;

    public class ProducerVector extends Thread{
        
        private Vector<String> messages;
        private int size;

        public ProducerVector(Vector<String> messages, int size){
            this.messages = messages;
            this.size = size;
        }

       @Override 
       public void run(){
           try{
               for(int i = 0; i < 10; i++){
                   String msg = "" + i;
                   putMsg(msg);
               }
           }catch(InterruptedException e){
               e.printStackTrace();
           }
       }

       private void putMsg(String msg) throws InterruptedException{
           while(messages.size() == size){
               synchronized(messages){
                   System.out.println("Vector is full, " + Thread.currentThread().getName() + " is waiting, size = " + messages.size());
                   messages.wait();
               }
           }
           synchronized(messages){
               messages.addElement(msg);
               System.out.println(Thread.currentThread().getName() + " --> put message: " + msg + ", vector: " + messages);
               messages.notify();
           }
       }

    }

> 生产者类,对Vector进行同步.

    package org.fmz.thread;

    import java.util.Vector;

    public class ConsumerVector extends Thread{
        
        private Vector<String> messages;

        public ConsumerVector(Vector<String> messages){
            this.messages = messages;
        }

       @Override 
       public void run(){
           try{
               for(int i = 0; i < 10; i++){
                    getMsg();
               }
           }catch(InterruptedException e){
               e.printStackTrace();
           }
       }

       private String getMsg() throws InterruptedException{
           while(messages.size() == 0){
               synchronized(messages){
                   System.out.println("Vector is empty, " + Thread.currentThread().getName() + " is waiting, size = " + messages.size());
                   messages.wait();
               }
           }
           synchronized(messages){
               String msg = messages.firstElement();
               messages.removeElement(msg);
               System.out.println(Thread.currentThread().getName() + " --> consume message: " + msg + ", vector: " + messages);
               messages.notify();
               return msg;
           }
       }

    }

> 消费者类,对Vector进行同步.

    package org.fmz.thread;

    import java.util.Vector;

    public class ProducerConsumerSolution{

        public static void main(String args[]){
            Vector<String> v = new Vector<String>();
            ProducerVector pv = new ProducerVector(v, 5);
            ConsumerVector cv = new ConsumerVector(v);
            pv.start();
            cv.start();
        }
    }

> main()方法类


<h4 id="4.6">4.6 不可更改(Immutable)对象</h4>

如果一个对象创建之后,它的状态(state)就不能别修改,我们称之为`Immutable Object`.在并发的应用中,Immutable对象显得尤为重要.

*示例: 对Mutable对象同步*

    package org.fmz.thread;

    public class SynchronizedRGB {

        // Values must be between 0 and 255.
        private int red;
        private int green;
        private int blue;
        private String name;

        private void check(int red,
                           int green,
                           int blue) {
            if (red < 0 || red > 255
                || green < 0 || green > 255
                || blue < 0 || blue > 255) {
                throw new IllegalArgumentException();
            }
        }

        public SynchronizedRGB(int red,
                               int green,
                               int blue,
                               String name) {
            check(red, green, blue);
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.name = name;
        }

        public void set(int red,
                        int green,
                        int blue,
                        String name) {
            check(red, green, blue);
            synchronized (this) {
                this.red = red;
                this.green = green;
                this.blue = blue;
                this.name = name;
            }
        }

        public synchronized int getRGB() {
            return ((red << 16) | (green << 8) | blue);
        }

        public synchronized String getName() {
            return name;
        }

        public synchronized void invert() {
            red = 255 - red;
            green = 255 - green;
            blue = 255 - blue;
            name = "Inverse of " + name;
        }
    }

当我们使用的时候,就要用到下面的同步方法:

    package org.fmz.thread;

    synchronized (color) {
        int myColorInt = color.getRGB();
        String myColorName = color.getName();
    } 

我们可以试图设计一个Immutable类,避免上述繁琐的同步操作:

    package org.fmz.thread;

    final public class ImmutableRGB {

        // Values must be between 0 and 255.
        final private int red;
        final private int green;
        final private int blue;
        final private String name;

        private void check(int red,
                           int green,
                           int blue) {
            if (red < 0 || red > 255
                || green < 0 || green > 255
                || blue < 0 || blue > 255) {
                throw new IllegalArgumentException();
            }
        }

        public ImmutableRGB(int red,
                            int green,
                            int blue,
                            String name) {
            check(red, green, blue);
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.name = name;
        }


        public int getRGB() {
            return ((red << 16) | (green << 8) | blue);
        }

        public String getName() {
            return name;
        }

        public ImmutableRGB invert() {
            return new ImmutableRGB(255 - red,
                           255 - green,
                           255 - blue,
                           "Inverse of " + name);
        }
    }

- 类被定义为`final`
- 所有的字段定义为`final`
- 删除了`set`方法,不能有改变对象的状态
- 如果要改变对象状态,除非新生成一个对象

> 这样,再使用这个对象的时候,就不用一些同步的操作了.

<h4 id="4.7">4.7 并发进阶</h4>

**Lock Object**

`Synchronized`程序依赖Java对象的内置锁,这种锁是一种`reentrant lock`,这种锁机制很容易使用,但是也有自身的局限性.`java.util.concurrent.locks`包中提供了功能更加复杂的锁机制.

`java.util.concurrent.locks.Lock`和内置锁非常相似。与内置锁一样，一个线程一次只能拥有一个`Lock`对象，通过与之相关联的`Condition`对象，`Lock`对象也支持`wait/notify`机制。

`Lock`对象与内置锁相比较，最大的优点是：它能够退出正在尝试获取的锁。`tryLock()`方法当锁对象不可用是立即退出，或者在尝试获取指定时间后退出(如果设置的尝试获取时间)；`lockInterruptibly()`方法在某线程发送中断信号时退出正在尝试获取锁。

示例：用`Lock`对象尝试解决死锁的问题：

    package org.fmz.thread;

    import java.util.concurrent.locks.Lock;
    import java.util.concurrent.locks.ReentrantLock;
    import java.util.Random;

    public class Safelock {
        static class Friend {
            private final String name;
            private final Lock lock = new ReentrantLock();

            public Friend(String name) {
                this.name = name;
            
            }

            public String getName() {
                return this.name;
            
            }

            public boolean impendingBow(Friend bower) {
                Boolean myLock = false;
                Boolean yourLock = false;
                try {
                    myLock = lock.tryLock();
                    yourLock = bower.lock.tryLock();
                
                } finally {
                    if (! (myLock && yourLock)) {
                        if (myLock) {
                            lock.unlock();
                        
                        }
                        if (yourLock) {
                            bower.lock.unlock();
                        
                        }
                    
                    }
                
                }
                return myLock && yourLock;
            
            }
                
            public void bow(Friend bower) {
                if (impendingBow(bower)) {
                    try {
                        System.out.format("%s: %s has"
                            + " bowed to me!%n", 
                            this.name, bower.getName());
                        bower.bowBack(this);
                    
                    } finally {
                        lock.unlock();
                        bower.lock.unlock();
                    
                    }
                
                } else {
                    System.out.format("%s: %s started"
                        + " to bow to me, but saw that"
                        + " I was already bowing to"
                        + " him.%n",
                        this.name, bower.getName());
                
                }
            
            }

            public void bowBack(Friend bower) {
                System.out.format("%s: %s has" +
                    " bowed back to me!%n",
                    this.name, bower.getName());
            
            }
        
        }

        static class BowLoop implements Runnable {
            private Friend bower;
            private Friend bowee;

            public BowLoop(Friend bower, Friend bowee) {
                this.bower = bower;
                this.bowee = bowee;
            
            }
        
            public void run() {
                Random random = new Random();
                for (;;) {
                    try {
                        Thread.sleep(random.nextInt(10));
                    
                    } catch (InterruptedException e) {}
                    bowee.bow(bower);
                
                }
            
            }
        
        }
                

        public static void main(String[] args) {
            final Friend alphonse =
                new Friend("Alphonse");
            final Friend gaston =
                new Friend("Gaston");
            new Thread(new BowLoop(alphonse, gaston)).start();
            new Thread(new BowLoop(gaston, alphonse)).start();
        
        }

    }

**Executors**

当项目的规模变大时,将线程从创建和管理从应用中隔离出来,这时候要用的`Executor`.

*Executor接口:*

- `Executor`: 支持部署任务的简单接口.
    - `executor.execute(new runnable(){...})`
- `ExecutorService`: `Executor`的子接口,增加`Executor`的功能并且帮助管理独立任务和`executor`本身.
    - `Feature submit(new Callable())`
- `ScheduledExecutorService`: `ExecutorService`的子接口,支持预期和周期执行任务.
    - schedule

*线程池:*

- ThreadPool
    - fixed thread pool    
        - `newFixedThreadPool`
        - 有固定的Thread对象组成,一个对象停止了工作,其他对象自动顶替上来
    - cached thread pool(可扩容)
        - `newCachedThreadPool`
    - single thread exceutor
        - `newSingleThreadExecutor`

*并发类集:*

- Concurrent Collections
    - `BlockingQueue`: 定义一个先来后到的数据结构,当试图插入一个满的队列或者从空队列中取数据时阻塞或者timeout
    - `ConcurrentMap`: 插入和删除操作是原子性的.标准的实现是`ConcurrentHashMap`,`ConcurrentHashMap`是`HashMap`的线程安全版本
    - `ConcurrentNavigableMap`: 是`ConcurrentMap`的子接口,支持近似匹配.标准的实现是`ConcurrentSkipListMap`,`ConcurrentSkipListMap`是`TreeMap`的线程安全版本

**原子性变量(Atomic Variables)**

- Atomic Variables
    - `AtomicInteger`
    - `AtomicBoolean`
    - `AtomicIntegerArray`
    - `AtomicLong`
    - `AtomicLongArray`
    - `AtomicMarkableReference`
    - `AtomicReference`
    - `AtomicReferenceArray`
    - `AtomicStampedReference`
    - `...`

<h4 id="4.8">4.8 并发安全策略</h4>

多线程听起来是一个很酷的概念,当你使用的时候,才会让你欢喜让你忧.

并发的出现,主要带来了两个方面的问题:线程的相互干扰和内存的一致性.解决这两个问题的策略称之为并发访问策略.

- 假设1: 线程之间的冲突会发生-悲观锁
    - 通过锁定资源的方式,独占的访问临界区
    - block-based algorithm
    - 优点:
        - 同时解决线程干扰和内存一致性问题
        - 利用lock object,较容易编写正确的并发程序
    - 缺点:
        - bolck queue的管理,调度等消耗CPU
        - 活跃性问题: 死锁,活锁,饿死
- 假设2: 线程之间的冲突不会发生-乐观锁
    - 通过CAS的方式尝试-成功-执行或者尝试-不成功-重新尝试
        - 将临界区数据保存为一个副本,尝试改变这个副本,并验证是否和临界区数据相等(改变临界区数据过程中,是否数据别其他线程修改),如果是,改变临界区数据为副本,如果不是,重新尝试
    - nonblock-based algorithm
    - 优点:
        - 当执行线程delay(网络或者I/O等原因),是其他线程不会受干扰
        - 使用乐观锁有很好的扩展性(避免了锁产生的活跃性问题)
    - 缺点
        - 适用于线程竞争低或者中等的情况,改变副本的程序不易过长
        - 不易编写正确的并发程序

**为什么Java API中提供了Synchronized方式的并发,而atomic包中要用CAS方式实现并发安全控制?**

- concurrent包中并发类集很多依赖于atomic包,atomic包提供的CAS并发机制有很好的扩展性
- 基本数据类型的操作都是相对简单的,适合与CAS方式的并发机制

<h4 id="4.9">4.9 Java并发实践(读取并入库large-size XML文件)</h4>

**参考博文：**[Java多线程解析并入库large-size XML 文件](https://fengmengzhao.github.io/2018/05/09/java-concurrent-parse-and-store-large-size-XML.html)

---

<h2 id="5">5. Java虚拟机(JVM, Java Virtual Machine)</h2>

<h3 id="5.1">5.1 JVM内存模型</h3>

- Java堆
- 方法区
- Java虚拟机栈
- 本地方法栈
- 程序计数器(Program Counter)

**Java虚拟机栈**

Java虚拟机栈是Java方法执行的内存模型，每一个方法在执行的时候都会创建一个栈帧，栈帧当中存放着局部变量表、操作数栈、动态链接、返回地址等。

Java虚拟机栈在编译的时候就确定了内存的大小。如果线程请求的栈深度大于虚拟机所能够提供的深度，将会抛出`StackOverflowError`；如果虚拟机在动态扩展栈时无法申请到足够的内存空间将会抛出`OutofMemoryError`。

局部变量表中存放的是方法参数和方法内部定义的局部变量、各种基本的数据类型、对象引用(reference)和ReturnAddress类型。

操作数栈用于保存中间变量。

动态链接指向方法区中的运行时常量池。

**堆**

Java堆是用来存放Java对象和数组的地方，又叫做"gc堆"，其内存物理上可以不连续，逻辑上必须连续。

**方法区**

方法区用来储存：已经被虚拟机加载的类信息、常量、静态变量、JIT编译后的代码等。

方法区又称为永久代，是堆的一个逻辑部分，运行时的常量池也是方法区的一部分。

Class文件除了类的版本、字段、方法、借口等描述信息之外，还有一项是Class文件常量池，这部分在加载之后将放在方法区的运行时常量池中。运行时的常量池相对于Class文件常量池的另一个特点就是动态性。运行期间可以将新的常量放入池中，较多用的是String类的`inter()`方法。

**详细内容请参考：《深入理解Java虚拟机》**

<h3 id="5.2">Java垃圾回收</h3>

**概念区别**

* 强引用(Object o = new Object())，只要强引用还存在GC就永远不会回收它。
* 软引用，可能还有用，但并非必须的对象，第一次gc时进行标记，第二次gc时进行回收。
* 弱引用，强度比软引用更弱一些，引用关联的对象只能生存到下一次gc回收之前。
* 虚引用，最弱的一种引用关系，用处是希望在这个对象被收集器回收时得到一个系统通知。

**垃圾对象的判定**

* 引用计数法：每多一个引用，引用计数器加1，引用失效，计数器减1，计数为0时，回收。
* 跟搜索法：定义GC-Root，从GC Root开始向下搜索，当搜索不可达时，证明此对象不可用。GC-Root包括栈帧中的本地变量表引用对象、方法区静态属性引用变量、方法区常量引用变量和本地方法栈中Native引用的对象。

**垃圾回收算法**

* 标记-清除算法
* 标记-整理算法
* 复制算法
* 分代收集算法(新生代，老生代)

**详细内容请参考：《深入理解Java虚拟机》**

<h3 id="5.3">5.3 Java类加载机制</h3>

Java的类加载大体上按照**加载 --> 验证 --> 准备 --> 解析 --> 初始化**的顺序进行的。

其中，加载、验证、准备和初始化时严格按照顺序开始的，解析可能发生在初始化之后(目的是为了支持Java运行时绑定)，也可能发生在初始化之后。虽然上述阶段是按照严格顺序开始的，但不是按照顺序进行或者完成的，通常是互相交叉混合进行的。

Java当中的绑定指的是，将一个方法的调用与一个方法的主题关联起来，分为静态绑定和动态绑定。静态绑定是compile-time绑定，也叫前期绑定。Java当中只有final、static、private和constructor方法是前期绑定的；动态绑定是run-time绑定，又叫后期绑定，运行时根据对象的类型进行绑定，几乎所有的方法都是动态绑定。

**加载阶段**

通过一个类的全限定名获取其二进制字节流，将这个字节流代表的静态储存结构转化为方法区运行时数据结构，在java堆中生成一个java.lang.Class对象，最为方法区某些数据的访问入口。

可以使用系统或者自己定义的类加载器完成类的加载，类加载器包括：

* 启动类加载器(BootStrap ClassLoader)，负责加载`jdk/jre/lib/java core API`(java开头的文件)。启动类加载器无法被Java程序直接引用。
* 扩展类加载器(Extension ClassLoader)，负责加载`jdk/jre/lib/ext`目录中(javax开头的文件)。扩展类加载器可以在开发中直接引用。
* 应用类加载器(Application ClassLoader)，负责加载用户类路径ClassPath所包含的类。应用类加载器可以在开发中直接引用。

`自定义加载器 --> 应用程序加载器 --> 扩展类加载器 --> 启动加载器`称之为"双亲委派模型"。后面的加载器是前面加载器的父加载器，他们之间并不是继承关系，而是通过composition来复用父加载器的代码。

加载器的工作流程是：`收到类加载请求 --> 把请求委托给父加载器完成 --> 依次向上 --> 到启动类加载器 --> 父类加载无法加载(找不到相应的class文件) --> 自己完成加载`。

**验证阶段**

验证的目的是确保class文件的字节流包含的信息符合当前虚拟机的要求，而不会危害虚拟机自身的安全，验证的内容：文件格式、元数据、字节码和符号引用。

**准备阶段**

为**类变量**分配内存并设置类变量的初始值，这些内存都将在方法区中分配。这时候的内存分配仅包含类变量(static)，不包含实例变量，实例变量会在对象实例化时随对象分配在堆中，这里的初始值为默认初始值，而不是Java代码中assign的值。

**Note:**

1. 对于基本数据类型对应的类变量(static)和全局变量，不显式assign值，会使用默认值，局部变量不assign值，编译不通过。
2. static final变量和只有final修饰的变量可以在声明的时候显式赋值或者初始化的时候赋值，否则编译不通过。总之，有final修饰的变量，在使用之前必须显式assign，系统不会默认assign default value。
3. 对于引用数据类型、数组或引用对象，没有显式的赋值，系统为其赋默认值null。
4. 数据在初始化时没有赋值，则默认为0。
5. static final变量在准备阶段就会变成常量，在编译阶段将其结果放入调用它的类的常量池当中。

**解析阶段**

将常量池中的符号引用转化为直接引用的过程，解析可能发生在初始化之前或者之后，分为静态解析和动态解析。static变量发生在静态解析阶段。

**初始化**

在这一个阶段真正的执行类中定义的Java程序代码

`加载 --> 验证 --> 准备 --> 解析(没有严格顺序) --> 类初始化 --> 对象实例化`

<h3 id="5.3.1">5.3.1 Tomcat类加载</h3>

Tomcat的类加载和正常Java App的类加载机制(双亲委派模型)不太一样。

看看Tomcat的类加载树图：

![Tomcat类加载树](/img/posts/tomcat-class-loading-tree.png)

正常的类加载顺序是：

- Bootstrap classes of your JVM
    - 加载JDK`lib/rt.jar`和`ext/*.jar`
- `/WEB-INF/classes` of your web application
- `/WEB-INF/lib/*.jar` of your web application
- System class loader classes
    - 加载`$CATALINA_HOME/bin/bootstrap.jar`和`$CATALINA_HOME/bin/tomcat-juli.jar`
- Common class loader classes
    - 加载`$CATALINA_HOME/lib/*.jar`

如果设置了代理`<Loader delegate="true" />`

- Bootstrap classes of your JVM
    - 加载JDK`lib/rt.jar`和`ext/*.jar`
- System class loader classes
    - 加载`$CATALINA_HOME/bin/bootstrap.jar`和`$CATALINA_HOME/bin/tomcat-juli.jar`
- Common class loader classes
    - 加载`$CATALINA_HOME/lib/*.jar`
- `/WEB-INF/classes` of your web application
- `/WEB-INF/lib/*.jar` of your web application

**可以通过java命令启动Tomcat**

> 在`$CATALINA_HOME/bin`路径下进行:

`java -cp ./* org.apache.catalina.startup.Bootstrap start`

---

<h3 id="6">6. Heap dump Analysis</h3>

Java应用程序要求使用大小有限的共享内存空间,这个限制可以在程序启动的时候指定.为了更加方便应用,逻辑又将之分为堆内存(Heap Space)和方法区(Permanent Generation or Mothod Area).

Java共享内存区域的大小可以在JVM启动的时候可以设定,如果不显示的设定参数值,JVM会使用默认值.

- JVM 默认Heap Size查看方法:
- `java -XX:+PrintFlagsFinal -version | findstr HeapSize`
- `java -XX:+PrintFlagsFinal -version | grep HeapSize`
- 常用JVM启动参数:
- `-Xms <heap size> [g|m|k] -Xmx <heap size> [g|m|k]`
- `-XX:PermSize=<per gen size> [g|m|k] -XX:MaxPermSize=<perm gen size> [g|m|k]`
- `-Xmn <young size> [g|m|k]`
- `-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<output file>.hprof`
- `java -XX:+PrintFlagsFinal -version`

当Java应用程序试图在堆上申请更多的空间,但是设定的内存空间不足时就会引起`java.lang.OutOfMemoryError`.

> 物理机的内存空间可能是充足的,但是当堆内存达到上限的时候就会抛出`java.lang.outOfMemoryError`.

正常情况下,当Java应用程序运行需要比堆上限更对的内存的时候,就会出现异常.这个时候,我们需要重新设定JVM的Heap Space limit.同时,由于程序上的一些错误或者复杂的情况下,也可能发生内存溢出的异常:

- 程序的使用量或者数据量出现峰值
- 内存泄漏(Memory Leak)

<h4 id = "6.1">6.1 内存溢出的实例</h4>

**堆内存限制导致内存溢出**

    class OMM{

        static final int size = 2*1024*1024;
        public static void main(String args[]){
            int[] i = new int[size];
        }
    }

> `java OMM -Xmx12m` 会导致内存溢出;`java OMM -Xmx13m`则不会出现内存溢出.

**内存泄漏导致内存溢出**

    import java.util.*;

    public class MemoryLeak{

        static class Key{
            Integer id;

            Key(Integer id){
                this.id = id;
            }

            @Override
            public int hashCode(){
                return id.hashCode();
            }

            /*
            @Override
            public boolean equals(Object o){
                boolean response = false;
                if(o instanceof Key){
                    response = (((Key)o).id).equals(this.id);
                }
                return response;
            }
            */
        }

        public static void main(String args[]){
            Map<Key, String> m = new HashMap<Key, String>();

            while(true){
                for(int i = 0; i < 10000; i++){
                    if(!m.containsKey(new Key(i))){
                        m.put(new Key(i), "Nuumber: " + i);
                    }
                }
            }
        }
    }

- 程序中如果Key类只复写了`hashCode()`方法,而没有`equals()`方法,则程序会无限制的申请Key对象,直到内存溢出.
- 内存泄漏: 一些程序不再使用的对象不能够被JVM GC识别,而无法回收.上述的内存泄漏问题可以通过复写`equal()`方法解决.

<h4 id="6.2">6.2 通过工具进行Heap Dump</h4>

查看并分析Java Heap Space有很多方法:

- `jmap(java Memory Map),jhat(java Heap analysis tool)`
- `jvisualvm`
- `Plumbr`
- `IBM HeapAnalyzer`
- `Eclipse MAT(Memory Analysis Tool)`

**通过Jmap和jhat分析堆内存**

- `jps -lm`: 列出正在运行的Java进程和进程号
    - `jps`工具在jdk1.6的版本中有
- `jmap -dump:format=b,file=<some_file>.bin <java_process_num>`
    - 命令格式可以通过`jmap -help`得到
- `jhat <some_file>.bin`
    - 启动一个本地服务:`http://127.0.0.1:7000`,访问即可

**通过jvisualvm分析堆内存**

- 正在运行的应用
    - `jvisualvm`: 自动监控本地的Java应用
        - 右键java进程,选择Heap Dump可以查看堆内存实时状态
- 执行结束的应用
    - `java <java_app> -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<output file>.hprof`
    - `jvisualvm`: 将上述文件装入,即可查看运行结束的Java应用堆内存

<h4 id="6.3">6.3 Java hasCode and equals method</h4>

- `equals`方法
    - 对于任意的非`null`引用x,`x.equals(x)`应该放回`true`
    - 对于任意的非`null`引用x y,如果`x.equals(y)`放回true,`y.equals(x)`也应该返回`true`
    - 对于任意的非`null`引用x y,如果`x.equals(y)`为`true`,`y.equals(z)`为`true`,则`x.equals(z)`应该返回`true`
    - 多次调用的返回结果应该保持一致性
    - 对于任意非`null`引用x,`x.equals(null)`应该返回false
- `hashCode`方法
    - 在一次的Java运行过程中,一个对象的`hashCode`的返回结果应该保持一致性;多个Java运行实例中一个对象的`hashCode`结果没必要保持一致性.
    - 如果两个对象equals,则这两个对象必须有相等的hashCode
    - 如果两个对象有相等的hashCode,则这两个对象未必equals;也就是说非equals的对象,也可能有相等的hashCode
        - 虽然不equals的对象不要求有不同的hashCode,但如果不同对象对应不同的hashCode,则能够提供性能

**覆写equals方法必须覆写hashCode方法**

    import java.util.*;

    public class Apple{

        private String color;    

        public Apple(String color){
            this.color = color;
        }

        @Override
        public boolean equals(Object obj){
            if(null == obj){
                return false;
            }
            if(! (obj instanceof Apple)){
                return false;
            }
            if(obj == this){
                return true;
            }
            return color.equals(((Apple)obj).color);
        }

        /*
        @Override
        public int hashCode(){
            return color.hashCode();
        }
        */

        public static void main(String args[]){

            Map<Apple, Integer> appleMap = new HashMap<Apple, Integer>();
            appleMap.put(new Apple("red"), 1);
            appleMap.put(new Apple("green"), 2);
            System.out.println(appleMap.get(new Apple("green")));
        }
    }

---
