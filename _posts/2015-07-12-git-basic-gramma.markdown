---
layout: post
title: Git基本语法 
---

**Git Clone**

`git clone url`

url可以写成两种情况

- url = https://github.com/FengMengZhao/FengMengZhao.github.io.git
- url = git@github.com:FengMengZhao/FengMengZhao.github.io.git

> - FengMengZhao 为 github 的 username；FengMengZhao.github.io 为仓库(Repository)的名称
- 最好使用第二个URL，第一个URL每次在进行push的时候需要输入密码，如果需要可以使用`git remote set-url origin git@github.com:username/repo.git`

**Git Modification Status**

`$ git status`

**Git update Modification to local**

`$ git add .`

**Git undate Modification to log(real update)**

`$ git commit -m"Title"`

> Title 为修改的说明

**Set remote git push url**

`$ git remote set-url origin url`

> - url为原始的URL( https://github.com/ 或者 git@github.com: )
- To ingnore input username and password during push remote, we must use URL"git@github.com:".例如：`https://{USER_NAME}:{PASSWORD}@github.com/fmzhao/fmzhao.github.io.git`

**push local repository to remote**

`$ git push origin master`

**Git update local repository to remote**

`$ git pull origin master`

**Git ignore deleted file**

`$ git checkout -- .`

**Github的SSH配置**

*设置Git的User name 和 Email*

`$ git config --global user.name "xxx"`

`$ git config --global user.email "xxx@gmail.com"`

*生成密匙*

`$ ssh-keygen -t rsa -C "xxx@gmail.com"`

> 按三个回车键，密码为空，得到两个文件：id_rsa 和 id_rsa.pub(生成在用户工作目录的.ssh目录中)

*在自己的git设置中添加密匙ssh: ssh-add*

> - 添加的是id_rsa.pub文件夹里的内容, 将id_rsa_pub用文本编辑器打开，复制其中的内容到对应的地方，点击添加即可
- 如果原来的remote URL 是`https://github.com/USER_NAME/*.git`的形式, 要将remote URL重新设置为`get@github.com:USER_NAME/*.git`

---

第一次上传Github Repository

	git inint
	git add .
	git commmit -m"firstUpdate"
	git remote add origin https://github.com/FengMengZhao/StudentsManagerWeb.git
	git push -u origin master

[More...](http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000)

**git 忽略掉删除的文件**

     git add --a
     git commit -m" "
     git push origin master

**git恢复删除的文件**

    git checkout -- .

**windows git status 中文乱码**

    git config --global core.quotepath false

**git将用户名(username)和密码(passwd)加入remote url**

    git remote set-url origin https://username:passwd@github.com/repo.git

- 例如: `git remote set-url origin https://fmzhao:FENG799520.Github@github.com/fmzhao/fmzhao.github.io.git`
- 这样clone一个项目之后(自己的账号,知道用户名和密码),直接可以push而不需要ssh设置

**git重命名文件**

    git mv old_filename new_filename

**git-reset**

`git reset [--mixed] HEAD /path/to/somefile`: 将文件状态定位到working dir

`git reset [--soft] HEAD~1 `: 将文件状态定位到staged

`git reset [--hard] SHA-CODE `: 将文件状态定位到某个commit

**改变gvim字体的大小**

    if has("gui_running")
      if has("gui_gtk2")
        set guifont=Inconsolata\ 12
      elseif has("gui_macvim")
        set guifont=Menlo\ Regular:h14
      elseif has("gui_win32")
        set guifont=Consolas:h13:cANSI
      endif
    endif

**查看本地repo使用的Remote URL**

    git config --get remote.origin.url

    git remote show origin

**设置文件字符集和展示字符集**

    set encoding=utf-8 " The encoding displayed
    set fileencoding=utf-8 " The encoding written to file

**分支(branch)相关**

`git branch <new_branch>`：创建一个新的分支。新的分支会来源分支的内容。

`git checkout <new_branch>`：转到新的分支

`git branch -d(--delete) <branch_name>`：删除本地分支；`git branch -D(--delte --force) <branch_name>`：强制删除本地分支

`git push -d <remote_name> <branch_name>`：删除远程分支

`git branch [-a]`：列出[所有]的分支

`git push -u origin <branch_name>`：推送到某个远程分支

`git remote -r`：列出远程分支

**Git清除历史提交中的大文件**

首先，找出大文件

- 找出大文件ID
    - `git verify-pack -v .git/objects/pack/pack-*.idx | sort -k 3 -g | tail -5`
- 根据大文件的ID(上述命令的第一列)，找出文件名
    - `git rev-list --objects --all | grep 8f10eff91bb6aa2de1f5d096ee2e1687b0eab007`

接下来，删除文件

```
git filter-branch --index-filter 'git rm --cached --ignore-unmatch <your-file-name>'
rm -rf .git/refs/original/
git reflog expire --expire=now --all
git fsck --full --unreachable
git repack -A -d
git gc --aggressive --prune=now
git push --force [remote] master
```

> `<your-file-name>`可以是文件名，也可以是目录名

---
