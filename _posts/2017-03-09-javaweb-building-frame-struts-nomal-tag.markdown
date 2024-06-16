---
layout: post
title: javaweb框架开发篇之Struts常用标签库
---

## 目录

- [1 Struts标签库简介](#1)
- [2 Bean标签](#2)
	- [2.1  bean:define标签](#2.1)
	- [2.2 bean:size标签](#2.2)
	- [2.3 资源访问标签](#2.3)
	- [2.4 bean:write标签](#2.4)
	- [2.5 bean:include标签](#2.5)
	- [2.6 bean:resourec标签](#2.6)
	- [2.7 国际化与bean:message](#2.7)
- [3 Logic标签](#3)
	- [3.1 logic:present & logic:notPresent标签](#3.1)
	- [3.2 logic:empty & logic:notEmpty标签](#3.2)
	- [3.3 关系预算标签](#3.3)
	- [3.4 logic:iterate标签](#3.4)
- [4 Html标签](#4)
	- [4.1 实例：编写基本表单](#4.1)
	- [4.2 复选框标签](#4.2)
	- [4.3 下拉列表框](#4.3)

---

---

<h2 id="1"> 1 Struts标签库简介</h2> 

| NO. | 标签库 | 描述 |
|--- | --- | ---|
| 1 | Bean标签 | 管理JSP页面中的bean操作 |
| 2 | Logic标签 | 完成各种逻辑控制操作 |
| 3 | HTML标签 | 显示标签，主要生成HTML标记 |
| 4 | TILES标签 | 使用动态模板构造显示页面 |
| 5 | NESTED标签 | 使用嵌套标签进行复杂的页面显示 |

---

---

<h2 id="2"> 2 Bean标签</h2> 

<h3 id="2.1"> 2.1 bean:define标签</h3> 

	<%@ page language="java" pageEncoding="UTF-8"%> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:define id="info" scope="page"> Hello World!!!</bean:define> 
		<bean:define id="teacher" value="冯孟昭"/> 
		<h3> 定义内容：${info }</h3> 
		<h3> 老师：${pageScope.teacher }</h3> 
	  </body>  
	</html:html> 

定义一个JavaBean-CopyBean.java

	package org.fmz.struts.vo;

	public class CopyBean {
		private String msg ;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

在JSP页面中定义并且复制此Bean-bean-copy.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<jsp:useBean id="copybean" class="org.fmz.struts.vo.CopyBean" scope="page"/> 
		<jsp:setProperty name="copybean" property="msg" value="Hello FMZ"/> 
		<bean:define id="info" name="copybean" property="msg"/> 
		<h3> 拷贝：${info }</h3> 
	  </body> 
	</html:html> 

<h3 id="2.2"> 2.2 bean:size标签</h3> 

计算集合的长度-bean_size.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<%
			List list = new ArrayList() ;
			list.add("Hello") ;
			list.add("FMZ") ;
			list.add("光临！") ;
			pageContext.setAttribute("info", list) ;
		 %> 
		 <bean:size id="len" name="info" scope="page"/> 
		 <h3> 集合长度：${len }</h3> 
	  </body> 
	</html:html> 

<h3 id="2.3"> 2.3 资源访问标签</h3> 

设置Cookie-cookie.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://str2015-09-20uts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:cookie name="username" id="mycookie" value="FMZ"/> 
		<%
			mycookie.setMaxAge(3000) ;
			response.addCookie(mycookie) ;
			Cookie[] cookies = request.getCookies() ;
			for(int x=0;x<cookies.length;x++){
		%> 
				<h3> <%=cookies[x].getName() %>  -->  <%=cookies[x].getValue() %> </h3> 
		<%
			} 
		 %> 
	  </body> 
	</html:html> 

设置头信息-header.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:header name="Accept-Language" id="myheader"/> 
				<h3> Accept-Language头信息内容：${myheader }</h3> 
	  </body> 
	</html:html> 

设置参数-param.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base />  	
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:parameter id="myparam" name="msg" value="FMZ"/> 
		<h3> myparam的内容：${myparam}</h3> 
	  </body> 
	</html:html> 

<h3 id="2.4"> 2.4 bean:write标签</h3> 

使用bean:write输出-bean_write.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base />  	
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:define id="info" scope="page"> 
			<h3> Hello FMZ</h3> 
		</bean:define> 
		<bean:write name="info"/> 
	  </body> 
	</html:html> 

<h3 id="2.5"> 2.5 bean:include标签</h3> 

定义被包含页面-content.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base />  	
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<h3> Hello FMZ</h3> 
	  </body> 
	</html:html> 

使用bean:include标签包含页面-include.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base />  	
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:include id="inc" page="/content.jsp"/> 	
		${inc }
	  </body> 
	</html:html> 

<h3 id="2.6"> 2.6 bean:resourec标签</h3> 

定义被包含页面-content.xml

	<?xml version="1.0" encoding="utf-8"?> 
	<book> 
		<author> 冯孟昭</author> 
		<title> java开发实战经典</title> 
	</book> 

使用bean:resource包含页面-resource.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base />  	
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:resource id="source" name="/content.xml"/> 
		<bean:write name="source"/> 
	  </body> 
	</html:html> 

<h3 id="2.7"> 2.7 国际化与bean:message</h3> 

Struts本事支持国际化程序的开发，用户只需要根据区域的不同配置不同的语言资源文件(资源文件通过struts-config.xml指定)，此处为ApplicationResource.properties，即可显示不同区域的语言。

定义中文资源文件-ApplicationResource_zh_CN.properties

	# Resources for parameter 'org.fmz.struts.ApplicationResources'
	# Project StrutsBean
	hello.info =<h3> {0}\u60A8\u597D\uFF0C\u6B22\u8FCE{1}\u5149\u4E34\uFF01</h3> 

> 转码操作通过doc启动`native2ascii`即可

定义中文资源文件-ApplicationResource_en_US.properties

	# Resources for parameter 'org.fmz.struts.ApplicationResources'
	# Project StrutsBean
	hello.info =<h3> {0} hello,welcome{1}\uFF01</h3> 

使用bean:message读取资源文件-message.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*" %> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base />  	
	    
	    <title> bean_define.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 
	  </head> 
	  
	  <body> 
		<bean:message key="hello.info" arg0="FMZ" arg1="CLL"/> 
	  </body> 
	</html:html> 

> 通过浏览器的语言设置，重启浏览器后即可完成国际化的转变。

---

---

<h2 id="3"> 3 Logic标签</h2> 

Logic标签的主要作用是进行各种逻辑处理，如执行分支语句、迭代、比较操作等，所有的logic标签都定义在struts-logic.tld文件中

<h3 id="3.1"> 3.1 logic:present & logic:notPresent标签</h3> 

判断属性是否存在-present.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 

	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<%
			request.setAttribute("author", "fmz") ;
		 %> 
		 <logic:present name="author" scope="request"> 
			<h3> author属性存在，内容是：${author }</h3> 
		 </logic:present> 
		 <logic:notPresent name="url" scope="request"> 
			<h3> url属性不存在！</h3> 
		 </logic:notPresent> 
	  </body> 
	</html:html> 

<h3 id="3.2"> 3.2 logic:empty & logic:notEmpty标签</h3> 

判断内容是否为空-empty.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<% 
			List<String>  list = new ArrayList<String> () ;
			request.setAttribute("author",list) ;  		
		 %> 
		 <logic:empty name="author" scope="request"> 
			<h3> 属性的内容为空，长度为0！</h3> 
		 </logic:empty> 
		 <logic:empty name="url" scope="request"> 
			<h3> url属性不存在！</h3> 
		 </logic:empty> 
	  </body> 
	</html:html> 

<h3 id="3.3"> 3.3 关系运算标签</h3> 

关系运算标签的使用-rel.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<% 
			request.setAttribute("author","fmz") ;
			request.setAttribute("num",30) ;
		 %> 
		 <logic:equal name="author" value="fmz" scope="request"> 
			<h3> 满足等于条件！</h3> 
		 </logic:equal> 
		 <logic:notEqual name="url" value="fmz" scope="request"> 
			<h3> 不满足等于条件！</h3> 
		 </logic:notEqual> 
	  </body> 
	</html:html> 

<h3 id="3.4"> 3.4 logic:iterate标签</h3> 

输出集合对象-iterate.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<% 
			String allArr[] = {"fmz","cll","wlm"} ;
			request.setAttribute("allArr",allArr) ;
		 %> 
		<h3> 输入对象数组：</h3> 
		<logic:iterate id="arr" name="allArr" scope="request"> 
			<h3> 名字：${arr }</h3>  
		</logic:iterate> 
		<%
			List<String>  allList = new ArrayList<String> () ;
			allList.add("fmz") ;
			allList.add("cll") ;
			allList.add("wlm") ;
			request.setAttribute("allList",allList) ;
		 %> 
		 <h3> 输入List集合：</h3> 
		<logic:iterate id="arr" name="allList" scope="request"> 
			<h3> 名字：${arr }</h3>  
		</logic:iterate> 
		<%
			Map<String,String>  allMap = new HashMap<String,String> () ;
			allMap.put("name1","fmz") ;
			allMap.put("name2","cll") ;
			allMap.put("name3","wlm") ;
			request.setAttribute("allMap",allMap) ;
		 %> 
		  <h3> 输入Map集合：</h3> 
		<logic:iterate id="map" name="allMap" scope="request"> 
			<h3> 名字${map.key } -->  ${map.value }</h3>  
		</logic:iterate> 
	  </body> 
	</html:html> 

<h3 id="3.5"> 3.5 充定向标签-logic:redirect标签</h3> 

修改struts-config.xml，增加全局跳转，修改global-farward元素

	<?xml version="1.0" encoding="UTF-8"?> 
	<!DOCTYPE struts-config PUBLIC "-//Apache Software Foundation//DTD Struts Configuration 1.3//EN" "http://struts.apache.org/dtds/struts-config_1_3.dtd"> 

	<struts-config> 
	  <form-beans /> 
	  <global-exceptions /> 
	  <global-forwards> 
		<forward name="hello" path="/hello.jsp"/> 
	  </global-forwards> 
	  <action-mappings /> 
	  <message-resources parameter="org.fmz.struts.ApplicationResources" /> 
	</struts-config> 

跳转后的页面-hello.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<logic:redirect forward="hello"/> 
	  </body> 
	</html:html> 

使用logic:redirect标签进行跳转-redirect.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<logic:redirect forward="hello"/> 
	  </body> 
	</html:html> 

---

---

<h2 id="4"> 4 Html标签</h2> 

Html标签主要用于页面显示。

<h3 id="4.1"> 4.1 实例：编写基本表单</h3> 

定义输入表单-input_simple.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<% 
			request.setCharacterEncoding("utf-8") ;
		 %> 
		 <html:form action="simple.do" method="post"> 
			姓名：<html:text property="name"/> <br> 
			密码：<html:password property="password"/> <br> 
			性别：<html:radio property="sex" value="male"/> 男
			<html:radio property="sex" value="female"/> 女<br> 
			简介：<html:textarea property="note" cols="30" rows="3"/> <br> 
			<html:hidden property="id" value="30"/> 
			<html:submit value="提交"/> 
			<html:reset value="重置"/> 
		 </html:form> 
	  </body> 
	</html:html> 

定义ActionForm-SimpleForm.java

	/*
	 - Generated by MyEclipse Struts
	 - Template path: templates/java/JavaClass.vtl
	 */
	package org.fmz.struts.form;

	import javax.servlet.http.HttpServletRequest;
	import org.apache.struts.action.ActionErrors;
	import org.apache.struts.action.ActionForm;
	import org.apache.struts.action.ActionMapping;

	/*- 
	 - MyEclipse Struts
	 - Creation date: 09-26-2015
	 - 
	 - XDoclet definition:
	 - @struts.form name="simpleForm"
	 */
	public class SimpleForm extends ActionForm {
		/*
		 - Generated fields
		 */

		/*- id property */
		private int id;

		/*- sex property */
		private String sex;

		/*- name property */
		private String name;

		/*- password property */
		private String password;

		/*- note property */
		private String note;

		/*
		 - Generated Methods
		 */

		/*- 
		 - Method validate
		 - @param mapping
		 - @param request
		 - @return ActionErrors
		 */
		public ActionErrors validate(ActionMapping mapping,
				HttpServletRequest request) {
			// TODO Auto-generated method stub
			return null;
		}

		/*- 
		 - Method reset
		 - @param mapping
		 - @param request
		 */
		public void reset(ActionMapping mapping, HttpServletRequest request) {
			// TODO Auto-generated method stub
			this.sex = "男" ;
		}

		/*- 
		 - Returns the id.
		 - @return int
		 */
		public int getId() {
			return id;
		}

		/*- 
		 - Set the id.
		 - @param id The id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/*- 
		 - Returns the sex.
		 - @return String
		 */
		public String getSex() {
			return sex;
		}

		/*- 
		 - Set the sex.
		 - @param sex The sex to set
		 */
		public void setSex(String sex) {
			this.sex = sex;
		}

		/*- 
		 - Returns the name.
		 - @return String
		 */
		public String getName() {
			return name;
		}

		/*- 
		 - Set the name.
		 - @param name The name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/*- 
		 - Returns the password.
		 - @return String
		 */
		public String getPassword() {
			return password;
		}

		/*- 
		 - Set the password.
		 - @param password The password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/*- 
		 - Returns the note.
		 - @return String
		 */
		public String getNote() {
			return note;
		}

		/*- 
		 - Set the note.
		 - @param note The note to set
		 */
		public void setNote(String note) {
			this.note = note;
		}
	}

定义Action-SimpleAction.java

	/*
	 - Generated by MyEclipse Struts
	 - Template path: templates/java/JavaClass.vtl
	 */
	package org.fmz.struts.action;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import org.apache.struts.action.Action;
	import org.apache.struts.action.ActionForm;
	import org.apache.struts.action.ActionForward;
	import org.apache.struts.action.ActionMapping;
	import org.fmz.struts.form.SimpleForm;

	/*- 
	 - MyEclipse Struts
	 - Creation date: 09-26-2015
	 - 
	 - XDoclet definition:
	 - @struts.action path="/simple" name="simpleForm" input="/input_simple.jsp" scope="request" validate="true"
	 */
	public class SimpleAction extends Action {
		/*
		 - Generated Methods
		 */

		/*- 
		 - Method execute
		 - @param mapping
		 - @param form
		 - @param request
		 - @param response
		 - @return ActionForward
		 */
		public ActionForward execute(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) {
			try{
				request.setCharacterEncoding("utf-8") ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
			SimpleForm simpleForm = (SimpleForm) form;// TODO Auto-generated method stub
			System.out.println("编号："+simpleForm.getId()) ;
			System.out.println("姓名："+simpleForm.getName()) ;
			System.out.println("性别："+simpleForm.getSex()) ;
			System.out.println("密码："+simpleForm.getPassword()) ;
			System.out.println("简介："+simpleForm.getNote()) ;
			return null;
		}
	}

<h3 id="4.2"> 4.2 复选框标签</h3> 

定义表单-input_box.jsp

	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<% 
			request.setCharacterEncoding("utf-8") ;
		 %> 
		 <html:form action="box.do" method="post"> 
			<html:checkbox property="inst" value="sing"/> 唱歌
			<html:checkbox property="inst" value="dance"/> 跳舞
			<html:checkbox property="inst" value="read"/> 读书
			<html:checkbox property="inst" value="write"/> 写作
			<html:checkbox property="inst" value="swim"/> 游泳<br> 
			<html:submit value="提交"/> 
			<html:reset value="重置"/> 
		 </html:form> 
	  </body> 
	</html:html> 

定义ActionForm-BoxForm.java

	/*
	 - Generated by MyEclipse Struts
	 - Template path: templates/java/JavaClass.vtl
	 */
	package org.fmz.struts.form;

	import javax.servlet.http.HttpServletRequest;
	import org.apache.struts.action.ActionErrors;
	import org.apache.struts.action.ActionForm;
	import org.apache.struts.action.ActionMapping;

	/*- 
	 - MyEclipse Struts
	 - Creation date: 09-26-2015
	 - 
	 - XDoclet definition:
	 - @struts.form name="boxForm"
	 */
	public class BoxForm extends ActionForm {
		
		private String[] inst = {"编程","编程","编程","编程","编程"} ;
		
		public ActionErrors validate(ActionMapping mapping,
				HttpServletRequest request) {
			// TODO Auto-generated method stub
			return null;
		}

		/*- 
		 - Method reset
		 - @param mapping
		 - @param request
		 */
		public void reset(ActionMapping mapping, HttpServletRequest request) {
			// TODO Auto-generated method stub
		}

		public String[] getInst() {
			return inst;
		}

		public void setInst(String[] inst) {
			this.inst = inst;
		}
	}

定义Action-BoxAction.java

	/*
	 - Generated by MyEclipse Struts
	 - Template path: templates/java/JavaClass.vtl
	 */
	package org.fmz.struts.action;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import org.apache.struts.action.Action;
	import org.apache.struts.action.ActionForm;
	import org.apache.struts.action.ActionForward;
	import org.apache.struts.action.ActionMapping;
	import org.fmz.struts.form.BoxForm;

	/*- 
	 - MyEclipse Struts
	 - Creation date: 09-26-2015
	 - 
	 - XDoclet definition:
	 - @struts.action path="/box" name="boxForm" input="/input_box.jsp" scope="request" validate="true"
	 */
	public class BoxAction extends Action {
		/*
		 - Generated Methods
		 */

		/*- 
		 - Method execute
		 - @param mapping
		 - @param form
		 - @param request
		 - @param response
		 - @return ActionForward
		 */
		public ActionForward execute(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) {
			try{
				request.setCharacterEncoding("utf-8") ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
			BoxForm boxForm = (BoxForm) form;// TODO Auto-generated method stub
			String[] inst = boxForm.getInst() ;
			System.out.println("兴趣：") ;
			for(int i=0;i<inst.length;i++){
				System.out.print(inst[i]+"\t") ;
			}
			return null;
		}
	}

<h3 id="4.3"> 4.3 下拉列表框</h3> 

定义vo类-city.java

	package org.fmz.struts.vo;

	public class City {
		private String name ;
		private int id ;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}

编写表单文件-input_select.jsp

	<%@ page import="org.apache.struts.util.LabelValueBean"%> 
	<%@ page language="java" pageEncoding="UTF-8"%> 
	<%@ page import="java.util.*,org.fmz.struts.vo.*"%> 
	<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
	<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
	<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
	<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
	<html:html lang="true"> 
	  <head> 
	    <html:base /> 
	    
	    <title> present.jsp</title> 

		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache"> 
		<meta http-equiv="expires" content="0">     
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
		<meta http-equiv="description" content="This is my page"> 

	  </head> 
	  
	  <body> 
		<% 
			request.setAttribute("author","fmz") ;
		 %> 
		 <%
			List all = new ArrayList() ;
			City city = new City() ;
			city.setId(1) ;
			city.setName("南阳") ;
			all.add(city) ;
			city = new City() ;
			city.setId(2) ;
			city.setName("成都") ;
			all.add(city) ;
			request.setAttribute("allCity",all) ;
		  %> 
		  <%
			all = new ArrayList() ;
			all.add(new LabelValueBean("管理员","admin")) ;
			all.add(new LabelValueBean("游客","guest")) ;
			request.setAttribute("allUser",all) ;
		  %> 
		  <html:form action="select.do" method="post"> 
			水果：<html:select property="fruit"> 
				<html:option value="xg"> 西瓜</html:option> 
				<html:option value="pg"> 苹果</html:option> 
			</html:select> 
			城市：<html:select property="city"> 
				<html:options collection="allCity" labelProperty="name" property="id"/> 
			</html:select> 
			用户：<html:select property="user"> 
				<html:optionsCollection name="allUser" label="label" value="value"/> 
			</html:select> <br> 
			<html:submit value="提交"/> 
			<html:reset value="重置"/> 
		  </html:form> 
	  </body> 
	</html:html> 

定义ActionForm-SelectForm.java

	/*
	 - Generated by MyEclipse Struts
	 - Template path: templates/java/JavaClass.vtl
	 */
	package org.fmz.struts.form;

	import javax.servlet.http.HttpServletRequest;
	import org.apache.struts.action.ActionErrors;
	import org.apache.struts.action.ActionForm;
	import org.apache.struts.action.ActionMapping;

	/*- 
	 - MyEclipse Struts
	 - Creation date: 09-26-2015
	 - 
	 - XDoclet definition:
	 - @struts.form name="selectForm"
	 */
	public class SelectForm extends ActionForm {
		private String fruit ;
		private String city ;
		private String user ;
		/*
		 - Generated Methods
		 */

		/*- 
		 - Method validate
		 - @param mapping
		 - @param request
		 - @return ActionErrors
		 */
		public ActionErrors validate(ActionMapping mapping,
				HttpServletRequest request) {
			// TODO Auto-generated method stub
			return null;
		}

		/*- 
		 - Method reset
		 - @param mapping
		 - @param request
		 */
		public void reset(ActionMapping mapping, HttpServletRequest request) {
			// TODO Auto-generated method stub
		}

		public String getFruit() {
			return fruit;
		}

		public void setFruit(String fruit) {
			this.fruit = fruit;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}
	}

定义Action文件-SelectAction.java

	/*
	 - Generated by MyEclipse Struts
	 - Template path: templates/java/JavaClass.vtl
	 */
	package org.fmz.struts.action;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import org.apache.struts.action.Action;
	import org.apache.struts.action.ActionForm;
	import org.apache.struts.action.ActionForward;
	import org.apache.struts.action.ActionMapping;
	import org.fmz.struts.form.SelectForm;

	/*- 
	 - MyEclipse Struts
	 - Creation date: 09-26-2015
	 - 
	 - XDoclet definition:
	 - @struts.action path="/select" name="selectForm" input="/input_select.jsp" scope="request" validate="true"
	 */
	public class SelectAction extends Action {
		/*
		 - Generated Methods
		 */

		/*- 
		 - Method execute
		 - @param mapping
		 - @param form
		 - @param request
		 - @param response
		 - @return ActionForward
		 */
		public ActionForward execute(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) {
			SelectForm selectForm = (SelectForm) form;// TODO Auto-generated method stub
			System.out.println("水果："+selectForm.getFruit()) ;
			System.out.println("城市："+selectForm.getCity()) ;
			System.out.println("用户："+selectForm.getUser()) ;
			return null;
		}
	}

---

---
