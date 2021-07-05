---
layout: post
title: 'Docker指南-2021【译】'
subtitle: '从基本的概念到Docker中级，带你从零到一学习Docker。通过本指南你应该学习到：几乎基于所有平台的容器化;上传一个自定义的Docker镜像(Image)到在线仓库(registry);使用docker-compose协作多个容器。'
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

IBM指出：容器化技术就是将软件代码和其所有的依赖打包或者封装，使其统一能够在任何平台上持续运行的技术。

换句话说，容器技术就是能够让你把软件和其依赖打包在一个自包含的包中，这样软件运行的时候就不必解决启动依赖的一些问题。

让我们考虑一个实际的生活场景，假设你开发了一个神奇的图书管理系统，你也可以向你的朋友提供书籍的借阅功能。如果把这个系统的依赖都列出来，可能是这样的：

- Node.js
- Express.js
- SQLite3

理论上来讲，应该就是这些依赖了，但是实际上并不止这些。我们知道`Node.js`使用一个叫做`node-gyp`的构建工具用来构建本地插件，并且根据官方的安装文档，这个构建工具需要`Python`2或者3和一个合适的c/c++编译器。

这样说来，最终的依赖列表可能是这样的：

- Node.js
- Express.js
- SQLite3
- Pytho2 or 3
- c/c++ 编译器

在任何平台安装`Python`2或者3都比较简单。在Linux平台上安装c/c++编译器比较容易，但是在Windows和Mac系统上安装就是一个痛苦的过程了。

在Windows中，C++编译器有超过1G的大小，需要花费不少的时间安装。在Mac系统中，你需要安装`Xcode`或者体量小的`Xcode`命令行工具。

尽管你安装好了依赖，在OS更新后，你的依赖可能被破坏。事实上，在macOS系统上，这个问题如此常见，以至于在官方的仓库中记录着安装日志。

让我们假设你克服了重重困难安装好了开发应用的所有依赖，你认为现在就万事大吉了吗？还没有。

如果你的同事使用的Windows系统开发，而你使用的是macOS系统，你需要考虑两个操作系统对文件路径的差异处理。再或者说Nginx并没有很好的针对Windows系统做优化。一些技术例如Redis甚至没有Windows系统的预编译包。

即使项目已经开发完成，如果部署人员不清楚部署的流程呢？

如果采用下面的办法，所有上面的问题都能够被解决：

- 在和你最终部署环境匹配的隔离环境（所谓的容器）中开发并运行系统。
- 把你的应用和其所有的依赖及配置打包为一个单文件（所谓的镜像）。
- 通过一个中央服务器（所谓的仓库）分享应用给有合适权限的人。

你的同事们可以从中央仓库中下载镜像，启动应用时不用担心环境的不一致的问题，甚至可以直接启动应用，因为镜像中可能已经做好了相关配置。

这就是容器化的概念：将你的应用（和依赖）打包在一个字包含的镜像中，这个镜像是轻量级的并且可以在不同的环境中复制。

那么，现在的问题是：**Docker 到底是做什么的？**

就像我上面说的，容器化技术就是通过将应用的环境、依赖和配置封装在一个黑盒子中，来解决应用部署时千千万万的问题。

容器化技术已经有一些实现，Docker是其中的一种。它是一个开源的容器平台，能够将你的应用容器化，通过私有或者公共仓库分享，并且还使这些容器协作起来。

如今，Docker不是市面上唯一的容器化工具，但它是最流行的一个。另一个我喜爱的容器化平台是红帽公司开发的`Podman`。其他的容器化工具像Google的Kaniko，CoreOS的rkt也很优秀，但是暂时还不是Docker的可替代的工具。

如果你想了解容器化的历史，你可以读一下`A Brief History of Containers:From the 1970s Till Now`这个经典介绍，里面介绍了容器化技术的重要演变过程。

---

<h2 id="3">3.怎么样安装Docker</h2>

根据操作系统的不同，安装的Docker的方法也不相同，但是总体来说，安装过程是比较简单的。

Docker能够运行在不同的主流操作系统macOS、Windows和Linux上，在这三个平台上，macOS系统上安装最容易，所以我们就从macOS系统开始。

<h3 id="3.1">3.1 怎么在macOS系统上安装Docker</h3>

在macOS上，你首先要做的就是找到官网的下载地址并下载一个mac平台的稳定版本。

你将会得到一个macOS系统的安装包并将它拖拽一个应用程序文件夹中。

![](/img/posts/docker-handbook-2021-01.jpg)

你可以双击安装包，一旦应用启动成功，你会在菜单栏中看到Docker的图标。

![](/img/posts/docker-handbook-2021-02.jpg)

现在，打开命令行终端并且执行`docker --version`和`docker-compose --version`命令来验证Docker的安装是否成功。

<h3 id="3.2">3.2 怎么在Windows系统上安装Docker</h3>

在Windows系统上除了额外的步骤要执行意外，其他的步骤和在macOS系统上大体一致。安装的步骤如下：

