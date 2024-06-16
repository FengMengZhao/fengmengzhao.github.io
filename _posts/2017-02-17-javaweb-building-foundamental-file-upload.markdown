---
layout: post
title: javaweb开发基础篇之文件上传
---

## 目录

- [1 SmartUpload 上传组件](#1)
	- [1.1 上传单个文件](#1.1)
	- [1.2 混合表单](#1.2)
	- [1.3 为上传文件自动命名](#1.3)
	- [1.4 批量上传文件](#1.4)
- [2 2 FileUpload](#2)
	- [2.1 使用FileUpload组件接收上传内容](#2.1)
	- [2.2 保存上传内容](#2.2)
	- [2.3 开发FileUpload组件的专属操作类](#2.3)

---

---

<h2 id="1"> 1 SmartUpload 上传组件</h2> 

SmartUpload 是一套上传组件包，可以实现文件的上传及下载功能，可以轻松的实现上传文件类型的限制，也可以轻易的取得上传文件的名称、后缀、大小等等

<h3 id="1.1"> 1.1 上传单个文件</h3> 

上传表单-smart_upload_demo01.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="smart_upload_demo01.jsp" method="post" enctype="multipart/form-data"> 
				请选择文件：<input type="file" name="pic"> 
				<input type="submit" value="提交"> 
			</form> 
		</body> 
	</html> 

> 在form中使用了`enctype="multipart/form-data"`进行了封装，表示表单将按照二进制的方式进行提交。

接受文件，保存在项目更目录upload文件夹下-smart_upload_demp01.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="org.lxh.smart.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				SmartUpload smart = new SmartUpload() ;//实例化SmartUpload上传组件
				smart.initialize(pageContext) ;//初始化上传操作
				smart.upload() ;//上传准备
				smart.save("/upload") ;//将上传的文件保存在upload文件夹中
			%> 
		</body> 
	</html> 

<h3 id="1.2"> 1.2 混合表单</h3> 

表单一旦被enctype封装之后，其他的非表单控件的内容无法通过request内置对象获得，此时必粗通过SmartUpload类种提供的getRequest()方法取得全部的请求参数。

混合表单-smart_upload_demo02.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="smart_upload_demo02.jsp" method="post" encType="multipart/form-data"> 
				姓名：<input type="text" name="uname"> <br> 
				照片：<input type="file" name="pic"> <br> 
				<input type="submit" value="上传"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

接收封装表单文本数据-smart_uploade_demo02.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="org.lxh.smart.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				SmartUpload smart = new SmartUpload() ;
				smart.initialize(pageContext) ;
				smart.upload() ;
				String name = smart.getRequest().getParameter("uname") ;
				smart.save("/upload") ;
			%> 
			<h2> 姓名：<%=name%> </h2> 
			<h2> 封装后无法通过request内置对象取得：<%=request.getParameter("uname")%> 
		</body> 
	</html> 

> 乱码问题没有解决

> 通过smart.getRequest()方法，必须在smart.upload()完成之后才能实现。

<h3 id="1.3"> 1.3 为上传文件自动命名</h3> 

在上述的上传方式中，如果用户长传的文件名称一致，那么就会发生覆盖，为了解决这个问题，可以采用自动命名的方式，自动命名的方式可以采用 IP地址 + 时间戳 + 三位随机数 的方式。

定义取得IP、时间戳的操作类-IpTimeStamp.java

	package cn.fmz.custom.util ;

	import java.text.- ;
	import java.util.- ;

	public class IpTimeStamp{
		private SimpleDateFormat sdf = null ;
		private String ip = null ;
		
		public IpTimeStamp(){
		
		}
		public IpTimeStamp(String ip){
			this.ip = ip ;
		}

		public String getIpTimeRandom(){
			StringBuffer buf = new StringBuffer() ;
			if(this.ip != null){
				String[] s = this.ip.split("\\:") ;
				for(int i=0;i<s.length;i++){
					buf.append(this.addZero(s[i],3)) ;
				}
			}
			buf.append(this.getTimeStamp()) ;

			Random r = new Random() ;
			for(int i=0;i<3;i++){
				buf.append(r.nextInt(10)) ;
			}

			return buf.toString() ;
		}

		private String addZero(String str,int len){
			StringBuffer buf = new StringBuffer() ;
			buf.append(str) ;
			while(buf.length() < len){
				buf.insert(0,"0") ;
			}
			return buf.toString() ;
		}
		
		public String getTimeStamp(){
			this.sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS") ;
			return this.sdf.format(new Date()) ;
		}
	}

> 编译之后可以更新jar文件，doc中输入如下指令：`jar uf ../lib/javaweb_based.jar(指定路径的jar文件) cn(文件夹)`

> 由于使用的是Ipv6,所以Ip的格式是 xx:xx:xx:xx:...，在上述代码解析的时候要用代码：`String[] s = this.ip.split("\\:") ;`才可以。

自动命名实现-smart_upload_demo03.jsp

	<%@ page contentType="text/html" pageEncoding="UTF-8"%> 
	<%@ page import="org.lxh.smart.*"%> 
	<%@ page import="cn.fmz.custom.util.*"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				SmartUpload smart = new SmartUpload() ;
				smart.initialize(pageContext) ;
				smart.upload() ;
				String name = smart.getRequest().getParameter("uname") ;
				IpTimeStamp its = new IpTimeStamp(request.getRemoteAddr()) ;
				String ext = smart.getFiles().getFile(0).getFileExt() ;//取得文件后缀
				String fileName = its.getIpTimeRandom() + "." + ext ;//拼凑文件的名称
				boolean flag = false ;
				flag = smart.getFiles().getFile(0).getFileName().matches("^\\w+.(jpg|gif)$") ;
				if(flag){
					smart.getFiles().getFile(0).saveAs(this.getServletContext().getRealPath("/")+"upload"+java.io.File.separator + fileName) ;
			%> 
				<center> <h2> 文件上传成功！</h2> </center> 
			<%
				}else{
			%> 
				<center> <h2> 文件格式不正确，上传失败！</h2> </center> 
			<%
				}
			%> 
				<center> 
					<h2> 姓名：<%=name%> </h2> 
					<img src="../upload/<%=fileName%> "> 
				</center> 
		</body> 
	</html> 

<h3 id="1.4"> 1.4 批量上传文件</h3> 

通过代码：`smart.getFiles().getFile(0).saveAs(this.getServletContext().getRealPath("/")+"upload"+java.io.File.separator + fileName) ;`给我们启发，通过循环可以批量上传文件。

表单文件-smart_upload_demo04.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="smart_upload_demo04.jsp" method="post" encType="multipart/form-data"> 
				照片1：<input type="file" name="pic01"> <br> 
				照片2：<input type="file" name="pic02"> <br> 
				照片3：<input type="file" name="pic03"> <br> 
				<input type="submit" value="上传"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

接收表单文件-smart_upload_demo04.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="org.lxh.smart.*,cn.fmz.custom.util.*"%> 
	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				SmartUpload smart = new SmartUpload() ;
				smart.initialize(pageContext) ;
				smart.upload() ;
				IpTimeStamp its = new IpTimeStamp(request.getRemoteAddr()) ;
				for(int x=0;x<smart.getFiles().getCount();x++){
					boolean flag = false ;
					flag = smart.getFiles().getFile(x).getFileName().matches("^\\w+.(jpg|gif)$") ;
					if(flag){
						String ext = smart.getFiles().getFile(x).getFileExt() ;
						String fileName = its.getIpTimeRandom() + "." + ext ;
						smart.getFiles().getFile(x).saveAs(this.getServletContext().getRealPath("/") + "upload" +java.io.File.separator +fileName) ;
			%> 
					<center> 
						<h2> 第<%=x+1%> 个文件上传成功！文件名称为：<%=fileName%> </h2> 
					</center> 

			<%
					}else{
			%> 
				<center> 
					<h2> 第<%=x+1%> 个文件上传失败！</h2> 
				</center> 

			<%
					}
				}
			%> 
		</body> 
	</html> 

---

---

<h2 id="2"> 2 FileUpload</h2> 

FileUpload是Apache组织免费提供的上传组件包，可以直接从Apache组织站点记性[下载](http://commons.apache.org/proper/commons-fileupload/ "FileUpload 组件下载") 。但是FileUpload组件本身还依赖Commons组件，所以要连同Commons组件的IO包一起[下载](http://commons.apache.org/proper/commons-io/ "commonts 组件 IO 包下载")

> 从实际的开发来讲，FileUpload组件使用起来会非常麻烦，建议多使用SmartUpload组件

<h3 id="2.1"> 2.1 使用FileUpload组件接收上传内容</h3> 

上传表单文件-file_upload_demo01.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="file_upload_demo01.jsp" method="post" enctype="multipart/form-data"> 
				姓名：<input type="text" name="uname"> <br> 
				照片：<input type="file" name="pic"> <br> 
				<input type="submit" value="提交"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

接收FileUpload上传的基本步骤

1. 创建磁盘工厂：`DiskFileItemFactory factory = new DiskFileItemFactory() ;`

2. 创建处理工具：`ServletFileUpload upload = new ServletFileUpload(factory) ;`

3. 设置上传文件的大小：upload.setFileSizeMax(3145728) ;

4. 接收全部内容：List<FileItem>  items = upload.parseRequest(request) ;

接收表单文件-file_upload_demo01.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ page import="org.apache.commons.fileupload.disk.*"%> 
	<%@ page import="org.apache.commons.fileupload.servlet.*"%> 
	<%@ page import="org.apache.commons.fileupload.*"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				DiskFileItemFactory factory = new DiskFileItemFactory() ;
				ServletFileUpload upload = new ServletFileUpload(factory) ;
				upload.setFileSizeMax(3145728) ;
				List<FileItem>  items = upload.parseRequest(request) ;
				Iterator<FileItem>  itr = items.iterator() ;
				while(itr.hasNext()){
					FileItem item = itr.next() ;
					String fieldName = item.getFieldName() ;
			%> 
				<ul> <h4> 表单控件的名称：<%=fieldName%>  -->  <%=item.isFormField()%> </h4> 
			<%
					if(!item.isFormField()){//不是普通的文本数据，是上传文件
						String fileName = item.getName() ;
						String contentType = item.getContentType() ;
						long sizeInBytes = item.getSize() ;
			%> 
					<li> 上传文件名称：<%=fileName%> </li> 
					<li> 上传文件类型：<%=contentType%> </li> 
					<li> 上传文件大小：<%=sizeInBytes%> </li> 
			<%
					}else{
						String value = item.getString() ;
			%> 
					<li> 普通参数：<%=value%> </li> 
			<%
					}
			%> 
				</ul> 
			<%
				}
			%> 
		</body> 
	</html> 

> FileUpload组件接收完全部的数据之后，就会储存在List集合中，则需要使用Iterator输出，由于其中既有上传文本又有上传文件，所以每一个内容都需要使用FileItem类表示。

> 当使用Iterator依次取出FileItem对象时，就可以使用FileItem类种的IsFormField()方法判断当前操作的内容是普通文本还是上传文件，如果是上传文件，则将文件内容依次取出；如果是普通文本，直接通过getString()方法取得具体信息。

<h3 id="2.2"> 2.2 保存上传内容</h3> 

表单文件-file_upload_demo02.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="file_upload_demo02.jsp" method="post" enctype="multipart/form-data"> 
				姓名：<input type="text" name="uname"> <br> 
				照片1：<input type="file" name="pic01"> <br> 
				照片2：<input type="file" name="pic02"> <br> 
				照片3：<input type="file" name="pic03"> <br> 
				<input type="submit" value="提交"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

接收表单并保存上传文件-file_upload_demo02.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*,java.io.*"%> 
	<%@ page import="org.apache.commons.fileupload.disk.*"%> 
	<%@ page import="org.apache.commons.fileupload.servlet.*"%> 
	<%@ page import="org.apache.commons.fileupload.*"%> 
	<%@ page import="cn.fmz.custom.util.*"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				DiskFileItemFactory factory = new DiskFileItemFactory() ;//创建磁盘工厂
				factory.setRepository(new File(this.getServletContext().getRealPath("/") + "upload")) ;//设置服务器保存文件夹
				ServletFileUpload upload = new ServletFileUpload(factory) ;//创建处理工具
				upload.setFileSizeMax(3145728) ;//设置最大上传大小为3M，3*1024*1024
				List<FileItem>  items = upload.parseRequest(request) ;//接收全部内容
				Iterator<FileItem>  itr = items.iterator() ;//将全部内容转换为Iterator实例
				IpTimeStamp its = new IpTimeStamp(request.getRemoteAddr()) ;//实例化Ip时间戳对象
				while(itr.hasNext()){//依次取出每一个内容
					FileItem item = itr.next() ;//取出每一个上传文件
					String fieldName = item.getFieldName() ;//得到表单控件的名称
					if(!item.isFormField()){//不是普通的文本数据，是上传文件
						File saveFile = null ;
						InputStream input = null ;
						OutputStream output = null ;
						input = item.getInputStream() ;
						output = new FileOutputStream(new File(this.getServletContext().getRealPath("/") + "upload" + File.separator + its.getIpTimeRandom() + "." + item.getName().split("\\.")[1])) ;//定义输出文件
						byte[] data = new byte[512] ;
						int temp = 0 ;
						while((temp = input.read(data,0,512)) != -1){//依次读取内容
							output.write(data) ;//保存内容
						}
						input.close() ;
						output.close() ;
					}else{
						String value = item.getString() ;
			%> 
					普通参数：<%=value%> 
			<%
					}
				}
			%> 
		</body> 
	</html> 

<h3 id="2.3"> 2.3 开发FileUpload组件的专属操作类</h3> 

FileUploadTools.java

	package org.fmz.java.util ;

	import java.io.- ;
	import java.util.- ;
	import javax.servlet.http.- ;
	import org.fmz.java.util.- ;
	import org.apache.commons.fileupload.- ;
	import org.apache.commons.fileupload.disk.- ;
	import org.apache.commons.fileupload.servlet.- ;

	public class FileUploadTools{
		private HttpServletRequest request = null ;//取得HttpServletRequest对象
		private List<FileItem>  items = null ;//保存全部的上传内容
		private Map<String,List<String> >  params = new HashMap<String,List<String> > () ;//保存所有的参数
		private Map<String,FileItem>  files = new HashMap<String,FileItem> () ;//保存全部的上传文件
		private int maxSize = 3145728 ;//默认上传文件大小为3M：3*1024*1024

		public FileUploadTools(HttpServletRequest request,int maxSize,String tempDir)throws Exception{//传递request对象，最大上传限制，临时保存目录
			this.request = request ;
			DiskFileItemFactory factory = new DiskFileItemFactory() ;//创建磁盘工厂
			if(tempDir != null){//判断是否需要进行临时文件上传
				factory.setRepository(new File(tempDir)) ;//设置临时文件保存目录
			}
			ServletFileUpload upload = new ServletFileUpload(factory) ;//创建处理工具
			if(maxSize >  0){//如果已知上传大小限制大于0，则使用新的设置
				this.maxSize = maxSize ;
			}
			upload.setFileSizeMax(this.maxSize) ;//设置最大上传大小限度为3M：3*1024*1024
			try{
				this.items = upload.parseRequest(request) ;//接收全部内容
			}catch(Exception e){
				throw e ;//向上抛出异常
			}
			this.init() ;//进行初始化操作
		}

		public void init(){
			Iterator<FileItem>  iter = this.items.iterator() ;
			IpTimeStamp its = new IpTimeStamp(request.getRemoteAddr()) ;
			while(iter.hasNext()){//依次取出每一个上传项
				FileItem item = iter.next() ;//取出每一个上传的文件
				if(item.isFormField()){
					String name = item.getFieldName() ;//取得表单的名称
					String value = item.getString() ;//取得表单的内容
					List<String>  temp = null ;//保存内容
					if(this.params.containsKey(name)){//判断内容是否已经存放
						temp = this.params.get(name) ;//如果存在则取出
					}else{//不存在
						temp = new ArrayList<String> () ;//重新开辟list数组
					}
					temp.add(value) ;//向List数组中设置内容
					this.params.put(name,temp) ;//向Map中增加内容
				}else{
					String fileName = its.getIpTimeRandom() + "." + item.getName().split("\\.")[1] ;
					this.files.put(fileName,item) ;
				}
			}
		}

		public String getParameter(String name){//取得一个参数
			String ret = null ;//保存返回内容
			List<String>  temp = this.params.get(name) ;//从集合中取出内容
			if(temp != null){//判断是否可以根据key值取出内容
				ret = temp.get(0) ;//取出其中的内容
			}
			return ret ;
		}

		public String[] getParameterValues(String name){//取得一组上传内容
			String[] ret = null ;//保存返回内容
			List<String>  temp = this.params.get(name) ;//从集合中取出内容
			if(temp != null){//避免NullPointerException
				ret = temp.toArray(new String[] {}) ;//将内容变为字符串数组
			}
			return ret ;
		}

		public Map<String,FileItem>  getUploadFiles(){//取得全部的上传文件
			return this.files ;//得到全部的上传文件
		}

		public List<String>  saveAll(String saveDir)throws IOException{//保存全部文件，并返回文件明名称
			List<String>  names = new ArrayList<String> () ;
			if(this.files.size() >  0){
				Set<String>  keys = this.files.keySet() ;//取得全部的Key
				Iterator<String>  iter = keys.iterator() ;//实例化Iterator对象
				File saveFile = null ;
				InputStream input = null ;
				OutputStream output = null ;
				while(iter.hasNext()){
					FileItem item = this.files.get(iter.next()) ;
					String fileName = new IpTimeStamp(this.request.getRemoteAddr()).getIpTimeRandom() + "." + item.getName().split("\\.")[1] ;
					saveFile = new File(saveDir + fileName) ;//重新拼凑出来的路径
					names.add(fileName) ;//保存生成后的文件名称
					try{
						input = item.getInputStream() ;
						output = new FileOutputStream(saveFile) ;
						int temp = 0 ;
						byte[] data = new byte[512] ;
						while((temp = input.read(data,0,512)) != -1){
							output.write(data) ;
						}
					}catch(IOException e){
						throw e ;
					}finally{
						try{
							input.close() ;
							output.close() ;
						}catch(IOException e1){
							throw e1 ;
						}
					}
				}
			}
			return names ;
		}

	}

混合表单-file_upload_demo03.html

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<form action="file_upload_demo03.jsp" method="post" enctype="multipart/form-data"> 
				姓名：<input type="text" name="uname"> <br> 
				兴趣：<input type="checkbox" name="inst" value="Swim"> 游泳
				<input type="checkbox" name="inst" value="Sing"> 唱歌
				<input type="checkbox" name="inst" value="Run"> 跑步
				<input type="checkbox" name="inst" value="Read"> 读书<br> 
				照片1：<input type="file" name="pic01"> <br> 
				照片2：<input type="file" name="pic02"> <br> 
				照片3：<input type="file" name="pic03"> <br> 
				<input type="submit" value="提交"> 
				<input type="reset" value="重置"> 
			</form> 
		</body> 
	</html> 

接收表单-file_upload_demo03.jsp

	<%@ page contentType="text/html" pageEncoding="utf-8"%> 
	<%@ page import="java.util.*"%> 
	<%@ page import="org.fmz.java.util.*"%> 

	<!doctype html> 
	<html> 
		<head> 
			<meta charset="utf-8"> 
			<title> </title> 
		</head> 
		<body> 
			<%
				request.setCharacterEncoding("utf-8") ;
				FileUploadTools fut = new FileUploadTools(request,3*1024*1024,this.getServletContext().getRealPath("/") + "uploadtemp") ;
				String name = fut.getParameter("uname") ;
				String[] inst = fut.getParameterValues("inst") ;
				List<String>  list = fut.saveAll(this.getServletContext().getRealPath("/") + "upload" + java.io.File.separator) ;
			%> 
				<h3> 姓名：<%=name%> </h3> 
				<h3> 兴趣：
			<%
				for(int x=0;x<inst.length;x++){
			%> 
					<%=inst[x]%> 、
			<%
				}
			%> 
				</h3> 
			<%
				Iterator<String>  iter = list.iterator() ;
				while(iter.hasNext()){
			%> 
					<img src="../upload/<%=iter.next()%> "> 
			<%
				}
			%> 
		</body> 
	</html> 

> FileUploadTools只是完成了一些基本的功能，可以在此基础上进一步的完善，以适应各种开发的需求。

---

---
