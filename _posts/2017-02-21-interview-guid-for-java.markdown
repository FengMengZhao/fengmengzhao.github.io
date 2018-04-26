---
title: Java面试准备向导
layout: post
---

### Java笔试算法题OJ

[LeetCode OJ 平台](https://leetcode.com/ "LeetCode")

> 根据平台说明，先将easy level的算法题做一做。

---

### JVM内存模型

* Java堆
* 方法区
* Java虚拟机栈
* 本地方法栈
* 程序计数器(Program Counter)

#### Java虚拟机栈

Java虚拟机栈是Java方法执行的内存模型，每一个方法在执行的时候都会创建一个栈帧，栈帧当中存放着局部变量表、操作数栈、动态链接、返回地址等。

Java虚拟机栈在编译的时候就确定了内存的大小。如果线程请求的栈深度大于虚拟机所能够提供的深度，将会抛出`StackOverflowError`；如果虚拟机在动态扩展栈时无法申请到足够的内存空间将会抛出`OutofMemoryError`。

局部变量表中存放的是方法参数和方法内部定义的局部变量、各种基本的数据类型、对象引用(reference)和ReturnAddress类型。

操作数栈用于保存中间变量。

动态链接指向方法区中的运行时常量池。

#### 堆

Java堆是用来存放Java对象和数组的地方，又叫做"gc堆"，其内存物理上可以不连续，逻辑上必须连续。

#### 方法区

方法区用来储存：已经被虚拟机加载的类信息、常量、静态变量、JIT编译后的代码等。

方法区又称为永久代，是堆的一个逻辑部分，运行时的常量池也是方法区的一部分。

Class文件除了类的版本、字段、方法、借口等描述信息之外，还有一项是Class文件常量池，这部分在加载之后将放在方法区的运行时常量池中。运行时的常量池相对于Class文件常量池的另一个特点就是动态性。运行期间可以将新的常量放入池中，较多用的是String类的`inter()`方法。

**详细内容请参考：《深入理解Java虚拟机》**

---

### Java垃圾回收

#### 概念区别

* 强引用(Object o = new Object())，只要强引用还存在GC就永远不会回收它。
* 软引用，可能还有用，但并非必须的对象，第一次gc时进行标记，第二次gc时进行回收。
* 弱引用，强度比软引用更弱一些，引用关联的对象只能生存到下一次gc回收之前。
* 虚引用，最弱的一种引用关系，用处是希望在这个对象被收集器回收时得到一个系统通知。

#### 垃圾对象的判定

* 引用计数法：每多一个引用，引用计数器加1，引用失效，计数器减1，计数为0时，回收。
* 跟搜索法：定义GC-Root，从GC Root开始向下搜索，当搜索不可达时，证明此对象不可用。GC-Root包括栈帧中的本地变量表引用对象、方法区静态属性引用变量、方法区常量引用变量和本地方法栈中Native引用的对象。

#### 垃圾回收算法

* 标记-清除算法
* 标记-整理算法
* 复制算法
* 分代收集算法(新生代，老生代)

**详细内容请参考：《深入理解Java虚拟机》**

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

* 参考文章: [设计模式](http://172.16.192.208:4000/2016/11/16/design-pattern.html "设计模式")

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
