---
layout: post
title: javaweb开发基础篇之基础语法
---

## 注释及Scriptlet

### 注释

1. 显式注释，客户端是允许看见的。`<!--注释内容--> `

2. 隐式注释，格式一：`//单行注释`；格式二：`/*多行注释*/`；格式三：`<%--注释--%> `，jsp注释

注释代码示例

<!doctype html> 
<html> 
	<head> 
		<meta charset="utf-8"> 
		<title> </title> 
	</head> 
	<body> 
		<!-- 这个注释客户端是可见的 --> 
		<%-- 这个事jsp独有的注释，客户端是不可见的 --%> 
		<%
			//java单行注释，客户端不可见
			/*
				java多行注释，客户端不可见
			*/
		%> 
	</body> 
</html> 

### Scriptlet(脚本小程序)
 
>  所有嵌入在HTML代码中的java程序必须使用Scriptlet标记出来

在JSP中共有3种Scriptlet代码

1. 第一种：`<% %> `

2. 第二种：`<%! %> `

3. 第三种：`<%= %> `

#### `<% %> `

> 在此Scriptlet中可以定义局部变量、编写语句等

代码示例

	<%
		int x = 10 ;
		String str = "fmz" ;
		out.println("<h2> x="+(++x)+"</h2> ") ;
		out.println("<h2> str="+str+"</h2> ") ;
	%> 

#### `<%! %> `

> 在此Scriptlet中可以定义全局变量、方法、类等

代码示例

	<%!
		public static final String str = "fmz" ;
	%> 
	<%!
		int x = 10 ;
	%> 
	<%!
		public int add(int x,int y){
			return x+y ;
		}
	%> 
	<%!
		public class Person{
			private String name ;
			private int age ;

			public Person(String name,int age){
				this.name = name ;
				this.age = age ;
			}

			public String toString(){
				return "姓名："+this.name+"，年龄："+this.age ;
			}
		}
	%> 

	<%//编写普通的Scriptlet
		out.println(str) ;
		out.println("5 + 3 = "+add(5,3)) ;
		out.println(new Person("某某某",25)) ;
		out.println("x= "+(x++)) ;
	%> 

> 尽量不要在JSP中定义类或者方法，虽然上述可以定义，但是从正确的开发思路来看，当JSP中需要类或者方法时，往往通过JavaBean的形式调用

#### `<%= %> `

> 此Scriptlet的主要功能是输出一个变量或者一个具体内容

代码示例

	<%
		String name = "fmz" ;
		int age = 25 ;
	%> 
	<h2> <%= name %> </h2> 
	<h3> <%= age %> </h3> 

> 使用这种代码的好处是可以更好的将HTML代码和java代码相分离

两种输出代码：`out.println()`和`<%= %> `那种方式更好？

在JSP的开发中，实际上是在HTML代码中加入了一些控制和输出语句，所以在输出时，为了使HTML代码和java代码相分离，最好的做法就是只输出由JSP产生的变量，那么就是用`<%= %> `输出更方便

##### 比较两者输出

使用out.println()输出

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				int rows = 10 ;
				int cols = 10 ;
				out.println("<table border=\"1\" width=\"100%\"> ") ;
				for(int i=0;i<rows;i++){
					out.println("<tr> ") ;
					for(int j=0;j<cols;j++){
						out.println("<td> "+(i*j)+"</td> ") ;
					}
					out.println("</tr> ") ;
				}
				out.println("</table> ") ;
			%> 
		</body> 
	</html> 

> 这样将HTML代码和java代码混合在一起很混乱，不好调试

使用<%= %> 输出

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<table border="1" width="100%"> 
			<%
				int rows = 10 ;
				int cols = 10 ;
				for(int i=0;i<rows;i++){
			%> 
				<tr> 
			<%
					for(int j=0;j<cols;j++){
			%> 
						<td> <%= i*j %> </td> 
			<%
					}
			%> 
				</tr> 
			<%
				}
			%> 
			</table> 
		</body> 
	</html> 

