---
layout: post
title: Javaweb基础知识
---

2015/7/22

> HTML(Hypertext Markup Language) is the standard markup language used to cread web pages,是一种超文本标记语言。

### 表格HTML代码示例

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>冯孟昭个人简历</title>
		</head>
		<body>
			<h2>基本信息</h2>
			<table border="1" width="100%">
				<tr>
					<td>姓名</td>
					<td>冯孟昭</td>
				</tr>

				<tr>
					<td>联系方式</td>
					<td>邮箱：fengmengzhao@gmail.com</td>
				</tr>

				<tr>
					<td>教育背景</td>
					<td>黑龙江科技大学（2010级）  西南财经大学（2014级）</td>
				</tr>

				<tr>
					<td>博客</td>
					<td>
						<a href="http://fengmengzhao.github.io">http://fengmengzhao.github.io</a>
					</td>
				</tr>
			</table>
		</body>
	</html>

### 表单HTML代码示例

	<!doctype html>
	<html>
		<head>
			<title>表单</title>
			<meta charset="utf-8">
		</head>
		<body>
			<form action="" method="post" >
				编&nbsp号：<input type="text" name="userid" value="No." size="2" maxlength="2"><br>

				用户名：<input type="text" name="username value="请输入用户名"><br>

				密&nbsp码：<input type="password" name="password" value="请输入用户名"><br>

				性&nbsp别：<input type="radio" name="sex" value="男" checked>男

				<input type="radio" name="sex" value="女">女<br>

				部&nbsp门：<select name="dept">
									<option value="技术部">技术部</option>
									<option value="销售部" selected>销售部</option>
									<option value="财务部">财务部</option>
				</select><br>

				兴&nbsp趣：<input type="checkbox" name=inst value="唱歌">唱歌
				<input type="checkbox" name=inst value="读书">读书
				<input type="checkbox" name=inst value="跳舞">跳舞
				<input type="checkbox" name=inst value="编程" checked>编程
				<input type="checkbox" name=inst value="游泳">游泳<br>
				
				说&nbsp明：<textarea name="note" cols="30" rows="3">冯孟昭学习javaweb基础课程</textarea><br>

				<input type="submit" value="注册"><input type="reset" value="重置">
			</form>
		</body>
	</html>

> JavaScript是一种基于对象的脚本语言(Scripting language),可以直接使用已经提供好的对象进行操作。JavaScript中所有的变量都用`var`声明，变量的类型由具体的变量内容决定，变量声明为了简便可以不用声明直接使用变量。
JavaScript中的函数都使用`function`关键字来声明，如果需要函数有返回值则直接通过`return`返回即可。

### 九九乘法表HTML代码示例（JavaScript）

	<!doctype html>
	<html>
		<head>
			<title>javaScript九九乘法表</title>
			<meta charset="utf-8">
			<script language="javascript">
				document.write("<table border=\"1\">") ;
					for(i=1;i<=9;i++){
						document.write("<tr>") ;
						for(j=1;j<=9;j++){
							if(i>=j){
								document.write("<td>"+i+"*"+j+"="+i*j+"</td>") ;
							}else{
								document.write("<td>&nbsp</td>") ;
							}
						}
						document.write("</tr>") ;
					}
				document.write("</table>") ;	
			</script>
		</head>
		<body>
		</body>
	</html>

***

2015/7/27

### 事件处理之onLoad

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>事件处理</title>
			<script language="javascript">
				function hello(){
					alert("欢迎您的光临！") ;
				}
				
				function byebye(){
					alert("谢谢您的光临！") ;
				}
			</script>
		</head>
		<body onload="hello()" onUnload="byebye()">
			
		</body>
	</html>

### 事件处理之onClick

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>事件处理</title>
			<script language="javascript">
				function fun(){
					alert("欢迎光临！！！") ;
				}
			</script>
		</head>
		<body>
			<h3><a href="http://fengmengzhao.github.io" onclick="fun()">冯孟昭博客</h3>
		</body>
	</html>

### 事件处理之表单(正则表达式)

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>表单操作</title>
			<script language="javascript">
				function validate(f){
					value = f.email.value ;

					if(!/^\w+@\w+.\w+$/.test(value)){
						alert("邮箱输入格式不正确") ;
						f.email.focus() ;
						f.email.select() ;
						return false ;
					}

					return true ;
				}
			</script>
		</head>
		<body>
			<form action="表单.html" method="post" name="myform" onsubmit="return validate(this)">
				Email: <input type="text" name="email">
				<input type="submit" value="提交"s> 
			</form>
		</body>
	</html>

