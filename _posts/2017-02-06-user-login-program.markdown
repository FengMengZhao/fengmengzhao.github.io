---
layout: post
title: 用户登录程序开发
---

### 完成登录界面需要的页面

1. login.html 登录页，提供用户的登录表单，可以输入用户的id和密码
2. login_check.jsp 登录检查页，根据表单提交过来的id和密码进行数据库验证。成功，则跳转到登陆成功页；否则，跳转到登录失败页。
3. login_success.jsp 登录成功页，显示欢迎信息
4. login_failure.jsp 登录失败页，提示用户登录错误，并提供用户重新登录的超链接

数据库建立表格

	/*======================= 使用MLDN数据库 =======================*/
	USE mldn ;
	/*======================= 删除user数据表 =======================*/
	DROP TABLE IF EXISTS user ;
	/*======================= 创建user数据表 =======================*/
	CREATE TABLE user(
		userid			VARCHAR(30)		PRIMARY KEY ,
		name			VARCHAR(30)		NOT NULL ,
		password		VARCHAR(32)		NOT NULL
	) ;
	/*======================= 插入测试数据 =======================*/
	INSERT INTO user (userid,name,password) VALUES ('admin','某某某','admin') ;

### 程序实现

#### 登录页

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>登录界面</title>
		</head>
		<body>
			<center>
				<form action="login_check.jsp" method="post">
					<table border="1">
						<tr colspan="2">
							<td>用户登录</td>
						</tr>
						<tr>
							<td>登录&nbsp;ID：</td>
							<td><input type="text" name="id"></td>
						</tr>
						<tr>
							<td>登录密码：</td>
							<td><input type="password" name="password"></td>
						</tr>
						<tr colspan="2">
							<td><input type="submit" value="提交"></td>
							<td><input type="reset" value="重置"></td>
						</tr>
					</table>
				</form>
			</center>
		</body>
	</html>

#### 登录检查页

	<%@ page language="java" contentType="text/htm;" pageEncoding="utf-8"%>
	<%@ page import="java.sql.*"%>
	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title></title>
		</head>
		<body>
		<%!
			public static final String DBDRIVER = "org.gjt.mm.mysql.Driver" ;
			public static final String DBURL ="jdbc:mysql://localhost:3306/fmz?useUnicode=true&characterEncoding=utf-8" ;
			public static final String DBUSER = "root" ;
			public static final String DBPASS = "mysqladmin" ;
		%>
		<%
			Connection conn = null ;
			PreparedStatement pstmt = null ;
			ResultSet rs = null ;
			boolean flag = false ;
			String name = null ;
		%>
		<%
			try{
				Class.forName(DBDRIVER) ;
				conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS) ;

				String sql = "SELECT name FROM user WHERE userid = ? AND password=?" ;
				pstmt = conn.prepareStatement(sql) ;
				pstmt.setString(1,request.getParameter("id")) ;
				pstmt.setString(2,request.getParameter("password")) ;
				rs = pstmt.executeQuery() ;
				if(rs.next()){
					name = rs.getString(1) ;
					flag = true ;
				}
		%>
		<%
			}catch(Exception e){
			System.out.println(e) ;
			}finally{
				try{
					rs.close() ;
					pstmt.close() ;
					conn.close() ;
				}catch(Exception e){
					System.out.println(e) ;
				}
			}
		%>
		<%
			if(flag){
		%>
			<jsp:forward page="login_success.jsp">
				<jsp:param name="uname" value="<%=name%>"/>
			</jsp:forward>
		<%
			}else{
		%>
			<jsp:forward page="login_failure.html"/>
		<%
			}
		%>
		</body>
	</html>

#### 登录成功页

	<%@ page contentType="text/html" pageEncoding="utf-8"%>
	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title></title>
		</head>
		<body>
			<center>
				<h2>登录成功</h2>
				<h2>欢迎<font color="red"><%=request.getParameter("uname")%></font>光临！<h2>
			</center>
		</body>
	</html>

#### 登录失败页

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title></title>
		</head>
		<body>
			<h2>登录失败，请<a href="login.html">重新登录</a></h2>
		</body>
	</html>

> jdbc链接mysql数据库，在显示中文时，出现乱码，目前还没有解决。
