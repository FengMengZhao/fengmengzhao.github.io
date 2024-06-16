---
layout: post
title: JavaScript之锋利的JQuery
---

## 目录

- [1 认识JQuery](#1)
- [2 JQuery选择器](#2)
	- [2.1 常用的CSS(Cascading Style Sheet，层叠样式表)选择器](#2.1)
	- [2.2 JQuery选择器](#2.2)
	- [2.3 案例研究](#2.3)
- [3 JQuery中的DOM操作](#3)
- [4 JQuery中的事件和动画](#4)
- [5 JQuery对表单、表格的操作及更多的应用](#5)

---

<h2 id="1">1 认识JQuery</h2>

每一份DOM都可以表示成一棵树，JQuery对象就是通过JQuery包装DOM后产生的对象。

> JQuery对象无法使用DOM中的任何方法，DOM也不能使用JQuery中的方法。

将JQuery转化为DOM对象，方法1：

	var $cr = $("#cr") ;	//JQuery对象
	var cr = $cr[0] ;	//DOM对象
	alert(cr.checked);	//检查这个checkbox是否被选中

将JQuery转化为DOM对象，方法2：

	var $cr = $("#cr") ;
	var cr = $cr.get(0) ;
	alert(cr.checked);

DOM对象转化为JQuery对象

	var cr = document.getElementById("cr") ;	//DOM对象
	var $cr = $(cr) ;				//JQuery对象

> 平时使用的JQuery对象都是通过`$()`函数制造出来的，`$()`函数就是一个JQuery对象的制造工厂。

---

<h2 id="2">2 JQuery选择器</h2>

<h3 id="2.1">常用的CSS(Cascading Style Sheet，层叠样式表)选择器</h3>

1. 标签选择器，语法：`E{css规则}`，描述：以文档元素作为选择符，示例：`td{font-size: 14px;width: 120px}`
2. ID选择器，语法：`#ID{css规则}`，描述：以文档元素的唯一标识符ID作为选择器，示例：`#note{font-size: 14px;width: 120px}`
3. 类选择器，语法：`E.className{css规则}`，描述：以文档元素的class作为选择符，示例：`div.note{font-size: 14px;width: 120px}`
4. 群组选择器，语法：`E1,E2,E3{css规则}`，描述：多个元素应用同样样式的选择符，示例：`td,p,div.a{font-size: 14px;width: 120px}`
5. 后代选择器，语法：`E F{css规则}`，描述：元素E的任意后代元素F，示例：`#links a{font-size: 14px;width: 120px}`
6. 通配符选择器，语法：`*{css规则}`,描述：以文档中的所有元素作为选择符，示例：`*{font-size: 14px;width: 120px}`

JQuery选择器，示例：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p class="demo">点击我</p>
		<script>
			$(".demo").click(function(){
				alert("中华人民共和国！！！") ;
			})
		</script>
	 </body>
	</html>

<h3 id="2.2">JQuery选择器</h3>

1. 简洁的写法
2. 支持CSS1到css3选择器
3. 完善的处理机制

### JQuery基本选择器同css类似，加上`$("")`即可

### JQuery层次选择器

1. `$("ancestor descentant")`表示ancestor元素里的所有descendant元素
2. `$("parent>child")`表示parent元素下的child元素
3. `$('prev+next')`表示紧跟在prev元素下面的next元素
4. `$('prev~siblings')`表示prev元素之后的所有siblings元素

### 过滤选择器

#### 基本的过滤选择器

1. `:first`表示选取第一个元素；`:last`表示选取最后一个元素
2. `:not(selector)`去除所有与给定选择器匹配的元素
3. `:even`选取索引是偶数的所有元素，索引从0开始；`:odd`选取索引是奇数的所有元素，索引从0开始
4. `:eq(index)`选取索引等于index的元素，索引从0开始；`:gt(index)`选取索引大于index的元素，索引从0开始；`:lt(index)`选取索引小于index的元素，索引从0开始
5. `:header`选取所有的标题元素
6. `:animated`选取所有正在执行动画的所以元素

#### 内容过滤选择器

1. `:contains(text)`表示含有文本内容为text是元素
2. `:empty`表示不包含子元素或者文本的空元素
3. `:has(selector)`选取含有选择器所匹配的元素的元素
4. `:parent`表示选取含有子元素或者文本的元素

#### 可见过滤选择器

1. `:hidden`表示选取所有不可见元素
2. `visible`表示所有可见元素

#### 属性过滤选择器

1. `[attribute]`选取拥有此属性的元素
2. `[attribute=value]`选取属性值为value的元素
3. `[attribute != value]`选取属性值不等于value的元素
4. `[attribute ^= value]`选取属性值以value开始的元素
5. `[attribute $= value]`选取属性值以value结束的元素
6. `[attribute *= value]`选取属性值含有value的元素
7. `[selector1][selector2][selector3]`表示用属性选择器复合成一个复合的属性选择器，满足多个条件

#### 子元素过滤选择器

1. `:nth-child(index/even/odd/equation)`选取每一个父元素下的第index个子元素或者奇偶元素，示例：`:nth-child(even)`能选取每个父元素下索引值为偶数的元素。
2. `:first-child`表示选取每一个父元素的第一个子元素；`:last-child`表示选取每一个父元素的最后一个子元素；`:only-child`表示如果只含有唯一子元素将会匹配，否则不会匹配。

#### 表单对象属性过滤选择器

1. ':enable'表示选取所有可用元素
2. `:disable`表示选取所有不可用元素
3：`:checked`表示选取所有被选中元素(单选框，复选框)
4：`:selected`表示选取所有被选中的元素(下拉列表)

#### 表单选择器

1. `:input`表示选取所有的input textarea select button元素
2. `:text`选取所有的单行文本框
3. `:password`选取所有的密码框
4：`:radio`选取所有的单选框
5. `:checkbox`选取所有的复选框
6. `:submit`选取所有的提交按钮
7. `:image`选取所有的图像按钮
8. `:reset`选取所有的重置按钮
9. `:button`选取所有的按钮
10. `:file`选取所有的上传域
11. `:hidden`选取所有的不可见元素

用JQuery代码改进如下代码

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	 </head>
	 <body>
		<table id="tb">
			<tbody>
				<tr><td>第一行</td><td>第一行</td></tr>
				<tr><td>第二行</td><td>第二行</td></tr>
				<tr><td>第三行</td><td>第三行</td></tr>
				<tr><td>第四行</td><td>第四行</td></tr>
				<tr><td>第五行</td><td>第五行</td></tr>
				<tr><td>第六行</td><td>第六行</td></tr>
				<tr><td>第七行</td><td>第七行</td></tr>
				<tr><td>第八行</td><td>第八行</td></tr>
			</tbody>
		</table>
		<script>
			var table = document.getElementById("tb") ;
			var tbody = table.getElementsByTagName("tbody")[0] ;
			var trs = tbody.getElementsByTagName("tr") ;
			for(var i=0;i<trs.length;i++){
				if(i % 2 == 0){
					trs[i].style.backgroundColor = "gray" ;
				}
			}
		</script>
	 </body>
	</html>

>DOM代码

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<table id="tb">
			<tbody>
				<tr><td>第一行</td><td>第一行</td></tr>
				<tr><td>第二行</td><td>第二行</td></tr>
				<tr><td>第三行</td><td>第三行</td></tr>
				<tr><td>第四行</td><td>第四行</td></tr>
				<tr><td>第五行</td><td>第五行</td></tr>
				<tr><td>第六行</td><td>第六行</td></tr>
				<tr><td>第七行</td><td>第七行</td></tr>
				<tr><td>第八行</td><td>第八行</td></tr>
			</tbody>
		</table>
		<script>
			$("#tb tbody tr:even").css("backgroundColor","red") ;
		</script>
	 </body>
	</html>

>jquery代码

>jquery语法一些特殊字符，如 . # ( ]等需要用转义字符：`\\`

<h3 id="2.3">2.3 案例研究</h3>

案例代码

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<div class="subCategoryBox">
			<ul>
				<li><a href="#">佳能</a></li>
				<li><a href="#">索尼</a></li>
				<li><a href="#">三星</a></li>
				<li><a href="#">尼康</a></li>
				<li><a href="#">松下</a></li>
				<li><a href="#">卡西欧</a></li>
				<li><a href="#">富士</a></li>
				<li><a href="#">柯达</a></li>
				<li><a href="#">宾得</a></li>
				<li><a href="#">理光</a></li>
				<li><a href="#">奥林巴斯</a></li>
				<li><a href="#">明基</a></li>
				<li><a href="#">爱国者</a></li>
				<li><a href="#">其他相机品牌</a></li>
			</ul>
			<div class="showmore">
				<a href="more.html"><span>显示全部品牌</span></a>
			</div>
		</div>
		<script>
			$(function(){
				var $category = $('ul li:gt(5):not(:last)') ;
				$category.hide() ;
				var $toggleBtn = $('div.showmore > a') ;
				$toggleBtn.click(function(){
					if($category.is(":visible")){
						$category.hide() ;
						$('div.showmore a span')
							.text("显示全部品牌") ;
					}else{
						$category.show() ;
						$('div.showmore a span')
						.text("精简显示品牌") ;
					}
					return false ;
				})
			})
		</script>
	 </body>
	</html>

>代码中用到的几个JQuery方法的意思如下：show()：显示隐藏的匹配信息；css(name,value)：给元素设置样式；text(string)：设置所有匹配元素的文本内容；filter(expr)：筛选出与指定表达式匹配的元素集合，其中expr可以是多个选择器集合；addClass(class)：为匹配元素增加指定的类名。

---

---

<h2 id="3">3 JQuery中的DOM操作</h2>

DOM是一种与浏览器、平台、语言无关的接口，使用该接口可以轻松的访问页面中的所有标准组件。简单说就是解决了Netscape公司的JavaScript和Microsoft公司的JScript之间从冲突，位于web设计师和开发者一套标准的方法，让他们能够轻松的获取和错做网页中的数据、脚本和表现层对象。

1. DOM Core并不专属于JavaScript，任何一种支持DOM的程序设计语言都可以使用它，可以处理任何一种使用标记语言编写出来的文档，例如：XML
2. HTML_DOM提供了一些更加简明的记号来描述各种HTML元素的属性，如：document.forms提供了一个forms对象，element.src获取某元素的src属性。HTML_DOM只能用来处理web文档
3. CSS_DOM针对CSS的操作，主要作用是获取和设置style对象的各种属性。通过Style对象的各种属性，可以使网页呈现出各种不同的效果，例如：element.style.colr="red"

>JQuery作为JavaScript库，继承并且发扬了JavaScript对DOM对象的操作的特征。

查找节点

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			var $li = $("ul li:eq(0)") ;
			var li_text = $li.text() ;
			alert(li_text) ;
			var $para = $("p") ;
			var para_text = $para.attr("title") ;
			alert(para_text) ;
		</script>
	 </body>
	</html>

创建节点

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			var $li_1 = $("<li title='香蕉'>香蕉</li>")
			var $li_2 = $("<li title='雪梨'>雪梨</li>")
			$("ul").append($li_1) ;
			$("ul").append($li_2) ;
		</script>
	 </body>
	</html>

插入节点，有如下方法：append()：向每个匹配元素总追加内容；appendTo()：将所有匹配的元素追加到指定的元素中；prepend()：向每个匹配的元素内部前置内容；prependTo()： 将所有匹配的元素前置到指定的元素中；after()：在每个匹配度元素之后插入内容；insertAfter()：将所有匹配的元素插入到指定元素的后面；before()：在每个匹配的元素之前插入内容；insertBefore()：将所有的匹配元素插入到指定的元素前面，示例代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			var $li_1 = $("<li title='香蕉'>香蕉</li>")
			var $li_2 = $("<li title='雪梨'>雪梨</li>")
			var $li_3 = $("<li title='其他'>其他</li>")
			var $parent = $("ul") ;
			var $two_li = $("ul li:eq(1)") ;
			$("ul").append($li_1) ;
			$("ul").append($li_2) ;
			$parent.append($li_1) ;
			$parent.append($li_2) ;
			$li_3.insertAfter($two_li) ;
			var $one_li = $("ul li:eq(1)") ;
			var $two_li = $("ul li:eq(2)") ;
			$two_li.insertBefore($one_li) ;
			$("ul li:eq(1)").remove() ;
			$("ul li:eq(1)").empty() ;
		</script>
	 </body>
	</html>

> empty()方法，并不是删除节点，而是清空节点，它能够清空元素中所有后代的节点

复制节点

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			$("ul li").click(function(){
				$(this).clone(true).appendTo("ul") ;
			})
		</script>
	 </body>
	</html>

> 复制节点后，被复制的新元素不具有任何行为，如果需要新的元素也具有复制功能，要传递参数true：`$(this).clone(true).appendTo("ul") ;`，不传递参数生成的节点副本不具有复制功能。

替换节点

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			$("p").replaceWith("<strong>你最不喜欢的水果是？</strong>") ;
		</script>
	 </body>
	</html>

包裹节点，方法是：wrap()；包裹全部的节点：wrapAll()；包裹元素的子内容：wrapInner()，示例：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			$("p").wrapAll("<b></b>") ;
			$("p").wrapInner("<i></i>") ;
		</script>
	 </body>
	</html>

属性操作：`$("p").attr("title")`表示获取元素节点属性；`$("p").attr("title","your title")`表示设置设置p元素的title属性值；删除属性：`$("p").remoteAttr("title")`表示删除p元素的title属性

样式操作：获取和设置样式：`$("p").attr("class","your new class"`；追加样式：`$("p").addClass("class")`，此时p元素会出现两个class；移除样式：`$("p").removeClass("class")`；切换样式：`toggleBtn.toggle(function(){显示元素},funciton(){隐藏元素})`，还有另外一宗方法：`$("p").toggleClass("another")`，当不断点击切换样式时，会不停的重复切换；判断是否含有某个样式：`$("p").hasClass("another")`，实际上是调用`$("p").is(".another")`，只是为了增加代码的可读性。

设置和获取HTML、文本和值

**1. html()方法，相当于JavaScript中的innerHTML属性，可以用来读取和设置某个元素中的HTML内容。**

**2. text()方法，相当于JavaScript中的innerText属性，可以用来读取和设置某个元素的文本内容。示例代码**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			$("p").html("<strong>中户人民共和国！！！</strong>") ;
			alert($("ul").html()) ;
			alert($("ul").text()) ;
			$("ul li").text("美味") ;
		</script>
	 </body>
	</html>

**3. val()方法，相当于JavaScript中的value属性，可以用来设置或者获取元素的值，示例代码**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<input type="text" id="address" value="请输入邮箱地址"/><br>
		<input type="password" id="passwd" value="请输入邮箱密码"/><br>
		<input type="button" value="登录"/>
		<script>
			$(function(){
				$("#address").focus(function(){//处理获得焦点时的事件
					var text_val = $(this).val() ;
					//if(text_val == "请输入邮箱地址"){
					if(text_val == this.defaultValue){//此处可以用defaultValue来替代
						$(this).val("") ;
					}
				});
				$("#address").blur(function(){//处理失去焦点时的事件
					var text_val = $(this).val() ;
					if(text_val == ""){
						//$(this).val("请输入邮箱地址") ;
						$(this).val(this.defaultValue) ;//此处可以用defaultValue来替代
					}
				})
				$("#passwd").focus(function(){//处理获得焦点时的事件
					var text_val = $(this).val() ;
					if(text_val == "请输入邮箱密码"){
						$(this).val("") ;
					}
				});
				$("#passwd").blur(function(){//处理失去焦点时的事件
					var text_val = $(this).val() ;
					if(text_val == ""){
						$(this).val("请输入邮箱密码") ;
					}
				})
			})
		</script>
	 </body>
	</html>

> this指向当前文本框，this.defaultValue表示当前文本框的默认值。

> jquery中的focus()和blur()方法相当于JavaScript中的onfocus()和onblur()方法，分别表示处理获得焦点和失去焦点时的事件。

> 对下拉列表和单选复选的选择，select：`$("single").val("选择2号")`或者以数组的形式赋值：`$("multiple").val(["选择2号","选择三号"])`；redio：`$(":radio").val(["radio2"])`；checkbox以数组的形式：`$(":checkbox").val(["check2","check3"])`

遍历节点

1. children()方法，用于取得匹配元素的子元素的集合，注意：此方法只考虑子元素而不考虑任何后代元素
2. next()方法，用于取得匹配元素后面紧邻的同辈元素；prev()方法，用于取得匹配元素前面紧邻的同辈元素
3. siblings()方法，用于取得匹配元素前后所有的同辈元素
4. closest()方法，用于取得最近的匹配元素，首先查看当前元素是否匹配，如果匹配返回元素本身，如果不匹配向上查找父元素，直到找到匹配选择器的元素，如果什么也没找到返回一个空的JQuery对象，示例代码

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<p title="选择你喜欢吃的水果">你最喜欢吃的水果是？</p>
		<ul>
			<li title="苹果">苹果</li>
			<li title="橘子">橘子</li>
			<li title="菠萝">菠萝</li>
		</ul>
		<script>
			$(document).bind("click",function(e){
				$(e.target).closest("li").css("color","red") ;
			})
		</script>
	 </body>
	</html>

CSS-DOM操作

css-dom操作简单来说就是读取和设置style对象的各种属性

**1. offset()方法，获取元素在当前视窗中的相对偏移，返回对象包括两个属性，即top和left，它只对可见元素有效，示例代码**

	var offset = $("p").offset() ;
	var left = offset.left ;
	var top = offset.right ;

**2. position()方法，获取元素相对于最近的一个position样式属性设置为relative或者absolute的祖父节点的相对偏移。返回对象包括两个属性，left和top,示例代码：**

	var position = $("p").position() ;
	var left = position.left ;
	var top = position.right ;

**3. scrollTop()方法和scrollLeft()方法，分别获取滚动条距离顶端的距离和距离左侧的距离**

案例1

超链接提示效果，代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				var x = 100 ;
				var y = 20 ;
				$("a.tooltip").mouseover(function(e){
					this.myTitle = this.title ;
					this.title = "" ;
					var tooltip = "<div id='tooltip'>" + this.myTitle + "</div>" ;
					$("body").append(tooltip) ;
					$("#tooltip").css({
							"top": (e.pageY+y) + "px" ,
							"left": (e.pageX+x) + "px"
					}).show("fast") ;
				}).mouseout(function(){
					this.title = this.myTitle ;
					$("#tooltip").remove() ;
				}).mousemove(function(e){
					$("#tooltip").css({
							'top': (e.pageY+y) + "px" ,
							'left': (e.pageX+x) + "px"
					})
				})
			})
		</script>
		<style>
			#tooltip {
				position: relative ;
			}
		</style>
	 </head>
	 <body>
		<p><a href="#" class="tooltip" title="这是我的超链接提示1.">提示1</a></p>
		<p><a href="#" class="tooltip" title="这是我的超链接提示2.">提示2</a></p>
	 </body>
	</html>

> 还没有解决div会动的问题

案例2-为图片超链接添加效果，代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				var x = 1 ;
				var y = 2 ;
				$("a.tooltip").mouseover(function(e){
					this.myTitle = this.title ;
					this.title = "" ;
					var imageTitle = this.myTitle? "</br>" +this.myTitle : "" ;
					var tooltip = "<div id='tooltip'><image src='"+this.href+"'/>"+imageTitle+"</div>" ;
					$("body").append(tooltip) ;
					$("#tooltip").css({
							"top": (e.pageY+y) + "px" ,
							"left": (e.pageX+x) + "px"
					}).show("fast") ;
				}).mouseout(function(){
					this.title = this.myTitle ;
					$("#tooltip").remove() ;
				}).mousemove(function(e){
					$("#tooltip").css({
							'top': (e.pageY+y) + "px" ,
							'left': (e.pageX+x) + "px"
					})
				})
			})
		</script>
		<style>
			ul li {
				list-style: none ;
				float: left ;
			}
			#tooltip {
				position: relative ;
			}
		</style>
	 </head>
	 <body>
		<ul>
			<li ><a href="images/1.jpg" class="tooltip" title="1_samll"><img src="images/1_samll.jpg"/></a></li>
			<li ><a href="images/2.jpg" class="tooltip" title="2_small"><img src="images/2_small.jpg"/></a></li>
			<li ><a href="images/3.jpg" class="tooltip" title="3_samll"><img src="images/3_samll.jpg"/></a></li>
			<li ><a href="images/4.jpg" class="tooltip" title="4_samll"><img src="images/4_samll.jpg"/></a></li>
		</ul>
	 </body>
	</html>

---

---

<h2 id="4">4 JQuery中的事件和动画</h2>

JQuery中的事件

**1. 加载DOM，JQuery中有方法：`$(document).ready()`，此方法与JavaScript中的方法：`window.onloads`是由区别的。区别一：执行时机，JQuery中的方法是在DOM完全就绪时就可以被调用，而JavaScript中的方法是在网页中的所有元素包括元素的关联文件全部加载到浏览器中之后才执行；如果需要JQuery中的方法在全部元素加载完毕之后再执行需要用函数：`$(window).onload(function(){//编写代码})`；区别二：JQuery中的方法能够多次使用，而JavaScript中的方法会被后面的一个覆盖掉。区别三：JQuery中的方法代码可以简写为：`$(function(){//编写代码})`或者`$().read(function(){//编写代码})`**

**2. 事件绑定，使用bind()方法对匹配的元素进行特定事件的绑定，调用格式为：`bind(type,[,date],fn);`，第一个参数是事件类型，第二个参数是可选参数，第三个参数是用来绑定的处理函数。**

基本效果，加强效果，示例代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				$("#panel h5.head").bind("click",function(){
					var $content = $(this).next("div.content") ;
					if($(this).next("div.content").is(":visible")){
						$content.hide() ;
					} else{
						$content.show() ;
					}
				})
			})
		</script>
	 </head>
	 <body>
		<div id="panel">
			<h5 class="head">什么是JQuery</h5>
			<div class="content" style="display:none">
				&nbsp;&nbsp;&nbsp;&nbsp;jquery是继prototype之后又一个优秀的JavaScript库，它是一个由John Resing创建于2006年1月的开源项目。JQuery凭借简洁的语法和跨平台的兼容性，极大的简化了JavaScript开发人员遍历html文档、操作DOM、处理事件、执行动画和开发AJAx。它独特而又优雅的代码风格改变了JavaScript程序员的设计思路和编写程序的方式。
			</div>
		</div>
	 </body>
	</html>

