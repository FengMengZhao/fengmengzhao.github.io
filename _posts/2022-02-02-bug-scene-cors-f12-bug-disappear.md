---
layout: post
title: 'bug现场谜之总不能告诉客户你要按F12(打开控制台)吧？（跨域详解）'
subtitle: '现场两套系统，集成同一个单点登录。其中一个系统跳转到另外一个系统时现场浏览器会刷新两次。奇怪的是打开F12，问题就不能复现（问题消失了）。不敢让客户按F12打开控制台访问系统，还是看一看吧...'
background: '/img/posts/bug-scene-cros-origin.png'
comment: false
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 跨域详解](#3)
    - [3.1 何谓同域（同源）？](#3.1)
    - [3.2 何谓跨域（跨域怎么发生？）](#3.2)
    - [3.3 跨域有什么安全问题？](#3.3)
    - [3.4 何谓浏览器同源策略（`same-origin policy`）](#3.4)
    - [3.5 何谓CROS（跨域资源共享）？](#3.5)
- [4. 总结](#4)
- [引用](#98)
- [更新记录](#99)
- [相关文章推荐](#100)

---

<h3 id="1">1. bug现场情况</h3>

现场两套系统，集成同一个单点登录。其中一个系统跳转到另外一个系统时浏览器会刷新两次。

奇怪的是打开F12，问题就不能复现。

<h3 id="2">2. 尝试破案</h3>

打开控制台问题就解决了？真是奇怪！可能是控制台打开后，静态文件在浏览器端不再缓存造成的。

禁止F12的`Network --> Disable cache`设置，果然问题能够复现，前端`js`的请求确实是缓存的。

初步判断两次刷新原因：前端`js`缓存，发送异步权限数据请求接口时没有权限（第一次请求刷新），然后重定向单点登录服务获取`service ticket`，重新登录后，再次请求权限数据接口（第二次请求刷新），页面成功展示。

> 笔者对浏览器的行为不熟，这里只是猜测。

浏览器刷新两次`fiddler`抓包如图：

![](/img/posts/cros-application-client-cache-browser-refresh-two-times.png)

第一次异步请求后，由于没有权限，`302`重定向访问单点登录服务。这里控制台会提示跨域请求，跨域在[跨域详解](#3)部分详细介绍。

前端明确说了，不是前端的问题，解决不了。

> 笔者公司的前端就是硬气。

在后台处理，后台是`springboot`项目，增加配置：

```shell
spring:
  resources:
    cache:
      cachecontrol:
        max-age: 0
```

前端文件不会缓存，问题解决。

禁用缓存后，不会出现地址栏刷新两次现象，`fiddler`抓包如图：

![](/img/posts/cros-application-client-cache-browser-refresh-two-times-server-side-cache-0.png)

问题解决了，笔者对那个跨域的报错产生了兴趣。之前也看过不少跨域的文章，始终对跨域云里雾里。春节找出[收藏的跨域文章](https://ieftimov.com/post/deep-dive-cors-history-how-it-works-best-practices/)，好好研读了一下，有所获，赶紧借此文分享出来。

<h3 id="3">3. 跨域详解</h3>

做`web`开发，工作中肯定接触过如下浏览器控制台报错：

> No ‘Access-Control-Allow-Origin’ header is present on the requested resource.

请求的跨域资源`response`的`header`中没有`Access-Control-Allow-Origin`信息。

> Cross-Origin Request Blocked: The Same Origin Policy disallows reading the remote resource at https://example.com/

浏览器同源策略禁止读取跨域资源。

> Access to fetch at ‘https://example.com’ from origin ‘http://localhost:3000’ has been blocked by CORS policy.

在域`http://localhost:3000`下访问域`https://example.com`资源被禁止。

<h4 id="3.1">3.1 何谓同域（同源）？</h4>

1. 相同协议（http、https）
2. 相同主机名/ip（127.0.0.1、localhost、google.com）
3. 相同端口（80、443、8080）

满足三个条件是同源，否则就是不同的域。

<h4 id="3.2">3.2 何谓跨域（跨域怎么发生？）</h4>

1989诞生的[World Wide Web](https://en.m.wikipedia.org/wiki/World_Wide_Web)最初的`html`只有纯文本。

[1993年](http://1997.webhistory.org/www.lists/www-talk.1993q1/0182.html)引入`<img>`标签，在`html`渲染时允许加载图片资源，这样纯文本中就可以展示图片。像这样在`html`中允许加载子资源（`subresource`）的`tag`还有：

- `<ifame>`
- `<link>`
- `<form>`
- `<audio>`
- `<video>`
- `<script>`

> 诚如上面`html`标签语义，所谓的“子资源（`subresource`）”就是例如表单、文件、音视频、脚本等外部资源。

当一个域中包含有上面`tag`的`html`渲染时，就会加载`subresource`，当这个`subresource`和当前域不同源时，跨域请求就发生了。例如，在一个域中`xmlhttprequest`（`ajax`）请求另外一个域的接口时就是跨域请求。

> 世界上第一个[web页面](http://info.cern.ch/hypertext/WWW/TheProject.html)

<h4 id="3.3">3.3 跨域有什么安全问题？</h4>

一个域中加载另一个域的文件、音视频、脚本等`subresource`时，大部分情况不会产生什么安全问题，但是有一些情况如果不做限制，就存在安全隐患。

例如一个域中提供了基于`cookie/session`的发送邮件接口，该域允许任何域对该接口的跨域请求。那么恶意网站有可能在获取有效`cookie`后任意调用发送邮件接口攻击网站。

可能有同学疑问：发送邮件接口如果需要认证才能成功调用，别人没有认证信息如何能成功调用呢？

实际上用户在浏览器端完成登录后，用户信息就存储在浏览器端，这时打开恶意网站就有可能被恶意脚本携带用户信息完成攻击。

<h4 id="3.4">3.4 何谓浏览器同源策略（`same-origin policy`）</h4>

既然跨域请求有安全的问题，浏览器端就做了相关限制，称之为“浏览器同源策略”。

同源策略阻止读取跨域请求得到的资源。

> 这是广义的一个定义，实际上浏览器针对不同`subresource`有不同的限制策略，下面有详细说明。

同源策略在1995年`网景浏览器2.02`中引入，最开始是为了保护跨域`DOM`而设计的。

跨域请求有三种形式：

1. 跨域写（Cross-origin writes）
2. 跨域内嵌（Cross-origin embeds）
3. 跨域读（Cross-origin reads）

同源策略的规则定义如下：

- `<ifame>`：跨域内嵌允许（需要合适的`X-Frame-Options`）。
- `<link>`：跨域内嵌允许（需要合适的`Content-Type`）。
- `<form>`：跨域写允许。
- `<audio>`：跨域内嵌允许。
- `<video>`：跨域内嵌允许。
- `<script>`：跨域内嵌允许，某些api的调用可能会被禁止（例如`ajax`跨域调用）。
- `<img>`：跨域内嵌允许，通过JavaScript跨域读或者在`<canvas>`中加载被禁止。

**何谓CROS（跨域资源共享）？**

浏览器的同源策略能解决很多安全的问题，但是其限制也带来了不便。

CROS（`Cross-origin resource sharing`）跨域资源共享就是来放宽浏览器同源策略的严格限制，便于某些场景的使用。

这里重点讲述`ajax`跨域请求时，其请求过程和解决办法。

一个域中`ajax`跨域请求另一个域的接口时，该请求的生命历程是由客户端和被请求资源服务端共同决定的。客户端的行为是浏览器同源策略指定的，被请求资源服务端行为由资源提供者具体实现提供。具体来说：

> 所谓的请求“生命历程”是指：该请求从浏览器发起，到服务端响应，再到浏览器读取响应结果并展示这个过程。

**如果是“简单”的`ajax`跨域请求，那么浏览器会放行改请求，但是会限制浏览器对请求到资源`reponse`的读取。**

**如果是“复杂”的`ajax`跨域请求，那么浏览器会先自行触发一个`preflight`请求，根据服务端的相应`header`信息决定是否放行客户端请求。**

这里所谓的“简单”和“复杂”请求是相关规范定义的，一个“复杂”请求要至少满足如下其中一个条件：

1. 非`GET`、`POST`或者`HEAD`请求。
2. 请求头信息包含除`Accept`、`Accept-Language`或者`Content-Language`外的头信息。
3. 请求`Content-Type`的值不是`application/x-www-form-urlencoded`、 `multipart/form-data`或者`text/plain`。

接下来用[Crystal](https://crystal-lang.org/)启动http接口服务，看看不同跨域请求的生命历程：

`Crystal`安装参考[官方文档](https://crystal-lang.org/install/)，脚本`basic_greet.cr`为：

```crystal
require "kemal"

port = 4000

get "/" do
  "Hello world!"
end

get "/greet" do
  "Hey!"
end

post "/greet" do |env|
  name = env.params.json["name"].as(String)
  "Hello, #{name}!"
end

post "/greet_str" do |env|
  name = env.params
  "Hello, 成功了!"
end

Kemal.config.port = port
Kemal.run
```

使用命令`sudo crystal run src/basic_greet.cr`启动接口服务。

0). 同域下请求

![](/img/posts/same-origin-ajax-post-request.png)

同域下请求，一切正常，接口能发起成功并且浏览器能读取响应接口数据。

1). “简单”的`post`跨域读取

从`http://172.22.27.215:4000/greet`接口域下发送“简单”的`ajax`请求，如图：

![](/img/posts/cros-ajax-post-simple-request-console-and-network-message.png)

接口能发起成功。但是，如上图控制台报错，浏览器同源策略禁止读取远端资源，提示`CORS header ‘Access-Control-Allow-Origin’ missing`，也就是说响应头信息中缺少`Access-Control-Allow-Origin`信息。

> 这里之所以是“简单”请求，是因为`Content-Type`是`text/plain`，参考上面“复杂”请求规则，不满足任意一个。

2). “复杂”的`post`跨域写入

从[天涯bbs论坛](http://bbs.tianya.cn)域下发送“复杂”的`ajax`请求。

控制台报错如图：

![](/img/posts/cros-ajax-post-complex-request-console-error.png)

网络抓包如图：

![](/img/posts/cros-ajax-post-complex-request-network-traffic.png)

图中`1.`为`preflight`请求，请求方法为`OPTIONS`。服务端目前没有实现`OPTIONS`方法实现，提示`404 Not Found`。

图中`2.`为真正的`POST`请求，因为`1.`的`preflight`请求没有获得同源策略规定的头信息，所以`2.`的真正`POST`请求被浏览器级别`blocked`。

这种情况下的请求生命历程为：先行的`preflight`请求`404 Not Found`（“身先死”），真正的`POST`请求没有发起成功（“出师未捷”）。也就是所谓的：“出师未捷身先死”。

那，“复杂”的跨域请求`preflight`要求怎样的实现呢，才能满足浏览器`CROS`协议的要求呢？

浏览器在发送`preflight`后会寻找相应中的3个`header`：

- `Access-Control-Allow-Methods`：`CROS`协议允许的请求方法，例如`GET`、`POST`等。
- `Access-Control-Allow-Headers`：`CROS`协议允许的请求`header`，例如`Content-Type`等。
- `Access-Control-Max-Age`：上面两个信息能够缓存的秒数（默认值是5），实际上只有上面2个`header`是必须的，当前`header`没有，不影响请求的生命历程。

也就是说，根据`CROS`协议，`preflight`请求响应头信息中要明确返回`客户端实际请求`的方法（通过响应头信息`Access-Control-Allow-Methods`值）和头信息（通过响应头信息`Access-Control-Allow-Headers`值），这样浏览器才会同意发送`客户端实际请求`。当然`preflight`请求响应头`Access-Control-Max-Age`也可以指定上面2个信息缓存的时间，响应中不设置也可以，默认就是5秒钟。

> 这里对`客户端实际请求`进行了标注，避免和`preflight`请求混为一谈。当一个“复杂”的跨域请求发起的时候，首先，**浏览器**会发送一个`preflight`请求，“试探”一下服务端是否允许该跨域请求，如果允许，浏览器才允许该“复杂”请求（也就是`客户端实际请求`）紧随`preflight`请求之后发起，否则就会被浏览器`blocked`。

那，按照要求实现下`preflight`的`OPTIONS`请求吧。

修改`basic_greet.cr`，增加`OPTIONS`实现：

```crystal
options "/greet" do |env|
  # Allow `POST /greet`...
  env.response.headers["Access-Control-Allow-Methods"] = "POST"
  # ...with `Content-type` header in the request...
  env.response.headers["Access-Control-Allow-Headers"] = "Content-type"
end
```

重启接口服务，控制台重新请求，如图：

![](/img/posts/cros-ajax-post-complex-request-with-options-implementation-but-withou-allow-origin-in-options.png)

图中通过`Status 200 OK`可以看出`preflight`请求是成功的，但是下面控制台报错：响应头信息中缺少`Access-Control-Allow-Origin`信息。也就是说，`preflight`请求是成功了，`CROS`协议要求的`preflight`请求响应头信息也存在，但是由于`Access-Control-Allow-Origin`头信息的缺失，浏览器同源策略限制读取请求响应内容。

修改`basic_greet.cr`，响应信息头增加`env.response.headers["Access-Control-Allow-Origin"] = "http://bbs.tianya.cn"`：

```crystal
options "/greet" do |env|
  # Allow `POST /greet`...
  env.response.headers["Access-Control-Allow-Methods"] = "POST"
  # ...with `Content-type` header in the request...
  env.response.headers["Access-Control-Allow-Headers"] = "Content-type"
  # ...from https://www.google.com origin.
  env.response.headers["Access-Control-Allow-Origin"] = "http://bbs.tianya.cn"
end
```

重新请求：

`preflight`请求成功，如图：

![](/img/posts/cros-ajax-post-complex-request-with-options-implementation-but-real-request-access-allow-none1.png)

控制台有`客户端实际请求`报错就很熟悉了：

![](/img/posts/cros-ajax-post-complex-request-with-options-implementation-but-real-request-access-allow-none2.png)

响应头信息中缺少`Access-Control-Allow-Origin`信息被浏览器禁止读取响应内容。接口中增加响应`header`信息：

```crystal
post "/greet" do |env|
  name = env.params.json["name"].as(String)
  env.response.headers["Access-Control-Allow-Origin"] = "http://bbs.tianya.cn"
  "Hello, #{name}!"
end
```

重新请求：

![](/img/posts/cros-ajax-post-complex-request-all-success.png)

“复杂”请求的`preflight`和`客户端实际请求`都成功了，实现了“复杂”请求的跨域资源共享。

<h3 id="4">4. 总结</h3>

- 看似玄学的问题，其背后都有一定的原因。能不能准确的识别出来取决于对问题涉及的相关知识广度和深度的了解，抱着好奇心多了解多涉猎能增加知识的广度，抱着探索的意志剖析技术点及其源头能深挖知识的深度。
- 成熟的程序员不是任何知识都懂，而是当遇到问题涉及自己不懂的地方时，能迅速识别出盲区并学习掌握。
- 跨域总结：由于浏览器跨域请求存在安全隐患，所以浏览器制定了同源策略进行跨域请求等行为的限制（一定程度）。跨域资源共享基于一定的规则放宽了同源策略严格限制，使得不同域之间数据交互更加方便。跨域的问题一旦产生（前端后完全分离项目尤其常见），需要前后端共同努力解决。一个接口是否允许被跨域请求是由服务端接口头信息告诉浏览器的，而客户端请求参数的设置，尤其涉及到cookie信息携带等配置需要客户端了解个中原理才能完成。

<h3 id="98">引用</h3>

- [https://ieftimov.com/post/deep-dive-cors-history-how-it-works-best-practices/](https://ieftimov.com/post/deep-dive-cors-history-how-it-works-best-practices/)
- [https://mobilejazz.com/blog/which-security-risks-do-cors-imply/](https://mobilejazz.com/blog/which-security-risks-do-cors-imply/)

<h3 id="99">更新记录</h3>

- 2022-02097 18:10 首次提交文章到[冯兄话吉](https://fengmengzhao.github.io)。

<h3 id="100">相关文章推荐</h3>

- [有后端认证的前后端分离项目跨域问题](https://fengmengzhao.github.io/2019/05/24/front-and-rear-separation-under-authentication-with-CORS.html)
