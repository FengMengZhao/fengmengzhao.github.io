---
layout: post
title: 'Mybatis简单使用'
subtitle: 'Mybatis作为一个ORM框架将SQL与Java对象映射起来大大提高开发效率，其配置使用虽为简单，也经常概念混淆并且搜素资料耗时。本文旨在提供清晰的Mybatis配置和使用方式。'
background: '/img/posts/insight-into-mybatis.jpg'
comment: true
---

# 目录

- [1. Mybatis概要](#1)
- [2. Mybatis3使用](#2)
    - [2.1 Mybatis3基本Maven工程(集成LOG4J日志)](#2.1)
    - [2.2 DAO接口方式使用Mybatis3](#2.2)
    - [2.3 Mapper代理的方式使用Mybatis3](#2.3)
        - [2.3.1 Mapper代理注解SQL(@select)的方式使用Mybatis3](#2.3.1)
- [3. Mybatis使用问题收集](#3)
    - [3.1 动态SQL Where条件中使用`<if test='xxx == "abc"' />`报错: `There is no getter for property named 'xxx' in 'class java.lang.String'`](#3.1)
    - [3.2 使用$和使用#来占位变量的区别](#3.2)
    - [3.3 需要从SQL的执行结果封装为Map集合返回](#3.3)
    - [3.4 mybatis自定义typehandler改变字段数据类型](#3.4)
    - [3.5 mybatis自定typehandler数据库array类型映射为pojo数组](#3.5)
    - [3.6 mybatis将sql结果集部分字段转化为hashmap类型](#3.6)

---

<h3 id="1">Mybatis概要</h3>

在原始`JDBC`操作数据库的时候，通常需要：

1. 加载`JDBC`驱动(`Driver`);
2. 获取数据库连接(`Connection`);
3. 创建SQL + `Statement`;
4. 为`PreparedStatement`中的SQL设置参数值(占位符参数);
5. 通过`PreparedStatement`执行SQL并获取`ResultSet`;
6. 解析`ResultSet`进行处理;
7. 释放资源(`ResultSet`、`PreparedStatement`、`Connection`).

原始的JDBC存在以下几个方面的问题：

1. 每次使用数据库连接时需要创建，使用后即释放资源，频繁的开/闭数据源，造成资源的浪费和影响数据库的性能(可以考虑用数据库连接池);
2. SQL语句硬编码在Java代码中，耦合性太高，SQL一旦改变需要重新编译项目(可以考虑使用配置文件);
3. SQL占位符参数类型、位置和值硬编码在Java代码中，耦合性太高(考虑将占位符参数可以配置在配置文件中);
4. 硬编码处理`ResultSet`结果集(将结果集自动映射为Java对象).

这时候`Mybatis`作为ORM框架就出现了，主要解决的是SQL和Java对象的映射问题。

首先，看一看ORM框架(Mybatis/Mybatis-Spring)在整个项目中的层级：

![ORM框架层级](/img/posts/how-orm-works-in-application.png "ORM框架层级")

> 图中的`Repository`就是应用开发中的`Dao`层。`ORM`框架建立在`Java JDBC API`基础之上，`Java JDBC API`建立在`Java JDBC API Implementation`基础之上，`Java JDBC API Implementation`建立在持久层数据库之上。

接下来，看一看Mybatis3各个组件之间的工作原理：

![Mybatis工作原理](/img/posts/relation-of-mybatis3-components.png "Mybatis3工作原理")

> 1. 项目全局只需要执行一次的操作：`SqlSessionFactoryBuilder`通过读取Mybatis配置文件，创建`SqlSessionFactory`;
2. `Dao`层每次接口被调用时执行的操作：`SqlSessionFactory`创建`SqlSession`，`SqlSession`读取`MappingFile`映射文件执行SQL.<br><br>
需要注意的是：<br>
- `SqlSessionFactory`应该是单例的;
- `SqlSession`是线程不安全的，其作用域应该是方法级的.

---

<h3 id="2">Mybatis3使用</h3>

Mybatis3的使用方式有两种：

1. 原始的`Dao`接口开发;
2. `mapper`代理的方式.

首先，建立一个Mybatis工程(Maven)，集成`LOG4j`日志，使用`JUNIT4`进行单元测试。然后分别介绍`Dao`接口方法和`Mapper`代理方法。

<h3 id="2.1">Mybatis3基本Maven工程(集成LOG4J日志)</h3>

> Maven工程的核心`POM.xml`依赖：

    <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.37</version>
    </dependency>
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.2.3</version>
    </dependency>
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>
    </dependencies>

> Mybatis的SQL、SQL参数和对象映射关系都是在`Mapper`文件中配置的。`Mapper`文件如下：

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

        <select>...</select>
        <insert>...</insert>
        <update>...</update>
        <delete>...</delete>

    </mapper>

> Mybatis3支持多种日志框架的实现，只需要将日志实现的`Jar`文件放到`CLASSPATH`下并且定义好配置文件即可。这里使用`LOG4J`日志输出(`log4j.properties`)：

    # Global logging configuration
    #日志要输出的级别和输出的位置
    log4j.rootLogger=DEBUG, stdout
    # Console output...
    log4j.appender.stdout=org.apache.log4j.ConsoleAppender
    log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
    log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n

> Mybatis3配置文件(`mybatis-conf.xml`，名称可以任意)，配置数据源、事务、Mapper依赖等:

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>

        <!-- 如果CLASSPATH中多种LogImpl，可以在这里指定使用的日志类型 -->
        <!--<settings>
            <setting name="logImpl" value="LOG4J"/>
        </settings>-->

        <!-- 和spring整合后 environments配置将废除-->
        <environments default="development">
            <environment id="development">
                <!-- 使用jdbc事务管理，事务控制由mybatis-->
                <transactionManager type="JDBC" />
                <!-- 数据库连接池,由mybatis管理-->
                <dataSource type="POOLED">
                    <property name="driver" value="com.mysql.jdbc.Driver" />
                    <property name="url" value="jdbc:mysql://172.16.193.14:3306/mybatis?characterEncoding=utf-8" />
                    <property name="username" value="root" />
                    <property name="password" value="root" />
                </dataSource>
            </environment>
        </environments>
        

        <mappers>
            <mapper resource="sqlmap/UserMapper.xml"></mapper>
        </mappers>

    </configuration>

<h3 id="2.2">DAO接口方式使用Mybatis3</h3>

> 首先建一个要映射的数据库(Mysql)表，表主键ID采用自增的模式:

    CREATE TABLE `user` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `username` varchar(255) DEFAULT NULL,
      `sex` varchar(255) DEFAULT NULL,
      `birthday` date DEFAULT NULL,
      `address` varchar(255) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

> 有表了，既然是映射肯定需要一个类与表相对应(`User.java`)：

    package com.fmz.mybatis.pojo;

    import java.util.Date;

    public class User {

        private int id;
        private String username;
        private String sex;
        private Date birthday;
        private String address;

        //setter & getter method omit
    }

> 建最核心的`Mapper`文件:

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <!-- namespace 命名空间，作用就是对sql进行分类化管理,理解为sql隔离
     注意：使用mapper代理方法开发，namespace有特殊重要的作用
     -->
    <mapper namespace="test">
        <!-- 在映射文件中配置很多sql语句 -->
        <!--需求:通过id查询用户表的记录 -->
        <!-- 通过select执行数据库查询
         id:标识映射文件中的sql，称为statement的id
         将sql语句封装到mappedStatement对象中，所以将id称为statement的id
         parameterType:指定输入参数的类型
         #{}标示一个占位符,
         #{value}其中value表示接收输入参数的名称，如果输入参数是简单类型，那么#{}中的值可以任意。

         resultType：指定sql输出结果的映射的java对象类型，select指定resultType表示将单条记录映射成java对象
         -->
        <select id="findUserById" parameterType="int" resultType="com.fmz.mybatis.pojo.User">
            SELECT * FROM  user  WHERE id=#{value}
        </select>

        <!-- 根据用户名称模糊查询用户信息，可能返回多条
            resultType：指定就是单条记录所映射的java对象类型
            ${}:表示拼接sql串，将接收到参数的内容不加任何修饰拼接在sql中。
            使用${}拼接sql，引起 sql注入
            ${value}：接收输入参数的内容，如果传入类型是简单类型，${}中只能使用value
         -->
        <select id="findUserByName" parameterType="java.lang.String" resultType="com.fmz.mybatis.pojo.User">
            SELECT * FROM user WHERE username LIKE '%${value}%'
        </select>


        <!-- 添加用户
               parameterType：指定输入 参数类型是pojo（包括 用户信息）
               #{}中指定pojo的属性名，接收到pojo对象的属性值，mybatis通过OGNL获取对象的属性值
               -->
        <insert id="insertUser" parameterType="com.fmz.mybatis.pojo.User">
            <!--
             将插入数据的主键返回，返回到user对象中

             SELECT LAST_INSERT_ID()：得到刚insert进去记录的主键值，只适用与自增主键

             keyProperty：将查询到主键值设置到parameterType指定的对象的哪个属性(自已获取自增主键id并赋值给USer#id)
             order：SELECT LAST_INSERT_ID()执行顺序，相对于insert语句来说它的执行顺序
             resultType：指定SELECT LAST_INSERT_ID()的结果类型
              -->
            <selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer">
                SELECT LAST_INSERT_ID()
            </selectKey>
            INSERT INTO user (username,birthday,sex,address)values (#{username},#{birthday},#{sex},#{address})
            <!--
                使用mysql的uuid（）生成主键
                执行过程：
                首先通过uuid()得到主键，将主键设置到user对象的id属性中
                其次在insert执行时，从user对象中取出id属性值
                 -->
            <!--  <selectKey keyProperty="id" order="BEFORE" resultType="java.lang.String">
                SELECT uuid()
            </selectKey>
            insert into user(id,username,birthday,sex,address) value(#{id},#{username},#{birthday},#{sex},#{address}) -->

        </insert>

        <!-- 删除 用户
            根据id删除用户，需要输入 id值
             -->
        <delete id="deleteUser" parameterType="java.lang.Integer">
            delete from user where id=#{id}
        </delete>

        <!-- 根据id更新用户
        分析：
        需要传入用户的id
        需要传入用户的更新信息
        parameterType指定user对象，包括 id和更新信息，注意：id必须存在
        #{id}：从输入 user对象中获取id属性值
         -->
        <update id="updateUser" parameterType="com.fmz.mybatis.pojo.User">
            update user set username=#{username},birthday=#{birthday},sex=#{sex},address=#{address}
            where id=#{id}
        </update>

    </mapper>

> 为了方便起见，不建`Dao`接口，直接使用`DaoImpl`:

    package com.fmz.mybatis.dao;

    import com.fmz.mybatis.pojo.User;
    import org.apache.ibatis.session.SqlSession;
    import org.apache.ibatis.session.SqlSessionFactory;
    import org.apache.ibatis.session.SqlSessionFactoryBuilder;

    import java.io.InputStream;
    import java.sql.Date;
    import java.util.List;

    public class UserDao{

        public void findUserById() throws Exception {

            String conf = "mybatis-conf.xml";
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(conf);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            SqlSession sqlSession = sqlSessionFactory.openSession();
            User u = sqlSession.selectOne("test.findUserById", 2);
            System.out.println(u);
        
        }

        public void findUserByName() throws Exception {

            String conf = "mybatis-conf.xml";
            //InputStream is = this.getClass().getResourceAsStream("/mybatis-conf.xml");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(conf);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            SqlSession sqlSession = sqlSessionFactory.openSession();
            List<User> u = sqlSession.selectList("test.findUserByName", "王小军");
            System.out.println(u);
        
        }

        public void insertUser() throws Exception {

            String conf = "mybatis-conf.xml";
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(conf);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);

            User user = new User();
            user.setUsername("fmz");
            user.setSex("28");
            user.setBirthday(Date.valueOf("1990-07-19"));
            user.setAddress("海淀区-中关村");
            sqlSession.insert("test.insertUser", user);
            System.out.println(user);
        
        }

    }

> 注意：`Class.getResourceAsStream(String path)` vs `ClassLoader.getResourceAsStream(String path)`是有区别的：<br>
- `Class.getResourceAsStream(String path)`中的`path`可以是相对路径(相对于当前包的路径)或者是绝对路径;
- `ClassLoader.getResourceAsStream(String path)`中的`path`是相对于`CLASSPATH`根目录的路径;
- 因此上述读取配置文件的路径也可以写为：`InputStream is = this.get().getResourceAsStream("/mybatis-conf.xml");`.

> 总结：<br><br>
- `Dao`接口的方式使用Mybatis3实际上是：`Dao` + `DaoImpl` + `Mapper.xml`;
- `DaoImpl`中每一个方法需要获取`SqlSession`，通过`SqlSession`和`Mapper.xml`的配置进行相应的映射，这种方法的`Mapper.xml`中的`namespace`只是起到了隔离文件的目的(两个不同的文件同时定义了`id=adduser`，能够通过`namespace`将二者区分开来);

<h3 id="2.3">Mapper代理的方式使用Mybatis3</h3>

> 在`2.2`部分中数据库表和对应的pojo已经建成功了，由于使用的是`Mapper`动态代理的方法，所以`Mapper.xml`中的`namespace`不仅仅作为隔离sql文件使用，而且还要与对用的`Mapper.java`接口类路径一致;<br>
除此之外，`Mapper.xml`和`Mapper.java`中定义的`id`与方法名称、`parameterType`与方法参数类型、`resultType`与方法参数返回值类型要保持一致:

    <!-- namespace要和响应的Mapper接口类路径保持一致 -->
    <mapper namespace="com.fmz.mybatis.dao.UserMapper">
    <!-- id、parameterType、resultType要和对应的Mapper接口对应的类型保持一致 -->
    <select id="findUserById" parameterType="int" resultType="com.fmz.mybatis.pojo.User">
        SELECT * FROM  user  WHERE id=#{value}
    </select>

> 定义Mapper接口`UserMapper.java`:

    package com.fmz.mybatis.dao;

    import com.fmz.mybatis.pojo.User;

    import java.util.List;

    public interface UserMapper {

        public User findUserById(int id);

        public List<User> findUserByName(String name);

        public void insertUser(User user);

        public void deleteUser(int id);

        public int updateUser(User user);

    }

> 总结：<br><br>
- `Mapper`接口的方式使用Mybatis3实际上是：`*Mapper.java` + `*Mapper.xml`(二者的文件名不一定要保持一致);
- `Mapper`代理接口的方式采用动态代理的方法动态生成实现的接口，使得开发更加灵活。

<h5 id="2.3.1">Mapper代理注解SQL(@select)的方式使用Mybatis3</h5>

> Mapper类：

    package com.fmz.mybatis.dao;

    import com.fmz.mybatis.pojo.User;
    import org.apache.ibatis.annotations.Delete;
    import org.apache.ibatis.annotations.MapKey;
    import org.apache.ibatis.annotations.Select;
    import org.apache.ibatis.annotations.Update;

    import java.util.HashMap;
    import java.util.List;

    public interface UserMapperAnnotation {

        @Select("SELECT * FROM  user  WHERE id=#{id}")
        public User findUserById(int id);

        @Select("SELECT * FROM user WHERE username LIKE '%${value}%'")
        public List<User> findUserByName(String name);

        //public void insertUser(User user);

        @Delete("delete from user where id=#{id}")
        public void deleteUser(int id);

        @Update("update user set username=#{username},birthday=#{birthday},sex=#{sex},address=#{address} where id=#{id}")
        public int updateUser(User user);

        @Select("select id, username from user")
        @MapKey("id")
        public HashMap<Integer, User> idNameMap();
    }

> 需要将这个类(`UserMapperAnnotation`)注册到`MapperRegister`中(在`mybatis-conf.xml`中进行配置)：

    <mappers>
        <mapper class="com.fmz.mybatis.dao.UserMapperAnnotation"></mapper>
    </mappers>

> 这种方式直接将SQL语句写在了Java类中，如果SQL语句发生变化需要重新编译，并且功能上有限制。

---

<h3 id="3">3. Mybatis使用问题收集</h3>

<h4 id="3.1">3.1 动态SQL Where条件中使用test判断报错: There is no getter for property named 'xxx' in 'class java.lang.String'</h4>

场景是：在`Mapper.xml`中where里想动态生成SQL：

    ...

    <!-- 判断netId不为空时 -->
    <if test="netId == ''">
        AND table.column = #{netId}
    </if>

    ...

> 其中`parameterType='String'`，这样就会报错：`There is no getter for property named 'xxx' in 'class java.lang.String`。

解决办法：

    ...

    <!-- 判断netId不为空时 -->
    <if test="_parameter == ''">
        AND table.column = #{netId}
    </if>

    ...

---

<h4 id="3.2">3.2 使用$和使用#来占位变量的区别</h4>

- `#{}`会进行预编译和进行数据类型的匹配
- `${}`不会进行数据类型的匹配

示例：变量name的类型是`String`，值是"张三"

> - `${name}` --> `name='张三'`
- `#{name}` --> `name=张三`
如果使用`#{}`得到的结果是是`name='张三'`；<br>
使用`${}`得到的结果是`name=张三`；<br>
如果要得到字符串`'张三'`，需要使用`'${name}'`

原因是:`#{}`会对SQL进行预编译，转化为`PreparedStatement`的占位符(?)的形式并进行数据类型的匹配；而`${}`只是进行简单的字符串拼接。


<h4 id="3.3">3.3 需要从SQL的执行结果封装为Map集合返回</h4>

比如说一张表中有`code`和`name`两个字段，希望通过将表中的`code`作为key、`name`作为value封装为一个Map返回。

有两种方法，第一种方法可以通过`Mapper + 注解(@MapKey)`的方式实现，另一种方式是写一个`ResultHandler`的实现。

**Mapper + 注解(@MapKey)的方式:**

> Mapper接口中的方法：

    @Select("select id, username from user")
    @MapKey("id")
    public HashMap<Integer, User> idNameMap();

这中方法必须通过一个字段(`id`)和pojo(`User`)映射起来，放回的结果是:`{"id1": "1232@User", "id2": "1234@User",...}`

**实现ResultHandler的方式:**

> `ResultHandler的实现类：`

    package com.fmz.mybatis.util;

    import org.apache.ibatis.session.ResultContext;
    import org.apache.ibatis.session.ResultHandler;

    import java.util.HashMap;
    import java.util.Map;

    public class UserResultHandler implements ResultHandler{

        Map<Integer, String> idNameMap= new HashMap<Integer, String>();

        public Map<Integer, String> getIdNameMap() {
            return idNameMap;
        }

        @Override
        public void handleResult(ResultContext resultContext) {

            Map<String, Object> m = (Map<String, Object>)resultContext.getResultObject();
            idNameMap.put((Integer)getFromMap(m, "id"), (String)getFromMap(m, "username"));
        }

        private Object getFromMap(Map<String, Object> map, String key){
            if(map.containsKey(key.toLowerCase())){
                return map.get(key.toLowerCase());
            }else{
                return map.get(key.toUpperCase());
            }
        }
    }

> 使用dao接口的方式进行调用：

    public void getIdNameMap() throws Exception {
        String conf = "mybatis-conf.xml";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(conf);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserResultHandler userResultHandler = new UserResultHandler();
        sqlSession.select("com.fmz.mybatis.dao.UserMapper.idNameMap", userResultHandler);
        Map<Integer, String> result = userResultHandler.getIdNameMap();
        System.out.println(result);
    }

这个方式可以将两个字段中的一个作为key，另一个作为value来返回Map，返回的格式是：`{"id1": "name1", "id2": "name2",...}`

<h4 id="3.4">3.4 mybatis自定义typehandler改变字段数据类型</h4>

两种方法：

**1. 自定义`typeHandler`**

```	java
public class StrToIntTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(PreparedStatement ps, int i,
                             String parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, Integer.parseInt(parameter));
    }

    @Override
    public String getResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.getString(s);
    }

    @Override
    public String getResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString(i);
    }

    @Override
    public String getResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getString(i);
    }
}
```

**2. `OGNL`表达式**

```java
<bind name="param" value="@java.lang.Integer@valueOf(jobXXX)" />
  //...
 where  #{rmi_auftrag_xxx} = #{param}
或者
 where #{rmi_auftrag_xxx} = ${@java.lang.Integer@valueOf(jobXXX)}
```

<h4 id="3.5">3.5 mybatis自定typehandler数据库array类型映射为pojo数组</h4>

代码如下：

```java
// 继承自BaseTypeHandler<Object[]> 使用时传入的参数一定要是Object[]，例如 int[]是 Object, 不是Object[]，所以传入int[] 会报错的
public class ArrayTypeHandler extends BaseTypeHandler<Object[]> {

    private static final String TYPE_NAME_VARCHAR = "varchar";
    private static final String TYPE_NAME_INTEGER = "integer";
    private static final String TYPE_NAME_BOOLEAN = "boolean";
    private static final String TYPE_NAME_NUMERIC = "numeric";
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object[] parameter,
            JdbcType jdbcType) throws SQLException {
        
        String typeName = null;
        if (parameter instanceof Integer[]) {
            typeName = TYPE_NAME_INTEGER;
        } else if (parameter instanceof String[]) {
            typeName = TYPE_NAME_VARCHAR;
        } else if (parameter instanceof Boolean[]) {
            typeName = TYPE_NAME_BOOLEAN;
        } else if (parameter instanceof Double[]) {
            typeName = TYPE_NAME_NUMERIC;
        }
        
        if (typeName == null) {
            throw new TypeException("ArrayTypeHandler parameter typeName error, your type is " + parameter.getClass().getName());
        }
        
        // 这3行是关键的代码，创建Array，然后ps.setArray(i, array)就可以了
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(typeName, parameter);
        ps.setArray(i, array);
    }

    @Override
    public Object[] getNullableResult(ResultSet rs, String columnName)
            throws SQLException {

        return getArray(rs.getArray(columnName));
    }

    @Override
    public Object[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        return getArray(rs.getArray(columnIndex));
    }

    @Override
    public Object[] getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {

        return getArray(cs.getArray(columnIndex));
    }
    
    private Object[] getArray(Array array) {
        
        if (array == null) {
            return null;
        }

        try {
            return (Object[]) array.getArray();
        } catch (Exception e) {
        }
        
        return null;
    }
}
```

<h4 id="3.6">3.6 mybatis将sql结果集部分字段转化为hashmap类型</h4>

问题：自己定义或者第三方的pojo类型中有扩展字段，类型是hashmap。想要将sql中查询结果集部分字段映射为hashmap。

映射的pojo类代码：

```java
public class TreeNode {

	/**
	 * 节点的id，唯一
	 *
	private String id;

	/**
	 * 节点的显示名称
	 */
	private String name;

	/**
	 * 自定义配置
	 */
	private Map<String, Object> cfg = new HashMap<String, Object>();
}
```

mybaits映射的mapper内容：

```shell
<select id="loadAllDwTree" resultMap="query_treeNode">
    select c_id, c_name, c_ssdw from table
</select>

<resultMap id="query_treeNode" type="TreeNode">
    <id column="c_id" property="id" />
    <result column="c_name" property="name" />
    <association property="cfg" resultMap="cfgMapper" />
<resultMap>

<resultMap id="cfgMapper">
    <result column="c_ssdw" property="ssdw" />
</resultMap>
```

这样查询，就能得到TreeNode的映射，对应pojo的json结果：

```json
{
    "id":"xxx",
    "name": "xxxx",
    "cfg": {"ssdw": "xxx"}
}
```

这样就得到了将sql结果集中部分字段（这里配置一个，实际可配置多个字段）映射为pojo中的hashmap类型。

---
