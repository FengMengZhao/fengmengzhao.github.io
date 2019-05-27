---
layout: post
title: '有后端认证的前后端分离项目跨域问题'
subtitle: '所谓的完全前后端分离是指前端和后端单独部署，前端提供静态页面服务，后端提供接口服务，二者不在同一个域下。这种前后端分离的方式在接口调用的时候会出现跨域问题，需要前端和后端配合处理才能够解决。本文提供了一个在后端服务有登录验证的情况下前端和后端处理方案，解决跨域问题。'
background: '/img/posts/front-rear-separation-CORS.jpg'
comment: true
---

# 目录

- [1. 前后端分离项目问题](#1)
- [2. Ajax跨域请求认证接口实例](#2)
    - [2.1 Ajax跨域请求非认证接口](#2.1)
    - [2.2 Ajax跨域请求认证接口](#2.2)
- [3. 完全前后端分离项目跨域解决方法](#3)

---

<h3 id="1">1. 前后端分离项目问题</h3>

前后端分离项目的前端和后端独立开发，前端负责数据的请求和展示，后端负责数据的接口开发，前端和后端代码不会混淆在一起。细分分为两种：部分前后端分离和完全前后端分离。部分前后端分离指的是前端页面和后端服务部署在同一个web服务器下，前端对后端接口的调用实际上是走的本地，也就是同域调用；而完全前后端分离指的是前端和后端独立部署各自的项目，接口的调用走的是网络，一般情况下是不同域的。

如果前端的项目是vue webpack项目，后端采用部分前后端分离的模式，对于非权限接口前后端联调，后端可以采用去掉登录认证并简单解决跨域来联调。但是对于权限数据后端只能登陆之后才能提供接口，而在后端有登录认证的情况下，简单的解决跨域并不能实现跨域联调，只能是前端文件打包后放入后端服务中，而这样前端就无法调试代码，前后端联调权限接口的效率就很低。

如果在后端接口有登录认证的情况下，采用完全的前后端分离模式，则前后端联调就会顺畅很多，并且前后端部署也增加了灵活性。

同域情况下的部分前后端项目比较简单，后端完成登录认证之后，前端对后端请求时会带上认证信息(`cookie`)，接口数据也就能正常返回；跨域情况下的完全前后端分离项目在接口请求时就会出现跨域问题，其本质是Ajax跨域请求了其他域的接口，造成接口接口请求失败。

接下来通过一个简单的Ajax示例，分别展示后端在没有登录认证和有登录认证的情况下Ajax对跨域接口的调用及解决办法，总结出在有后端有登录认证的情况下完全前后端分离项目跨域接口调用的解决方案。

---

<h3 id="2">2. Ajax跨域请求认证接口实例</h3>

由于浏览器的同源策略(SOP, Same Origin Policy)，正常一个源内Ajax是不能请求其他源的资源的。这种限制是出于安全考虑，但是也带来了一定的限制性，因此前后端也提供了解决跨域请求的办法。跨域的问题是需要前后端一起解决的，下面分别展示后端有登录认证和没有登录认证的情况下，Ajax调用跨域接口的问题。

<h4 id="2.1">2.1 Ajax跨域请求非认证接口</h4>

前端代码如下：

    <!DOCTYPE html>
    <html>
    <head>
    <script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.0.min.js"></script>
    <script>
    $(document).ready(function(){
      $("button").click(function(){
        $.ajax({
            url: "http://ip:port/path", <!-- 某个接口地址 -->
            type: "GET",
            success: function(data){
                $("#div1").html(JSON.stringify(data));
        }});
      });
    });
    </script>
    </head>
    <body>

    <div id="div1"><h2>Let jQuery AJAX Change This Text</h2></div>

    <button>Get External Content</button>

    </body>
    </html>

如果后端没有解决跨域问题，这时候浏览器会报错：

![front-rear-separation-CORS-error.png](/img/posts/front-rear-separation-CORS-error.png "跨域请求失败")

后端可以这样解决跨域：

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
    }

> 实现接口`org.springframework.web.servlet.config.annotation.WebMvcConfigurer`，Override方法`addCorsMappings`。

<h4 id="2.2">2.2 Ajax跨域请求认证接口</h4>

如果对上述的接口加入登录验证进行请求，请求还会报同样的错，因为没有登录信息，接口就302跳转了。

我们的目的是，如果当前浏览器中有登录认证(cookie)信息，Ajax在跨域请求是能够带上目标域的cookie信息，这样就能正常访问接口了。修改前端的请求：

    <!DOCTYPE html>
    <html>
    <head>
    <script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.0.min.js"></script>
    <script>
    $(document).ready(function(){
      $("button").click(function(){
        $.ajax({
            url: "http://ip:port/path", 
            type: "GET",
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function(data){
                $("#div1").html(JSON.stringify(data));
        }});
      });
    });
    </script>
    </head>
    <body>

    <div id="div1"><h2>Let jQuery AJAX Change This Text</h2></div>

    <button>Get External Content</button>

    </body>
    </html>

后端先在同一个浏览器会话中登录，然后再请求接口，就直接能够返回数据了，通过header也可以发现，Ajax请求的信息中携带了目标域的cookie信息。

---

<h3 id="3">3. 完全前后端分离项目跨域解决方法</h3>

通过【2.2】部分的验证，我们可以有下面的思路：

1. 访问前端入口
2. 前端异步请求后端接口
3. 如果后端发现前端没有登录并且请求是Ajax(XMLHttpRequest)请求，返回前端`401`状态码，
4. 前端拦击`401`状态码，统一跳转到后端进行登录认证
5. 登录认证完成后，后端重定向到前端入口
6. 前端携带后端的cookie信息异步请求后端接口
7. 接口请求成功，前后端交互正常

前端需要做两件事：

- 在异步请求中带上跨域请求的信息
    - `withCredentials: true`: 异步请求携带目标域的cookie
    - `crossDomain: true`: 表明是跨域请求
    - `headers: {'X-Requested-With': 'XMLHttpRequest'},`: 表明是Ajax请求
- 拦截器，拦截`401`状态接口并重定向到后端登录页
    
不同的框架有不同的实现，这里展示一个Ajax处理`401`状态码的方法：

    $(document).ready(function(){
      $("button").click(function(){
        $.ajax({
            url: "http://ip:port/path", 
            type: "GET",
            headers: {'X-Requested-With': 'XMLHttpRequest'},
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            <!-- 处理401状态码 -->
            statusCode: {
                401: function(xhr){
                    window.location.href = 'http://ip:port/context';
                }
            },
            success: function(data){
                $("#div1").html(JSON.stringify(data));
        }});
      });
    });

后端需要允许跨域请求：

    @Configuration
    public class CorsConfig {

        private CorsConfiguration buildConfig() {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedOrigin("*");//为了安全可以只加入前端所在的域
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }

        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", buildConfig());
            return new CorsFilter(source);
        }
    }

---
