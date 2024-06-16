---
layout: post
title: javaweb开发基础篇之JavaBean
---

## 目录

- [1 JavaBean简介](#1)
- [2 在JSP中使用JavaBean](#2)
	- [2.1 web开发的标准目录结构](#2.1)
	- [2.2 使用JSP的page指令导入所需要的JavaBean](#2.2)
	- [2.3 使用`<jsp:useBean/> `指令](#2.3)
- [3 JavaBean与表单](#3)
- [4 设置属性`<jsp:setProperty/> `](#4)
- [5 取得属性`<jsp:getProperty/> `](#5)
- [6 JavaBean的保存范围](#6)
- [7 删除JavaBean](#7)
- [8 实例操作：注册验证](#8)
- [9 DAO设计模式](#9)
	- [9.1 DAO设计模式简介](#9.1)
	- [9.2 DAO开发](#9.2)
		- [9.2.1 VO类设计](#9.2.1)
		- [9.2.2 数据库连接及数据库工厂模式设计](#9.2.2)
		- [9.2.3 DAO设计模式](#9.2.3)
		- [9.2.4 测试类](#9.2.4)
	- [9.3 JSP调用DAO](#9.3)

---

---

<h2 id="1"> 1 JavaBean简介</h2> 

JavaBean是使用java语言开发的一个可重用组件，在JSP开发中可以使用JavaBean减少重复代码，使整个JSP代码开发更加简洁。

jsp与JavaBean搭配使用的优点：

1. 可以将HTML代码和java代码相分离，主要是为了日后维护的方便
2. 可以利用JavaBean的优点。将常用到的程序写成JavaBean组件，当JSP使用时，只要调用JavaBean组件来执行用户所需要的功能即可，可以节省开发的时间。

在JSP中如果使用JSP提供的JavaBean标签来操作简单类，必须满足如下开发要求：

1. 所有的类必须放在一个包中，在web中没有包的类是不存在的
2. 所有的类必须声明为 public class ，这样才能被外部访问
3. 类种所有的属性必须封装，即使用private修饰
4. 封装的属性如果需要被外部操作，必须编写对应的 setter 和 getter 方法
5. 在一个JavaBean中至少存在一个无参构造方法，此方法为JSP中标签所用

第一个JavaBean组件-SimpleBean.java

	package cn.fmz.demo ;

	public class SimpleBean{
		private String name ;
		private int age ;
		public void setName(String name){
			this.name = name ;
		}
		public void setAge(int age){
			this.age = age ;
		}
		public String getName(){
			return this.name ;
		}
		public int getAge(){
			return this.age ;
		}
		public String toString(){
			return "姓名："+this.name+"，年龄："+this.age ;
		}
	}

>  如果一个类中只包含了属性和 setter、getter，那么这种类就称为简单的JavaBean。类似的这种称呼有：POJO(Plain Ordinary Java Objects)，简单的java对象、VO(Value Object)，值对象、TO(Transfers Object)，传输对象

---

---

<h2 id="2"> 2 在JSP中使用JavaBean</h2> 

<h3 id="2.1"> 2.1 web开发的标准目录结构</h3> 

web开发的标准目录结构

`webroot/(WEB-INF/(web.xml/(lib|classes|tags))|jsp|js|css|images|index.jsp)`

web项目各个目录的作用：

| NO. | 目录或者文件名称 | 作用 |
| --- | :---: | ---: |
| 1 | webroot | 根目录，虚拟目录直接指向此文件夹，此文件夹下必然包含WEB-INF文件夹 |
| 2 | WEB-INF | 最安全的文件夹，保存各种类，第三方jar包，配置文件等 |
| 3 | web.xml | web的部署描述符 |
| 4 | classes | 保存所有的JavaBean，如果不存在可以手工创建 |
| 5 | lib | 保存所有的第三方jar文件 |
| 6 | tags | 保存所有的标签文件 |
| 7 | jsp | 存放所有的*.jsp文件，一般根据功能再创建子文件夹 |
| 8 | js | 存放所有的*.js文件 |
| 9 | css | 样式表文件的保存路径 |
| 10 | images | 存放所有图片，如*.gif、*.jpg文件 |

在WEB-INF文件夹下建立classes文件夹，写入一个简单的JavaBean程序-SimpleBean.java

	package cn.fmz.demo ;

	public class SimpleBean{
		private String name ;
		private int age ;
		public void setName(String name){
			this.name = name ;
		}
		public void setAge(int age){
			this.age = age ;
		}
		public String getName(){
			return this.name ;
		}
		public int getAge(){
			return this.age ;
		}
		public String toString(){
			return "姓名："+this.name+"，年龄："+this.age ;
		}
	}

> 在编译时，可以直接通过打包编译：`javac -d . -encoding utf-8 SimlpleBean.java`，这样可以直接将编译后的class文件放在定义的包中

<h3 id="2.2"> 2.2 使用JSP的page指令导入所需要的JavaBean</h3> 

此种方法就相当于导入了相应的class文件的包

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="cn.fmz.demo.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				SimpleBean simple = new SimpleBean() ;
				simple.setName("冯孟昭") ;
				simple.setAge(25) ;
			%> 
			<h3> <%=simple%> </h3> 
		</body> 
	</html> 

<h3 id="2.3"> 2.3 使用<jsp:useBean/> 指令</h3> 

除了import语句之外，还可以使用<jsp:useBean> JSP标签来完JSP对JavaBean的使用，代码格式如下：

`<jsp:useBean id="实例化对象的名称" scope="保存范围" class="包.类名称"/> `

> scope一共有四种属性范围：page、request、session、application。

如果修改了JavaBean，则在编译之后，需要重新启动浏览器才能完成对新的JavaBean的加载，如果想要Tomcat服务器实时更新JavaBean的修改，需要修改Tomcat配置文件conf/server.xml：`<Context path="/fmz" docBase="D:\fmzwebDemo" reloadable="true"/> `

> 注意：当真正的项目运行的时候，一定要将reloadable设置为false，因为不断的加载会使系统运行的性能比较低；Tomcat重新加载新的内容后，所有的操作都将初始化，所有设置过的session属性都将消失。

使用`<jsp:useBean/> `标签指令的时候，可以直接进行对象的实例化，在开发中带来了很多方便，其内部的实现原理是反射机制完成的，其中要求的 包.类名称 和 无参构造方法 都是反射机制的前提条件。

<h3 id="3"> 3 JavaBean与表单</h3> 

在JavaBean语法中，实际上最大的特点就是与表单的交互上

输入表单-input_bean.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="input_bean.jsp" method="post"> 
				姓名：<input type="text" name="name"> <br> 
				年龄：<input type="text" name="age"> <br> 
				<input type="submit" value="提交"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

JSP文件接收参数-input_bean.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="cn.fmz.demo.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				SimpleBean simple = new SimpleBean() ;
				simple.setName(request.getParameter("name")) ;
				simple.setAge(Integer.parseInt(request.getParameter("age"))) ;
			%> 
			<h2> <%=simple%> </h2> 
		</body> 
	</html> 

> 这样的方式，如果接收表单的参数过多的话，接收起来非常麻烦。

使用JavaBean标签来接受参数，编写JSP文件-input_bean01.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
			%> 
			<jsp:useBean id="simple" scope="page" class="cn.fmz.demo.SimpleBean"/> 
			<jsp:setProperty name="simple" property="*"/> 
			<h2> <%=simple%> </h2> 
		</body> 
	</html> 

`<jsp:setProperty name="javaBean对象" property="*"/> `，property表示要操作的属性值，*表示自动匹配(接收所有的参数)。此处的原理是：如果匹配成功，则会自动调用 setter 方法进行内容的设置。

---

---

<h2 id="4"> 4 设置属性 jsp:setProperty</h2> 

设置属性的操作

| NO. | 类型     | 语法格式                                                               |
|-----|:--------:|-----------------------------------------------------------------------:|
| 1   | 自动匹配 | jsp:setProperty name="实例化对象名称" property="*"                     |
| 2   | 指定属性 | jsp:setProperty name="实例化对象名称" property="属性名称"              |
| 3   | 指定参数 | jsp:setProperty name="实例化对象名称" property="属性名称" param="参数" |
| 4   | 指定内容 | jsp:setProperty name="实例化对象名称" property="属性名称" value="内容" |

---

---

<h2 id="5"> 5 取得属性 jsp:getProperty</h2> 

取得属性格式：

`<jsp:getProperty name="实例化对象名称" property="属性名称"/> `

此标签取得属性的原理是：自动调用JavaBean中的 getter 方法。

---

---

<h2 id="6"> 6 JavaBean的保存范围</h2> 

在`<jsp:useBean/> `标签的指令上存在一个scope属性，表示一个JavaBean的保存范围，共有四种保存范围：

1. page：保存在一个页的范围之内，跳转后此JavaBean无效
2. request：一个JavaBean可以保存在一次服务器的跳转的范围中
3. session：在一个用户的操作范围中保存，重新打开浏览器才会才生新的JavaBean
4. application：在整个服务器上保存，服务器关闭时才会消失

---

---

<h2 id="7"> 7 删除JavaBean</h2> 

如果一个JavaBean不再使用，可以使用四个属性范围的方法进行删除：

1. 删除page范围的JavaBean：pageContext.rsemoveAttribute(JavaBean 名称)
2. 删除request范围的JavaBean：request.removeAttribute(JavaBean 名称)
3. 删除session范围的JavaBean：session.removeAttribute(JavaBean 名称)
4. 删除application范围的JavaBean：application.removeAttribute(JavaBean 名称)

---

---

<h2 id="8"> 8 实例操作：注册验证</h2> 

JavaBean文件-Register.java

	package cn.fmz.demo ;
	import java.util.- ;

	public class Register{
		private String name ;
		private String age ;
		private String email ;
		private Map<String,String>  errors = null ;

		public Register(){
			this.name = "" ;
			this.age = "" ;
			this.email = "" ;
			this.errors = new HashMap<String,String> () ;
		}

		public boolean isValidate(){
			boolean flag = true ;
			if(! this.name.matches("\\w{6,15}")){
				flag = false ;
				this.name = "" ;
				errors.put("errorname","请输入6~15位字母或者数字") ;
			}
			if(! this.email.matches("\\w+@\\w+\\.\\w+\\.?\\w*")){
				flag = false ;
				this.email = "" ;
				errors.put("erroremail","输入的email地址不合法") ;
			}
			if(! this.age.matches("\\d+")){
				flag = false ;
				this.age = "" ;
				errors.put("errorage","年龄只能是数字") ;
			}
			return flag ;
		}

		public String getErrorMsg(String key){
			String value = this.errors.get(key) ;
			return value == null ? "" : value ;
		}

		public String getName(){
			return this.name ;
		}
		public String getEmail(){
			return this.email ;
		}
		public String getAge(){
			return this.age ;
		}
		public void setName(String name){
			this.name = name ;
		}
		public void setEmail(String email){
			this.email = email ;
		}
		public void setAge(String age){
			this.age = age ;
		}

		public String toString(){
			return "姓名："+this.name+"<br> 年龄："+this.age+"<br> E-mail："+this.email ;
		}
	}

表单文件-index.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%
		request.setCharacterEncoding("utf-8") ;
	%> 
	<jsp:useBean id="reg" scope="request" class="cn.fmz.demo.Register"/> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="check.jsp" method="post"> 
				姓名：<input type="text" name="name" value="<jsp:getProperty name="reg" property="name"/> "> 
				<%=reg.getErrorMsg("errorname")%> <br> 
				年龄：<input type="text" name="age" value="<jsp:getProperty name="reg" property="age"/> "> 
				<%=reg.getErrorMsg("errorage")%> <br> 
				邮箱：<input type="text" name="email" value="<jsp:getProperty name="reg" property="email"/> "> <%-- 表单中加入value属性的作用是：当只有一个属性错误时，其他属性的内容能保持不变 --%> 
				<%=reg.getErrorMsg("erroremail")%> <br> 
				<input type="submit" value="提交"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

检查页文件-check.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%
		request.setCharacterEncoding("utf-8") ;
	%> 
	<jsp:useBean id="reg" scope="request" class="cn.fmz.demo.Register"/> 
	<jsp:setProperty name="reg" property="*"/> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				if(reg.isValidate()){
			%> 
					<jsp:forward page="success.jsp"/> 
			<%
				}else{
			%> 
					<jsp:forward page="index.jsp"/> 
			<%
					}
			%> 
		</body> 
	</html> 

成功登陆文件-success.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%
		request.setCharacterEncoding("utf-8") ;
	%> 
	<jsp:useBean id="reg" scope="request" class="cn.fmz.demo.Register"/> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
		<center> 
			<h2> 
			<%--
				姓名：<jsp:getProperty name="reg" property="name"/> <br> 
				年龄：<jsp:getProperty name="reg" property="age"/> <br> 
				E-mail：<jsp:getProperty name="reg" property="email"/> <br> 
			--%> 
				<%=reg%> 
			</h2> 
		</center> 
		
		</body> 
	</html> 

---

---

<h2 id="9"> 9 DAO设计模式</h2> 

DAO(Data Access Object，数据访问对象)的主要功能是数据操作，在程序的标砖开发构架中属于数据层的操作。

<h3 id="9.1"> 9.1 DAO设计模式简介</h3> 

程序的标准开发框架

客户层(Client) -->  显示层(JSP/Servlet) -->  业务层(BO) -->  数据层(DAO) -->  资源层(DataBase)

客户层：因为现在都使用B/S开发框架，所以一般使用浏览器进行访问。

显示层：使用JSP/Servlet进行页面效果的显示。

业务层(Bussiness Object)：会将多个原子性的DAO操作进行组合，组成完整的一个业务逻辑。

数据层(DAO)：提供多个原子性的DAO操作，如增加、修改、删除等都属于原子性操作。

DAO由以下几个部分组成：

DatabaseConnction：专门负责数据库的打开与关闭操作。

VO：主要由属性、setter、getter方法组成，VO类中的属性与表中的字段相对应，每一个VO类的对象都表示表中的每一条记录。

DAO：主要定义操作的接口，定义一系列数据库的原子性操作，如增加、修改、删除、按ID查询等。

Impl：DAO接口的真实实现类，完成具体的数据库操作，但是不负责数据的打开和关闭。

Proxy：代理实现类，主要完成数据库的打开和关闭，并且调用真实实现类对象的操作。

Factory：工厂类，通过工厂取得一个DAO的实例化对象。

包的命名规范：

数据库连接：xxx.dbc.DatabaseConnection。

DAO接口：xxx.dao.XxxDAO。

DAO接口的真实实现类：xxx.dao.impl.XxxDAOImpl。

DAO接口的代理实现类：xxx.dao.proxy.XxxDAOProxy。

VO类：xxx.vo.Xxx。VO的命名要与表的命名一致。

工厂类：xxx.factory.DAOFactory。

<h3 id="9.2"> 9.2 DAO开发</h3> 

DAO开发完全是围绕着数据操作进行的。

<h4 id="9.2.1"> 9.2.1 VO类设计</h4> 

定义对应的VO类-Emp.java

	package cn.fmz.vo ;
	import java.util.Date ;
	public class Emp{
		private int empno ;
		private String ename ;
		private String job ;
		private Date hiredate ;
		private float sal ;

		public void setEmpno(int empno){
			this.empno = empno ;
		}
		public void setEname(String ename){
			this.ename = ename ;
		}
		public void setJob(String job){
			this.job = job ;
		}
		public void setHiredate(Date date){
			this.hiredate = date ;
		}
		public void setSal(Float sal){
			this.sal = sal ;
		}
		public int getEmpno(){
			return this.empno ;
		}
		public String getEname(){
			return this.ename ;
		}
		public String getJob(){
			return this.job ;
		}
		public Date getHiredate(){
			return this.hiredate ;
		}
		public float getSal(){
			return this.sal ;
		}

		public String toString(){
			return "雇员编号："+this.empno+"，雇员姓名："+this.ename+"，雇员工作"+this.job+"，雇佣日期："+this.hiredate+"，雇员工资："+this.sal ;
		}
	}

> 每一个VO类的对象，表示表中的一条记录，包括属性和一系列 getter 和 setter 方法，注意：此处使用的日期格式是 java.util.Date 类。

<h4 id="9.2.2"> 9.2.2 数据库连接及数据库工厂模式设计</h4> 

定义数据库连接

> 数据库为了实现在不同的数据库下都能连接成功，需要创建接口和工厂模式进行取得。

1. 定义数据库接口-IDatabaseConnection.java

	package cn.fmz.dbc ;

	import java.sql.- ;

	public interface IDatabaseConnection{

		public Connection getConnection() throws Exception ;

		public void close() throws Exception;
	}

2. 定义 MySQL 数据库的连接实现类-MySQLDatabaseConnection.java

	package cn.fmz.dbc.impl ;

	import java.sql.- ;
	import cn.fmz.dbc.IDatabaseConnection ;

	public class MySQLDatabaseConnection implements IDatabaseConnection{

		public static final String DBDRIVER = "org.gjt.mm.mysql.Driver" ;
		public static final String DBURL = "jdbc:mysql://localhost:3306/mldn" ;
		public static final String DBUSER = "root" ;
		public static final String DBPASSWORD = "mysqladmin" ;
		public static Connection conn = null ;

		public MySQLDatabaseConnection()throws Exception{
			try{
				Class.forName(DBDRIVER) ;
				this.conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD) ;
			}catch(Exception e){
				throw e ;
			}
		}

		public Connection getConnection()throws Exception{
			try{
				return this.conn ;
			}catch(Exception e){
				throw e ;
			}
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

3. 定义数据库连接工厂管理器-DatabaseConnectionFactoryManager.java

	package cn.fmz.factory ;

	import cn.fmz.dbc.- ;
	import cn.fmz.dbc.impl.- ;
	import cn.fmz.exception.- ;

	public class DatabaseConnectionFactoryManager{
		public static IDatabaseConnection getDatabaseConnectionFactory(String DB)throws Exception{
			
			if(DB.equalsIgnoreCase("MySQL")){
				try{
					return new MySQLDatabaseConnection() ;
				}catch(Exception e){
					throw e ;
				}
			}else{
				throw new ErrorSQLConnectionException("Error SQL request") ;
			}
			/*
			if(DB.equalsIgnoreCase("SQL Server")){
				return new SQLServerDatabaseConnection() ;
			}else{
				throw new ErrorSQLConnectionException("Error SQL request") ;
			}

			if(DB.equalsIgnoreCase("Oracle")){
				return new OracleDatabaseConnection() ;
			}else{
				throw new ErrorSQLConnectionException("Error SQL request") ;
			}
			*/
		}
	}

> 数据库连接工厂管理器里面定义了工厂方法：`getDatabaseConnectionFactory(String DB)`，通过此方法可以取得不同数据库的连接，当参数输入不正确时会抛出异常。

> 注意：放回的类型的接口的类型，这是java程序设计语言对象多态性的设计模式。

4. 数据库连接错误异常抛出类-ErrorSQLConnectionException.java

	package cn.fmz.exception ;

	public class ErrorSQLConnectionException extends Exception{ 
		public ErrorSQLConnectionException(String str){
			super(str) ;
		}
	}

通过以上4个接口和类，可以取得不同的数据库连接：`DatabaseConnectionFactoryManager.getDatabaseConnectionFactory("mysql")`方法会返回一个`new MySQLDatabaseConnection() ;`，放回类型为：`IDatabaseConnection`，通过自动向上转型完成子类对父类接口的实例化，体现出了JAVA语言的对象的多态性。

<h4 id="9.2.3"> 9.2.3 DAO设计模式</h4> 

在DAO设计模式中，最重要的就是定义DAO接口，在进行DAO接口定义之前必须对业务进行详细的分析，要清楚知道一张表在一个系统之中应该具备何种功能。

1. 定义DAO操作接口-IEmpDAO.java

	package cn.fmz.dao ;

	import java.util.- ;
	import cn.fmz.vo.- ;

	public interface IEmpDAO{

		/**
		  - 数据的增加操作，命名规范为：doXxx
		  - @param emp 要增加的数据对象
		  - @return 是否增加成功的标记
		  - @throws Exception 有异常交给别调用处处理
		  */
		public boolean doCreate(Emp emp) throws Exception ;

		/**
		  - 查询全部的数据，命名规范：findXxx
		  - @param keyWord 查询关键字
		  - @return 返回全都的查询结果，每一个Emp对象表示表的一行记录
		  - @throws Exception 有异常交给别调用处处理
		  */
		public List<Emp>  findAll(String keyWord)throws Exception ;

		/**
		  - 根据雇员标号查询雇员信息，
		  - @List<Emp> (retutn type) 返回多条查询结果
		  - @param empno 雇员编号
		  - @return 雇员的VO对象
		  - @throws Exception 有异常交给别调用处处理
		  */
		public Emp findById(int empo)throws Exception ;

		/**
		  *判断雇员在数据库中是否存在
		  - @param Emp 要查询的数据对象
		  - @return 范围布尔值，存在，返回true；不存在，返回false
		  - @throws Exception 有异常交给别调用处处理
		  */
		public boolean exists(Emp emp) throws Exception ;
		
	}

DAO接口完成之后要定义具体的实现类，一种是真实主题实现类，另一种是代理操作类，真实主题负责数据库的具体操作，代理主题负责数据库的关闭操作。

2. 真实主题实现类-EmpDAOImpl.java

	package cn.fmz.dao.impl ;

	import java.sql.- ;
	import java.util.- ;
	import cn.fmz.dao.- ; 
	import cn.fmz.vo.- ;

	public class EmpDAOImpl implements IEmpDAO{
		private Connection conn = null ;
		private PreparedStatement pstmt = null ;
		public EmpDAOImpl(Connection conn){
			this.conn = conn ;
		}

		public boolean doCreate(Emp emp) throws Exception{
			boolean flag = false ;
			String sql = "INSERT INTO emp(empno,ename,job,hiredate,sal) VALUES(?,?,?,?,?)" ;
			this.pstmt = this.conn.prepareStatement(sql) ;
			this.pstmt.setInt(1,emp.getEmpno()) ;
			this.pstmt.setString(2,emp.getEname()) ;
			this.pstmt.setString(3,emp.getJob()) ;
			this.pstmt.setDate(4,new java.sql.Date(emp.getHiredate().getTime())) ;
			this.pstmt.setFloat(5,emp.getSal()) ;
			if(this.pstmt.executeUpdate() >  0){
				flag = true ;
			}
			this.pstmt.close() ;
			return flag ;
		}

		public List<Emp>  findAll(String keyWord)throws Exception{
			List<Emp>  list = new ArrayList<Emp> () ;
			String sql = "SELECT empno,ename,job,hiredate,sal FROM emp WHERE ename LIKE ? OR job LIKE ?" ;
			this.pstmt = this.conn.prepareStatement(sql) ;
			this.pstmt.setString(1,"%"+keyWord+"%") ;//百分号是模糊查询的作用
			this.pstmt.setString(2,"%"+keyWord+"%") ;
			ResultSet rs = this.pstmt.executeQuery() ;
			Emp emp = null ;
			while(rs.next()){
				emp = new Emp() ;
				emp.setEmpno(rs.getInt(1)) ;
				emp.setEname(rs.getString(2)) ;
				emp.setJob(rs.getString(3)) ;
				emp.setHiredate(rs.getDate(4)) ;
				emp.setSal(rs.getFloat(5)) ;
				list.add(emp) ;
			}
			this.pstmt.close() ;
			return list ;
		}

		public Emp findById(int empno)throws Exception{
			String sql = "SELECT empno,ename,job,hiredate,sal FROM emp WHERE empno LIKE ?" ;
			this.pstmt = this.conn.prepareStatement(sql) ;
			this.pstmt.setInt(1,empno) ;
			ResultSet rs = this.pstmt.executeQuery() ;
			Emp emp = null ;
			while(rs.next()){
				emp = new Emp() ;
				emp.setEmpno(rs.getInt(1)) ;
				emp.setEname(rs.getString(2)) ;
				emp.setJob(rs.getString(3)) ;
				emp.setHiredate(rs.getDate(4)) ;
				emp.setSal(rs.getFloat(5)) ;
			}
			this.pstmt.close() ;
			return emp ;
		}

		public boolean exists(Emp emp)throws Exception{
			return  this.findById(emp.getEmpno()) == null ? false : true ;
		}
	}

3. 代理主题实现类-EmpDAOProxy.java

	package cn.fmz.dao.proxy ;

	import java.util.- ;
	import cn.fmz.dao.- ;
	import cn.fmz.dao.impl.- ;
	import cn.fmz.dbc.IDatabaseConnection ;
	import cn.fmz.factory.- ;
	import cn.fmz.vo.- ;

	public class EmpDAOProxy implements IEmpDAO{
		//private DatabaseConnection dbconn =	null ;
		//private MySQLDatabaseConnection msql_dbconn = null ;
		private IDatabaseConnection msql_dbconn = null ;
		private IEmpDAO dao = null ;
		public EmpDAOProxy()throws Exception{
			this.msql_dbconn = DatabaseConnectionFactoryManager.getDatabaseConnectionFactory("mysql") ;
			this.dao = new EmpDAOImpl(this.msql_dbconn.getConnection()) ;
		}

		public boolean doCreate(Emp emp)throws Exception{
			boolean flag = false ;
			try{
				if(this.dao.findById(emp.getEmpno()) == null){
					flag = this.dao.doCreate(emp) ;
				}
			
			}catch(Exception e){
				throw e ;
			}finally{
				this.msql_dbconn.close() ;
			}
			return flag ;
		}

		public List<Emp>  findAll(String keyWord)throws Exception{
			List<Emp>  list = null ;
			try{
				list = this.dao.findAll(keyWord) ;
			}catch(Exception e){
				throw e ;
			}finally{
				this.msql_dbconn.close() ;
			}
			return list ;
		}

		public Emp findById(int empno)throws Exception{
			Emp emp = null ;
			try{
				emp = this.dao.findById(empno) ;
			}catch(Exception e){
				throw e ;
			}finally{
				this.msql_dbconn.close() ;
			}
			return emp ;
		}

		public boolean exists(Emp emp)throws Exception{
			boolean flag = false ;
			try{
				flag = this.dao.exists(emp) ;
			}catch(Exception e){
				throw e ;
			}finally{
				this.msql_dbconn.close() ;
			}
			return flag ;
		}
	}

> 可以发现：在代理类的构造方法中实例化了数据库连接类的对象以及真实主题的实现类，而在代理类的各个方法中知识调用了真实主题类的相应方法。

> 可以在代理类中加入事务控制。

> 在这里之所以要加入代理类，主要是为以后进行更复杂的业务准备的，这样可以有效的避免程序的耦合度过高的问题，在简单的代码中作用并不明显。

> > 如果一个程序可以由A--> B，那么中间最好加一个过渡，使用A--> C--> B的形式，目的是避免程序的耦合度过高。

4. DAO工厂类-DAOFactory.java

	package cn.fmz.factory ;

	import cn.fmz.dao.- ;
	import cn.fmz.dao.proxy.- ;

	public class DAOFactory{
		public static IEmpDAO getIEmpDAOInstance()throws Exception{
			return new EmpDAOProxy() ;
		}
	}

> 注意：返回对象为中间代理类(中间代理类实例化了具体实现类的对象，并且具有对应的处理-此处为数据库的关闭操作)，返回类型为接口类，通过自动向上转型体现Java程序设计语言的对象多态性。

<h4 id="9.2.4"> 9.2.4 测试类</h4> 

1. TestDAOInset.java

	package cn.fmz.test ;

	import cn.fmz.factory.- ;
	import cn.fmz.vo.- ;

	public class TestDAOInsert{
		public static void main(String args[])throws Exception{
			Emp emp = null ;
			/*
			for(int x=0;x<100;x++){
				emp = new Emp() ;
				emp.setEmpno(100+x) ;
				emp.setEname("陈亮亮-"+x) ;
				emp.setJob("人力资源专员-"+x) ;
				emp.setHiredate(new java.util.Date()) ;
				emp.setSal((float)(500.0 - x)) ;
				DAOFactory.getIEmpDAOInstance().doCreate(emp) ;
			}
			*/

			emp = new Emp() ;
			emp.setEmpno(0) ;
			emp.setEname("陈亮亮") ;
			emp.setJob("人力资源总监") ;
			emp.setHiredate(new java.util.Date()) ;
			emp.setSal(20000F) ;
			DAOFactory.getIEmpDAOInstance().doCreate(emp) ;
		}
	}

2. TestDAOFindAll.java

	package cn.fmz.test ;

	import java.util.- ;
	import cn.fmz.factory.- ;
	import cn.fmz.vo.- ;

	public class TestDAOFindAll{
		public static void main(String args[])throws Exception{
			List<Emp>  list = null ;
			list = DAOFactory.getIEmpDAOInstance().findAll("冯孟昭") ;

			Iterator<Emp>  itr = list.iterator() ;
			while(itr.hasNext()){
				System.out.println(itr.next()) ;
			}
		}
	}

3. TestDAOFindById.java

	package cn.fmz.test ;

	import cn.fmz.factory.- ;
	import cn.fmz.vo.- ;

	public class TestDAOFindById{
		public static void main(String args[])throws Exception{
			Emp emp = null ;
			emp = DAOFactory.getIEmpDAOInstance().findById(44444) ;
			
			System.out.println(emp) ;
		}
	}

<h3 id="9.3"> 9.3 JSP调用DAO</h3> 

增加雇员-emp_insert.jsp、emp_insert_do.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="emp_insert_do.jsp" method="post"> 
				雇员编号：<input type="text" name="empno"> <br> 
				雇员姓名：<input type="text" name="ename"> <br> 
				雇员工作：<input type="text" name="job"> <br> 
				雇用日期：<input type="text" name="hiredate"> <br> 
				雇员工资：<input type="text" name="sal"> <br> 
				<input type="submit" value="提交"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

表单jsp文件

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="cn.fmz.factory.*,cn.fmz.vo.*"%> 
	<%@ page import="java.text.*"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				Emp emp = new Emp() ;
				emp.setEmpno(Integer.parseInt(request.getParameter("empno"))) ;
				emp.setEname(request.getParameter("ename")) ;
				emp.setJob(request.getParameter("job")) ;
				emp.setHiredate(new SimpleDateFormat("yyy-MM-dd").parse(request.getParameter("hiredate"))) ;
				emp.setSal(Float.parseFloat(request.getParameter("sal"))) ;
				try{
					if(DAOFactory.getIEmpDAOInstance().doCreate(emp)){
			%> 
						<h3> 雇员信息添加成功！</h3> 
			<%
					}else{
			%> 
						<h3> 雇员信息添加失败！</h3> 
			<%
				}
			}catch(Exception e){
				e.printStackTrace() ;
			}
			%> 
		</body> 
	</html> 

> 信息添加文件

> 注意：日期的格式要从字符串转换为java.util.Date，转变方法为`new SimpleDateFormat("yyy-MM-dd").parse(request.getParameter("hiredate"))`

雇员信息模糊查询-emp_list_FuzzyQuery.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="cn.fmz.vo.*,cn.fmz.factory.*"%> 
	<%@ page import="java.util.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;

				try{
					String keyWord = request.getParameter("kw") ;
					if(keyWord == null){
						keyWord = "" ;
					}
					List<Emp>  all = DAOFactory.getIEmpDAOInstance().findAll(keyWord) ; 
			%> 
				<center> 
					<form action="emp_list_FuzzyQuery.jsp" method="post"> 
						请输入雇员姓名或者雇员工作：<input type="text" name="kw"> 
						<input type="submit" value="提交"> 
					</form> 
					<table border="1" width="80%"> 
						<tr> 
							<td> 雇员编号</td> 
							<td> 雇员姓名</td> 
							<td> 雇员工作</td> 
							<td> 雇用日期</td> 
							<td> 雇员工资</td> 
						</tr> 
			<%
					Iterator<Emp>  itr = all.iterator() ;
					while(itr.hasNext()){
						Emp emp = itr.next() ;
			%> 
						<tr> 
							<td> <%=emp.getEmpno()%> </td> 
							<td> <%=emp.getEname()%> </td> 
							<td> <%=emp.getJob()%> </td> 
							<td> <%=emp.getHiredate()%> </td> 
							<td> <%=emp.getSal()%> </td> 
						</tr> 
			<%
					}
			%> 
					</table> 
				</center> 
			<%
				}catch(Exception e){
					throw e ;
				}
			%> 
		</body> 
	</html> 

> 注意：`字符串 == null 和字符串 == ""`的区别，在jsp文件中经常会报错！

根据雇员编号查询-emp_list_QueryFindById.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="cn.fmz.vo.*,cn.fmz.factory.*,cn.fmz.custom.string.*"%> 
	<%@ page import="java.util.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;

				try{
					String empno = request.getParameter("empno") ;
					String errorMsg = "" ;
					if(!FString.isNum(empno)){
						empno = "-1" ;
						errorMsg = "员工编号必须为数字！" ;
					}

					int id ;
					id = Integer.parseInt(empno) ;
					List<Emp>  list = new ArrayList<Emp> () ;
					if(id == -1){
						list = DAOFactory.getIEmpDAOInstance().findAll("") ; 
					}else{
						Emp emp = DAOFactory.getIEmpDAOInstance().findById(id) ;
						if(emp != null){
							list.add(emp) ;
						}else{
							errorMsg = "查询结果不存在！" ;
						}
					}
			%> 
				<center> 
					<form action="emp_list_QueryById.jsp" method="post"> 
						请输入雇员编号：<input type="text" name="empno"> 
						<input type="submit" value="提交"> &nbsp;<%=errorMsg%> 
					</form> 
					<table border="1" width="80%"> 
						<tr> 
							<td> 雇员编号</td> 
							<td> 雇员姓名</td> 
							<td> 雇员工作</td> 
							<td> 雇用日期</td> 
							<td> 雇员工资</td> 
						</tr> 
			<%
					Iterator<Emp>  itr = list.iterator() ;
					while(itr.hasNext()){
						Emp emp = itr.next() ;
			%> 
						<tr> 
							<td> <%=emp.getEmpno()%> </td> 
							<td> <%=emp.getEname()%> </td> 
							<td> <%=emp.getJob()%> </td> 
							<td> <%=emp.getHiredate()%> </td> 
							<td> <%=emp.getSal()%> </td> 
						</tr> 
			<%	
					}
			%> 
					</table> 
				</center> 
			<%
					}catch(Exception e){
						throw e ;
					}
			%> 

		</body> 
	</html> 

---

---
