---
layout: post
title: 'Windows10 WSL体验如此丝滑(Windows上使用完整服务的Linux)'
subtitle: '在Windows上使用一些Linux工具，一般会在本机装一个Linux虚拟机或者在Windows上装Docker，这两种方法对于本地开发环境来说都比较重。Windows推出了WSL，可以直接在Windows上使用完整的Linux功能，笔者用起来还是比较实用。但是想丝滑入WSL这个坑，要解决一系列问题，比如安装docker、代理使用等，本文针对wsl使用的痛点，一一给出使用办法。'
background: '/img/posts/wsl-resolve-headache-problems.jpg'
comment: false
---

# 目录

- [1. WSL介绍](#1)
    - [1.1 WSL vs 虚拟机](#1.1)
- [2. WSL2丝滑入坑](#2)
    - [2.1 WSL动态IP，如何从外部访问？](#2.1)
    - [2.2 不安装Docker Desktop，如何安装Docker？](#2.2)
    - [2.3 Win和WSL文件系统如何打通任督二脉？](#2.3)
    - [2.4 WSL代理使用](#2.4)
        - [2.4.1 系统使用代理](#2.4.1)
        - [2.4.2 git使用代理](#2.4.2)
- [3. WSL2使用遇到的问题](#3)
    - [3.1 WSL和VirtualBox对于Hyper-v冲突](#3.1)
    - [3.2 Centos6在wsl2 docker上运行有问题](#3.1)
- [4. 引用](#4)

---

<h3 id="1">1. WSL介绍</h3>

<h4 id="1.1">1.1 WSL vs 虚拟机</h4>

<h3 id="2">2. WSL2丝滑入坑</h3>

<h4 id="2.1">2.1 WSL动态IP，如何从外部访问？</h4>

<h4 id="2.2">2.2 不安装Docker Desktop，如何安装Docker？</h4>

<h4 id="2.3">2.3 Win和WSL文件系统如何打通任督二脉？</h4>

<h4 id="2.4">2.4 WSL代理使用</h4>

<h5 i="2.4.1">2.4.1 系统使用代理</h5>

<h5 id="2.4.2">2.4.2 git使用代理</h5>

<h3 id="3">3. WSL2使用遇到的问题</h3>

<h4 id="3.1">3.1 WSL和VirtualBox对于Hyper-v冲突</h4>

<h4 id="3.2">3.2 Centos6在wsl2 docker上运行有问题</h4>

<h3 id="4">4. 引用</h3>

- [https://dowww.spencerwoo.com/](https://dowww.spencerwoo.com/)
- [https://superuser.com/questions/1582234/make-ip-address-of-wsl2-static](https://superuser.com/questions/1582234/make-ip-address-of-wsl2-static)
- [https://www.illuminiastudios.com/dev-diaries/ssh-on-windows-subsystem-for-linux/](https://www.illuminiastudios.com/dev-diaries/ssh-on-windows-subsystem-for-linux/)
- [https://dev.to/bowmanjd/install-docker-on-windows-wsl-without-docker-desktop-34m9](https://dev.to/bowmanjd/install-docker-on-windows-wsl-without-docker-desktop-34m9)

---
