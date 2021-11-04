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
    # pg_dump备份的时候，最好添加一个 --inserts参数，尽量备份出来dml语句格式的，因为是扩版本恢复，默认的可能存在问题
    # --inserts参数备份下来的dump语句数据是insert into的方式一行行插入，而不是默认的copy形式，如果数据量大备份还原，建议不加这个参数
    pg_dump --inserts -h hostname -p port -U user_name -d database_name [--schema=schema_name] -t table_name -f dump_file

> 执行sql 文件`-d`参数可以缺省.<br><br>
如果要还原一个表(表中有schema信息),必须要先建立对应的schema和数据库;如果是要还原一个schema,则要建立数据库(数据库是任意的).<br><br>
数据库 --> schema --> 表.<br><br>
备份一个表格时schema名字可缺省,指定schema时要用格式`--schema=schema_name`.

### 造数常用的语句

```
# 生成从a到b的整数
FLOOR(random() * (b-a+1) + a)

# 生成从a到b的数
random() * (b-a+1) + a

# 保留m位小数
round(NUMERIC_VALUE, m)

# 从数组中任意生成一个
(array['0712', '0713', '0714', '0715', '0720', '0721'])[floor(random() * (6-1+1)) + 1]

# 把查询结果作为数组任意返回一个
(array(select colume from table))[floor(random() * (SIZE-1+1)) + 1]

# 从某个时间到某个时间随机日期
timestamp '2013-01-01 00:00:00' + random() * (timestamp '2019-11-01 00:00:00' - timestamp '2013-01-01 00:00:00')
```

### 最佳实践

**postgre更改某个schema下所有表的owner**

```
SELECT format(
  'ALTER TABLE %I.%I.%I OWNER TO %I;',
  table_catalog,
  table_schema,
  table_name,
  current_user  -- or another just put it in quotes
)
FROM information_schema.tables
WHERE table_schema = 'mySchema'
```

**暴力修改字段的长度postgre**

```
select * from pg_attribute where attribute = ${表名称}::regclass and attname = ${字段名称}
update pg_attribute set atttymod=${要修改的字段长度+4} where attrelid = ${表名称}::regclass and attname = ${字段名称}
```

**postgre赋权**

```
grant select,insert,delete,update,truncate on all tables in schema ${schema_name} to ${user_name}
grant usage on schema ${schema_name} to ${user_name}
```

**postgre创建只读账号**

```
create user tyyw_rdonly ENCRYPTED PASSWORD '123456'
alter user tyyw_rdonly set default_transaction_read_only=on

--postgre高版本用这个（进到库里面执行）
grant select on all tables in schema tyyw to tyyw_rdonly 

--postgre低版本
--生成grant语句
SELECT 'GRANT SELECT ON ' || table_schema || '.' table_name || ' to tyyw_rdonly;'
from information_schema.TABLES
where table_type = 'BASE_TABLE'
AND table_schema = ${schema_name}
```

**postgre数据库中查询元数据的定义**

```
--conf
select name,setting from pg_settings

--view
select pg_get_viewdef('viewname', true)

--index
select tablename,indexdef from pg_indexes where tablename ='${table_name}'
```

**postgre日志开启**

```
--日志位置：$PG_DATA/logs

--打开慢sql记录 如果sql运行1000ms，则日志该sql
alter system set log_min_duration_statement=1000
--重载配置
select pg_reload_conf()

--用psql登陆数据库，然后开启记录时间 \timing，再然后 copy 导出表数据到本地看看，两边所花费的时间
```

**postgre获取所有表-自定义函数**

```
CREATE 
	OR REPLACE FUNCTION getalltables ( schemaname VARCHAR ) RETURNS VARCHAR AS $$ BEGIN
RETURN (SELECT
	string_agg ( table_name, ', ' ) 
FROM
	information_schema.TABLES 
WHERE
	table_schema = schemaname);
	END;
$$ LANGUAGE PLPGSQL;
```

**postgre生成删除主键限制sql**

```
SELECT
	'alter table tyyw.' || table_name || ' drop constraint ' || constraint_name || ';' AS my_query 
FROM
	information_schema.table_constraints 
WHERE
	table_schema = 'tyyw' 
	AND table_name in(select * from tyyw.tablenametest)
	AND constraint_type = 'PRIMARY KEY'
```

**删除数据库连接**

```
--step 1
REVOKE CONNECT ON DATABASE db_jcdsj FROM public;

--step 2 如果procid报不存在 应该是版本问题 用pid
SELECT 
   pg_terminate_backend(pg_stat_activity.procpid)
FROM 
   pg_stat_activity
WHERE 
   pg_stat_activity.datname = '${db_name}'
AND procpid <> pg_backend_pid();

-- 删除IDLE状态的数据库连接
select pg_terminate_backend(procpid) from pg_stat_activity where current_query='<IDLE>'
```

**`postgresql` group by 主键可在SELECT字段选择任意字段**

`select c_bh, c_name, c_address, c_identity_code from t_user group by c_bh`，`c_bh`是表`t_user`的主键，当`group by`主键的时候，其他字段也可以出现在select的列表中

为什么？官方[doc](https://www.postgresql.org/docs/current/sql-select.html#SQL-GROUPBY)：

> *When* `GROUP BY` *is present, or any aggregate functions are present, it is not valid for the* `SELECT` *list expressions to refer to ungrouped columns except within aggregate functions or when the ungrouped column is functionally dependent on the grouped columns, since there would otherwise be more than one possible value to return for an ungrouped column.* **A functional dependency exists if the grouped columns (or a subset thereof) are the primary key of the table containing the ungrouped column***.*

**当我们在`sql`中使用聚合函数的时候，select查询字段正常只能是聚合字段，除非这个字段依赖于聚合字段。如果聚合的字段是这个表的主键，那么其他字段和主键都是依赖关系（1对1），因此上面`sql`非`group by`字段可以出现在select字段中。也可以理解当`group by`主键的时候，相当于`group by`了这个表的所有字段。**

---
