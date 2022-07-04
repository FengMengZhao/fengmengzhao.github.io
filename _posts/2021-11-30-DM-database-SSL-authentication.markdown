---
layout: post
title: '达梦数据库生成、配置SSL证书，测试JDBC连接'
subtitle: '达梦数据库提供SSL认证功能。文章介绍自签名CA证书生成并部署证书到数据库服务端完成数据库SSL环境搭建，提供达梦manager工具、原生JDBC连接达梦数据库测试'
background: '/img/posts/DM-SSL-configuration.jpg'
comment: false
---

# 目录

- [1. 环境介绍](#1)
- [2. 证书生成](#1)
    - [2.1 CA证书生成](#2.1)
    - [2.2 服务端证书生成](#2.2)
    - [2.3 客户端证书生成](#2.3)
        - [2.3.1 JDBC连接证书生成](#2.3.1)
        - [2.3.2 拷贝CA自签名证书到client/SYSDBA中](#2.3.2)
- [3. 部署SSL证书到服务并配置SSL启用](#3)
- [4. JDBC连接SSL认证达梦数据库](#4)
    - [4.1 达梦Manager工具JDBC连接](#4.1)
    - [4.2 原生Java JDBC程序连接](#4.2)
- [5. 注意事项](#5)
- [6. 参考](#6)

---

<h3 id="1">1. 环境介绍</h3>

达梦：`DM8-安全版`

open-ssl：`OpenSSL 1.1.1f  31 Mar 2020`

操作系统：`Microsoft WSL2 Ubuntu 20.04.3 LTS`

<h3 id="2">2 证书生成</h3>

证书的生成分为CA证书、服务端证书、客户单证书和JDBC证书。首先，要修改openssl的默认配置文件：

备份并修改`/usr/lib/ssl/openssl.cnf`，修改地方如下：

```shell
#需要修改的地方标注有【修改前：xxx】
[ CA_default ]

dir             = /opt/ca2              # 【修改前：somepath】
certs           = $dir/certs            # Where the issued certs are kept
crl_dir         = $dir/crl              # Where the issued crl are kept
database        = $dir/index.txt        # database index file.
#unique_subject = no                    # Set to 'no' to allow creation of
                                        # several certs with same subject.
new_certs_dir   = $dir/newcerts         # default place for new certs.

certificate     = $dir/ca-cert.pem      # 【修改前：$dir/cacert.pem】
serial          = $dir/serial           # The current serial number
crlnumber       = $dir/crlnumber        # the current crl number
                                        # must be commented out to leave a V1 CRL
crl             = $dir/crl.pem          # The current CRL
private_key     = $dir/ca-key.pem       # 【修改前：$dir/private/cakey.pem】

x509_extensions = usr_cert              # The extensions to add to the cert
```

> Centos系统配置文件在位置：`/etc/pki/tls/openssl.cnf`。

<h4 id="2.1">2.1 CA证书生成</h4>

```shell
#生成 CA 根证书、秘钥 
#密码设置为 123456，后面会用到
openssl req -new -x509 -days 3650 -keyout ca-key.pem -out ca-cert.pem -subj "/C=cn/ST=hunan/L=changsha/O=dameng/OU=dev/CN=lw/emailAddress=abc@dm.com"
```

<h4 id="2.2">2.2 服务端证书生成</h4>

执行下面的命令，注意确保当前目录是`/opt/ca2`(配置文件中设置的`dir`)：

```shell
#生成服务端秘钥
#这里方便起见不设置密码，如果设置了密码在启动达梦数据库的时候用命令行启动，需要数据该服务端私钥密码
openssl genrsa -out server_ssl/server-key.pem

#根据服务端私钥文件生成服务端签发申请文件，需要输入服务端私钥密码（由于上面服务端私钥没有设置密码，这里不需要输入）
openssl req -new -key server_ssl/server-key.pem -out server_ssl/server.csr -subj "/C=cn/ST=hunan/L=changsha/O=dameng/OU=dev/CN=server/emailAddress=server@dm.com"

#根据CA根证书和上一步签发申请文件生成服务端签名证书
#需要输入CA私钥密码，本例中上面设置的是123456
openssl ca -days 3650 -in server_ssl/server.csr -out server_ssl/server-cert.pem

#转换证书格式
openssl x509 -in server_ssl/server-cert.pem -out server_ssl/server.cer

#复制CA根证书、秘钥到服务端证书目录中
cp ca-cert.pem server_ssl/ && \
cp ca-key.pem server_ssl/
```

<h4 id="2.3">2.3 客户端证书生成</h4>

执行下面的命令：

```shell
#生成客户端相关的证书
#生成客户端私钥，本例中设置为dameng
openssl genrsa -aes256 -out client_ssl/SYSDBA/client-key.pem

#根据客户端私钥文件生成客户端签发申请文件，需要输入客户端私钥密码（本例中上面设置客户端私钥密码为dameng）
openssl req -new -key client_ssl/SYSDBA/client-key.pem -out client_ssl/SYSDBA/client.csr -subj "/C=cn/ST=hunan/L=changsha/O=dameng/OU=dev/CN=SYSDBA/emailAddress=dmclient@dm.com"

#根据CA根证书和上一步签发申请文件生成服务端签名证书
#需要输入CA私钥密码，本例中上面设置的是123456
openssl ca -days 365 -in client_ssl/SYSDBA/client.csr -out client_ssl/SYSDBA/client-cert.pem

#将私钥和证书合并转化为pkcs12格式文件
#需要输入客户端私钥密码，本例设置为dameng
#需要设定Export密码，本例设置为abc123
openssl pkcs12 -export -inkey client_ssl/SYSDBA/client-key.pem -in client_ssl/SYSDBA/client-cert.pem -out client_ssl/SYSDBA/client-pkcs.p12
```

<h5 id="2.4">2.3.1 JDBC连接证书生成</h5>

导入所有的证书到keystore中，并设置keystore的密码，本例设置为abc123。

```shell
#
keytool -import -alias ca -trustcacerts -file ca-cert.pem -keystore client_ssl/SYSDBA/.keystore -deststorepass abc123 -noprompt
keytool -import -alias server -trustcacerts -file server_ssl/server.cer -keystore client_ssl/SYSDBA/.keystore -deststorepass abc123 -noprompt
keytool -importkeystore -srckeystore client_ssl/SYSDBA/client-pkcs.p12 -srcstorepass abc123  -srcstoretype PKCS12 -keystore client_ssl/SYSDBA/.keystore  -deststorepass abc123
```

<h5 id="2.3.2">2.3.2 拷贝CA自签名证书到client/SYSDBA中</h5>

```shell
cp ca-cert.pem client_ssl/SYSDBA/
```

<h3 id="3">3. 部署SSL证书到服务端</h3>

```shell
#拷贝server_ssl、client_ssl到达梦bin目录（和dmserver在同一个目录）中
#如果原来已经有相应目录，做一下备份
cp /opt/ca2/server_ssl /home/dmdba/dmdbms/bin/ -r
chmod 777 -R /home/dmdba/dmdbms/bin/server_ssl
cp /opt/ca2/client_ssl /home/dmdba/dmdbms/bin/ -r
chmod 777 -R /home/dmdba/dmdbms/bin/client_ssl
```

打开`dm.ini`文件配置如下红框中配置：

![](/img/posts/dm-ssl-set-config-value.png)

通过命令：`nohup ./dmserver /home/dmdba/dm_ssl_test/SSL_TEST/dm.ini &`重启达梦数据库。

<h3 id="4">4. JDBC连接SSL认证达梦数据库</h3>

JDBC连接达梦是通过`client_ssl/SYSDBA/.keystore`文件，而ODBA和其他方式是通过`client_SYSDBA`目录下`ca-cert.pem`、`client-cert.pem`和`client-key.pem`文件。

<h4 i="4.1">4.1 达梦Manager工具JDBC连接</h4>

切换到`tool`，执行`./manager`能调出来图形化界面（如果是在Linux系统中打开manager报错，不能调出来图形化界面，需要了解下Linux X SERVER知识）。如下图所示，像配置SSL并开启配置之前那样正常输入用户名和密码，连接不成功：

![](/img/posts/dm-manager-jdbc-sslconn-common-error.png)

点击高级，指定`SSL目录`为`client_ssl/SYSDBA`客户端证书目录，输入`keystore`生成设定的密码，本例中为`abc123`，重新连接即可成功连接SSL认证达梦数据库，如图所示：

![](/img/posts/dm-manager-jdbc-sslconn-common-success.png)

连接成功后，数据库SSL连接为是：

![](/img/posts/dm-manager-jdbc-sslconn-common-success2.png)

<h4 id="4.2">4.2 原生Java JDBC程序连接</h4>

达梦自带manager连接数据库的方式就是`JDBC`，驱动在目录`tool\dropins\com.dameng\plugins\com.dameng.jdbc.drivers`下，可以使用原生Java JDBC程序连接验证：

```java
import java.sql.*;

public class FirstExample {
   static final String DB_URL = "jdbc:dm://xx.26.19.77:5336?sslFilesPath=client_ssl/SYSDBA&sslKeystorePass=dameng";
   static final String USER = "SYSDBA";
   static final String PASS = "SYSDBA";
   static final String QUERY = "SELECT * from dba_users";

   public static void main(String[] args) {
      // Open a connection
      try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(QUERY);) {
         // Extract data from result set
         while (rs.next()) {
            // Retrieve by column name
            System.out.println("USERNAME:" + rs.getString("USERNAME"));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } 
   }
}
```

编译并设定好`classath`执行，能够成功连接SSL达梦。


<h3 id="5">5. 注意事项</h3>

1. JDBC方式使用的是keystore密码，而ODBC、disql等其他连接使用的是证书文件。
2. 注意服务端和客户端目录的权限，设置为777
3. 本地测试，dbeaver客户端没有成功通过JDBC连接到SSL认证达梦，原因未知

<h3 id="6">6. 参考</h3>

- [https://eco.dameng.com/community/article/af22b724ddb048f400003a7312122f28](https://eco.dameng.com/community/article/af22b724ddb048f400003a7312122f28)
- [https://www.modb.pro/db/98970](https://www.modb.pro/db/98970)

---
