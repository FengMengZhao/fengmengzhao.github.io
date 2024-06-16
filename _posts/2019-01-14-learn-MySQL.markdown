---
layout: post
title: 学习MySQL
---

##### 安装登录

    # CentOS安装
    sudo yum install mysql-server

    # 登录
    mysql -uroot -p
    
    # 设置密码
    UPDATE MYSQL.USER SET PASSWORD = PASSWORD('password') WHERE USER = 'root';

    # 密码忘记后怎么将\'\'用户具有权限
    vim /etc/my.cnf
    [mysqld]
    skip-grant-tables #add the lien 
    
    # 重新启动
    sudo service mysqld restart
    
##### 用户管理命令

    # 创建新的用户和密码
    CREATE USER user_name [IDENTIFIED BY 'password'];

    # 赋予用户权限
    GRANT ALL PRIVILEGES ON *.* TO user_name@host_name IDENTIFIED BY 'password';

    # 刷新权限
    flush privileges;

    # 查看所有的用户
    SELECT host, user, password from mysql.user;
