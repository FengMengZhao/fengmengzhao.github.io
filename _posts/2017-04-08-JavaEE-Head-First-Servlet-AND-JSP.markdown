---
layout: post
title: JavaEE之Head First Servlet AND JSP
---

### web容器能提供什么？

1. 通信支持，利用web容器提供的方法，你可以轻松的让Servlet与web服务器对话，而不用自己建立ServerSocket监听某个端口、创建流等。
2. 声明周期管理，容器控制着Servlet的生与死，它会负责加载类、实例化和初始化Servlet、调用Servlet方法以及使Servlet实例能够被垃圾收回。有了容器的控制就不用考虑资源的管理了。
3. 多线程支持，容器会自动为它接收的每个Servlet创建一个新的java线程，由服务器创建和管理多个线程来处理多个请求。
4. 声明方式实现安全，可以用DD文件来配置和修改安全性，而不必将硬编码写到类代码中。
5. JSP支持，由容器负责将JSP代码翻译成真正的JAVA。

一个完全兼容的J2EE应用服务器必须有一个web容器和一个EJB容器(还有其他的一些东西，包括JNDI和JMS的实现等)，Tomcat是一个web容器而不是一个完整的J2EE应用服务器，J2EE1.4服务器包括Servlet2.4规范、JSP2.0规范以及EJB2.1规范。

### Servlet

#### ServletConfig对象

每一个Servlet都有一个ServletConfig对象；用于向Servlet传递部署信息(例如：数据库或者企业Bean的查找名)，而你不想把这个信息写到硬编码的Servlet中，称为Servlet初始化参数。

#### ServletContext

每一个web应用都有一个ServletContext；用于访问web应用参数(也是在DD中配置)；相当于应用的一个公告栏，可以在这里放消息(称为属性)，应用的其他部分可以访问这些消息；用于得到服务器信息，包括容器的名字和版本以及所支持的API版本等。

> ServletConfig和ServletContext存在只是为了支持Servlet的真正任务：处理客户请求。

#### 幂等

幂等的意思是：你可以一遍又一遍的反复做同一个事情，而不会有预想不到的副作用。

get方法只是要得到一些东西，不会修改服务上的任何内容，所以它能执行多次，是幂等的；相反post方法相当于更新，不是幂等的。

#### HttPServletRequest对象

#### HttPServletResponse对象

setHeader()方法会覆盖现有的值，addHeader()会增加另外一个值

sendRedirect()使用的是相对URL，sendRedirect()方法不能在响应之后再调用，否则会抛出IllegalStateException，重定向是让浏览器工作。

请求分派是在服务器端工作，重定向=客户，请求分派=服务器

### 属性和监听

容器初始化一个Servlet时，会为这个Servlet建一个唯一的ServletConfig；容器从DD文件中读取Servlet初始化参数并把这个参数交给ServletConfig，然后把ServletConfig传递给Servlet的Init()方法。

#### 上下文初始化参数

##### Servlet初始化参数和上下文初始化参数的区别

1. 上下文初始化参数：`<web-app ...><context-param><param-name></param-name><param-value></param-value></context-param></web-app>`；Servlet初始化参数：`<web-app ...><servlet><servlet-name></servlet-name></servlet-class></servlet-class><init-param></init-param></servlet></web-app>`
2. 每一个Servlet一个ServletConfig，每个web应用一个ServletContext，整个web应用只有一个ServletContext，而且web应用中的所有部分都能访问它。

##### Servlet初始化

1. 容器读取Servlet的部署描述文件，包括Servlet的初始化参数。
2. 容器为Servlet创建一个新的ServletConfig实例
3. 容器为每个Servlet初始化参数创建一个String名/值对(假设只有一个初始化参数)
4. 容器向ServletConfig提供名/值对的引用
5. 容器创建一个Servlet的新实例
6. 容器调用Servlet的init()方法，传入ServletConfig的引用

##### web应用的初始化

1. 容器读取DD，为每一个context-param创建名/值对
2. 容器创建ServletContext的一个新实例
3. 容器为ServletContext提供上下文初始化参数各个名/值对的引用
4. 在web应用中部署各个Servlet和JSP都能访问的同样的ServletContext

#### 如果希望应用初始化参数是一个数据库(对象)而不是一个简单的String呢？

##### ServletContextListener

上下文监听很简单只要实现ServletContextListener即可

##### 上下文监听实例