> 切记：一定不要使用out.println()进行输出

利用交互性打印表格

	html文件

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="print_table.jsp" method="post"> 
				<table border="0"> 
				<tr> 
					<td> 请输入打印表格行数：</td> 
					<td> <input type="text" name="rows"> 
				</tr> 
				<tr> 
					<td> 请输入打印表格列数：</td> 
					<td> <input type="text" name="cols"> 
				</tr> 
				<tr colspan="2"> 
					<td> 
						<input type="submit" value="提交"> 
						<input type="reset" value="重置"> 
					</td> 
				</tr> 
			</table> 
			</form> 
		</body> 
	</html> 

jsp文件

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<table border="1" width="100%"> 
			<%
				int rows = 0 ;
				int cols = 0 ;
				rows = Integer.parseInt(request.getParameter("rows")) ;
				cols = Integer.parseInt(request.getParameter("cols")) ;
				for(int i=0;i<rows;i++){
			%> 
				<tr> 
			<%
					for(int j=0;j<cols;j++){
			%> 
						<td> <%= i*j %> </td> 
			<%
					}
			%> 
				</tr> 
			<%
				}
			%> 
			</table> 
		</body> 
	</html> 

#### Scriptlet标签

在程序汇总可以用`<jsp:scriptlet> java代码</jsp:scriptlet> `，相当于`<% %> `

> 注意此标签必须完结，即成对出现

## page指令

page指令在JSP开发中较为重要，使用此属性，可以定义一个JSP页面的相关属性，包括设置MIME类型、定义需要导入的包、错误页指定等

> page指令有很多，只有import指令可以重复出现多次，而对于其他属性只能出现一次，比较常用的指令有四个：`contentType`、`pageEncoding`、`errorPage/isErrorPage`和`import`这四个指令

page指令语法：`<%@ page 属性="内容"%`> 

### 设置MIME

> 在page指令中，contentType属性是使用最多的一个属性，如果想让一个JSP文件显示中文，则必须对整个页面指定MIME编码。MIME(Multipurpose Internet Mail Extensions,多功能Internet邮件扩充服务)就是指定某个扩展名文件将使用何种应用程序打开的一个说明，当该扩展名文件被访问时，浏览器会自动指定应用程序来打开。

为JSP页面指定编码

	<%@ page language="java" contentType="text/html;charset=GBK"%> 
	<center> 
		<h2> 冯孟昭博客</h2> 
		<h3> http://fengmengzhao.github.io</h3> 
	</center> 

> 显示中文时，编码的方式可以为utf-8或者GBK，注意文件的编码必须和设置的contentType属性编码方式一致，否则会出现乱码

如果想要将在网页上显示的内容以 *.word 的形式下载下载，只需要将contentType中的`text/html`改为`application/msword`即可，来源于conf\web.xml中的文件配置代码：`<mime-mapping> <extension> doc</extension> <mime-type> application/msword</mime-type> </mime-mapping> <mime-mapping> `。
同时可以通过`reponse`对象完成对下载文件名称的设置，设置方法如下：

在jsp文件中加入代码：`<%reponse.setHeader("Content-Disposition","attachment;filename=*.doc");%> `

#### 通过交互性打印表格，并完成下载及命名的实现

HTML代码

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="print_table.jsp" method="post"> 
				<table border="0"> 
				<tr> 
					<td> 请输入打印表格行数：</td> 
					<td> <input type="text" name="rows"> 
				</tr> 
				<tr> 
					<td> 请输入打印表格列数：</td> 
					<td> <input type="text" name="cols"> 
				</tr> 
				<tr colspan="2"> 
					<td> 
						<input type="submit" value="提交"> 
						<input type="reset" value="重置"> 
					</td> 
				</tr> 
			</table> 
			</form> 
		</body> 
	</html> 