>this引用的是携带相应行为的DOM元素

**3. 改变绑定事件的类型**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				$("#panel h5.head").bind("mouseover",function(){
					var $content = $(this).next("div.content") ;
						$content.show() ;
				})
				$("#panel h5.head").bind("mouseout",function(){
					var $content = $(this).next("div.content") ;
						$content.hide() ;
				})
			})
		</script>
	 </head>
	 <body>
		<div id="panel">
			<h5 class="head">什么是JQuery</h5>
			<div class="content" style="display:none">
				&nbsp;&nbsp;&nbsp;&nbsp;jquery是继prototype之后又一个优秀的JavaScript库，它是一个由John Resing创建于2006年1月的开源项目。JQuery凭借简洁的语法和跨平台的兼容性，极大的简化了JavaScript开发人员遍历html文档、操作DOM、处理事件、执行动画和开发AJAx。它独特而又优雅的代码风格改变了JavaScript程序员的设计思路和编写程序的方式。
			</div>
		</div>
	 </body>
	</html>

**4. 简写绑定事件，代码：**

	$("#panel h5.head").mouseover(function(){
		var $content = $(this).next("div.content") ;
		$content.show() ;
	})

>绑定事件的简写形式

合成事件

**1. hover()，示例代码：**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				$("#panel h5.head").hover(function(){
						$(this).next("div.content").show() ;
					},function(){
						$(this).next("div.content").hide() ;
					})
			})
		</script>
	 </head>
	 <body>
		<div id="panel">
			<h5 class="head">什么是JQuery</h5>
			<div class="content" style="display:none">
				&nbsp;&nbsp;&nbsp;&nbsp;jquery是继prototype之后又一个优秀的JavaScript库，它是一个由John Resing创建于2006年1月的开源项目。JQuery凭借简洁的语法和跨平台的兼容性，极大的简化了JavaScript开发人员遍历html文档、操作DOM、处理事件、执行动画和开发AJAx。它独特而又优雅的代码风格改变了JavaScript程序员的设计思路和编写程序的方式。
			</div>
		</div>
	 </body>
	</html>