模型代码：

	package org.fmz.listener.vo ;

	public class Dog{
		private String breed ;
		public Dog(String breed){
			this.breed = breed ;
		}
		public void setBreed(String breed){
			this.breed = breed ;
		}
		public String getBreed(){
			return this.breed ;
		}
	}

监听类代码：

	package org.fmz.listener ;

	import org.fmz.listener.vo.* ;
	import javax.servlet.* ;

	public class MyServletContextListener implements ServletContextListener{
		public void contextInitialized(ServletContextEvent event){
			ServletContext sc = event.getServletContext() ;
			String breed = sc.getInitParameter("breed") ;
			Dog dog = new Dog(breed) ;
			sc.setAttribute("dog",dog) ;
		}
		public void contextDestroyed(ServletContextEvent event){
		
		}
	}

测试类Servlet代码：

	package org.fmz.listener.servlet ;

	import java.io.* ;
	import javax.servlet.* ;
	import javax.servlet.http.* ;

	import org.fmz.listener.vo.* ;

	public class MyServletContextListenerTest extends HttpServlet{
		public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,java.io.IOException{
			response.setContentType("text/html") ;
			Dog dog = (Dog)getServletContext().getAttribute("dog") ;
			PrintWriter out = response.getWriter() ;
			out.println(dog.getBreed()) ;
		}
	}

>上述代码能够实现上下文的监听，web应用都能够共享。

##### HttpSessionBindingListener

你有一个属性类(这个类表示的对象被放在一个属性中)，而且你希望这个类型的对象绑定到一个会话或者从会话中删除时得到通知。

##### 到底什么是属性

属性就是一个对象，设置或者说绑定到另外3个Servlet API对象中：ServletContext、HttpServletRequest(或ServletRequest)、HttpSession。

##### 属性和参数的区别

1. 属性的类型：应用/上下文、请求、会话；设置方法：serAttribute()；返回类型：Object；获取方法：getAttribute()，切记要强制转型。

2. 参数的类型：应用/上下文初始化参数、请求参数、Servlet初始化参数；设置方法；不能设置应用和Servlet初始化参数，它们都是在DD文件中设置，对于请求参数可以调整查询串；返回类型：String，这是属性和参数一个很大的区别；获取方法：getInitParam()。

##### 属性的三个作用域：上下文、请求、会话

上下文属性：应用中的每一个部分都能访问；会话属性：能访问特定HttpSession的部分才能访问；请求属性：能访问特定ServletRequest的部分才能访问。

##### 作用域的线程安全性

1. 上下文作用域不是线程安全的，保护上下文属性的一般做法是对上下文对象本身同步，就相当于对上下文对象上了一把锁，这样就能保证一次只有一个线程能够得到或者设置上下文属性，还有一个条件是当处理上下文属性的所有其他代码也对ServletContext同步时，这个方法才能奏效。代码：`synchronized(getServletContext()){//上下文属性设置及获得代码}`

2. 会话作用域也不是线程安全的，保护会话属性的通上下文属性一样需要同步会话的所有代码：`synchronized(session){//属性的设置及获取代码}`。

3. 只有请求属性和局部变量是线程安全的

> 实例变量不是线程安全的。

> 切记，只会有一个Servelt实例，但是可以有多个线程。

> 如果希望所有的线程都能访问一个值，通常可以这么做：把变量声明为服务方法中的局部变量，而不是一个实例变量；在合适的的作用域内使用一个属性。

#### 请求属性和请求分派

##### RequestDispatcher

>ServletRequest中RequestDispatcher()方法需要一个String路径作为参数，这个路径是相对路径，也就是说如果有`/`就是根目录下的，如果没有则表示路径是相对于原来的请求，这时候就要注意Servlet DD部署文件中的映射路径问题了。

#### HttpSession

HttpSession session = Request.getSession()，如果请求包含了一个会话ID cookie，则找到与此会话匹配的当前会还否则创建一个新的会话。如果是只想要一个已经有的Session，则使用方法：`HttpSession session = request.getSession(false)`，此方法只会返回一个已经有的会话，如果会话不存在则返回null。

如果是浏览器禁用coolie，则还有URL重写者一条后路来保证客户端与服务器端建立会话联系

> 一般的容器都是先尝试cookie方法建立会话，如果cookie禁用则使用url重写的方法建立会话，不过第一次容器同时用两种方法来上一个上保险，因为只有取得会话才能克服http协议的无状态性。

