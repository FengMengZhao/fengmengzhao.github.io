---
title: 学习PostgreSQL
layout: post
---

### 安装之后允许non-host连接

**修改data目录下的`pg_hba.conf`**

`host all all 0.0.0.0/0 trust`

> all DATABASE all users all host will be trust

### 常用数据库命令-控制台

    # 控制台连接数据库
    psql -U user_name -d database_name -h host -p port

    # 控制台常用命令
    \h: 查看sql命令的解释,比如\h select
    \?: 查看sql的命令列表
    \l: 列出所有的数据库
    \c [database_name]: 连接其他数据库
    \d: 列出当前数据库的所有表格
    \d [table_name]: 列出一张表格的结构
    \du: 列出所有的用户
    \e: 打开文本编辑器
    \x: 扩展显示打开|关闭(以列的形式展示数据)
    \o file_name: 将控制台输出重定向到文件file_name中
    \conninfo: 列出当前的数据库和连接信息
    \password user_name: 为用户设置密码
    \q: 退出控制台

### 用户管理常用命令-控制台

    # 创建用户[设置密码]
    CREATE USER user_name [WITH PASSWORD 'password'];

    # 创建数据库并指定拥有者
    CREATE DATABASE database_name OWNER user_name;

    # 将数据库(database_name)的所有权都赋予用户(user_name),否则用户只能登陆,没有任何数据库操作权限
    GRANT ALL PRIVILEGES ON DATABASE database_name TO user_name

    # 创建超级用户
    CREATE USER user_name SUPERUSER PASSWORD 'password';

    # 删除用户
    DROP USER IF EXISTS user_name 

    # 为用户指定查询路径(search_path)
    ALTER USER user_name SET SEARCH_PATH TO schema_name, database_name;

    # 更改数据库的owner
    alter DATABASE database_name owner TO user_name

### 用户管理常用命令-shell

    # 创建用户
    sudo -u postgres CREATEuser --superuser user_name

    # 设置密码
    \password user_name

    # shell命令下创建数据库并指定拥有者
    sudo -u postgres CREATEdb -O user_name database_name

### 数据库操作

    # 创建新表
    CREATE TABLE table_name(name VARCHAR(20), time DATE);

    # 插入数据
    INSERT into table_name(name, time) VALUES('张三', '2017-08-02')

    # 选择记录
    select * from table_name;

    # 删除记录
    UPDATE table_name SET name = '李四' where name = '张三';

    # 添加栏位
    ALTER TABLE table_name ADD email VARCHAR(40);
    ALTER TABLE table_name ADD id serial PRIMARY KEY;

    # 更新结构     
    ALTER TABLE table_name ALTER COLUMN time SET NOT NULL;

    # 更名栏位
    ALTER TABLE table_name RENANE COLUMN time TO currenttime;

    # 表重命名
    ALTER TABLE table_name RENAME TO new_name;

    # 删除栏位
    ALTER TABLE table_name DROP COLUMN email;

    # 删除表格
    DROP TABLE IF EXISTS table_name;

    # 删除表格的内容 
    TRUNCATE table_name;
    DELETE FROM table_name;

    # 转变column类型
    SELECT * FROM table_name ORDER BY colunmn_str::integer;

    # 增加主键
    ALTER TABLE table_name ADD PRIMARY KEY (column_name);

    # 删除主键
    ALTER TABLE table_name DROP CONSTRAINT table_name_pkey;

    # 获取数据库内schema下的所有表信息
    SELECT * FROM INFORMATION_SCHEMA.TALBES WHERE TABLE_SCHEMA = <schema_name>

    # 获取表的所有字段信息
    SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = <schema_name> AND TABLE_NAME = <table_name>

### 备份还原操作

    # 执行sql文件
    psql -U user_name [-d] database_name < sql_file

    # 备份文件
    pg_dump -h hostname -p port -U user_name -d database_name [--schema=schema_name] -t table_name > dump_file

> 执行sql 文件`-d`参数可以缺省.<br><br>
如果要还原一个表(表中有schema信息),必须要先建立对应的schema和数据库;如果是要还原一个schema,则要建立数据库(数据库是任意的).<br><br>
数据库 --> schema --> 表.<br><br>
备份一个表格时schema名字可缺省,指定schema时要用格式`--schema=schema_name`.
