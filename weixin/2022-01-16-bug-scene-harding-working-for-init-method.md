---
layout: post
title: 'bug现场谜之困在“init”方法上的那些时间！'
subtitle: 'war包从tomcat迁移到jetty，报错NoSuchMethodError: xxx.WebSocketServerFactory.init(Ljavax/servlet/ServletContext;)V，可是找到对应的类，明明方法存在啊？最后得出结论：“出来混，知识的盲区迟早是要还的！”'
background: 'https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/bug-scene-init-constructor-or-normal.jpg'
comment: false
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 真相浮出水面](#3)
- [4. 总结](#4)

---

<h3 id="1">1. bug现场情况</h3>

现场将在`Tomcat 8.5`中运行的`war`包迁移到`jetty 9.4.19`上，启动容器后报错：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/init-weixin-page-01.jpg)

`NoSuchMethodError`应该是看到后最有头绪一个错误了：“在加载到`JVM`的对应类中找不到当前调用的方法”。

如果编译环境中对应类没有对应的方法，是不能编译成功的（集成开发环境会报错）。如果编译成功后部署时候报错`NoSuchMethodError`，说明运行时和编译时依赖的类不一致。

> 这里说的“编译时依赖”指的是：构建工具在编译时`CLASSPATH`中依赖的`class`；“运行时依赖”指的是：`JVM`实例运行时加载到`JVM`中的`class`。对于同一个`class loader`，只会成功加载一次`class`。

上面的异常堆栈显示：**类org.eclipse.jetty.websocket.server.WebSocketServerFactory没有构造方法WebSocketServerFactory(javax.servlet.ServletContext)**。那就看一看运行时依赖的类有没有对应的构造方法吧。

现场的情况下，只能用`javap`命令，但是首先你要找到这个类是从哪个`jar`包加载的，如何根据类找到加载的`jar`包路径在接下来的[尝试破案](#2)做进一步说明。

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/init-weixin-page-02.jpg)

> 注意这里如果使用 `javap -cp lib/websocket/* xxxx`这样指定`classpath`用`*`配置的方式无效，但是对于`javac`命令是有效的。

这不是存在`WebSocketServerFactory(ServletContext context)`构造方法吗？？？

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/bug-scene-init-constructor-exists.png)

后来笔者有尝试了多种途径确认这个构造方法是存在的，但是却报错`NoSuchMethodError`，网上一大堆找“java.lang.nosuchmethoderror but method exists”，无果。因为网上说的最后都证明确实没有对应的方法。

但本案发现场的情况是它有啊！现场变得诡异起来了！难道笔者找到了一个超级`bug`？直觉告诉我100%不会，一定是自己哪块错了。

<h3 id="2">2. 尝试破案</h3>

回顾一下案发现场的情况，报错`java.lang.NoSuchMethodError: org.eclipse.jetty.websocket.server.WebSocketServerFactory.init(Ljavax/servlet/ServletContext;)V`，可是通过`javap`工具反编译明明有构造方法`WebSocketServerFactory(javax.servlet.ServletContext)`啊！

一般`NoSuchMethodError`异常有两种情况：

1. `classpth`中该方法的类在多个jar包中，而JVM加载的jar包的那个类没有该方法。
2. 只有一个`jar`包，`jar`包中的类没有该方法。

这两种情况归根结底是JVM运行时加载的类中确实缺失了方法。但是上面遇到的问题查找加载类是存在报错的构造方法的。

> 如果`JVM`的`classpth`中有多个包存在同一个`class`，到底`JVM`会加载那个包中的`class`是平台相关的(`Linux`系统和`Windows`系统上可能加载的不是同一个`jar`包)。需要注意：`JVM`从`classpth`下的`jar`包中`load`对应的`class`文件，这跟`jar`包的命名没有关系。

可以通过以下方法根据报错信息定位加载的jar包：

1). `JVM`使用参数`-verbose:class`，这个参数能够输出加载`class`的`jar`包绝对路径。