##### 删除会话

会话有三中死法：超时、你再会话中调用invalidate()、应用结束

1. 在DD中配置会话超时：`<web-app...><session-config>15</session-config></web-app>`，这里的参数是分钟
2. 设置一个特定的会话会超时：`session.setMaxInactiveInterval(20*60)`，这里的参数是秒

#### Cookie的其他作用

coolie其实是客户和服务端之间交换的小数据(一个名/值String对)，客户的浏览器退出时，会话Cookie就会消失，但是你可以告诉Cookie在客户端上呆的更久一点，甚至在浏览器关闭之后还能持久保存。

#### 会话绑定监听(HttpSessionBindingListener)

例如：你的属性(通常是一个对象)自己什么时候增加到一个会话中，从而与一个底层数据库同步，而属性从会话中删除时，能更新数据库

#### 会话迁移

只有HttpSession对象及其属性会从一个VM移到另外一个VM上

> 每一个VM有一个ServletContext。每一个VM的每一个Servlet上有一个ServletConfig。

### 作为JSP

JSP中的隐式对象与API文档中的类相对应

JspWriter out ；HttpServletRequest request ；HttpServletResponse response ；HttpSession session ；ServletContext application ；ServletConfig congig ；JSPException exception ；pageContext pageContext ；Object page。

容器根据你的JSP生成一个类，这个类实现了`HttpJspPage`接口，在JSP的整个生命周期中，整个转化为java和编译为Class文件只会发生一次。

#### 配置JSP的Servlet初始化参数

	<web-app...>
		<servlet-name></servlet-name>
		<jsp-file></jsp-file>
		<init-param>
			<param-name></param-name>
			<param-value></param-value>
		</init-param>
	</web-app>

#### 获取JSP的Servlet初始化参数-对jspInit()方法进行覆写

	<%!
		public void jspInit(){
			ServletConfig config = getServletConfig() ;
			String email = config.getInitParam("email") ;
			ServletContext ctx = getServletContext() ;
			ctx.setAttribute("eamil",email) ;
		}
	%>

#### JSP中的属性

JSP属性共有四个作用域：上下文、请求、会话和页面作用域，而Servlet属性只有三个作用域：上下文、请求和会话。

在Servlet和JSP中分别设置不同属性的作用域

1. 应用，Servlet中：`getServletContext().setAttribute("foo","barObj")`；JSP中：`application.setAttribute("foo","barObj")`
2. 请求，Servlet中：`request.setAttribute("foo","barObj")`；JSP中：`request..setAttribute("foo","barObj")`
3. 会话，Servlet中：`request.getHttpSession().setAttribute("foo","barObj")`；JSP中：`session.setAttribute("foo","barObj")`
4. 页面，Servlet中：不适用；JSP中：`pageContext.setAttribute("foo","barObj")`

>但是这还不是全部，还有一种方法来设置和获取任意作用域的属性，可以使用pageContext隐式对象

#### 使用pageContext得到任意作用域的对象

可以通过方法`public abstract void setAttribute(java.lang.String name,java.lang.Object value,int scope)`，scope有四种：PAGE_SCOPE、REQUEST_SCOPE、SESSIO_SCOPE、APPLICATION_SCOPE

pageContext中的findAttribute()方法，首先会在最严格的page作用域查找属性、然后是请求作用域、然后是会话作用域、最后是应用作用域，一旦在某个作用域找的属性则不再继续查找。

#### JSP指令：page、taglib、include

page指令的重要属性：import、isThreadSafe、contentType、isELIgnore、isErrorPage、errorPage

#### 使用scripting-invalidate，在DD文件中配置就可以禁用Script、java或者声明

	<web-app...>
		<jsp-config>
			<jsp-property-group>
				<url-pattern>*.jsp</url-pattern>
				<scripting-invalidate>true</scripting-invalidate>
			</jsp-property-group>
		</jsp-config>
	</web-app>

#### 禁用EL

	<web-app...>
		<jsp-config>
			<jsp-property-group>
				<url-pattern>*.jsp</url-pattern>
				<el-ignored>true</el-ignored>
			</jsp-property-group>
		</jsp-config>
	</web-app>

>在jsp中page指令也可以完成禁用EL：`<%@ page isELIgnored="true"%>`

### 无脚本的JSP

`<jsp:useBean id="" class="" scope`/>，这称之为JSP的标准动作，如果没有找到一个名为id属性的对象就会创建一个对象名为id属性的对象。

