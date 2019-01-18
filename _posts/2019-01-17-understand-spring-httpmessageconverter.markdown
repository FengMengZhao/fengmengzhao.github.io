---
layout: post
title: '理解Spring HttpMessageConverter'
subtitle: 'Http Message Converter在Spring MVC中负责两件事：一是将前台传过来的数据表示反序列化为Java Object；二是将后台返回的Java Object序列化为JSON或者XML等数据表示。本文介绍Spring MVC Http Message Converter使用、自定义HTTPMessageConverter及JSON/XML不同类型数据表示的配置。'
background: '/img/posts/spring-httpmessageconverter.png'
comment: true
---

# 目录

- [1. Http Message Converter简介](#1)
- [2. Http Message Converter是怎么工作的？](#2)
- [3. 序列化过程@ResponseBody](#3)
    - [3.1 Spring Content Negotiation](#3.1)
    - [3.2 Spring Content Negotiation是怎么工作的？](#3.2)
    - [3.3 Spring Content Negotiation自定义配置](#3.3)
- [4. 反序列化过程@RequestMapping](#4)
- [5. 自定义HttpMessageConverter](#5)

---

<h3 id="1">Http Message Converter简介</h3>

Http Message Converter负责将Java Object序列化为JSON/XML数据表示和将JSON/XML数据表示反序列化为Java Object。

当我们在based XML中配置：`<mvc:annotation-driven />`或者在based Java中配置`@EnableWebMvc`时(二者是等价的)，`AnnotationDrivenBeanDefinitionParser`会注册一系列的conversion service、validators和message-converters。

如果在`<mvc:annotation-driven />`中没有自定义的`<message-converters>`标签，Spring默认会注册一系列的message converters：

- `ByteArrayHttpMessageConverter`
- `StringHttpMessageConverter`
- `ResourceHttpMessageConverter`
- `SourceHttpMessageConverter`
- `AllEncompassingFormHttpMessageConverter`

另外，如果有相应的lib包在CLASSPATH中检测到时候，Spring默认也会注册下面的message converters：

- `Jaxb2RootElementHttpMessageConverter` – Java Object <-> XML
- `MappingJackson2HttpMessageConverter` – JSON -> Java Object
- `MappingJacksonHttpMessageConverter` – JSON -> Java Object
- `AtomFeedHttpMessageConverter` – Atom feeds -> Java Object
- `RssChannelHttpMessageConverter` – RSS feeds -> Java Object

> `WebMvcConfigurationSupport.java`中对lib包的检测顺序进行了定义。

---

<h3 id="2">Http Message Converter是怎么工作的？</h3>

当服务器需要响应客户端的一个请求时，Spring会根据请求头(`Header`)的`Accept`参数值的媒体类型(media type)来决定要返回的数据类型。接着，Spring会尝试找到一个合适的注册converter来处理这种媒体类型，利用这个converter来进行类型转换并返回给客户端。

> Spring在`@ResponseBody`时，有content negotiation策略，策略当中的最后一个是根据请求Header中的`Accept`媒体类型来决定返回值类型。该策略是可配置的，下文有讲解。

当服务器接收到客户端的一个请求时，Spring会根据请求头(`Header`)的`Content-Type`参数值的媒体类型类决定请求消息体的数据类型，接着，Spring会尝试找到一个合适的注册converter来将消息体中的数据转化为Java Object。

一个完整的客户端请求到服务器端响应过程中，Http Message Converter参与的过程如下：

- 查看请求头的`Content-Type`(仅需要)
- 根据`Content-Type`的媒体类型找到合适的`HttpMessageConverter`(仅需要)
- 将请求数据反序列化为Java Object(仅需要)
- 获取请求参数和路径变量(Path Variable)
- 业务逻辑
- 判断`Accept` header(根据content negotiation策略，下文有讲解)
- 根据`Accept` header找到合适的`HttpMessageConverter`
- 返回响应到客户端

---

<h3 id="3">序列化过程@ResponseBody</h3>

<h4 id="3.1">Spring Content Negotiation</h4>

当客户端向服务器端请求数据，服务器端需要决定以何种形式将数据返回给客户端，这个决定的过程就叫做内容协商(`Content Negotiation`)。

服务器端决定以何种形式来返回数据依赖内容协商策略(`ContentNegotiationStrategy`)，Spring有默认的策略，也可以通过配置自定义。

<h4 id="3.2">Spring Content Negotiation是怎么工作的？</h4>

正常HTTP协议的工作方式是通过`Accept` header来决定返回数据的类型，但是这种方式太过于依赖客户端的请求参数(`Accept` header)，有些时候用起来不太方便，所以Spring默认定义了一些列的内容协商策略(包括原生HTTP `Accept` header的方式)。

Spring定义了默认的内容协商策略：

- 第一是URL中路径的后缀(`Path Extension Strategy`)。如果后缀是`.html`，则返回HTML格式数据；如果后缀是`.xls`，则返回Excel格式数据。默认这种方式是开启的。
- 第二是URL参数`format`(可以自定义)(`Parameter Strategy`)。例如：`http://myserver/myapp/accounts/list?format=xls`，Spring会根据format的定义来决定返回数据的格式。默认这种方式关闭的。
- 最后一个是`Accept` header(`Header Strategy`)。这个是真正HTTP工作的方式。默认这种方式是开启的。

这三种方式Spring会按照先后顺序来检查是否开启，如果开启了就使用该方式，不会继续往下检查。这些方式在类`ContentNegotiationConfigurer.java`中定义。

<h4 id="3.3">Spring Content Negotiation自定义配置</h4>

Java Config的方式：

    @Configuration
    @EnableWebMvc
    public class WebConfig extends WebMvcConfigurerAdapter {

        @Override
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            configurer.favorPathExtension(false)//默认后缀模式是开启的，自定义关闭
            .mediaType("xml", MediaType.APPLICATION_XML)//如果后缀是xml，返回媒体类型为application/xml
            .mediaType("json", MediaType.APPLICATION_JSON)//如果后缀是json，返回媒体类型为application/json
            .favorParameter(true)//默认是关闭的，自定义开启
            .defaultContentType(MediaType.APPLICATION_XML)//自定义默认返回媒体类型为application/xml
            .ignoreAcceptHeader(true)//关闭Header Strategy
        }
    }

[详细配置参考项目](https://github.com/FengMengZhao/spring-learn/tree/master/spring-message-body-conversion "Spring Http Message Converter Project")

---

<h3 id="4">反序列化过程@RequestMapping</h3>

`@RequestMapping`可以将不同形式的数据表示反序列化为Java Object，服务器端需要根据Request Header中的`Content-Type`来找到合适的`HttpMessageConverter`。

反序列化的过程不止可以反序列化为POJO，还可以是`Map`、`File`等其他数据支持。

---

<h3 id="5">自定义HttpMessageConverter</h3>

继承`AbstractHttpMessageConverter`，自定义`ReportConverter`：

    package com.fmz.learn.spring.utils;

    public class ReportConverter
            extends AbstractHttpMessageConverter<Report> {

        public ReportConverter() {
            super(new MediaType("text", "report"));
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return Report.class.isAssignableFrom(clazz);
        }

        @Override
        protected Report readInternal(Class<? extends Report> clazz, HttpInputMessage inputMessage)
                throws IOException, HttpMessageNotReadableException {
            String requestBody = toString(inputMessage.getBody());
            int i = requestBody.indexOf("\n");
            if (i == -1) {
                throw new HttpMessageNotReadableException("No first line found");
            }

            String reportName = requestBody.substring(0, i).trim();
            String content = requestBody.substring(i).trim();

            Report report = new Report();
            report.setReportName(reportName);
            report.setContent(content);
            return report;
        }

        @Override
        protected void writeInternal(Report report, HttpOutputMessage outputMessage)
                throws IOException, HttpMessageNotWritableException {
            try {
                OutputStream outputStream = outputMessage.getBody();
                String body = report.getReportName() + "\n" +
                        report.getContent();
                outputStream.write(body.getBytes());
                outputStream.close();
            } catch (Exception e) {
            }
        }

        private static String toString(InputStream inputStream) {
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            return scanner.useDelimiter("\\A").next();
        }
    }

配置自定义的`ReportConverter`：

    @Configuration
    @EnableWebMvc
    public class WebConfig extends WebMvcConfigurerAdapter {

        // 添加新的HTTPMessageConverter实现
        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(new ReportConverter());
        }
    }

[详细自定义Converter参考项目](https://github.com/FengMengZhao/spring-learn/tree/master/spring-message-body-conversion "Spring Http Message Converter Project")

---

**参考文章：**

- [http://www.jcombat.com/spring/understanding-http-message-converters-in-spring-framework](http://www.jcombat.com/spring/understanding-http-message-converters-in-spring-framework "Spring Http Message Converter")
- [https://spring.io/blog/2013/05/11/content-negotiation-using-spring-mvc](https://spring.io/blog/2013/05/11/content-negotiation-using-spring-mvc "Spring Content Negotiation")
- [https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/custom-http-message-converter.html](https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/custom-http-message-converter.html "自定义Converter")