>hover()方法准确的说是替代JQuery中的mouseenter和mouseleave方法。

**2. toggle()方法，此方法模拟的是鼠标连续单击事件，第一次单击是触发第一个函数，当再次点击同一个元素是触发第二个函数，如果有更多的元素则依次触发，直到最后一个，随后重复。代码示例：**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				$("#panel h5.head").toggle(function(){
						$(this).next("div.content").show() ;
					},function(){
						$(this).next("div.content").hide() ;
					})
			})
		</script>
	 </head>
	 <body>
		<div id="panel">
			<h5 class="head">什么是JQuery?</h5>
			<div class="content" style="display:none">
				&nbsp;&nbsp;&nbsp;&nbsp;jquery是继prototype之后又一个优秀的JavaScript库，它是一个由John Resing创建于2006年1月的开源项目。JQuery凭借简洁的语法和跨平台的兼容性，极大的简化了JavaScript开发人员遍历html文档、操作DOM、处理事件、执行动画和开发AJAx。它独特而又优雅的代码风格改变了JavaScript程序员的设计思路和编写程序的方式。
			</div>
		</div>
	 </body>
	</html>

>为什么此代码连标题都隐藏了，没有解决。

**3. 再次加强效果**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				$("#panel h5.head").toggle(function(){
						$(this).addClass("hightlight") ;
						$(this).next("div.content").show() ;
					},function(){
						$(this).removeClass("hightlight") ;
						$(this).next("div.content").hide() ;
					})
			})
		</script>
	 </head>
	 <body>
		<div id="panel">
			<h5 class="head">什么是JQuery?</h5>
			<div class="content" style="display:none">
				&nbsp;&nbsp;&nbsp;&nbsp;jquery是继prototype之后又一个优秀的JavaScript库，它是一个由John Resing创建于2006年1月的开源项目。JQuery凭借简洁的语法和跨平台的兼容性，极大的简化了JavaScript开发人员遍历html文档、操作DOM、处理事件、执行动画和开发AJAx。它独特而又优雅的代码风格改变了JavaScript程序员的设计思路和编写程序的方式。
			</div>
		</div>
	 </body>
	</html>

