---
layout: post
title: '基本的Java Web Filter总结'
subtitle: '当Http请求到达Servlet(JSP)之前或者Servlet(JSP)处理请求完成之后，Filter可以拦截请求，并进行响应的preprocess或者postprocess。本文通过一个简单的登录应用，看一看Filter是如何工作的。'
background: '/img/posts/java-web-filter.jpg'
comment: true
---

# 目录

- [1. 建一个简单的Maven Java Web项目](#1)
- [2. 为Java Web增加登录(Login)和退出(Logout)Servlet](#2)
- [3. 使用Filter增强应用的功能](#3)
    - [3.1 Request设置字符编码Filter](#3.1)
    - [3.2 请求日志输出Filter](#3.2)
    - [3.3 用户认证(Authentication)Filter](#3.3)

---

<h3 id="1">建一个简单的Maven Java Web项目</h3>

使用IDEA工具建一个Webapp Maven项目：

> pom.xml

    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.0.1</version>
    </dependency>

> 说明：<br><br>
1). 使用Maven自带的archetype：`maven-archetype-webapp`;<br>
2). IDEA集成开发环境下载存在问题，需要增加参数：`{archetypeCatalog: internal}`

> 整个项目的目录结构：

![Java Filter Web App目录结构](/img/posts/java-web-filter-webapp-structure.jpg)

> web.xml:

    <!DOCTYPE web-app PUBLIC
     "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
     "http://java.sun.com/dtd/web-app_2_3.dtd" >

    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns="http://java.sun.com/xml/ns/javaee"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                                 http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" version="3.0">

        <!-- Servlet Context Name -->
        <display-name>servlet-filter</display-name>

        <welcome-file-list>
            <welcome-file>login.jsp</welcome-file>
            <welcome-file>login.html</welcome-file>
        </welcome-file-list>"
        
        ...

    </web-app>

---

<h3 id="2">为Java Web增加登录(Login)和退出(Logout)Servlet</h3>

> 前端登录页面：`$CLASSPATH:webapp/login.html`:

    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>登录页面</title>
    </head>
    <body>

    <form action="LoginServlet" method="post">

        Username: <input type="text" name="user">
        <br>
        Password: <input type="password" name="pwd">
        <br>
        <input type="submit" value="Login">
    </form>
    </body>
    </html>

> 登录后处理的Servlet(`$CLASSPATH:src/main/java/com.fmz.learn.servlet.LoginServlet.java`):

    package com.fmz.learn.servlet;

    import java.io.IOException;
    import java.io.PrintWriter;
    import javax.servlet.RequestDispatcher;
    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.*;

    /**
     * Servlet implementation class LoginServlet
     * 注解Servlet，不需要在web.xml中配置
     */
    @WebServlet("/LoginServlet")
    public class LoginServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;
        private final String userID = "admin";
        private final String password = "password";

        protected void doPost(HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {

            // get request parameters for userID and password
            String user = request.getParameter("user");
            String pwd = request.getParameter("pwd");

            if(userID.equals(user) && password.equals(pwd)){
                HttpSession session = request.getSession();
                session.setAttribute("user", "Pankaj");
                //setting session to expiry in 30 mins
                session.setMaxInactiveInterval(30*60);
                Cookie userName = new Cookie("user", user);
                userName.setMaxAge(30*60);
                response.addCookie(userName);
                //登录成功后跳转的JSP
                response.sendRedirect("LoginSuccess.jsp");
            }else{
                //RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
                response.setContentType("text/html; charset=UTF-8");
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                PrintWriter out= response.getWriter();
                out.println("<font color=red>Either user name or password is wrong.</font>");
                rd.include(request, response);
            }

        }
    }

> 登录成功后跳转的JSP(`$CLASSPATH:webapp/LoginSuccess.jsp`):

    <%@ page pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>登录成功页面</title>
        </head>

        <body>

            <%
                String user = (String)session.getAttribute("user");
                String userName = null;
                String sessionID = null;
                Cookie[] cookies = request.getCookies();
                if(cookies != null){
                    for(Cookie cookie : cookies) {
                        if(cookie.getName().equals("user")) {
                            userName = cookie.getValue();
                        }
                        if(cookie.getName().equals("JSESSIONID")){
                            sessionID = cookie.getValue();
                        }
                    }
                }

                String loginName = request.getParameter("user");
            %>

            <h3>这个是登录用户名(用来判断跳转是否是同一个Request):: <%= loginName%></h3>
            <h3>Hi <%=userName %>, Login successful. Your Session ID=<%=sessionID %></h3>
            <br>
            User=<%=user %>
            <br>
            <a href="CheckoutPage.jsp">Checkout Page</a>
            <form action="LogoutServlet" method="post">
            <input type="submit" value="Logout" >
            </form>
        </body>
    </html>

> 登录失败后跳转的JSP(`$CLASSPATH:webapp/login.jsp`):

    <%@page session="false" pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Login Success Page</title>
        </head>

        <body>

        <%
            String loginUser = request.getParameter("user");
        %>
        <h3>用户:: <%= loginUser%>登录失败！</h3>
        <form action="LoginServlet" method="POST">
            Username: <input type="text" name="user">
            <br>
            Password: <input type="password" name="pwd">
            <br>
            <input type="submit" value="Login">
        </form>
        </body>
    </html>

> 退出登录执行的Servlet(`$CLASSPATH:src/main/java/com.fmz.learn.servlet.LogoutServlet.java`)：

    package com.fmz.learn.servlet;

    import java.io.IOException;

    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.Cookie;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.servlet.http.HttpSession;

    /**
     * Servlet implementation class LogoutServlet
     */
    @WebServlet("/LogoutServlet")
    public class LogoutServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("JSESSIONID")){
                        System.out.println("JSESSIONID="+cookie.getValue());
                        break;
                    }
                }
            }
            //invalidate the session if exists
            HttpSession session = request.getSession(false);
            System.out.println("User="+session.getAttribute("user"));
            if(session != null){
                session.invalidate();
            }
            response.sendRedirect("login.html");
        }

    }