1. 访问该网址，并且按照指示在Windows10系统上安装WSL2。
2. 找到官方网站，下载Windows平台的稳定版本。
3. 双击安装包，根据引导默认完成安装。

安装完成后，你可以从桌面或者开始菜单中打开Docker，你的Docker就会出现在任务栏中。

![](/img/posts/docker-handbook-2021-03_1.jpg)

现在，可以打开你从Microsoft Store中安装的Ubuntu或者任何发行版，执行`docker --version`和`docker-compose --version`命令来验证Docker的安装是否成功。

![](/img/posts/docker-handbook-2021-03.jpg)

你也可以打开cmd或者Power shell命令行终端来验证，只是在Windows上我喜欢WSL2命令行工具。

<h3 id="3.3">3.3 怎么在Linux系统上安装Docker</h3>

在Linux系统上安装Docker和上面两个系统完全不同，并且你使用Linux发行版不同，安装方式也不相同。但实际上，安装过程也很简单。

在Windows或者macOS系统上的Docker桌面安装包是包含像Docker Engine, Docker Compose, Docker Dashboard, Kubernetes等工具的集合包。然而在Linux操作系统上，没有这样捆绑一起的包，你需要手动安装你需要的工具包。不同平台的安装过程如下：

- 如果你是Ubuntu系统，你可以按照Ubuntu官网文档指示安装Docker Engine。
- 其他不同的Linux发行版，你也能从官方文档中获取安装指示。
- 如果官方文档中你使用发行版的说明，你需要按照二进制编译安装的指示进行安装。
- 无论你怎么样安装，一些针对Linux系统重要的安装后步骤的执行是很重要的。
- 在你完成了Docker的安装，你还需要安装Docker Composed，你可以从官方文档中获取安装Docker Composed的说明。

安装完成后，打开命令行终端并且执行`docker --version`和`docker-compose --version`命令来验证Docker的安装是否成功。

![](/img/posts/docker-handbook-2021-04.jpg)

尽管Docker能够在不同的平台上使用，我还是更倾向于在Linux上使用，本书中我将使用Ubuntu20.10和Fedora33。

另外一件事我像现在就说明的是：我没有使用任何GUI工具操作Docker。

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

hello-world镜像Docker提供的一个很小的容器化程序，它是很简单的hello.c程序，在终端打印出Hello Worl字符串。

在终端中，你可以执行使用`docker ps -a`命令来查看目前或者历史运行的Docker容器

```
docker ps -a

# CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                     PORTS               NAMES
# 128ec8ceab71        hello-world         "/hello"            14 seconds ago      Exited (0) 13 seconds ago                      exciting_chebyshev
```

![](/img/posts/docker-handbook-2021-06.jpg)

输出结果中，镜像hello-world对应有一个命名为`exciting_chebyshev`的容器，容器的ID为`128ec8ceab71`，还有一个Exited（0）13 seconds ago的状态表示容器运行的过程中没有产生错误。

为了能够理解刚才屏幕中输出的内容，必须要了解Docker的架构和容器化技术的三个基本概念，如下：

我按照字母表的顺序开始第一个重要概念的讲解：

<h3 id="4.1">4.1 什么是容器？</h3>

在容器化技术中，没有比容器更基本的概念了。

Docker官方文档中是这样说的：

> 容器是可以将应用和其依赖打包在一起的应用层的一种抽象。与虚拟化整个硬件不同，容器仅仅将宿主操作系统虚拟化。

你可以认为容器化技术是下一代的虚拟化技术。

就像虚拟机一样，容器之间以及容器和宿主机之间环境都是彼此隔离的。相比较虚拟机，容器也更加轻量级，因此同一个宿主机上可以同时跑过个容器，并且不影宿主机的性能。

容器和虚拟机使用不同的方法虚拟化硬件，两者的主要不同是虚拟化方法的不同。

虚拟机通常被一个叫做Hypervisor的程序创建并管理，例如Oracle VM VirtualBox、VMware、KVM和Hyper-V等等。这个hypervisor程序处在宿主机操作系统和虚拟机之间，承担中间通信的职责。

![](/img/posts/docker-handbook-2021-07.svg)

在虚拟机中运行的程序和本地操作系统（gust operating system）通信，本地操作系统和hypervisor程序通信，hypervisor程序再像宿主机操作系统从硬件中申请必要的资源来运行程序。

从上面可以看出，虚拟机中运行的程序和宿主基础硬件之间有一个常常的通信链，即使是虚拟机中程序申请很小的资源，由于本地操作系统的存在也增加了明显的性能消耗。

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

上面的代码中，我先在宿主机上执行了`uname -a`命令，获取宿主机操作系统的内核详情。然后第二行运行了一个Alpine Linux容器执行了同样的命令。

从输出的结果可以看出来，容器实际上使用了宿主机操作系统的内核，这也证明了容器虚拟化了宿主机的操作系统而不是自身也拥有一个。

如果你使用Windows机器，你会发现所有的容器都是使用WSL2内核，这是因为WSL2Windows上Docker的后台服务。在macOS系统上，默认的后台服务是一个基于HiperKit hypervisor的VM。

