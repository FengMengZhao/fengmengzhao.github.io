---
layout: post
title: Internet之图解HTTP
---

## 目录

- [1 了解web及网络基础](#1)
- [2 简单的HTTP协议](#2)
- [3 HTTP报文中的HTTP信息](#3)
- [4 状态码](#4)
- [5 HTTPS](#5)

---

---

<h2 id="1">1 了解web及网络基础</h2>

1994年12月，网景通讯公司发布了NetScape Navigator 1.0,1995年微软公司发布了Internet Explorer 1.0和2.0，2004年Mozilla基金发布了FireFox浏览器，另外还有Chrome、Opera、Safari等浏览器。

HTTP/1.01996年5月正式作为标准被公布；1997年公布HTTP/1.1是目前主流的HTTP协议版本。

TCP/IP是把互联网相关联的协议集合起来。

TCP/IP的分层管理

TCP/IP按层次分别分为以下四层：应用层、传输层、网络层和数据链路层。

1. 应用层决定向用户提供应用服务时通信的活动，主要包括：FTP(File Transfer Protocol，文本传输协议)和DNS(Domain Name System，域名系统)和HTTP协议。
2. 传输层，提供网络连接中两台计算机之间的数据传输，在传输层有连个不同性质的协议：TCP(Transmission Control Protocol，传输控制协议)和UDP(User Date Protocol，用户数据报协议)。
3. 网络层(网络互联层)，用于处理在网络上流动的数据包，数据包是网络传输的最小数据单位。该层规定了怎样的路径到达对方的计算机，并且把数据包传送给对方。	网络层所起到的作用是在众多的选项中选择一条传输路径。
4. 链路层，用来处理连接网络的硬件部分，包括控制操作系统、硬件的设备驱动、NIC(Network Interface Card，网络适配器，即网卡)及光纤等物理可见部分。

IP地址是指明了节点被分配到的地址，MAC地址(Media Access Control Address)是指网卡所属的固定地址。IP地址可变但是MAC地址基本不会改变。

---

---

<h2 id="2">2 简单的HTTP协议</h2>

HTTP协议是不保存状态协议，是为更快的处理大量事务，确保协议的可伸缩性。为了实现期望的保留状态引进cookie技术。

CGI(Common Gateway Interface)通用网管接口

HTTP方法，get主要用来获取资源；post主要用来传输实体主体；put用于文件的传输，由于没有验证机制，一般的web网站不适用该方法；head方法获取报文首部；delete用于删除文件，与put一样没有验证机制；options询问支持的方法；trace用来追踪路径；connect要求用隧道协议链接代理。

---

---

<h2 id="3">3 HTTP报文中的HTTP信息</h2>

MIME(Multiple Internet Mail Extensions，多用途因特网邮件扩展)

---

---

<h2 id="4">4 状态码</h2>

状态码的类别：

1xx，信息状态码，接受的请求正在处理；2xx，成功状态码，请求正常处理完毕；3xx，重定向状态码，需要进行附加操作完成请求；4xx，客户端错误状态码，服务器无法处理请求；5xx，服务器端错误状态码，服务器处理请求错误。

缓存是指代理服务器或客户端本地磁盘内保存的资源副本，利用缓存能够减少对源服务器的访问，因此节省了通讯流量和通讯时间。

通讯数据转发程序：代理、网关、隧道。

HTTP1.1通用首部字段

1. Cache-Control，操作缓存的工作机制。
2. Connection，可以控制不再转发给代理的首部字段和管理持久连接。
3. Date表明HTTP报文的日期和时间。

Cookie服务的首部字段：响应首部：set-Cookie；请求首部：Cookie

> httpOnly是Cookie的一个属性，它是JavaScript脚本无法获得Cookie，主要是为了防止跨站脚本攻击对Cookie信息的窃取。

---

---

<h2 id="5">5 HTTPS</h2>

SSL(Secure Socket Layer，安全套接层)，HTTP + SSL = HTTPS

CGI(Common Gateway Interface，通用网管接口)是指web服务器在接收到客户端发送过来的请求之后转发给程序的一组机制。

---

---