**JSP显示乱码：**

启动程序后，当输入中文用户名(`你好`)和密码(`d`)时，身份认证出现错误，会`INCLUDE`到`Login.jsp`，这时候中文显示是乱码的：

![jsp显示乱码](/img/posts/hello-decode-fault.png)

通过Chrome监控网络，得到的表单提交封装参数如下：

![encode后的表单数据](/img/posts/hello-username-form-data.png)

从图中可以看出来，JSP文件提交表单数据时，通过`UTF-8`的编码格式(两个汉字编码为三个字节)对`user=你好&pwd=d`进行编码。那么问题一定是出在了解码的时候，通过修改`LoginServlet.java`对`HttpServletRequest`对象设置encoding可以解决编码问题：

`request.setCharacterEncoding("UTF-8")`

或者从默认的encoding(ISO-8859-1)解码后的字符串中得到bytes，然后重新解码(不建议使用这种方法)：

`String user = new String(request.getParameter("user").getBytes("ISO-8859-1"), "UTF-8")`

> 设置编码说明：<br>
- 如果对JVM设置文件编码格式使用：`-Dfile.encoding=UTF-8`;
- 如果对`HTTP GET`请求的URL设置编码格式(tomcat server.xml设置)：`URIEncoding=UTF-8`;
- 如果`HTTP POST`请求的请求体设置编码格式(上述乱码情况)：`request.setCharacterEncoding("UTF-8")`.

---

<h3 id="3">使用Filter增强应用的功能</h3>

使用Filter可以在请求到达Servlet(JSP)之前或者请求被Servlet(JSP)处理完之后而响应未到达客户端之前对请求和响应做相应的处理。

<h4 id="3.1">Request设置字符编码Filter</h4>

> 可以在请求未到达Servlet时，设置Request的解码字符编码：

    package com.fmz.learn.filter;

    import javax.servlet.*;
    import javax.servlet.annotation.WebFilter;
    import java.io.IOException;

    /**
     * Servlet implementation class LoginServlet
     */
    @WebFilter(filterName="filter1")
    public class SetCharacterEncodingUTF8 implements Filter{

        private ServletContext servletContext;

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            this.servletContext = filterConfig.getServletContext();
            this.servletContext.log("SetCharacterEncodingUTF8 Filter初始化！");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            request.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }

> 在`web.xml`中定义`filter-mapping`:

    <filter-mapping>
        <filter-name>filter1</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

> 说明：<br><br>
1). 使用注解(`@WebFilter`)的方法设置该类为一个Filter类;<br>
2). 注解(`@WebFilter`)可以通过`@WebFilter(filterName="xxx")`自定义指定Filter的`filter-name`,默认为该类的名字;也可以通过`@WebFilter(filterName="xxx", urlPattern="xxx")`同时指定`url-pattern`;<br>
3). filter的执行顺序是按照`web.xml`中`<filter-mapping>`定义的先后顺序进行的.如果用了注解的方法,没有在`web.xml`中定义`filter-mapping`,则会按照filter类名的字典顺序执行.

