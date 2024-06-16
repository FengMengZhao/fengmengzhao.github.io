---
layout: post
title: BootStrap框架基础
---

## CSS

代码：

	<!doctype html> 
	<html lang="en"> 
	<head>     
		<meta charset="UTF-8">
		<title>Css</title>
		<link rel="stylesheet" href="BootStrap3.0.3\css\bootstrap.min.css">
			<script src="BootStrap3.0.3\js\jquery.min.js"></script>
			<script src="BootStrap3.0.3\js\bootstrap.min.js"></script>
			<script src="BootStrap3.0.3\js\holder.min.js"></script>
			<script src="BootStrap3.0.3\js\application.js"></script>     
	<style>
		.container{             
			background: #ccc ;        
		}         
		.row{
			margin-bottom: 5px ;
		}
		.container{
			width: 1600px ;
		}
		.list-inline li{
			margin-left: 100px ;
			margin-right: 100px ;
		}
		.page-header{
			border-color: black ;
		}
	</style>
	</head>
	<body>
		<div class="container">




			<h3 class="page-header">Grid System</h3>
			<div class="row">
				<div class="col-lg-12">
					<h4>将整个屏幕铺满<small>这里可以添加副标题</small></h4>
					<p class="text-center"><mark>栅格系统</mark>，<del>是通过将<strong>整个屏幕<strong>分为12小的块状div来实现div的快速布局</del></p>
					<p class="lead"><mark>栅格系统</mark>，<s>是通过将整个屏幕分为12小的块状div来实现div的快速布局</s></p>
					<p class="text-right"><mark>栅格系统</mark>，<u>是通过将整个屏幕分为12小的块状div来实现div的快速布局</u></p>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<h4><ins><em>通过一个4块div和4块偏移量来实现div块的居中</em></ins></h4>
					<img src="holder.js/100%x200" alt="">
				</div>
			</div>	
			<div class="row">
				<div class="col-lg-6">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-6">
					<img src="holder.js/100%x200" alt="">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-4">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-4">
					<img src="holder.js/100%x200" alt="">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-3">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-3">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-3">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-3">
					<img src="holder.js/100%x200" alt="">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-2">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-2">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-2">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-2">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-2">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-2">
					<img src="holder.js/100%x200" alt="">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
				<div class="col-lg-1">
					<img src="holder.js/100%x200" alt="">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-9">
					<div class="row">
						<div class="col-lg-4">
							<img src="holder.js/100%x200" alt="">
						</div>
						<div class="col-lg-8">
							<img src="holder.js/100%x200" alt="">
						</div>
					</div>
				</div>
			</div>




			<h3 class="page-header">Typography</h3>
			<div class="row">
				<div class="col-lg-12">
					<abbr title="fengmengzhao">fmz</abbr>
					<abbr title="fengmengzhao" class="initialism">fmz</abbr>
					<blockquote>
						<p>声明诚可贵，爱情价更高；若为自由故，两者皆可抛</p>
						<footer>fmz</footer>
					</blockquote>
					<ul class="list-unstyled">
						<li>fmz</li>
						<li>fmz</li>
						<li>fmz</li>
						<li>fmz</li>
						<li>fmz</li>
						<li>fmz</li>
					</ul>
					<ul class="list-inline">
						<li>Home</li>
						<li>Blog</li>
						<li>Resume</li>
						<li>Work</li>
						<li>Honor</li>
						<li>Love</li>
					</ul>
				</div>
			</div>


			<h3 class="page-header">Code</h3>
			<div class="row">
				<div class="col-lg-12">
					<p><code>Linux:</code>I love you!</p>
					<pre>
						public class Person{
							public static void main(String args[]){
								System.out.println("Hello World!!!")
							}
						}
					</pre>
				</div>
			</div>




			<h3 class="page-header">Tables</h3>
			<div class="row">
				<div class="col-lg-12">
					<table class="table">
						<tr>
							<th>1</th>
							<td>2</td>
							<td>3</td>
							<td>4</td>
							<td>5</td>
							<td>6</td>
							<td>7</td>
						</tr>
						<tr class="active">
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
						<tr class="success">
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
						<tr class="danger">
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
						<tr class="warning">
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
						<tr class="info">
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
					</table>
					<table class="table table-striped table-bordered table-hover">
						<tr>
							<th>1</th>
							<td>2</td>
							<td>3</td>
							<td>4</td>
							<td>5</td>
							<td>6</td>
							<td>7</td>
						</tr>
						<tr>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
						<tr>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
						<tr>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
						<tr>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
							<td>fmz</td>
						</tr>
					</table>
				</div>
			</div>



			<h3 class="page-header">Forms</h3>	
			<form action="">
				<div class="form-group">
					<label for="emailid">Email</label>
					<input type="text" class="form-control" id="emailid" placeholder="Email">
				</div>
				<div class="form-group">
					<label for="passwdid">Password</label>
					<input type="password" class="form-control" id="passwdid" placeholder="password">
				</div>
				<div class="form-goup">
					<label for="inputid">File input</label>
					<input type="file" id="inputid">
					<p class="help-block">plese upload your file here.</p>
				</div>
				<div class="checkbox">
					<label for="">
						<input type="checkbox">Check me out
					</label>
				</div>
				<div>
					<button type="submin" class="btn btn-primary">Submit</button>
					<button type="reset" class="btn btn-danger">Cancel</button>			
				</div>
			</form>
			<h4>内联表单样式1</h4>
			<form class="form-inline">
				<div class="form-group">
					<label for="emailid">Email</label>
					<input type="text" class="form-control" id="emailid" placeholder="Email">
				</div>
				<div class="form-group">
					<label for="passwdid">Password</label>
					<input type="password" class="form-control" id="passwdid" placeholder="password">
				</div>
				<button type="submit" class="btn btn-primary">Send invitation</button>
			</form>
			<h4>内联表单样式2</h4>
			<form class="form-inline">
				<div class="form-group">
					<label class="sr-only" for="emailid">Email</label>
					<input type="text" class="form-control" id="emailid" placeholder="Email">
				</div>
				<div class="form-group">
					<label class="sr-only" for="passwdid">Password</label>
					<input type="password" class="form-control" id="passwdid" placeholder="password">
				</div>
				<div class="checkbox">
					<label for="">
						<input type="checkbox">Remeber me
					</label>
				</div>
				<button type="submit" class="btn btn-primary">Sign in</button>
			</form>
			<h4>内联表单样式3</h4>
			<form action="" class="form-inline">
				<label class="sr-only" for="amountid">Amount</label>
				<div class="input-group">
					<div class="input-group-addon">$</div>
					<input type="text" class="form-control" id="amountid" placeholder="Amount">
					<div class="input-group-addon">.00</div>
				</div>
				<button type="submit" class="btn btn-primary">Transfer cash</button>
			</form>
			<h4>内联表单样式4</h4>
			<form class="form-horizontal">
				<div class="form-group has-success">
					<label class="col-lg-1 control-label" for="emailid">Email</label>
					<div class="col-lg-10">					
						<input type="text" class="form-control" id="emailid" placeholder="Email">
					</div>
				</div>
				<div class="form-group has-warning">
					<label class="control-label col-lg-1" for="passwdid">Password</label>
					<div class="col-lg-10">
						<input type="password" class="form-control col-lg-9" id="passwdid" placeholder="password">
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-10 col-lg-offset-1">
						<div class="checkbox">
							<label for="">
							<input type="checkbox">Remeber me
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-10 col-lg-offset-1">	
						<button type="submit" class="btn btn-primary">Sign in</button>
					</div>
				</div>		
			</form>
			<h4>Checkbox and Radios</h4>
			<div class="checkbox">
				<label for="">
					<input type="checkbox">Fmz
				</label>
			</div>
			<div class="checkbox disabled">
				<label for="">
					<input type="checkbox" disabled checked>Fmz
				</label>
			</div>
			<div class="radio">
				<label for="">
					<input type="radio" name="fmz">fmz1
				</label>
			</div>
			<div class="radio">
				<label for="">
					<input type="radio" name="fmz">fmz2
				</label>
			</div>
			<div class="radio">
				<label for="">
					<input type="radio" name="fmz">fmz3
				</label>
			</div>
			<h4>Inline Checkbox and Radios</h4>
			<label for="" class="checkbox-inline">
				<input type="checkbox">1
			</label>
			<label for="" class="checkbox-inline">
				<input type="checkbox">2
			</label>
			<label for="" class="checkbox-inline">
				<input type="checkbox">3
			</label>
			<div>
				<label for="" class="radio-line">
					<input type="radio" name="fmz2">1
				</label>
				<label for="" class="radio-line">
					<input type="radio" name="fmz2">2
				</label>
				<label for="" class="radio-line">
					<input type="radio" name="fmz2">3
				</label>
			</div>
			<h4>Selection</h4>
			<select name="" id="" class="form-control">
				<option value="">1</option>
				<option value="">2</option>
				<option value="">3</option>
				<option value="">4</option>
				<option value="">5</option>
			</select>
			<select name="" id="" class="form-control" multiple>
				<option value="">1</option>
				<option value="">2</option>
				<option value="">3</option>
				<option value="">4</option>
				<option value="">5</option>
			</select>
			<h4>内联表单样式5-form-control-static样式</h4>
				<form class="form-horizontal">
				<div class="form-group">
					<label class="col-lg-1 control-label" for="emailid">Email</label>
					<div class="col-lg-10">
						<p class="form-control-static">fengmengzhao.gmail.com</p>					
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-lg-1" for="passwdid">Password</label>
					<div class="col-lg-10">
						<input type="password" class="form-control col-lg-9" id="passwdid" placeholder="password">
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-10 col-lg-offset-1">
						<div class="checkbox">
							<label for="">
							<input type="checkbox">Remeber me
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-10 col-lg-offset-1">	
						<button type="submit" class="btn btn-primary">Sign in</button>
					</div>
				</div>		
			</form>



			<h3 class="page-header">Button<small>能使用button类的有button a 和 input标签</small></h3>
			<a href="#" class="btn btn-default">fmz</a>
			<button class="btn btn-primary">点我</button>		
			<input type="text" class="form-control btn btn-warning">



			<h3 class="page-header">Images</h3>
			<img src="holder.js/500x500" alt="" class="img-rounded">
			<img src="holder.js/500x500" alt="" class="img-circle">
			<img src="holder.js/500x500" alt="" class="img-thumbnail">



			<h3 class="page-header">Helper classes</h3>
			<h4>语境字体颜色</h4>
			<p class="text-primary">某某某是一个好孩子。</p>
			<p class="text-success">某某某是一个好孩子。</p>
			<p class="text-info">某某某是一个好孩子。</p>
			<p class="text-warning">某某某是一个好孩子。</p>
			<p class="text-danger">某某某是一个好孩子。</p>
			<h4>上下文段落背景</h4>
			<p class="bg-primary">某某某是一个坏孩子。</p>
			<p class="bg-success">某某某是一个坏孩子。</p>
			<p class="bg-info">某某某是一个坏孩子。</p>
			<p class="bg-warning">某某某是一个坏孩子。</p>
			<p class="bg-danger">某某某是一个坏孩子。</p>
			<h4>Quick floats</h4>
			<div class="row">
				<div class="col-lg-4 pull-right">
					<p class="bg-primary">某某某是一个好的坏孩子。</p>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 pull-left">
					<p class="bg-primary">某某某是一个好的坏孩子。</p>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 center-block">
					<p class="bg-primary">某某某是一个好的坏孩子。</p>
				</div>
			</div>
			<h1 class="text-hide">美女<img src="holder.js/50x30" alt=""></h1>
			<a href="#" class="text-hide">Home<img src="holder.js/50x30" alt=""></a>
		</div>
	</body>
	</html>

