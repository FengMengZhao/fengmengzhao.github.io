---
layout: post
title: 'SSH终端怎们打开Linux gui程序-X Window System？'
subtitle: 'Windows系统上使用大多都是图形化界面程序，也有少数的命令行程序(比如netstat)，而Linux大多使用的是命令行程序，但是也有gui程序(比如jvisualvm)。但是有时候在Linux打开gui程序会报错，有时候又没有问题。这里面涉及到Linux gui程序显示用到的X Window Syste服务，本文一探究竟什么是X Window System，如果能正确在ssh终端中打开Linux gui程序。'
background: '/img/posts/x-server.png'
comment: false
---

# 目录

- [1. 引言](#1)
- [2. X Server是什么？](#2)
- [3. 使用Microsoft VcXsrv打开Linux gui程序](#3)

---

<h3 id="1">1. 引言</h3>

工作中笔者接触到国产化达梦数据库，该数据库在提供了图形化的管理工具`manager`。Windows上安装的达梦打开管理工具很简单，直接在`开始-->所有程序`中能找到`manager`管理工具；而Linux上安装的达梦，需要在安装位置中找到`tool`目录并执行`./manager`，幸运的话就能够调出达梦manager图形化界面。

这里之所以说：**幸运的话**就能够调出达梦manager图形化界面，是因为访问同样的达梦服务器，有些时候在Linux SSH终端中能够打开成功，有些时候就会报错。比如说用`putty`连达梦数据库服务器，执行`./manager`就报错；用`MobaXterm`连服务端执行`./manager`就能够成功调出来。

> 实际上在没有理解`X DISPLAY SERVER`之前是没有发现用不同的SSH客户端连接会造成打开达梦manager工具成功或者失败的不同结果。

现象就是：不同的SSH终端，有时候能打开manager图形化管理工具，有些时候就打开失败（后台报错）。到底是什么问题呢？

把问题抽象一下，实际上Linux上运行的达梦manager就是一个Linux gui程序，那么问题就是**SSH终端怎么打开Linux gui程序？**一番查询之后，发现是知识的盲区`X DISPLAY SERVER`。

<h3 id="2">2. X Server是什么？</h3>

---