### 事件处理之表单(显示)

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>表单</title>
			<script language="javascript">
				function show(){
					var name=document.myform.name.value ;
					alert("姓名：" + name) ;

					var sex ;
					if(document.myform.sex[0].checked){
						sex=document.myform.sex[0].value ;
					}else{
						sex=document.myform.sex[1].value ;
					}
					alert("性别：" + sex) ;

					var inst="" ;
					for(i=0;i<document.myform.inst.length;i++){
						if(document.myform.inst[i].checked){
							inst+=document.myform.inst[i].value+"；";
						}
					}
					alert("兴趣：" + inst) ;
				}
			</script>
		</head>
		<body>
			<form action="" method="post" name="myform">
			姓名：<input type="text" name="name"><br>
			性别：<input type="radio" name="sex" value="男" checked>男
			<input type="radio" name="sex" value="女" checked>女<br>
			兴趣：<input type="checkbox" name="inst" value="唱歌">唱歌
			<input type="checkbox" name="inst" value="跳舞">跳舞
			<input type="checkbox" name="inst" value="游泳">游泳
			<input type="checkbox" name="inst" value="编程" checked>编程
			<input type="checkbox" name="inst" value="上网">上网<br>
			<input type="button" value="显示" onClick="show()">
			</form>
		</body>
	</html>

### 事件处理之表单(chekbox)

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>表单</title>
			<script language="javascript">
				function show(val){
					document.myform.result.value=val ;
				}
			</script>
		</head>
		<body>
			<form action="" method="post" name="myform">
				部门：<select name="dept" onChange="show(this.value)">
					<option value="技术部">技术部</option>
					<option value="销售部" selected>销售部</option>
					<option value="财务部">财务部</option>
				</select>
				<input type="text" name="result" value="">
			</form>
		</body>
	</html>

### window之confirm

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>window</title>
			<script language="javascript">
				function fun(){
					if(window.confirm("确认删除？")){
						alert("您选择是") ;
					}else{
						alert("你选择否") ;
					}
				}
			</script>
		</head>
		<body>
			<a href="#" onClick="fun()">删除邮件</a>
		</body>
	</html>

### window之站点切换

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>window</title>
			<script language="javascript">
				function fun(url){
					window.location=url ;
				}
			</script>
		</head>
		<body>
			<form action="" method="post" name="myform">
				友情链接：<select name="url" onChange="fun(this.value)">
					<option value="#">==================请选择站点==================</option>
					<option value="http://fengmengzhao.github.io">冯孟昭博客</option>
					<option value="http://baidu.com">百度</option>
					<option value="http://sohu.com">搜狐</option>
				</select> 
			</form>
		</body>
	</html>

### window之父子窗口

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>window</title>
			<script language="javascript">
				function fun(url){
					window.open(url,"弹出页面","width=500,height=500,scrollable=yes,resizable=no") ;
				}
			</script>
		</head>
		<body>
			<form name="myform">
				<input type="button" value="打开" onClick="fun('openerDemo.html')">
			</form>
		</body>
	</html>

> 父窗口

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>window</title>
			<script language="javascript">
				function closeWin(){
					window.close() ;
					
				}
				window.opener.location.reload() ;
			</script>
		</head>
		<body>
			<h3 a href="#" onClick="closeWin()">关闭网页</h3>
		</body>
	</html>

> 子窗口

### window之父窗口返回子窗口内容

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>window</title>
			<script language="javascript">
				function showNewPage(url){
					window.open(url,"弹出页面","width=500,height=500,scrollable=yes,resizable=no") ;
				}
			</script>
		</head>
		<body>
			<form name="parentForm">
				<input type="button" value="选择信息" onClick="showNewPage('content.html')"><br>
				选择结果：<input type="text" name="result" value="">
			</form>
		</body>
	</html>