## [运行代码](/source_htmls/bootstrap_learn_css.html)

### 栅格系统

`.col-lg-num`的padding-left: 15px ,padding-right=15px；其他padding、boder、margin都为0px

`.container`同`.col-lg-num`相同，with=1170像素

`.row`的margin-left=margin-right=-15px；其他为0px

栅格系统中也可以嵌套，嵌套在内部的块之和要小于或者等于12，如上述代码最后几行

想要将内容铺满整个屏幕，而与设备无关需要使用class container-fluid，即：`<div class="container-fluid">`，并且可以自定义contain的with值，当width值发生改变时，其他内容随之发生改变，及bootstrap能实现动态的计算

### 段落

`h_123456`标题的padding-bottom=9px，margin-top=40px，margin-botoom=20px，其他为0px，加入`<small></small>`表示标题一个副标题，也就是字体变小，其他样式发生一些改变

`p`段落的margin-bottom=10px，其他为0px，段落中字体的像素统一为14px，加入`.lead`可以段落突出，也就是字体变大，加入`<mark></mark>`可以将字体高亮，也就是标黄，加入`<s></s>`表示文本块不再使用，加入`<del></del>`表示文本快删除，加入`<ins></ins>或者<u></u>`表示下划线，加入`<strong></strong>`表示加粗字体，加入`<em></em>`表示字体倾斜

文本对齐的方式是在段落中加class，即`<p class="text-center/text-left/text-right"></p>`；如果是英文，段落中加入`class="text-lowercase"`表示所有英文字母小写，加入`class="text-uppercase"`表示所有英文字母大写，加入`class="text-capitalize"`表示所有的英文单词，首字母大写

