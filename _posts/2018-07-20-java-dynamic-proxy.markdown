---
layout: post
title: Java动态代理
subtitle: 
background: '/img/posts/6-or-9.jpg'
comment: true
---

Java的动态代理是一个比较难理解的概念，静态代理和动态代理有什么区别？代理模式和装饰器模式何其相像，它们有什么区别？怎么使用动态代理？这些问题，我们一探究竟。

代理对象往往用在当我们想在客户端(Client)和被代理对象(target)之间增加一个中间层的时候。

首先我们来看一看静态代理。

静态代理的代理类(Proxy Class)是提前编译好的，当使用代理类的时候，该类会被JVM加载，生成代理类对象。

假如我们有一个交通工具(`IVehicle`)的接口，接口有一个`Car`的实现，我们看看如果对target(`IVehicle`)实现代理：

    package com.fmz.pattern;

    public interface IVehicle {

        void start();
        void stop();
        void forward();
        void reverse();
        String getName();
    }

> `IVehicle`交通工具类，也是我们要代理的对象(target)。

    package com.fmz.pattern;

    public class Car implements IVehicle {

        private String name;

        public Car(String name){
            this.name = name;
        }

        @Override
        public void start(){
            System.out.println("小轿车" + name + " 启动了！");
        }

        @Override
        public void stop(){
            System.out.println("小轿车" + name + " 停下来了！");
        }

        @Override
        public void forward(){
            System.out.println("小轿车" + name + " 向前进！");
        }

        @Override
        public void reverse(){
            System.out.println("小轿车" + name + " 掉头了！");
        }

        @Override
        public String getName(){
            return this.name;
        }
    }

> `Car`小轿车类，交通工具的实现。

    package com.fmz.pattern;

    public class Client1 {

        public static void main(String args[]){
            IVehicle v = new Car("别克");
            System.out.println("这是一辆" + v.getName() + "小轿车！");
            v.start();
            v.forward();
            v.reverse();
            v.stop();
        }
    }/*output:
        这是一辆别克小轿车！
        小轿车别克 启动了！
        小轿车别克 向前进！
        小轿车别克 掉头了！
        小轿车别克 停下来了！
    */

> `Client1`客户端。没有代理的情况下客户端和target进行交互。

![没有代理的调用](/img/posts/java-no-proxy.png "没有代理的调用")

*现在我们想在交通工具每次执行方法时，向控制台做一个输出，实现这样的代理，增加代理类：*

    package com.fmz.pattern;

    public class ProxyVehicle implements  IVehicle {
        private IVehicle v;

        public ProxyVehicle(IVehicle v){
            this.v = v;
        }

        @Override
        public void start(){
            System.out.println("运输工具代理: 该工具要启动了！");
            v.start();
        }

        @Override
        public void stop(){
            System.out.println("运输工具代理: 该工具要停止了！");
            v.stop();
        }

        @Override
        public void forward(){
            System.out.println("运输工具代理: 该工具要前进了！");
            v.forward();
        }

        @Override
        public void reverse(){
            System.out.println("运输工具代理: 该工具要掉头了！");
            v.forward();
        }

        @Override
        public String getName(){
            System.out.println("运输工具代理: 有人取得了该工具的名称！");
            return v.getName();
        }
    }

> `ProxyVehicle`代理类。代理类实现了target接口，同时持有了target的一个引用，在覆写方法的时候对target接口做了相应的访问控制(access control)。

    package com.fmz.pattern;

    public class Client2 {

        public static void main(String args[]){
            IVehicle v = new Car("别克");
            ProxyVehicle pv = new ProxyVehicle(v);
            System.out.println("这是一辆" + pv.getName() + "小轿车！");
            pv.start();
            pv.forward();
            pv.reverse();
            pv.stop();
        }
    }/*output:
        运输工具代理: 有人取得了该工具的名称！
        这是一辆别克小轿车！
        运输工具代理: 该工具要启动了！
        小轿车别克 启动了！
        运输工具代理: 该工具要前进了！
        小轿车别克 向前进！
        运输工具代理: 该工具要掉头了！
        小轿车别克 向前进！
        运输工具代理: 该工具要停止了！
        小轿车别克 停下来了！
    */

> `Client2`客户端。客户端和代理类进行交互，代理类实现了target接口同时持有了target的一个引用，在覆写的代理方法中对target进行访问控制，实现代理。

![有代理的调用](/img/posts/java-proxy.png "有代理的调用")