JSP代码

	<%@ page language="java" contentType="application/msword;charset=utf-8"%> 

		<% 
			response.setHeader("Content-Disposition","attachment;filename=tableDownload.doc");
		%> 

	<table border="1" width="100%"> 

		<%
			int rows = 0 ;
			int cols = 0 ;
			rows = Integer.parseInt(request.getParameter("rows")) ;
			cols = Integer.parseInt(request.getParameter("cols")) ;
			for(int i=0;i<rows;i++){
		%> 
			<tr> 
		<%
				for(int j=0;j<cols;j++){
		%> 
					<td> <%= i*j %> </td> 
		<%
				}
		%> 
			</tr> 
		<%
			}
		%> 
	</table> 

> 注意：上述代码能够完成输入行和列打印出对应功能的表格，并且能够转化为指定文件名称的doc文件进行下载。代码中是`Disposition`,而不是`Dispositon`

> 后缀为*.htm和*.html的文档二者完全相同，可以通过conf\web.xml查看其处理类型也完全一样

### 设置文件编码

还可以通过`pageEncoding="编码"`的属性设置来设置编码，例如：`<%@ page contentType="text/html;" pageEncoding="编码"%> `。

> pageEncoding 和 ContentType中的charset 都可以设置编码，前者是JSP文件本身的编码，后者是服务器发给客户端的内容编码，前者存在JSP文件编码为前者，否则为后者编码，二者都不存在未默认编码 ISO-8859-1 。

### 错误页的设置

1. 指定错误出现时跳转页，通过`errorPage`属性指定

2. 错误处理也必须有明确的标识，通过`isErrorPage`属性设定

出错网页(跳转页)

	<%@ page language="java" contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page errorPage="error.jsp"%> <%-- 一旦出现错误就会跳转到error.jsp --%> 

	<%
		int x = 10 / 0 ;
	%> 

	<h2> fmz</h2> 

处理出错页

	<%@ page language="java" contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page isErrorPage="true"%> 
	<% response.setStatus(200) ; %> <%-- 表示此页正确，可以处理错误 --%> 
	<h2> 程序出现错误</h2> 

> 跳转页跳转后，地址栏并没有发生改变，显示内容却变成了处理错误页的内容，这样的跳转在程序中称为“服务器端跳转”，与此相对的程序跳转后，地址栏发生了改变，则此种跳转称为“客户端跳转”，例如：超练级就是客户端跳转。

要想在整个虚拟目录中指定全局的错误处理，必须修个虚拟文件中的web.xml文件，在其中加入错误页的操作

全局的错误处理可以分为两种：HTTP代码错误，如：404、500,和异常的错误，如：NullPointerException

#### 修改web.xml文件，在其中加入如下代码

	<error-page> 
		<error-code> 500</error-code> 
		<location> /ch05error.jsp</location> 
	</error-page> 
	<error-page> 
		<error-code> 404</error-code> 
		<location> /ch05error.jsp</location> 
	</error-page> 
	<error-page> 
		<error-code> java.lang.NullPointerException</error-code> 
		<location> /ch05error.jsp</location> 
	</error-page> 

