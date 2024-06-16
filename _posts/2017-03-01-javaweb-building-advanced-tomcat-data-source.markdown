---
layout: post
title: javaweb开发高级篇之Tomcat数据源
---

## 目录

- [1 数据源的操作原理](#1)
	- [1.1 数据源的操作原理](#1.1)
	- [1.2 在Tomcat中使用数据库连接池](#1.2)
	- [1.3 查找数据源](#1.3)

---

---

<h2 id="1"> 1 数据源的操作原理</h2> 

JDBC的操作原理

1. 加载数据库的驱动程序，数据库的驱动程序通过CLASSPATH配置
2. 通过DriverManager类取得数据库的连接对象
3. 通过Connection实例化PreparedStatement对象，编写SQL命令操作数据库
4. 数据库数据资源操作，操作完后要关闭数据库以释放资源

<h3 id="1.1"> 1.1 数据源的操作原理</h3> 

数据源操作的核心原理是，在一个对象池中保存多个数据库的连接(也称为数据库连接池(Connection Pool))，这样以后在进行数据库操作时，直接从操作池中取出一个数据库的连接，当数据库操作完成后将此连接放回到数据库连接池中，等待其他用户继续使用。

<h3 id="1.2"> 1.2 在Tomcat中使用数据库连接池</h3> 

在web容器中，数据库的连接池都是通过数据源(javax.sql.DataSource)访问的，即可以通过javax.sql.DataSource类取得一个Connection对象，但是要想得到一个connection对象需要使用JNDI进行查找

> JNDI(Java Naming and Directory Interface，Java命名及目录接口)，是JavaEE提供的一种接口，其服务的重要功能是通过一个名称"key"查找一个对应的对象"Value"，这一设计也能体现出java的程序设计理念。

在server.xml文件中配置数据库连接池-%TOMCAT_HOME%\conf\server.xml

	<Context path="/fmz" docBase="D:\fmzwebDemo" reloadable="true"> 
		<Resource name="jdbc/mldn" 				配置一个连接池，命名为jdbc/mldn
			auth="Container"				容器负责资源的连接
			type="javax.sql.DataSource"			此数据源名称对应的类型是DataSource	
			maxActive="100" 				可以打开的最大连接数
			maxIdle="30" 					维持的最小连接数
			maxWait="10000"					用户等待的最大时间
			username="scott" 				数据库用户名
			password="tiger" 				数据库密码
			driverClassName="org.gjt.mm.mysql.Driver"	数据库驱动程序	
			url="jdbc:mysql://localhost:3306/mldn"/> 	数据库名称
	</Context> 

> 本次配置在context节点中增加了一个resource节点，表示配置的连接池选项，name表示数据源的名称，auth表示连接数据库的方法

配置Oracle数据库的连接池

	<Context path="/fmz" docBase="D:\fmzwebDemo" reloadable="true"> 
		<Resource name="jdbc/mldn" 					配置一个连接池，命名为jdbc/mldn
			auth="Container"					容器负责资源的连接
			type="javax.sql.DataSource"				此数据源名称对应的类型是DataSource	
			maxActive="100" 					可以打开的最大连接数
			maxIdle="30" 						维持的最小连接数
			maxWait="10000"						用户等待的最大时间
			username="root" 					数据库用户名
			password="mysqladmin" 					数据库密码
			driverClassName="oracle.jdbc.driver.OracleDriver"	数据库驱动程序	
			url="jdbc:oracle:thin:@localhost:1521:MLDN/> 		数据库名称
	</Context> 



	<resource-ref> 
		<description> DB Connection</description> 
		<res-ref-name> jdbc/mldn</res-ref-name> 
		<res-type> javax.sql.DataSource</res-type> 
		<res-auth> Container</res-auth> 
	</resource-ref> 

<h3 id="1.3"> 1.3 查找数据源</h3> 

数据源的操作是使用JNDI的方式进行的，必须按如下步骤：

1. 初始化名称，查找上下文：Context ctx = new InitialContext() ;
2. 通过名称查找DataSource对象，DataSource ds = (DataSource) ctx.lookup(JNDI名称) ;
3. 通过DataSource取得数据库的连接，Connection conn = ds.getConnection() ;

通过数据源取得数据库的连接-datasource.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="javax.naming.*"%> 
	<%@ page import="javax.sql.*"%> 
	<%@ page import="java.sql.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String DSNAME = "java:comp/env/jdbc/mldn" ;
				Context ctx = new InitialContext() ;
				DataSource ds = (DataSource) ctx.lookup(DSNAME) ;
				Connection conn = ds.getConnection() ;//从连接池中取出连接
			%> 
			<%=conn%> 
			<%
				conn.close() ;//表示将连接放回到连接池中
			%> 
		</body> 
	</html> 

> `String DSNAME = "java:comp/env/jdbc/mldn" ;`与server.xml文件中配置的数据库名称不一致，多出了：`java:comp/env/`，实际上是JavaEE规定的一个环境命名的上下文，主要是为了解决JNDI查找时的冲突问题。

> `java:comp/env/`环境属性不一定到处都使用，对于一些高级的服务器(WebLogic、Websphere)本身已经设置好了此属性，查找数据源的时候就不用配置，但时对于Tomcat必须要配置，否则无法找到

修改DAO中的DatabaseConnection.java为DatabaseConnectionBySource.java

	package org.fmz.mvcdemo.dbc ;

	import java.sql.- ;
	import javax.sql.- ;
	import javax.naming.- ;

	public class DatabaseConnectionBySource{
		private static final String DSNAME = "java:comp/env/jdbc/mldn" ;
		private Connection conn = null ;

		public DatabaseConnectionBySource()throws Exception{
			try{
				Context ctx = new InitialContext() ;//初始化名称查找上下文
				DataSource ds = (DataSource)ctx.lookup(DSNAME) ;//取得DataSource实例
				this.conn = ds.getConnection() ;//取得数据库的连接
			}catch(Exception e){
				throw e ;
			}
		}

		public Connection getConnection(){
			return this.conn ;
		}

		public void close()throws Exception{
			if(this.conn != null){
				try{
					this.conn.close() ;
				}catch(Exception e){
					throw e ;
				}
			}
		}
	}

> 从本程序中可以看出来，只要数据源的名称不发生改变，则数据库可以任意更换，成分体现了java的设计思想-可移植性。

---

---
