---
layout: post
title: 一步一个脚印学习创建免费的个人博客
subtitle: 也许你需要分享，也许你需要记录，也许你需要宣传...，跟随我的脚步，你就能创建一个神奇的个人网站。如果你对前端感兴趣，那么更能够大显身手...
background: '/img/posts/step-by-step-create-free-jekyll-blog.jpg'
comment: true
---

## 目录

- [1 基于Github Pages的Jekyll-blog你需要知道这些](#1)
    - [1.1 Github之Github Pages](#1.1)
    - [1.2 SSG之Jekyll](#1.2)
    - [1.3 Jekyll + Github Pages](#1.3)
- [2 快速克隆拥有自己的博客](#2)
    - [2.1 注册Github账号](#2.1)
    - [2.2 克隆项目](#2.2)
    - [2.3 更改项目名称](#2.3)
    - [2.4 访问自己的博客](2.4)
- [3 定制化你的博客](#3)
    - [3.1 项目目录结构讲解](#3.1)
    - [3.2 增加博客功能](#3.2)
        - [3.2.1 分页](#3.2.1)
        - [3.2.2 访客统计](#3.2.2)
        - [3.2.3 百度后台统计分析](#3.2.3)
        - [3.2.4 评论](#3.2.4)
        - [3.1.5 搜索](#3.2.5)
- [4 jekyll本地开发](#4)

---

<h3 id="1">1 基于Github Pages的Jekyll-blog你需要知道这些</h3>

我刚接触`Github`的时候，对于`Github`,`Github Pages`,`Jekyll`等词汇总是傻傻的弄不明白，不知道它们是用来干什么的，又是怎么依赖的，怎么就可以免费的创建一个博客呢？接下来我们弄明白这些概念吧。

<h4 id="1.1">1.1 Github之Github pages</h4>

Github是使用Git进行版本控制的基于Web的托管服务。单位和个人可以把项目免费托管在Github上，但是这些项目必须是开源的。如果想要将私有的项目托管，需要付费。类似于Github服务的还有Gitlab。

> 如果不了解Git，[Pro Git中文版](http://iissnan.com/progit/ "Pro Git中文版")提供了详细的教程。

**Github Pages**是Github推出的基于Git仓库的为个人/组织或者项目提供静态站点的托管服务。

说白了，如果你有静态站点(或者希望Github Pages帮助生成)，只需要把代码推送到Github仓库(Repository)中并进行一定的配置，Github就会为你提供免费的托管服务和域名(可以理解为一个Web Server服务)。

> 这里说的代码可能是静态网站生成后的HTML静态代码，也可能是静态站点生成器(SSG, Static Site Generator)的代码(Jekyll代码)。

具体来说，分为两个方面：

**1. 如果你想把代码推送到Github仓库，希望Git Pages来生成静态站点:**

你只需要一个分支:

- 对于个人或者组织，建立一个仓库`github.com/username/username.github.io`，把代码推送到这个仓库的`master`分支
- 对于一个项目，建立一个仓库`github.com/username/projectRepository`，把代码推送到这个仓库的`gh-pages`分支

> 为什么项目需要另外一个分支来托管静态站点呢？<br><br>
因为：项目的`master`分支上是你的项目的源码啊！！！

**2. 如果你想在本地生成静态代码，然后把生成后的代码推送到Git仓库:**

你需要为代码创建一个分支(非必须)，同时生成后的静态站点用一个分支

- 针对个人/组织或者项目不同，像1一样分别把代码推送到`master`或者`gh-pages`分支上
- 注意：只需要把生成后的静态代码推送到相应分支，代码可以推送的任意分支或者在本地维护

> 为什么Github Pages既支持帮助生成静态站点，又支持生成后的静态HTML直接托管？<br><br>
因为：Github Pages默认支持的静态站点生成器(SSG)有局限，如果你使用的SSG默认Github Pages不支持，这样你可以在本地生成后，把生成的结果推送到Github Pages，就具有更大的灵活性。

总结一下，对于一个Github账户：

- 如果存在`github.com/username/username.github.io`这个仓库并且仓库中有`master`分支，Github Pages会对这个仓库的`master`分支进行托管；
- 如果存在`github.com/username/projectRepository`仓库并且仓库中有`gh-pages`分支，Github Pages会对这个仓库的`gh-pages`分支进行托管；
- 托管的规则是：如果是SSG生成后的静态代码直接托管；如果是SSG代码，这Github Pages负责生成静态代码并托管

<h4 id="1.2">1.2 SSG之Jekyll</h4>

SSG是静态博客生成器，近年来逐渐流行起来。Jekyll是SSG的一种。

Jekyll是一个用`ruby`写的静态博客生成器，能够将纯文本文档(模板文件、YMAL文件、Markdown文档等)转化为一个完整的静态站点。

**那么Jekyll是如何工作的呢？**

- `liquid templating`: 将模板文件中的变量、循环、控制流等进行替换；
- `转换内容`: 将markdown等支持格式的文件转化为HTML文档；
- `套用模板`: 将上述转化的内容套用模板结合在一起

> Jekyll详细是如何工作的，[参考这里](https://www.bytesandwich.com/jekyll/software/blogging/2016/09/14/how-does-jekyll-work.html "How does Jekyll work")。

<h4 id="1.3">1.3 Jekyll + Github Pages</h4>

Github Pages默认支持Jekyll作为静态站点生成器。如果你写的Jekyll源代码按照1.1中的说明推动到了Github仓库中，则Github Pages会自动为你生成静态代码。这样就可以拥有免费的主页了。

---

<h3 id="1">2 快速克隆拥有自己的博客</h3>

> 本节的说明仅仅针对个人/组织主页，针对项目的情况，请参考第1部分讲解完成。

接下来我们来看看怎么实践快速创建自己的博客(以本博客为例说明)。

<h4 id="2.1">2.1 注册Github账号</h4>

注册账号本身就不在这里详细说明了，[请移步](https://www.github.com "Github")。

注意：一定要为自己起一个有意义的账号名(username)，因为你的主页被分配的域名是`https://username.github.io`。例如，本博客的域名就是：`https://fengmengzhao.github.io`

<h4 id="2.2">2.2 克隆项目</h4>

1. 登录你的Github账号
2. 访问[移步这里-冯兄话吉博客V3](https://github.com/FengMengZhao/fengmengzhao.github.io "冯兄话吉博客V3")
3. Fork(参照附图 - 冯兄话吉博客V3-克隆)
4. 修改你Github账号中仓库`fengmengzhao.github.io`为`username.github.io`(参照附图 - 修改克隆后仓库名为username.github.io)
5. 这样，在浏览器中输入`https://username.github.io`(记得把`username`替换为你真实注册的用户名)，你新的博客就可以访问了

> 冯兄话吉博客V3-克隆 

![冯兄话吉博客V3-克隆](/img/posts/fxhj-blog-repository-fork.png "冯兄话吉博客V3-Fork")

> 修改克隆后仓库名为**username.github.io**

![更改克隆后仓库名为username.github.io](/img/posts/change-repository-name.png "更改克隆后仓库名为username.github.io")

---

<h3 id="3">3 定制化你的博客</h3>

如果上面操作成功了，恭喜你有自己的博客了！可是全部是[冯兄话吉](https://fengmengzhao.github.io)的内容啊？怎么把它定制化为自己的呢？接下来我们首先讲解一下Github Jekyll仓库的目录结构，然后再讲解是怎么集成一个个插件的，这样就可以根据自己的情况定制修改啦！

<h4 id="3.1">3.1 项目目录结构讲解</h4>

![Jekyll目录结构](/img/posts/jekyll-folder-tree.png)

> 上图是本博客仓库源码目录，仅对部分目录、文件作出说明：

- `_config.yml`文件是默认项目配置文件
    - 这个YMAL文件定义变量，可以被`Liquid Template`使用
    - 修改文件内变量的内容为相应自己的内容
- `assets`目录是存放CSS和JS等依赖的目录
- `_posts`目录是默认存放博客内容(markdwon文档)的地方
- `_includes`目录是默认存放一些公共的引用文件(用`Liquid Template`语法编写)
- `_layouts`目录是默认存放页面模板的文件(用`Liquid Template`语法编写)

<h4 id="3.2">3.2 增加博客功能</h4>

一个简单的博客是需要一些支持的功能才能够好用，下面对本博客支持的内容一一讲解。

<h5 id="3.2.1">3.2.1 分页</h5>

分页用的插件是`paginate`，`_config.yml`文件中可以直接配置分页的大小。

<h5 id="3.2.2">3.2.2 访客统计</h5>

访客统计用的是[不蒜子-极简网页计数器](http://busuanzi.ibruce.info/ "不蒜子-极简网页计数器")。

1. 在路径`_includes/scripts.html`文件中可以找到`不蒜子`JS的引用。
2. 在路径`_includes/footer.html`文件中可以找到`不蒜子`HTML代码的位置。

<h5 id="3.2.3">3.2.3 百度后台统计分析</h5>

百度后台统计分析用的是[百度统计](https://tongji.baidu.com/web/welcome/login "百度统计")

1. 注册一个百度统计账号
2. 登录百度统计账号，【管理】-【新增网站】，如图 - 百度统计新增网站，将自己的域名`username.github.io`填入
3. 在百度统计注册的网站中找到【获取代码】，【复制代码】保存到`_includes/baidu-analytics.js`中
4. 在`_layouts/default.html`中可以找到引用了3中的`baidu-analytics.js`

> 百度统计新增网站

![百度统计新增网站](/img/posts/baidutongji-new-site.png "百度统计新增网站")

<h5 id="3.2.4">3.2.4 评论</h5>

评论使用的是[gitment](https://github.com/imsun/gitment "gitment")，听说是国内大神开发的。

1. 登录Github账号。
2. 访问[Developer Settings](https://github.com/settings/developers "Developer Settings")。
3. `new OAuth APP`，`Application name`，`Homepage URL`，`Application description`可以随意填写，`Authorization callback URL`一定要写自己的Github pages的URL。填写完信息后，按`Register application`按钮，得到`Client ID`和`Client Secret`。
4. 在路径`_layouts/post.html`中可以找到引用的`Gitment`的JS代码。

<h5 id="3.2.5">3.2.5 搜索</h5>

搜索使用的是[Simple-Jekyll-Search](https://github.com/christian-fei/Simple-Jekyll-Search "Simple-Jekyll-Search")。

1. 创建一个`search.json`文件，文件内用`Liquid Template`语言写索引模板。文件的路径是`search.json`。
2. 在路径`assets/simple-jekyll-search.js`能找到搜索时调用的JS。
3. 在路径`_includes/scripts.html`中能找到关于搜索的JS引用。
4. 在路径`_includes/navbar.html`中能找到搜索li标签和搜索框的HTML代码。
5. 默认的样式太丑了，在路径`assets/vendor/startbootstrap-clean-blog/scss/_navbar.scss`和`assets/scripts.js`总分别有样式和点击弹出搜索框的代码。

---

<h3 id="4">4 jekyll本地开发</h3>

如果你前端厉害或者感兴趣，经过第3部分，博客已经被你定制化的清新脱俗了。

但是，存在一个问题，每次我在本地写完代码后，还有上传到Github，才能看到样式，这种效率太低了，接下来我们就讲一讲怎么样在本地运行调试Jekyll，生成静态网站。

参考[Jekyll官方中文文档](https://jekyllcn.com/docs/home/ "Jekyll官方中文文档")。

需要注意的是：如果在虚拟机上进行开发，最好指定IP启动，例如：`bundle exec jekyll serve --host 172.16.192.208`。

> 因为官方有中文文档，讲解特别清晰，就不再赘述了。

**Jekyll本地开发遇到的问题**

**Reference：**

> 2019年10月22日发现新增的文章在github-page上构建失败，最后用docker环境将代码在本地运行，其中更改了一个编码的问题，代码在本地能够运行成功，但是在github-pages仍然失败。<br>
编码问题的报错是：`非US-ASCII字符`，修改了ruby的一个文件：`engine.rb`在`require`语句后加入`Encoding.default_external = Encoding.find('utf-8')`本地不会报错，运行成功！

- [startbootstrap-clean-blog](https://github.com/BlackrockDigital/startbootstrap-clean-blog "startbootstrap-clean-blog")


