---
layout: post
title: Java日志框架详解
subtitle: 这些年用过的日志框架和这些年走过的坑...
background: '/img/posts/java-logging-framework.png'
comment: true
---

## 目录

- [1 日志框架](#1)
- [2 SL4J日志API和日志框架实现](#2)
    - [2.1 SL4J默认简单实现方式](#2.1)
    - [2.2 JDK Logging](#2.2)
    - [2.3 log4j](#2.3)
    - [2.4 logback](#2.3)

---

<h3 id="1">1 日志框架</h3>

当你使用Java输出日志时，你需要一个或者多个日志框架。框架能提供对象、方法和必要的配置来发送日志信息。Java语言本身有自带的日志实现包`java.util.logging`。还有很多第三方的日志框架，包括`log4j`、`log4j 2`、`logback`。`sl4j`和Apache Commons Logging提供了日志的抽象层次，通过它可以从底层的日志框架实现解耦合，从而在不同的日志框架间切换。

<h3 id="2">2 SL4J日志API和日志框架实现</h3>

SL4J提供了日志框架的一个API。它是怎么工作的呢:

1. 在`CLASSPATH`中寻找SL4J的bindings(`StaticLoggerBinder`)
2. 确保仅仅存在一个这样的binding
3. 确定好具体的binding后，依据具体的配置进行工作

> 下图说明Java日志API和框架如何协同工作的：

![Java日志API和框架如何协同工作的](/img/posts/how-java-logging-facade-works.png "Java日志API和日志框架是如何协同工作的")

**SL4J API maven包依赖**

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.21</version>
    </dependency>

<h4 id="2.1">2.1 SL4J默认简单的实现方式</h4>

**maven包依赖**

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.25</version>
    </dependency>

<h4 id="2.2">2.2 JDK Logging</h4>

> JDK Logging就是`package java.util.logging`，通常也被大家叫做`JUL`。

**maven包依赖**

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-jdk14</artifactId>
        <version>1.7.5</version>
    </dependency>

**配置文件**

JUL可以通过两种方式进行配置：

- 通过配置class(通过JVM启动参数`java.util.logging.config.class`可以指定配置class)
- 通过配置文件(通过JVM启动参数`java.util.logging.config.file`可以指定配置file)

在类**LogManager.java**中可以看到配置文件的寻找规则。

`LogManager.java`寻找配置文件规则源码：

    String fname = System.getProperty("java.util.logging.config.file");
    if (fname == null) {
        fname = System.getProperty("java.home");
        if (fname == null) {
            throw new Error("Can't find java.home ??");
        
        }
        File f = new File(fname, "lib");
        f = new File(f, "logging.properties");
        fname = f.getCanonicalPath();

    }
    InputStream in = new FileInputStream(fname);
    BufferedInputStream bin = new BufferedInputStream(in);
    try {
        readConfiguration(bin);

    } finally {
        if (in != null) {
            in.close();
        
        }

    }

从上面源码中可以看出(针对配置file的情况)：

- 不指定`java.util.logging.config.file`时，会默认`logging.properties`在JVM安装目录`lib`下
- 如果指定的话，会在JVM启动目录的`相对路径`中读取

在JVM启动目录的相对路径进行读取logging.properties会出现一个问题：JVM启动目录变了，则可能就读取不到logging.properties了。

这会造成一种结果：在本地开发时能够正常读取配置文件，maven打包后使用命令行执行时找不到配置文件，原因就在于JVM启动目录发生了改变。所以得出的结论是：用**相对路径**不可靠，应该使用`CLASSPATH`的路径，API中也提供了可以重新加载配置文件的API，因此可以在代码中进行加载：

    static {
            LogManager manager = LogManager.getLogManager();
            //InputStream is = new ClassPathResource("logging.properties").getInputStream();//Spring Framework
            try {
                // 从CLASSPATH中读取logging.properties
                manager.readConfiguration(Wombat.class.getClassLoader().getResourceAsStream("logging.properties"));
            
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            
            }
        
    }

> 这样就可以保证是从`CLASSPATH`中读取`logger.properties`了

> 问：何为JVM启动目录？<br><br>
答：就是执行`java`命令是所在的目录。<br><br>
问：使用IDE时，启动目录是什么？<br><br>
答：在`Run as configuration`中可以看到一个`Working directory`选项，应该就是JVM的启动目录，可以改变这个值，从不同的目录启动JVM。如下图 - Myeclipse JVM启动目录。

> Myeclipse JVM启动目录

![Myeclipse JVM启动目录](/img/posts/myeclipse-jvm-starts-up-directory.png "Myeclipse JVM启动目录")


> 简单的配置 - logging.properties：

    #This is JDK Logger property
    #Works with a startup arguement: Djava.util.logging.config.file=logging.properties
    #Level的五个等级SEVERE（最高值） 、WARNING 、INFO 、CONFIG 、FINE 、FINER 、FINEST（最低值）  。这个不同于log4j  

    handlers=java.util.logging.ConsoleHandler,java.util.logging.FileHandler

    .level=INFO

    #为 Handler 指定默认的级别（默认为 Level.INFO）。   
    #java.util.logging.ConsoleHandler.level=FINE
    # 指定要使用的 Formatter 类的名称（默认为 java.util.logging.SimpleFormatter）。   
    java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter  
    # 为 Handler 指定默认的级别（默认为 Level.ALL）。   
    #java.util.logging.FileHandler.level=FINE
    # 指定要使用的 Formatter 类的名称（默认为 java.util.logging.XMLFormatter）。   
    java.util.logging.FileHandler.formatter=java.util.logging.SimpleFormatter  
    # 指定要写入到任意文件的近似最大量（以字节为单位）。如果该数为 0，则没有限制（默认为无限制）。   
    java.util.logging.FileHandler.limit=1024000  
    # 指定有多少输出文件参与循环（默认为 1）。   
    java.util.logging.FileHandler.count=1  
    # 为生成的输出文件名称指定一个模式。有关细节请参见以下内容（默认为 "%h/java%u.log"）。   
    java.util.logging.FileHandler.pattern=E\:/SSLog%u.log  
    # 指定是否应该将 FileHandler 追加到任何现有文件上（默认为 false）。   
    java.util.logging.FileHandler.append=true

<h4 id="2.3">2.3 log4j</h4>

**maven包依赖**

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.5</version>
    </dependency>

**配置文件**

在类**LogManager.java**中可以看到配置文件的寻找规则。会从`CLASSPATH`中读取`log4j.properties`(详细规则看源码)。

> 简单的配置 - log4j.properties

    #This is a log4j property
    log4j.rootLogger=INFO, STDOUT
    log4j.logger.deng=INFO
    log4j.appender.STDOUT=org.apache.log4j.ConsoleAppender
    log4j.appender.STDOUT.layout=org.apache.log4j.PatternLayout
    log4j.appender.STDOUT.layout.ConversionPattern=%5p [%t] (%F\:%L) - %m%n

<h4 id="2.4">2.4 logback</h4>

**maven包依赖**

    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.0.1</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>1.0.1</version>
    </dependency>

**配置文件**

在类**StaticLoggerBinder.java#init()**方法中可以看到配置文件的寻找规则。

> 简单的配置 - logback.xml

    <?xml version="1.0" encoding="UTF-8"?>

    <configuration>

        <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
            <!-- encoders are assigned the type ch.qos.logback.classic.encoder.PatternLayoutEncoder 
                by default -->
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n
                </pattern>
            </encoder>
        </appender>

        <logger name="com.lordofthejars.foo" level="INFO" additivity="false">
            <appender-ref ref="STDOUT" />
        </logger>

        <!-- Strictly speaking, the level attribute is not necessary since -->
        <!-- the level of the root level is set to DEBUG by default. -->
        <root level="INFO">
            <appender-ref ref="STDOUT" />
        </root>

    </configuration>

---
