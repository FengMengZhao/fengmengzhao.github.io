---
layout: post
title: java与模式
---

### 模式的简史和形而上学

- 模式化的过程就是把问题抽象化，在忽略不重要的细节后，发现问题的一般性本质，并找出普遍适用的解决方案的过程
- 设计模式可以分为: 架构模式;设计模式;实现模式.构架模式是针对模块和子系统;设计模式是针对包;构架模式是针对类和对象.

---

### 统一建模语言

#### 类图(Class Diagram)

- 类名
    - 类名如果用正体的字，表示具体类(Concrete);如果用斜体的字，表示抽象的类(abstract);类名是不能省略的，必须显示
    - 接口的类图名称有 "Interface" 字样
- 属性清单
    - 一个属性可以是 public(+ 表示) private(- 表示) protected(# 表示) ;
- 方法清单
    - 方法也有三种形式，分别用(+ - #)表示；a pulic static method()方法下面加一个下划线，表示静态方法
    - 一个类的构造方法不一定是 public 类型的，也可以是 private 或者 protected 类型的
- 性质清单
    - 性质是由一个属性即一个内部变量，一个赋值函数(mutator)和一个取值函数(accessor)组成的结构，也就是所谓的setter和getter方法

#### 类和类之间的关系

- 一般化关系(Generalization)
    - 表示类与类之间的继承关系，接口与接口之间的继承关系，类和接口之间的实现关系，一般化关系是从子类指向父类或者从实现接口的类指向被实现接口的类，与继承的关系方向相反
    - 一般化关系在 JAVA 中可以直接翻译为关键字 extends 和 implements
- 关联关系(Association)
    - 是类与类之间的联接，它使一个类知道另一个类是属性和方法
- 聚合关系(Aggregation)
    - 聚合关系是关联关系的一种，是强的关联关系。聚合是整体和个体之间的关系
- 合成关系(Composition)
    - 合成关系是关联关系的一种，是比聚合关系强的关联关系
- 依赖关系(Dependency)
    - 是类与类之间的连接，依赖总是单向的，表示一个类依赖另外一个类的定义

> 一般而言，JAVA 语言的依赖关系体现在局部变量、方法参数以及静态方法的调用

---

### 软件的可维护性(Maintainability)和可复用性(Reuseablity)

#### 系统设计的目标

- 可扩展性
- 灵活性
- 可插入性

#### 传统的复用

- 代码的剪切复制
- 算法的复用
- 数据结构的复用

#### 面向对象设计的复用

- 在JAVA这样的面向对象的语言中，数据的抽象化、封装、继承、多态是几项重要的语言特征
- 数据的抽象化和继承关系使得概念和定义可以复用；多态性使得实现和应用可以复用

> - 善为士者不武
- 天下有道，却走马以粪，天下无道，戎马生于郊.

---

### 开-闭原则(OCP)(Open-Closed Principle)

**开-闭原则讲的是：一个软件实体应该对扩展开放，对修改关闭(Software entries should be open for extension, but closed for modification)**

**抽象化是关键**

在java语言设计中，应该给系统定义出一个一劳永逸、不再更改的抽象设计，此设计有无穷无尽的行为在实现层被实现.

> - 可以给出一个或者多个抽象JAVA类或者接口，规定出所有的具体类必须提供的方法的特征(Signature)作为系统设计的抽象层，抽象层预见了所有可能的扩展，因此在任何扩展情况下都不需要修改，从而满足了开-闭原则的第二条：对修改关闭
- 由于从抽象层导出一个或者多个新的具体类可以改变系统的行为，因此系统的设计对扩展是开放的，这就满足了开-闭原则的第二条：对扩展开放

**对可变性的封装原则**

> - 开-闭原则如果从另外一个角度考虑，就是所谓的“对可变性的封装原则(Principle of Encapsulation of Variation)”
- 继承应当被看做是封装变化的方法，而不应当被认为是从一般性的对象生成特殊对象的方法

---

### JAVA 语言的接口

- 一个java接口是一些方法特征(包括方法的名字、参量的名字、参量的类型、参量的数目、方法的返回类型、所抛出的异常等)的结合，一个接口只有方法的特征而没有方法的实现，java的接口中还可以定义public常量.
- 在 JAVA 的语言规范中，一个方法的特征仅包括方法的名字、参量的数目和种类，而不包括方法的返回类型、参量的名字以及所抛出的异常。在java编译器检查方法的重载(Overload)时，会根据这些条件判断两个方法是否是重载的方法。但是在java编译器在检查方法的覆写(Override)时，则会进一步检查两个方法(分处超类和子类)的返回类型和抛出的异常是否相同.
- 在java语言中，继承的关系可以分为两种，一种是类对接口的实现，称之为接口继承；另一种是类对类的继承，称之为实现继承.

#### JAVA 接口中常见的用法

1. 单方法接口，表示一个接口中只含有一个方法。比如java.lang.Runnable接口
2. 标识接口，表示没有任何方法和属性的接口。比如java.io.Serializable接口
3. 常量接口，表示接口中仅仅含有一些常量

---

### 抽象类

- JAVA的接口和抽象类一样都是用来声明一个新的类型，并且作为一个类型的等级结构的起点，但是java的接口具有比抽象类更好的特征，因此应该应该优先使用java接口声明一个超类

- 抽象类仅提供了一个类型的部分实现。抽象类可以有实例变量以及一个或者多个构造法方法。抽象类可以同时具有抽象方法和具体方法

#### 抽象类的用途

1. 抽象类是用来继承的，具体类不是用类继承的。在一个以继承关系形成的等级结构中，树叶节点均应当是具体类，而树枝节点均应是抽象类(或者java接口)
2. 抽象类应该拥有尽可能多的共同代码，也就是说在一个继承等级结构中，共同的代码应当尽量向等级结构的上方移动
3. 抽象类应该拥有尽可能少的数据，也就是说与代码移动方向相反的是数据的移动应该从继承的等级结构的高端向等级结构的低端移动
4. 应当针对抽象编程，而不是针对具体编程

---

### 里氏替换原则(LSP)

**定义:** 如果对每一个类型为T1的对象o1,都有类型为T2的对象o2,使得以T1定义的所有程序P在所有的对象o1都替换成o2时，程序P的行为没有发生改变，那么类型T2是类型T1的子类型。换言之，一个软件实体如果使用的是一个基类的话，那么也一定适用其子类，而且它根本不能察觉出基类对象和子类对象的区别

> 里氏替换原则是继承复用的基石。只有当衍生类可以替换掉基类，软件的单位功不能受到影响时，基类才能真正被复用，而衍生类才能够在基类基础上增加新的行为

一般而言，如果有两个具体类A和B有继承关系，那么一个最简单的符合里氏替换原则的方案应该是：建立一个抽象类C，然后让类A和类B成为抽象类C的子类

> 里氏替换原则的理解:<br>
- “白马，马也；乘白马，乘马也。骊马，马也；乘骊马，乘马也”
- “妹，美人也，爱妹，非爱美人也”

---

### 简单工厂(Simple Factory)

简单工厂模式的例子

#### Furit Interface

	package simpleFactory;

	public interface Fruit {
		void grow() ;
		void harvest() ;
		void plant() ;
	}

#### Apple Class implements Fruit Interface
	
	package simpleFactory;

	public class Apple implements Fruit {
		private int treeAge ;

		@Override
		public void grow() {
			log("Apple is growing...") ;
		}

		@Override
		public void harvest() {
			log("Apple has been harvested") ;
		}

		@Override
		public void plant() {
			log("Apple has been planted") ;
		}
		
		public static void log(String str){
			System.out.println(str) ;
		}
		
		public int getTreeAge(){
			return treeAge ;
		}
		
		public void setTreeAge(int treeAge){
			this.treeAge = treeAge ;
		}
	}

#### Grape Class implements Fruit Interface

	package simpleFactory;

	public class Grape implements Fruit{
		private boolean seedless ;

		@Override
		public void grow() {
			log("Grape is growing...") ;
		}

		@Override
		public void harvest() {
			log("Grape has been harvested") ;
		}

		@Override
		public void plant() {
			log("Grape has been planted") ;
		}
		
		public void log(String str){
			System.out.println(str) ;
		}
		
		public boolean getSeedless(){
			return seedless ;
		}
		
		public void setSeedless(boolean seedless){
			this.seedless = seedless ;
		}
	}

#### Strawberry Class implements Fruit Interface

	package simpleFactory;

	public class Strawberry implements Fruit {

		@Override
		public void grow() {
			log("Strawberry is growing...") ;
		}

		@Override
		public void harvest() {
			log("Strawberry has been harvested") ;
		}

		@Override
		public void plant() {
			log("Strawberry has been planted") ;
		}
		
		public void log(String str){
			System.out.println(str) ;
		}
	}

#### Static FruitGardener Class 实例化类

	package simpleFactory;

	public class FruitGardener {
		public static Fruit factory(String which)throws BadFruitException{
			if(which.equalsIgnoreCase("apple")){
				return new Apple() ;
			}else if(which.equalsIgnoreCase("grape")){
				return new Grape() ;
			}else if(which.equalsIgnoreCase("strawberry")){
				return new Strawberry() ;
			}else{
				throw new BadFruitException("Bad fruit request") ;
			}
		}
	}

#### 异常处理

	package simpleFactory;

	public class BadFruitException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public BadFruitException(String str){
			super(str) ;
		}
	}

#### 测试类(Main)

	package simpleFactory;

	public class Main {
		public static void main(String args[]){
			try{
				Fruit apple = FruitGardener.factory("Apple") ;
				Fruit strawberry = FruitGardener.factory("strawberry") ;
				Fruit grape = FruitGardener.factory("grape") ;
				
				//Fruit f = FruitGardener.factory("a") ;
				
				apple.harvest() ; 
				apple.plant() ;
				apple.grow() ;
				
				strawberry.grow() ;
				
				grape.grow() ;
			}catch(BadFruitException e){
				System.out.println(e) ;
			}
		}
	}

### 简单工厂模式的结构

简单的工厂模式就是一个工厂类根据传入参量决定创建出哪一种产品类的实例。

**工厂类(Creator)角色**

担任这个角色的是工厂方法模式的核心。工厂类在客户端的直接调用下创建产品对象，它往往由一个具体java类实现

**抽象产品(Product)角色**

担任这个角色的类是由工厂方法模式所创建的对象的父类，或它们共同拥有的接口。抽象产品的角色可以用一个java接口或者java抽象类实现

**具体产品(Concrete Product)**

工厂方法模式所创建的任何对象都是这个角色的实例，具体产品由一个java类实现

#### 源代码

##### Creator Class

	public class Creator{
		public static Product factory(){
			return new ConcreteProduct() ;
		}
	}

##### Abstract Product Class

	public interface Product{
		
	}

##### Concrete Product Class

	public class ConcreteProtuct(){
		public ConcreteProduct(){
		
		}
	}

#### 工厂角色和抽象产品角色的合并

最经典的应用是 java.text.DateFormat 类

#### 三种角色全部合并

> 如果抽象产品角色应经被忽略，那么具体产品角色可以有自己的工厂

	public class ConcreteProduct{
		public ConcreteProduct(){
		
		}

		public static ConcretePorduct factory(){
			return new ConcteteProduct() ;
		}
	}

#### java.text.dateFormat 类对对象多态性和简单工厂模式的绝佳应用

> 这里使用静态工厂的方法是为了将具体子类实例化的工作隐藏起来，从而客户端不必考虑如何将具体子类实例化，因为抽象类DateFormat会提供合适的具体子类的实例

`DateFormat`是抽象类，`SimpleDateFormat`是`DateFormat`的子类，静态方法`getDateInstance()`完全可以返回`SimpleDateFormat`的实例，并且将之声明为`DateFormat`类型，这是最纯正的对象的多态性的应用

> 这也是java针对抽象编程的具体体现

---
