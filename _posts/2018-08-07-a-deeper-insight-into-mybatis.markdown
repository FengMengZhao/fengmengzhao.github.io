---
layout: post
title: '深入理解Mybatis'
subtitle: 'Mybatis作为一个ORM框架将SQL与Java对象映射起来大大提高开发效率，其配置和使用相对简单，本文从使用出发试图在源码级别窥探其背后的工作原理。'
background: '/img/posts/insight-into-mybatis.jpg'
comment: true
---

# 目录

- [1. Mybatis概要](#1)

---

<h3 id="1">Mybatis概要</h3>

在原始`JDBC`操作数据库的时候，通常需要：

1. 加载`JDBC`驱动(`Driver`);
2. 获取数据库连接(`Connection`);
3. 创建SQL + `Statement`;
4. 为`PreparedStatement`中的SQL设置参数值(占位符参数);
5. 通过`PreparedStatement`执行SQL并获取`ResultSet`;
6. 解析`ResultSet`进行处理;
7. 释放资源(`ResultSet`、`PreparedStatement`、`Connection`).

原始的JDBC存在以下几个方面的问题：

1. 每次使用数据库连接时需要创建，使用后即释放资源，频繁的开/闭数据源，造成资源的浪费和影响数据库的性能(可以考虑用数据库连接池);
2. SQL语句硬编码在Java代码中，耦合性太高，SQL一旦改变需要重新编译项目(可以考虑使用配置文件);
3. SQL占位符参数类型、位置和值硬编码在Java代码中，耦合性太高(考虑将占位符参数可以配置在配置文件中);
4. 硬编码处理`ResultSet`结果集(将结果集自动映射为Java对象).

这时候`Mybatis`作为ORM框架就出现了，主要解决的是SQL和Java对象的映射问题。

首先，看一看ORM框架(Mybatis/Mybatis-Spring)在整个项目中的层级：

![ORM框架层级](/img/posts/how-orm-works-in-application.png "ORM框架层级")

> 图中的`Repository`就是应用开发中的`Dao`层。`ORM`框架建立在`Java JDBC API`基础之上，`Java JDBC API`建立在`Java JDBC API Implementation`基础之上，`Java JDBC API Implementation`建立在持久层数据库之上。

接下来，看一看Mybatis3各个组件之间的工作原理：

![Mybatis工作原理](/img/posts/relation-of-mybatis3-components.png "Mybatis3工作原理")

> 1. 项目全局只需要执行一次的操作：`SqlSessionFactoryBuilder`通过读取Mybatis配置文件，创建`SqlSessionFactory`;
2. `Dao`层每次接口被调用时执行的操作：`SqlSessionFactory`创建`SqlSession`，`SqlSession`读取`MappingFile`映射文件执行SQL.<br><br>
需要注意的是：<br>
- `SqlSessionFactory`应该是单例的;
- `SqlSession`是线程不安全的，其作用域应该是方法级的.

