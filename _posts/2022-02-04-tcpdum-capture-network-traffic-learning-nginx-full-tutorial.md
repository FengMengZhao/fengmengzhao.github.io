---
layout: post
title: 'tcpdump抓包学习Nginx(反向代理)，学完不怵nginx了，还总想跃跃欲试！(Nginx使用、原理完整版手册)'
subtitle: '之前遇到Nginx总是把配置文件改吧改吧能用就可以了，不理解也不敢改动相关的配置文件，云里雾里。抽时间整体上将Nginx捋一遍，囊括了Nginx的基础配置、功能使用。tcpdump抓包探究反向代理实现。学完再看到Nginx，总想试一试！'
background: '/img/posts/nginx-switch-army-knife.jpg'
comment: false
---

- [0. 前话](#0)
- [1. Nginx基本介绍](#1)
- [2. 怎么安装nginx](#2)
- [3. Nginx配置文件管理](#3)
- [4. Nginx配置为一个基本的Web Server](#4)
    - [4.1 写第一个配置文件](#4.1)
    - [4.2 校验、重载Nginx配置文件](#4.2)
    - [4.3 理解Nginx配置文件中的Directives和Contexts](#4.3)
    - [4.4 使用Nginx作为静态文件服务器](#4.4)
    - [4.5 Nginx中处理静态文件类型解析](#4.5)
    - [4.6 Nginx子配置引入](#4.6)
- [5. Nginx的动态路由](#5)
    - [5.1 地址匹配](#5.1)
    - [5.2 Nginx的变量](#5.2)
    - [5.3 重定向和重写](#5.3)
    - [5.4 多文件容错(try_files)](#5.4)
- [6. Nginx的日志](#6)
- [7. Nginx作为反向代理服务器](#7)
    - [7.1 什么是反向代理](#7.1)
    - [7.2 反向代理基本原理](#7.2)
    - [7.3 反向代理基本配置](#7.3)
    - [7.4 Nginx反向代理地址匹配规则](#7.4)
    - [7.5 反向代理header重写](#7.5)
    - [7.6 反向代理试试，tcpdump抓包解析，探个中究竟](#7.6)
    - [7.7 反向代理处理相对路径问题](#7.7)
- [8. Nginx作为一个负载均衡服务器](#8)
- [9. 优化Nginx性能](#9)
    - [9.1 怎么设置工作进程数(Worker Processes)和工作连接数(Worker Connections)](#9.1)
    - [9.2 怎样缓存静态文件](#9.2)
    - [9.3 怎样压缩相应(response)](#9.3)
- [10. 理解Nginx整个配置文件](#10)
- [11. 如何配置SSL和HTTP/2](#11)
    - [11.1 如何配置SSL](#11.1)
    - [11.2 如何开启HTTP/2](#11.2)
    - [11.3 如何开启服务端推送](#11.3)

<h3 id="0">0. 前话</h3>

俄罗斯年轻程序员[Igor Sysoev](https://en.wikipedia.org/wiki/Igor_Sysoev)为了解决所谓[C10K problem](https://en.wikipedia.org/wiki/C10k_problem)，也就是以前的Web Server不能支持超10k并发请求的问题，在2002年开启了新的Web Server的开发。

[Nginx](https://nginx.org/)2004年在[2-clause BSD](https://en.wikipedia.org/wiki/2-clause_BSD)证书下发布于众，根据[2021年3月Web Server的调查](https://news.netcraft.com/archives/2021/03/29/march-2021-web-server-survey.html)，Nginx持有35.3%的市场占有率，为4.196亿网站提供服务。

感谢[DigitalOcean](https://digitalocean.com/)公司的[NGINXConfig](https://www.digitalocean.com/community/tools/nginx)项目，提供了很多写好的Nginx模板供下载，这样就可以在不理解Nginx配置的情况下复制粘贴配置Nginx。

![](/img/posts/nginx-programmer-copy-and-paste.jpg)

这里不是说复制粘贴是不对的，而是如果只复制粘贴并不理解的话，迟早会出问题。所以，你必须理解Nginx的配置，通过学习本文，你能够：

- 理解工具生成或者别人配置的Nginx。
- 从0到1配置Web服务器、方向代理服务器和负载均衡服务器。
- 优化Nginx获取最大性能。
- 配置HTTPS和HTTP/2。

学习本文需要有一定的Linux基础，会执行例如`ls`、`cat`等Linux命令，还需要你对前后端有一定的了解，不过这些对前端或者后端程序员都很容易。

<h3 id="1">1. Nginx基本介绍</h3>

Nginx是一个高性能的Web服务器，着眼于高性能、高并发和低资源消耗。尽管Nginx作为一个Web服务器被大家所熟知，它另外的一个核心功能时反向代理。

Nginx不是市场上唯一的Web服务器，它最大的竞争对手[Apache HTTP Server(httpd)](https://httpd.apache.org/)在1995年就发布了。人们在选择Nginx作为Web服务器时候，基于下面两点考虑：

- 支持更高的并发。
- 用更少的硬件资源提供静态文件服务。

Nginx和Apache谁更好的争论没有意义，如果想了解更多Nginx和Apache的区别可以参考[Justin Ellingwood](https://www.digitalocean.com/community/users/jellingwood)的[文章](https://www.digitalocean.com/community/tutorials/apache-vs-nginx-practical-considerations)。

关于Nginx对请求处理的新特点，引用Justin的文章解释如下：

Nginx在Apache之后出现，更多认识到网站业务扩大之后面临的并发性问题，所以从一开始就设计为异步、非阻塞和事件驱动连接处理的算法。

Nginx工作时候会设定worker进程(worker process)，每一个worker进程都能够处理数千个连接。worker进程通过`fast looping`的机制来不断轮询处理事件。将具体处理请求的工作和连接解耦能够让每一个worker进程仅当新的事件触发的时候将其与一个连接关联。

Nginx基本工作原理图：

![](/img/posts/nginx-basic-working-process.png)

Nginx之所以能够在低资源消耗的情况下高性能提供静态文件服务，是因为它没有内置动态编程语言处理器。当一个静态文件请求到达后，Nginx就是简答的响应请求文件，并没有做什么额外的处理。

这不是说Nginx不能够整合动态编程语言处理器，它可以将请求任务代理到独立的进程上，例如`PHP-FPM`、`Node.js`或者`Python`。一旦第三方进程处理完请求，再将响应代理回客户端，工作如图：

![](/img/posts/nginx-basic-working-with-external-process.png)

<h3 id="2">2. 怎么安装nginx</h3>

Nginx的安装网上示例很多，这里以`Ubuntu`为例：

```shell
#更新源
sudo apt update && sudo apt upgrade -y

#安装
sudo apt install nginx -y
```

这种方式安装Nginx成功之后，Nginx会注册为`systemd`系统服务，查看服务：

```shell
sudo systemctl status nginx

#如果没有注册为systemd服务，可以用service查看试下
sudo service nginx status
```

Nginx的配置文件经常放在`/etc/nginx`目录中，默认的配置端口是`80`，如果启动成功，可以访问得到页面：

![](/img/posts/nginx-install-success.png)

恭喜！Nginx安装成功了！

<h3 id="3">3. Nginx配置文件管理</h3>

Nginx为静态或者动态文件提供服务，具体怎么样提供服务是由配置文件设置的。

Nginx的配置文件以`.conf`结尾，常常位于`/etc/nginx`目录中。访问`/etc/nginx`目录：

```shell
cd /etc/nginx

ls -lh

# drwxr-xr-x 2 root root 4.0K Apr 21  2020 conf.d
# -rw-r--r-- 1 root root 1.1K Feb  4  2019 fastcgi.conf
# -rw-r--r-- 1 root root 1007 Feb  4  2019 fastcgi_params
# -rw-r--r-- 1 root root 2.8K Feb  4  2019 koi-utf
# -rw-r--r-- 1 root root 2.2K Feb  4  2019 koi-win
# -rw-r--r-- 1 root root 3.9K Feb  4  2019 mime.types
# drwxr-xr-x 2 root root 4.0K Apr 21  2020 modules-available
# drwxr-xr-x 2 root root 4.0K Apr 17 14:42 modules-enabled
# -rw-r--r-- 1 root root 1.5K Feb  4  2019 nginx.conf
# -rw-r--r-- 1 root root  180 Feb  4  2019 proxy_params
# -rw-r--r-- 1 root root  636 Feb  4  2019 scgi_params
# drwxr-xr-x 2 root root 4.0K Apr 17 14:42 sites-available
# drwxr-xr-x 2 root root 4.0K Apr 17 14:42 sites-enabled
# drwxr-xr-x 2 root root 4.0K Apr 17 14:42 snippets
# -rw-r--r-- 1 root root  664 Feb  4  2019 uwsgi_params
# -rw-r--r-- 1 root root 3.0K Feb  4  2019
```

该目录中的`/etc/nginx/nginx.conf`就是Nginx的主配置文件。如果你打开这个配置文件，会发现很多内容，不要害怕，本文就是一点一点的要学会它。

在进行配置文件修改的时候，不建议直接修改`/etc/nginx/nginx.conf`，可以将之备份之后再修改：

```shell
#重命名文件
sudo mv nginx.conf nginx.conf.backup

#新建配置文件
sudo touch nginx.conf
```

<h3 id="4">4. Nginx配置为一个基本的Web Server</h3>

这一部分，将会从零一步步学习Nginx配置文件的书写，目的是了解Nginx配置文件的基本语法和基本概念。

<h4 id="4.1">4.1 写第一个配置文件</h4>

`vim /etc/nginx/nginx.conf`打开配置文件并更新内容：

```shell
events {

}

http {

    server {

        listen 80;
        server_name localhost;

        return 200 "Bonjour, mon ami!\n";
        #配置重定向
        #return 302 https://www.baidu.com$request_uri;
    }

}
```

重启Nginx并访问，你会得到如下信息：

```shell
curl -i http://127.0.0.1

HTTP/1.1 200 OK
Server: nginx/1.18.0 (Ubuntu)
Date: Sat, 19 Feb 2022 08:31:59 GMT
Content-Type: text/plain
Content-Length: 21
Connection: keep-alive

Bonjour, mon ami!
```

<h3 id="4.2">4.2 校验、重载Nginx配置文件</h3>

Nginx的配置文件是否正确可以通过`-t`参数校验：

```shell
sudo nginx -t

nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
nginx: configuration file /etc/nginx/nginx.conf test is successful
```

如果有相关的语法错误，上述命令会有相关提示。

如果你想改变Nginx的相关状态，例如重启、重载等，可以有三种办法。一是通过`-s`(signal)参数向Nginx发送信号；二是使用系统服务管理工具`systemd`或者`service`等；三是使用`kill`命令对Linux进程操作。

**向Nginx发送信号**

nginx信号：`nginx -s reload|quit|stop|reopen`，分别表示重载配置文件、优雅停止Nginx、无条件停止Nginx和重新代开log文件。

> 所谓的“优雅停止”Nginx，是指处理完目前的请求再停止；而“无条件停止”Nginx，相当于`kill -9`，进程直接被杀死。

**系统服务管理Nginx**

```shell
#使用systemctl
sudo systemctl start|restart|stop nginx

#或者使用service
sudo service nginx start|restart|stop
```

**kill命令杀死进程并手动启动**

```shell
#杀死主进程及各子进程
sudo kill -TERM $MASTER_PID

#指定配置文件启动Nginx
sudo /usr/sbin/nginx -c /etc/nginx/nginx.conf
```

<h4 id="4.3">4.3 理解Nginx配置文件中的Directives和Contexts</h4>

Nginx的配置文件虽然看起来只是简单的配置文本，但它是包含语法的。实际上配置文件中的内容都是`Directives`。`Directives`分为两种：

- `Simple Directives`
- `Block Directives`

`Simple Directives`：包含名称和空格，以分号（`;`）结尾。例如`listen`、`return`等。

`Block Directives`：包裹在`{}`中，`{}`由`Simple Directives`组成，称之为`Contexts`。

Nginx配置中核心的`Contexts`：

- `events{}`：总体配置nginx如何处理请求，只能在配置文件中出现一次。
- `http{}`：配置nginx如何处理`http`或者`https`请求，只能在配置文件中出现一次。
- `server{}`：内嵌在`http{}`中，用来配置一个独立主机上指定的虚拟主机。`http{}`可以配置多个`server{}`，表示多个虚拟主机。
- `main`：上述3个`Contexts`之外的配置都在该`Contex`上。

在主机上设置不同的虚拟主机（多个`server{}`、相同`server_name`），监听不同的端口（`listen`不同）：

```shell
http {
    server {
        listen 80;
        server_name localhost;

        return 200 "hello from port 80!\n";
    }


    server {
        listen 8080;
        server_name localhost;

        return 200 "hello from port 8080!\n";
    }
}
```

不同的虚拟主机，监听同一个端口（多个`server{}`、不同`server_name`），监听同一个端口（`listen`相同）：

> 这种情况必须用域名，Nginx会将请求头中`Host`信息取出来和服务端配置`server_name`做匹配，匹配到哪个就就进入到那个处理块中。

```shell
http {
    server {
        listen 8088;
        server_name library.test;

        return 200 "your local library!\n";
    }


    server {
        listen 8088;
        server_name librarian.library.test;

        return 200 "welcome dear librarian!\n";
    }
}
```

当访问不同的域名时，会返回不同的结果：

```shell
curl -i http://library.test:8088

HTTP/1.1 200 OK
Server: nginx/1.18.0 (Ubuntu)
Date: Sun, 20 Feb 2022 08:02:20 GMT
Content-Type: application/octet-stream
Content-Length: 21
Connection: keep-alive

your local library !

curl -i http://librarian.library.test:8088

HTTP/1.1 200 OK
Server: nginx/1.18.0 (Ubuntu)
Date: Sun, 20 Feb 2022 08:04:26 GMT
Content-Type: application/octet-stream
Content-Length: 24
Connection: keep-alive

welcome dear librarian!
```

这样能成功的提前是指定的域名解析到同一个IP，或者在本地的hosts文件中配置好域名进行本地测试：

```shell
172.19.146.188 library.test librarian.library.test
```

> 注意，这里`return`这个`Directive`后面跟两个参数，一个是状态码，一个是返回的文本信息，文本信息要用引号引起来。

<h4 id="4.4">4.4 使用Nginx作为静态文件服务器</h4>

更新Nginx配置文件如下：

```shell
events {

}

http {

    server {

        listen 8088;
        server_name localhost;

        root /usr/share/nginx/html;
    }

}
```

> 这里对Nginx默认的展示页面做了修改，在文件`/usr/share/nginx/html/assets/mystyle.css`写入`p {background: red;}`并在`html`文件中引入该`css`，这样正常情况段落的背景会变成红色。

访问页面，展示的是`index.html`，但是段落的背景色没有生效。debug一下`css`文件：

```shell
curl -i http://fengmengzhao.hypc:8088/assets/mystyle.css

HTTP/1.1 200 OK
Server: nginx/1.18.0 (Ubuntu)
Date: Sun, 20 Feb 2022 08:43:58 GMT
Content-Type: text/plain
Content-Length: 27
Last-Modified: Sun, 20 Feb 2022 08:38:54 GMT
Connection: keep-alive
ETag: "6211fe1e-1b"
Accept-Ranges: bytes

p {
    background: red;
}
```

注意，这里响应头信息`Content-Type`是`text/plain`，而不是`text/css`。也就是说Nginx将`css`文件做为一个普通的文本提供服务，而没有当做`stylesheet`，浏览器自然就不会渲染样式。

> 本文会在本地`hosts`文件增加域名解析，所以会在示例中看到对域名请求。在操作本文示例时，要根据自己环境对ip（域名）或者端口做相应修改。

<h4 id="4.5">4.5 Nginx中处理静态文件类型解析</h4>

实际上这里涉及到Nginx对静态文件类型解析的处理，默认不进行任何设置情况下，Nginx认为文本文件的类型是`text/plain`。

修改配置文件如下：

```shell
events {

}

http {

    types {
        text/html html;
        text/css css;
    }

    server {

        listen 8088;
        server_name localhost;

        root /usr/share/nginx/html;
    }
}
```

重新访问页面，样式正常，`mystyle.css`文件的`response`头`Content-Type`为`text/css`：

![](/img/posts/nginx-learn-file-type-handle-right.png)

这里在`http{}`中引入了`types{}`，通过文件的后缀映射文件的类型。需要注意，如果没有`types{}`，nginx会认为`.html`文件的类型是`text/html`，但是一旦引入`types{}`，nginx只会解析定义的类型映射。所以这里引入`types{}`后，不能只定义`css`的类型映射，同样要显式定义`html`的类型映射，否则nginx会将`html`解析为普通文本文件。

<h4 id="4.6">4.6 Nginx子配置引入</h4>

手动在`http{}`中增加`types{}`来映射文件类型对于小项目还可以，对大型项目来说手动配置就太繁琐了，Nginx提供了默认的解析映射（常常在`/etc/nginx/mime.types`文件中），可以通过`include`语法将子配置引入配置文件中。

修改配置如下：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;

        root /usr/share/nginx/html;
    }

}
```

重启Nginx，自定义的`css`文件能够正常展示。

<h3 id="5">5. Nginx的动态路由</h3>

上面的示例非常简单，访问`root`定义目录下的文件，存在就返回，不存在就返回默认`404`页面。

接下来学习Nginx的`location`动态路由用法，包括重定向、重写和`try_files` Directive。

> 所谓的动态路径就是用户访问的路径到达Nginx后，Nginx如何匹配访问内容。

**Location Matches**

修改配置文件，如下：

```shell
events {

}

http {

    server {

        #设置默认的Content-Type text/html，否则将以流的方式下载
        default_type text/html;
        #设置字符编码为utf-8，否则页面会乱码
        charset utf-8;

        listen 80;
        server_name localhost;
        #前缀匹配，示例：http://fengmengzhao.hypc:8088/agatha----
        location /agatha {
            return 200 "前缀匹配-Miss Marple.\nHercule Poirot.\n";
        }
        #完全匹配，示例：http://fengmengzhao.hypc:8088/agatha
        location = /agatha {
            return 200 "完全匹配-Miss Marple.\nHercule Poirot.\n";
        }
        #正则匹配，默认大小写敏感，示例：http://fengmengzhao.hypc:8088/agatha01234
        #正则匹配的优先级要高于前缀匹配，低于优先前缀匹配
        location ~ /agatha[0-9]{
            return 200 "正则匹配，大小写敏感-Miss Marple.\nHercule Poirot.\n";
        }
        #正则匹配，大小写不敏感，示例：http://fengmengzhao.hypc:8088/AGatHa01234
        location ~* /agatha[0-9]{
            return 200 "正则匹配，大小写不敏感-Miss Marple.\nHercule Poirot.\n";
        }
        #优先前缀匹配，示例：http://fengmengzhao.hypc:8088/Agatha01234
        #在前缀匹配前加^~即可转化为优先前缀匹配
        location ^~ /Agatha {
            return 200 "优先前缀匹配-Miss Marple.\nHercule Poirot.\n";
        } 
    }
}
```

匹配规则总结：

 匹配 | 关键字
---|---
完全  | `=`
 优先前缀 | `^~`
 正则 |  `~`或者`~*`
  前缀|  `None`

如果一个请求满足多个配置的匹配，正则匹配的优先级大于前缀匹配，而优先前缀匹配的优先级大于正则匹配，完全匹配优先级最高。

**nginx中的变量（`Variables`）**

设置变量：

```shell
set $<variable_name> <variable_value>;

# set name "Farhan"
# set age 25
# set is_working true*
```

变量类型：

- String
- Integer
- Boolean

除了自定义变量外，nginx有内置的变量，参考[https://nginx.org/en/docs/varindex.html](https://nginx.org/en/docs/varindex.html)。

例如，如下配置中使用内置变量：

```shell
events {

}

http {

    server {

        listen 80;
        server_name localhost;

        return 200 "Host - $host\ - $uri\nArgs - $args\n";
    }

}

# curl http://localhost/user?name=Farhan

# Host - localhost
# URI - /user
# Args - name=Farhan
```

上面使用了`$host`、`$uri`和`$args`内置变量，分别表示主机名、请求相对路径和请求参数。变量可以作为值赋值给自定义变量，例如：

```shell
events {

}

http {

    server {

        listen 80;
        server_name localhost;
        
        set $name $arg_name; # $arg_<query string name>

        return 200 "Name - $name\n";
    }

}
```

上面出现了`$arg_*`内置变量，使用`$arg_<query string name>`可以获取`$args`变量中指定的`query string`。

**重定向（`Redirects`）和重写（`Rewrites`）**

nginx中的重定向和其他平台上见到的重定向一样，`response`返回`3xx`的状态码和`location`头信息。如果是在浏览器中访问，浏览器会自动重新发起`location`指定的请求，地址栏`url`也会发生改变。

重定向示例：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;

        root /usr/share/nginx/html;

        location = /index_page {
                return 307 https://fengmengzhao.github.io;
        }

        location = /about_page {
                return 307 https://fengmengzhao.github.io/about;
        }
    }
}

#curl -I http://localhost/about_page

HTTP/1.1 307 Temporary Redirect
Server: nginx/1.18.0 (Ubuntu)
Date: Mon, 21 Feb 2022 11:47:42 GMT
Content-Type: text/html; charset=utf-8
Content-Length: 180
Connection: keep-alive
Location: https://fengmengzhao.github.io/about
```

重写（`Rewrites`）和重定向不一样，重写内部转发了请求，地址栏不会发生改变。示例如下：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;

        root /usr/share/nginx/html;

        rewrite /image /assets/generate.png;
    }
}

#curl -i http://localhost/image

HTTP/1.1 200 OK
Server: nginx/1.18.0 (Ubuntu)
Date: Mon, 21 Feb 2022 11:56:42 GMT
Content-Type: image/png
Content-Length: 144082
Last-Modified: Sun, 20 Feb 2022 08:35:21 GMT
Connection: keep-alive
ETag: "6211fd49-232d2"
Accept-Ranges: bytes

Warning: Binary output can mess up your terminal. Use "--output -" to tell
Warning: curl to output it to your terminal anyway, or consider "--output
Warning: <FILE>" to save to a file.
```

如果在浏览器上访问`http://fengmengzhao.hypc:8088/image`，即可展示图片。

**`try_files`尝试多个文件**

`try_files`示例：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;

        root /usr/share/nginx/html;

        try_files /assets/xxx.jpg /not_found;

        location /not_found {
                return 404 "sadly, you've hit a brick wall buddy!\n";
        }
    }
}
```

示例查找`/assets/xxx.jpg`文件，如果不存在就查找`/not_found`路径。

`try_files`常常和`$uri`内置变量一起使用：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;

        root /usr/share/nginx/html;

        try_files $uri /not_found;
        #当访问http://localhost返回404
        #这里表示，当访问$uri文件不存在时，尝试$uri/作为一个目录访问
        #try_files $uri $uri/ /not_found;

        location /not_found {
                return 404 "sadly, you've hit a brick wall buddy!\n";
        }
    }
}
```

<h3 id="6">6. Nginx的日志</h3>

日志位置（常常在`/var/log/nginx`）：

```shell
ls -lh /var/log/nginx/

# -rw-r----- 1 www-data adm     0 Apr 25 07:34 access.log
# -rw-r----- 1 www-data adm     0 Apr 25 07:34 error.log
```

删除日志文件并`reopen` Nginx：

```shell
# delete the old files
sudo rm /var/log/nginx/access.log /var/log/nginx/error.log

# create new files
sudo touch /var/log/nginx/access.log /var/log/nginx/error.log

# reopen the log files
sudo nginx -s reopen
```

这里如果采用上面删除文件后再创建文件的方法清空日志，就需要`nginx -s reopen`重载Nginx，否则新的日志文件不会被写入日志，因为Nginx的输出流指向还是之前删除的日志文件。实际上这里想清空日志文件可以采用`echo "" > /var/log/nginx/access.log`的方法，这样就不用`reopen` Nginx了。

访问Nginx并查看日志：

```shell
curl -I http://localhost

# HTTP/1.1 200 OK
# Server: nginx/1.18.0 (Ubuntu)
# Date: Sun, 25 Apr 2021 08:35:59 GMT
# Content-Type: text/html
# Content-Length: 960
# Last-Modified: Sun, 25 Apr 2021 08:35:33 GMT
# Connection: keep-alive
# ETag: "608529d5-3c0"
# Accept-Ranges: bytes

sudo cat /var/log/nginx/access.log 

# 192.168.20.20 - - [25/Apr/2021:08:35:59 +0000] "HEAD / HTTP/1.1" 200 0 "-" "curl/7.68.0"
```

默认情况下，任何访问的日志都会记录在`access.log`文件中，但是也可以通过`access_log` Directive来自定义路径：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;
        
        location / {
            return 200 "this will be logged to the default file.\n";
        }
        
        location = /admin {
            #日志会输出在/var/logs/nginx/admin.log文件中
            access_log /var/logs/nginx/admin.log;
            
            return 200 "this will be logged in a separate file.\n";
        }
        
        location = /no_logging {
            access_log off;
            
            return 200 "this will not be logged.\n";
        }
    }
}
```

在`location{}`中可以自定义`access.log`的路径，也可以用`access_log off`来关闭指定路径的log输出。

同样，`error_log`也可以自定义Nginx的`error.log`路径：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;
	
        error_log /var/log/error.log warn;
        #return后面只能跟两个参数，这里是为了让Nginx报错，输出错误日志
        return 200 "..." "...";
    }

}
```

使用`nginx -s reload`重载Nginx：

```shell
sudo nginx -s reload

# nginx: [emerg] invalid number of arguments in "return" directive in /etc/nginx/nginx.conf:14
```

访问错误日志文件，有同样的错误信息：

```shell
sudo cat /var/log/nginx/error.log 

# 2021/04/25 08:35:45 [notice] 4169#4169: signal process started
# 2021/04/25 10:03:18 [emerg] 8434#8434: invalid number of arguments in "return" directive in /etc/nginx/nginx.conf:14
```

Nginx error日志信息是有级别的：

- `debug`：能帮忙排查哪里出错了。
- `info`：可以了解但是不必要的信息。
- `notice`：比`info`更值得了解的信息，但不知道也没什么。
- `warn`：意料之外的事情发生了，哪里出问题了，但还能工作。
- `error`：什么失败了的信息。
- `crit`：严重问题，急需解决。
- `alert`：迫在眉睫。
- `emerg`：系统不稳定，十万火急。

默认情况下，Nginx记录所有级别的Error信息，可以通过`error_log`第二个参数覆写。如果要设置最低级别的日志输出为`warn`，更新配置文件如下：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;
	
        error_log /var/log/error.log warn;

        return 200 "..." "...";
    }

}
```

重载Nginx并查看日志：

```shell
cat /var/log/nginx/error.log

# 2021/04/25 11:27:02 [emerg] 12769#12769: invalid number of arguments in "return" directive in /etc/nginx/nginx.conf:16
```

这里可以看到，没有输出之前的`[notice]`日志了。

<h3 id="7">7. Nginx作为反向代理服</h3>

<h4 id="7.1">7.1 什么是反向代理？</h4>

所谓的反向代理，首先是一种代理，是客户端和服务端之外的第三方。把正向代理（Forward proxy）和反向代理（Reverse proxy）比较起来看就很容易理解。

正向代理一般代理的是客户端，用户（客户端）是知道代理存在（一般是客户端配置的）。客户端对目标服务的请求会经由代理转发并将目标服务响应返回给客户端。常见的`VPN`代理、浏览器（设置）代理、Git（设置）代理和`Fiddler`抓包软件等都是正向代理。

正向代理示意图：

![](/img/posts/forward_proxy-3.png)

反向代理一般代理的是服务端，客户端直接和代理服务打交道（如果有反向代理的话），而对被代理的服务一无所知。客户端请求到达代理服务之后，代理服务再将请求转发到被代理的服务并将响应返回给客户端。

反向代理示意图：

![](/img/posts/reverse_proxy-resized-600.png)

Nginx作为反向代理时，处在客户端和服务端之间。客户端发送请求到Nginx（反向代理），Nginx将请求发送给服务端。一旦服务端处理完请求，会将结果返回给Nginx，Nginx再将结果返回给客户端。在这整个过程中，客户端并不知道实际上谁处理了请求。

<h4 id="7.2">7.2 反向代理基本原理</h4>

笔者刚接触反向代理的时候，感觉这是一个很神奇的事情。进行简单的配置就能将第三方的网站代理到自己的主机上吗？

实际上，不尽然。有些网站能够将主页代理过来，但功能不能完全使用；有些代理过来样式、图片等加载会出问题。只有理解了个中原理，才能够解释各种各样的情况。

反向代理就是将客户端的请求，重写`header`信息之后，在代理服务的服务端转发请求到被代理服务，被代理服务处理请求将响应返回给代理服务，代理服务进而转发响应回客户端。

> 代理服务转发的请求是代理服务端重新发起，因此在客户端的浏览器或者`Fiddler`工具进行网络抓包是抓不到的。要看具体的代理发起网络请求需要用`Wireshark`工具抓包代理服务器对应的网卡。

别理解复杂了，就是`客户端<--->代理<--->被代理服务`。Nginx的反向代理不会改变响应的内容，被代理服务响应页面的绝对引用（`/assets/image/abc.jpg`）、相对引用（`assets/image/abc.jpg`）或者图床引用（`https://image.com/image/abc.jpg`）代理回客户端的时候不会发生改变。这些引用在客户端解析`html`时候会重新在当前域发起新请求，如果请求指向了代理服务，会同样进行`请求<--->代理<--->被代理服务`。

> `--->`表示请求，`<---`表示响应。

有些时候代理之后之所以情况变得复杂，是因为被代理服务存在重定向或者权鉴的约束产生的，而代理的过程就是`请求<--->代理<--->被代理服务`这么简单，并且不会改变被代理服务的响应内容。

<h4 id="7.3">7.3 反向代理基本配置</h4>

看一个简单的反向代理配置：

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {
        listen 80;
        server_name localhost;

        location / {
                proxy_pass "https://bbs.tianya.cn/";
        }
    }
}
```

代理后页面如下：

![](/img/posts/nginx-bbs-tianya-cn-proxy.png)

> 因为是`http`反向代理了`https`，运营商竟然还在右下角插入了广告（`https://bbs.tianya.cn/`不会）。

`proxy_pass`能够简单的将客户端请求转发给第三方服务端并反向代理响应结果返回给客户端。

这只是简单的代理，如果你要反向代理一个接口并且使用`WebSocket`，那么就要覆写`header`信息：

```shell
#WebSocket需要http/1.1，默认是http/1.0
proxy_http_version 1.1;
#覆写header Upgrade为$http_upgrade的值，该值为Nginx获取客户端请求过来的Upgrade头信息值
proxy_set_header Upgrade $http_upgrade;
#覆写header Connection为'upgrade'
proxy_set_header Connection 'upgrade';
```

<h4 id="7.4">7.4 Nginx反向代理地址匹配规则</h4>

客户端发送给Nginx的请求，究竟Nginx会怎样拼接到`proxy_pass`指定的上游服务呢？Nginx有一定的规则：

1. 如果`proxy_pass`代理的上游服务是域名加端口（没有端口时默认端口为80或者443），那么客户端请求的代理路径会直接拼到上游服务地址上。示例，`proxy_pass http://redis.cn`就只是对域名（和端口）的代理。
2. 如果`proxy_pass`代理的上游服务有请求路径，那么客户端请求的代理路径将会是把客户端请求路径裁剪掉匹配路径后再拼到上游服务地址上。示例，`proxy_pass http://redis.cn/`或者`proxy_pass http://redis.cn/commands`是有请求路径的代理。

> 上面1、2分别定义为“情况1”和“情况2”，下面中有引用。

```shell
events {

}

http {

    include /etc/nginx/mime.types;

    server {
        listen 8088;
        server_name localhost;

        location / {
            #情况1，客户端路径和代理路径映射：
            #http://fengmengzhao.hypc:8088/commands --> http://redis.cn/commands
            proxy_pass http://redis.cn;
        }

        #location /redis {
            #情况1，客户端路径和代理路径映射：
            #http://fengmengzhao.hypc:8088/redis/commands --> http://redis.cn/redis/commands
        #   proxy_pass http://redis.cn;
        #}

        location /redis {
            #情况2，客户端路径和代理路径映射：
            #http://fengmengzhao.hypc:8088/redis/commands --> http://redis.cn/redis//commands
            proxy_pass http://redis.cn/;
        }

        location /redis/ {
            #情况2，客户端路径和代理路径映射：
            #http://fengmengzhao.hypc:8088/redis/commands --> http://redis.cn/redis/commands
            proxy_pass http://redis.cn/;
        }

        #location /redis-commands {
            #情况2，客户端路径和代理路径映射：
            #http://fengmengzhao.hypc:8088/redis-commands --> http://redis.cn/redis/commands
            #http://fengmengzhao.hypc:8088/redis-commands/keys.html --> http://redis.cn/redis/commands/keys.html
        #   proxy_pass http://redis.cn/commands;
        #}

        #location /redis-commands/ {
             #情况2，客户端路径和代理路径映射：
        #    #http://fengmengzhao.hypc:8088/redis-commands/keys.html --> http://fengmengzhao.hypc:8088/commandskeys.html
        #    proxy_pass http://redis.cn/commands;
        #}

        #location /redis-commands/ {
             #情况2，客户端路径和代理路径映射：
        #    #http://fengmengzhao.hypc:8088/redis-commands/keys.html --> http://fengmengzhao.hypc:8088/commands/keys.html
        #    proxy_pass http://redis.cn/commands/;
        #}

        location /redis-commands {
            #情况2，客户端路径和代理路径映射：
            #http://fengmengzhao.hypc:8088/redis-commands/keys.html --> http://fengmengzhao.hypc:8088/commands//keys.html
            proxy_pass http://redis.cn/commands/;
        }

    }
}
```

总结客户端请求和代理端转发请求的对应关系，如下：

| 匹配路径     | proxy_pass                | 客户端请求                                       | 代理后请求                            |
| ------------ | ------------------------- | ------------------------------------------------ | ------------------------------------- |
| `/`          | http://redis.cn           | http://fengmengzhao.hypc:8088                    | http://redis.cn                       |
| `/redis`     | http://redis.cn           | http://fengmengzhao.hypc:8088/redis              | **http://redis.cn/redis**             |
| `/`          | http://redis.cn/          | http://fengmengzhao.hypc:8088                    | http://redis.cn/                      |
| `/`          | http://redis.cn/          | http://fengmengzhao.hypc:8088/                   | http://redis.cn/                      |
| `/redis`     | http://redis.cn/          | http://fengmengzhao.hypc:8088/redis              | http://redis.cn/                      |
| `/redis`     | http://redis.cn/          | http://fengmengzhao.hypc:8088/redis/commands     | **http://redis.cn//commands**         |
| `/redis/`    | http://redis.cn/          | http://fengmengzhao.hypc:8088/redis              | http://redis.cn/                      |
| `/redis/`    | http://redis.cn/          | http://fengmengzhao.hypc:8088/redis/commands     | http://redis.cn/commands              |
| `/redis-commands`  | http://redis.cn/commands  | http://fengmengzhao.hypc:8088/redis-commands           | http://redis.cn/commands              |
| `/redis-commands`  | http://redis.cn/commands  | http://fengmengzhao.hypc:8088/redis-commands/keys.html | http://redis.cn/commands/keys.html    |
| `/redis-commands/` | http://redis.cn/commands  | http://fengmengzhao.hypc:8088/redis-commands           | http://redis.cn/commands              |
| `/redis-commands/` | http://redis.cn/commands  | http://fengmengzhao.hypc:8088/redis-commands/keys.html | **http://redis.cn/commandskeys.html** |
| `/redis-commands`  | http://redis.cn/commands/ | http://fengmengzhao.hypc:8088/redis-commands           | http://redis.cn/commands/             |
| `/redis-commands`  | http://redis.cn/commands/ | http://fengmengzhao.hypc:8088/redis-commands/keys.html | **http://redis.cn/commands//keys.html**   |
| `/redis-commands/` | http://redis.cn/commands/ | http://fengmengzhao.hypc:8088/redis-commands           | http://redis.cn/commands/             |
| `/redis-commands/` | http://redis.cn/commands/ | http://fengmengzhao.hypc:8088/redis-commands/keys.html | http://redis.cn/commands/keys.html    |

代理后的请求在客户端看不到网络请求，可以用`tcpdump`抓包代理服务所在主机的网卡生成`.cap`文件，并在`Wireshark`中查看具体请求。

`tcpdump`监听命令：

```shell
#172.19.146.188是Nginx代理IP；121.42.46.75是被代理上游服务IP，也就是redis.cn域名的解析IP
#ech0是172.19.146.188使用的网卡IP
sudo tcpdump -i eth0 tcp port 8088 and host 172.19.146.188 or host 121.42.46.75 -c 100 -n -vvv -w /opt/nginx-2.cap
```

启动后，访问代理服务，数据包经过网卡`eth0`就会被捕捉到。将`nginx-2.cap`文件在`Wireshark`中打开即可查看具体网络包。

以下表请求为`demo`，抓包获取代理请求，如下图：

| 匹配路径     | proxy_pass                | 客户端请求                                       | 代理后请求                            |
| ------------ | ------------------------- | ------------------------------------------------ | ------------------------------------- |
| `/redis-commands/` | http://redis.cn/commands  | http://fengmengzhao.hypc:8088/redis-commands/keys.html | **http://redis.cn/commandskeys.html** |

![](/img/posts/nginx-wireshark-capture-location-match-get-proxy-request.png)

<h4 id="7.5">7.5 反向代理header重写</h4>

<h3 id="8">8. Nginx作为一个负载均衡服务器</h3>

配置示例：

```shell
events {

}

http {

    upstream backend_servers {
        server localhost:3001;
        server localhost:3002;
        server localhost:3003;
    }

    server {

        listen 80;
        server_name localhost;

        location / {
            proxy_pass http://backend_servers;
        }
    }
}
```

`upstream{}`可以包含多个服务并且作为一个服务端被引用。

测试负载均衡：

```shell
while sleep 0.5; do curl http://localhost; done

# response from server - 2.
# response from server - 3.
# response from server - 1.
# response from server - 2.
# response from server - 3.
# response from server - 1.
# response from server - 2.
# response from server - 3.
# response from server - 1.
# response from server - 2.
```

**nginx性能优化**

配置Worker进程和Worker连接：

```shell
#一般情况，主机有多少核，就设置Worker进程的个数为多少
worker_processes 2;
#根据主机cpu的不同自动设置Worker进程的个数
#worker_processes auto;

events {

}

http {

    server {

        listen 80;
        server_name localhost;

        return 200 "worker processes and worker connections configuration!\n";
    }
}
```

`worker_connections`表示一个Worker进程能够处理的最大连接数，该参数跟主机cpu core个数和一个core能打开的文件个数有关（该值可以通过命令`ulimit -n`查询）。

`worker_connections`设置：

```shell
worker_processes auto;

events {
    worker_connections 1024;
}

http {

    server {

        listen 80;
        server_name localhost;

        return 200 "worker processes and worker connections configuration!\n";
    }
}
```

**怎么缓存静态内容**

```shell
worker_processes auto;

events {
    worker_connections 1024;
}

http {

    include /env/nginx/mime.types;

    server {

        listen 80;
        server_name localhost;

        root /srv/nginx-handbook-demo/static-demo;
        #正则匹配，大小写不敏感
        location ~* \.(css|js|jpg)$ {
            access_log off;
            
            add_header Cache-Control public;
            add_header Pragma public;
            add_header Vary Accept-Encoding;
            expires 1M;
        }
    }
}
```

像之前反向代理设置中的`proxy_set_header`给可以给代理到后端的请求增加`header`一样，使用`add_header`可以给response增加`header`。

`Cache-Control`头信息设置为`public`，是在告诉client该请求内容可以被缓存。`Pragma`是`Cache-Control`的old version。

`Vary`头信息设置为`Accept-Encoding`，后续详解。

`expires` directive表示设置`Expires`头信息，其值可以是`1M`（1 month）、`10m/10 minutes`或者`24h/24 hours`等。

测试请求的响应信息：

```shell
curl -I http://localhost/the-nginx-handbook.jpg

# HTTP/1.1 200 OK
# Server: nginx/1.18.0 (Ubuntu)
# Date: Sun, 25 Apr 2021 15:58:22 GMT
# Content-Type: image/jpeg
# Content-Length: 19209
# Last-Modified: Sun, 25 Apr 2021 08:35:33 GMT
# Connection: keep-alive
# ETag: "608529d5-4b09"
# Expires: Tue, 25 May 2021 15:58:22 GMT
# Cache-Control: max-age=2592000
# Cache-Control: public
# Pragma: public
# Vary: Accept-Encoding
# Accept-Ranges: bytes
```

**怎么压缩响应**

压缩配置：

```shell
worker_processes auto;

events {
    worker_connections 1024;
}

http {
    include /env/nginx/mime.types;

    gzip on;
    gzip_comp_level 3;

    gzip_types text/css text/javascript;

    server {

        listen 80;
        server_name localhost;

        root /srv/nginx-handbook-demo/static-demo;
        
        location ~* \.(css|js|jpg)$ {
            access_log off;
            
            add_header Cache-Control public;
            add_header Pragma public;
            add_header Vary Accept-Encoding;
            expires 1M;
        }
    }
}
```

默认nginx会对`html`文件进行`gzip`压缩，如果要对其他类型文件压缩，需要设置`gzip_types text/css text/javascript;`。

`gzip_comp_level`不是设置的越大越好，一般设置为1-4。

服务端设置`gzip`之后，客户端需要增加`header`信息`"Accept-Encoding: gzip"`才能完成服务端到客户端的压缩传输。

客户端请求没有`"Accept-Encoding: gzip"`的示例：

```shell
curl -I http://localhost/mini.min.css

# HTTP/1.1 200 OK
# Server: nginx/1.18.0 (Ubuntu)
# Date: Sun, 25 Apr 2021 16:30:32 GMT
# Content-Type: text/css
# Content-Length: 46887
# Last-Modified: Sun, 25 Apr 2021 08:35:33 GMT
# Connection: keep-alive
# ETag: "608529d5-b727"
# Expires: Tue, 25 May 2021 16:30:32 GMT
# Cache-Control: max-age=2592000
# Cache-Control: public
# Pragma: public
# Vary: Accept-Encoding
# Accept-Ranges: bytes
```

客户端请求设置"Accept-Encoding: gzip"的示例：

```shell
curl -I -H "Accept-Encoding: gzip" http://localhost/mini.min.css

# HTTP/1.1 200 OK
# Server: nginx/1.18.0 (Ubuntu)
# Date: Sun, 25 Apr 2021 16:31:38 GMT
# Content-Type: text/css
# Last-Modified: Sun, 25 Apr 2021 08:35:33 GMT
# Connection: keep-alive
# ETag: W/"608529d5-b727"
# Expires: Tue, 25 May 2021 16:31:38 GMT
# Cache-Control: max-age=2592000
# Cache-Control: public
# Pragma: public
# Vary: Accept-Encoding
# Content-Encoding: gzip
```

注意，这里response的`header`中有`Vary: Accept-Encoding`信息，该头信息告诉客户端，根据客户端设置的`Accept-Encoding`头信息的不同，服务端响应会发生变化。

对比压缩前后传输内容的大小：

```shell
cd ~
mkdir compression-test && cd compression-test

curl http://localhost/mini.min.css > uncompressed.css

curl -H "Accept-Encoding: gzip" http://localhost/mini.min.css > compressed.css

ls -lh

# -rw-rw-r-- 1 vagrant vagrant 9.1K Apr 25 16:35 compressed.css
# -rw-rw-r-- 1 vagrant vagrant  46K Apr 25 16:35 uncompressed.css
```

没压缩的版本大小是是`46k`，而压缩后的版本大小事`9.1k`。

**理解nginx `main` Contexts配置**

完整nginx配置文件：

```shell
user www-data;
worker_processes auto;
pid /run/nginx.pid;
include /etc/nginx/modules-enabled/*.conf;

events {
	worker_connections 768;
	# multi_accept on;
}

http {

	##
	# Basic Settings
	##

	sendfile on;
	tcp_nopush on;
	tcp_nodelay on;
	keepalive_timeout 65;
	types_hash_max_size 2048;
	# server_tokens off;

	# server_names_hash_bucket_size 64;
	# server_name_in_redirect off;

	include /etc/nginx/mime.types;
	default_type application/octet-stream;

	##
	# SSL Settings
	##

	ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3; # Dropping SSLv3, ref: POODLE
	ssl_prefer_server_ciphers on;

	##
	# Logging Settings
	##

	access_log /var/log/nginx/access.log;
	error_log /var/log/nginx/error.log;

	##
	# Gzip Settings
	##

	gzip on;

	# gzip_vary on;
	# gzip_proxied any;
	# gzip_comp_level 6;
	# gzip_buffers 16 8k;
	# gzip_http_version 1.1;
	# gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;

	##
		# Virtual Host Configs
	##

	include /etc/nginx/conf.d/*.conf;
	include /etc/nginx/sites-enabled/*;
}


#mail {
#	# See sample authentication script at:
#	# http://wiki.nginx.org/ImapAuthenticateWithApachePhpScript
# 
#	# auth_http localhost/auth.php;
#	# pop3_capabilities "TOP" "USER";
#	# imap_capabilities "IMAP4rev1" "UIDPLUS";
# 
#	server {
#		listen     localhost:110;
#		protocol   pop3;
#		proxy      on;
#	}
# 
#	server {
#		listen     localhost:143;
#		protocol   imap;
#		proxy      on;
#	}
#}
```

`pid /run/nginx.pid;`设置nginx进程的process id。

`include /etc/nginx/modules-enabled/*.conf;`设置`include`指定目录中任何`.conf`结尾的配置文件。该目录用来设置nginx的动态模块。

在`http{}`下，有基本的优化设置，如下：

- `sendfile on;`：禁止静态文件buffering。
- `tcp_nopush on;`：允许在一个包中发送响应头信息。
- `tcp_nodelay on;`：disables Nagle's Algorithm resulting in faster static file delivery。

`keepalive_timeout`设置http connection的连接时间。`types_hash_maxsize`设置`Hash map`的大小。

**配置SSL和http/2**

**实践**

```shell
server {
    listen 80;
    server_name example.net;
    root /path/to/aaa;

    location /bbb/ {
        proxy_pass http://example.com/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location / {
        try_files $uri $uri/ /index.html;
    }
    location ~ \.(svg|ttf|js|css|svgz|eot|otf|woff|jpg|jpeg|gif|png|ico)$ {
        access_log off;
        log_not_found off;
        expires max;
    }
}
```

The problem is basically that using a proxy_pass directive won't rewrite HTML code and therefor relative URL's to for instance a img src="/assets/image.png" won't magically change to img src="/bbb/assets/image.png".

I wrote about potential strategies to address that in Apache httpd here and similar solutions are possible for nginx as well:

方法一：

If you have control over example.com and the how the application/content is deployed there, deploy in the same base URI you want to use on example.net for the reverse proxy. deploy your code in example.com/bbb and then your proxy_pass will become quite an easy as /assets/image.png will have been moved to /bbb/assets/image.png:

```shell
location /bbb/ {
    proxy_pass http://example.com/bbb/;
}
```

方法二：

If you have control over example.com and the how the application/content is deployed: change to relative paths, i.e. rather than img src="/assets/image.png" refer to img src="./assets/image.png" from a page example.com/index.html and to img src="../../assets/image.png"from a page example.com/some/path/index.html

方法三：

Maybe you're lucky and example.com only uses a few URI paths in the root and non of those are used by example.net, then simply reverse proxy every necessary subdirectory:

```shell
location /bbb/ {
     proxy_pass http://example.com/; 
}
location /assets/ {
     proxy_pass http://example.com/assets/; 
}
location /styles/ {
     proxy_pass http://example.com/styles/;
}
```

方法四：

give up using a example.com as subdirectory on example.net and instead host it on a subdomain of example.net:

```shell
server { 
  server_name bbb.example.net 
  location / {
     proxy_pass http://example.com/; 
  }
}
```

方法五：

rewrite the (HTML) content by enabling the nginx ngx_http_sub_module. That will also allow you to rewrite absolute URL's with something similar to:

```shell
location /bbb/ {
     sub_filter 'src="/assets/'  'src="/bbb/assets/';
     sub_filter 'src="http://example.com/js/' 'src="http://www.example.net/bbb/js/' ;
     sub_filter_once off;
     proxy_pass http://example.com/; 
}
```

**反向代理**

所谓的反向代理就是将客户端发送来的请求转发给实际处理请求服务端（`proxy_pass`指定的服务端），服务端响应之后，再将响应代理回客户端。如图：

![](/img/posts/)

既然是代理，就不仅仅简单的只做转发，在代理收到客户端请求后，准备转发到指定代理服务端之前，会对请求的`header`信息进行重写，默认情况下规则如下：

1. 值为空的`header`不会进行转发；`header`的`key`中包含有`_`下划线的不会进行转发。
2. 默认改写`Host`和`Connection`两个`header`，分别为：`Host: $proxy_host`、`Connection: close`。

> 如果代理服务器只是转发，还要什么代理？就像生活中的代理一样，会提供增值服务，什么事情都帮你搞定。

**引用**

- [https://www.freecodecamp.org/news/the-nginx-handbook/](https://www.freecodecamp.org/news/the-nginx-handbook/)
- [https://serverfault.com/questions/932628/how-to-handle-relative-urls-correctly-with-a-nginx-reverse-proxy](https://serverfault.com/questions/932628/how-to-handle-relative-urls-correctly-with-a-nginx-reverse-proxy)
- [https://www.cnblogs.com/sky-cheng/p/11058221.html](https://www.cnblogs.com/sky-cheng/p/11058221.html)
- [https://stackoverflow.com/questions/15414810/whats-the-difference-of-host-and-http-host-in-nginx](https://stackoverflow.com/questions/15414810/whats-the-difference-of-host-and-http-host-in-nginx)
- [https://www.jscape.com/blog/bid/87783/forward-proxy-vs-reverse-proxy](https://www.jscape.com/blog/bid/87783/forward-proxy-vs-reverse-proxy)