### 数据库的连接操作

	数据库连接代码：`mysql -uroot -pmysqladmin`

	创建数据库的脚本

	/*======================= 删除数据库 =======================*/
	DROP DATABASE IF EXISTS mldn ;
	/*======================= 创建数据库 =======================*/
	CREATE DATABASE mldn ;
	/*======================= 使用数据库 =======================*/
	USE mldn ;
	/*======================= 删除数据表 =======================*/
	DROP TABLE IF EXISTS emp ;
	/*======================= 创建数据表 =======================*/
	CREATE TABLE emp(
	   empno			INT(4)			PRIMARY KEY,
	   ename			VARCHAR(10),
	   job				VARCHAR(9),
	   hiredate			DATE,
	   sal				FLOAT(7,2)
	) ;
	/*======================= 插入测试数据 =======================*/
	INSERT INTO emp (empno,ename,job,hiredate,sal) VALUES (6060,'李兴华','经理','2001-09-16',2000.30) ;
	INSERT INTO emp (empno,ename,job,hiredate,sal) VALUES (7369,'董鸣楠','销售','2003-10-09',1500.90) ;
	INSERT INTO emp (empno,ename,job,hiredate,sal) VALUES (8964,'李祺','分析员','2003-10-01',3000) ;
	INSERT INTO emp (empno,ename,job,hiredate,sal) VALUES (7698,'张惠','销售','2005-03-12',800) ;
	INSERT INTO emp (empno,ename,job,hiredate,sal) VALUES (7782,'杨军','分析员','2005-01-12',2500) ;
	INSERT INTO emp (empno,ename,job,hiredate,sal) VALUES (7762,'刘明','销售','2005-03-09',1000) ;
	INSERT INTO emp (empno,ename,job,hiredate,sal) VALUES (7839,'王月','经理','2006-09-01',2500) ;

> 注意：Mysql的连接需要额外的驱动程序，将mysql的驱动程序(jar包)放入Tomcat8.0\lib中即可

将上述数据库脚本创建的数据库表格进行输出，代码如下：

	<%@ page language="java" contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.sql.*"%> 
	<html> 
	<head> 
		<title> JSP连接数据库</title> 
	</head> 
	<body> 
	<%!
		//定义数据库驱动程序
		public static final String DBDRIVER = "org.gjt.mm.mysql.Driver" ;
		//数据库连接地址
		public static final String DBURL = "jdbc:mysql://localhost:3306/mldn" ;
		public static final String DBUSER = "root" ;//数据库连接用户名
		public static final String DBPASS = "mysqladmin" ;//数据库连接密码
	%> 	
	<%
		Connection conn = null ;//声明数据库连接对象
		PreparedStatement pstmt = null ;//声明数据库操作
		ResultSet rs = null ;//声明数据库结果集
	%> 
	<%
		try{//数据库的操作中会出现异常，所以要用try...catch处理
			Class.forName(DBDRIVER) ;//数据库驱动程序加载
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS) ;//取得数据库连接
			String sql = "SELECT empno,ename,job,sal,hiredate FROM emp" ;
			pstmt = conn.prepareStatement(sql) ;//实例化PreparedStatement对象
			rs = pstmt.executeQuery() ;//执行查询操作
	%> 
	<center> 
		<table border="1" width="80%"> 
			<tr> 
				<td> 雇员编号</td> 
				<td> 雇员姓名</td> 
				<td> 雇员工作</td> 
				<td> 雇员工资</td> 
				<td> 雇佣日期</td> 
			</tr> 
	<%
		while(rs.next()){
		int empno = rs.getInt(1) ;//取出雇员编号
		String ename = rs.getString(2) ;//取出雇员姓名
		String job = rs.getString(3) ;//取出雇员工作
		float sal = rs.getFloat(4) ;//取出雇员工资
		java.util.Date date = rs.getDate(5) ;//取出雇用日期
	%> 
			<tr> 
				<td> <%=empno%> </td> 
				<td> <%=ename%> </td> 
				<td> <%=job%> </td> 
				<td> <%=sal%> </td> 
				<td> <%=date%> </td> 
			</tr> 
	<%
		}
	%> 
		</table> 
	</center> 
	<%
		}catch(Exception e){
		System.out.println(e) ;
	}finally{
		rs.close() ;
		pstmt.close() ;
		conn.close() ;
	}
	%> 
	</body> 
	</html> 

***

2015/8/6

***

## 包含指令

### 包含的意义

包含指令可以实现这样的功能：将工具栏、页面头部、页面尾部(代码基本上是固定的)分别作成一个文件，然后在需要的地方导入（包含）。

### 静态包含

