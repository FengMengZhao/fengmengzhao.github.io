---
layout: post
title: '学习达梦数据库'
subtitle: '达梦数据库学习、使用中的备忘。'
background: '/img/posts/dm-database-learn.jpg'
comment: false
---

# 目录

- [1. 用户创建及权限相关](#1)
    - [1.1 用户创建并赋予权限](#1.1)
    - [1.2 查看用户及权限](#1.2)
    - [1.3 用户、角色、资源权限理解](#1.3)
- [2. 用户密码相关](#2)
    - [2.1 密码修改](#2.1)
    - [2.2 密码策略修改](#2.2)
- [3. 修改用户资源限制](#3)
- [4. 审计相关](#4)
- [5. 版本和license](#5)
- [6. 问题记录](#6)
    - [6.1 达梦数据库获取一个表所有字段的拼接串](#6.1)
    - [6.2 达梦数据迁移整个数据目录并重新启动数据库](#6.2)

---

<h3 id="1">1. 用户创建及权限相关</h3>

<h4 id="1.1">1.1 用户创建并赋予权限</h4>

```shell
#创建新的用户（禁用超级管理员账号）
create user <用户名> identified by <PWD>
#分配权限
grant resource,public,vti to <用户名>;
#收回权限
revoke "RESOURCE" from <用户名>
```

<h4 id="1.2">1.2 查看用户及权限</h4>

```shell
#查看用户
select * form dba_users;

#DBA 拥有构建数据库的全部特权，只有 DBA 才可以创建数据库结构 
#RESOURCE 可以创建和删除角色 
#PUBLIC 只能查询相关的数据字典表 
#VTI 具有系统动态视图的查询权限，VTI 默认授权给 DBA 且可转授 
#SOI 具有系统表的查询权限 
select * from session_roles # 查看用户权限
```

<h4 id="1.3">1.3 用户、角色、资源权限理解</h4>

达梦数据库的权限分为两类：`数据库权限`（系统权限）和`对象权限`。实际上这二者的划分是根据对数据库对象的操作不同而区别的，对数据库对象的增(`create`)、删(`drop`)、改(`alter`)和备份(`backup`)的权限称之为`数据库权限`（系统权限），而对数据库对象的访问权限称之为`对象权限`。

> 注意，这里的“对数据库对象的访问”不仅仅指代查询，而是对数据库对象内容的“增删改查”等。例如，对于表和视图数据库对象来说，所谓的`对象权限`包括：`SELECT`、`INSERT`、`DELETE`、`UPDATE`、`REFERENCES`和`SELECT FOR DUMP`。

达梦数据库每创建一个用户就默认创建一个和该用户同名的模式，默认情况下，该用户对自己默认同名模式内对象资源有`对象权限`，而`数据库权限`为空。也就是说，默认普通用户只能对自己模式内对象数据进行增删改查等，不能进行新建表、删除表、新建视图等操作。

要想让普通用户具有`数据库权限`需显式授权，可以使用`SYSDBA`或者有授权权限的用户对普通用户授权`数据库权限`。比如可以授予普通用户`create schema`权限，这样普通的用户就能够创建`schema`，但是`shema`的归属（或者说授权）只能给自己。

> 授权的方式可以使用`grant`语句或者使用达梦`manager`客户端图形化操作。

达梦数据库内置了5个重要角色，一个普通用户默认的角色是`PUBLIC`和`SOI`。实际上为了让普通用户能够在所属的`schema`下具有部分`数据库权限`，可以再赋予`VTI`和`RESOURCE`角色。这些角色的权限如下：

- `DBA`：DM 数据库系统中对象与数据操作的最高权限集合，拥有构建数据库的全部特权，只有 DBA 才可以创建数据库结构。
- `RESOURCE`：可以创建数据库对象，对有权限的数据库对象进行数据操纵，不可以创建数据库结构。
- `PUBLIC`：不可以创建数据库对象，只能对有权限的数据库对象进行数据操纵。
- `VTI`：具有系统动态视图的查询权限，VTI 默认授权给 DBA 且可转授。例如`v$ciphers`的查询权限。
- `SOI`：具有系统表的查询权限。

> 在使用`manager`达梦管理工具图形化新建表的时候，命名有`create table`权限却会报错没有`v$ciphers`系统视图或者系统对象（例如表）的查询权限，这时候要赋予用户`VTI`和`SOI`角色。

达梦数据库`表`没有属主的概念（和PostgreSQL不同），只有所属`schema`，而`schema`有属主（授权用户）的概念。

> PostgreSQL表和`schema`都有属主的概念。

<h3 id="2">2. 用户密码相关</h3>

<h4 id="2.1">2.1 密码修改</h4>

```shell
#修改用户密码。
alter user <用户名> identified by <PWD>;    
```

<h4 id="2.2">2.2 密码策略修改</h4>

达梦数据库密码策略：

- 0 无策略
- 1 禁止与用户名相同
- 2 口令长度不小于 9 l
- 4 至少包含一个大写字母（A-Z） l
- 8 至少包含一个数字（0-9） l
- 16 至少包含一个标点符号（英文输入法状态下，除“和空格外的所有符号）

> 口令策略可单独应用，也可组合应用。组合应用时，如需要应用策略1 和 4，则设置口令策略为 1+4=5 即可。

修改密码策略：

```shell
#设置用户口令策略。策略为长度至少为8位，包含数字、字母和特殊字符。
#对已有账户修改密码策略
alter user <用户名> PASSWORD_POLICY 31;

#设置系统密码策略
SP_SET_PARA_VALUE(1, ‘PWD_POLICY’, 31);
```

<h3 id="3">3. 修改用户资源限制</h3>

```shell
#语法是：alter user <用户名> limit KEY VALUE，VALUE可以是unlimited，表示无限制
alter user <用户名> limit session_per_user <用户允许的session数>, connection_idle_time <连接空闲时间>, password_life_time <密码有效时长>, password_reuse_time <reuse时间>, password_reuse_max <reuse最大时间>, connect_time <连接超时时间>, cpu_per_session <数值>, cpu_per_call <数值>, read_per_call <数值> mem_space <数值>

#设置用户口令的有效时长。单位：天，unlimited设置无限制
alter user <用户名> limit password_life_time 90;
#设置用户口令过期后可使用天数。过期后，禁止执行除修改口令外的其他操作。
alter user <用户名> limit password_grace_time 10;
#设置密码连接错误最大次数
alter user <用户名> limit failed_login_attemps 5;
#设置连续错误后锁定时间；单位：分钟
alter user <用户名> limit password_lock_time 5;
#设置用户最大空闲时间
alter user <用户名> limit connect_idle_time 30;

#限制TEST账号只允许通过指定网段IP访问数据库：
ALTER USER TEST ALLOW_IP "10.201.34.*","192.168.*.*";
#解除IP访问限制
ALTER USER TEST ALLOW_IP null ;

#用户解除锁定
alter user TEST account unlock;
```

<h3 id="4">4. 审计相关</h3>

```shell
#开启审计开关
# 登录SYSAUDITOR用户或者其他拥有审计权限用户
SP_SET_ENABLE_AUDIT(1); 
#开启覆盖所有用户的语句级审计日志，中间一个参数是用户
#语句级别的审计只针对用户
SP_AUDIT_STMT(‘ALL’,’SYSDBA’,’ALL’);
#设置单个审计文件的大小
#设置单个审计文件大小为1G
sp_set_para_value(1,‘AUDIT_MAX_FILE_SIZE’,1024);

#查看审计日志
select * from V@AUDITRECORDS where USERNAME = 'SYSDBA' order by OPTIME DESC;
#或者使用达梦自带日志分析工具analyze，图形化界面展示日志
```

<h3 id="5">5. 版本和license</h3>

```shell
#查看安装包相关信息，例如：2-2-18-21.08.10-xxx-xxx-SEC SPE Pack13
select id_code;

#查看DM版本
select * from v$version;

#查看license信息
select * form v$license;
```

<h3 id="6">6. 问题记录</h3>

<h4 id="6.1">6.1 达梦数据库获取一个表所有字段的拼接串</h4>

达梦数据库想要获取一张表的所有字段并且用','将各个字段按照顺序拼接起来，形成例如'select a, b, c, d from t_xxxx'这样的形式。

达梦的表`all_tab_columns`存储有各个表以及对应的字段，可以通过函数`wm_concat`将行转列。具体sql如下：

```shell
/opt/dmdbms/bin/disql user_name/'"passwd"'@xx.xx.xx.xx:xx -e "select wm_concat(column_name) from all_tab_columns where owner='TYYW2_LCBA_DATA' and table_name = '$line'" | tail -n 1
```

但是有一个问题，如果行的个数过多，查询出来的结果会被截取，这样拼接出来的结果就可能不完整。有可能是达梦的一个bug。通过shell找折中的办法解决：

```shell
#tail -n +10  --> 从第10行开始到
#tr -s '\n' '\n' --> 删除空行
#tr '\n' ',' --> 换行符替换为','，也就是就每一行用','连起来
/opt/dmdbms/bin/disql user_name/'"passwd"'@ip:port -c "set heading off" -e "select wm_concat(column_name) from all_tab_columns where owner='TYYW2_LCBA_DATA' and table_name = '$line'" | tail -n +10|tr -s '\n' '\n'|tr '\n' ','
```

<h4 id="6.2">6.2 达梦数据迁移整个数据目录并重新启动数据库</h4>

从一台服务器迁移达梦数据库数据目录到另一台服务器之后，启动报错找不到`.DBF`文件。

首先迁移到目标服务器之后，可以修改`dm.ini`文件对应的路径，但是修改之后还是报错。原来有的路径是在数据目录的`dm.ctl`中写死的，这时候就要修改`dm.ctl`文件里的路径。

但是`dm.ctl`是二机制文件，不能进行修改。达梦提供了工具可以经二进制文件转化为文本文件，修改后，再转化为二进制文件：

```shell
#将dm.ctl二进制文件转化为dmctl.txt文本文件
./dmctlcvt TYPE=1 SRC=$DATADIR/dm.ctl DEST=$DATADIR/dmctl.txt

#修改文件中的路径并保存
vim $DATADIR/dmctl.txt

#将dmctl.txt文本文件转化为dm.ctl二进制文件
./dmctlcvt TYPE=2 SRC=$DATADIR/dmctl.txt DEST=$DATADIR/dm.ctl
```

---
