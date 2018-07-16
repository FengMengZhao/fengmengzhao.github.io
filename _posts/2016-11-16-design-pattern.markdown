---
layout: post
title: 设计模式
---

### 目录

- [1 模式是什么](#1)
- [2 设计模式原则](#2)
    - [2.1 开-闭原则](#2.1)
    - [2.2 单一职责原则](#2.2)
    - [2.3 里氏替换原则](#2.3)
    - [2.4 接口隔离原则](#2.4)
    - [2.5 依赖倒转原则](#2.5)
    - [2.6 迪米特法则](#2.6)
- [3 设计模式方法](#3)
    - [3.1 单例模式](#3.1)
    - [3.2 简单工厂模式](#3.2)
    - [3.3 工厂方法模式](#3.3)
    - [3.4 Builder 模式](#3.4)
    - [3.4 抽象工厂模式](#3.4)
    - [3.5 适配器模式](#3.5)
    - [3.6 装饰器模式](#3.6)
    - [3.7 代理模式](#3.7)
    - [3.8 合成模式](#3.8)
    - [3.9 桥梁模式](#3.9)
    - [3.10 模板模式](#3.10)
    - [3.11 策略模式](#3.11)
    - [3.12 迭代子模式](#3.12)
    - [3.13 观察者模式](#3.13)
    - [3.14 责任链模式](#3.14)
- [4 设计模式实践](#4)
    - [4.1 exercise-1](#4.1)

> 学习设计模式避免进入的误区：<br>
- 我们经常试图记住一些设计模式，而没有明白设计模式是要解决什么问题的
- 我们不能够准确的分析业务场景来恰当的使用设计模式，我们总是试图把自己知道的设计模式硬生生的套用在不恰当的业务场景中

<h3 id='1'>模式是什么?</h3>

人们在自己环境中不断的发现问题和寻找问题解决方案的时候,发现有一些问题及其解决方案不断变换面孔重复出现,但这些不同面孔后面有着共同的本质,这些共同的本质就是模式.

模式化的过程就是把问题抽象化，在忽略不重要的细节后，发现问题的一般性本质，并找出普遍适用的解决方案的过程.

*学而时习之,不亦乐乎?*

模仿+实践是一种学习新事物的模式.

*知行合一.*

知行合一是王阳明心学这追求的模式.

---

<h3 id='2'>设计模式原则</h3>

<h4 id='2.1'>开-闭原则(OCP)(Open-Closed Principle)</h4>

开-闭原则讲的是：一个软件实体应该对扩展开放，对修改关闭(Software entries should be open for extension, but closed for modification)

> - 对元素的修改关闭(不允许修改的是系统的抽象层)
- 对体系结构的修改开放(允许扩展的是系统的实现层)

![Simple Factory Pattern UML](/img/posts/simple_factory.png "简单工厂模式")

> 如果要新增加一种新的Product,只需要新增加一个Product类继承或者实现Product,完成了对体系结构的修改开放;而不必要去修改Product接口,完成了对元素的修改关闭.

<h4 id='2.2'>单一职责原则(Singleton Responsibility Principle, SRP)</h4>

不要存在多于一个导致类变更的原因，也就是说：一个类只做一件事。

类T负责两个不同的职责：职责P1，职责P2。当由于职责P1需求发生改变而需要修改类T时，有可能会导致原本运行正常的职责P2功能发生故障。另外，如果另外一个类需要复用职责P2，往往采用复制粘贴的办法，无法达到优雅的复用。

![单一职责原则示例](/img/posts/srp.png)

> `OrderController`类中应该只做控制相关的事情,而不应该有处理订单业务相关的接口.可以将处理订单业务相关接口封装为一个`Service`类,`OrderController`类与`Service`类通过关联关系进行协作,可以完成对职责的分离.<br><br>
再例如：Spring开发中的MVC模式，`controller`层只负责对请求参数的解析、业务逻辑层结果的封装、前端的交互等；`service`层只负责业务逻辑的处理等；`dao`层只负责持久层的交互等。

违背单一职责原则的开发之所以容易发生，是因为职责扩散。所谓职责扩散，就是因为某种原因，职责P被分化为粒度更细的职责P1和P2。

<h4 id='2.3'>里氏替换原则(Liskov Substitution Principle, LSP)</h4>

**定义:** 如果对每一个类型为T1的对象o1,都有类型为T2的对象o2,使得以T1定义的所有程序P在所有的对象o1都替换成o2时，程序P的行为没有发生改变，那么类型T2是类型T1的子类型。换言之，一个软件实体如果使用的是一个基类的话，那么也一定适用其子类，而且它根本不能察觉出基类对象和子类对象的区别.

> 里氏替换原则是继承复用的基石。只有当衍生类可以替换掉基类，软件的单位功不能受到影响时，基类才能真正被复用，而衍生类才能够在基类基础上增加新的行为

一般而言，如果有两个具体类A和B有继承关系，那么一个最简单的符合里氏替换原则的方案应该是：建立一个抽象类C，然后让类A和类B成为抽象类C的子类.

> - “白马，马也；乘白马，乘马也。骊马，马也；乘骊马，乘马也”
- “妹，美人也，爱妹，非爱美人也”

<h4 id='2.4'>接口隔离原则(Interface Segregation Principle)</h4>

接口隔离原则讲的是: 使用多个专门的接口比使用单一的总借口要好.

从一个客户端的角度来讲,一个类对另一个类的依赖性要建立在最小的接口之上.客户端不应该依赖它不需要的接口.

![臃肿的接口](/img/posts/isp-1.jpg "接口隔离原则")

![隔离的接口](/img/posts/isp-2.jpg "接口隔离原则")

<h4 id='2.5'>依赖倒转原则(Dependece Inversion Priciple)</h4>

依赖倒转原则讲的是: 要依赖于抽象,不要依赖于具体.

抽象不应该依赖于细节;细节应当依赖抽象.(Abstraction should not depend upon details. Details should depend upon abstractions)

[参考: 依赖接口而不是具体实现](https://fmzhao.github.io/thinking-in-java/#9)

<h4 id='2.6'>迪米特法则</h4>

**定义:**一个对象应该对其他对象保持最少的了解.

> 一个类对自己依赖的类知道的越少越好.

只与`直接的朋友`通信.`直接的朋友`是指:成员变量,参量,返回类型.局部变量不是`直接的朋友`.陌生的类最好不要作为局部变量的形式出现在类内部.

---

<h3 id='3'>设计模式方法</h3>

<h4 id='3.1'>单例模式(Singleton Pattern, creational)</h4>

![Singleton Pattern UML](/img/posts/singleton.png "单例模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/singleton)

> 单例模式要解决的问题：一个类只要求对外提供一个对象。

*轻对象单例模式代码：*

    package com.fmz.pattern;

    public class SingletonLightObject2 {
        
        private String attribute;

        private SingletonLightObject2(String attr){
            attribute = attr;
        
        };

        public void setAttribute(String attr){
            attribute = attr;
        
        }

        public String getAttribute(){
            return attribute;
        
        }

        private static final SingletonLightObject2 obj = new SingletonLightObject2("init attribute");

        public static SingletonLightObject2 getInstance(){
            return obj;
        
        }

    }

> 轻对象单例模式不涉及到线程的安全问题，对象在类初始化(load --> link --> init)的过程中创建对象。

*重对象单例模式代码：*

    package com.fmz.pattern;

    public class SingletonHeavyObject {

        private static SingletonHeavyObject obj = null;
        
        private String attribute;

        private SingletonHeavyObject(){}

        public void setAttribute(String attr){
            attribute = attr;
        
        }

        public String getAttribute(){
            return attribute;
        
        }

        public static SingletonHeavyObject getInstance() {
            if(obj == null){
                synchronized(SingletonHeavyObject.class){
                    if(obj == null){
                        obj = new SingletonHeavyObject();
                    
                    }
                
                }
            
            }
            return obj;
        
        }

    }

> 重对象(对象的实例化耗费较大的资源)采用了延迟初始化(lazy-instance)的方法创建单例对象，所谓的延迟实例化时指：当需要用到单例对象，即访问静态方法`getInstance()`时，进行单例对象的实例化，而不是在类加载的时候就完成了。

<h4 id='3.2'>简单工厂模式(Simple Factory Pattern, creational)</h4>

![Simple Factory Pattern UML](/img/posts/simple_factory.png "简单工厂模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/simple_factory)

*代码：*

    package com.fmz.pattern;

    public interface Fruit {

        void plant();

        void grow();

        Fruit harvest();

    }

> `Fruit`类是工厂生产对象的接口

    package com.fmz.pattern;

    public class Apple implements Fruit {

        @Override
            public void plant(){
                System.out.println("Apple has been plant!");
            }

        @Override
            public void grow(){
                System.out.println("Apple is growing!");
            }

        @Override
            public Fruit harvest(){
                System.out.println("Apple has been harvest!");
                return this;
            }

    }

> `Apple`是工厂生产的具体对象。

    package com.fmz.pattern;

    public class Orange implements Fruit {

        @Override
        public void plant(){
            System.out.println("Orange has been plant!");
        }

        @Override
        public void grow(){
            System.out.println("Orange is growing!");
        }

        @Override
        public Fruit harvest(){
            System.out.println("Orange has been harvest!");
            return this;
        }

    }

> `Orange`是工厂生产的具体对象。

    package com.fmz.pattern;

    public class FruitFactory {
        public Fruit getFruit(String fruitName) throws Exception {
            if("Apple".equals(fruitName)){
                return new Apple();
            }else if("Orange".equals(fruitName)){
                return new Orange();
            }else{
                throw new Exception("工厂不支持" + fruitName + "类型的水果!");
            
            }
        
        }

    }

> `FruitFactory`是水果的工厂类，当传入的参数不同时，返回不同的对象。<br><br>
这里存在一个问题：<br><br>
当工厂新进一中水果`Banana`时，如果想对客户端返回新的水果，则必须修改`FruitFactory`的生产逻辑，这不符合开闭原则(open for extension, closed for modification)，工厂方法模式解决了这个问题。

<h4 id='3.3'>工厂方法模式(Factory Method Pattern, creational)</h4>

![Factory Method Pattern UML](/img/posts/factory_method.png "工厂方法模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/factory_method)

*代码：*

> `Fruit.java`,`Apple.java`,`Orange.java`同简单工厂模式相同。

    package com.fmz.pattern;

    public interface FruitFactory {

        Fruit getFruit();

    }

> `FruitFactory`是一个抽象的工厂。

    package com.fmz.pattern;

    public class AppleFactory implements FruitFactory{

        @Override
        public Fruit getFruit() {
            return new Apple();
    
        }

    }

> `AppleFactory`是抽象工厂的一个具体实现，返回`Apple`对象。

    package com.fmz.pattern;

    public class OrangeFactory implements FruitFactory{

        @Override
        public Fruit getFruit() {
            return new Orange();
        }

    }

> `OrangeFactory`是抽象工厂的一个具体实现，返回`Orange`对象。

> 如果这个时候工厂新进了一种水果`Banana`，我们只需要分别实现一个`Fruit`接口和一个`FruitFactory`接口即可完成对程序的扩展，而无需修改代码，符合程序设计的开闭原则。

**新进的一种水果Banan扩展代码如下：**

    package com.fmz.pattern;

    public class Banana implements Fruit {

        @Override
        public void plant(){
            System.out.println("Banana has been plant!");
        }

        @Override
        public void grow(){
            System.out.println("Banana is growing!");
        }

        @Override
        public Fruit harvest(){
            System.out.println("Banana has been harvest!");
            return this;
        }
    }

> 新进的水果`Banana`。

    package com.fmz.pattern;

    public class BananaFactory implements FruitFactory{

        @Override
        public Fruit getFruit() {
            return new Banana();
        }
    }

> 新扩展的工厂类`BananaFactory`用来生产`Banana`。

<h4 id='3.4'>抽象工厂模式(Abstract Factory Pattern, creational)</h4>

![Abstract Factory Pattern UML](/img/posts/abstract_factory.png "抽象工厂模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/abstract_factory)

> 如果一家餐馆要制作一次饮食服务，服务包括开胃菜(Appetizer)、主食(Entree)和甜点(dessert)，这家餐馆可以理解为一个工厂，工厂需要生产一系列相关的产品。

*代码：*

    package org.fmz.pattern;

    public interface Appetizer{
        void eat();
    }

> `Appetizer`抽象开胃菜。

    package org.fmz.pattern;

    public class Oysters implements Appetizer{
        public void eat(){
            System.out.println("Eating oysters...");
        }
    }

> `Oysters`生蚝，开胃菜的一个实现。

    package org.fmz.pattern;

    public class Pizza implements Appetizer{
        public void eat(){
            System.out.println("Eating pizza...");
        }
    }

> `Pizza`披萨，开胃菜的一个实现。

    package org.fmz.pattern;

    public interface Entree{
        void eat();

    }

> `Entree`抽象主食。

    package org.fmz.pattern;

    public class Pasta implements Entree{
        public void eat(){
            System.out.println("Eating pasta...");
        }
    }

> `Pasta`意大利面，主食的一个实现。

    package org.fmz.pattern;

    public class Steak implements Entree{
        public void eat(){
            System.out.println("Eating steak...");
        }
    }

> `Steak`牛排，主食的一种实现。

    package org.fmz.pattern;

    public interface Dessert{
        void eat();

    }

> `Dessert`抽象甜点。

    package org.fmz.pattern;

    public class CheeseCake implements Dessert{
        public void eat(){
            System.out.println("Eating cheesecake...");
        
        }

    }

> `CheeseCake`奶酪蛋糕，甜点的一种实现。

    package org.fmz.pattern;

    public class Gelato implements Dessert {
        public void eat(){
            System.out.println("Eating gelato...");
        
        }

    }

> `Gelato`冰淇淋，甜点的一种实现。

    package org.fmz.pattern;

    public interface RestaurantOrderFactory {
        Order getOrder();

    }

> `RestaurantOrderFactory`工厂类接口，产生一个订单，订单中包含开胃菜、主食和甜点。

    package org.fmz.pattern;

    public class Order {
        private Appetizer appetizer;
        private Entree entree;
        private Dessert dessert;

        public Order(Appetizer appetizer, Entree entree, Dessert dessert){
            this.appetizer = appetizer;
            this.entree = entree;
            this.dessert = dessert;
        }

        public void setAppetizer(Appetizer appetizer){
            this.appetizer = appetizer;
        }

        public Appetizer getAppetizer(){
            return appetizer;
        }

        public void setEntree(Entree entree){
            this.entree = entree;
        }

        public Entree getEntree(){
            return entree;
        }

        public void setDessert(Dessert dessert){
            this.dessert = dessert;
        }

        public Dessert getDessert(){
            return dessert;
        }
    }

> `Order`订单，每个订单有开胃菜、主食和甜点组成。

    package org.fmz.pattern;

    public abstract class AbstractRestaurantOrderFactory implements RestaurantOrderFactory {

        @Override
        public Order getOrder(){
            return new Order(getAppetizer(), getEntree(), getDessert());
        }

        public abstract Appetizer getAppetizer();
        public abstract Entree getEntree();
        public abstract Dessert getDessert();
    }

> `AbstractRestaurantOrderFactory`抽象的订单工厂生产模板，实现基本的方法，将具体的实现抽象化到子类实现。

    package org.fmz.pattern;

    public class AmericanRestaurantOrderFactory extends AbstractRestaurantOrderFactory {

        @Override
        public Appetizer getAppetizer(){
            return new Oysters();//牡蛎
        }

        @Override
        public Entree getEntree(){
            return new Steak();//牛排
        }

        @Override
        public Dessert getDessert(){
            return new CheeseCake();//奶酪
        }
    }

> `AmericanRestaurantOrderFactory`美国订单生产工厂，生产符合美国口味的订单。

    package org.fmz.pattern;

    public class ItalianRestaurantOrderFactory extends AbstractRestaurantOrderFactory {

        @Override
        public Appetizer getAppetizer(){
            return new Pizza();//披萨
        }

        @Override
        public Entree getEntree(){
            return new Pasta();//意大利面
        }

        @Override
        public Dessert getDessert(){
            return new Gelato();//冰淇淋
        }

    }

> `ItalianRestaurantOrderFactory`意大利订单生产工厂，生产符合意大利口味的订单。

> 工厂模式的核心在于工厂，一般情况下使用工厂模式生产的对象都比较特殊(复杂，例如对象创建过程中有其他依赖、对象创建消耗的资源比较大等)，如果客户端对依赖对象的具体实现不关心或者不想关心，这时候使用工厂模式能够使得客户端对对象是怎么创建的(对象的具体实现)不可见(只依赖对象的创建，不依赖对象具体实现)，使得客户端把对象的创建和对象的实现解耦合(解除了客户端对对象创建不必要的依赖或者解除客户端对创建对象负责)。<br><br>
简单工厂模式、工厂方法模式和抽象工厂模式都是工厂模式，核心在于将客户端对依赖对象（复杂对象）的创建从该对象的具体实现中解耦出来。当客户端想创建的对象发生改变时，为了程序的扩展性(符合开闭原则)，出现了工厂方法模式；后来发现，对象的创建过程也会发生改变(创建的对象可能是多种对象的组合，如何将不同的对象组合在一起这个过程会发生改变)，就出现了抽象工厂模式。<br><br>
工厂模式的通过不同的方式供客户端调用来生产对象，至于对象是怎么生产出来的，客户端不关心或者不想关心，这是工厂模式的核心和本质，简单工厂到工厂方法到抽象工厂都首先是工厂，其次由于不同的场景，衍生出不同的模式。<br><br>
简单工厂模式最简单，通过客户端的参数不同，返回不同的对象。简单工厂模式的缺点在于：当工厂新增一个提供的对象时，需要需要工厂类，不符合设计原则中的开闭原则，这个缺点在工厂方法模式中能够克服。<br><br>
工厂方法模式将工厂抽象，抽象工厂的具体实现对应一个提供的对象，当工厂新增加对象时，只需要新实现抽象工厂即可，符合开不原则。工厂方法模式也有缺点，如果一个对象有多个组件组成，或者说要向创建一系列对象的组合，工厂方法就不适合了，这时候就出现了抽象工厂模式。<br><br>
抽象工厂模式旨在创建一系列相关的产品，比如说一家餐厅提供的饮食是由开胃菜、主食和甜点组成的，开胃菜、主食和甜点有多重多样，这家餐厅如果要提供一道饮食就适合采用抽象工厂模式，每一种饮食提供一种组合，当新的组合产生时，可以增加工厂的实现，提供新的组合。

<h4 id='3.4'>Builder模式(Builder Pattern, creational)</h4>

![Builder Design Pattern](/img/posts/builder-design-pattern.png "Builder模式")

Builder模式主要是为了为了解决**复杂对象**的创建，复杂对象主要表现为两个方面：

1. 对象构造的构造方法参数很多并且一些构造参数是可选的。如果让客户端通过传参去直接创建对象，很容易放生错误；并且如果通过构造方法重写的方式来实现构造参数可选构会很复杂。比如烤冷面基本有酸口的、甜口的和辣口，并且这些口味可以组合，如果用构造方法来重写所有的口味，则需要8个构造方法，使用Builder模式的话，可以在对象的创建过程中来选择口味。
2. 对象的构造需要一定的流程，并且构造参数位置的不同可能导致最后生产的对象不同。通过Builder模式，我们可以把用这个流程生产出来不同的对象。

*示例代码-针对构造参数多且可选的情况：*

    package com.fmz.pattern;

    /* 做一个烤冷面吧 */
    public class KaoLengMian{
        private String egg;//鸡蛋
        private String lengMian;//冷面

        /* 默认情况下是不加糖醋辣 */
        private boolean ifSugar;//是否加糖(甜口味)
        private boolean ifVinegar;//是否加醋(酸口味)
        private boolean ifPepper;//是否加辣(辣口味)

        private KaoLengMian(KaoLengMianBuilder builder){
            this.egg = builder.egg;
            this.lengMian = builder.lengMian;
            this.ifSugar = builder.ifSugar;
            this.ifVinegar = builder.ifVinegar;
            this.ifPepper= builder.ifPepper;
        }

        public static class KaoLengMianBuilder{
            private String egg;//鸡蛋
            private String lengMian;//冷面
            private boolean ifSugar;//是否加糖(甜口味)
            private boolean ifVinegar;//是否加醋(酸口味)
            private boolean ifPepper;//是否加辣(辣口味)

            public KaoLengMianBuilder(String egg, String lengMian){
                this.egg = egg;
                this.lengMian = lengMian;
            }

            public KaoLengMianBuilder setIfOrNotNeedSugar(boolean ifSugar) {
                this.ifSugar = ifSugar;
                return this;
            }

            public KaoLengMianBuilder setIfOrNotNeedVinegar(boolean ifVinegar) {
                this.ifVinegar= ifVinegar;
                return this;
            }

            public KaoLengMianBuilder setIfOrNotNeedPepper(boolean ifPepper) {
                this.ifPepper = ifPepper;
                return this;
            }

            public KaoLengMian build(){
                return new KaoLengMian(this);
            }
        }

        public String toString(){
            return "这是一个[" + (this.ifSugar ? "甜" : "不含糖") + "、" + (this.ifVinegar ? "酸" : "没加醋") + "、" + (this.ifPepper ? "辣" : "不辣") + "]的烤冷面";
        }

        public static void main(String args[]){
            KaoLengMian k1 = new KaoLengMian.KaoLengMianBuilder("柴鸡蛋", "正大冷面").setIfOrNotNeedSugar(true).setIfOrNotNeedPepper(true).build();//这是一个甜辣口的
            KaoLengMian k2 = new KaoLengMian.KaoLengMianBuilder("柴鸡蛋", "正大冷面").setIfOrNotNeedVinegar(true).setIfOrNotNeedPepper(true).build();//这是一个酸辣口的
            KaoLengMian k3 = new KaoLengMian.KaoLengMianBuilder("柴鸡蛋", "正大冷面").build();//这是一个酸辣口的

            System.out.println(k1);
            System.out.println(k2);
            System.out.println(k3);
        }
    }

> 这种实现方式通过一个内部类来实现：<br><br>
0). 对象的构造方法传入`Builder`对象，赋值`Builder`对象的属性值给要构造的对象<br><br>
1). 在需要构建的类中构建一个`Builder`类<br><br>
2). `Builder`类中持有和对象一样的属性<br><br>
3). 在`Builder`中写`Setter`方法，为可选属性设置值，方法的返回类型为Builder<br><br>
4). 在`Builder`中写一个`build()`方法，方法返回要构造的对象

*示例代码-针对用同一个流程构造不同的对象：*

> 场景是：KFC的套餐是由主食、饮料和甜点组成的，并且给客户上单时要按照先主食、接着饮料和最后甜点的顺序来进行(这样的目的非正常情况下客户随时来取单的时候，这个顺序对顾客来说是用餐人性化的，也就是说不能客户来拿订单准备吃的时候，只上了冰淇淋，这样对顾客的伤害是很大的，还不如什么都没准备好呢！)。

    package com.fmz.pattern;

    public class KFCOrder {
        
        private String main;//主食
        private String juice;//果汁
        private String dessert;//甜点

        public String getMain(){
            return this.main;
        }

        public void setMain(String main){
            this.main = main;
        }

        public String getJuice(){
            return this.juice;
        }

        public void setJuice(String juice){
            this.juice = juice;
        }

        public String getDessert(){
            return this.dessert;
        }

        public void setDessert(String dessert){
            this.dessert = dessert;
        }

        public String toString(){
            return "主食是：" + main + "；饮料是：" + juice + "；甜点是：" + dessert;
        }

    }

> `KFCOrder`KFC的订单。

    package com.fmz.pattern;

    public abstract class AbstractKFCOrderBuilder {
        protected KFCOrder kfcOrder;
        
        public abstract void buildMain();

        public abstract void buildJuice();

        public abstract void buildDessert();

        public KFCOrder getKFCOrder(){
            return kfcOrder;
        }

        public void createKFCOrder(){
            kfcOrder = new KFCOrder();
        }
    }

> `AbstractKFCOrderBuilder`抽象的KFC订单Builder。

    package com.fmz.pattern;

    public class BreakfastKFCOrderBuilder extends AbstractKFCOrderBuilder {

        @Override
        public void buildMain(){
            kfcOrder.setMain("油条");
        }

        @Override
        public void buildJuice(){
            kfcOrder.setJuice("豆浆");
        }

        @Override
        public void buildDessert(){
            kfcOrder.setDessert("冰淇淋");
        }
    }

> `BreakfastKFCOrderBuilder`早餐KFC订单，抽象KFC订单的实现类。

    package com.fmz.pattern;

    public class NoonKFCOrderBuilder extends AbstractKFCOrderBuilder {

        @Override
        public void buildMain(){
            kfcOrder.setMain("汉堡包");
        }

        @Override
        public void buildJuice(){
            kfcOrder.setJuice("百事可乐");
        }

        @Override
        public void buildDessert(){
            kfcOrder.setDessert("沙拉");
        }

    }

> `NoonKFCOrderBuilder`午餐KFC订单，抽象KFC订单的实现类。

    package com.fmz.pattern;

    public class KFCDirector {
        private AbstractKFCOrderBuilder kfcBuilder;

        public KFCDirector(AbstractKFCOrderBuilder kfcBuilder){
            this.kfcBuilder = kfcBuilder;
        }

        public void setKFCOrderBuilder(AbstractKFCOrderBuilder kfcBuilder){
            this.kfcBuilder = kfcBuilder;
        }

        public KFCOrder getKFCOrder(){
            return kfcBuilder.getKFCOrder();
        }

        public void constructKFCOrder(){
            kfcBuilder.createKFCOrder();
            kfcBuilder.buildMain();
            kfcBuilder.buildJuice();
            kfcBuilder.buildDessert();
        }

        public static void main(String args[]){
            KFCDirector director = new KFCDirector(new BreakfastKFCOrderBuilder());
            director.constructKFCOrder();

            KFCOrder kc = director.getKFCOrder();
            System.out.println(kc);
        }
    }

> `KFCDirector`KFC订单操作者，具体对象的生产流程在这里定义。

**抽象工厂模式与Builder模式的区别：**

抽象工厂模式强调的是生产一系列相关的产品，注重产品的生产结果；Builder模式强调的是按照某一个生产流程，生产出组件不同的产品，注重的产品的生产过程。上述例子中的`AbstractRestaurantOrderFactory`生产的餐厅的订单，订单是由开胃菜、主食和甜点组成的，不同国家的订单，只需要不同的组合即可；`AbstractKFCOrderBuilder`生产的餐厅订单，订单是由主食、果汁和甜点组成，并且订单的生产过程中，这些产品的生产顺序是由要求的，在`KFCDirector#constructKFCOrder()`中可以看到订单的生产顺序。

<h4 id='3.5'>适配器模式(Adapter Pattern，structural)</h4>

![Adapter Pattern UML](/img/posts/adapter.png "适配器模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/adapter)

> 适配器模式分为三种：<br><br>
1). 对象的适配器。这种适配器简单，就是引用一个被适配的对象，把这个对象转换成另一个对象(接口)。例如JDK中`InputStreamReader(InputStream is)`可以将一个`Inputstream`对象转换为一个`Reader`对象。<br><br>
2). 类的适配器。这种适配器模式一般要求适配器类继承被适配的类，同时实现新的接口。当客户端访问接口时，适配器类会将请求转发给被适配器类(结合自身的一些处理)，从而达到接口兼容的目的。<br><br>
3). 接口的适配器。当一个接口里面有很多方法时，我们要使用这个接口，就要实现接口中的所有方法，这往往是不必要的。我们可以在接口和实现类之间定义一个适配器类，这个适配器类覆写接口中的所有方法(可以是空方法)，这样当继承这个抽象类的时候，我们只需要覆写类本身需要的方法即可。

> 示例说明：<br><br>
假设有一个第三方类库类`NumberSorter`提供一个方法`sort(List list)`是对`List`进行排序<br><br>
客户端想要对原生的array进行排序<br><br>
这个时候就可以用到适配器模式，接口的实现中引用`NumberSorter`，把Array转化为`List`后，调用`NumberSorter`的`sort`方法进行排序，完成适配的目的。<br><br>
实际上是将客户端的请求转发给被适配的类

> 生活中的适配器例子：<br><br>
当我们用笔记本电脑电源插电时，发现只有双脚插座，这时候是不能用的。用适配器模式就是，我们寻找一个插排(插排上提供三角插头)，插排插入双脚插座，把电脑插入插排上的三角插座，这样电脑就完成了充电。这个插排就是所谓的适配器。

<h4 id='3.6'>装饰器模式(Decorator Pattern, structrual)</h4>

![Decorator Pattern UML](/img/posts/decorator.png "装饰器模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/decorator)

> 装饰器模式的存在是为了能够动态的改变对象的行为<br><br>
相比较于`继承`，装饰器能够在运行时(runtime)动态改变对象的行为，而不会影响到这个类原来的结构。
举一个例子进行说明：<br><br>
我们有一个`Logger`接口，有两个实现类`LoggerCloud`和`LoggerFileSystem`，分别可以向云端和日志文件中打印日志；现在来了一个新的需求，如果用户选择输出日志方式中加了时间(日志输出方式可以配置，有一个时间的单选框，选中表明日志输出时间)，需要在每条日志后边加上时间<br><br>
这是我们当然可以考虑两种方法：1，写两个新的实现类分别继承`LoggerCloud`和`LoggerFileSystem`，在打印方法后，打印出日期。这种方法增加了类的继承层次，如果用户日志输出方式可配置项很多，将无法维护类的层次结构；2，改写`LoggerCloud`和`LoggerFileSystem`，这种方法不符合设计模式中的开闭原则，影响的原始的对象，并这种变化是永久的，不是动态的<br><br>
适配器模式就是用来解决这个问题，添加一个抽象类`TimeLoggerDecorator`，这个类实现`Logger`接口，同时持有一个`Logger`对象，它的实现类调用`Logger`的`print()`方法，同时可以动态的添加一些行为。类图如下：

![decorator-demo](/img/posts/decorator-demo.png)

> 在`java.io`包中有经典的装饰器模式的实现。`InputStream`有一些基本的实现`ByteArrayInputStream`、`PipeInputStream`、`ObjectInputStream`、`FileInputStream`，当我们想为IO动态的增加一些功能的时候，比如说增加一个`Buffer`功能，这个`Buffer`功能在这些`InputStream`上都合适，也就是说当我们读取文件`FileInputStream`或者在读取对象`ObjectInpuStream`时都可以增加`Buffer`功能，这时候如果单纯继承这些类的话就使得类的层次关系变得臃肿，而且每增加一个新的实现，如果想要增加`Buffer`功能，都需要新实现一个继承类来完成`Buffer`功能。这时候就装饰器模式就能够解决问题：`FilterInputStream`是一个装饰类，它同时持有了`InputStream`基类的引用，在`Buffer`功能的实现过程中，需要调用`InputStream`的`read()`，这时候传入不同的实现类，由于多态就能表现出不同的功能。<br><br>
看看IO包的UML图吧：

![java io decorator pattern](/img/posts/inputstream-decorator-diagram.png "Java IO 装饰器模式UML图")

> 再举一个例子讲述一下为什么要**动态**增加对象的功能:<br><br>
考虑有这样一家Pizza店，提供基本的Pizza和Pizza上面的水果馅(Topping)，如果有四种基本的Pizza和8中不同的Topping，如果程序要维持全部的组合，也有32中之多。

*代码：*

    package com.fmz.pattern;

    public abstract class Pizza {
        private double price;

        public abstract double getPrice();

    }

> `Pizza`披萨。

    package com.fmz.pattern;

    public class AmericanPizza extends Pizza {

        public String name;

        public AmericanPizza(String name){
            this.name = name;
        }
        
        public double getPrice(){
            return 33.33;
        }

        public String toString(){
            return name;
        }
    } 

> `AmericanPizza`美国披萨。基本Pizza的一种。

    package com.fmz.pattern;

    public class ChinesePizza extends Pizza {
        private String name;

        public  ChinesePizza(String name){
            this.name = name;
        }
        
        public double getPrice(){
            return 13.33;
        }

        public String toString(){
            return name;
        }

    } 

> `ChinesePizza`中国披萨。基本Pizza的一种。

    package com.fmz.pattern;

    public abstract class Topping {
        private String toppingContent;

        public Topping(String content){
            toppingContent = content;
        }
        
        public abstract double getPrice();

        public String toString(){
            return "水果馅的内容是：" + toppingContent;
        }
    }

> `Topping`披萨上的水果馅。

    package com.fmz.pattern;

    public class AppleTopping extends Topping {

        public AppleTopping(String content){
            super(content);
        }

        public double getPrice(){
            return 0.3;
        }
    }

> `AppleTopping`苹果馅。

    package com.fmz.pattern;

    public class StrawberryTopping extends Topping {

        public StrawberryTopping(String content){
            super(content);
        }

        public double getPrice(){
            return 0.9;
        }
    }

> `StrawberryTopping`草莓馅。

    package com.fmz.pattern;

    public class PizzaDecorator extends Pizza {

        private Pizza pizza;

        private Topping topping;

        public PizzaDecorator(Pizza pizza, Topping topping){
            this.pizza = pizza;
            this.topping = topping;
        }

        public double getPrice(){
            return pizza.getPrice() + topping.getPrice();
        }

        public String toString(){
            return pizza.toString() + ";" + topping.toString();
        }
    }

> `PizzaDecorator`披萨的装饰类(Decorator)。

    package com.fmz.pattern;

    public class ApplePizzaDecorator extends PizzaDecorator {

        public ApplePizzaDecorator(Pizza pizza){
            super(pizza, new AppleTopping("苹果馅"));
        }
    }

> `ApplePizzaDecorator`苹果馅披萨的装饰类。

    package com.fmz.pattern;

    public class StrawberryPizzaDecorator extends PizzaDecorator {

        public StrawberryPizzaDecorator(Pizza pizza){
            super(pizza, new StrawberryTopping("草莓馅"));
        }
    }

> `StrawberryPizzaDecorator`草莓馅披萨的装饰类。

    package com.fmz.pattern;

    public class PizzaTest {

        public static void main(String args[]){
            Pizza strawberryToppingAmericanPizza = new StrawberryPizzaDecorator(new AmericanPizza("美国Pizza"));
            Pizza strawberryToppingChinesePizza = new StrawberryPizzaDecorator(new ChinesePizza("中国Pizza"));
            Pizza appleToppingChinesePizza = new ApplePizzaDecorator(new ChinesePizza("中国Pizza"));
            Pizza appleToppingAmericanPizza = new ApplePizzaDecorator(new AmericanPizza("美国Pizza"));

            System.out.println(strawberryToppingAmericanPizza + "的价格为：" + strawberryToppingAmericanPizza.getPrice());
            System.out.println(strawberryToppingChinesePizza + "的价格为：" + strawberryToppingChinesePizza.getPrice());
            System.out.println(appleToppingChinesePizza + "的价格为：" + appleToppingChinesePizza.getPrice());
            System.out.println(appleToppingAmericanPizza + "的价格为：" + appleToppingAmericanPizza.getPrice());
        }
    }

> `PizzaTest`披萨测试类。通过传入不同的基本披萨，装饰类能够获取不同的装饰组合。

<h4 id='3.7'>代理模式(Proxy Pattern, structural)</h4>

![Proxy Pattern UML](/img/posts/proxy.png "代理模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/proxy)

<h4 id='3.8'>合成模式(Composite Pattern, structural)</h4>

![Composite Pattern UML](/img/posts/composite.png "合成模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/composite)

<h4 id='3.9'>桥梁模式(Bridge Pattern, structural)</h4>

![Bridge Pattern UML](/img/posts/bridge.png "桥梁模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/bridge)

<h4 id='3.10'>模板模式(Template Pattern, structural)</h4>

![Template Pattern UML](/img/posts/template.png "模板模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/template)

<h4 id='3.11'>策略模式(Strategy Pattern, behavioral)</h4>

![Strategy pattern UML](/img/posts/strategy.png "策略模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/strategy)

<h4 id='3.12'>迭代子模式(Iterator Pattern, behavioral)</h4>

![Iterator Pattern UML](/img/posts/iterator.png "迭代子模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/iterator)

<h4 id='3.13'>观察者模式(Observer Pattern, behavioral)</h4>

![Observer Pattern UML](/img/posts/observer.png "观察者模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/observer)

<h4 id='3.14'>责任链模式(Chain of Responsibility Pattern, behavioral)</h4>

![Chain of Responsibility Pattern UML](/img/posts/chain_of_responsibility.png "责任链模式")

[示例代码](https://github.com/FengMengZhao/language_learn/tree/master/thinking_in_java/design_pattern/chain_of_responsibility)

---

<h3 id='4'>设计模式实践</h3>

<h4 id='4.1'>exercise-1</h4>

*问题描述：*

> 设计一个万能遥控器（手机端），要求实现如下功能：<br><br>
1. 控器通过网络把控制指令发送到红外设备上实现控制
2. 可以控制不同品牌的电视、空调等电器
3. 不同品牌同类电器的按钮相同，控制指令不同
4. 不同种类的电器控制按钮不同，控制指令也不同
5. 遥控器内置空调、电视等电器的常见品牌的控制程序，也支持自定义新的电器

*自己的设计：*

![上述问题自己的设计](design-practice-exercise-1-01.png)

> `RemoteController`遥控器抽象类<br><br>
`TVRemoteController`、`RefrigeratorRemoteController`电视和冰箱的遥控器实现<br><br>
`Menu`菜单抽象类<br><br>
`TVMenu`、`RefrigeratorMenu`电视和冰箱的菜单实现类<br><br>
`Button`按钮抽象类<br><br>
`CenterController`万能遥控（硬件）

*架构师的设计：*

期待...

---