> 静态包含指令是在JSP编译时插入一个包含文本或者代码的文件，这个包含的过程是静态的，而包含文件可以使JSP文件、HTML文件、文本文件，或者一段java程序。

> 在每一个完整的页面中，对于<html> 、</html> 、<head> 、</head> 、<body> 、<body> 、<title> 、</title> 这几个元素只能出现一次，如果重复出现则会造成显示的错误。

静态包含语法：`<%@ include file="要包含的文件路径"%> `

示例代码：

包含文件1

	<h2> 
	<font color="red"> info.html</font> 
	</h2> 

包含文件2

	<h2> 
		<font color="green"> 
			<%="info.jsp"%> 
		</font> 
	</h2> 

包含文件3

	<h2> 
		<font color="blue"> info.inc</font> 
	</h2> 

使用静态包含指令包含上述三个文件

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h1> 静态包含操作</h1> 
			<%@ include file="info.html"%> 
			<%@ include file="info.jsp"%> 
			<%@ include file="info.inc"%> 
		</body> 
	</html> 

> 在以上静态包含指令中，实际上是所包含的三个文件的内容导入到有包含指令的文件中，然后在进行一起编译，也就是先包含，然后将全部的代码进行集中的编译处理。

### 动态包含

> 动态包含与静态包含不同，动态语句可以自动区分被包含的页面是静态的还是动态的。如果是静态页面和之前的静态包含相同，将静态内容包好进来；如果是动态页面，则要先进行动态处理，然后再将处理后的结果包含进来。

#### 动态包含语法

1. 不传递参数类型：`<jsp:include page="{要包含的文件路径|<%= 表达式%> }" flush="true|false"/`

2. 传递参数类型：`<jsp:include page="{要包含的文件路径|<%= %> }" flush="true|false"> <jsp:param name="参数名称" value="参数内容"/> </jsp:include> `

> 可以向包含语句中传递多个参数

> `<jsp:include> `这样的语法格式属于标签指令形式，所有的标签指令必须完结

使用动态包含的第二种语法可以向被包含页面传递参数，被包含也变可以使用`request.getParameter()`方法进行参数的接收

定义被包含页

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<h1> 
		参数一：<%=request.getParameter("name")%> 
	</h1> 
	<h1> 
		参数二：<%=request.getParameter("info")%> 
	</h1> 

定义包含页

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String username = "fmz" ;
			%> 
			<h1> 动态包含传递参数</h1> 
			<jsp:include page="receive_param.jsp"> 
				<jsp:param name="name" value="<%=username%> "/> 
				<jsp:param name="info" value="http:fengmengzhao.github.io"/> 
			</jsp:include> 
		</body> 
	</html> 



### 静态包含OR动态包含

静态包含的处理方式是先包含再处理，而动态包含的处理方式是先处理后包含，如果包含页和处理页定义了相同的变量，则使用静态处理方式会出现错误，而在开发中很难避免定义相同变量，所以使用动态处理会更加方便。

## 跳转指令

### 页面跳转语法

1. 不传递参数：`<jsp:forward page="{要包含的文件路径|<%=表达式%> }"/> `
2. 传递参数：`<jsp:forward page="{要包含的文件路径|<%=表达式%> }"> <jsp:param name="参数名称" value="参数内容"/> </jsp:forward> `

> 可以向页面中传递多个参数

跳转页

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<jsp:forward page="skip_demo01.jsp"> 
				<jsp:param name="name" value="fmz"/> 
				<jsp:param name="info" value="http://fengmengzhao.github.io"/> 
			</jsp:forward> 
		</body> 
	</html> 

跳转到的页面

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<center> 
		<h1> 跳转到页面</h1> 
		<h2> 
			<%=request.getParameter("name")%> 
		</h2> 
		<h2> 
			<%=request.getParameter("info")%> 
		</h2> 
	</center> 

> 注意这种跳转实在地址栏没有发生改变的情况下发生的，故称为：服务器跳转。