> 父窗口

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>window</title>
			<script language="javascript">
				function returnValue(){
					var city = document.myform.city.value ;
					var doc = window.opener.document ;
					doc.parentForm.result.value = city ;
				}
			</script>
		</head>
		<body>
			<form name="myform">
				选择：<select name="city">
					<option value="北京">北京</option>
					<option value="上海">上海</option>
					<option value="广州">广州</option>
					<option value="天津">天津</option>
					<option value="重庆">重庆</option>
				</select><br>
				<input type="button" value="返回" onClick="returnValue()">
			</form>
		</body>
	</html>

> 子窗口

***

2015/7/28

## XML

>XML(eXtended Markup Language,可扩展的标记性语言)提供一种跨平台、跨程序、跨网络的语言的数据描述方式，使用XML可以方便地实现数据交换、系统配置、内容管理等常见的功能。
XML和HTML类似，都是标记性语言，两者都是由SGML(Standard General Markup Language)语言发展而来的，最大的不同是HTML中的元素都是固定的，且以现实为主，而XML语言中的标记都是用户自定义的，主要以数据的保存为主。

### 比较HTML和XML的区别

#### HTML下的记事本

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>记事本</title>
		</head>
		<body>
			<ul>
				<li>冯孟昭</li>
				<ul>
					<li>id: 001</li>
					<li>school: 西南财经大学</li>
					<li>email: fengmengzhao@gmail.com</li>
					<li>tel: 18723344555</li>
					<li>site: fengmengzhao.github.io</li>
				</ul>
			</ul>
		</body>
	</html>

#### XML下的记事本

	<?xml version="1.0"?>
	<?xml-stylesheet type="text/css" href="记事本.css"?>
	<addresslist>
		<linkman>
			<name>冯孟昭</name>
			<id>001</id>
			<school>西南财经大学</school>
			<email>fengmengzhao@gmail.com</email>
			<tel>18723344555</tel>
			<site>fengmengzhao.github.io</site>
		</linkman>
	</addresslist>

#### 记事本的css样式

	name
	{	display:block;
		color:blue;
		font-size:20pt;
		font-weight:bold;	}

	id,school,email,tel,size
	{	display:block;
		color:black;
		font-size:14pt;
		font-weight:normal;
		font-style:italic;	}

> 使用css样式可以让一个XML文件按照HTML的样式进行显示，但是从实际上来讲，XML并不是用来显示的，更多的是用于数据结构的描述，如果想要进行数据的显示，则使用HTML会更加方便。

> XML的注释语法`<[!CDATA[不解析内容]]>`

### XML解析

#### DOM解析

> DOM(Document Object Model,文档对象模型)，在应用程序中基于DOM的XML解析器将一个XML文档转换成一个对象模型集合(通常称为DOM树)，应用程序通过对这个对象模型的操作，来实现XML文档的数据操作。通过DOM接口，应用程序可以在任何时候访问XML文档的任何一部分数据，因此这种利用DOM接口的机制称作随机访问机制。
DOM分析器将整个XML文档转化成DOM树存放在内存中

##### XML文档-联系人

	<?xml version="1.0"?>
	<addresslist>
		<linkman>
			<name>冯孟昭</name>
			<email>fengmengzhao@gmai.com</email>
		</linkman>
		<linkman>
			<name>陈亮亮</name>
			<email>342652980@qq.com</email>
		</linkman>
	</addresslist>

> DOM解析中四个核心接口

> `Document`,代表整个XML文档，表示整颗DOM树的根，提供了对文档中数据进行访问和操作的入口

> `Node`,每一个Node接口代表DOM树中的一个节点

> `NodeList`,此接口表示一个节点的集合，一般用于表示顺序关系的一组节点

> `NamedNodeMap`,此节点表示一组节点和其唯一名称对应的一一对应关系，主要用于属性节点的表示

##### Dom解析XML文件的步骤

1. 创建DocumentFactory

2. 创建DocumentBuilder

3. 创建Document

4. 创建NodeList

***

2015/7/29

##### DOM解析联系人.xml文件的代码

	import java.io.File;
	import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;
	import org.w3c.dom.Document;
	import org.w3c.dom.Element;
	import org.w3c.dom.NodeList;

	public class DomParsingDemo02 {
		public static void main(String args[])throws Exception{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;//创建DocumentFactory
			DocumentBuilder builder = factory.newDocumentBuilder() ;//创建DocumentBuilder
			Document doc = builder.parse(new File("D:\\html_file\\domDemo01.xml")) ;//创建Document
			NodeList nl = doc.getElementsByTagName("linkman") ;//创建NodeList
			
			//输出
			for(int i=0;i<nl.getLength();i++){
				Element e = (Element) nl.item(i) ;
				System.out.println("姓名："+e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue()+"\t"+"邮箱："+e.getElementsByTagName("email").item(0).getFirstChild().getNodeValue()) ;
			}
		}
	}

