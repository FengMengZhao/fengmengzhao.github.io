---
layout: post
title: 'Spring整合Mybatis项目配置总结'
subtitle: 'Mybatis提供了一种ORM的映射框架，单纯使用Mybatis要求SqlSessionFactory是单例的，并且SqlSession是非线程安全的，需要在方法级中应用。而Spring框架作为一个IOC框架可以管理单例对象，可以从框架级别保证SqlSession线程的安全性及事务等相关特性。本文提供一个Spring-Mybatis整合的简单例子，供spring-Mybatis配置查询之用。'
background: '/img/posts/spring-mybatis-config-summary.jpg'
comment: true
---

# 目录

- [1. Spring-Mybatis职责及必要配置](#1)
- [2. 相关配置文件](#2)
    - [2.1 pom.xml](#2.1)
    - [2.2 web.xml](#2.2)
    - [2.3 springmvc.xml](#2.3)
    - [2.4 applicationContext-dao.xml](#2.4)
    - [2.5 applicationContext-service.xml](#2.5)
    - [2.6 applicationContext-transaction.xml](#2.6)
    - [2.7 mybatis-config.xml](#2.7)
    - [2.8 db.properties](#2.8)
    - [2.9 log4j.properties](#2.9)
- [3. 各个层次代码](#3)
    - [3.1 Controller层](#3.1)
    - [3.2 Servic3层](#3.2)
    - [3.3 Dao层](#3.3)
    - [3.4 POJO](#3.4)
    - [3.5 JSP文件](#3.5)
- [4. 项目结构图](#4)

---

<h3 id="1">Spring-Mybatis职责及必要配置</h3>

**Spring-Mybatis相比较Mybatis(整合前)**

- 由Spring来管理单例的SqlSessionFactory
- 由Spring管理Mapper对象或者Dao对象
    - 使用Spring和Mybatis整合开发mapper代理或者Dao接口
    - 自动开启事务，自动关闭SqlSession
- 由Spring管理数据源

**必要配置：**

- `web.xml`：配置前端控制器Servlet(`DispatcherServlet`)
    - 配置`<context-param>`指定spring上下文的配置文件(`applicationContext-*.xml`)
    - 配置`<init-param>`指定SpringMVC的配置文件(`springmvc.xml`)的位置
- `springmvc.xml`：配置相应的控制器映射器、控制器适配器和视图解析器
    - 配置控制器映射器(`HandlerMapping`)
    - 配置控制器适配器(`HandlerAdapter`)
    - 配置视图解析器(`ViewResolver`)
- `applicationContext-*.xml`：配置spring上下文的Bean
    - `applicationContext-dao.xml`：数据源、SqlSessionFactory和Mybatis包扫描(`MapperScannerConfigurer`)
    - `applicationContext-service.xml`：Service层的Bean(如果开启注解包扫描，可以直接用注解，不需要这个配置文件)
    - `applicationContext-transaction.xml`：事务相关
- `mybatis-config.xml`：Mybatis本身的一些配置
- `*.properties`：日志和数据源placeholder配置

---

<h3 id="2">相关配置文件</h3>

<h4 id="2.1">pom.xml</h4>

    <?xml version="1.0" encoding="UTF-8"?>

    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        
      <modelVersion>4.0.0</modelVersion>

      <groupId>com.fmz.learn.springmybatis</groupId>
      <artifactId>spring-mybatis</artifactId>
      <version>1.0-SNAPSHOT</version>
      <packaging>war</packaging>

      <name>spring-mybatis Maven Webapp</name>
      <!-- FIXME change it to the project's website -->
      <url>http://www.example.com</url>

      <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.7</maven.compiler.source>
        <maven.compiler.target>1.7</maven.compiler.target>
        <!-- jar 版本设置 -->
        <spring.version>4.2.4.RELEASE</spring.version>
      </properties>

      <dependencies>
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.11</version>
          <scope>test</scope>
        </dependency>

        <!-- spring框架-->
        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-webmvc</artifactId>
          <version>${spring.version}</version>
        </dependency>

        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-core</artifactId>
          <version>${spring.version}</version>
        </dependency>

        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-orm</artifactId>
          <version>${spring.version}</version>
        </dependency>

        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-aspects</artifactId>
          <version>${spring.version}</version>
        </dependency>

        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-test</artifactId>
          <version>${spring.version}</version>
        </dependency>

        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-jdbc</artifactId>
          <version>${spring.version}</version>
        </dependency>

        <!-- Spring & Mybatis -->
        <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis</artifactId>
          <version>3.3.1</version>
        </dependency>
        <dependency>
          <groupId>org.mybatis</groupId>
          <artifactId>mybatis-spring</artifactId>
          <version>1.2.4</version>
        </dependency>

        <!-- MySQL数据库驱动 -->
        <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <version>5.1.38</version>
        </dependency>

        <!-- 日志 -->
        <dependency>
          <groupId>log4j</groupId>
          <artifactId>log4j</artifactId>
          <version>1.2.17</version>
        </dependency>

        <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-api</artifactId>
          <version>1.7.18</version>
        </dependency>

        <!-- 数据源 -->
        <dependency>
          <groupId>commons-dbcp</groupId>
          <artifactId>commons-dbcp</artifactId>
          <version>1.4</version>
        </dependency>

        <!-- JSP tag -->
        <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>jstl</artifactId>
          <version>1.2</version>
        </dependency>
        <dependency>
          <groupId>taglibs</groupId>
          <artifactId>standard</artifactId>
          <version>1.1.2</version>
        </dependency>
      </dependencies>

      <build>
        <!-- 覆写maven的默认设置以便源文件中的mapper文件可以被打包 -->
        <!-- 打包时的文件名 -->
        <finalName>spring-mybatis</finalName>
        <!-- 自定义的一个占位符,用project.build.directory引用 -->
        <directory>${project.basedir}/target</directory>
        <!-- 源码位置,相当于在IDE中mark as source root -->
        <sourceDirectory>${project.basedir}/src/main/java</sourceDirectory>
        <!-- 输出编译文件位置 -->
        <outputDirectory>${project.build.directory}/classes</outputDirectory>
        <!-- 配置文件位置,指定这个位置在打包时会从这里copy配置文件 -->
        <!-- 如果覆写了super pom中的某个节点,需要补全整个节点的内容
             下面配置为了能够打包源文件中的xml文件,
             必须重新添加下src/main/resources为resources路径 -->
        <resources>
          <resource>
            <filtering>false</filtering>
            <directory>${project.basedir}/src/main/java</directory>
            <excludes>
              <exclude>**/*.java</exclude>
            </excludes>
          </resource>
          <!-- 这个几点的添加是必要的 -->
          <resource>
            <directory>${project.basedir}/src/main/resources</directory>
          </resource>
        </resources>
      </build>
    </project>

<h4 id="2.2">web.xml</h4>

    <?xml version="1.0" encoding="utf-8" ?>
    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
             id="WebApp_ID" version="3.0">
        <display-name>spring-mybatis</display-name>

        <!-- 加载spring容器 -->
        <context-param>
            <param-name>contextConfigLocation</param-name>
            <!--<param-value>WEB-INF/classes/spring/applicationContext-*.xml</param-value>-->
            <param-value>classpath:spring/applicationContext-*.xml</param-value>
        </context-param>
        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>


        <!-- springmvc 前端控制器  -->
        <servlet>
            <servlet-name>springmvc</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <!-- contextConfigLocation配置springmvc加载的配置文件(配置处理器映射器、适配器等等)
              若不配置，默认加载WEB-INF/servlet名称-servlet(springmvc-servlet.xml)
            -->
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>classpath:spring/springmvc.xml</param-value>
            </init-param>
        </servlet>

        <servlet-mapping>
            <servlet-name>springmvc</servlet-name>
            <!--
            第一种:*.action,访问以.action三结尾，由DispatcherServlet进行解析
            第二种:/,所有访问的地址由DispatcherServlet进行解析，对静态文件的解析需要配置不让DispatcherServlet进行解析，
                    使用此种方式和实现RESTful风格的url
            第三种:/*,这样配置不对，使用这种配置，最终要转发到一个jsp页面时，仍然会由DispatcherServlet解析jsp地址，
                    不能根据jsp页面找到handler，会报错
            -->
            <!--<url-pattern>*.action</url-pattern>-->
            <url-pattern>/</url-pattern>
        </servlet-mapping>


        <welcome-file-list>
            <welcome-file>index.html</welcome-file>
            <welcome-file>index.htm</welcome-file>
            <welcome-file>index.jsp</welcome-file>
            <welcome-file>default.html</welcome-file>
            <welcome-file>default.htm</welcome-file>
            <welcome-file>default.jsp</welcome-file>
        </welcome-file-list>
    </web-app>


<h4 id="2.3">springmvc.xml</h4>

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:mvc="http://www.springframework.org/schema/mvc"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">

        <!-- 对于注解的Handler 可以单个配置
         实际开发中加你使用组件扫描
         -->
        <!-- 可以扫描controller、service、...
        这里让扫描controller，指定controller的包
         -->
        <context:component-scan base-package="com.fmz.learn.springmybatis.controller"></context:component-scan>


        <!-- 使用mvc:annotation-driven代替上面两个注解映射器和注解适配的配置
         mvc:annotation-driven默认加载很多的参数绑定方法，
         比如json转换解析器默认加载了，如果使用mvc:annotation-driven则不用配置上面的RequestMappingHandlerMapping和RequestMappingHandlerAdapter
         实际开发时使用mvc:annotation-driven
         -->
        <mvc:annotation-driven></mvc:annotation-driven>

        <!-- 视图解析器
        解析jsp,默认使用jstl,classpath下要有jstl的包
        -->
        <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
            <!-- 配置jsp路径的前缀 -->
            <property name="prefix" value="/WEB-INF/jsp/"/>
            <!-- 配置jsp路径的后缀 -->
            <property name="suffix" value=".jsp"/>
        </bean>

    </beans>

<h4 id="2.4">applicationContext-dao.xml</h4>

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">

        <!-- 加载db.properties文件中的内容，db.properties文件中key命名要有一定的特殊规则 -->
        <context:property-placeholder location="classpath:props/db.properties" />

        <!-- 配置数据源 ，dbcp -->
        <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
            <property name="driverClassName" value="${jdbc.driver}"/>
            <property name="url" value="${jdbc.url}"/>
            <property name="username" value="${jdbc.username}" />
            <property name="password" value="${jdbc.password}" />
            <property name="maxActive" value="30" />
            <property name="maxIdle" value="5" />
        </bean>

        <!-- 从整合包里找，org.mybatis:mybatis-spring:1.2.4 -->
        <!-- sqlSessionFactory -->
        <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
            <!-- 数据库连接池 -->
            <property name="dataSource" ref="dataSource" />
            <!-- 加载mybatis的全局配置文件 -->
            <property name="configLocation" value="classpath:mybatis/mybatis-conf.xml" />
            <!-- 如果mapper.xml和mapper.java在同一个目录下，不需要指定mapper的地址
             如果不在同一个目录下，需要指定在classpath的那个目录下扫描mapper.xml文件
             例如将mapper.xml放在classpath:mapper/*.xml时，需要这样指定
             注意mapper.xml文件不要和其他xml配置文件放在同一个目录下，配置的是整个扫描目录，如果有
             其他配置文件会解析异常
             -->
            <!-- <property name="mapperLocations" value="classpath:mapper/*.xml" />-->
        </bean>
        <!-- mapper扫描器 -->
        <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
            <!-- 扫描包路径，如果需要扫描多个包，中间使用半角逗号隔开 -->
            <property name="basePackage" value="com.fmz.learn.springmybatis.mapper"/>
            <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
            <!-- <property name="sqlSessionFactory" ref="sqlSessionFactory" />
            会导致数据源配置不管用，数据库连接不上。
            且spring 4弃用
            -->
        </bean>

    </beans>

<h4 id="2.5">applicationContext-service.xml</h4>

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd">

        <!-- 商品管理的service -->
        <bean id="itemsService" class="com.fmz.learn.springmybatis.service.impl.ItemsServiceImpl"/>

    </beans>

<h4 id="2.6">applicationContext-transaction.xml</h4>

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:tx="http://www.springframework.org/schema/tx"
           xmlns:aop="http://www.springframework.org/schema/aop"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
            http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">


        <!-- 事务管理器
                对mybatis操作数据库事务控制，spring使用jdbc的事务控制类
            -->
        <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
            <!-- 数据源
            dataSource在applicationContext-dao.xml中配置了
             -->
            <property name="dataSource" ref="dataSource"/>
        </bean>

        <!-- 通知 -->
        <tx:advice id="txAdvice" transaction-manager="transactionManager">
            <tx:attributes>
                <!-- 传播行为 -->
                <tx:method name="save*" propagation="REQUIRED"/>
                <tx:method name="delete*" propagation="REQUIRED"/>
                <tx:method name="insert*" propagation="REQUIRED"/>
                <tx:method name="update*" propagation="REQUIRED"/>
                <tx:method name="find*" propagation="SUPPORTS" read-only="true"/>
                <tx:method name="get*" propagation="SUPPORTS" read-only="true"/>
                <tx:method name="select*" propagation="SUPPORTS" read-only="true"/>
            </tx:attributes>
        </tx:advice>
        <!-- aop -->
        <aop:config>
            <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.iot.learnssm.firstssm.service.impl.*.*(..))"/>
        </aop:config>
    </beans>

<h4 id="2.7">mybatis-config.xml</h4>

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>

        <!-- 全局setting设置，根据需要添加 -->
        <!--<settings>
            <setting name="logImpl" value="LOG4J"/>
        </settings>-->

        <!-- 配置别名 -->
        <typeAliases>
            <!-- 批量扫描别名 -->
            <package name="com.fmz.learn.springmybatis.entity" />
        </typeAliases>

        <!-- 配置mapper
            由于使用spring和mybatis的整合包进行mapper扫描，这里不需要配置了。
            必须遵循：mapper.xml和mapper.java文件同名且在一个目录
            如果不在一个目录下，需要在applicationContext-dao中mapper文件扫描中
            配置mapperLocation
        -->
        <!--
        <mappers>
            ...
        </mappers>
        -->
    </configuration>

<h4 id="2.8">db.properties</h4>

    jdbc.driver=com.mysql.jdbc.Driver
    jdbc.url=jdbc:mysql://xx.xx.193.14:3306/mybatis?characterEncoding=utf-8
    jdbc.username=root
    jdbc.password=root

<h4 id="2.9">lo4j.properties</h4>

    # Global logging configuration
    log4j.rootLogger=DEBUG, stdout
    # Console output...
    log4j.appender.stdout=org.apache.log4j.ConsoleAppender
    log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
    log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n

---

<h3 id="3">各个层次代码</h3>

<h4 id="3.1">Controller层</h4>

    package com.fmz.learn.springmybatis.controller;

    import com.fmz.learn.springmybatis.entity.ItemsCustom;
    import com.fmz.learn.springmybatis.service.ItemsService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.servlet.ModelAndView;

    import java.util.List;

    /**
     * Created by fmz on 2018/09/21.
     */

    //使用@Controller来标识它是一个控制器
    @Controller
    //为了对url进行分类管理 ，可以在这里定义根路径，最终访问url是根路径+子路径
    //比如：商品列表：/items/queryItems.action
    @RequestMapping("/items")
    public class ItemsController {

        @Autowired
        private ItemsService itemsService;

        //商品查询列表
        @RequestMapping("/queryItems")
        //实现 对queryItems方法和url进行映射，一个方法对应一个url
        //一般建议将url和方法写成一样
        public ModelAndView queryItems() throws Exception{
            //调用service查找数据库，查询商品列表
            List<ItemsCustom> itemsList = itemsService.findItemsList(null);

            //返回ModelAndView
            ModelAndView modelAndView = new ModelAndView();
            //相当于request的setAttribute方法,在jsp页面中通过itemsList取数据
            modelAndView.addObject("itemsList",itemsList);

            //指定视图
            //下边的路径，如果在视图解析器中配置jsp的路径前缀和后缀，修改为items/itemsList
            //modelAndView.setViewName("/WEB-INF/jsp/items/itemsList.jsp");
            //下边的路径配置就可以不在程序中指定jsp路径的前缀和后缀
            modelAndView.setViewName("items/itemsList");

            return modelAndView;
        }


    }

> `ItemsController.java`

<h4 id="3.2">Service层</h4>

    package com.fmz.learn.springmybatis.service.impl;

    import com.fmz.learn.springmybatis.entity.ItemsCustom;
    import com.fmz.learn.springmybatis.entity.ItemsQueryVo;
    import com.fmz.learn.springmybatis.mapper.ItemsMapperCustom;
    import com.fmz.learn.springmybatis.service.ItemsService;
    import org.springframework.beans.factory.annotation.Autowired;

    import java.util.List;

    public class ItemsServiceImpl implements ItemsService{

        @Autowired
        private ItemsMapperCustom itemsMapperCustom;

        @Override
        public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception {
            return itemsMapperCustom.findItemsList(itemsQueryVo);
        }
    }

> `ItemsServiceImpl.java`

<h4 id="3.2">Dao层</h4>

    package com.fmz.learn.springmybatis.mapper;

    import com.fmz.learn.springmybatis.entity.ItemsCustom;
    import com.fmz.learn.springmybatis.entity.ItemsQueryVo;

    import java.util.List;

    public interface ItemsMapperCustom {

        List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception;
    }

> `ItemsMapperCustom.java`

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
    <mapper namespace="com.fmz.learn.springmybatis.mapper.ItemsMapperCustom" >

        <!-- 定义商品查询的sql片段，就是商品查询条件 -->
        <sql id="query_items_where">
            <!-- 使用动态sql，通过if判断，满足条件进行sql拼接 -->
            <!-- 商品查询条件通过ItemsQueryVo包装对象 中itemsCustom属性传递 -->
            <if test="itemsCustom!=null">
                <if test="itemsCustom.name!=null and itemsCustom.name!=''">
                    items.name LIKE '%${itemsCustom.name}%'
                </if>
            </if>

        </sql>

        <!-- 商品列表查询 -->
        <!-- parameterType传入包装对象(包装了查询条件)
            resultType建议使用扩展对象
         -->
        <select id="findItemsList" parameterType="com.fmz.learn.springmybatis.entity.ItemsQueryVo"
                resultType="com.fmz.learn.springmybatis.entity.ItemsCustom">
            SELECT items.* FROM items
            <where>
                <include refid="query_items_where"></include>
            </where>
        </select>

    </mapper>

> `ItemsMapperCustom.xml`

<h4 id="3.4">POJO</h4>

    package com.fmz.learn.springmybatis.entity;

    import java.util.Date;

    public class Items {

        private Integer id;
        private String name;
        private Float price;
        private String pic;
        private Date createtime;
        private String detail;

        @Override
        public String toString() {
            return "Items{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", pic='" + pic + '\'' +
                    ", createtime=" + createtime +
                    ", detail='" + detail + '\'' +
                    '}';
        }
    }

> `Items.java`

    package com.fmz.learn.springmybatis.entity;

    public class ItemsCustom extends Items{
        //添加商品的扩展属性
    }

> `ItemsCustom.java`

    package com.fmz.learn.springmybatis.entity;

    public class ItemsQueryVo {

        private Items items;
        private ItemsCustom itemsCustom;

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }

        public ItemsCustom getItemsCustom() {
            return itemsCustom;
        }

        public void setItemsCustom(ItemsCustom itemsCustom) {
            this.itemsCustom = itemsCustom;
        }

        @Override
        public String toString() {
            return "ItemsQueryVo{" +
                    "items=" + items +
                    ", itemsCustom=" + itemsCustom +
                    '}';
        }
    }

> `ItemsQueryVo.java`

<h4 id="3.5">JSP文件</h4>

    <%@page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>查询商品列表</title>
    </head>
    <body>
    <form action="${pageContext.request.contextPath }/item/queryItem.action" method="post">
        查询条件：
        <table width="100%" border=1>
            <tr>
                <td><input type="submit" value="查询"/></td>
            </tr>
        </table>
        商品列表：
        <table width="100%" border=1>
            <tr>
                <td>商品名称</td>
                <td>商品价格</td>
                <td>生产日期</td>
                <td>商品描述</td>
                <td>操作</td>
            </tr>
            <c:forEach items="${itemsList }" var="item">
                <tr>
                    <td>${item.name }</td>
                    <td>${item.price }</td>
                    <td><fmt:formatDate value="${item.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${item.detail }</td>

                    <td><a href="${pageContext.request.contextPath }/item/editItem.action?id=${item.id}">修改</a></td>

                </tr>
            </c:forEach>

        </table>
    </form>
    </body>

    </html>

> `itemsList.jsp`

<h3 id="4">项目结构图</h3>

![项目结构图](/img/posts/spring-mybatis-basic-config-project-structure.png)
