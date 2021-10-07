---
layout: post
title: 'Docker指南-2021【译】'
subtitle: '从基本的概念到Docker中级，带你从零到一学习Docker。通过本指南你应该学习到：几乎基于所有平台的容器化工具的安装;上传一个自定义的Docker镜像(Image)到镜像仓库(registry);使用docker-compose协作多个容器。'
background: '/img/posts/docker-handbook-2021.jpg'
comment: false
---

# 目录

- [0. 前言](#0)
- [1. 准备](#1)
- [2. 容器化(Containerization)和Docker简介](#2)
- [3. 怎么样安装Docker](#3)
    - [3.1 怎么在macOS系统上安装Docker](#3.1)
    - [3.2 怎么在Windows系统上安装Docker](#3.2)
    - [3.3 怎么在Linux系统上安装Docker](#3.3)
- [4. Docker运行Hello World - Docker基本介绍](#4)
    - [4.1 什么是容器？](#4.1)
    - [4.2 什么是镜像？](#4.2)
    - [4.3 什么是Docker仓库（Docker Registry）？](#4.3)
- [5. Docker架构](#5)
- [6. 完整的Docker工作过程](#6)
- [7. Docker容器基本操作](#7)
    - [7.1 怎样运行一个容器？](#7.1)
    - [7.2 怎样发布一个端口？](#7.2)
    - [7.3 怎样使用后台模式？](#7.3)
    - [7.4 怎样查看容器？](#7.4)
    - [7.5 怎样命名或者重命名一个容器？](#7.5)
    - [7.6 怎样停止或者杀死一个运行中的容器？](#7.6)
    - [7.7 怎样重启一个容器？](#7.7)
    - [7.8 怎样不启动容器的情况下创建一个容器？](#7.8)
    - [7.9 怎样移除一个不用的容器？](#7.9)
    - [7.10 怎样使用命令行交互的方式启动一个容器？](#7.10)
    - [7.11 怎样在容器内执行命令？](#7.11)
    - [7.12 怎样操作可执行镜像？](#7.12)
- [8. Docker镜像基本操作](#8)
    - [8.1 怎样创建一个Docker镜像？](#8.1)
    - [8.2 怎样给镜像打标签？](#8.2)
    - [8.3 怎样展示和删除镜像？](#8.3)
    - [8.4 怎样理解多层镜像？](#8.4)
    - [8.5 怎样从源代码构建NGINX？](#8.5)
    - [8.6 怎样优化一个Docker镜像？](#8.6)
    - [8.7 拥抱Alpine Linxu](#8.7)
    - [8.8 怎样创建一个可执行Docker镜像？](#8.8)
    - [8.9 怎样在线共享你的镜像？](#8.9)
- [9. 怎样容器化一个Javascript应用？](#9)
    - [9.1 怎么写Dockerfile？](#9.1)
    - [9.2 怎么在Docker中处理Bind Mounts(绑定挂载)？](#9.2)
    - [9.3 怎么使用Docker的匿名卷？](#9.3)
    - [9.4 怎么在Docker中执行多阶段构建（Multi-Staged Builds）？](#9.4)
    - [9.5 怎么忽略掉不必要的文件？](#9.5)
- [10. Docker中的网络操作](#10)
    - [10.1 Docker网络基础](#10.1)
    - [10.2 怎样在Docker中自定义一个桥接网络？](#10.2)
    - [10.3 怎样在Docker中附接一个容器到网络中？](#10.3)
    - [10.4 怎样在Docker中将容器从网络中断开连接？](#10.4)
    - [10.5 怎样在Docker中删除网络？](#10.5)
- [11. 怎样容器化配置一个多容器JavaScript应用？](#11)
    - [11.1 怎样运行一个数据库服务？](#11.1)
    - [11.2 怎样在Docker中使用实名卷？](#11.2)
    - [11.3 怎样在Docker中查看容器的日志？](#11.3)
    - [11.4 怎样在Docker中创建一个网络并且将数据库容器附接到网络中？](#11.4)
    - [11.5 怎样写Dockerfile？](#11.5)
    - [11.6 怎样在运行的容器中执行命令？](#11.6)
    - [11.7 怎样写管理Docker的脚本？](#11.7)
- [12. 怎样使用Docker-Compose？](#12)

---

<h2 id="0">0. 前言</h2>

容器化是一个相当古老的技术，2013年Docker Engine的出现让一个应用容器化变得更加简单。

根据Stack Overflow开发者调查，2020年Docker是最被需要、最受喜爱和最流行的平台。

Docker技术如此流行，似乎我们不得不学习它。因此，本指南囊括有从基本到中级的容器化技术。通过本指南你应该学习到：几乎基于所有平台的容器化;上传一个自定义的Docker镜像(Image)到在线仓库(registry);使用docker-compose协作多个容器。

---

<h2 id="1">1. 准备</h2>

- 熟悉Linux命令行操作
- 熟悉JavaScript语言(后续的项目中使用到了JavaScript)

<h2 id="2">2. 容器化(Containerization)和Docker简介</h2>

[IBM](https://www.ibm.com/cloud/learn/containerization#toc-what-is-co-r25Smlqq)指出：容器化技术就是将软件代码和其所有的依赖打包或者封装，使其统一能够在任何平台上持续运行的技术。

换句话说，容器技术就是能够让你把软件和其依赖打包在一个自包含的包中，这样软件运行的时候就不必解决启动依赖的一些问题。

让我们考虑一个实际的生活场景，假设你开发了一个神奇的图书管理系统，你也可以向你的朋友提供书籍的借阅功能。如果把这个系统的依赖都列出来，可能是这样的：

- Node.js
- Express.js
- SQLite3

理论上来讲，应该就是这些依赖了，但是实际上并不止这些。我们知道[Node.js](https://nodejs.org/)使用一个叫做`node-gyp`的构建工具用来构建本地插件，并且根据[官方仓库](https://github.com/nodejs/node-gyp)的[安装文档](https://github.com/nodejs/node-gyp#installation)，这个构建工具需要`Python`2或者3和一个合适的c/c++编译器。

这样说来，最终的依赖列表可能是这样的：

- Node.js
- Express.js
- SQLite3
- Pytho2 or 3
- c/c++ 编译器

在任何平台安装`Python`2或者3都比较简单。在Linux平台上安装c/c++编译器比较容易，但是在Windows和Mac系统上安装就是一个痛苦的过程了。

在Windows中，C++编译器有超过1G的大小，需要花费不少的时间安装。在Mac系统中，你需要安装[Xcode](https://developer.apple.com/xcode/)或者体量小的[Xcode命令行工具](https://developer.apple.com/downloads/)。

尽管你安装好了依赖，在OS更新后，你的依赖可能被破坏。事实上，在macOS系统上，这个问题如此常见，以至于在官方的仓库中记录着[安装日志](https://github.com/nodejs/node-gyp/blob/master/macOS_Catalina.md)。

让我们假设你克服了重重困难安装好了开发应用的所有依赖，你认为现在就万事大吉了吗？还没有。

如果你的同事使用的Windows系统开发，而你使用的是macOS系统，你需要考虑两个操作系统对文件路径的差异处理。再或者说[Nginx](https://nginx.org/)并没有很好的针对Windows系统做优化。一些技术例如[Redis](https://redis.io/)甚至没有Windows系统的预编译包。

即使项目已经开发完成，如果部署人员不清楚部署的流程呢？

如果采用下面的办法，所有上面的问题都能够被解决：

- 在和你最终部署环境匹配的隔离环境（所谓的容器）中开发（部署）并运行系统。
- 把你的应用和其所有的依赖及配置打包为一个单文件（所谓的镜像）。
- 通过一个中央服务器（所谓的仓库）分享应用给有合适权限的人。

你的同事们可以从中央仓库中下载镜像，启动应用时不用担心环境的不一致的问题，甚至可以直接启动应用，因为镜像中可能已经做好了相关配置。

这就是容器化的概念：将你的应用（和依赖）打包在一个自包含的镜像中，这个镜像是轻量级的并且可以在不同的环境中复制。

那么，现在的问题是：**Docker 到底是做什么的？**

就像我上面说的，容器化技术就是通过将应用的环境、依赖和配置封装在一个黑盒子中，来解决应用部署时千千万万的问题。

容器化技术已经有一些实现，[Docker](https://www.docker.com/)是其中的一种。它是一个开源的容器平台，能够将你的应用容器化，通过私有或者公共仓库分享，并且还可以让这些[容器协作起来](https://docs.docker.com/get-started/orchestration/)。

如今，Docker不是市面上唯一的容器化工具，但它是最流行的一个。另一个我喜爱的容器化平台是红帽公司开发的[Podman](https://podman.io/)。其他的容器化工具像Google的[Kaniko](https://github.com/GoogleContainerTools/kaniko)，CoreOS的[rkt](https://coreos.com/rkt/)也很优秀，但是暂时还不是Docker的可替代的工具。

如果你想了解容器化的历史，你可以读一下[A Brief History of Containers:From the 1970s Till Now](https://blog.aquasec.com/a-brief-history-of-containers-from-1970s-chroot-to-docker-2016)这个经典介绍，里面介绍了容器化技术的重要演变过程。

---

<h2 id="3">3.怎么样安装Docker</h2>

根据操作系统的不同，安装的Docker的方法也不相同，但是总体来说，安装过程是比较简单的。

Docker能够运行在不同的主流操作系统macOS、Windows和Linux上，在这三个平台上，macOS系统上安装最容易，所以我们就从macOS系统开始。

<h3 id="3.1">3.1 怎么在macOS系统上安装Docker</h3>

在macOS上，你首先要做的就是找到官网的[下载地址](https://www.docker.com/products/docker-desktop)并下载一个mac平台的稳定版本。

你将会得到一个macOS系统的安装包并将它拖拽一个应用程序文件夹中。

![](/img/posts/docker-handbook-2021-01.jpg)

你可以双击安装包，一旦应用启动成功，你会在菜单栏中看到Docker的图标。

![](/img/posts/docker-handbook-2021-02.jpg)

现在，打开命令行终端并且执行`docker --version`和`docker-compose --version`命令来验证Docker的安装是否成功。

<h3 id="3.2">3.2 怎么在Windows系统上安装Docker</h3>

在Windows系统上除了额外的步骤要执行以外，其他的步骤和在macOS系统上大体一致。安装的步骤如下：

1. 访问[该网址](https://docs.microsoft.com/en-us/windows/wsl/install-win10)，并且按照指示在Windows10系统上安装WSL2。
2. 找到官方网站[下载页](https://www.docker.com/products/docker-desktop)，下载Windows平台的稳定版本。
3. 双击安装包，根据引导默认完成安装。

安装完成后，你可以从桌面或者开始菜单中打开Docker，你的Docker就会出现在任务栏中。

![](/img/posts/docker-handbook-2021-03_1.jpg)

现在，可以打开你从Microsoft Store中安装的Ubuntu或者任何发行版，执行`docker --version`和`docker-compose --version`命令来验证Docker的安装是否成功。

![](/img/posts/docker-handbook-2021-03.jpg)

你也可以打开cmd或者Power shell命令行终端来验证，只是在Windows上我喜欢WSL2命令行工具。

<h3 id="3.3">3.3 怎么在Linux系统上安装Docker</h3>

在Linux系统上安装Docker和上面两个系统完全不同，并且你使用Linux发行版不同，安装方式也不相同。但实际上，安装过程也很简单。

在Windows或者macOS系统上的Docker桌面安装包是包含像Docker Engine, Docker Compose, Docker Dashboard, Kubernetes等工具的集合包。然而在Linux操作系统上，没有这样捆绑一起的包，你需要手动安装你需要的工具包。不同平台的安装过程如下：

- 如果你是Ubuntu系统，你可以按照[Ubuntu官网文档指示安装Docker Engine](https://docs.docker.com/engine/install/ubuntu/)。
- 其他不同的Linux发行版，你也能从官方文档中获取安装指示。
    - [Debian系统上安装Docker](https://docs.docker.com/engine/install/debian/)
    - [Fedora系统上安装Docker](https://docs.docker.com/engine/install/fedora/)
    - [Centos系统上安装Docker](https://docs.docker.com/engine/install/centos/)
- 如果官方文档中没有你使用发行版的说明，你需要按照[二进制编译安装的指示](https://docs.docker.com/engine/install/binaries/)进行安装。
- 无论你怎么样安装，一些重要的针对[Linux系统安装后必须执行的步骤](https://docs.docker.com/engine/install/linux-postinstall/)是很重要的。
- 在你完成了Docker的安装，你还需要安装Docker Composed，你可以从官方文档中获取[安装Docker Composed的说明](https://docs.docker.com/compose/install/)。

安装完成后，打开命令行终端并且执行`docker --version`和`docker-compose --version`命令来验证Docker的安装是否成功。

![](/img/posts/docker-handbook-2021-04.jpg)

尽管Docker能够在不同的平台上使用，我还是更倾向于在Linux上使用，本书中我将使用[Ubuntu20.10](https://releases.ubuntu.com/20.10/)和[Fedora33](https://fedoramagazine.org/announcing-fedora-33/)。

另外一件事我想现在就说明的是：我没有使用任何GUI工具操作Docker。

尽管不同平台有一些很好用的GUI工具，但是学习基本命令行Docker命令是本书的主要目标之一。

---

<h2 id="4">4. Docker运行Hello World - Docker基本介绍</h2>

现在在你的机器上有一个运行中的Docker，是时候运行你的第一个容器了，打开命令行，输入如下命令：

```shell
docker run hello-world

# Unable to find image 'hello-world:latest' locally
# latest: Pulling from library/hello-world
# 0e03bdcc26d7: Pull complete 
# Digest: sha256:4cf9c47f86df71d48364001ede3a4fcd85ae80ce02ebad74156906caff5378bc
# Status: Downloaded newer image for hello-world:latest
# 
# Hello from Docker!
# This message shows that your installation appears to be working correctly.
# 
# To generate this message, Docker took the following steps:
#  1. The Docker client contacted the Docker daemon.
#  2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
#     (amd64)
#  3. The Docker daemon created a new container from that image which runs the
#     executable that produces the output you are currently reading.
#  4. The Docker daemon streamed that output to the Docker client, which sent it
#     to your terminal.
#
# To try something more ambitious, you can run an Ubuntu container with:
#  $ docker run -it ubuntu bash
# 
# Share images, automate workflows, and more with a free Docker ID:
#  https://hub.docker.com/
#
# For more examples and ideas, visit:
#  https://docs.docker.com/get-started/
```

[hello-world](https://hub.docker.com/_/hello-world)镜像是Docker提供的一个很小的容器化程序，它是很简单的[hello.c](https://github.com/docker-library/hello-world/blob/master/hello.c)程序，在终端打印出Hello Worl字符串。

在终端中，你可以执行使用`docker ps -a`命令来查看目前或者历史运行的Docker容器

```
docker ps -a

# CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                     PORTS               NAMES
# 128ec8ceab71        hello-world         "/hello"            14 seconds ago      Exited (0) 13 seconds ago                      exciting_chebyshev
```

输出结果中，镜像hello-world对应有一个命名为`exciting_chebyshev`的容器，容器的ID为`128ec8ceab71`，还有一个`Exited（0）13 seconds ago`的状态表示容器运行的过程中没有产生错误。

为了能够理解刚才屏幕中输出的内容，必须要了解Docker的架构和容器化技术的三个基本概念，如下：

- [容器](https://www.freecodecamp.org/news/@fhsinchy/s/the-docker-handbook/~/drafts/-MS1b3opwENd_9qH1jTO/hello-world-in-docker#container)
- [镜像](https://www.freecodecamp.org/news/@fhsinchy/s/the-docker-handbook/~/drafts/-MS1b3opwENd_9qH1jTO/hello-world-in-docker#image)
- [仓库](https://www.freecodecamp.org/news/@fhsinchy/s/the-docker-handbook/~/drafts/-MS1b3opwENd_9qH1jTO/hello-world-in-docker#registry)

我按照字母表的顺序开始第一个重要概念的讲解：

<h3 id="4.1">4.1 什么是容器？</h3>

在容器化技术中，没有比容器更基本的概念了。

[Docker官方文档](https://www.docker.com/resources/what-container)中是这样说的：

> 容器是可以将应用和其依赖打包在一起的应用层的一种抽象。与虚拟化整个硬件不同，容器仅仅将宿主操作系统虚拟化。

你可以认为容器化技术是下一代的虚拟化技术。

就像虚拟机一样，容器之间以及容器和宿主机之间环境都是彼此隔离的。相比较虚拟机，容器也更加轻量级，因此同一个宿主机上可以同时跑多个容器，并且不影响宿主机的性能。

容器和虚拟机使用不同的方法虚拟化硬件，两者的主要不同是虚拟化方法的不同。

虚拟机通常被一个叫做Hypervisor的程序创建并管理，例如[Oracle VM VirtualBox](https://www.virtualbox.org/)、[VMware](https://www.vmware.com/)、[KVM](https://www.linux-kvm.org/)和微软[Hyper-V](https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/about/)等等。这个hypervisor程序处在宿主机操作系统和虚拟机之间，承担中间通信的职责。

![](/img/posts/docker-handbook-2021-07.svg)

在虚拟机中运行的程序和本地操作系统（gust operating system）通信，本地操作系统和hypervisor程序通信，hypervisor程序再向宿主机操作系统从硬件中申请必要的资源来运行程序。

从上面可以看出，虚拟机中运行的程序和宿主基础硬件之间有一个长长的通信链，即使是虚拟机中程序申请很小的资源，由于本地操作系统的存在也增加了明显的性能消耗。

和虚拟机使用的虚拟方法不一样，容器使用更加聪明的方式。容器没有完整的本地操作系统，它通过运行时的容器服务使用宿主机操作系统，同时就像虚拟机那样保持环境的隔离性。

![](/img/posts/docker-handbook-2021-08.svg)

运行时的容器服务，也就是Docker，处在宿主机操作系统和容器之间，容器通过Docker和宿主机操作系统进行通信，从基础物理硬件获取程序运行的资源。

容器由于取消了完整的本地操作系统，相比虚拟机更加轻量级和资源少消耗。

为了验证这一点，执行下面的代码：

```
uname -a
# Linux alpha-centauri 5.8.0-22-generic #23-Ubuntu SMP Fri Oct 9 00:34:40 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux

docker run alpine uname -a
# Linux f08dbbe9199b 5.8.0-22-generic #23-Ubuntu SMP Fri Oct 9 00:34:40 UTC 2020 x86_64 Linux
```

上面的代码中，我先在宿主机上执行了`uname -a`命令，获取宿主机操作系统的内核详情。然后第二行运行了一个[Alpine Linux](https://alpinelinux.org/)容器执行了同样的命令。

从输出的结果可以看出来，容器实际上使用了宿主机操作系统的内核，这也证明了容器虚拟化了宿主机的操作系统而不是自身也拥有一个。

如果你使用Windows机器，你会发现所有的容器都是使用WSL2内核，这是因为WSL2Windows上Docker的后台服务。在macOS系统上，默认的后台服务是一个基于[HiperKit](https://github.com/moby/hyperkit) hypervisor的VM。

<h3 id="4.2">4.2 什么是镜像？</h3>

镜像是分层的，自包含的，用来创建容器的模版源文件。它们可以通过镜像仓库共享。

过去，不同的容器引擎需要不同的镜像格式。后来，[Open Container Initiative（OCI）](https://opencontainers.org/)定义了标准的容器镜像规范，大部分的主流容器化平台都遵循这一规范，这也意味着在Docker上构建的镜像在没有任何修改的情况下可以直接在Podman容器平台上使用。

容器其实就是运行时的镜像，当你从互联网上获取一个镜像并且运行这个镜像的时候，你实际上基于只读镜像层新创建了一个临时可写入层。

镜像的概念随着本书的深入会越来越清晰，现在需要记住的是，镜像是一个多层次的、只读的，将你的应用打包为系某一状态的文件。

<h3 id="4.3">4.3 什么是Docker仓库（Docker Registry）？</h3>

我们已经学习了两个非常重要的概念，容器和镜像，剩下的就是Docker仓库的概念了。

镜像仓库是一个你能够上传并且下载别人上传镜像的中央镜像管理处。[Docker Hub](https://hub.docker.com/)是Docker官方默认的镜像仓库，另外一个流行的镜像仓库是Red Hat的[Quay](https://quay.io/)。

本书中，我们使用Docker Hub作为我们的镜像仓库。

![](/img/posts/docker-handbook-2021-11.jpg)

你可以免费在Docker Hub上分享你的公开镜像，互联网上的人们可以从那里免费下载它们。我上传的镜像可以在我的主页（[fhsinchy](https://hub.docker.com/u/fhsinchy)）中找到。

![](/img/posts/docker-handbook-2021-12.jpg)

除了Docker Hub和Quay，你也可以创建私有的镜像仓库。实际你本地也运行着一个私有仓库，用来存储从远端仓库拉下来的镜像。

---

<h2 id="5">5. Docker架构</h2>

现在你已经学习了容器化技术和Docker的基本概念，是时候学习Docker是怎么工作的。

Docker引擎包含三个主要部分：

1. **Docker Daemon**：daemon进程（`dockerd`）在后台运行并且监听客户端的请求，它能够管理各种各样的Docker对像。
2. **Docker Client**：客户端（`docker`）是一个命令行接口程序，主要负责传递用户的请求。
3. **REST API**：REST接口是Docker后台程序和客户端之间的桥梁，用户输入的任务命令行请求都会通过接口传递到后代Docker Daemon那里。

根据官方[文档](https://docs.docker.com/get-started/overview/#docker-architecture)：

> Docker使用C/S的架构，客户端向服务端通信，服务端处理构建、运行和分发容器的工作。

你作为用户使用Docker客户端输入命令，客户端使用REST API将命令转发给后台守护进程dockerd，并由后台进程完成工作。

---

<h2 id="6">6. 完整的Docker工作过程</h2>

好的，基础的概念已经了解了，我们开始把所学习的一切串起来，看看它们合在一起是怎么工作的。在我们详细讲解`docker run hello-world`命令背后到底发生什么之前，先看看我画的一张图：

![](/img/posts/docker-handbook-2021-12.svg)

这张图基于Docker官方网站的一张稍微作出改动，当你执行命令的时候，发生的事情如下：

1. 你执行docker run hello-world命令，这里的hello-world是Docker镜像的名字。
2. Docker客户端转发命令到Docker后台进程，说要获取hello-world镜像并创建一个容器。
3. Docker后台进程从本地仓库中寻找镜像，但是没有找到，日记中会打印出：`Unable to find image 'hello-world':latest`。
4. Docker后台进程会去默认的Docker Hub仓库中下载最新的镜像，日志中会打印出：`latest: pulling from library/hello-world`。
5. Docker后台进程基于下载的镜像创建一个容器。
6. 最后，Docker后台进程运行这个容器，并输出结果到命令行终端上。

Docker在本地找不到拉取镜像的时候，默认会去Docker Hub仓库下载。一旦镜像被下载，就会缓存到本地仓库。所以，你重新执行命令，就看不到如下的日志输出：

```
Unable to find image 'hello-world:latest' locally
latest: Pulling from library/hello-world
0e03bdcc26d7: Pull complete
Digest: sha256:d58e752213a51785838f9eed2b7a498ffa1cb3aa7f946dda11af39286c3db9a9
Status: Downloaded newer image for hello-world:latest
```

如果在公共的Docker仓库有一个新的镜像版本，Docker后台程序会重新获取镜像，这里的`:latest`是一个标签。Docker镜像会有一个有意义标签表示版本或者构建。关于标签我们在后续会做详细介绍。

---

<h2 id="7">7. Docker容器基本操作</h2>

在前面的章节中，我们学习了Docker的基础概念并且使用docker run命令运行了一个Docker容器。

在这一部分中，你们将会学习到详细的Docker容器操作。容器操作是你每天必须执行的基本任务，所以正确的理解各种命令很关键。

要知道的是，这里列出来的Docker命令不是全部的，而是一些最基本的命令。如果想要学习更多Docker支持的命令，可以访问官方文档的[命令行说明](https://docs.docker.com/engine/reference/commandline/container/)。

<h3 id="7.1">7.1 怎样运行一个容器？</h3>

之前的章节我们基于hello-world镜像，使用docker run命令启动了一个容器。基本的命令行语法是这样的：

`docker run <image-name>`

尽管这是一个很有效命令，Docker提供了更好的方式，可以将客户端命令传递给Docker后台进程。

在Docker1.13之前，Docker只支持上面语法的命令。后来，Docker[重构](https://www.docker.com/blog/whats-new-in-docker-1-13/)了命令行语法：

`docker <object> <command> <options>`

在这个语法中：

- object表示你要操作的Docker对象，可以是container、image、network和volume对象。
- command表示Docker后台进程要执行的内容，比如说run。
- options可以是任何有效的覆盖默认行为的参数，例如`--publish`参数表示端口映射。

参照上面的语法，run命令可以写成：

`docker container run <image-name>`

image name可以是任何本地或者远程仓库上的镜像名称。例如，你也可以使用[fhsinchy/hello-dock](https://hub.docker.com/r/fhsinchy/hello-dock)这个镜像名称，这个镜像包含了一个简单的[vue.js](https://vuejs.org/)应用，运行容器内应用启动并监听80端口。想要运行这个镜像的容器，执行下面的命令：

```
docker container run --publish 8080:80 fhsinchy/hello-dock

# /docker-entrypoint.sh: /docker-entrypoint.d/ is not empty, will attempt to perform configuration
# /docker-entrypoint.sh: Looking for shell scripts in /docker-entrypoint.d/
# /docker-entrypoint.sh: Launching /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh
# 10-listen-on-ipv6-by-default.sh: Getting the checksum of /etc/nginx/conf.d/default.conf
# 10-listen-on-ipv6-by-default.sh: Enabled listen on IPv6 in /etc/nginx/conf.d/default.conf
# /docker-entrypoint.sh: Launching /docker-entrypoint.d/20-envsubst-on-templates.sh
# /docker-entrypoint.sh: Configuration complete; ready for start up
```

命令行很好理解，需要解释的`--publish 8080:80`参数会在下一部分内容中解释。

<h3 id="7.2">7.2 怎样发布一个端口？</h3>

容器是隔离的环境。容器的宿主机并不知道容器内发生的一切。因此，容器外部是不能直接访问容器内部的。

如果想要容器外部访问容器，需要将容器内部的端口发布到容器宿主机的网路上，语法`--publish`和`-p`如下：

`--publish <host port>:<container port>`

当你像刚才命令中那样指定`--publish 8080:80`参数，意味着任何访问宿主机8080端口的请求都会被转发到容器内到80端口。

现在可以在浏览器中访问http://127.0.0.1:8080。

![](/img/posts/docker-handbook-2021-15.jpg)

你可以使用ctrl + c命令停止容器，命令行终端将会停止进程或者关闭整个终端。

<h3 id="7.3">7.3 怎样使用后台模式？</h3>

另外一个`run`命令常用的命令行参数是`--detach`或者`-d`。在上面的操作中，容器如果想保持运行状态，必须要保持命令行窗口打开的状态。关闭命令行窗口，容器就会被停掉。

这是因为，默认容器在前台启动，并且会像其他程序一样绑定在调用方终端上。

我们可以通过`--detach`参数让容器在后台启动，命令如下：

```
docker container run --detach --publish 8080:80 fhsinchy/hello-dock

# 9f21cb77705810797c4b847dbd330d9c732ffddba14fb435470567a7a3f46cdc
```

和之前不一样，你的命令行终端上打印出了容器的ID。

参数的顺序先后没有关系，如果你把`--publish`参数放在了`--detach`参数之前，容器依然可以正常启动。对于`docker run`命令，只要记住镜像的名字是放在最后的就可以，如果镜像的名字后面还有内容，会被作为容器entry-point（见[在一个容器中执行命令](https://fengmengzhao.github.io/2021/06/25/docker-handbook-2021.html#executing-commands-inside-a-container)模块）的参数传递，可能会得到意想不到的结果。

<h3 id="7.4">7.4 怎样查看容器？</h3>

`container ls`命令可以展示出正在运行中的容器，命令如下：

```
docker container ls

# CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                  NAMES
# 9f21cb777058        fhsinchy/hello-dock   "/docker-entrypoint.…"   5 seconds ago       Up 5 seconds        0.0.0.0:8080->80/tcp   gifted_sammet
```

一个名称为`gifted_sammet`的容器在运行中，它再5秒钟之前被创建，并且已经正常启动了5秒钟。

容器的ID是`9f21cb777058`，这个ID是完整容器ID的前12个字符，完整的ID是`9f21cb77705810797c4b847dbd330d9c732ffddba14fb435470567a7a3f46cdc`，含有64个字符。当前面使用`docker container run`运行容器的时候，完整的容器ID就输出在了控制台。

`port`列表示宿主机8080端口执行容器的80端口。`gifted_sammet`名字是docker生成的，根据平台不同，这个名字可能也不同。

`container ls`命令仅仅展示出了目前正在运行的容器，如果想列出来历史运行过的容器，使用`--all`或者`-a`参数。

```
docker container ls --all

# CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS                     PORTS                  NAMES
# 9f21cb777058        fhsinchy/hello-dock   "/docker-entrypoint.…"   2 minutes ago       Up 2 minutes               0.0.0.0:8080->80/tcp   gifted_sammet
# 6cf52771dde1        fhsinchy/hello-dock   "/docker-entrypoint.…"   3 minutes ago       Exited (0) 3 minutes ago                          reverent_torvalds
# 128ec8ceab71        hello-world           "/hello"                 4 minutes ago       Exited (0) 4 minutes ago                          exciting_chebyshev
```

可以看出来，第二个名字为`reverent_torvalds`的容器早些时候运行过，退出时候exit code为0，标识容器运行的时候没有产生错误。

<h3 id="7.5">7.5 怎样命名或者重命名一个容器？</h3>

每一个容器默认都有两个标识，它们是：

- 容器ID - 64位长度的字符串。
- 容器名称 - 用下划线连接的两个随机单词。

使用随机生成的容器名称来指代容器很不方便，我们也可以自定义容器的名称。

通过参数`--name`可以定义容器名称，基于镜像`fhsinchy/hello-dock`启动另外一个名称为`hello-dock-container`的容器，使用如下命令：

```
docker container run --detach --publish 8888:80 --name hello-dock-container fhsinchy/hello-dock

# b1db06e400c4c5e81a93a64d30acc1bf821bed63af36cab5cdb95d25e114f5fb
```

8080的本地端口被我们之前启动的容器占用着，因此我们使用了一个新的端口8888。可以查看下启动的容器：

```
docker container ls

# CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                  NAMES
# b1db06e400c4        fhsinchy/hello-dock   "/docker-entrypoint.…"   28 seconds ago      Up 26 seconds       0.0.0.0:8888->80/tcp   hello-dock-container
# 9f21cb777058        fhsinchy/hello-dock   "/docker-entrypoint.…"   4 minutes ago       Up 4 minutes        0.0.0.0:8080->80/tcp   gifted_sammet
```

名称为`hello-dock-container`的容器处于运行中状态。

你也可以使用`docker container rename`命令重命名一个容器，语法如下：

`docker container rename <container identifier> <new name>`

重命名之前的名称为gifted_sammet的容器为hello-dock-container-2，命令如下：

`docker container rename gifted_sammet hello-dock-container-2`

这个命令执行没有任何输出结果，不过你可以通过查看容器列表`container ls`确实是否修改成功

无论容器在运行态或者停止态，`rename`命令都可以使用。

<h3 id="7.6">7.6 怎样停止或者杀死一个运行中的容器？</h3>

前台运行的容器可以通过关闭终端命令行或者按键ctrl + c来停止运行。后台运行的容器需要使用不同的方法。

有两个命令可以停止容器，第一个是`container stop`命令，基本的语法是：

`docker container stop <container identifier>`

这里的container identifier可以是容器的名称或者ID。

应该还记得我们之前启动的容器还在后台运行着，通过container ls查看容器的标识（这里我们以hello-dock-container作为示例）。执行下面的命令来停止容器运行：

```
docker container stop hello-dock-container

# hello-dock-container
```

如果你使用容器的名称作为标识，容器停止后控制台会将名字输出。stop命令通过发送`SIGTERM`信号优雅的关闭掉了容器。如果容器在一段时间内没有停掉，则会发送一个`SIGKILL`信号，立即停止容器。

如果你想发送`SIGKILL`而不是`SIGTERM`信号，你可以使用container kill命令，命令的语法如下：

```
docker container kill hello-dock-container-2

# hello-dock-container-2
```

<h3 id="7.7">7.7 怎样重启一个容器？</h3>

这里说的重启，有两种场景：

- 重启之前已经停掉或者被杀死的容器
- 重启一个运行中的容器

在上面的章节中，我们系统中有停掉的容器，可以使用`container start`命令来启动停掉或者被杀死的容器，语法如下：

`docker container start <container identifier>`

可以使用`container ls --all`命令列出来所有的容器，找到状态是`Exited`的。

```
docker container ls --all

# CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS                        PORTS               NAMES
# b1db06e400c4        fhsinchy/hello-dock   "/docker-entrypoint.…"   3 minutes ago       Exited (0) 47 seconds ago                         hello-dock-container
# 9f21cb777058        fhsinchy/hello-dock   "/docker-entrypoint.…"   7 minutes ago       Exited (137) 17 seconds ago                       hello-dock-container-2
# 6cf52771dde1        fhsinchy/hello-dock   "/docker-entrypoint.…"   7 minutes ago       Exited (0) 7 minutes ago                          reverent_torvalds
# 128ec8ceab71        hello-world           "/hello"                 9 minutes ago       Exited (0) 9 minutes ago                          exciting_chebyshev
```

现在可以重启`hello-dock-container`容器，执行以下命令：

```
docker container start hello-dock-container

# hello-dock-container
```

可以通过`container ls`命令展示运行中的容器，验证容器是否启动成功。

`container start`命令默认情况下，保留之前的端口配置启动任何后台容器，所以如果你现在访问`http://127.0.0.1:8080`，就能够像之前那样访问到`hello-dock`应用。

![](/img/posts/docker-handbook-2021-16.jpg)

现在说下重启运行中容器的场景，要用到`container restart`命令，语法和`container start`命令类似。

```
docker container restart hello-dock-container-2

# hello-dock-container-2
```

二者不同的地方在于，重启容器（restart）是先停掉容器在启动，而启动（start）容器就直接启动。

对于停止状态的容器，二者都可以使用。但是对于运行中的容器，只能使用`docker restart`命令。

<h3 id="7.8">7.8 怎样不启动容器的情况下创建一个容器？</h3>

目前，我们学习用`docker run`命令启动一个容器。实际上，这个命令包含两部分：

- `container create`命令，基于一个镜像创建一个容器。
- `container start`命令，启动刚刚创建的容器。

我们可以像[怎样运行一个容器](#7.1)章节那样，分两个步骤，启动一个容器：

```
docker container create --publish 8080:80 fhsinchy/hello-dock

# 2e7ef5098bab92f4536eb9a372d9b99ed852a9a816c341127399f51a6d053856

docker container ls --all

# CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS               NAMES
# 2e7ef5098bab        fhsinchy/hello-dock   "/docker-entrypoint.…"   30 seconds ago      Created                                 hello-dock
```

通过上面`container ls --all`命令展示所有容器，我们看到一个基于镜像`fhsinchy/hello-dock`的名称是`hello-dock`的容器。目前容器的状态是`Created`，说明这个容器没有运行，没有参数`--all`容器也不会展示出来。

容器创建后，我们可以用`container start`命令来启动它：

```
docker container start hello-dock

# hello-dock

docker container ls

# CONTAINER ID        IMAGE                 COMMAND                  CREATED              STATUS              PORTS                  NAMES
# 2e7ef5098bab        fhsinchy/hello-dock   "/docker-entrypoint.…"   About a minute ago   Up 29 seconds       0.0.0.0:8080->80/tcp   hello-dock
```

容器的状态从`Created`变为了`Up 29 seconds`，表明容器现在是运行的状态。之前空着的PORTS列也有了数据。

<h3 id="7.9">7.9 怎样移除一个不用的容器？</h3>

一个被停掉或者杀死的容器还会停留在系统中，这些不用的容器会占用空间并且可能会很新的容器冲突。

可以是`container rm`移除停掉的容器，语法如下：

`docker container rm <container-identifier>`

想找出来哪些容器不是运行的状态，可以使用`container ls --all`命令并寻找`Exited`状态的容器。

```
docker container ls --all

# CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS                      PORTS                  NAMES
# b1db06e400c4        fhsinchy/hello-dock   "/docker-entrypoint.…"   6 minutes ago       Up About a minute           0.0.0.0:8888->80/tcp   hello-dock-container
# 9f21cb777058        fhsinchy/hello-dock   "/docker-entrypoint.…"   10 minutes ago      Up About a minute           0.0.0.0:8080->80/tcp   hello-dock-container-2
# 6cf52771dde1        fhsinchy/hello-dock   "/docker-entrypoint.…"   10 minutes ago      Exited (0) 10 minutes ago                          reverent_torvalds
# 128ec8ceab71        hello-world           "/hello"                 12 minutes ago      Exited (0) 12 minutes ago                          exciting_chebyshev
```

从上面我们能找出来，容器ID是`6cf52771dde1`和`128ec8ceab71`的容器不是运行的状态，移除容器ID`6cf52771dde1`的容器，执行命令：

```
docker container rm 6cf52771dde1

# 6cf52771dde1
```

可以使用`container ls --all`来验证容器是否被移除。如果要批量移除容器，可以一次性将容器的标识用空格隔开传递给命令。

或者，你可以不用单个的移除容器，使用`container prune`命令可以一次性移除所有停掉或者退出的容器。

使用`container ls --all`命令验证容器是否被移除：

```
docker container ls --all

# CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                  NAMES
# b1db06e400c4        fhsinchy/hello-dock   "/docker-entrypoint.…"   8 minutes ago       Up 3 minutes        0.0.0.0:8888->80/tcp   hello-dock-container
# 9f21cb777058        fhsinchy/hello-dock   "/docker-entrypoint.…"   12 minutes ago      Up 3 minutes        0.0.0.0:8080->80/tcp   hello-dock-container-2
```

如果严格按照本书的执行，执行到了这里，应该能够看到列表中的另个容器：`hello-dock-container`和`hello-dock-container-2`。建议进行下面的内容之前将这两个容器移除。

`container run`命令和`container start`命令也有一个参数`--rm`，表示一旦容器停掉就删除容器。可以使用`--rm`参数启动另一个`hello-dock`容器。

```
docker container run --rm --detach --publish 8888:80 --name hello-dock-volatile fhsinchy/hello-dock

# 0d74e14091dc6262732bee226d95702c21894678efb4043663f7911c53fb79f3
```

使用`container ls`命令查看容器是否启动：

```
docker container ls

# CONTAINER ID   IMAGE                 COMMAND                  CREATED              STATUS              PORTS                  NAMES
# 0d74e14091dc   fhsinchy/hello-dock   "/docker-entrypoint.…"   About a minute ago   Up About a minute   0.0.0.0:8888->80/tcp   hello-dock-volatile

```

现在可以停掉这个容器并使用`container ls --all`名称查看确认：

```
docker container stop hello-dock-volatile

# hello-dock-volatile

docker container ls --all

# CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

可以看见容器被自动删除了。后面我们基本上都会用上`--rm`参数，不需要的地方会特殊说明。

<h3 id="7.10">7.10 怎样使用命令行交互的方式启动一个容器？</h3>

目前我们基于[hello-world](https://hub.docker.com/_/hello-world)和[fhsinchy/hello-dock](https://hub.docker.com/r/fhsinchy/hello-dock)镜像创建并运行容器，这些都是简单程序的镜像，不需要命令行交互。

镜像不都是这么简单的，它可以封装进去整个Linux的发行版。

流行的Linux发型版，例如[Ubuntu](https://ubuntu.com/)、[Fedora](https://fedora.org/)和[Debian](https://debian.org/)在官方的仓库中都有Docker镜像。编程语言，像[python](https://hub.docker.com/_/python)、[php](https://hub.docker.com/_/php)、[go](https://hub.docker.com/_/golang)，过着运行时环境[node](https://hub.docker.com/_/node)和[deno](https://hub.docker.com/r/hayd/deno)都有自己的官方镜像。

这些镜像不仅仅是做好了提前的配置，默认会配置执行一个shell。对于操作系统镜像来说，可能是一个`sh`或者`bash`，对于一个编程语言或者运行时环境，可能是语言自身的shell。

我们都知道，shell是命令行交互式程序。如果一个镜像配置执行这样一个程序，这样的镜像称之为交互式的镜像。他们在启动容器`docker run`的时候需要一个`-it`参数。

举一个例子，如果你使用`docker container run ubuntu`启动一个ubuntu镜像的容器，命令行上你会看不到任何输出。但是如果你加上`-it`参数，你就能够直接在命令行中操作这个Ubuntu容器。

````
docker container run --rm -it ubuntu

# root@dbb1f56b9563:/# cat /etc/os-release
# NAME="Ubuntu"
# VERSION="20.04.1 LTS (Focal Fossa)"
# ID=ubuntu
# ID_LIKE=debian
# PRETTY_NAME="Ubuntu 20.04.1 LTS"
# VERSION_ID="20.04"
# HOME_URL="https://www.ubuntu.com/"
# SUPPORT_URL="https://help.ubuntu.com/"
# BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
# PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
# VERSION_CODENAME=focal
# UBUNTU_CODENAME=focal
````

上面，我们执行命令`cat /etc/os-release`得到相应的输出，说明我们确实是和Ubuntu容器在进行交互。

`-it`参数能够让你和容器内的交互式程序进行交互。这个参数实际上是有两部分组成在一起。

- `-i`参数或者`--interactive`绑定连接终端到容器的输入流中，因此你可以在终端中输入。
- `-t`或者`--tty`参数保证你获取格式化后的内容，并且通过分配一个伪tty让你有一个很好的终端体验。

当你像以命令行交互的方式运行一个容器的时候，你需要使用`-it`。你可以这样启动node容器：

```
docker container run -it node

# Welcome to Node.js v15.0.0.
# Type ".help" for more information.
# > ['farhan', 'hasin', 'chowdhury'].map(name => name.toUpperCase())
# [ 'FARHAN', 'HASIN', 'CHOWDHURY' ]
```

任何有效的JavaScript代码都可以在这个终端中运行。`-it`是缩写，我们也分别可以写成`--interactive --tty`。

<h3 id="7.11">7.11 怎样在容器内执行命令？</h3>

在上面的章节，我们运行了一个Alpine Linux容器，并且执行了一个命令：

```
docker run alpine uname -a
# Linux f08dbbe9199b 5.8.0-22-generic #23-Ubuntu SMP Fri Oct 9 00:34:40 UTC 2020 x86_64 Linux
```

在上面的命令中，我在容器Alpine Linux容器中执行了`uname -a`命令。像这种的场景（启动一个容器的时候，需要在容器内执行一个命令）很常见。

假设你想base64一个字符串，这在Linux或者Unix系统中很容易做到（不包括Windows系统）。

你可以快速的启动一个基于[busybox](https://hub.docker.com/_/busybox)的镜像的容器，让这个容器做base64的工作。

把一个字符串base64的基本语法是：

```
echo -n my-secret | base64

# bXktc2VjcmV0
```

将命令传递给没有在运行的容器的基本语法是：

`docker container run <image name> <command>`

使用`busybox`镜像运行容器，并让容器执行base64命令，可以这样启动：

```
docker container run --rm busybox echo -n my-secret | base64

# bXktc2VjcmV0
```

这里的处理逻辑上，`docker run`命令任何在image name后面的内容都作为参数传递给容器的默认`entry point`。

所谓的`entry point`就是一个镜像的入口。除了可执行镜像（在下面[怎样操作可执行镜像](#7.12)章节说明）外，大部分镜像使用shell或者`sh`作为默认的`entry point`。因此任何有效的shell命令都可以作为参数传递。

<h3 id="7.12">7.12 怎样操作可执行镜像？</h3>

之前我们有简单提到可执行容器，这些容器的目的是样程序一样可执行。

看一看我的[rmbyext](https://github.com/fhsinchy/rmbyext)项目，这是一个简单的Python脚本，能够递归删除给定扩展名的文件。可以访问仓库了解更详细的信息。

如果你已经安装了Git和Python，可以执行下面的命令安装这个脚本：

`pip install git+https://github.com/fhsinchy/rmbyext.git#egg=rmbyext`

如果你已经配置好了Python环境命令，在任意的命令行中，都可以使用一下命令：

`rmbyext <file extension>`

为了测试，在一个空目录中打开命令行并以不同的扩展创建多个文件。你可以使用touch命令来这样做，现在我们有了一个目录，目录中有如下文件：

```
touch a.pdf b.pdf c.txt d.pdf e.txt

ls

# a.pdf  b.pdf  c.txt  d.pdf  e.txt
```

要删除所有的pdf文件，可以执行如下的命令：

`rmbyext pdf`

一个可执行的镜像也应该像`rmbyext`脚本文件一样，接收一个文件后缀的参数，能够删除后缀结尾的文件。

[fhsinchy/rmbyext](https://hub.docker.com/r/fhsinchy/rmbyext)镜像和上面的程序类似，它包含了`rmbyext`脚本，并且配置了执行脚本时删除容器内`/zone`目录下的文件。

现在问题是容器和宿主机环境之间是隔离的，因此容器内运行的`rmbyext`程序不能够访问到宿主机的文件系统。如果我们能够将宿主机的目录映射到容器内的`/zone`目录，本地的文件就可以被宿主机访问到了。

使容器能够访问到宿主机文件系统的一种方法是绑定挂载点（bind mount）。

bind mount能够实现容器宿主机（源端）目录和容器（目标端）目录的内容保持一致，源端和目标端目录内和和的修改都会彼此同步。

我们来使用一下bind mount，不直接使用脚本删除文件，使用镜像来进行操作：

COPY

你可能已经猜到，我们在命令中使用了`-v $(pwd):/zone`参数。`-v`或者`--volume`参数用来为容器绑定一个挂载点，这个参数可以用冒号隔开的三部分组成，基本的语法是：

`--volume <local file system directory absolute path>:<container file system directory path>:<read write access>`

参数中前两部分是必选的，第三部分可选。

我们例子中的源端目录是`/home/fhsinchy/the-zone`，这个目录在命令行中是打开的，`$(pwd)`表示了之前提到的包含.pdf和.txt文件的当前工作目录。

你可以在这里学习到更多的命令行用法。

`-v`或者`--volume`参数对于`container run`命令是有效的，同样对于`container create`也是有效的。我们将在接下来的章节中详细研究volume这个概念，所以现在如果不太理解，不用太担心。

可执行的镜像和常规镜像不同的地方是，可执行镜像的入口是执行一个程序而不是shell。在`rmbyext`示例中，我们之前也说过，任何在镜像名称后面的内容都会作为参数传递给容器的入口。

因此最后我们的`docker container run --rm -v $(pwd):/zone fhsinchy/rmbyext pdf`命令将`rmbyext pdf`程序转化了可执行镜像在容器内运行。可执行镜像实际上并不常见，但是有时候会非常有用。

---

<h2>8. Docker镜像基本操作</h2>

目前为止，我么学会了怎样启动一个镜像。接下来学习怎样创建你自己的镜像。

这部分中，我们将会学习基本的创建一个镜像，运行这个镜像和在线分享镜像。

我建议你下载Visual Studio Code的Docker官方插件，这样能极大有利于你的开发。

<h3>8.1 怎样创建一个Docker镜像？</h3>

在hello-world章节我们有解释过，Docker镜像是多层次的、自包含的文件，是可以用来创建Docker容器的模版，就像是静态的容器的一份克隆。

在把你的程序创建成一个镜像之前，你必须要清楚定义这个镜像的版本。例如，基于Nginx的官方镜像，你可以通过如下命令启动容器：

COPY

现在，你可以在浏览器中访问`http://127.0.0.1:8080`，你会得到一个默认的响应界面。

![](/img/posts/docker-handbook-2021-17.jpg)

这很好，但是如果你想自定义制作一个像官方那样的Nginx镜像呢？坦白来说，这种场景是会遇到的。我们来学习怎么制作它。

为了制作一个自定的Nginx镜像，必须要清楚镜像的最终状态是什么样的，在我看来应该是这样的：

- 镜像中的Nginx一定是安装好的，可以通过包管理命令行或者源代码编译的方式完成。
- 镜像启动的时候，Nginx应该自动启动。

很简单的，如果你克隆了书中链接的仓库，可以进入克隆的项目目录，找到一个`custom-nginx`的目录。

现在，在那个目录中创建一个名称为Dockerfile的文件，文件中定义的命令会执行怎样创建一个容器。Dockerfile的内容是：

COPY

镜像是多层的文件系统，在Dockerfile文件中，你写的每一行（称之为instructions）为镜像创建了一层。

- 每一个有效的Dockerfile都以`FROM`命令开头，该命令设定了你目标镜像的基础镜像。通过设置`ubuntu:latest`基础镜像，你可以在自定义的镜像中获取ubuntu系统的完美功能，你可以使用`apt-get`命令方便的进行包的安装。
- `EXPOSE`命令表示端口需要被发布。使用该指标后你仍然需要使用`--publish`命令发布端口，它仅仅起到了一个文档的作用来指示运行你镜像的人。它还有其他的作用，在这里就不讨论了。
- Dockerfile中的`RUN`命令在容器的shell中执行命令。`apt-get update && apt-get install nginx -y`命令首先更新包版本然后安装nginx。`apt-get clean && rm -rf /var/lib/apt/list/*`命令用来清理包缓存，因为你不需要在镜像中有不必要的垃圾。这两个命令在Ubuntu系统中是两个常见的操作，没什么特殊的。这里的`RUN`命令写成了`shell`的形式，它也可以写成`shell`的形式，你可以访问官方的文档获取更多的信息。
- 最后`CMD`命令设置镜像默认执行的命令。命令以`exec`的形式包含了三个部分。`nginx`表示可执行的nginx程序，`-g`和`daemon off`是nginx程序的可选参数表示让nginx以单进程的方式在容器内运行，`CMD`也可以用`shell`的形式来写，你可以官方文档获取更多信息。

现在，你有一个有效的Dockerfile可以用来创建镜像。可以容器相关的命令一样，镜像相关的命令语法如下：

`docker image <command> <options>`

想要创建你刚刚写的Dockerfile的镜像，在`custom-nginx`目录中打开终端，执行如下命令：

`docker image build .`

COPY

为了执行镜像的构建，后台进程需要知道特定的信息，比如Dockerfile的名称和执行的上下文，命令的含义是：

- `docker image build`是构建镜像的命令，后台进程会在上下中寻找名称为Dockerfile的文件。
- 命令末尾的`.`设置build的上下文，也就是后台进程在构建过程中能访问的目录。

现在，你可以运行你刚刚构建的镜像。你可以使用`container run`命令和刚才构建镜像时得到的构建进程返回的镜像ID一起使用。我执行返回的ID是COPY，表示镜像成功构建。

COPY

你可以访问`http://127.0.0.1:8000`验证容器时否启动成功。

![](/img/posts/docker-handbook-2021-18.jpg)

<h3>8.2 怎样给镜像打标签？</h3>

和容器一样，你可以不使用随机生成的ID而给镜像设置自定义的标识。对于一个镜像来说，这叫做打标签而不是重命名。`--tag`或者`-t`参数可以用来打标签。

通用的语法如下：

`--tag <image repository>:<image tag>`

repository是镜像的名称，image tag表示一个特定的构建或者版本。

拿官方镜像mysql为例，如果你想让运行特定版本，例如5.7的mysql容器，你可以执行`docker container run mysql:5.7`，这里mysql就是镜像名称，5.7是对应的标签。

如果你想定义custom-nginx:packaged这样自定义的标签，你可以在执行构建命令的时候这样：

COPY

现在你可以使用custom-nginx标识代替随机的串来引用你的镜像。

如果你在构建的时候忘记给镜像打标签，可以使用`image tag`命令重打标签：

`docker image tag <image id> <image repository>:<image tag>`

或者使用

`docker image tag <image repository>:<image tag> <new image repository>:<new image tag>`

<h3>8.3 怎样展示和删除镜像？</h3>

和`container ls`命令一样，你可以使用`image ls`命令展示出你本地的所有镜像：

COPY

这里展示的镜像可以使用`image rm`命令删除，基本的语法如下：

`docker image rm <image identifier>`

这里镜像的标识可以镜像ID，也可以是镜像名称，如果是名称的话必须带上标签。例如要删除custom-nginx:package镜像，你可以执行如下命令：

`docker image rm custom-nginx:package`

COPY

你也可以使用`image prune`命令来清理所有未打标签的未使用的镜像。

`docker image prune --force`

COPY

`--force`或者`-f`参数可以让你跳过时否确认的问题。你也可以使用`--all`或者`-a`命令删除本地所有缓存的镜像。

<h3>8.4 怎样理解多层镜像？</h3>

从这本书的一开始，我就说容器是一个多层的文件。在这一部分中，我将证明镜像的不同层和它们在镜像的构建中起到的重要作用。

我们将使用上一章节中的custom-nginx:packaged镜像来进行说明。

你可以使用`image history`命令来展示一个镜像的不同层次，custom-nginx镜像的不同层可以使用如下命令展示：

COPY

这个镜像一共有8个层，最上面的是最新的层，越往下是越基础的层。最上面的层也就是你用来运行容器的层。

我们来进一步看看从镜像COPY到COPY，最后四个IMAG是`<missing>`的层我们忽略不看。

- COPY是由COPY创建的，表示Ubuntu系统默认的shell加载成功。
- COPY是由COPY创建的，代表Dockerfile中的第二行语句。
- COPY是由COPY创建的，代表Dockerfile中的第三行语句。你也可以在镜像创建的执行中该镜像安装所有的包后有60M的大小。
- 最后最上层的镜像由COPY创建，设置该镜像默认执行的命令。

我们可以看到，镜像实际上是由很多只读层组成的，每一层记录着由特定的指令所触发对改变。当你运行一个镜像为容器时，实际上是基于最上层创建了一个可写入层。

这种分层模型的实现是由一个叫做联合文件系统(Union File System)的技术实现的，这里的联合指的是集合理论中的联合，根据维基百科：

> 它允许不同文件系统的文件或者目录，也称之为分支，覆盖重叠在一起形成统一的单个文件系统。相同目录的文件合并后会都出现新的虚拟文件系统合并分支的同一个目录中。

利用这个技术，Docker能够避免数据重复并且能够利用前一个创建的层作为cache构建下一个层。这样就会形成小巧的、有效的镜像，你可以在任何地方使用。

<h3>8.5 怎样从源代码构建NGINX？</h3>

在上一部分中，我们学习了`FROM,EXPOSE,RUN,CMD`等命令，接下来我们学习更多的命令。

这一部分中我们还是创建一个自定义的NGINX镜像，但是不是用`apt-get`包管理的方式进行安装，而是从源代码构建一个NGINX。

为了从源代码构建NGINX，你首先需要拿到NGINX的源代码。如果你克隆了我的项目，在custom-nginx目录中你就会找到nginx-1.19.2.tar.gz包，我们将使用这个nginx源代码包就行构建NGINX。

在写代码之前，我们首先理一下构建的过程。这次镜像的构建步骤有这些：

- 获取一个正确的基础镜像进行构建。例如ubuntu。
- 在基础镜像中安装必要的依赖。
- 将nginx-1.19.2.tar.gz包复制到基础镜像中。
- 解压源代码压缩包并删除压缩包。
- 配置构建参数，使用make命令编译并安装程序。
- 删除解压后到源代码。
- 运行可执行nginx。

这些步骤清楚之后，我们打开之前到Dockerfile，更新为：

COPY

你可以看到，Dockerfile中的命令就是我们上面写的几个步骤。

- `FROM`命令把Ubuntu作为构建任何应用的合适基础镜像。
- `RUN`命令安装源码构建NGINX依赖的基础包。
- `COPY`是一个新的命令。这个命令表示将nginx-1.19.2.tar.gz压缩包复制到镜像中。基本的语法是`COPY <source> <destination>`，这里的source是本地文件系统，destination是镜像。`.`表示复制的目的地，也就是镜像中的工作目录，如果没有特殊制定，默认该目录是根目录`/`。
- 第二个`run`命令从压缩包解压文件并且删除压缩包。
- 压缩包中是一个目录nginx-1.19.2包含着nginx源代码。因此下一步你不必须要`cd`到这个目录再执行构建的程序。你可以阅读[COPY]()这篇文章了解关于这个主题的更多内容。
- 一旦你构建并且安装成功，你可以使用`rm`命令删除nginx-1.19.2这个目录。
- 最后一步你像之前以一样用单进程的方式启动NGINX。

现在你可以使用下面的命令构建镜像：

COPY

代码没有问题，但是我们有一些地方可以改进。

- 不要将文件名称写死为nginx-1.19.2.tar.gz，你可以用ARG命令创建一个参数，这样就可以通过改变参数来改变文件的名称或者版本。
- 不要手动下载源文件，你可以让后台进程下载文件。`ADD`命令能够做到从网络上加载资源到镜像中。

打开之前到Dockerfile并更新内容：

COPY

除了13、14行使用的`ARG`命令和16行使用的`ADD`命令外，新的的代码和之前的几乎一样。更新的内容如下：

- `ARG`命令让可以像其他语言一样声明一个变量，其他地方可以像使用`${argumentname}`这样使用。这里我把文件名nginx-1.19.2和文件扩展名tar.gz定义为两个变量，这样如果改变了nginx的版本或者改变了压缩方式就只有一个地方改变。代码中我给变量设置了默认值，变量的值可以通过`image build`命令的参数传递，你可以在官方文档中了解更多。
- `ADD`命令中，我使用变量动态构建了一个URL。COPY。使用`ARG`命令只改变一个地方就可以改变文件的版本或者后缀名。
- `ADD`命令默认并不会解压网络中获取的资源，所以18行中使用了`tar`命令。

其余的代码几乎没有改变，现在你应该懂得了参数的使用。我们来更新后的镜像：

COPY

现在，你可以使用custom-nginx:build镜像来运行一个容器。

新的容器运行起来了，通过`http://127.0.0.1:8080`应该能够访问这个容器。

COPY

这里NGINX默认返回了一个页面，你可以访问官方文档来学习更多的指令。

<h3>8.6 怎样优化一个Docker镜像？</h3>

我们上一部分中构建的镜像是能够运行的，但是不是最优的。我们可以通过`image ls`命令来看一看镜像的大小：

COPY

对于一个只包含NGINX的镜像太大了。如果你拉去官方的NGINX镜像查看大小，你会发现要小的多：

COPY

为了找到根本原因，我们首先开看看Dockerfile：

COPY

第三行中我们看到`RUN`命令安装了很多东西，尽管这些东西对于构建NGINX是必要的，但是对于运行NGINX就不全是的了。

在我们安装的6个包中，仅仅有两个是运行NGINX必须的。它们是`libpcre3`和`zlib1g`。因此我们的想法是构建完成后卸载运行态不必要的包。

你可以更新Dockerfile为：

COPY

你可以看到，第10一整行做了所有必要的工作。实际的流程是这样的：

- 从第10到17行，所有必要的依赖包都安装了。
- 第18行，下载源代码包并解压后删除。
- 从第19行到28行，NGINX被配置、构建并安装在系统中。
- 第29行，从下载的压缩包中解压出来的文件被删除。
- 从第30行到36行，所有不必要的依赖包被卸载并清理的缓存。`libpcre3`和`zlib1g`包作为NGINX运行时以来被保留。

你可能会问为什么在这一个`RUN`命令中要做这么多的操作，而不是像之前那样分为多个`RUN`命令，因为分开时错误的。

如果你安装包和删除包在分开的`RUN`命令中，它们将存在于镜像的不同层中。尽管最终的镜像也不会有删除的包，但是包含安装的镜像层也会反应到最终镜像的大小上。因此，要确保这些改变发生在同一个镜像层中。

我们重新构建优化后的Dockerfile。

COPY

你可以看到镜像的大小从343MB下降到了81.6MB。官方的镜像大小为133MB。这已经是很大的优化了，我们在下一部分中会更进一步。

<h3>8.7 拥抱Alpine Linxu</h3>

如果你已经了解容器一段时间了，你应该遇到过叫做Alpine Linux的东西，它是一个类似于Ubuntu、Debian或者Fedora的linux发行版。

Alpine的优点是基于`musl libc`和`busybox`并且非常轻量级。最新版本的Ubuntu镜像大小是28MB，而alpine只有2.8MB。

除了轻量级之外，Alpine也具有安全性并且相比较其他Linux发行版非常适合作为容器的基础镜像。

和其他商业发行版相比较Alpine使用上没有那个友好，但是（对于构建镜像来说）它的优势也非常明显。在这个章节中，我们学习使用Alpine基础镜像重新构建custom-nginx镜像。

更新Dockerfile如下：

COPY

代码和之前的没有什么差异，有差异的地方列出来如下：

- 使用`apk add`而不是`apt-get install`命令安装依赖包。`--no-cache`参数表示下载的依赖包不会被缓存。同样适用了`apk del`而不是`apt-get remove`来卸载依赖包。
- `apk add`命令的`--virtual`参数表示将多个包捆扎成单个包便于管理。那些仅仅是构建时候依赖的包被打上`.build-deps`的标签，后续可以通过`apk del .build-deps`命令删除。你可以在官方文档中了解更多关于virtuals参数的内容。
- 这里包的名称可能有些不同。一般每一个Linux发行版都有自己的包仓库开放给大家寻找包。如果你知道你任务要依赖的包，你可以直接到目的发行版的仓库中还搜索它。你可以在[这里]()寻找Alpine Linux的包。

现在使用新的Dockerfile构建镜像，看看镜像的大小：

COPY

之前Ubuntu版本的大小是81.6MB，Alpine版本的大小下降到12.8MB，小了很多。Alpine和Ubuntu不同的地方出来`apk`包管理不一样之外还有一些其他的内容，但是并不重要。如果你使用遇到困难可以在网上寻找答案。

<h3>8.8 怎样创建一个可执行Docker镜像？</h3>

在之前的章节中你使用过了fhsinchy/rmbyext镜像，这一部分中你会学习怎样制作一个可执行的镜像。

首先打开之前在本书中克隆的仓库，rmbyext应用的代码就在同名的目录中。

在开始写Dockerfile之前，思考一下最终的输出应该是什么样的，在我看来应该是这样的：

- 镜像应该要提前安装好Python。
- 应该包含rmbyext脚本。
- 应该设置一个脚本运行的工作目录。
- rmbyext脚本应该设置为镜像的入口，这样镜像可以把文件扩展名作为参数传递。

为了制作上面描述的镜像，可以按照下面的步骤操作：

- 获取一个支持pytho运行的基础镜像，例如pytohn。
- 设置一个容易访问的工作目录。
- 安装Git以便从Github仓库上安装脚本。
- 使用Git和pip安装脚本。
- 删除运行态不必要依赖的的构建时依赖包。
- 设置rmbyext脚本作为镜像的入口。

现在，在rmbyext目录中创建一个Dockerfile文件，写入如下代码：

COPY

解释如下：

- `FROM`命令设置python作为基础镜像，提供python脚本运行的环境。`3-alpine`标签表示你使用Alpine的Python3版本。
- `WORKDIR`命令设置`/zone`作为工作目录，这里工作目录的选择完全是随机的，我使用的是`/zone`，你可以自己设定目录。
- rmbyext是从Github中安装的，git是安装时的依赖。第5行的`RUN`命令安装了git并且用git和pip装了rmbyext脚本，随后git被删除。
- 最后在第9行设置rmbyext脚本为镜像入口。

在整个文件中，第9行价将普通的镜像转化为了可执行镜像。现在你可以通过下面的命令构建镜像：

COPY

这里我没有提供给镜像提供任何tag，所以默认的tag是latest。你可以像之前章节那样运行这个镜像，记得引用你设置的镜像名字而不是这里的fhsinchy/rmbyext。

<h3>8.9 怎样在线共享你的镜像？</h3>

现在你知道了怎么制作一个镜像，是时候分享给这个世界了。分享镜像很简单，你只需要具有一个在线镜像仓库的账户，这里我们使用Docker Hub。

导航到注册页并注册一个免费用户。免费用户可以上传不限制的公共仓库和一个私有仓库。

创建用户之后，你需要使用docker CLI登陆，打开终端执行如下命令：

`docker login`

COPY

弹出框中要求你输入用户名和密码，如果输入正确，你就能够成功登陆你的账户。

为了能够在线分享镜像，镜像必要要打标签，你已经在上面的章节中学习了打tag，回想一下，基本的语法是：

`--tag <image repository>:<image tag>`

我们以共享custom-nginx镜像为例，在custom-nginx项目目录中打开一个新的命令行终端。

为了上传镜像，你必须遵照`<docker hub username>/<image name>:<image tag>`语法。我的用户名是fhsinchy，因此命令如下：

COPY

在命令中，fhsinchy/custom-nginx是镜像仓库，latest是标签，镜像名称可以根据你的喜欢设定并且一旦镜像上传就不能更改。tag你可以随时修改，一般表示应用的版本或者不同的构建。

以`node`镜像为例，`node:lts`镜像表示长期支持的版本，而`node:lts-alpine`版本表示在Alpine Linux上构建的版本，这个版本比其他版本会更小一些。

如果你没有给镜像设置标签，默认的标签是latest，但这并不意味着就是最新的版本，如果你不小心设置一个老的版本为latest，Docker不会校验是否是最新的版本。

镜像创建之后，你就可以通过下面的命令上传它：

`docker image push <image repository>:<image tag>`

具体到这个例子，你可以使用如下命令：

COPY

根据镜像的大小，上传的过程可能要花费一点时间。一旦镜像上传成功，你就可以在你的仓库主页中找到它。

---

<h2>9. 怎样容器化一个Javascript应用？</h2>

现在你学习了怎样创建一个镜像，接下来做一些更相关的内容。

在接下来的章节你将会书写之前用过的`fhsinchy/hello-dock`镜像的源代码。在容器化这个简单应用的过程中，你将会了解卷（volume）和多步构建（two-stage builds）两个Docker重要的概念。

<h3 id="9.1">9.1 怎么写Dockerfile？</h3>

打开你克隆本书的仓库目录，`hello-dock`应用的代码就在同名的子目录中。

这是一个使用`vitejs/vite`的非常简单的JavaScript项目，要了解接下来的内容你不需要懂JavaScript或者vite，只要对Node.js和npm有简单的了解就足够了。

就像之前章节的其他项目一样，你首先要对应用怎么运行有一个规划，在我看来，规划应该是：

- 获取一个运行JavaScript的基础镜像，例如node。
- 在镜像中设置默认的工作目录。
- 复制package.json文件到镜像中。
- 安装必要的依赖。
- 复制其他的项目文件。
- 通过执行`npm run dev`启动vite dev服务。

这样的规划通常来自于应用的开发人员，如果你本身是一个开发人员，你应该已经对应用怎么运行有很好的理解了。

如果将上述的规划放在Dockerfile.dev中，内容应该是这样的：

COPY

上述命令的解释如下：

- `FROM`命令表示指定Node.js镜像作为基础镜像，该镜像中你可以运行任何的JavaScript应用。`lts-alpine`标签表示你使用的是Alpine的镜像版本，这是一个支持长久维护的镜像版本。所有的tag和文档都可以在node hub页面找到。
- `USER`命令设置该镜像默认的user为node。默认情况Docker以root用户运行容器，但是根据Docker和Node.js的最佳实践这样会带来安全上的问题，所以最好尽可能用非root用户。这样node镜像拥有了一个非root的用户叫做node，你可以使用`USER`命令来设置默认的用户。
- `RUN mkdir -p /home/node/app/`命令使用node用户在家目录中创建了一个叫app的目录。Linux中一般非root用户的家目录默认是`/home/<username>`。
- `WORKDIR`命令设置默认的工作目录为新创建的`/home/node/app`目录。镜像默认的工作目录是root，你也不想一些不必要的文件散落在root目录是吧？因此你改变了默认的工作目录为更有意义的目录`/home/node/app`或者其他你喜欢的目录。接下来的copy、ADD和CMD等命令都是在该工作目录下执行的。
- `COPY`指令复制package.json文件，该文件中包含了应用必要的依赖信息。`RUN`命令执行`npm install`命令，该命令是node项目中使用package.json来安装依赖包的默认命令。`.`代表工作目录。
- 第二个`COPY`命令表示复制文件系统中当前目录(`.`)剩余的内容到Docker镜像的工作目录(`.`)中。
- 最后`CMD`命令用`exec`的格式设置该镜像默认的运行命令为`npm run dev`。
- vite dev服务默认运行的端口是3000，因此用`EXPOSE`命令加以说明是一个很好的选择。

现在使用该Dockerfile.dev文件来构建一个镜像，你可以执行如下指令：

```shell
COPY
```

因为Dockerfile文件名不是默认的Dockerfile，因此你需要使用`--file`参数来手动指定文件。你可以使用下面的命令来运行一个容器：

```shell
docker container run \
--rm \
--detach \
--publish 3000:3000 \
--name hello-dock-dev \
hello-dock:dev

COPY
```

现在可以通过`http://127.0.0.1:3000`来访问hello-dock应用了。

![](/img/posts/docker-handbook-2021-19.jpg)

恭喜你成功运行了第一个容器化的应用。你的代码没有问题，但是一些地方还有很大提高的地方，我们首先来看第一个问题。

<h3 id="9.2">9.2 怎么在Docker中处理Bind Mounts(绑定挂载)？</h3>

如果你之前有接触过前端的一些框架，你应该了解这些框架的dev服务都有热部署的功能。那就是如果你源代码有所改变，服务端就会reload，任何修改都能够自动的立刻完成重新部署显现出来。

但是如果你现在改变你的源代码，启动的服务不会有任何改变。这是因为你的修改发生在本地文件系统上，而你看到的页面的后台服务的代码是在容器内的文件系统上。

![](/img/posts/docker-handbook-2021-20.jpg)

为来解决这个问题，你可以使用bind mount，能很容易的实现将本地文件系统的一个目录挂载到容器上。bind mount不是复制了一份本地文件系统中的内容，而是让容器可以直接引用本地文件系统上的内容。

![](/img/posts/docker-handbook-2021-21.jpg)

这样在你本地文件系统中改变的内容就会立马体现在容器中，触发vite dev服务的热部署。容器中的任何改变也会反映在本地文件系统中。

在[怎样操作可执行镜像](#7.12)章节中我们学习了可以使用`container run`或者`container start`命令的`--volume|-v`参数来创建bind mount，提醒一下，基本的语法是这样的：

```shell
--volume <local file system directory absolute paty>:<container file system directory absolute path>:<read write access>
```

停掉你之前启动的hello-dock-dev容器，使用下面的命令启动一个新的容器：

```shell
docker container run \
--rm \
--publish 3000:3000 \
--name hello-dock-dev \
--volume $(pwd):/home/node/app \
hello-dock:dev

COPY
```

请注意，我省略了`--detach`参数是为了证明一个重要的内容，终端中可以看出，应用没有运行起来。

这是因为尽管volume的使用解决了热部署的问题，它带来了一个新的问题。如果你有node.js的经验，你会知道Node项目的依赖都在根目录下一个`node_modules`目录中。

如今，你讲整个Node项目的挂载到本地文件系统的目录上，容器内的`node_modules`目录也被替换了，这样应用就不能正常启动了。

<h3 id="9.3">9.3 怎么使用Docker的匿名卷？</h3>

这个问题可以通过Docker匿名卷的方式解决，匿名卷除了不需要指定一个源目录之外和一个bind mount以一样的，通用的语法是：

```shell
--volume <container file system directory absolute path>:<read write access>
```

因此基于匿名卷最终运行容器hello-dock的命令是：

```shell
docker container run \
--rm \
--detach \
--publish 3000:3000 \
--name hello-dock-dev \
--volume $(pwd):/home/node/app \
--volume /home/node/app/node_modules \
hello-dock:dev

COPY
```

在这里，Docker将容器内node_modules整个目录和由Docker daemon管理的匿名目录绑定。

<h3 id="9.4">9.4 怎么在Docker中执行多阶段构建（Multi-Staged Builds）？</h3>

目前为止，我们已经在开发环境下可以构建JavaScript应用镜像。如果你要在生产环境中构建一个镜像，新的挑战就出来了。

在生产环境中，`npm run build`编译了所有的JavaScript代码为HTML、CSS和JavaScript文件。为了运行这些文件，你不在需要node或者其他运行时的依赖。你所需要的是一个像nginx的http服务器。

为了创建在生产环境中运行应用的镜像，你可以按照按照一下步骤：

- 使用node作为基础镜像构建应用。
- 在node镜像中安装nginx并且使用它提供http服务。

上面的办法是可行的，但是问题是node镜像很大并且镜像中的很多东西对于http服务来说都不是必须的，这种情况更好的办法是：

- 使用node作为基础镜像构建应用。
- 复制node镜像中产生的文件到一个nginx镜像中。
- 抛弃node所有相关的东西，基于nginx创建最终的镜像。

这样制作的镜像仅仅包含需要的文件，镜像也会很小。

这种方法是多阶段构建，为了执行这样的构建，在你的hello-dock目录中创建一个新的Dockerfile，写入如下内容：

COPY

这个Dockefile除了新增的一些行外和之前的很类似，命令解释如下：

- 第1行，开始第一阶段的构建，使用`node-alpine`作为基础镜像，`as builder`语法指定了该阶段的一个名称，以便后续能够访问到。
- 第3到9行，这是我们之前看到过的很多标准的命令。`RUN npm run build`命令是将整个项目编译并将结果输出到`/app/disk`目录，app目录是指定的工作目录，disk目录是vite默认的文件输出目录。
- 第11行，开启了一个新的bulid阶段，使用`nginx:stable-alpine`作为基础了nginx镜像。
- nginx服务默认监听80端口，因此加上`EXPOSE 80`。
- 最后一行是一个copy命令，`--from=builder`参数表明你想要从builder阶段copy一些东西。那之后是一个标准的copy命令，`/app/dist`是源端、`/usr/share/nginx/html`是目标端。目录段路径使用的是nginx的默认配置路径，该路径下的文件会自动被nginx server读取。

从上面可以看到，生成的镜像以nginx为基础镜像，镜像中仅仅包含运行应用的必要文件。可以通过下面的命令构建镜像：

```shell
docker image build --tag hello-dock:prod .
COPY
```

镜像创建后，你可以通过执行下面的命令来运行容器：

```shell
docker container run \
--rm \
--detach \
--name hello-dock-prod \
--publish 8080:80 \
hello-dock:prod

COPY
```

可用通过`http://127.0.0.1`来访问启动的服务。

![](/img/posts/docker-handbook-2021-22.jpg)

这里你可以看到hello-dock应用。多阶段构建在构建许多依赖的大项目的时候非常有用，如果能够很好的配置，多阶段构建的镜像非常的小并且优化。

<h3 id="9.5">9.5 怎么忽略掉不必要的文件？</h3>

如果你了解过git，你可能会知道.gitignore文件，该文件中包含的一系列文件和目录将会从git仓库中忽略掉。

同样，Docker也有一个类似概念，`.dockerignore`文件中包含一系列的文件和目录会在镜像构建时候忽略，你可以在hello-dock目录中找到.dockerignore。

```shell
.git
*Dockerfile*
*docker-compose*
node_modules
```

这个`.dockerignore`只适用在build阶段，.dockerignore中的文件和目录在COPY命令时会被忽略，但是如果你要进行目录挂载，.dockerignore就没有任何作用。我会在有必要的项目中添加.dockerignore文件。

---

<h2>10. Docker中的网络操作</h2>

目前为止本书中你只接触到了单个的容器，但是在实际的工作中，你要处理的大部分项目都超过一个容器。事实上，如果你不了解容器隔离的细微差别，就很难协作好多个容器。

因此本书的这一部分你会熟悉Docker网络并能够协作一个小的多容器项目。

从之前的章节中你会了解到容器时一个隔离的环境。现在假设你有一个基于Express.js的notes-api应用和一个postgres数据库服务分别在两个容器上运行。

这两个容器彼此间时完全隔离的并且批次意识不到对方的存在，那么两个容器之间这门能够连接起来，是不是很困难？

你可能会想到这个问题的两个解决方案，它们是：

- 通过暴露的端口访问数据库服务。
- 通过数据库服务器的IP和默认端口访问数据库服务。

第一个方案需要postgres容器暴露一个端口，notes-api通过这个暴露的端口例如5432进行连接。如果你在notes-api容器中使用`127.0.0.1:5432`来连接数据库服务，你会发现note-api无法找到数据库服务。

原因是当你在note-api容器中使用`127.0.0.1`时，你实际上在访问当前容器的localhost，postgres服务在notes-api容器中并不存在，这样也就连接不上。

第二种方案你可能会想用`container inspect`命令找到postgres数据库容器的实际IP地址并且使用IP和端口进行连接。假设postgres数据库服务容器的名称是notes-api-db-server，你可以通过下面的命令，很方便的获得容器的IP地址：

```shell
```

现在已知postgres默认的端口是5432，notes-api容器可以方便的通过`172.17.0.2:5432`连接到数据库服务。

这种办法也存在问题，实际上使用容器的IP来连接容器是不被推荐的。并且，假设容器被销毁或者被重新创建，容器的IP是会发生改变的，跟踪这些变化的IP是一件很繁琐的事情。

现在否定了原始问题的所有答案，我们给出正确的答案：将要互联的容器放在一个用户自定义的桥接网络中。

<h3 id="10.1">10.1 Docker网络基础</h3>

像`container`和`image`一样，`network`是Docker中的另外一个逻辑对象，同样有`docker network`开头的很多命令来管理网络。

列出系统中的所有网络，执行下面的命令：

```shell
docker network ls
COPY
```

你能够看到系统中有三个网络，现在看下表格中的DRIVER列，该列表示网络的类型。

默认，Docker有5种网络drivers，它们是：

- 桥接（bridge）-Docker中的默认网络类型，这种类型适合使用在独立运行的容器并且容器间需要相互通信。
- 主机（host）-完全移除了网络的隔离。只要在主机网络下的任何容器都连接到了宿主机网络下。
- 无（none）-这种类型容器之间的网络连接，我还没有发现该中类型的任何用处。
- overlay-这种类型跨主机连接多个Docker daemon，不在本书的讨论范围之内。
- macvlan-它允许给容器分配MAC地址，是容器模拟一个物理设备。

也有第三方的插件允许你整合Docker和特殊的网络栈。在上面5中网络类型中，在本书中你只需要了解桥接网络类型。

<h3 id="10.2">10.2 怎样在Docker中自定义一个桥接网络？</h3>

在创建自定义网络之前，最好花一些时间认识一个Docker默认的桥接网络。从列出系统上的所有网络开始：

```shell
docker network ls
COPY
```

可以看到，Docker有一个默认的桥接网络名字叫做bridge，你运行的容器都会自动附接在这个桥接网络下：

```shell
COPY
```

之前有讲到过，附接在这个默认桥接网络下的所有容器相互间可以通过IP通信。

一个自定义的桥接网络相比较默认桥接有一些额外的功能，根据官方文档，额外的功能如下：

- 自定义桥接网络提供容器间自动DNS解析功能。这意味着在同一个自定义桥接网络下的容器间可以通过容器名称通信。因此如果你有两个容器分别是notes-api和notes-db，那么API容器就可以通过note-db容器名字来连接数据库容器。
- 自定义桥接网络提供更好的容器间的隔离。所有附接在默认桥接网络下的容器可能会彼此冲突，自定义桥接网络下的容器能确保更好的隔离。
- 容器能够随时附接在或者从自定义桥接中断开连接。在容器的生命周期中你可以随时附接在或者断开于自定义桥接网络中，从自定义桥接网络中断开容器连接的方法是，首先要停掉容器，然后重新运行容器并指定网络参数。

现在你了解了足够多自定义桥接网络的内容，是时候自己动手创建一个了，可以使用`network create`命令来创建网络，基本的语法如下：

```shell
docker network create <network name>
```

创建一个名称为skynet的网络，命令如下：

```shell
docker network create skynet
COPY
```

上面可以看到一个指定名称的网络被创建，目前没有容器附连在这个网络下面，下一部分你会学习如何附接一个容器到一个网络。

<h3 id="10.3">10.3 怎样在Docker中附接一个容器到网络中？</h3>

基本上有两个办法将一个容器附接在一个网络上。首先，你可以使用network的命令来附接容器，基本的语法如下：

```shell
docker network connect <network identifier> <container identifier>
```

为了能够将`hello-dock`容器附接在`skynet`网络下，你可以执行下面的命令：

```shell
docker network connnect skynet hell-dock
COPY
```

从上面我们执行两条network inspect的输出中可以看到，hello-dock容器已经附接在skynet和默认bridge桥接网络下了。

第二种附接容器在网络下的方法是使用命令`container run|create`命令的`--network`参数，基本的语法如下：

```shell
--network <network identifier>
```

运行另外一个alpine-box容器并附接在同样的网络下，可以执行：

```shell
docker container run --network skynet --rm --name alpine-box -it alpine sh
COPY
```

你可以看到，在alpine-box容器中运行`ping hello-dock`能成功联通，因为二容器同在自定义的桥接网络中且具备自动DNS解析的功能。

需要注意的是，要想让自定义DNS解析功能成功，你必须给容器设定自定的名称，用自动生成的容器名称在自定义DNS解析时是无效的。

<h3 id="10.4">10.4 怎样在Docker中将容器从网络中断开连接？</h3>

上面你学习了将一个容器附接在一个网络中，这一节你会学习到如何将一个容器从网络中断开连接。你可以使用`network disconnect`命令来完成，基本的语法如下：

```shell
docker network disconnect <network identifier> <container identifier>
```

将hello-dock容器从skynet网络中断开连接，你可以执行下面的命令：

```shell
docker network disconnect skynet hello-dock
```

和network connect命令一样，network disconnect命令也没有任何输出

<h3 id="10.5">10.5 怎样在Docker中删除网络？</h3>

和其他Docker中的逻辑对象一样，网络可以使用`network rm`命令删除，基本的语法是：

```shell
docker network rm <network identifier>
```

将skynet网络从系统中删除，可以执行如下命令：

```shell
docker network rm skynet
```

你也可以使用`network prune`命令来删除任何系统中没有使用的网络，该命令也具有`-f|--force`和`-a|--all`参数。

---

<h2>11. 怎样容器化配置一个多容器JavaScript应用？</h2>h

现在你学习了足够多的Docker网络知识，在这一部分中，你会学习到如何容器化配置一个完整的多容器项目，该项目包括一个基于Express.js的notes-api和PostgreSQL。

在这个项目当中，一共有两个容器需要你用网络将它们连在一起。此外，你将会学习到如环境变量和实名卷（named volume）的概念，我们开始吧。

<h3 id="11.1">11.1 怎样运行一个数据库服务？</h3>

该项目的数据库服务是PostgreSQL，使用的是官方的镜像。

根据官方文档，如果运行官方镜像，你必须提供`POSTGRES_PASSWORD`环境变量，我还同时使用`POSTGRES_DB`环境变量作为默认的数据库名称。PostgreSQL默认使用的端口是5432，因此你也需要发布该端口。

运行数据库容器，你可以执行下面的命令：

```shell
docker container run \
--detach \
--name=notes-db \
--env POSTGRES_DB=notesdb \
--env POSTGRES_PASSWORD=secret \
--network=notes-api-network \
postgres:12

COPY
```

`--env`参数用在`container run`和`container create`命令中为运行的容器设置环境变量。上面可以看到数据库容器创建成功并且在运行中。

尽管容器成功运行中，还有一个问题。像PostgreSQL、MongoDB和MySQL持久化数据在一个数据目录中。PostgreSQL使用容器内的`/var/lib/postgresql/data`目录持久化数据。那如果由于某种原因容器被删除了，你将失去你所有的数据，为了解决这个问题，需要用到实名卷。

<h3 id="11.2">11.2 怎样在Docker中使用实名卷？</h3>

在之前我们学习了绑定挂载（bind mounts）和匿名卷，所谓的实名卷和匿名卷类似，但是实名卷可以通过名字来引用它。

卷（volume）在Docker中也是一个逻辑对象，可以通过命令行来进行管理。`volume create`命令可以用来创建实名卷。

基本的语法如下：

```shell
docker volume create <volume name>
```

创建一个名称为notes-db-data的实名卷，可以执行以下命令：

```shell
docker volume create notes-db-data
COPY
```

实名卷可以挂载到notes-db容器的目录`/var/lib/postgresql/data`目录上，要进行挂载，首先停止并删除notes-db容器：

```shell
docker container stop notes-db

COPY
```

现在运行一个新的容器，用--volume或者-v参数进行挂载：

```shell
COPY
```

现在使用inspect命令查看notes-db容器的挂载是否成功：

```shell
COPY
```

现在notes-db-data的数据将会安全的持久化到卷中并且后面可以复用。这里也可以使用绑定挂载来代替实名卷，只是这种场景我更喜欢使用实名卷。

<h3 id="11.3">11.3 怎样在Docker中查看容器的日志？</h3>

为了查看一个容器的日志，你可以使用`contaier logs`命令，基本的语法是：

```shell
docker container logs <container identifier>
```

获取notes-db-container的日志，你可以执行下面的命令：

```shell
COPY
```

从57行的日志可以看出来数据库服务已经启动并且等待外部的连接。有参数`--follow|-f`可以让你把终端的输出重定向到日志文件中，得到一个持续到输出的文档。

<h3 id="11.4">11.4 怎样在Docker中创建一个网络并且将数据库容器附接到网络中？</h3>

之前我们有学过，为了容器之间能够通信，怎样创建一个自定的网络并且将容器附接到网络中。首先在系统中创建一个名称为notes-api-network的网络：

```shell
docker network create notes-api-network
```

现在将notes-db容器附接在这个网络下，执行下面的命令：

```shell
docker network connect notes-api-network notes-db
```

<h3 id="11.5">11.5 怎样写Dockerfile？</h3>

进入你下载的项目目录，进入`notes-api/api`目录，创建一个新的Dockerfile文件，copy下面的内容进去：

```shell
COPY
```

这是一个多阶段的构建，第1阶段使用`node-gyp`进行构建和安装依赖，第2阶段用来运行应用。我简单介绍下相关步骤：

- 阶段1，使用`node:lts-alpine`作为基础镜像并且使用`builder`最为阶段1的阶段名称。
- 第5行，我们安装python、make和g++，`node-gyp`工具运行需要这3个依赖包。
- 第7行，我么是`/app`作为工作目录。
- 第9和10行，我们复制package.json文件到工作目录中并且安装所有的依赖。
- 阶段2，仍然使用`node:lts-alpine`最为基础镜像。
- 第16行，我们设置NODE_ENV环境变量为production，要让API正常运行，这个设置相当重要。
- 第18到20行，我们设置默认的用户为node，创建`/home/node/app`目录并且设置该目录为工作目录。
- 第22行，我们复制所有的项目文件，第23行，我们从`builder`阶段复制了node_modules目录，该目录中包含所有运行依赖的构建阶段的包。
- 第25行，我们设置了默认的启动命令。

根据Dockerfile构建镜像，执行下面的命令：

```shell
docker image build --tag notes-api .

COPY
```

在使用构建出来的镜像运行容器之前，确保数据库容器是运行的并且已经附接在notes-api-network网络下。

```shell
docker container inspect notes-db

COPY
```

为了方便展示，这里我省略了一些信息。在我的系统中，notes-db容器是运行的，该容器挂载了notes-db-data卷并且附接在桥接网络notes-api-network下。

确保所有的都正确后，你可以用下面的命令运行一个新的容器：

```shell
COPY
```

你应该能够自己能够理解这个长长的命令，因此我只对环境变量做简单的说明。

notes-api容器需要设置三个环境变量，它们时：

- DB_HOST-这个是数据库服务的主机名。由于数据库服务和API都附接在同一个自定义的桥接网络下，数据库服务可以使用容器名称notes-db来进行引用。
- DB_DATABASE-API使用的数据库。在运行数据库服务的时候，我们通过环境变量POSTGRES_DB设置了默认的数据库名称为notesdb，正是我们使用的这个。
- DB_PASSWORD-连接数据库的密码。这个变量也是通过之前的环境变量POSTGRES_PASSWORD设置过。

查看应用是否正常启动，可以使用下面的命令：

```shell
COPY
```

现在，容器在运行中，你可以通过`http://127.0.0.1:3000/`来访问一下API。

![](/img/posts/docker-handbook-2021-23.jpg)

该API共有5个路由，你可以在`/notes-api/api/api/routes/notes.js`查看。

尽管容器在运行中，在正式使用之前仍然有最后一件事情要做。你必要要进行必要的数据库迁移来设置数据库中的表，在容器中执行`npm run db:migrate`来完成。

<h3 id="11.6">11.6 怎样在运行的容器中执行命令？</h3>

在运行中的容器内执行命令，你需要用到`exec`命令，基本的语法是：

```shell
docker container exec <container identifier> <command>
```

在notes-api容器中执行`rpm run db:migrate`命令，你需要执行下面的命令：

```shell
docker container exec notes-api npm run db:migrate
COPY
```

如果你想用命令行交互的方式在容器内执行命令，必须要使用`-it`参数。例如你想在运行中的notes-api容器中启动交互式shell，你可以执行下面的命令：

```shell
COPY
```

<h3 id="11.7">11.7 怎样写管理Docker的脚本？</h3>

管理一个多容器带有卷和网络的多容器项目意味着有很多命令，为了简化这个过程，我通常用简单的shell scripts和Makefile。

你可以在notes-api目录中找打4个shell scripts，它们分别是：

- boot.sh-用来启动已经存在的容器。
- build.sh-用来创建并运行容器，如果必要的话也会创建卷和网络。
- destroy.js-删除该项目所有容器、卷及网络。
- stop.js-停掉所有的容器。

有一个Makefile包含了4个目标叫做start、stop、build和destory，每一个目标都是调用上面4个shell scripts。

如果系统中容器处于运行态，执行`make stop`命令应该停掉所有的容器。执行`make destory`应该停掉所有的容器并且删除所有的相关内容。确保你在notes-api目录中：

```shell
make destory
COPY
```

如果你遇到了permission denied的错误，你需要在脚本上执行`chmod +x`：

```shell
COPY
```

在这里不再解释这些脚本，因为它们都是一些if else指令加上见过很多次的Docker命令。如果你有了解Linux Shell，你应该能够理解脚本中的内容。

---

<h2>12. 怎样使用Docker-Compose？</h2>
