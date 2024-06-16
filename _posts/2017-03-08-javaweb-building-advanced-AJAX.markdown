---
layout: post
title: javaweb开发高级篇之AJAX技术
---

## 目录

- [1 AJAX技术简介](#1)
- [2 XMLHttpRequest对象](#2)
- [3 第一个AJAX程序](#3)
- [4 异步验证](#4)
- [5 返回xml数据](#5)

---

---

<h2 id="1"> 1 AJAX技术简介</h2> 

AJAX(Asynchronous JavaScript and XML，异步JavaScript和XML)主要的目的是用于页面的局部刷新
---

---

<h2 id="2"> 2 XMLHttpRequest对象</h2> 

创建XMLHttpRequest对象-create_ajax.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
			<script language="javaScript"> 
				var xmlHttp ;//AJAX核心对象名称
				function creatXMLHttp(){//创建XMLHttpRequest核心对象
					if(window.XMLHttpRequest){//判断当前使用的浏览器类型
						xmlHttp = new XMLHttpRequest() ;//表示使用的是FireFox内核的浏览器
					}else{//表示使用的是IE内核的浏览器
						xmlHttp = new ActiveXObject("Microsoft.XMLHTTP") ;
					}
				}
			</script> 
		</head> 
		<body> 
			
		</body> 
	</html> 

---

---

<h2 id="3"> 3 第一个AJAX程序</h2> 

返回数据页面-content.html

	Hello World!!!

使用异步处理-ajax_receive_content.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
			<script language="javaScript"> 
				var xmlHttp ;//AJAX核心对象名称
				function creatXMLHttp(){//创建XMLHttpRequest核心对象
					if(window.XMLHttpRequest){//判断当前使用的浏览器类型
						xmlHttp = new XMLHttpRequest() ;//表示使用的是FireFox内核的浏览器
					}else{//表示使用的是IE内核的浏览器
						xmlHttp = new ActiveXObject("Microsoft.XMLHTTP") ;
					}
				}
				function showMsg(){
					creatXMLHttp() ;//创建一个xmlHttp核心对象
					xmlHttp.open("POST","content.htm") ;//设置一个请求
					//设置请求完成之后处理回调函数
					xmlHttp.onreadystatechange = showMsgCallback ;
					xmlHttp.send(null) ;//发送请求，不传递任何参数
				}
				function showMsgCallback(){//定义回调函数
					if(xmlHttp.readystate = 4){//数据返回完毕
						if(xmlHttp.status == 200){//Http 操作正常
							var text = xmlHttp.responseText ;//接收返回内容
							//设置要使用的css样式表名称
							document.getElementById("msg").className = "样式表名称" ;
							//设置Msg标签元素中要显示的内容为AJAX接收的返回值内容
							document.getElementById("msg").innerHTML = text ;
						}
					}
				}
			</script> 
		</head> 
		<body> 
			<input type="button" onclick="showMsg()" value="调用AJAX显示内容"> 
			<span id="msg"> </span> 
		</body> 
	</html> 

---

---

<h2 id="4"> 4 异步验证</h2> 

编写注册表表单-register.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
			<script language="javaScript"> 
				var xmlHttp ;
				var flag ;
				function creatXMLHttp(){
					if(window.XMLHttpResponse){
						xmlHttp = new XMlHttpResponse() ;
					}else{
						xmlHttp = new ActiveXObject("Microsoft.XMLHTTP") ;
					}
				}
				function checkUserid(userid){
					creatXMLHttp() ;
					xmlHttp.open("POST","checkServlet?userid="+userid) ;
					xmlHttp.onreadystatechange = checkUseridCallback ;
					xmlHttp.send(null) ;
					document.getElementById("msg").innerHTML = "正在验证..." ;
				}
				function checkUseridCallback(){
					if(xmlHttp.readyState == 4){
						if(xmlHttp.status == 200){
							var text = xmlHttp.responseText ;
							if(text == "true"){
								flag = false ;
								document.getElementById("msg").innerHTML = "用户ID重复，无法使用！" ;
							}else{
								flag = true ;
								document.getElementById("msg").innerHTML = "此用户ID可以注册！" ;
							}
						}
					}
				}
				function checkForm(){
					return flag ;
				}
			</script> 
		</head> 
		<body> 
			<form action="register.jsp" method="post" onsubmit="return checkForm()"> 
				用户ID：<input type="text" name="userid" onblur="checkUserid(this.value)"> <span id="msg"> </span> <br> 
				姓&nbsp;名：<input type="text" name="uname"> <br> 
				密&nbsp;码：<input type="password" name="upass"> <br> 
				<input type="submit" value="注册"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

验证用户名是否存在Servlet程序-CheckServlet.java

	package org.fmz.ajaxdemo ;

	import java.io.- ;
	import java.sql.- ;
	import javax.servlet.- ;
	import javax.servlet.http.- ;

	public class CheckServlet extends HttpServlet{
		public static final String DBDRIVER = "org.gjt.mm.mysql.Driver" ;
		public static final String DBURL = "jdbc:mysql://localhost:3306/mldn" ;
		public static final String DBUSER = "root" ;
		public static final String DBPASS = "mysqladmin" ;
		public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
			request.setCharacterEncoding("utf-8") ;
			response.setContentType("text/html") ;
			Connection conn = null ;
			PreparedStatement pstmt = null ;
			ResultSet rs = null ;
			PrintWriter out = response.getWriter() ;
			String userid = request.getParameter("userid") ;
			try{
				Class.forName(DBDRIVER) ;
				conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS) ;
				String sql = "SELECT COUNT(userid) FROM user WHERE userid=?" ;
				pstmt = conn.prepareStatement(sql) ;
				pstmt.setString(1,userid) ;
				rs = pstmt.executeQuery() ;
				if(rs.next()){
					if(rs.getInt(1) >  0){
						out.print("true") ;
					}else{
						out.print("false") ;
					}
				}
				out.close() ;
			}catch(Exception e){
				e.printStackTrace() ;
			}finally{
				try{
					rs.close() ;
					pstmt.close() ;
					conn.close() ;
				}catch(Exception e){
					e.printStackTrace() ;
				}
			}
			
		}
		public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
			this.doPost(request,response) ;
		}
	}
---

---

<h2 id="5"> 5 返回xml数据</h2> 

xml文件-allarea.xml

	<?xml version="1.0" encoding="utf-8"?> 
	<allarea> 
		<area> 
			<id> 1</id> 
			<title> 北京</title> 
		</area> 
		<area> 
			<id> 2</id> 
			<title> 上海</title> 
		</area> 
		<area> 
			<id> 3</id> 
			<title> 广州</title> 
		</area> 
	</allarea> 

使用AJAX解析，并生成下拉列表框-ajax_select.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
			<script language="javaScript"> 
				var xmlHttp ;
				function createXMLHttp(){
					if(window.XMLHttpRequest){
						xmlHttp = new XMLHttpRequest() ;
					}else{
						xmlHttp = ActiveXObject("Microsoft.XMLHTTP") ;
					}
				}
				function getCity(){
					createXMLHttp() ;
					xmlHttp.open("POST","allarea.xml") ;
					xmlHttp.onreadystatechange = getCityCallback ;
					xmlHttp.send(null) ;
				}
				function getCityCallback(){
					if(xmlHttp.readyState == 4){
						if(xmlHttp.status == 200){
							var allarea = xmlHttp.responseXML.getElementsByTagName("allarea")[0].childNodes ;//取得下拉列表框city对象
							var select = document.getElementById("city") ;
							select.length = 1 ;
							select.options[0].selected = true ;
							for(var i=0;i < allarea.length;i++){
								var area = allarea[i] ;
								var option = document.createElement("option") ;//创建option元素
								var id = area.getElementsByTagName("id")[0].firstChild.nodeValue ;//取得每一个<area> 中的id元素
								var title = area.getElementsByTagName("title")[0].firstChild.nodeValue ;
								option.setAttribute("value",id) ;//在option元素中设置显示内容
								option.appendChild(document.createTextNode(title)) ;
								select.appendChild(option) ;
							}
						}
					}
				}
			</script> 
		</head> 
		<body onLoad="getCity()"> 
			<form action="" method="post"> 
				请选择喜欢的城市：
				<select name="city"> 
					<option value="0"> =====请选择城市=====</option> 
				</select> 
			</form> 
		</body> 
	</html> 

> 解析文件没有成功！！！

利用dom解析，Servlet程序-CityServlet.java

	package org.fmz.ajaxdemo ;

	import java.io.- ;
	import org.w3c.dom.- ;
	import javax.xml.transform.- ;
	import javax.xml.transform.dom.- ;
	import javax.xml.transform.stream.- ;
	import javax.xml.parsers.- ;
	import javax.servlet.- ;
	import javax.servlet.http.- ;

	public class CityServlet extends HttpServlet{
		public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
			this.doPost(request,response) ;
		}
		public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
			response.setContentType("text/xml;charset=utf-8") ;
			PrintWriter out = response.getWriter() ;
			ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
			try{
			DocumentBuilderFactory db_factory = DocumentBuilderFactory.newInstance() ;
			DocumentBuilder builder = db_factory.newDocumentBuilder() ;
			Document doc = builder.newDocument() ;
			String[] data = {"北京","上海","广州","天津","香港"} ;
			Element allarea = doc.createElement("allarea") ;
			for(int x=0;x<data.length;x++){
				Element area = doc.createElement("area") ;
				Element id = doc.createElement("id") ;
				Element title = doc.createElement("title") ;
				id.appendChild(doc.createTextNode("" + (x+1))) ;
				title.appendChild(doc.createTextNode(data[x])) ;
				area.appendChild(id) ;
				area.appendChild(title) ;
				allarea.appendChild(area) ;
			}
			doc.appendChild(allarea) ;
			TransformerFactory tf_factory = TransformerFactory.newInstance() ;
			Transformer t = tf_factory.newTransformer() ;
			t.setOutputProperty(OutputKeys.ENCODING,"utf-8") ;
			DOMSource source = new DOMSource(doc) ;
			StreamResult result = new StreamResult(bos) ;
			t.transform(source,result) ;
			out.println(bos) ;
			out.close() ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
		}
	}

调用Servlet程序，生成下拉列表框-ajax_select_servlet.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
			<script language="JavaScript"> 
				var xmlHttp ;
				function createXMLHttp(){
					if(window.XMLHttpRequest){
						xmlHttp = new XMLHttpRequest() ;
					}else{
						xmlHttp = ActiveXObject("Microsoft.XMLHTTP") ;
					}
				}
				function getCity(){
					createXMLHttp() ;
					xmlHttp.open("POST","cityServlet") ;
					xmlHttp.onreadystatechange = getCityCallback ;
					xmlHttp.send(null) ;
				}
				function getCityCallback(){
					if(xmlHttp.readyState == 4){
						if(xmlHttp.status == 200){
							var allarea = xmlHttp.responseXML.getElementsByTagName("allarea")[0].childNodes ;//取得下拉列表框city对象
							var select = document.getElementById("city") ;
							select.length = 1 ;
							select.options[0].selected = true ;
							for(var i=0;i < allarea.length;i++){
								var area = allarea[i] ;
								var option = document.createElement("option") ;//创建option元素
								var id = area.getElementsByTagName("id")[0].firstChild.nodeValue ;//取得每一个<area> 中的id元素
								var title = area.getElementsByTagName("title")[0].firstChild.nodeValue ;
								option.setAttribute("value",id) ;//在option元素中设置显示内容
								option.appendChild(document.createTextNode(title)) ;
								select.appendChild(option) ;
							}
						}
					}
				}
			</script> 
		</head> 
		<body onLoad="getCity()"> 
			<form action="" method="post"> 
				请选择喜欢的城市：
				<select name="city"> 
					<option value="0"> =====请选择城市=====</option> 
				</select> 
			</form> 
		</body> 
	</html> 

> 解析文件没有成功！！！

---

---
