---
layout: post
title: 深入理解计算机系统
---

---

## 计算机系统之旅

**一个经典计算机系统组成示意图**

![Hardware organization](/img/posts/Hardware_organization_of_a_typical_system.png)

**计算机当中的一些重要抽象示意图**

![Abstraction in computer](/img/posts/Some_Abstractions_Provided_by_a_Computer_System.png)

**进程的虚拟地址空间示意图**

![Virtual address](/img/posts/Process_Virtual_Address_Space.png)

---

## 程序的结构和执行(第一部分)

### 表示(Representing)和操作(Manipulating)信息

*参考文章*: 

[https://fmzhao.github.io/a-tutorial-on-data-representation-Integers-Floating-point-numers-and-Characters/](https://fmzhao.github.io/a-tutorial-on-data-representation-Integers-Floating-point-numers-and-Characters/ "参考文章")

---

### 程序的机器级别(Machine-Level)表示

`gcc -o p p1.c p2.c`

> 首先，C预处理器(Preprocessor)将有`#include`定义的命令(command)和宏(Macros)加载(expand)到文件当中；<br><br>
然后，编译器(Complier)产生汇编语言版本的文件p1.s和p2.s；<br><br>
接着，汇编器(Assembler)将汇编源码转化为二进制对象文件(object-code)p1.o和p2.o，对象代码是机器代码的一种，它包含了所有指令的二进制表示，但是不包含全局变量的地址；<br><br>
最后，链接器(Liker)组合者两个对象文件以及代码实现中的库函数(library function, e.g., printf)并且生成最终的可执行文件，这是严格意义上被处理器执行的代码格式
