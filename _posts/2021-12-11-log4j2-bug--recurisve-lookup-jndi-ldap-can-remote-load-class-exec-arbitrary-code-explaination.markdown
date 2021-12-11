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

实际上2021年11月24日，阿里云安全团队已经向Apache官方团队报告了`Apache Log4j2`远程代码执行漏洞。之后陆续国内多家机构监测到`Apache Log4j2`存在任意代码执行的漏洞。2021年12月10日阿里云再次报过官方`2.15-rc1`版本存在漏洞绕后，建议升级`2.15.0`版本。网上出现了`Apache Log4j2`任意远程代码执行漏洞的攻击代码，仿佛一夜间大家才紧张起来，也有网友感叹“第一次感受到互联网的脆弱”。

<h4 id="1.1">1.1 log4j日志简介</h4>

java有很多优秀的日志框架，并且设计是解耦的。接口层比如：`SL4j`、`Apache commons logging(JCL)`；实现层比如：`log4j`、`logbak`、`java util logging(JUL)`等。

`log4j`只是Apache出品的java日志框架的一种实现，所以不是说用了`Tomcat`就一定用了`log4j`框架输出日志，主要看应用是集成了哪种日志框架。

`log4j2`的集成需要在`pom.xml`中引入依赖：

```shell
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.5</version>
</dependency>
```

简单的配置文件`log4j.properties`demo如下：

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

更多java日志框架内容参考：[https://fengmengzhao.github.io/2018/06/12/detailed-explanation-of-java-logging-framework.html]([https://fengmengzhao.github.io/2018/06/12/detailed-explanation-of-java-logging-framework.html])

<h4 id="1.2"> 1.2 判断是否使用有漏洞的log4j日志框架</h4>

`log4j`存在漏洞的版本是`2.x <= 2.15-rc1`。查看对应的依赖是否存在有漏洞版本即可。具体方法：

1. maven或者gradle构建项目，查看对应的配置文件，是否有相应的log4j依赖。
2. 非第三方构建项目，找到项目的依赖`CLASSPAHT`，查看是否有相应的log4j依赖。
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

<h3 id="2">2. 漏洞分析</h3>

<h3 id="3">3. 攻防演练</h3>

<h3 id="4">4. 引用</h3>

- [https://github.com/tangxiaofeng7/apache-log4j-poc](https://github.com/tangxiaofeng7/apache-log4j-poc)
- [https://help.aliyun.com/noticelist/articleid/1060971232.html](https://help.aliyun.com/noticelist/articleid/1060971232.html)
- [https://m.freebuf.com/articles/308151.html](https://m.freebuf.com/articles/308151.html)
- [https://logging.apache.org/log4j/2.x/download.html](https://logging.apache.org/log4j/2.x/download.html)
- [https://logging.apache.org/log4j/2.x/security.html](https://logging.apache.org/log4j/2.x/security.html)
- [https://mp.weixin.qq.com/s/K74c1pTG6m5rKFuKaIYmPg](https://mp.weixin.qq.com/s/K74c1pTG6m5rKFuKaIYmPg)

---