事件冒泡，当多个元素响应同一个事件的时候，并且这些元素之间存在嵌套的关系，点击内部的元素会触发外部元素的事件，这样想冒泡一样沿着DOM树直到顶端，称之为事件冒泡。示例代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				$("span").bind("click",function(){
					var txt = $('#msg').html() + "<p>内层span元素被单击.</p>"
					$("#msg").html(txt) ;
				})
				$("#content").bind("click",function(){
					var txt = $('#msg').html() + "<p>外层div元素被单击.</p>"
					$("#msg").html(txt) ;
				})
				$("body").bind("click",function(){
					var txt = $('#msg').html() + "<p>body元素被单击.</p>"
					$("#msg").html(txt) ;
				})
			})
		</script>
	 </head>
	 <body>
		<div id="content">
			外层div元素
			<span>内层span元素</span>
			外层div元素
		</div>
		<div id="msg"></div>
	 </body>
	</html>

解决冒泡事件，JQuery中取得事件对象，示例代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <script>
			$(function(){
				$("span").bind("click",function(event){
					var txt = $('#msg').html() + "<p>内层span元素被单击.</p>"
					$("#msg").html(txt) ;
					event.stopPropagation() ;
				})
				$("#content").bind("click",function(){
					var txt = $('#msg').html() + "<p>外层div元素被单击.</p>"
					$("#msg").html(txt) ;
				})
				$("body").bind("click",function(){
					var txt = $('#msg').html() + "<p>body元素被单击.</p>"
					$("#msg").html(txt) ;
				})
			})
		</script>
	 </head>
	 <body>
		<div id="content">
			外层div元素
			<span>内层span元素</span>
			外层div元素
		</div>
		<div id="msg"></div>
	 </body>
	</html>

