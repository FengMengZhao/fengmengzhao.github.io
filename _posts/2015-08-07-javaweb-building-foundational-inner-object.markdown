---
layout: post
title: javaweb开发基础篇之内置对象
---

## 目录

- [1 JSP内置对象](#1)
	- [1.1 JSP的9中内置对象](#1.1)
	- [1.2 四种属性范围](#1.2)
		- [1.2.1 age属性范围(pageContext)](#1.2.1)
		- [1.2.2 request 属性](#1.2.2)
		- [1.2.3 session 属性](#1.2.3)
		- [1.2.4 application属性](#1.2.4)
		- [1.2.5 深入研究page属性范围](#1.2.5)
	- [1.3 request 对象](#1.3)
		- [1.3.1 乱码处理](#1.3.1)
		- [1.3.2 接受请求参数](#1.3.2)
			- [1.3.2.1 URL地址重写](#1.3.2.1)
		- [1.3.3 显示全部的头信息](#1.3.3)
		- [1.3.4 角色验证](#1.3.4)
			- [1.3.4.1 修改conf\tomcat-user.xml文件，增加两个新用户](#1.3.4.1)
			- [1.3.4.2 配置虚拟文件中的WEB-INF\web.xml，增加以下代码](#1.3.4.2)
			- [1.3.4.3 进行安全角色验证-security.jsp](#1.3.4.3)
		- [1.3.5 其他操作](#1.3.5)
	- [1.4 response对象](#1.4)
		- [1.4.1 设置头信息](#1.4.1)
		- [1.4.2 页面跳转](#1.4.2)
		- [1.4.3 客户端跳转和服务器端跳转的区别](#1.4.3)
		- [1.4.4 操作CooKie](#1.4.4)
	- [1.5 session对象](#1.5)
		- [1.5.1 取得session ID](#1.5.1)
		- [1.5.2 重启服务器之后继续使用session](#1.5.2)
		- [1.5.3 登录及注销](#1.5.3)
		- [1.5.4 在web开发中一共存在4种会话跟踪技术](#1.5.4)
		- [1.5.5 判断新用户(isNew())](#1.5.5)
		- [1.5.6 取得用户的操作时间(getCreationTime()、getLastAccessedTime())](#1.5.6)
	- [1.6 application对象](#1.6)
		- [1.6.1 取得虚拟目录对应的绝对路径](#1.6.1)
		- [1.6.2 简单的文件操作](#1.6.2)
		- [1.6.3 网站计数器程序(范例讲解)](#1.6.3)
		- [1.6.4 查看application范围的属性](#1.6.4)
	- [1.7 web安全性及config对象](#1.7)
		- [1.7.1 config对象](#1.7.1)
	- [1.8 out对象](#1.8)
	- [1.9 pageContext对象](#1.9)

---

---

<h2 id="1"> 1 JSP内置对象</h2> 

在JSP中为了简化用户开发，提供了9中内置对象，这些内置对象将由容器为用户实例化，用户直接使用即可，而不必要像java中那样用new关键字来实例化后才能使用

<h3 id="1.1"> 1.1 JSP的9中内置对象</h3> 

| NO. | 内置对象 | 类型 | 描述 |
| 1 | pageContext | javax.servlet.jsp.PageContext | JSP的页面容器 |
| 2 | requset | javax.servlet.http.HttpServletRequest | 得到用户的请求信息 |
| 3 | response | javax.servlet.http.HttpservletResponse | 服务器向客户端的回应信息 |
| 4 | session | javax.servlet.http.HttpservletSession | 用来保存每一个用户的信息 |
| 5 | application | javax.servlet.ServletContext | 表示所用用户的共享信息 |
| 6 | config | javax.servlet.ServletConfig | 服务器配置，可以取得初始化参数 |
| 7 | out | javax.servlet.jsp.JspWriter | 页面输出 |
| 8 | page | java.lang.Object | 从该页面中表示出来的一个Servlet实例 |
| 9 | excepion | java.lang.Throwable | 表示JSP页面所发生的异常，在错误页中才起作用 |

<h3 id="1.2"> 1.2 四种属性范围</h3> 

> 所谓的属性的保存范围是指：一个内置的对象可以在多少个页面中保存并继续使用

1. `page`: 只在一个页面中保存属性，跳转之后无效

2. `request`: 只在一次请求中保存，服务器跳转之后依然有效

3. `session`: 在一次会话范围内，无论何种跳转都可以使用，但是新的浏览器打开之后无法使用

4. `application`: 在整个服务器中保存，所有用户都可以使用

<h4 id="1.2.1"> 1.2.1 age属性范围(pageContext)</h4> 

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
			pageContext.setAttribute("name","fmz") ;
			pageContext.setAttribute("birthday",new Date()) ;
		%> 
		<%
			String name = (String)pageContext.getAttribute("name") ;
			Date birthday = (Date)pageContext.getAttribute("birthday") ;
		%> 
		<center> 
			<h2> 姓名：<%=name%> </h2> 
			<h2> 生日：<%=birthday%> </h2> 
		</center> 
		</body> 
	</html> 

> 可以取得本页内的属性

跳转页

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
			pageContext.setAttribute("name","fmz") ;
			pageContext.setAttribute("birthday",new Date()) ;
		%> 
		<jsp:forward page="page_scope03.jsp"/> 
		</body> 
	</html> 

所跳转到页

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
			String name = (String)pageContext.getAttribute("name") ;
			Date birthday = (Date)pageContext.getAttribute("birthday") ;
		%> 
		<center> 
			<h2> 姓名：<%=name%> </h2> 
			<h2> 生日：<%=birthday%> </h2> 
		</center> 
		</body> 
	</html> 

> 跳转之后无法获得跳转页的属性，属性内容只能从当前页获取

<h4 id="1.2.2"> 1.2.2 request 属性</h4> 

> request属性表示在服务器跳转之后，所有设置的内容依然会保留下来，但是客户端的跳转，设置的内容在第二次请求时已经丢失，无法获取

> request表示客户端的请求，正常情况下，一次请求服务器只会给予一次回应，如果是服务器端跳转，地址栏没有发生改变，也就是相当回应一次了；而如果地址栏发生了改变，就相当于发出了第二次请求，第一次请求的内容已经消失，所以无法取得。属性内容保存在每次请求当中，请求回应后即消失。

<h4 id="1.2.3"> 1.2.3 session 属性</h4> 

> session 属性可以取得服务器端和客户端的跳转

> 实际上，每一个浏览器连接到服务器之后，都表示各自的session，表示每一位上网者有自己的属性，所以新的浏览器打开之后，无法取得其他session设置的属性，属性保存在每一个连接服务器的浏览器上，浏览器关闭即消失

<h4 id="1.2.4"> 1.2.4 application属性</h4> 

> application设置的属性可以让每一个用户(session)都看得见，这样的属性保存在服务器上。

> application范围的属性设置过多会影响服务器的性能

> 由于属性是保存在服务器当中的，所以当服务器重启的时候，之前设置的属性就全部消失了

<h4 id="1.2.5"> 1.2.5 深入研究page属性范围</h4> 

`pageContext`对象可以任意范围的属性，通过方法`pageContext.setAttribute(String name,Object value,int scope)`,而且scope的内容可以是`PAGE_SCOPE、REQUEST_SCOPE、SESSION_SCOPE、APPLICATION_SCOPE`四中类型

<h3 id="1.3"> 1.3 request 对象</h3> 

> request对象的主要作用是接受客户端发送而来的信息，如请求是参数、发送的头信息等都属于客户端发送来的信息。request是javax.servlet.http.HttpServletRequest接口的实例化对象。

> 接口javax.servlet.http.HttpServletRequest是javax.servlet.ServletRequest的子接口,但是javax.servlet.ServletRequest接口中只有一个接口，为什么不将二者合为一个接口？这样的设计主要是为了以后扩展应用，虽然现在的java web只支持HTTP协议，但是如果以后要是支持了新的协议，则直接继承公共接口就可以了

request内置对象的常用方法

| NO. | 方法 | 描述 |
| 1 | public String getParameter(String name) | 接收客户端发送的请求参数内容 |
| 2 | public String[] getParameterValues(String name) | 取得客户端发送的一组请求参数的内容 |
| 3 | public Enumeration getParameterNames() | 取得全都请求参数的名称 |
| 4 | public String getRemoteAddr() | 取得客户端IP地址 |
| 5 | void setCharacterEncoding(String env) throws UnsupportedEncodingException | 设置统一请求编码 |
| 6 | public boolean is UserInrole(String role) | 进行用户身份验证 |
| 7 | public HttpSession getSession() | 取得当前的session对象 |
| 8 | public StringBuffer gerRequestURL() | 返回正在请求的路径 |
| 9 | public Enumeration getHeaderNames() | 取得全部头信息名称 |
| 10 | public String getHeader(String name) | 根据名称取得头信息的内容 |
| 11 | public String getMethod() | 取得用户的提交方式 |
| 12 | public String getServletPath() | 取得访问路径 |
| 13 | public String getContextPath() | 取得上下文资源路径 |

<h4 id="1.3.1"> 1.3.1 乱码处理</h4> 

可以直接通过`request.setCharacterEncoding("编码")`来进行统一编码设置

示例代码

表单文件

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="GBK"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="request_demo01.jsp" method="post"> 
				请输入内容：<input type="text" name="info"> 
				<input type="submit" value="提交"> 
			</form> 
		</body> 
	</html> 

接收表单参数文件

	<%@ page contentType="text/html" pageEncoding="GBK"%> 
	<center> 
		<%
			request.setCharacterEncoding("GBK") ;
			String info = request.getParameter("info") ;
		%> 
		<h2> <%=info%> </h2> 
	</center> 

> 注意：当设置统一格式为utf-8时，浏览器接收参数产生乱码；此问题没有解决

> 可以统一设置为gbk编码，则不会产生乱码，但是注意，设置为gbk编码时，文件的编码方式一定要设置成ANSI编码格式

<h4 id="1.3.2"> 1.3.2 接受请求参数</h4> 

> 单一参数的接收可以通过`getParameter()`，而一组参数的接收需要通过`getParameterValues()`完成。在表单控件中，像文本框(text)、单选框(radio)、密码框(password)、隐藏域(hidden)等，参数的名称只有一个，不会重复，一般都通过getParameter()方法接收；而像复选框(checked)，一般参数名称都是重复的，是一组参数，只能通过getParamterValues()方法接收，如果使用getParameter()方法，则只会接受第一个内容。

接收表单复选框参数示例

表单代码

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="request_demo03.jsp" method="post"> 
				姓名：<input type="text" name="uname"> <br> 
				兴趣：<input type="checkbox" name="inst" value="唱歌"> 唱歌
					  <input type="checkbox" name="inst" value="跳舞"> 跳舞
					  <input type="checkbox" name="inst" value="游泳"> 游泳
					  <input type="checkbox" name="inst" value="上网"> 上网
					  <input type="checkbox" name="inst" value="编程"> 编程
					  <input type="hidden" name="id" value="fmz"> <br> 
					  <input type="submit" value="提交"> 
					  <input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

接收参数代码

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				String id = request.getParameter("id") ;
				String name = request.getParameter("uname") ;
				String[] inst = request.getParameterValues("inst") ;
			%> 
			<center> 
			<h2> 姓名：<%=name%> </h2> 
			<h2> ID：<%=id%> </h2> 
			<h2> 兴趣：
			<%
				if(inst != null){
					for(int i=0;i<inst.length;i++){
			%> 
						<%=inst[i]%> ，
			<%
					}
				}
			%> 
			</h2> 
			</center> 
		</body> 
	</html> 

在WEB开发中，所有的参数不一定通过表单传递过来，也可以通过地址重写的方式进行传递

<h5 id="1.3.2.1"> 1.3.2.1 URL地址重写</h5> 

`动态页面地址?参数名称1=参数内容1&参数名称2=参数内容2&...`

> 多个参数之间要用&隔开

> 在表单上有两种提交方式，method：post OR get 两种，使用哪种提交方式好呢？若使用get提交方式，提交的内容会显示在地址栏中，如提价的内容为图片等较大的信息，则会受到地址栏容量的限制(地址栏最大的容量为4~5K),而使用post方式提价，则不会出现这种情况。

request内置对象还有一个灵活的方法：`getParameterNames()`，这个方法可以返回所有请求参数的名称，此方法的返回类型是`Enumeration`，所以要使用`hasMoreElements()`方法判断是否有内容，以及使用`nextElement()`方法进行输出

通过getParameterNames()方法进行参数输出示例

要输出的表单文件

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="request_demo05.jsp" method="post"> 
				姓名：<input type="text" name="uname"> <br> 
				性别：<input type="radio" value="男" checked> 男
					  <input type="radio" value="女"> 女<br> 
				城市：<select name="city"> 
						<option value="北京"> 北京</option> 
						<option value="上海"> 上海</option> 
						<option value="广州"> 广州</option> 
						<option value="天津"> 天津</option> 
					  </select> <br> 
				兴趣：<input type="checkbox" name="_inst" value="唱歌"> 唱歌
					  <input type="checkbox" name="_inst" value="跳舞"> 跳舞
					  <input type="checkbox" name="_inst" value="游泳"> 游泳
					  <input type="checkbox" name="_inst" value="上网"> 上网
					  <input type="checkbox" name="_inst" value="编程"> 编程
					  <input type="hidden" name="id" value="fmz"> <br> 
				自我介绍：<textarea cols="30" rows="3" name="note"> </textarea> <br> 
				<input type="hidden" name="id" value="fmz"> 
			    <input type="submit" value="提交"> 
			    <input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

jsp文件输出表单信息-`Enumeration`输出

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
				request.setCharacterEncoding("utf-8") ;
			%> 
			<center> 
				<table border="1"> 
					<tr> 
						<td> 参数名称</td> 
						<td> 参数内容</td> 
					</tr> 
			<%
				Enumeration enu = request.getParameterNames() ;
				while(enu.hasMoreElements()){
					String paramName = (String)enu.nextElement() ;
			%> 
					<tr> 
						<td> <%=paramName%> </td> 
						<td> 
			<%
					if(paramName.startsWith("_")){
						String[] paramValue = request.getParameterValues(paramName) ;
						for(int i=0;i<paramValue.length;i++){
						
			%> 
								<%=paramValue[i]%> 、
			<%
						}
					}else{
						String paramValue = request.getParameter(paramName) ;
			%> 
						<%=paramValue%> 
			<%
					}
			%> 
						</td> 
					</tr> 
			<%
				}
			%> 
				</table> 
			</center> 
		</body> 
	</html> 

> 此种方式适合表单会动态变化的情况，在一般的开发中，以上的程序并不多见，但是在进行购物车程序开发时，本段代码就非常有用了。

<h4 id="1.3.3"> 1.3.3 显示全部的头信息</h4> 

> java的web开发使用的是http协议，主要的操作是基于请求和回应，但是请求和回应的同时也会包含一些其他信息(如客户端的IP、Cookie、语言等)，这些信息就称为头信息。

取得头信息名称的方法是：`getHeaderNames()`方法；取得头信息内容的方法是：`getHeader()`

取得头信息名称及内容的代码

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
				Enumeration enu = request.getHeaderNames() ;
				while(enu.hasMoreElements()){
					String headerName = (String)enu.nextElement() ;
					String headerValue = (String)request.getHeader(headerName) ;
			%> 
					<h5> <%=headerName%> --> <%=headerValue%> </h5> 
			<%
				}
			%> 
		</body> 
	</html> 

<h4 id="1.3.4"> 1.3.4 角色验证</h4> 

如果某些JSP页面需要输入特定的管理员账户才能访问，那么就需要进行角色验证。进行角色验证必须使用request内置对象中的`isUserInRole()`方法完成，且必须完成以下操作。

<h5 id="1.3.4.1"> 1.3.4.1 修改conf\tomcat-user.xml文件，增加两个新用户</h5> 

	<user username="fmz" password="799520" roles="admin"/> 
	<user username="cll" password="799520" roles="fmzuser"/> 

> 第一个用户的角色是管理员身份，第二个是一个简单的fmz用户

<h5 id="1.3.4.2"> 1.3.4.2 配置虚拟文件中的WEB-INF\web.xml，增加以下代码</h5> 

	<security-constraint> 
		<web-resource-collection> 	
			<web-resouce-name> RegisteredUsers</web-resouce-name> 
			<url-pattern> /scope_demo/security.jsp
			</url-pattern> 
		</web-resource-collection> 	
		<auth-constraint> 
			<role-name> fmzuser</role-name> 
			<role-name> admin</role-name> 
		</auth-constraint> 
	</security-constraint> 
	<login-config> 
		<auth-method> BASIC</auth-method> 
		<realm-name> Registered Users</realm-name> 
	</login-config> 
	<security-role> 
		<role-name> fmzuser</role-name> 
	</security-role> 
	<security-role> 
		<role-name> admin</role-name> 
	</security-role> 

<h5 id="1.3.4.3"> 1.3.4.3 进行安全角色验证-security.jsp</h5> 

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				if(request.isUserInRole("admin")){
			%> 
					<h2> 欢迎您的光临！！！</h2> 
			<%
				}
			%> 
			<%
				if(request.isUserInRole("fmzuser")){
			%> 
					<h2> 欢迎fmzuser的光临！！！</h2> 
			<%
				}
			%> 
		</body> 
	</html> 

<h4 id="1.3.5"> 1.3.5 其他操作</h4> 

在request对象中还可以访问客户端的IP地址、访问路径信息、提价的方式等

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String method = request.getMethod() ;
				String  ip = request.getRemoteAddr() ;
				String path = request.getServletPath() ;
				String contextPath = request.getContextPath() ;
			%> 
			<center> 
			<h3> 请求方式--> <%=method%> </h3> 
			<h3> IP地址--> <%=ip%> </h3> 
			<h3> 访问路径--> <%=path%> </h3> 
			<h3> 上下文名称--> <%=contextPath%> </h3> 
			</center> 
		</body> 
	</html> 

输出结果为

	请求方式--> GET
	IP地址--> 0:0:0:0:0:0:0:1
	访问路径--> /scope_demo/request_demo07.jsp
	上下文名称--> /fmz

> ip地址输出的为ipv6的地址，目前还没有解决这个问题。

> request内置对象在实际的开发中使用最多，而且当服务器端想得到客户端信息时就会使用request对象完成。

---

---

<h3 id="1.4"> 1.4 response对象</h3> 

> response的主要是对客户端的请求进行回应，将web服务器处理后的结果发给客户端，同request对象一样，response也也有一个父接口ServletResponse，并且此接口也只有一个子接口HttpServletResponse.

response对象的常用方法

| No. | 方法 | 描述 |
| 1 | public void addCookie(Cookie cookie) | 向客户端增加Cookie |
| 2 | public void setHeader(String name,String value) | 设置回应头信息 |
| 3 | public void sendRedirect(String location) throws IOException | 页面跳转

<h4 id="1.4.1"> 1.4.1 设置头信息</h4> 

定时刷新的头信息

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%!
				int count = 0 ;
			%> 
			<%
				response.setHeader("refresh","2") ;//设置两秒刷新一次
			%> 
			<center> 
				<h2> 您已经访问了<%=++count%> 次！</h2> 
			</center> 
		</body> 
	</html> 

设置3秒后跳转到某个页面

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				response.setHeader("refresh","3;URL=hello.html") ;
			%> 
			<h3> 页面将在三秒后跳转，如果没有跳转，请点<a href="hello.html"> 这里</a> </h3> 
		</body> 
	</html> 

> 如果将上述代码的跳转时间改为0，表示无条件跳转

> 定时跳转的时候，地址栏发生了改变，所以这种跳转称为客户端跳转。

上述的跳转操作，HTML本身也能够实现

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8" http-equiv="refresh" content="3;url=hello.html"> 
			<title> </title> 
		</head> 
		<body> 
			<center> 
				<h3> 页面将在三秒后跳转，如果没有跳转，请点<a href="hello.html"> 这里</a> </h3> 
			</center> 
		</body> 
	</html> 

> 当网页为静态网页时，只需要从文件系统中读取文件，没有JSP代码，这时候可以用HTML的方式头信息跳转，当动态页面时，要用response内置对象进行页面的头信息跳转操作。

<h4 id="1.4.2"> 1.4.2 页面跳转</h4> 

在JSP中除了使用头信息进行页面跳转操作之外，还可以使用response对象的sendRedirect()方法直接完成页面的跳转

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				response.sendRedirect("hello.html") ;
			%> 
		</body> 
	</html> 

> 这种跳转方式也属于客户端跳转

<h4 id="1.4.3"> 1.4.3 客户端跳转和服务器端跳转的区别</h4> 

1. `<jsp:forward> `属于服务器端跳转，`response.sendRedirect()`属于客户端跳转，前者地址栏不会发生改变，后者地址栏会发生改变。

2. 在使用request属性范围时，只有服务器端的跳转才能够将request属性保存到跳转页，如果是客户端跳转，则无法进行属性的传递。

3. 如果是服务器端的跳转，则执行到跳转句会立刻进行跳转；如果使用的是客户端跳转，则在整个页面执行完之后才执行跳转。

验证代码

jsp跳转

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				System.out.println("==================跳转之前=================") ;
			%> 
			<jsp:forward page="hello.html"/> 
			<%
				System.out.println("==================跳转之后=================") ;
			%> 
		</body> 
	</html> 

response内置对象跳转

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				System.out.println("==================跳转之前=================") ;
				response.sendRedirect("hello.html") ;
				System.out.println("==================跳转之后=================") ;
			%> 
		</body> 
	</html> 

> 在以后的代码开发中，若使用了JDBC操作，一定要在<jsp:forward> 语句执行之前关闭数据库的连接，否则数据库再也无法关闭，当达到某个程度时会“数据库连接已经达到最大异常”，这是只用重启服务器了。

> 使用<jsp:forward> 方便进行参数的传递，如果使用response内置对象跳转，只能通过地址重写的方式进行参数的传递。

<h4 id="1.4.4"> 1.4.4 操作CooKie</h4> 

> Cookie是浏览器所提供的一种技术，这种技术让服务器端的程序将一些只需要保存在客户端，或者在客户端进行处理的数据，放在本身使用的计算机中，不需通过网络的传输，因而提高了网页处理的效率，而且也减小了服务器端的负载。但是由于服务器端保存在客户端的信息，所以其安全性也很差。

Cookie定义的常用方法

| NO. | 方法 | 描述 |
| 1 | public Cookie(String name,String value) | 实例化Cookie对象，同时设置名称和内容 |
| 2 | public String getName() | 取得Cookie的名称 |
| 3 | public String getValue() | 取得Cookie的内容 |
| 4 | public void setMaxAge() | 设置Cookie的保存时间，以秒为单位 |

所有的Cookie都是由服务器端设置到客户端上去的，所以要向客户端增加Cookie，必须使用response对象的方法：`public void addCookie(Cookie cookie)`

向客户端增加Cookie

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				Cookie c1 = new Cookie("fmz","fengmengzhao") ;
				Cookie c2 = new Cookie("cll","chenliangliang") ;
				response.addCookie(c1) ;
				response.addCookie(c2) ;
			%> 
		</body> 
	</html> 

取得客户端Cookie(使用request对象)

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				Cookie[] c = request.getCookies() ;
				for(int x=0;x<c.length;x++){
			%> 
					<h2> <%=c[x].getName()%> --> <%=c[x].getValue()%> </h2> 
			<%
				}
			%> 
		</body> 
	</html> 

> 在每一个客户端访问服务器时，服务器为了明确区分每一个客户端，都会自动设置一个JSESSIONID的Cookie，表示用户的唯一身份标识。

实际上以上代码，Cookie默认是保存在浏览器上，当浏览器重新启动时，Cookie就不存在了。如果想要Cookie真正的保存在客户端上，必须使用`setMaxAge()`方法

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				Cookie c1 = new Cookie("fmz","fengmengzhao") ;
				Cookie c2 = new Cookie("cll","chenliangliang") ;
				c1.setMaxAge(60) ;
				c2.setMaxAge(60) ;
				response.addCookie(c1) ;
				response.addCookie(c2) ;
			%> 
		</body> 
	</html> 

> Cookie 并不能无限制地保存信息，一个客户单最多只能保存300个Cookie。

也可以通过设置头信息来设置Cookie

	<%
		response.setHeader("Set-Cookie","fmz=fengmengzhao") ;
	%> 

> 在web开发中，通过头信息来设置cookie并不常见，重点在response设置Cookie的操作上。

<h3 id="1.5"> 1.5 session对象</h3> 

> session 是javax.servlet.http.HttpSession 接口的实例化对象，所以session只能应用在Http协议中

httpSession接口的常用方法

| NO. | 方法 | 描述 |
| 1 | public String getId() | 取得session ID |
| 2 | public long getCreationTime() | 取得session创建时间 |
| 3 | public long getLaseAccessedTime() | 取得session最后一次操作时间 |
| 4 | public boolean isNew() | 判断是否为新的session(新用户) |
| 5 | public void invalidate() | 让session实效 |
| 6 | public Enumeration getAttributeNames() | 取得全部属性的名称 |

> 有一些方法在doc文档中都标记有Deprecated进行声明，标明这些方法在JSP版本升级中都已经过时了。

<h4 id="1.5.1"> 1.5.1 取得session ID</h4> 

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String id = session.getId() ;
			%> 
			<h2> SESSION ID：<%=id%> </h2> 
			<h2> SESSION 长度：<%=id.length()%> </h2> 
		</body> 
	</html> 

> 在使用session操作时，实际上使用了Cookie处理机制，即在客户端的Cookie中保存着每一个SESSION ID，这样用户每次在发送请求时，都会将此SESSION ID 发送到服务器端，服务器端依靠此SESSION ID区别每一个不同的客户端。

对于每一个已经连接到服务器的用户，如果重启服务器，则这些用户再次发出请求实际上表示的都是一个新连接的用户，服务器会为每一个用户分配一个新的SESSION ID

<h4 id="1.5.2"> 1.5.2 重启服务器之后继续使用session</h4> 

在server.xml文件中加入代码

	<Context path="/fmz" docBase="D:\fmzwebDemo"> 
		<Manager className="org.apache.catelina.session.PersistentManager"> 
			debug=0	saveOnRestart="true"
			maxActiveSession="-1"	minIdleSwap="-1"
			maxIdleSwap="-1"	maxIdleBackup="-1"									      
			<Store className="org.apache.catalina.session.FileStore" directory="d:\temp"/> 
		</Manager> 
	</context> 

> 以上代码可以通过序列化操作将，session内容序列化后保存在临时文件中。

<h4 id="1.5.3"> 1.5.3 登录及注销</h4> 

session实现用户登录及注销的功能思路：当用户登录成功后，设置一个session范围属性，然后在其他需要验证的页面中判断是否存在此session范围的属性，如果存在则表示已经是正常登陆过的合法用户；如果不存在，给出提示，并跳转到登录页重新登录

表单及验证代码-login.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="login.jsp" method="post"> 
				用户名：<input type="text" name="uname"> <br> 
				密&nbsp;码：<input type="password" name="upass"> <br> 
				<input type="submit" value="登录"> 
				<input type="reset" value="重置"> 
			</form> 
			<%
				String name = request.getParameter("uname") ;
				String password = request.getParameter("upass") ;
				if(!(name == null || password == null)){
					if(name.equals("fmz") && password.equals("799520")){
						response.setHeader("refresh","2;URL=welcome.jsp") ;
						session.setAttribute("userid",name) ;
			%> 
						<h3> 用户登录成功，两秒后跳转到欢迎页！</h3> 
						<h3> 如果没有跳转，请按<a href="welcome.jsp"> 这里</a> </h3> 
			<%
					}else{
			%> 
							<h3> 错误的用户名或密码！</h3> 
			<%
					}
				}
			%> 
		</body> 
	</html> 

欢迎页-welcome.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String userid = (String)session.getAttribute("userid") ;
				if(userid != null){
			%> 
					<h3> 欢迎<%=userid%> 光临本系统，<a href="logout.jsp"> 注销</a> ！</h3> 
			<%
				}else{
			%> 
						<h3> 您已退出系统，请先进行系统的<a href="login.jsp"> 登录</a> ！</h3> 
			<%
				}
			%> 
		</body> 
	</html> 

注销页-logout.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				response.setHeader("refresh","2;URL=login.jsp") ;
				session.invalidate() ;
			%> 
			<h3> 您也成功退出本系统，两秒后跳转到首页！</h3> 
			<h3> 若系统没有跳转，请点<a href="login.jsp"> 这里</a> ！</h3> 
		</body> 
	</html> 

<h4 id="1.5.4"> 1.5.4 在web开发中一共存在4种会话跟踪技术</h4> 

1. 通过session提供的方法保存

2. 使用Cookie保存信息

3. 通过表单的隐藏域保存信息

4. 通过地址的重写方式保存信息

<h4 id="1.5.5"> 1.5.5 判断新用户(isNew())</h4> 

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				if(session.isNew()){
			%> 
					<h2> 欢迎新用户光临！</h2> 
			<%
				}else{
			%> 
						<h2> 您是我们的老客户！</h2> 
			<%
				}
			%> 
		</body> 
	</html> 

<h4 id="1.5.6"> 1.5.6 取得用户的操作时间(getCreationTime()、getLastAccessedTime())</h4> 

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				long start = session.getCreationTime() ;
				long end = session.getLastAccessedTime() ;
				long time = (end - start)/1000 ;
			%> 
			<h2> 您已经在本站停留了<%=time%> 秒！</h2> 
		</body> 
	</html> 

> 方法`getCreationTime()`：用户第一次连接到服务器时，服务器会自动保留一个session的创建时间。

<h3 id="1.6"> 1.6 application对象</h3> 

> application 是javax.servlet.ServletContext接口的实例化对象，表示整个Servlet的上下文

application对象的主要方法

| NO. | 方法 | 描述 |
| 1 | String getRealPath(String path) |	得到虚拟目录对应的绝对路径 |
| 2 | public Enumeration getAttributeNames() | 得到所有属性的名称 |
| 3 | public String getContextPath() | 取得当前的虚拟路径的名称 |

<h4 id="1.6.1"> 1.6.1 取得虚拟目录对应的绝对路径</h4> 

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String path = application.getRealPath("/") ;
			%> 
			<h2> 真实路径：<%=path%> </h2> 
		</body> 
	</html> 

> `\`是根目录的意思

在web中还可以通过`this.getServletContext().getRealPath("/")`取得真实(绝对)路径

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String path = this.getServletContext().getRealPath("/") ;
			%> 
			<h2> 真实路径：<%=path%> </h2> 
		</body> 
	</html> 

> ServletCotext本身就表示真个容器，this是本容器的意思。

> 两种方法实现的功能是一样的，但是在开发中尽量使用getServletContext()方法获取绝对路径，这对以后程序的理解非常有帮助。

<h4 id="1.6.2"> 1.6.2 简单的文件操作</h4> 

表达文件

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="input_content.jsp" method="post"> 
				输入文件名称：<input type="text" name="fileName"> <br> 
				输入文件内容：<textarea name="fileContent" rows="3" cols="30"> </textarea> <br> 
				<input type="submit" value="提交"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

将上述表单文件内容保存并输出

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.io.*"%> 
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
				String name = request.getParameter("fileName") ;
				String content = request.getParameter("fileContent") ;
				String filePath = this.getServletContext().getRealPath("/")+"note"+File.separator+name ;
				File file = new File(filePath) ;
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdir() ;
				}
				PrintStream ps = null ;
				ps = new PrintStream(new FileOutputStream(file)) ;
				ps.println(content) ;
				ps.close() ;
			%> 
			<%
				Scanner scan = new Scanner(new FileInputStream(file)) ;
				scan.useDelimiter("\n") ;
				StringBuffer buf = new StringBuffer() ;
				while(scan.hasNext()){
					buf.append(scan.next()).append("<br> ") ;
				}
				scan.close() ;
			%> 
			<h2> <%=buf%> </h2> 
		</body> 
	</html> 

> 上述内容通过PrintStream进行保存，又通过Scanner进行输出。

> 在HTML文件总通过<br> 进行换行，所以要加上代码`<br> `才能按照正常的换行格式进行读取。

<h4 id="1.6.3"> 1.6.3 网站计数器程序(范例讲解)</h4> 

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.io.*"%> 
	<%@ page import="java.util.*"%> 
	<%@ page import="java.math.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%!
				BigInteger count = null ;
			%> 
			<%!
				public BigInteger load(File file){
					BigInteger count = null ;
					try{
						if(file.exists()){
							Scanner scan = null ;
							scan = new Scanner(new FileInputStream(file)) ;
							if(scan.hasNext()){
								count = new BigInteger(scan.next()) ;
							}
							scan.close() ;
						}else{
							count = new BigInteger("0") ;
							save(file,count) ;
						}
					}catch(Exception e){
						e.printStackTrace() ;
					}
					return count ;
				}
				public void save(File file,BigInteger count){
					try{
						PrintStream ps = null ;
						ps = new PrintStream(new FileOutputStream(file)) ;
						ps.println(count) ;
						ps.close() ;
					}catch(Exception e){
						e.printStackTrace() ;
					}
				}
			%> 
			<%
				String fileName = this.getServletContext().getRealPath("/")+"note"+File.separator+"count.txt" ;
				File file = new File(fileName) ;
				if(session.isNew()){
					synchronized(this){//必须进行同步操作
						count = load(file) ;
						count = count.add(new BigInteger("1")) ;//自增操作
						save(file,count) ;
					}
				}
			%> 
			<center> 
				<h2> 您是第<%= count==null?0:count %> 位访客！</h2> 
			</center> 
		</body> 
	</html> 

用字符串操作的代码

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.io.*"%> 
	<%@ page import="java.util.*"%> 
	<%@ page import="java.math.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%!
				String count = null ;
			%> 
			<%!
				public String load(File file){
					String count = null ;
					try{
						if(file.exists()){
							Scanner scan = null ;
							scan = new Scanner(new FileInputStream(file)) ;
							if(scan.hasNext()){
								count = scan.next() ;
							}
							scan.close() ;
						}else{
							count = "0" ;
							save(file,count) ;
						}
					}catch(Exception e){
						e.printStackTrace() ;
					}
					return count ;
				}
				public void save(File file,String count){
					try{
						PrintStream ps = null ;
						ps = new PrintStream(new FileOutputStream(file)) ;
						ps.println(count) ;
						ps.close() ;
					}catch(Exception e){
						e.printStackTrace() ;
					}
				}
			%> 
			<%
				String fileName = this.getServletContext().getRealPath("/")+"note"+File.separator+"count01.txt" ;
				File file = new File(fileName) ;
				if(session.isNew()){
					synchronized(this){//必须进行同步操作
						count = load(file) ;
						int temp = Integer.parseInt(count) ;
						temp++ ;
						count = String.valueOf(temp);//自增操作
						save(file,count) ;
					}
				}
			%> 
			<center> 
				<h2> 您是第<%= count==null?0:count %> 位访客！</h2> 
			</center> 
		</body> 
	</html> 

> 一定要勤于查看jdk文档，查找的过程就是进步的过程

<h4 id="1.6.4"> 1.6.4 查看application范围的属性</h4> 

在application对象中，提供了`getAttributeNames()`方法，可以取得全部的属性名称

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
				Enumeration enu = application.getAttributeNames() ;
				while(enu.hasMoreElements()){
					String name = (String)enu.nextElement() ;
			%> 
				<h6> <%=name%> --> <%=this.getServletContext().getAttribute(name)%> </h6> 
				//<h6> <%=name%> --> <%=application.getAttribute(name)%> </h6> 
			<%
				}
			%> 
		</body> 
	</html> 

<h3 id="1.7"> 1.7 web安全性及config对象</h3> 

虚拟目录中有一个WEB-INF文件夹，此文件夹用户无法访问到，一次将文件存放在这里是最安全的，但是无法访问，如果想要访问，则需要通过一个映射进行操作

增加配置文件到WEB-INF\web.xml中

	<servlet> 
		<servlet-name> temp</servlet-name> 
		<jsp-file> /WEB-INF/hello.jsp</jsp-file> 
	</servlet> 
	<servlet-mapping> 
	<servlet-name> temp</servlet-name> 
		<url-pattern> /hello.fmz</url-pattern> 
	 </servlet-mapping> 

> 输入http://localhost/fmz/hello.fmz即可访问WEB-INF\hello.jsp文件

> <servlet-name> 节点的主要作用是连接<servlet> 和<servlet-mapping> ，此节点的名称只在配置文件内部起作用，并且不能重复命名

<h4 id="1.7.1"> 1.7.1 config对象</h4> 

> config对象是javax.servlet.ServletConfig接口的实例化对象，主要的功能是取得一些初始化的配置信息，

常用方法

| NO. | 方法 | 描述
| 1 | public String getInitParameter(String name) | 取得指定名称的初始化参数内容 |
| 2 | public Enumeration getInitParameterNames() | 取得全部初始化参数内容 |

所有的初始化参数必须在web.xml中配置，如果一个JSP文件想要取得初始化参数内容，一定要在web.xml文件中完成映射

增加配置文件到WEB-INF\web.xml中

	<servlet> 
		<servlet-name> dbint</servlet-name> 
		<jsp-file> /WEB-INF/init.jsp</jsp-file> 
		<init-param> 
			<param-name> driver</param-name> 
			<param-value> org.gjt.mm.mysql.Driver</param-value> 
		</init-param> 
		<init-param> 
			<param-name> url</param-name> 
			<param-value> jdbc:mysql://localhost:3306/fmz</param-value> 
		</init-param> 
	</servlet> 
	<servlet-mapping> 
		<servlet-name> dbint</servlet-name> 
		<url-pattern> /config.fmz</url-pattern> 
	</servlet-mapping> 

> 映射文件配置后，需要重新启动服务器

取得参数文件-WEB-INF\init.jsp

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
				Enumeration enu = config.getInitParameterNames() ;
				while(enu.hasMoreElements()){
					String name = (String)enu.nextElement() ;
			%> 
					<center> 
						<h3> <%=name%> --> <%=config.getInitParameter(name)%> </h3> 
					</center> 
			<%
				}
			%> 
		</body> 
	</html> 

> 直接访问http://localhost/fmz/config.fmz即可取得初始化属性内容

<h3 id="1.8"> 1.8 out对象</h3> 

> out对象是javax.servlet.jsp.JspWriter类的实例化对象，主要功能为完成页面的输出工作，在可发中很少使用。

out对象取得缓冲区的大小

<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				int buffer = out.getBufferSize() ;
				int available = out.getRemaining() ;
				int use = buffer - available ;
			%> 
			<h3> 缓冲区的大小：<%=buffer%> </h3> 
			<h3> 可用缓冲区的大小：<%=available%> </h3> 
			<h3> 正在使用缓冲区的大小：<%=use%> </h3> 
		</body> 
	</html> 

<h3 id="1.9"> 1.9 pageContext对象</h3> 

>  pageContext对象是javax.servlet.jsp.PageContext类的实例，

pageContext对象的主要方法

| NO. | 方法 | 描述 |
| 1 | public abstract void forward(String relativeUrlPath)throws ServletException,IoException | 页面跳转 |
| 2 | public void include(String relativeUrlPath)throws ServletException,IoException | 页面包含 |
| 3 | public ServletConfig getServletConfig() | 取得ServletConfig对象 |
| 4 | public ServletContext getServletContext() | 取得ServletContext对象 |
| 5 | public ServletRequest getRequest() | 取得ServletRequest对象 |
| 6 | public HttpSession getSession() | 取得HttpSession对象 |

示例代码

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.forward("pagecontext_forward_demo02.jsp?name=fmz") ;
			%> 
		</body> 
	</html> 

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

> 实现页面的跳转

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String name= pageContext.getRequest().getParameter("name") ;
				String realPath = pageContext.getServletContext().getRealPath("/") ;
			%> 
			<center> 
				<h3> name：<%=name%> </h3> 
				<h3> realPath：<%=realPath%> </h3> 
			</center> 
		</body> 
	</html> 

>  实现接收参数和绝对路径的获取
