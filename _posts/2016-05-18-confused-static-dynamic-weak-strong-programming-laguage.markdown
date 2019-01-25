---
layout: post
title: 令人迷惑的动态静态强弱编程语言 
---

### static typed language vs dynamic typed language & weaked typed laguage vs strong typed language

一个变量有变量名、变量的类型和变量的值。

静态类型语言的变量名是和数据类型绑定的(一旦声明，不能更改)，并且要求在编译期知道变量的类型，这样可以做许多类型的校验，提前发现BUG，这也是静态类型语言的一个优点；动态类型的变量类型是根据变量的值而定的，不和变量的名进行绑定。

在一个静态类型的语言中，每一个变量名，通过变量声明(declaration)在编译时，绑定一个声明类型或者一个对象。对象的绑定是可选的，如果没有绑定一个对象，则这个变量名为null。一旦变量名绑定一个类型之后，这个变量名只能绑定相同类型的对象，不能绑定不同类型的对象。如果试图绑定其他类型的对象则会一起类型异常(tpye error)。

在一个动态类型的语言中，非null的变量名仅仅绑定一个对象。在执行的过程中，通过赋值(assignment)，这个变量名可能绑定不同类型的对象。

在一个弱类型的语言中，变量可以隐含(implicitly)转化为其他非相关的类型。

在一个强类型的语言中，变量不能够隐含转化为非相关的类型，如果要转化，显示的转换的必须的。

### 示例

#### 动态类型语言(pyton and php) vs 静态类型语言(java and c)

python中这样的写法是合法的

    a = 1;
    a = '1';

Java中这样的写法是不合法的

    int a;// 声明的时候类型已经确定为integer
    a = '1';// 不能指向一个非integer类型的对象

#### 弱类型语言(php and c) vs 强类型语言(python and java)

php语言中这样的写法是合法的

    a = 1;
    a = 1 + '1'

python语言中这样的写法是不合法的

    a = 1;
    b = "1"
    c = a + b;

### 显式类型语言(manifestly typed language) vs 隐式类型语言(implicitly typed language)

显式类型语言在定义变量的时候必须显式的声明变量的类型；隐式类型的语言没有这样的要求。显式类型的语言是通过变量的声明来确定变量的类型，而隐式类型的语言是通过类型引用(typing referencing)来确定变量类型的。
