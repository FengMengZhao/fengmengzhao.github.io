---
layout: post
title: 详解主动型(Active Mode)FTP和被动型(Passive Mode)FTP
subtitle: ftp的传统的连接模式是主动模式，为什么有了被动模式？主动模式和被动模式分别是怎么工作的？
background: '/img/posts/active-passive-ftp.png'
comment: false
weixinurl: 'https://mp.weixin.qq.com/s/GLI71hcMRS49kZ8NcSMbMw'
---

## 目录

- [1. 简介](#1)
- [2. 基本概念](#2)
- [3. Active Mode FTP](#3)
- [4. Passive Mode FTP](#4)
- [5. 总结](#5)
- [6. 问题汇总](#6)
    - [6.1 Apache FTP银河麒麟FT2000+ SM环境Apache FTPClient和WinSCP设置passive模式不能下载文件（待解决）](#6.1)
        - [6.1.1 主动模式连接报错“x.x.x.x is not the same as server y.y.y.y”](#6.1.1)
- [更新记录](#99)

<h3 id="1">1. 简介</h3>

当第一次接触FTP的时候，肯定会为FTP的不同连接模式而苦恼，`Active Mode`和`Passive Mode`到底有什么样的不同？具体到网络中的防火墙又要怎么配置才能保证网络安全的同时获取正确的FTP访问？本篇文章给出了详细的解释。

---

<h3 id="2">2. 基本概念</h3>

FTP服务仅仅基于TCP协议，实现不包括UDP协议部分。FTP工作时同时使用了两个端口：`数据(data)`端口和`命令(command)`端口(又称为control port)。传统的FTP实现(Active Mode)用`20`作为数据端口，`21`作为命令端口；而被动模式(Passive Mode)会随机启用本机的一个端口作为数据监听端口。当使用不同的Mode(Active或者Passive)连接FTP时，数据端口总是会发生改变，这就是令人迷惑的地方。

> 控制端口和数据端口都可以指定。

---

<h3 id="3">3. Active Mode FTP</h3>

主动模式(Active Mode)连接FTP时，客户端会在本地启用一个非预留端口`N`(N > 1023)，客户端从这个端口连接FTP Server端口`21`并发送FTP command `PORT N+1`到FTP Server，同时客户端会监听端口`N+1`。FTP Server将通过本地的`20`端口连接回客户端指定的监听端口`N+1`。

从服务端防火墙的角度，为了支持主动模式的FTP访问，如下策略是必须是打开的：

- 客户端任意大于1023端口到FTP Server的21端口(客户端初始化连接)
- FTP Server的21端口到客户端任意大于1023端口(服务回应客户端的命令端口)
- FTP Server的20端口到客户端任意大于1023端口(服务初始化数据连接(和发送数据)到客户端的数据端口)
- 客户端任意大于1023端口到FTP Server 20端口(客户端发送ACK到服务数据端口)

> 这里的`N`和`N+1`端口是示意端口，实际上并不一定满足`+1`的关系。

主动模式的网络图：

![activeftp](/img/posts/activeftp.png)

> 第一步：客户端的命令端口连接服务端的命令端口，并且发送命令`Port 1027`。<br><br>
第二步：服务发回ACK到客户端命令端口。<br><br>
第三步：服务端数据端口(默认为20端口)初始化数据连接到客户端数据端口(监听端口，端口号第一步中已经通过命令端口发送给服务端)。<br><br>
第四步：客户端数据端口发回ACK到服务端数据端口。

主动模式存在的问题：假设公网的网段为`210.33.55.xxx`，局域网共享了`210.33.55.2`这个IP，公网中有公共的ftp，当局域网中`192.168.200.2`用户访问公共ftp下载时，如果连接方式为主动模式就会出现问题。因为公网中的ftp需要向`210.33.55.2`这个路由的`192.168.200.2`中开启的数据端口发送数据，很显然路由`210.33.55.2`IP中找不到该数据端口。

> 那可能有的同学会有这样的疑问：我在家中用局域网(192.168.1.101)也能访问互联网啊，那说明公网IP的数据至少是能到达我的局域网IP(192.168.1.101)端口的，为什么这里就说公网IP找不到局域网中的数据端口？实际上，这里的关键是**谁**来初始化连接，我们在内网中访问互联网是**本地(局域网)的客户端**主动初始化互联网中某IP端口连接，一旦建立连接就可以进行双向通信。但是**互联网**不能主动初始化连接到局域网中某个监听端口，这也就是为什么主动模式访问部署在公网IP中的ftp不能连接成功，因为这种模式需要公网主动初始化连接到局域网数据监听端口。就像某个企业HR用内部电话打电话说让你去面试，电话通之后你们就能通讯了(局域网访问互联网通了之后，就能够互相通讯了)。但是，电话挂断之后你再打过去就是前台了(互联网访问局域网，只能访问到局域网共享的互联网出口)，找不到之前给你打电话的人(因为是互联网服务主动初始化连接到局域网)。这里你的电话就是公网，企业的内部电话就是局域网，企业内部电话能主动联系到你，而你不能主动联系到企业内部的某个座机(当然了找前台报分机号，前台会帮你找，但是对于局域网-互联网模型，公网IP是无法初始化连接到内网的)。

使用ftp客户端工具设置主动连接到FTP服务，这里FTP服务的地址是`ftp://xx.20.146.247:2121`，设置主动连接模式服务端数据端口为`2020`（这里ftp使用的是Apache FTP，平时经常用的还有Vsftpd，相关主动、被动连接配置参考[这里](https://fengmengzhao.github.io/2015/12/20/Linux-basic-learning.html#6.6)），FTP建立连接后服务端tcp连接如图：

![](/img/posts/ftp-active-transfer-data-serverside.png)

客户端tcp连接如图：

![](/img/posts/ftp-active-transfer-data-clientside.png)

这里我们可以看到服务端`2120`端口和客户端`60089`端口建立tcp连接，`60089`作为客户端数据监听端口，由服务端`2120`端口主动连接（这就是为啥称之为主动active模式）。

> 测试的时候可以传输一个大文件或者设置一下传输速率，这样能够有机会看到服务端和客户端tcp连接情况。Linux服务使用命令：`netstat -nalp |grep 2120`，Windows客户端使用命令：`netstat -nao |findstr 2120`

---

<h3 id="4">4. Passive Mode FTP</h3>

为了解决服务端要向客户端初始化数据连接的问题，被动模式应运而生。客户端可以通过`PASSIVE ON`告诉服务端使用的连接方式是被动模式。

在被动模式中，命令端口和数据端口的初始化连接都是由客户端主动发起的，这样就解决了客户端防火墙可能过滤掉服务端数据端口发来的连接。当打开FTP连接时，客户端在本地启用了两个端口(N>1023和N+1)。第一个端口连向服务端命令端口`21`，然后向服务端发出`Pasv`命令而不是像主动模式那样发送`PORT N+1`，这个`PSAV`命令可以让服务端随机开启一个非预留端口(P>1023)并且将这个端口号返回给客户端回应`PASV`命令。客户端就可以从端口`N+1`向端口`P`进行初始化连接来传输数据。

从服务端防火墙的角度，为了支持主动模式的FTP访问，如下策略是必须是打开的：

- 客户端任意大于1023端口到FTP Server的21端口(客户端初始化连接)
- FTP Server的21端口到客户端任意大于1023端口(服务回应客户端的命令端口)
- 客户端任意大于1023端口到FTP Server的任意大于1023端口(客户端初始化数据连接到服务端的数据端口)
- FTP Server的任意大于1023端口到客户端任意大于1023端口(服务端发送ACK(和数据)到服务数据端口)

被动模式的网络图：

![passiveftp](/img/posts/passiveftp.png)

> 第一步：客户端连接服务端`21`并且发出`PASV`命令。<br><br>
第二步：服务端开启2024端口并监听来自客户端的数据连接，同时回应第一步`PORT 2024`。<br><br>
第三步：客户端从本地数据端口初始化数据连接到服务端指定数据端口2024。<br><br>
第四部：服务端回应客户端`ACK`(和数据)。

虽然被动模式解决了很多服务端的问题，但是服务端也产生了一些问题。最大的问题就是要求客户端能够连接到服务端任意大于1023的端口，不过幸运的是服务端的任意数据端口可以设定在一个范围内。

相关主动、被动连接配置参考[这里](https://fengmengzhao.github.io/2015/12/20/Linux-basic-learning.html#6.6)

> 注意：<br><br>
- 如果被动模式端口范围最大最小设置为一个固定的值，这个固定端口处于监听状态时，新的客户端不能用被动模式连接到ftp服务，原因还没有弄清楚。如果把端口设置为一个区间，就不会出现此问题。**在更新这篇文章（2021-12-01）的时候，这个问题有了答案：假如你设置pasv port为一个固定的值，那么只能建立一个连接，因为客户端使用pasv模式连接服务端并传输数据的时候，设定的pasv port处于ESTABLISH状态，新的连接会尝试重新建立tcp连接（这里其实不太明白为什么不公用同一个pasv端口建立tcp数据连接，可能是数据传输tcp实现上的要求），这时候就会失败。也就是说pasv模式下，不同的连接用的是不同的服务端pasv端口。这也是pasv port要设定一个范围的原因吧。**<br><br>
- 当使用web浏览器或者Windows网络共享作为客户端访问FTP时，经常会输入`ftp://ip:port`，大部分浏览器只支持被动模式。<br><br>
- Windows系统资源管理器输入ftp地址默认是被动模式，可以在IE中设置为主动模式。

使用ftp客户端工具设置被动连接到FTP服务，这里FTP服务的地址是`ftp://xx.xx.19.68:2121`，被动模式pasv端口没有做限制。

下图展示一个客户端用pasv模式连接ftp并传输数据时，服务端查看tcp连接情况：

![](/img/posts/ftp-pasv-one-conn-data-transfer.png)

新增一个客户端pasv模式连接并传输数据，服务端查看tcp连接如图

![](/img/posts/ftp-pasv-two-conn-data-transfer.png)

上二图我们可以看出，2个pasv模式连接到ftp服务客户端传输数据时，服务端用了2个不同的pasv端口，分别是连接1的`36369`和连接2的`44219`

---

<h3 id="5">5. 总结</h3>

主动与被动ftp工作连接方式：

    Active FTP:
        command: client >N   --> server 21
        data   : client >N+1 <-- server 20 
    Passive FTP:
        command: client >N   --> server 21
        data   : client >N+1 --> server P(P>1023)

> 通俗的语言将主动模式与被动模式的区别：<br><br>
主动模式：客户端对服务端说，我给你发一个端口，这个端口在我本地监听，我需要数据的时候会通过命令端口发送命令，然后你把数据**主动推送**到我这里！<br><br>
被动模式：客户端对服务端说，我给你发一个`PASV`命令，这样我就约定好了用被动模式连接了，你启动一个监听端口吧，我需要数据的时候会去你那里取的，你需要**被动传**数据到我这里！

注意问题：

- 主动连接：服务端有一个监听端口，服务端主动连接客户端的监听端口来传输数据。这种方式对于服务端只需要2个端口（命令端口+数据端口）即可。
- 被动连接：服务端有两个监听端口，命令端口和数据端口（数据传输的时候会进入监听状态），服务端开启数据端口监听客户端的连接（被动），不同的客户端开启的服务端数据监听端口是不同的。这种方式对于服务端需要1（命令端口）+N（不同的pasv连接不同的端口）个端口。
- 如果有防火墙的限制，服务端的端口的开发个数有限，可以设置使用主动模式，2个端口即可满足。但是主动模式有限制，如果服务端对客户端端口不可达（这里的可达指的是tcp可以主动初始化连接到该端口），则不能使用。被动模式没有要求客户端端口可达的限制，但是每一个客户端传输文件在服务端要有一个监听的数端口。
- IE（资源管理器）、FTP客户端工具一般都默认用被动模式连接，可以设置为主动模式。

---

<h3 id="6">6. 问题汇总</h3>

<h4 id="6.1">6.1 Apache FTP银河麒麟FT2000+ SM环境Apache FTPClient和WinSCP设置passive模式不能下载文件（待解决）</h4>

现场银河麒麟FT2000+ SM服务器，装有Apache FTP，相关配置为：

```shell
<active enable="true" local-address="0.0.0.0" local-port="9999" ip-check="true" />
<passive ports="19000-20000" address="0.0.0.0" external-address="0.0.0.0" />
```

客户端连接代码为：

```java
FTPClient ftpClient = new FTPClient();
ftpClient.setControlEncoding(config.getEncoding());
ftpClient.setConnectTimeout(config.getConnectTimeout());
try {
    //连接ftp服务器
    ftpClient.connect(config.getHost(), config.getPort());
    //获取返回代码
    int replyCode = ftpClient.getReplyCode();
    //如果没连上
    if (!FTPReply.isPositiveCompletion(replyCode)) {
        ftpClient.disconnect();
        log.warn(FTPSERVER_REFUSED_CONNECTION, replyCode);
        return null;
    }
    //如果用户名密码不对
    if (!ftpClient.login(config.getUsername(), config.getPassword())) {
        log.warn(FTP_CLIENT_LOGIN_FAILED, config.getUsername(), config.getPassword());
    }
    //设置一些参数
    ftpClient.setBufferSize(config.getBufferSize());
    ftpClient.setFileType(config.getTransferFileType());
    if (config.isPassiveMode()) {
        ftpClient.enterLocalPassiveMode();
    }

} catch (IOException e) {
    log.error(CREATE_FTP_CONNECTION_FAILED, e);
}
//下载文件
InputStream inputStream = null;
try {
    // 验证FTP服务器是否登录成功
    if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
        log.error(FTP_CONNECTED_FAILED);
        throw new IllegalArgumentException(FTP_CONNECTED_FAILED);
    }
    String ftpIsPassive = ArteryConfigUtil.getProperty("ftp.isPassive");
    ftpClient.enterLocalPassiveMode();//使用passive模式连接
    log.info(MSG_START_DOWNLOADING, remotePath);
    inputStream = ftpClient.retrieveFileStream(remotePath);
    log.info(MSG_END_DOWNLOADING, remotePath);
} finally {
    if (null != ftpClient && ftpClient.isConnected()) {
        try {
            log.info("退出ftp");
            ftpClient.logout();
            log.info("断开ftp");
            ftpClient.disconnect();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
```

使用上面的代码下载文件流，报错：

```shell
Exception in thread "main" java.net.ConnectException: 拒绝连接 (Connection refused)
    at java.net.PlainSocketImpl.socketConnect(Native Method)
    ...
    at java.net.SocketsSocketImpl.connetct(SocketsSocketImpl.java:394)
    at java.net.Socket.connetct(Socket.java:606)
    at org.apache.commons.net.ftp.FTPClient._openDataConnection_(FTPClient.java:924)
    at org.apache.commons.net.ftp.FTPClient._retriveFileStream(FTPClient.java:1984)
    at org.apache.commons.net.ftp.FTPClient.retriveFileStream(FTPClient.java:1971)
```

现场的Apache FTP所在服务器端口`19000-20000`确认没有防火墙策略，是可达的。代码执行到开始下载文件时，用`netstat`命令查看Apache FTP进程已经监听了`19000-20000`的端口，但报错似乎提示客户端初始化连接该端口就被“拒绝连接”。

使用`WinSCP`工具和Apache FTPClient在被动连接模式下效果一样，但是用`curl`命令行工具被动模式下能够下载成功：`curl -o a.obj ftp://xxx:xxx@xxx:xxx/xxx`。

网上查了很多资料，没有验证成功。现场抓包工具不可用，以后需要时再用`Fiddler`、`Wireshark`、`tcpdump`等抓包工具进一步排查`tcp`初始化连接情况。

上述代码注释掉`ftpClient.enterLocalPassiveMode()`使用主动模式连接下载文件流没有问题。

<h5 id="6.1.1">6.1.1 主动模式连接报错“x.x.x.x is not the same as server y.y.y.y”</h5>

上面“6.1”如果问题是端口可达性问题的话，那么可以把ftp服务端和客户端放在一起，这样肯定就不会出现端口不可达的情况了。测试后发现报错：

```shell
Writing File failed with: File operation failed... Host attempting data connection 127.0.0.1 is not the same as server 141.151.1.62 
```

可以有两种解决办法：

1. 设置ftp的ip地址为`127.0.0.1`，这样就不会不一致了，能正常工作。
2. 代码中设置FTPClient参数`FTPClient.setRemoteVerificationEnabled(false)`。参考：[https://stackoverflow.com/questions/57164983/how-to-handle-host-attempting-data-connection-x-x-x-x-is-not-the-same-as-server](https://stackoverflow.com/questions/57164983/how-to-handle-host-attempting-data-connection-x-x-x-x-is-not-the-same-as-server)

**Reference**

- [http://slacksite.com/other/ftp.html](http://slacksite.com/other/ftp.html)

<h3 id="99">更新记录</h3>

- 2022-01-13 15:22 排查信创机Apache FTP使用Apache FTPClient passive模式连接问题“6.1”。
- 2022-01-20 10:00 记录信创机主动连接模式报错“connection x.x.x.x is not the same as server y.y.y.y”。