阻止默认行为，例如在验证表单时，某个元素不够6位，不符合提交要求时，要组织表单的的提交(默认行为)，用event.preventDefault() ;示例代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form action="test.html" method="post">
			用户名：<input type="text" id="username"/><span id="msg"></span><br>
			<input type="submit" value="提交" id="sub"/><br>
		</form>
		 <script>
			$(function(){
				$("#sub").bind("click",function(event){
					var username = $("#username").val() ;
					if(username == ""){
						$("#msg").html("<span style='color:red'>用户名内容不能为空！</span>") ;
						event.preventDefault() ;
					}
				})
			})
		</script>
	 </body>
	</html>

> 阻止事件冒泡或者阻止默认行为，都可以用`return false ;`代替JQuery语句。

事件捕获，事件捕获和事件冒泡刚好相反，事件捕获是从最顶端往下开始触发，如果需要使用事件捕获，请使用原生态的JavaScript。

事件对象(event)的属性：even.type() ;event.preventDefault() ;event.stopPropagation() ;event.target()获取触发事件的元素...

移除事件：`$("#btn").unbind("click")`；unbind的语法结构：`unbind([type],[ ,data)`，说明：如果没有参数则删除所有的绑定事件，如果提供了事件类型，只删除该类型的绑定事件；如果第二个参数是处理函数，则之后此函数的事件被删除。

模拟操作：

**1. 常用模拟，以上触发事件必须通过单击才能触发，有时需要模拟用户操作，来达到单击的效果。例如用户输入银行卡六位密码时自动提交表单。**

在JQuery中，可以通过trigger()方法完成模拟操作，完成的操作时当用户进入界面后就触发事件，代码：`$("#sub").trigger("click")`或者简写为：`$("#sub").click()`

**2. 触发自定义事件**

	$("#btn").bind("myClick",function(){
		$('#text').append("<p>我的自定义事件</p>") ;
	})
	$("#btn").trigger("myClick") ;

**3. 传递数据**

**4. 执行默认操作，`$("input").trigger("focus")`会触发input元素绑定的focus事件，同时也会使input元素本事得到焦点；如果只想触发事件不想得到焦点：`$("input").triggerHandler("focus")`**

>所谓的获得焦点讲的是将鼠标选中。

### JQuery中的动画

1. show()和hide()方法是JQuery中最基本的动画方法，产生动画效果需要触底参数：`show("slow")`或者`show(1000)`在一秒内隐藏或者展示
2. fadein()和fadeOut()方法，只会改变元素的不透明度。
3. slideUp()和slideDown()方法，该方法只会改变元素的高度。

>jquery中的任何动画效果都可以指定参数，slow、normal、fast分别是0.6、0.4、0.2秒，也可以指定具体的时间，时间参数以秒为单位，不加引号。

自定义动画方法animate()

animate()方法的语法结构是：`animate(params,speed,callback)`，参数说明：params包含样式属性及值的映射；speed速度，可选；callback完成动画时执行的函数

简单动画：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		#panel {
			position: relative ;
			width: 100px ;
			height: 100px ;
			border: 1px solid #0050D0 ;
			background: #96E555 ;
			cursor: pointer ;
		}
	  </style>
	 </head>
	 <body>
		<div id="panel"></div>
		 <script>
			$(function(){
				$("#panel").click(function(){
					$(this).animate({left: "500px"},3000) ;
				})
			})
		</script>
	 </body>
	</html>

累加累减动画

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		#panel {
			position: relative ;
			width: 100px ;
			height: 100px ;
			border: 1px solid #0050D0 ;
			background: #96E555 ;
			cursor: pointer ;
		}
	  </style>
	 </head>
	 <body>
		<div id="panel"></div>
		 <script>
			$(function(){
				$("#panel").click(function(){
					$(this).animate({left: "+=500px"},3000) ;
				})
			})
		</script>
	 </body>
	</html>

多重动画-同时执行动画

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		#panel {
			position: relative ;
			width: 100px ;
			height: 100px ;
			border: 1px solid #0050D0 ;
			background: #96E555 ;
			cursor: pointer ;
		}
	  </style>
	 </head>
	 <body>
		<div id="panel"></div>
		 <script>
			$(function(){
				$("#panel").click(function(){
					$(this).animate({left: "500px",height: "200px"},3000) ;
				})
			})
		</script>
	 </body>
	</html>

多重动画顺序执行

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		#panel {
			position: relative ;
			width: 100px ;
			height: 100px ;
			border: 1px solid #0050D0 ;
			background: #96E555 ;
			cursor: pointer ;
		}
	  </style>
	 </head>
	 <body>
		<div id="panel"></div>
		 <script>
			$(function(){
				$("#panel").click(function(){
					$(this).animate({left: "500px"},3000)
					       .animate({height: "200px"},3000) ;
				})
			})
		</script>
	 </body>
	</html>

综合动画

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		#panel {
			position: relative ;
			width: 100px ;
			height: 100px ;
			border: 1px solid #0050D0 ;
			background: #96E555 ;
			cursor: pointer ;
			opacity: 0.5 ;
		}
	  </style>
	 </head>
	 <body>
		<div id="panel"></div>
		 <script>
			$(function(){
				$("#panel").click(function(){
					$(this).animate({left: "500px",height: "200px",opacity: "1"},3000)
						.animate({top: "200px",width: "200px"},3000)
						.fadeOut("slow") ;
				})
			})
		</script>
	 </body>
	</html>

动画回掉函数

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		#panel {
			position: relative ;
			width: 100px ;
			height: 100px ;
			border: 1px solid #0050D0 ;
			background: #96E555 ;
			cursor: pointer ;
			opacity: 0.5 ;
		}
	  </style>
	 </head>
	 <body>
		<div id="panel"></div>
		 <script>
			$(function(){
				$("#panel").click(function(){
					$(this).animate({left: "500px",height: "200px",opacity: "1"},3000)
						.animate({top: "200px",width: "200px"},3000,function(){
							$(this).css("border","5px,solid,blue") ;
						}) ;
				})
			})
		</script>
	 </body>
	</html>

>回调函数适合于JQuery所有动画效果，例如：`$("#element").slideDown("normal",function(){//执行完动画后的效果})`

简单的实现动态图片切换效果示例：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title>Images Gallery</title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		div img {
			display: none ;
			position: absolute ;
			margin: 100px 600px ;
		}
	  </style>
	 </head>
	 <body>
		<div class="fader">
			<img src="images/1_samll.jpg"/>
			<img src="images/2_small.jpg"/>
			<img src="images/3_samll.jpg"/>
			<img src="images/4_samll.jpg"/>
		</div>
		<script>
		$(function(){
			var $imgs = $(".fader").find("img") ;
			i = 0 ;
			function changeImage(){
				var next = (++i % $imgs.length) ;
				$($imgs.get(next -1)).fadeOut(500) ;
				$($imgs.get(next)).fadeIn(500) ;
			}
			setInterval(changeImage,2000) ;
		})
		</script>
	 </body>
	</html>

>能够实现图片的幻灯片播放。

---

---

<h2 id="5">5 JQuery对表单、表格的操作及更多的应用</h2>