<h3 id="4.2">4.2 什么是镜像？</h3>

镜像是分层的，自包含的，用来创建容器的模版源文件。它们可以通过仓库共享。

过去，不同的容器引擎需要不同的镜像格式。后来，Open Container Initiative（OCI）定义了标准的容器镜像规范，大部分的主流容器化平台都遵循这一规范，这也意味着在Docker上构建的镜像在没有任何修改的情况下可以直接在Podman容器平台上使用。

容器其实就是运行时的镜像，当你从互联网上获取一个镜像并且运行这个镜像的时候，你实际上基于只读镜像层新创建了一个临时可写入层。

镜像的概念随着本书的深入会越来越清晰，现在需要记住的是，镜像是一个多层次的、只读的，将你的应用打包为系某一状态的文件。

<h3 id="4.3">4.3 什么是Docker仓库（Docker Registry）？</h3>

我们已经学习了两个非常重要的概念，容器和镜像，剩下的就是Docker仓库的概念了。

镜像仓库是一个你能够上传并且下载别人上传镜像的中央镜像管理处。Docker Hub是Docker官方默认的镜像仓库，另外一个流行的镜像仓库是Red Hat的Quay。

本书中，我们使用Docker Hub作为我们的镜像仓库。

![](/img/posts/docker-handbook-2021-11.jpg)

你可以免费在Docker Hub上分享你的公开镜像，互联网上的人们可以从那里免费下载它们。我上传的镜像可以在我的主页（fhsinchy）中找到。

![](/img/posts/docker-handbook-2021-12.jpg)

除了Docker Hub和Quay，你也可以创建私有的镜像仓库。实际你本地也运行着一个私有仓库，用来存储从远端仓库拉下来的镜像。

---

<h2 id="5">5. Docker架构</h2>

现在你已经学习了容器化技术和Docker的基本概念，是时候学习Docker是怎么工作的。

Docker引擎包含三个主要部分：

1. **Docker Daemon**：daemon进程（dockerd）在后台运行并且监听客户端的请求，它能够管理各种各样的Docker对像。
2. **Docker Client**：客户端（docker）是一个命令行接口程序，主要负责传递用户的请求。
3. **REST API**：REST接口是Docker后台程序和客户端之间的桥梁，用户输入的任务命令行请求都会通过接口传递到后代Docker Daemon那里。

根据官方文档：

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

如果在公共的Docker仓库有一个新的镜像版本，Docker后台程序会重新获取镜像，这里的`:latest`是一个标签。Docker镜像会有有意义标签表示版本或者构建。关于标签我们在后续会做详细介绍。

---

<h2 id="7">7. Docker容器基本操作</h2>

在前面的章节中，我们学习了Docker的基础概念并且使用docker run命令运行了一个Docker容器。

在这一部分中，你们将会学习到详细的Docker容器操作。容器操作是你每天必须执行的基本任务，所以正确的理解各种命令很关键。

要知道的是，这里列出来的Docker命令不是全部的，而是一些最基本的命令。如果想要学习更多Docker支持的命令，可以访问官方文档的命令行说明。

<h3 id="7.1">7.1 怎样运行一个容器？</h3>

之前的章节我们基于hello-world镜像，使用docker run命令启动了一个容器。基本的命令行语法是这样的：

`docker run <image-name>`

尽管这是一个很好的有效命令，Docker提供了更好的方式，可以将客户端命令传递给Docker后台进程。

在Docker1.13之前，Docker之支持上面语法的命令。后来，Docker重构了命令行语法：

`docker <object> <command> <options>`

在这个语法中：

- object表示你要操作的Docker对象，可以是container、image、network和volume对象。
- command表示Docker后台进程要执行的内容，比如说run。
- options可以是任何有效的覆盖默认行为的参数，例如--publish参数表示端口映射。

参照上面的语法，run命令可以写成：

`docker container run <image-name>`

image name可以是任何本地或者远程仓库上的镜像名称。例如，你也可以使用fhsinchy/hello-dock这个镜像名称，这个镜像包含了一个简单的vue.js应用，运行容器内应用启动监听80端口。想要运行这个镜像的容器，执行下面的命令：

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

命令行很好理解，需要解释的--publish 8080:80参数会在下一部分内容中解释。

<h3 id="7.2">7.2 怎样发布一个端口？</h3>

容器是隔离的环境。容器的宿主机并不知道容器内发生的一切。因此，容器外部是不能直接访问容器内部的。

如果想要容器外部访问容器，需要将容器内部的端口发布到容器宿主机的网路上，语法--publish和-p如下：

`--publish <host port>:<container port>`

当你像刚才命令中那样指定--publish 8080:80参数，意味着任何访问宿主机8080端口的请求都会被转发到容器内到80端口。

现在可以在浏览器中访问http://127.0.0.1:8080。

![](/img/posts/docker-handbook-2021-15.jpg)

你可以使用ctrl + c命令停止容器，命令行终端将会停止进程或者关闭整个终端。

<h3 id="7.3">7.3 怎样使用后台模式？</h3>