上述的代理我们称之为静态代理，代理类`ProxyVehicle`需要我们来写，提前编译好；代理类实现了target接口，需要覆写接口定义的所有方法。

Java API 1.3提供了动态代理的功能，使用动态代理，我们不需要编写代理类，代理类是在运行时动态生成的。

**动态代理相关的基本概念：**

> 动态代理类: 在运行时动态创建并实现了一系列接口的代理类<br><br>
代理接口：被动态代理类实现的一个接口List(可以是多个接口)<br><br>
代理实例：代理类的一个实例化对象

每一个代理实例要组合一个`invoke handler object`，这个对象实现了`InvocationHandler`接口。

通过代理接口对代理实例方法的调用会被转发(dispatch)到代理实例的`invoke handler object`的`invoke()`方法上。

动态代理类创建通过`java.lang.reflect.Proxy`来实现，看一看API里面的重要方法：

`public static Class getProxyClass(ClassLoader loader, Class[] interfaces) throws IllegalArguementException`

> 在一个指定的类加载器中创建一个实现指定系列接口的动态代理类。

`public static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler ih) throws IllegalArguementException`

> 在一个指定的类加载器中创建一个实现指定系列接口的动态代理类。同时，根据指定的与动态代理对象相关联的`invoke handler object`，通过调用一个公共的动态代理类构造方法，创建一个代理实例，并返回实例的引用。<br><br>
`Proxy.newProxyInstance(cl, interfaces, ih);`和`Proxy.getProxyClass(cl, interfaces).getConstructor()(new Class[]{InvocationHandler.class}).newInstance(new Object[]{ih});`是等价的。

每一个代理实例都要和一个`invoke handler object`相关联，当代理实例中的方法被调用时，该方法的调用会被编码并且转发给`invoke handler object`对象的`invoke()`方法。

`public Object invoke(Object proxy, Method method, Object[] args) throws Throwable`

> 处理代理实例的方法调用并返回结果。`proxy`参数是被调用的代理实例；`method`参数是代理实例实现接口的方法对象；`args`是接口中方法的参数集合，如果方法的参数为空，则args为null。

接下来看一看，如何实现对`IVehicle`的动态代理：

*首先创建一个InvocationHandler对象：*

    package com.fmz.pattern;

    import java.lang.reflect.*;

    public class VehicleHandler implements InvocationHandler {
        
        private IVehicle v;

        public VehicleHandler(IVehicle v){
            this.v = v;
        }

        @Override
        public Object invoke(Object proxy, Method m, Object[] args) throws Exception{
            System.out.println("运输工具 Handler：调用" + m.getName() + "方法！");
            return m.invoke(v, args);
        }
    }

> `m.invoke(v, args)`是通过反射的API实现了target对象恰当方法的调用。

    package com.fmz.pattern;

    import java.lang.reflect.*;

    public class Client3 {

        public static void main(String args[]){
            IVehicle v = new Car("别克");
            IVehicle dynamicV = (IVehicle)Proxy.newProxyInstance(v.getClass().getClassLoader(), new Class[]{IVehicle.class}, new VehicleHandler(v));
            System.out.println("这是一辆" + dynamicV.getName() + "小轿车！");
            dynamicV.start();
            dynamicV.forward();
            dynamicV.reverse();
            dynamicV.stop();
        }
    }/*output:
        运输工具 Handler：调用getName方法！
        这是一辆别克小轿车！
        运输工具 Handler：调用start方法！
        小轿车别克 启动了！
        运输工具 Handler：调用forward方法！
        小轿车别克 向前进！
        运输工具 Handler：调用reverse方法！
        小轿车别克 掉头了！
        运输工具 Handler：调用stop方法！
        小轿车别克 停下来了！
    */

> 实现了对target的动态代理。我们并没有创建代理类，只是创建了一个`invoke handler object`，传入`Proxy.newProxyInstance`的工厂方法中，就动态的创建了代理类(运行时)。

![java动态代理](/img/posts/java-dynamic-proxy.png)

**重点来了：**

> 我们为什么要使用动态代理?<br><br>
从上面的例子来看，我们不用编写`ProxyVehicle`类了，但是我们要编写`VehicleHandler`类，在客户端和target之间还是存在这个中间的一层<br><br>
那么，我们什么时候应该使用动态代理：<br>
1. 通用代理
2. 远程对象的代理

我们详细说明**通用代理**在动态代理中的应用:

