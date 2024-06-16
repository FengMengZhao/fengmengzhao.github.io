---
layout: post
title: 2018年『web』开发者不得不知的技术趋势
subtitle: "对于『web』开发者来说2018年充满乐趣而又富有挑战，有一些不得不知的技术趋势可能在接下里的一年中流行起来，关注并了解这些新技术能增长知识并且开阔眼界。"
background: '/img/posts/web-development-trends-2018.png'
comment: true
---

作为一个『web』开发者，无论是做前端还是后端，都应该时刻保持着对技术的敏感性。技术的流行需要一定时间的沉淀，有哪些web相关的技术会可能会在2018年成为web开发的新宠呢？下面列举业界经过实践并且被普遍看好或者大公司推荐使用的技术。

## Progressive Web Apps(PWAs)

『Progressive Web Apps』可以让你做到在访问网址的时候就像是在访问本地APP一样的体验。这项技术最初是Google在2015年提出来，Progressive Web App结合了web和本地APP的优点于一身。在[PWA Rocks](https://pwa.rocks/ "PWA应用实例")里可以看到Progressive Web App是什么样子。

![PWA应用实例](/assets/images/pwa-rocks.png "PWA应用实例")

Progressive Web App最大的一个优点在于它的可靠性。在后台增加了『Service Workers』，能够做到快速载入、离线或者在网络环境极差时使用、同步更新等。之所以称之为『Progressive』，是因为它能够在包括但不局限于的PC桌面、移动端、平板电脑或者是将来的任何新设备的设备上响应式运行。因为后台的Service Worker能够拦截网络请求，保证Progressive Web App的传输协议是『HTTPS』方式，这样就能够保证应用的安全性。

### 阿里巴巴PWAs

[阿里巴巴](http://www.alibaba.com/ "阿里巴巴官网")是世界上最大的在线B2B公司，在超过200个国家进行服务。在网站的开发上，用户体验会作为最重要的考虑。阿里巴巴网站要同时关注于『移动端Web』和『移动端App』的开发，以满足客户移动端体验的需求。但是，它意识到这样做并不是最大限度提高用户体验度的最好方法，Web和APP开发需要投入二倍的资源，而仅仅是为了满足移动端同一个需求。同时，Web与网络强结合，不能离线使用；App开发周期长，需要用户频繁更新。所以，阿里巴巴网站有76%部分已经转换为了PWAs。如果有兴趣可以体验一下阿里巴巴PWAs，在手机Chrome中，输入阿里巴巴官网，PWAs支持商家推送提醒，应用自定义启动界面等，真的就是在浏览器中有本地APP一样的体验。

## 单页Web应用(Single-Page WebSites)

目前，越来越多的网站开始致力于浏览的简洁和速度，我们也会看到越来越多的单页Web应用。所谓的单页Web应用是指：网站仅仅包含一个Html页面和一个长长的滚动条。来看一个单页Web应用[『神奇的交互式简历』](http://www.rleonardi.com/interactive-resume/ "神奇的交互式简历")：

![单页Web应用demo](/assets/images/single-web-page-demo.jpg "单页web应用demo")

单页Web应用具有很多优势：所有的内容展示在一个Web页面上，当用户线性浏览网站时，你能够控制网站信息流。有了这样的控制权，当滚动条滚动时，你可以带领用户完成一次旅行。同时，这样可以让访客统一入口的方式参与进来，你可以讲一个故事、论证一个观点或者模拟一次经历。

单页Web应用追求简洁和高效，支持移动端，用户界面友好，转换起来也相当方便。但是，它并不适合电子商务应用。

## 静态网站生成器(Static Site Generators)

现在来说，内容管理系统(content management System)领域是动态网站的天下。然而，互联网刚刚出现的第一个站点是静态的。现在，在静态网站生成器的帮助下，静态网站又回来的，这是一种混合式的Web开发方式。[Jekyll](https://jekyllrb.com/ "Jekyll")是目前最流行的静态网站生成器之一。

![Jekyll静态网站生成器](/assets/images/jekyll-website.png "Jekyll静态网站生成器")

静态网站生成器或者静态网站引擎把动态的内容和数据作为输入，输出可部署静态的文件(HTML, CSS, JavaScript)。不涉及服务器端语言和数据库。这样的静态站点速度更快、更加可靠、更便宜甚至免费并且很好部署和版本管理。由于网站没有服务器端语言和数据库的参与，因此网站也更加安全。

我的博客[冯兄话吉](https://fengmengzhao.github.io/ "冯兄话吉")就是基于Jekyll做的，托管在GitHub上。

![冯兄话吉](/assets/images/fengmengzhao-blog.png "冯兄话吉")

Jekyll、Hugo、GitBook和Pelican是现在比较流行的静态博客生成器。你也可以访问[StaticGen](https://www.staticgen.com/ "StaticGen")，发现更多的开源静态网站生成器。

## Motion UI

你可能在一些比较前卫的网站或者APP上已经看过了如背景动画、页面转换动画、滚动条滚动动画或者动画表格等效果，这些复杂动画的背后就是**Motion UI**。先看一个炫酷的效果实例[Business Card App design by Tubik](https://uimovement.com/ui/4781/business-card-app/ "Business Card App design by Tubik")。

![Motion UI Demo](/assets/images/motion-ui-demo.jpg "Motion UI Demo")

Motion UI最初是2014年12月Zurb基金会APP发布时附带的一个为了创造灵活的css切换和动画效果的依赖包。2016年，Zurb团队又增加了一些新的特性，作为一个独立的库正式发布。最新版的Motion UI Css样式能够兼容所有的JS，实现了将动画与Web无缝整合。

将Motion UI的元素加入到你的站点当中，能够大大提高用户的参与度和使用率。访问[UI Movement](https://uimovement.com/ "UI Movement")，这个站点帮助你学习使用Motion UI。

## 聊天机器人(Chatbots)

如果你使用过即时通讯工具，你应该就和聊天机器人聊过天了。仅仅『Facebook Messager』就有超过1万个聊天机器人。ChatBot是一项可以通过会话接口(例如即时通讯APP微信)实现应用交互的服务。这项服务可以帮助你网上购物、Uber打车、推动新闻或者是提供一些生活建议。这是一个预报天气的聊天机器人[Poncho](https://poncho.is/ "Poncho")。

![ChatBot Demo](/assets/images/chatbot-demo.jpg "ChatBot Demo")

聊天机器人有着巨大的商业价值和潜力。通过这个聊天机器人入口，你可以获得各种各样的服务。随着人工智能的兴起，聊天机器人能极大的改变改变公司的运作方式，国外的公司[Octane AI](https://octaneai.com/ "Octane AI")和[Drift](https://www.drift.com/ "Drift")已经认识到了这一点，开始进行这方面平台的搭建。

## 微信小程序

2017年9月微信小程序正式上线。是一种不需要下载安装即可使用的应用，它实现了应用『触手可及』的梦想，用户扫一扫或搜一下即可打开应用。我们先来回顾一下应用程序的历史变迁：

微信小程序在这样的历史下出现：

- PC 桌面程序 C/S机构
- PC 浏览器程序 B/S结构
- 移动端 APP C/S结构
- 移动端 H5 小程序 B/S结构 Chatbot ？
- IOT时代 B/S结构 ?

![应用程序历史](/assets/images/history-of-app.jpg "应用程序历史")

面对小程序的特点：无需安装、用完即走、没有入口、触发点触发。本质上微信中运行的H5程序而已(融合了运行平台,服务平台)，从APP到小程序就是从『CS』到『BS』结构的迁移；重复PC时代的路径而已。

小程序用来面对IOT(物联网)时代的尝试是否会在2018年有所成就呢？究竟是西方世界的大布局ChatBot还是中国的微信小程序能够在接下来的IOC时代成功运用还要拭目以待。

**2018年刚刚开始，作为一个开发者，保持对前沿技术的敏感性，提升格局，放眼远方。防止坐井观天，埋头走路的同时，多仰望星空。时刻牢记『穷则变，变则通，通则久远』，只要用心思考并且不断探索，你会有无限的可能。**
