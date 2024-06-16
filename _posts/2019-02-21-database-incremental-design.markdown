---
title: 数据库增量设计
layout: post
---

### 数据库增量简介

通常在数据量较少的情况下,我们将一个数据源的全部数据加载到目标数据库的时候,可以采取的策略是:将目标数据库的数据全部清空掉,然后全部重新从数据源加载进来.这是一个简单直观并且最不容易出错的一种解决方案,但是很多时候会带来性能上的问题.

如果我们的数据源来自不同的业务系统,数据动辄百万,千万甚至亿级算.第一次需要全部加载,如果在第二周期或者第三周期的时候仍然全部加载的话,耗费了极大的物理和时间资源.有可能部分数据源并未发生改变,而有的数据源可能只增加了少量的数据.

我们考虑的问题是,对于已经存在目标数据库的数据是历史数据,对于数据源中的数据,我们只应该考虑新修改的记录和新插入的记录,只应该考虑这两种数据.所以增量处理实质上是处理变化的数据.

### 数据库增量示例

这是一张会员信息表,创建会员的时候会生成一条记录,会在CreatDate标记一下,并且在UpdateDate中保存的也是CreateDate的值.当CreateDate和UpdateDate相同的时候说明这是一条插入操作,但是这个会员是可以被编辑和修改的,于是每次更新的同时也更新的UpdateDate时间戳.

![会员表1](/img/posts/incremental-table-one.png)

假设上面的几条数据在第一次加载到目标数据库后,源表新加入了一条会员记录并同时修改了一条会员的信息.

![会员表2](/img/posts/incremental-table-two.png)

这时候要实现数据库的增量处理,可以这样处理:

- 第一次加载动作完成后,记录下最大的UpdateDate时间戳,保存到一个加载记录表中(第一次是2010-10-23).
- 第二次加载数据的时候,用加载记录表中的时间戳与源表中的UpdateDate相比较,比时间戳大的说明是新添加或者修改的数据(大于2010-0-23的第一条是Update数据,第四条是新增加的数据).
- 当整个加载过程完成后,更新最大的UpdateDate到记录表中(记录表中将2010-10-26记录下来)

### Linux中实现MySQL增量同步

#### 同步方法

- 将如下脚本写入sync.sh文本文件中
- 需提前配置好源数据库与目标数据库的地址、用户名、密码、数据库信息，及数据库中的哪些table需同步
- 需提前在目标数据中建好相关的表，且表结构与源数据库相同
- 配置增量数据的本地下载地址
- 配置好增量更新的sql where条件子句，如”createDate >= ‘2016-07-01’”
- 从操作系统级别启动定时任务执行sync.sh

#### 配置信息

    #!/bin/sh
    # 远程数据库服务器信息（从此数据库下载数据）
    remoteMysqlServer="远程数据库ip"
    remoteMysqlUser="远程数据库用户名"
    remoteMysqlPassword="远程数据库密码"
    remoteMysqlDB="远程数据库表shema"
    # 本地数据库服务器信息（将数据导入到此数据库）
    localMysqlServer="本地数据库ip"
    localMysqlUser="本地数据库用户名"
    localMysqlPassword="本地数据库密码"
    localMysqlDB="本地数据库schema"
    # 数据下载目录
    dataDownloadPath="/usr/local/data/download/"
    # 处理哪些数据库表（注意，需提前在localMysql中建好相关的表，且表结构与remoteMysql相同）
    array_name=(table1 table2 table3 table4 table5)

#### 根据不同的操作系统计算日期

    #!/bin/sh
    today=$(date +%Y%m%d)

    case "$OSTYPE" in
      linux*)
        echo "LINUX"
        deleteDay=$(date -d "-7 day" +%Y%m%d)
        ;;
      darwin*)
        echo "OSX"
        deleteDay=$(date -v -7d +%Y%m%d)
        ;;
      win*)
        echo "Windows"
        ;;
      cygwin*)
        echo "Cygwin"
        ;;
      bsd*)
        echo "BSD"
        ;;
      solaris*)
        echo "SOLARIS"
        ;;
      *)
        echo "unknown: $OSTYPE"
        ;;
    esac

#### 下载远程数据到本地

    #!/bin/sh
    # 判断${dataDownloadPath}/$today目录是否存在
    if [ ! -d "${dataDownloadPath}/${today}"  ]; then
      mkdir -p ${dataDownloadPath}/${today}
    fi
    echo "下载远程数据库到本地文件${dataDownloadPath}/${today}-----------------"
    for i in ${array_name[@]}
        do
            echo "正在下载远程数据库表 ${i} 的数据"
            # 获取本地数据库最新一条记录的时间
            mysql  -h ${localMysqlServer} -P3306 -u${localMysqlUser} -p${localMysqlPassword} --compress ${localMysqlDB} -e "select max(datatime) from ${i}" > ${dataDownloadPath}/${today}/tmp.txt
            mysqldump -t -h ${remoteMysqlServer} -u${remoteMysqlUser} -p${remoteMysqlPassword} --single-transaction --compress ${remoteMysqlDB} ${i} --where="createDate > '`tail -1 ${dataDownloadPath}/${today}/tmp.txt`'">${dataDownloadPath}/${today}/${i}_${today}.sql
            echo "远程数据库表 ${i} 的增量数据下载成功"
        done
    echo "-----------------------------------------------------------------"

#### 将文件中的数据导入到本地仓库

    #!/bin/sh
    echo "从${dataDownloadPath}中提取数据,导入本地数据库中----------------------"
    for i in ${array_name[@]}
        do
            echo "即将从本地 ${dataDownloadPath}/${today}/${i}_${today}.sql 提取数据并导入到 ${i} 的数据库表"
            mysql  -h ${localMysqlServer} -P3306 -u${localMysqlUser} -p${localMysqlPassword} ${localMysqlDB} -e "source ${dataDownloadPath}/${today}/${i}_${today}.sql"
            echo "成功导入数据到数据库表 ${i}"
            echo 
            echo 
        done
    echo "-----------------------------------------------------------------"
    echo 

#### 删除一周前的本地旧数据

    #!/bin/sh
    if [ -z "${deleteDay}"  ]; then 
        echo "deleteDay is empty----------------------------------------------"
        exit
    fi

    if [ -d "${deleteDay}"  ]; then
        echo "${deleteDay}文件夹即将删除-----------------------------------------"
        rm -rf ${dataDownloadPath}/${deleteDay}/
        echo "${deleteDay}文件夹删除成功-----------------------------------------"
    fi

    exit

---

<center style="color: red">End</center>

---
