---
layout: post
title: 'Spring MVC项目配置总结'
subtitle: 'Spring作为一个bean管理框架，提供了很多的模块(Module)和架构的实现。Spring Web是Spring和Web技术相关的一个模块，而Spring Web MVC是MVC架构的一个Web实现，建立在Spring Web Module之上。本文总结Spring Web MVC项目的基本配置(注解和非注解)，以备参考之用。'
background: '/img/posts/spring-mvc-config-summary.jpg'
comment: true
---

# 目录

- [1. Spring MVC的总体工作流程及必要配置](#1)
- [2. 非注解相关配置](#2)
- [3. 注解相关配置](#3)

---

<h3 id="1">Spring MVC的总体工作流程及必要配置</h3>

**Spring MVC的总体工作流程：**

- 所有的请求先到达前端控制器(`DispatcherServlet`);
- 前端控制器根据配置的一系列控制器映射器(`HandlerMapping`)和Bean配置得到Handler(`Controller`);
- 前端控制器根据配置的控制器适配器(`HandlerAdapter`)执行相应的Handler(`Controller`)得到`ModelAndView`;
- 前端控制器根据配置的视图解析器(`ViewResolver`)将`ModelAndView`进行解析为真正的视图(`jsp`);
- 前端控制器进行视图渲染(即将模型数据(在`ModelAndView`中)填充到request域中).

**必要配置：**

- `web.xml`：配置前端控制器Servlet(`DispatcherServlet`)
    - 配置`<init-param>`指定spring-mvc bean配置文件(`springmvc.xml`)的位置
- `springmvc.xml`：配置相应的控制器映射器、控制器适配器和视图解析器
    - 配置控制器映射器(`HandlerMapping`)
    - 配置控制器适配器(`HandlerAdapter`)
    - 配置视图解析器(`ViewResolver`)

---

<h3 id="2">非注解相关配置</h3>

用IDEA先建一个简单的Spring MVC Web Maven项目:

> pom.xml:

    <!-- servlet -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.0.1</version>
    </dependency>

    <!-- 在jsp中使用jstl标签是引用 -->
    <!-- jstl -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>

    <!-- 无需显式引用spring web依赖 -->
    <!-- spring-mvc -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>4.2.4.RELEASE</version>
    </dependency>

    <!-- 使用log4j最为日志输出 -->
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>

> web.xml：

    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns="http://java.sun.com/xml/ns/javaee"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                                 http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" version="3.0">


      <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- contextConfigLocation配置springmvc加载的配置文件(配置处理器映射器、适配器等等)
          若不配置，默认加载WEB-INF/servlet名称-servlet(springmvc-servlet.xml)
        -->
        <init-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>classpath:springmvc.xml</param-value>
        </init-param>
      </servlet>
      
      <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <!--
        第一种:*.action,访问以.action结尾，由DispatcherServlet进行解析
        第二种:/,所有访问的地址由DispatcherServlet进行解析，对静态文件的解析需要配置不让DispatcherServlet进行解析，
                使用此种方式和实现RESTful风格的url
        第三种:/*,这样配置不对，使用这种配置，最终要转发到一个jsp页面时，仍然会由DispatcherServlet解析jsp地址，
                不能根据jsp页面找到handler，会报错
        -->
        <!--<url-pattern>*.action</url-pattern>-->
        <url-pattern>/</url-pattern>
      </servlet-mapping>
    </web-app>

> log4j.properties:

    # Root logger option
    log4j.rootLogger=DEBUG, stdout, file

    # Redirect log messages to console
    log4j.appender.stdout=org.apache.log4j.ConsoleAppender
    log4j.appender.stdout.Target=System.out
    log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
    log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

    # Redirect log messages to a log file
    log4j.appender.file=org.apache.log4j.RollingFileAppender
    #outputs to Tomcat home
    log4j.appender.file.File=${catalina.home}/logs/springmvc-demo.log
    log4j.appender.file.MaxFileSize=5MB
    log4j.appender.file.MaxBackupIndex=10
    log4j.appender.file.layout=org.apache.log4j.PatternLayout
    log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

> pojo:

    package com.fmz.learn.springmvcdemo.entity;

    import java.util.Date;

    public class Items {

        private Integer id;
        private String name;
        private Float price;
        private String pic;
        private Date createtime;
        private String detail;

        ...
    }

> 前端jsp视图(`$CLASSPATH:/WEB-INF/jsp/items/itemsList.jsp`)：

    <%@ page pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

**springmvc.xml配置：**

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
                               http://www.springframework.org/schema/beans/spring-beans.xsd
                               http://www.springframework.org/schema/context
                               http://www.springframework.org/schema/context/spring-context.xsd">

        <!-- 处理器映射器(HandlerMapping)
        将bean的name作为url进行查找，需要在配置Handler时指定beanname(就是url) -->
        <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping" />

        <!-- 处理器映射器(HandlerMapping)
            简单的URL映射 -->
        <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
            <property name="mappings">
                <props>
                    <prop key="/queryItems1.action">itemsController</prop>
                    <prop key="/queryItems2.action">itemsController2</prop>
                </props>
            </property>
        </bean>

        <!-- 处理器适配器(HandlerAdapter)
            所有的处理器适配器都实现了HandlerAdapter接口 -->
        <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter" />

        <!-- 处理器适配器(HandlerAdapter)
            另一个非注解的适配器 -->
        <bean class="org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter" />

        <!-- 视图解析器
            解析JSP，默认使用jstl,classpath下要有jstl的包 -->
        <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
            <!-- 通过配置前缀和后缀路径，可以在程序中不指定前缀和后缀
                下边的路径，如果在视图解析器中配置jsp的路径前缀和后缀，修改为items/itemsList
                modelAndView.setViewName("/WEB-INF/jsp/items/itemsList.jsp") -->
            <!-- 配置jsp前缀路径 -->
            <property name="prefix" value="/WEB-INF/jsp/" />
            <!-- 配置jsp后缀路径 -->
            <property name="suffix" value=".jsp" />
        </bean>

        <!-- 配置Handler -->
        <!-- 这两个bean也可以通过开启component-scan和@Controller注解的方法配置 -->
        <bean id="itemsController" class="com.fmz.learn.springmvcdemo.controller.ItemsController" />
        <bean id="itemsController2" class="com.fmz.learn.springmvcdemo.controller.ItemsController2" />
        <!-- 这个bean也可以通过开启component-scan和@Controller("/xxx")的方法配置 -->
        <bean name="/queryItems.action" class="com.fmz.learn.springmvcdemo.controller.ItemsController" />

        <!-- 可以扫描controller、service、...
            这里让扫描controller，指定controller的包 -->
        <!--
        <context:component-scan base-package="com.fmz.learn.springmvcdemo"></context:component-scan> 
        -->
    </beans>

> 能够被处理器适配器`HttpRequestHandlerAdapter`调用的`Controller`:

    package com.fmz.learn.springmvcdemo.controller;

    import com.fmz.learn.springmvcdemo.entity.Items;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.HttpRequestHandler;

    import javax.servlet.ServletException;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    //如果使用注解的方式(需要指定URL(/xxx)模式的名称)，需要开启component-scan,在xml文件中就不用配置了
    //@Controller("/queryItems0.action")
    public class ItemsController implements HttpRequestHandler{

        @Override
        public void handleRequest(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) throws ServletException, IOException {
            //调用service查找数据库，查询商品列表，这里使用静态数据模拟
            List<Items> itemsList = new ArrayList<Items>();

            //向list中填充静态数据
            Items items_1 = new Items();
            items_1.setName("联想笔记本");
            items_1.setPrice(6000f);
            items_1.setDetail("ThinkPad T430 联想笔记本电脑！");

            Items items_2 = new Items();
            items_2.setName("苹果手机");
            items_2.setPrice(5000f);
            items_2.setDetail("iphone6苹果手机！");

            itemsList.add(items_1);
            itemsList.add(items_2);

            //设置模型数据
            httpServletRequest.setAttribute("itemsList",itemsList);

            //设置转发的视图
            httpServletRequest.getRequestDispatcher("/WEB-INF/jsp/items/itemsList.jsp").forward(httpServletRequest,httpServletResponse);

        }
    }

> 能够被处理器适配器`SimpleControllerHandlerAdapter`调用的`Controller`:

    package com.fmz.learn.springmvcdemo.controller;

    import com.fmz.learn.springmvcdemo.entity.Items;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.Controller;

    import javax.servlet.ServletException;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    //如果使用注解的方式，需要开启component-scan,在xml文件中就不用配置了
    //@org.springframework.stereotype.Controller
    public class ItemsController2 implements Controller{

        @Override
        public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) throws ServletException, IOException {
            //调用service查找数据库，查询商品列表，这里使用静态数据模拟
            List<Items> itemsList = new ArrayList<Items>();

            //向list中填充静态数据
            Items items_1 = new Items();
            items_1.setName("联想笔记本");
            items_1.setPrice(6000f);
            items_1.setDetail("ThinkPad T430 联想笔记本电脑！");
            items_1.setCreatetime(new Date());

            Items items_2 = new Items();
            items_2.setName("苹果手机");
            items_2.setPrice(5000f);
            items_2.setDetail("iphone6苹果手机！");
            items_2.setCreatetime(new Date());

            itemsList.add(items_1);
            itemsList.add(items_2);

            //返回ModelAndView
            ModelAndView mv = new ModelAndView();
            //相当于request的setAttribute方法，在jsp页面中通过itemsList取数据
            mv.addObject("itemsList", itemsList);

            //指定视图
            mv.setViewName("items/itemsList");

            return mv;
        }
    }

---

<h3 id="3">注解相关配置</h3>

使用非注解的上述方式，只能通过一个RUL对应一个`Handler`类。使用注解的方法，可以将不同的RUL对应同一个`Controller`，使用注解`@RequestMapping(xxx)映射不同的URL`。

> 开启控制器映射器(`HandlerMapping`)的注解:

    <!-- 注解的映射器 -->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping" />

> 开启控制器适配器(`HandlerAdapter`)的注解:

    <!-- 注解的适配器 -->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter" />

上述通过Bean配置注解控制器映射器和控制器适配器可以通过下面的方法代替：

    <!-- 使用这种方式，需要增加xml schema信息 
    <beans xmlns:mvc="http://www.springframework.org/schema/mvc"
           xsi:schemaLocation="http://www.springframework.org/schema/mvc(增加)
                               http://www.springframework.org/schema/mvc/spring-mvc.xsd">
    -->
    <!-- 使用mvc:annotation-driven代替上面两个注解映射器和注解适配的配置
    mvc:annotation-driven默认加载很多的参数绑定方法，
    比如json转换解析器默认加载了，如果使用mvc:annotation-driven则不用配置上面的RequestMappingHandlerMapping和RequestMappingHandlerAdapter
    实际开发时使用mvc:annotation-driven
    -->

    <mvc:annotation-driven></mvc:annotation-driven>

使用注解(`@RequestMapping`)的方式，可以开启包扫描配置使用注解(`@Controller`)获取bean或者用配置文件定义bean的方式获取bean，但二者不能同时使用。

完整的通过注解来配置的`springmvc.xml`:

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:mvc="http://www.springframework.org/schema/mvc"
           xmlns:context="http://www.springframework.org/schema/context"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
                               http://www.springframework.org/schema/beans/spring-beans.xsd
                               http://www.springframework.org/schema/context
                               http://www.springframework.org/schema/context/spring-context.xsd
                               http://www.springframework.org/schema/mvc
                               http://www.springframework.org/schema/mvc/spring-mvc.xsd">


        <mvc:annotation-driven></mvc:annotation-driven>

        <context:component-scan base-package="com.fmz.learn.springmvcdemo"></context:component-scan>

        <!-- 或者将component-scan注释掉，取消注释 -->
        <!--
        <bean class="com.fmz.learn.springmvcdemo.controller.ItemsController3" />
        -->

        <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
            <property name="prefix" value="/WEB-INF/jsp/" />
            <property name="suffix" value=".jsp" />
        </bean>

    </beans>

> 通过注解(`@RequestMapping`)实现的`Controller`类：

    package com.fmz.learn.springmvcdemo.controller;

    import com.fmz.learn.springmvcdemo.entity.Items;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.servlet.ModelAndView;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    @Controller
    public class ItemsController3 {

        @RequestMapping("/queryItems")
        public ModelAndView queryItems() throws Exception {
            //调用service查找数据库，查询商品列表，这里使用静态数据模拟
            List<Items> itemsList = new ArrayList<Items>();

            //向list中填充静态数据
            Items items_1 = new Items();
            items_1.setName("联想笔记本");
            items_1.setPrice(6000f);
            items_1.setDetail("ThinkPad T430 联想笔记本电脑！");
            items_1.setCreatetime(new Date());

            Items items_2 = new Items();
            items_2.setName("苹果手机");
            items_2.setPrice(5000f);
            items_2.setDetail("iphone6苹果手机！");
            items_2.setCreatetime(new Date());

            itemsList.add(items_1);
            itemsList.add(items_2);

            ModelAndView mv = new ModelAndView();
            mv.addObject("itemsList", itemsList);

            mv.setViewName("items/itemsList");
            return mv;
        }
    }

---
