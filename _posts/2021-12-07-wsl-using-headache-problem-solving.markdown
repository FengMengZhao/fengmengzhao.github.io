---
layout: post
title: 'Windows10 WSL2体验如此丝滑(Windows上使用完整服务的Linux)'
subtitle: '在Windows上使用一些Linux工具，一般会在本机装一个Linux虚拟机或者在Windows上装Docker，这两种方法对于本地开发环境来说都比较重。Windows推出了WSL，可以直接在Windows上使用完整的Linux功能，笔者用起来还是比较实用。但是想丝滑入WSL这个坑，要解决一系列问题，比如安装docker、动态IP端口映射等，本文针对wsl使用的痛点，一一给出使用办法。'
background: '/img/posts/wsl-resolve-headache-problems.jpg'
comment: false
weixinurl: 'https://mp.weixin.qq.com/s/5g-Dx9wl1iyW2pxwe4qtVg'
---

# 目录

- [1. WSL介绍](#1)
    - [1.1 WSL vs 虚拟机](#1.1)
- [2. WSL2丝滑入坑](#2)
    - [2.1 WSL动态IP，如何从外部访问？](#2.1)
    - [2.2 不安装Docker Desktop，如何安装Docker？](#2.2)
        - [2.2.1 删除已存在的docker](#2.2.1)
        - [2.2.2 安装依赖](#2.2.2)
        - [2.2.3 Ubuntu package仓库配置](#2.2.3)
        - [2.2.4 安装Docker](#2.2.4)
        - [2.2.5 设置docker命令权限](#2.2.5)
        - [2.2.6 配置dockerd](#2.2.6)
        - [2.2.7 启动dockerd](#2.2.7)
        - [2.2.8 dockerd启动脚本](#2.2.8)
        - [2.2.9 zsh设置开启自启dockerd](#2.2.9)
    - [2.3 Win和WSL系统如何打通任督二脉？](#2.3)
        - [2.3.1 文件系统互通](#2.3.1)
        - [2.3.2 程序调用互通](#2.3.2)
    - [2.4 设置WSL sshd服务（让你的Windows能ssh远程登录）](#2.4)
- [3. WSL2使用遇到的问题](#3)
    - [3.1 WSL和VirtualBox对于Hyper-v冲突](#3.1)
    - [3.2 Centos6在wsl2 docker上运行有问题](#3.1)
    - [3.3 WSL2自定义内存分配](#3.3)
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

网上对于`WSL`的吐槽很多，认为很难用。笔者亲身实践，使用`WSL2`能有丝滑般的开发体验，但也有一些头痛的问题，这里记录解决方案，方便诸位丝滑入坑`WSL2`。

<h4 id="1.1">1.1 WSL vs 虚拟机</h4>

如果你想在`Windows`系统上运行`Linux`发行版，在没有`WSL`之前，可以装一个虚拟机。

虚拟机和`WSL2`相比较，前者和宿主机之间文件系统是完全隔离的；而后者是相通的。

`WSL2`基于`Hyper-v`实现了`Linux`内核，所以`WSL`也算是一个完整的Linux系统。相较于虚拟机，`WSL2`会轻量级一些。但是支持的发行版也有限。

建议：如果没有接触过`Linux`，要学习`Linux`，最好自己装一个`Linux`虚拟机；而如果对`Linux`非常熟悉了，可以尝试安装`WSL2`，对开发人员来说`WSL2`会带来更好的开发体验。

<h3 id="2">2. WSL2丝滑入坑</h3>

WSL2的安装请参考文章：[https://dowww.spencerwoo.com/](https://dowww.spencerwoo.com/)。

WSL2迁移参考回答：[https://stackoverflow.com/questions/63252225/is-this-the-correct-way-to-import-a-wsl-export-overwriting-default-installati](https://stackoverflow.com/questions/63252225/is-this-the-correct-way-to-import-a-wsl-export-overwriting-default-installati)

WSL2默认安装在Windows的`C`盘，随着WSL2使用容量的变大，可能会造成`C`盘空间的预警。可以使用上面的办法将`WSL2`迁移到其他逻辑卷。实际上，WSL像虚拟机一样，在HOST的存储是一个映像文件，笔者Win10上安装的`WSL2-Ubuntu 20`已经使用了`40G`的存储，如图：

![](/img/posts/wsl-ext4-image-file.png)

> 如果将WSL2从开发环境电脑迁移到家中电脑，也是很方便的，将映像文件copy走，直接导入目标机即可。

笔者`WSL2`选择安装的是微软商店的`Ubuntu 20.04 LTS`，接下来的操作基于该Ubuntu发行版。

`WSL`的启动就是打开对应从微软商店安装的`Ubuntu`或者其他发行版（如下图），`WSL`关闭可以在`PowerShell`中使用：`wsl --shutdow`。

![](/img/posts/wsl2-ubuntu-from-microsoft-store.png)

<h4 id="2.1">2.1 WSL动态IP，如何从外部访问？</h4>

`WSL2`的网路类似于虚拟机中设置的`NAT`网络模式，该模式下`WSL2`实例借助于宿主机的网卡访问外部网络，宿主机也可以访问`WSL2`实例，但是宿主机所在局域网的其他主机不能访问`WSL2`实例。如图：

> 所谓的`WSL2`实例指的就是安装的某个`WSL2`支持的发行版，笔者使用的是`Ubuntu 20.04 LTS`。

![](/img/posts/wsl2-nat-network-ping-relation.png)

`WSL2`的网络地址是动态变化的（像docker容器一样，重启IP地址发生改变），并且局域网内的其他host都不能访问，这样太不方便了。

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

如果本地装的是虚拟机，安装docker就很容易了。但是在`WSL2`实例上安装，还是要费一些功夫。

官方的解决方案是建议在`WSL2`上安装`Docker Desktop for Windows`，可以参考：[https://docs.microsoft.com/zh-cn/windows/wsl/tutorials/wsl-containers](https://docs.microsoft.com/zh-cn/windows/wsl/tutorials/wsl-containers)。

如果是不想装`Docker Desktop`但又想在`WSL2`上使用`docker`，参考[https://dev.to/bowmanjd/install-docker-on-windows-wsl-without-docker-desktop-34m9](https://dev.to/bowmanjd/install-docker-on-windows-wsl-without-docker-desktop-34m9)给出解决方案，接下来做详细的说明。

> 这里只针对`WSL2`版本安装`docker`，`WSL1`版本不支持`docker`。

> 下面的配置针对`WSL2 Ubuntu 20.04 LTS`进行docker配置，其他的`WSL2`支持的发行版在用户、权限配置有差别，可参考原文章。

<h5 id="2.2.1">2.2.1 删除已存在的docker</h5>

```shell
sudo apt remove docker-engine docker docker.io docker-ce docker-ce-cli
```

<h5 id="2.2.2">2.2.2 安装依赖</h5>

```shell
sudo apt install --no-install-recommends apt-transport-https ca-certificates curl gnupg2
```

<h5 id="2.2.3">2.2.3 Ubuntu package仓库配置</h5>

设置os-release相关环境变量：

```shell
#执行完成后，可以在shel中执行$ID验证是否存在该变量
source /etc/os-release
```

让`apt`信任仓库：

```shell
#这里的${ID}是上面source命令执行后的环境变量ID，本示例是：ubuntu
curl -fsSL https://download.docker.com/linux/${ID}/gpg | sudo apt-key add -
```

新增仓库地址并更新仓库列表，以便`apt`能够用到：

```shell
#本示例变量替换后的结果为：deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable
echo "deb [arch=amd64] https://download.docker.com/linux/${ID} ${VERSION_CODENAME} stable" | sudo tee /etc/apt/sources.list.d/docker.list
sudo apt update
```

<h5 id="2.2.4">2.2.4 安装Docker</h5>

执行命令：

```shell
sudo apt install docker-ce docker-ce-cli containerd.io
```

<h5 id="2.2.5">2.2.5 设置docker命令权限</h5>

1. 可以使用root用户执行。这样做不符合规范，害怕出现`rm -rf /`的误操作。
2. 每次使用`sudo docker ...`的方式，普通用户使用sudo就会以`root`用户的身份来操作。这里将普通用户加上sudo功能是在`/etc/sudoers`中配置。`WSL2 Ubuntu`实例创建过程中默认将普通创建用户加入了`sudoers`中。

除此之外，还可以将用户加入某个组下，赋予组执行某个进程免密的设置。

```shell
#这里的$USER会解析为当前用户
sudo usermod -aG docker $USER
```

docker进程在`sudoers`中的免密配置如下：

```shell
# sudo visudo, 添加下面
%docker ALL=(ALL)  NOPASSWD: /usr/bin/dockerd
```

> `docker`属组可能是安装`docker`的命令创建的，如果访问`/etc/group`没有`docker`属组，可以用命令`sudo groupadd -g 999 docker`。

> 实际上这里还存在一个困惑，在Ubuntu系统中赋予一个普通用户加入某个属组，该属组可以设置免密以root用户身份操作某个命令。如果命令涉及到数据写入可能会报错`Permission denied`。比如`git`，设置完成会后要使用没问题还需要设置：`sudo chown -R $USER:$USER $GIT_REPO`。查了一下，原因可能是上面的设置确实是以`root`用户来执行命令，但是命令产生的结果会触发新的进程就是以普通用户执行了，所以要对目录有权限。

<h5 id="2.2.6">2.2.6 配置dockerd</h5>

设置一个docker socket使用的目录，设置权限让`docker`属组有权限写入：

```shell
DOCKER_DIR=/mnt/wsl/shared-docker
mkdir -pm o=,ug=rwx "$DOCKER_DIR"
chgrp docker "$DOCKER_DIR"
```

设置`dockerd`启动参数，编辑`/etc/docker/daemon.json`，配置如下：

```json
{
  "hosts": ["unix:///mnt/wsl/shared-docker/docker.sock"],
  "insecure-registries":["a.b.com:5000", "m.n.com:9500"]
}
```

> 如果没有对应的`/etc/docker/daemon.json`文件，可以手动创建。参数中的`insecure-registries`设置表示允许不安全的`http`镜像registry，有需要时候可以再配置。

<h5 id="2.2.7">2.2.7 启动dockerd</h5>

使用命令`sudo dockerd`就可以启动了。但是这种方式启动的`dockerd`，我们要启动一个容器需要这样：

```shell
docker -H unix:///mnt/wsl/shared-docker/docker.sock run --rm hello-world
```

我们可以用脚本启动`dockerd`，使用起来会更方便。

<h5 id="2.2.8">2.2.8 dockerd脚本启动</h5>

启动脚本：

```shell
DOCKER_DISTRO="Ubuntu-20.04"
DOCKER_DIR=/mnt/wsl/shared-docker
DOCKER_SOCK="$DOCKER_DIR/docker.sock"
export DOCKER_HOST="unix://$DOCKER_SOCK"
if [ ! -S "$DOCKER_SOCK" ]; then
    mkdir -pm o=,ug=rwx "$DOCKER_DIR"
    chgrp docker "$DOCKER_DIR"
    /mnt/c/Windows/System32/wsl.exe -d $DOCKER_DISTRO sh -c "nohup sudo -b dockerd < /dev/null > $DOCKER_DIR/dockerd.log 2
```

将脚本保存在`docker-service`放在`path`下，本示例放入`/usr/bin`中。执行`docker-service`即可启动`dockerd`。

<h5 id="2.2.9">2.2.9 zsh设置开启自启dockerd</h5>

本`WSL2 Ubuntu`使用的默认`sh`是`zsh`，设置`WSL2`开机启动`dockerd`可以在`/etc/zsh/zprofile`添加`source /usr/bin/docker-service`启动脚本：

![](/img/posts/wsl2-zsh-init-start-dockerd.png)

<h4 id="2.3">2.3 Win和WSL系统如何打通任督二脉？</h4>

<h5 id="2.3.1">2.3.1 文件系统互通</h5>

**win上访问WSL文件系统**

在wsl实例上执行`explorer.exe .`，或者直接打开Windows资源管理器，输入：`\\wsl$\Ubuntu-20.04\opt`。如下图：

![](/img/posts/wsl2-explorer-exe-from-wsl.png)

这种方式打开wsl文件系统目录，Windows用户对`WSL`普通用户数据有读写权限，对root用户是只读权限。并且使用这种方式打开文件修改并保存很卡顿，性能有问题。所以，**不建议在Windows系统上修改wsl实例文件系统的内容**。

**WSL上访问win文件系统**

`WSL2`将Windows的磁盘逻辑卷映射为挂载文件。`/mnt/c`为Windows C盘挂载路径；`/mnt/d`为Windows D文件挂载路径等如此这种。如果要在`WSL`上用`vim`打开Win系统上`D:\out.txt`，可以直接使用：`vim /mnt/d/out.txt`。

在`WSL`上操作`Windows`文件系统同样会有一定的性能问题，但是没有`Windows`操作`WSL`文件系统性能延迟严重。如果是像大一点的git仓库，最好克隆到`WSL`本地文件系统操作。

<h5 id="2.3.2">2.3.2 程序调用互通</h5>

`WSL`是直接能够读取`Windows`系统`PAHT`变量并在wsl上直接打开，比如在`WSL`上打开`ping.exe`：

![](/img/posts/wsl2-open-pingexe.png)

> 有些时候这个功能是很有用的，尤其是当你打开图形化应用的时候。比如用`notepad++`打开`WSL`系统中的一个文本文件等等。**记得Win系统PATH变量中的exe程序才能在WSL中找到**。

当然了，在`Windows`系统上也能够执行`WSL`命令，如在`PowerShell`中执行`wsl ping`：

![](/img/posts/wsl2-win-open-wslping.png)

二者的命令还可以组合使用，例如：在`PowerShell`中执行：`wsl ls -la | findstr "git"`。更多请参考：[https://docs.microsoft.com/en-us/windows/wsl/filesystems](https://docs.microsoft.com/en-us/windows/wsl/filesystems)

<h4 id="2.4">2.4 设置WSL sshd服务（让你的Windows能ssh远程登录）</h4>

WSL和你的Windows主机已经互通了，如果能够通过ssh访问WSL，那么就相当于ssh能访问Windows。

基本的配置思路：

1. WSL实例中开启sshd服务。
2. 将WSL的sshd服务监听端口和Windows主机某个端口做映射。
3. 外界（和Windows主机映射端口可达）通过ssh客户端连接WSL。

**配置WSL sshd服务**

```shell
#修改配置文件
#vim /etc/ssh/sshd_config

#设置需要认证
PasswordAuthentication yes

#设置登录用户，这里是WSL实例用户
AllowUsers ${wsl_yourusername}

#server会60s自动发送信号到Client，Client没有回应会记录下来，达到ClientAliveCountMax的次数
ClientAliveInterval 60
# 600分钟后断开连接
ClientAliveCountMax 600
```

**启动sshd**

```shell
#查看ssh状态
service ssh status

#启动服务
sudo service start 

#重启
sudo service ssh --full-restart
```

**ssh端口映射到Windows主机上**

```shell
netsh interface portproxy reset
$wsl_ip = (wsl hostname -I).trim().split()[0]
Write-Host "WSL Machine IP: ""$wsl_ip"""
netsh interface portproxy add v4tov4 listenport=${Windows主机端口} connectport=22 connectaddress=$wsl_ip
```

更多端口映射理解请参考：[WSL动态IP，如何从外部访问？](#2.1)

现在就可以通过ssh客户端命令`ssh -p ${Windows主机端口} ${wsl_yourusername}@${Windows主机IP}`登录WSL实例了，例如：`ssh -p 2222 user@172.22.28.22`

<h3 id="3">3. WSL2使用遇到的问题</h3>

<h4 id="3.1">3.1 WSL和VirtualBox对于Hyper-v冲突</h4>

报错：请启用虚拟机平台 Windows 功能并确保在 BIOS 中启用虚拟化。

因为之前在使用VirtualBox时候关闭了`Hyper-v`服务。设置自动开启Hyper-v模式：`bcdedit /set hypervisorlaunchtype auto`。

Hyper-v服务启动后，需要重启计算机，WSL能正常启动。

重启后启动v2ray程序，发现can not bind to `10809`，但是通过`netstat -ano|findstr 10809`没发现端口被占用。查找发现可能是Hyper-v程序预留的端口占用了，解决方法：

```shell
netsh int ip show dynamicport tcp #查看端口范围
netsh int ip set dynamicport tcp start=49152 num=16384 #设置新的端口范围，重启计算机
```

<h4 id="3.2">3.2 Centos6在wsl2 docker上运行有问题</h4>

`WSL2`的docker上运行基于`centos6`镜像的容器有问题，解决办法：在`%userprofile%\.wslconfig`上添加：

```shell
[wsl2]
kernelCommandLine = vsyscall=emulate
```

<h4 id="3.2">3.2 Centos6在wsl2 docker上运行有问题</h4>

在配置文件`%userprofile%\.wslconfig`配置如下内容：

```shell
memory=6GB
swap=1GB
localhostForwarding=true
processors=4
```

<h3 id="4">4. 引用</h3>

- [https://dowww.spencerwoo.com/](https://dowww.spencerwoo.com/)
- [https://superuser.com/questions/1582234/make-ip-address-of-wsl2-static](https://superuser.com/questions/1582234/make-ip-address-of-wsl2-static)
- [https://www.illuminiastudios.com/dev-diaries/ssh-on-windows-subsystem-for-linux/](https://www.illuminiastudios.com/dev-diaries/ssh-on-windows-subsystem-for-linux/)
- [https://dev.to/bowmanjd/install-docker-on-windows-wsl-without-docker-desktop-34m9](https://dev.to/bowmanjd/install-docker-on-windows-wsl-without-docker-desktop-34m9)
- [https://docs.microsoft.com/en-us/windows/wsl/filesystems](https://docs.microsoft.com/en-us/windows/wsl/filesystems)

---
