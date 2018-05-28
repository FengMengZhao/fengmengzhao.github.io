---
layout: post
title: JavaScript之JavaScript和DOM编程艺术
---

## 目录

- [1 JavaScript简史](#1)
	- [1.1 JavaScript起源](#1.1)
	- [1.2 DOM](#1.2)
	- [1.3 浏览器的战争](#1.3)
		- [1.3.1 DHTML](#1.3.1)
		- [1.3.2 浏览器之间的冲突](#1.3.2)
	- [1.4 制定标准](#1.4)
		- [1.4.1 浏览器以外的考虑](#1.4.1)
		- [1.4.2 浏览器战争的结局](#1.4.2)
		- [1.4.3 崭新的起点](#1.4.3)
- [2 JavaScript语法](#2)
- [3 DOM](#3)
- [4 JavaScript图片库](#4)
- [5 最佳实践](#5)
- [6 动态创建标记](#6)
- [7 充实文档内容](#7)

---
---

<h2 id="1">1 JavaScript简史</h2>

<h3 id="1.1">1.1 JavaScript起源</h3>

JavaScript是Netspace公司和Sun公司合作可发的，是一种脚本语言，只能通过浏览器完成一些操作而不能向普通意义上的程序那样独立运行，需要web浏览器进行解释执行。

<h3 id="1.2">1.2 DOM</h3>

DOM是对文档的内容进行抽象和概念化的方法。

<h3 id="1.3">1.3 浏览器的战争</h3>

<h4 id="1.3.1">1.3.1 DHTML</h4>

DHTML是Dynamic HTML 的简称，是描述HTML、CSS和JavaScript技术组合的术语。

> 利用HTML将网页标记成各种元素，利用CSS设置元素样式和它们显示的位置，利用JavaScript实时的操控页面和改变样式。

<h4 id="1.3.2">1.3.2 浏览器之间的冲突</h4>

DHTML打开了一个充满机会的全新世界，但是要想进入其中的人们却发现这是一个充满苦难的世界。

<h3 id="1.4">1.4 制定标准</h3>

w3c推出了标准化的DOM，1998年10月完成了DOM Level 1 。

w3c推出的的DOM可以让任何一种程序设计语言对使用任何一种标记语言编写出来的任何一份文档进行操控

<h4 id="1.4.1">1.4.1 浏览器以外的考虑</h4>

DOM是一种API(应用编程接口)，简单地说API就是取得各方共同认可的基本约定。

w3c对DOM的定义：一个与系统平台和程序语言无关的接口，程序和脚本可以通过这个接口动态访问和修改文档的内容、结构和样式。

<h4 id="1.4.2">1.4.2 浏览器战争的结局</h4>

微软战胜了Netscape，只是因为所有装有Windows的人都预装了微软的IE，这时候[WebSP(Web Standard Project,web标准计划)](http://webstandards.org "web标准计划")的小组应运而生。

<h4 id="1.4.3">1.4.3 崭新的起点</h4>

[WebKit](http://webkit.org "WebKit")是Safari和Chrome浏览器采用的一种开源web浏览器引擎，Firefox浏览器的核心是[Gecko](https://developers.mozilla.org/en/Gecko "火狐浏览器的核心")，这些在推荐微软的核心Trident逐步向web靠近的过程中起到特别积极的作用。

---
---

<h2 id="2">2 JavaScript语法</h2>

<h3 id="2.1">2.1 准备工作</h3>

引入*.js文件的方式

	<!doctype html>
    <html lang="en">
		<head>
			<meta charset="UTF-8">
			<title></title>
			<script src="*.js"></script>
		</head>
		<body>

		</body>
	</html>	

或者

	<!doctype html>
    <html lang="en">
		<head>
			<meta charset="UTF-8">
			<title></title>
		</head>
		<body>
			<script src="*.js"></script>
		</body>
	</html>	

>这样能使浏览器更快的加载页面。

<h3 id="2.2">2.2 语法</h3>

<h4 id="2.2.1">2.2.1 语句(statement)</h4>

建议每一条JavaScript statement 面加上一个`;`,表示一条statement的结束.

	first statement;
	second statement;

<h4 id="2.2.2">2.2.2 注释(comment)</h4>

用 `//` 注释单行，用 `/*    */` 注释多行，还可以用 `<!--   -->` 注释

<h4 id="2.2.3">2.2.3 变量(variable)</h4>

把值存入变量叫做赋值(assignment)

在JavaScript语言中，变量和其他语法元素的名字都是区分字母大小写的，变量不必事先声明，但是为了良好的编程习惯，变量前面加上 `var`，变量允许字母、数字、下划线和美元符号，但是第一个字符不允许是数字，变量中可以适当的加入下划线，亦可以用驼峰模式(camel case)，首字母小写，其他单词首字母大写，驼峰格式是函数名、方法名和对象属性名命名的首选格式。

<h4 id="2.2.4">2.2.4 数据类型</h4>

**JavaScript是一个动态,弱类型的语言.**

弱类型语言的变量没有类型,有类型的是变量的值.

1. 字符串，字符串可以用单引号或者双引号，根据需求，如果字符串中含有双引号，就把整个字符串放在单引号中；如果字符串中含有单引号，就把整个字符串放在双引号之中；根据适当的情况可以进行单引号或者双引号的转移操作；注意在整个js文件中使用一种风格的字符串操作。
2. 数值，不区分整数或者浮点数，直接赋值即可。
3. Boolean值。

<h4 id="2.2.5">2.2.5 数组</h4>

变量意味着在任意的时刻只能有一个值，如果想要储存一组值，就需要使用数组(Array)，例如：

	var beatles = Array(4) ;
	beatles[0] = "John"
	beatles[1] = "Paul"
	beatles[2] = "George"
	beatles[3] = "Ringo"

或者

	var beatles = Array("John","Paul","George","Ringo") ;

>JavaScript中数组声明可以不必给出数组的大小：`var beatles = Array() ;`

关联数组，示例：

	var lennon = Array() ;
	lennon["name"] = "John" ;
	lennon["year"] = 1940 ;
	lennon["living"] = false ;

>这是一种关联数组，不推荐使用

<h4 id="2.2.6">2.2.6 对象</h4>

与数组类似，对象也是使用一个名字表示一组值，对象中的每一个值都是对象的一个属性，示例：

	var lennon = Object() ;
	lennon.name = "John" ;
	lennon.year = 1940 ;
	lennon.living = false ;

对象赋值还可以写成这样：

	var lennon = {name:"John",year:1940,living:false} ;

> 用多想来代替传统数组的做法意味着可以通过元素的名字而不是下标数字来引用他们，大大提高了脚本的可读性。<br><br>
对象的声明可以直接用`{}`例如：`var lennon = {}`，表示lennon是一个对象。 

<h3 id="2.3">2.3 操作(operation)</h3>

同java语言

<h3 id="2.4">2.4 条件语句</h3>

同java语言

<h3 id="2.2">2.5 循环语句 </h3>

同java语言

<h3 id="2.6">2.6 函数</h3>

如果需要多次使用同一个代码，可以把它们封装成一个函数，函数就是一组允许在你的代码中随时调用的语句，事实上每一个函数都是一个短小的脚本。

> 函数的作用除了重复代码随时调用之外，函数真正威力在于把不同的数据传递给他们，他们将使用这些数据完成预定的操作，传递给函数的数据称之为参数(argument)。<br><br>
函数真正价值体现在，我们可以把他作为一个数据类型来使用，可以把函数调用的结果赋值给一个变量。<br><br>
命名规范，在命名变量时建议用下划线区分，在命名函数时用大小写的驼峰命名法。

变量的作用域(scope)

- 全局变量(global variable)
- 局部变量(local variable)

> 如果在某个函数中使用了var，那么这个变量就被视为一种局部变量，只存在于函数的上下文之中；反之，如果没有var，这个变量就会被视为全局变量，如果脚本中存在同名的全局变量，这个函数就会改变这个全局变量的值。

<h3 id="2.7">2.7 对象</h3>

对象是一种非常特殊的数据类型，是包含数据的集合。对象中的数据可以通过两种形式访问，属性(property)和方法(method)，属性是隶属于某个特定对象的变量，方法是只有某个特定的对象才能调用的函数。对象就是属性和方法组合在一起构成的数据实体。

<h4 id="2.7.1">2.7.1 内建对象</h4>

例如Array、Math、Date都是JavaScript的内建对象。

<h4 id="2.7.2">2.7.2 宿主对象</h4>

JavaScript脚本语言还可以使用一些已经预定好的其他对象，这些对象不是由JavaScript语言本身而是由它的运行环境提供的。具体到web应用，这个环境就是浏览器，由浏览器提供的预定对象被称为宿主对象(host object)。宿主对象包括`Form`、`Image`、`Element`等。另外一个对象是网页的任何一个元素的信息，它就是`document`对象。

---

---

<h2 id="3">3 DOM</h2>

<h3 id="3.1">3.1 DOM中的D(Document)</h3>

当创建一个网页并把它加载到web浏览器的时候，DOM就在幕后悄然而生了，它把你编写的网页文档转化为一个文档对象。

<h3 id="3.2">3.2 DOM中的O(host object,宿主对象)</h3>

在JavaScript最初的版本中，对编写脚本语言非常重要的一些宿主对象已经可以使用了，最基础的是window对象。

<h3 id="3.3">3.3 DOM中的M(Model || Map)</h3>

M代表Model或者说是Map，含义是某种事物的表现形式。

DOM把一份文档表示成一棵树，一颗家谱树(parent,child,sibling)。

<h3 id="3.3">3.4 节点</h3>

节点 Node ，是网络术语，表示网络中的一个连接点。一个网络就是由一个节点构成的集合。

<h4 id="3.4.1">3.4.1 元素节点</h4>

DOM的原子是元素节点(element node)

<h4 id="3.4.2">3.4.2 文本节点</h4>

元素节点只是节点类型的一种，如果文档只是由元素节点构成，它将只是一个结构。

<h4 id="3.4.3">3.4.3 属性节点</h4>

属性节点是用来对元素作出更具体的描述。

<h4 id="3.4.4">3.4.4 CSS</h4>

DOM并不是与网页结构打交道的唯一技术，我们还可用CSS告诉浏览器如何显示一份文档的的内容

继承(inheritance)是CSS技术的一个强大功能，类似于DOM，CSS把文档内容视为一个节点树。节点树各个元素将继承父元素的样式属性。

为了将某一个元素或者几个元素同其他元素区分开来，需要用class属性或者id属性。

1. class属性，在样式表中可以用`.class属性{}`定义CSS内容，当然还可以指定特定类型的元素属性：`h2.class属性{}`。
2. id属性，用途是给网页里的某个元素加上独一无二的标识符，用`#id属性{}`定义CSS内容，还可以为包含该特定元素里的其他元素定义样式：`#id属性 li{}`

<h4 id="3.4.5">3.4.5 获取元素</h4>

1. `document.getElementById()`，返回一个对象，事实上文档中的每一个元素都是一个对象。
2. `document.getElementByTagName()`，返回一个对象数组
3. `document.getElementByClassName()`，返回一个对象数组

示例：

	<!doctype html>
	<html lan="en">
		<head>
			<title></title>
			<meta charset="utf-8">
		</head>
		<body>
			<h1>What to buy?</h1>
			<p title="a gentle reminder">Do not forget to buy this stuff.</p>
			<ul id="purchases">
				<li>A tin of beans</li>
				<li class="sal">Cheese</li>
				<li class="sal important">Milk</li>
			</ul>
			<script>
				//alert(typeof document.getElementById("purchases")) ;
				//alert(document.getElementsByTagName("li").length) ;
				//alert(document.getElementById("purchases").getElementsByTagName("*").length) ;
				//alert(document.getElementsByClassName("important sal").length) ;
				alert(getElementsByClassName(document.getElementById("purchases"),"important sal").length) ;
				function getElementsByClassName(node classname){
					if(node.getElemnetsByClassName){
						return node.getElementsByClassName(classname) ;
					}else{
						var results = new Array() ;
						var elems = node.getElementsByTagName("*") ;
						for(var i=0;i<elems.length;i++){
							if(elems.className.indexOf(classname) != -1){
								results[results.length] = elems[i] ;
							}
						}
						return results ;
					}
				}
			</script>
		</body>
	</html>

> 一份文档就是一颗节点树。<br><br>
节点分为不同的类型，元素节点、属性节点和文本节点。<br><br>
getElementById返回一个对象，该对象对应着文档里的一个特定的元素节点。<br><br>
getElementsByTagName和getElementsByClassName将返回一个对象数组，他们对应着文档里面的一组特定的元素节点。<br><br>
每个节点都是一个对象。

<h3 id="3.5">3.5 获取和设置元素</h3>

<h4 id="3.5.1">3.5.1 getAttribute</h4>

示例：

	<!doctype html>
	<html lan="en">
		<head>
			<title></title>
			<meta charset="utf-8">
		</head>
		<body>
			<h1>What to buy?</h1>
			<p title="a gentle reminder">Do not forget to buy this stuff.</p>
			<ul id="purchases">
				<li>A tin of beans</li>
				<li class="sal">Cheese</li>
				<li class="sal important">Milk</li>
			</ul>
			<script>
				var paras = document.getElementsByTagName("p") ;
				alert(paras[0].getAttribute("title")) ;
				var lan = document.getElementsByTagName("html") ;
				alert(lan[0].getAttribute("lan")) ;
				var para = document.getElementsByTagName("p") ;
				for(var i=0;i<para.length;i++){
					var title_text = para[i].getAttribute("title") ;
					if(title_text != null){//通过一个判断，避免输出内容为空
						alert(title_text) ;
					}
				}
				for(var i=0;i<para.length;i++){
					var title_text = para[i].getAttribute("title") ;
					if(title_text){//更加简洁且贴近生活的判断
						alert(title_text) ;
					}
				}
			</script>
		</body>
	</html>

<h4 id="3.5.2">3.5.2 setAttribute</h4>

示例：

	<!doctype html>
	<html lan="en">
		<head>
			<title></title>
			<meta charset="utf-8">
		</head>
		<body>
			<h1>What to buy?</h1>
			<p title="a gentle reminder">Do not forget to buy this stuff.</p>
			<ul id="purchases">
				<li>A tin of beans</li>
				<li class="sal">Cheese</li>
				<li class="sal important">Milk</li>
			</ul>
			<script>
				var shopping = document.getElementById("purchases") ;
				alert(shopping.getAttribute("title")) ;
				shopping.setAttribute("title","a list of goods") ;
				alert(shopping.getAttribute("title")) ;
			</script>
		</body>
	</html>

---

---

<h2 id="4">4 JavaScript图片库</h2>

<h3 id="4.1">4.1 标记</h3>

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title>Images Gallery</title>
	 </head>
	 <body>
		<h1>Images</h1>
		<ul>
			<li>
				<a href="images/1.jpg" title="pic1">图片1</a>
			</li>
			<li>
				<a href="images/2.jpg" title="pic2">图片2</a>
			</li>
			<li>
				<a href="images/3.jpg" title="pic3">图片3</a>
			</li>
			<li>
				<a href="images/4.jpg" title="pic4">图片4</a>
			</li>
		</ul>
		<img id="placeholder" src="images/placeholder.jpg" alt="my image gallery"/>
	 </body>
	</html>

<h3 id="4.2">4.2 JavaScript</h3>

js文件-showPic.js

	function showPic(whichpic){
		var source = whichpic.getAttribute("href") ;
		var palceholder = document.getElementById("placehoder") ;
		placeholder.setAttribute("src",source) ;
	}

修改后标记文件-gallery.html

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title>Images Gallery</title>
	  <script type="text/javascript" src="js/showPic.js"></script>
	 </head>
	 <body>
		<h1>Images</h1>
		<ul>
			<li>
				<a href="images/1.jpg" title="pic1" onclick="showPic(this); return false ;">图片1</a>	return false ;表示点击这个链接没有被点击
			</li>
			<li>
				<a href="images/2.jpg" title="pic2" onclick="showPic(this); return false ;">图片2</a>
			</li>
			<li>
				<a href="images/3.jpg" title="pic3" onclick="showPic(this); return false ;">图片3</a>
			</li>
			<li>
				<a href="images/4.jpg" title="pic4" onclick="showPic(this); return false ;">图片4</a>
			</li>
		</ul>
		<img id="placeholder" src="images/placeholder.jpg" alt="my image gallery"/>
	 </body>
	</html>

>在给某个元素添加事件处理函数之后，一旦事件发生，相应的JavaScript代码就会得到执行，被调用的JavaScript代码可以返回一个值，这个值将会被传递给那个事件处理的函数。如果返回turn，则表示被点击了；如果返回false，表示没有被点击。

<h3 id="4.3">4.3 对这个函数进行扩展</h3>

<h4 id="4.3.1">4.3.1 childNodes属性</h4>

js代码：

	function countBodyChildren(){
		var body_element = document.getElementsByTagName("body")[0] ;
		alert(body_element.childNodes.length) ;
		alert(body_element.nodeType) ;
	}

<h4 id="4.3.2">4.3.2 nodeType属性</h4>

元素节点的nodeType属性值为1。

属性节点的nodeType属性值为2。	

文本节点的nodeType属性值为3。

<h4 id="4.3.3">4.3.3 在标记中增加一段描述</h4>

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title>Images Gallery</title>
	  <script type="text/javascript" src="js/showPic.js"></script>
	 </head>
	 <body>
		<h1>Images</h1>
		<ul>
			<li>
				<a href="images/1.jpg" title="pic1" onclick="showPic(this); return false ;">图片1</a>
			</li>
			<li>
				<a href="images/2.jpg" title="pic2" onclick="showPic(this); return false ;">图片2</a>
			</li>
			<li>
				<a href="images/3.jpg" title="pic3" onclick="showPic(this); return false ;">图片3</a>
			</li>
			<li>
				<a href="images/4.jpg" title="pic4" onclick="showPic(this); return false ;">图片4</a>
			</li>
		</ul>
		<img id="placeholder" src="images/placeholder.jpg" alt="my image gallery"/>
		<p id="description">Choose an image.</p>
	 </body>
	</html>

<h4 id="4.3.4">4.3.4 用JavaScript改变这段描述</h4>

js代码：

	function showPic(whichpic){
		var source = whichpic.getAttribute("href") ;
		var palceholder = document.getElementById("placehoder") ;
		placeholder.setAttribute("src",source) ;
		var text = whichpic.getAttribute("title") ;
		var description = document.getElementById("description") ;
		description.firstChild.nodeValue = text ;
	}
>这样就可以完成改变文本的效果了。

---

---

<h2 id="5">5 最佳实践</h2>

<h3 id="5.1">5.1 平稳退化和渐进增强</h3>

所谓的平稳退化(graceful degradation)是指：虽然某些功能无法使用，但是基本的操作仍然能够顺利完成。

所谓的渐进增强是指：用一些额外的信息层取包裹原始的数据。

>CSS负责提供表示的信息，JavaScript负责提供关于行为的信息。

<h3 id="5.2">5.2 分离JavaScript</h3>

1. 把文档中的所有链接放在一个数组中。
2. 遍历数组。
3. 如果某个链接的class属性等于popup，就表示这个链接在被点击时应该调用popUp()函数。

示例：

js文件-onclick.js

	window.onload = preparelinks ;
	function preparelinks(){
		var links = document.getElementsByTagName("a") ;
		for(var i=0;i<links.length;i++){
			if(links[i].getAttribute("class") == "popup"){
				links[i].onclick = function(){
					popUp(this.getAttribute("href")) ;
					return false ;
				}
			}
		}
	}
	function popUp(winURL){
		window.open(winURL,"popup","width=320,height=480") ;
	}

html文件

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script type="text/javascript" src="js/onclick.js"></script>
	 </head>
	 <body>
		<a href="http://fengmengzhao.github.io" class="popup">fmz blog</a>	实现JavaScript代码与html代码的完全分析，向CSS学习的
	 </body>
	</html>

<h3 id="5.3">5.3 向后兼容</h3>

加入语句：`if(! document.getElementsByTagName) return false ;`

示例，上面的js代码可以改为：

	window.onload function(){
		if(! document.getElementsByTagName) return false ;
		var links = document.getElementsByTagName("a") ;
		for(var i=0;i<links.length;i++){
			if(links[i].getAttribute("class") == "popup"){
				links[i].onclick = function(){
					popUp(this.getAttribute("href")) ;
					return false ;
				}
			}
		}
	}
	function popUp(winURL){
		window.open(winURL,"popup","width=320,height=480") ;
	}

>这样就能解决“古老的”浏览器不会因为js脚本代码而出现问题，使脚本语言具有良好的向后兼容性。

<h3 id="5.4">5.4 性能的考虑</h3>

<h4 id="5.4.1">5.4.1 尽量少访问DOM和尽量减少标记</h4>

下面的代码:

	if(document.getElementsByTagName("a").length > 0){
		var links = document.getElementsByTagName("a") ;
		for(var i=0;i<links.length;i++){
			//对每一个链接点处理
		}
	}

>对整个DOM树遍历了两次

可以优化为如下代码：

	var links = var links = document.getElementsByTagName("a") ;
	if(links.length > 0){
		for(var i=0;i<links.length;i++){
			//对每一个链接点处理
		}
	}

>对DOM树只遍历一次

>尽量减少文档中标记的数量，过多的元素只会增加DOM树的规模，进而增加遍历DOM树查找特定元素的时间。

<h4 id="5.4.2">5.4.2 合并放置脚本</h4>

尽量将脚本函数放置在一个脚本文件中。

建议将脚本标签放在文档的末尾，body标记之前。

<h4 id="5.4.3">5.4.3 压缩脚本</h4>

应有有两个js的版本，一个是工作副本，另外一个是精简版本，用于放在站点上

推荐有代表性的代码压缩工具

1. [Douglas Crockford 的JSMin](http://http://www.crockford.com/javascript/jsmin.html "JSMin")
2. [雅虎的 YUI Compressor](http://yui.github.io/yuicompressor/ "雅虎的 YUI Compressor")
3. [谷歌的 Closure Compiler](http://closure-compiler.appspot.com/home "谷歌的 Closure Compiler")

---

---

<h2 id="6">6 动态创建标记</h2>

把结构、行为和样式分开永远是一个好主意，只要有可能就应该用外部的CSS文件代替font标签去设定和管理网页的样式信息，最好用外部的JavaScript文件去控制网页的行为，而文档的结果是由标记完成构建的。

<h3 id="6.1">6.1 InnerHTML属性</h3>

示例：

js文件

	window.onload = function(){
		var testdiv = document.getElementById("testdiv") ;
		testdiv.innerHTML = "<p>This is <em>fmz</em> content.</p>"
	}

HTML文件

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script type="text/javascript" src="js/example.js"></script>
	 </head>
	 <body>
		<div id="testdiv">
			<p>This is <em>my</em> content.</p>
		</div>
	 </body>
	</html>

>能够完成对原有代码的替换。

<h3 id="6.2">6.2 DOM方法</h3>

<h4 id="6.2.1">6.2.1 createElement方法</h4>

示例

js文件

	window.onload = function(){
		var testdiv = document.getElementById("testdiv") ;
		var p = document.createElement("p") ;
		var em = document.createElement("em") ;
		var p_text1 = document.createTextNode("This is ") ;
		var em_text = document.createTextNode("my") ;
		var p_text2 = document.createTextNode(" content.") ;

		testdiv.appendChild(p) ;
		p.appendChild(p_text1) ;
		p.appendChild(em) ;
		em.appendChild(em_text) ;
		p.appendChild(p_text2) ;
	}

html文件

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script type="text/javascript" src="js/dom_example01.js"></script>
	 </head>
	 <body>
		<div id="testdiv">
		</div>
	 </body>
	</html>

<h3 id="6.3">6.3 Ajax</h3>

2005年，Adaptive Path公司的Jesse James Garrete发明了Ajax这个词，用于概括异步加载页面内容的技术。Ajax的主要优势是对页面的请求以异步的方式发送到服务器，而服务器不会以整个页面来回应请求，与此同时用户还能继续浏览页面并与页面进行交互。

<h4 id="6.3.1">6.3.1 XMLHttpRequest</h4>

Ajax的核心就是XMLHttpRequest对象，这个对象充当浏览器中的脚本(客户端)与服务器之间的中间人的角色。以往的请求都是由浏览器发出，而JavaScript可以通过这个对象自己发送请求，同时自己处理响应。

<h4 id="6.3.2">6.3.2 Hijax</h4>

Ajax主要依赖于后台服务器，实际上是服务端的脚本语言完成了绝大部分的工作。XMLHttpRequest作为浏览器与服务器之间的中间人，它只是负责传递请求和响应。如果中间人被禁止了，浏览器和服务器之间的请求和响应应该继续完成而不是中断，只不过花的时间可能会长一点。

js代码-addLoadEvent.js

	function addLoadEvent(func){
		var oldonload = window.onload ;
		if(typeof window.onload != 'function'){
			window.onload = func ;
		}else{
			window.onload = function(){
				oldonload() ;
				func() ;
			}
		}
	}

js代码-getHTTPObject.js

	function getHTTPObject(){
		if(typeof XMLHttpRequest == "undefined"){
			XMLHttpRequest = function(){
				try{ return new  ActiveXObject("Msxml2.XMLHTTP.6.0"); }
					catch(e){}
				try{ return new  ActiveXObject("Msxml2.XMLHTTP.3.0"); }
					catch(e){}
				try{ return new  ActiveXObject("Msxml2.XMLHTTP"); }
					catch(e){}
				return false ;
			}
		}
		return new XMLHttpRequest() ;
	}

js代码-getNewContent.js

	function getNewContent(){
		var request = getHTTPObject() ;
		if(request){
			request.open("GET","example.txt",true) ;
			request.onreadystatechange = function(){
				if(request.readyState == 4){
					alert("Response Received")
					var para = document.createElement("p") ;
					var txt = document.createTextNode(request.responseText) ;
					para.appendChild(txt) ;
					document.getElementById('new').appendChild(para) ;
				}
			};
			request.send(null)
		}else{
				alert("Sorry,your browser doesn't support XMLHttpRequest") ;
			}
	}
	addLoadEvent(getNewContent) ;

---

---

<h2 id="7">7 充实文档内容</h2>

JavaScript的应用准则

1. 渐进增强(progressive enhancement)。你应该总是从最核心的的部分，也就是从内容开始。更具内容用标记实现良好的结构；然后再逐步加强这些内容。这些增强的效果可以通过CSS改进呈现效果，也可以通过DOM添加各种行为。如果用DOM添加核心内容，那么未免太迟了，核心内容从一开始就考虑了。

2. 平稳退化。支持了渐进增强也就支持了平稳退化，在退化的过程中，核心的内容不会丢失。

>script引用：`<script src="*.js"></script>`，同行将代码放入/scripts文件夹中

>css引用：`<link rel="stylesheet" href="*.css" media="screen" />`，同行将代码放入/styles文件夹中。可以将文件导入basic.css文件中，语句：`@import url(*.css)`，例如：

	@import url(layout.css) ;
	@import url(color.css) ;
	@import url(typography.css) ;

---

---