### 文本应用

**1. 单行文本框的应用：**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		input:focus ,textarea:focus{
			border: 1px solid red ;
			background: #fcc ;
		}
	  </style>
	 </head>
	 <body>
		<form action="#" method="post" id="regForm">
			<fieldset>
				<legend>个人基本信息</legend>
				<div>
					<label for="username">名称：</label>
					<input id="username" type="text"/>
				</div>
				<div>
					<label for="pass">密码：</label>
					<input id="pass" type="password"/>
				</div>
				<div>
					<label for="msg">名称：</label>
					<textarea id="msg"></textarea>
				</div>
			</fieldset>
		</form>
	 </body>
	</html>

> 当文本框获得焦点时，提供一个样式，失去焦点时，失去样式。

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.focus{
			border: 1px solid red ;
			background: #fcc ;
		}
	  </style>
	 </head>
	 <body>
		<form action="#" method="post" id="regForm">
			<fieldset>
				<legend>个人基本信息</legend>
				<div>
					<label for="username">名称：</label>
					<input id="username" type="text"/>
				</div>
				<div>
					<label for="pass">密码：</label>
					<input id="pass" type="password"/>
				</div>
				<div>
					<label for="msg">名称：</label>
					<textarea id="msg"></textarea>
				</div>
			</fieldset>
		</form>
		<script>
			$(function(){
				$(":input").focus(function(){
					$(this).addClass("focus") ;
				}).blur(function(){
					$(this).removeClass("focus") ;
				})
			})
		</script>
	 </body>
	</html>

>对于一些浏览器不兼容的情况，上述代码利用JQuery能提供同样的效果。

**2. 多行文本框的应用，评论框的放大缩小**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form>
			<div class="msg">
				<div class="msg_caption">
					<span class="bigger">放大</span>
					<span class="smaller">缩小</span>
				</div>
				<div>
					<textarea id="comment" rows="8" cols="20"></textarea>
				</div>
			</div>
		</form>
		<script>
			$(function(){
				$comment = $("#comment") ;
				$(".bigger").click(function(){
					if($comment.height() < 500){
						$comment.height($comment.height() + 50) ;
					}
				});
				$(".smaller").click(function(){
					if($comment.height() > 500){
						$comment.height($comment.height() - 50) ;
					}
				});
			})
		</script>
	 </body>
	</html>

增加动画效果设置评论框的放大缩小

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form>
			<div class="msg">
				<div class="msg_caption">
					<span class="bigger">放大</span>
					<span class="smaller">缩小</span>
				</div>
				<div>
					<textarea id="comment" rows="8" cols="20"></textarea>
				</div>
			</div>
		</form>
		<script>
			$(function(){
				$comment = $("#comment") ;
				$(".bigger").click(function(){
					if($comment.height() < 500){
						$comment.animate({height: "+=50"},400) ;
					}
				});
				$(".smaller").click(function(){
					if($comment.height() > 500){
						$comment.animate({height: "-=50"},400) ;
					}
				});
			})
		</script>
	 </body>
	</html>

>此效果会使评论框有一定的放大缩小时的缓冲效果。

控制滚动条的变化

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form>
			<div class="msg">
				<div class="msg_caption">
					<span class="bigger">放大</span>
					<span class="smaller">缩小</span>
				</div>
				<div>
					<textarea id="comment" rows="8" cols="20"></textarea>
				</div>
			</div>
		</form>
		<script>
			$(function(){
				$comment = $("#comment") ;
				$(".up").click(function(){
					if(!$comment.is(":animated")){
						$comment.animate({scrollTop: "-=50"},400) ;
					}
				});
				$(".down").click(function(){
					if(!$comment.is(":animate")){
						$comment.animate({scrollTop: "+=50"},400) ;
					}
				});
			})
		</script>
	 </body>
	</html>

复选框的应用，示例代码：

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form>
			你的爱好是什么？<br>
			<input type="checkbox" name="items" value="足球"/>足球
			<input type="checkbox" name="items" value="篮球"/>篮球
			<input type="checkbox" name="items" value="羽毛球"/>羽毛球
			<input type="checkbox" name="items" value="乒乓球"/>乒乓球
			<input type="checkbox" name="items" value="排球"/>排球
			<input type="checkbox" name="items" value="桌球"/>桌球<br>
			<input type="button" id="checkedAll" value="全选"/>
			<input type="button" id="checkedNo" value="全不选"/>
			<input type="button" id="checkedRev" value="反选"/>
			<input type="button" id="send" value="提交"/>
		</form>
		<script>
			$(function(){
				$("#checkedAll").click(function(){
						$("[name=items]:checkbox").attr("checked",true) ;
				})
				$("#checkedNo").click(function(){
					$("[name=items]:checkbox").attr("checked",false) ;
				})
				$("#checkedRev").click(function(){
					$("[name=items]:checkbox").each(function(){
						this.checked = !this.checked ;
					})
				})
				$("#send").click(function(){
					var str = "您选中的是：\r\n"
					$("[name=items]:checkbox:checked").each(function(){
						str += $(this).val()+"\r\n"
					})
					alert(str) ;
				})
			})
		</script>
	 </body>
	</html>

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form>
			你的爱好是什么？<input type="checkbox" id="checkedAll" value="全选/全不选"/>全选/全不选<br>
			<input type="checkbox" name="items" value="足球"/>足球
			<input type="checkbox" name="items" value="篮球"/>篮球
			<input type="checkbox" name="items" value="羽毛球"/>羽毛球
			<input type="checkbox" name="items" value="乒乓球"/>乒乓球
			<input type="checkbox" name="items" value="排球"/>排球
			<input type="checkbox" name="items" value="桌球"/>桌球<br>
			<input type="button" id="checkedRev" value="反选"/>
			<input type="button" id="send" value="提交"/>
		</form>
		<script>
			$(function(){
				$("#checkedAll").click(function(){
						$("[name=items]:checkbox").attr("checked",this.checked) ;
				})
				$("#checkedRev").click(function(){
					$("[name=items]:checkbox").each(function(){
						this.checked = !this.checked ;
					})
				})
				$("#send").click(function(){
					var str = "您选中的是：\r\n"
					$("[name=items]:checkbox:checked").each(function(){
						str += $(this).val()+"\r\n"
					})
					alert(str) ;
				})
			})
		</script>
	 </body>
	</html>

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form>
			你的爱好是什么？<input type="checkbox" id="checkedAll" value="全选/全不选"/>全选/全不选<br>
			<input type="checkbox" name="items" value="足球"/>足球
			<input type="checkbox" name="items" value="篮球"/>篮球
			<input type="checkbox" name="items" value="羽毛球"/>羽毛球
			<input type="checkbox" name="items" value="乒乓球"/>乒乓球
			<input type="checkbox" name="items" value="排球"/>排球
			<input type="checkbox" name="items" value="桌球"/>桌球<br>
			<input type="button" id="send" value="提交"/>
		</form>
		<script>
			$(function(){
				$("#checkedAll").click(function(){
						$("[name=items]:checkbox").attr("checked",this.checked) ;
				});
				$("[name=items]:checkbox").click(function(){
					var flag = true ;
					$("[name=items]:checkbox").each(function(){
						if(!this.checked){
							flag = false ;
						}
					})
					$("#checkedAll").attr('checked',flag) ;
				})
				$("#send").click(function(){
					var str = "您选中的是：\r\n"
					$("[name=items]:checkbox:checked").each(function(){
						str += $(this).val()+"\r\n"
					})
					alert(str) ;
				})
			})
		</script>
	 </body>
	</html>

