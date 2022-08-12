---
layout: post
title: 'bug现场谜之报错也识趣-客户一上班报错就消失了！'
subtitle: '友商系统的一个bug，已经存在一年多了，解决不了。客户意见很大，让我司解决下。这谜一样的bug，上班前出现，客户上报就消失了，不影响客户使用，也难怪忍了这么久。看看吧......'
background: '/img/posts/bug-scene-bug-disappear-when-working-time.jpg'
comment: false

weixinurl: ''
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 破案绊脚石](#3)
    - [3.1 Tomcat JNDI容器数据源的配置](#3.1)
    - [3.2 项目字符编码配置](#3.2)
    - [3.3 DB2数据库迁移](#3.3)
    - [3.4 第一次听说currentFunctionPath这玩意](#3.4)
- [4. 总结](#4)
- [更新记录](#99)

---

<h3 id="1">1. bug现场情况</h3>

部署的是Java应用，容器中间件使用的是IBM WebSphere，数据库使用的是IBM DB2，数据源使用的是容器中间件提供的`JNDI`数据源服务。

程序在上班前的一段时间点击报错mybatis查询失败(`sqlcode=-774 sqlstate=2D522`)，上班之后就恢复正常了。生产环境的日志没有有效的信息，全是`-774`的报错，而把报错的sql拿出来在数据库执行没有问题，并且同样的sql在客户上班后系统中执行没有问题。

粗略在网上查一下，DB2数据库`sqlcode=-774`的含义是：`Explanation: Statement cannot be executed within a compound SQL statement.`，意思是“语句不能在复合sql中执行”；`sqlstate=2D522`的含义是：
“ATOMIC 复合语句中不允许 COMMIT 和 ROLLBACK”。初步的判断可能和数据库的事务相关。

<h3 id="2">2. 尝试破案</h3>

日志中没有有效的报错信息，只能先观察程序表象。发现，报错的现象在中午`13:00`之后是能够稳定复现的，在`14:30`之后就稳定消失。问题出现的时候，随意刷新页面就会报错，具体后台就是`-774`，也就是`mybatis`执行sql不成功。而`14:30`之后，随便刷新页面都不会出现问题，即使客户端并发多个请求造成响应慢但是也不会出现报错`-774`。如果在报错这段时间重启了应用程序，则报错就会消失。

从以上信息，大概估计，问题应该是出现在了**数据库连接池**上。在某个时刻，程序用数据库连接池中的连接执行了某些操作，该数据库连接就处于了“异常”状态，其他任何功能用到该“异常”数据库连接执行sql时，就会报错，不是数据库有什么问题，而是连接数据库的数据库连接有问题。这样设想也解释了两点，第一：为什么同样的sql、同一时刻在程序中执行就会报错，而拿出来到数据库客户端中执行就没有问题；第二：程序报错的时候重启，重启后为什么就正常了。原因都是报错来自于用到了数据库连接池中“异常”的数据库连接。

那具体是什么样的操作造成了数据库连接池连接的异常呢？正常在日志中应该有所体现才对，生产环境IBM Websphere的日志太不方便看了，并且也没法设置参数重启进行监控。研究决定将生产环境迁移出来一份作为测试环境。原来容器中间件为`IBM WebSphere`迁移到`Tomcat`上。迁移相关配置参考[Tomcat JNDI容器数据源的配置](#3.1)。

迁移完成后程序运行一段时间后查看日志，发现最先开始报错的日志有如下内容：`Processing was cancelled ... SQLSTATE=57014`，日志显示在执行一个存储过程的时候，由于某种原因（可能是时间过长）客户端主动取消了。后来在网上查到了IBM官方的一个帖子：[https://www.ibm.com/support/pages/apar/IC64958](https://www.ibm.com/support/pages/apar/IC64958)。心里一下子豁然开朗了，这不就是DB2的一个bug吗？

IBM官方帖子说：“在DB2版本9上有一个bug，如果你在一个数据库连接中执行一个存储过程，因为某种原因客户端取消了，这时候在这个连接上执行的任何sql都会报错-774”。官方还提供了bug的补丁包。果然，找到报错的存储过程，处理之后，后续程序就没有出现过问题了。

这下找到了是什么样的操作导致数据库连接池中的连接“异常”了，就是DB2数据库的一个[bug](https://www.ibm.com/support/pages/apar/IC64958)（存储过程被客户端取消后，使用同一连接执行sql都会报错-774），DB2在后续高版本上解决了这个bug。

<h3 id="3">3. 破案绊脚石</h3>

<h4 id="3.1">3.1 Tomcat JNDI容器数据源的配置</h4>

1). 配置`server.xml`中Resource。在`GlobalNamingResources`节点下增加节点`Resource`：

```shell
  </GlobalNamingResources>
    ...
    <Resource name="jdbc/court" global="jdbc/court"
        auth="Container"
        type="javax.sql.DataSource"
        driverClassName="com.ibm.db2.jcc.DB2Driver"
        url="jdbc:db2://xxx.xxx.xxx.xxx:50000/xxx:currentSchema=xxx;currentFunctionPath=xxx;"
        username=""
        password=""
        maxActive="20"
        initialSize="0"
        minIdle="0"
        maxIdle="8"
        maxWait="10000"
        timeBetweenEvictionRunsMills="30000"
        minEvictableIdleTimeMillis="60000"
        testWhileIdle="true"
        validationQuery="select current date from sysibm.sysdummy1"
        maxAge="600000"
        rollbackOnReturn="true"
        factory="org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory"/>
    ...
  </GlobalNamingResources>
```

2). 配置`context.xml`。在`Context`节点下增加节点`ResourceLink`：

```shell
<Context>
  <ResourceLink global="jdbc/court" name="jdbc/court" auth="Container" type="javax.sql.DataSource" />
</Context>
```

3). 配置数据源`applicationContext.xml`：

```shell
<bean id="datasource" class="org.springframework.jndi.JndiObjectFactoryBean">
    <property name="jndiName">
        <value>java:comp/env/jdbc/court</value>
    </property>
</bean>
```

<h4 id="3.2">3.2 项目字符编码配置</h4>

由于解决问题的项目是10年前的老项目，因此项目的字符编码使用的是`GBK`。在迁移项目的过程中本地启动项目发现乱码，于是就尝试将`xml`文件的编码从`GBK`改为`utf8`。但是`xml`文件太多了，没有有效的工具能批量将`GBK`编码改为`utf8`编码。

实际上这个解决编码问题的思路方向走错了，如果原来项目使用的是`GBK`编码，那么迁移过也要使用`GBK`编码，而不是尝试将原来文本文件的字符编码进行修改。JVM参数指定项目字符编码的参数是：`-Dfileencoding=GBK`。

<h4 id="3.3">3.3 DB2数据库迁移</h4>

DB2数据库之前没有接触过，迁移一个DB2数据库到新的服务器上也没有什么经验。走了一些弯路，主要问题是迁移表缺失、数据量不匹配。需要注意的问题是：

1. `db2lookup`生成的迁移sql在目标db2服务上执行时，如果缺失表空间等元信息可能就会报错；一些sql语句可能在不同配置的环境上执行不了。关键是要验证表是否完整的迁移了，这一步是基础工作，非常重要。
2. 一些表因为触发器、外键约束等的存在直接使用`db2move`命令迁移数据可能会不成功，这些表需要做单独的处理。

<h4 id="3.4">3.4 第一次听说currentFunctionPath这玩意</h4>

之前知道一些数据库的jdbcurl上支持配置一个`currentSchema`，表示默认查询的模式名，如果在jdbcurl配置上默认的schema，在程序中的sql可以省去查询语句表名前的schema。

迁移的程序启动后报错一个`function`找不到，但实际上数据库中该函数是存在的，将sql中函数前面加上模式名就可以调用成功。如果要将一个个函数都加上模式名实在太傻太累了，实际上这个方法是走偏了，应该上官网上找一找DB2的jdbcurl支持的参数，还真有`currentFunctionPath`这样的参数。有时候需要限定问题真正出现的范围，往这个范围查找。不然不从根本上解决问题会带来更多的问题或者非常大的工作量。

<h3 id="4">4. 总结</h3>

1. 再着急的工作也一定要有个整体的方案，一些基础性的工作（例如数据迁移）是着急不来的，反而越着急，越会出问题，越影响整个进度。做整体方案的时候，那些脑海中一闪而过的担忧要仔细分析，不能心存侥幸心理、或者说后面再说。有些事情前期做成本很小，到了后期再做就会走非常多的弯路。
2. 对于自己不熟悉的技术，一定要好好看看官网解释，不要随便找到一个网络上的命令就直接用，你们的场景可能是不太一样的，所需要的参数可能也是不同的，最好弄懂每个命令/参数的含义，不能着急，否则做了也会是有问题的。
3. 问题一定是有原因的，可以暂时不去追根溯源，但要坚信肯定不是玄学。利用自己的知识将原因锁定在一定的范围内，大胆猜测、查资料、排查。 
4. 解决问题不能只解决表象，只解决表象可能还有大量问题等着你，实际上还是没有找到解决问题的途径。稳住参照3揪出来问题的元凶，就舒服了。

<h3 id="99">更新记录</h3>

- 2022-08-11 18:20 首次提交文章到[冯兄话吉](https://blog.learnbyteaching.xyz)。
- 2022-08-12 15:00  微信公众号“冯兄画戟”文章、[掘金专栏](https://juejin.cn/column/7049663804136751140)发表前重读、优化、勘误
