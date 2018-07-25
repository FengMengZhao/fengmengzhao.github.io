---
layout: post
title: 'I/O那些事'
subtitle: 'I/O总是披着一层神秘的面纱，袅袅婷婷，令人心存畏惧，不敢靠近。我们来揭开这层面纱，探一探I/O的那些事！'
background: '/img/posts/something-about-io.jpg'
comment: true
---

## 目录

- [1. IO](#1)
- [2. NIO](#2)
- [3. IO杂记](#3)
    - [3.1 字节流(Buffered or Not) vs 字节缓冲流(Buffered or Not)COPY文件比较](#3.1)
    - [3.2 字节流转字符流编码问题及Windows常见控制台乱码解释](#3.2)

---

<h3 id="3">3. IO杂记</h3>

<h4 id="3.1">字节流(Buffered or Not) vs 字节缓冲流(Buffered or Not)COPY文件比较</h4>

> 当我们读取二进制文件的时候会优先选择字节流，有以下几种方式：<br>
1). 一个字节一个字节的读取(原始流);
2). 构造一个缓冲区，输入流读取到缓冲区中，再讲缓冲区中的数据写入到输出流中(缓冲区 + 原始流);
3). 使用装饰的缓冲流，一个字节一个字节的读取(缓冲流);
4). 构造一个缓冲区，使用装饰的缓冲流，在缓冲区中读和写(缓冲区 + 缓冲流).<br><br>
下面分别用四种方法读取`100M`文件：

    package com.fmz.io;

    import java.io.*;

    public class UseStreamCopyFile{

        /* 缓冲区 + 原始流 */
        private static void copyFileUsingStreamWithBuffer(File source, File dest){
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(source);
                os = new FileOutputStream(dest);
                byte[] buffer = new byte[1024 * 8];//缓冲区
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                
                }
            
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                try{
                    is.close();
                    os.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            
            }

        }

        /* 原始流 */
        private static void copyFileUsingStreamWithoutBuffer(File source, File dest) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(source);
                os = new FileOutputStream(dest);
                int c;

                while ((c = is.read()) != -1) {
                    os.write(c);
                }
            
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                try{
                    is.close();
                    os.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }

        /* 缓冲流 */
        private static void copyFileUsingBufferedStream(File source, File dest) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new BufferedInputStream(new FileInputStream(source));
                os = new BufferedOutputStream(new FileOutputStream(dest));
                int c;

                while ((c = is.read()) != -1) {
                    os.write(c);
                }
            
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                try{
                    is.close();
                    os.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }

        /* 缓冲区 + 缓冲流 */
        private static void copyFileUsingBufferedStreamWithBuffer(File source, File dest) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new BufferedInputStream(new FileInputStream(source));
                os = new BufferedOutputStream(new FileOutputStream(dest));
                
                byte[] buf = new byte[1024 * 8];
                int len;
                while ((len = is.read(buf)) > 0) {
                    os.write(buf, 0, len);
                }
            
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                try{
                    is.close();
                    os.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }



        public static void main(String args[]) throws Exception{
            String originFile = "fileDemo.txt";
            long start = System.nanoTime();

            copyFileUsingStreamWithoutBuffer(new File(originFile), new File("test1.txt"));
            long elapsedTime = System.nanoTime() - start;
            System.out.println("无Buffer用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingStreamWithBuffer(new File(originFile), new File("test2.txt"));
            elapsedTime = System.nanoTime() - start;
            //long elapsedTime = System.nanoTime() - start;
            System.out.println("有Buffer用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingBufferedStream(new File(originFile), new File("test3.txt"));
            elapsedTime = System.nanoTime() - start;
            //long elapsedTime = System.nanoTime() - start;
            System.out.println("Buffered Stream用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingBufferedStream(new File(originFile), new File("test4.txt"));
            elapsedTime = System.nanoTime() - start;
            //long elapsedTime = System.nanoTime() - start;
            System.out.println("Buffered Stream使用Buffer用时：" + elapsedTime);

        }
    }/*output:
        无Buffer用时：27,9574,234,330
        有Buffer用时：152,272,750
        Buffered Stream用时：1,706,302,578
        Buffered Stream使用Buffer用时：585,076,498
    */

> `UseStreamCopyFile`从上述输出结果中可以看出来：当使用字节流COPY文件时，使用`缓冲区 + 原生流`的方式用时最少。<br>
究其原因：`FileInputStream|FileOutputStream`的`read(byte[] buf)|write(byte[] buf, int off, int len)`都是`native`方法，读起来较快！

---

<h4 id="3.2">字节流转字符流编码问题及Windows常见控制台乱码解释</h4>

> 假设我们在Windows机器上使用字符流(`Reader`)读取一个UTF-8格式的文件，打印输出在Windows控制台上，会出现什么样的状况呢？

    package com.fmz.io;

    import java.io.*;

    public class TestFileReader {

        /*验证字节流转化为字符流*/
        public static void main(String args[]){

            Reader r = null;
            try{
                r = new FileReader("TestFileReader.java");//字符流输入流
                int c;
                while((c = r.read()) != -1){
                    System.out.print((char)c);
                    //new PrintStream(System.out, true, "UTF8").print((char)c);
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    r.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

> `TestFileReader`读取`TestFileReader.java`文件，在控制台打印字符，输出结果为：

![字符流读取文件打印到控制台1](/img/posts/filereader-output1.png "字符流读取文件打印到控制台1")

> 从图中可以看出：打印到控制台上的汉字乱码了。这是为什么呢？<br><br>
原因是：字符流都是从字节流转化而来的(`InputStreamReader|OutputStreamWriter`)，从字符到字节涉及到一个`编码过程`，从字节到字符涉及到一个`解码过程`。程序中的`FileReader`实际上是使用Windows操作系统默认的编码格式(GBK)，而`new FileReader(String filename)`相当于`new InputStreamReader(new FileInputStream(String filename), Charset.DEFAULT_CHARSET)`，因此UTF8的文件用GBK编码来解码，打印到控制台的字符就是乱码的了。

为了解决上述问题，可以将程序改为：

    package com.fmz.io;

    import java.io.*;

    public class TestFileReader {

        /*验证字节流转化为字符流*/
        public static void main(String args[]){

            Reader r = null;
            try{
                r = new InputStreamReader(new FileInputStream("TestFileReader.java"), "UTF8");
                int c;
                while((c = r.read()) != -1){
                    System.out.print((char)c);
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    r.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

> 这样打印输出的汉字就不会乱码了。

下面来看另外一个问题：

> 这个程序如果我们是在Windows CMD上执行是不会出现乱码的，但是如果在Windows的Git Bash(编码为UTF8)又会出现乱码，这是什么原因呢？<br>
看一看再Git Bash上的输出：

![字符流读取文件打印到控制台2](/img/posts/filereader-output2.png "字符流读取文件打印到控制台2")

> 问题一看就知道出在哪里了: 因为CMD的编码和Git Bash不同。具体来说：<br><br>
1. `System.out.print((char)c)`是先根据操作系统默认的编码将字符转化为字节，而后Windows CMD或者Git Bash又将这些字节转化为字符打印处理；
2. Windows CMD的编码和操作系统默认相同，而Git Bash编码和操作系统默认不同，所以用一种编码(GBK)来解码后用另一种编码(UTF8)来编码就造成了乱码。<br><br>
解决方法为：

    package com.fmz.io;

    import java.io.*;

    public class TestFileReader {

        /*验证字节流转化为字符流*/
        public static void main(String args[]){

            Reader r = null;
            try{
                r = new InputStreamReader(new FileInputStream("TestFileReader.java"), "UTF8");
                int c;
                while((c = r.read()) != -1){
                    new PrintStream(System.out, true, "UTF8").print((char)c);
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    r.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

> 这样就不会出现乱码了。这也是为什么在Git Bash上用Java编码执行命令时为什么总是出现乱码的原因。<br><br>
记住：有人的地方就有江湖，有IO的地方就有乱码。