`<abbr title="fengmengzhao">fmz</abbr>`表示缩写，即"fmz"是"fengmengzhao"的缩写'；<abbr title="fengmengzhao class="initialism">fmz</abbr>"就是讲缩写的字母都大写

`<blockquote></blockquote>`表示引用文字，引用的出处可以用`<footer></footer>`来表示

### 代码

`<code></code>`表示内联的代码

`<pre></pre>`表示代码段，也就是一段代码

### 表格

`.table`表示普通的表格，做了一些改变；`.table-bordered`表示有border的表格；`.table-striped`表示有斑马线的表格；`.table-hover`表示有悬停动画的表格

`.active` `.success` `.warning` `.danger` `.info`表示有不同颜色的表格

### 表单

上下结构的表单要使用`<form><div .form-group><label></label><input .form-control></div></form>`来完成

inline form要使用`<form .form-inline><div .form-group><label></label><input .form-control></div></form>`只是比正常的form表当样式在form元素上多加了一个`form-inline`样式

>如果想要吟唱inline form的label，可以在label上加一个`.sr-only`即可；如果想在input元素的前后加上一个特殊的符号，比如`$`符号等，需要将input包装，使用`...<div .input-group><div .input-group-addon>$</div><input /><div .input-group-addon>.00</div></div>...`完成

左右结构表单要使用`<form .horizontal><div .form-group><label .col-lg-2 .control-label></label><div .col-lg-10 .form-control><input /></div></div></form>`

>input中可以使用`placeholder`表示占位符，`disabled`表示不能编辑，`readonly`表示不能输入

上下结构的复选框使用`<div .checkbox><label><input tpye="checkbox">*</label></div>`

上下结构的单选框使用`<div .radio><label><input tpye="radio">*</label></div>`

水平结构的复选框使用`<label .checkbox-inline><input type="checkbox" /></label>`

水平结构的单选框使用`<label .radio-inline><input type="radio" /></label>`

下拉选框使用`<select .form-control><option></option><option></option></select>`，如果想要下拉的内容显现出多个来使用`<select .form-control multiple><option></option><option></option></select>`

左右结构的表单中，如果想要input的内容固定，需要使用`form-control-static`，只需要在将input元素换成p元素，加入class即可，即`<p .form-control-static>fengmengzhao.github.io</p>`

### 按钮

`.btn btn-primary...`可以应用于三种元素，分别是：`<a></a>`、`<button></button>`、`<input></input>`元素

### 图片

`<img .img-rounded/>`表示圆角图像；`<img .img-circle/>`表示圆形图像；`<img .img-thumbnail/>`表示有边框的图像

### Helper class(一些有用的类)

特殊语境下的字体可以加上适当的class：`<p .text-primary|success|info|warning|danger>`分别会给不同的字体加上不同的颜色

如果需要段落加上特殊的背景，需要：`<p .bg-primary|success|info|warning|danger`分别会加上不同的背景颜色

想让一个div向左向右或者居中对齐，分别使用：`<div .pull-lefg|.pull-right|block-center`>

可以使用`<div .show|.hidden></div>`来显示或者隐藏块中的内容

如果想让文字隐藏，可以使用`<h1 .text-hide></h1>`把文字隐藏后可以使用图片替代

## Components

