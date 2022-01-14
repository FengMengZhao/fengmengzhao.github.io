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
- [2. 用户密码相关](#2)
    - [2.1 密码修改](#2.1)
    - [2.2 密码策略修改](#2.2)
- [3. 修改用户资源限制](#3)
- [4. 审计相关](#4)
- [5. 版本和license](#5)

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
#开启覆盖所有用户的语句级审计日志
SP_AUDIT_STMT(‘ALL’,’NULL’,’ALL’);
#设置单个审计文件的大小
#设置单个审计文件大小为1G
sp_set_para_value(1,‘AUDIT_MAX_FILE_SIZE’,1024);
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

---
