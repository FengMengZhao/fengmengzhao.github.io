---
layout: post
title: Java面试准备向导
subtitle: 致力于收集、整理和完善Java程序员面试基础知识。成竹于胸，方能决胜千里之外。
background: '/img/posts/java-interview-guid.jpg'
comment: true
---

- [Java语言高级特性](#1)
    - [Java类集](#1.1)
    - [JVM](#1.2)
        - [Java内存模型](#1.2.1)
        - [Java类加载机制](#1.2.2)
        - [GC](#1.2.3)
        - [JVM](#1.2.4)
        - [JVM性能调优](#1.2.5) 
    - [Java多线程](#1.3)
    - [Java IO/NIO](#1.4)
    - [Java泛型](#1.5)
    - [Java语言经典面试题目](#1.6)
        - Java代理机制中的静态代理和动态代理
        - 接口和抽象类的异同，什么时候定义接口，什么时候定义抽象类
- [Java框架](#2)
    - [SSM框架](#2.1)
    - [Spring boot框架](#2.2)
    - [日志框架](#2.3)
- [数据结构&算法](#3)
    - [算法](#3.1)
        - [经典排序算法](#3.1.1)
        - [算法思想](#3.1.2)
            - 分而治之、减而治之
            - 递归
    - [经典数据结构](#3.2)
    - [数据结构&算法项目实战](#3.3)
- [数据库](#4)
    - [基本概念](#4.1)
        - [索引](#4.1.1)
        - [视图](#4.1.2)
        - [函数](#4.1.3)
        - [事务](#4.1.4)
        - [约束](#4.1.5)
        - [触发器](#4.1.6)
        - [存储过程](#4.1.7)
        - [游标](#4.1.8)
    - [SQL查询基础](#4.2)
    - [SQL优化](#4.3)
- [设计模式](#5)
    - [设计模式的原则](#5.1)
    - [23种设计模式详解](#5.2)
        - 设计模式UML图
        - 设计模式要解决什么问题
        - 设计模式的应用场景
        - 设计模式的优缺点
    - [项目中使用过的设计模式](#5.3) 
- [工具](#6)
    - [Maven](#6.1)
- [网络](#7)
    - [网络模型](#7.1)
        - [OSI七层模型](#7.1.1)
        - [TCP/IP四层模型](#7.1.2)
    - [网络应用](#7.2)
        - Redis缓存
        - FTP文件服务器
        - Nginx代理
    - [网络编程](#7.3)

**Java笔试算法题OJ**

[LeetCode OJ 平台](https://leetcode.com/ "LeetCode")

> 根据平台说明，先将easy level的算法题做一做。

---

<h3 id="1.2">1.2 JVM</h3>

<h4 id="1.2.1">1.2.1 Java内存模型</h4>

- Java堆(Java Heap)
- 方法区(Method Area)
- Java虚拟机栈(Java Stack)
- 本地方法栈(Native Stack)
- 程序计数器(PC, Program Counter)

Java虚拟机栈是Java方法执行的内存模型，每一个方法在执行的时候都会创建一个栈帧(Stack Frame)，JVM负责push和pop栈帧到Java虚拟机栈中。

**Stack Frame(栈帧)**

- 局部变量表(Local Variable Array)
    - 保存的内容是：this引用(如果是Instance Method)、方法参数和局部变量
    - 如果是Instance Method，`Index 0`表示`this`应用，紧接着就是参数和方法内声明的局部变量
- 操作数栈(Operand Stack)
    - 方法执行时会把局部变量表中的内容push到操作数栈进行操作，改变局部变量的值
    - 操作数栈用于保存中间变量。
- Frame Data
    - 类运行时常量池引用(Runtime Constant Pool Reference)
        - 通过Runtime Constant Pool Reference可以访问Class Data，用来进行Dynamic Symbolic Resolution
    - Normal Method return
    - Exception Dispatch

Java虚拟机的操作是基于栈进行的，方法执行时，大部分的操作都是在局部变量表和操作数栈之间进行数据的更新。

Java虚拟机栈在编译的时候就确定了内存的大小。如果线程请求的栈深度大于虚拟机所能够提供的深度，将会抛出`StackOverflowError`；如果虚拟机在动态扩展栈时无法申请到足够的内存空间将会抛出`OutofMemoryError`。

局部变量表中存放的是方法参数和方法内部定义的局部变量、各种基本的数据类型、对象引用(reference)和ReturnAddress类型。

##### 堆

Java堆是用来存放Java对象和数组的地方，又叫做"gc堆"，其内存物理上可以不连续，逻辑上必须连续。

##### 方法区

方法区用来储存：已经被虚拟机加载的类信息、常量、静态变量、JIT编译后的代码等。

方法区又称为永久代，是堆的一个逻辑部分，运行时的常量池也是方法区的一部分。

Class文件除了类的版本、字段、方法、接口等描述信息之外，还有一项是Class文件常量池，这部分在加载之后将放在方法区的运行时常量池中。运行时的常量池相对于Class文件常量池的另一个特点就是动态性。运行期间可以将新的常量放入池中，较多用的是String类的`inter()`方法。

**详细内容请参考：《深入理解Java虚拟机》**

<h4 id="1.2.3">1.2.3 GC(Garbage Collector, Java垃圾回收)</h4>

##### 概念区别

- 强引用(Object o = new Object())，只要强引用还存在GC就永远不会回收它。
- 软引用，可能还有用，但并非必须的对象，第一次gc时进行标记，第二次gc时进行回收。
- 弱引用，强度比软引用更弱一些，引用关联的对象只能生存到下一次gc回收之前。
- 虚引用，最弱的一种引用关系，用处是希望在这个对象被收集器回收时得到一个系统通知。

#### 垃圾对象的判定

- 引用计数法：每多一个引用，引用计数器加1，引用失效，计数器减1，计数为0时，回收。
- 根搜索法：定义GC-Root，从GC Root开始向下搜索，当搜索不可达时，证明此对象不可用。GC-Root包括栈帧中的本地变量表引用对象、方法区静态属性引用变量、方法区常量引用变量和本地方法栈中Native引用的对象。

#### 垃圾回收算法

- 标记-清除算法
- 标记-整理算法
- 复制算法
- 分代收集算法(新生代，老生代)

> 一般商用的JVM实现都使用的是分代收集算法：在对象存活率较低的新生代(10%对象存活)使用的是复制算法。在对象存活率高的老生代和方法区(永久代)采用标记清除算法或者标记整理算法。<br><br>
复制算法：Java堆新生代又分为`Eden`，`Survivor0`和`Survivor1`；它们之间的占比大约是`8:1:1`；新生成的对象就放在`Eden`中，当`Eden`满时，就把存活的对象复制到`S0`中，清除`Eden`；当下一次GC回收时(S0也满了时)，把`Eden`和`S0`中的存活对象复制到`S1`中，清空`Eden`和`S0`；当再下一次GC时，`S0`和`S1`的角色就换了，JVM只扫描`Eden`和`S1`中的对象，如此往复进行。实际上`S0`和`S1`同时只有一个配合`Eden`在工作。如果`S0`或者`S1`不足以存放复制过来的对象时，就会放到老生代中。


**详细内容请参考：《深入理解Java虚拟机》**

#### 对象虚拟机中的访问定位

- 句柄访问
    - Java堆中会划分出一块内存作为句柄池，栈中的Reference指向对象的句柄地址，句柄中包含了对象实例数据引用(Instance Data Reference)和类型数据引用(Class Data Reference)
    - 直接指针访问，Reference中存的是对象的地址(地址中包含的有Class Data Reference)

---

### Java类加载机制

Java的类加载大体上按照**加载 --> 验证 --> 准备 --> 解析 --> 初始化**的顺序进行的。

其中，加载、验证、准备和初始化时严格按照顺序开始的，解析可能发生在初始化之后(目的是为了支持Java运行时绑定)，也可能发生在初始化之后。虽然上述阶段是按照严格顺序开始的，但不是按照顺序进行或者完成的，通常是互相交叉混合进行的。

Java当中的绑定指的是，将一个方法的调用与一个方法的主题关联起来，分为静态绑定和动态绑定。静态绑定是compile-time绑定，也叫前期绑定。Java当中只有final、static、private和constructor方法是前期绑定的；动态绑定是run-time绑定，又叫后期绑定，运行时根据对象的类型进行绑定，几乎所有的方法都是动态绑定。

#### 加载阶段

通过一个类的全限定名获取其二进制字节流，将这个字节流代表的静态储存结构转化为方法区运行时数据结构，在java堆中生成一个java.lang.Class对象，最为方法区某些数据的访问入口。

可以使用系统或者自己定义的类加载器完成类的加载，类加载器包括：

* 启动类加载器(BootStrap ClassLoader)，负责加载`jdk/jre/lib/java core API`(java开头的文件)。启动类加载器无法被Java程序直接引用。
* 扩展类加载器(Extension ClassLoader)，负责加载`jdk/jre/lib/ext`目录中(javax开头的文件)。扩展类加载器可以在开发中直接引用。
* 应用类加载器(Application ClassLoader)，负责加载用户类路径ClassPath所包含的类。应用类加载器可以在开发中直接引用。

`自定义加载器 --> 应用程序加载器 --> 扩展类加载器 --> 启动加载器`称之为"双亲委派模型"。后面的加载器是前面加载器的父加载器，他们之间并不是继承关系，而是通过composition来复用父加载器的代码。

加载器的工作流程是：`收到类加载请求 --> 把请求委托给父加载器完成 --> 依次向上 --> 到启动类加载器 --> 父类加载无法加载(找不到相应的class文件) --> 自己完成加载`。

#### 验证阶段

验证的目的是确保class文件的字节流包含的信息符合当前虚拟机的要求，而不会危害虚拟机自身的安全，验证的内容：文件格式、元数据、字节码和符号引用。

#### 准备阶段

为**类变量**分配内存并设置类变量的初始值，这些内存都将在方法区中分配。这时候的内存分配仅包含类变量(static)，不包含实例变量，实例变量会在对象实例化时随对象分配在堆中，这里的初始值为默认初始值，而不是Java代码中assign的值。

**Note:**

1. 对于基本数据类型对应的类变量(static)和全局变量，不显式assign值，会使用默认值，局部变量不assign值，编译不通过。
2. static final变量和只有final修饰的变量可以在声明的时候显式赋值或者初始化的时候赋值，否则编译不通过。总之，有final修饰的变量，在使用之前必须显式assign，系统不会默认assign default value。
3. 对于引用数据类型、数组或引用对象，没有显式的赋值，系统为其赋默认值null。
4. 数据在初始化时没有赋值，则默认为0。
5. static final变量在准备阶段就会变成常量，在编译阶段将其结果放入调用它的类的常量池当中。

#### 解析阶段

将常量池中的符号引用转化为直接引用的过程，解析可能发生在初始化之前或者之后，分为静态解析和动态解析。static变量发生在静态解析阶段。

#### 初始化

在这一个阶段真正的执行类中定义的Java程序代码

`加载 --> 验证 --> 准备 --> 解析(没有严格顺序) --> 类初始化 --> 对象实例化`

---

### 设计模式

* 参考文章: [设计模式](http://xx.xx.192.208:4000/2016/11/16/design-pattern.html "设计模式")

* 参考书籍：《Java与模式》作者：阎宏

---

### 数据结构与算法

* 参考书籍：[A concise Introduction to Object-Oriented DSA](http://www.biostat.umn.edu/~johnh/book.html "DSA")

* 参考文章: [一部一个脚印学习数据结构与算法](https://fmzhao.github.io/follow-me-step-by-step-to-learn-DSA/ "数据结构与算法")

* 参考文章：[排序算法总结](https://fmzhao.github.io/sequence-algorithm/ "排序算法")

---

### java并发

(待续)...

### 数据库

(待续)...

### 操作系统

(待续)...

### 网络

(待续)...


### 面试中常见的问题汇总

参考总结：[秋季校招笔面试](https://fmzhao.github.io/autumn-interview-problems-collection/ "笔试面试")

### 书籍推荐

* 《Java编程思想》第四版
* 《深入理解Java虚拟机》第二版
* 《Java与模式》作者：阎宏
* 《java并发编程实战》英文名：《Java concurrency in practice》
* 《现代操作系统》英文名：《Modern Operating System》
* ...

---
---

## Questions

### \#1 为什么代码的输出结果是"父亲"

    public class Child extends Father{
        private String name = "孩子";
        public static void main(String args[]){
            Child c = new Child();
            System.out.println(c.getName());
        }
    }

    class Father {
        private String name = "父亲";
        public String getName(){
            return name;
        }
    }// output: "父亲"

*Answer:*

`Child`类继承了`Father`类，当然也继承了`Father`类中的`getName()`方法。当`Child`对象调用`getName()`方法的时候，要去`Father`类中进行调用，`name`属性的值为"父亲"。这里，我的理解是自动向上转型。

当代码变为如下：

    public class Child extends Father{
        private String name = "孩子";
        @Override
        public String getName(){
            return name;
        }
        public static void main(String args[]){
            Father c = new Child();// 或者 Child c = new Child()
            System.out.println(c.getName());
        }
    }

    class Father {
        private String name = "父亲";
        public String getName(){
            return name;
        }
    }// output: "孩子"

输出的结果都是："孩子"。

当代码变为如下：

    public class Child extends Father{
        private String name = "孩子";
        @Override
        public String getName(){
            return name;
        }
        public static void main(String args[]){
            Father c = new Father();
            System.out.println(c.getName());
        }
    }

    class Father {
        private String name = "父亲";
        public String getName(){
            return name;
        }
    }// output: "父亲"

输出结果是: "父亲"。

---
