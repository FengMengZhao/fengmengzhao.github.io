---
layout: post
title: 'Spring Bean注入配置总结'
subtitle: 'Spring框架是一个DI框架，在配置Bean的时候采用多种方式(XML配置、Java配置类或者是注解)，怎么样注入Bean也有多样(setter方法、构造方法或者注解)，配置过程中经常会出现各种已经配置但是无效的情况，本文总结配置情况作为开发时参考。'
background: '/img/posts/spring-bean-injection-config.jpg'
comment: true
---

# 目录

- [1. Spring工程&配置文件](#1)
- [2. setter方法方式注入Bean](#2)
- [3. 构造方法(constructor)方式注入Bean](#3)
- [4. 注解(@Autowired)引用Bean(ref)方式注入Bean](#4)
    - [4.1 context:annotation-config(配置文件读取开启)方式](#4.1)
    - [4.2 context:component-scan(包扫描开启)方式](#4.2)

---

<h3 id="1">Spring工程&配置文件</h3>

> 基本的Spring工程只需要引入如下依赖：

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>4.3.7.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-beans</artifactId>
        <version>4.3.7.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>4.3.7.RELEASE</version>
    </dependency>

> Spring配置文件(spring-config.xml):

    <?xml version="1.0" encoding="UTF-8"?>

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:context="http://www.springframework.org/schema/context"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
                               http://www.springframework.org/schema/beans/spring-beans.xsd
                               http://www.springframework.org/schema/context
                               http://www.springframework.org/schema/context/spring-context.xsd">

        <bean></bean>
        ...
        <bean></bean>

    </beans>

> 如何通过ID获取一个Bean：

    package com.fmz.demo;

    //获取一个ApplicationContext
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml")
    Bean bean = applicationContext.getBean("beanId")

---

<h3 id="2">setter方法方式注入Bean</h3>

> 一个简单的pojo：

    package com.fmz.learn.pojo;

    public class Title {
        private String titleValue;
    
        public String getTitleValue() {
            return titleValue;
        }

        public void setTitleValue(String titleValue) {
            this.titleValue = titleValue;
        }

        @Override
        public String toString() {
            return "Title{" +
                    "titleValue='" + titleValue + '\'' +
                    '}';
        }
    }

> 配置`Title`类对应的Bean(在spring-config.xml中)：

    <bean id="bookTitle" class="com.fmz.learn.pojo.Title">
        <property name="titleValue">
            <value>第一本Spring Book</value>
        </property>
    </bean>

---

<h3 id="3">构造方法(constructor)方式注入Bean</h3>

> 一个简单的pojo：

    package com.fmz.learn.pojo;

    public class Book {

        private Title title;

        public Book(Title title){
            this.title = title;
        }

        public Title getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "bookTitle=" + title +
                    '}';
        }
    }

> 上述pojo中没有setter方法，可以通过constructor的方法配置Bean(spring-config.xml):

    <bean id="book" class="com.fmz.learn.pojo.Book">
        <constructor-arg index="0" ref="bookTitle" />
    </bean>

---

<h3 id="4">注解(@Autowired)引用Bean(ref)方式注入Bean</h3>

在通过constructor方法配置Bean的时候，使用`ref`来应用已经定义好的Bean，Spring提供了注解的方法可以注入引用Bean。

使用注解时，首先要在配置文件(spring-config.xml)开启对应的Bean扫描。Spring提供了两种的Bean扫描方式：`<context: annotation-config />`(对配置文件中定义的Bean进行扫描);`<context: component-scan />`(对Java类中使用诸如`@Controller`、`@Service`、`@Repository`、`@Component`等进行扫描)。

<h4 id="4.1">context:annotation-config(配置文件读取开启)方式</h4>

> 在`spring-config.xml`中开启配置文件扫描：

`<context:annotation-config />`

> 在pojo中用注解进行注入：

    package com.fmz.learn.pojo;

    import org.springframework.beans.factory.annotation.Autowired;

    public class Book {

        @Autowired
        private Title title;

        public Title getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "bookTitle=" + title +
                    '}';
        }
    }

> 在配置文件中定义该Bean的时候，就不使用setter或者constructor了：

    <bean id="book" class="com.fmz.learn.pojo.Book" />

---

<h4 id="4.2">context:component-scan(包扫描开启)方式</h4>

> 在`spring-config.xml`中开启包扫描：

    <context:component-scan base-package="com.fmz.learn" />

> 用`@Repository`定义一个Dao为Bean：

    package com.fmz.learn.dao;

    @Repository
    public class UserDao {

    }

> 在Service类中注入上述`Repository`:

    package com.fmz.learn.service;

    import java.util.List;

    @Service
    public class UserService {

        @Autowired
        private UserDao userDao;
    }

> 用Spring-Junit进行测试：

    package com.fmz.learn.service;

    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = "/spring-config.xml")
    public class UserServiceTest {


        @Autowired
        private UserService userService;

        @Test
        public void getUserByName() throws Exception {
            System.out.println(userService.getUserByName("fmz"));
        }

    }

---