##### DOM解析XML文件示例

	<?xml version="1.0"?>
	<addresslist>
		<linkman>
			<name>冯孟昭</name>
			<id>001</id>
			<school>西南财经大学</school>
			<email>fengmengzhao@gmail.com</email>
			<tel>18723344555</tel>
			<site>fengmengzhao.github.io</site>
		</linkman>
	</addresslist>

> 待解析XML文件

	import java.io.*;
	import javax.xml.parsers.*;
	import org.w3c.dom.*;

	public class DomParsingDemo03
	{
		public static void main(String args[])throws Exception
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
			DocumentBuilder builder = factory.newDocumentBuilder() ;
			Document doc = builder.parse(new File("D:\\html_file\\domDemo03.xml")) ;
			NodeList nl = doc.getElementsByTagName("linkman") ;

			for(int i=0;i<nl.getLength();i++)
			{
				Element e = (Element)nl.item(i) ;
				System.out.println("姓名："+e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue()) ;
				System.out.println("id："+e.getElementsByTagName("id").item(0).getFirstChild().getNodeValue()) ;
				System.out.println("学校："+e.getElementsByTagName("school").item(0).getFirstChild().getNodeValue()) ;
				System.out.println("邮箱："+e.getElementsByTagName("email").item(0).getFirstChild().getNodeValue()) ;
				System.out.println("电话："+e.getElementsByTagName("tel").item(0).getFirstChild().getNodeValue()) ;
				System.out.println("主页："+e.getElementsByTagName("site").item(0).getFirstChild().getNodeValue()) ;
			}
		}
	}

> 解析XML文件代码

##### DOM将XML文档写入硬盘

###### 要写入的文档内容

	<?xml version="1.0" encoding="GBK" standalone="no"?>
	<addresslist>
	<linkman>
	<name>冯孟昭</name>
	<email>fengmengzhao@gmail.com</email>
	</linkman>
	</addresslist>

###### 写入文档代码

	package summerVacation;

	import java.io.File;

	import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;
	import javax.xml.transform.OutputKeys;
	import javax.xml.transform.Transformer;
	import javax.xml.transform.TransformerFactory;
	import javax.xml.transform.dom.DOMSource;
	import javax.xml.transform.stream.StreamResult;

	import org.w3c.dom.Document;
	import org.w3c.dom.Element;

	public class WriteDOMFileDemo01 {
		public static void main(String args[])throws Exception{
			//设置入口
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
			DocumentBuilder builder = factory.newDocumentBuilder() ;
			Document doc = builder.newDocument() ;
			
			//创建节点
			Element addresslist = doc.createElement("addresslist") ;
			Element linkman = doc.createElement("linkman") ;
			Element name = doc.createElement("name") ;
			Element email = doc.createElement("email") ;
			
			//创建DOM树状结构
			name.appendChild(doc.createTextNode("冯孟昭")) ;
			email.appendChild(doc.createTextNode("fengmengzhao@gmail.com")) ;
			linkman.appendChild(name) ;
			linkman.appendChild(email) ;
			addresslist.appendChild(linkman) ;
			doc.appendChild(addresslist) ;
			
			//将DOM树状接口转化为XML文档输出
			TransformerFactory tf = TransformerFactory.newInstance() ;
			Transformer t = tf.newTransformer() ;
			t.setOutputProperty(OutputKeys.ENCODING, "GBK") ;
			DOMSource source = new DOMSource(doc) ;
			StreamResult result = new StreamResult(new File("D:\\html_file\\DomOutputDemo01.xml")) ;
			t.transform(source, result) ;
		}
	}


2015/7/30

### SAX解析

> SAX(Simple APIS for XML,操作XML的简单接口)，SAX采用的是一种顺序的模式进行访问，是一种快速读取XML数据的方式。

> 如果在开发中想要使用SAX解析，首先需要编写一SAX解析器。