`<jsp:setProperty name="" property=""/>`可以作为useBean体内容，表示有条件的创建，如果是新建的一个Bean就会运行体的内容。

`<jsp:useBean id="" type="接口/抽象类" class="实现类" scope=""`，如果属性中只有type而没有Class，那么bean必须已经存在。

#### 表单文件直接提价到JSP文件中，不经过Servlet。可以这样做：

	<jsp:useBean id="person" type="foo.Person" class="foo.Employee">
		<jsp:setProperty name="person" property="*"/>		自动进行属性的匹配
	</jsp:useBean>

>bean标记具有自动装换基本类型的性质，能够自动将取得的表单String参数转化为int数据类型。

#### 表达式语言

##### 表达式语言规则

1. EL表达式总是放在一个大括号里，而且前面有一个$符号前缀
2. 表达式的第一个命名变量可以是一个EL隐式对象，也可以是一个属性。EL隐式对象有：pageScope requestScope sessionScope applicationScope param paramValues header headerValues cookie initParam pageContext，这些隐式对象只有pageContext不是映射，而是pageContext对象的实际引用，pageContext是一个JavaBean。EL隐式对象和JSP脚本中的隐式对象不同，只有pageContext除外。如果EL表达式的第一项是一个属性，则可以是保存在任意一个作用域中的属性
3. 使用(.)操作符访问性质和映射值。如果第一个变量是映射，则点号右边可以是一个映射键；如果第一个变量是一个JavaBean属性，则点号右边可以是一个bean性质。
4. 点号左边的变量要么是一个Map(有键)，要么是一个bean(有性质)，无论变量是一个隐式对象还是一个属性，都是如此。pageContext隐式对象是一个bean，它有获取方法，其他的饮食对象都是一个MAP
5. []就好像是更好的点号。如果表达式中变量的后面有一个[]，左侧的变量则有更多的选择，可以是：MAP、bean、List或者数组；如果中括号中是一个String类的直接量，则这个字符串可以是：Map键、bean性质还可以是List或者数组中的索引；如果是List或者数组的String索引，则可以自动转化为int。
6. bean或者MAP也可以使用[]，这个时候可以用字符串索引或者.键值/bean性质，二者都可以。
7. 如果[]中不是String直接量，就会计算。容器会搜索与改名字绑定的属性，并替换为这个属性的值。
8. 在[]中还可以使用嵌套表达式。

#### EL中的请求参数

如果对应给定的一个参数名只有一个参数，可以使用param对象；如果对应给定的一个参数性有多个参数值，就要使用paramValues。

注意：EL的隐式对象initParam指的是上下文中的初始化参数而不是Servlet中的初始化参数。

#### EL函数

1. 编写一个公共的静态方法java类
2. 编写一个TLD文件：`<taglib...><uri></uri><function><name></name><function-class></function-class><function-signature>返回类型 方法()</function-signature></function></taglib>`
3. 在JSP中放一个taglib指令：`<%@ taglib uri="不必是文件的实际路径">`
4. 使用EL调用函数，按照：`${prefix:name()}`的形式就可以了。

#### EL能妥善的处理Null值

#### include指令和jsp:include标准动作

include指令在转化时发生，jsp:include标准动作在运行时发生。jsp:include的关键是，容器要根据页面属性创建一个请求分派器，并应用include方法，所分派和包含的JSP针对同样的请求和响应对象在同一线程中执行。也就是说：include指令在转换时插入header.jsp源码，而include标准动作在运行时插入header.jsp的响应。

> 对于include指令和jsp:include标准指令，其位置都是敏感的。采用include指令，被包含页面的源代码将成为有include指令的外围页面的一部分。

>不要把开始和结束的HTML和body标记放在可重用标记中，设计和编写局部模板部件时(如页眉、导航等)，要假设它们会包含在其他的页面中。

#### 使用jsp:param定制包含的内容。

	<jsp:include page="header.jsp">
		<jsp:param name="subTitle" value=""/>
	</jsp:include>

#### jsp:forward标准动作

> 利用jsp:forward，缓冲区会在转发前清空。

	<jsp-config>
		<taglib>
			<taglib-uri>http://fmz.org/jsp/jstl/hello</taglib-uri>
			<taglib-location>/WEB-INF/tags/hellotag.tld</taglib-location>
		</taglib>
	</jsp-config>
