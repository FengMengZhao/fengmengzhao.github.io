---
layout: post
title: javaweb开发高级篇之JSP标签编程
---

## 目录

- [1 标签编程简介](#1)
- [2 定义一个简单的标签](#2)
- [3 定义有属性的标签](#3)
- [4 TagSupport类](#4)
- [5 定义有标签体的标签库](#5)
- [6 开发迭代标签](#6)
- [7 BodyTagSupport类](#7)
- [8 TagExtraInfo和VariableInfo类](#8)
- [9 使用BodyTagSupport类开发迭代输出](#9)
- [10 简单标签](#10)
- [11 DynamicAttribute接口](#11)

---

---

<h2 id="1"> 1 标签编程简介</h2> 

JSP的开发就是在HTML代码中嵌入了大量的java代码，但是这样维护起来非常的不方便，而使用JSP标签库编程的主要目的是减少页面中Scriptlet代码，使程序更加容易理解和修改。

<h2 id="2"> 2 定义一个简单的标签</h2> 

要想实现一个标签，可以直接继承javax.servlet.jsp.tagext.TagSupport类，如果定义的标签内没有标签体，可以直接覆写TagSupport类中的doStartTag()方法

> 没有标签体的标签，例如：<jsp:forward page=""/> 

定义标签的操作类-HelloTag.java

	package org.fmz.tagdemo ;

	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class HelloTag extends TagSupport{
		public int doStartTag()throws JspException{
			JspWriter out = super.pageContext.getOut() ;//取得页面的输出流对象
			try{
				out.println("<h3> Hello Word！！！</h3> ") ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
			return TagSupport.SKIP_BODY ;
		}
	}

> 此程序TagSupport类中的pageContext对象取得了当前页面的输出对象进行输出，由于此标签没有任何标签体，所以在程序的最会返回SKIP_BODY常量，表示不执行标签体的内容。

标签类定义完成之后需要标签描述文件(Tag Library Discriptor，TLD)，在*.tld文件中，可以描述标签的名称、简介、处理类和标签使用到的各个属性等。

定义标签描述文件-WEB-INF/hellotag.tld

	<?xml version="1.0" encoding="UTF-8"?> 
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_1.xsd"
	    version="2.1"> 
	    <tlib-version> 1.0</tlib-version> 				表示标签库的版本
	    <short-name> firsttag</short-name> 				表示标签库在TLD中描述的名称
	    <tag> 				
		<name> hello</name> 					表示标签在JSP中使用的名称
		<tag-class> org.fmz.tagdemo.HelloTag</tag-class> 		表示标签所指向的class文件
		<body-content> empty</body-content> 			表示标签体的内容为空
	    </tag> 
	</taglib> 

JSP使用标签的格式：

`<%@ taglib prefix="标签前缀" uri="TLD文件路径"%> `

> prefix表示标签使用时的前缀，这一点与<jsp:forward> 中的jsp类似，而uri表示此标签对应的*.tld文件的路径

编写JSP文件调用标签-hellotag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="mytag" uri="/WEB-INF/hellotag.tld"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<mytag:hello/> 
		</body> 
	</html> 

由于编写TLD文件的引用非常麻烦：`uri="/WEB-INF/hellotag.tld"`，可以采用映射的方法简化

修改web.xml文件，映射TLD文件

	<jsp-config> 
		<taglib> 
			<taglib-uri> hellotag</taglib-uri> 
			<taglib-location> /WEB-INF/hellotag.tld</taglib-location> 
		</taglib> 
	</jsp-config> 

修改JSP文件：`<%@ taglib prefix="mytag" uri="hellotag"%> `，即可以访问TLD文件

完成标签操作需要的步骤：

1. 标签处理类，HelloTag.java

2. 标签描述文件：hellotag.tld

3. JSP页面，通过<%@ taglib%> 完成

4. 在web.xml文件中配置映射名称(可选)

---

---

<h2 id="3"> 3 定义有属性的标签</h2> 

在<jsp:forward page=""/> 跳转语句中，可以通过page属性指定一个跳转路径，在用户自定义标签时也可以定义属性。

开发一个格式化显示日期的标签

格式化日期标签类-DateTag.java

	package org.fmz.tagdemo ;

	import java.util.- ;
	import java.text.- ;
	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class DateTag extends TagSupport{
		private String format ;
		public int doStartTag() throws JspException{
			SimpleDateFormat sdf = new SimpleDateFormat(this.format) ;
			try{
				super.pageContext.getOut().write(sdf.format(new Date())) ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
			return TagSupport.SKIP_BODY ;
		}
		public void setFormat(String format){
			this.format = format ;
		}
		public String getFormat(){
			return this.format ;
		}
	}

> 在本类中定义了一个format属性，并且编写了setter和getter方法，当用户通过变迁设置属性时，就会调用其中的setter方法完成属性的设置，实际上时java的反射机制在起作用。

定义标签描述文件-datetag.tld

	<?xml version="1.0" encoding="UTF-8"?> 
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_3_1.xsd"
	    version="3.1"> 
	    <tlib-version> 1.0</tlib-version> 
	    <short-name> datetag</short-name> 
	    <tag> 
			<name> date</name> 
			<tag-class> org.fmz.tagdemo.DateTag</tag-class> 
			<body-content> empty</body-content> 
			<attribute> 
				<name> format</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
	    </tag> 
	</taglib> 

配置web.xml文件
		<jsp-config> 
			<taglib> 
				<taglib-uri> datetag</taglib-uri> 
				<taglib-location> /WEB-INF/datetag.tld</taglib-location> 
			</taglib> 
		</jsp-config> 

测试格式化日期-datetag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="mytag" uri="datetag"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h1> <mytag:date format="yyyy-MM-dd HH:mm:ss.SSS"/> </h1> 
		</body> 
	</html> 

> 关于TLD文件中的<rtexprvalue> 元素的主要功能是定义format属性是否支持表达式输出，如果设置成true，则可以通过如下代码完成format属性内容的设置

	<% pageContext.setAttribute("fm","yyyy-MM-dd HH:mm:ss.SSS"); %> 
	<mytag:date format="${fm}"/> 
	或者
	String fm = "yyyy-MM-dd HH:mm:ss.SSS" ;
	<mytag:date format="<%=fm%> "/> 

---

---

<h2 id="4"> 4 TagSupport类</h2> 

在整个TagSupport类中，doStartTag()、doEndTag()、doAfterBody()和release()4个方法最为重要：

doStartTag()：此方法有两个返回值，一个是SKIP_BODY，另外一个事EVAL_BODY_INCLUDE，前者表示忽略标签体的内容，交给doEndTag()方法处理；后者表示执行标签体的内容。

doAfterBody()：用来重复执行标签体的内容，有两个返回值，一个是SKIP_BODY，另外一个是EVAL_BODY_AGAIN。返回后者重复调用doAferBody()方法，一直循环下去直到doAfterBody()返回SKIP_BODY值

doEndTag()：此方法在标签结束时使用，有两个返回值，一个SKIP_PAGE，表示JSP页面立即停止执行，并将输出的结果传到浏览器上；另外一个事EVAL_PAGE，表示JSP页面可以正常的运行完毕。

release()：将标签处理类所产生或者获得的资源全部释放，等待用户下次继续使用。

---

---

<h2 id="5"> 5 定义有标签体的标签库</h2> 

标签处理类-AttributeTag.java

	package org.fmz.tagdemo ;

	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class AttributeTag extends TagSupport{
		private String name ;
		private String scope ;
		public int doStartTag()throws JspException{
			Object value = null ;
			if("page".equals(this.scope)){
				value = super.pageContext.getAttribute(name,pageContext.PAGE_SCOPE) ;
			}
			if("request".equals(this.scope)){
				value = super.pageContext.getAttribute(name,pageContext.REQUEST_SCOPE) ;
			}
			if("session".equals(this.scope)){
				value = super.pageContext.getAttribute(name,pageContext.SESSION_SCOPE) ;
			}
			if("application".equals(this.scope)){
				value = super.pageContext.getAttribute(name,pageContext.APPLICATION_SCOPE) ;
			}
			if(value == null){
				return TagSupport.SKIP_BODY ;
			}else{
				return TagSupport.EVAL_BODY_INCLUDE ;
			}
		}
		public void setName(String name){
			this.name = name ;
		}
		public void setScope(String scope){
			this.scope = scope ;
		}
		public String getName(){
			return this.name ;
		}
		public String getScope(){
			return this.scope ;
		}
	}

定义标签描述文件-fmztag.tld

	<?xml version="1.0" encoding="UTF-8"?> 
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_3_1.xsd"
	    version="3.1"> 
	    <tlib-version> 1.0</tlib-version> 
	    <short-name> attributetag</short-name> 
	    <tag> 
			<name> present</name> 
			<tag-class> org.fmz.tagdemo.AttributeTag</tag-class> 
			<body-content> JSP</body-content> 
			<attribute> 
				<name> name</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
			<attribute> 
				<name> scope</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
	    </tag> 
	</taglib> 

配置web.xml，设置映射名称

	<jsp-config> 
		<taglib> 
			<taglib-uri> fmztag</taglib-uri> 
			<taglib-location> /WEB-INF/fmztag.tld</taglib-location> 
		</taglib> 
	</jsp-config> 

调用标签，完成判断-presenttag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="mytag" uri="fmztag"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String scope = "session" ;
				session.setAttribute("blog","http://fengmengzhao.github.io") ;
			%> 
			<mytag:present name="blog" scope="<%=scope%> "> 
				<h2> session属性范围存在属性，内容是："${sessionScope.blog}"</h2> 
			</mytag:present> 
			<mytag:present name="allusers" scope="request"> 
				<h2> request属性范围存在属性，内容是："${requestScope.allusers}"</h2> 
			</mytag:present> 
		</body> 
	</html> 

---

---

<h2 id="6"> 6 开发迭代标签</h2> 

在程序开发中迭代输出是一种比较常见的输出，JSP页面的主要功能是进行输出，在JSP中应该尽量避免Scriptlet代码的出现，为了达到这种页面的编写效果，可以通过迭代标签的开发完成。

开发迭代标签处理类-IteratorTag.java

	package org.fmz.tagdemo ;

	import java.util.- ;
	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class IteratorTag extends TagSupport{
		private String name ;
		private String scope ;
		private String id ;
		private Iterator<?>  iter = null ;

		public int doStartTag()throws JspException{
			Object value = null ;
			if("page".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.PAGE_SCOPE) ;
			}
			if("request".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.REQUEST_SCOPE) ;
			}
			if("session".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.SESSION_SCOPE) ;
			}
			if("application".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.APPLICATION_SCOPE) ;
			}
			if(value != null && value instanceof List<?> ){
				this.iter = ((List<?> )value).iterator() ;//向List接口进行向下转型
				if(iter.hasNext()){
					super.pageContext.setAttribute(this.id,iter.next()) ;//强属性保存在page属性范围中
					return TagSupport.EVAL_BODY_INCLUDE ;
				}else{
					return TagSupport.SKIP_BODY ;
				}
			}else{
				return TagSupport.SKIP_BODY ;
			}
		}
		public int doAfterBody()throws JspException{
			if(iter.hasNext()){
					super.pageContext.setAttribute(this.id,iter.next()) ;//强属性保存在page属性范围中
					return TagSupport.EVAL_BODY_AGAIN;//反复执行
			}else{
					return TagSupport.SKIP_BODY ;
			}
		}

		public void setName(String name){
			this.name = name ;
		}
		public void setScope(String scope){
			this.scope = scope ;
		}
		public void setId(String id){
			this.id = id ;
		}
		public String getName(){
			return this.name ;
		}
		public String getScope(){
			return this.scope ;
		}
		public String getId(){
			return this.id ;
		}
	}

修改标签描述文件，增加迭代标签配置-fmztag.tld

	<?xml version="1.0" encoding="UTF-8"?> 
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_3_1.xsd"
	    version="3.1"> 
	    <tlib-version> 1.0</tlib-version> 
	    <short-name> fmztag</short-name> 
	    <tag> 
			<name> present</name> 
			<tag-class> org.fmz.tagdemo.AttributeTag</tag-class> 
			<body-content> JSP</body-content> 
			<attribute> 
				<name> name</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
			<attribute> 
				<name> scope</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
	    </tag> 
	    <tag> 
			<name> iterator</name> 
			<tag-class> org.fmz.tagdemo.IteratorTag</tag-class> 
			<body-content> JSP</body-content> 
			<attribute> 
				<name> name</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
			<attribute> 
				<name> scope</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
			<attribute> 
				<name> id</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
	    </tag> 
	</taglib> 

编写JSP执行标签-iteratortag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib prefix="mytag" uri="fmztag"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				List<String>  list = new ArrayList<String> () ;
				list.add("http://fengmengzhao.github.io") ;
				list.add("fengmengzhao@gmail.com") ;
				list.add("1913565900@qq.com") ;
				request.setAttribute("info",list) ;
			%> 
			<mytag:present name="info" scope="request"> 
				<mytag:iterator id="url" name="info" scope="request"> 
					<h3> 网站：${url}</h3> 
				</mytag:iterator> 
			</mytag:present> 
		</body> 
	</html> 

> 本程序首先在request属性范围内保存了一个List集合，之后通过<mytag:present name="" scope=""/> 标签判断request范围内是否存在此属性，如果存在则使用迭代输出标签输出全部的内容。

> 在MVC设计模式中，集合是在Servlet保存之后再传递给JSP页面中的。

---

---

<h2 id="7"> 7 BodyTagSupport类</h2> 

BodyTagSupport类是TagSupport类的子类，可以直接处理标签提内容的数据。

---

---

<h2 id="8"> 8 TagExtraInfo类和VariableInfo类</h2> 

在使用<jsp:useBean id="" scope="" class=""/> 时，定义了一个id属性，这个属性可以像对象一样直接在Script中访问，而如果用户也想实现同样的效果，就需要TagExtraInfo类和VariableInfo类完成

在JSP中定义一个变量-BodyIteratorTagExtraInfo.java

	package org.fmz.tagdemo ;

	import javax.servlet.jsp.tagext.- ;


	public class BodyIteratorTagExtraInfo extends TagExtraInfo{
		public VariableInfo[] getVariableInfo(TagData data){
			return new VariableInfo[]{new VariableInfo(data.getId(),"java.lang.String",true,VariableInfo.NESTED)} ;
		}
	}

> 本程序定义的是一个脚本变量，在getVariableInfo()方法中定义了一个TagData对象，通过此对象的getId()方法，可以找到标签定义的id属性内容。由于只定义一个变量，所以只返回一个VariableInfo类对象，根据id指定的名称定义变量，变量的类型是String，而且是声明一个新的变量(true)，变量的有效期是在标签之中(标签开始和标签结束有效)

> 定义完之后，要想在标签中真正的起作用，需要在*.tld文件中增加如下配置：`<tei-class> org.fmz.tagdemo.BodyIteratorTagExtraInfo</tei-class> `，随后就可以在标签中编写的id属性定义成变量。

---

---

<h2 id="9"> 9 使用BodyTagSupport开发迭代输出</h2> 

定义标签处理类-BodyIteratorTag.java

	package org.fmz.tagdemo ;

	import java.util.- ;
	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class BodyIteratorTag extends BodyTagSupport{
		private String name ;
		private String scope ;
		private String id ;
		private Iterator<?>  iter = null ;

		public int doStartTag()throws JspException{
			Object value = null ;
			if("page".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.PAGE_SCOPE) ;
			}
			if("request".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.REQUEST_SCOPE) ;
			}
			if("session".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.SESSION_SCOPE) ;
			}
			if("application".equals(this.scope)){
				value = super.pageContext.getAttribute(this.name,pageContext.APPLICATION_SCOPE) ;
			}
			if(value != null && value instanceof List<?> ){
				this.iter = ((List<?> )value).iterator() ;
				if(iter.hasNext()){
					super.pageContext.setAttribute(this.id,iter.next()) ;//强属性保存在page属性范围中
					return BodyTagSupport.EVAL_BODY_BUFFERED ;
				}else{
					return BodyTagSupport.SKIP_BODY ;
				}
			}else{
				return BodyTagSupport.SKIP_BODY ;
			}
		}
		public int doAfterBody()throws JspException{
			if(iter.hasNext()){
					super.pageContext.setAttribute(this.id,iter.next()) ;//强属性保存在page属性范围中
					return BodyTagSupport.EVAL_BODY_AGAIN;//反复执行
			}else{
					return BodyTagSupport.SKIP_BODY ;
			}
		}
		public int doEndTag()throws JspException{
			if(super.bodyContent != null){
				try{
					super.bodyContent.writeOut(super.getPreviousOut()) ;
				}catch(Exception e){
					e.printStackTrace() ;
				}
			}
			return BodyTagSupport.EVAL_PAGE ;
		}

		public void setName(String name){
			this.name = name ;
		}
		public void setScope(String scope){
			this.scope = scope ;
		}
		public void setId(String id){
			this.id = id ;
		}
		public String getName(){
			return this.name ;
		}
		public String getScope(){
			return this.scope ;
		}
		public String getId(){
			return this.id ;
		}
	}

定义标签描述文件-fmztag.tld

	<?xml version="1.0" encoding="UTF-8"?> 
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_3_1.xsd"
	    version="3.1"> 
	    <tlib-version> 1.0</tlib-version> 
	    <short-name> fmztag</short-name> 
	    <tag> 
			<name> present</name> 
			<tag-class> org.fmz.tagdemo.AttributeTag</tag-class> 
			<body-content> JSP</body-content> 
			<attribute> 
				<name> name</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
			<attribute> 
				<name> scope</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
	    </tag> 
	    <tag> 
			<name> bodyiterator</name> 
			<tag-class> org.fmz.tagdemo.BodyIteratorTag</tag-class> 
			<tei-class> org.fmz.tagdemo.BodyIteratorTagExtraInfo</tei-class> 
			<body-content> JSP</body-content> 
			<attribute> 
				<name> name</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
			<attribute> 
				<name> scope</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
			<attribute> 
				<name> id</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
	    </tag> 
	</taglib> 

使用标签体执行操作-bodyiteratortag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib prefix="mytag" uri="fmztag"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				List<String>  list = new ArrayList<String> () ;
				list.add("http://fengmengzhao.github.io") ;
				list.add("fengmengzhao@gmail.com") ;
				list.add("1913565900@qq.com") ;
				request.setAttribute("info",list) ;
			%> 
			<mytag:present name="info" scope="request"> 
				<mytag:bodyiterator id="url" name="info" scope="request"> 
					<h3> 网站：${url}、<%=url.length()%> </h3> 
				</mytag:bodyiterator> 
			</mytag:present> 
		</body> 
	</html> 

> 不建议用户开发标签库，进行标签库的开发非常麻烦，而且用户自己开发的无法带来广泛的应用，所以读者只需要明白标签库操作的基本原理即可，以后会使用第三方标签库中的标签，如JSTL或者Struts等。

---

---

<h2 id="10"> 10 简单标签</h2> 

定义标签处理类-SimpleDateTag.java

	package org.fmz.tagdemo ;

	import java.util.- ;
	import java.text.- ;
	import java.io.- ;
	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class SimpleDateTag extends SimpleTagSupport{
		private String format ;
		public void doTag()throws JspException,IOException{
			SimpleDateFormat sdf = new SimpleDateFormat(this.format) ;
			try{
				super.getJspContext().getOut().write(sdf.format(new Date())) ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
		}
		public void setFormat(String format){
			this.format = format ;
		}
		public String getFormat(){
			return this.format ;
		}
	}

修改标签描述文件-fmztag.tld

	<?xml version="1.0" encoding="UTF-8"?> 
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_3_1.xsd"
	    version="3.1"> 
	    <tlib-version> 1.0</tlib-version> 
	    <short-name> fmztag</short-name> 
	    <tag> 
			<name> simpledate</name> 
			<tag-class> org.fmz.tagdemo.SimpleDateTag</tag-class> 
			<body-content> empty</body-content> 
			<attribute> 
				<name> format</name> 
				<required> true</required> <!-- 此属性必须设置 --> 
				<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
			</attribute> 
	    </tag> 
	</taglib> 

在JSP文件中使用标签-simpledatetag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="mytag" uri="fmztag"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h1> 
				<mytag:simpledate format="yyyy-MM-dd HH:mm:ss.SSS"/> 
			</h1> 
		</body> 
	</html> 

完成迭代输出

定义标签处理类-SimpleIteratorTag.java

	package org.fmz.tagdemo ;

	import java.io.- ;
	import java.util.- ;
	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class SimpleIteratorTag extends SimpleTagSupport{
		private String id ;
		private String name ;
		private String scope ;
		public void doTag()throws JspException,IOException{
			Object value = null ;
			if("page".equals(this.scope)){
				super.getJspContext().getAttribute(this.name,PageContext.PAGE_SCOPE) ;
			}
			if("request".equals(this.scope)){
				super.getJspContext().getAttribute(this.name,PageContext.REQUEST_SCOPE) ;
			}
			if("session".equals(this.scope)){
				super.getJspContext().getAttribute(this.name,PageContext.SESSION_SCOPE) ;
			}
			if("application".equals(this.scope)){
				super.getJspContext().getAttribute(this.name,PageContext.APPLICATION_SCOPE) ;
			}
			if(value != null && value instanceof List<?> ){
				Iterator<?>  iter = ((List<?> )value).iterator() ;
				while(iter.hasNext()){
					super.getJspContext().setAttribute(id,iter.next()) ;
					super.getJspBody().invoke(null) ;
				}
			}
		}
		public void setId(String id){
			this.id = id ;
		}
		public void setName(String name){
			this.name = name ;
		}
		public void setScope(String scope){
			this.scope = scope ;
		}
		public String getId(){
			return this.id ;
		}
		public String getName(){
			return this.name ;
		}
		public String getScope(){
			return this.scope ;
		}
	}

修改标签文件，增加新的的标签配置-fmztag.tld

<?xml version="1.0" encoding="UTF-8"?> 
<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_3_1.xsd"
    version="3.1"> 
    <tlib-version> 1.0</tlib-version> 
    <short-name> fmztag</short-name> 
    <tag> 
		<name> simpleiterator</name> 
		<tag-class> org.fmz.tagdemo.SimpleIteratorTag</tag-class> 
		<body-content> scriptless</body-content> 
		<attribute> 
			<name> id</name> 
			<required> true</required> <!-- 此属性必须设置 --> 
			<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
		</attribute> 
		<attribute> 
			<name> name</name> 
			<required> true</required> <!-- 此属性必须设置 --> 
			<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
		</attribute> 
		<attribute> 
			<name> scope</name> 
			<required> true</required> <!-- 此属性必须设置 --> 
			<rtexprvalue> true</rtexprvalue> <!-- 支持表达式的输出--> 
		</attribute> 
    </tag> 
</taglib> 

> <body-content> 中可以设置的内容一共有三种：1，empty表示没有标签体。2，JSP标签体可以包含文本、EL表达式或者JSP标签，但对于简单标签无效。3，sciptless标签体可以包含文本、EL表达式或者JSP标签，但是不能包含JSP的脚本元素。4，tagdepedent表示标签体交由标签本身去解析处理。

在JSP文件中使用标签-SimpleIteratortag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib prefix="mytag" uri="fmztag"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				List<String>  list = new ArrayList<String> () ;
				list.add("http://fengmengzhao.github.io") ;
				list.add("fengmengzhao@gmail.com") ;
				list.add("1913565900@qq.com") ;
				request.setAttribute("info",list) ;
			%> 
			<mytag:present name="info" scope="request"> 
				<mytag:bodyiterator id="url" name="info" scope="request"> 
					<h3> 网站：${url}、<%=url.length()%> </h3> 
				</mytag:bodyiterator> 
			</mytag:present> 
		</body> 
	</html> 

---

---

<h2 id="11"> 11 DynamicAttributes接口</h2> 

之前的标签只能编写制定个数及名称的属性，JSP2.0之后增加了一个DynamicAttributes接口，此接口的主要功能是完成动态属性的设置。

完成一个动态加法操作

标签处理类-DynamicAddTag.java

	package org.fmz.tagdemo ;

	import java.io.- ;
	import java.util.- ;
	import javax.servlet.jsp.- ;
	import javax.servlet.jsp.tagext.- ;

	public class DynamicAddTag extends SimpleTagSupport implements DynamicAttributes{
		private Map<String,Float>  num = new HashMap<String,Float> () ;
		public void setDynamicAttribute(java.lang.String uri, java.lang.String localName, java.lang.Object value)throws JspException{
			num.put(localName,Float.parseFloat(value.toString())) ;
		}
		public void doTag() throws JspException,java.io.IOException{
			Float sum = 0.0f ;//定义变量，保存相加结果
			Iterator<Map.Entry<String,Float> >  iter = this.num.entrySet().iterator() ;//迭代输出
			while(iter.hasNext()){
				Map.Entry<String,Float>  value = iter.next() ;//取出每一个Map.Entry
				sum += value.getValue() ;
			}
			super.getJspContext().getOut().write(sum + "") ;
		}
	}

编写标签描述文件，增加动态属性设置

	<?xml version="1.0" encoding="UTF-8"?> 
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_3_1.xsd"
	    version="3.1"> 
	    <tlib-version> 1.0</tlib-version> 
	    <short-name> fmztag</short-name> 
	    <tag> 
			<name> add</name> 
			<tag-class> org.fmz.tagdemo.DynamicAddTag</tag-class> 
			<body-content> empty</body-content> 
			<dynamic-attributes> true</dynamic-attributes> 
	    </tag> 
	</taglib> 

调用标签，设置动态属性-addtag.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="mytag" uri="fmztag"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h2> 
				计算结果：<mytag:add num1="11.2" num2="12.3" num3="13.5"/> 
			</h2> 
		</body> 
	</html> 

---

---
