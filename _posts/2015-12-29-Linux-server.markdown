---
layout: post
title: Linux服务器
---

### 目录

- [1 Linux基础](#1)
    - [l.1 Linux操作系统基础](#1.1)
    - [1.2 用户、组和权限](#1.2)
- [2 Linux中级](#2)
- [3 Linux高级](#3)

---

<h2 id="1">1 Linux基础</h2> 

<h3 id="1.1">1.1 操作系统基础</h3>

### 计算机的体系结构

1. 运算器
2. 控制器
3. 储存器(内存)，是一种平面编址的存储单元
4. 输入设备
5. 输出设备

程序=指令+数据

硬件架构：ARM、x86、x64、安腾、alpha、UltraSparc、Power、m68000,m68k、PowerPc，这些是常见的CPU系列

OS：Windows、Linux、Unix

**有了操作系统之后，任何程序都不能和硬件打交道，都是通过系统调用(system call)来完成，但是由于系统调用过于复杂，于是就产生了库(API)，库是可执行程序，但是没有入口，只有调用之后才能够执行。操作系统的内核(kernel)不单独执行任何一个程序，只是将硬件的功能抽象化出来提供一个接口，能更好的实现硬件的性能**

> 应用程序可以直接进行内核调用，也可以进行库的调用

#### 内核的功能：

- 进程管理
- 内存管理
- 文件系统
- 网络功能
- 硬件驱动
- 安全机制

> 汇编语言是和硬件联系非常紧密的，高级语言与硬件的联系没有那么紧密，是基于API的

### Linux思想

#### Linux的基本原则：

1. 由目的单一小程序组成，组合小程序完成复杂的任务
2. 一切皆文件
3. 尽量避免捕获用户接口
4. 配置文件保存为纯文本格式

> 用户要想跟操作系统进行交互，必须通过shell进行实现，shell可以是图形用户界面(GUI)的，也可以是命令行模式(CLI)

### Linux常用命令

命令提示符(prompt)：普通的用户是：`$`，root用户是：`#`

命令的的格式：`命令 选项 参数`，选项分为短选项和长选项，短选项：`-h`，长选项：`--help`；多个选项可以组合起来，例如：`ls -a -l 或者 ls -al`，长选项通常不能组合

GUI：Gnome(C语言编写)、KDE(C++语言编写)、XFace；Linux的只是一个外围的程序，与windows不同，windows不使用桌面系统就不使用，而Linux不是这样的

CLI：sh、bash、csh、zsh、ksh、tcsh

切换用户：`su [-l] user1`，表示切换到user1用户，su(swith user)

> windows环境下的`.dll`：dynamic link library指的是动态链接库，同样在Linux环境下也有同样的库文件：`.so`即shared object，共享对象

操作系统的组成结构：内核 + 库 + shell(内核是对硬件)

认证机制：Authentication，密码、指纹...；授权：Authorization；审计：Audition(日志)

程序之所以能够执行，是因为程序存在一个入口，在文件的开头必须有一些特殊的字符，这些字符就是程序的"魔数"，例如：`#!/bin/bash`

目录其实上也是一种文件，只是一种映射路径的文件。操作系统使用路径是为了层次化管理所有的文件，其实是一种数据结构(自己的理解)

Linux的文件类型：`-`表示普通文件(file)；`d`表示目录文件(directory)；`b`表示块设备文件(block)；`c`表示字符设备文件(character)；`l`表示符号链接文件也就是软链接文件(symbolic link file)；`p`表示命令管道文件(pipe)；`s`表示套接字文件(socket)

```
-: (文件类型)
rwxr-xr-x: (mode也就是权限)
1: (文件硬链接的次数) 
root: (owner) root: (group)
803: (size，默认单位是字节byte) 
Apr  5  2010(时间戳timestamp，这里显示的是最近一次别修个的时间)
```

> 时间戳：访问(access)；修改(modify)；改变(change)，是文件的内容发生改变；matadata(元数据)也就是文件的属性

`ls -h`可以记性单位换算，h是human-readable的缩写，表示人类容易读取的文件

"缓存为王，catcher is king"，计算机中到处都能看到缓存的影子，在Linux中，第一次使用的命令，是查找的路径，当第二次再次使用的时候，直接从缓存中读取，可以通过`hash`来查找之前使用过的所用命令的缓存，缓存使用哈希列表来储存数据，哈希表是键值对，在哈希表中查找数据的速度是O(1)的，缓存是使系统加速的

Linux的时间分为：系统时间和硬件时间

Linux的命令分为：外部命令和内部命令。内部命令一般使用`help COMMAND`；外部命令一般使用`COMMAND --help|-h`

manual：`man 命令`，man命令显示是使用章节的；1章：用户命令(/bin；/usr/bin；/usr/local/bin)；2章：系统调用；3章：库用户；4章：特殊文件(设备文件)；5章：文件格式(配置文件的语法)；6章：语法；7章：杂项；8管理命令(/sbin，/usr/sbin，/usr/local/sbin)

> 命令中中括号表示可选，尖括弧号表示必选，竖线表示多选1，三个点表示可以出现多次。NAME表示命令名称及功能简要说明；SYNOPSIS：表示用法说明，包括可用的选项；DESCRIPTION：命令功能的详细说明，可以包括每一个选项的意义；OPTIONS：说明每一个选项的意义；Files：此命令相关的配置文件；BUGS：相关bug；EXAMPLES：相关示例；SEE ALSO：另外参照

> 翻屏：向后翻一页`SPACE`；想前翻一页`b`；向后翻一行`ENTER`；向前翻一行`k`

> 查找：`/keywords`表示向后查找关键字；`?keywords`表示向前查找关键字；`n`表示下一个查询的关键字；`N`表示上一个查询的关键字

`hwclock -s`将硬件的时间同步到系统当中；`hwclock -w`将系统的时间同步到硬件当中

`info COMAND`可以获得帮助，是man命令的增强版本

`/usr/share/doc`保存的文档，这里面也可以获得帮助

`cal`：calendar，可以显示日历

`type COMMAND`可以查看是内部命令还是外部命令；`echo`默认打印换行符；`printf`默认不打印换行符

`file`可以查看文件的格式，在Linux系统中，`ELF`是可执行和可链接格式(Executable and Linkable Format)

### Linux的文件系统

`/boot`系统启动相关的文件，如：内核、initrd，以及grub(bootloader)

`/dev`设备文件(Linux重要思想，一切皆文件)

> 设备文件分为两种，一种是`块设备`，是随机访问的，数据块(如硬盘)；另外一种是`字符设备`，是线性访问，按字符为单位(如硬盘，显示屏，鼠标)

> 设备文件是没有大小的，没有数据，只有元数据，是一个入口

`/etc`配置文件

`/home`用户的家目录，每一个用户的家目录通常为`/home/USERNAME`

`/root`管理员的家目录

`/lib`库文件，库分为静态库(.a，直接链接到程序的地址空间中，是作为程序的一部分的运行的，事件库文件包含进来的)和动态库(.so(shared object)，windows是.dll，在内存中只需要一份即可)

> `/lib/modules`是内核模块文件

`/media`表示挂载点目录，挂载移动设备

`/mnt`表示挂载点目录，挂载临时文件系统

`/misc`表示杂项，没法归类就放在这里

`/opt`表示可选目录，第三方程序安装目录

`/proc`伪文件系统，可以理解为内核映射文件，记录了内核的行动，本身是空目录，随着系统的启动而生成

`/sys`伪文件系统，跟硬件设备属性相关的映射文件，本身是空目录，随着系统的启动而生成

`/tmp`临近文件

`/var`可变化的文件

`/bin`可执行文件，存放的是用户命令

`/sibn`可执行文件，存放的是管理员命令

`/usr`，universal shared read-only，全局的共享的只读的文件，文件下有/bin /sbin /lib

`/usr/local`第三方软件安装目录(非关键性的程序，是站在系统的立场上来说)

#### 文件名的命名规则

1. 长度不能超过255个字符
2. 不能使用`/`当文件名
3. 严格区分大小写

#### Linux系统学习的内容：

- 目录管理
- 运行程序
- 设备管理
- 软件管理
- 进程管理
- 网络管理

#### 命令行展开

Linux命令中会有`{}`展开的操作，比如说`mkdir /mnt/test{x/m,y}`，会在逗号那里隔开。递归创建文件夹: `mkdir -p /mnt/test/a/b`

递归的创建目录：`makdir -pv /mnt/test{x/m,y}`

`stat`命令，可以查看文件的时间戳

`install`命令可以用来复制文件和修改属性，`install -d`可以用来创建目录，并且可以创建多个目录

Linux的发行版：Fedooa、RedHat(CentOS)、SUSE、Debian(Ubuntn、Mint)、Gentoo、LFS(Linux From Scratch)

> 编译时将源程序转化为硬件可以执行的二进制程序，和硬件是紧密相连的

王国维的三重境界：昨夜西风凋碧树，独上高楼，望断天涯路；衣带渐宽终不悔，为伊消得人憔悴；众里寻他千百度，蓦然回首，那人却在灯火阑珊处

目录管理常用命令：`ls`、`cd`、`pwd`、`mkdir`、`tree`

文件管理常用命令：`touch`、`stat`、`file`、`rm`、`cp`、`mv`、`nano` `rmdir`

日期时间：`date`、`clock`、`hwclock`、`cal`

查看文本：`cat`、`tac`、`more`、`less`、`head`、`tail`

> `tail -f`查看文件尾部不退出，等待显示后面追加(append)的新内容

文本处理：`cut`、`join`、`sed`、`awk`

`cut`，`cut -d`表示字段分隔符，默认是空格；`cut -f`指定要显示的字段，其或后面可以跟数字表示第几个字段，比如("1,3"表示第1个和第3个字段，"1-3"表示1、2、3三个字段)

文本排序：

`sort`，排序(默认的排列规则是ASCII码)，`sort -n`对数值进行排序；`sort -r`逆序排列；`sort -t`序段分隔符；`sort -k`哪个字段为基准；`sort -u`重复内容不显示；`sort -f`排序时忽略大小写，也就是不区分大小写

`uniq`，只有相邻的重复项才会被忽略，不相邻的认为不一样；`uniq -c`显示每一行重复的次数

文本统计(wc：word count)：`wc`统计结果分别表示：行数(-l)，单词数(-w)，字节数(-c)

字符处理命令：`tr`，是逐个字符进行处理的，可以将小写字母换成大写字母，如：`tr 'ab' 'AB'`；`tr -d`表示删除字符集中出现的所有字符

### bash

进程：在每个进程看来，在主机上只存在内核和当前进程；进程是程序的副本，是程序执行的实例

shell中也可以出现子shell，当在shell中在启动`bash`后，就会产生一个子shell，但是每一个shell都是一个进程，即使是父子关系，也彼此之间是不一定有关联关系的

bash(支持)：命令历史；管道、重定向；命令别名；命令行编辑；命令行展开；文件名通配；变量；编程

#### 命令行编辑

`ctrl + a`：跳到命令行首；`ctrl + e`：跳到命令行尾

`ctrl + u`：删除光标至命令行首的内容；`ctrl + k`：删除光标至命令行尾的内容

`ctrl + l`：清屏，或者使用命令：`clear`

`history`，`history -w`保存命令历史至历史文件中

`!n`执行命令历史中的第n条命令；`!-n`执行命令历史中的倒数第n个命令；`!!`执行上一条命令；`!string`执行最近的与字符串想匹配的命令；`!$`引用前一个命令的最后一个参数或者使用`Esc, .`

命令别名：在shell中定义的别名，仅仅在当且的shell周期内有效，别名的有效范围是当前shell周期。`alias CMDALIAS='COMMAND [options] [arguements]'`，单独的`alias`能够显示当前的所有系统中定义的命令别名

命令替换：把命令中的某个子命令替换为其执行结果的过程；使用`$(COMMAND)`，也可以使用反引号将命令包括起来

bash中支持的引号：反引号是命令替换；`""`：弱引用，可以实现变量替换；`''`：强引用，不完成变量替换

文件名通配，globbing

`*`表示：任意长度的任意字符；`?`表示：任意单个字符；`[]`：匹配指定范围内的任意单个字符，例如"[abc]、[a-z]、[A-Z]、[a-zA-Z]、[0-9]"；`[^]`表示指定范围之外的任意单个字符，例如"[^0-9]"；特殊的：`[:space:]`表示所有空白字符、`[:punct:]`表示所有的标点符号、`[:lower:]`表示所有的小写字母、`[:upper:]`表示所有的大写字母、`[:alpha:]`表示大小写字母、`[:digit:]`表示数字、`[:alnum:]`表示数字和大小写字母

<h3 id="1.2">1.2 用户、组和权限</h3>

> 用户操作计算机的过程实际上就是发起了一个又一个进程，这一个又一个进程是用户操作计算机的代理，每一个进程都存在自己的安全上下文(secure context)，也就是说谁发起的进程，就以谁的身份来运行

#### 权限

文件：`r`表示可读，可以使用类似cat等命令查看文件内容；`w`表示可写，可以编辑或者删除此文件；`x`表示可执行，可以在命令提示符下当做命令提交给内核运行

文件夹：`r`表示可以对此目录执行ls以列出内部的所有文件；`w`表示可以在此目录创建文件；`x`表示可以使用cd切换到此目录，也可以使用ls -l查看文件的详细信息

用户：UID，`/etc/passwd`；组：GID，`/etc/group`；影子口令：用户`/etc/shadow`，组`/etc/gshadow`

用户类别：管理员：0；普通用户：1-65535，包括系统用户(专门用来运行后台服务进程的用户)：1-499，一般用户：500-65535

用户组类别：管理员组；普通组，包括系统组、一般组

另外一个种用户组类别：基本组(用户的默认组)、私有组(在创建用户的时候没有为其制定所属的组，系统会自动为其创建一个用户名同名的组)、附加组(额外组，默认组以外的其他组)

`/etc/passwd`：account登录名、password密码、UID、GID基本组ID、comment注释、HOME DIR家目录、SHELL用户默认的shell

`/etc/shadow`：account登录名、encrypted加密的密码

> 加密的方法：对称加密(加密和解密使用同一个密码)、公钥加密(每一个密码都成对出现，一个为私钥(secret key)，一个为公钥(public key))、单向加密(散列加密，指纹加密，是提取数据的特征码，常用于数据完整性校验，特征是不可逆，两大特性：雪崩效应，定长输出)

用户管理常用命令：`useradd userdel usermod passwd chsh chfn finger id chage`

组管理常用命令：`groupadd groupdel groupmod gpasswd`

权限管理常用命令：`chown chgrp chmod umask`

`useradd`，`useradd -u`指定UID；`useradd -g GID`指定基本组；`useradd -G GID...`多个，可以有附加组；`useradd -d /path/to/somedirectory`指定家目录；`useradd -c`指定注释信息；`useradd -s /bin/bash`；`useradd -m`；`useradd -r`增加系统用户(没有家目录，ID号在1~499之间)

`userdel`，`userdel -u`...

`finger`查找用户账号信息

`usermod`，`usermod -u`修改UID；`usermod -g`修改基本组GID；`usermod -a -G`为用户追加附加组，单独使用`-G`会覆盖此前的附加组；`usermod -c`修改注释信息；`usermod -d -m`更改用户的家目录，并且将之前家目录中的文件复制到新的家目录；`usermod -s`修改shell；`usermod -L`禁用账户；`usermod -U`解锁账户；`usermod -l`更改用户名

`chsh`更改shell；`chfn`更改finger信息(注释信息)

`passwd`，`passwd --stdin`从标准的输入中读取；`passwd -l`锁定用户；`passwd -u`解锁用户 

`pwck`检查用户账号的完整性

`groupadd`，`groupadd -g GID`增加组，指定GID；`group -r`增加系统组

`groupmod`，`groupmod -g`修改GID；`groupmod -n`修改GROUPNAME

`groupdel`删除组

`gpasswd`给组加密码，组不会用来登录系统；

`newgrp`可以所属组切换为其他组

`chage`修改用户密码过期信息

`chown`改变文件的属主(只有管理员才可以使用此命令)；`chown -R`修改目录及其内部文件的属主；`chown --reference=/path/to/somefile file,...`把这个或者这些文件修改和参考文件一样的属性；`chown OWNER:GROUP`可以同时改变属主和属组，也同样支持reference

`chgrp`修改文件的属组(只有管理员才可以使用此命令)；用法同上

`chmod`修改文件的权限；例子：`chmod 755 /mnt/a.txt`，`chmod ug="rw" /mnt/a.txt`，`chmod u+x,g-x /mnt/a.txt`

练习：

1. 新建一个没有家目录的用户fmz：`useradd -M fmz`

2. 复制/etc/skel为/home/fmz：`cp -r /etc/skel /home/fmz`

3. 改变/home/fmz及其内部文件的属主属组均为fmz：`chown -R fmz:fmz /home/fmz`

4. /home/fmz及其内部的文件，属组和其他用户没有任何访问权限：`chmod -R go= /home/fmz`

> 这个练习也提供了怎么样不通过命令行还创建一个用户，一个用户的信息只是跟三个文件有关：1.`/etc/passwd`、2.`/etc/shadow`、3.`/etc/group`有关

手动添加用户fmz，基本组为fmz(5000)

1. `vi /etc/group`，添加`fmz:x:5000:`
2. `vi /etc/passwd`，添加`fmz:x:5000:5000:Fmz:/home/fmz:/bin/bash`
3. `vi /etc/shadow`，添加`fmz:!!:16802:0:99999:7:::`
4. `cp -r /etc/skel /home/fmz`
5. `chown -R fmz:fmz /home/fmz`
6. `chmod -R go= /home/fmz`
7. 检测是否成功：`su fmz`

手动生成密码：`openssl passwd -1 -salt "12345678"`能生成具有"salt"的密码

`umask`表示显示当前用户umask(遮罩码)，表示用来生成文件或者目录的默认权限，管理员的默认umask是022，所以创建文件的默认权限就是`666-022=644`，创建目录的默认权限是`777-022=755`；普通用户的默认umask是002，所以普通用户创建文件的默认权限是`666-002=664`，创建目录的默认权限是`777-002=775`

注意：文件默认不能具有执行权限，如果按照umask算的结果中具有执行权限，则将其权限加1

站在用户的角度来说，SHELL的类型：一种是登录式的SHELL，包括正常通过终端登录或者`su - USERNAME`或者`su -l USERNAME`；另一种是非登录式SHELL，包括`su USERBNAME`或者图形终端下打开命令窗口或者自动执行的shell脚本

#### bash的配置文件，分为全局配置和个人配置

全局配置：`/etc/profile`、`/etc/profile.d/*.sh`、`/etc/.bashrc`

个人配置：`~/.bash_profile`、`~/.bashrc`

profile类文件用来设定：环境变量、运行命令或者脚本

bashrc类文件用来设定：设定本地变量、定义命令别名

>作用范围越小的月最终生效(当两者同时存在的时候就以小范围的为主)

#### 登录式SHELL如何读取配置文件

`/etc/profile --> /etc/profile.d/*.sh --> ~/.bash_profile --> ~/.bashrc --> /etc/bashrc`

#### 非登录式的SHELL如何读取配置文件

`~/.bashrc --> /etc/bashrc --> /etc/profile.d/*.sh`

`bash`：可以理解为脚本解释器

### I/O重定向

系统设定：默认的输出设备叫做标准输出，STDOUT ，1；默认的输入设备叫做标准输入，STDIN，0；默认的标准错误输出，STDERR，2。

计算机中默认的标准输入是：键盘；默认的标准输出和错误输出是：屏幕、

如果我们想要改变默认输入或者改变默认输出以及默认的错误输出就是所谓的I/O重定向的过程

#### Linux中I/O重定向

`>`：覆盖输出，这样的命令可能是致命的。我们可以使用`set -C`这样就禁止对已经存在的文件使用覆盖重定向，如我在禁止覆盖重定向的情况下还想要强制覆盖就需要使用`ls /etc/passwd >| /tmp/passwd`；`set +C`可以关闭上述功能

`>>`：追加输出

`2>`：表示覆盖错误输出重定向(要认识到，标准输出和标准的错误输出是两个不同的输出流)

`2>>`：表示追加错误输出

`&>`：覆盖的方式重定向标准输出和错误输出到同一个文件

`<`：输入重定向

`<<`：Here Document，表示输出文档。例如`cat >> /mnt/myfile.txt << EOF`

#### 管道

命令管道：把前一个命令的输出作为后一个命令输入，`命令1 | 命令2 | 命令3`，例如：`echo "hello,world | tr 'a-z' 'A-Z'"、`cut -d : -f 3 /etc/passwd |sort -n`

>Linux重要哲学思想之一：组合小命令完成复杂任务

`tee`，read from standard input and write to standard output and files ，例如：`echo "Hello,Fmz." | tr /mnt/hello.out`这样就可以实现既在屏幕上进行标准输出又重定向输出到文件中

练习：

1. 统计/usr/bin目录下文件的个数：`ls /usr/bin | wc -l`

2. 取出当前系统中所有用户的shell，要求每种shell只显示一次，并且按顺序进行显示：`cut -d: -f7 /etc/passwd | sort -u`

3. 如何显示/var/log目录下每个文件的内容类型：`file /var/log/*` 或者 `cd /var/log; file `ls /var/log``这个命令必须切换到当前目录

4. 取出/etc/inittab文件第六行：`head -6 /etc/inittab | tail -1`

5. 取出/etc/passwd文件中倒数第九个用户的用户名和shell，显示在屏幕上并将其保存在/tmp/users文件中：`tail -9 /etc/passwd | head -1 | cut -d: -f1,7 | tee /tmp/users`

6. 显示/etc目录下的所有以pa开头的文件，并统计其个数：`ls -d /etc/pa* |wc -l`

7. 不使用文本编辑器的命令，将alias cls=clear一行的内容添加至当前用户的.bash文件中：`echo "alias cls=clear >> ~/.bashrc"`

### 文本查找

#### grep egrep fgrep

grep根据模式(pattern：文本字符和正则表达式的元字符组合而成匹配条件)搜索文本，并将符合模式的文本行显示出来。`grep -i`表明忽略大小写；`grep --color`匹配到的字符高亮显示；`grep -v`显示没有被匹配到的行；`grep -o`之显示匹配到的字符串

正则表达式：REGlar EXPression，Regex(默认情况下，正则表达式工作在贪婪模式下，也就是说尽可能多的去匹配)

##### 元字符：

`.`匹配任意单个字符

`[]`：指定范围

`[^]`：指定范围外

`*`匹配其前面的字符任意次数

`.*`匹配任意长度的任意字符

`\?`匹配其前面的字符1次或者0次

`\{m,n\}`匹配其前面的字符至少m次数，至多n此

##### 位置锚定

`^`：锚定首行，此字符后面的任意内容必须出现在行首

`$`：锚定行尾，此字符前面的任意内容必须出现在行尾

`^$`：锚定空白行

`\<或\b(放在前面)`：其后面的任意字符必须作为单词首部出现

`\>或\b(放在后面)`：其前面的任意字符必须作为单词的尾部出现

`\<root\>`：表示精确的匹配"root"这个单词

##### 分组

`\(\)`

后向引用：`\1`引用第一个左括号以及与之对应的右括号所包括的所有内容；`\2 \3 \4`

`grep`使用基本正则表达式定义的模式来过滤文本的命令

`grep -A 数字`表示匹配到字符串所在的行后，再显示行后的数字行；`grep -B 数字`表示匹配到字符串所在的行后，再显示行前的数字行；`grep -C 数字`表示匹配到字符串所在的行前后，再显示行前的数字行

#### 增强的正则表达式(与正则表达式不同的列出来)

次数匹配：`?`不需要反斜杠，表示1次或者0此；`+`表示匹配其前面的字符至少一次

分组：`()`不需要反斜杠，`|`表示或者(or)

>`grep -E = egrep`，支持扩展的正则表达式，例如：找出1-255的数字`ifconfig |egrep --color '\<([1-9]|[1-9][0-9]|1[0-9][0-9]|25[0-5])\>'`；找出ifconfig中的ip(粗略)地址：`ifconfig |egrep --color '(\<([0-9]|[1-9][0-9]|1[0-9][0-9]|25[0-5])\>\.){3}\<([0-9]|[1-9][0-9]|1[0-9][0-9]|25[0-5])\>'`

特殊的：`[[:space:]]`表示所有空白字符、`[[:punct:]]`表示所有的标点符号、`[[:lower:]]`表示所有的小写字母、`[[:upper:]]`表示所有的大写字母、`[[:alpha:]]`表示大小写字母、`[[:digit]]`表示数字、`[[:alnum:]]`表示数字和大小写字母

### IP

IPv4分为5类：A B C D E

A类：1-127

B类：128-191

C类：192-223

找出ipconfig中的IP地址：`ifconfig |egrep --color '\<([1-9]|[1-9][0-9]|1[0-9][0-9]|2[01][0-9]|22[0-3])\>(\.\<([0-9]|[1-9][0-9]|1[0-9][0-9]|25[0-4])\>){2}\.\<([1-9]|[1-9][0-9]|1[0-9][0-9]|25[0-4])'`

`fgrep`是一种快速查找的grep，但是不支持正则表达式

### shell编程

计算机语言：机器语言、汇编语言、高级语言

高级语言分为动态语言和静态语言，动态语言是编译型语言，是一种强类型语言(变量)，事先需要转换成可执行格式，例如：C C++ Java C#语言等；动态语言是一种解释型语言，是一种弱类型语言，语言的执行是边解释边执行，例如：PHP SHELL Python Perl等

面向过程：Shell C ；面向对象：Java Python perl C++

内存：编址的储存单元；变量：内存空间，命名

变量类型：事先确定数据的存储格式和长度

Bash变量：环境变量、本地变量(局部变量)、位置变量、特殊变量

定义环境变量：`export VARNAME=VAL`

>环境变量的作用域是当前shell进程及其子进程，本地变量的作用域是整个shell进程，而局部变量的作用域是其所在的代码块

>在Bash中单引号是强引用，是要不进行变量替换的；而双引号是弱引用，是会进行变量替换的

>脚本执行时会启动一个子shell进程；命令中启动的脚本会继承当前shell环境变量；系统自动执行的脚本(非命令行启动)就需要自我定义需要的各个环境变量

位置变量：`$1` `$2`，指的是脚本的第一个参数，第二个参数

特殊变量：例如：`$?`用于保存上一个命令执行状态的返回值(0代表执行成功，1-255表示执行不成功)，也就是说幸福加听总是一样的，而不幸的家庭各有各自的不行；例如：`id root &> /dev/null` `echo $?`；`$#`表示有多少个参数；`$*`表示参数列表；`$@`表示参数列表；`shift`表示在参数之间跳跃，默认跳跃到下一个，可以用`shift -n`来指定

撤销变量：`unset VARNAME`，不仅仅是环境变量，所有的变量都可以通过unset命令来撤销；查看所有的变量(包括本地变量)：`set`；查看所有的环境变量：`printenv` 或者 `export `

为一个变量添加内容：`export PATH=$PATH:/usr/local/apache/bin`

所有的变量都是以字符串的类型来储存的，不能用来加法运算

#### 脚本

脚本是命令的堆砌，按实际需求，结合命令

练习1

1. 添加5个用户，user1,...,user5

2. 每个用户的密码同用户名，而且要求，添加密码完成后不显示passwd命令的执行结果信息

3. 每个用户添加完成后，都要显示用户某某已经添加成功

练习2

1. 使用一个变量保存一个用户名

2. 删除此变量中的用户，且一并删除其家目录

3. 显示"用户删除完成"的信息

#### 条件判断

条件测试类型：整数测试(双目操作)、字符测试、文件测试

条件测试的表达式：`[ expr ]` 或者 `[[ expr ]]` 或者 `test expr`

整数比较：`-eq`，测试两个整数是否相等，例如`$A -eq $B`；`-nq`：测试两个整数是否不相等；`-gt`：测试一个整数是否大于另一个数；`-lt`：测试小于；`-ge`：测试大于等于；`-le`：测试小于等于

命令间的逻辑关系：逻辑与`&&`；逻辑或`||`，逻辑与表示当一个条件为假时，不会执行符号后面的；逻辑或表示当一个条件为真时，不会执行符号后面的

条件判断控制语句：`if 条件判断; then statement1、statement2...`

shell中如何进行算数运算：

1. `let C=$A+$B`

2. `C=$[$A+$B]`

3. `C=$(($A+$B))`

4. expr算数表达式，表达式中各个操作符及算数符之间要有空格，而且要使用命令引用``：C=`expr $A + $B`

`exit #`可以退出脚本，后面的符号即为其状态码。如果脚本没有明确定义退出状态码，那么，最后执行的一条命令的退出码即为脚本的退出状态码

#### 文件测试(单目操作)

`-e FILE`：测试文件是否存在；`-f FILE`：测试文件是否为普通文件；`-d FILE`：测试指定路径是否为目录；`-r FILE`：测试当前用户对指定文件是否有读权限；`-w FILE`：测试当前用户对指定文件是否有写权限；`-x FILE`：测试当前用户对指定文件是否有执行权限

例如：`[ -e /etc/inittab ]`，测试文件是否存在； `[ -x /etc/rc.d/rc.sysinit ]`，测试当前用户对文件是否具有执行权限

测试脚本是否有语法错误：`bash -n file`

`bash -x`可以一步一步的执行程序，这样可以排除bug

#### sed(Stream EDitor)

sed是行编辑器，是逐行处理文本的，把每一行读取到内存(模式空间)当中，在内存中进行处理，处理后再输出到屏幕中。

sed在默认不编辑原文件，仅仅对模式空间内的数据进行处理，处理结束后，将模式空间打印到屏幕上。

sed命令：`sed 'AddressCommand' file...`

Address：1.StartLine~EndLine；2./RegExp/，例如：`/^root/`；3./pattern1/,/pattern2/表示第一次被pattern1匹配到的行开始，至第一次被pattern2匹配到行结束；4.LineNumber表示精确指定的行，`$`表示最后一行，`$-1`表示；5.StartLine,+N表示从startline开始，向后的N行，工N+1行

Command：`d`表示删除符合条件的行；`p`表示打印符合条件的行；`a \string`表示在指定的行后追加新行，内容为string，注意a后面有空格；`i \string`表示在指定的行前面增加新行；`r FILE`将指定文件的内容添加至符合条件的行处；`w FILE`将地址指定范围内的内容另存至指定的文件中；`s/pattern/string/修饰符`表示查找并且替换，默认只替换每一行第一次被模式匹配到的字符串，修饰符`g`表示全局替换，`i`表示忽略大小写，不仅仅可以使用`s///`可以使用`s###`等等只要三个符号形同即可

sed选项：`sed -n`表示不是处空间模式；`sed -i`表示直接修饰原文件；`sed -e`可以同时执行多个脚本；`sed -f /path/to/scripts FILE`表示将一个个脚本执行到后面的文件中；`sed -r`表示使用扩展的正则表达式

sed练习

1. 删除/etc/grub.conf文件中行首的空白符：`sed -r 's/^[[:space:]]//g' /etc/grub.conf`(-r的意思是使用增强的正则表达式)

2. 替换/etc/inittab文件中"id:3:initdefault:"一行中数字为5：`sed 's/\(id:\)[0-9]\(:initdefault:\)/\15\2/' /etc/inittab`

3. 删除/etc/inittab文件中的空白行：`sed '^$d' /etc/inittab`

4. 删除/etc/inittab文件开头的#号：`sed 's/^#//g' /etc/inittab`

5. 删除某文件开头的#号，以及后面的空白字符，但要求#号后面必须有空白字符：`sed -r 's/^#[[:space:]]+//g' /etc/inittab`

6. 取出一个文件路径的目录名称：

字符测试：`==`是否等于；`!=`是否不等于；`>`；`<`；`-n string`测试字符串是否为空；`-z string`测试字符串是否不为空

`bc <<< "scale=2;111/2;"`计算也可以这样使用

#### 循环：进入条件，退出条件

`for` `while` `until`

整数列表：`seq 开始值 等差值 最终值`，默认开始值和等差值都是1，可以缺省

默认情况的命令复制是字符串；声明变量用`declare`，`declare -i`表示声明整数；`declare -x`表示声明环境变量

### VI编辑器

`vi +# FILE`加一个数字表示直接光标处在第#行，`vi + FIlE`直接处在文件的尾行；`vi +/PATTERN`打开文件，定位到第一次被PATTEERN匹配的行首

vi模式：编辑模式(命令模式)；输入模式；末行模式

`ZZ`可以编辑模式下推出；`#hljk`可以定义光标移动的距离

编辑模式下：`w`表示移到下一个单词的词首；`e`表示移到当前或者下一个单词的词尾；`b`表示移动到当前或者上一个单词的词尾

行内跳转：`0`表示跳到严格行首；`^`表示跳到非严格行首；`$`表示跳到行尾

行间跳转：`#G`调至#行，`G`表示跳到最后一行；末行模式下直接给出行号即可：`:#`

翻屏操作：`ctrl+f`：向上翻一屏；`ctrl+b`：向上翻一屏幕；`ctrl+d`：向上翻半屏；`ctrl+u`：向下翻半屏

删除单个字符：`x`；`#x`表示删除光标所在处以及向后的#个字符

删除命令：`d`命令要和跳转命令结合使用，`dw` `de` `db`；`dd`表示删除光标所在行，`#dd`表示删除光标所在行以及其后面的行共#行；末行模式下：`.`表示当前行；`$`表示最后一行，`+#`表示向下的#行

>删除的内容是别保存在缓冲区当中的，可以用来粘贴

粘贴命令：`p`：如果删除的或者复制的为整行的内容，则粘贴到光标的下一行；否则粘贴到光标字符的后面；`P`：如果删除的或者复制的为整行的内容，则粘贴到光标的上一行；否则粘贴到光标字符的前面，支持数字。

复制命令：`y`：同`p`；`Y`：`P`，支持数字

先删除内容，在转换为数据模式，也就是修改：`c`，用法同`d`

替换单个字符：`r `；`R`直接进入替换模式，敲击的内容就替换了

撤销编辑操作：`u`撤销前一次的编辑操作(最多50次)，支持数字：`#u`撤销#此操作

撤销自己的撤销操作：`ctrl + r`

重复前一次的编辑操作：`.`

可视化模式：`v`按字符选取；`V`按矩形选取

查找：`/`从首部开始找；`?`从尾部开始找；`n`下一个；`N`上一个

查找并替换：末行模式下使用`s`命令；如果是全文查找并且替换`1,$s///`或者`%s///`

使用vi编辑多个文件：`vi file1 file2 file3`，在末行模式下输入`:next`表示切换至下一个文件；`:prev`；`:last`；`first`；`:qa`表示全部退出

分屏显示一个文件：`ctrl+w,s`水平拆分窗口；`ctrl+w,v`垂直拆分窗口；在窗口之间切换光标：`ctrl+w,ARROW`

分窗口编辑多个文件：`vi -o file1 file2`水平显示；`vi -O file1 file2`垂直显示

将当前文件的部分内容另存到另一个文件中：末行模式中使用`w`命令，`:ADDR1,ADDR2w /path/to/somewhere`

将另一文件的内容填充到当前的文件中：`:r /path/to/somefile`

跟shell交互：`:! COMMAND`，例如：`:! ls /var`

#### 高级话题

显示或者取消行号：`:set nu`，`:set nonu`

显示或略或者区分字符大小写：`:set ignorecase` `:set ic` 或者 `:set noic`

设置自动缩进：`:set ai` `:set autoindent` 或者 `:set noai`

查找文本高亮显示或者取消：`:set hlsearch` 或者 `set nohlsearch`

设置语法高亮：`:syntax on` 或者 `syntax off`

配置文件：`/etc/vimrc`这个配置文件对所有的用户都生效；`~/.vimrc`只是对自己生效

vim复制当前行到最后一行：`"+yG`

vim删除当前行到最后一行：`dG`

vim代码缩进：`=G`

vim代码块缩进：1.`v` 2.`>`(右缩进)或者 `<`(左缩进)

>vi在非法退出的时候，会在同一个目录下生成一个隐藏文件，这时候打开文件会有警告，如果想去掉警告，只需要删除这个文件即可

组合测试条件：`-a`表示与关系；`-o`表示或关系；`-!`表示非关系 或者 `if [ $# -gt 1 ] && if [ $# -lt 5]`

### 文件查找

`locate`：非实时，模糊匹配，查找是根据全系统文件数据库进行的，`updatedb`，手动生成文件数据库

`find`：实时，精确，支持众多查找标准，遍历指定目录中的所有文件完成查找，速度慢

`find 查找路径 查找标准 查找到以后的处理运作`，查找路径默认为当前目录，查找标准默认路径下的所有文件，查找以后的处理结果默认为显示

匹配标准：`-name 'FILENAME'`文件名统配：`*`任意长度的任意字符 `?` `[]`；`-iname 'FILENAME'`文件匹配时不区分大小写；`-regex PATTERN`基于正则表达式进行文件匹配；`-user USERNAME`根据属主进行查找；`-group GROUPNAME`根据属组进行查找；`-uid UID`根据UID进行查找；`-gid GID`根据GID记性查找；`-nouser`查找没有属主的文件；`-nogroup`查找没有属性的文件；`-type TYPE`根据文件类型记性查找；`-size SIZE`，例如：`find /etc -size 10k`会找出9k到10k的文件

文件查找可以组合条件：`-a`表示与关系；`-o`表示或关系；`-not`表示非关系

根据文件的时间戳来查找：`-mtime`修改时间；`-ctime`改变时间；`-atime`访问时间；`[+|-]#`：`#`表示刚好有#天；`-#`表示#天之内；`#5`表示大于#天；还可以将time换为min表示分钟：`-mmin`或者`-cmin`或者`-amin`

根据文件的权限进行查找：`-perm -MODE`表示精确查找；`-perm /MODE`表示只要有一个匹配即可；`-perm -MODE`表示对应的位必须完全包含MODE才可以显示

运作：`-print`默认是打印出来；`-ls`：类似于`ls -l`的形式显示每一文件的详细信息；`-ok COMMAND {} \`每一个操作都需要用户确认，表示对找到的这些文件执行COMMAND命令；`-exec COMMAND {} \`每一个操作不需要用户确认，只要引用这个文件的文件名就需要使用`{}`

到对运作的结果进行操作还可以使用：`| xargs COMMAND`

练习：

1. 查找/var目录下属主为root并且属组为mail的所有文件：`find /var -user root -a -group mail`

2. 查找/user目录下不属于root，bin或student的文件：`find /usr -not -user root -a -user bin -a -user student` 或者 `find /usr -not \( -user root -o -user student \)`

3. 查找/etc目录下最近一周内修改过且不属于root及student用户的文件：`find /etc mtime -7 -a -not \( -user root -o -user student \)` 或者 `find /etc mtine -7 -a -not -user root -a -not -user student`

4. 查找当前系统上没有属主或者属组且最近一天内曾经被访问过的文件，并将其属主属组均改为root：`find / -not \( -nouser -o -nogroup \) -a -atime -1 -exec chown root:root {} \;`

5. 查找/etc目录大大于1M的文件，并且将其文件名写入/tmp/etc.largfiles文件中：`find /etc -size +1M >> /tmp/etc.largefiles`

6. 查找/etc目录下所有用户没有写权限的文件，显示出其详细信息：`find /etc -not -perm /222 -exec -ls` 或者 `find /etc -not -perm /222 -exec ls -la {} \;`

#### 特殊权限

SUID：运行某程序时，相应的进程的属主是程序文件自身的属主，而不是启动者；`chmod u+s FILE`；`chmod u-s FILE`

SGID：运行某程序时，相应的进程的属组是程序文件自身的属组，而不是启动者；`chmod g+s FILE`；`chmod g-s FILE`

Sticky：在一个公共目录，每一个可以创建文件，删除自己的文件，但不能删除别人的文件；`chmod o+s FILE`；`chmod o-s FILE`

>三者可以组合成二进制的八位数

练习：

root用户创建一个/tmp/project的目录，属主为root，属组为developteam；创建两个用户，用户的属主和属组都是用户名；附加组为developteam，这样两个用户的身份都可以在/tmp/project下创建文件；但是两个用户如法方位对方的文件，这时候要给/tmp/project SGID 特殊权限，也就是通过这个目录创建的文件具有这个目录的属组是这个目录的属组而不是默认的用户属组；这样对彼此的文件就具有的编辑的权限，但是同样也能删除，为了放置别人删除，要对/etc/project 加上Sticky权限

`useradd hadoop` `useradd hbase` `passwd hadoop` `passwd hbase`

`groupadd developteam`

`mkdir /tmp/project` `chown -R :developteam /tmp/project`

`usermod -a -G developteam hadoop`  `usermod -a -G developteam hbase`

>这个时候两个用户分别在自己登陆情况下可以在/tmp/project下创建文件，但是不能笔记对方的文件，如果想要笔记对方的问价需要对目录价SGID权限

`chmod g+s /tmp/project`

>这个时候两个用户在自己登陆时候可以编辑对方文件，但是也可以删除对方文件，如果想要只能删除自己的文件，而不能删除别人的文件，需要对目录加Sticky权限

`chmod o+t /tmp/project`

>这个时候就完成了项目的管理

#### FACL(Filesystem Access Control List，文件系统访问列表)

FACL是利用文件扩展保存额外的访问控制权限

`setfacl`，`setfacl -m`表示设定，`setfacl -m u:UID:perm`；`setfacl -x u:UID`表示取消设定；`setfacl -m g:GID:perm`表示设定设定组的访问权限

>在没有facl特殊设定的情况下，当一个进程来访问一个文件的时候权限判断的过程：首先，判断该文件的属主是否和进程的属主一致，如果一致说明这个文件就是自己的文件，则会以属主的身份进行运行；如果文件的属主进程的属主并不一致，则会判断进程的属主是否属于文件的属组，如果属于就以属组的名义进行；如果进程的属主不属于文件的属组，则会以文件其他组的身份进行

>在设定了facl特殊权限之后，当一个进程来访问一个文件的时候权限的判断过程在上一个过程当中加了两部：文件的属主和进程的属组是否相同 --> 文件的facl中的特殊user是否和进程的属主相同 --> 进程的属主是否属于文件的属组 --> 进程的属主是否属于文件的facl中特殊规定的属组 --> 都不是，以文件的其他组名义进行

>我们也可以给目录设定默认的访问控制列表，在此目录下创建的文件就自动从目录中继承了对该文件的访问控制列表，语法：`setfacl -m d:u:UID:perm`或者`setfacl -m g:u:GID:perm`

终端类型

1. 控制台(console)，一定是连接到主机上的硬件设备(键盘、显示屏)，控制台不是终端，一般情况下，物理终端(pty)就是控制台；

2. pty，物理终端控制台；

3. tty/#，虚拟终端控制台，通常是附属在物理控制台上加上软件虚拟出来的；

4. ttyS，串行终端；

5. pts/#，伪终端；

常用命令

`who`

`whoami`

`w`

`last`：显示当前系统的登录日志；`last -n #`显示最近几次的登录信息；查看的文件是`/var/log/wtmp`

`lastb`：显示错误的的登录尝试，显示的文件是`/var/log/btmp`

`lastlog`：显示每一个用户最近一次成功登录的信息；`lastlog -u USERNAME`显示特定用户最近一次成功登陆的信息

`basename`：取得文件的基名；`$0`执行脚本时，脚本路径的名称

`mail`：发邮件`cat /etc/fstab |mail -s "/etc/fstab" root`或者`mail -s "/etc/fstab" root < /etc/fstab`，这里的`-s`指的是邮件的标题是什么

`RANDOM`：0-32768

随机数生成器(熵池)：/dev/random(真正的随机数)；`/dev/urandom(熵池中随机数用完之后就用然间自动生成随机数)`

>软件生成的随机数是伪随机数

>`bash -x SCRIPTS`可以查看脚本执行的过程

bash脚本

	#!/bin/basih
	#
	DEBUG=0
	ADD=0
	DEL=0
	for I in `seq 0 $#`;do
		if [ $# -gt 0 ];then
			case $1 in
			-v|--verbose)
				DEBUG=1
				shift
				;;
			-h|--help)
				echo "Usage `basename $0` --add user1,user2... --del user1,user2... -v|--verbose -h|--help"
				shift
				;;
			-add)
				ADD=1
				ADDUSER=$2
				shift 2
				;;
			-del)
				DEL=1
				DELUSER=$2
				shift 2
				;;
			*)
				echo "Usage `basename $0` --add user1,user2... --del user1,user2... -v|--verbose -h|--help"
				shift
				exit 7
				;;
			-add)
			esac
		fi
	done
	if [ $ADD -eq 1 ];then
		for USER in `echo $ADDUSER |sed 's/,/ /g'`;do
			if id $USER &> /dev/null;then
				[ $DEBUG -eq 1 ] && echo "$USER exits."
			else
				useradd $USER
				[ $DEBUG -eq 1 ] && echo "user $USER add finished."
			fi
		done
	fi
	if [ $DEL -eq 1 ];then
		for USER in `echo $DELUSER |sed 's/,/ /g'`;do
			if id $USER &> /dev/null;then
				userdel -r $USER
				[ $DEBUG -eq 1 ] && echo "user $USER delete finished."
			else
				[ $DEBUG -eq 1 ] && echo "user $USER not exits."
			fi
		done
	fi

### 磁盘管理

机械式硬盘：U盘、光盘、软盘、硬件、磁带

MBR：master Boot Record，称为主引导记录，在硬盘的0盘面0磁道0扇区共有512bytes，这区域不属于任何操作系统，是全局的，不被操作系统使用的，是独立于操作系统之外的

MBR分为3个段：

1. bootloader(466bytes)，是一段程序，是启动操作系统的重中之重

2. 64bytes，每16个字节可以标识一个主分区

3. 2bytes，magic number，主要是用来标记MBR是否有效

>当电脑刚刚开机的时候，我们的电脑的内存是空的，BIOS中的代码可以映射到内存的最低端，然后根据BIOS中的代码完成计算的自检；当BIOS的指令完成之后(自检完成之后)，计算机会根据BIOS中设定的设备的启动次序(如启动顺序是：光盘 --> 硬盘 --> 软盘)，依次去寻找各个设备的MBR，可能会去读取硬盘的MBR，硬盘的MBR是一段程序，于是这段程序又会被BIOS加载到内存当中，内存当中显示读取的bootloader，bootloader会去读取分区表，根据分区表，去加载有操作系统的分区，然后读取操作系统的内核，内核开始启动自身，然后启动文件系统和程序

>主分区加扩展分区一般不能超过4个，其实扩展分区只是指针，在一个操作系统中只能有一个

对磁盘应该按照柱面进行的，因为文件的存储就是按照柱面记性储存的

笔记本的硬盘的盘片的转速一般是4500转的，而台式机的一般是7200转的

硬盘上完成分区之后就可以在磁盘上进行文件储存了，这就是文件系统的意义了

文件系统：文件系统实际上也是一个软件，也是储存在磁盘上的某个位置的，并不是直接在这个分区上的，但是文件系统的数据是在这个分区上面的，将磁盘分为两段：元数据存储区和数据存储区(是由块组成的)，文件系统是内核应该提供的功能

元数据中存储的内容是：文件的访问权限、文件的大小、时间戳，文件存储的路径

元数据区里面有一块区域称之为：块位图(bitmap)，目的是为了能够更有效率的找到哪些是没有使用块，每一个块只对应块位图中的一位

元数据区里面有另一快位图，使用来储存每一个文件的条目的，也是为了加速寻找哪些空间的空闲的

inode，每一个inode都有一个全局唯一的编号，iNode标识的有：iNode号、文件的权限、属主属组、扩展属性、时间戳、大小以及所储存在磁盘块的地址，但是没有文件名

inode有间接目录，可以多级指向文件

Linux会自己引用到根，也就是在Linux中，根是自引用的

原来文件的名字是在目录中储存的，目录不是容器，目录也就是一个磁盘块，其中有一块区域成为dentry，将之分为两块，第一个块为目录下文件的iNode号，第二块是该目录下可以找到的文件名

block group：块组，实际的硬盘工作的时，使用块组，更加复杂

在一个文件系统中寻找一个文件的过程：

例如：需找`/etc/passwd`文件

1. 由于根是自引用的，所有我们可以在iNode去也中找个根的iNode，进而找到数据区域中对应的根的block，在根的block上储存的有dentry，也就是目录条目，从目录条目中我们可以知道，根下到底有哪些文件，找到对应名字为etc的问价的iNode

2. 根据etc文件的iNode返回元数据区域总的iNode块，去寻找etc文件对应的数据区域的block，在根据block中的dentry找到password文件的iNode

3. 根据passwd的iNode去找到password对应数据区域中的block，也就可以直接访问文件了

>这样就更加深刻的理解了为什么说目录也是文件，只不过是一个映射路径，因为目录也对应有自己的iNode和block以及block中的dentry

在一个文件系统中创建一个新的文件的过程：

例如：假设创建的是一个文件而不是一个目录，也就是一个非目录的文件

1. 扫描bitemap，找到一个未被占用的iNode

2. 根据根和目录路径找到文件父目录的Block中dentry，在dentry中写入创建文件文件名称和上一步找到的空闲iNode

3. 将数据写入iNode对应的block中，或者说数据写入block之后，iNode将之记录下来

在一个文件系统中复制一个文件过程：

其实，复制一文件的过程和新生成一个文件的过程是差不多的，只不过是将原始文件block中的数据写入新的block当中

在一个文件系统中删除一个文件过程

在一个文件系统中删除一根文件只需要两部：1，将block中的dentry中的对应文件的条目删除；2，在bitemap中将iNode的标记状态标记为空闲即可

在一个文件系统中剪切一个文件的过程

在一个文件系统中剪切一个文件，只是将block中dentry对应的路径发生了改变，其他没有放生任何改变，所有在一个文件系统中剪切一个文件会非常的快

硬链接：

1. 只能对文件创建，不能应用于目录

2. 不能跨文件系统

3. 创建硬链接会增加文件被链接的次数

符号链接：

1. 可应用于目录

2. 可以跨文件系统

3. 不会增加被链接文件的链接次数

4. 其大小为指定路径所包含的字符个数

设备文件：

每一个设备都有两个数字，第一个是主设备号(major number)，用来标识设备类型；第二个是此设备号(minor number)，用于标识同一种设备的不同类型

创建设备文件：`mknod`，`mknod -m`用来指定权限

硬盘设别的设别文件名：

IDE、ATA：hd

SATA：sd

IDE：第一个IDE口：主(/dev/hda)、从(/dev/hdb)；第二个IDE口：主(/dev/hdc)，从(/dev/hdd)

hda：hda1(第一个主分区)、hda2(第二个主分区)...hda5(第一个扩展分区)...，注：扩展分区一定是从5开始的

查看当前的系统识别了多少块硬盘：`fdisk -l [/dev/to/some_divice_file]`

VFS：Virtual FileSystem，正是由于VFS的存在，才能够使得Linux支持中多的文件系统，它弥合了不同文件系统之间的不同

>windows上常用的文件系统：FAT32(linux中叫)、NTFS、ISO9660、CIFS

>Linux上常用的文件系统：ext2、ext3、ext4、xfs、reiserfs、jfs、nfs、ocfs2、gfs2

>低级格式化是分磁道，而高级的格式化是：`mkfs -t ext3`，高级格式化其实就是创建文件系统的

管理磁盘分区：

`fdisk /dev/sda`，会出现交互式的命令，`p`表示显示当前的硬件分区；`n`表示创建新分区；`d`表示删除一个分区；`w`表示保存退出；`q`表示不保存退出；`t`表示修改分区类型，`l`表示显示所支持的所有类型

>一块硬盘在出厂的时候一般是经过低级的格式化的，也就是划分好了不同的磁道、扇区等，但是并没有分区；在我们买来之后，需要根据磁道进行不同的分区，分区是根据磁道进行的，越是外围的磁道其旋转的速度也就也快，当然读写文件的速度也是越快的，一般的分区都是从外向内进行的，也就是最开始分的分区是磁盘最外围的磁道，然后往内扩展，这也就是为什么总是把c盘作为操作系统的系统盘的原因。在磁盘完成分区之后，需要对其进行格式化，这种格式格式化也称为高级格式化，也就是建立文件系统的过程。格式化之后通过分区的挂载才能够使用

>在磁盘进行分区的过程中，最多可以分为4个主分区，如果4个都分成了主分区，那么这4个主分区之外剩余的磁盘空间就不能利用了。也可以是3个主分区加上一个逻辑分区，逻辑分区就是一个指针，在逻辑分区内，我们可以再进行分区，通常可以分很多个

磁盘：盘片(platter)、磁道(track，位于不同盘面上的相同编号的磁道我们称之为柱面，)、柱面(cylinder)、扇区(sector，store user data along with its own location data:sector number、head number、platter number and track number，每个扇区的大小一般是512bytes)、磁头、磁性材料、同心圆

如果我们划分了分区，每个分区都可以使用一个文件系统，如果没有划分分区，则只能存在一种文件系统。分区也就是将磁盘划分成多种不同的逻辑结构

在文件系统中存在`超级块(super block)`的概念，超级块使用来储存整个分区的全局信息：1，一共有多少个块组(block group)；2，每个块组当中有多少个块；3，块大小；4，空闲磁盘块、已用磁盘块；5，空闲iNode、已用iNode

>超级块在文件系统中非常重要，因此会有多个备份

在文件系统中还存在`块组描述符表`的概念，描述的信息是每一个块组是从哪一个块开始到哪一个块结束的、存在多少个块组等等，同样也很重要，也就是存在多个备份

>在每个块组中同样存在iNode区域，iNode位图(iNode bitmap)，block位图(block bitmap)以及数据区域

MBR是跟分区没有关系的，是存在扇区当中的

一个分区当中最开始是`boot block`，这个是不能用的。其他空间分成不同的块组(block group)

在每一个块组总存在以下信息：1. Super Block(超级块)；2. GDT(Groupblock Description Table，块组描述表)；3. BlockBitmap(块位图)；4.iNodeBitmap(iNode位图)；5.Data Blocks

上面所讲的文件系统是`ext2`；`ext3`是一种日志文件系统(journal file system)，这样就大大减小了开机需要修复文件的时间(需要修复的文件是由于非法断电造成的)；工作原理是先将iNode信息写入日志文件，如果没有问题就在写入元数据区域中

如果想要知道CPU是否识别了分区需要使用命令：`cat /proc/patitions`；而如果我们刚刚新建了一个分区，CPU并不能识别，需要我们让CPU来重新读一下分区表，我们使用命令：`partprobe [/dev/sda1]`

#### 创建文件系统

>重新创建文件系统会损坏原有的文件

`mkfs`：make file system；`mkfs -t FSTYPE`，例如：`mkfs -t ext2 /dev/sdb5`；`mksf -t FSTYPE` = `mksf.ext2`

还可以使用：`mke2fs`来创建`ext2`格式的文件系统；也可以使用`mke2fs -j`来创建`ext3`的文件系统；`mke2fs -b 1024|2048|4096`可以用来指定块大小；`mke2fs -L`可以从来指定卷标；`mke2fs -M #`可以用来指定预留给超级用户的块数的百分比；`mke2fs -i #`用于指定为对少个字节的的空间创建一个iNode，默认为8192，这里给出的大小应该是块的2^倍；`mke2fs -N #`指定的iNode的个数；`mke2fs -F`表示强制创建文件系统；`mke2fs -E`用于指定额外的文件系统的属性

`blkid DEV`查询或查看磁盘设别的相关属性，`e2label`用于查看或者定义卷标，`e2label 设备文件 卷标`表示设定卷标

`cat /proc/filesystems`可以查看当前的CPU都支持哪些文件系统

#### 调整文件系统的相关属性

`tune2fs -j DEV`，在不损害原有数据的前提下，将ext2升级为ext3；`tune2fs -L LABEL`可以设定和修改卷标；`tune2fs -m #`调整预留百分比；`tune2fs -r #`可以指定预留块数；`tune2fs -o`和挂载选项相关的；`tune2fs -C #`指定挂载次数达到#次数后进行自检，0或者-1表示关闭此功能；`tune2fs -i #`表示每挂载使用多少天之后开始自检，0或者-1表示关闭此功能；`tune2fs -l`表示显示超级块中的信息

`dumpe2fs`，也可以用来显示文件系统的属性；`dumpe2fs -h`只显示超级块的信息

`fsck`，用于检查并修复文件系统；`fsck -t FSTYPE`；`fsck -a`表示不询问，自动进行修复

`e2fsck`，专门用于修复ext2、ext3文件系统；`e2fsck -p`表示自动修复；`e2fsck -f`表示强制进行检查

#### 挂载、卸载

挂载：将新的文件系统关联至当前根文件系统

卸载：将某文件系统与当前根文件系统的关联关系移除

`mount 设备 挂载点`；设备可以是设备文件(/dev/sdb5)、卷标(label="")、UUID(UUID="")；挂载点：目录，要求：1，此目录没有被其他进程使用；2，目录得实现存在；3，目录中原有的文件会暂时隐藏

`umount`；`umount 设备`或者`umount 挂载点`

`mount [options] [-o options] DEVICE MOUNT_POINT`:

`mount -a`表示挂载/etc/fstab文件中定义的所有文件系统

`mount -n`默认情况下，mount命令每挂载一个设备，它都会把挂载的设备信息保存到/etc/mtab文件；使用-n选项意味着挂载设备时，不把信息写入此文件；`mount -t FSTYPE`指定正在挂载设备上的文件系统的类型；不使用此选项时，mount会调用blkid命令获取对应文件系统的类型；`mount -r`只读挂载，挂载光盘的时候常用此选项；`mount -w`读写挂载

`moutn -o`指定额外的挂载选项，也即文件系统启用的属性；`async`表示异步；`atime`表示每一次访问都更改时间戳；`remount`表示重新挂载；`ro`表示只读

#### swap分区

在储存器当中：寄存器是最快的，相当于1纳秒的级别；其次是CPU的一级二级三级缓存相当于10纳秒左右；而对于内存来说是10毫秒左右；而对于磁盘来说是秒的级别

查看本机的物理内存和交换内存：`free`，如果想要将单位换算成M，则使用命令：`free -m`

>缓冲(buffers)和缓存(catch)，缓冲是为了解决速度快的一方对速度慢的一方造成的冲击；而缓存是为了重复使用

`swap -a`可以用来定义在/etc/fstab文件中的交换设备

>一般来说，在计算机中缓存用来存放数据的，而缓冲是用来存放元数据的；例如，在文件系统中查找文件的时候，当我们找到文件的时候就将路径储存在缓存中，以后就从这里寻找；而那些block的数据就存放在缓存中，以备重复使用

#### 创建一个新的swap的过程

1. 在磁盘空间上分处1G的分区，分区的类型为`Linux swap`，编号为`82`；命令`fdisk /dev/sdb`，根据交互式命令进行操作

2. 将分区格式化，同普通的分区格式化的命令不同：`mkswap /dev/sdb5`，也就是将第二个磁盘上的第5个分区格式化为swap分区

3. 将格式化后的分区进行挂载使用，同一般的挂载也不相同：`swapon /dev/sdb5`，如果想要卸载，就是用命令：`swapoff /dev/sdb5`

>挂载成功之后，可以使用命令：`free -m`查看是否swap的空间变大

`dd`，可以用来复制文件，`dd if=数据来源 of=数据存储目标`

dd命令可以实现MBR的复制，`dd if=/dev/sda of=/mnt/mbr.backup bs=512 count=1`，这样就可完成MBR的备份；同样的dd命令可以完成将MBR进行替换：`dd if=/mnt/mbr.backup of=/dev/sda sb=512 count=1`

dd命令可以完成创建一个假的文件：`dd if=/dev/zero of=/mnt/virtualfile seek=1023 bs=1M count=1`，这样创建的文件看起来有1G，但是实际的大小只有1M，因为跳过了1023个block。跟虚拟机的创建原理差不多

`dd seek=#`表示创建数据文件时，跳过空间的大小

mount命令，可以挂载ISO镜像

`mount DEVICE MOUNT_POINT`，`mount -o loop`挂载本地回环设备

#### 文件系统配置文件`/etc/fstab`

OS在初始时，会自动挂载/etc/fstab文件中定义的每一个文件系统

/etc/fstab文件中每一个字段的意思：要挂载的设备  挂载点  文件系统类型  挂载选项  转储频率(每多少天备份一次)  文件系统检测测序(只有根为1，其他的可以同时进行检查，0表示不检查)

`fuser`验证进程正在使用的文件或套接字文件；`fuser -v`可以用来查看；`fuser -km MOUNT_POINT`终止正在访问此挂载点的所有进程

### 压缩

压缩格式：gz，bz2，xz，zip，Z

压缩算法：算法不同，压缩比也不相同

`gzip /PATH/TO/SOMEFILE`：默认压缩完成之后会删除源文件；`gzip -k`压缩后保留源文件；`gzip -d`相当于`gunzip`；`gzip -#`可以用来指定压缩比；`zcat /PATH/TO/SOMEFILE.gz`表示在不解压的情况下查看压缩文件的内容；`gunzip /PATH/TO/SOMEGIPFILE`用于解压缩文件

`bzip2 /PATH/TO/SOMEFILE`同上述gzip用法相同；`bunzip2 /PATH/TO/SOMEFILE.bz2`同样可以用于解压缩

`xz /PATH/TO/SOMEFILE`；`unxz /PATH/TO/SOMEFILE.xz`可以用来解压缩xz文件`；xzdec`也可以用来解压缩(不常用)

`zip`可以用来压缩目录，是一种既归档又压缩的工具。上述的其他命令不能用压缩目录；`zip FILENAME.zip FILE1 FILE2...`；`unzip /PATH/TO/SOMEFILE.zip`

>archive：归档，归档本身并不意味着压缩
 
`tar`，是一种只归档不压缩的工具；`tar -c`表示创建归档文件；`tar -f FILE.tar`操作的归档文件；`tar -x`表示还原归档；`tar --xattrs`表示在归档时，保留文件的扩展属性信息；`tar -tf FILE.tar`表示不展开归档，查看归档了哪些文件；`tar -zcf FILE.tar.gz`归档并且调用gzip压缩；`tar -zxf FILE.tar.gz`调用gzip解压缩并展开归档文件，`-z`选项可以省略，会根据你的文件后缀自动调用工具；`tar -jcf FILE.tar.bz2`可以用来归档并且调用bzip2来压缩文件；`tar -jxf FILE.tar.bz2`可以用来解压并且还原压缩文件，当然了`-j`的选项可以省略，会根据文件的后缀名称来自动调用解压工具

`tar -C`表示解压后可以重定向目录，例如：`tar xf /tmp/users.tar.gz -C ./`表示将解压后的文件展开在当前目录中

>`tar`命令中的`-`是可以省略的

`cpio`，也是一个归档工具，是一种更加古老的工具

`read -p "prompt"`可以用来给出提示；`read -t #`可以用来指定等待时间，这样就要给出默认值

`echo`还可以用来进行颜色的标记，以及字体的设置：`echo -e "\033[1mHello\033[0m,world."`在这里，1表示将字体加粗，还可以用3来引导前景色，也就是字体颜色，用4来引导背景色，例如：`echo -e "\033[31mHello\033[0m,world."`，其中31表示前景色为颜色1，也就是红色。共有7中颜色；背景色只需要用4来引导就可以了；另外，前景色和背景色可以同时使用，二者之间需要用`;`隔开，例如：`echo -e "\033[31;42mHello\033[0m,world."`表示背景色为绿色，前景色为红色

控制器、适配器、协议

IDE：133Mbps，并行；SATA：300Mbps，600Mbps,6Gbps，串行；USB3.0：480Mbps，串行；SCSI(320Mbps)：Small Computer System Interface，SCSI的扩展能力是非常强的，并行工作；SAS

RAID控制器：Redundant array of inexpensive disks；RAID的级别仅仅代表磁盘的组织方式不同，没有速度快慢之分；在组织RAID的时候，不能够只能够考虑速度，还要考虑数据的可用性

保证数据的可用性：1.磁盘镜像技术(mirror)；2.校验码

调带(RAID0)，性能提升n倍数，但是没有冗余能力(容错能力)，空间利用率是全部；镜像(RAID1)，写性能是下降的，但是读性能是提升的，有冗余能力，空间使用率只是1/2；RAID10，先镜像后调带，这是有钱的企业使用的方法，读写能力都提升，有冗余能力，空间利用率是(n-2)/n；RAID5是使用校验码，读写都提升了，有冗余能力，空间利用率是(n-1)/n；JBOD技术：是简单的将多快盘当做一块盘使用(Hadoop使用)，读写能力无提升，无冗余能力，空间利用率是100%

####怎么去实现RAID

RAID分为硬件RAID和软件RAID

硬件RAID：BIOS

软件RAID：模拟成了一个假的RAID，任何一个硬件设备都是可以用软件来模拟的。这个软件就是MD(multi disks)模块。

>在实际生产当中尽量使用硬件RAID，不要使用软件RAID

`mdadm`：可以将任何设备做成RAID，是一种模式化命令：

创建模式：`-C`创建模式，`-C -l`指定级别；`-C -n`设备的个数；`-C -a yse|no`表示自动为其创建设备文件，是否为其创建；`-C -c`表示CHUNK大小，默认为64k，可以自己指定，一般为2^n倍；`-C -x #`表示空闲盘的个数

管理模式：`mdadm --add|-a`，`mdadm --remove|-r`；`mdadm /dev/md1 --fail /dev/sd7`表示将阵列md1中的设备/dev/sd7分区模拟损坏(并不是真的损坏了)

监控模式：`mdadm -F`

增长模式：`mdadm -G`

装配模式：`mdadm -A`，主要用来当我们停用(`mdadm -S|--stop /dev/md1`)掉一个RAID之后，如果设备文件没有被删除，那么直接可用装配模式来加载新的磁盘：`mdadm -A /dev/md1 /dev/sdb7 /dev/sdb8`

查看RAID阵列的详细信息：`mdadm -D /dev/md0`

#### 创建一个用两个1G的分区创建一个RAID0

1. 创建设别：` mdadm -C /dev/md0 -a yes -l 0 -n 2 /dev/sdb{5,6}`

2. 格式化分区：`mke2fs -j /dev/md0`

3. 挂载使用：`mount /dev/md0 /mnt`。查看是否成功：`ls /mnt`，如果发现lost+fount目录，则表示挂载成功，可以使用

#### 模拟RAID的一个配置过程

1. 将RAID1中的一个磁盘模拟损坏：`mdadm /dev/md1 -f|--fail /dev/sdb7`

2. 将损坏的磁盘移除RAID：`mdadm /dev/md1 -r|--remove /dev/sdb7`

3. 将一块同样大小的好的磁盘加进来：`mdadm /dev/md1 -a /dev/sdb9`；可以用`mdadm -D /dev/md1`开查看RAID的详细信息，也可以使用`cat /proc/mdstat`来查看RAID的状态

#### 将RAID1中加一块空闲盘，当其中的一块盘换掉之后，空闲盘可以自动顶替上去

`mdadm /dev/md1 -a /dev/sdb9`，这样就可以新加了一块空闲的盘进去

#### 将当前的RAID信息保存至配置文件，以便以后进行自动装配(直接从配置文件中读取装配哪些设备)

`mdadm -D --scan > /etc/mdadm.conf`，这样，在我们停用一个RAID1之后，直接可以重新装配：`mdadm -S /dev/md1`、`mdadm -A /dev/md1`

#### 用四个磁盘创建一个RAID5的过程，其中一个是空闲盘

1. `fdisk`创建分区，格式化`mke2fs`，CPU重读分区表`partprob`

2. 创建设别文件/dev/md0：`mdadm -C /dev/md0 -a yes -l 5 -n 3 /dev/sdb{5,6,7} -x 1 /dev/sdb8`

3. 查看详细信息：`mdadm -D /dev/md0`；或者使用`mdadm -D --scan`

###监控

`watch`可以用来周期性的执行指定命令，并以全屏的方式显示；`watch -n #`指定周期长度，单位为秒，默认为2；格式：`watch -n # ‘COMMAND’`

#### 调带过程优化

当我们在进行磁盘格式化的时候，而已指定调带的大小，所谓的调带的大小也就是：CHUNK的大小除以block的倍数，例如：`mde2fs -j -E stride=16 -b 4096`，那么默认的CHUNK大小就是64k，这样就避免了CPU在进行调带的过程中要记性计算每一个CHUNK包含多少的block

MD：multi disks|meta device，可以将多个物理设备映射成一个逻辑设备；DM：device mapper，也可以将多个物理设备映射成一个逻辑设备，DM和MD是有些功能是相同的，但是不完全相同。 MD和DM都是Linux中的实现将多个多个物理设备抽象化成一个逻辑设备的模块，以后我们访问设备文件就是访问这些设备入口

DM：是LVM2的基础；快照、多路径

PV(physical Volume，物理卷) --> VG(Volume Group，卷组) --> LV(Logicall Volume，逻辑卷)，PV要分成PE(Physical Extent)，LV要分成LE(Logical Extent)

pv相关的命令是以pv开头的：`pvcreate`、`pvremove`、`pvscan`、`pvdisplay`、`pvmove`将PV中的数据移到其他PV当中，目的是可以从vg中删除pv、`pvs`

vg相关的命令是以vg开头的：`vgcreate`，`vgcreate VG_NAME /PATH/TO/PV -s #`，s选项可以指定PE的大小，默认是4M，例如：`vgcreate myvg /dev/sdb{5,6,7}`、`vgremove`可以用来将整个vg删除、`vgextend`、`vgreduce`可以用来将卷组中的某个PV删除，例如：`vgreduce myvg /dev/sdb5`

lv相关的命令是以lv开头的：`lvcreate`，`lvcreate -n NAME -L #G VOLUME_GROPE_NAME`、`lvremove`，例如：`lvremove /dev/myvg/logicaltest`

>fdisk最多只能创建15个分区

#### 怎么去扩展逻辑卷

1. 创建逻辑1G的逻辑分区：`lvcreate -n testlv -L 1G myvg`

2. 格式化、挂载：`mk2sfs -j /dev/myvg/testlv`、`mount /dev/myvg/testlv /mnt`

3. 扩展逻辑卷的物理边界：`lvextend -L 2G /dev/myvg/testlv`

4. 扩展逻辑卷的逻辑边界：`resize2fs /dev/myvg/testlv 2G`

`lvextend`，用来扩展物理边界，`lvextend -L [+]#G /PATH/TO/LV`

`resize2fs`，用来扩展或者缩小逻辑边界，`resize2fs /PATH/TO/LV #G`，`resize2fs -p /PATH/TO/LV`，直接扩展到物理边界那么大

#### 怎么去缩减逻辑卷

1. `df -lh`，查看当前即将缩减的逻辑卷挂载情况

2. `umount /mnt`：卸载正在挂载的逻辑卷

3. `e2fsck -f /dev/myvg/testlv`：强行记性逻辑卷文件格式的检验

4. `resize /dev/myvg/testlv 1G`：将逻辑卷的逻辑边间缩小到1G

5. `lvreduce -L 1G /dev/myvg/testlv`：将逻辑卷的物理边界缩小到1G

6. 重新挂载就可以使用了

`lvreduce`，用来缩小逻辑卷的物理边界，`lvreduce -L [-]#G /PATH/TO/LV`

>注意：1.不能在线缩减，得现卸载；2.确保缩减后的空间大小依然能够储存原有的所有数据；3.在缩减之前应该强行检查文件，以确保文件系统处于一致性的状态

`resize2fs /PATH/TO/LV #G`

#### 快照卷

1. 快照卷的主要目的是为了备份，声明周期为真个数据时长：在这段时长内，数据的增长量不能超过快照卷的大小

2. 快照卷应该是只读的

3. 跟原卷应该在一个卷组中，原卷就是要对哪个逻辑卷记性快照

#### 快照卷过程

1. 将逻辑卷挂载，使用，复制文件到逻辑卷中，用来演示

2. 创建快照卷：`lvcreate -s`；例如：`lvcreate -s -n testlv-snap -p r -L 50M /dev/myvg/testlv`表示对myvg卷组下的testlv逻辑卷创建一个大小为50M，权限为只读，卷标为test-snap的快照卷

3. 快照卷一旦创建成功，则其生命周期结束，也就是完成了备份功能。将快照卷中的内容压缩归档：`tar -zcf /tmp/users.tar.gz inittab fstab`

4. 一旦逻辑卷中的内容被误删除了，我们可以通过解压的方式来进行还原：`tar xf /tmp/users.tar.gz -C /media`

>常见分区的过程是创建物理边界的过程；格式化的过程是创建逻辑边界的过程

>`bash -x SCRIPT`可以用来显示脚本的执行过程

`ping`，`ping -c`；`ping -w`

### awk

`awk`更强大的文本处理工具：`awj -F '{COMMAND}'`	

练习：

写一个脚本(前提：请为虚拟机新增一块硬盘，假设它为/dev/sdb)，为指定的硬盘创建分区：

1. 列出当前系统的所有磁盘，让用户选择，如果选择quit则退出脚本；如果用户选择错误，让用户从新选择

2. 当用户选择后，提醒用户确认接下来的操作可能会损坏数据，并请用户确认；如果用户选择了y就继续，否则，让用户重新选择

3. 抹除那块硬盘上的所有分区(提示：抹除所有分区后执行sync命令，并让脚本睡眠3秒钟后再分区)；并为其创建三个主分区，第一个为20M，第二个为512M，第三个为128M，且第三个为swamp分区类型(提示：将命令分区通过echo传送给fdisk即可完成)

代码：

	#!/bin/bash
	#
	fdisk -l 2> /dev/null |grep '^Disk /dev/s[dh][a-z]' |awk -F: '{print $1}'
	read -p "Please select above one Disk File:" DISK
	case $DISK in
	quit)
		exit 7;;
	/dev/sda)
		echo 'Warning:This disk has the system,you can not repart it.'
		exit 8;;
	/dev/sdb)
		echo 'Warning:This action may destroy your file in this disk.'
		read -p "Please confirm your selection(y/n)" SELECTION
		if [ $SELECTION == 'y' ];then
		dd if=/dev/zero of=/dev/sdb bs=512M count=1
		sync
		sleep 3
		echo 'n
		p
		1

		+128M
		n
		p
		2

		+512M
		n
		p
		3

		+20M
		t
		3
		82
		w' |fdisk /dev/sdb
		else
		read -p "Please select above one Disk File:" DISK
		fi ;;
	*)
		echo 'You must input the existing disk above.';;
	esac

### NETWORK(网络)

总线型网络

环装网络(IBM)

星形网络(HUB)，逻辑上还是一种总线型网络

>电子是从负极流向正极的，电子在运动过程中会产生碰撞，遭遇到阻力，一部分转化为热能，一部分转化为动能，于是就产生了电阻

#### Ethernet network

CSMA/CD：Carrier Sense Multi Access Collision Detection，载波侦听多路访问，冲突检验。是一种边发送边侦听的方式，是一种线路仲裁机制

网桥，已用网桥可以将一个大网划分为多个小网，隔离了冲突域。多个接口的网桥就是交换机，冲突域是网桥的口和网桥所连的主机

MAC地址、广播，广播的主要目的是由逻辑地址找出来MAC地址，这个过程是ARP(Address Rosolve Protocol)地址解析；有MAC地址找到IP地址我们成为RARP，是反向地址解析

逻辑地址是分为两段：一个是网络地址，另一个是本地地址，这是由子网掩码来确定的，主要是根据IP地址来取它的网络地址的

任何时候本机地址的IP一定要和网关在同一个网络地址，不然的网关就无法转发其他网络的报文

路由器中维护着一张表，这张表要说明哪一个网络通过哪一个接口到达，这张表称之为路由表，交换机中维护的表称之为MAC表	

网桥：用来隔离冲突，但是对于广播现象是无能为力的

用多个交换机来解决广播现象，于是就在MAC地址的基础上产生了逻辑地址，作用是将一个交换网络的报文转发给另外一个交换网络，我们称之为路由器

端口号：来识别同一个主机上的不同进程。监听：一个进程只能分配一个端口

端口和IP的绑定称为套接字(Socket)，ip：port

协议分为7个分层(OSI模型)：物理层(Physical Layer) -->链路层(Data Link Layer) -->网络层(Network Layer) -->传输层(Transport Layer) -->会话层(Session Layer) -->表示层(Presentation Layer) -->应用层(Application Layer)

TCP/IP：物理层 -->链路层 --> 网络层-->传输层 -->应用层(现实中正在使用的模型)

IP Header

IP地址是点分十进制表示，32位

IP地址是分为网络地址和主机地址

IP地址分为：A类、B类、C类、D类；A类地址的掩码：255.0.0.0

A类：8位中首位是0，共有1-127，127用作回环地址。故共有126个，0 000 0001 - 0 111 1111；剩下的全0表示网路地址，全1表示广播地址，共可以连接2^24-2个主机

B类：首位从128-191，10 00 0000 - 10 11 1111，共有2^14个

C类：首位从192-223，110 0 0000 - 110 1 1111，共有2^21个

D类：首位从1110 0000 - 1110 1111，224-239

私有地址：

A类：10.0.0.0/8

B类：172.16.0.0/16-172.31.0.0/16

C类：192.168.0.0/24-192.168.255.0/24

路由表中的entry分为：主机路由和网络路由，其中0.0.0.0表示默认路由

子网和超网

ICANN

报文

TCP：Transfer Control Protocol(互联网的大多数应用都是用TCP)；UDP：User Dategram Protocol(即时通讯工具使用UDP、DNS查询)

TCP传输关系的建立需要"三次握手"；TCP传输关系的断开需要"四次断开"

Linux分为：User Space 和 Kernel Space，而对于Linux来说，网络是内核的功能；地址是内核的不是网卡的(虽然看似配置在网卡上)

#### 主机接入网络

IP

手动指定DHCP：Dynamic Host Configuration Protocol("169.254.0.0/8")

NETMASK

GATEWAY

HOSTNAME

DNS1

DNS2

DNS3

路由

网卡接口：

lo：本地回环

ethX：以太网网卡

CentOS5：/etc/modprobe.conf；CentOS6：/etc/udev/rules.d/70-persistent-net.rules

`ifconfig ethX IP/MASK [up|down]`

网络服务：

RHEL5：/etc/init.d/network 

RHEL6：/etc/init.d/NetworkManager

网关：

`route`，`route -n`表示以数字信息来查看相关路由信息；

`route add -net DEST gw NEXTHOP`表示添加网络路由；`route add -host DEST gw NEXTHOP`表示添加本地路由；`route add default gw NEXTHOP`

例如：`route add -net 10.0.0.0/8 gw 192.168.100.1`、`route add default gw 192.168.100.3`

`route del`，用来删除路由；`route del -net 0.0.0.0`

#### 永久配置网络

`/etc/sysconfig/network-scripts`

DEVICE表示关联的设别名称，要与文件名的后半部分"INTERFACE_NAME"保持一致

BOOTPROTO={static|none|dhcp|bootcp}：引导协议；要使用静态地址，使用static或者none；dhcp表示使用DHCP服务器获取地址

IPADDRESS；IP地址

NETMASK；子网掩码

GATEWAY；设定默认网关

ONBOOT；开机时是否自动激活此网络接口

HWADDR；硬件地址，要与硬件的地址保持一致；可省

USERCTL={yes|no}：是否允许普通用户控制此接口

PEERDNS={yes|no}：是否在BOOTPROTO为dhcp时接受由DHCP服务器指定的DNS地址

#### 设置路由

`/etc/sysconfig/network-scripts/`下增加文件`/etc/sysconfig/network-scripts/route-ethX`

添加的格式1为：`DEST via NEXTHOP`；例如：`192.168.10.0/24 via 10.10.10.254`

添加格式2为：

ADDRESS0=192.168.100.0

NETMASK0=255.255.255.0

GATEWAY0=10.10.10.254

>两种格式都可以使用，但是如果添加的是对个条目，要使用统一的格式

>上述两个设定，可以通过命令设定，也可以通过配置文件设定；命令设定立即生效，但重启无效；配置文件设定不会立即生效，需要重启服务才能生效

#### DNS服务器指定(方法只有一种，只能修改配置文件)

`/etc/resolv.conf`

nameserver DNS_IP_1

nameserver DNS_IP_s

#### 指定本地解析

`/etc/hosts`

主机IP 主机名 主机别名，例如：`172.16.0.1 www.magedu.com  www`

DNS-->/etc/hosts-->DNS

#### 修改主机名

`/etc/sysconfig/network`

iproute2

ip，`ip link`：配置网络接口属性，常用`ip -s link show`，`ip link set`；`ip addr`：协议地址，`ip addr show`，`ip addr add ADDRESS dev DEV `，例如：`ip addr add 192.168.100.4 dev eth0 label eth0:1`，`ip addr del ADDRESS dev DEV`，例如：`ip addr del 192.168.100.4/24 dev eth0`；`ip addr show dev DEV to prefix`显示到哪个网卡接口上的所有匹配前缀地址；`ip addr flush dev DEV to prefis`删除某个网卡接口上匹配前缀的所有网络地址；`ip route show`...

一个网卡可以使用多个地址，网络设别可以有别名(eth0)：ethX:X，例如eth0:0、eth0:1、eth0:2

给一个网卡配置多个ip地址的方法：`ifconfig ethX:X IP/NETMASK`(命令配置，立即生效，重启无效)；修改配置文件：`/etc/sysconfig/network-scripts/ifcfg-ethX:X`，将配置文件中的设别改为DEVICE=ethX:X

>若是给一块网卡配置了多个地址，非主要地址不能使用DHCP动态获取

### 软件包管理

源代码-->编译-->链接-->运行

程序的组成部分(核心的4部分)：二进制程序 + 库 + 配置文件 + 帮助文件

`/etc /bin /sbin /lib`是系统启动就需要用到的程序，这个目录不能挂载到额外的分区，必须在根文件系统的分区上

`/usr/bin /usr/sbin /usr/lib`是为系统提供基本(核心)的功能，可以单独分区

`/usr/local/bin /usr/local/sbin /usr/local/lib /usr/local/etc /usr/local/share/man`是第三方软件安装的地方，可以是一个独立的分区，和根的关系不太紧密

`/proc /sys`不能单独分区，默认为空

`/dev`设备文件，不能单独分区；`udev`在Linux kernel2.4以前，/dev目录中存放着上万个目录，这些目录是你所可能使用的硬件的设备文件，因此我们无法根据设备文件判断出对应的硬件是否存在；再Linux Kernel2.6是时候增加了udev的功能，udev能够根据内核识别的硬件信息动态的创建相应的设备文件，并且取一个别致的名字，这样我们就可以根据设备文件来判断出对应的硬件设别是否存在

`/home`建议单独分区，不是操作系统的核心

`/root`不是操作系统的核心，如果存在的话，要放在根分区内

`/var`不是操作系统的核心，如果存在，应该单独分区，是为了Linux系统管理更加顺畅

`/boot`存在内核，和系统的启动过程没有关系，一般来说需要将根做成一个LVM(可以动态扩展)，而/boot就做成一个单独的分区

系统的启动过程：POST(系统自检)-->BIOS(HW)-->(MBR)bootloader(文件系统结构，ex2、ex3、xfs)-->内核(文件系统的创建是内核的功能)

程序：指令+数据；指令：最终调用的是调用芯片的指令集；CPU：分为普通指令和特权指令

>编译的过程是将程序指令转化为汇编指令，汇编指令在装换为CPU所能够识别的指令(机器指令)；编译器实际上是转化为汇编和转化为对应CPU指令集的过程

软件包管理器功能：

1. 将二进制程序、库文件、配置文件、帮助文件打包成一个文件

2. 生成数据库，追踪所安装的每一个文件

软件包管理器应该提供的的核心功能：

1. 制作软件包

2. 安装、卸载、升级、查询、校验

RPM(后端工具)：Redhat Package Manager --> RPM is Package Manager

YUM(前端工具)：Yellowdog Update Modifier

rpm命令：

`rpm`：数据库，`/var/lib/rpm`

`rpmbuild`

rpm命名：`name-version-release.arch.rpm`，例如：`bind.9.7.1-1.el5.i586.rpm`；version：`major.minor.release`，major：重大改进，minor：某个子功能发生重大变化，release：修正了部分bug，调整了一些功能

rpm包：分为二进制格式(例如：bind.9.7.1.tar.bz)和源码格式(更好的发挥本机硬件的功能)

1. rpm安装：`rpm -i /PATH/TO/PACKAGE_FILE`；`rpm -h`表示以#显示进度，一个#是2%的进度；`rpm -v`显示详细过程；`rpm -vv`显示更加详细的过程；通常我们这样使用：`rpm -ivh /PATH/TO/PACKAGE_FILE`；`rpm --nodeps`忽略依赖关系；`rpm --replacepkgs`表示从新安装，替换原有的安装；`rpm --force`强行安装，可以实现重装或者降级；`rpm --test`可以实现测试，但是不安装

2. rpm查询：`rpm -q PACKAGE_NAME`表示查询指定的包是否安装；`rpm -qa`显示当前系统下安装的所有rpm包；`rpm -qi PACKAGE_NAME`查询指定包的说明信息；`rpm -ql PACKAGE_NAME`查询安装后生成的文件列表；`rpm -qf /PATH/TO/SOMEFILE`：查询指定的文件是由哪个rpm包安装生成的；`rpm -qc PACKAGE_NAME`查询指定软件包安装的配置文件；`rpm -qd PACKAGE_NAME`查询指定软件包的帮助文件；`rpm -q --scripts PACKAGE_NAME`查询指定包中包含的脚本

如果rpm包尚未安装，我们需要查询说明信息，安装以后生成的文件等：`rpm -qpi /PAHT/TO/PACKAGE_FILE`，将上述的命令中加一个`p`，表示指定路径

3. 升级：`rpm -Uvh /PATH/TO/NEW_PACKAGE_FILE`如果装有老版本，则升级，否则，安装；`rpm -Fvh /PATH/TO/NEW_PACKAGE_FILE`表示如果装有老版本，则升级，否则，退出；`rpm -Uvh --oldpackage`表示强制降级安装

4. 卸载：`rpm -e PACKAGE_NAME`表示卸载；`rpm -e --nodeps PACKAGE_NAME`表示没有解除依赖关系卸载

5. 校验(帮助我们查看是否被非法改动过)：`rpm -V PACKAGE_NAME`

6. 重建数据库：`rpm --rebuilddb`重新重建数据库，一定会重新建立的；`rpm --initbd`初始化数据库(没有就建立，有就不用建立了)

7. 检验来源的合法性和软件的完成性

加密类型：对称，加密解密使用同一个秘钥；公钥：一对秘钥，用公钥来加密，用私钥来解密，公钥隐藏在私钥当中，可以提取出来，并公布出去；单向

1)`rpm --import /etc/pki/RPM-GPG-KEY-CentOS-6`表示导入秘钥文件，只有导入了秘钥文件之后，才能进行相应的验证合法性和完整型操作

2)`rpm -k /PATH/TO/PACKAGE_FILE`；dsa，gpg是验证来源的合法性，也就是验证签名；可以使用--nosignature，略过此项；sha1，md5使用来验证软件包的完整性；可以使用--nodigest略过此项

#### yum

yum是C/S构架，也就是服务器/客户端类型

yum仓库中的元数据文件(repodata目录下)：

`primary.xml.gz`：当前仓库的所有rpm包的列表；包之间的依赖关系；每个rpm包安装生成的文件列表

`filelists.xml.gz`：当前仓库总所有rpm包所有的文件列表

`oter.xml.gz`：额外信息，rpm包的修改日志

`repomd.xml`：记录的是上面三个文件的时间戳和校验和

`comps*.xml`：rpm包的分组信息

如何为yum定义repo文件

[REPO_ID]
name=Descrption
baseurl={ http:// | ftp:// | file:/// }
enable={0|1}：是否允许使用yum仓库
gpgcheck={0|1}：用来检验rpm包的数据的完整性和来源的可靠性
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-6；如果`gpgcheck=1`则必须导入`gpgkey`

`yum list`，`yum list avaiable`：可用的(仓库中有，但是尚未安装的)；`yum list installed`：已经安装的；`yum list update`可用的升级

`yum clean`：表示清理yum缓存；`yum clean [ packages | headers | metadata | dbcache | all ]`

`yum repolist`：表示显示repo列表及简要信息；`yum repolist [ all | enabled | disabled ]`

`yum install { -y | --nogpgcheck } PACKAGE_NAME`：yum安装

`yum localinstall PACKAGE_FILE`，本地安装的时候使用这个命令，可以自动从yum源当中查找依赖关系并下载，但是本地安装必须指定rpm包的路径而不是名称(yum安装直接指定名称即可)

`yum update`：升级；`yum update_to`升级为指定版本

`yum remove|erase`：卸载

`yum info PACKAGE_NAME`：查询程序的信息

`yum provides|whatprovides FILE`查看指定的文件是由哪个 

`yum groupinfo`

`yum grouplist`

`yum groupinstall`

`yum groupremove`

`yum groupupdate`

>`mkdir -pv`

如何自己制作yum仓库：

1. 在/etc/repos.d/下创建配置文件

2. `createrepo rpm包所在的目录`：创建包之间的依赖关系，但是没有组合关系

3. `createrepo -g compose*.xml rpm包所在的目录`，创建组合关系

rpm的安装：

1. 二进制格式：源程序-->编译-->二进制格式。存在问题：有些特性是编译选定的，如果编译时没有选定此特性，将无法使用；rpm包的版本会落后于源码包，甚至落后很多

2. 定制：手动编译安装

gcc编译器：GNU C Compiler

make：项目管理工具，makefile：定义了make(gcc,g++)按何种次序去编译这些源程序文件中的源程序

编译安装的三步骤：

前提：准备开发环境(编译环境)，安装"Development Tools"和"Development Libraries"

1. `./configure`，`./configure --help`可以查看帮助命令。`--prefix=/path/to/somewhere`；`--sysconfdir=/PATH/TO/CONFFILE_PATH`；功能：1.让用户选定编译特性；2.检查编译环境

2. `make`

3. `make install`

例如：

1. `./config --prefis=/usr/local/tengine --conf-path=/etc/tegine/tegine.conf`，会生成一个makefile文件，也就是项目管理文件

2. `make`，执行makefile文件，进行编译

3. `make install`，将编译后的文件复制到指定的目录内

这种情况会出现以下问题： 

1. 将路径添加到PATH中：`vim /etc/profile`，`PATH=$PAHT:/usr/local/engine/sbin`(在export语句的前面添加)；还可以在`/etc/profile.d/`目录建立一个以.sh为名称后缀的文件，在里面定义`exprot PATH=$PATH:/path/to/somewhere`

2. 默认情况下，系统搜素库文件的路径是`/lib`，`/usr/lib`，如果我们要增添额外的搜索路径，需要在`/etc/ld.so.conf.d/`中创建以.conf为后缀的文件，而后要把添加的路径直接写到此文件中，例如：`/usr/local/apache/lib`；`ldconfig`命令表示通知系统重新搜索库文件，`ldconfig -v`显示重新搜索库的过程

3. 头文件：输出给系统(默认：`/usr/include`)，如果想让系统搜索到安装程序的头文件需要使用链接进行，两种方式：`ln -s /usr/local/tengine/include/* /usr/include/`(表示将第三方安装的程序的头文件链接到默认的头文件目录中)或者`ln -s /usr/local/tengine/include /usr/include/tengine`(表示将第三方安 装程序的头文件的目录链接到默认头文件目录下的一个文件当中) 

4. man文件路径：安装在--prefix指定的目录下的man目录；而默认的搜索路径是：`/usr/share/man`。有两种解决办法：1.`man -M /PATH/TO/MAN_DIR COMMAND`，例如：` man -M /usr/local/apache/man apachectl`，2.在`/etc/man.config`中添加一条MANPATH

#### netstat命令(网络相关)

`netstat -r`显示路由表；`netstat -rn`以数字的形式显示路由表

`netstat -t`显示建立的tcp连接

`netstat -u`显示udp连接

`netstat -l`显示监听状态的连接

`netstat -p`显示监听指定的套接字的进程的进程号及进程名

### 脚本编程之函数(功能)

函数是实现结构是实现结构化编程的重要方法，目的是实现代码的重用

`function FUNCTION{COMMAND}`或者`FUNCTIONNAME(){COMMAND}`

`return`可以自定义执行状态返回值，在函数中无论在什么位置遇到`return`语句，函数就终止了

函数也可以传递参数($1、$2...)

### 进程管理

现在的操作系统实际上就是由内核和运行在内核之上的许多进程来实现的，内核的功能：文件系统、网络功能、驱动程序、安全功能、进程管理...

进程是一个逻辑概念；内存分为：用户空间(进程使用)和内核空间(Kernel使用)，进程在执行的过程当中，进程的指令和数据本身是储存在用户空间的当中的，而进程的描述信息(进程ID，进程父ID、进程名字、进程执行到哪里)是保存在内核空间中"task structure"的。进程从CPU退回到内存的过程，我们称之为"现场保存"；而进程从内存重新载入到CPU当中，我们称之为"恢复现场"

x86系列的CPU的运行是分环的，和内核相关的命令(也就是直接操作硬件的，如清空寄存器、缓存等)是在0环上执行的；而和用户相关的进程命令是操作在第3环上的(操作系统的历史原因导致)

为了避免内存的恶意占用或者bug等不可预知情况，内存分为线性内存(虚拟内存)和物理内存，虚拟内存分为：process virtual Memory 和 kernel virtual memory两段。所谓的线性内存是指：在进程看来，内存空间当中只有内核和自己，对于32位的操作系统来说，内核占有1G内存，而进程最多可占有3G内存。线性内存从下到上依次为：forbidden(禁用的)；initialized data(已经初始化了的数据)；uninitialized data(未初始化的数据)；runtime heap(堆内存，所谓的堆内存是指在物理内存空间中，是根据需要来动态申请的内存空间)；Memory mapped region for shared library(共享库的映射地址)；stack(栈，主要是储存本地变量，堆和栈互相朝着对方的方向增长，因为堆和栈的内存是不断地变化的。上面的这些称之为进程内存空间)；

在物理内存空间当中也分为两部分：用户内存空间和内核内存空间，实际上只有内核内存空间是连续的，而用户内存空间往往不是连续的，用户内存空间被分成了一个的叶匡，当一个进程发起的时候，内核会为进程分配几个叶匡的内存，这些内存的地址信息会被映射到虚拟内存的进程内存空间当中，最开始进程的堆、栈虚拟内存和物理内存之间只是存在一个映射关系，随着堆栈内存空间的不断变换，内核会动态的分配物理内存当中的叶匡给映射的虚拟堆栈内存，所以物理内存当中同一个进程的内存内置很可能是离散的碎片化

实际上一个进程虚拟内存当中的页面和物理内存当中叶匡的对应关系是储存在物理内核空间当中的，在虚拟内存和物理内存相对应的过程，为了更加有效率的执行是由一个芯片来执行的，这个芯片叫做"MMU(Memory Management Unit)"，利用缓存的办法来解决重复查找、查找缓慢等问题，当缓存满了的时候，清楚"最近的、最长时间没有使用的"可以填入新的内存(这个过程称之为TLB)

进程的切换也称为上下文的切换(进程的环境切换)

VSZ：是进程的虚拟内存大小，包括进程的text(指令文件)、data(已初始化变量)、bss(未初始化变量)、heap(堆内存空间)、shared libraries(共享库)、stack(占内存空间)等

RSS：是进程在物理内存空间当中出去可以交换到swap分区的内存空间

当CPU是多核的时候，如果程序不是并行开发的程序，实际上是没有太大的作用的。如果是并行开发的程序，那么同样一个进程的多个线程可以在同时在多个CPU上同时进行，那么执行效率就会非常的高，另外还可以共享资源，那么就可以节省内存空间

多进程模型和单进程多线程模型，二者没有好坏之分，单进程多线程模型也存在问题：多个线程之间需要同步，而且会发生"死锁"等情况

进程是由状态的：stoped(停止状态)、ready(排队状态)、executing(执行状态，获得CPU资源)、uninterruptible(不可中断睡眠状态，当进程在通过系统调用访问I/O的时候，没有其他事情可以做了，暂时睡眠)、interruputible(可中断状态，当一个进程执行完毕，但是不能退出的时候，执行可中断休眠状态)、Zombie(僵尸状态，继承执行完毕，但是无法回收的进程)

在进程状态中，`D`表示不可中断的睡眠；`R`运行或者就绪；`S`可中断的睡眠；`T`表示停止；`Z`表示僵死；`<`表示高优先级进程；`N`表示低优先级进程；`+`表示前台进程组中的进程；`l`表示多线程进程；`s`表示会话进程首进程

>加了总括号的COMMAND，表示"内核线程"

进程是有父子关系的，任何进程都是`init`的子进程

进程是有优先级关系的：Linux的优先级是从`0-139`之间变换，共有140个优先级，其中`100-139`是用户可控的，而`0-99`是内核可控的

>进程的优先级越高，可以1.获得更多的CPU运行时间；2.更优先获得运行机会。

>每一个进程都有一个`nice`值，nice值在`-20~19`之间进行变换，对应用户可调CPU的优先级`100~139`；普通用户只能够调大自己进程的nice值，也就是只能减低自己进程的优先级

进程相关命令：

>liunx的命令有两种风格，第一种是system V，又叫做SysV风格(这种风格的命令选项有`-`)，另外的一种风格是BSD风格(这种风格的命令选项没有`-`)

>进程分类：跟终端相关的进程和跟终端无关的进程(随着系统的启动而启动)

`ps`，process state，BSD风格：`ps a`表示跟终端相关的进程；`ps u`显示更过的信息；`ps x`表示与终端无关的进程；经常结合使用`ps aux`可以用来查看进程的发起者、进程号、CPU占用比、内存占用比、虚拟内存占用的大小、物理内存占用大小、和哪个终端相关、状态、在CPU上实际运行的时间、是由哪个命令发起的(带中括号表示进程是由内核线程发起的)。
SysV风格：`ps -e`表示所有进程；`ps -f`表示多个字段；`ps -eF|ef`经常组合来使用；`ps -o`可以用来指定要显示的字段，例如：`ps -axo command,ni`

`pgrep`，`pgrep -u USERNAME PROCESS`查找是用户USERNAME发起的进程

`pidof PROCESS`可以用根据进程名来查找相关进程的ID号

`pstree`用来显示进程树

`top`：过去的一分钟、五分钟、十五分钟的平均队列长度，值越小，表示CPU的平均负载越低

`top`还有一些交互式的命令：`M`根据主流内存大小进行排序；`P`根据CPU使用的百分比进行排序；`T`根据累计时间进行排序；`l`表示是否显示平均负载启动时间；`t`是否显示进行和CPU状态相关信息；`m`是否显示内存相关信息；`c`是否显示完整的命令行信息；`q`退出top；`k`终止某个进程 

`top -d`可以指定延迟时长；`top -b`批处理模式；`top -n`在批模式下，一共显示多少屏

#### 进程间通信(IPC：Inner Process Communication)

共享内存

信号：signal，用`kill -l`来显示所有可用信号；重要的信号

`1:SIGHUP`：让一个进程不用重启，就可以重读其配置文件，让新的配置文件生效

`2:SIGINT`：中断一个进程，例如：`Ctrl + c`

`9:SIGKILL`：杀死一个进程(强行杀死一个进程)

`15:SIGTREM`：终止一个进程(比较人道的死亡办法)，是默认信号

指定一个信号：1.信号号码：`-1`；2.信号名称：`-SIGKILL`；3.信号名称简写`-KILL`

`kill PID`，kill后面只能跟上PID，`killall COMMAND`，killall后面可以跟命令

Semaphore

#### 调整nice值

调整已经启动的进程的nice值：`renice NI PID`；在启动时指定nice值：`nice -n NI COMMAND`

前台作业：占据了命令提示符；后台作业：启动之后，释放命令提示符，后续的操作在后台完成

把作业从前提送到后台：`Ctrl + z`(默认是stop状态) 或者 `COMMAND &`

`jobs`可以查看当前系统的所有作业号；+号表示默认要执行的作业，-表示下一个要执行的作业

`bg`，可以让后台停止的作业继续进行，`bg JOBID`

`fg`，调回后台的作业到前台来，`fg JOBID`

`kill %JOBID`表示终止某个作业，`bg`和`fg`不用加"%"号

`vmstat`，表示虚拟内存状态，`vmstat 1`表示每隔1秒显示一次；`vmstat 1 5`表示每隔一秒显示一次，但是只显示5次

`uptime`显示一些时间、用户、平均负载信息

`/proc/meminfo`可以用来查看内存信息

### 系统的启动流程(PC：OS(linux))

POST(加电自检)(ROM芯片)-->BIOS(Boot Sequence)-->MBR(bootloader,446)-->Kernel(内核空间)-->initrd-->(ROOTFS)/sbin/init(用户空间)(/etc/inittab)

内核设计风格：

单内核：Linux；包括核心：ko(kernel object)和内核模块(动态加载)，储存在`/lib/moudules/"内核版本号命令的目录"`

微内核：Windows，Solaris(线程支持好)

内核文件：vmlinuz-2.6.32

RedHat5：ramdisk-->initrd；RedHat6：ramfs-->initramfs

`du -sh du -sh /lib/modules/2.6.32-573.el6.i686/`用于查询目录下所有文件的大小

`ldd`可以查询文件依赖于哪些库文件

`chroot`可以用来实现一个小根，`chroot /PATH/TO/TEMPROOT [COMMAND...]`

#### 运行级别

启动的服务不同，导致运行级别不同；`0`:halt；`6`:reboot；`1`:single user mode，直接以管理员身份切入；`2`:multi user mode,NO NFS；`3`:multi usre mode,text mode；`5`:multi user mode,graphic mode；`4`:reserved,保留级别

#### bootloader(MBR)

linux的bootloader

1. LILO：LInux LOader

2. GRUB：GRand Unified Bootloader，Stage1：MBR；Stage1.5；stage2：/boot/grub/

`/etc/grup.conf`：

	default=0	#设定默认启动的title的编号，从0开始
	timeout=5	#等待用户选择的超长时间，单位是秒
	splashimage=(hd0,0)/grub/splash.xpm.gz	#grub的背景图片
	hiddenmenu	#隐藏菜单
	password redhat #明文的方式加密
	password --md5	$1$smXTe$jYAhrBxo5Nh19pfM6/vnE0	#加密的方式加密
	title CentOS 6 (2.6.32-573.el6.i686)	#内核标题，或操作系统名称，字符串，可自由修改
		root (hd0,0)	#内核文件所在的设备，对grub而言，所有类型硬盘一律为hd；hd#表示第几块磁盘，最后的0表示对应磁盘的分区，格式为:(hd#,#)
		kernel /vmlinuz-2.6.32-573.el6.i686 ro root=/dev/mapper/VolGroup-lv_root	#指定内核文件路径，及传递给内核的参数，grub访问内核文件的时候，尚未有文件系统，因此是在根下
		rd_NO_LUKS LANG=en_US.UTF-8 rd_NO_MD rd_LVM_LV=VolGroup/lv_swap SYSFONT=latarcy                                  
		rheb-sun16 crashkernel=auto rd_LVM_LV=VolGroup/lv_root  KEYBOARDTYPE=pc KEYTABLE                                  
		=us rd_NO_DM rhgb quiet
		initrd /initramfs-2.6.32-573.el6.i686.img	#ramdisk文件路径，操作系统安装的最后一步生成的

>使用加密的方式进行加密需要使用密令：`grub-md5-crypt`，输入密码就能生成加密的密码

***

*** 

## Linxu 的一些常见的问题补充

### 如何使用普通用户的`sudo`功能

1. 添加一个普通用户：`user add cll`
2. 添加用户密码：`passwd cll`
3. 将wheel用户改为具有所有权限：`vim /etc/sudoers`找到其中的一个entry，将注释去掉 --> `%wheel ALL=(ALL) NOPASSWD: ALL`，
4. 将用户cll添加一个附属组wheel：`usermod -a -G wheel cll`

> 这样用户cll不用输入密码的情况下可以实现sudo开完成一些需要管理员才能完成的指令

### 如何定义一个自动完成的周期任务

1. 周期任务需要在文件中定义：`crontab -e`即可以打开一个文件，在里面定义任务
2. 任务的格式在文件中说明：`cat /etc/crontab`
3. 可以将需要的任务写成一个脚本，放在其他地方，定义路径即可
4. 任务的执行是在后台进行，在标准的输出上并不会有任何变化

例如定义的git自动pull和push脚本

crontab文件

	1 0 0 * * * /home/fmz/language_learn/shell_learn/auto_git_pull.sh
	2 10 0 * * * /home/fmz/language_learn/shell_learn/auto_git/push.sh

auto_git_pull.sh

	#!/bin/bash

	adddate(){
	        while IFS= read -r line; do
	                echo "$(date) $line"
	        done
	}


	cd /home/fmz/git_repo/fengmengzhao.github.io/
	git pull origin master | adddate >> /home/fmz/git_auto.log

auto_git_push.sh

	#!/bin/bash


	adddate(){
	        while IFS= read -r line; do
	                echo "$(date) $line"
	        done
	}

	cd /home/fmz/git_repo/fengmengzhao.github.io/
	git add . | adddate >> /home/fmz/git_auto.log
	git commit -m"auto push" | adddate >> /home/fmz/git_auto.log
	git push origin master | adddate >> /home/fmz/git_auto.log

#### 如何在不同的Linux服务器之间进行文件的copy

1. 查看是否有`scp`命令，如果没有需要进行安装`opensshl`
2. `scp /file/to/some_path username@localhost:/file/to/some_path`，例如：`scp /home/fmz/.vimrc fmz@192.168.1.2:`，缺省的目录是用户的家目录，也就是文件复制到了`/home/fmz/`文件夹中
3. 也可以从其他的服务器中复制文件到本地：`scp username@localhost:/file/to/some_path /file/to/some_path`，例如：`scp root@192.168.1.4:dead.letter .`，这样就把其他服务器上的文件复制到了本地的目录中，这里的`.`指的是当前敲入命令的家目录

#### 如何让Linux终端支持256种颜色

正常的情况下Linux终端只支持8种颜色，$TERM=xterm，可以通过如下代码来检验终端支持多少种颜色:

`(x=`tput op` y=`printf %76s`;for i in {0..256};do o=00$i;echo -e ${o:${#o}-3:3} `tput setaf $i;tput setab $i`${y// /=}$x;done)`

需要通过在`.bashrc`文件中加入以下代码，将$TERM变为xterm-256color:

```
f [ "$TERM" == "xterm"  ]; then
    # No it isn't, it's gnome-terminal
    export TERM=xterm-256color
fi
```

如果使用PUTTY进行终端连接，需要将`PUTTY --> Connection --> Data --> Terminal-type string --> xterm-256color`

---

---

### [?]

#### Linux中Java环境增加CLASSPATH[?]

**临时添加变量**

`echo ${CLASSPAT}`: 查看用户下Java环境的CLASSPATH

`export CLASSPATH = $CLASSPATH:/path/to/classpath`: 为当前用户的Java环境临时添加CLASSPATH

**永久性添加变量**

`vim /etc/profile.d/java.sh|maven.sh`

将export语句加入java.sh(Java环境变量)或者maven.sh(maven环境变量)

`javac -d . -cp ".:/path/to/classpath"` *.java: 为Java file编译时添加CLASSPATH

#### Linux时区设置正确,时间不正确[?]

    #安装工具
    yum install ntp ntpdate

    #设置系统时间与网络时间同步
    ntpdate cn.pool.ntp.org

    #将系统时间写入硬件时间
    hwclock --systohc

