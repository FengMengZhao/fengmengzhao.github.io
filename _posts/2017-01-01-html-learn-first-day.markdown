---
layout: post
title: html学习之FirstDay
---

### html一般标记

1. `<h#></h#>`表示标题
2. `<p></p>`表示段落
3. `<br>`表示强制换行
4. `<--!注释-->`表示给代码添加注释
5. `<b></b>`表示粗体
6. `<i></i>`表示斜体
7. `<u></u>`表示下划线
8. `<s></s>`表示删除线
9. `<sup></sup>`表示上标
10. `<sub></sub>`表示下标

### html字体标记

1. `<font size="num(1-7)"></font>`表示字体的大小
2. `<font color=""></font>`表示字体的颜色。1和2可以结合使用，`<font size="" color=""></font>`
3. `<code></code>`表示代码块

### html文字布局

1. `<nobr></nobr>`表示不会换行
2. `align="#"`表示文字的对齐(left right center )
3. `<center></center>`表示居中对齐
4. `<ul> <li>列表1</li> <li>列表2</li> </ul>`表示无序列表
5. `<ol type="#"><li>列表1</li><li>列表2</li></ol>`表示有序列表
6. `<pre></pre>`表示格式化文本，可以用来显示源代码

### html图像

1. `<img src="#" alt="" width="" height="" align="" border="">`表示插入图像，alt表示提示信息，width表示宽度，height表示高度，align表示图像与文本的对齐，border表示图像的边框
2. `<a href=""></a>`表示链接，href为引用的链接地址

### html超级链接

1. `<a href="" target="" title=""`target属性表示链接的打开方式，_blank: 在新的浏览器中打开;_parent: 在父窗口中打开;_self: 在同一框架下打开;_top: 在当前整个浏览器窗口中打开，title表示注释
2. `<a name=""></a>`表示命名锚;`<a href="# "></a>`表示指定这个命名锚
3. `<a href="mailto:#"></a>`表示邮箱链接


### 相对路径与绝对路径

1. `../`表示上级目录;`../../`表示上上级目录，以此类推
2. `<a href:"index.html">index.html</a>`表示源文件和引用文件（index.html）在同一个文件夹下面
3. `<a href:"../index.html">index.html</a>`表示引用文件位于源文件的上级目录
4. `<a href:"#/index.html">index.html</a>`表示引用文件位于源文件的上级目录#中

> - 相对路径是这个文件所在的位置与其他文件或者文件夹的关系
- 绝对路径是指带域名文件的完整路