#### 编写SAX解析器

	package summerVacation;

	import org.xml.sax.Attributes;
	import org.xml.sax.SAXException;
	import org.xml.sax.helpers.DefaultHandler;

	public class MySAX extends DefaultHandler{
		
			public void startDocument()
			throws SAXException{
				System.out.println("<?xml version=\"1.0\"?>") ;
			}
			
			public void endDocument()
			throws SAXException{
				System.out.println("文档读取结束。。。") ;
			}
			
			public void startElement(String uri,
			String localName,
			String qName,
			Attributes attributes)
		 throws SAXException{
				System.out.print("<") ;
				System.out.print(qName) ;
				if(attributes != null){
					for(int i=0;i<attributes.getLength();i++){
						System.out.print(" "+attributes.getQName(i)+"=\""+attributes.getValue(i)+"\"") ;
					}
				}
				System.out.print(">") ;
			}
			
			public void characters(char[] ch,
			int start,
			int length)
		 throws SAXException{
				System.out.print(new String(ch,start,length)) ;
			}
			
			public void endElement(String uri,
			String localName,
			String qName)
		 throws SAXException{
				System.out.print("</") ;
				System.out.print(qName) ;
				System.out.println(">") ; 
			}
	}


#### 要解析的xml文件

	<?xml version="1.0"?>
	<addresslist>
		<linkman id="fmz">
			<name>冯孟昭</name>

			<email>fengmengzhao@gmai.com</email>

		</linkman>

		<linkman id="cll">
			<name>陈亮亮</name>

			<email>342652980@qq.com</email>

		</linkman>

	</addresslist>

#### 解析测试代码

	package summerVacation;

	import javax.xml.parsers.*;

	public class SaxParsingDemo02 {

		public static void main(String args[])throws Exception{
			//建立SAX解析工厂
			SAXParserFactory factory = SAXParserFactory.newInstance() ;
			//构建解析器
			SAXParser parser = factory.newSAXParser() ;
			//解析XML，使用HANDLER
			parser.parse("d:\\html_file\\saxDemo02.xml", new MySAX());
		}
	}

> SAX解析明显要比DOM解析更加容易，DOM解析适合于对文件进行修改和随机存储的操作，但不适于大型文件的操作。SAX采用部分读取的方式，可以处理大型文件，而且只需要从文件中读取特定内容，SAX可以由用户自己建立自己的对象模型。

### JDOM

