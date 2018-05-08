---
layout: post
title: Java并发读取并入库large-size XML文件
---

> 项目中需要读取1G大小的XML文件，解析文件，将相应的字段存入数据库中<br><br>
解析XML可以通过`SAX`和`DOM`两种方式；`SAX`可以边读取边入库；`DOM`必须将整个文件加载到内存<br><br>
解析一个1G的XML文件很快，但是1G文件有数十万条记录，入库的过程相对于解析就很慢<br><br>
考虑的内存的问题，采用`SAX`的方式对文件进行解析<br><br>
考虑到边读取边入库，要创建不同的`解析文件线程`和`入库线程`<br><br>
考虑到入库的`I/O`花销时间要远远大于解析的时间，采用`1个解析文件线程`和`多个(32)入库线程`

**SAX**

The Simple API for XML(SAX)是一个基于回调函数(callback routines)或者事件处理器(event handler)的事件驱动型(event-based)API，可以用来解析XML的不同部分。使用SAX时，需要注册不同事件的handler，然后解析XML文件。

**程序大体思路：**

- 解析文件线程(`SourceProcessor.java`)
- 入库线程(`DatabaseWriter.java`)
- 线程协作类(`SaxProcessor.java`)