代码：

	<!doctype html>
	<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Document</title>
		<link rel="stylesheet" href="BootStrap3.0.3\css\bootstrap.min.css">
		<script src="BootStrap3.0.3\js\jquery.min.js"></script>
		<script src="BootStrap3.0.3\js\bootstrap.min.js"></script>
		<script src="BootStrap3.0.3\js\holder.min.js"></script>
		<script src="BootStrap3.0.3\js\application.js"></script>  
		<style>
			.container{
				background: #ccc ;
			}
			.page-header{
				border-color: black ;
			}				
		</style>
	</head>
	<body>
		<div class="container">
			<h3 class="page-header">Glyphicons</h3>
			<div class="text-center">
				<button type="button" class="btn btn-primary">
					<span class="glyphicon glyphicon-signal" style="font-size:15px"></span>
				</button>	
				<button type="button" class="btn btn-primary">
					<span class="glyphicon glyphicon-pencil" style="font-size:15px"></span>
				</button>	
				<button type="button" class="btn btn-primary">
					<span class="glyphicon glyphicon-user" style="font-size:15px"></span>
				</button>	
				<button type="button" class="btn btn-primary">
					<span class="glyphicon glyphicon-trash" style="font-size:15px"></span>	
				</button>
			</div>
			<h3 class="page-header">Dropdowns</h3>
			<div class="pull-right">
				<div class="dropdown">
					<a href="#" class="btn btn-primary" data-toggle="dropdown">请选择<span class="caret"></span></a>		
					<ul class="dropdown-menu">
						<li class="dropdown-header">families</li>
						<li class="divider"></li>
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li class="disabled"><a href="javascript: ;" >fsl</a></li>
						<li class="dropdown-header">fridnds</li>
						<li class="divider"></li>
						<li><a href="">wlm</a></li>
						<li><a href="">zjb</a></li>
					</ul>
				</div>
			</div>
			<h3 class="page-header">Button groups</h3>
			<h4>basic</h4>
			<div class="text-center">
				<div class="btn-group btn-group-lg">
					<button type="button" class="btn btn-primary">left</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">right</button>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-primary">left</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">right</button>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-primary">left</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">middle</button>
					<button type="button" class="btn btn-primary">right</button>
				</div>
			</div>
			<h4>button toolbar</h4>
			<div class="text-center">
				<div class="btn-toobar">
					<div class="btn-group">
						<button type="button" class="btn btn-primary">left</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">right</button>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-primary">left</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">right</button>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-primary">left</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">middle</button>
						<button type="button" class="btn btn-primary">right</button>
					</div>
				</div>
			</div>
			<h4>nesting</h4>
			<div class="text-center">
				<div class="btn-group">
					<button type="button" class="btn btn-primary">fmz</button>
					<button type="button" class="btn btn-primary">cll</button>
					<div class="btn-group">
						<button class="btn btn-primary" type="primary" data-toggle="dropdown">plese select
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a href="">fsl</a></li>
							<li><a href="">fls</a></li>
							<li><a href="">rbr</a></li>
						</ul>
					</div>
				</div>
			</div>
			<h4>vertical variation</h4>
			<div class="text-center">
				<div class="btn-group-vertical">
					<button type="button" class="btn btn-primary">fmz</button>
					<button type="button" class="btn btn-primary">cll</button>
					<div class="btn-group">
						<button class="btn btn-primary" type="primary" data-toggle="dropdown">plese select
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a href="">fsl</a></li>
							<li><a href="">fls</a></li>
							<li><a href="">rbr</a></li>
						</ul>
					</div>
				</div>
			</div>
			<h3 class="page-header">Button dropdowns</h3>
			<div class="text-center">
				<div class="btn-group">
					<button type="button" data-toggle="dropdown" class="btn btn-default">
						Default
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" data-toggle="dropdown" class="btn btn-primary">
						Default
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" data-toggle="dropdown" class="btn btn-success">
						Success
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" data-toggle="dropdown" class="btn btn-info">
						Info
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" data-toggle="dropdown" class="btn btn-warning">
						Warning
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" data-toggle="dropdown" class="btn btn-danger">
						Danger
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
			</div>
			<h4>Split button dropdown</h4>	
			<div class="text-center">
				<div class="btn-group">
					<button type="button" class="btn btn-default">Default</button>
					<button type="button" data-toggle="dropdown" class="btn btn-default">
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-danger">Danger</button>
					<button type="button" data-toggle="dropdown" class="btn btn-danger">
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
				<div class="btn-group dropup">
					<button type="button" class="btn btn-warning">Dropup</button>
					<button type="button" data-toggle="dropdown" class="btn btn-warning">
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">fmz</a></li>
						<li><a href="">cll</a></li>
						<li><a href="">fsl</a></li>
					</ul>
				</div>
			</div>
			<h3 class="page-header">Input groups</h3>
			<h4>basic</h4>
			<div>
				<div class="input-group">
					<span class="input-group-addon">@</span>
					<input type="text" placeholder="username" class="form-control">
				</div>
			</div>
			<div class="input-group input-group-lg">
				<input type="text" placeholder="username"class="form-control">
				<span class="input-group-addon">@example.com</span>
			</div>
			<div class="input-group">
				<span class="input-group-addon">$</span>
				<input type="text" placeholder="cash" class="form-control">
				<span class="input-group-addon">.00</span>
			</div>
			<label for="basicurlid">Your basic url</label>
			<div class="input-group">
				<span class="input-group-addon">https://fengmengzhao.github.io</span>
				<input type="text" id="basicurlid" class="form-control">
			</div>
			<h4>chechboxes and radio addons</h4>
			<div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<span class="input-group-addon">
								<input type="checkbox">
							</span>
							<input type="text" class="form-control">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<span class="input-group-addon">
								<input type="radio">
							</span>
							<input type="text" class="form-control">
						</div>
					</div>
				</div>
			</div>
			<h4>button addons</h4>
			<div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<span class="input-group-btn">
								<button type="button" class="btn btn-primary">GO!</button>
							</span>
							<input placeholder="search for..." type="text" class="form-control">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<input placeholder="search for..." type="text" class="form-control">
							<span class="input-group-btn">
								<button type="button" class="btn btn-primary">GO!</button>
							</span>
						</div>
					</div>
				</div>
			</div>
			<h4>buttons with dropdowns</h4>
			<div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<div class="input-group-btn">
								<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Action
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a href="">fmz</a></li>
									<li><a href="">cll</a></li>
									<li><a href="">fsl</a></li>
								</ul>
							</div>
							<input type="text" class="form-control">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<input type="text" class="form-control">
							<div class="input-group-btn">
								<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Action
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a href="">fmz</a></li>
									<li><a href="">cll</a></li>
									<li><a href="">fsl</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<h4>segmented buttons</h4>
			<div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<div class="input-group-btn">
								<button type="button" class="btn btn-primary">Action</button>
								<button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a href="">fmz</a></li>
									<li><a href="">cll</a></li>
									<li><a href="">fsl</a></li>
								</ul>
							</div>
							<input type="text" class="form-control">
						</div>	
					</div>
				</div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<input type="text" class="form-control">
							<div class="input-group-btn">
								<button type="button" class="btn btn-primary">Action</button>
								<button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a href="">fmz</a></li>
									<li><a href="">cll</a></li>
									<li><a href="">fsl</a></li>
								</ul>
							</div>
						</div>	
					</div>
				</div>
			</div>
			<h4>multiple buttons</h4>
			<div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<div class="input-group-btn">
								<button class="btn btn-danger" type="button"><stong>A</strong></button>		
								<button class="btn btn-danger" type="button"><stong>B</strong></button>		
								<button class="btn btn-danger" type="button"><stong>?</strong></button>		
							</div>
							<input type="text" class="form-control">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-6 col-lg-offset-3">
						<div class="input-group">
							<input type="text" class="form-control">
							<div class="input-group-btn">
								<button class="btn btn-danger" type="button"><stong>A</strong></button>		
								<button class="btn btn-danger" type="button"><stong>B</strong></button>		
								<button class="btn btn-danger" type="button"><stong>?</strong></button>		
							</div>
						</div>
					</div>
				</div>
			</div>
			<h3 class="page-header">Navs</h3>
			<h4>标签</h4>
			<div>
				<ul class="nav nav-tabs">
					<li class="active"><a href="">Home</a></li>
					<li class=""><a href="">Blog</a></li>
					<li class=""><a href="">Contact</a></li>
				</ul>
			</div>
			<h4>pills</h4>
			<div>
				<ul class="nav nav-pills">
					<li class="active"><a href="">Home</a></li>
					<li class=""><a href="">Profile</a></li>
					<li class=""><a href="">Message</a></li>
				</ul>
			</div>

			<h4>pills 竖直方向</h4>
			<div>
				<ul class="nav nav-pills nav-stacked">
					<li class="active"><a href="">Home</a></li>
					<li class=""><a href="">Profile</a></li>
					<li class=""><a href="">Message</a></li>
				</ul>
			</div>
			<h4>标签自适应</h4>
			<div>
				<ul class="nav nav-tabs nav-justified">
					<li class="active"><a href="">Home</a></li>
					<li class=""><a href="">Blog</a></li>
					<li class=""><a href="">Contact</a></li>
				</ul>
			</div>
			<h4>pills 自适应</h4>
			<div>
				<ul class="nav nav-pills nav-justified">
					<li class="active"><a href="">Home</a></li>
					<li class="disabled"><a href="javascript: ;">Blog</a></li>
					<li class=""><a href="">Contact</a></li>
				</ul>
			</div>
			<h4>using dropdowns</h4>
			<div>
				<ul class="nav nav-tabs">
					<li class="active"><a href="">Home</a></li>
					<li class=""><a href="">Blog</a></li>
					<li class="dropdown">
						<a href="" data-toggle="dropdown">Love
							<span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="">家庭</a></li>
							<li><a href="">朋友</a></li>
							<li><a href="">亲戚</a></li>
						</ul>
					</li>
				</ul>
			</div>
			<div>
				<ul class="nav nav-pills">
					<li class="active"><a href="">Home</a></li>
					<li class=""><a href="">Blog</a></li>
					<li class="dropdown">
						<a href="" data-toggle="dropdown">Love
							<span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="">家庭</a></li>
							<li><a href="">朋友</a></li>
							<li><a href="">亲戚</a></li>
						</ul>
					</li>
				</ul>
			</div>
			<h3 class="page-header">Navbar 导航条</h3>
			<div>
				<div class="navbar navbar-default">
					<div class="navbar-header">
						<button class="navbar-toggle" data-toggle="collapse" data-target="#myheader">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
						<a href="" class="navbar-brand"><img src="..." alt="Brand"></a> 
					</div>
					<div class="collapse navbar-collapse" id="myheader">
						<ul class="nav navbar-nav">
							<li class="active"><a href="">Home <span class="badge">11</span></a></li>
							<li><a href="">Blog</a></li>
							<li><a href="">Profile</a></li>
							<li><a href="">Love</a></li>
							<li><a href="">Contact</a></li>
							<li class="dropdown">
								<a href="" data-toggle="dropdown">Love
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<li><a href="">家庭</a></li>
									<li><a href="">朋友</a></li>
									<li><a href="">亲戚</a></li>
								</ul>
							</li>
						</ul>
						<form action="" class="navbar-form pull-right">
							<div class="form-group">
								<input type="text" class="form-control" placeholder="keywords">
							</div>
							<div class="form-group">
								<input type="submit" value="Search" class="btn btn-primary">
							</div>
						</form>
						<p class="navbar-text pull-right">欢迎<a href="">某某某</a>登录!</p>
					</div>
				</div>
			</div><div>
				<div class="navbar navbar-inverse">
					<div class="navbar-header">
						<button class="navbar-toggle" data-toggle="collapse" data-target="#myheader">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
						<a href="" class="navbar-brand">Brand</a> 
					</div>
					<div class="collapse navbar-collapse" id="myheader">
						<ul class="nav navbar-nav">
							<li class="active"><a href="">Home</a></li>
							<li><a href="">Blog</a></li>
							<li><a href="">Profile</a></li>
							<li><a href="">Love</a></li>
							<li><a href="">Contact</a></li>
							<li class="dropdown">
								<a href="" data-toggle="dropdown">Love
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<li><a href="">家庭</a></li>
									<li><a href="">朋友</a></li>
									<li><a href="">亲戚</a></li>
								</ul>
							</li>
						</ul>
						<form action="" class="navbar-form pull-right">
							<div class="form-group">
								<input type="text" class="form-control" placeholder="keywords">
							</div>
							<div class="form-group">
								<input type="submit" value="Search" class="btn btn-primary">
							</div>
						</form>
						<p class="navbar-text pull-right">欢迎<a href="">某某某</a>登录!</p>
					</div>
				</div>
			</div>
			<h3 class="page-header">面包屑导航</h3>
			<div>
				<ul class="breadcrumb">
					<li><a href="">Home</a></li>
					<li><a href="">Library</a></li>
					<li class="active">Data</li>
				</ul>
			</div>		
			<h3 class="page-header">分页</h3>
			<div class="text-center">
				<ul class="pagination">
					<li><a href="">&laquo;</a></li>
					<li class="active"><a href="">1</a></li>
					<li><a href="">2</a></li>
					<li class="disabled"><a href="" onclick="return false ;">3</a></li>
					<li><a href="">4</a></li>
					<li><a href="">5</a></li>
					<li><a href="">&raquo;</a></li>
				</ul>
			</div>
			<div class="text-center">
				<ul class="pagination pagination-lg">
					<li><a href="">&laquo;</a></li>
					<li class="active"><a href="">1</a></li>
					<li><a href="">2</a></li>
					<li class="disabled"><a href="" onclick="return false ;">3</a></li>
					<li><a href="">4</a></li>
					<li><a href="">5</a></li>
					<li><a href="">&raquo;</a></li>
				</ul>
			</div>
			<div class="text-center">
				<ul class="pagination pagination-sm">
					<li><a href="">&laquo;</a></li>
					<li class="active"><a href="">1</a></li>
					<li><a href="">2</a></li>
					<li class="disabled"><a href="" onclick="return false ;">3</a></li>
					<li><a href="">4</a></li>
					<li><a href="">5</a></li>
					<li><a href="">&raquo;</a></li>
				</ul>
			</div>
			<h4>翻页</h4>
			<div>
				<ul class="pager">
					<li><a href="">previous</a></li>
					<li><a href="">next</a></li>
				</ul>
			</div>
			<div>
				<ul class="pager">
					<li class="previous"><a href="">&larr;previous</a></li>
					<li class="next"><a href="">next&rarr;</a></li>
				</ul>
			</div>
			<h3 class="page-header">Labels</h3>
			<div>
				<h3>Example heading<span class="label label-default">new</span></h3>
				<h3>Example heading<span class="label label-primary">new</span></h3>
				<h3>Example heading<span class="label label-success">new</span></h3>
				<h3>Example heading<span class="label label-info">new</span></h3>
				<h3>Example heading<span class="label label-warning">new</span></h3>
				<h3>Example heading<span class="label label-danger">new</span></h3>
			</div>
			<h3 class="page-header">徽章</h3>
			<div>
				<a href="">Inbox <span class="badge">15</span></a>
				<button class="btn btn-primary">Message
					<span class="badge">33</span>
				</button>
			</div>
			<h3 class="page-header">大屏幕显示</h3>
			<div>
				<div class="jumbotron">
					<h1>通知</h1>
					<p>今天下午六点准时在教务处开会，今天下午六点准时在教务处开会，今天下午六点准时在教务处开会，今天下午六点准时在教务处开会，今天下午六点准时在教务处开会，今天下午六点准时在教务处开会，今天下午六点准时在教务处开会，今天下午六点准时在教务处开会。</p>
					<button class="btn btn-primary">Learn More</button>
				</div>
			</div>
			<h3 class="page-header">Thumbnails</h3>
			<div>
				<div class="row">
					<div class="col-lg-3">
						<a href="" class="thumbnail">
							<img src="holder.js/100%x200" alt="">
						</a>
					</div>
					<div class="col-lg-3">
						<a href="" class="thumbnail">
							<img src="holder.js/100%x200" alt="">
						</a>
					</div>
					<div class="col-lg-3">
						<a href="" class="thumbnail">
							<img src="holder.js/100%x200" alt="">
						</a>
					</div>
					<div class="col-lg-3">
						<a href="" class="thumbnail">
							<img src="holder.js/100%x200" alt="">
						</a>
					</div>
				</div>
			</div>
			<h4>定制内容</h4>
			<div>
				<div class="row">
					<div class="col-lg-3">
						<div class="thumbnail text-center">
							<img src="holder.js/100%x200" alt="">
							<h3>书籍</h3>
							<p>好的东西总是经得住琢磨，好的东西总是经得住琢磨，好的东西总是经得住琢磨</p>
							<p>
								<a href="" class="btn btn-primary">购买</a>
								<a href="" class="btn btn-primary">取消</a>
							</p>
						</div>
					</div>
					<div class="col-lg-3">
						<div class="thumbnail text-center">
							<img src="holder.js/100%x200" alt="">
							<h3>书籍</h3>
							<p>好的东西总是经得住琢磨，好的东西总是经得住琢磨，好的东西总是经得住琢磨</p>
							<p>
								<a href="" class="btn btn-primary">购买</a>
								<a href="" class="btn btn-primary">取消</a>
							</p>
						</div>
					</div>
					<div class="col-lg-3">
						<div class="thumbnail text-center">
							<img src="holder.js/100%x200" alt="">
							<h3>书籍</h3>
							<p>好的东西总是经得住琢磨，好的东西总是经得住琢磨，好的东西总是经得住琢磨</p>
							<p>
								<a href="" class="btn btn-primary">购买</a>
								<a href="" class="btn btn-primary">取消</a>
							</p>
						</div>
					</div>
					<div class="col-lg-3">
						<div class="thumbnail text-center">
							<img src="holder.js/100%x200" alt="">
							<h3>书籍</h3>
							<p>好的东西总是经得住琢磨，好的东西总是经得住琢磨，好的东西总是经得住琢磨</p>
							<p>
								<a href="" class="btn btn-primary">购买</a>
								<a href="" class="btn btn-primary">取消</a>
							</p>
						</div>
					</div>
				</div>
			</div>
			<h3 class="page-header">警告框</h3>
			<div>
				<div class="alert">请输入你的名字</div>
				<div class="alert alert-success">请输入你的名字</div>
				<div class="alert alert-info">请输入你的名字</div>
				<div class="alert alert-warning">请输入你的名字</div>
				<div class="alert alert-danger">请输入你的名字</div>
			</div>
			<h4>可关闭的警告框</h4>
			<div>
				<div class="alert alert-success">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					请输入你的名字
				</div>
				<div class="alert alert-info">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					请输入你的名字
				</div>
				<div class="alert alert-warning">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					请输入你的名字
				</div>
				<div class="alert alert-danger">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					请输入你的名字
				</div>
			</div>
			<h4>警告框中的链接</h4>
			<div>
				<div class="alert alert-success">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					请<a class="alert-link" href="">这里是一个链接</a>输入你的名字
				</div>
				<div class="alert alert-info">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					请输入你的名字<a href="" class="alert-link">这里是一个链接</a>
				</div>
				<div class="alert alert-warning">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					<a href="" class="alert-link">这里是一个链接</a>请输入你的名字
				</div>
				<div class="alert alert-danger">
					<button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
					请输入你<a href="" class="alert-link">这里是一个链接</a>的名字
				</div>
			</div>
			<h3 class="page-header">进度条</h3>
			<div>
				<div class="progress">
					<div class="progress-bar" style="width: 40%">40%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progress-bar-success" style="width: 50%">50%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progress-bar-info" style="width: 60%">60%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progress-bar-warning" style="width: 70%">70%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progerss-bar-danger" style="width: 80%">80%</div>
				</div>
			</div>
			<h3 class="page-header">有条纹的进度条和激活的进度条</h3>
			<div>
				<div class="progress">
					<div class="progress-bar progress-bar-striped" style="width: 40%">40%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progress-bar-success progress-bar-striped" style="width: 50%">50%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progress-bar-info progress-bar-striped" style="width: 60%">60%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progress-bar-warning progress-bar-striped active" style="width: 70%">70%</div>
				</div>
				<div class="progress">
					<div class="progress-bar progerss-bar-danger progress-bar-striped active" style="width: 80%">80%</div>
				</div>
			</div>
			<h3 class="page-header">评论列表</h3>
			<div>
				<div class="media">
					<div class="media-left">
						<a href=""><img src="holder.js/50x60" alt=""></a>
					</div>
					<div class="media-body">
						<h4 class="media-heading">生活的意义</h4>
						<p>中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。</p>
						<div class="media">
							<div class="media-left">
								<a href=""><img src="holder.js/50x60" alt=""></a>
							</div>
							<div class="media-body">
								<h4 class="media-heading">生活的意义</h4>
								<p>中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。</p>
								<div class="media">
									<div class="media-left">
										<a href=""><img src="holder.js/50x60" alt=""></a>
									</div>
									<div class="media-body">
										<h4 class="media-heading">生活的意义</h4>
										<p>中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。</p>
									</div>
								</div>
							</div>
						</div>
						<div class="media">
							<div class="media-left">
								<a href=""><img src="holder.js/50x60" alt=""></a>
							</div>
							<div class="media-body">
								<h4 class="media-heading">生活的意义</h4>
								<p>中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。</p>
							</div>
						</div>
					</div>
				</div>
				<div class="media">
					<div class="media-left">
						<a href=""><img src="holder.js/50x60" alt=""></a>
					</div>
					<div class="media-body">
						<h4 class="media-heading">生活的意义</h4>
						<p>中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。中国人民从此过上了幸福的生活，是如此的美好。</p>
					</div>
				</div>
			</div>
			<h3 class="page-header">list列表</h3>
			<h4>list列表中的徽章</h4>
			<div>
				<ul class="list-group">
					<li class="list-group-item">
						<span class="badge">44</span>
						fmz
					</li>
					<li class="list-group-item">cll</li>
					<li class="list-group-item">wlm</li>
					<li class="list-group-item">fsl</li>
					<li class="list-group-item">fls</li>
					<li class="list-group-item">
						<span class="badge">66</span>
						rbr
					</li>
				</ul>	
			</div>
			<h4>列表中的链接</h4>
			<div>
				<div class="list-group">
					<a href="" class="list-group-item active">www.baidu.com<span class="badge">44</span></a>
					<a href="" class="list-group-item">www.baidu.com</a>
					<a href="" class="list-group-item">www.baidu.com</a>
					<a href="" class="list-group-item">www.baidu.com</a>
				</div>
			</div>
			<h4>列表内容定制</h4>
			<div>
				<div class="list-group">
					<a href="" class="list-group-item active">
						<h4>中国最高的人</h4>
						<p>中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？中国最高的人是谁呢？</p>
					</a>
					<a href="" class="list-group-item">www.baidu.com</a>
					<a href="" class="list-group-item">www.baidu.com</a>
					<a href="" class="list-group-item">www.baidu.com</a>
				</div>
			</div>
			<h4>无标题面板</h4>
			<h3 class="page-header">面板</h3>
			<div class="panel panel-primary">
				<div class="panel-heading">没有标题</div>
				<div class="panel-body">这里面是面板的内容，我们也可以在这里写出自己的心里话。</div>
			</div>
			<h4>有标题面板</h4>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">中华民族的伟大复兴</h4>
				</div>
				<div class="panel-body">这里面是面板的内容，我们也可以在这里写出自己的心里话。</div>
			</div>
			<h4>有标题的footer的面板</h4>
			<div class="panel panel-danger">
				<div class="panel-heading">
					<h4 class="panel-title">中华民族的伟大复兴</h4>
				</div>
				<div class="panel-body">这里面是面板的内容，我们也可以在这里写出自己的心里话。这里面是面板的内容，我们也可以在这里写出自己的心里话。这里面是面板的内容，我们也可以在这里写出自己的心里话。这里面是面板的内容，我们也可以在这里写出自己的心里话。</div>
				<div class="panel-footer">还是好好学习吧</div>
			</div>
			<h4>有表格的面板</h4>
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">中华民族的伟大复兴</h3>
				</div>
				<div class="panel-body">
					<table class="table table-bordered table-striped">
						<tr>
							<th>数字</th>
							<th>数字</th>
							<th>数字</th>
							<th>数字</th>
							<th>数字</th>
							<th>数字</th>
							<th>数字</th>
							<th>数字</th>
						</tr>
						<tr>
							<td>1</td>
							<td>2</td>
							<td>3</td>
							<td>4</td>
							<td>5</td>
							<td>6</td>
							<td>7</td>
							<td>8</td>
						</tr>
						<tr>
							<td>1</td>
							<td>2</td>
							<td>3</td>
							<td>4</td>
							<td>5</td>
							<td>6</td>
							<td>7</td>
							<td>8</td>
						</tr>
						<tr>
							<td>1</td>
							<td>2</td>
							<td>3</td>
							<td>4</td>
							<td>5</td>
							<td>6</td>
							<td>7</td>
							<td>8</td>
						</tr>
						<tr>
							<td>1</td>
							<td>2</td>
							<td>3</td>
							<td>4</td>
							<td>5</td>
							<td>6</td>
							<td>7</td>
							<td>8</td>
						</tr>
					</table>
				</div>
			</div>
			<h4>带list的面板</h4>
			<div class="panel panel-success">
				<div class="panel-heading">
					<h3 class="panel-title">中华民族的伟大复兴</h3>
				</div>
				<div class="panel-body">
					<ul class="list-group">
						<li class="list-group-item">某某某</li>
						<li class="list-group-item">某某某</li>
						<li class="list-group-item">某某某</li>
						<li class="list-group-item">某某某</li>
						<li class="list-group-item">某某某</li>
						<li class="list-group-item">某某某</li>
						<li class="list-group-item">某某某</li>
						<li class="list-group-item">某某某</li>
					</ul>
				</div>
			</div>
			<h3 class="page-header">Well墙体</h3>
			<div>
				<div class="well">
					中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力，中国人民自己的抵抗力。
				</div>
			</div>
		</div>	
	</body>
	</html>

