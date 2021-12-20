---
layout: post
title: 'bug现场谜之古老的jar包classpath玄机'
subtitle: 'ps发现主机上只有一个jar -jar xxx.jar的进程，jar包打开发现没有lib依赖包，启动进程中也没有-classpath值，系统环境变量也没有，它是怎么加载依赖启动的？'
background: '/img/posts/bug-scene-old-jar-classpath-mystery.jpg'
comment: false
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 真相浮出水面](#3)
- [4. 总结](#4)

---

<h3 id="1">1. bug现场情况</h3>

现场从`Docker`上迁移一个应用到Linux主机。

使用命令`docker exec -it $CONTAINER /bin/sh`进入容器，`ps -ef`发现只有一个进程：`jar -jar xxx.jar`。将jar包解压缩，如图所示：

![](/img/posts/common-jar-structure.jpg)

和springboot的jar包结构不一样，这里面直接是class、配置文件及`META-INF`目录。

看了一下`env`环境变量，也没有`CLASSPATH`值。心里想着奇怪（那些依赖是怎么加载的呢？），但是也没有想明白咋回事，**暂且不管**。

把jar包迁移到Linux服务器上，尝试用`java -jar xxx.jar`启动，果然报错一大堆基础的类找不到。这时候突然想起在原docker容器jar包同目录中有一个`SYNC_lib`目录，该目录似乎包含了jar包依赖的第三方包。

将`SYNC_lib`目录也迁移到jar包同级目录上，指定classpath重新启动：`java -cp .:SYNC_lib/*: xxx.jar`。这时候相关的类都加载了，有一个报错是数据库的驱动不是最新的。将原驱动备份，复制一个新的驱动到`SYNC_lib`目录内：

```shell
mv SYNC_lib/Postgresql-old-version.jar SYNC_lib/Postgresql-old-version.jar.bak
cp /path/Postgresql-new-version.jar SYNC_lib/
```

自信满满地使用`java -cp .:SYNC_lib/*: xxx.jar`重新启动，报错：`ClassNotFoundException: org.postgresql.Driver not found`。

什么情况？明明新的驱动包已经在`classpath`里面了，为什么会找不到class呢？笔者还特意将新的驱动包解压缩确认是能够找到`org.postgresql.Driver`类的。

更奇怪的是切换到旧版本的驱动包就能够加载`org.postgresql.Driver`驱动类了（通过JVM参数`-verbose`能在日志中打印出加载的详细类和对应的jar包）。

> Docker学习参考[https://fengmengzhao.github.io/2021/06/25/docker-handbook-2021.html](https://fengmengzhao.github.io/2021/06/25/docker-handbook-2021.html)。

<h3 id="2">2. 尝试破案</h3>

总结一下案发现场疑点：

- **问题1**：容器内的java进程用`java -jar xxx.jar`启动，命令行和`CLASSPATH`环境变量都没有指定`SYNC_lib`路径，该JVM实例是怎么加载这些第三方`jar`包的？
- **问题2**：迁移后指定`classpath`启动jar包，只是替换了`classpath`下jar包的版本，竟然报错`ClassNotFoundException`？

没思路了，只能手动写个代码看看从指定的`classpath`下能不能加载对应的class：

```java
import java.security.*;

public class FindClass {

    public static void main(String args[]) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.uxun.uxunplat.util.OperateResult");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        CodeSource cs = clazz.getProtectionDomain().getCodeSource();
        String location = cs.getLocation().getPath();
        System.out.println(location);
    }
}
```

结果：能成功加载`org.postgresql.Driver`，说明新的驱动jar包是没问题的，命令行参数`-classpath`（或者`-cp`）的设定方法也是正确的。

百思不得其解......

<h3 id="3">3. 真相浮出水面</h3>

突然，灵机一动，可以不使用jar包启动，而是**直接启动包含`main()`的class类**，说不定是jar包在作妖。

`main()`方法启动：

```shell
#这里在-cp中增加xxx.jar
#也就是将之前-jar启动的应用加入到classpath中
java -cp .:SYNC_lib/*:xxx.jar: com.xxx.xxx
```

应用成功启动了！

果不其然，是jar包在作妖。此时笔者再次打开`jar`，这次没有忽略任何细节，打开`META-INF\MAINFEST.MF`文件，如图：

![](/img/posts/common-jar-MANIFEST.jpg)

原来玄机在这里，捶胸顿足，悔之晚矣！

`META-INF\MAINFEST.MF`文件是jar包的元数据文件，该文件指明了：

1. `Main-Class`：该jar包的入口类（包含`main`方法的类）。
2. `Class-Path`：依赖jar包的classpath路径。jar包路径之间使用空格分隔。

破案：

- **问题1**：容器内的jar包没设置`classpath`也能够加载第三方依赖，不是玄学，而是`classpath`在jar包内指定了。
- **问题2**：替换新的驱动包报错`ClassNotFoundException`，是因为在`MAINFEST.MF`文件中定义的`classpath`会覆盖掉命令行中指定的`-classpath`参数设置。也就是说命令行中正确指定的`-classpath`实际上并没有生效（不是参数设定错误，而是参数被覆盖了）。

实际上，回到"刀耕火种"的时代，在没有构建工具（例如`ant`、`maven`等）时，构建一个有第三方依赖的java程序，可以使用命令：

```shell
#参数c表示创建jar归档文件
#参数v表示打印详细日志
#参数f表示指定jar的名称，这里是xxx.jar
#参数m表示指定元数据信息文件，这里是MAINFEST.txt
java cvfm xxx.jar MAINFEST.txt com.xxx.xxx
```

关于`jar`打包和`MAINFEST.MF`更多内容参考：[https://docs.oracle.com/javase/tutorial/deployment/jar/downman.html](https://docs.oracle.com/javase/tutorial/deployment/jar/downman.html)。

现代开发java程序用IDE集成开发环境，不需要手动敲命令。例如，用IDEA导出一个jar包：

1). 在项目中增加一个`Artifacts`。

![](/img/posts/IDEA-export-jar-1.jpg)

![](/img/posts/IDEA-export-jar-2.jpg)

2). 执行构建，导出jar文件。

![](/img/posts/IDEA-export-jar-3.jpg)

![](/img/posts/IDEA-export-jar-4.jpg)

> 即使现代程序开发用IDE方便了很多，基础知识（如jar包中`MAINFEST`到底是什么？有什么作用？）的掌握有利于对编程体系的理解。

<h3 id="4">4. 总结</h3>

- 不要浅尝辄止看一个有疑问的点。如果你跳过这个点，可能你就偏离了找到bug的方向，再回到正确的方向上会更费劲。本例中没有深入思考为什么在没有指定classpath的情况下，`java -jar xxx.jar`能够正常运行；也没有打开jar包时顺便看看`MAINFEST.MF`文件内容。如果这两个任意一个做了，在前30%时间内就能破案。
- 有时候看到的现象不只是一个bug引起的，做好控制变量尝试，准确定位造成异常的原因。避免一锅粥，乱尝试，最后身心疲惫，脑子就不清晰了。本例中迁移后连接的库是高版本的Postgresql库，用低版本的驱动会报错。升级高版本后，报错`ClassNotFoundException`，要确信不是高版本驱动不可用，而是依赖jar包加载有问题，这时候千万不能跑偏。
- 基于认知，把确定能推出来的结论找出来。本例中替换驱动jar包后，报`ClassNotFoundException`，实际上可以认定命令行参数没有最终起作用（本例被jar包内MANIFEST文件覆盖了）。当然了，认知可能会有盲区，多一步验证，如果发现认知盲区，要搞明白关联知识。
