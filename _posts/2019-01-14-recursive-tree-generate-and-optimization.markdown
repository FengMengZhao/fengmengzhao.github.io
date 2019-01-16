---
layout: post
title: '递归树生成和优化'
subtitle: '日常的开发中，递归树很常见。比如，一个递归的目录结构、导航栏等。在数据库中存储也通常是PID的方式呈现。如何将数据库中的数据通过JSON的方式表示出来，本文提供三种方法，其本质都是递归。递归时访问数据库是大忌，需要将基础数据一次性查到内存中，再递归处理。'
background: '/img/posts/recursive-tree-generate-and-optimization.png'
comment: true
---

# 目录

- [1. 问题描述](#1)
- [2. Mybatis collection一对多递归调用方法](#2)
- [3. 程序中递归调用方法](#3)
- [4. 基础数据加载到内存后递归调用方法](#4)

---

<h3 id="1">问题描述</h3>

数据库中的数据如下图：

![数据](/img/posts/recursive-tree-generate-and-optimization_1.png "数据")

建表语句：

    -- mysql建表语句
    DROP TABLE IF EXISTS "sys_dept";
    CREATE TABLE "sys_dept" (
      "dept_id" varchar(300) DEFAULT NULL,
      "dept_name" varchar(300) DEFAULT NULL,
      "is_use" varchar(300) DEFAULT NULL,
      "parent_id" varchar(300) DEFAULT NULL
    ) 

    INSERT INTO "sys_dept" VALUES ('1', '**软件', '0', '-1');
    INSERT INTO "sys_dept" VALUES ('2', '**信息', '0', '-1');
    INSERT INTO "sys_dept" VALUES ('3', '经理', '0', '1');
    INSERT INTO "sys_dept" VALUES ('4', '审计办公室', '0', '1');
    INSERT INTO "sys_dept" VALUES ('5', '北京研发中心', '0', '2');
    INSERT INTO "sys_dept" VALUES ('6', '大数据开发部', '0', '5');
    INSERT INTO "sys_dept" VALUES ('7', '测试部', '0', '2');
    INSERT INTO "sys_dept" VALUES ('8', '开发二组', '0', '6');
    INSERT INTO "sys_dept" VALUES ('9', '产品开发部', '0', '2');

需要将上述数据转化为JSON，格式为：

    [{
        "id": "1",
        "name": "**软件",
        "childrenList": [{
            "id": "3",
            "name": "经理"
        },
        {
            "id": "4",
            "name": "审计办公室"
        }]
    },
    {
        "id": "2",
        "name": "**信息",
        "childrenList": [{
            "id": "5",
            "name": "北京研发中心",
            "childrenList": [{
                "id": "6",
                "name": "大数据开发部",
                "childrenList": [{
                    "id": "8",
                    "name": "开发二组"
                }]
            }]
        },
        {
            "id": "7",
            "name": "测试部"
        },
        {
            "id": "9",
            "name": "产品开发部"
        }]
    }]

---

<h3 id="2">Mybatis collection一对多递归调用方法</h3>

数据对应的POJO对象为：

    public class DeptTree {
        private String id;
        private String name;
        private List<DeptTree> childrenList;//子节点

        ...
    }

Mapper文件：

    <select id="selectDeptChildrenById" resultMap="deptTree" parameterType="string">
        select dept_id, dept_name from sys_dept
        -->
        <!-- 当有参数注入的时候，不能写为1行：
            select dept_id, dept_name from "public"."sys_dept" where parent_id = #{id}
        -->
        where parent_id = #{id}
    </select>

    <!-- Mybatis resultMap collection标签下面有select属性，可以递归调用 -->
    <!-- 这种递归调用的方式存在性能问题慎重使用 -->
    <resultMap type="com.fmz.learn.spring.pojo.DeptTree" id="deptTree">
        <id column="dept_id" property="id" javaType="java.lang.String"/>
        <result column="dept_name" property="name" javaType="java.lang.String"/>
        <collection property="childrenList" column="dept_id" ofType="com.fmz.learn.spring.pojo.DeptTree"
                    javaType="java.util.ArrayList"
                    select="com.fmz.learn.spring.mapper.GetDeptTree.selectDeptChildrenById"/>
    </resultMap>

使用方法：`mapper.selectDeptChildrenById("-1")`调用时，会在resultMap中发生递归调用，生成JSON对应的实体类。

这种递归调用的方法，会一遍一遍的执行子查询，有性能方面的问题。

---

<h3 id="3">程序中递归调用方法</h3>

考虑到上面方法执行的性能问题，事实上，当数据有5000条左右时，执行的时间就需要15s左右。

想通过在程序中递归调用的方法执行。

service层的递归方法：

    private void getDeptTree(List<DeptTree> result){

        for(DeptTree dt : result) {
                List<DeptTree> childrenList = getDeptTree.selectDeptChildrenById(dt.getId());
                if(childrenList != null){
                    dt.setChildrenList(childrenList);
                
                }
                getDeptTree(dt.getChildrenList());//这里是递归调用
            
        }
    }

Mapper文件：

    <resultMap type="com.fmz.learn.spring.pojo.DeptTree" id="deptTree">
        <id column="dept_id" property="id" javaType="java.lang.String"/>
        <result column="dept_name" property="name" javaType="java.lang.String"/>
    </resultMap>

> result是传进来的根节点的数据，递归根节点的数据，配合查询数据库来将设置子节点。

事实上，这种方法比第一种方法还要糟糕，原因除了每次递归调用要执行SQL之外，还要有访问数据库的消耗。

这种在递归调用中访问数据库的方式在开发公司的技术规范中都是命令禁止的。

---

<h3 id="4">基础数据加载到内存后递归调用方法</h3>

再考虑，需要将每个节点及其子节点的数据都先出来，在递归中使用到的时候不去数据库查询，而直接从内存中读取，这样就不存在性能的问题了。

Mapper文件：

    <select id="queryParentidChildrenMap" resultMap="deptTreeMap">
        select * from sys_dept
    </select>

    <resultMap type="com.fmz.learn.spring.pojo.DeptTree" id="deptTreeMap">
        <id column="parent_id" property="id" javaType="java.lang.String"/>
        <collection property="childrenList" ofType="com.fmz.learn.spring.pojo.DeptTree">
            <id property="id" column="dept_id" javaType="java.lang.String" />
            <id property="name" column="dept_name" javaType="java.lang.String" />
        </collection>
    </resultMap>

需要将Mapper映射得到的数据转化为一个Map，这样方便递归时查询：


    private Map<String, List<DeptTree>> compactMap(List<DeptTree> paramResult) {
            Map<String, List<DeptTree>> result = new HashMap<>();
            for(DeptTree dt : paramResult) {
                result.put(dt.getId(), dt.getChildrenList());
            
            }
            return result;
    }

使用上述得到的Map，再递归设置子节点：

    private void getDeptTree(List<DeptTree> result, Map<String, List<DeptTree>> map) {

        for(DeptTree dt : result) {
                List<DeptTree> childrenList = map.get(dt.getId());
                if(childrenList != null){
                    dt.setChildrenList(childrenList);
                    getDeptTree(dt.getChildrenList(), map);
                
                }
            
        }
    }

---
