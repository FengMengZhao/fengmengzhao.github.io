---
layout: post
title: 'I/O那些事'
subtitle: 'I/O总是披着一层神秘的面纱，袅袅婷婷，令人心存畏惧，不敢靠近。我们来揭开这层面纱，探一探I/O的那些事！'
background: '/img/posts/something-about-io.jpg'
comment: true
---

## 目录

- [1. Blocking I/O VS Non-Blocking I/O](#1)
    - [1.1 Blocking I/O](#1.1)
    - [1.2 Non-Blocking I/O](#1.2)
- [2. IO杂记](#2)
    - [2.1 字节流(Buffered or Not) vs 字节缓冲流(Buffered or Not)COPY文件比较](#2.1)
    - [2.2 字节流 vs 字符流COPY文件比较](#2.2)
    - [2.3 字节流转字符流编码问题及Windows常见控制台乱码解释](#2.3)

---

<h3 id="1">1. Blocking I/O VS Non-Blocking I/O</h3>

在客户端-服务器端的应用程序中，每当客户端向服务器端请求信息，服务器端会处理请求然后返回处理结果。在这个过程中，客户端和服务器端都需要向对方建立一个连接，这个时候所谓的`Socket`就派上用场了。客户端和服务器端需要将自己绑定到一个连接末端的`Socket`上，并且服务器端监听一个客户端发送连接的`Socket`。

![客户端-服务器端Socket连接](/img/posts/client-server-socket.png "客户端-服务器端Socket连接")

当客户端和服务器端建立起来连接之后，双方从连接末端(Endpoint)绑定的`Socket`中读取和发送数据。

<h4 id="1.1">Blocking I/O</h4>

在Blocking I/O中，客户端发送一个请求，服务器端启动一个线程来处理这个请求，如果线程读取没有可读取的数据或者线程写入而写入没有全部完成时，这个线程就会处于`Blocking`状态，直到相关的读操作有可读数据或者写操作全部写入之后，线程才能做其他事情。如果是这种模式来实现并发请求，服务器端需要为每个客户端连接申请一个线程。通过代码，我们看看具体是怎么工作的：

`ServerSocket serverSocket = new ServerSocket(portNum);`

![创建服务器端监听Socket](/img/posts/create-server-socket.png "创建服务器端监听Socket")

> 1). 服务器端建立一个监听`ServerSocket`，该`Socket`绑定指定的监听端口。

`Socket clientSocket = ServerSocket.accept();`

![服务器端接受连接并分配新的Socket](/img/posts/server-socket-accept-connection.png "服务器端接受连接并分配新的Socket")

![新的连接建立成功后重新监听](/img/posts/new-connection-establish.png "新的连接建立成功后重新监听")

> 2). 服务器端Socket执行accept方法，该方法为blocking方法(除非有相应的事件发生，否者线程会阻塞在这里)，这时候服务器开始等待客户端的连接请求，如果请求到达，服务器会接受这个请求，并且返回一个新的`Socket`来和客户端Socket进行通信。如果新的连接建立成功，服务器端`ServerSocket#accpet()`方法就会返回，继续监听新的连接。

```
BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
```

> 3). 从服务器端连接的`Socket`中获取输入流和输出流。

```
String request, response;//请求信息和响应信息
while ((request = in.readLine()) != null) {//获取客户单请求信息
    response = processRequest(request);//处理请求信息并返回响应信息
    out.println(response);//写回客户端响应信息
    if ("Done".equals(request)) {
        break;
    }
}
```

> 4). 服务器端从连接末端的Socket中获取输入流和输入流和输出流。从输入流中读取数据，处理数据，然后写会到输出流中。

需要注意的是：上述的过程只针对客户单对服务器端的一次连接，如果客户单并发请求服务器，我们需要为每一个客户端创建一个线程来处理请求；

```
while (listening) {
    accept a connection;
    create a thread to deal with the client;
}
```

![Blocking I/O客户端服务器端建立连接过程](/img/posts/blocking-io-client-server-connection-process.png "Blocking I/O客户端服务器端建立连接过程")

这种Blocking I/O实现客户端-服务器端通信的方法存在的缺点：

- 每一个客户端对应一个线程，每一个线程需要分配一定的内存空间，当线程量大时，线程之间的切换影响性能。
- 可能有大量的线程只是为了客户端的请求而处于阻塞状态，这些线程浪费了很多的资源。

所以，Blocking I/O实现方式的client-server应用不适合高并发的场景。还好Java NIO提供了另外的可能性。

<h4 id="1.2">Non-Blocking I/O</h4>



---

<h3 id="2">2. IO杂记</h3>

<h4 id="2.1">字节流(Buffered or Not) vs 字节缓冲流(Buffered or Not)COPY文件比较</h4>

> 当我们读取二进制文件的时候会优先选择字节流，有以下几种方式：<br>
1). 一个字节一个字节的读取(原始流);<br>
2). 构造一个缓冲区，输入流读取到缓冲区中，再将缓冲区中的数据写入到输出流中(缓冲区 + 原始流);<br>
3). 使用装饰的缓冲流，一个字节一个字节的读取(缓冲流);<br>
4). 构造一个缓冲区，使用装饰的缓冲流，在缓冲区中读和写(缓冲区 + 缓冲流).<br><br>
下面分别用四种方法读取`100M`文件：

    package com.fmz.io;

    import java.io.*;

    public class UseStreamCopyFile{

        /* 缓冲区 + 原始字节流 */
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

        /* 原始字节流 */
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

        /* Buffer字节流 */
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

        /* 缓冲区 + Buffer字节流 */
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
            String originFile = "fileDemo.txt";//100M Text File
            long start = System.nanoTime();

            copyFileUsingStreamWithoutBuffer(new File(originFile), new File("test1.txt"));
            long elapsedTime = System.nanoTime() - start;
            System.out.println("【原始字节流】用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingStreamWithBuffer(new File(originFile), new File("test2.txt"));
            elapsedTime = System.nanoTime() - start;
            System.out.println("【缓冲区 + 原始字节流】用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingBufferedStream(new File(originFile), new File("test3.txt"));
            elapsedTime = System.nanoTime() - start;
            System.out.println("【Buffer字节流】用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingBufferedStreamWithBuffer(new File(originFile), new File("test4.txt"));
            elapsedTime = System.nanoTime() - start;
            //long elapsedTime = System.nanoTime() - start;
            System.out.println("【缓冲区 + Buffer字节流】用时：" + elapsedTime);

        }
    }/*output:
        【原始字节流】用时：306087832761
        【缓冲区 + 原始字节流】用时：125783149
        【Buffer字节流】用时：3201044157
        【缓冲区 + Buffer字节流】用时：125480215
    */

> `UseStreamCopyFile`从上述输出结果中可以看出来：当使用字节流COPY文件时，使用`缓冲区 + 原生流`的方式用时最少。<br>
究其原因：`FileInputStream|FileOutputStream`的`read(byte[] buf)|write(byte[] buf, int off, int len)`都是`native`方法，读起来较快！

---

<h4 id="2.2">字节流 vs 字符流COPY文件比较</h4>

> 我们知道当使用字节流COPY文件时，最有效率的是使用`缓冲区 + 原生流(字节流)`的方式进行。如果们的文件是Text文档(非二进制文件)，使用字符流读取文件会更快吗？或者我们为什么要使用字符流？<br><br>
看一看具体的程序：

    package com.fmz.io;

    import java.io.*; 

    public class ByteVsCharacterStreamCopyFile {

        /* 缓冲区 + 原始字节流 */
        private static void copyFileUsingByteStream(File source, File dest){
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

        /* 缓冲区 + 原始字符流 */
        private static void copyFileUsingCharacterStream(File source, File dest){
            Reader reader = null;
            Writer writer = null;
            try {
                reader = new FileReader(source);
                writer = new FileWriter(dest);
                char[] buffer = new char[1024 * 8];//缓冲区
                int length;
                while ((length = reader.read(buffer)) > 0) {
                    writer.write(buffer, 0, length);
                
                }
            
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                try{
                    reader.close();
                    writer.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            
            }

        }

        /* 原始字符流 */
        private static void copyFileUsingCharacterStreamWithoutBuffer(File source, File dest) {
            Reader reader = null;
            Writer writer = null;
            try {
                reader = new FileReader(source);
                writer = new FileWriter(dest);
                int c;

                while ((c = reader.read()) != -1) {
                    writer.write(c);
                }
            
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                try{
                    reader.close();
                    writer.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }

        /* 缓冲字符流 */
        private static void copyFileUsingBufferedCharacterStream(File source, File dest) {
            Reader reader = null;
            Writer writer = null;
            try {
                reader = new BufferedReader(new FileReader(source));
                writer = new BufferedWriter(new FileWriter(dest));
                int c;

                while ((c = reader.read()) != -1) {
                    writer.write(c);
                }
            
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                try{
                    reader.close();
                    writer.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }

        public static void main(String args[]) throws Exception{
            String originFile = "fileDemo.txt";//100M Text File
            long start = System.nanoTime();

            copyFileUsingByteStream(new File(originFile), new File("test1.txt"));
            elapsedTime = System.nanoTime() - start;
            System.out.println("【缓冲区 + 原始字节流】用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingCharacterStream(new File(originFile), new File("test2.txt"));
            elapsedTime = System.nanoTime() - start;
            System.out.println("【缓冲区 + 原始字符流】用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingCharacterStreamWithoutBuffer(new File(originFile), new File("test3.txt"));
            elapsedTime = System.nanoTime() - start;
            System.out.println("【原始字符流】用时：" + elapsedTime);

            start = System.nanoTime();
            copyFileUsingBufferedCharacterStream(new File(originFile), new File("test4.txt"));
            elapsedTime = System.nanoTime() - start;
            System.out.println("【缓冲字符流】用时：" + elapsedTime);
        }
    }/*
        【缓冲区 + 原始字节流】用时：129137192
        【缓冲区 + 原始字符流】用时：816083110
        【原始字符流】用时：5057263196
        【缓冲字符流】用时：1823119012
    */

> `ByteVsCharacterStreamCopyFile`从输出结果中可以看出：COPY`100M`文件，使用`缓冲区 + 原始字节流`的方式用时最少。<br><br>
也就是针对COPY文件来说，不论文件是二级制文件还是文本文件，使用字节流的方式读取都是最有效率的(不包含NIO方式)。<br><br>
我们之所以使用字符流的原因是：我们将字节流转化为字符流，然后需要从字符流中读取字符并进行处理(比如说readLine()等)。<br><br>
所以，选择使用字节流与字符流时：二级制文件只能使用字节流；文本文件如果需要对字符进行使用，用字符流；文本文件如果只是COPY用字节流更有效率。

---

<h4 id="2.3">字节流转字符流编码问题及Windows常见控制台乱码解释</h4>

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
