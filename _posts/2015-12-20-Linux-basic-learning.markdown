---
layout: post
title: Linux 基础 
subtitle: 'Linux的基础命令、操作、技巧等，记录下来，方便使用的时候查阅。'
background: '/img/posts/linux-basic.jpg'
comment: false
---

# 目录

- [1. Linux系统安装](#1)
    - [1.1 系统安装和基础设置](#1.1)
    - [1.2 网络设置](#1.2)
- [2. Linux基础知识](#2)
    - [2.1 根目录规划](#2.1)
    - [2.2 文件管理](#2.2)
    - [2.3 目录操作](#2.3)
    - [2.4 用户管理](#2.4)
    - [2.5 文件压缩](#2.5)
    - [2.6 网络设置](#2.6)
    - [2.7 selinux](#2.7)
- [3. Linux系统启动、RPM软件包和权限](#3)
    - [3.1 Linux系统启动流程](#3.1)
    - [3.2 Linux 安装软件，RPM：redhat package management](#3.2)
- [4. 用户对文件的操作权限](#4)
    - [4.1 基本权限操作](#4.1)
    - [4.2 用户与组](#4.2)
    - [4.3 acl权限分配](#4.3)
    - [4.4 sodo 分权-让一个人对一个应用程序有什么权限](#4.4)
- [5. Linux服务进程和任务计划](#5)
    - [5.1 服务进程](#5.1)
    - [5.2 设置本机启动](#5.2)
    - [5.3 任务计划(周期性的去执行一段命令)](#5.3)
- [6. Linux软件安装](#6)
    - [6.1 安装Java1.8.0](#6.1)
    - [6.2 安装Apache2.2.9(用gcc编译安装)](#6.2)
    - [6.3 安装Tomcat9.0.0](#6.3)
    - [6.4 用yum源安装mysql-server and mysql](#6.4)
    - [6.5 安装ruby,调试jeklly静态博客](#6.5)
    - [6.6 Linux ftp配置](#6.6)
        - [6.6.1 ftp映射本地Linux目录](#6.6.1)
        - [6.6.2 lftp下载ftp数据](#6.6.2)
        - [6.6.3 将FTP文件映射为本地目录](#6.6.3)
        - [6.6.4 将本地的一个目录挂载到另外一个目录上](#6.6.4)
        - [6.6.5 curl命令也可以交互ftp](#6.6.5)
    - [6.7 Linux rz sz 上传下载文件的使用](#6.7)
- [7. 日常小技巧](#7)
    - [7.1 监控某个端口的数据](#7.1)
    - [7.2 浏览器中点点点密码显示](#7.2)
    - [7.3 Notepad++使用技巧](#7.3)
    - [7.4 maven小技巧](#7.4)
    - [7.5 增量tar打包文件](#7.5)
    - [7.6 jar包更新某个文件并重新打包](#7.6)
    - [7.7 Linux ssh客户端连接服务端不掉线](#7.7)
    - [7.8 vim使用技巧](#7.8)
        - [7.8.1 vi/vim中用正确的字符编码打开文件](#7.8.1)
    - [7.9 Linux实用小命令](#7.9)
- [8. 专项问题](#8)
    - [8.1 磁盘占用一点点，但是df -lh显示已经占用100%](#8.1)
    - [8.2 Linux安装本地字符编码](#8.2)
    - [8.3 Linux 终端上能执行任务，在crontab定时任务上执行失败](#8.3)

---

<h3 id="1">1. Linux 系统安装</h3>

<h4 id="1.1">1.1 系统安装和基础设置</h4>

设置系统进入模式：`vi /etc/inittab`，需改`id:5:inintdefault:`为`id:3:initdefault:`，这样系统重启就进入了命令行模式。

常用指令：`history -c`：清空历史记录；`ls`：查看当前目录下的文件；`cd`：切换目录

`/dev/cdrom`：光驱；`/dev/sda`：scsi硬盘；`/dev/sda1`：第一块scsi硬盘的以一个主分区；`/dev/sdb2`：第二块scsi硬盘的第二个主分区；`/dev/sda7`：第一块scsi硬盘的第三个逻辑分区；`/dev/sdb4`：第二块scsi硬盘的第四个主分区或者扩展分区

网卡配置：`192.168.100.1`；检查ip是否生效：`ifconfig eth0`

如果没有ip，设置ip：`vi /etc/sysconfig/network-scripts/ifcfg-eth0`，设置`IPADDR=192.168.100.1 NETMASK=255.255.255.0 ONBOOT=yes BOOTPROTO=none`这里设置的是静态IP地址
		
	DEVICE=eth0
	HWADDR=00:23:24:8C:38:E5
	TYPE=Ethernet
	UUID=85441b22-21c0-47cd-a628-ffad599d1d2a
	ONBOOT=yes
	NM_CONTROLLED=no
	BOOTPROTO=static
	IPADDR=192.168.1.4
	NETMASK=255.255.255.0
	GATEWAY=192.168.1.1

设置免费的域名解析器：

`vim /etc/resolv.conf`，在文件的里面添加一行：`nameserver 168.95.1.1`

<h4 id="1.2">1.2 宽带连接虚拟机上网</h4>

* NAT网络连接方式可以让虚拟机连接上外网，但是宿主机不能访问虚拟机
* Host-only网络连接方式能够让宿主机访问虚拟机
* 虚拟机启用上述两种网络方式，均设置为`DHCP`的连接方式，可以实现宿主机访问虚拟机，也可以访问外网

**eth0**

    DEVICE=eth0
    HWADDR=08:00:27:4A:6C:B4
    ONBOOT=yes
    NM_CONTROLLED=no
    BOOTPROTO=dhcp

**eth1**

    DEVICE=eth1
    HWADDR=08:00:27:99:66:A3
    ONBOOT=yes
    NM_CONTROLLED=no
    BOOTPROTO=dhcp

---

<h3 id="2">2. Linux基础知识</h3>

**Linux基础命令：**

```
ctrl + c：强制中断当前程序

clear：请屏；`ctrl + l`：清屏

cd -：切换最近使用过的两次目录

pwd：查看当前路径

ctrl + u：清空当前的命令行

tab：命令补全功能

[root@localhost home]#：命令提示符 [当前用户名@主机名 当前目录]#

sed -n '<line_num_start>, <line_num_end>p' filename：显示文档的第start行到end行

#用鼠标划着就是复制，点右键就是粘贴(使用putty客户端)

```

<h4 id="2.1">2.1 根目录规划</h4>

`/home`：普通用户的家目录

`/root`：root用户家目录

`/dev`：磁盘管理器，专门用来管理设备的

`/media`：测试目录，挂载光盘

`/mnt`：测试目录，挂在U盘

`/boot`：Linux内核文件和init进程启动文件

`/etc`：应用程序的配置文件

`/usr`：用户自定义安装的应用程序或源代码

`/bin`：所用用户可执行的命令；`/usr/bin`：所用用户可执行的命令

`/sbin`：超级用户root才能执行的命令；`/usr/sbin`：超级用户root才能执行的命令

`/var`：日志文件

`/tmp`：临时文件

<h4 id="2.2">2.2 文件管理</h4>

新建文件：`vi file.txt`、`touch file.txt`

删除文件：`rm -rf file.txt`；删除所有文件和目录：`rm -rf *`

复制文件：`cp file.txt /mnt`

移动文件：`mv file.txt /mnt`

重命名文件：`mv file1.txt file2.txt`

查看文件内容：`cat file.txt`；从始行查看所有的文件内容：`cat file.txt |more`；查看文件的前十行：`head file.txt`；查看文件的后十行：`tail file.txt`；查看前五行：`head-5 file.txt`；查看后五行：`tail-5 file.txt`

**查找文件：**

查找包括file的文件名：`find / -name file*`

建立搜索数据库：`updatedb`，在数据库中查找文件：`locate file`(查找包含file的文件名，同上一个区分，此处不需要使用通配符)

**查找应用命令：**

`which ls`

**在文件总过滤查找：**

查找文件中包含root的那些行：`cat /etc/passwd |grep *root*`；查找文件中以root开头的那些行：`cat /etc/passwd |grep ^root`；查找文件中以root结尾的那些行：`cat /etc/passwd |grep root$`

**创建隐藏文件夹：**

`touch .a.txt`：文件名字前面加一个点就是隐藏文件夹；`ls -a`：查看所有文件夹

**同步目录文件：**

`rsync -azv /path/to/some_source_dir /path/to/some_destination_dir`，`-a`表示保留文件的时间戳；`-z`表示可以进行压缩；`-v`表示显示进度(verbose)。如果`some_source_dir`是目录并且以斜杠结尾，表示同步该目录内的内容，不包括该目录；如果是目录并且没有以斜杠结尾，表示同步整个目录（包括该目录）。如果`some_destination_dir`是目录并且以斜杠结尾，标识同步内容（无论内容是一个文件或者是一个目录）到该目录中（目录不存在会自动创建）；如果没有以斜杠结尾，同步的内容是文件或者目录，就同步为文件或者目录。

> 例如：`rsync -azv /path/to/some_source_dir $USERNAME@$HOST:/path/to/some_destination_dir`

`rsync`支持OpenSSL不同服务器之间免密传输文件。例如：`rsync -avz -e ssh /data/thunisoft/rel_zip_cs.csv root@146.0.11.60:/home/thunisoft/ --log-file="/data/thunisoft/logs-sb/sync-rel_zip_cs-${sync_date}.log"`

`rsync`支持删除源文件，对目标文件夹可以有不同的同步策略。

**查看目录文件占用磁盘的大小：**

`du -hs /path/to/some_dir`，`-h`表示human readable；`-s`表示不深入到目录的子目录(separate)

**查看文件的时间戳：**

`stat /path/to/some_file`，时间戳有Last Access Time：最近的使用事件；Last Modify Time: 最近的修改时间，指的是内容的修改；Last Change Time：最近的改变时间，指的是文件metadata的改变。

<h4 id="2.3">2.3 目录操作</h4>

目录创建：`mkdir dir1`

目录删除：`rm -rf dir1`

目录复制：`cp -R dir1 dir2`(递归的复制目录1给目录2)

目录移动：`mv dir1 dir2`

查看目录树：`tree dir1`

递归的创建新目录：`mkdir -p a/b/c/d/e/f`

查看目录的大小：`du -hs /path/to/some_file`, `-h`表示将字节转化为人类可读的形式(human)，`-s`表示不展示字母占用的大小，`du`命令表示磁盘占用的大小查看

<h4 id="2.4">用户管理</h4>

创建用户：`useradd user1`

**了解用户：**

用户的家目录：`/home/user1`

用户文件：`/etc/passwd`

组文件：`/etc/group`

密码文件：`/etc/shadow`，`!!`说明没有密码，`!`说明被禁用

**删除用户：**

`userdel -r user1`

**设置密码：**

`passwd user1`

**查看用户：**

`cat /etc/passwd`；`id user1`

**禁用用户：**

`usermod -L userl`

**启用用户：**

`usermod -U user1`

**修改账号和密码的有效期限**

```shell
#修改用户密码保持有效的最大天数
chage -M 180 USER_NAME
```

<h4 id="2.5">文件压缩</h4>

**gz 压缩包：**

压缩：`tar czf file.tar.gz *`(将当前目录的所有文件压缩)

查看压缩包中的内容：`tar tf file.tar.gz`

解压：`tar xzf file.tar.gz`

**zip 压缩包：**

压缩：`zip file.zip *`

查看压缩包中的内容：`unzip -l file.zip`

解压：`unzip file.zip`

<h4 id="2.6">网络设置</h4>

重新启动网络：`service network restart`

临时修改网卡ip：`ifconfig eth0 192.168.100.3`

**防火墙设置：**

**iptables 网络防火墙**

查看防火墙：`iptables -L`

清空防火墙策略：`iptables -F`(flush)

保存防火墙策略：`service iptables save`

关闭所有的`INPUT FORWARD OUTPUT`端口:

```
iptables -P INPUT DROP
iptables -P OUTPUT DROP
iptables -P FORWARD DROP
```

禁用/开放端口:

```
iptables -A INPUT -p tcp --dport 20 -j [ACCEPT|DROP]
iptables -A OUTPUT -p tcp --sport 20 -j [ACCEPT|DROP]
```

常用的规则配置：

```
#允许对外请求的返回包
iptables -A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT
#允许icmp包通过
iptables -A INPUT -p icmp --icmp-type any -j ACCEPT
#允许来自于lo接口的数据包，如果没有此规则，将不能通过127.0.0.1访问本地服务
iptables -A INPUT -i lo -j ACCEPT

#常用端口
iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 21 -j ACCEPT
iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 22 -j ACCEPT
iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 23 -j ACCEPT
iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 80 -j ACCEPT
iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 443 -j ACCEPT
iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 3306 -j ACCEPT
iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 8080 -j ACCEPT

#过滤所有非以上规则的请求
iptables -P INPUT DROP

#如果要添加内网ip信任（接受其所有TCP请求）
#注：(**.**.**.**)为IP,下同
iptables -A INPUT -p tcp -s **.**.**.** -j ACCEPT

#要封停一个IP，使用下面这条命令
iptables -I INPUT -s **.**.**.** -j DROP
#要解封一个IP，使用下面这条命令
iptables -D INPUT -s **.**.**.** -j DROP

#开放多个端口
iptables -A INPUT -p tcp --match multiport --dports 1024:65535 -j ACCEPT
```

删除规则：

```
iptables -L -n --line-number
iptables -D [INPUT|OUTPUT] ${num}
```
	
**Centos7防火墙：**

```
- `firewall-cmd --state`：查看防火墙运行状态
- `firewall-cmd --list-all`：查看所有开放的端口
- `systemctl [stop|start|restart] firewalld.service`：停运|启动|重启防火墙
- `systemctl disable firewalld.service`：禁止防火墙开机启动
- `firewall-cmd --zone=public --[add|remove]-port=1025/tcp --permanent`：永久开启|移除1025端口
- `firewall-cmd --reload`：重载配置(不用重启重启防火墙)
- `firewall-cmd --zone=public --add-port=1080/tcp --permanent`：开启1080端口
- `firewall-cmd --list-ports`：查看已经开发的端口
```

**Centos7设置静态网络：**

> `ip addr`查看网络名称，打开`/etc/sysconfig/network-scripts/ifcfg-${ip addr文件名}`

修改配置为：

```
TYPE=Ethernet
BOOTPROTO=none #修改
DEFROUTE=yes
PEERDNS=yes
PEERROUTES=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_PEERDNS=yes
IPV6_PEERROUTES=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens33
UUID=0ff4f0d2-641f-4080-9e24-3feb02de5542
DEVICE=ens33
HWADDR=00:0C:29:82:F8:C8
ONBOOT=yes #修改
IPADDR=192.168.20.27 #修改
PREFIX=24 #修改
GATEWAY=192.168.20.254 #修改
DNS1=192.168.8.3 #修改 会自动在/etc/resolve.conf中增加
```

查看网络地址：`ip addr`

重启网络：`systemctl restart network`

**Centos7修改hostname**

```
hostnamectl set-hostname <new-host-name>
```

<h4 id="2.7">2.7 selinux</h4>

`vi /etc/selinux/config`，设置：`SELINUX=disabled`

> selinux防火墙要彻底关闭，必须重启服务器

---

<h3 id="3">3. Linux系统启动、RPM软件包和权限</h3>

<h4 id="3.1">3.1 Linux系统启动流程</h4>

1. grub内核引导页面
2. 去第一块硬盘的第一块分区读取系统内核文件
3. 启动init进程
4. init去找自己的配置文件 `/etc/inittab`
5. init启动了很多子进程
6. 登录界面

**读取 `/etc/rc.d/rc.sysinit`脚本：**

1. 读取系统主机名
2. 识别、加载硬件驱动
3. 读取selinux防火墙
4. 设置系统字体
5. 进入一个简单的小欢迎页面
6. 设置系统闹钟
7. 设置文件和目录权限
8. 加载映射分区

**读取 `/etc/rc.d/rc`脚本并传入一个运行级别**

rc 3 就进去读取 `/etc/rc.d/rc.3`中的进程，以S开头是要启动的程序，以K开头的是要关闭从程序

<h4 id="3.2">Linux 安装软件，RPM：redhat package management</h4>

查看系统中所有安装的RPM包：`rpm -qa`

查看RPM包中是否安装Apache：`rpm -qa |grep httpd*`

挂在光盘到系统的Media目录下：`mount /dev/cdrom /media`

安装RPM包：`rpm -ivh httpd-2.2.3-43.el5.centos.i386.rpm`

强制卸载RPM包：`rpm -e httpd-2.2.3-43.el5.centos --force --nodeps`

查看RPM安装了些什么：`rpm -ql httpd-2.2.3-43.el5.centos.i386.rpm |more`，这里可以是本地的一个rpm包，也可以不加后缀，也可以是具体安装的应用。如：`rpm -ql /root/elasticsearch-5.0.0-thunisoft.ky10.ky10.aarch64.rpm`或者`rpm -ql elasticsearch-5.0.0-thunisoft.ky10.ky10.aarch64`或者`rpm -ql elasticsearch`

查看RPM的配置文件：`rpm -qc mariadb-server-10.3.9-8.ky10.aarch64.rpm`

根据RPM包安装的默认路径查看对应的包：`rpm -qf $(which mysql)`

查看rpm包的具体内容：`rpm -qi mariadb-10.3.9-8.ky10.aarch64`

**yum工具来安装RPM软件包**

准备工作

1. `cd /etc/yum.repos.d/`
2. `move CentOS-Base.repo CentOS-Base`(重命名操作)

配置yum光盘源配置文件

`vi CentOS-Media.repo`，设置：`baseurl=file:///media/(光盘挂载的目的地) gpgcheck=0(关闭gpg签名) enable=1(开启本光盘的yum源)`

查看系统中已经挂在的分区

`df`；`df -h`(将k单位换算成M和G)

> 如果光盘没有挂在，则需要挂载光盘：`mount /etc/cdrom /media`

让计算机重启后/dev/cdrom光盘自动挂载到/media下

`vi /etc/fstab`，加入一行：`/dev/cdrom/  /media  iso9660 defaults 0 0`，行的内容分别表示：硬件分区、挂载目录、文件系统、文件属性、读取顺序

yum查看已经安装的RPM包和yum源中的RPM包

`yum list`

yum工具安装RPM包

`yum -y install httpd*`

yum工具卸载RPM包

`yum -y remove httpd*`

> 此命令谨慎使用，因为会卸载很多相关软件，建议使用：`rpm -e httpd-2.2.3-43.el5.centos --force --nodeps*`

**RPM包会安装三个文件**

1. 应用程序：`/usr/sbin/httpd`
2. 配置文件：`/etc/httpd/conf/httpd.conf`
3. 服务脚本：`/etc/init.d/httpd`

**启动一个应用程序**

```
/etc/init.d/httpd
service httpd start
```

---

<h3 id="4">4. 用户对文件的操作权限</h3>

<h4 id="4.1">4.1 基本权限操作</h4>

1. `chmod`：权限分配
2. `acl`：权限分配
3. `sudo`：权限分配

**如何查看一个用户对一个文件有什么权限：**

```
ls -l
#r：读(4)；`w`：写(2)；`x`：执行(1)
#权限人群：`u`表示root用户；`g`root组内的其他用户；`o`表示其他用户；`a = u + g + o`表示所有人群
```

chmod权限分配

`chmod u+x,o+x rootfile`表示修改rootfile文件的权限，使root用户具有执行的权限，其他用户具有执行的权限

`chmod 747 rootfile`表示修改rootfile的权限，使root用户具有`7`权限，root组内的其他用户具有`4`权限，其他用户具有`7`权限

> - 7=4+2+1，即7表示读写执行的权限，6=4+2，即6具有读和写的全息...；`4`表示读的权限；`2`表示写的权限；`1`表示执行的权限
- 如果对所有的用户(root用户、root组内的其他用户和其他用户)进行权限操作，使用数字；如果只是某一类用户进行权限操作，用字母`u+x`...

<h4 id="4.2">4.2 用户与组</h4>

把用户加入到组中：`gpasswd -a user1 root`

把用户从组中删除：`gpasswd -d user1 root`

<h4 id="4.3">4.3 acl权限分配</h4>

设置：`setfacl -m u:user1:rwx rootdir`

查看：`getfacl -m u:user1:rx rootdir`

删除某一acl权限：`setfacl -x u:user1 rootdir`

删除所有的acl权限：`setfacl -b rootdir`

<h4 id="4.4">4.4 sodo 分权-让一个人对一个应用程序有什么权限</h4>

`visudo`，在打开的文件中设置：`user1 fmz(主机名默认是localhost)=/sbin/service httpd restart,/usr/sbin/useradd user4`或者`user1 localhost=/sbin/service httpd restart,/usr/sbin/useradd user2`

已经赋予权限的用户执行命令：`sudo /usr/sbin/useradd user2`，必须按照写出完整的命令格式，而且前面必须加 sudo

> - 永久修改主机名主机的名称：`vi /etc/sysconfig/network`；临时修改主机名：`hostname server`
- 查看主机名称：`hostname`
- 查看命令所在的位置：`which ls`
- 显示格式化时间：`date "+%Y-%m-%d %H:%M:%S"`

---


#### shell技巧

`|`管道符，用于过滤

`tab`补全

`history`查看历史命令

`!vi`调用历史中离你最近的一次以vi开头的命令

##### 帮助方法

`ls --help`；`zip -h`

**Windows CMD技巧**

查看端口号-PID：`netstat -nao | findstr ${PORT}`；Linux中使用：`netstat -natp |grep ${PID OR PROCESS_NAME}`

> -n：代表显示数字；-a：表示显示所有状态；-l：表示只显示LISTENING状态；-t：表示tcp协议；-u：表示udp协议；-p：表示显示程序的名字；-o：表示显示timer

查看PID：`tasklist |findstr ${PID}`

强制结束进程：`taskkill /pid ${PID} -t -f`

在目录中查找包含字符串的文件：`findstr /s /i "匹配字符串" *.*`(注意这里要寻找的字符串用双引号引起来)；linux中的命令是：`grep -rn '匹配字符串' *.*`

### Centos中下载rpm包及包的依赖

第一种方法：

```
yum install yum-plugin-downloadonly #下载工具
yum install --downloadonly --downloaddir=<directory> <package-name>
```

第二种方法：

```
yum install yum-utils #下载工具
yumdownloader --resolve --destdir=<directory> <package-name>
```

### Linux挂载的理解

所谓的挂载就是将Linux的某个目录(逻辑分区)执行磁盘的某个物理分区上，这样在访问该目录的时候实际上是读取所挂载的物理磁盘。

如果一个应用需要的磁盘空间很大，一般情况下是数据越来越大，我们可以通过将数据目录挂载到一个新的磁盘的方法来实现扩容。如果挂载前要挂载的目录中存在数据，则挂载以后该数据还存在但是不能被读取，需要将原来目录的数据迁移到新的要挂载的磁盘上。

### Linux中yum仓库的问题

Linux的默认yum源是在`/etc/yum.repos.d/*.repo`中定义的，会定义仓库的地址。

当Linux处在内网无法连上公共仓库地址的时候，要安装一些工具包是比较麻烦的，解决的思路有三个：

1. 从和内网处于同一个状态的虚拟机中下载该工具的rpm包以及其所有的依赖(使用yumdownloader和createrepo命令)，将rpm包拷贝到内网环境中安装；
2. 使用源代码编译的方式：`./configure make make install`
3. 挂载本地yum仓库，可以挂载Linux ISO安装包

使用本地yum仓库的配置命令：

```
# 将原来的yum源备份
mkdir -p /etc/yum.repo.d/bak
mv /etc/yum.repo.d/*.repo /etc/yum.repo.d/bak

# 打开文件
vi local.repo

# local.repo的内容
[local]
name=local
baseurl=file:///repo #可以挂载到ISO文件上，也可以将ISO文件解压放在/repo目录中
enabled=1
gpgcheck=0

# 更新yum的缓存
yum clean all 
yum makecache
```

---

<h3 id="5">5. Linux服务进程和任务计划</h3>

<h4 id="5.1">5.1 服务进程</h4>

**进程管理**

查看进程：`ps -ef |grep httpd`；`pstree |grep httpd`；`pstree -p |grep httpd`(查看PID，即进程号)

杀掉进程：`kill -9 pid`；`killall httpd`；`pkill httpd`(这个好用)

查看端口：`netstat -tunpl |grep http`；`netstat -tunpl |grep 80`

<h4 id="5.2">5.2 设置本机启动</h4>

`vi /etc/rc.d/rc.local`，设置添加：`service httpd start`，设置：`chkconfig --level 3 httpd on`。这样在开机之后Apache就自动启动了。

<h4 id="5.3">5.3 任务计划(周期性的去执行一段命令)</h4>

编辑任务计划：`crontab -e`

> 在Linux可以通过`pgrep cron`查看定时任务是否后台运行。启动cron：`service cron start`

查看任务计划：`crontab -l`

删除任务计划：`crontab -r`

计划任务模板：`*/2 * * * * /bin/echo "fmzfmzfmz" >> /mnt/file`表示每个两分钟向file文件中追加内容"fmzfmzfmz"

> 五颗星分别代表：分 时 日 月 周

---

<h3 id="6">6. Linux软件安装</h3>

<h4 id="6.1">6.1 安装Java1.8.0</h4>

1. `wget http://download.oracle.com/otn-pub/java/jdk/8u65-b17/jdk-8u65-linux-i586.tar.gz`，从网络上下载Java的最新版本
2. `mv jdk-8u65-linux-i586.tar.gz /usr/local/java`，将压缩文件剪切到指定的文件夹（方便管理）
3. `tar zxf jdk-8u65-linux-i586.tar.gz`，强压缩文件解压到当前目录,这里的z参数可以省略，另外前面的`-`也可以省略，这个操作需要管理员的权限
4. `alternatives --install /usr/bin/java jave /usr/local/java/jdk1.8.0_65/bin/java 2`，这里的数字`2`表示优先级，0表示第一优先级，其他数字一次类推。将安装的执行文件java创建到符号链接/usr/bin/java中
5. `alternatives --config java`，指定/usr/bin/java中链接的java版本

- symbolic link是符号链接，一个符号链接可以指向一个执行程序，也可以指向另外一个符号链接，具有很大的灵活性，这也是与快捷方式不同的地方
- `which java`只能够查看这个软链接的地址，而不能查看真正查看执行程序所在的位置，如果想要定位执行程序所在的位置，需要：`readlink -p $(which java)`，这里`-p`这里的意思是递归所有的软链接，如果不加此参数只能指向第一个软链接
- 如果想要使用`alternatives --install`来控制版本，同时想要创建PATH内的软链接，则需要：`ln -s /usr/local/java/jdk1.8.0_65/bin/jar jar`，意思是："在当前目录下创建一个名字为jar的软链接，这个软链接指向/usr/local/jdk1.8.0_65/bin/jar"；然后再使用"alternatives"命令安装：`alternatives --install /usr/bin/jar jar /usr/local/jdk1.8.0_65/bin/jar 2`即可

<h4 id="6.2">6.2 安装Apache2.2.9(用gcc编译安装)</h4>

1. 将文件下载，移动到指定的文件夹，并解压
2. `cd 解压后的目录`，在解压的目录中进行操作
3. `./configure --prefix=/usr/local/apache2/ --sysconfdir=/usr/local/apache2/etc/ --with-included-apr --enable-so --enable-deflate=shared --enable-expires=shared --enable-rewrite=shared`，作用是制作编译地图，指定文件要安装的位置和配置文件的放置位置，以及其他一些问题
4. `make`，第3步完成之后，如果成功，则会生成一个地图，也就是Make文件，第一个不完成的前提是一定要有gcc编译器、
5. `make install`，第4步完成，在没有报错的情况下，可以进行安装。安装完成后就可以在制定的目录下的bin目录中找到`apachectl`这个就是执行程序，然后可以创建软链接

<h4 id="6.3">6.3 安装Tomcat9.0.0</h4>

**1. 从官方现在最新的安装包，从Binary Distribution中下载`tar.gz`，解压到制定的问价中**

**2. 设置Java的家目录：`vi /root/.bash_profile`(用户环境)或者`vim /etc/profile`(系统环境)，在export PATH下面添加：**

```
export JAVA_HOME=/path/to/JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
#source /etc/profile`可以生效配置文件
```

**3. 设置Tomcat的家目录：`vi /root/.bash_profile`,在export PATH下面添加：`export CATALINA_HOME=/usr/local/tomcat9/apache-tomcat-9.0.0`，可以通过`env`查看系统的环境变量，可以通过`echo $JAVA_HOME`来查找单一的环境变量；如果想要定义的环境对所有的用户都生效，需要在`/etc/profile`文件中添加上述信息**

**4. 定义symbolic link**

<h4 id="6.4">6.4 用yum源安装mysql-server and mysql</h4>

1. `yum list |grep mysql`
2. `yum -y install mysql.i386`
3. `yum -y install mysql-server.i386`，一定要安装mysql-server之后，MySQL才会启动，没有服务怎么启动呢？
4. `service mysqld start`，启动mysql-server服务
5. `mysql -u root`，启动mysql，初次启动，没有密码
6. 进入MySQL后，为MySQL的root用户设置密码：`use mysql ;`  `update user set password=PASSWORD("your_new_password") where user="root" ;`  `flush privileges ;`  `quit ;`
7. `service mysqld stop`，之后重新启动mysql-server，然后 `mysql -u root -p`进入MySQL(命令敲击后，会让你输入密码)

<h4 id="6.5">6.5 安装ruby,调试jeklly静态博客</h4>

[下载ruby源代码](https://www.ruby-lang.org/en/downloads/)

Linux环境中如果没有gcc,安装group: `Development Tools`

`sudo yum groupinstall 'Development Tools'`

安装开发环境

`yum install openssl* openssl-devel zlib-devel gcc gcc-c++ make autoconf readline-devel curl-devel expat-devel gettext-devel`

安装ruby

    ./configure --enable-shared --enable-pthread --prefix=/usr/local/ruby
    make && make install

<h4 id="6.6">6.6 Linux ftp配置</h4>

客户端连接ftp server分两种模式：主动模式(Active)和被动模式(Passive)。

主动模式(Active)工作原理：需要数据传输时，客户端开启一个RANDOM端口，并通过21端口将RANDOM端口告知server，并监听。Server端主动通过默认20端口和RANDOM端口建立数据传输通道。

被动模式(Passive)工作原理：由客户端发起PASV(passive mode)的数据传输通道连接请求。客户端被动接受，所以称为被动模式。

**Vsftpd Active、Passive配置：**

```
pasv_enable=NO #(passive模式关闭)
pasv_min_port=3000
pasv_max_port=4000
port_enable=YES #(active模式开启)
connect_from_port_20=YES #(默认Active Mode情况下server端数据传输通过20端口)
```

**Apache FTP Active、Passive配置：**

修改配置文件：`$FTP_HOME/res/conf/ftpd-typical.xml`

```shell
<listeners>
	<nio-listener name="default" port="2121">
		<ssl>
			<keystore file="./res/ftpserver.jks" password="password" />
		</ssl>
		<data-connection idle-timeout="60">
			<active enabled="true" local-address="0.0.0.0" local-port="2120" ip-check="true" />
			<passive ports="10000-20000" address="0.0.0.0" external-address="0.0.0.0" />
		</data-connection>
	</nio-listener>
</listeners>
```

<h5 id="6.6.1">6.6.1 ftp映射本地Linux目录</h5>

- 安装curlftpfs
    - yum -y install epel-release
    - yum -y install curlftpfs
- 挂卸载
    - 挂载：`curlftpfs -o codepage=utf8 ftp://${ftp_username}:${ftp_password}@${ftp_if}:${ftp_port} ${local_path}`
    - 卸载：`unmount ${local_path}`

<h5 id="6.6.2">6.6.2 lftp下载ftp数据</h5>

```
#!/bin/bash

# -e引号内是命令
# /dev/null 吃掉stdout日志
# 2>nohup 错误日志输出到nohup中
nohup lftp -u drspInner,123456 -e"mirror -e -n -v /home/drspInner/test ./" xx.xx.30.57 >/dev/null 2>nohup &
```

<h5 id="6.6.3">6.6.3 将FTP文件映射为本地目录</h5>

本文档介绍了如何安装curlftpfs,及使用该工具将ftp中的目录映射成linux本地的目录，以方便solution读取ftp上的文件。

**组件功能说明**

通过该组件可以 将FTP目录 映射为 Linux上的文件目录，这样ETL程序就可以像访问本地文件一样访问FTP上的文件。

本文档介绍了如何安装curlftpfs,及使用该工具将ftp中的目录映射成linux本地的目录，以方便solution读取ftp上的文件。

**组件原理说明**

该功能是通过软件curlftpfs来实现的。

**安装curlftpfs ：**

离线安装：

```shell
tar -zxvf curlftpfs-0.9.2.tar.gz
cd  curlftpfs-0.9.2/
./configure
make
make install
```

在线安装：

```shell
yum -y install epel-release
yum -y install curlftpfs
```

**Linux挂载目录：**

```shell
#将ftp上的目录通过curlftpfs挂载到指定的 /xxx目录下： 
curlftpfs ftp://ftpusername:ftppasswd@ftpip:ftpport /xxx
#如：curlftpfs -o codepage=utf8   ftp://admin:admin@xx.xx.32.221:2121 /data/bigdata_v/ftpmapdir
```

**卸载挂载目录：**

```shell
umount /xxx
```

**设置开机启动/开机加载：**

```shell
#当挂载该映射目录的服务器重启后，该映射目录会失效；当ftp服务器重启后，该映射目录不会失效。
#因此，当挂载该映射目录的服务器重启后，需要手动重新挂载该目录到FTP服务器。或将其挂载命令设置为开机启动/开机加载。
#开机启动项的设置如下：
vi /etc/rc.d/rc.local
#添加挂载命令：
curlftpfs -o codepage=utf8 ftp://admin:admin@xx.xx.32.221:2121 /ftp/ftpmapdata
#确保文件rc.local具有可执行权限：
chmod a+x /etc/rc.d/rc.local
```

<h5 id="6.6.4">6.6.4 将本地的一个目录挂载到另外一个目录上</h5>

```shell
#将/path/to/newfolder目录挂载到/path/to/folder，类似于软连接的概念
#该方法也可以用来扩容
mount -o bind /path/to/folder /path/to/newfolder
#例如：mount -o bind /acloud_cp/acloud/docker/volumes /acloud/docker/volumes

#如果需要改在永久生效，需要修改/etc/fstab，增加配置
/path/to/folder /path/to/newfolder none defaults,bind 0 0
```

详细参考文章：[https://gamblisfx.com/how-to-mount-a-folder-to-another-folder-on-linux/](https://gamblisfx.com/how-to-mount-a-folder-to-another-folder-on-linux/)

<h5 id="6.6.5">6.6.5 curl命令也可以交互ftp</h5>

从ftp下载`xxx.txt`文件：`curl ftp://141.151.1.102:8080 -u admin:123456 -o xxx.txt`

> 没有`-o`参数可以展示文本内容，不能展示二进制内容。

从ftp上传`xxx.txt`文件：`curl ftp://141.151.1.102:8080 -u admin:123456 -T xxx.txt`

ftp展示目录：`curl ftp://141.151.1.102:8080 -u admin:123456 -s`

> 展示ftp路径内容最好在路径后面跟上`/`。

> `-u $FTP_USER:$PASSWD`也可以在ftp中写成`ftp://$FTP_USER:$PASSWD@$HOST:$PORT`，例如：`curl ftp://141.151.1.102:8080 -u admin:123456 -s` --> `curl ftp://admin:123456@141.151.1.102:8080 -s`。

> 默认`curl`命令和ftp交互使用的是`pasv`模式，参数是：`--ftp-pasv`。

**新增用户**

编辑`res/conf/users.properties`，复制已有的user信息新增即可，如：

```shell
ftpserver.user.$FTP_USER.userpassword=$PASSWD
ftpserver.user.$FTP_USER.homedirectory=/data/ftp_data
...
```

<h4 id="6.7">6.7 Linux rz sz 上传下载文件的使用</h4>

安装：`yum -y install lrzsz`。

使用：

- `rz`(receive Zmodem)：上传文件。
- `sz`(send Zmodem)：下载文件到本地。

`lrzsz`需要`Zmodem`协议的支持，默认`MobaXterm`和`XShell`都有实现。在`MobaXterm`上使用`lrzsz`：

1. 输入rz/bz
2. Ctrl加右点击，选择Receive file using Z-modem/Send file using Z-modem。

---

<h3 id= "7">7. 日常小技巧</h3>

<h4 id="7.1">7.1 监控某个端口的数据</h4>

`tcpdump -i eth1 port 514` #eth1是网卡的名称

<h4 id="7.2">7.2 浏览器中点点点密码显示</h4>

密码输入框中的隐藏密码在控制台显示：`$0.value`

<h4 id="7.3">7.3 Notepad++使用技巧</h4>

```
# 将匹配到所有字符处理后替换(基于匹配到的字符处理)
# 正则匹配表达式需要用括号括起来，例如：数据：ABC，正则式为：(.*$)；替换为：insert into '\1'，结果为insert into 'ABC'。原理是用正则表达式匹配字符串，用括号隔开，可以用\1 \2等数字第几组匹配进而替换
(.*$)
insert into '\1'

# 删除重复行
# notepad中选中匹配新行(.matches newline),替换正则为空
^(.*?)$\s+?^(?=.*^\1$)

# notepad中使用非贪婪模式的正则表达式
^(.*?)\t
```

<h4 id="7.4">7.4 maven小技巧</h4>

```
# 清除本地仓库的jar包
mvn dependency:purge-local-repository -DreResolve=false

# 安装本地jar包到仓库
mvn install:install-file -Dfile=lib\artery-core-6.1.4.jar -DgroupId=com.thunisoft.artery -DartifactId=artery-core -Dversion=6.1.4 -Dpackaging=jar -DgeneratePom=true

# 查找某一个jar包的依赖关系
mvn dependency:sources -DincludeArtifactIds=guava

# 查找maven依赖并输出到文件中
mvn dependency:tree -DoutputType=graphml -DoutputFile=dependency.graphml
```

<h4 id="7.5">7.5 增量tar打包文件</h4>

```shell
#tar中指定z参数会压缩，否则生成的只是一个归档文件，没有压缩
tar --listed-incremental=202103031022.file -czvf backup.tar.gz testincreasetar/
cp 202103031022.file 202103031025.file.1
tar --listed-incremental=202103031025.file.1 -czvf backup.tar.gz.1 testincreasetar/
#增量压缩的时候要创建另外一个tar.gz文件
```

<h4 id="7.6">7.6 jar包更新某个文件并重新打包</h4>

**1). 使用zip命令解压、修改、更新包**

```shell
#解压jar包
unzip -x xxx.jar -d xxx.jar.tmp
#进入临时目录
cd xxx.jar.tmp

#修改文件

#在临时目录中更新jar包
#-r表示递归更新当前目录中的内容
zip -ru ../xxx.jar *
```

**2). 使用jar命令解压、修改、更新包**

```shell
#查看jar包内文件
jar tf xxx.jar

#解压出来需要修改的文件
jar xf xxx.jar path/xxx.yml

#修改文件
vim path/xxx.yml

#更新修改后的文件到jar包中
jar uf xxx.jar path/xxx.yml
```

**3). 修改第三发jar包中的某个源代码并编译、重新打包**

```shell
mkdir dir
cd dir
jar xvf ../SomeClass-belong-to.jar #解压
vim /some/path/SomeClass.java #modification
javac -source 1.x /some/path/SomeClass.java #使用原来编译该jar包的版本编译该java文件 通过javap -verbose 查看编译版本
jar Mcf ../SomeClass-belong-to.jar * #重新打包
```

<h4 id="7.7">7.7 Linux ssh客户端连接服务端不掉线</h4>

经常用ssh客户端连接购买的云服务主机时总是掉线，过一会儿就要重连。提供两种方法解决：

1). 配置服务端`ssh`服务，参考[https://fengmengzhao.github.io/2021/12/07/wsl-using-headache-problem-solving.html#2.4](https://fengmengzhao.github.io/2021/12/07/wsl-using-headache-problem-solving.html#2.4)

2). 对于方法1)，可能在一些云厂商那里不生效。可以实用`tmux`（是一个程序），很实用、方便。

<h4 id="7.8">7.8 vim使用技巧</h4>

**删除匹配/不匹配行**

```shell
#全局删除匹配到的行
:g/pattern/d

#删除1-10行里匹配到的行
:1,10g/pattern/d

#删除不包含指定字符的行
#可以这样
:g!/pattern/d
#也可以这样
:v/pattern/d
```

**分组替换**

```shell
#正则匹配表达式需要用括号括起来，例如：数据：ABC，正则式为：(.*$)；替换为：insert into '\1'，结果为insert into 'ABC'。原理是用正则表达式匹配字符串，用括号隔开，可以用\1 \2等数字第几组匹配进而替换
#vim中执行需要转意括号
:%s/\(^.*$\)/inster into '\1'/g
```

**一些示例**

```shell
#截取字符串，插入换行符，删除不匹配的行
:/[0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}
:%s/\([0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\)/\r\1\r/g
:%g!/[0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}/d
:sort u
:w! v1.x_base_extract.txt

#截取字符串，插入换行符，删除不匹配的行
:/[0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}_[0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}
:%s/\([0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}_[0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\)/\r\1\r/g
:%g!/[0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}_[0-9]\{6\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}/d
:sort u
:w! v1.x_underline1_tmp.txt

#整合文件内容，删除\r(^M)字符
cat v1.x*extract* > v1.x_result.txt
:%s/\r//g
:sort u
```

**vim在命令模式下贴入剪切板内容方法**

`vim`命令模式下输入`:`或者`/`，按键`Ctrl r`，光标会出现`"`符号，再按键`"`或者`Shift +`，会填充`vim`复制或者剪切板中的内容。

<h4 id="7.9">7.9 Linux实用小命令</h4>

**替换目录下所有文件内容匹配到的字符串**

```shell
sed -i "s/oldstring/newstring/g" `grep oldstring -rl yourdir`

#例如：替换/home下所有文件中的www.bcak.com.cn为bcak.com.cn
sed -i "s/www.bcak.com.cn/bcak.com.cn/g" `grep www.bcak.com.cn -rl /home`
```

**查找某个目录下所有包含字符的文件并替换**

```shell
#查找当前目录中所有包含ip(xx.xx.12.13)的txt文件并替换为141.151.1.111
find . -type f -name '*.txt' -exec sed -i 's/xx.xx.12.13/141.151.1.111/g' {} \;

#将xml文件中包含【null as xxx,】的字符替换为【CAST(null as char) as xxx,】，最后的i表示忽略大小写
find . -type f -name '*.xml' -exec sed -i 's/null[[:space:]]\{1,\}as \([a-zA-Z0-9 ]*\),/CAST\(null as CHAR\) as \1,/gi' {} \;
```

**查看某一个目录下所有大文件**

```shell
find / -type f -size +1024M  -print0 | xargs -0 du -h
```

**Linux中用vim替换Windowns换行符^M**

```shell
#1
:%s/^M//g

#2
:set ff=unix;

#3
:%s/\r//g替换试一试
```

**vim中匹配到的内容转大小写**

```shell
#将<source_table>db_jcy.t_abc</source_table> 转化为 <source_table>DB_JCY.T_ABC</source_table>
#\U\1 --> 将匹配到分组内容转为大写
:%s/<source_table>\(.*\)</<source_table>\U\1>/g

#将<source_table>DB_JCY.T_ABC</source_table> 转化为 <source_table>db_jcy.t_abc</source_table>
#\L\1 --> 将匹配到分组内容转为小写
:%s/<source_table>\(.*\)</<source_table>\L\1>/g
```

<h5 id="7.8.1">7.8.1 vi/vim中用正确的字符编码打开文件</h5>

```shell
:set fileencoding #展示当前文件的字符编码
:set fileencoding=UTF-8/gbk #将文件编码设置为UTF-8/gbk
:set fileencodings; #展示vim编码探测的顺序，如果没有对应的文件编码（如gbk)可以加上（当然前提是本地系统支持该字符集（有安装））
```

参考链接：[https://blog.csdn.net/sdcxyz/article/details/23467657](https://blog.csdn.net/sdcxyz/article/details/23467657)

<h3 id="8">8. 专项问题</h3>

<h4 id="8.1">8.1 磁盘占用一点点，但是df -lh显示已经占用100%</h4>

一般出现磁盘空间占满的情况，首先在系统用`df -lh`看看是哪个目录满了，在对目录中的文件执行`du -lh --max-depth=1 ./xxx`，看看到底是谁占满了（一般是日志，或者临时文件），删除临时或者无用文件即可。

但是会出现用`df -lh`查看某挂载目录占用`100%`，而用`du -lh`命名查看实际目录文件的大小只有一点点（很小）。

这时候一般有两种情况，分别加以说明：

**情况一：子目录原来文件很大，当前挂载下没删除文件，又重新挂载到其他硬盘文件很小**

`./xxx`目录下某个子目录`./xxx/child_folder`被挂载到其他硬盘上，在挂载前该目录(`child_folder`)中含有大量的文件没有删除，而挂载后目录内的文件很小。这样，当你在用`du -lh`命令统计`./xxx`目录大小时，其统计的`child_folder`是挂载后的文件量，是很小的。而`df -lh`它统计的是某个磁盘下(`./xxx`挂载的磁盘)的空间，是计算的`child_folder`挂载前的大量文件。这样就造成了二者的不一致。

解决办法，先临时将`./xxx/child_folder`从目前的挂载中卸载下来，这样`df -lh`和`du -lh`的统计应该就一致了，再找出文件占满的原因，根据需要删除无用文件即可。最后记得把卸载的目录挂载回去。

Linux卸载/挂载的命令：

```shell
umount ./xxx/child_folder

mount /dev/sdb2  ./xxx/child_folder
```

**情况二：某进程删除的文件依然占用空间，也就是程序删除文件了，但是没有释放链接，空间还占用着**

和情况一不同，这时候确认`./xxx`目录下没有子目录挂载其他磁盘，但是`df -lh`和`du -lh`查出来的相差`90%`多。这个时候有能是删除的文件没有释放空间，用命令：

```shell
#查看是否删除的文件还被进程占用着
lsof |grep delete

#确认进程并重启
#笔者遇到的是rsyslog
systemctl restart rsyslog

#如果还解决不了
#看一看问题目录是怎么挂载的，重新挂载即可
```

<h4 id="8.2">8.2 Linux安装本地字符编码</h4>

```shell
#安装中文字体支持
sudo apt-get install language-pack-zh-hans
#查看本地支持的字符集
locale -a
#修改本地支持的字符集
sudo vim /var/lib/locales/supported.d/zh-hans
#添加
#zh_CN.GBK GBK
#zh_CN GB2312
#zh_CN.UTF-8 UTF-8
#生成本地字符集
sudo locale-gen
#再次查看本地支持的字符集
locale -a
```

<h4 id="8.3">8.3 Linux 终端上能执行任务，在crontab定时任务上执行失败</h4>

Linux系统上部署一个服务，该服务封装了一个Java程序。服务启动的时候首先创建一个进程，该进程的父进程ID为1，同时该进程会启动一个Java子进程。使用bash脚本在终端上可以重启该服务，但是在crontab任务上就执行失败。crontab mail日志没有报错，父进程会存活一下，然后就不见了。

排查了一段时候才发现，可能是crontab的环境中没有`JAVA`环境变量，将`JAVA`变量加入crontab中后，就能够启动成功了。

问题找到了就感觉很简单了，排查的过程中却是一头雾水，给的启示：

1. 有日志的时候，日志为王。以日志为突破口最能够找到问题。但是也要地方，程序可能会把日志给吃掉，没有日志但是存在异常，会要从其他角度分析排查问题。
2. 要将问题拆解、细化才可能找到问题的根源。例如本例中，一方面要知道crontab的运行机制（独立的运行环境和环境变量），另一方面要知道该服务实际上是封装了一个java服务，因此启动需要java环境。知道这两点问题就比较清晰了。
