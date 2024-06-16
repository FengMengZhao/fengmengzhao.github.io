---
layout: post
title: javaweb开发高级篇之表达式语言
---

## 目录

- [1 表达式语言简介](#1)
- [2 表达式语言的内置对象](#2)
	- [2.1 访问四种属性范围的内容](#2.1)
	- [2.2 调用内置对象进行操作](#2.2)
	- [2.3 接收请求参数](#2.3)
- [3 集合操作](#3)
- [4 在MVC中运用表达式语言](#4)
- [5 运算符](#5)

---

---

<h2 id="1"> 1 表达式语言简介</h2> 

表达式语言(Expression Language，EL)是JSP2.0中新增加的功能。使用表达式语言可以方便的地访问标志位(在JSP中一共提供了四种标志位：page(PageContext)、request、session、application)中的属性内容，这样就可以避免出现很多的Script代码

表达式语言的使用

`${属性名称}`

> 使用表达式语言的最大好处是：如果输出的内容为null，会自动使用空字符串("")表示

实例代码-print_attribute_demo01.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setAttribute("info","http://fengmengzhao.github.io") ;	
			%> 
			<h3> ${info}</h3> 
			<h3> <%=request.getAttribute("info")%> </h3> 
		</body> 
	</html> 

---

---

<h2 id="2"> 2 表达式语言的内置对象</h2> 

表达式语言的主要功能是进行内容的显示，为了显示方便，在表达式语言中提供了很多的内置对象，通过不同的内置对象的设置表达式输出不同的内容

表达式语言的内置对象

| NO. | 表达式的内置对象 | 描述 |
|--- | --- | ---|
| 1 | pageContext | 表示javax.servlet.jsp.PageContext对象 |
| 2 | pageScope | 表示从page属性范围查找输出属性 |
| 3 | requestScope | 表示从request属性范围查找输出属性 |
| 4 | sessionScope | 表示从session属性范围查找输出属性 |
| 5 | applicationScope | 表示从application属性范围查找输出属性 |
| 6 | param | 接收传递到本页面的参数 |
| 7 | paramValues | 接收传递到本页面的一组参数 |
| 8 | header | 取得一个头信息数据 |
| 9 | headerValues | 取得一组头信息数据 |
| 10 | cookie | 取得cookie中的数据 |
| 11 | initParam | 取得配置的初始化参数 |

<h3 id="2.1"> 2.1 访问四种属性范围的内容</h3> 

示例代码-repeat_attribute_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%	pageContext.setAttribute("info","page") ;
				request.setAttribute("info","http://fengmengzhao.github.io") ;
				session.setAttribute("info","session") ;
				application.setAttribute("info","application") ;
			%> 
			<h3> ${pageScope.info}</h3> 
			<h3> ${requestScope.info}</h3> 
			<h3> ${sessionScope.info}</h3> 
			<h3> ${applicationScope.info}</h3> 
		</body> 
	</html> 

> 如果使用内置对象进行输出，在属性同名的情况下会先查找page属性范围，然后依次是request、session、application属性范围，用表达式语言可以在属性同名的情况下进行指定的属性范围进行输出。

<h3 id="2.2"> 2.2 调用内置对象进行操作</h3> 

示例代码-invoke_method_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h3> IP地址：${pageContext.request.remoteAddr}</h3> 
			<h3> Session ID：${pageContext.session.id}</h3> 
			<h3> 是否是新的session：${pageContext.session.new}</h3> 
		</body> 
	</html> 

> 表达式语言中更多的是利用反射的操作机制，在通过表达式的内置对象调用方法时，都是以调用getXxx()或者isXxx()的方法居多

<h3 id="2.3"> 2.3 接收请求参数</h3> 

接收一个参数：`${param.参数名称}`

实例代码-get_param_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
				<h3> request接受参数：<%=request.getParameter("ref")%> </h3> 
				<h3> 表达式接受参数：${param.ref}</h3> 
		</body> 
	</html> 

接收一组参数：`${paramVlues.参数名称}`

实例代码-param_values_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
				<%
					request.setCharacterEncoding("utf-8") ;//在实际的开发中，需要用过滤器的完成。
				%> 
				<h3> 第一个参数：${paramValues.inst[0]}</h3> 
				<h3> 第二个参数：${paramValues.inst[1]}</h3> 
				<h3> 第三个参数：${paramValues.inst[2]}</h3> 
				<h3> 第四个参数：${paramValues.inst[3]}</h3> 
		</body> 
	</html> 

---

---

<h2 id="3"> 3 集合操作</h2> 

集合有Collection、Map、Iterator接口，Collection接口的子接口有Set、List，所有的集合都使用Iterator方法进行输出，而且List和Set集合的本质区别在于List接口对Collection接口进行了扩充，而Set接口并没有进行扩充。Map接口一次记性一对内容的保存操作，而且Map每次保存都是Map Enty接口对象。

输出Collection接口-print_collection.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				List list = new ArrayList() ;
				list.add("fmz") ;
				list.add("http://fengmengzhao.github.io") ;
				list.add("cll") ;
				request.setAttribute("info",list) ;
			%> 
			<h3> 第一个属性内容：${info[0]}</h3> 
			<h3> 第二个属性内容：${info[1]}</h3> 
			<h3> 第三个属性内容：${info[2]}</h3> 
		</body> 
	</html> 

> 使用request范围保存属性目的是为MVC设计模式做准备，因为在MVC设计模式中都是使用request属性范围将Servlet中的内容传递给JSP页面进行显示，而表达式语言的最大优点需要结合MVC模式才能显示出来

输出Map接口-print_map.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				Map map = new HashMap() ;
				map.put("fmz","冯孟昭") ;
				map.put("blog","http://fengmengzhao.github.io") ;
				request.setAttribute("info",map) ;
			%> 
			<h3> key为fmz的内容：${info["fmz"]}</h3> 
			<h3> key为blog的内容：${info["blog"]}</h3> 
		</body> 
	</html> 

---

---

<h2 id="4"> 4 在MVC模式中应用表达式语言</h2> 

表达式语言的强大功能支出在于，可以直接通过反射的方式调用保存在属性范围中的java对象内容

定义一个VO类-Dept.java

	package org.fmz.eldemo.vo ;
	public class Dept{
		private String dname ;
		private int deptno ;
		private String loc ;
		public void setDname(String dname){
			this.dname = dname ;
		}
		public void setDeptno(int deptno){
			this.deptno = deptno ;
		}
		public void setLoc(String loc){
			this.loc = loc ;
		}
		public String getDname(){
			return this.dname ;
		}
		public int getDeptno(){
			return this.deptno ;
		}
		public String getLoc(){
			return this.loc ;
		}
	}

通过jsp文件进行输出-print_vo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="org.fmz.eldemo.vo.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				Dept dept = new Dept() ;
				dept.setDeptno(112) ;
				dept.setDname("工商管理学院") ;
				dept.setLoc("四川成都") ;
				request.setAttribute("info",dept) ;
			%> 
			<h3> 部门编号为：${info.deptno}</h3> 
			<h3> 部门名称为：${info.dname}</h3> 
			<h3> 部门位置为：${info.loc}</h3> 
		</body> 
	</html> 

> 首先实例化对象，分别设置里面的属性内容，并将属性保存在request属性范围中，然后可以方便的通过表达式的方式进行输出。

> 在使用表达式进行输出时，实际上是使用的java的反射机制。

编写Servlet传递request属性-ElServlet.java

	package org.fmz.eldemo.servlet ;

	import javax.servlet.- ;
	import javax.servlet.http.- ;
	import org.fmz.eldemo.vo.- ;

	public class ElServlet extends HttpServlet{
		public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,java.io.IOException{
			Dept dept = new Dept() ;
			dept.setDeptno(110) ;
			dept.setDname("工商管理学院") ;
			dept.setLoc("四川成都") ;
			request.setAttribute("info",dept) ;	
			request.getRequestDispatcher("dept_info.jsp").forward(request,response) ;
		}
		public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,java.io.IOException{
			this.doGet(request,response) ;
		}
	}

> 本程序通过request属性范围保存了一个VO对象，之后通过服务器跳转的方式将request属性内容传递给dept_info.jsp

接收request属性并输出-dept_info.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h3> 部门编号为：${info.deptno}</h3> 
			<h3> 部门名称为：${info.dname}</h3> 
			<h3> 部门位置为：${info.loc}</h3> 
		</body> 
	</html> 

现在完成的是一个对象的输出，如果在DAO操作中执行的是数据库的查询，则传递到JSP页面上的将是一个对象集合(List)，这是就要想办法进行集合的输出

一个JSP页面只应该包含三种代码：接收属性、判断和输出，而且在JSP页面中最好只导入一个java.util包，所以此时就需要Iterator记性输出，通过Iterator找出集合中的每一个元素，然后将元素元素保存在page属性范围内(因为每一个输出的内容只在本页面内有效)

定义Servlet传递集合-ElServlet_dept.java

	package org.fmz.eldemo.servlet ;

	import java.util.- ;
	import javax.servlet.- ;
	import javax.servlet.http.- ;
	import org.fmz.eldemo.vo.- ;

	public class ElServlet_dept extends HttpServlet{
		public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,java.io.IOException{
			List<Dept>  list = new ArrayList<Dept> () ;
			Dept dept = null ;
			dept = new Dept() ;
			dept.setDeptno(110) ;
			dept.setDname("工商管理学院") ;
			dept.setLoc("四川成都") ;
			list.add(dept) ;
			dept = new Dept() ;
			dept.setDeptno(120) ;
			dept.setDname("金融学院") ;
			dept.setLoc("四川成都") ;
			list.add(dept) ;
			request.setAttribute("info",list) ;	
			request.getRequestDispatcher("dept_list.jsp").forward(request,response) ;
		}
		public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,java.io.IOException{
			this.doGet(request,response) ;
		}
	}

> 将对象集合传递到dept_list.jsp文件中进行输出

输出属性集合-dept_list.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				List list = (List)request.getAttribute("info") ;
				if(list != null){
			%> 
				<table border="1" width="80%"> 
					<tr> 
						<td> 部门编号</td> 
						<td> 部门名称</td> 
						<td> 部门地址</td> 
					</tr> 
			<%
					Iterator iter = list.iterator() ;
					while(iter.hasNext()){
						pageContext.setAttribute("dept",iter.next()) ;
			%> 
						<tr> 
							<td> ${dept.deptno}</td> 
							<td> ${dept.dname}</td> 
							<td> ${dept.loc}</td> 
						</tr> 	
			<%
					}
			%> 
				</table> 
			<%
				}
			%> 
		</body> 
	</html> 

> 为了防止出现NollPointerException报错，需要进行非空判断

> 表达式语言让JSP文件中的Script代码越来越少

> 表达式语言实际上是将每一个对象取出放在page方位内，因为只在本页面内有效。

---

---

<h2 id="5"> 5 运算符</h2> 

算术运算符操作-math_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.setAttribute("numl",20) ;
				pageContext.setAttribute("num2",30) ;
			%> 
			<h3> 加法操作：${numl + num2}</h3> 
			<h3> 减法操作：${numl - num2}</h3> 
			<h3> 乘法操作：${numl - num2}</h3> 
			<h3> 除法操作：${numl / num2} 和 ${num1 div num2}</h3> 
			<h3> 取余操作：${numl % num2} 和 ${num1 mod num2}</h3> 
		</body> 
	</html> 

关系运算符操作-rel_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.setAttribute("numl",10) ;
				pageContext.setAttribute("num2",10) ;
			%> 
			<h3> 等于：${numl == num2} 和 ${num1 eq num2}</h3> 
			<h3> 不等于：${numl != num2} 和 ${num1 ne num2}</h3> 
			<h3> 大于：${numl >  num2} 和 ${num1 gt num2}</h3> 
			<h3> 小于：${numl < num2} 和 ${num1 lt num2}</h3> 
			<h3> 大于等于：${numl > = num2} 和 ${num1 ge num2}</h3> 
			<h3> 小于等于：${numl <= num2} 和 ${num1 le num2}</h3> 
		</body> 
	</html> 

其他运算符操作-other_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.setAttribute("num1",10) ;
				pageContext.setAttribute("num2",20) ;
				pageContext.setAttribute("num3",30) ;
			%> 
			<h3> Empt：${empty info}</h3> 
			<h3> 三目运算：${num1 >  num2 ? true : false}</h3> 
			<h3> 优先运算：${num1 - (num2 + num3)}</h3> 
		</body> 
	</html> 

---

---