2). 使用java代码：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/init-weixin-page-03.jpg)

3). 使用linux命令：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/init-weixin-page-04.jpg)

在允许重启系统或者启动的`JVM`中设置了`--verbose:class`参数的话“1)”方法是最方便的，可以直接在日志中查找对用的类。

不允许重启`JVM`的话可以采用方法“2)”，但是要指定正确的`classpath`，否则加载不到对应的类。查找`classpath`可以从`jvm`对应的进程中查找。

> 对于`springboot`框架打成的`jar`包，一般依赖都打进在`jar`包中了；对于`severlet`容器使用的`war`包，依赖除了`WEB-INF/lib`外还包括容器安装目录下的`lib`包；对于普通的`jar`包，依赖可能定义在了`MANIFEST`元文件中(更多关于`MANIFEST`内容可以参考：[https://fengmengzhao.github.io/2021/12/18/bug-scene-of-old-jar-classpath-mystery.html](https://fengmengzhao.github.io/2021/12/18/bug-scene-of-old-jar-classpath-mystery.html)。

如果想查找指定目录的哪个`jar`包含有某个`class`，可以使用“3)”方法，列出`jar`中包含的文件清单并查找匹配。

为什么要费劲找到报错类是从哪个`jar`包中加载的呢？一来`jar`包一般能提供版本相关的信息；二来`javap`命令是需要指定`jar`包作为`classpath`才能成功反编译。

使用`javap`命令反编译，语法如下：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/init-weixin-page-05.jpg)

> `javap`的`-verbose`参数展示`class`文件的详细编译信息，如果只想判断是是否有某个方法，可以不加`-verbose`参数。

通过上面的方法确认本示例的情况：明明方法存在啊，为什么`NoSuchMethodError`，百思不得其解！

<h3 id="3">3. 真相浮出水面</h3>

怎么办呢？问题总是要解决的。

在开发环境上准备调试代码，突然意识到报错中的`init`是不是一个普通方法啊？

赶快看看反编译的代码发现确实没有`init`普通方法，只有`init`构造方法。问题就出在这里，查了一下发现`jetty`在`9.3`升级到`9.4`的时候对`WebSocketServerFactory`的`init`由**普通方法**改为**构造方法**。

这是笔者的一个知识误区，以为`WebSocketServerFactory.init(Ljavax/servlet/ServletContext;)V`是一个构造方法，实际上如果是构造方法报错是长这样的：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/init-weixin-page-06.jpg)

普通方法和构造方法实际上就是`.init()`和`.<init>()`的区别。

> 这里报错中`.<init>([Ljava/lang/Object;)V`中`.`表示是一个方法的调用；`<init>`表示构造方法的调用；`[`表示一个数组；`Ljava/lang/Object;`表示`java.lang.object`对象；`V`表示返回类型是`void`。实际上就是`SpringApplicationBuilder(java.lang.Object...)`的构造方法，方法的参数是`java.lang.Object`数组。这种写法和`class`文件的内部表示是一致的。`jvm`更多类型表示参考：[https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html)

对于一个程序员来说，异常的的堆栈信息是司空见惯的，也就懒得深究其中的一些玄机，果然“报应不爽”！出来混，迟早要还的。

<h3 id="4">4. 总结</h3>

1. 很多看似玄学的`bug`解释不了，最后原因总是归结为“知识的盲区”。很多知识不必懂的很深入，但是基本的东西要了解，此时“不求甚解”，彼时“这是玄学？”。
2. 有些时候会无意识的想当然一些结论（比如本示例中`.init()`方法自然认为是构造方法）。没办法十分敲定的东西，要多查一查，多一份思路。
3. 排查问题，针对一个思路要充满信心，即时这个思路不能解决问题，之前也要能得出这条思路的结论。不能急躁、粗心、盲目尝试。思路窄了，就停下来，明天再尝试，避免进入死胡同。
