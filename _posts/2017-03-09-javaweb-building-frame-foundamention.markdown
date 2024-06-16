---
layout: post
title: javaweb框架开发篇之Struts基础开发
---

## 目录

- [1 Struts简介](#1)
- [2 配置Struts开发环境](#2)
- [3 开发第一个Struts程序](#3)
- [4 Struts的工作原理](#4)

---

---

<h2 id="1"> 1 Struts简介</h2> 

Struts是Apache基金组织中Jakarta项目组的一个开源项目，主要实现mvc设计模式，在Struts中有自己的控制器(ActionServlet)，同时也提供了各种的常用的页面标签库以减少JSP页面中的Scriptlet代码。


---

---

<h2 id="2"> 2 配置Struts开发环境</h2> 

**1. 复制全部的Struts解压缩文件夹中的lib文件夹中的jar包到项目的lib包**

**2. 配置struts-config.xml文件-WEB-INF/struts-config.xml**

```
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE struts-config PUBLIC "-//Apache Software Foundation//DTD Struts Configuration 1.3//EN" "http://struts.apache.org/dtds/struts-config_1_3.dtd"> 

<struts-config> 

    <form-beans /> 	用于配置ActionForm
    <global-exceptions /> 	用于配置全局异常
    <global-forwards /> 	用于配置Action
    <action-mappings /> 	用于配置资源文件路径，资源文件保存在WEB-INF/classes文件夹中，通过parameter指定路径及文件名称，文件名称的后缀是*.properties。
    <message-resources parameter="org.fmz.struts.ApplicationResources" /> 

</struts-config> 
```

**3. 配置web.xml文件**

```
<?xml version="1.0" encoding="UTF-8"?> 
<web-app xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="3.0" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee   http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"> 
<display-name /> 
    <servlet> 
        <servlet-name> action</servlet-name> 
        <servlet-class> org.apache.struts.action.ActionServlet</servlet-class> 
        <init-param> 
            <param-name> config</param-name> 
            <param-value> /WEB-INF/struts-config.xml</param-value> 
        </init-param> 
        <init-param> 
            <param-name> debug</param-name> 
            <param-value> 3</param-value> 
        </init-param> 
        <init-param> 
            <param-name> detail</param-name> 
            <param-value> 3</param-value> 
        </init-param> 
        <load-on-startup> 0</load-on-startup> 
    </servlet> 
    <servlet-mapping> 
        <servlet-name> action</servlet-name> 
        <url-pattern> *.do</url-pattern> 
    </servlet-mapping> 
<welcome-file-list> 
<welcome-file> index.jsp</welcome-file> 
</welcome-file-list> 
</web-app> 
```

> 在Struts中配置的*.do的映射路径是Struts的一个著名标志。

4. 增加标签库配置，配置web.xml文件

```
<jsp-config> 
    <taglib> 
        <taglib-uri> http://www.fmz.org/struts/bean</taglib-uri> 
        <taglib-location> /WEB-INF/struts-bean.tld</taglib-location> 
    </taglib> 
    <taglib> 
        <taglib-uri> http://www.fmz.org/struts/logic</taglib-uri> 
        <taglib-location> /WEB-INF/struts-logic.tld</taglib-location> 
    </taglib> 
    <taglib> 
        <taglib-uri> http://www.fmz.org/struts/html</taglib-uri> 
        <taglib-location> /WEB-INF/struts-html.tld</taglib-location> 
    </taglib> 
</jsp-config> 
```

> Struts一共提供了五大种标签库，即bean、logic、html、nested和tiles，其中重点为前三个标签库。

---

---

<h2 id="3"> 3 开发第一个Struts程序</h2> 

建立JSP页面-hello.jsp

```
<%@ page language="java" pageEncoding="utf-8"%> 

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%> 
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%> 
<html:html lang="true"> 
<head> 
    <html:base /> 
    <title> hello.jsp</title> 
</head> 
<body> 
    <html:errors/> 
    <html:messages id="error_info" message="true"> 
        ${error_info}
    </html:messages> 
    <logic:present name="msg" scope="request"> 
        <h2> ${msg}</h2> 
    </logic:present> 
    <html:form action="hello.do" method="post"> 
        请输入信息：<html:text property="info"> </html:text> 
        <html:submit value="显示"> </html:submit> 
        <html:reset value="重置"> </html:reset> 
    </html:form> 
</body> 
</html:html> 
```

建立ActionForm-HelloForm.java

```
package org.fmz.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/*- 
 - MyEclipse Struts
 - Creation date: 09-19-2015
 - 
 - XDoclet definition:
 - @struts.form name="helloForm"
 */
@SuppressWarnings("serial")
public class HelloForm extends ActionForm {
    /*
     - Generated fields
     */

    /*- info property */
    private String info;

    /*
     - Generated Methods
     */

    /*- 
     - Method validate
     - @param mapping
     - @param request
     - @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (this.info == null || "".equals(this.info)) { // info的输入内容为空
            // 现在应该保存错误信息
            errors.add("error_info", new ActionMessage("emptyError.info"));
        }
        return errors;
    }

    /*- 
     - Method reset
     - @param mapping
     - @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
    }

    /*- 
     - Returns the info.
     - @return String
     */
    public String getInfo() {
        return info;
    }

    /*- 
     - Set the info.
     - @param info The info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }
}
```

> HelloForm类的主要功能是验证，此类继承ActionForm类，

定义Action-HelloAction.java

```
package org.fmz.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fmz.struts.form.HelloForm;

public class HelloAction extends Action {
    /*
     - Generated Methods
     */

    /*- 
     - Method execute
     - @param mapping
     - @param form
     - @param request
     - @param response
     - @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        HelloForm helloForm = (HelloForm) form;// TODO Auto-generated method
        String info = helloForm.getInfo(); // 所有的输入内容从ActionForm取出
        if(info.length() >  15){
            ActionMessages errors = new ActionMessages() ;
            errors.add("error_info",new ActionMessage("tooLangError.info")) ;//增加一个新的错误
            super.saveMessages(request,errors) ;//保存错误
            return mapping.getInputForward() ;//挑战到input指定页面
        }else{
            request.setAttribute("msg", info); // 将信息设置在request范围之中
        }
        return mapping.findForward("show"); // 此处返回的是一个映射的路径
    }
}
```

> 在Struts中实际上每个Action相当于一个Servlet，HelloAction类要继承Action类，在Action中可以通过ActionForm取得用户输入的参数，并将此参数放在request属性范围中，最后使用ActionMapping中的findForward()方法进行跳转。

配置struts-config.xml

```
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE struts-config PUBLIC "-//Apache Software Foundation//DTD Struts Configuration 1.3//EN" "http://struts.apache.org/dtds/struts-config_1_3.dtd"> 

<struts-config> 
    <form-beans > 
        <form-bean name="helloForm" type="org.fmz.struts.form.HelloForm" /> 	表示配置的每一个ActionForm
    </form-beans> 

    <global-exceptions /> 
    <global-forwards /> 
    <action-mappings > 
        <action		表示配置的每一个Action
            attribute="helloForm"	指定此Action要使用的ActionForm名称
            input="/hello.jsp"	表示当验证出错时要跳转的错误显示页
            name="helloForm"	指定此Action要使用的ActionForm名称
            path="/hello"		此Action的对应路径，此处为hello.do
            scope="request"		表示Action的作用范围，有request和session两种
            type="org.fmz.struts.action.HelloAction"	此Action对应的包.类名称
            cancellable="true" > 
            <forward name="show" path="/hello.jsp"> </forward> 	表示映射的跳转路径，同时可以定义多个<forward> 节点
        </action> 
    </action-mappings> 

    <message-resources parameter="org.fmz.struts.ApplicationResources" /> 
</struts-config> 
```

---

---

<h2 id="4"> 4 Struts的工作原理</h2> 

1. 在web.xml文件中为ActionServlet配置一个映射路径，一般都为*.do
2. 当一个JSP页面执行时，如果使用html标签定义的表单，则会根据action指定的路径与struts-config.xml文件中的路径相匹配，如果匹配失败则报错
3. 在运行一个JSP页面前，会调用指定的ActionForm中的reset()方法，进行表单元素的初始化操作
4. 用户提交表单时，会将所有的操作都提交到ActionServlet(有*.do指定)中，之后由ActioServlet根据struts-config文件中的配置调用指定的ActionForm和Action进行处理
5. 表单提交数据首先会交给ActionForm处理，自动调用validate()方法进行验证，如果成功则交个相应的Action进行处理；如果失败则提交到Action中配置的input属性中指定的页面路径，此事可以通过<html:errros/> 标签显示所有的错误信息
6. Action负责完成所有的业务操作(如调用DAO操作)，并根据操作的结果通过ActionMapping进行跳转，ActionMapping的findForward()方法返回一个ActionForward对象以完成跳转

---

---
