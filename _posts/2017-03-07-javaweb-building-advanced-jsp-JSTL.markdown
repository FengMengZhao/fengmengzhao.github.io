---
layout: post
title: javaweb开发高级篇之JSP标准标签库
---

## 目录

- [1 JSTL简介](#1)
- [2 安装JSTL1.2](#2)
- [3 核心标签库](#3)
	- [3.1 out标签](#3.1)
	- [3.2 set标签](#3.2)
	- [3.3 remove标签](#3.3)
	- [3.4 catch标签](#3.4)
	- [3.5 if标签](#3.5)
	- [3.6 choose标签](#3.6)
	- [3.7 forEach标签](#3.7)
	- [3.8 forTokens标签](#3.8)
	- [3.9 import标签](#3.9)
	- [3.10 url标签](#3.10)
	- [3.11 redirect标签](#3.11)
- [4 redirect标签](#4)
	- [4.1 setLocale标签](#4.1)
	- [4.2 requestEncoding标签](#4.2)
	- [4.3 读取资源文件](#4.3)
	- [4.4 数字格式化标签](#4.4)
	- [4.5 日期时间的格式化标签](#4.5)
	- [4.6 设置时区](#4.6)
- [5 SQL标签库](#5)
	- [5.1 setDataSource标签](#5.1)
	- [5.2 数据库操作标签](#5.2)
	- [5.3 事务处理](#5.3)
- [6 XML标签库](#6)
	- [6.1 XPath简介](#6.1)
	- [6.2 parse标签](#6.2)
	- [6.3 out标签](#6.3)
	- [6.4 set标签](#6.4)
	- [6.5 if标签](#6.5)
	- [6.6 choose标签](#6.6)
	- [6.7 forEach标签](#6.7)
- [7 函数标签库](#7)

---

---

<h2 id="1"> 1 JSTL简介</h2> 

JSTL(JSP Standard Tag Library，JSP标准标签库)是一个开放源代码标签组件，由Apache组织的Jakarta小组开发，可以直接从[http://tomcat.apache.org/taglibs/](http://tomcat.apache.org/taglibs/ "JSTL标签库下载")下载。

JSTL中的主要标签库

| NO. | JSTL | 标记名称 | 标签配置文件 | 描述 |
|--- | --- | --- | ---|
| 1 | 核心标签库 | c | c.tld | 定义属性管理、迭代、判断、输出 |
| 2 | SQL标签库 | sql | sql.tld | 定义查询数据库的操作 |
| 3 | XML标签库 | XML | x.tld | 用于操作XML数据 |
| 4 | 函数标签库 | fn | fn.tld | 提供了一些常用的操作函数，如字符串函数 |
| 5 | I18N格式标签库 | fmt | fmt.tld | 格式化数据 |

---

---

<h2 id="2"> 2 安装JSTL1.2</h2> 

将jar包复制到lib文件夹中，解压缩复制tld文件到WEB-INF文件夹下。

配置映射路径

	<jsp-config> 
		<taglib> 
			<taglib-uri> http://tomcat.apache.org/taglibs/jst/c</taglib-uri> 
			<taglib-location> /WEB-INF/c.tld</taglib-location> 
		</taglib> 
		<taglib> 
			<taglib-uri> http://tomcat.apache.org/taglibs/jst/fmt</taglib-uri> 
			<taglib-location> /WEB-INF/fmt.tld</taglib-location> 
		</taglib> 
		<taglib> 
			<taglib-uri> http://tomcat.apache.org/taglibs/jst/fn</taglib-uri> 
			<taglib-location> /WEB-INF/fn.tld</taglib-location> 
		</taglib> 
		<taglib> 
			<taglib-uri> http://tomcat.apache.org/taglibs/jst/sql</taglib-uri> 
			<taglib-location> /WEB-INF/sql.tld</taglib-location> 
		</taglib> 
		<taglib> 
			<taglib-uri> http://tomcat.apache.org/taglibs/jst/x</taglib-uri> 
			<taglib-location> /WEB-INF/x.tld</taglib-location> 
		</taglib> 
	</jsp-config> 

第一个JSTL程序-hello_jst.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h3> <c:out value="Hello World！！！"/> </h3> 
		</body> 
	</html> 

---

---

<h2 id="3"> 3 核心标签库</h2> 

核心标签库是JSTL中最重要的部分，也是开发中最常用到的部分。

<h3 id="3.1"> 3.1 out标签</h3> 

out标签的主要功能是输出

使用out进行输出-out_jstl_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<% pageContext.setAttribute("info","<http://fengmengzhao.github.io> ") ; %> 
			<h3> 属性存在：<c:out value="${info}"/> </h3> 
			<h3> 属性不存在：<c:out value="${ref}" default="属性不存在！"/> </h3> 
			<h3> 属性不存在：<c:out value="${ref}"> 属性不存在！</c:out> </h3> 
		</body> 
	</html> 

<h3 id="3.2"> 3.2 set标签</h3> 

主要用来将属性保存在四个属性范围中

通过<c:set> 设置属性-set_jsp_demo.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:set var="info" value="http://fengmengzhao.github.io" scope="page"/> 
			<h3> 属性内容是：${info}</h3> 
		</body> 
	</html> 

还可以将内容设置到一个JavaBean的属性中，此时通过target和property完成

定义JavaBean-SimpleInfo.java

	package org.fmz.jstldemo.vo ;

	public class SimpleInfo{
		private String content ;
		public void setContent(String content){
			this.content = content ;
		}
		public String getContent(){
			return this.content ;
		}
	}

设置属性-set_bean.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<%@ page import="org.fmz.jstldemo.vo.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				SimpleInfo si = new SimpleInfo() ; 
				request.setAttribute("simple",si) ;
			%> 
			<c:set value="http://fengmengzhao.github.io" target="${simple}" property="content"/> 
			<h3> 属性内容是：${simple.content}</h3> 
		</body> 
	</html> 

<h3 id="3.3"> 3.3 remove标签</h3> 

remove标签的主要作用是删除指定属性范围的属性

删除属性-remove_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:set value="http://fengmengzhao.github.io" scope="request" var="info"/> 
			<c:remove scope="request" var="info"/> 
			<h3> 属性内容是：${info}</h3> 
		</body> 
	</html> 

---

---

<h3 id="3.4"> 3.4 catch标签</h3> 

catch标签主要用来捕捉程序中产生的异常，并对异常进行处理

进行异常处理-catch_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:catch var="errorMsg"> 
				<%
					int x = 10 / 0 ;
				%> 
			</c:catch> 
			<h3> 错误信息是：${errorMsg}</h3> 
		</body> 
	</html> 

<h3 id="3.5"> 3.5 if标签</h3> 

判断操作-if_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:if test="${param.ref == 'fmz'}" var="res1" scope="page"> 
				<h3> 欢迎${param.ref}光临！</h3> 
			</c:if> 
			<c:if test="${10 < 30}" var="res2" scope="page"> 
				<h3> 10 确实小于 30！</h3> 
			</c:if> 
		</body> 
	</html> 

<h3 id="3.6"> 3.6 choose标签</h3> 

进行多个条件的判断

多条件判断-choos_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.setAttribute("num",10) ;
			%> 
			<c:choose> 
				<c:when test="${num == 10}"> 
					<h3> num属性内容是10！</h3> 
				</c:when> 
				<c:when test="${num == 20}"> 
					<h3> num属性内容是20！</h3> 
				</c:when> 
				<c:otherwise> 
					<h3> 没有一个条件成立！</h3> 
				</c:otherwise> 
			</c:choose> 
		</body> 
	</html> 

<h3 id="3.7"> 3.7 forEach标签</h3> 

输出数组-print_array_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String[] str = {"fmz","cll","http://fengmengzhao.github.io"} ;
				pageContext.setAttribute("ref",str) ;
			%> 
			<h3> 输出全部内容：
				<c:forEach items="${ref}" var="each"> 
					${each}、
				</c:forEach> 
			</h3> 
			<h3> 输出前2个内容：
				<c:forEach items="${ref}" var="each" begin="0" end="1"> 
					${each}、
				</c:forEach> 
			</h3> 
			<h3> 输出全部内容(间隔为2)：
				<c:forEach items="${ref}" var="each" step="2"> 
					${each}、
				</c:forEach> 
			</h3> 
		</body> 
	</html> 

输出集合-print_list_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8" import="java.util.*"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

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
				list.add("cll") ;
				list.add("http://fengmengzhao.github.io") ;
				pageContext.setAttribute("ref",list) ;
			%> 
			<h3> 输出全部内容：
				<c:forEach items="${ref}" var="each"> 
					${each}、
				</c:forEach> 
			</h3> 
			<h3> 输出前2个内容：
				<c:forEach items="${ref}" var="each" begin="0" end="1"> 
					${each}、
				</c:forEach> 
			</h3> 
			<h3> 输出全部内容(间隔为2)：
				<c:forEach items="${ref}" var="each" step="2"> 
					${each}、
				</c:forEach> 
			</h3> 
		</body> 
	</html> 

输出Map集合-print_map_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8" import="java.util.*"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				Map map = new HashMap() ;
				map.put("fmz","http://fengmengzhao.github.io") ;
				map.put("cll","http://cll.github.io") ;
				map.put("mail","fengmengzhao@gmail.com") ;
				pageContext.setAttribute("ref",map) ;
			%> 
			<h3> 输出全部内容：
				<c:forEach items="${ref}" var="each"> 
					${each.key} -->  ${each.value}、
				</c:forEach> 
			</h3> 
			<h3> 输出前2个内容：
				<c:forEach items="${ref}" var="each" begin="0" end="1"> 
					${each.key} -->  ${each.value}、
				</c:forEach> 
			</h3> 
			<h3> 输出全部内容(间隔为2)：
				<c:forEach items="${ref}" var="each" step="2"> 
					${each.key} -->  ${each.value}、
				</c:forEach> 
			</h3> 
		</body> 
	</html> 

> Map集合中的对象都是通过Map.Entry的形式保存的，需要通过Map.Entry中的getKey()和getValue()方法取得关键字和对应的值信息

<h3 id="3.8"> 3.8 forTokens标签</h3> 

使用forTokens进行输出-print_tokens_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8" import="java.util.*"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				String str = "http://fengmengzhao.github.io" ;
				pageContext.setAttribute("ref",str) ;
			%> 
			<h3> 拆分结果：
				<c:forTokens items="${ref}" delims="." var="con"> 
					${con}、
				</c:forTokens> 
			</h3> 
		</body> 
	</html> 

<h3 id="3.9"> 3.9 import标签</h3> 

导入外部项目站点-import_url_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:import url="http://www.baidu.com" charEncoding="utf-8"/> 
		</body> 
	</html> 

接收参数中的页面-param.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<h3> ${param.name}</h3> 
			<h3> ${param.url}</h3> 
		</body> 
	</html> 

传递参数-import_param.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:import url="param.jsp" charEncoding="utf-8"> 
				<c:param name="name" value="fmz"/> 
				<c:param name="url" value="http://fengmengzhao.github.io"/> 
			</c:import> 
		</body> 
	</html> 

<h3 id="3.10"> 3.10 url标签</h3> 

产生url地址-create_url.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:url value="http://fengmengzhao.github.io" var="urlinfo"> 
				<c:param name="autor" value="fmz"/> 
				<c:param name="logo" value="BeautifulYou"/> 
			</c:url> 
			<a href="${urlinfo}"> 冯孟昭博客地址</a> 
		</body> 
	</html> 

<h3 id="3.11"> 3.11 redirect标签</h3> 

客户单跳转到param.jsp-redirect_jstl.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:redirect url="param.jsp"> 
				<c:param name="name" value="fmz"/> 
				<c:param name="url" value="http://fengmengzhao.github.io"/> 
			</c:redirect> 
		</body> 
	</html> 


---

---

<h2 id="4"> 4 国际化标签库</h2> 

国际化是程序的重要组成部分，一个程序可以根据所在的区域进行相应的信息显示。

<h3 id="4.1"> 4.1 setLocale标签</h3> 

设置Locale显示-locale.jsp

<%@ page contentType="text/html" pageEncoding="utf-8"%> 
<%@ page import="java.util.*"%> 
<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.setAttribute("date",new Date()) ;
			%> 
			<h3> 中文格式显示：
				<fmt:setLocale value="zh_CN"/> 
				<fmt:formatDate value="${date}"/> 
			</h3> 
			<h3> 英文格式显示：
				<fmt:setLocale value="en_US"/> 
				<fmt:formatDate value="${date}"/> 
			</h3> 
			<fmt:requestEncoding value="utf-8"/> 
		</body> 
	</html> 

<h3 id="4.2"> 4.2 requestEncoding标签</h3> 

设置统一编码：`<fmt:requestEncoding value="utf-8"/> `

<h3 id="4.3"> 4.3 读取资源文件</h3> 

定义资源文件-/WEB-INF/classes/Message.properties

name = FengMengZhao
info = \u6b22\u8fce{0}\u5149\u4e34\uff01

> \u6b22\u8fce{0}\u5149\u4e34\uff01 是转义字符，为"欢迎{0}光临！"的转义

读取资源文件-message.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<fmt:bundle basename="Message"> 
				<fmt:message key="name" var="nameref"/> 
			</fmt:bundle> 
			<h3> 姓名：${nameref}</h3> 
			<fmt:bundle basename="Message"> 
				<fmt:message key="info" var="inforef"> 
					<fmt:param value="冯孟昭"/> 
				</fmt:message> 
			</fmt:bundle> 
			<h3> 信息：${inforef}</h3> 
		</body> 
	</html> 

设置要读取的资源文件-bundle_message.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<fmt:setBundle basename="Message" var="msg"/> 
			<fmt:message key="name" var="nameref" bundle="${msg}"/> 
			<h3> 姓名：${nameref}</h3> 
			<fmt:message key="info" var="inforef" bundle="${msg}"> 
				<fmt:param value="冯孟昭"/> 
			</fmt:message> 
			<h3> 信息：${inforef}</h3> 
		</body> 
	</html> 

可以通过<fmt:setLocale> 标签指定所要读取的区域资源文件

	<fmt:setLocale value="zh_CN"/> 
	<fmt:setBundle basename="Message" var="msg"/> 

> 由于设置中文环境，所以将读取Message_zh_CN.properties资源文件

---

---

<h3 id="4.4"> 4.4 数字格式化标签</h3> 

格式化数字显示-format_num.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<fmt:formatNumber value="13463522.2149873" maxIntegerDigits="8" maxFractionDigits="3" groupingUsed="true" var="num1"/> 
			<h3> 13463522.2149873格式化数字结果：${num1}</h3> 
			<fmt:formatNumber value="13463522.2149873" pattern="##.###E0" var="num2"/> 
			<h3> 13463522.2149873科学计数法结果：${num2}</h3> 
		</body> 
	</html> 

数字的反格式化-parse_num.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<fmt:parseNumber value="463,522.21" var="num1"/> 
			<h3> 463,522.21反格式化数字结果：${num1}</h3> 
			<fmt:parseNumber value="4.64E6" pattern="##.###E0" var="num2"/> 
			<h3> 4.64E6反科学计数法结果：${num2}</h3> 
			<fmt:parseNumber value="3.5%" pattern="00%" var="num3"/> 
			<h3> 3.5%反百分号结果：${num3}</h3> 
		</body> 
	</html> 

<h3 id="4.5"> 4.5 日期时间的格式化标签</h3> 

格式化日期时间-format_date.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.setAttribute("date",new Date()) ;
			%> 
				<fmt:formatDate value="${date}" type="both" dateStyle="default" timeStyle="default" var="date"/> 
				<h2> default日期时间显示格式：${date}</h2> 
				<fmt:formatDate value="${date}" type="both" dateStyle="short" timeStyle="short" var="date"/> 
				<h2> short日期时间显示格式：${date}</h2> 
				<fmt:formatDate value="${date}" type="both" dateStyle="medium" timeStyle="medium" var="date"/> 
				<h2> medium日期时间显示格式：${date}</h2> 
				<fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="long" var="date"/> 
				<h2> long日期时间显示格式：${date}</h2> 
				<fmt:formatDate value="${date}" type="both" dateStyle="full" timeStyle="full" var="date"/> 
				<h2> full日期时间显示格式：${date}</h2> 
				<fmt:formatDate value="${date}" type="both" pattern="yyyy年MM月dd日 HH时mm分ss秒SSS毫秒" var="date"/> 
				<h2> 自定义日期时间显示格式：${date}</h2> 
		</body> 
	</html> 

日期的反格式化-parse_date.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
				<fmt:parseDate value="2015年9月7日 星期一 下午04时15分55秒 CST" type="both" dateStyle="full" timeStyle="full" var="date"/> 
				<h3> 字符串转换为日期：${date}</h3> 
				<fmt:parseDate value="2015年09月07日 16时15分55秒891毫秒" type="both" pattern="yyyy年MM月dd日 HH时mm分ss秒SSS毫秒" var="date"/> 
				<h3> 自定义字符串转换为日期：${date}</h3> 
		</body> 
	</html> 

<h3 id="4.6"> 4.6 设置时区</h3> 

设置时区显示-time_zone.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ taglib prefix="fmt" uri="http://tomcat.apache.org/taglibs/jst/fmt"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				pageContext.setAttribute("date",new Date()) ;
			%> 
				<fmt:timeZone value="HST"> 
					<fmt:formatDate value="${date}" type="both" dateStyle="full" timeStyle="full" var="date"/> 
					<h2> 夏威夷时区时间：${date}</h2> 
				</fmt:timeZone> 
		</body> 
	</html> 

---

---

<h2 id="5"> 5 SQL标签库</h2> 

从MVC的设计模式上可以看出，对于web的开发一定是分层的，所有的数据库操作都要放在JavaBean(DAO)中完成，而JSTL是直接在JSP文件上操作数据库，这种做法在开发中并不推荐。

<h3 id="5.1"> 5.1 setDataSource标签</h3> 

此标签用来设定数据源

使用配置好的数据源(jdbc/mldn)-datasource.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="sql" uri="http://tomcat.apache.org/taglibs/jst/sql"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<sql:setDataSource dataSource="jdbc/mldn" var="mldnds"/> 
			<sql:query var="result" dataSource="${mldnds}"> 
				SELECT empno,ename,job,sal,hiredate FROM emp ;
			</sql:query> 
			<h3> 一共有${result.rowCount}条记录！</h3> 
		</body> 
	</html> 

<h3 id="5.2"> 5.2 数据库操作标签</h3> 

查询标签，输出全部的结果-query_emp.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<%@ taglib prefix="sql" uri="http://tomcat.apache.org/taglibs/jst/sql"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<sql:setDataSource dataSource="jdbc/mldn" var="mldnds"/> 
			<sql:query var="result" dataSource="${mldnds}"> 
				SELECT empno,ename,job,sal,hiredate FROM emp ;
			</sql:query> 
			<h3> 一共有${result.rowCount}条记录！</h3> 
			<table border="1" width="100%"> 
				<tr> 
					<td> 雇员编号</td> 
					<td> 雇员姓名</td> 
					<td> 雇员工作</td> 
					<td> 雇佣日期</td> 
					<td> 雇员薪资</td> 
				</tr> 
					<c:forEach items="${result.rows}" var="row"> 
					<tr> 
						<td> ${row.empno}</td> 
						<td> ${row.ename}</td> 
						<td> ${row.job}</td> 
						<td> ${row.hiredate}</td> 
						<td> ${row.sal}</td> 
					</tr> 
					</c:forEach> 
			</table> 
		</body> 
	</html> 

增加新数据标签-inset_emp.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="sql" uri="http://tomcat.apache.org/taglibs/jst/sql"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<sql:setDataSource dataSource="jdbc/mldn" var="mldnds"/> 
			<sql:update var="result" dataSource="${mldnds}"> 
				INSERT INTO emp(empno,ename,job,hiredate,sal) values(6888,'张军','经理','2014-10-22',10000) ;
			</sql:update> 
		</body> 
	</html> 

删除数据标签-delete_emp.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="sql" uri="http://tomcat.apache.org/taglibs/jst/sql"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<sql:setDataSource dataSource="jdbc/mldn" var="mldnds"/> 
			<sql:update var="result" dataSource="${mldnds}"> 
				DELETE FROM emp WHERE empno=6888 ;
			</sql:update> 
		</body> 
	</html> 

使用预处理更新操作-preupdate_emp.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="sql" uri="http://tomcat.apache.org/taglibs/jst/sql"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
		<%
			pageContext.setAttribute("empno",8964) ;
			pageContext.setAttribute("ename","李奇") ;
			pageContext.setAttribute("job","分析员") ;
			pageContext.setAttribute("date",new java.util.Date()) ;
		%> 
			<sql:setDataSource dataSource="jdbc/mldn" var="mldnds"/> 
			<sql:update var="result" dataSource="${mldnds}"> 
				UPDATE emp SET ename=?, job=?, hiredate=? WHERE empno=? ;
				<sql:param value="${ename}"/> 
				<sql:param value="${job}"/> 
				<sql:dateParam value="${date}" type="date"/> 
				<sql:param value="${empno}"/> 
			</sql:update> 
		</body> 
	</html> 

<h3 id="5.3"> 5.3 事务处理</h3> 

transaction.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="sql" uri="http://tomcat.apache.org/taglibs/jst/sql"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<sql:setDataSource dataSource="jdbc/mldn" var="mldnds"/> 
			<sql:transaction isolation="serializable" dataSource="${mldnds}"> 
				<sql:update var="result"> 
					INSERT INTO emp(empno,ename,job,hiredate,sal) values(6888,'张军','经理','2014-10-22',10000) ;
				</sql:update> 
			</sql:transaction> 
		</body> 
	</html> 
---

---

<h2 id="6"> 6 XML标签库</h2> 

JSTL提供的用于XML解析的操作可以轻松的进行XML文件的解析

<h3 id="6.1"> 6.1 XPath简介</h3> 

XPath的主要功能是能够在XML文档中准确的找到某一个节点的元素，`/`代表根元素，`../`表示父节点，`//`表示任何路径下的节点，`@属性名称`表示属性，`*`表示通配符

定义一个XML文件-address.xml

	<?xml version="1.0" encoding="utf-8"?> 
	<address> 
		<linkman> 
			<name id="fmz"> 冯孟昭</name> 
			<email> fengmengzhao@gmail.com</email> 
		</linkman> 
	</address> 



<h3 id="6.2"> 6.2 parse标签</h3> 

进行parse解析后即可以使用out标签输出。

<h3 id="6.3"> 6.3 out标签</h3> 

输出name和email的内容-xml_out.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<%@ taglib prefix="x" uri="http://tomcat.apache.org/taglibs/jst/x"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:import var="add" url="/tag_jst/xml_jst/address.xml" charEncoding="utf-8"/> 
			<x:parse var="addressXml" doc="${add}"/> 
			<h3> 姓名：<x:out select="$addressXml/address/linkman/name"/> (id：<x:out select="$addressXml/address/linkman/name/@id"/> )</h3> 
			<h3> 邮箱：<x:out select="$addressXml/address/linkman/email"/> </h3> 
		</body> 
	</html> 

<h3 id="6.4"> 6.4 set标签</h3> 

使用set标签保存解析结果-xml_set.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<%@ taglib prefix="x" uri="http://tomcat.apache.org/taglibs/jst/x"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:import var="add" url="/tag_jst/xml_jst/address.xml" charEncoding="utf-8"/> 
			<x:parse var="addressXml" doc="${add}"/> 
			<x:set var="nameXml" scope="page" select="$addressXml/address/linkman"/> 
			<h3> 姓名：<x:out select="$nameXml/name"/> (id：<x:out select="$nameXml/name/@id"/> )</h3> 
			<h3> 邮箱：<x:out select="$nameXml/email"/> </h3> 
		</body> 
	</html> 

<h3 id="6.5"> 6.5 if标签</h3> 

使用if标签判断输出-xml_if.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<%@ taglib prefix="x" uri="http://tomcat.apache.org/taglibs/jst/x"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:import var="add" url="/tag_jst/xml_jst/address.xml" charEncoding="utf-8"/> 
			<x:parse var="addressXml" doc="${add}"/> 
			<x:if select="$addressXml//name/@id='fmz'"> 
				<h3> 姓名：<x:out select="$addressXml/address/linkman/name"/> (id：<x:out select="$addressXml/address/linkman/name/@id"/> )</h3> 
			</x:if> 
		</body> 
	</html> 

<h3 id="6.6"> 6.6 choose标签</h3> 

使用choose标签进行判断-xml_choose.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<%@ taglib prefix="x" uri="http://tomcat.apache.org/taglibs/jst/x"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:import var="add" url="/tag_jst/xml_jst/address.xml" charEncoding="utf-8"/> 
			<x:parse var="addressXml" doc="${add}"/> 
			<x:choose> 
				<x:when select="$addressXml//name/@id='fmz'"> 
					<h3> 姓名：<x:out select="$addressXml/address/linkman/name"/> (id：<x:out select="$addressXml/address/linkman/name/@id"/> )</h3> 
				</x:when> 
				<x:otherwise> 
					<h3> 神马也不是！</h3> 
				</x:otherwise> 
			</x:choose> 
		</body> 
	</html> 

<h3 id="6.7"> 6.7 forEach标签</h3> 

定义xml文件-alladdress.xml

	<?xml version="1.0" encoding="utf-8"?> 
	<address> 
		<linkman> 
			<name id="fmz"> 冯孟昭</name> 
			<email> fengmengzhao@gmail.com</email> 
		</linkman> 
		<linkman> 
			<name id="kf"> 客服中心</name> 
			<email> kf@gmail.com</email> 
		</linkman> 
		<linkman> 
			<name id="hr"> 招聘中心</name> 
			<email> hr@gmail.com</email> 
		</linkman> 
	</address> 

forEach输出name节点内容-xml_foreach.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="c" uri="http://tomcat.apache.org/taglibs/jst/c"%> 
	<%@ taglib prefix="x" uri="http://tomcat.apache.org/taglibs/jst/x"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<c:import var="add" url="/tag_jst/xml_jst/alladdress.xml" charEncoding="utf-8"/> 
			<x:parse var="addressXml" doc="${add}"/> 
			<x:forEach select="$addressXml//linkman" var="linkman"> 
				<h3> 姓名：<x:out select="name"/> (编号：<x:out select="name/@id"/> )</h3> 
			</x:forEach> 
		</body> 
	</html> 

---

---

<h2 id="7"> 7 函数标签库</h2> 

所谓的函数标签库就是对String类的封装

字符串判断操作-string_demo01.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="fn" uri="http://tomcat.apache.org/taglibs/jst/fn"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
		<%
			pageContext.setAttribute("info","Hello swufe,Hello Fmz") ;
		%> 
			<h3> 查找Fmz：${fn:contains(info,"Fmz")}</h3> 
			<h3> 不区分大小写查找Fmz：${fn:containsIgnoreCase(info,"fmz")}</h3> 
			<h3> 判断开头：${fn:startsWith(info,"Hello")}</h3> 
			<h3> 判断结尾：${fn:endsWith(info,"Fmz")}</h3> 
			<h3> 查找位置：${fn:indexOf(info,",")}</h3> 
		</body> 
	</html> 

字符串操作-string_demo02.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ taglib prefix="fn" uri="http://tomcat.apache.org/taglibs/jst/fn"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
		<%
			pageContext.setAttribute("info","Hello swufe,Hello Fmz") ;
		%> 
			<h3> 替换：${fn:replace(info,"Fmz","fmz")}</h3> 
			<h3> 截取：${fn:substring(info,0,10)}</h3> 
			<h3> 拆分：${fn:split(info," ")[1]}</h3> 
		</body> 
	</html> 

---

---