## [运行代码](/source_htmls/bootstrap_learn_component.html)

## JavaScript

代码：

	<!doctype html>
	<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Document</title>
		<link rel="stylesheet" href="BootStrap3.0.3\css\bootstrap.min.css">
		<script src="BootStrap3.0.3\js\jquery.min.js"></script>
		<script src="BootStrap3.0.3\js\bootstrap.min.js"></script>
		<script src="BootStrap3.0.3\js\holder.min.js"></script>
		<script src="BootStrap3.0.3\js\application.js"></script>
	</head>
	<body>
		<div class="container">
			<h3 class="page-header">Modals</h3>
			<h4>default modal</h4>
			<div>
				<button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal">查看内容</button>
				<div class="modal fade" id="myModal">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" data-dismiss="modal"><span>&times;</span></button>
								<h4 class="modal-title">中国人民解放军</h4>
							</div>
							<div class="modal-body">
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
							</div>
							<div class="modal-footer">
								<buttton class="btn btn-default" type="button" data-dismiss="modal">Close</buttton>
								<buttton class="btn btn-primary" type="button">Save changes</buttton>
							</div>
						</div>
					</div>
				</div>
			</div>
			<h4>small modal</h4>
			<div>
				<button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal2">查看内容</button>
				<div class="modal fade" id="myModal2">
					<div class="modal-dialog modal-sm">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" data-dismiss="modal"><span>&times;</span></button>
								<h4 class="modal-title">中国人民解放军</h4>
							</div>
							<div class="modal-body">
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
							</div>
							<div class="modal-footer">
								<buttton class="btn btn-default" type="button" data-dismiss="modal">Close</buttton>
								<buttton class="btn btn-primary" type="button">Save changes</buttton>
							</div>
						</div>
					</div>
				</div>
			</div>
			<h4>big modal</h4>
			<div>
				<button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal3">查看内容</button>
				<div class="modal fade" id="myModal3">
					<div class="modal-dialog modal-lg">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" data-dismiss="modal"><span>&times;</span></button>
								<h4 class="modal-title">中国人民解放军</h4>
							</div>
							<div class="modal-body">
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
							</div>
							<div class="modal-footer">
								<buttton class="btn btn-default" type="button" data-dismiss="modal">Close</buttton>
								<buttton class="btn btn-primary" type="button">Save changes</buttton>
							</div>
						</div>
					</div>
				</div>
			</div>
			<h4>无动画 modal</h4>
			<div>
				<button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal4">查看内容</button>
				<div class="modal" id="myModal4">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" data-dismiss="modal"><span>&times;</span></button>
								<h4 class="modal-title">中国人民解放军</h4>
							</div>
							<div class="modal-body">
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
							</div>
							<div class="modal-footer">
								<buttton class="btn btn-default" type="button" data-dismiss="modal">Close</buttton>
								<buttton class="btn btn-primary" type="button">Save changes</buttton>
							</div>
						</div>
					</div>
				</div>
			</div>
			<h4>js实现modal</h4>
			<div>
				<button class="btn btn-primary" id="mybtn" type="button">查看内容</button>
				<div class="modal fade" id="myModal5">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" data-dismiss="modal"><span>&times;</span></button>
								<h4 class="modal-title">中国人民解放军</h4>
							</div>
							<div class="modal-body">
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
								<p>世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了，世界人民从此站起来了。</p>
							</div>
							<div class="modal-footer">
								<buttton class="btn btn-default" type="button" data-dismiss="modal">Close</buttton>
								<buttton class="btn btn-primary" type="button">Save changes</buttton>
							</div>
						</div>
					</div>
				</div>
			</div>
			<h3 class="page-header">Dropdown</h3>
			<div>
				<div class="dropdown">
					<button class="btn btn-primary" data-toggle="dropdown">
						请选择
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="">1</a></li>
						<li><a href="">2</a></li>
						<li><a href="">3</a></li>
						<li><a href="">4</a></li>
						<li><a href="">5</a></li>
					</ul>
				</div>
			</div>
			<h3 class="page-header">ScrollSpy</h3>
			<div>
				<nav id="navbar_example">
					<div class="navbar navbar-default">
						<div class="navbar-header">
							<a href="" class="navbar-brand">Project Name</a>
						</div>
						<div>
							<ul class="nav navbar-nav">
								<li><a href="#fmz">@fmz</a></li>
								<li><a href="#cll">@cll</a></li>
								<li class="dropdown">
									<a href="" data-toggle="dropdown">
										请选择
										<span class="caret"></span>
									</a>
									<ul class="dropdown-menu">
										<li><a href="#one">one</a></li>
										<li><a href="#two">two</a></li>
										<li class="divider"></li>
										<li><a href="#three">three</a></li>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				</nav>
				<div data-spy="scroll" data-target="#navbar_example" style="position:relative ;">
					<h4 id="fmz">@fmz</h4>
					<p>Ad leggings keytar, brunch id art party dolor labore. Pitchfork yr enim lo-fi before they sold out qui. Tumblr farm-to-table bicycle rights whatever. Anim keffiyeh carles cardigan. Velit seitan mcsweeney's photo booth 3 wolf moon irure. Cosby sweater lomo jean shorts, williamsburg hoodie minim qui you probably haven't heard of them et cardigan trust fund culpa biodiesel wes anderson aesthetic. Nihil tattooed accusamus, cred irony biodiesel keffiyeh artisan ullamco consequat.</p>
					<h4 id="cll">@cll</h4>
					<p>Veniam marfa mustache skateboard, adipisicing fugiat velit pitchfork beard. Freegan beard aliqua cupidatat mcsweeney's vero. Cupidatat four loko nisi, ea helvetica nulla carles. Tattooed cosby sweater food truck, mcsweeney's quis non freegan vinyl. Lo-fi wes anderson +1 sartorial. Carles non aesthetic exercitation quis gentrify. Brooklyn adipisicing craft beer vice keytar deserunt.</p>
					<h4 id="one">@one</h4>
					<p>Occaecat commodo aliqua delectus. Fap craft beer deserunt skateboard ea. Lomo bicycle rights adipisicing banh mi, velit ea sunt next level locavore single-origin coffee in magna veniam. High life id vinyl, echo park consequat quis aliquip banh mi pitchfork. Vero VHS est adipisicing. Consectetur nisi DIY minim messenger bag. Cred ex in, sustainable delectus consectetur fanny pack iphone.</p>
					<h4 id="two">@two</h4>
					<p>In incididunt echo park, officia deserunt mcsweeney's proident master cleanse thundercats sapiente veniam. Excepteur VHS elit, proident shoreditch +1 biodiesel laborum craft beer. Single-origin coffee wayfarers irure four loko, cupidatat terry richardson master cleanse. Assumenda you probably haven't heard of them art party fanny pack, tattooed nulla cardigan tempor ad. Proident wolf nesciunt sartorial keffiyeh eu banh mi sustainable. Elit wolf voluptate, lo-fi ea portland before they sold out four loko. Locavore enim nostrud mlkshk brooklyn nesciunt.</p>
					<h4 id="three">@three</h4>
					<p>Ad leggings keytar, brunch id art party dolor labore. Pitchfork yr enim lo-fi before they sold out qui. Tumblr farm-to-table bicycle rights whatever. Anim keffiyeh carles cardigan. Velit seitan mcsweeney's photo booth 3 wolf moon irure. Cosby sweater lomo jean shorts, williamsburg hoodie minim qui you probably haven't heard of them et cardigan trust fund culpa biodiesel wes anderson aesthetic. Nihil tattooed accusamus, cred irony biodiesel keffiyeh artisan ullamco consequat.</p>
				</div>
			</div>
			<p class="bg-danger">注：上面模仿未成功</p>
			<h3 class="page-header">Tag</h3>
			<div>
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#home">Home</a></li>
					<li><a data-toggle="tab" href="#profile">Profile</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active fade in" id="home">
						<p>Ad leggings keytar, brunch id art party dolor labore. Pitchfork yr enim lo-fi before they sold out qui. Tumblr farm-to-table bicycle rights whatever. Anim keffiyeh carles cardigan. Velit seitan mcsweeney's photo booth 3 wolf moon irure. Cosby sweater lomo jean shorts, williamsburg hoodie minim qui you probably haven't heard of them et cardigan trust fund culpa biodiesel wes anderson aesthetic. Nihil tattooed accusamus, cred irony biodiesel keffiyeh artisan ullamco consequat.</p>
					</div>
					<div class="tab-pane fade in" id="profile">
						<p>In incididunt echo park, officia deserunt mcsweeney's proident master cleanse thundercats sapiente veniam. Excepteur VHS elit, proident shoreditch +1 biodiesel laborum craft beer. Single-origin coffee wayfarers irure four loko, cupidatat terry richardson master cleanse. Assumenda you probably haven't heard of them art party fanny pack, tattooed nulla cardigan tempor ad. Proident wolf nesciunt sartorial keffiyeh eu banh mi sustainable. Elit wolf voluptate, lo-fi ea portland before they sold out four loko. Locavore enim nostrud mlkshk brooklyn nesciunt.</p>
					</div>
				</div>
			</div>
			<h3 class="page-header">幻灯片播放图片</h3>
			<div>
				<div id="mycarousel" class="carousel slide" data-ride="carousel">
					<ol class="carousel-indicators">
						 <li data-target="#mycarousel" data-slide-to="0" class="active"></li>
						 <li data-target="#mycarousel" data-slide-to="1"></li>
						 <li data-target="#mycarousel" data-slide-to="2"></li>
					</ol>
					<div class="carousel-inner">
						<div class="item active">
							<img src="images/cll_life_hair.jpg" alt="">
							<div class="carousel-caption">first beautiful girl</div>
						</div>
						<div class="item">
							<img src="images/cll_life_hair2.jpg" alt="">
							<div class="carousel-caption">
								<h3>这是一个大美女</h3>
								<p>美女就会有美女的气质，当她站在人群当中的时候就会脱颖而出！</p>
							</div>
						</div>
						<div class="item">
							<img src="images/cll_life_station.jpg" alt="">
							<div class="carousel-caption">third beautiful girl</div>
						</div>
					</div>
					<a href="#mycarousel" data-slide="prev" class="left carousel-control">
						<span class="glyphicon glyphicon-chevron-left"></span>
					</a>
					<a href="#mycarousel" data-slide="next" class="right carousel-control">
						<span class="glyphicon glyphicon-chevron-right"></span>
					</a>
				</div>
			</div>
		</div>
	</body>
	<script>
		$('#mybtn').click(function(){
			$('#myModal5').modal() ;
		}) ;
	</script>
	</html>

[运行代码](/source_htmls/bootstrap_learn_js.html)
