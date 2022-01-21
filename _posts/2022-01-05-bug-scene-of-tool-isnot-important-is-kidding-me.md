---
layout: post
title: 'bug现场谜之超级权限的root用户也存在“创建文件失败”的时候？'
subtitle: '现场ETL抽数报错“创建文件失败”，无法将数据通过达梦dmfldr工具导入数据库中，问题好几天了，看看吧...'
background: '/img/posts/bug-scene-tool-isnot-important-is-kidding-me.jpg'
comment: false
weixinurl: https://mp.weixin.qq.com/s/3J-6FuLMOaHlFtG3Ct4HmA
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 真相浮出水面](#3)
- [4. 总结](#4)
- [更新记录](#99)
- [相关文章推荐](#100)

---

<h3 id="1">1. bug现场情况</h3>

现场`ETL`抽数报错“创建文件失败”，无法将数据通过达梦`dmfldr`工具导入数据库中。

`ETL`的实现是，首先通过`sql`查询将数据库数据导出为`dat`文本文件，实际上就是一个`csv`文件，用分隔符`$`将每一列数据隔开，用换行符`\r\n`将每一行隔开；然后程序中调用`shell`命令借助于数据库load工具（本例中达梦数据load工具为`dmfldr`，`PostgreSQL`数据load可以使用`psql`工具）将文本`csv`数据导入目标数据库。比如对于`PostgreSQL`数据库：

目标表`target_table`为：

```shell
学号 || 姓名 || 年龄 || 得分
```

导出的`csv`文件`student_score.dat`为：

```shell
set client_encoding to 'UTF8'
COPY student_score from stdin WITH DELIMITER '$' ESCAPE E'\\' CSV;
00001$冯兄_01$30$75
00002$冯兄_02$31$85
00003$冯兄_03$32$95
00004$冯兄_04$33$65
00005$冯兄_05$34$55
```

用`shell`命令导入`csv`文件到表中：

```shell
psql -h $HOST -p $PORT -d $DB -U $DB_USER -f /path/to/student_score.dat
```

> 正常如上面那样使用`psql`客户端导入数据是需要输入密码的，可以使用免密的方式，如在客户端程序所在的主机的`~/.pgpass`中增加：`$HOST:$DB:$DB_USER:$DB_PASSWD`。

本例中的达梦数据库也一样，生成的`dat`文件后，程序执行`shell`命令，通过`dmfldr`工具将数据导入目标表。

通过看日志，发现本例中是生成了`dat`文件，只是在导入的时候报错“**创建文件失败**”：

```shell
Caused by: xxxxException: xxxx错误：创建文件失败
    at xxxx.Execute.execute(Execute.java:239)
```

<h3 id="2">2. 尝试破案</h3>

由于报错摸不到头脑，先上后台用命令尝试是否成功导入：

> 为什么说“报错摸不到头脑”？因为程序是以root用户运行的，不存在权限问题，并且`shell`命令导入数据所需要的`csv`数据文件和参数`ctrl`文件都已经生成了，为什么会“创建文件失败”？

达梦`dmfldr`工具导入`csv`数据的方式如下：

```shell
./dmfldr userid=$DB_USER/$DB_PASSWD@$HOST:$PORT control=\'/path/to/test.ctrl\'
```

> 如果`DB_PASSWD`中含有特殊字符，可以使用`'"$DB_PASSWD"'`方法逃逸特殊字符。

`test.ctrl`文件示例：

```shell
OPTIONS (
DIRECT = false
rows = 50000
skip = 0
ERRORS = 0
)
LOAD DATA
INFILE '/path/to/*.dat' STR X '0D0A'
APPEND
INTO TABLE $TABLE
FIELDS '$'
```

> 更多达梦`dmfldr`工具使用参考官方文档：[https://eco.dameng.com/docs/zh-cn/pm/getting-started-dmfldr.html](https://eco.dameng.com/docs/zh-cn/pm/getting-started-dmfldr.html)

报错“文件少列”，意思是用`$`隔开的列的个数和目标表列的个数不匹配，但实际上经过确认`csv`文件数据列的个数和目标的数据列个数是相同的。

通过增加列（不是说少列吗，那就就是增加`$`）、删除字段数据等多种方法反复尝试，最后确认导入`csv`不能成功的原因是字符编码的问题，默认`dmfldr`使用的`GBK`编码，但是`csv`文件是`UTF-8`编码，用`GBK`解码`UTF-8`文件就出现各种奇怪的报错。

> 期间会报各种错误，如“字符串被截断”、“数据格式不正确”等。

`dmfldr`工具默认使用`GBK`编码没法改变，想着是不是可以更改系统使用字符编码为`GBK`，让导出的文件跟随系统编码为`GBK`，这样应该就能够导入了。

尝试修改当前终端系统字符集：

```shell
#查看本地字符集
locale

#查看所有本地支持的字符集
locale -a

#更改字符集，要选择locale -a展示支持的字符集
export LAGNG=zh_CN.gbk
```

> 实际上这里称“字符集”为“字符编码”更为准确，理解字符集与字符编码区别，参考文章：[https://fengmengzhao.github.io/2015/07/30/computer-character-coding-styles.html](https://fengmengzhao.github.io/2015/07/30/computer-character-coding-styles.html)

修改后，重启系统，发现系统界面、日志到处是乱码，导出的文件编码还是`UTF-8`编码，实际上说明导出文件的编码不会随运行系统字符集改变而改变，这也是开发的规范。

只能联系产品的研发修改代码了吗？

<h3 id="3">3. 真相浮出水面</h3>

第二天将前一天的验证结论又确认了一遍，本地使用命令导入数据时，字符编码存在问题。在`ctrl`文件中加入参数`CHARACTER_CODE = 'UTF-8'`之后数据能正确导入。

就要联系产品提`bug`的时候，突然想到不是有传说中的[Arthas](https://arthas.aliyun.com/zh-cn/)存在吗？可以做到在线反编译、修改代码、重新编译并重新加载类。上`Arthas`！玩一玩。

> `Arthas`的安装不再赘述，[文档](https://arthas.aliyun.com/doc)很清楚。

使用命令启动并连接`Arthas`：

```shell
#启动Arthas
#注意替换$PID，$PID是运行的JVM进程pid，通过命令ps -ef |grep xxx 获取
nohup java -Xbootclasspath/a:/opt/jdk-1.8/lib/tools.jar -jar ~/.arthas/lib/3.5.4/arthas/arthas-core.jar -pid $PID -target-ip 127.0.0.1 -telnet-port 9658 -http-port 9563 -core ~/.arthas/lib/3.5.4/arthas/arthas-core.jar -agent ~/.arthas/lib/3.5.4/arthas/arthas-agent.jar &

#确认Arthas是否启动成功，上面设置telnet的端口号为9658，该端口可以修改
netstat -nalp |grep 9658

#连接Arthas
telnet 127.0.0.1 9658 
```

> 注意：用命令行启动`Arthas`进程后，立即用命令`ps -ef |grep arthas`能看到一个进程，说明`Arthas`在启动中，过一会儿进程消失，说明`Arthas`已经启动成功或者失败。如果成功的话，使用`netstat -anlp |grep $PID`能看到`Arthas`启动是指定的`telnet`监听端口。找不到指定的`telnet`监听端口说明没有启动成功，需要查看`~/log/arthas/arthas.log`日志文件。

使用`Arthas`修改代码并重新编译：

```shell
#根据报错日志，找到报错类Execute
sc *Execute

#反编译运行class文件为源代码
jad --source-only xxxx.Execute > /tmp/Execute.java

#修改源代码
#ctrl文件中增加字符编码设置：CHARACTER_CODE = 'UTF-8'

#查找该类的类加载器hash值
sc -d *Execute |grep classLoaderHash

#在线编译修改
mc -c $CLASSLOADER_HASH /tmp/Execute.java

#重新热加载class
redefine -c $CLASSLOADER_HASH /tmp/xxxx/Execute.class
```

重新执行`ETL`程序，发现还是报同样的错。后台查看`ctrl`文件内已加上了`UTF-8`字符编码的设置，手动执行`dmfldr`收入导入，能够导入成功。

可为什么还报错呢？这时候意识到可能**程序执行的`shell`命令和笔者后台执行的命令不一致**。`Arthas`不是能`wath`参数吗？走一波：

```shell
#查看方法入参、类成员信息、返回信息、异常信息
#params是参数 target是当前类成员信息 returnObj是方法返回值 throwExp是抛出异常信息
#-x 2 表示递归层级 -e 表示异常时抛出
watch xxxx.Execute exec "{params, target, returnObj, throwExp}" -e -x 2
```

> `Arthas`还可以使用`OGNL`表达式，例如：`watch xxx.FileDAO TransString @org.apache.commons.io.IOUtils@toByteArray(params[0].getBinaryStream()) -b -e -x 2`，这里`@`是`OGNL`调用类静态成员或者方法的写法。

**arthas执行静态方法、属性**

```shell
#调用静态属性
ognl '@全路径类目@静态属性名'

#ognl执行静态方法
ognl '@全路径类目@静态方法名("参数")'

#ognl参数的使用
ognl '#value1=@com.shirc.arthasexample.ognl.OgnlTest@getPerson("src",18), #value2=@com.shirc.arthasexample.ognl.OgnlTest@setPerson(#value1) ,{#value1,#value2}' -x 2
```

更多`OGNL`用法请参考：[https://commons.apache.org/proper/commons-ognl/language-guide.html](https://commons.apache.org/proper/commons-ognl/language-guide.html)

重新执行程序，控制台得到程序完整的执行`command`是：

```shell
/path/to/dmfldr userid=$DB_USER/'"DB_PASSWD"'@$HOST:$PORT control=\'/path/to/*.ctrl\' character_code=\'utf-8\' log=\'/path/logs/dmfldrLog/fldr.log.2022-01-05\' badfile=\'/path/logs/dmfldrLog/fldr.bad.2022-01-05\'
```

复制命令，手动在后台执行以下，**报错“创建文件失败”，和日志中的报错一致**。知道问题是哪里了，应该就是日志文件创建的时候缺少目录，造成不能创建日志文件报错。

手动创建日志文件目录：`mkdir -p /path/dmfldrLog`，重新执行导入命令，执行成功。重新执行`ETL`抽数程序，也成功，破案了！

<h3 id="4">4. 总结</h3>

1. 实际上在笔者自己后台执行`dmfldr`命令的时候就走偏了，手动执行的命令和程序执行的命令不一致，结果自己的命令出新的bug以为就是问题所在，方向没找对，陷得更深了。
2. 第一时间应该要用`Arthas`，当时现场环境只有`JRE`，笔者懒了，也付出了代价。
3. 报错日志（本例中是“创建文件失败”，最后排查实际上问题就是一个日志文件路径目录不存在，造成`dmfldr`不能创建日志文件）很重要，查bug的时候多联系报错信息，能有助于查错不跑偏方向。
4. 如果在不知道代码的情况下，`Arthas`真是一个利器，能极大提高排查问题的效率。大神总说工具不重要，实际上大神对工具都运用自如了，才说不重要。`Arthas`工具值得Java程序员好好学习。
5. 不能先入为主，看到“创建文件失败”，认为以`root`启动的程序就能够创建文件成功，本例中是用`root`身份执行了`dmfldr`命令，关键是命令中带有绝对路径的日志路径，由于路径目录不存在，`dmfldr`工具就报错了。关键不在于是否是`root`的问题，而是`dmfldr`在没有目录的情况下不会自动创建目录。
6. 实际上这里`ETL`程序通过`shell`调用第三方程序，要考虑周全第三方程序可能的报错，否则就会出现类似`bug`。

<h3 id="99">更新记录</h3>

- 2022-01-07 18:16 “冯兄画戟”微信公众号文章发表前重读、优化、勘误
- 2022-01-20 10:13 增加arthas启动判断内容
- 2022-01-21 22:35 [掘金专栏](https://juejin.cn/column/7049663804136751140)发表前重读、优化、勘误

<h3 id="100">相关文章推荐</h3>

- [计算机字符编码](https://fengmengzhao.github.io/2015/07/30/computer-character-coding-styles.html)