---
layout: post
title: 'docker基本概念及常用命令'
subtitle: '什么是镜像，什么是容器；容器技术和虚拟化技术有什么不同；怎么启动一个镜像，进入镜像，映射镜像的端口到主机；主机和镜像之间怎么共享数据...记录一下！'
background: '/img/posts/docker-basic.png'
comment: false
---

# 目录

- [1. 什么是Docker?](#1)
    - [1.1 Docker的基本概念和术语](#1.1)
    - [1.2 容器技术与虚拟化技术的区别](#1.2)
    - [1.3 Docker镜像的分层架构](#1.3)
- [2. Docker的基本操作、命令](#2)
    - [2.1 镜像相关](#2.1)
    - [2.2 容器相关](#2.2)
    - [2.3 制作镜像相关](#2.3)
- [3. Docker实战](#3)
    - [3.1 启动一个Nginx镜像](#3.1)
    - [3.2 启动一个Mysql镜像](#3.1)
    - [3.3 制作一个应用镜像](#3.3)

---

<h3 id="1">1. 什么是Docker?</h3>

`Build Ship and Run`：构建 -- 发送 -- 运行。

`Build once, Run anywhere`：一次构建，到处运行。

<h4 id="1.1">1.1 Docker的基本概念和术语</h4>

**镜像**

Docker镜像是一个具有分层结构的文件，其中具有一切应用用以容器的形式运行所需的依赖。主要包括：

- 代码
- 运行环境
- 依赖类库
- 环境变量
- 配置文件等

镜像可以ship到任何Docker平台上，并且作为容器的形式运行。

**容器**

容器是镜像运行时的一个实例。基于同一个镜像可以在一个或者多个Docker平台上启动多个容器。

在同一宿主机上，容器与容器之间的隔离是通过不同的进程区分的，这些进程隔离了容器运行需要资源(例如内存等)。

> 类比：`程序` --> `进程`；`jar文件` --> `jvm实例`

**仓库**

存放Docker镜像的地方。分为私有仓库和公共仓库。

> 类比：`maven`、`yum`、`npm`等

**镜像标签(tag)**

标签是给镜像起的别名，一个镜像可以打多个标签。

**Volume(卷)**

Docker Volume可以将容器读写层(read-write layer)的数据持久化。这样：

- 宿主机和docker容器之间可以共享数据
- 容器删除之后，有价值的数据仍然能够存在
- 容器和容器之间可以共享数据

<h4 id="1.2">1.2 容器技术与虚拟化技术的区别</h4>

|          | Docker                  | Virtualization         |
| -------- | ----------------------- | ---------------------- |
| 底层技术 | Libcontainer            | Hypervisor             |
| 运行实例 | 容器                    | 虚拟机                 |
| 启动时间 | 秒级                    | 分钟级                 |
| 移植性   | 平台无关                | Hypervisor相关         |
| 体量     | 轻量级(lightweight)     | GB级                   |
| 隔离性   | 操作系统级别(OS-level)  | 硬件级别(machine-level)|

Docker：管理和部署linux容器的工具。

Hyersivor(虚拟机管理程序)：将操作系统和应用从一个主机的底层硬件中抽象分离出来的一个程序。

虚拟机(VMs)相比较容器来说，占用更过的系统资源。每一个虚拟机不仅复制了整个操作系统，而且还有提供操作系统运行的硬件的虚拟环境。而对于容器来说，它仅仅需要一个操作系统和支持指定应用运行的资源和类库。

如果说虚拟化技术从硬件级别提供了虚拟机之间的隔离，那么容器技术提供了操作系统级别的隔离。也就是说虚拟化技术共享的是底层的硬件，而容器技术共享的是操作系统的内核。

<h4 id="1.3">1.3 Docker镜像的分层架构</h4>

**Docker镜像的分层是什么？**

Docker的镜像是由一系列只读(read-only)镜像加上最上面可读写(readable/writeable)的镜像组成的。

```dockerfile
FROM registry.thunisoft.com:5000/base/tomcat:8-jdk8-9

ADD master.war /opt/tomcat/webapps/

LABEL acloud.env.appconfigServerUrl=""

LABEL acloud.env.EUREKA.URL=""

VOLUME ["/opt/tomcat/webapps"]
```

日志输出：

```
Step 1/5 : FROM registry.thunisoft.com:5000/base/tomcat:8-jdk8-9
 ---> a08a48b5703a
Step 2/5 : ADD master.war /opt/tomcat/webapps/
 ---> 072dc6b0e2f0
Step 3/5 : LABEL acloud.env.appconfigServerUrl=""
 ---> Running in 9ca0e3f1babc
Removing intermediate container 9ca0e3f1babc
 ---> d29d7689e2e7
Step 4/5 : LABEL acloud.env.EUREKA.URL=""
 ---> Running in 09738f555fcb
Removing intermediate container 09738f555fcb
 ---> 49fa8b2ce399
Step 5/5 : VOLUME ["/opt/tomcat/webapps"]
 ---> Running in 705adeb8812b
Removing intermediate container 705adeb8812b
 ---> 974758b70d87
Successfully built 974758b70d87
```

**Docker镜像为什么要分层？**

- 节省磁盘空间
- 复用中间层，快速启动

**Docker镜像怎么做到的分层？**

`Union Mont`：将众多目录组合起来挂载到一个目录上，就好像是这个目录包含了众多目录的所有内容。

`AUFS`： Advanced multi-layered unification filesystem。AUFS是`Union Mount`在Linux操作系统中的一种实现。

通过这种Union文件系统，Docker镜像能够将不同Layer的文件映射组合在一起，对外提供为一个只读的目录，如果上层Layer的文件和下层Layer的文件相同，则覆盖下层Layer的文件。

---

<h3 id="2">2 Docker的基本操作、命令</h3>

Docker在我们的日常工作中就像是git一样，是一个工具。如果有很浓厚的兴趣，可以做深入的研究。这些命令没必要一一记住，使用的时候查询并能够运用好就可以了。

<h4 id="2.1">2.1 镜像相关</h4>

```shell
# 查看本地镜像
docker image ls

# 拉取一个镜像
# Docker官方的仓库的镜像分为三种：官方维护的镜像、社区镜像和私人镜像
# <image-name>的命名一般格式为：${repository}/${user}/${name}
# 例如：registry.thunisoft.com:5000/jcdsj/eureka、hub.c.163.com/library/nginx
# 如果是想从指定的仓库拉取镜像，只要镜像名称是符合${repository}/${user}/${name}这个规范，就能拉取
# 默认是从官方的镜像拉取，如docker pull mysql
docker pull IMAGE[:TAG]

# 删除本地镜像
docker rmi IMAGE_NAME[:TAG]

# 给镜像起一个别名
# 每一个镜像的名字都是有TAG,默认的TAG是:latest
docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]

# 推送镜像到指定repository
# 镜像的名字已经决定了要推送的地址
# 从一个仓库中拉取再推送到另一个仓库需要起别名(tag)
docker push IMAGE[:TAG]

# 将镜像保存为一个文件
docker save -o <path for generated tar file> IMAGE[:TAG]

# 导入镜像
docker load -i <path to image tar file>
```

<h4 id="2.2">2.2 容器相关</h4>

```shell
# 查看容器
# 容器的状态可以是运行中(running),也可以是已退出(exited)
# 容器退出的时候容器内修改的状态会保存,不会丢失
docker ps [-a]

# 停止运行中的容器
# 发送信号停止进程(./stop.sh)
# 停止容器后,容器处在exited的状态
docker stop CONTAINER_ID|CONTAINER_NAME
# 直接杀死进程(kill -9)
docker kill CONTAINER_ID|CONTAINER_NAME

# 启动退出的容器
docker start CONTAINER_ID|CONTAINER_NAME

# 从某个镜像创建一个容器
docker run IMAGE[:TAG]
# docker run常用参数
--name="CONTAINER_NAME" #指定容器的名字
-d, --detach #以后台的方式运行
-i #以交互模式运行容器，通常与 -t 同时使用
-t #为容器重新分配一个伪输入终端，通常与 -i 同时使用
--rm #删除退出的容器
-p, --publish #宿主机的端口与容器内部端口做映射
--restart=always|on-failure... #容器退出后可以指定重启的策略
{-v|--volume "source:detination"}:CONTAINER_DES_PATH #将容器内部PATH绑定一个逻辑卷,如果source是绝对路径就是bind模式,如果不是默认创建一个volume

# 进入容器内部
docker exec -it CONTAINER_ID|CONTAINER_NAME /bin/bash
```

<h4 id="2.3">2.3 制作镜像相关</h4>

```shell
# 通过Dockerfile构建镜像

# Dockerfile是构建一个Docker镜像的源代码
# Dockerfile里面包含了需要构建一个镜像的所有命令
# Dockerfile命令
FROM registry.thunisoft.com:5000/bdasp/jdk8u151 #基于一个BASE镜像
LABEL maintainer="yanpengyu@thunisoft.com" #指定作者
RUN mkdir /bdmp/apps #创建一个目录
EXPOSE 8080 #暴露一个端口被其他linked container使用
ADD easytransfer.jar /bdmp/apps/easytransfer.jar #copy文件
WORKDIR /bdmp/apps # 指定工作目录
ENTRYPOINT ["java","-jar","easytransfer.jar"] #容器启动后执行
CMD ["npm", "start"] # 容器启动后执行
LABEL acloud.env.HADOOP_USER_NAME="hdfs" #环境变量
VOLUME /bdmp/apps #为容器内的目录创建一个卷

# docker 构建镜像
docker build -f Dockerfile --no-cache --force-rm -t registry.thunisoft.com:5000/jcdsj.3.4.2/master:3.4.2 .
# 参数 
--no-cache #不使用缓存
--force-rm #总是删除中间临时容器

# docker commit容器提交为一个镜像
docker commit CONTAINER_NAME|CONTAINER_ID IMAGE_NAME[:TAG]
```

**Dockerfile RUN CMD VS ENTRYPOINT**

三种指令都可以使用Shell或者Exec的形式来运行。

Shell and Exec格式：Shell格式运行实际上是调用了`/bin/sh -c <command>`,Exec格式运行直接调用的可执行的目录，不会进行shell的处理。

- Shell格式
    - <instruction> <command>
        - RUN apt-get install python3
        - CMD echo "Hello world"
        - ENTRYPOINT echo "Hello world"
- Exec格式
    - <instruction> ["executable", "param1", "param2", ...]
        - RUN ["apt-get", "install", "python3"]
        - CMD ["/bin/echo", "Hello world"]
        - ENTRYPOINT ["/bin/echo", "Hello world"]

三种指令的区别：

- RUN
    - 执行命名,并创建一个新的镜像Layer.常用来安装软件包
    - 命令格式
        - RUN <command> (shell form)
        - RUN ["executable", "param1", "param2"] (exec form)
- CMD
    - 设置默认的命令或者参数.该命令或者参数能够被Docker容器启动命令给覆盖
    - 命令格式
        - CMD ["executable","param1","param2"] (exec form, preferred)
        - CMD ["param1","param2"] (sets additional default parameters for ENTRYPOINT in exec form)
        - CMD command param1 param2 (shell form)
- ENTRYPOINT
    - 配置容器为一个可执行的容器
    - 命令格式
        - ENTRYPOINT ["executable", "param1", "param2"] (exec form, preferred)
        - ENTRYPOINT command param1 param2 (shell form)


**Dockerfile EXPOSE VS Docker RUN --publish**

- 既没有在Dockerfile中使用EXPOSE指令,也没有在Docker RUN中使用--publish参数
    - 容器的服务只能在容器内访问
- 仅仅在Dockerfile中使用EXPOSE指令
    - 容器的服务能在容器内访问,服务外除了链接(linked)容器外都不能访问
- 既在Dockerfile中使用EXPOSE指令,也在Docker RUN中使用--publish参数
    - 容器的服务在容器内和容器外都可以访问

**Docker volume VS BIND**

- volume不依赖宿主机的绝对路径，而是由Docker的Storage目录管理
- volume由Docker统一管理，方便备份和迁移
- Docker Volume可以由Docker CLI和Docker API调用

**使用Docker命令**

```shell
# 查看容器、镜像或者逻辑卷
Docker inspect IMAGE_NAME|CONTAINER_NAME|VOLUME_NAME

# 查看容器日志
docker logs CONTAINER_NAME|CONTAINER_ID
```

---

<h3 id="3">3. Docker实战</h3>

<h4 id="3.1">3.1 启动一个Nginx镜像</h4>

```
docker run -d --name=nginx-demo -v "nginx-demo-vol:/usr/share/nginx/html" -p 80:80 nginx
```

<h4 id="3.2">3.2 启动一个Mysql镜像</h4>

```
# 创建一个容器
docker run -d --name=mysql-comments-demo --env="MYSQL_ROOT_PASSWORD=root" --publish 13306:3306 mysql/mysql-server:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

# 进入容器修改访问限制
docker exec -it mysql-comments /bin/bash
mysql -uroot -proot
update mysql.user set host = '%' where user = 'root'

# 重启容器
docker restart mysql-comments-demo

# 访问
mysql -uroot -proot -h 192.168.20.45 -P 13306
```

<h4 id="3.3">3.3 制作一个应用镜像</h4>

```
# build.sh
docker build -f Dockerfile --no-cache --force-rm -t registry.thunisoft.com:5000/dbfybcw/dbfybcw:1.0 .

# Dockfile
from pluribuslabs/centos7-oracle-jdk-8

RUN mkdir /opt/dbfybcw

ADD comments-0.0.1-SNAPSHOT.jar /opt/dbfybcw

WORKDIR /opt/dbfybcw

ENTRYPOINT java -jar comments-0.0.1-SNAPSHOT.jar --spring.datasource.url=${DATASOURCE_URL} --mail.to=${MAIL_TO} --server.port=${SERVER_PORT}

```

---

**Reference**

- [https://www.edureka.co/blog/what-is-docker-container](https://www.edureka.co/blog/what-is-docker-container)
- [https://blog.hipolabs.com/understanding-docker-without-losing-your-shit-cf2b30307c63](https://blog.hipolabs.com/understanding-docker-without-losing-your-shit-cf2b30307c63)
- [https://medium.com/@jessgreb01/digging-into-docker-layers-c22f948ed612](https://medium.com/@jessgreb01/digging-into-docker-layers-c22f948ed612)
- [https://jingsam.github.io/2017/08/26/docker-save-and-docker-export.html](https://jingsam.github.io/2017/08/26/docker-save-and-docker-export.html)
- [https://medium.com/docker-captain/docker-volumes-d55d18aafbc0](https://medium.com/docker-captain/docker-volumes-d55d18aafbc0)
- [https://docs.docker.com/storage/volumes/](https://docs.docker.com/storage/volumes/)
- [https://medium.com/codingthesmartway-com-blog/docker-beginners-guide-part-1-images-containers-6f3507fffc98](https://medium.com/codingthesmartway-com-blog/docker-beginners-guide-part-1-images-containers-6f3507fffc98)
- [https://searchitoperations.techtarget.com/definition/Docker-image](https://searchitoperations.techtarget.com/definition/Docker-image)
- [https://www.freecodecamp.org/news/an-introduction-to-docker-tags-9b5395636c2a/](https://www.freecodecamp.org/news/an-introduction-to-docker-tags-9b5395636c2a/)
- [https://goinbigdata.com/docker-run-vs-cmd-vs-entrypoint/](https://goinbigdata.com/docker-run-vs-cmd-vs-entrypoint/)
- [https://stackoverflow.com/questions/22111060/what-is-the-difference-between-expose-and-publish-in-docker](https://stackoverflow.com/questions/22111060/what-is-the-difference-between-expose-and-publish-in-docker)
- [https://stackify.com/docker-build-a-beginners-guide-to-building-docker-images/](https://stackify.com/docker-build-a-beginners-guide-to-building-docker-images/)
- [https://blog.csdn.net/jiankunking/article/details/71190814](https://blog.csdn.net/jiankunking/article/details/71190814)

