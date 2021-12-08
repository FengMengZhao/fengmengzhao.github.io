---
layout: post
title: 'SSH终端怎们正确打开Linux gui程序-X Window System？'
subtitle: 'Windows系统上使用大多都是图形化界面程序，也有少数的命令行程序(比如netstat)，而Linux大多使用的是命令行程序，但是也有gui程序(比如jvisualvm)。但是有时候在Linux打开gui程序会报错，有时候又没有问题。这里面涉及到Linux gui程序显示用到的X Window System服务，本文一探究竟什么是X Window System，如何能正确在ssh终端中打开Linux gui程序。'
background: '/img/posts/x-server.png'
comment: false
---

# 目录

- [1. 引言](#1)
- [2. X Server是什么？](#2)
- [3. 使用Microsoft VcXsrv打开Linux gui程序](#3)
- [4. 引用](#4)

---

<h3 id="1">1. 引言</h3>

工作中笔者接触到国产化达梦数据库，该数据库在提供了图形化的管理工具`manager`。Windows上安装的达梦打开管理工具很简单，直接在`开始-->所有程序`中能找到`manager`管理工具；而Linux上安装的达梦，需要在安装位置中找到`tool`目录并执行`./manager`，幸运的话就能够调出达梦manager图形化界面。

这里之所以说：**幸运的话**就能够调出达梦manager图形化界面，是因为访问同样的达梦服务器，有些时候在Linux SSH终端中能够打开成功，有些时候就会报错。比如说用`putty`连达梦数据库服务器，执行`./manager`就报错；用`MobaXterm`连服务端执行`./manager`就能够成功调出来。

> 实际上在没有理解`X DISPLAY SERVER`之前是没有发现用不同的SSH客户端连接是造成打开达梦manager工具成功或者失败差异所在。

现象就是：不同的SSH终端，有时候能打开manager图形化管理工具，有些时候就打开失败（后台报错）。到底是什么问题呢？

把问题抽象一下，实际上Linux上运行的达梦manager就是一个Linux gui程序，那么问题就是**SSH终端怎么正确打开Linux gui程序？**一番查询之后，发现是知识的盲区`X DISPLAY SERVER`。

<h3 id="2">2. X Server是什么？</h3>

Windows系统中打开图形化应用程序很简单，这是因为Win程序的图形化功能是写在内核中的（微软在DOC系统之后发现了图形化界面的巨大商业价值，后来开发的操作系统内核级别就支持图形化内容）。Linux(或者POSIX)系统要支持图形化界面程序，需要`Window System`（Unix系统的哲学之一是：一个工具只做一件事并且把这件事做好，因此图形化显示在架构上也是解耦独立的）。`Window System`是一种实现了窗口、图标、菜单和像素点范式的gui。我们熟知的Unix操作系统大多使用`X Window System`，苹果的OSX系统使用`Quartz Compositor` Window System。

> 注意这里的`Window System`不是`Windows System`，和微软的windows系统没有关系。

`Window System`核心是`DISPLAY SERVER`（或者称为window Server、compositor）。一个调用了`DISPLAY SERVER`来显示图形化的程序称之为该`DISPLAY SERVER`的客户端（**client**）。

既有client（调用`DISPLAY SERVER`服务的gui程序），也有server（`DISPLAY SERVER`），它们的交互可能涉及到协议（protocol），这种协议就称为**display server protocol**。目前`X Window System`的`X DISPLAY SERVER`使用的协议就是`X 11`（11表示的是版本）。

`DISPLAY SERVER`的主要功能是协调操作系统、硬件和其他模块与gui程序之间的输入输出，它在图形化硬件上面提供提供一个抽象供更高级别（没错，Window System的设计是有层级的，这也体现了Unix系统的设计哲学）的图形化接口（例如`window manager`）使用。

`X Window System`图形化调用架构如下：

![](/img/posts/x-server-hierarchical-design.png)

如图所示，`DISPLAY SERVER`是`Window System`的核心所在。当你在Linux上打开一个gui程序的时候，该程序会调用`DISPLAY SERVER`的图形化显示服务。这个时候`DISPLAY SERVER`是服务端，而gui程序是客户端。这里和我们平时的理解有差异，一般我们任务服务端都是在远程端，而客户端是在本地，这里`DISPLAY SERVER`服务的调用是反过来。

那到底`DISPLAY SERVER`在本地哪里呢？可能是你本地Linux操作系统中自带的，也可能是你在Windows系统中手动安装的（Windows系统默认没有display server服务），还可能是ssh客户端工具（例如MobaXterm）内置提供的等等。但是总之，你想在本地启动Linux gui程序，本地一定是要有`DISPLAY SERVER`服务的。

Linux gui程序是如何找`DISPLAY SERVER`服务的呢？在程序启动的环境变量中会查找`DISPLAY`设置的服务地址。比如`EXPORT DISPLAY=:0.0`表示调用本机`DISPLAY SERVER`服务，服务的端口是`127.0.0.1:6000`；`EXPORT DISPLAY=:10.0`表示调用本机`DISPLAY SERVER`服务，服务的端口是`127.0.0.1:6010`；`EXPORT DISPLAY=172.26.18.37:3600.0`表示调用非本机**但在本地**`DISPLAY SERVER`，服务的端口是`172.26.18.37:9600`。这里实际`DISPLAY SERVER`服务监听的端口号是设置环境变量`:`后第一个数字`+6000`，正如上面`:3600.0`实际服务监听的端口就是`6000 + 3600 --> 9600`。配置完`DISPLAY`环境变量之后，可以使用`xhost +`来验证并禁用掉Access Control限制。

> 有些Linux服务端查看`echo $DISPLAY`设置的是`:0.0`，但是并不能通过`netstat -nalp |grep 60000`找打对应的X SERVER监听服务，这样的X SERVER只能够正常打开本地的gui程序，远程其他host并配置当前机器`export DISPLAY=x.x.x.x:0.0`的DISPLAY属性并不能调通X SERVER服务。如果通过端口查看本地X服务在监听，则远程host配置本地X SERVER能够调用X SERVER服务并打开gui程序。

X11 Forwarding和DISPLAY环境变量设置是两个概念。DISPLAY是告诉你的环境去哪里调用X SERVER服务，而X11 Forwarding能够将本地配置的DISLAY SERVICE转发到Forwarding服务启动的X SERVER上。

常见的`DISPLAY SERVER`如下：

- X.Org server (mostly for unix like)
- Wayland (mostly for unix like)
- Mir (mostly for unix like)
- SurfaceFlinger (This is for Google Android)
- Quartz Compositor (This is what Apple MacOS uses)
- Desktop Window Manager (This is what Microsoft Windows uses)

<h3 id="3">3. 使用Microsoft VcXsrv打开Linux gui程序</h3>

Windows系统的图形化是有内核程序支持的，因此Windows默认没有X SERVER。我们如果远程Linux机器并且打开Linux giu程序在Win本地，就需要安装X SERVER。

Windows经常用的X SERVER有`XManager`、`MobaxTerm X SERVER`、`XMing`等。`MobaxTerm`启动会默认在本地`6000`端口(6000端口也是X协议:0的默认端口)启动一个X SERVER并开启X Forwarding功能。当在Linux机器上启动gui程序的时候，内核会启动环境变量中配置的`DISPLAY`服务绘制图形化界面，`DISPLAY`的值写为`IP:PORTOFFSET:0.0`的形式，会访问`IP:6000+PORTOFFSET`的X服务。我们试验一下：

在Win10上下载一个微软的`VcXsrv X Server`，记得启动的时候机制Access Control，设置DISPLAY NUM为3600，这样你远程访问一个Linux图形化工具如jvisualvm，配置`export DISPLAY=172.26.18.37:9600`就能够成功启动Linux gui程序。

远程一个安装有完整JDK的虚拟机，打开`jvisualvm`，如果没有`DISPLAY`环境变量设置，会报错：

![](/img/posts/x-server-display-env-not-set-error.png)

在Windows10系统上安装VcXsrv Windows X Server，，安装后打开并配置`display num`为`3600`、`Access Control`为`disable`，如图：

![](/img/posts/microsoft-x-server-set-port-3600.png)

![](/img/posts/microsoft-x-server-set-accesscontrol-disable.png)

在虚拟机上设置`export DISPLAY=$HOST:3600.0`，重新打开`jvisualvm`：

![](/img/posts/x-server-display-env-set.png)

这时候就能够成功打开`jvisualvm`这个linux的gui程序了。

> 这里的`$HOST`是安装VcXsrv的Windows的IP，保证`9600`端口和虚拟机是通的。

<h3 id="4">4. 引用</h3>

- [https://unix.stackexchange.com/questions/345344/difference-between-xorg-and-gnome-kde-xfce](https://unix.stackexchange.com/questions/345344/difference-between-xorg-and-gnome-kde-xfce)

---