<h4 id="3.2">请求日志输出Filter</h4>

> 日志输出的Filter类：

    package com.fmz.learn.filter;

    import javax.servlet.*;
    import javax.servlet.annotation.WebFilter;
    import javax.servlet.http.Cookie;
    import javax.servlet.http.HttpServletRequest;
    import java.io.IOException;
    import java.util.Enumeration;

    @WebFilter(filterName="filter2")
    public class RequestLoggingFilter implements Filter {

        private ServletContext servletContext;

        @Override
        public void init(FilterConfig filterConfig) throws ServletException{
            this.servletContext = filterConfig.getServletContext();
            this.servletContext.log("RequestLoggingFilter初始化！");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException{
            HttpServletRequest req = (HttpServletRequest)request;
            Enumeration<String> params = req.getParameterNames();
            while(params.hasMoreElements()){
                String name = params.nextElement();
                String value = req.getParameter(name);
                this.servletContext.log(req.getRemoteAddr() +
                        "::Request Params::{" + name + "=" + value + "}");
            }
            Cookie[] cookies = req.getCookies();
            if(cookies != null){
                for(Cookie cookie : cookies){
                    this.servletContext.log(req.getRemoteAddr() +
                            "::Cookie::{" + cookie.getName() + "," + cookie.getValue() + "}");
                }
            }
            chain.doFilter(request, response);
        }

        @Override
        public void destroy(){

        }

    }

> 在`web.xml`中定义`filter-mapping`:

    <filter-mapping>
        <filter-name>filter2</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

**在request.getParameter()之后进行request.setCharacterEncoding()就不起作用了：**

在`RequestLoggingFilter.java`中有取得`HttpServletRequest`的参数信息，如果`filter2`是先于`filter1`执行的，则`filter1`中`request.setCharacterEncoding("UTF-8")`就会失去作用。

因此：`request.setCharacterEncoding()`需要在任意取得参数(`request.getParameter()`)的取参语句之前执行。

<h4 id="3.3">用户认证(Authentication)Filter</h4>

> `HttpSession`认证filter：

    package com.fmz.learn.filter;

    import javax.servlet.*;
    import javax.servlet.annotation.WebFilter;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.servlet.http.HttpSession;
    import java.io.IOException;

    @WebFilter(filterName="filter3")
    public class AuthenticationFilter implements Filter{

        private ServletContext servletContext;

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            this.servletContext = filterConfig.getServletContext();
            this.servletContext.log("Authentication初始化！");
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            HttpServletResponse response = (HttpServletResponse)servletResponse;

            String URI = request.getRequestURI();
            this.servletContext.log("Request Source::"  + URI);

            HttpSession session = request.getSession(false);
            if(session == null && !(URI.endsWith("html") ||
                URI.endsWith("LoginServlet"))){
                this.servletContext.log("未经认证的访问！");
                response.sendRedirect("login.html");
            }else{
                filterChain.doFilter(request, response);
                System.out.println("AuthenticationFilter执行完毕！");
            }
        }

        @Override
        public void destroy() {

        }
    }

> 说明：<br><br>
1). 认证的逻辑是：如果session是null且不是获取静态资源(`*.html`)且请求路径不是(`*/LoginServlet`)，直接跳转到登录页(login.html);反之，交给`LoginServlet`进行用户名密码认证;<br>
2). Servlet默认情况下不会创建`HttpServletSession`，只能通过`request.getSession()`获取，如果Session不存在就创建一个。如果不想自动创建则使用：`request.getSession(false)`;<br>
3). JSP页面默认会创建`HttpServletSession`,如果要禁用自动创建，则配置: `<@page session="false">`.

`LoginServlet`如果认证用户信息失败，则会跳转到`login.jsp`，如果不想让JSP文件自动创建Session，需要配置`<@page session="false">`

**RequestDispatcher#include(request, response)到login.jsp乱码问题：**

![include JSP页面乱码](/img/posts/include-jsp-decode-fault.png "include JSP页面乱码")

乱码问题的原因一定是：编码(encode)和解码(decode)所使用的字符编码不一致。

可以通过：`response.setContentType("text/html; charset=UTF-8")`设置解码格式。

---