> JDOM = DOM + SAX,[JDOM](http://www.jdom.org/ "JODM组件下载")是一个开源的java组件，它以直接易懂的形式向java开发者描述了XML文档和文档的内容。

#### JDOM输出文件代码(与DOM的输出相比较)

	package summerVacation;

	import java.io.File;
	import java.io.FileOutputStream;

	import org.jdom.Attribute;
	import org.jdom.Document;
	import org.jdom.Element;
	import org.jdom.output.XMLOutputter;


	public class JdomWriteXml {
		public static void main(String args[]) throws Exception{
			
			//定义节点
			Element addresslist = new Element("addresslist") ;
			Element linkman = new Element("linkman") ;
			Element name = new Element("name") ;
			Element email = new Element("email") ;
			
			//定义属性
			Attribute id = new Attribute("id","fmz") ;
			
			//声明Document对象,同时定义根节点
			Document doc = new Document(addresslist) ;
			
			//设置元素的内容
			name.setText("冯孟昭") ;
			email.setText("1913565900@qq.com") ;
			
			//设置元素的属性
			name.setAttribute(id) ;
			
			//设置节点关系
			linkman.addContent(name) ;
			linkman.addContent(email) ;
			addresslist.addContent(linkman) ;
			
			//设置输出
			XMLOutputter out = new XMLOutputter() ;
			out.setFormat(out.getFormat().setEncoding("utf-8")) ;//设置编码，GBK与ANSI相对应
			out.output(doc, new FileOutputStream(new File("d:\\html_file\\JdomDemo01.xml"))) ;
		}
	}

2015/7/31

#### JDOM解析XML文件(与SAX解析相比较)

#### 带解析XML文件

	<?xml version="1.0" encoding="utf-8"?>
	<addresslist>
		<linkman>
			<name id="fmz">冯孟昭</name>
			<email>1913565900@qq.com</email>
		</linkman>
	</addresslist>

#### JDOM解析文件代码

	package summerVacation;

	import java.io.File;
	import java.util.Iterator;
	import java.util.List;

	import org.jdom.Document;
	import org.jdom.Element;
	import org.jdom.input.SAXBuilder;

	public class JdomReadXMLDemo01 {
		public static void main(String args[])throws Exception{
			SAXBuilder builder = new SAXBuilder() ;//建立SAX解析
			Document doc_read = builder.build(new File("d:\\html_file\\jdomDemo01.xml")) ;//找到Document文件
			Element root =  doc_read.getRootElement() ;//读取根元素
			List<?> list = root.getChildren("linkman") ;//读取全部的子元素
			
			Iterator<?> itr = list.iterator() ;//构造迭代器
			while(itr.hasNext()){
				Element e = (Element)itr.next() ;
				String name = e.getChildText("name") ;
				//String id = e.getChild("name").getAttribute("id").getValue() ;
				String id = e.getChild("name").getAttributeValue("id") ;
				String email = e.getChildText("email") ;
				
				System.out.println("姓名："+name+"，编号："+id) ;
				System.out.println("邮箱："+email) ;
			}
		}
	}

### DOM4J

> [DOM4J](http://www.sourceforge.net/projects/dom4j/files/ "DOM4J组件下载")也是XML的一个开源的操作组件包，主要用来读写XML文件，一些框架例如Hibernate和Spring都使用了DOM4J进行XML的解析。

> 在导入包的过程中，还要导入lib\jaxen-1.1-beta-6.jar文件，否则执行时可能抛出java.lang.NoClassDefFoundError: org\jaxen\JaxenException 异常

#### DOM4J输出XML文档代码

	package summerVacation;

	import java.io.File;
	import java.io.FileOutputStream;

	import org.dom4j.Document;
	import org.dom4j.DocumentHelper;
	import org.dom4j.Element;
	import org.dom4j.io.OutputFormat;
	import org.dom4j.io.XMLWriter;

	public class DOM4JWriteXMLDemo01 {
		public static void main(String args[])throws Exception{
			Document doc = DocumentHelper.createDocument() ;//创建文档
			
			//创建节点和节点之间的关系
			Element addresslist = doc.addElement("addresslist") ;
			Element linkman = addresslist.addElement("linkman") ;
			Element name = linkman.addElement("name") ;
			Element email = linkman.addElement("email") ;
			
			//设置节点内容
			name.setText("冯孟昭") ;
			name.addAttribute("id", "fmz") ;//增加属性
			name.addAttribute("name", "fmz") ;
			email.setText("fengmengzhao@gmail.com") ;
			email.addAttribute("id", "GOOGLE EMAIL") ;
			
			//输出
			OutputFormat output = OutputFormat.createPrettyPrint() ;
			output.setEncoding("utf-8") ;//格式设置
			XMLWriter writer = new XMLWriter(new FileOutputStream(new File("D:\\html_file\\dom4jDemo01.xml")),output) ;//输出文件
			writer.write(doc) ;//输出内容
			writer.close() ;
		}
	}

#### DOM4J 解析输出的文档

	package summerVacation;

	import java.io.File;
	import java.util.Iterator;

	import org.dom4j.io.SAXReader;
	import org.dom4j.Document;
	import org.dom4j.Element;

	public class DOM4JParsingDemo01 {
		public static void main(String args[])throws Exception{
			SAXReader reader = new SAXReader() ;//建立SAX解析读取
			Document doc = reader.read(new File("d:\\html_file\\dom4jDemo01.xml")) ;//读取文档
			Element root = doc.getRootElement() ;
			
			Iterator<?> itr = root.elementIterator() ;
			while(itr.hasNext()){
				Element linkman = (Element)itr.next() ;
				System.out.println("姓名："+linkman.elementText("name")) ;
				System.out.println("邮箱："+linkman.elementText("email")) ;
			}
		}
	}

> XML的解析操作工具非常多，如JAXP和STAX等，但是其核心从操作原理就是DOM和SAX，只要掌握住核心就可以了

### 使用JavaScript操作DOM

> 由于在HTML语言中，其本身采用了标记语言的方式，所以在HTML中通过JavaScript进行DOM操作，这样可以使页面运行更加绚丽、丰富。

2015/8/3

在HTML中可以通过`document.getElementById("id名称")`取得每一个设置的表单元素的对象，程序如下：

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>JavaScript控制DOM树操作</title>
			<script language="javaScript">
				function show(){
					//通过DOM解析取得info元素，并设置其内容
					document.getElementById("info").innerHTML = "<h2>http://fengmegnzhao.github.io/</h2>"
				}
			</script>
		</head>
		<body>
			<form action="" method="post">
				<input type="button" value="显示" onClick="show()">
				<span id="info"></span>
			</form>
		</body>
	</html>

#### 通过DOM取得下拉列表

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>JavaScript控制DOM树操作</title>
			<script language="javaScript">
				function setFun(){
					var id = new Array(1,2,3) ;
					var value = new Array("北京","上海","广州") ;

					//通过DOM取得下拉列表
					var select = document.getElementById("area") ;
					select.length = 1 ;//设置每次只能选中一个
					select.options[0].selected = true ;//设置第一个为默认选中

					for(i=0;i<id.length;i++){
						//设置option中的内容，建立option节点
						var option = document.createElement("option") ;
						option.setAttribute("value",id[i]) ;//设置option属性值
						//在option元素下增加文本节点
						option.appendChild(document.createTextNode(value[i])) ;
						select.appendChild(option) ;
					}
				}
			</script>
		</head>
		<body onload="setFun()">
			<form action="" method="post" name="myform">
				<select name="city" id="area">
					<option value="0">没有城市</option>
				</select>
			</form>
		</body>
	</html>

#### 通过DOM操作完成增加和删除行

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>JavaScript控制DOM树操作动态增加和删除表格</title>
			<script language="javaScript">
				var count = 3 ;
				function addRow(){
					var table = document.getElementById("mytable") ;//取得表格(DOM 操作)
					var tr = table.insertRow() ;//插入一个新的行
					var td1 = tr.insertCell() ;//为行插入单元格
					var td2 = tr.insertCell() ;//为行插入单元格
					var td3 = tr.insertCell() ;//为行插入单元格
					td1.innerHTML = "冯孟昭" + "_" + count;
					td2.innerHTML = "SWUFE" ;
					td3.innerHTML = "<input type='button' value='-' onClick='delRow(this);'>" ;
					count++ ;
				}

				function delRow(btn){
					var tr = btn.parentNode.parentNode ;
					var table = document.getElementById("mytable") ;
					table.deleteRow(tr.rowIndex) ;
				}
			</script>
		</head>
		<body>
			<input type="button" value="+" onClick="addRow();">
			<table id="mytable" border="1">
				<tr>
					<td>冯孟昭_1</td>
					<td>黑龙江科技大学</td>
					<td><input type="button" value="-" onClick="delRow(this);"></td>
				</tr>
				<tr>
					<td>冯孟昭_2</td>
					<td>西南财经大学</td>
					<td><input type="button" value="-" onClick="delRow(this);"></td>
				</tr>
			</table>
		</body>
	</html>

> 以上的代码通过JavaScript完成，增加和删除的操作也可以通过DOM完成

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>JavaScript控制DOM树操作动态增加和删除表格(DOM 操作完成)</title>
			<script language="javaScript">
				function addRow(){
					var table = document.getElementById("mytable") ;
					var id = document.getElementById("id").value ;
					var name = document.getElementById("name").value ;
					
					var tbody = document.createElement("tbody") ;
					var tr = document.createElement("tr") ;
					var td_id = document.createElement("td") ;
					var td_name = document.createElement("td") ;
					var td_delButton = document.createElement("td") ;

					td_id.appendChild(document.createTextNode(id)) ;
					td_name.appendChild(document.createTextNode(name)) ;
					td_delButton.innerHTML = "<input type='button' value='-' onClick='delRow(this);'>" ;//JavaScript的操作（还没有或者DOM操作如何完成）
					tr.appendChild(td_id) ;
					tr.appendChild(td_name) ;
					tr.appendChild(td_delButton) ;
					tbody.appendChild(tr) ;
					table.appendChild(tbody) ;
				}
				function delRow(thisbtn){
					var table = document.getElementById("mytable") ;
					var tr = thisbtn.parentNode.parentNode ;
					table.deleteRow(tr.rowIndex) ;
				}
			</script>
		</head>
		<body>
			编号：<input type="text" id="id">
			姓名：<input type="text" id="name">
			<input type="button" value="+" onClick="addRow();">
			<table id="mytable" border="1">
				<tr>
					<td>编号</td>
					<td>姓名</td> 
				</tr>
			</table>
		</body>
	</html>

>DOM操作，将之看做一个DOM树，需要创造节点`createElement()`和增加节点`appendChild()`；而JavaScript的操作主要是基于对象的现成的一些方法

### Tomcat服务器的安装及配置

#### 静态请求与动态请求的区别

在一般的web站点中，html、htm之类的后缀往往都属于静态请求，所以这样的操作一般是通过文件系统取出然后返回的，而如果请求的后缀是jsp、php之类的话，则可定就是动态请求了。

静态请求所有的代码都是固定的，而动态请求所有的代码都是拼凑出来的。

#### [Tomcat](http://www.apache.org "Apache软件基金会")

#### Tomcat主要目录的作用

| No. | 目录 | 作用 |
| 1 | bin | 所有可执行二进制命令，启用和关闭服务器的命令就在此文件夹中 |
| 2 | conf | 服务器的配置文件夹，其中保存了各种配置信息 |
| 3 | lib | Tomcat所需要的各个库文件 |
| 4 | logs | 保存服务器的系统日志 |
| 5 | webapps | web应用程序保存的目录，web项目保存到此目录即可发布 |
| 6 | work | 临时文件夹，生成所有的临时文件(*.java*、.class) |

#### Tomcat服务器配置

#### 修改端口号

Tomcat服务器安装后，默认端口号是8080，可以通过Tomcat目录conf\server.xml文件内容进行修改

`<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />`改为

	<Connector port="80" protocol="HTTP/1.1"
	connectionTimeout="20000"
	redirectPort="8443" 
	useBodyEncodingForURL="true"
	IEncoding="utf-8" 
	URIEncoding="utf-8"/>

以上配置完成后，服务器必须重启才能实现，因为服务器每次重启都会访问`server.xml`文件

#### 配置虚拟目录(Tomcat服务器配置最重要的配置)

在conf\server.xml文件的代码`</Host>`前面加入代码`<Context path="/fmz" docBase="D:\fmzwebDemo"/>` 

如果页面`http://localhost/fmz/` 运行异常，将conf\web.xml文件代码 `<init-param><param-name>listings</param-name><param-value>false</param-value></init-param>`改为`<init-param><param-name>listings</param-name><param-value>true</param-value></init-param>`即可

#### 配置首页

在虚拟目录fmzwebDemo文件夹下建立`index.html`，在输入`http:localhost/fmz/` 之后会自动跳转到`index.html`，主要原因是在conf\web.xml文件内有如下代码

	<welcome-file-list>
		<welcome-file>index.html</welcome-file>
		<welcome-file>index.htm</welcome-file>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>

> 默认值为`index.html`可以修改为其他，如：`main.html`

2015/8/4

### 编写第一个jsp文件

> jsp(Java Server page),jsp文件后缀必须为*.jsp,文件名的字母要求全部是小写

> 所谓的jsp程序代码开发指的是在HTML代码中嵌入大量的java代码而已

#### jsp文件执行过程

首先一个客户端向服务器端发送一个请求页面地址，服务器端在接收到用户请求的内容后对*.jsp文件进行转换，将其转化成*.java源文件，并最终编译为*.class文件，最后真正的执行也是以*.class文件为主的。*.java和*.class文件可以在Tomcat文件目录的work文件夹下找到。

### 交互性

表单文件(input.html)

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>fmz javaweb</title>
		</head>
		<body>
			<form action="input.jsp" method="post">
				请输入要显示的内容：<input type="text" name="info">
				<input type="submit" value="显示">
			</form>
		</body>
	</html>

JSP文件(input.jsp)

	<!doctype html>
	<html>
		<head>
			<meta charset="utf-8">
			<title>fmz javaweb</title>
		</head>
		<body>
			<%
				String str = request.getParameter("info") ;//接收表单输入内容
				out.println("<h1>"+str+"</h1>") ;//输出信息
			%>
		</body>
	</html>

> 以上完成了一个简单的交互程序操作，可以通过表单与服务器进行交互，在JSP中服务器要想取得客户端输入的信息，需要使用`request.getParameter("info")`操作，其中`request`就是JSP提供的一个内置对象，而`getParameter()`方法接收的参数就是表单中文本框对应的名称。
