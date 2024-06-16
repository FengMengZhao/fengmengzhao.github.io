---
layout: post
title: 'RPM打包基础指南'
subtitle: '平时会使用RPM包安装软件，那么如何打RPM包呢？本文分别展示本地编译型、原生解释型、字节码编译解释型程序的RPM打包操作和基础原理。'
background: '/img/posts/rpm-package-guid.png'
comment: false
---

<h2 id="1">1. 简介</h2>

RPM打包指南：

**怎样准备打RPM包的源代码**

这是为没有软件开发经验的人准备的，请看[准备要打包的程序](#5)

**怎样将源代码打成RPM包**

这是为要将软件打成RPM包的开发人员准备的，请看[打包程序](#6)

**高级使用场景**

这是打包RPM高级场景的一些指南，请看[高级主题](#7)

<h3 id="1.1">1.1 PDF 版本</h3>

你可以从[本文档的PDF版本](https://rpm-packaging-guide.github.io/rpm-packaging-guide.pdf)下载PDF版本。

<h3 id="1.2">1.2 文章书写约定</h3>

文章书写预定如下：

- 命令行输出和包括源代码在内的文本文件的内容放在代码块中：

```shell
[root@xx.rpmbuild]# ls -l
total 0
drwx------ 3 root root 58 Oct 18 16:47 BUILD
drwx------ 2 root root  6 Oct 18 16:47 BUILDROOT
drwx------ 4 root root 35 Oct 18 16:47 RPMS
drwx------ 2 root root  6 Oct 18 16:47 SOURCES
drwx------ 2 root root  6 Oct 18 16:47 SPECS
drwx------ 2 root root 92 Oct 18 16:21 SRPMS
```

- 文中出现的其他内容引用或者专业术语会用**粗体**或者*斜体*来标识，第一次出现的术语引用会用超链接形式链接到不同的文档。
- 工具名称、命令或者代码常用的内容会用代码的方式引用起来。

<h3 id="1.3">1.3 协作文章指南</h3>

你可以通过提`issue`或者`PR`的形式协作贡献[GitHub repository](https://github.com/redhat-developer/rpm-packaging-guide)上的该文档，不同形式的参与贡献都是受欢迎的。

---

<h2 id="2">2. 准备</h2>

在接下来的文章中，你需要安装下面提到的Linux依赖包：

> 一些包在[Fedora](https://getfedora.org/)，[CentOS](https://www.centos.org/)和[RHEL](https://www.redhat.com/en/technologies/linux-platforms)系统中是默认安装的。下面命令中的包列出来了本文章完整的依赖。

On Fedora，CentOS 8，and RHEL 8：

```shell
dnf install gcc \
    rpm-build \
    rpm-devel \
    rpmlint \
    make python bash \
    coreutils diffutils patch \
    rpmdevtools
```

On CentOS 7 and RHEL 7：

```shell
yum install gcc \
    rpm-build \
    rpm-devel \
    rpmlint \
    make python bash \
    coreutils diffutils patch \
    rpmdevtools
```

---

<h2 id="3">3. 为什么要打RPM包？</h2>

`RPM Package Manager(RPM)`是运行在红帽企业版Linux、CentOS和Fedora上的一个包管理系统。`RPM`使得在这里Linux发行版上发布、管理和更新软件变得更加方便。许多软件的提供商用传统的压缩包的方式发布软件，但是使用RPM进行包管理有如下优点：

**安装、重装、卸载、升级和校验包**

用户可以使用标准的包管理工具（例如`yum`或者`PackageKit`）来安装、重装、卸载、升级和检验你的RPM包。

**使用一个数据库来索引和校验包**

因为`RPM`维护着一个所安装的RPM包和文件的数据库，用户可以在系统上索引和校验包。

**使用元信息来描述包安装命令等**

每一个RPM包都包含描述该包的组成、版本、发布、大小、项目主页和安装命令等元信息。

**可以将原始软件源代码打包成RPM源文件和二进制包**

RPM支持你将原始的软件源代码打包成RPM源文件和二进制包。RPM源文件包中包含了原始软件源代码和各种补丁文件。这种设计可以让你在软件有新的版本发布的时候更方便的维护包。

**将包加入到Yum仓库中**

你可以将RPM包加入到一个Yum仓库中以便其他人可以找到并部署你的软件。

**将你的包进行数字签名**

使用`GPG`签名密钥，你可以将你的RPM包进行数字签名，这样你的包的用户就能够确认包的可靠性。

---

<h2 id="4">4. 你打的第一个RPM包</h2>

创建一个RPM包是复杂的，这里完整说明了如何处理`RPM Spec`文件，其中的一些细节会简化或者跳过。

```shell
Name:       hello-world
Version:    1
Release:    1
Summary:    Most simple RPM package
License:    FIXME

%description
This is my first RPM package, which does nothing.

%prep
# we have no source, so nothing here

%build
cat > hello-world.sh <<EOF
#!/usr/bin/bash
echo Hello world
EOF

%install
mkdir -p %{buildroot}/usr/bin/
install -m 755 hello-world.sh %{buildroot}/usr/bin/hello-world.sh

%files
/usr/bin/hello-world.sh

%changelog
# let's skip this for now
```

保存上面的内容到`hello-world.spec`文件中，执行如下命令：

```shell
$ rpmdev-setuptree
$ rpmbuild -ba hello-world.spec

```

命令`rpm-dev setuptree`会在`$HOME`中创建一些必要的目录，这些目录不会被删除，所以该命令执行一次即可。

命令`rpmbuild`创建了rpm包，该命令输出类似：

```shell
Executing(%prep): /bin/sh -e /var/tmp/rpm-tmp.2PwGew
+ umask 022
+ cd /root/rpmbuild/BUILD
+ RPM_EC=0
++ jobs -p
+ exit 0
Executing(%build): /bin/sh -e /var/tmp/rpm-tmp.GSkFSU
...
Executing(%clean): /bin/sh -e /var/tmp/rpm-tmp.C2EpwV
+ umask 022
+ cd /root/rpmbuild/BUILD
+ /usr/bin/rm -rf /root/rpmbuild/BUILDROOT/hello-world-1-1.ky10.aarch64
+ RPM_EC=0
++ jobs -p
+ exit 0
```

`~/rpmbuild/RPMS/X86_64/hello-world-1-1.x86_64.rpm`就是你打的第一个RPM包，该包可以在系统中安装测试。

---

<h2 id="5">5. 准备要打包的程序</h2>

本章主要介绍源代码和软件的生成，是后续打RPM包要了解的基础知识。

<h3 id="5.1">5.1 源代码是什么？</h3>

**源代码**是计算机能够读懂的指令，描述如何进行一次计算。源代码通过[编程语言](https://en.wikipedia.org/wiki/Programming_language)来表达。本教程给出了使用不同编程语言编写的3个版本的Hello World程序，这3中不同语言的程序会用不同的方式打包，覆盖了RPM打包3个主要的操作方法。

> 世界上有数以千计的编程语言，本文档只举例了其中的3中，但是对于整体概念性的了解就足够了。

[bash](https://www.gnu.org/software/bash/)语言版的Hello World：

**bello**

```shell
#!/binb/bash

printf "Hello World\n"
```

[Python](https://www.python.org/)语言版的Hello World：

**pello.py**

```python
#!/usr/bin/env python

print("Hello World")
```

[C](https://en.wikipedia.org/wiki/C_%28programming_language%29)语言版本的Hello World：

**cello.c**

```c
#include <stdio.h>

int main(void) {
    printf "Hello World\n";
    return 0;
}
```

这3个版本的程序都是用命令行输出一个Hello World。

> 对于打包程序来说，了解如何编程不是必要的，但是如果知道的话会很有帮助。

<h3 id="5.2">5.2 程序是怎么制作的？</h3>

将人类能够读懂的源代码转化为计算机实际能够一行行执行的机器指令有很多方法，总结起来共有3种：

1. 程序是本地编译的（natively compiled）。
2. 程序是通过原生解释器进行解释的（interpreted by raw interpreting）。
3. 程序是通过预编译好的byte文件进行解释的（interpreted by byte compiling）

<h4 id="5.2.1">5.2.1 本地编译型代码</h4>

**本地编译型**程序是将源代码编译为机器代码，也就是一个可执行的二进制文件，这样的程序是独立的。

这样的程序所打的RPM包是[架构](https://en.wikipedia.org/wiki/Microarchitecture)相关的，也就是说你使用64-bit（`x86_64`）的ADM或者Intel处理器编译的这类程序不能够在32-bit的处理器上运行，所生成的RPM包名上有处理器架构信息。

<h4 id="5.2.2">5.2.2 解释型代码</h4>

一些编程语言，例如bash或者Python，不将程序编译为机器代码，而是通过一个[解释器](https://en.wikipedia.org/wiki/Interpreter_%28computing%29)或者虚拟机在没有转化源代码的情况下一行行执行代码。

完全用解释型语言写的程序是架构无关的，因此这样程序的RPM包名称中会有`noarch`字样。

解释型语言是字节码编译解释的或者是原生解释的，这两种类型在程序构建和打包流程上是不同的。

**Raw-interpreted programs**

原生解释型语言程序不需要进行编译，它们可以直接在解释器上运行。

**Byte-compiled programs**

字节码编译解释型语言程序需要预编译成字节码，然后在对应语言虚拟机上运行。

> 一些编程语言既可以选择原生解释也可以是字节码编译解释。

<h3 id="5.3">5.3 从源代码构建程序</h3>

这一节内容介绍如何从源代码构建程序。

- 对于编译型编程语言写的程序，源代码需要经过**构建**产生机器码。不同的编程语言中这个过程可能叫做**compiling**或者**translating**，构建的结果是可以被程序员指定运行任务的**run**或者**executed**的程序。
- 对于解释型编程语言写的程序，源代码不需要构建，直接可以有运行。
- 对于字节码编译解释型编程语言写的程序，源代码首先需要编译成字节码，然后再在叫做对应语言的虚拟机上运行。

<h4 id="5.3.1">5.3.1 本地编译型代码</h4>

这个例子中，你会创建一个`cello.c`的C语言程序，并将之转化为可执行程序。

**cello.c**

```c
#include <stdio.h>

int main(void){
    printf("Hello World\n")
    return 0
}
```

**手动构建**

调用C语言的`GUN`编译工具[GCC](https://gcc.gnu.org/)将源代码转化为二进制：

```shell
gcc -g -o cello cello.c
```

执行二进制程序cello：

```shell
./cello

Hello World
```

就是这么简单，你已经成功从源代码构建并运行了本地编译型程序。

**自动构建**

除了手动构建之外，你还可以选择自动构建，这也是大型项目的最佳实践。自动构建需要创建一个`Makefile`文件并通过运行GNU [make](http://www.gnu.org/software/make/)工具完成。

在cello.c源代码的同级目录创建一个文件并命名为`Makefile`：

```shell
cello:
    gcc -g -o cello cello.c
clean:
    rm cello
```

执行`make`命令就可以构建程序：

```shell
$ make
make: 'cello' is up to date.
```

因为已经有一个构建结构存在，先删除然后再次`make`：

```shell
$ make clean
rm cello

$ make
gcc -g -o cello cello.c
```

紧接着一次构建之后再次构建，不会执行任何内容

```shell
$ make
make: 'cello' is up to date.
```

最后，执行程序：

```shell
$ ./cello
Hello World
```

现在你学会手动或者使用工具构建一个程序了。

<h4 id="5.3.2">5.3.2 解释型代码</h4>

接下来的两个例子展示用Python语言写的字节码编译解释型程序和用bash编写的原生解释型程序。

> 下面两个例子中文件开头的的`#!`是所谓的[shebang](https://en.wikipedia.org/wiki/Shebang_%28Unix%29)，并不是编程语言源代码的一部分。**shebang**能够让文本文件可执行，系统程序会解析含有**shebang**的文本并找到二进制可执行程序的path，这个可执行程序就作为对应编程语言的解释器。

**字节码编译型代码**

这个例子中，你将编译用python语言写的`pello.py`程序，然后用Python语言的虚拟机执行编译后生成第字节码。Python语言源代码可以原生解释运行，但是字节码编译解释的解释的方式执行更快，因此RPM打包一般更倾向于字节码编译版本推送给大家使用。

**pello.py**

```python
#!/usr/bin/env python

print("Hello World")
```

不同的字节码编译解释型语言的编译运行流程是不同的，它取决于这个编程语言、语言虚拟机、相关工具以及语言定义的流程。

> 实践中Python往往是字节码编译解释运行的，但是不是这里展示的这种方式。接下来的流程为了简单没有遵照社区的标准。对于实际的Python项目，参考[Software Packing and Distribution](https://docs.python.org/2/library/distribution.html)。

字节码编译`pello.py`：

```python
$ python -m compileall pello.py

$ file pello.pyc
pello.pyc: python 2.7 byte-compiled
```

运行字节码`pello.pyc`：

```shell
$ python pello.pyc
Hello World
```

**原生解释型程序**

在这个例子中，你会原生解释运行bash语言写的**bello**程序。

**bello**

```shell
#!/bin/bash

printf "Hello World\n"
```

使用shell脚本语言（例如bash）写的程序是原生解释运行的，因此你只需要将源代码赋予可执行权限并且运行：

```shell
$ chmod +x bello
$ ./bello
Hello World
```

<h3 id="5.4">5.4 给程序打补丁</h3>

一个补丁程序是可以用来更新其他源代码的源代码。补丁使用`diff`文件格式，因为它代表了程序两个不同版本之间的差异。`diff`文件需要用`diff`工具来创建，它可以被[patch](http://savannah.gnu.org/projects/patch/)工具用来给源代码打补丁。

> 软件开发人员大都使用版本控制软件，例如[git](https://git-scm.com/)，来管理代码仓库，这类软件针对软件打补丁提供了自己的方法。

在接下来的示例中，我们使用`diff`创建源文件的补丁，再使用`patch`对源文件打补丁。对程序打补丁会在后续RPM打包[处理SPEC files](#6.1.7)的时候用到。

打补丁和RPM打包有什么关系？在打包的时候，不是直接更改源代码，而是在源文件上使用补丁包。

1). 保留原始源代码内容：

```shell
diff -Naur cello.c.orig cello.c > cello-output-first-patch.patch
```

2). 修改`cello.c`内容：

```c
#include <stdio.h>

int main(void) {
    printf("Hello World from my very first patch!\n");
    return 0;
}
```

3). 使用`diff`工具生成patch文件：

> 我们在`diff`工具使用的时候指定了一些列的参数，获取更多的内容可以参考[diff man page](https://man7.org/linux/man-pages/man1/diff.1.html)。

```shell
$ diff -Naur cello.c.orig cello.c
--- cello.c.orig        2016-05-26 17:21:30.478523360 -0500
+++ cello.c     2016-05-27 14:53:20.668588245 -0500
@@ -1,6 +1,6 @@
 #include<stdio.h>

 int main(void){
-    printf("Hello World!\n");
+    printf("Hello World from my very first patch!\n");
     return 0;
 }
```

`-`开头的行将会被删除，替换为`+`开头的行。

4). 将patch内容保存在文件中：

```shell
$ diff -Naur cello.c.orig cello.c > cello-output-first-patch.patch
```

5). 恢复原始的`cello.c`：

```shell
$ cp cello.c.orig cello.c
```

我们保留了原始的`cello.c`，因为当一个rpm包构建的时候，使用的是原始文件，而不是修改过的。更多信息访问[处理SPEC files](#6.1.7)。

使用`cello-output-first-patch.patch`文件给`cello.c`程序打补丁，需要将补丁文件重定向到`patch`程序就可以：

```shell
$ patch < cello-output-first-patch.patch
patching file cello.c
```

现在`cello.c`的内容为：

```shell
$ cat cello.c
#include<stdio.h>

int main(void){
    printf("Hello World from my very first patch!\n");
    return 0;
}
```

可以构建并且执行打补丁后的`cello.c`：

```shell
$ make clean
rm cello

$ make
gcc -g -o cello cello.c

$ ./cello
Hello World from my very first patch!
```

现在你学会了创建一个patch，给一个程序打patch，构建打补丁后的程序并运行了。

<h3 id="5.5">5.5 安装Arbitrary Artifacts</h3>

[Linux](https://en.wikipedia.org/wiki/Linux)和其他Unix-like操作系统的好处是[Filesystem Hierarchy Standard(FHS)](https://en.wikipedia.org/wiki/Filesystem_Hierarchy_Standard)，它规定了什么样的文件应该放置在什么样的目录中。通过RPM包安装的软件也应该遵照`FHS`的约定，比如，一个可执行文件应该放置在系统的[PATH](https://en.wikipedia.org/wiki/PATH_%28variable%29)变量中。

本文章中，`Arbitrary Artifacts`指的是任何通过RPM包安装在系统中的内容。对于RPM包和系统来说，它可以是一个脚本、一个从源代码编译的二进制文件或者是提前编译好的二进制及任何其他文件。

我们将介绍把`Arbitrary Artifacts`安装到系统的两种方法：分别是`install`和`make install`命令。

<h4 id="5.5.1">5.5.1 使用install命令</h4>

有时候使用`GNU make`命令自动化工具不是最优的选择，例如，如果你的包很简单，没有什么别的内容，这种情况下，经常使用`install`（由系统[coreutils](http://www.gnu.org/software/coreutils/coreutils.html)提供）命令将`Artifact`放置在文件系统的指定目录中并赋予一系列的权限。

接下来的例子将使用我们之前创建的`bello`文件作为`arbitrary artifact`进行安装。注意你需要在命令中指定[sudo](http://www.sudo.ws/)权限或者使用root用户（删除命令中sudo部分）来执行这个命令。

这个例子将`bello`文件安装在`/usr/bin/`目录下并赋予可执行的权限。

脚本是：

```shell
sudo install -m 0755 bello /usr/bin/bello
```

现在`bello`在你的系统`$PATH`变量路径中来，因此你可以不指定全命令路径的情况下在系统的任何目录执行`bello`：

```shell
$ cd ~

$ bello
Hello World
```

<h4 id="5.5.2">5.5.2 使用make install命令</h4>

一个自动安装程序到系统中的方法是`make install`命令，它需要你在`Makefile`中指定如何安装`arbitrary artifact`到系统中。

> `Makefile`经常是由开发人员提供，而不是打包的人。

在Makefile中添加`install`部分：

```shell
cello:
        gcc -g -o cello cello.c

clean:
        rm cello

install:
        mkdir -p $(DESTDIR)/usr/bin
        install -m 0755 cello $(DESTDIR)/usr/bin/cello
```

<h3 id="5.6">5.6 准备打包的源代码</h3>

> 这一部分中的代码可以在[这里]($COPY$)找到。

开发人员常常会用压缩包的方式发布源代码并用来创建RPM包，这一部分你讲学习制作这样的压缩包。

> 创建源代码压缩包往往不是RPM打包人员做的事情，而是由开发人员负责。RPM打包人员打包时会拿到准备好的压缩包。

软件应该用某一个[license]($COPY$)发布，例如我们将要使用的[GPLv3]($COPY$)。每一个软件包当中应该有一个`LICENSE`文件指定license的内容。RPM打包人员在打包时要处理license文件。

接下来的示例使用的license内容如下：

```shell
$COPY$
```

<h3 id="5.7">5.7 将源代码放入压缩文件中</h3>

接下来的示例，我们分别将三个不同语言版本的Hello World放在三个[gzip]($COPY$)压缩包中。软件总是用这种方式发布并进一步打包。

<h4 id="5.7.1">5.7.1 bello</h4>

`bello`程序使用[bash]($COPY$)语言实现Hello World，源代码中仅仅包含bello bash脚本。因此，结果的tar.gz压缩包中除了LICENSE文件只包含一个文件。我们假设程序的版本是`0.1`。

准备bello项目的发布包：

1). 将文件放入单独的一个目录中：

```shell
$COPY$
```

2). 将目录打成压缩包并移动到`~/rpmbuild/SOURCES/`目录中：

```shell
$COPY$
```

<h4 id="5.7.2">5.7.2 pello</h4>

`pello`程序使用[Python]($COPY$)语言实现Hello World，源代码中仅仅包含`python.py`程序。因此，结果的tar.gz压缩包中除了LICENSE文件只包含一个文件。我们假设程序的版本是`0.1.1`。

准备pello项目的发布包：

1). 将文件放入单独的一个目录中：

```shell
$COPY$
```

2). 将目录打成压缩包并移动到`~/rpmbuild/SOURCES/`目录中：

```shell
$COPY$
```

<h4 id="5.7.3">5.7.3 cello</h4>

`cello`程序使用[c]($COPY$)语言实现Hello World，源代码中仅仅包含`cello.c`和`Makefile`文件。因此，结果的tar.gz压缩包中除了LICENSE文件只包含两个文件。我们假设程序的版本是`1.0`。

> 补丁包没有和发布包一起放在压缩文件中，RPM打包人员在RPM构建后进行程序打补丁。补丁包和`.tar.gz`一起放在`~/rpmbuild/SOURCES/`目录中。

准备cello项目的发布包：

1). 将文件放入单独的一个目录中：

```shell
$COPY$
```

2). 将目录打成压缩包并移动到`~/rpmbuild/SOURCES/`目录中：

```shell
$COPY$
```

3). 添加patch

```shell
$COPY$
```

现在要打RPM包的源代码都准备好了。

---

<h2 id="6">6. 打包程序</h2>

本指南介绍红帽系列Linux发行版的RPM打包，主要包括：

- Fedora
- Centos
- Red Hat Enterprise Linux(RHEL)

这些Linux发行版使用rpm包。

尽管我们的目标环境重要是上述Linux发行版，本指南适合所有[RPM based]($COPY$)的发行版。需要注意的是本指南中使用的命令需要适配为发行版特定的方式。

本指南假设你没有任何给操作系统（Linux或者其他）打包软件的经验。

> 如果你不了解什么是一个软件RPM包或者GUN/Linux发行版，可以访问[Linux]($COPY)和[Package Manager]($COPY)进行了解。

<h3 id="6.1">6.1 RPM Packages</h3>

这一部分中会介绍RPM打包的基础，高级主题请访问[Advanced Topics]($COPY$)。

<h4 id="6.1.1">6.1.1 什么是RPM包？</h4>

RPM包是一个包含其他文件和系统须知元信息的文件。具体来说，RPM包中包含[cpio]($COPY)压缩文件和RPM头，压缩文件就是文件的压缩包，RPM头包含了该RPM包的元信息。RPM打包人员利用包元信息决定如何解决依赖关系、在哪些路径安装软件和其他信息。

有两种类型的RPM包：

- 源代码RPM包(SRPM)
- 二进制RPM包

源代码RPM包和二进制RPM包有同样的文件格式和操作方式，但是它们有不同的内容和用途。一个源代码RPM包含源代码和一个`SPEC`文件，也有可能包含补丁文件，用来描述如何将源代码打包为RPM。二进制RPM包是从源代码和补丁构建成的二进制文件。

<h4 id="6.1.2">6.1.2 RPM Packaging工具</h4>

在[2. 准备](#2)章节中安装的rpmdevtools包提供了打包RPM的一些工具，列出来这些工具可以执行：

```shell
$COPY$
```

查看这些工具的详细使用方法可以看它们对应的`man page`。

<h4 id="6.1.3">6.1.3 RPM Packaging Workspace</h4>

创建RPM打包workspace的目录结构需要使用`rpmdev-setuptree`工具：

```shell
$COPY$
```

创建的目录解释如下：



<h4 id="6.1.4">6.1.4 什么是SPEC File？</h4>



<h4 id="6.1.5">6.1.5 BuildRoots</h4>



<h4 id="6.1.6">6.1.6 RPM Macros</h4>



<h4 id="6.1.7">6.1.7 处理SPEC files</h4>



<h3 id="6.2">6.2 构建RPM包</h3>



<h4 id="6.2.1">6.2.1 RPMs源代码</h4>



<h4 id="6.2.2">6.2.2 RPMs二进制包</h4>



<h3 id="6.3">6.3 校验RPMs完整性</h3>



<h4 id="6.3.1">6.3.1 校验bello SPEC File</h4>



<h4 id="6.3.2">6.3.2 校验bello Binary RPM</h4>



<h4 id="6.3.3">6.3.3 校验pello SPEC File</h4>



<h4 id="6.3.4">6.3.4 校验pello Binary RPM</h4>



<h4 id="6.3.5">6.3.5 校验cello SPEC File</h4>



<h4 id="6.3.6">6.3.6 校验cello Binary RPM</h4>



---
