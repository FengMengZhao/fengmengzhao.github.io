---
layout: post
title: Spring Boot学习
subtitle: 一个内嵌Web服务器(Tomcat/Jetty)的可执行程序框架,利用SpringBoot开发的程序不需要作为war包部署到web服务器中,而是作为一个可执行的程序,直接启动执行.
background: '/img/posts/spring-boot.jpg'
comment: true
---


**Spring Cloud**是一个微服务开发和治理框架,包含了微服务运行功能,比如远程调用过程,动态服务发现,负载均衡,限流等.

**Spring Boot**是一个内嵌Web服务器(Tomcat/Jetty)的可执行程序框架,利用SpringBoot开发的程序不需要作为war包部署到web服务器中,而是作为一个可执行的程序,直接启动执行.

**微服务构架**(Microservice Architecture)是将传统的整体风格(monolithic style)应用程序分解为一套小服务的集合,每个服务运行在自己的进程中,并使用轻量级通信机制,通常是HTTP API.这些服务围绕业务能力来构建,并通过完全自动化部署机制来独立部署.这些服务使用不同的编程语言书写,以及不同存储技术,并保持最低限度的集中式管理.

Spring boot比较适合微服务部署方式,因此Spring Cloud是建立在Spring Boot的基础之上提供快速实现分布式系统中各种问题的组件,而且没项都不是一种选择,在替换的时候业务代码是不受影响的. 

## 目录

- [Spring Boot是什么](#1)
- [开始使用Spring Boot](#2)
    - [Hello World Project](#2.1)
- [参考资料](#3)

---

<h3 id="1">Spring Boot是什么</h3>

Spring Boot是一个帮助你从繁琐的配置spring应用中脱离出来的轻量级框架.

Spring Boot的目的是提供一系列工具帮助快速构建易于配置的Sring应用.

> 仅仅是创建一个输出"Hello, world!"的spring应用,就需要做大量的配置.

**spring boot标签:**

- 单体的(stand-alone)
- 产品级(production-grade)
- 一键运行(just run)
- 最小依赖(optionated view)
- 很少配置(easy)

> Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run." We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss.

上述特点意味着: 你可以用很少的配置迅速创建一个Spring项目,配置的方式是使用注解(annotation),没有xml配置.

**spring boot特点**

- 创建独立运行的spring项目
- 内置Tomcat,Jetty等容器(不需要部署war包)
- 提供可选的'starter' POMS简化Maven配置
- 任何阶段可以动态配置spring
- 提供生产就绪的特性,例如指标,健康检查(health check),扩展性的配置
- 无代码产生,无XML配置

---

<h3 id="2">开始使用Spring Boot</h3>

**starter**

Spring Boot中的`starter`用来最小限度的减少手动的依赖关系配置.

一个`starter`就是一个依赖关系的集合(例如一个Maven POM),代表特定的一种应用类型.

所有的`starter`都遵循命名规范:`spring-boot-starter-XYZ`,`XYZ`是要构建的应用类型.常用的Spring Boot `starters`:

- `spring-boot-starter-web`: RESTful风格的web service,内置SpringMVC和Tomcat容器
- `spring-boot-starter-jesery`: 和`spring-boot-starter-web`相似,内置Apache Jesery而不是SpringMVC
- `spring-boot-starter-jdbc`: 用来创建JDBC连接池,由Tomcat JDBC连接池实现.

[参考: 更多starters](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-starter)

**Auto-configuration**

Spring Boot使用注解`@EnableAutoConfiguration`来自动配置应用.自动配置依据CLASSPATH中的JARS和Bean的定义方式.

<h4 id="2.1">Hello World Project</h4>

**新建一个maven项目(eclipse)**

![新建maven项目](/img/posts/new-maven-project.png)

![选择工作空间](/img/posts/set-workspace.png)

![指定构架](/img/posts/select-archetype.png)

![输入参数](/img/posts/specify-parameter.png)

**修改POM文件**

    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>

      <groupId>com.thunisoft</groupId>
      <artifactId>springbootdemo</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <packaging>jar</packaging>

      <name>springbootdemo</name>
      <url>http://maven.apache.org</url>

      <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java-version>1.7</java-version>
      </properties>
      
      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
      </parent>

      <dependencies>
      
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
      
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
      </dependencies>
    </project>


**修改main()文件**

    package com.thunisoft.springbootdemo;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;


    @SpringBootApplication
    public class App 
    {
        public static void main( String[] args  )
        {
            SpringApplication.run(App.class, args);
        
        }

    }

**编写HelloWorld文件**

    package com.thunisoft.springbootdemo.controller;

    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping(path="/springboot")
    public class HelloWorld {
        
        @RequestMapping(path="hello")
        public String hello(){
            return "Hello,world!";
    
        }

    }

**项目启动成功**

![项目启动成功](/img/posts/startup-success.png)

> 访问`http://localhost:8080/springboot/hello`即可输出: `Hello,world!`

**构建可执行的Jar文件**

![创建可执行jar文件](/img/posts/create-jar.png)

> java -jar target/springbootdemo-0.0.1-SNAPSHOT.jar即可执行成功

---

<h3 id="3">参考资料</h3>

- [原文](https://www.ibm.com/developerworks/library/j-spring-boot-basics-perry/)
- [项目源码](http://gitlab.thunisoft.com/ZHSPDCSJ/spring-boot-practice)
- [spring vs springMVC vs springboot](https://dzone.com/articles/spring-boot-vs-spring-mvc-vs-spring-how-do-they-compare)

---

> 注: 这是spring cloud技术学习之**spring boot**,后续还有[微服务构架](https://fmzhao.github.io/microservice-architecture/)和[spring cloud](https://fmzhao.github.io/spring-cloud-learn/)两部分.
