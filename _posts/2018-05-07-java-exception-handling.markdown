---
layout: post
title: Java的异常处理
subtitle: 让人让人忧的java异常处理机制
background: 'img/posts/java-exception.jpg'
comment: true
---

> Java对异常的定义

![Java Exception](/img/posts/exception-in-java.png)

- `UncheckExcption`
    - `RuntimeExceptin` & `Error`
- `CheckedException`
    - `Exception` exclude `RuntimeExceptin`

> `CheckedException`在编译时进行检查;一个方法如果`throw CheckedException`,必须对该方法进行`try{}catch{}`处理<br><br>
`RuntimeException`和`Error`在运行时进行检查<br><br>
`UncheckExcption`被认为是程序的错误.其中,`RuntimeException`被认为是程序可以掌控的错误;`Error`被认为是程序无法掌控的错误(多交给JVM处理)

### 9种异常处理的最佳实践

### 1. `try{}catch{}finally{}`或者`try-with-resource()`对打开的资源(Resources)进行关闭

当打开一个资源，进行读写操作时，如果捕获到异常，资源永久不会被关闭

    package org.fmz.exception;

    public void doNotCloseResourceInTry() {
        FileInputStream inputStream = null;
        try {
            File file = new File("./tmp.txt");
            inputStream = new FileInputStream(file);
            
            // use the inputStream to read a file
            
            // do NOT do this
            inputStream.close();
        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
    }

**用finally语句最终关闭资源**

    package org.fmz.exception;

    public void closeResourceInFinally() {
        FileInputStream inputStream = null;
        try {
            File file = new File("./tmp.txt");
            inputStream = new FileInputStream(file);
            
            // use the inputStream to read a file
            
        
        } catch (FileNotFoundException e) {
            log.error(e);
        
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                
                } catch (IOException e) {
                    log.error(e);
                
                }
            
            }
        
        }

    }

**用Java7中的try-with-resource自动关闭资源**

    package org.fmz.exception;

    public void automaticallyCloseResource() {
        File file = new File("./tmp.txt");
        try (FileInputStream inputStream = new FileInputStream(file);) {
            // use the inputStream to read a file
            
        
        } catch (FileNotFoundException e) {
            log.error(e);
        
        } catch (IOException e) {
            log.error(e);
        
        }

    }

### 2. 更加明确指定异常

当你能更明确的给你的同事或者client指明异常信息时，就不要仅仅使用`Exception`

    package org.fmz.exception;

    public void doNotDoThis() throws Exception { ...  }
        
    public void doThis() throws NumberFormatException { ...  }

### 3. 注释你指定的异常(自定义异常)

方法中抛出的异常就像方法的参数一样，是需要你注释的

    package org.fmz.exception;

    /**
    * This method does something extremely useful ...
    *
    * @param input
    * @throws MyBusinessException if ... happens
    */
    public void doSomething(String input) throws MyBusinessException { ...  }

### 4. 抛出异常要有描述性的信息

一般从异常的类名就可以了解到，抛出的异常时什么问题导致的。所有在自定义异常时，要加上描述性的信息

    package org.fmz.exception;

    try {
        new Long("xyz");

    } catch (NumberFormatException e) {
        log.error(e);

    }

当你捕获到异常时，你就知道是什么问题导致异常了

    17:17:26,386 ERROR TestExceptionHandling:52 - java.lang.NumberFormatException: For input string: "xyz"

### 5. 最先捕获自定义异常

如果捕获异常的顺序是基类异常先于自定义异常，只有基类异常的捕获块会执行，这样就丢失了自定义异常中的重要信息，给debug定位带来问题

正确的做法是要先捕获继承层次结构底端的异常，再往上捕获

    package org.fmz.exception;

    public void catchMostSpecificExceptionFirst() {
        try {
            doSomething("A message");
        
        } catch (NumberFormatException e) {
            log.error(e);
        
        } catch (IllegalArgumentException e) {
            log.error(e)
        
        }

    }

### 6. 不要捕获`Throwable`

`Throwable`是所有异常和Error的父类，如果捕获`Throwable`你将捕获所有的异常和Error，而`Error`是JVM抛出来的，预示着严重的错误，但这个错误是程序控制不了的；典型的Error，如`OutOfMemoryError`和`StackOverflowError`，堆和栈的内存异常这样的异常时程序控制不了的.

**不要使用下面示例代码**

    package org.fmz.exception;

    public void doNotCatchThrowable() {
        try {
            // do something
        
        } catch (Throwable t) {
            // don't do this!
        
        }

    }

### 7. 不要捕获异常而不做任何处理

如果捕获了异常，而不做任何处理，程序的异常信息将无法被记录，这样你的程序是有问题的，你却不知道

**不要这样捕获异常**

    package org.fmz.exception;

    public void doNotIgnoreExceptions() {
        try {
            // do something
        
        } catch (NumberFormatException e) {
            // this will never happen
        
        }

    }

**至少你要在日志中输出一下异常信息**

    package org.fmz.exception

    public void logAnException() {
        try {
            // do something
        
        } catch (NumberFormatException e) {
            log.error("This should never happen: " + e);
        
        }

    }

### 8. 不要日志异常后又重新`throw`

日志输出后重新`throw`异常，似乎是顺利成章的事情

    package org.fmz.exception;

    try {
        new Long("xyz");

    } catch (NumberFormatException e) {
        log.error(e);
        throw e;

    }

这样带来的问题是：同样的异常信息在日志中会出现多处

    17:44:28,945 ERROR TestExceptionHandling:65 - java.lang.NumberFormatException: For input string: "xyz"
    Exception in thread "main" java.lang.NumberFormatException: For input string: "xyz"
        at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
        at java.lang.Long.parseLong(Long.java:589)
        at java.lang.Long.(Long.java:965)
        at com.stackify.example.TestExceptionHandling.logAndThrowException(TestExceptionHandling.java:63)
        at com.stackify.example.TestExceptionHandling.main(TestExceptionHandling.java:58)

这种多出来的异常信息，没有给我们提供多余有价值的信息。异常信息应该描述异常事件，`Stack Trace`应该描述哪一个类，什么方法，哪一行抛出的异常

如果需要给输出的异常增加额外的信息，应该封装自定义的异常，在方法签名中定义它

    package org.fmz.exception;

    public void wrapException(String input) throws MyBusinessException {
        try {
            // do something
        
        } catch (NumberFormatException e) {
            throw new MyBusinessException("A message that describes the error.", e);
        
        }

    }

因此，原则是：仅仅捕获你能够处理的异常，否则定义在方法签名中，让调用者捕获它

### 9. 抛出自定义异常时，要将原始异常作为参数传入

`Exception`类提供了一个构造方法可以传入一个`Throwable`对象，如果不传入原始异常，整个异常栈的追踪会出现问题

经典的自定义异常的方法是：

    package org.fmz.exception;

    public class CustomException extends Exception {

        public CustomException(String message) {
            super(message);
        
        }

        public CustomException(String message, Throwable throwable) {
            super(message, throwable);
        
        }


    }

> 参考文章：<br>
- [9 Best Practices to Handle Exceptions in Java](https://stackify.com/best-practices-exceptions-java/)
- [Is it really that bad to catch a general exception?](https://stackoverflow.com/questions/21938/is-it-really-that-bad-to-catch-a-general-exception)