>还没有解决问什么第三次点击全选之后就不起作用了。

下拉列框应用

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<div class="content" style="float:left">
			<select multiple id="select1" style="width:125px;height:180px">
				<option value="1">选项1</option>
				<option value="2">选项2</option>
				<option value="3">选项3</option>
				<option value="4">选项4</option>
				<option value="5">选项5</option>
				<option value="6">选项6</option>
				<option value="7">选项7</option>
				<option value="8">选项8</option>
			</select>
			<div>
				<span id="add">选中添加到右边&gt;&gt;</span><br>
				<span id="add_all">全部添加到右边&gt;&gt;</span>
			</div>
		</div>
		<div class="content" style="float:left;;margin-left:20px">
			<select multiple id="select2" style="width:125px;height:180px"></select>
			<div>
				<span id="remove">&lt;&lt;选中删除到左边</span><br>
				<span id="remove_all">&lt;&lt;全部删除到左边</span>
			</div>
		</div>
		<script>
		$(function(){
			$("#add").click(function(){
				var $options = $("#select1 option:selected") ;
				$options.appendTo("#select2") ;
			})
			$("#add_all").click(function(){
				var $options = $("#select1 option") ;
				$options.appendTo("#select2") ;
			})
			$("#select1").dblclick(function(){
				$("#select1 option:selected").appendTo("#select2") ;
			})
			$("#remove").click(function(){
				var $options = $("#select2 option:selected") ;
				$options.appendTo("#select1") ;
			})
			$("#remove_all").click(function(){
				var $options = $("#select2 option") ;
				$options.appendTo("#select1") ;
			})
			$("#select2").dblclick(function(){
				$("#select2 option:selected").appendTo("#select1") ;
			})
		})
		</script>
	 </body>
	</html>

表单验证

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<form action="" method="post">
			<div class="int">
				<label for="username">用户名：&nbsp;&nbsp;&nbsp;</label>
				<input type="text" id="username" class="required"/>
			</div>
			<div class="int">
				<label for="email">邮箱：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<input type="text" id="email" class="required"/>
			</div>
			<div class="int">
				<label for="personinfo">个人资料：</label>
				<input type="text" id="personinfo"/>
			</div>
			<div class="sub">
				<input type="submit" value="提交" id="send"/>
				<input type="reset" value="重置" id="res"/>
			</div>
		</form>
		<script>
		$(function(){
			$("form :input.required").each(function(){
				var $required = $("<strong class='high'> *</strong>") ;
				$(this).parent().append($required) ;
			})
			$("form :input").blur(function(){
				var $parent = $(this).parent() ;
				$parent.find(".formtips").remove() ;
				if($(this).is("#username")){
					if(this.value == "" || this.value.length < 6){
						var errorMsg = "请输入至少6位的用户名." ;
						$parent.append("<span class='formtips onError'>"+errorMsg+"</span>") ;
					}else{
						var okMsg = "输入正确." ;
						$parent.append("<span class='formtips onSuccess'>"+okMsg+"</span>") ;
					}
				}
				if($(this).is("#email")){
					if(this.value == "" || (this.value!="" && !/.+@.+\.[a-zA-Z]{2,4}$/.test(this.value))){
						var errorMsg = "请输入正确的E-Mail地址." ;
						$parent.append("<span class='formtips onError'>"+errorMsg+"</span>") ;
					}else{
						var okMsg = "输入正确." ;
						$parent.append("<span class='formtips onSuccess'>"+okMsg+"</span>") ;
					}
				}
				$("#send").click(function(){
					$("form .required:input").trigger("blur") ;
					var numError = $("form .onError").length ;
					if(numError){
						return false ;
					}
					alert("注册成功，密码已经发送到您的邮箱，请注意查收！") ;
				})
			}).keyup(function(){
				$(this).triggerHandler("blur") ;
			}).focus(function(){
				$(this).triggerHandler("blur") ;
			})
		})
		</script>
	 </body>
	</html>

>客户端验证只是为了提升用户的操作体验，服务器端仍需要对用户输入数据的合法性进行校验，对于禁用了脚本的用户和自制网页提交的操作，必须在服务器端进行验证。

### 表格应用

**1. 普通的隔行变色**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.even {
			background: #fff38f ;
		}
		.odd {
			background: #ffffee ;
		}
	  </style>
	 </head>
	 <body>
		<table>
			<thead>
				<tr><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr class="odd"><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="even"><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr class="odd"><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="even"><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr class="odd"><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr class="even"><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
	 </body>
	</html>

另外一种更加简单的做法

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.even {
			background: red ;
		}
		.odd {
			background: yellow ;
		}
	  </style>
	 </head>
	 <body>
		<table>
			<thead>
				<tr><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
		<script>
		$(function(){
			$("tr:odd").addClass("odd") ;
			$("tr:even").addClass("even") ;
		})
		</script>
	 </body>
	</html>

**2. 单选框控制表格行高亮**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.selected {
			background: yellow ;
		}
	  </style>
	 </head>
	 <body>
		<table>
			<thead>
				<tr><th>&nbsp;</th><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr><td><input type="radio" name="dd" value="1"/></td><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr><td><input type="radio" name="dd" value="1" checked/></td><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr><td><input type="radio" name="dd" value="1"/></td><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr><td><input type="radio" name="dd" value="1"/></td><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr><td><input type="radio" name="dd" value="1"/></td><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr><td><input type="radio" name="dd" value="1"/></td><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
		<script>
			$(function(){
				$("tbody > tr").click(function(){
					$(this).addClass("selected")
					.siblings().removeClass("selected")
					.end()
					.find(":radio").attr("checked",true) ;
				})
				$("table :radio:checked").parent().parent().addClass("selected") ;
			})
		</script>
	 </body>
	</html>

>记住，单选框的作用是只选一个，所以属性nane要相同，不容没有意义。

**3. 复选框表格行高亮**

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.selected {
			background: yellow ;
		}
	  </style>
	 </head>
	 <body>
		<table>
			<thead>
				<tr><th>&nbsp;</th><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr><td><input type="checkbox" value="1"/></td><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr><td><input type="checkbox" value="1" checked/></td><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr><td><input type="checkbox" value="1"/></td><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr><td><input type="checkbox" value="1"/></td><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr><td><input type="checkbox" value="1"/></td><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr><td><input type="checkbox" value="1"/></td><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
		<script>
			$(function(){
				$("tbody > tr").click(function(){
					if($(this).hasClass("selected")){
						$(this)
							.removeClass("selected")
							.find(":checkbox").attr("checked",false)
					}else{
						$(this)
							.addClass("selected")
							.find(":checkbox").attr("checked",true)
					}
				})
				$("table :checkbox:checked").parent().parent().addClass("selected") ;
			})
		</script>
	 </body>
	</html>

