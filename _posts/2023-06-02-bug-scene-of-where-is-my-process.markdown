---
layout: post
title: 'bug现场谜之进程为什么消失了'
subtitle: '谜之bug现场：进程好好的不见了，找不到他杀的任何证据，总不能是自杀吧。复活它之后某一个时刻消失，再复活再消失...'
background: '/img/posts/bug-scene-of-disappeared.jpg'
comment: false
weixinurl: 'https://mp.weixin.qq.com/s/zm-Sw2vpK_t4jViQ6syoGQ'
---

# 目录

- [1. bug现场情况](#1)
- [2. 尝试破案](#2)
- [3. 真相浮出水面](#3)
- [4. 总结](#4)
- [更新记录](#99)
- [相关文章推荐](#100)

---

<h3 id="1">1. bug现场情况</h3>

2021年11月，客户现场某业务系统挂掉，运维发现系统的进程没了，看日志也没有任何报错信息。

运维重启服务，系统运行正常，第二天或者隔几天又挂掉...

值得注意的现象：周五重启的服务，周末基本没人用服务，系统周末正常运行，周一上班，大家一用系统就又挂了。

<h3 id="2">2. 尝试破案</h3>

因为没有任何错误日志输出，首先想到的是由于操作系统资源限制导致的，比如内存、CPU、socket连接数、打开文件数等。如果是这样，操作系统级别会杀死进程并记录相关日志。

**1. 查看系统级别的日志**

查看系统日志：`cat /var/log/messages |grep -i 'kill'`

查看内核级别的日志：`dmesg |grep -i 'kill'`

没有查到任何java、OOM、或者kill相关的系统级记录。

**2. 查看JVM crash日志**

系统级别的日志没有，可能是JVM本身bug造成的crash，这样JVM在crash的时候会生成`hs_err_pid_%p.log`，该文件默认生成在JVM运行的工作目录中，也可以在JVM启动的时候指定路径：`-XX:ErrorFile=/var/log/hs_err_pid_%p.log`

工作目录中没有找到JVM crash日志。

**3. 查看OOM Heap Dump日志**

系统启动的时候如果设定了JVM参数`-XX:+HeapDumpOnOutOfMemoryError`和`-XX:HeapDumpPath=*/java.hprof`并且是由OOM导致的crash，可以在对应的路径上找到heap dump文件，使用`jvisualvm`这种工具分析dump文件也可以定位问题。

该系统有设定对应的JVM参数，对应路径上没有dump日志输出。

什么日志都没有，会不会是什么问题导致日志没有生成呢？因为周一客户一旦使用就发生宕机，就怀疑是内存问题，监控一下内存吧：

**4. 监控OS进程内存、JVM堆内存**

使用`top`命令监控OS进程内存脚本：

```shell
#!/bin/bash

while true
do
  datetime=$(date '+%Y-%m-%d %H:%M:%s')
  echo "$datetime" >> record_new3.txt 2>&1
  top -d 1 -b -n1 |grep $PID >> record_new3.txt 2>&1
  sleep 60
done
```

使用`jmap`命令监控JVM堆内存：

```shell
#!/bin/bash

while true
do
  datetime=$(date '+%Y-%m-%d %H:%M:%s')
  echo "$datetime" >> record_jmap_new3.txt 2>&1
  /home/jdk8u282-b08/bin/jmap -heap $PID >> record_jmap_new3.txt 2>&1
  sleep 120
done
```

分析日志发现：

JVM堆内存（通过`jmap`命令脚本监控得出）从3G到接近14G（JVM设置的是最大堆内存16G），然后JVM会进行GC，内存就会降下来，如此往复。没有出现堆内存溢出情况。

JAVA进程的内存（通过`top`命令脚本监控得出）不断增大，最后维持在16G左右（系统内存是256G，还非常充裕）。

> 当JVM进行GC的时候JVM堆内存是回收了一部分，但是对于分配给操作系统JAVA进程的内存不会回收。在JVM堆中GC时，释放的内存只是标记内存空间可用，所以这也是为什么系统级别JAVA进程内存一直增大，最后维持一个较大的值不变（这种是堆外内存正常的情况，有另外一种情况堆外内存持续增加最终导致内存过大，进程被OS杀死，这样的情况应该会有一些其他原因造成堆外内存异常增长，就要想办法找出导致堆外内存异常增长的原因）。

本宕机系统的监控显示：内存确实没有问题：堆内存增加到一定程度后JVM进行GC，堆内存会降下来，系统进程内存最后处在一个稳定的值。

至此，笔者已经尽了最大的努力，没有找到实际的问题，只能写一个重启脚本，让宕机的系统在5分钟内重启：

**宕机后，5分钟重启服务**

```shell
#!/bin/bash

while true
do

log_date=$(date '+%Y-%m-%d')
sync_date=$(date '+%Y-%m-%d %H:%M:%s')
pid=`/usr/sbin/lsof -t -i tcp:7001`
logfile="/home/TAS/TAS2810/logs/restart-$log_date.log"

if [ ! -z "$pid" ];then
  echo "${sync_date}::PID为：$pid，XX系统服务启动中..." >> "$logfile"
else
  echo "${sync_date}::XX系统服务不存在，重新启动..." >> "$logfile"
  cd /home/TAS/TAS2810/bin
  nohup bash /home/TAS/TAS2810/bin/StartTAS.sh &
  echo "${sync_date}::重新启动完成!" >> "$logfile"
fi

sleep 300

done
```

<h3 id="3">3. 真相浮出水面</h3>

再总结下上面的情况：服务隔一段时间就挂一次，而且没有任何错误的业务日志和操作系统级别的crash日志，监控内存也很正常。

那到底是什么问题呢？进程肯定是死了，被谁杀了？他杀吗？操作系统日志显示没有杀死它，内存或者JVM层次crash自杀吗？也没有找到任何日志。

还能怎么排查呢？项目派来了一个该系统的架构师，稳了！

Linux有一个`strack`工具可以跟踪进程的信号，也就是说用这个工具，进程是怎么退出的能够监控出来：

```shell
#!/bin/sh

nohup strace -T -tt -e trace=all -p $(netstat -tnalp | grep 7001 | grep LISTEN | awk '{print $7}' | tr -d '/java')  > trace.log &
```

> 第二天系统宕机，通过`strack`输出日志发现是系统**自己退出**的，确实没有任何他杀和自杀。

另外架构师说很可能是Linux X Server调用的问题，本地环境复现一下，果然是这个问题。

具体是：在系统中有流程相关的功能，该功能会使用java的`awt`库调出来gui图形化界面，而gui的绘制是调用服务端启动环境的`X DISPLAY Server`，当服务端启动shell窗口关闭后，客户再点击流程功能，服务端不能找到`X DISPLAY Server`环境，系统就自己退出了。

> 这里应该是程序日志处理上有一些问题，`awt`库找不到`X DISPLAY Server`环境应该会报错的，而日志上没有任何体现，这也是问题难以找到的原因。

> 需要了解`X DISPLAY Server`更多内容，可以参考文章：[SSH终端怎们正确打开Linux gui程序-Window System](https://fengmengzhao.github.io/2021/12/07/how-ssh-terminal-open-linux-gui-program.html)

怎样修改呢？需要添加JVM参数：`-Djava.awt.headless=true`，该参数的含义是告诉JVM，该运行环境没有相关显示屏、鼠标、键盘等硬件，可以利用计算机后台的资源满足`awt`相关的调用（不是所有图形化的内容都需要显示服务的，比如在后台产生一些图片就不需要显示屏）。来看一下demo理解一下这个参数：


```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Calculator {
    static double num;
    public static void main(String[] args) {
        //System.setProperty("java.awt.headless", "true");
        System.setProperty("java.awt.headless", "false");
        System.out.println("是否是headless环境：" + java.awt.GraphicsEnvironment.isHeadless());
        System.out.println("java.awt.headless 默认值：" + System.getProperty("java.awt.headless"));
        // set up frame
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setTitle("Simple Calculator");
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up panel
        JPanel panel = new JPanel();
        // set layout to 5x2 grid layout
        panel.setLayout(new GridLayout(5, 2));

        // set up answer label
        JLabel answer = new JLabel();

        // set up number text fields
        JTextField num1 = new JTextField();
        JTextField num2 = new JTextField();

        // set up buttons
        JButton add = new JButton();
        add.setText("+");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    num = Double.parseDouble(num1.getText())
                    + Double.parseDouble(num2.getText());
                    answer.setText(Double.toString(num));
                } catch (Exception e) {
                    answer.setText("Error!");
                }
            }
        });
        JButton sub = new JButton();
        sub.setText("-");
        sub.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    num = Double.parseDouble(num1.getText())
                    - Double.parseDouble(num2.getText());
                    answer.setText(Double.toString(num));
                } catch (Exception e) {
                    answer.setText("Error!");
                }
            }
        });
        JButton mul = new JButton();
        mul.setText("*");
        mul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    num = Double.parseDouble(num1.getText())
                    * Double.parseDouble(num2.getText());
                    answer.setText(Double.toString(num));
                } catch (Exception e) {
                    answer.setText("Error!");
                }
            }
        });
        JButton div = new JButton();
        div.setText("/");
        div.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    num = Double.parseDouble(num1.getText())
                    / Double.parseDouble(num2.getText());
                    answer.setText(Double.toString(num));
                } catch (Exception e) {
                    answer.setText("Error!");
                }
            }
        });

        // add components to panel
        panel.add(new JLabel("Number 1"));
        panel.add(new JLabel("Number 2"));
        panel.add(num1);
        panel.add(num2);
        panel.add(add);
        panel.add(sub);
        panel.add(mul);
        panel.add(div);
        panel.add(new JLabel("Answer"));
        panel.add(answer);

        // add panel to frame and make it visible
        frame.add(panel);
        frame.setVisible(true);
    }
}
```

执行代码，如果是Oracle JDK1.8，默认是`java.awt.headless`是`false`，而openjdk默认值是`true`。上面的代码打开一个简单的gui的计算器，如果设置`java.awt.headless=true`，就是告诉JVM没有相关的显示服务，就会报错：

![](/img/posts/java_awt_headless-set-true-use-awt-get-gui-error.png)

> 为什么报错呢？awt要调出来gui程序，JVM参数headless的`true`设置告诉JVM运行环境没有显示服务，不支持gui程序。

上面代码设置`java.awt.headless=false`，执行输出：

![](/img/posts/java_awt_headless-set-false-use-awt-error-display-not-working.png)

这里报错信息是不能连接到启动环境中的`X DISPLAY Server`，本地环境中有安装`Microsoft VcXsrv X Server`，设置的display port为`3600`，因此在JVM启动的shell环境中设置`export DISPLAY=xx.26.18.37:3600`，重新执行：

![](/img/posts/java_awt_headless-set-false-use-awt-error-display-working.png)

那`java.awt.headless=true`什么场景时候用呢？比如，要生成图片，没有用到显示服务，但是用`awt`库，下面demo所示：

```java
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TestCHSGraphic {

    public static void main (String[] args) throws Exception {
        // 设置Headless模式
        //System.setProperty("java.awt.headless", "true");
        //System.setProperty("java.awt.headless", "false");
        System.out.println("是否是headless环境：" + java.awt.GraphicsEnvironment.isHeadless());
        System.out.println("java.awt.headless 默认值：" + System.getProperty("java.awt.headless"));

        BufferedImage bi = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);

        Graphics g = bi.getGraphics();
        g.drawString(new String("Headless Test".getBytes(), "utf-8"), 50, 50);

        ImageIO.write(bi, "jpeg", new File("test.jpg"));
    }

}
```

> 这里如果`java.awt.headless`设置为`false`，并且在JVM的运行环境中没有`X DISPLAY Server`，就会出现和上面一样找不到`X DISPLAY Server`的报错。

<h3 id="4">4. 总结</h3>

1. 不要先入为主，认为程序一定是被自杀或者他杀的。事实表明程序是自己退出的。
2. 日志是解决问题的最佳突破口，如果有日志就从日志出发。没有日志也能说明一些问题，比如本例中要尽早排除掉内存或者JVM crash导致问题的排查方向。
3. 不能完全依赖日志，代码有时候处理不当会把日志吃掉。尝试复现问题能够找出突破口。
4. 有些问题找不出原因可能是知识的盲区，多了解相关支持能帮助排查问题。
5. 问题排查要一点点缩小排查范围，一定不能想当然，要像教孩子一样亲身实践，一点点排除。很多情况由于自己想当然，很小的点疏忽了，会浪费大量时间。

<h3 id="99">更新记录</h3>

- 2022-01-07 12:15 [掘金专栏](https://juejin.cn/column/7049663804136751140)发表前重读、优化、勘误

<h3 id="100">相关文章推荐</h3>

- [SSH终端怎们正确打开Linux gui程序-Window System](https://fengmengzhao.github.io/2021/12/07/how-ssh-terminal-open-linux-gui-program.html)

---
