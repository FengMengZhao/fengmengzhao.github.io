---
layout: post
title: 理解SQL的join用法
---

当我们在试图理解`SQL`中的`join`用法时，获得大量的信息都是韦恩图(Venn Diagram)示例。然而，韦恩图真的能准确的说明`join`的用法，不然！

实际上，韦恩图只适合以下三种的`SQL`操作：

- UNION(并集)
- INTERSECT(交集)
- EXCEPT(补集)

我们可以用这样的韦恩图来表示它们：

**UNION(并集)**

![并集](/img/posts/venn-union.png)

**INTERSACT(交集)**

![交集](/img/posts/venn-intersection.png)

**EXCEPT(补集)**

![补集](/img/posts/venn-difference.png)

关键的问题在于：韦恩图的操作基于的集合都是同种类型的。例如图中示例，所有的记录都是由`first name`和`last name`组成，如果不是由同种类型的记录组成的集合，则`INTERSECT`和`EXCEPT`操作就变的没有什么意义了。那么，如果我想将演员表和对应的电影表关联起来，用韦恩图这种方法显然就不可能了。

于是，`SQL`中出现了`join`，可以用来关联不同类型的数据。那么`join`的实质是什么呢？

`join`说白了是：带有`filter`的笛卡尔积(cartesian produce)，用图说明：

![join是带有filter的笛卡尔积](/img/posts/venn-cross-product.png)

我们使用什么样的方法来形象化`join`操作呢？

首先我们来看看`cross join`吧，`cross join`是一种笛卡尔积的形式，任何类型的`join`都可以从`cross join`中推到出来。

![cross join](/img/posts/veen-cross-join1.png)

需要注意的是：`cross join`也可以写成用逗号隔开表的形式，它做的事情仅仅是将左边的每一条记录和右边的每一条记录组合起来。如果左边是3条记录，右边是4条记录，那么组合及时`3x4=12`条记录。

**所有的join都是基于cross join，再辅助与一定的filter，有的时候还要加上UNION操作。**

**INNER JOIN**

![INNER JOIN](/img/posts/venn-join1.png)

`INNER JOIN`是`cross join`结果集中再指定一个过滤条件，看一个SQL示例：

    -- "Classic" ANSI JOIN syntax
    SELECT *
    FROM author a
    JOIN book b ON a.author_id = b.author_id
     
    -- "Nice" ANSI JOIN syntax
    SELECT *
    FROM author a
    JOIN book b USING (author_id)
     
    -- "Old" syntax using a "CROSS JOIN"
    SELECT *
    FROM author a, book b
    WHERE a.author_id = b.author_id

**OUTER JOIN**

`OUTER JOIN`可以让你在没有满足指定过滤条件时，仍然保留左边或者右边或者两边的记录。

用`LEFT JOIN`做一个SQL示例：

    SELECT *
    FROM author a
    LEFT JOIN book b USING (author_id)

这个SQL将作者和著作用笛卡尔积的方式先组合起来，然后过滤出来有相同的author_id的记录，并且如果作者没有相应的著作，仍然保留作者的记录，著作的记录用`NULL`表示。

用基本的SQL来表示上述的`LEFT JOIN`操作：

    SELECT *
    FROM author a
    JOIN book b USING (author_id)
     
    UNION
     
    SELECT a.*, NULL, NULL, NULL, ..., NULL
    FROM (
      SELECT a.*
      FROM author a
       
      EXCEPT
       
      SELECT a.*
      FROM author a
      JOIN book b USING (author_id)

    ) a

> 当你再向你小伙伴解释`join`操作时，就不要再用不确切的韦恩图方式了。
