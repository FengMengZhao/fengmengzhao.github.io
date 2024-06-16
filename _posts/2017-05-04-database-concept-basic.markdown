---
layout: post
title: 数据库学习之数据库系统概念
---

## 目录

- [1 简单SQL](#1)
- [2 高级SQL](#2)

---

---

<h2 id="1">1 简单SQL</h2>

### 基本域类型

`char(n)`：表示固定长度的字符串，用户指定长度n。也可以用全称character。

`varchar(n)`：表示可变长度的字符串，用户指定最大长度为n。等价于character varying。

`int`：整形类型，等价于integer。

`numeric(p, d)`：定点数，精度由用户指定。这个数有p位数字(还有一个符号位)，其中d位数字在小数点右边。numeric(3, 1)可以储存44.5，但是不能储存444.5或0.32这样的数。

`real, double precision`：浮点数与双精度浮点数，精度与机器相关。

`float(n)`：精度至少为n的浮点数。

SQL的基本模式定义：

	create table r(A1	D1,
		       A2	D2,
		       A3	D3,
		       ...,
		       完整性约束)

示例代码：

	 create table customer(
	 customer_name    char(20),
	 customer_street  char(30),
	 customer_city    char(30),
	 primary key(customer_name)
	) ;

插入语句：`insert into r values(...)`

删除语句：`delete from r`，此处的删除只是删除元组，不删除关系。

删除关系语句：`drop table r`

为已经有的关系增加属性：`alert table r add A D`，A代表属性的名字，D代表属性的域；`alert table r drop A`：表示从关系中去掉属性。

<h3 id="1.1">1.1 SQL查询的基本结构</h3>

select字句对应关系代数中的投影运算，用来列出查询结果所要的属性

>select子句默认是包含重复的，如果要强行删除重复，可以用：`distinc`；还可以显示的使用`all`来包含重复元组。

from字句对应关系代数中的笛卡尔积，它列出表达式求值中需要扫描的关系

where字句对应关系中的选择谓词，它包含一个作用在from字句中关系的属性上的谓词。

典型的sql查询：`select A1, A2, A3... from r1, r2, r3... where P`，如果where字句省略，则P为true。

更换属性名：`旧名 as 新名`

用as来修改元组变量：`select T.stname from st_homework_grade as T, st_homework_grade as S where T.st_5_grade > S.st_5_grade and S.stname = '董建芳'`

>上述用法主要用于同一个关系中的两个元组的比较中，使用它会特别有用。

#### 字符串运算

`''`可以表示`'`

`%`表示匹配任意子串

`_`表示匹配任意字符

> 上述模式是大小写敏感的

示例：`Perry%`表示匹配任意以"Perry"开头的字符串；`%idge%`表示匹配任何包含"idge"为子串的字符串；`___`表示匹配只含有三个字符的字符串；`__%`表示匹配至少含有三个字符的字符串

> 在sql语句中，当进行匹配的时候要用关键字：`like`。

排列元组的显示次序：`order by`，默认使用升序排列，可以用`desc`表示降序，用`asc`表示升序。

#### 集合的运算

`union`表示交集，默认会删除重复(只出现一次)，如果需要列出重复，需要显示的使用：`union all`。

`intersect`表示交集，默认删除重复(只出现一次)，列出重复，需使用：`intersect all`。

`except`表示补集，默认删除重复(只出现一次)，列出重复，需使用：`except all` 。

#### 聚集函数

`avg`平均数；`min`最小值；`max`最大值；`sum`总和；`count`计数。其中，sum和avg必须是数字集合，其他的运算符可以作用于非数字的数据类型。

我们可以将聚集函数应用在单个元组上：`select avg(balance) as '平均结余' from account where branch_name = 'ICBC' ;`。

我们也可应将聚集函数应用在一组元组集上，在SQL中可以使用`group by`子句实现：`select branch_name, avg(balance) as '平均结余' from account group by branch_name ;`。算出每个支行的平均结余。

有些情况，我们需要在聚集函数前面删除掉重复项：`select branch_name, count(distinct customer_name) as 储户数量 from depositor, account where depositor.account_number = account.account_number group by branch_name ;`。找出各个支行中储户的数量，如果具有相同的名字，则认为是一个储户。

我们还可以对分组进行条件的限定，在SQL语句中，我们使用`having`子句：`select branch_name, avg(balance) as '平均结余大于4500' from account group by branch_name having avg(balance) > 4500 ;`

我们可以将整个关系看成是一个分组：`select avg(balance) from account ;`

我们经常使用聚集函数count来计算一个关系中元组的个数：`select count(*) from customer ;`

总之查询都在这样的一个函数当中：`select A... form r... where P... group by A... having P...`。

#### 数据库的修改

删除：`delete from r where P in...`在这个可以用嵌套字句。例如：`delete from account where branch_name in (select branch_name from branch where assets >  738422233) ;`

插入：`insert into r values(...)`；也可以用嵌套字句：`insert into r select... from... where...`。例如：`insert into account select loan_number, branch_name, 22 from loan where branch_name = "ICBC" ;`。

更新，我们希望在不改变元组的情况下，来改变元组的属性值。语法：`update r set... where...`。例如：` update account set balance = balance - 10 where branch_name = '工商银行' ;`。

更新操作还提供了`case`结构，语法：

	case
		when pred1 then result1
		when pred2 then result2
		when pred3 then result3
		...
		when predn then resultn
		else result0
	end

例如：

	update account set balance = case
					when balance <=1000 then balance*1.05
					else balance*1.06
					end

#### 视图

任何不适逻辑模型的一部分但作为虚关系对用户可见的关系称之为视图(view)。

视图定义：`create view v as<查询表达式>`。例如：`create view account_view as select - from account`。

#### 事务(transaction)

事务是由查询或者更新语句序列组成。commit work：提交当前事务，也就是将该事物所做的更新在数据库中持久保存，事务被提交后，一个新的事务开始。rollback work：回滚当前事务，即，撤销该事物中所有sql语句对数据库的更新。这样数据库就恢复到执行该事物第一条语句之前的状态。

---

---

<h2 id="2">2 高级SQL</h2>

#### SQL的数据类型与模式

##### SQL的内建数据类型

`date`日历日期，包含年(四位)、月、日。

`time`一天中的时间，包含小时、分、秒。可以用一个变量time(p)来表示秒的小数点的后的数字(默认值为0)。

`timestamp`data和time的组合变量，可以用一个变量timestamp(p)表示秒的小数点后面的数字(这里的默认值6)。

##### 用户定义类型

语法：`create type Dollars as numeric(12, 2) final`

#### 完整性约束

完整性约束保证党授权用户对数据库进行修改时不会破坏数据的一致性。因此，完整性约束时防止对数据的以外破坏。

##### 单个关系的约束

`not null`、`unique`、`check(<谓词>)`

not null：示例代码：`create domain Dollars numeric(12, 2) not null`。如果插入空值，数据库都会产生错误的诊断信息。

unique：限定属性形成一个候选码。

check子句：`check(assets >= 0)`

##### 参照完整性(referential integrity)

参照完整性：一个关系中给定属性集上的取值也在另外一个关系的某一个属性的取值中出现。语法：`foreign key(A) references r`

---

---