#### 表格展开、关闭

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.selected {
			background: yellow ;
		}
	  </style>
	 </head>
	 <body>
		<table>
			<thead>
				<tr><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr class="parent" id="01"><td colspan="3">前台设计组</td></tr>
				<tr class="children_01"><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_01"><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr class="parent" id="02"><td colspan="3">前台开发组</td></tr>
				<tr class="children_02"><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_02"><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr class="parent" id="03"><td colspan="3">后台设计组</td></tr>
				<tr class="children_03"><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr class="children_03"><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
		<script>
		$(function(){
			$("tr.parent").click(function(){
				$(this)
					.toggleClass("selected")
					.siblings(".children_"+this.id).toggle() ;
			})
		})
		</script>
	 </body>
	</html>

#### 表格内容的筛选

用contains筛选

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.selected {
			background: yellow ;
		}
	  </style>
	 </head>
	 <body>
		<table>
			<thead>
				<tr><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr class="parent" id="01"><td colspan="3">前台设计组</td></tr>
				<tr class="children_01"><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_01"><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr class="parent" id="02"><td colspan="3">前台开发组</td></tr>
				<tr class="children_02"><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_02"><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr class="parent" id="03"><td colspan="3">后台设计组</td></tr>
				<tr class="children_03"><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr class="children_03"><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
		<script>
		$(function(){
			$("tr:contains('王五')").addClass("selected") ;
		})
		</script>
	 </body>
	</html>

利用过滤器筛选

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.selected {
			background: yellow ;
		}
	  </style>
	 </head>
	 <body>
		筛选：<input type="text" id="filterName" />
		<table>
			<thead>
				<tr><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr class="parent" id="01"><td colspan="3">前台设计组</td></tr>
				<tr class="children_01"><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_01"><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr class="parent" id="02"><td colspan="3">前台开发组</td></tr>
				<tr class="children_02"><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_02"><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr class="parent" id="03"><td colspan="3">后台设计组</td></tr>
				<tr class="children_03"><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr class="children_03"><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
		<script>
		$(function(){
			$("table tbody tr").hide()
				.filter(":contains('赵')").show() ;
		})
		</script>
	 </body>
	</html>

利用过滤器输入筛选

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.selected {
			background: yellow ;
		}
	  </style>
	 </head>
	 <body>
		筛选：<input type="text" id="filterName" />
		<table>
			<thead>
				<tr><th>姓名</th><th>性别</th><th>暂住地</th></tr>
			</thead>
			<tbody>
				<tr class="parent" id="01"><td colspan="3">前台设计组</td></tr>
				<tr class="children_01"><td>张山</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_01"><td>李四</td><td>女</td><td>浙江杭州</td></tr>
				<tr class="parent" id="02"><td colspan="3">前台开发组</td></tr>
				<tr class="children_02"><td>王五</td><td>男</td><td>浙江宁波</td></tr>
				<tr class="children_02"><td>赵六</td><td>女</td><td>湖南长沙</td></tr>
				<tr class="parent" id="03"><td colspan="3">后台设计组</td></tr>
				<tr class="children_03"><td>Rain</td><td>男</td><td>浙江温州</td></tr>
				<tr class="children_03"><td>MAXMAN</td><td>男</td><td>浙江宁波</td></tr>
			</tbody>
		</table>
		<script>
		$(function(){
			$("#filterName").keyup(function(){
				$("table tbody tr")
					.hide()
					.filter(":contains('"+( $(this).val() )+"')").show() ;
			}).keyup() ;
		})
		</script>
	 </body>
	</html>

>后面又加一个keyup()函数的作用是，避免页面在刷新之后，不能执行函数。表示：DOM加载完成时，为表单元素绑定事件，并且立即触发该事件。

### 网页字体的大小

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	 </head>
	 <body>
		<div class="msg">
			<div class="msg_caption">
				<span class="bigger">放大</span>
				<span class="smaller">缩小</span>
			</div>
			<div>
				<p id="para">
					This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.This is some text.
				</p>
			</div>
		</div>
		<script>
		$(function(){
			$("span").click(function(){
				var thisEle = $("#para").css("font-size") ;
				var textFontSize = parseFloat(thisEle,10) ;
				var cName = $(this).attr("class") ;
				if(cName == "bigger"){
					textFontSize += 2 ;
				}else if(cName == "smaller"){
					textFontSize -= 2 ;
				}
				$("#para").css("font-size",textFontSize+"px") ;
			})
		})
		</script>
	 </body>
	</html>

### 网页选项卡

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.selected {
			background: yellow ;
		}
		div.tab_menu {
			margin-bottom: 0px ;
		}
		div.tab_box {
			margin-top: 0px ;
		}
		li {
			display: inline ;
			border: 1px solid ;
		}
		div.tab_box div {
			width: 100px ;
			height: 160px ;
			border: 1px solid ;
			float: left ;
		}
		div.tab_box div.hide {
			display: none ;
		}
	  </style>
	 </head>
	 <body>
		<div class="tab">
			<div class="tab_menu">
				<ul>
					<li class="selected">时事</li>
					<li>体育</li>
					<li>娱乐</li>
				</ul>	
			</div>
			<div class="tab_box">
				<div>时事</div>
				<div class="hide">体育</div>
				<div class="hide">娱乐</div>
			</div>
		 </div>
			<script>
		$(function(){
			var $div_li = $("div.tab_menu ul li") ;
			$div_li.click(function(){
				$(this)
					.addClass("selected")
					.siblings().removeClass("selected") ;
					var index = $div_li.index(this) ;
				$("div.tab_box > div ")
					.eq(index).show()
					.siblings().hide() ;
			})
		})
		</script>
	 </body>
	</html>

增加hover功能

	<!doctype html>
	<html lang="en">
	 <head>
	  <meta charset="UTF-8">
	  <title></title>
	  <script src="/assets/vendor/jquery/jquery-1.11.3.min.js"></script>
	  <style>
		.hover {
			background: red ;
		}
		.selected {
			background: yellow ;
		}
		div.tab_menu {
			margin-bottom: 0px ;
		}
		div.tab_box {
			margin-top: 0px ;
		}
		li {
			display: inline ;
			border: 1px solid ;
		}
		div.tab_box div {
			width: 100px ;
			height: 160px ;
			border: 1px solid ;
			float: left ;
		}
		div.tab_box div.hide {
			display: none ;
		}
	  </style>
	 </head>
	 <body>
		<div class="tab">
			<div class="tab_menu">
				<ul>
					<li class="selected">时事</li>
					<li>体育</li>
					<li>娱乐</li>
				</ul>	
			</div>
			<div class="tab_box">
				<div>时事</div>
				<div class="hide">体育</div>
				<div class="hide">娱乐</div>
			</div>
		 </div>
			<script>
		$(function(){
			var $div_li = $("div.tab_menu ul li") ;
			$div_li.click(function(){
				$(this)
					.addClass("selected")
					.siblings().removeClass("selected") ;
					var index = $div_li.index(this) ;
				$("div.tab_box > div ")
					.eq(index).show()
					.siblings().hide() ;
			}).hover(function(){
				$(this).addClass("hover") ;
			},function(){
				$(this).removeClass("hover") ;
			})
		})
		</script>
	 </body>
	</html>

---

---
