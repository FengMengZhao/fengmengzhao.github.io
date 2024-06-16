---
layout: post
title: Java环境变量
---

## 目录

- [1 path&CLASSPATH](#1)
	- [1.1 为什么要配置path？](#1.1)
	- [1.2 为什么要配置CLASSPATH？](#1.2)
	- [1.3 在Eclipse中为什么不用配置path和CLASSPATH](#1.3)
- [2 jar文件](#2)

---

---

<h2 id="1"> 1 path&CLASSPATH</h2> 

<h3 id="1.1"> 1.1 为什么要配置path？</h3> 

配置path是为了更加便捷，不设置path，需要在doc中输入`path\to\jdk\bin\javac Xxx.java`才能编译程序。如果是添加path后，直接在doc中输入`javac Xxx.java`即可

<h3 id="1.2"> 1.2 为什么要配置CLASSPATH？</h3> 

Java虚拟机(JVM)借助类装载器装入应用程序使用的类，具体装入哪些类可以根据当时的需要决定。

> CLASSPATH环境变量告诉类装载器到哪里去寻找第三方提供的类和用户定义的类。

<h3 id="1.3"> 1.3 在Eclipse中为什么不用配置path和CLASSPATH</h3> 

Eclipse 是IDE(Integrated Development Environment，集成开发环境)，在这样的环境中，一切都自动设置好了，只需要导入自己的jar包即可。

---

---

<h2 id="2"> 2 jar文件</h2> 

JAR包是Java中所特有一种压缩文档。

**创建一个jar包**

`jar cf file.jar 目录名/文件名(当前目录用 . 表示)`

**向存在的jar文件中添加文件**

`jar uf 制定jar包名称.jar 要添加文件名/文件夹名`

**-C应用**

表示切换到这个目录时在执行jar命令

---

---
