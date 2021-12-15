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

维基百科：

> 适用于Linux的Windows子系统（英语：Windows Subsystem for Linux，简称WSL）是一个为在Windows 10和Windows Server 2019上能够原生运行Linux二进制可执行文件（ELF格式）的兼容层。

也就是说借助于`WSL`，在Windows上也能够执行Linux程序。对于开发人员来说，这是很有意义的。

高级语言程序开发一般要依赖图形化`集成开发环境（IDE）`，所以一般开发环境系统都是`Windows`或者`OSX`。`OSX`是从`Unix`系统发展而来的，所以自带命令行终端，对开发人员来说很友好；而`Windows`天生支持图形化操作，虽然有`cmd`和`PowerShell`终端支持简单的命令行，但终究功能有限，无法替代`Linux`功能。

举一个例子，在Windows上进行开发，现在需要将某个目录及其子目录中所有配置文件的`172.16.12.13`替换为`141.151.1.111`，不知道具体哪个配置文件中含有待替换字符串并且这样的配置文件多而且分散。如果在`Windows`手动查找或者写`bat`脚本都是一件很费劲的事情，而在`Linux`中只需要一行命令：

```shell
#该命令递归寻找当前目录及子目录后缀是txt的文件并执行替换字符串
find . -type f -name '*.txt' -exec sed -i 's/172.16.12.13/141.151.1.111/g' {} \; 
```

可见，如果在`Windows`能够丝滑使用`Linux`系统程序，开发效率会大大提高。

微软做了在`Windows`系统上兼容`Unix like`的尝试，先推出了`WSL1`，`WSL1`在文件系统性能上有很大的问题。后又推出了`WSL2`，底层的实现和`WSL1`不同，文件系统性能有了很大的改善。`WSL1`和`WSL2`的区别参考：[https://docs.microsoft.com/en-us/windows/wsl/compare-versions](https://docs.microsoft.com/en-us/windows/wsl/compare-versions)。

网上对于`WSL`的吐槽很多，认为很难用。笔者亲身实践，使用`WSL2`能有丝滑般的开发体验，但也有一些头痛的问题，这里记录解决方案，方便诸位丝滑入坑`WSL`。

<h4 id="1.1">1.1 WSL vs 虚拟机</h4>

如果你想在`Windows`系统上运行`Linux`发行版，在没有`WSL`之前，可以装一个虚拟机。

虚拟机和`WSL2`相比较，前者和宿主机之间文件系统是完全隔离的；而后者是相通的。

`WSL2`基于`Hyper-v`实现了`Linux`内核，所以`WSL`也算是一个完整的Linux系统。相较于虚拟机，`WSL2`会轻量级一些。但是支持的发行版也有限。

建议：如果没有接触过`Linux`，要学习`Linux`，最好自己装一个`Linux`虚拟机；而如果对`Linux`非常熟悉了，可以尝试安装`WSL2`，对开发人员来说`WSL2`会开来更好的开发体验。

<h3 id="2">2. WSL2丝滑入坑</h3>

WSL2的安装请参考文章：[https://dowww.spencerwoo.com/](https://dowww.spencerwoo.com/)。

WSL2迁移参考回答：[https://stackoverflow.com/questions/63252225/is-this-the-correct-way-to-import-a-wsl-export-overwriting-default-installati](https://stackoverflow.com/questions/63252225/is-this-the-correct-way-to-import-a-wsl-export-overwriting-default-installati)

WSL2默认安装在Windows的`C`盘，随着WSL2使用容量的变大，可能会造成`C`盘空间的预警。可以使用上面的办法将`WSL2`迁移到其他逻辑卷。实际上，WSL像虚拟机一样，在HOST的存储是一个镜像文件，笔者Win10上安装的`WSL2-Ubuntu 20`已经使用了`40G`的存储，如图：

![](/img/posts/wsl-ext4-image-file.png)

> 如果将WSL2从开发环境电脑迁移到家中电脑，也是很方便的。

笔者`WSL2`选择安装的是微软商店的`Ubuntu 20.04 LTS`，接下来的操作基于该Ubuntu发行版。

<h4 id="2.1">2.1 WSL动态IP，如何从外部访问？</h4>

`WSL2`的网路类似于虚拟机中设置的`NAT`网络模式，该模式下`WSL2`实例借助于宿主机的网卡访问外部网络，宿主机也可以访问`WSL2`实例，但是宿主机所在局域网的其他主机不能访问`WSL2`实例。如图：

![](/img/posts/wsl2-nat-network-ping-relation.png)

`WSL2`的网络地址是动态变化的，并且局域网内的其他host都不能访问，这样太不方便了。

> 为什么说不方便呢？动态IP就不说了，公司内网的电脑肯定不是动态分配的，而是设置静态IP，因为本地环境除了开发之外还会提供服务供外部联调，需要固定的IP。另外，如果你在`WSL2`上启动的服务和同事联调，同事访问不到，就很麻烦了。

怎么解决呢？可以通过`Windows`端口映射的方式，将`WSL2`的端口映射到host win主机上。

脚本如下：

```shell
#清除网络映射
netsh interface portproxy reset
#获取WSL2实例动态IP
$wsl_ip = (wsl hostname -I).trim().split()[0]
#日志信息
Write-Host "WSL Machine IP: ""$wsl_ip"""
#网络映射win:8080 --> wsl2:8080
netsh interface portproxy add v4tov4 listenport=8080 connectport=8080 connectaddress=$wsl_ip
```

可以直接在`PowerShell`终端执行，也可以保存为`.ps1`脚本，在`PowerShell`终端执行脚本，如图：

![](/img/posts/wsl-add-port-mapping-to-win.png)

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
