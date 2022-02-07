---
layout: post
title: 'bug现场谜之总不能告诉客户你要按F12(打开控制台)吧？（跨域详解）'
subtitle: '现场两套系统，集成同一个单点登录。其中一个系统跳转到另外一个系统时现场浏览器会刷新两次。奇怪的是打开F12，问题就不能复现。不敢让客户按F12打开控制台访问系统，还是看一看吧...'
background: '/img/posts/bug-scene-cros-origin.png'
comment: false
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 跨域详解](#3)
- [4. 总结](#4)
- [更新记录](#99)

---

<h3 id="1">1. bug现场情况</h3>

现场两套系统，集成同一个单点登录。其中一个系统跳转到另外一个系统时现场浏览器会刷新两次。

奇怪的是打开F12，问题就不能复现。

<h3 id="2">2. 尝试破案</h3>

禁止F12的`Network --> Disable cache`设置，发现前端`js`的请求是缓存的。

初步判断是前端`js`缓存，发送异步权限数据请求接口时没有权限（第一次请求），然后重定向单点登录服务获取`service ticket`，重新登录后，再次请求权限数据接口（第二次请求），页面成功展示。

第一次异步请求后，由于没有权限，`302`重定向访问单点登录服务。这里控制台会提示跨域请求，跨域在[跨域详解](#3)部分详细介绍。

前端明确说了，不是前端的问题，解决不了。在后台处理，后台是`springboot`项目，增加配置：

```shell
spring:
  resources:
    cache:
      cachecontrol:
        max-age: 0
```

前端文件不会缓存，问题解决。

> 笔者公司的前端就是硬气。

问题解决了，笔者对那个跨域的报错产生了兴趣。之前也看过不少跨域的文章，始终对跨域云里雾里，春节找出收藏的跨域文章，好好研读了一下，有所获，赶紧借此文分享出来。

<h3 id="3">3. 跨域详解</h3>

![](/img/posts/cros-application-client-cache-browser-refresh-two-times-server-side-cache-0.png)

![](/img/posts/cros-application-client-cache-browser-refresh-two-times.png)

![](/img/posts/cros-ajax-post-complex-request-all-success.png)

![](/img/posts/cros-ajax-post-complex-request-with-options-implementation-but-withou-allow-origin-in-options.png)

![](/img/posts/cros-ajax-post-complex-request-with-options-implementation-but-real-request-access-allow-none2.png)

![](/img/posts/cros-ajax-post-complex-request-with-options-implementation-but-real-request-access-allow-none1.png)

![](/img/posts/cros-ajax-post-simple-request-console-and-network-message.png)

![](/img/posts/cros-ajax-post-complex-request-network-traffic.png)

![](/img/posts/cros-ajax-post-complex-request-console-error.png)

![](/img/posts/same-origin-ajax-post-request.png)

> No ‘Access-Control-Allow-Origin’ header is present on the requested resource.

请求的跨域资源`response`的`header`中没有`Access-Control-Allow-Origin`信息。

> Cross-Origin Request Blocked: The Same Origin Policy disallows reading the remote resource at https://example.com/

浏览器同源策略禁止读取跨域资源。

> Access to fetch at ‘https://example.com’ from origin ‘http://localhost:3000’ has been blocked by CORS policy.

在域`http://localhost:3000`下访问域`https://example.com`资源被禁止。

**何谓同域（同源）？**

1. 相同协议（http、https）
2. 相同主机名/ip（127.0.0.1、localhost、google.com）
3. 相同端口（80、443、8080）

满足三个条件是同源，否则就是不同的域。

**何谓跨域（跨域怎么发生？）**

1989诞生的[World Wide Web](https://en.m.wikipedia.org/wiki/World_Wide_Web)最初的`html`只有纯文本，[1993年](http://1997.webhistory.org/www.lists/www-talk.1993q1/0182.html)引入`<img>`标签，在`html`渲染时允许加载图片资源，这样纯文本中就可以展示图片。像这样在`html`中允许加载子资源（`subresource`）的`tag`还有：

- `<ifame>`
- `<link>`
- `<form>`
- `<audio>`
- `<video>`
- `<script>`

> 诚如上面`html`标签英文字面含义，所谓的“子资源（`subresource`）”就是例如表单、文件、音视频、脚本等外部资源。

当一个域中包含有上面`tag`的`html`渲染时，就会加载`subresource`，当这个`subresource`和当前域不同源时，跨域请求就发生了。例如，在一个域中`xmlhttprequest`（`ajax`）请求另外一个域的接口时就是跨域请求。

> 世界上第一个[web页面](http://info.cern.ch/hypertext/WWW/TheProject.html)

**跨域有什么安全问题？**

一个域中加载另一个域的文件、音视频、脚本等`subresource`大部分情况不会产生什么安全问题，但是有一些情况如果不做限制，就存在安全隐患。

例如一个域中提供了基于`cookie/session`的发送邮件接口，该域允许任何域对该接口的跨域请求。那么恶意网站有可能在获取有效`cookie`后任意调用发送邮件接口攻击网站。

可能有同学疑问：发送邮件接口如果需要认证才能成功调用，别人没有认证信息如何能成功调用呢？

实际上用户在浏览器端完成登录后，用户信息就存储在浏览器端，这时打开恶意网站就有可能被恶意脚本携带用户信息完成攻击。

**何谓浏览器同源策略（`same-origin policy`）**

既然跨域请求有安全的问题，浏览器端就做了相关限制，称之为“浏览器同源策略”。

同源策略阻止读取跨域请求得到的资源。

同源策略在1995年`网景浏览器2.02`中引入，最开始是为了保护跨域`DOM`而设计的。

跨域请求有三种形式：

1. 跨域写（Cross-origin writes）
2. 跨域内嵌（Cross-origin embeds）
3. 跨域读（Cross-origin reads）

同源策略的规则定义如下：

- `<ifame>`：跨域内嵌允许（合适的`X-Frame-Options`）。
- `<link>`：跨域内嵌允许（合适的`Content-Type`）。
- `<form>`：跨域写允许。
- `<audio>`：跨域内嵌允许。
- `<video>`：跨域内嵌允许。
- `<script>`：跨域内嵌允许，某些api的调用可能会被禁止。
- `<img>`：跨域内嵌允许，通过JavaScript跨域读取或者在`<canvas>`中加载被禁止。

**何谓CROS（跨域资源共享）？**

浏览器的同源策略能解决很多安全的问题，但是其限制也带来了不便。

CROS（`Cross-origin resource sharing`）跨域资源共享就是来放宽浏览器同源策略的严格限制，便于某些场景的使用。

这里重点讲述`ajax`跨域请求时，其请求过程和解决办法。

一个域中`ajax`跨域请求另一个域的接口时，该请求的生命历程是由客户端和被请求资源服务端共同决定的。客户端的行为是浏览器同源策略指定的，被请求资源服务端行为是资源提供时具体实现提供的。具体来说：

如果是“简单”的`ajax`跨域请求，那么浏览器会放行改请求，但是会限制浏览器对请求到资源`reponse`的读取。

如果是“复杂”的`ajax`跨域请求，那么浏览器会先自行触发一个`preflight`请求，根据服务端的相应`header`信息决定是否放行客户端请求。

这里所谓的“简单”和“复杂”请求是相关规范定义的，一个“复杂”请求要至少满足如下其中一个条件：

1. 非`GET`、`POST`或者`HEAD`请求。
2. 请求头信息包含除`Accept`、`Accept-Language`或者`Content-Language`外的头信息。
3. 请求`Content-Type`的值不是`application/x-www-form-urlencoded`、 `multipart/form-data`或者`text/plain`。

接下来用[Crystal](https://crystal-lang.org/)启动http服务，看看不同跨域请求的生命历程：

1). “简单”的`get`跨域读取

2). “复杂”的`post`跨域写入

**引用**

- [https://ieftimov.com/post/deep-dive-cors-history-how-it-works-best-practices/](https://ieftimov.com/post/deep-dive-cors-history-how-it-works-best-practices/)
- [https://mobilejazz.com/blog/which-security-risks-do-cors-imply/](https://mobilejazz.com/blog/which-security-risks-do-cors-imply/)

<h3 id="4">4. 总结</h3>

<h3 id="99">更新记录</h3>

- 2022-02097 18:10 首次提交文章到[冯兄话吉](https://fengmengzhao.github.io)。
