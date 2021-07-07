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

COPY

上面，我们执行命令`cat /etc/os-release`得到相应的输出，说明我们确实是和Ubuntu容器在进行交互。

`-it`参数能够让你和容器内的交互式程序进行交互。这个参数实际上是有两部分组成在一起。

- `-i`参数或者`--interactive`绑定连接终端到容器的输入流中，因此你可以在终端中输入。
- `-t`或者`--tty`参数保证你获取格式化后的内容，并且通过分配一个伪tty让你有一个很好的终端体验。
