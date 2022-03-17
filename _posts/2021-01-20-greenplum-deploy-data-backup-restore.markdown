---
layout: post
title: 'Greenplum的部署和数据备份恢复'
subtitle: '详细讲解Greenplum集群的部署及数据的备份和恢复'
background: '/img/posts/it-is-a-long-way.jpg'
comment: false
---

# 目录

- [1. Greenplum部署](#1)
    - [1.1 简介](#1.1)
        - [1.1.1 集群架构](#1.1.1)
    - [1.2 安装前准备](#1.2)
        - [1.2.1 硬件环境要求](#1.2.1)
        - [1.2.2 操作系统要求](#1.2.2)
    - [1.3 安装环境配置](#1.3)
        - [1.3.1 环境清单](#1.3.1)
        - [1.3.2 设置主机名修改hosts](#1.3.2)
        - [1.3.3 关闭防火墙](#1.3.3)
        - [1.3.4 关闭SELinux](#1.3.4)
        - [1.3.5 NTP时间同步（所有的服务器）](#1.3.5)
    - [1.4 系统环境配置](#1.4)
        - [1.4.1 系统内核参数优化配置（所有的服务器）](#1.4.1)
        - [1.4.2 修改Linux最大限制（所有的服务器）](#1.4.2)
        - [1.4.3 xfs文件系统（所有的服务器）](#1.4.3)
        - [1.4.4 设置磁盘预读扇区数（所有的服务器）](#1.4.4)
        - [1.4.5 磁盘I/O优化调整（所有的服务器）](#1.4.5)
    - [1.5 运行安装greenplum（Jq01运行）](#1.5)
        - [1.5.1 在Jq01上安装Greenplum二进制版本](#1.5.1)
        - [1.5.2 在所有机器上安装Greenplum二进制版本](#1.5.2)
        - [1.5.3 初始化Greenplum](#1.5.3)
        - [1.5.4 修改配置参数](#1.5.4)
    - [1.6 Greenplum部署注意事项](#1.6)
- [2. Greenplum数据备份恢复](#2)
- [3. Greenplum使用技巧](#3)
    - [3.1 sql查询替换特殊字符](#3.1)
    - [3.2 Greenplum提示too many clients解决](#3.2)
    - [3.3 Greenplum节点间的通信模式](#3.3)
    - [3.4 Greenplum节点network error segment slice1 6000 check your network](#3.4)
    - [3.5 Greenplum报错：insufficient memory reserved for statement](#3.5)
    - [3.6 Greenplum报错：Canceling query because of high VMEM usage. Used 2308M, available 819M](#3.6)
    - [3.7 Greenplum报错：failed to acquire resources on one or more segments](#3.7)
    - [3.8 记一次现场节点被重启GP库起不来问题](#3.8)
- [更新记录](#99)

---

<h3 id="1">1. Greenplum部署</h3>
<h4 id="1.1">1.1 简介</h4>
Greenplum的数据库是一个大规模并行处理（MPP）数据库服务器架构，该架构是专为管理大规模的数据仓库和商业智能设计的。由Pivotal公司开源，之前是商业的。
MPP（也称为shared nothing架构）是指系统具有两个或更多个处理机合作执行操作，每个处理机有它自己的内存，操作系统和磁盘。Greenplum使用这种高性能的系统架构来分布式加载multi-terabyte数据仓库，并且可以使用所有的系统资源并行处理查询。

Greenplum的数据库是基于PostgreSQL的开源技术，它本质上是几个PostgreSQL数据库实例一起协作当做一个有凝聚力数据库管理系统（DBMS）使用。GREENPLUM 数据库是基于PostgreSQL 8.3.23(版本不同少许差异)的，在SQL支持、产品特点、配置选项和终端用户功能方面，大多数情况下是与Postgres非常相似的。与Greenplum数据库的用户交互也类似一个普通的PostgreSQL数据库管理系统。

PostgreSQL的内部已被修改或补充，以支持Greenplum数据库的并行结构。例如，该系统目录，优化器，查询执行器，和事务管理器组件已被修改和改进，以支持能够在所有并行的PostgreSQL数据库实例同时执行查询。Greenplum Interconnect可使不同的PostgreSQL实例之间进行通信，并且使系统表现为一个逻辑数据库。

Greenplum数据库也有特有的特征，如优化的PostgreSQL商业智能功能（BI）与工作负载。另外，Greenplum增加了并行数据加载（外部表）、资源管理、查询优化、存储增强等功能，这些功能在标准PostgreSQL中都是没有的。由Greenplum开发的许多功能和优化也进入PostgreSQL的社区。例如，表分区是首先由Greenplum的开发了一种功能，它现在也在标准的PostgreSQL中了。

Greenplum数据存储和处理大数据量，是通过跨多个服务器和主机分配数据和负载。Greenplum数据库是基于PostgreSQL8.3单个数据库的阵列，这些数据库像单个数据库一样共同工作。master主机是Greenplum数据库系统的入口点，它是客户端连接并提交SQL语句的接口数据库实例。 Master协调系统中其他数据库实例（称为Segment）的工作负载，处理数据和存储。 分段通过互连，Greenplum数据库的网络层互相通信和主控。

<h5 id="1.1.1">1.1.1 集群架构</h5>
![Greenplum集群架构](/img/posts/Greenplum-architecture.jpg "Greenplum集群架构")

<h4 id="1.2">1.2 安装前准备</h4>
<h5 id="1.2.1">1.2.1 硬件环境要求</h5>
**计算可用磁盘容量**

要计算Greenplum数据库系统可以容纳多少数据，您必须计算每个Segment主机的可用磁盘容量，然后将其乘以您的Greenplum数据库数组中的Segment主机数。从可用于数据存储（raw_capacity）的段主机上的物理磁盘的原始容量开始，它是：disk_size 乘以 number_of_disks。

对于记录文件系统格式化开销（大约10％）和您使用的RAID级别。例如，如果使用RAID-10，计算将是：（raw_capacity(数据存储空间) 乘以 0.9）/ 2 = formatted_disk_space(初始化空间大小)

为了获得最佳性能，Pivotal建议您不要将磁盘完全填满容量，而是运行在70％以下。所以考虑到这一点，计算可用的磁盘空间如下：formatted_disk_space(初始化空间大小) 乘以 0.7 = useful_disk_space(可用空间大小)

一旦占用了最大推荐容量（useful_disk_space），则需要计算实际可用于用户数据的存储空间。如果使用Greenplum数据库镜像进行数据冗余，则会使用户数据的大小增加一倍。Greenplum数据库还需要保留一些空间作为活动查询的工作区域，工作空间应大约用户数据大小的三分之一。

所以：假设我们有2块1t的硬盘做了raid0（或没做raid），计算将是 raw_capacity(数据存储空间) 2t 乘以 0.9 乘以 0.7= useful_disk_space(可用空间大小)=1.2t，在部署了镜像节点的情况下，那我们的useful_disk_space(可用空间大小)=600G。

**预估数据量的大小**

与所有数据库一样，原始数据的大小一旦加载到数据库中将略大（列存压缩除外）。平均来说，原始数据在加载到数据库之后将大约是磁盘的1.4倍，但根据你使用的数据类型，表存储类型，数据库内压缩等，可能会更小或更大。

页面开销 – 当您的数据加载到Greenplum数据库时，它分为32KB的页面，每页有20字节的页面开销。

行开销 – 在常规“堆”存储表中，每行数据具有24个字节的行开销，“append-optimized”存储表只有4字节的行开销。

属性开销 – 对于数据值本身，与每个属性值相关联的大小取决于所选择的数据类型。通常，您希望使用尽可能小的数据类型来存储数据（假设您知道列可能有的值）。

索引 – 在Greenplum数据库中，索引通过表数据分布在Segment主机上。Greenplum数据库中的默认索引类型是B-tree，因为索引大小取决于索引中唯一值的数量和要插入的数据，因此预先计算索引的确切大小是不可能的。但是，您可以使用这些公式粗略地估计索引的大小。

> Btree：unique_values 乘以（data_type_size + 24bytes）
Bitmap：（unique_values 乘以 number_of_rows 乘以 1 bit 乘以 compression_ratio / 8）+（unique_values 乘以 32）
注：元数据和日志的空间需求可忽略不计

**硬件环境的要求**

Gp提供集群服务来保证服务的高可用性，因此集群系统必须是3台及以上物理服务器组成，每台服务器必须具备以下最低配置：

- 2颗6核心或以上带超线程x86指令集CPU的服务器
- 32GB及以上内存(在主节点配置segment的情况下，主节点的内存应最低64GB)
- 2个1T的硬盘做RAID0，作为数据盘（可用也只有600G）（根据2.1.1和2.1.2适当配置大小）
- 2个千兆及以上网卡

> 注：推荐在主节点配置segment以免资源浪费因为Master只负责生成和优化查询计划、派发任务、协调数据节点进行并行计算，segment的部署数量应当为cpu核心数/2但不要超过8个（可以为8个），所以根据上面的最低需求3台物理机每台2个6核cpu32GB内存2t数据盘2个千兆网卡得出：每台部署6个segment，做18segment集群。
（如是生产环境这里的具体配置可连接开发人员协助评估）

<h5 id="1.2.2">1.2.2 操作系统要求</h5>
- Centos6.5
- Centos7.x (建议用7.3以及7.3以上版本

<h4 id="1.3">1.3 安装环境配置</h4>
<h5 id="1.3.1">1.3.1 环境清单</h5>
| 角色                   | 主机名 | 地址    |
| ---------------------- | ------ | ------- |
| master,segment,mirror  | jq02   | 1.1.1.1 |
| standby,segment,mirror | jq02   | 1.1.1.2 |
| segment,mirror         | jq03   | 1.1.1.3 |

> 以最低配置为例（3台物理机每台2个6核cpu32GB内存2t数据盘2个千兆网卡）

<h5 id="1.3.2">1.3.2 设置主机名修改hosts</h5>
Jq01：`echo "hostname Jq01" >> /etc/rc.local`

Jq02：`echo "hostname Jq02" >> /etc/rc.local`

Jq03：`echo "hostname Jq03" >> /etc/rc.local`

> 注：重启生效

Centos7也可用下面的命令修改，不用重启

`hostnamectl set-hostname 修改的主机名`

如：`hostnamectl set-hostname jq01`

Jq01，Jq02，Jq03（**所有的服务器**）：

```shell
vi /etc/hosts

# 添加
1.1.1.1 Jq01
1.1.1.2 Jq02
1.1.1.3 Jq03

```

> **注意：配置机器如果存在私网地址和公网地址，这里要设置私网地址。**

<h5 id="1.3.3">1.3.3 关闭防火墙</h5>

```shell
Centos7.x：
systemctl stop firewalld    --停止防火墙
systemctl disable firewalld    --禁用防火墙
Centos6.5：
service iptables stop    --停止防火墙
/sbin/chkconfig iptables off    --禁用防火墙
```

<h5 id="1.3.4">1.3.4 关闭SELinux</h5>
```shell
setenforce 0    --临时关闭SELinux
sed -i 's/SELINUX=enforcing/SELINUX=disabled/g' /etc/selinux/config   --永久关闭SELinux
getenforce    --获取SELinux状态
Centos6.5返回Disabled说明关闭成功
Centos7.X返回Permissive说明关闭成功
```

<h5 id="1.3.5">1.3.5 NTP时间同步（所有的服务器）</h5>
```shell
在Greenplum 所有节点安装ntp服务器，然后Jq02节点和Jq03节点都同步Jq01节点的时间。
yum install ntp   --所有节点执行安装命令
systemctl start ntpd    --所有节点执行启动服务
systemctl enable ntpd    --所有节点执行启用ntpd
然后在Jq02节点和Jq03节点执行：
ntpdate Jq01
#注：Greenplum需要各个节点的时间都是一致的，切记
```

<h4 id="1.4">1.4 系统环境配置</h4>
<h5 id="1.4.1">1.4.1 系统内核参数优化配置（所有的服务器）</h5>


```shell
追加到文件/etc/sysctl.conf，使用sysctl -p命令即时生效。
注意：如果安装gp的服务器装了petabase数据库，此参数不要更新，因为petabase所需的配置要比gp配置高，用原有参数即可。
#gp配置
kernel.shmmax = 50000000000
kernel.shmmni = 4096
kernel.shmall = 4000000000
kernel.sem = 250 512000 100 2048
kernel.sysrq = 1
kernel.core_uses_pid = 1
kernel.msgmnb = 65536
kernel.msgmax = 65536
kernel.msgmni = 2048
net.ipv4.tcp_syncookies = 1
net.ipv4.ip_forward = 0
net.ipv4.conf.default.accept_source_route = 0
net.ipv4.tcp_tw_recycle = 1
net.ipv4.tcp_max_syn_backlog = 4096
net.ipv4.conf.all.arp_filter = 1
net.ipv4.ip_local_port_range = 1025 65535
net.core.netdev_max_backlog = 10000
net.core.rmem_max = 2097152
net.core.wmem_max = 2097152
vm.overcommit_memory = 1
```

<h5 id="1.4.2">1.4.2 修改Linux最大限制（所有的服务器）</h5>

```shell
#下面内容追加到文件/etc/security/limits.conf即可
#gp配置
* soft nofile 65536
* hard nofile 65536
* soft nproc 131072 
* hard nproc 131072

Centos6.5：  修改/etc/security/limits.d/90-nproc.conf
将   *   soft    nproc  2048  改为  *   soft    nproc   131072
Centos7.x：  修改/etc/security/limits.d/20-nproc.conf
将   *   soft    nproc  2048  改为  *   soft    nproc   131072
```

<h5 id="1.4.3">1.4.3 xfs文件系统（所有的服务器）</h5>

```shell
GP建议使用XFS文件系统，CentOS 7默认就是XFS文件系统。如果你是CentOS 6系统，默认是EXT4，需要额外安装XFS文件系统。
所有节点单独挂载两块磁盘做raid0（或一块），我这里假定为sdb。
mkfs.xfs /dev/sdb
然后在/etc/fstab文件中追加挂载命令：
/dev/sdb /data xfs nodev,noatime,inode64,allocsize=16m 0 0
执行mount -a挂载即可。
```

<h5 id="1.4.4">1.4.4 设置磁盘预读扇区数（所有的服务器）</h5>

```shell
对于预读扇区数，官方文档的推荐值为16384，但应该为65536更合理。实际上预读的字节数是blockdev设置除以2，而GP缺省的blocksize为32KB，刚好与65536(32768B/32KB)对应。
检查某块磁盘的read-ahead设置：
blockdev --getra /dev/sdb
临时修改系统的read-ahead设置：
blockdev --setra 65536 /dev/sdb
要想永久生效，还是需要将修改命令追加到/etc/rc.local尾部
```

<h5 id="1.4.5">1.4.5 磁盘I/O优化调整（所有的服务器）</h5>

```shell
Linux磁盘I/O调度器对磁盘的访问支持不同的策略，默认的为CFQ，GP建议设置为deadline。
假定数据盘为sdb如下设置：
echo deadline > /sys/block/sdb/queue/scheduler
要想永久生效，还是需要将修改命令追加到/etc/rc.local尾部。
注：都配置完毕后，重启生效即可。
```

<h4 id="1.5">1.5 运行安装greenplum（Jq01运行）</h4>
<h5 id="1.5.1">1.5.1 在Jq01上安装Greenplum二进制版本</h5>

```shell
首先上传greenplum-db-5.10.2-rhel7-x86_64.bin文件到/data
cd /data
然后执行：
/bin/bash greenplum-db-5.10.2-rhel7-x86_64.bin
长按回车大概23s直到出现让输入yes或no
输入是否接受选项：
输入yes
输入安装路径选项：
输入/data/gp510
输入确认安装路径选项：
然后输入yes
输入确认创建目录选项：
再输入yes

安装完成后/data/gp510下的相关文件如下：
greenplum_path.sh：此文件包含Greenplum数据库的环境变量。
bin：此目录包含Greenplum数据库管理程序，此目录还包含PostgreSQL客户端和服务器程序，其中大部分也用于Greenplum数据库。
sbin：支持/内部脚本和程序。
docs：Greenplum数据库文档（PDF文件）。
etc：OpenSSL的示例配置文件。
ext：一些Greenplum数据库实用程序使用的捆绑程序（如Python）。
include：Greenplum数据库的C语言头文件。
lib：Greenplum数据库和PostgreSQL库文件。
share：Greenplum数据库的共享文件。
```

<h5 id="1.5.2">1.5.2 在所有机器上安装Greenplum二进制版本</h5>

```shell
#设置环境变量
source /data/gp510/greenplum_path.sh

#创建all_hosts和all_segments文件
cd /data/gp510
vi all_hosts
添加：
Jq01
Jq02
Jq03
cp all_hosts all_segments

#如果主节点机器不装数据节点，只装master主节点（建表等文件，没有数据文件），则：
vi all_segments
添加：
Jq02
Jq03

#配置免密登录
cd /data/gp510
gpssh-exkeys -f all_hosts

#运行gpinstall
cd /data/gp510
gpseginstall -f all_hosts -u gpadmin -p 123456
#该命令就是将Greenplum安装目录scp到所有机器，创建gpadmin用户（设置密码为-p指定的123456），并将安装目录所有权更改为gpadmin

#检察分发结果
cd /data/gp510
gpssh -f all_hosts -e ls -l $GPHOME
#输出结果包含这3个服务器上相同目录下有相同的文件则成功。

#创建数据目录并授权
#注意：这里创建的目录是要存储数据文件的，需在之前规划好的存储目录上
#此步骤在Jq01上执行：
#创建master节点目录，和权限
mkdir -p /data/gp510/gpdata/master
chown gpadmin:gpadmin -R /data/gp510/gpdata 
将Jq02机器作为stand by master的机器（master的镜像），创建目录和权限
gpssh -h Jq02 -e 'mkdir -p /data/gp510/gpdata/master'
gpssh -h Jq02 -e 'chown gpadmin:gpadmin -R /data/gp510/gpdata'
给所有安装数据节点的机器，创建primary和mirror镜像目录和权限
gpssh -f /data/gp510/all_host -e 'mkdir -p /data/gp510/gpdata/primary'
gpssh -f /data/gp510/all_host -e 'mkdir -p /data/gp510/gpdata/mirror'
gpssh -f /data/gp510/all_host -e 'chown gpadmin.gpadmin -R /data/gp510/gpdata/'
```

<h5 id="1.5.3">1.5.3 初始化Greenplum</h5>

```shell
#创建Greenplum初始化文件
su - gpadmin
mkdir -p /data/gp510/gpconfig
cp /data/gp510/docs/cli_help/gpconfigs/gpinitsystem_config /data/gp510/gpconfig
chmod 755 /data/gp510/gpconfig/gpinitsystem_config

#编辑Greenplum初始化文件
#参数解释
RRAY_NAME：设置集群名称，默认即可。
SEG_PREFIX：设置segment的前缀，默认gpseg即可。
PORT_BASE：设置segment的起始端口，会从此端口往上增加，默认从40000开始，一般默认即可。
DATA_DIRECTORY：设置segment primary的数据存储目录，有几个segment节点就需要设置几个数据存储目录（根据前文可知要部署6个segment节点，数据目录为 /data/gp510/gpdata/primary，所以要将数据目录填写6遍）。
MASTER_HOSTNAME：设置master的主机名，Jq01。
MASTER_DIRECTORY：设置master的存储目录，/data/gp510/gpdata/master。
MASTER_PORT：设置master的端口，默认5432。
TRUSTED_SHELL：设置节点之间的信任方式，默认SSH，默认即可。
CHECK_POINT_SEGMENTS：预写日志文件（WAL）数量，默认为8，这意味着为主机上的每个Segment或Master实例分配1088MB的WAL空间，默认即可。
ENCODING=UNICODE：设置初始字符集，默认UNICODE（UTF-8），默认即可。
MIRROR_PORT_BASE：指定第一个mirror segment的端口号，其它mirror segment在此基础上加1，它的设置范围是1到65535，但是设置时要注意不能与primary segment冲突，默认50000即可。
REPLICATION_PORT_BASE：指定第一个primary segment用于数据复制的端口号，其它primary segment以此为基础，每次加1,计算复制用端口号。它的设置也不要与primary segment冲突。取值范围1到65535，默认41000即可。
MIRROR_REPLICATION_PORT_BASE：指定第一个mirror segment用于数据复制的端口号，其它mirror segment以此为基础，每次加1,计算复制用端口号。它的设置也不要与mirror segment冲突。取值范围1到65535，默认51000即可。
MIRROR_DATA_DIRECTORY：指定了mirror segment的数据目录，声明的目录数量必须与primary segment的目录数量一致，而且要保证进行初始化操作的os用户对这些目录有读写权限（根据前文可知要部署6个segment节点，所以mirror也是6个，数据目录为 /data/gp510/gpdata/mirror，所以要将数据目录填写6遍）。
注：注意，其中所有需要的目录都是在创建数据存储区域时做好的。

#初始化数据库(用gpadmin用户执行)
gpinitsystem -c /data/gp510/gpconfig/gpinitsystem_config -h /data/gp510/all_segments -s Jq02
gpinitsystem命令参数解释：
-c：指定初始化文件。
-h：指定segment主机文件。
-s：指定standby主机，创建standby节点。
中间需要输入几次y，如果不报ERROR，GP数据库就安装好了并且各个节点都启动了。安装过程中一定要注意相关执行的方式和权限，不然可能会失败

#如何使用回退脚本
如果gpinitsystem实用程序失败，如果系统处于部分安装状态，它将创建以下备用脚本：
/ gpAdminLogs / backout_gpinitsystem_ <user> _ <timestamp>
您可以使用此脚本清理部分创建的Greenplum数据库系统。此回退脚本将删除任何实用程序创建的数据目录，postgres进程和日志文件。纠正导致gpinitsystem失败并运行退出脚本的错误后，您应该准备重试初始化您的Greenplum数据库数组。
以下示例显示如何运行回退脚本：
sh backout_gpinitsystem_gpadmin_***_***

#设置环境变量
在gpadmin 用户下执行
vi ~/.bash_profile
添加如下内容（其中的路径根据实际部署情况修改）
source /data/gp510/greenplum_path.sh
export MASTER_DATA_DIRECTORY=/data/gp510/gpdata/master/gpseg-1
export PGPORT=5432
export PGUSER=gpadmin
生效并拷贝到Standby
source ~/.bash_profile
scp ~/.bash_profile gpadmin@Jq02:~/.bash_profile

#修改访问权限
修改master数据目录下的pg_hba.conf访问权限
cd /data/gp510/gpdata/master/gpseg-1
vi pg_hba.conf
增加两行  (全部小写)
host   template1     all    0.0.0.0/0    reject
host   all    all   0.0.0.0/0  md5  

然后重新加载配置文件
gpstop -u

#数据库启动和关闭
在gpadmin用户下
gpstart
gpstop –M fast

#数据库修改gpadmin用户密码
在gpadmin用户下
psql -d postgres
在postgres的命令行，修改gpadmin用户密码为123456
alter user gpadmin with password ‘123456’
```

<h5 id="1.5.4">1.5.4 修改配置参数</h5>

```shell
在GP库master节点服务器上，切换到GP库的操作系统用户（如 gpadmin），执行如下命令

shared_buffers 默认值：125MB  建议值：1GB  
查看参数配置值：
gpconfig -s shared_buffers 
修改参数配置值
gpconfig -c shared_buffers -v 1GB

work_mem  默认值：32 MB  建议值：512MB
查看参数配置值：
gpconfig -s work_mem  
修改参数配置值
gpconfig -c work_mem  -v 512MB

statement_mem 默认值：125 MB  建议值：512MB
查看参数配置值：
gpconfig -s statement_mem 
修改参数配置值
gpconfig -c statement_mem -v 512MB

effective_cache_size  默认值：512 MB  建议值：8GB
查看参数配置值：
gpconfig -s effective_cache_size  
修改参数配置值
gpconfig -c effective_cache_size  -v 8GB


max_prepared_transactions 默认值：250  建议值：500
查看参数配置值：
gpconfig -s max_prepared_transactions 
修改参数配置值
gpconfig -c max_prepared_transactions -v 500


maintenance_work_mem 默认值：64MB  建议值：256MB
查看参数配置值：
gpconfig -s maintenance_work_mem 
修改参数配置值
gpconfig -c maintenance_work_mem -v 256MB


gp_vmem_protect_limit  建议值：10240
查看参数配置值：
gpconfig -s gp_vmem_protect_limit 
修改参数配置值
gpconfig -c gp_vmem_protect_limit -v 10240

max_connections 默认值：250  建议值：500
查看参数配置值：
gpconfig -s max_connections 
修改参数配置值
gpconfig -c max_connections -v 2500 -m 500


修改参数后需要重启GP库，使参数生效
关闭GP库
gpstop -M fast 
启动GP库
gpstart -a
```

<h4 id="1.6">1.6 Greenplum部署注意事项</h4>

最小节点规划：

```shell 
gpmaster master
gpslave1 standby segment
gpslave2 segment
gpslave3 segment
```

规划对应的初始化节点配置：

```shell
# FILE NAME: gpinitsystem_config

# Configuration file needed by the gpinitsystem

################################################
#### REQUIRED PARAMETERS
################################################

#### Name of this Greenplum system enclosed in quotes.
ARRAY_NAME="Greenplum Data Platform"

#### Naming convention for utility-generated data directories.
SEG_PREFIX=gpseg

#### Base number by which primary segment port numbers 
#### are calculated.
PORT_BASE=40000

#### File system location(s) where primary segment data directories 
#### will be created. The number of locations in the list dictate
#### the number of primary segments that will get created per
#### physical host (if multiple addresses for a host are listed in 
#### the hostfile, the number of segments will be spread evenly across
#### the specified interface addresses).
declare -a DATA_DIRECTORY=(/data/gpgdgf/gpdata/primary /data/gpgdgf/gpdata/primary)

#### OS-configured hostname or IP address of the master host.
MASTER_HOSTNAME=gpmaster

#### File system location where the master data directory 
#### will be created.
MASTER_DIRECTORY=/data/gpgdgf/gpdata/master

#### Port number for the master instance. 
MASTER_PORT=5432

#### Shell utility used to connect to remote hosts.
TRUSTED_SHELL=ssh

#### Maximum log file segments between automatic WAL checkpoints.
CHECK_POINT_SEGMENTS=8

#### Default server-side character set encoding.
ENCODING=UNICODE

################################################
#### OPTIONAL MIRROR PARAMETERS
################################################

#### Base number by which mirror segment port numbers 
#### are calculated.
MIRROR_PORT_BASE=50000

#### Base number by which primary file replication port 
#### numbers are calculated.
REPLICATION_PORT_BASE=41000

#### Base number by which mirror file replication port 
#### numbers are calculated. 
MIRROR_REPLICATION_PORT_BASE=51000

#### File system location(s) where mirror segment data directories 
#### will be created. The number of mirror locations must equal the
#### number of primary locations as specified in the 
#### DATA_DIRECTORY parameter.
declare -a MIRROR_DATA_DIRECTORY=(/data/gpgdgf/gpdata/mirror /data/gpgdgf/gpdata/mirror)


################################################
#### OTHER OPTIONAL PARAMETERS
################################################

#### Create a database of this name after initialization.
#DATABASE_NAME=name_of_database

#### Specify the location of the host address file here instead of
#### with the the -h option of gpinitsystem.
#MACHINE_LIST_FILE=/home/gpadmin/gpconfigs/hostfile_gpinitsystem
```

standby在初始化的时候失败，可能服务器是最小安装，没有net-tools依赖包

如果需要重新初始化，删除对应节点上目录里面的内容，目录留下，注意权限

日志文件再gpadmin的家目录中，再对应节点的pg_log中又更详细的日志

---

<h3 id="2">2. Greenplum数据备份恢复</h3>

```shell
#备份
gpbackup --dbname db_hybzk --single-data-file --backup-dir /data/thunisoft/gpbackup/

#恢复
gprestore --backup-dir /data/gpgdgf/backup/ --timestamp 20201214175326 --redirect-db db_hybzk_20201130
```

<h3 id="3">3. Greenplum使用技巧</h3>

<h4 id="3.1">3.1 sql查询替换特殊字符</h4>

数据采集的时候sql输出csv文件，用`$`分隔开，然后用`copy`命令导入数据。当数据中存在换行符(line feed)，回车符(carriage return)，或者斜杠(forward slash)、反斜杠(backslash)的时候，导出的数据再进行导入就会存在问题。比如换行符就会造成导出的一条数据分为多行，导入的时候被分成多行的数据导入就会存在问题。这时候就需要替换掉上述的特殊字符，在Greenplum中具体的替换方法为：

```
replace(replace(replace(column, chr(10), ''), chr(13), ''), chr(92), '')

#其中chr(10)、chr(13)、chr(92)分别代表换行符、回车符和斜杠。用ASCII码值分别代表不同的字符。
```

<h4 id="3.2">3.2 Greenplum提示too many clients解决</h4>

1. 看是否用Navicat客户端是否能连接到目标GP库
2. 操作1不能成功，提示太多client，需要在GP库后台手动杀死一个或者多个进程
  1. `ssh连接GP库主节点服务器`
  2. `ps -ef |grep db_jcdsj`
  3. `kill -9 ${PID}`
3. 重试操作1
4. 在目标数据库中执行sql，杀死连接：`select pg_terminate_backend(procpid) from pg_stat_activity where current_query='<IDLE>'`
5. 重试操作1，验证是否成功

> 杀死数据库连接的方法`pg_terminate_backend(procpid)`字段名`procpid`也可能是`pid`，前者对应低版本的`PostgreSQL(8.3.23)`，后者对应高版本的`PostgreSQL(9.4.24)`。

> 用`select version()`可以查看`PostgreSQL`和`Greenplum`的数据库版本。

<h4 id="3.3">3.3 Greenplum节点间的通信模式</h4>

默认GP库节点间通信的模式是：`UDP`，可以通过命令修改为TCP：

```shell
gpconfig -s gp_interconnect_type -v TCP
```

> 实际上在master节点上执行配置修改会在各个主子节点生效，分别会在`数据目录/postgresql.conf`增加一行配置，上面的命令会增加一行：`gp_interconnect_type=TCP`。

使用TCP传输执行查询速度会非常慢，在`UDP`通信情况下`10s`执行完毕的sql，在`TCP`通信情况下执行要超1个小时。从`TCP`改为`UDP`使用命令：

```shell
gpconfig -s gp_interconnect_type -v udpifc
```

> 不要使用`gpconfig -s gp_interconnect_type -v UDP`，这个`UDP`配置在Greenplum-6.7.1（对应PostgreSQL 9.4.24）的版本中不支持。如果不小心配置了不要重启（重新执行正确配置值的命令），重启就起不来了，只能在服务端`postgresql.conf`文件中一个个配置。

<h4 id="3.4">3.4 Greenplum节点network error segment slice1 6000 check your network</h4>

在`/etc/sysctl.conf`中有配置：`net.ipv4.ip_local_port_range`，该配置目前的理解是在本机可开启哪些端口供使用。因为现场环境有防火墙的限制，所以要配置的端口范围是防火墙放行的。

> 使用`sysctl -p`命令可以让`/etc/sysctl.conf`文件即刻生效。

~~GP库主子节点通信使用的端口还要再研究下，修改完防火墙放行的端口后再验证试一试。~~现场环境中将端口号改为不会有防火墙限制的`10000-20000`之后，没有出现报错：`network error`。

<h4 id="3.5">3.5 Greenplum报错：insufficient memory reserved for statement</h4>

默认`statement_mem`值为`128MB`，可以使用使用`show statement_mem`或者`select * from pg_settings where name like 'statement';`。

修改参数：

1). 使用Greenplum命令

```shell
statement_mem 默认值：125 MB  建议值：512MB
查看参数配置值：
gpconfig -s statement_mem 
修改参数配置值
gpconfig -c statement_mem -v 512MB
```

2). 通过`alter system`命令：`alter system set statement_mem='256MB'`

> 先通过`alter system`设置`128MB`之后，在通过`gpconfig -c`设置的`512MB`没有生效，后续有机会再研究。

> 参考文章：[https://community.pivotal.io/s/article/Greenplum-Queries-Fail-with-ERROR-insufficient-memory-reserved-for-statement?language=en_US](https://community.pivotal.io/s/article/Greenplum-Queries-Fail-with-ERROR-insufficient-memory-reserved-for-statement?language=en_US)

<h4 id="3.6">3.6 Greenplum报错：Canceling query because of high VMEM usage. Used 2308M, available 819M</h4>

该报错表示`segment`占用的内存超过了`gp_vmem_protect_limit`的一定比例（比如95%），这个比例在`/etc/sysctl.conf`配置`vm.overcommit_ratio`中配置。

上面的参数`statement_mem`代表每一个`statement`的最大使用内存，在一个节点的`segment`上，可能并发运行多个`statement`，这时候`segment`占用的内存就可能超过`gp_vmem_protect_limit`设置值。这时候执行的语句就会被取消。

这个时候，一种办法是`statement_mem`小一点，可以有更多的并发量，但是会慢一点；另一种办法是增加节点的RAM（内存），这样就可以设置更高的`gp_vmem_protect_limit`。

```shell
#建议值10240，需要重启GP库
gpconfig -c gp_vmem_protect_limit -v 10240
```

> 查考文章：
- [https://community.pivotal.io/s/article/Query-Failing-with-ERROR-Canceling-query-because-of-high-VMEM-usage?language=en_US](https://community.pivotal.io/s/article/Query-Failing-with-ERROR-Canceling-query-because-of-high-VMEM-usage?language=en_US)
- [https://stackoverflow.com/questions/61747639/greenplum-error-canceling-query-because-of-high-vmem-usage](https://stackoverflow.com/questions/61747639/greenplum-error-canceling-query-because-of-high-vmem-usage)
- [https://greenplum.org/calc/](https://greenplum.org/calc/)

<h4 id="3.7">3.7 Greenplum报错：failed to acquire resources on one or more segments</h4>

生产环境报错`failed to acquire resources on one or more segments`，也就是节点的资源不够，服务端取消了执行中的`sql`语句。

应用日志没有给出太多的信息，根据下面链接的官方回帖，报这个错，可能有多种情况：

- `$GPHOME`的权限不对（比如，可能被修改为root），正常`$GPHOME`的属主是`gpadmin`。如果权限错了，可以通过`chown -R gpadmin:gpadmin <$GPHOME>`修改目录权限。
- 数据库的`max_connections`和`max_prepared_transactions`设置的不合理（过小），这样当新的`connection`需要时，就会出现`resources`不足。具体配置增大`max_connections`后面详述。
- 磁盘空间占满了。
- `segment`日志上报的其他资源限制。

本示例情况暂时也看不出来，通过查看`master`节点的日志有报错：`failed to acquire resources on one or more segments. Fatal: remaining connection slots are reserved for non-replication superuser connections`

通过`select * form pg_stat_activity`查看`master`节点已有的连接是220个左右，`master`节点允许的`connections`是250。这里的报错含义可能是：虽然没达到最大的250，但是有一部分是预留出来的， 重复的`session`已经不能获取连接了（同一个事务可能用到多个connections）。试图增大`max_connections`：

```shell
#查看目前GP库max_connections
#master节点和segment节点是分开的
#本示例master: 250, segment: 750
gpconfig -s max_connections

#查看目前GP库max_prepared_transactions
#该值和max_connections的master节点值是一致的
#本示例master、segment均为250
gpconfig -s max_prepared_transactions

#重设max_connections值
#master: 500, segment: 1000
gpconfig -c max_connections -v 1000 -m 500

#重设max_prepared_transactions值
#master: 500, segment: 500
gpconfig -c max_prepared_transactions -v 100
```

> 这里要注意：
1. 当增大max_connections的master节点值时，一定要同步增大max_prepared_transactions的值，二者要保持一致，否则命令行设置完参数，GP库可能就起不来了。这时候要一个个节点修改`postgresql.conf`文件中写入的参数（`gpconfig`设置的参数实际上会写入`postgresql.conf`配置文件中），很麻烦。
2. `gpconfig`命令中`-v`表示对`segment`设置，`-m`表示对`master`设置。当没有`-m`指定时，`master`节点的值设置为`-v`指定的值。

修改参数后，重启GP库，参数可以生效。

> 参考文章：
- [https://community.pivotal.io/s/article/greenplum-error-failed-to-acquire-resources-on-one-or-more-segments?language=en_US](https://community.pivotal.io/s/article/greenplum-error-failed-to-acquire-resources-on-one-or-more-segments?language=en_US)

<h4 id="3.8">3.8 记一次现场节点被重启GP库起不来问题</h4>

问题描述：现场GP5，1master + 3salve(12segments)，其中gpslave3在GP库启动的情况下关机，造成GP库整个起不来。

排查过程；

GP库启动情况下，某个slave关停后，会造成该slave上对应的segments停掉(state为`down`)。

总体思路是，第一步：先将state为`up`的segments起来；第二步：尝试用`gprecovery`将state为`down`的segments恢复。这样就完成所有节点的启动。

在进行第一步之前，要登录到各个节点，使用`ps -ef |grep /postgres`查看是否有启动着的进程，如果有，用`kill -9 $PID`命令杀死进程。同时，要通过命令`ls -la /tmp |grep gpadmin`查看是否有`gpadmin`用户下的`.s.PGSQL.*`临时文件，如果有，要手动删除。需要注意的是，有时候虚拟机重启后`/tmp`目录的权限会从`777`变为`750`，这样`gpadmin`就没有写入`/tmp`的权限，这时候要通过命令`chmod 777 /tmp`将`/tmp`目录赋权`777`。

接下来就在`master`节点使用`gpstart`命令启动。如果有报错信息，就要去报错提示对应的segment节点数据目录下找到`pg_log`目录，查看相关日志，日志中会记录起不来的原因。例如路径：`/data/gpgdgf/gpdata/primary/gpseg0/pg_log/*`。这一步如果成功，就进行接下来的恢复节点。

> 如果所有节点一起实在起不来，可以通过命令`gpstart -m`单独启动`master`节点，使用`PGOPTIONS="-c gp_session_role=utility" psql postgres`连接到master节点中，用`sql`查看节点的相关信息。

连接上数据库后，通过sql语句`select * from gp_segment_configuration order by dbid;`查看各个节点的信息：

- `status`列代表segment的状态，是`up`或者`down`。
- `mode`列代表状态，`s(synchronized)`表示对应`primary`和`mirror`节点数据是同步的，是正常状态、`c(change tracking)`表示因为某种原因`primary`和`mirror`节点数据没有同步，是异常状态、`r(resynchronization)`表示`primary`和`mirror`节点数据在同步中，是过程状态。
- `content`列同一个值代表一对`primary`和`mirror`。

通过命令恢复状态为`down`的segment：

```shell
#会在当前目录生成一个recov文件，里面包含了要恢复的节点信息
1. gprecoverseg -o ./recov 

#该命令执行为异步过程，可以通过gpstate -s查看执行的进度
2. gprecoverseg -i ./recov

#通过sql查询所有的status为'u'，所有的`mode`为's'表示所有segment正常
3.select * from gp_segment_configuration order by dbid;
```

---

<h3 id="99">更新记录</h3>

- 2022-01-10 10:27 现场发现GP库报错报错，新增`3.5`内容。
- 2022-01-14 09:56 现场发现Greenplum库`too many clients`，补充“3.2”部分内容。
- 2022-02-28 18:30 现场报错补充“high VMEM usage”，补充“3.6”部分内容。
- 2022-03-02 17:33 现场报错补充“failed to acquire resources on one or more segments”，补充“3.7”部分内容。
- 2022-03-17 20:03 现场排查Greenplum slave节点主机重启后GP库宕机问题，补充“3.8”部分内容。