假设我们想对`IVehicle`的方法调用进行日志输出，而我们不想改边原来`Car`的代码。就好像我们要装饰`IVehicle`一样，听起来和装饰器模式很像，其实**装饰器模式的实质就是通过组合来完成代理。**

    package com.fmz.pattern;

    public class LoggedVehicle implements  IVehicle {
        private IVehicle v;

        public LoggedVehicle(IVehicle v){
            this.v = v;
        }

        @Override
        public void start(){
            System.out.println("日志输出: 该工具要启动了！");
            v.start();
        }

        @Override
        public void stop(){
            System.out.println("日志输出: 该工具要停止了！");
            v.stop();
        }

        @Override
        public void forward(){
            System.out.println("日志输出: 该工具要前进了！");
            v.forward();
        }

        @Override
        public void reverse(){
            System.out.println("日志输出: 该工具要掉头了！");
            v.forward();
        }

        @Override
        public String getName(){
            System.out.println("日志输出: 有人取得了该工具的名称！");
            return v.getName();
        }
    }

> `LoggedVehicle`日志输出运输工具。这里的实现和`ProxyVehicle`其实是一样的，既可以说是装饰器模式，也可以说是代理模式。<br><br>
装饰器模式和代理模式的关键不同在于：代理模式的目的是为了控制访问(access control)；而装饰器模式的目的是为了动态增加(enhance)对象的行为(functionality)。

`LoggedVehicle`通过装饰器模式为任何实现`IVehicle`接口的交通工具提供了Logger的功能。然而这种模式有两个方面的缺点：

1. 在`LoggedVehicle`类中需要实现繁琐的实现`IVehicle`的所有接口
2. Logger这个功能是一个通用的功能，需要能够应用到其他接口上

如果使用动态代理的方式，上述的两个缺点可以被克服：

> 1. 动态代理将自动实现我们指定的所有接口，而不用我们自己进行繁琐的执行
2. 反射的方法调用，支持我们的代理通用到所有的接口

    package com.fmz.pattern;

    import java.lang.reflect.*;

    public class GenericLogger implements InvocationHandler {

        private Object proxyee;

        public GenericLogger(Object target){
            proxyee = target;
        }

        @Override
        public Object invoke(Object proxy, Method m, Object[] args) throws Exception {
            System.out.println("日志输出：" + m.getName());
            return m.invoke(proxyee, args);
        }
    }

> `GenericLogger`是一个`invoke handler object`。

    package com.fmz.pattern;

    import java.lang.reflect.*;

    public class Client5 {
        public static void main(String args[]){

            IVehicle v = new  Car("小轿车");
            IVehicle genericDynamicV = (IVehicle)Proxy.newProxyInstance(v.getClass().getClassLoader(), new Class[] {IVehicle.class}, new GenericLogger(v));
            genericDynamicV.start();
            genericDynamicV.forward();
            genericDynamicV.reverse();
            genericDynamicV.stop();
        }
    }/*output:
        日志输出：start
        小轿车小轿车 启动了！
        日志输出：forward
        小轿车小轿车 向前进！
        日志输出：reverse
        小轿车小轿车 掉头了！
        日志输出：stop
        小轿车小轿车 停下来了！
    */

> `Client5`客户端代码。

如果这个时候有一个新的接口，也需要这样输出日志，这样我们API(InvocationHandler)的代码就不用动了：

    package com.fmz.pattern;

    public interface IShape {

        void draw();
        void print();
        void move();
        void resize();
    }

> `IShape`新的Shape接口。

用之前的通用动态代理方法，让上面的接口输出日志：

    package com.fmz.pattern;

    import java.lang.reflect.*;

    public class Client6 {
        public static void main(String args[]){

            IShape s = new IShape(){
                @Override
                public void draw(){
                    System.out.println("画图了！");
                }

                @Override
                public void move(){
                    System.out.println("移动了！");
                }

                @Override
                public void print(){
                    System.out.println("打印了！");
                }
                
                @Override
                public void resize(){
                    System.out.println("重新定义大小了！");
                }
            };

            IShape genericDynamicS = (IShape)Proxy.newProxyInstance(s.getClass().getClassLoader(), new Class[] {IShape.class}, new GenericLogger(s));
            genericDynamicS.draw();
            genericDynamicS.move();
            genericDynamicS.print();
            genericDynamicS.resize();
        }
    }/*output:
        日志输出：draw
        画图了！
        日志输出：move
        移动了！
        日志输出：print
        打印了！
        日志输出：resize
        重新定义大小了！
    */

> `Client6`客户端。通用的`GenericLogger`只需要客户端改变下即可实现了Logger功能，莫不妙哉！
