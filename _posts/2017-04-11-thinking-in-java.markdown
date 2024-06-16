---
layout: post
title: Java编程思想
---

## 目录

- [1 对象导论](#1)
- [2 一切皆是对象](#2)
- [3 操作符](#3)
- [4 控制执行流程](#4)
- [5 初始化与清理](#5)
- [6 访问权限控制](#6)
- [7 复用类](#7)
- [8 多态](#8)
- [9 接口](#9)
- [10 内部类](#10)
- [11 持有对象](#11)
- [12 通过异常处理错误](#12)
- [13 字符串](#13)
- [14 类型信息](#14)
- [15 泛型](#15)
- [16 数组](#16)
- [17 容器深入研究](#17)
- [18 Java I/O系统](#18)
- [19 枚举类型](#19)
- [20 注解](#20)
- [21 并发](#20)
- [22 图形化用户界面](#22)

---

<h2 id="1">1 对象导论</h2>

---

<h2 id="2">2 一切皆是对象</h2>

<h3 id="2.1">2.1 用引用操控对象</h3>

用引用操控对象可以想象成一个遥控器(引用)来操控电视机(对象)。即使没有对象，引用也可以单独存在，如果向一个没有对象与之关联的引用发送消息，会发生错误，因为找不到发送消息的对象。一种安全的机制是在创建引用的同时就对其进行初始化。

<h3 id="2.2">2.2 必须由你创建所有对象</h3>

#### 对象储存在设么地方

- 寄存器。这是最快的存储区，位于处理器内部。储存器的数量极其有限，按需进行分配，你不能直接控制。也不能在程序中感受到任何寄存器存在的痕迹。
- 堆栈。位于通用的RAM(随机访问储存器)中，通过堆栈的指针可以从处理器哪里获得直接的支持。堆栈指针向下移动则分配新的内存，堆栈指针向上移动则释放哪些内存。Java系统必须知道储存在堆栈总的所有项的确切声明周期，以便上下移动指针。
- 堆。一种通用的内存池(也位于RAM区中)，用于存放所有的java对象。
- 常量存储。常量值通常直接存放在代码中，这样做非常安全，因为它永远不会被改变。
- 非RAM存储。如果数据完全存活于程序之外，那么它可以不受程序的任何控制，在程序没有运行时也可以存在。两个基本的例子就是：流对象和持久化对象。

Java语言中的基本数据类型储存在堆栈中，因为它们是小的、简单的变量，没有必要储存在堆中，储存在堆栈中会更加有效。基本数据类型采用直接赋值的方法，可以简单的将之想象为一个容器，每一个容器代表一个基本数据类型的值，将这个值赋值给变量a或b，则a或b就进入这个容器中。一旦a或者b的值发生改变，堆栈中会产生一个新的容器，被改变的变量就进入新的容器中，而没被改变的还在原来的容器中，且原来的容器依然存在。这样做的基础是：堆栈中的数据允许共享(每一个容器可以放进去很多变量)。

> 数组属于引用型数据类型，每当创建一个数组，其实是创建了数据的一个引用，数据的默认初始化为`null`，当利用这个数组的引用时，必须将之指定为一个特定的对象，否则会发生异常。

<h3 id="2.3">2.3 永远不要销毁对象</h3>

#### 作用域(scope)

作用域由花括号的位置决定，作用域中定义的变量只可用于作用域结束之前，这里的变量指的是基本数据类型

#### 对象的作用域

java对象不具备和基本数据类型一样的声明周期，java的对象可以存活于作用域之外。实际上只要你需要java对象是一直保留在堆内存中的。

> 自己理解：堆栈中的基本数据类型和对象的引用由于堆栈的储存空间的机制只能用于作用域当中，一旦离开作用域，堆栈中的数据内存将会被释放。而堆内存空间中的数据是由垃圾回收器处理的，并没有通过处理器直接控制，而堆栈中的数据是处理器通过堆栈指针的上下移动获得获取和释放的。

<h3 id="2.4">2.4 创建一个新的数据类型：类</h3>

一旦创建了类，可以在类中设置两种类型的元素：字段(数据成员)和方法(成员函数)。类中的字段如果是基本的数据类型，在字段别创建的时候会给其一个默认的初始化值。

> 对于基本数据类型的成员变量(非类的字段)并不会获得程序默认的初始化值，必须在进行初始化之后才能使用。

<h3 id="2.5">2.5 方法、参数和返回值</h3>

#### 方法签名

方法签名是唯一的表示出某个方法的定义，方法签名的内容是：方法名称和参数列表。

对象调用方法，我们通常称之为向对象发送消息。

#### 参数列表

参数列表必须标明所要传递给参数对象的类型和名字。

> 除了基本数据类型以外，传递给方法的对象参数事实上是传递的是一个引用。

<h3 id="2.6">2.6 构建一个java程序</h3>

#### static关键字

当声明个事物是static时，意味着这个域或者方法不会不会与包含它的那个类的任何对象实例关联在一起

#### 什么时候使用static

- 只想为某一个特定区域分配单一储存空间，而不去考虑要创建多少对象，甚至根本就不创建任何对象。
- 希望某个方法不与包含它的类的任何对象实例关联在一起。

#### 使用static代表什么

- 当static作用于某个字段时，肯定会改变数据的创建方式。static字段对于每个类来说只有一份储存空间，而非static字段来说每个对象都有一个储存空间。
- 当static作用于方法时，和普通方法的差别不是很大，唯一的区别是：在不创建对象的情况下可以通过类来调用方法。这一点对于main()方法尤为重要，这个方法是运行一个应用的入口点。

---

<h2 id="3">3 操作符</h2>

---

<h2 id="4">4 控制执行流程</h2>

---

<h2 id="5">5 初始化与清理</h2>

<h3 id="5.1">5.1 用构造器确保初始化</h3>

java通过提供构造器，确保创建的每一个对象都会得到初始化。在java中初始化和创建捆绑在一起，二者不能分离。

<h3 id="5.2">5.2 方法的重载</h3>

#### 区分重载的方法

方法明后名称相同，而具有不同的参数列表，称为方法的重载

> 返回类型的不同并不能作为方法重载的标识，也就是说方法名称和参数列表相同而返回类型不同，编译器会认为是同一个方法，写在同一个类中会报错;如果方法名称相同,其他方法签名不同,则视为方法的重载(overwrite).

<h3 id="5.3">5.3 默认构造器</h3>

如果你写的类中没有构造器，编译器会为你默认创建一个无参的构造器，如果你编写了一个构造器，则编译器就不会帮你自动创建默认构造器了。所以这个时候再创建无参的对象时就会报错，因为无法找到一个向匹配的构造器。

	package org.fmz.constuctor ;

	public class ConstructorDemo01{
		int id = 0 ;
		public ConstructorDemo01(int i){
			this.id = i ;
		}
		public static void main(String args[]){
			ConstructorDemo01 cd = new ConstructorDemo01() ;
		}
	}

> 上述代码将会报错，没有相应的无参构造方法，不能生成相应的无参的对象。

<h3 id="5.4">5.4 this关键字</h3>

this关键字只能在方法内部使用，表示对调用方法的那个对象的引用。

> 注意：如果在方法内部调用同一个类中的另外一个方法，则没有必要使用关键字this。

如下代码表示返回当前类型对象的一个引用

	package org.fmz.init ;

	public class ThisDemoLeaf{
		int i = 0 ;
		ThisDemoLeaf increment(){
			i++ ;
			return this ;//表示返回当前ThisDmeoLeaf的一个对象的引用。
		}
	}

this关键字用于将当前的对象传递给其他方法也非常有用，代码示例：

	package org.fmz.init ;

	class Apple{
		Apple getPeeled(){
			return Peeler.peel(this) ; //this作为参数述传递给代表对调用方法的那个对象的应用
		}
	}

	class Person{
		public void eat(Apple apple){
			Apple peeled = apple.getPeeled() ;
			System.out.println("OK!") ;
		}
	}

	class Peeler{
		static Apple peel(Apple apple){
			return apple ;
		}
	}

	public class ThisDemoApple{
		public static void main(String args[]){
			new Person().eat(new Apple()) ;
		}
	}

#### 在构造器中调用构造器

在构造器中，如果为this添加了参数列表，表示：此参数列表的某个构造器的明确调用。

#### `this.`表示本类中的数据成员(字段)

#### static的含义

static方法就是没有this的方法。

在static方法的内部不能调用非静态的方法。

<h3 id="5.5">5.5 垃圾处理和垃圾回收</h3>

#### finalize()方法

#### 终结条件

当对某个对象不感兴趣的时候，也就是它们可以别清理了。为了使这些对象占用的内存得到安全的释放，需要一总结条件的验证，如果验证有消息抛出，则会发现人们关心的问题。下面是一个简单的例子：

	package org.fmz.init ;

	public class Book{
		boolean checkOut = false ;
		public Book(boolean checkOut){
			this.checkOut = checkOut ;
		}
		void checkIn(){
			checkOut = false ;
		}
		protected void finalize(){
			if(checkOut){
				System.out.print("Error: checkOut out") ;
			}
		}

		public static void main(String args[]){
			Book book = new Book(false) ;
			book.checkIn() ;
			new Book(true) ;
			System.gc() ;
		}
	}

> 本程序表示当还有书没有chekIn的情况下就清理了对象是不恰当的，通过这样的机制能够找出潜在的风险。

<h3 id="5.6">5.6 成员初始化</h3>

基本数据类型的局部变量(非字段或数据成员)在没有初始化的情况下使用会得到一个编译的错误信息。这样可以帮助找出程序里的缺陷(如果编译器默认一个值，这个值又一般不是想要的，那么就会掩盖这种失误)，但是对于字段或者说数据成员，类会为每个基本数据类型的成员一个初始值。

> 在类中定义了一个对象的引用时，如果没有进行初始化，那么就会获得一个特殊的值null。

#### 指定初始化

指定初始化就是给每一个基本数据类型的变量在定义时赋值，给定义的对象引用实例化。

<h3 id="5.7">5.7 构造器初始化</h3>

> 无法阻止自动初始化的进行，它将在构造器被调用之前发生。

#### 初始化的顺序

在类的内部，变量定义的先后顺序决定了初始化的顺序。即使变量的定义散布于方法定义之间，它仍旧会在任何方法(包括构造器)被调用之前得到初始化。

#### 静态数据的初始化

无论创建多少对象，静态数据只占有一份储存区域。static关键字不能作用域局部变量，因此它只能作用于域。

初始化的顺序是：先静态对象，而后是非静态对象。

#### 总结名为Dog的类的对象创建过程

1. 解释器首先根据构造器(构造器其实是一个隐式的静态方法(static))查找类路径，定位Dog.class。
2. 载入Dog.class，有关静态初始化的所有动作都会得到执行，因此静态初始化只会在class对象首次加载的时候进行一次。
3. 当用new Dog()创建对象的时候，首先为Dog对象在堆上分配足够的储存空间。
4. 将自动的为Dog对象所有基本数据类型设置其相应的默认值(0或者false)，而引用则被设置为null。
5. 执行所有出现在字段定义处的初始化动作。
6. 执行构造器。

#### 显式静态初始化

static块：`static{//...}`

#### 非静态实例初始化

又叫实例初始化，语法：`{//...}`

<h3 id="5.8">5.8 数组初始化</h3>

#### 可变参数列表

示例：

	package org.fmz.init ;

	class A{
	}

	public class NewVarArgs{
		static void printArray(Object... args){
			for(Object obj: args){
				System.out.print(obj + " ") ;
			}
			System.out.println() ;
		}
		public static void main(String args[]){
			printArray(new Integer(47),new Float(11.3F),new Double(44.3)) ;
			printArray(47,11.3F,44.3) ;
			printArray("one","two","three") ;
			printArray(new A(),new A(),new A()) ;
			printArray((Object[])new Integer[]{1,2,3,4}) ;//转型能避免编译器的警告
			printArray() ;
		}
	}

> 有了可变参数列表就不用显式的编写数组了，当你指定参数时，编译器会为你填充数组。

#### 枚举类型

枚举类

	package org.fmz.init ;

	public enum Spiciness{
		NOT,MILD,MEDIUM,HOT,FLAMING
	}

简单的枚举运用

	package org.fmz.init ;

	public class SimpleEnumUse{
		public static void main(String args[]){
			Spiciness howHot = Spiciness.MEDIUM ;
			System.out.println(howHot) ;//自动复写的toString方法
			for(Spiciness spi: Spiciness.values()){//方法values()
				System.out.println(spi) ;
			}
		}
	}

特别适用的枚举特性：

	package org.fmz.init ;

	public class Buttito{
		Spiciness degree ;
		public Buttito(Spiciness degree){
			this.degree = degree ;
		}
		public void describe(){
			System.out.print("This Buttito is ") ;
			switch(degree){
				case NOT:
					System.out.println("not spicy at all!") ;
					break ;
				case MILD:
				case MEDIUM:
					System.out.println("a little hot!") ;
					break ;

				case HOT:
				case FLAMING:
				default:
					System.out.println("maybe too hot!") ;
			}
		}
		public static void main(String args[]){
			Buttito
				plain = new Buttito(Spiciness.NOT) ,
				greenChile = new Buttito(Spiciness.MEDIUM) ,
				jalapeno = new Buttito(Spiciness.HOT) ;
			plain.describe() ;
			greenChile.describe() ;
			jalapeno.describe() ;
		}
	}


---

<h2 id="6">6 访问权限的控制</h2>

访问权限控制的等级，从最大权限到最小权限依次为：public、protected、包访问权限(没有关键词)、private

<h3 id="6.1">6.1 包：库单元</h3>

包内包含一组类，它们在单一的名字空间之下被组织在一起。

<h3 id="6.2">6.2 java访问权限修饰词</h3>

#### 包访问权限

包访问权限是默认的访问权限，没有关键字修饰。意味着：当前包总的所有其他类对那个包访问权限的类的成员有访问权限，对于包外的其他类，这个包访问权限的成员都是private的。

#### 接口访问权限：public

public关键字修饰的成员对于每一个人来说都是可用的，尤其是使用类库的客户端程序员根式如此。

#### private：你无法访问

实例代码：

	package org.fmz.access ;

	class Sundae{
		private Sundae(){}
		static Sundae makeASundaeInstance(){
			System.out.println("OK!!!") ;
			return new Sundae() ;
		}
	}

	public class PrivateAccess{
		public static void main(String args[]){
			//Sundae sun = new Sundae() ;
			Sundae.makeASundaeInstance() ;
		}
	}

> 如果你想控制如何产生对象，并且阻止别人访问某个特定的构造器，那么就将构造子定义为私有的，必须通过一个方法才能才生对象。

> 如果可以肯定一个方法(包括构造方法)只是该类的一个助手方法，都可以将之指定为private权限。这样可以保证该方法不会在包内的其他地方别误用到，于是也就防止了你去删除或者修改这个方法。

> 对于private域(数据成员)也同样适用，除非必须公布底层的实现细目，否则应该将所有的域指定为private。

#### protected：继承访问权限

1. 继承访问权限的类的成员可以被子类(继承此类的类)访问，而且不管这个子类和修饰类是否在同一个包中都是如此。
2. 继承的访问权限页也提供了包的访问权限，也就是说如果一个类是protected修饰，那么在一个包中的所有其他类都可以访问修饰类的成员。

<h3 id="6.3">6.3 接口和实现</h3>

访问权限的控制通常会被称为：具体实现的隐藏。把数据和方法包装进类中，以及具体实现是隐藏，常常共同被称为封装(人们也经常讲具体实现的隐藏称之为封装)。

#### 为什么要将访问权限控制权的边界划在数据类型的内部

1. 要设定客户端程序员可以使用和不可以使用的边界。这样可以在结构中建立自己的内部机制，而不必担心客户端程序员会偶然的将内部机制当做他们可以使用的接口的一部分。
2. 接口和具体实现的分离。如果结构是用于一组程序中，而客户端程序员除了向接口发送消息之外什么也不可以做的话，那么就可以随意的更改所有不是public的东西了(包访问权限、protected和private修饰的成员)，而不会破坏客户端的代码。

> 如果将接口和具体实现放在一个类中，应该按照等级的严格增强顺序来放置成员，这样可以增加程序的可读性。

> 对于客户端程序员而言，javadoc所提供的注释文档功能降低了程序代码的可读性重要性，因为客户端程序员只关心接口，而不关心程序的具体实现。javadoc注释文档只会生成public和protected修饰的数据成员和成员函数。

<h3 id="6.4">6.4 类的访问权限</h3>

如果将类定义为public，表示这个类可以被客户端程序员所调用，这样就可以控制客户端程序员是否可以创建该类的一个对象，具体的使用：`import org.fmz. ...`

#### 类访问权限的原则

1. 为了控制某个类的访问权限，修饰词必须出现在关键字class之前
2. 每一个编译单元(文件，库单元指的是包)都只能有一个public类。这表示：每一个编译单元都有单一的公共接口，用public类来实现。该接口可以包含众多具有包访问权限的类。如果某个编译单元内有一个以上的public类，编译器就会出现错误的信息。
3. public类的名称必须与该编译单元的文件名称想匹配，包括大小写，必须完全一致。这是因为解释器会根据隐式静态的构造方法(构造方法名同public名相同)来搜索和定位具体class文件的位置，以便下一步加载类文件。
4. 如果编译文件内没有public类，则编译文件的名称没有严格与哪个类名相同的要求，不过这样会造成人们在阅读和维护代码使产生混淆。

> 为了客户端程序员不会依赖一个包中的任何特定的实现细节，也就是你还有改变原有方案或者删除的灵活性，你可以将public类的修饰词去掉，这样该类就具有了包的访问权限，这能在该包中中使用，而不必想用户做出任何承诺，从而也有了前面所讲的灵活性。

> 在创建一个包访问权限的类时，应该将该类的域声明为private才有意义，应该尽可能的将域指定为私有的。但是，通常情况下，将该类中的方法赋予和类的访问权限(包访问权限)相同的访问权限。

> 注意：类的访问权限既不可以是private(对于该类外其他任何类都不能访问它)的,也不可以是protected的。所以对类的访问权限来说只能有public和默认访问权限。

> 如果你不希望任何人对该类具有访问权限，可以将该类的构造方法定义为private的，从而阻止了任何人创建该类的对象，不过可以通过该类的static方法在成员内部创建，这样你就可以要求有条件的创建对象了，你可以自定义条件在static方法体中。

	package org.fmz.access ;

	class Sundae{
		private Sundae(){}
		static Sundae makeASundaeInstance(){
			//添加自己的条件
			System.out.println("OK!!!") ;
			return new Sundae() ;
		}
	}

	public class PrivateAccess{
		public static void main(String args[]){
			//Sundae sun = new Sundae() ;
			Sundae.makeASundaeInstance() ;
		}
	}

> 如上述代码，就是private构造器，可以完成有条件的创建对象。比如说：你要知道创建了多少个Sundae对象(可能你要限制其创建对象的数量)，用这样的方法就很容易了，因为对象都是从这里创建出来的。

#### 经典的单例模式

	package org.fmz.access ;

	public class SingletonPattern{
		private SingletonPattern(){}
		private static SingletonPattern sp = new SingletonPattern() ;//静态域中，只能创建一个对象，对象共享
		public static SingletonPattern access(){
			return sp ;
		}
	}

> 此程序利用了静态域中成员共享的特性，在一个static域中，你只能创建一个对象，因为只有一份储存空间，对象是共享的(这里是以对象为中心，而不是引用，多个引用但是始终对应着一个对象)。

> 访问权限专注于类库的创建者和使用者之间的关系。通过对类和成员记性权限控制可以达到两个目标：1，使客户端成员不能接触到他们不该接触的，而只是关注他们所关心的接口；2，使类库的创建者具有更加多的灵活性，可以更改类的内部的工作方式，而不会担心这样会对客户端程序员造成重大的影响。也就是说可以确保客户端程序员不依赖某个类的底层实现的任何部分。底层实现的部分会作为类中的辅助成员用包访问权限或者private修饰而对客户端隐藏了起来。实例：

	package org.fmz.access ;

	public class Apple{
		public Apple peeled(Apple apple){
			return peelApple(apple) ;
		}
		Apple peelApple(Apple apple){
			//do something
			return apple ;
		}
	}

> 对于上述Apple类，有一个得到削好的苹果的方法和一个削苹果的方法，如果客户端只担心怎么得到一个削好的苹果而不担心这个苹果到底是怎么削的，我们可以将客户端关心的方法用public修饰(客户端可以调用)，而将类库创建者关系的用什么办法削苹果用默认访问权限修饰(如果不希望包内的其他类使用可以定义为private)，这样当类库的创建者发现一个更好的方法削苹果时，比如以前只是用小刀来削，现在科技发展了可以用削苹果机来削苹果，这时候类库的创建者完全可以将削苹果的方法替换掉，改为更先进的方法。具体在实际中，类库的创建者提高底层设计的性能而不会影响客户端的使用(实际上客户端得到的服务变得越来越好或者有了很多新的更优化的选择)

---

<h2 id="7">7 复用类</h2>

<h3 id="7.1">7.1 组合语法</h3>

组合(compose)复用示例

	package org.fmz.reusing ;

	class WaterSource{
		private String s ;
		WaterSource(){
			System.out.println("WaterSource()") ;
			s = "constructor" ;
		}
		public String toString(){//此方法为覆写的Object类中的方法，不能分配更低的访问权限
			return s ;
		}
	}

	public class SprinklerSystem{
		private String value ;
		private WaterSource ws = new WaterSource() ;
		private int i ;
		private float f ;
		public String toString(){//此方法为覆写的Object类中的方法，不能分配更低的访问权限
			return
				"value -->" + value + " " +
				"ws -->" + ws + " " +
				"i -->" + i + " " +
				"f -->" + f ;
		}
		public static void main(String args[]){
			SprinklerSystem ss = new SprinklerSystem() ;
			System.out.println(ss) ;
		}
	}

>注意覆写方法的时候一定要比原来被复写的方法更加宽松的访问权限：private-->包访问权限-->protected-->public，箭头指向更加宽松方向。

>对于非基本数据类型的对象数据成员，其初始化的默认值为null，在不抛出异常的情况下，仍然可以打印出null。

<h3 id="7.2">7.2 继承语法：关键字extends</h3>

当创建一个类时，总是在继承，因此除非明确已经指定要从其他类中继承，否则就隐式地从java的标准根类Object进行继承

继承的一般规则是：将所有的数据成员都指定为private，将所有的方法都指定为public或者protected(只允许子类调用的方法)

为了避免在覆写基类方法的时候调用本方法可能产生的递归现象，因此要使用关键字`super`指定是基类中的方法。  

#### 初始化基类

在初始化的过程中，java会自动在导出类的构造器中插入对基类构造器的调用。整个对象的构建过程是从基类向外扩散的，所以基类在导出类构造器可以访问它之前就已经完成了初始化。

#### 带参数的构造器

如果基类的构造器中含有参数，那么在导出类中必须要用到基类中的构造器(相同的参数列表)，因为这个时候已经没有了默认的无参构造器，而要想基类在基类子对象创建之前得到初始化，必须要有显示的引用才可以完成。示例代码：

	package org.fmz.reusing ;

	class A{
		public A(int i){
			System.out.println(i) ;
		}
	}

	public class B extends A{
		public B(int i){
			super(i) ;
			System.out.println("constructor") ;
		}
	}

>上例中如果不提供显式的super调用，则基类无法完成初始化，编译器会报错。

>在导出来构造器中对基类构造器的调用必须写在前面，因为只有基类完成了初始化才轮到导出来(因为导出类在创建对象的时候可能用到基类成员)。

>如果基类创建了一个有参数列表的构造器，在导出来一定要得到调用才可以。

<h3 id="7.3">7.3 代理</h3>

代理是组合和继承的中庸之道，因为我们将一个成员对象置于所要构造的类中(组合)，并且我们在新类中暴露了该成员对象的所有方法(继承)

宇宙飞船控制器

	package org.fmz.reusing ;

	public class SpaceShipControls{
		void up(int velocity){}
		void down(int velocity){}
		void left(int velocity){}
		void right(int velocity){}
		void forward(int velocity){}
		void back(int velocity){}
		void turboBoost(int velocity){}
	}

宇宙飞船继承了宇宙飞船控制器，以便具有其功能

	package org.fmz.reusing ;

	public class SpaceShip extends SpaceShipControls{
		private String name ;
		public SpaceShip(String name){
			this.name = name ;
		}
	}

>宇宙飞船明显并不是宇宙飞船控制器，这种情况用继承表示基类和导出类之间的关系显然并没有十分恰当，但是也能完成相应的功能。还有一点就是：宇宙飞船控制器的所有行为都暴露给了宇宙飞船。

用代理解决类型不恰当和方法暴露无法控制的问题

	package org.fmz.reusing ;

	public class SpaceShipDelegation{
		private String name ;
		private SpaceShipControls ssc = new SpaceShipControls() ;
		public SpaceShipDelegation(String name){
			this.name = name ;
		}
		//delegated methods
		public void up(int velocity){
			ssc.up() ;
		}
		public void down(int velocity){
			ssc.down();
		}
		public void left(int velocity){
			ssc.left() ;
		}
		public void right(int velocity){
			ssc.right() ;
		}
		public void forward(int velocity){
			ssc.forward() ;
		}
		public void back(int velocity){
			ssc.back() ;
		}
		public void turboBoost(int velocity){
			ssc.turboBoost() ;
		}
	}

>通过以上的代码，我们运用了组合，实现了代码的复用；同时我们想继承一样，拥有了接口的方法。

>同时，我们拥有了更多的控制权：我们可以选择只提供成员对象方法的某个子集。

<h3 id="7.4">7.4 结合使用组合和继承</h3>

#### 确保正确清理

有的时候类可能要在其生命周期内执行一些必要的清理活动，但你并不知道垃圾回收器何时将会被调用或者是否会被调用。因此如果你想要某个类清理一些东西，必须显式地编写一个特殊的方法来做这件事，并要客户端程序员知道他们必须要调用这一方法。注意：必须将这一动作置于finally子句之中，以防异常的发生。实例代码：

	package org.fmz.reusing ;

	class Shape{
		Shape(int i){
			System.out.println("Shape Constructor") ;
		}
		void dispose(){
			System.out.println("Shape Dispose") ;
		}
	}

	class Circle extends Shape{
		Circle(int i){
			super(i) ;
			System.out.println("Circle Constructor") ;
		}
		void dispose(){
			System.out.println("Erasing Circle") ;
			super.dispose() ;
		}
	}

	class Triangle extends Shape{
		Triangle(int i){
			super(i) ;
			System.out.println("Triangle Constructor") ;
		}
		void dispose(){
			System.out.println("Erasing Triangle") ;
			super.dispose() ;
		}
	}

	class Line extends Shape{
		private int start, end ;
		Line(int start,int end){
			super(start) ;
			this.start = start ;
			this.end = end ;
			System.out.println("Line Constructor" + start + "," + end) ;
		}
		void dispose(){
			System.out.println("Erasing Line" + start + "," + end) ;
			super.dispose() ;
		}
	}

	class CADSystem extends Shape{
		private Circle c ;
		private Triangle t ;
		private Line[] lines = new Line[3] ;
		public CADSystem(int i){
			super(i+1) ;
			c = new Circle(i) ;
			t = new Triangle(i) ;
			for(int j=0;j<lines.length;j++){
				lines[j] = new Line(j,j*j) ;
			}
			System.out.println("Combined Constructor") ;
		}
		public void dispose(){
			System.out.println("Erasing Combined Constructor") ;
			for(int j=0;j<lines.length;j++){
				lines[j].dispose() ;
			}
			t.dispose() ;
			c.dispose() ;
			super.dispose() ;
		}
		public static void main(String args[]){
			CADSystem cads = new CADSystem(1) ;
			try{
			
			}finally{
				cads.dispose() ;
			}
		}
	}

>finally表示无论try语句中发生什么事情，finally语句中的cads.dispose()都会得到执行。

>除了内存以外，不能依靠垃圾回收器做任何事情，如果要进行清理，最好编写自己的清理方法，但是不要使用finalize()方法(this方法只有子在内存清理的时候才会被调用，如果内存没有收到威胁，永远都不会发生)

#### 名称屏蔽

如果导出类对基类中的方法进行覆写，必须具有相同的方法签名(方法名称和参数列表)并且返回类型与基类一致；如果想要覆写基类中的方法，但是没有完全保证二者的方法签名一致(一般情况下名字一致，但是参数列表可能不同)，这时候不会屏蔽掉基类中的方法名字，是重载而不是覆写。

如果只想对基类中的方法进行覆写，而想避免误写而变成重载的现象，可以利用关键字： `@Override` ，示例代码：

	package org.fmz.reusing ;

	class A{
		void tell(){
		
		}
	}

	class B extends A{
		//@Override
		B tell(B b){
			return b ;
		}
	}
	public class TestOverride{
		public static void main(String args[]){
			B b = new B() ;
			b.tell() ;
		}
	}

>如果对基类的某个方法进行覆写，必须不有相同的返回类型和方法签名，这样才可以覆写成功；而当方法签名不一致时，这时候发生了重载，与返回类型就没有关系了(方法的重载只与方法签名有关)。

<h3 id="7.5">7.5 在组合和继承之间进行选择</h3>

组合和继承都是允许在新的类中放置子类对象，组合是显式的这样做，而继承则是隐式的做。

组合技术通常想用于在新的类中使用现有类的功能而非它的接口的情形。即，在新类中嵌入某个对象，让其实现所需要的功能，但是新类用户看到的只是新类的接口，而非嵌入对象的接口。为了取得此效果，需要在新的类中嵌入现有类的一个private对象。有时候允许类的用户直接访问类中的组合对象也是有必要的，如果组合进新类的对象自身隐藏了具体的实现，这种做法也是安全的。但这种情况极少数，大部分的组合都因该使域变为private。

如果是 is-a 的关系，则使用继承；如果是 has-a 的关系，则使用组合。

<h3 id="7.6">7.6 protected关键字</h3>

protected访问权限又称为继承访问权限，它允许子类对基类中的protected方法进行访问，同时它也提供了包的访问权限。

<h3 id="7.7">7.7 向上转型</h3>

为新的类提供方法并不是继承技术中最重要的方面，最重要的方面是用来表示新类和基类之间的关系：新类型是现有类型的一种类型

将子类的引用转换为基类的引用，我们称之为向上转型。

<h3 id="7.8">7.8 final关键字</h3>

final关键字通常是指：这是不能改变的。不想改变的原因有两种：效率和设计

#### fianl 数据

1. 在java中finial常量必须是基本数据类型(这里说的是finial常量，但是final还可以用来修饰对象引用)，并且以关键字finial表示；对finial常量进行定义的时候必须对其进行赋值
2. 一个既是static又是finial的域表示：只占据一段不能改变的储存空间
3. 当finial作用于对象引用时，finial是引用恒定不变，一旦引用被初始化指向一个对象，就无法再把它改为指向另外一个对象，然而对象本身是可以被修改的

> java并未提供使任何对象恒定不变的途径

> 带有恒定初始值(即，编译常量)的static finial基本数据类型取用大写字母命名，并且字与字之间用下划线隔开

##### 空白final

A blank final instance variable must be definitely assigned at the end of every constructor of the class in which it is declared; otherwise a compile-time error occurs.

- Final变量
    - Instance variable level
        - A final variable can be initialized only once
        - A final variable at class level must be initialized before the end of the constructor.
    - Local(method) level
        - A final variable at method level can be initialized only once
        - It must be initialized before it is used

##### final参数

当使用finial参数到时候，你可以读参数，但是无法修改参数

#### final方法

使用finial方法的原因主要有两个：

1. 处于设计的考虑。把方法锁定，以防止任何继承类修改它的含义。
2. 处于效率的考虑。在最近的java版本中，出于此种的考虑已经渐渐过时了，因为java虚拟机本身就能优化许多问题了。

#### finial和private关键字

类中的所有private方法都是隐式地指定为finial的。由于无法取得private方法，因此也就无法覆盖它。可以对private方法添加final修饰词，但是该方法并不能增加任何额外的意义。

下面一段代码看似可以覆写，是怎么回事？

	package org.fmz.reusing ;

	class WithFinals{
		private final void f(){
			System.out.println("WithFinals.f()") ;
		}
		private final void g(){
			System.out.println("WithFinals.g()") ;
		}
	}

	class OverridingPrivate extends WithFinals{
		private final void f(){
			System.out.println("OverridingPrivate.f()") ;
		}
		private final void g(){
			System.out.println("OverridingPrivate.g()") ;
		}
	}

	class OverridingPrivate2 extends OverridingPrivate{
		public final void f(){
			System.out.println("OverridingPrivate.f()") ;
		}
		public final void g(){
			System.out.println("OverridingPrivate.g()") ;
		}
	}

	public class FindingOverridingIllusion{
		public static void main(String args[]){
			OverridingPrivate2 op2 = new OverridingPrivate2() ;
			op2.f() ;
			op2.g() ;
			OverridingPrivate op = op2 ;
			//op.f() ;		//不能访问，因为是私有化方法，并没有覆写的发生
			//op.g() ;		//不能访问，因为是私有化方法，并没有覆写的发生
		}
	}

>覆盖只有在某方法是基类中的一部分的时候才会出现。即，必须能将一个对象向上转型为它的基本类型并调用相同的方法。如果某个方法是private，它就不是基类的接口的一部分。

>上述程序代码中私有化方法只是隐藏在类中的程序代码，只不过拥有相同的名称罢了。

>由于private方法无法触及并且能够有效隐藏，所以只把它考虑成它所归属类的组织结构的一部分，仅此而已。

#### final 类

当某个类被定义为final时，标明你不打算继承该类，也不允许别人这样做。

<h3 id="7.9">7.9 初始化及类的加载</h3>

类的代码在初次使用时才会被加载，这通常是指加载发生在类创建的第一个对象之时。但是访问static域或者static方法时，也会发生加载。注意：构造器也是static方法，所以更加准确的说法是类是在任何static成员被访问时加载的。

>初次使用指出也是static初始化发生之处。所有的static对象和static代码段都会依据程序中的顺序而依次加载。当然，定义为static的东西只会被初始化一次。

#### 继承与初始化

实例代码：

	package org.fmz.reusing ;

	class Insect{
		private int i = 9 ;
		protected int j ;
		Insect(){
			System.out.println("i = " + i + "，j = " + j) ;
			j = 39 ;
			System.out.println("i = " + i + "，j = " + j) ;
		}
		private static int x1 = printInit("Static Insect.x1 initialized") ;
		static int printInit(String s){
			System.out.println(s) ;
			return 47 ;
		}
	}

	public class Beetle extends Insect{
		private int k = printInit("Beetle.k initalized") ;
		public Beetle(){
			System.out.println("k = " + k) ;
			System.out.println("j = " + j) ;
		}
		private static int x2 = printInit("Static Beetle.x2 initialized") ;
		public static void main(String args[]){
			System.out.println("Beetle Constructor") ;
			Beetle b = new Beetle() ;
		}
	}

> 输出结果为：

	Static Insect.x1 initialized
	Static Beetle.x2 initialized
	Beetle Constructor
	i = 9，j = 0
	i = 9，j = 39
	Beetle.k initalized
	k = 47
	j = 39

> - 加载与初始化过程：在Beetle上运行java时所发生的第一件事情是试图访问Beetle.main()(一个static方法)，于是加载器就开始启动并且找出Beetle类的编译代码(在Beetle.class的文件中中)。在对它进行加载的过程中，编译器会注意到它有一个基类(这是有关键字extends得知的)，于是继续进行加载。不管你是否要产生一基类，这都要发生.
- 如果该基类还有自身的基类，那么第二个基类就会被加载，如此类推。接下来，根基类中的static初始化会被执行，然后是下一个导出类，以此类推。这种方法很重要，因为导出类的static初始化可能会依赖于基类成员是否被正确的初始化。
- 必要的类已经加载完毕，对象就可以开始创建了。首先对象中的所有基本数据类型都会别设置为默认值，对象的引用被设置为null(这是通过对象内存设置为二进制零一举生成的)，然后，基类的构造器会得到调用。导出类的构造器和基类的构造器一样，以相同的顺序来经理相同的过程。在基类构造器完成之后，实例变量按次序被初始化，最后，构造器的其余部分得到执行。

继承和组合都能从现有的类型生成新的类型。组合一般是将现有的类型作为新的类型底层实现的一部分来加以复用而继承复用的是接口。

在一开始的程序设计时，一般应该优先使用组合(或者可能是代理)，只是在确定必要时才使用继承。因为组合更加的灵活。

应该把一个项目视为一个有机的、进化着的生命体去培养，而不是打算盖一个摩天大楼一样快速见效，就会获得更多的成功和更迅速的回馈。

---

<h2 id="8">8 多态</h2>

封装是通过合并特征和行为来创建新的数据类型。实现隐藏则通过将细节私有化，从而把接口和实现分离开来。多态的作用是消除类型之间的耦合关系。

向上转型：将子类对象的引用看做基类对象的引用的做法。

<h3 id="8.1">8.1 再论向上转型</h3>

#### 多态性的实现

示例代码：

	package org.fmz.polymorphism ;

	public enum Note{
		MIDDLE_C, C_SHARP, B_FLAT ;
	}

> Note.java

	package org.fmz.polymorphism ;

	class Instrument{
		public void play(Note n){
			System.out.println("Instrument.play()") ;
		}
	}

> Instrument.java，将之定义为包的访问权限，不允许被包外调用。

	package org.fmz.polymorphism ;

	public class Wind extends Instrument{
		public void play(Note n){
			System.out.println("Wind.play()" + n) ;
		}
	}

> Wind.java，继承自Instrument类，在同一个包中继承，不需要包的导入。

	package org.fmz.polymorphism ;

	public class Music{
		public static void tune(Instrument i){
				i.play(Note.MIDDLE_C) ;
		}
		public static void main(String args[]){
			Wind wind = new Wind() ;
			tune(wind) ;
		}
	}

> Music.java，定义方法将接口的引用作为参数导入，实现了多态性。

#### 忘记数据类型

在进行向上转型的过程中，我们要做的就是忘记数据类型。

如果不忘记数据类型，会发生什么情况呢？示例代码：

	package org.fmz.polymorphism ;

	public class Stringed extends Instrument{
		public void play(Note n){
			System.out.println("Stringed.play()" + n) ;
		}
	}

> Stringed.java，增加一种新的乐器。

	package org.fmz.polymorphism ;

	public class Brass extends Instrument{
		public void play(Note n){
			System.out.println("Brass.play()" + n) ;
		}
	}

> Brass.java，增加另外一种新的乐器。

	package org.fmz.polymorphism ;

	public class Music2{
		public static void tune(Wind i){
				i.play(Note.MIDDLE_C) ;
		}
		public static void tune(Stringed i){
				i.play(Note.MIDDLE_C) ;
		}
		public static void tune(Brass i){
				i.play(Note.MIDDLE_C) ;
		}
		public static void main(String args[]){
			Wind wind = new Wind() ;
			Stringed stringed = new Stringed() ;
			Brass brass = new Brass() ;
			tune(wind) ;
			tune(stringed) ;
			tune(brass) ;
		}
	}

> Music2.java，这时候，如果想实现音乐的播放功能，而没有忘记数据类型(没有把接口的引用作为参数传递)，就需要为每一中新增加的乐器增加一种相对应的方法。而如果忘记数据类型，则会少做很多工作，而且实现同样的功能。示例代码：

	package org.fmz.polymorphism ;

	public class Music3{
		public static void tune(Instrument i){
				i.play(Note.MIDDLE_C) ;
		}
		public static void main(String args[]){
			Wind wind = new Wind() ;
			Stringed stringed = new Stringed() ;
			Brass brass = new Brass() ;
			tune(wind) ;
			tune(stringed) ;
			tune(brass) ;
		}
	}

> Music.java，Music2中还存在一个问题，如果我们忘记重载任何一种乐器参数的方法，编译器并不会报错。这就使得整个类变得不可控。如果我们进行向上转型，可以使得我们不管导出类是否存在，因为我们只与接口打交道。这样会变得更好。

<h3 id="8.2">8.2 转机</h3>

#### 方法调用的绑定

将一个方法的调用同方法主体关联起来被称作绑定。若程序在执行之前进行绑定(如果有的话，由编译器和链接程序实现)，叫做前期绑定。C语言只有一种方法的调用：前期绑定。

后期绑定也叫作动态绑定或者运行时绑定。含义是：运行时根据对象的类型进行绑定。这种后期绑定是由一种机制实现的：在运行时能判断出对象的类型，从而调用恰当的方法。

在java中，除了static和final(private方法属于final方法)之外，其他所有的方法都是后期绑定。

> 为什么要使用final方法呢？这样可以防止其他人覆盖掉该方法。更重要的是这样可以有效地关闭动态绑定，或者告诉编译器不需要对其进行动态绑定。

#### 产生正确的行为

示例代码

	package org.fmz.polymorphism.shape ;

	public class Shape{
		public void draw(){} ;
		public void erase(){} ;
	}

> Shape.java 基类

	package org.fmz.polymorphism.shape ;

	public class Circle extends Shape{
		public void draw(){
			System.out.println("Circle.draw()") ;
		}
		public void erase(){
			System.out.println("Circle.erase()") ;
		}
	}

> Circle.java，子类

	package org.fmz.polymorphism.shape ;

	public class Square extends Shape{
		public void draw(){
			System.out.println("Square.draw()") ;
		}
		public void erase(){
			System.out.println("Square.erase()") ;
		}
	}

> Square.java，子类

	package org.fmz.polymorphism.shape ;

	public class Triangle extends Shape{
		public void draw(){
			System.out.println("Triangle.draw()") ;
		}
		public void erase(){
			System.out.println("Triangel.erase()") ;
		}
	}

> Triangle.java，子类

	package org.fmz.polymorphism.shape ;

	import java.util.* ;

	public class RandomShapeGenerator{
		Random rand = new Random(470) ;
		public Shape next(){
			switch(rand.nextInt(3)){
				default:
				case 0: return new Circle() ;
				case 1: return new Square() ;
				case 2: return new Triangle() ;
			}
		}
	}

> 随机生成器工厂，在return语句中返回指向某个子类的引用，并将其以Shape类型从next()方法中发送出去。所以无论什么时候在调用next()方法的时候，绝对不知道具体类型到底是什么，因为我们总是能获得一个通用的Shape引用。

	package org.fmz.polymorphism.shape ;

	public class Shapes{
		private static RandomShapeGenerator gen = new RandomShapeGenerator() ;
		public static void main(String args[]){
			Shape[] s = new Shape[9] ;
			for(int i=0;i<s.length;i++){
				s[i] = gen.next() ;
			}
			for(Shape shape : s){
				shape.draw() ;
			}
		}
	}

> Shapes.java，测试类。main()方法中包含了shape引用组成的数组，这个数组通过RandomShapeGenerator.next()来填入数据。此时，我们只是知道我们拥有了一些Shape，并不知道Shape的具体情况(编译器也不知道)。只有当我们遍历数组时，并为每个元素调用draw()方法时，才会发生动态绑定的行为。

#### 缺陷：覆盖私有方法

只有非private方法才可以被覆盖。在导出类中，对于基类中的私有方法，最好采用不同的名字。示例代码：

	package org.fmz.polymorphism ;

	public class PrivateOverride{
		private void f(){
			System.out.println("private.f()") ;
		}
		public static void main(String args[]){
			PrivateOverride po = new Derived() ;
			po.f() ;
		}
	}
	class Derived extends PrivateOverride{
		public void f(){
			System.out.println("public.f()") ;
		}
	}

> 基类中的私有化方法不会被覆盖掉，因为它使私有的不属于基类中的一部分。

#### 缺陷：域与静态方法

只有普通方法的调用时多态的，如果你直接访问某个域，这个访问将在编译期间进行解析。

	package org.fmz.polymorphism ;

	class Super{
		public int field = 0 ;
		public int getField(){
			return field ;
		}
	}

	class Sub extends Super{
		public int field = 1 ;
		public int getField(){
			return field ;
		}
		public int getSuperField(){
			return super.field ;
		}
	}

	public class FieldAccess{
		public static void main(String args[]){
			Super sup = new Sub() ;
			System.out.println("sup.field = " + sup.field + "，sup.getField() = " + sup.getField()) ;
			Sub sub = new Sub() ;
			System.out.println("sub.field = " + sub.field + "，sub.getField() = " + sub.getField() +"，sub.getSuperField = " + sub.getSuperField()) ;
		}
	}/*output:
    sup.field = 0，sup.getField() = 1
    sub.field = 1，sub.getField() = 1，sub.getSuperField = 0
    */


> - 当Sub对象转型为Super引用时，任何域的访问操作都将由编译器来解析。因此不会是多态的。要想得到基类中的域，必须显式的指明super.field
- 在实践中，很难发生这样的情况，一方面一般会把所有的域设置为private，因此不能直接访问它，副作用就是必须用方法来访问；另一方面我们不会将子类中域的名字和基类中重复，因为这样做会产生混淆。
- 如果一个方法是静态的，那么其行为也就不具有多态性。这是因为静态方法是与类，并不是与单个对象相关联的。示例代码：

	package org.fmz.polymorphism ;

	class StaticSuper{
		public static void staticGet(){
			System.out.println("Base staticGet()") ;
		}
		public void dynamicGet(){
			System.out.println("Base dynamicGet()") ;
		}
	}

	class StaticSub extends StaticSuper{
		public static void staticGet(){
			System.out.println("Derived staticGet()") ;
		}
		public void dynamicGet(){
			System.out.println("Derived dynamicGet()") ;
		}
	}

	public class StaticPolymorphism{
		public static void main(String args[]){
			StaticSuper ssup = new StaticSub() ;
			ssup.staticGet() ;//不会发生多态性
			ssup.dynamicGet() ;
		}
	}/*
    Base staticGet()
    Derived dynamicGet()
    */

> static方法不会有多态性的发生。

<h3 id="8.3">8.3 构造器与多态</h3>

构造器不具有多态性(它们实际上是static方法，只不过是static的声明是隐式的)

1. 调用基类中的构造器
2. 按照声明顺序调用成员的初始化方法
3. 调用导出类的构造器主体

#### 继承与清理

示例代码：

	package org.fmz.polymorphism ;

	class Characteristic{
		private String s ;
		Characteristic(String s){
			this.s = s ;
			System.out.println("Creating Characteristic " + s) ;
		}
		protected void dispose(){
			System.out.println("Disposing Characteristic " + s) ;
		}
	}

	class Description{
		private String s ;
		Description(String s){
			this.s = s ;
			System.out.println("Creating Description " + s) ;
		}
		protected void dispose(){
			System.out.println("Disposing Description " + s) ;
		}
	}

	class LivingCreature{
		private Characteristic c = new Characteristic("is alive") ;
		private Description d = new Description("Basic Living Creature") ;
		LivingCreature(){
			System.out.println("LivingCreature()") ;
		}
		protected void dispose(){
			System.out.println("Dispose LivingCreature") ;
			d.dispose() ;
			c.dispose() ;
		}
	}

	class Animal extends LivingCreature{
		private Characteristic c = new Characteristic("has heart") ;
		private Description d = new Description("Animal not Vegetable") ;
		Animal(){
			System.out.println("Animal()") ;
		}
		protected void dispose(){
			System.out.println("Dispose Animal") ;
			d.dispose() ;
			c.dispose() ;
			super.dispose() ;
		}
	}

	class Amphibian extends Animal{
		private Characteristic c = new Characteristic("can live in the water") ;
		private Description d = new Description("Both water and land") ;
		Amphibian(){
			System.out.println("Amphibian()") ;
		}
		protected void dispose(){
			System.out.println("Dispose Amphibian") ;
			d.dispose() ;
			c.dispose() ;
			super.dispose() ;
		}
	}

	public class Forg extends Amphibian{
		private Characteristic c = new Characteristic("croaks") ;
		private Description d = new Description("Eat bugs") ;
		Forg(){
			System.out.println("Forg()") ;
		}
		protected void dispose(){
			System.out.println("Dispose Forg") ;
			d.dispose() ;
			c.dispose() ;
			super.dispose() ;
		}

		public static void main(String args[]){
			Forg forg = new Forg() ;
			System.out.println("Bye!") ;
			forg.dispose() ;
		}
	}
	/* Output:
	Creating Characteristic is alive
	Creating Description Basic Living Creature
	LivingCreature()
	Creating Characteristic has heart
	Creating Description Animal not Vegetable
	Animal()
	Creating Characteristic can live in the water
	Creating Description Both water and land
	Amphibian()
	Creating Characteristic croaks
	Creating Description Eat bugs
	Forg()
	Bye!
	Dispose Forg
	Disposing Description Eat bugs
	Disposing Characteristic croaks
	Dispose Amphibian
	Disposing Description Both water and land
	Disposing Characteristic can live in the water
	Dispose Animal
	Disposing Description Animal not Vegetable
	Disposing Characteristic has heart
	Dispose LivingCreature
	Disposing Description Basic Living Creature
	Disposing Characteristic is alive
	*///:~

> - 销毁的顺序应该是：首先对导出类进行销毁，然后是基类。因为导出类的清理可能会调用基类中的方法。所以销毁的顺序应该是与声明的顺序相反(因为字段的初始化是按照声明的顺序记性的)
- 如果成员对象中存在一个或者多个对象共享的情况，那么久不能简单的调用dispose()方法了。这种情况下需要使用引用计数来跟踪仍旧访问着共享对象的对象数量了。

#### 构造器内部多态方法的行为

如果在一个构造器的内部正在构造的对象的某种动态绑定方法，那么会发生什么样的情况呢？

在一般的方法内部，动态绑定的调用是在运行时才决定的。因为对象无法得知它是属于方法所在的类，还是属于方法所在的类的导出类。

从概念上讲，构造器的工作实际上时创建对象。在任何构造器的内部，整个对象可能只是部分形成-我们只知道基类对象已经初始化。如果构造器只是只是在构建对象过程中的一个步骤，并且该对象所属的类是从这个构造器所属的类导出的，那么导出部分在当前构造器正在被调用的时刻仍旧没有被初始化。然而一个动态绑定的方法调用时要深入到继承层次结构的内部，它可以调用导出类里的方法。如果我们在构造器里这么做，就可能会调用某些方法，而这个方法所操纵的成员可能还没有进行初始化，这是灾难性的。

示例代码：

	package org.fmz.polymorphism ;

	class Glyph{
		void draw(){
			System.out.println("Glyph.draw()") ;
		}
		Glyph(){
			System.out.println("Before Glyph.draw()") ;
			draw() ;
			System.out.println("After Glyph.draw()") ;
		}
	}

	class RoundGlyph extends Glyph{
		private int radius = 1 ;
		RoundGlyph(int r){
			radius = r ;
			System.out.println("RoundGlyph()，radius = " + radius) ;
		}
		void draw(){
			System.out.println("RoundGlyph.draw()，radius = " + radius) ;
		}
	}

	public class PolyConstructor{
		public static void main(String args[]){
			new RoundGlyph(5) ;
		}
	}
	/* Output:
	Before Glyph.draw()
	RoundGlyph.draw()，radius = 0
	After Glyph.draw()
	RoundGlyph()，radius = 5
	*///:~

>在构造器当中调用了覆写的draw()方法，但是导出类中的成员没有进行初始化，所以：`RoundGlyph.draw()，radius = 0`，在屏幕上只会画出一个点，而且编译器并不会报错，我们差错的时候也是很艰难。

#### 实例化的实际过程

1. 在其他任何事情发生之前，将分配对象的储存空间初始化为二进制的0。
2. 调用基类中的构造器。
3. 按照声明的顺序调用成员的初始化方法。
4. 调用导出类的构造器主体。

> - 这样做的优点是多有的东西都至少初始化为0，其中通过组合而嵌入类内部的对象的引用，其值为null，如果忘记对该引用进行初始化，在运行时就会发生异常，这也正是我们发现问题证据的地方。
- 通过上面的陈述，我么得出结论，编写构造器时有一条有效的准则：用尽量可能简单的方法使对象进入正常状态；如果可能的话避免调用其他方法。在构造器中唯一能够安全调用的方法是基类中的final方法(private方法也隐式属于final方法)

<h3 id="8.4">8.4 协变返回类型</h3>

在java SE5中添加了协变返回类型，表示：在导出类中被覆盖的方法可以返回基本方法的返回类型的某种导出类型。

示例代码：

	package org.fmz.polymorphism ;

	class Grain{
		public String toString(){
			return "Grain" ;
		}
	}

	class Wheat extends Grain{
		public String toString(){
			return "Wheat" ;
		}
	}

	class Mill{
		Grain process(){
			return new Grain() ;
		}
	}

	class WheatMill extends Mill{
		Wheat process(){//以前的版本，次覆盖的方法只能返回Grain，现在的版本可以返回更加具体的类型
			return new Wheat() ;
		}
	}

	public class CovariantReturn{
		public static void main(String args[]){
			Mill m = new Mill() ;
			Grain g = m.process() ;
			System.out.println(g) ;
			m = new WheatMill() ;
			g = m.process() ;
			System.out.println(g) ;
		}
	}

<h3 id="8.5">8.5 用继承来进行设计</h3>

我们在程序设计的时候有一条通用的准则：用继承表达行为之间的差异，用字段表示状态之间的变化。

实例代码：

	package org.fmz.polymorphism ;

	class Actor{
		void act(){
			
		} 
	}

	class HappyActor extends Actor{
		void act(){
			System.out.println("HappyActor") ;
		}
	}

	class SadActor extends Actor{
		void act(){
			System.out.println("SadActor") ;
		}
	}

	class Stage{
		private Actor actor = new HappyActor() ;
		public void change(){
			actor = new SadActor() ;
		}
		public void performPlay(){
			actor.act() ;
		}
	}

	public class StagePerform{
		public static void main(String args[]){
			Stage stage = new Stage() ;
			stage.performPlay() ;
			stage.change() ;
			stage.performPlay() ;
		}
	}

> - 这道程序用了继承和组合，组合不会强制我们的程序设计进入继承层次结构中；而且，组合更加灵活，因为它可以动态的选择了类型(也就选择了行为)，相反，继承在编译的时候就要知道确切的类型。
- 这种通过继承和组合一起使用的模式称之为：状态模式。在程序中，由继承来表现演员的不同行为(happy or sad)，由组合(stage类)来决定类的动态绑定，也就更加灵活的实现了行为的选择。

#### 纯继承与扩展

纯继承的关系也就是："is-a"，导出类有且只有覆写了基类中的多有方法，我们说导出类是一个基类。

扩展关系也就是："is-like-a"，导出类在复写基类的方法的同时，增加了自己特有的方法，我们称之为导出类像一个基类。

> - 纯继承关系也是纯替代关系，通过向上转型的多态性，将导出类对象的引用转换为基类对象的引用之后，基类可以接收发给导出类的任何消息。
- 扩展关系中的基类和导出类有着共同的接口，但是导出类还具有额外的方法实现其他特性。这时候，导出类中接口的扩展部分不能被基类访问，因此我们一旦向上转型就不能调用那些方法了。

#### 向下转型和运行时类型的识别

RTTI(Running Time Type Identification，运行时类型识别)，这是java的一种机制来确保对向下转型进行检查。

---

<h2 id="9">9 接口</h2>

抽象类是类与接口之间的一种中庸之道。

<h3 id="9.1">9.1 </h3>

如果一个基类只是想作为一个接口来操作它的所有导出类，那么创建这个基类的对象就没有任何意义了；并且我们还想阻止使用者这么做。可以让基类中的方法都产生错误就可以实现这个目的，但是这样做会将错误的信息延迟到运行的时候才能捕获，所以我们想在编译的时候就能够捕获这种信息，于是java提供了抽象方法的机制。

抽象方法是不完整的，仅仅有声明而没有方法体:`abstract void f()`

包含抽象方法的类称为抽象类。如果对一个抽象类进行实例化，那么编译器就会报错，抽象类无法进行实例化，但是抽象类有构造方法。

如果从一个抽象类继承，并想创建该新类的对象时，那么必须为基类中的所有抽象方法提供方法定义。如果不这样做(可以选择不这样做)，那么导出类也便是抽象类，而且编译器会强制用abstract关键字来限定这个类。

我们也可以创建一个没有任何抽象方法的抽象类。如果我们这样的一个类中，抽象方法都没有意义，并且我们想阻止产生该类的任何对象就可以用到没有任何抽象方法的抽象类来定义。

实例代码：

    package org.fmz.interfaces ;

        abstract class PureAbstract{
            abstract void f() ;
            abstract void g() ;
            public static void main(String args[]){
                PureAbstract pa = new PureAbstract() ;
            }
        }

> 纯粹的抽象类(抽象类中全部的方法都是抽象方法)不能创建对象

	package org.fmz.interfaces ;

	abstract class ImpureAbstract{
		abstract void f() ;
		abstract void g() ;
		void h(){
			
		}
		public static void main(String args[]){
			ImpureAbstract ia = new ImpureAbstract() ;
		}
	}

> - 不纯粹的抽象类(抽象类中的方法不全是抽象方法，甚至可以全部不是抽象方法)也不能创建对象
- 抽象类可以含有自己的构造器

<h3 id="9.2">9.2 接口</h3>

一个接口表示：所有实现了该特定接口的类看起来都像这样。因此接口是建立类与类之间的协议(protocol)，如果一个类实现了一个接口，那么这个类就知道可以调用接口中的哪些方法，而且仅仅知道这些。

interface也具有权限，但是只有public和默认权限。如果是默认权限，则仅仅限于在同一个包中的实现。

接口也可以包含域，这些域都是隐式的static和finial。

接口中的方法都是隐式的public，否则，它们将只能得到默认包的访问权限，这样在方法被继承的过程中，其可访问权限就被降低了，这是编译器所不允许的。

<h3 id="9.3">9.3 完全解耦</h3>

![完全解耦类关系图](../image/dip-4.png "类关系图")

![Processor类关系图](../image/dip-1.png "类关系图")

示例代码：

	package org.fmz.interfaces ;

	import java.util.* ;

	class Processor{
		public String name(){
			return getClass().getSimpleName() ;//Object类中的方法，其他类默认继承
		}
		Object process(Object input){
			return input ;
		}
	}

	class Upcase extends Processor{
		String process(Object input){//covariant return
			return ((String)input).toUpperCase() ;
		}
	}

	class Downcase extends Processor{
		String process(Object input){//covariant return
			return ((String)input).toLowerCase() ;
		}
	}

	class Splitter extends Processor{
		String process(Object input){//coveriant return
			return Arrays.toString(((String)input).split(" ")) ;//将字符串数组转化为一个可打印输出的数组
		}
	}

	public class Apply{
		public static void process(Processor p, Object s){
			System.out.println("Using Processor " + p.name()) ;
			System.out.println(p.process(s)) ;
		}

		public static String s = "Disagreement with beliefs is by definition incorrect" ;//静态域

		public static void main(String args[]){
			process(new Upcase(),s) ;
			process(new Downcase(),s) ;
			process(new Splitter(),s) ;
		}
	}/* Output:
	Using Processor Upcase
	DISAGREEMENT WITH BELIEFS IS BY DEFINITION INCORRECT
	Using Processor Downcase
	disagreement with beliefs is by definition incorrect
	Using Processor Splitter
	[Disagreement, with, beliefs, is, by, definition, incorrect]
	*/

> - 像本例这样，创建一个能够根据所传递的参数对象的不同而具有不同的行为的方法，被称为：策略模式。策略就是传递进去的参数对象，Processor对象就是一个策略。
- split()方法是String类的一部分，接收String类的对象，并以传递进来的参数作为边界，将该String对象分隔开，然后返回一个数组String[]
- java编译器无法从静态方法的上下文中引用非静态的变量
- 静态域和栈都是只有一份储存空间，其中的内容可以共享，不存在引用，每一个域都有一个名称，所有的成员处在一个空间中；堆中每个对象都有自己的空间，且其引用都是在栈中存在，对象之间不能共享，一旦对象发生改变，则引用就发生了改变。
- 静态域是针对类来说的，对于一个类来说只有声明的那一个静态域；而对于实例域来说，每一个对象都有其一份拷贝；产生多少个对象就有多少个实例域，而不管产生多少对象，静态域只有一个。

上述代码耦合的过紧，当我们想复用Apply.process()的时候，无法进行复用. Apply.process(Processor p, Object s)的参数Processor是一个具体类,如果要复用,必须复用Processor对象本身或者其子类对象,如果一个类和Processor相似,但是并不在Processor的继承结构中,也就没办法复用了.接口能很好的解决这样的问题,因为接口是多实现关系,而继承是单继承,如果这个类已经继承了其他类,就没办法在继承Processor类了,这样就无法复用.

![Processor解耦类关系图](../image/dip-1.1.png "类关系图")

可以用下面的办法解决：

	package org.fmz.interfaces ;

	public interface Processor{
		String name() ;
		Object process(Object input) ;
	}

> Processor.java，将Processor定义为接口

	package org.fmz.interfaces ;

	public class Apply{
		public static void process(Processor p, Object s){
			System.out.println("Using Processor " + p.name()) ;
			System.out.println(p.process(s)) ;
		}
	}

> Apply.java，能进行复用的静态方法独立出来为一个类。

	package org.fmz.interfaces ;

	import java.util.* ;

	public abstract class StringProcessor implements Processor{
		public String name(){
			return getClass().getSimpleName() ;
		}
		public abstract String process(Object input) ;
		public static String s = "Disagreement with beliefs is by definition incorrect" ;
		public static void main(String args[]){
			Apply.process(new Upcase(),s) ;
			Apply.process(new Downcase(),s) ;
			Apply.process(new Splitter(),s) ;
		}
	}

	class Upcase extends StringProcessor{
		public String process(Object input){//covariant return
			return ((String)input).toUpperCase() ;
		}
	}

	class Downcase extends StringProcessor{
		public String process(Object input){//covariant return
			return ((String)input).toLowerCase() ;
		}
	}

	class Splitter extends StringProcessor{
		public String process(Object input){//coveriant return
			return Arrays.toString(((String)input).split(" ")) ;//将字符串数组转化为一个可打印输出的数组
		}
	}

> 一个抽象类类实现接口，并且由其他类来继承这个抽象类，这样就可以复用Apply.process()方法了。

![适配器类关系图](../image/dip-2.png "类关系图")

如果对类无法进行修改就需要一个适配器，这样的模式叫做适配器模式，适配器将接受你所拥有的接口，并产生你所需要的接口，示例代码：

	package org.fmz.interfaces ;

	public class WaveForm{
		private static long counter ;
		private final long id = counter++ ;
		public String toString(){
			return "WaveForm" + id ;
		}
	}

> WaveForm.java，辅助类

	package org.fmz.interfaces ;

	public class Filter{
		public String name(){
			return getClass().getSimpleName() ;
		}
		public WaveForm process(WaveForm input){
			return input ;	
		}
	}

> Filter.java，接口类或者说是基类

	package org.fmz.interfaces ;

	public class LowPass extends Filter{
		double cutoff ;
		public LowPass(double cutoff){
			this.cutoff = cutoff ;
		}
		public WaveForm process(WaveForm input){
			return input ;	
		}
	}

> LowPass.java，子类或者说是实现类

	package org.fmz.interfaces ;

	public class HighPass extends Filter{
		double cutoff ;
		public HighPass(double cutoff){
			this.cutoff = cutoff ;
		}
		public WaveForm process(WaveForm input){
			return input ;	
		}
	}

> HighPass.java，子类

	package org.fmz.interfaces ;

	public class BandPass extends Filter{
		double lowCutoff, highCutoff ;
		public BandPass(double lowCutoff, double highCutoff){
			this.lowCutoff = lowCutoff ;
			this.highCutoff = highCutoff ;
		}
		public WaveForm process(WaveForm input){
			return input ;	
		}
	}

> BandPass.java，子类

![适配器类关系图](../image/dip-3.png "单例模式")

	package org.fmz.interfaces ;

	class FilterAdapter implements Processor{
		Filter filter ;
		public FilterAdapter(Filter filter){
			this.filter = filter ;
		}
		public String name(){
			return filter.name() ;
		}
		public WaveForm process(Object input){
			return filter.process((WaveForm)input) ;
		}
	}

	public class FilterProcessor{
		public static void main(String args[]){
			WaveForm w = new WaveForm() ;
			Apply.process(new FilterAdapter(new LowPass(1.0)), w) ;
			w = new WaveForm() ;
			Apply.process(new FilterAdapter(new HighPass(2.0)), w) ;
			w = new WaveForm() ;
			Apply.process(new FilterAdapter(new BandPass(3.0, 4.0)), w) ;
		}
	}/* Output:
		Using Processor LowPass
		WaveForm0
		Using Processor HighPass
		WaveForm1
		Using Processor BandPass
		WaveForm2
	*/

> - FilterProcessor.java，为了能够复用而创造的适配器，在适配器中实现接口，覆写方法，最重要的是复用代码。
-在FilterAdapter构造器中接收了你拥有的Filter，然后生成了你所需要的Processor接口对象，实际上还用到了代理。
-WaveForm中有一个静态的与：计数器，对于整个类来说不管产生多少对象都只有并且共享这样一个域，实现计数的功能。如果将static域改为实例域，输出的结果为：

	Using Processor LowPass
	WaveForm0
	Using Processor HighPass
	WaveForm0
	Using Processor BandPass
	WaveForm0

> 这个时候每一个WaveForm对象都有自己的计数器，因此都是0。

<h3 id="9.4">9.4 java中的多重继承</h3>

当要继承一个类和实现多个接口的时候，类必须写在前面，然后是实现接口，接口之间用`,`隔开。

使用接口的原因1(核心原因)：为了能够向上转型为多个基类型(以及由此而带来的灵活性)；原因2：与使用抽象基类的原因相同，防止客户端程序员创建该类的对象，并确保这仅仅是一个接口。

<h3 id="9.5">9.5 通过继承来扩展接口</h3>

接口与接口之间的扩展用关键字：`extends`，并且一个接口可以扩展(继承)多个接口，只需要用逗号隔开(不常见，extends一般用于单一类)，但是对于类来说是单继承。

#### 组合接口时名字的冲突

在打算组合的不同接口中使用相同的方法名通常会造成代码的可读性混乱，应该在开发中避免这种情况。

<h3 id="9.6">9.6 适配接口</h3>

接口最吸引人的原因之一就是允许同一个接口具有多个不同的具体实现。在简单的情况中，它的体现形式通常是一个接受接口类型的方法，而该接口的实现和向该方法传递的对象则取决于方法的使用者。

接口中常见的用法是策略设计模式，编写一个执行某种操作的方法，该方法将接受一个指定的接口，主要的声明是：可以用任何你想要的对象来调用我的方法，只要你对象遵循我的接口。这样方法就更加的灵活、通用，并且具有可复制性。

JavaSE的Scanner类的构造器就是就收的一个Readable接口。此Readable接口并没有作为java标准类库中其他任何方法的参数，它是单独为Scanner类创建的，使得Scanner类不必将参数限定为某个特定的类。通过这种方法Scanner可以作用于更多的类，如果你想创建一个类让Scanner作用于它，则必须让这个类称为Readable。实例代码：

	package org.fmz.interfaces ;

	import java.nio.* ;
	import java.util.* ;

	public class RandomWords implements Readable{
		private static Random rand = new Random(47) ;
		private static final char[] capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray() ;
		private static final char[] lowers = "abcdefghijklmnopqrstuvwxyz".toCharArray() ;
		private static final char[] vowels = "aeiou".toCharArray() ;
		private int count ;
		public RandomWords(int count){
			this.count = count ;
		}
		public int read(CharBuffer cb){
			if(count-- == 0){
				return -1 ;
			}
			cb.append(capitals[rand.nextInt(capitals.length)]) ;
			for(int i=0;i<4;i++){
				cb.append(vowels[rand.nextInt(vowels.length)]) ;
				cb.append(lowers[rand.nextInt(lowers.length)]) ;
			}
			cb.append(" ") ;
			return 10 ;
		}
		public static void main(String args[]){
			Scanner s = new Scanner(new RandomWords(10)) ;//默认状态下以空格进行split
			while(s.hasNext()){
				System.out.println(s.next()) ;
			}
		}
	}

如果你有一个还未实现Readable的类，没有实现Readable，怎么才能让它作用于Scanner呢？未实现Readable接口的类：

	package org.fmz.interfaces ;

	import java.nio.* ;
	import java.util.* ;

	public class AdaptRandomDoubles extends RandomDoubles implements Readable{
		private int count ;
		public AdaptRandomDoubles(int count){
			this.count = count ;
		}
		public int read(CharBuffer cb){
			if(count-- == 0){
				return -1 ;
			}
			String result = Double.toString(next()) + " " ;
			cb.append(result) ;
			return result.length() ;
		}

		public static void main(String args[]){
			Scanner s = new Scanner(new AdaptRandomDoubles(20)) ;
			while(s.hasNextDouble()){
				System.out.print(s.nextDouble() + " ") ;
			}
		}
	}

> 利用适配器的方式，我们可以在任何现有的类之上添加新的接口，即可以让方法就收接口的类型，这是使用接口，而不是类的强大之处。

<h3 id="9.7">9.7 接口中的域</h3>

放入接口中的任何域都自动(隐式的)是static和final的。

#### 初始化接口中的域

实例代码：

package org.fmz.interfaces ;

	import java.util.* ;

	public interface RandVals{
		Random RAND = new Random(47) ;
		int RAND_INT = RAND.nextInt(10) ;
		long RAND_LONG = RAND.nextLong() * 10 ;
		float RAND_FLOAT = RAND.nextFloat() * 10 ;
		double RAND_DOUBLE = RAND.nextDouble() * 10 ;
	}

> 既然是static的，它们在类第一次被加载的时候进行初始化，这发生在任何域首次被访问时。

测试代码：

	package org.fmz.interfaces ;

	public class TestRandVals{
		public static void main(String args[]){
			System.out.println(RandVals.RAND_INT) ;
			System.out.println(RandVals.RAND_LONG) ;
			System.out.println(RandVals.RAND_FLOAT) ;
			System.out.println(RandVals.RAND_DOUBLE) ;
		}
	}

> 这些域不是接口的一部分(能够被覆写的，向上转型的才是接口的一部分。而接口中的常量是static和final的)，它们被储存在接口的静态储存区域中。

<h3 id="9.8">9.8 嵌套接口</h3>

<h3 id="9.9">9.9 接口与工厂</h3>

工厂方法模式示例：

	package org.fmz.interfaces ;

	interface Service{
		void method1() ;
		void method2() ;
	}

	interface ServiceFactory{
		Service getService() ;
	}

	class Implementation1 implements Service{
		Implementation1(){}
		public void method1(){
			System.out.println("Implementation1 method1") ;
		}
		public void method2(){
			System.out.println("Implementation1 method2") ;
		}
	}

	class ImplementationFactory1 implements ServiceFactory{
		public Service getService(){
			return new Implementation1() ;
		}

	}

	class Implementation2 implements Service{
		Implementation2(){}
		public void method1(){
			System.out.println("Implementation2 method1") ;
		}
		public void method2(){
			System.out.println("Implementation2 method2") ;
		}
	}

	class ImplementationFactory2 implements ServiceFactory{
		public Service getService(){
			return new Implementation2() ;
		}

	}

	public class Factory{
		public static Service serviceCustomer(ServiceFactory sf){
			return sf.getService() ;
		}

		public static void main(String args[]){
			ServiceFactory sf = new ImplementationFactory1() ;
			serviceCustomer(sf).method1() ;
			serviceCustomer(sf).method2() ;
			sf = new ImplementationFactory2() ;
			serviceCustomer(sf).method1() ;
			serviceCustomer(sf).method2() ;
		}
	}/* Output:
	Implementation1 method1
	Implementation1 method2
	Implementation2 method1
	Implementation2 method2
	*/

> 如果不是工厂方法模式，你就必须在某处要创建的Service的确切类型，以便调用合适的构造器。

为什么我们要添加这些额外级别的间接性呢？一个常见的原因就是想创建框架。一个对弈游戏框架的示例代码：

	package org.fmz.interfaces ;

	interface Game{
		boolean move() ;
	}

	interface GameFactory{
		Game getGame() ;
	}

	class Checkers implements Game{//西洋跳棋
		private int moves ;
		private static final int MOVES = 3 ;
		public boolean move(){
			System.out.println("Checkers move" + moves) ;
			return ++moves != MOVES ;
		}
	}

	class CheckersFactory implements GameFactory{
		public Game getGame(){
			return new Checkers() ;
		}
	}

	class Chess implements Game{//国际象棋
		private int moves = 0 ;
		private static final int MOVES = 4 ;
		public boolean move(){
			System.out.println("Chess move" + moves) ;
			return ++moves != MOVES ;
		}
	}

	class ChessFactory implements GameFactory{
		public Game getGame(){
			return new Chess() ;
		}
	}

	public class Games{
		public static void playGame(GameFactory factory){
			Game s = factory.getGame() ;
			while(s.move()){
				;
			}
		}

		public static void main(String args[]){
			playGame(new CheckersFactory()) ;
			playGame(new ChessFactory()) ;
		}
	}

> 如果Gmaes表示一段很复杂的代码，那么这种方式就允许你在不同的游戏中复用这段代码，妙极！

总结：

1. 对于创建类，几乎在任何时刻，都可以替代为创建一个接口和一个工厂。
2. 任何的抽象性都是应该在真正的需求而产生的。当必须时候，应该重构接口而不是到处添加额外级别的间接性，并且由此带来的复杂性。
3. 恰当的原则是优先选择类而不是接口，从类开始，如果接口的必要性变得非常明确，那么就进行重构。接口是一种重要的工具，很容易被滥用。

---

# Continue...

<h2 id="10">10 内部类</h2>

将一个类的定义放在另一个类的定义内部就是内部类。

<h3 id="10.1">10.1 创建内部类</h3>

示例代码：

	package org.fmz.inner ;

	public class Parcel1{
		class Contents{
			private int i = 11 ;
			public int value(){
				return i ;
			}
		}

		class Destination{
			private String label ;
			Destination(String whereTo){
				label = whereTo ;
			}
			String readLabel(){
				return label ;
			}
		}

		public void ship(String dest){
			Contents c = new Contents() ;
			Destination d = new Destination(dest) ;
			System.out.println(c.value()) ;
			System.out.println(d.readLabel()) ;
		}

		public static void main(String args[]){
			Parcel1 p = new Parcel1() ;
			p.ship("Tasmania") ;
		}
	}

如果外部类中有一个方法，该方法返回一个指向内部类的引用。示例代码：

	package org.fmz.inner ;

	public class Parcel2{
		class Contents{
			private int i = 11 ;
			public int value(){
				return i ;
			}
		}

		class Destination{
			private String label ;
			Destination(String whereTo){
				label = whereTo ;
			}
			String readLabel(){
				return label ;
			}
		}

		public Contents contents(){
			System.out.println("create new Contents") ;
			return new Contents() ;
		}

		public Destination to(String s){
			System.out.println("create new Destination") ;
			return new Destination(s) ;
		}

		public void ship(String dest){
			Contents c = new Contents() ;
			Destination d = new Destination(dest) ;
			System.out.println(c.value()) ;
			System.out.println(d.readLabel()) ;
		}

		public static void main(String args[]){
			Parcel2 p = new Parcel2() ;
			p.ship("Tasmania") ;

			Parcel2 q = new Parcel2() ;
			Contents c = q.contents() ;
			//Parcel2.Contents c = q.contents() ;
			Destination d = q.to("chengdu") ;
			//Parcel2.Destination d = q.to("chengdu") ;
		}
	}

>此处与书中说的不一样，对于内部类的引用不必明确的指明对象的类型：`OuterClassName.InnerClassName`

<h3 id="10.2">10.2 链接到外部类</h3>

示例代码：

	package org.fmz.inner ;

	interface Selector{
		boolean end() ;
		Object current() ;
		void next() ;
	}

	public class Sequence{
		private Object[] items ;
		private int next = 0 ;
		public Sequence(int size){
			items = new Object[size] ;
		}
		public void add(Object x){
			if(next < items.length){
				items[next++] = x ;
			}
		}

		private class SequenceSelector implements Selector{
			private int i = 0 ;
			public boolean end(){
				return i == items.length ;
			}
			public Object current(){
				return items[i] ;
			}
			public void next(){
				if(i < items.length){
					i ++ ;
				}
			}
		}

		public Selector selector(){
			return new SequenceSelector() ;
		}

		public static void main(String args[]){
			Sequence sequence = new Sequence(10) ;
			for(int i=0;i<10;i++){
				sequence.add(Integer.toString(i)) ;
			}
			Selector selector = sequence.selector() ;//外部类对象创建一个内部类对象，二者发生关联
			while(!selector.end()){
				System.out.print(selector.current() + " ") ;
				selector.next() ;
			}
		}
	}

>程序中的SequenceSelector是一个内部类，然而可以访问外部类中的私有字段，如本程序中的`items`

所有的内部类自动拥有对其外围类的所有成员的访问权。这是怎么做到的呢？当一个外围类的对象创建一个内部类的对象时，此内部类对象就秘密的捕获一个指向那个外围类对象的引用。当访问外围类的成员的时候，就用这个引用访问，仿佛像是在访问自己的一样。

<h3 id="10.3">10.3 使用.this与.new</h3>

如果你想生成对外部类对象的引用，可以使用外部类的名字后面紧跟圆点和this，这样产生的引用就自动具有了正确的类型。示例代码：

	package org.fmz.inner ;

	public class DotThis{
		void f(){
			System.out.println("DotThis.f()") ;
		}
		public class Inner{
			public DotThis outer(){
				return DotThis.this ;//产生外部类的一个引用
			}
		}
		public Inner inner(){
			return new Inner() ;
		}
		public static void main(String args[]){
			DotThis dotthis = new DotThis() ;
			Inner inner = dotthis.inner() ;
			inner.outer().f() ;
		}
	}

有时候可能想告知某些其他对象，去创建其某个内部类对象。必须在new表达式中提供对其外部类对象的引用。示例代码：

	package org.fmz.inner ;

	public class DotNew{
		public class Inner{
		}
		public static void main(String args[]){
			DotNew dn = new DotNew() ;
			Inner inner = dn.new Inner() ;
		}
	}

>在拥有外部类对象之前是不可能创建内部类对象的。这是因为内部类对象会暗暗的连接到创建它的外部类对象上。

<h3 id="10.4">10.4 内部类与向上转型</h3>

实例代码：

	package org.fmz.inner ;

	interface Destination{
		String readLabel() ;
	}

	interface Contents{
		int value() ;
	}

	class Parcel4{
		private class PContents implements Contents{
			private int i = 11 ;
			public int value(){
				return i ;
			}
		}

		protected class PDestination implements Destination{
			private String label ;
			private PDestination(String whereTo){
				label = whereTo ;
			}
			public String readLabel(){
				return label ;
			}
		}

		public Destination destination(String s){
			return new PDestination(s) ;
		}

		public Contents contents(){
			return new PContents() ;
		}
	}

	public class TestParcel{
		public static void main(String args[]){
			Parcel4 p = new Parcel4() ;
			Contents c = p.contents() ;
			Destination d = p.destination("chengdu") ;
		}
	}

>内部类能够实现更加高效的实现隐藏。

<h3 id="10.5">10.5 在方法和作用域中的内部类</h3>

<h3 id="10.6">10.6 匿名内部类</h3>

示例代码：

	package org.fmz.inner ;

	public class Parcel7{
		public Contents contents(){
			return new Contents(){
				private int i = 11 ;
				public int value(){
					return i ;
				}
			};//需要分号
		}

		public static void main(String args[]){
			Parcel7 p = new Parcel7() ;
			Contents c = p.contents() ;
		}
	}

>这种语法看起来很奇怪，contents()方法的返回值与表示这个返回值的类的定义结合在一起。这个类是匿名的。

>这种奇怪的语法是：创建一个继承自Contents的匿名类对象，通过new表达式返回的引用被自动向上转型为Contents的引用，上述语法是下述代码的简化形式：

	public class Parcel7{
		class MyContents implements Contents{
			private int i = 11 ;
			public int value(){
				return i ;
			}
		}
		public Contents contents(){
			return new MyContents() ;
		}
		public static void main(String args[]){
			Parcel7 p = new Parcel7() ;
			Contents c = p.contents() ;
		}
	}

>其实就是一种简化的形式。

#### 再访工厂方法

优雅的工厂模式

	package org.fmz.inner ;

	interface Service{
		void method1() ;
		void method2() ;
	}

	interface ServiceFactory{
		Service getService() ;
	}

	class Implementation1 implements Service{
		private Implementation1(){}
		public void method1(){
			System.out.println("Implementation1 method1") ;
		}
		public void method2(){
			System.out.println("Implementation1 method2") ;
		}
		public static ServiceFactory factory =//通常是一个接口
			new ServiceFactory(){
				public Service getService(){
					return new Implementation1() ;
				}
		};//内部类
	}

	class Implementation2 implements Service{
		private Implementation2(){}
		public void method1(){
			System.out.println("Implementation2 method1") ;
		}
		public void method2(){
			System.out.println("Implementation2 method2") ;
		}
		public static ServiceFactory factory =
			new ServiceFactory(){
				public Service getService(){
					return new Implementation2() ;
				}
		};//内部类
	}

	public class Factory{
		public static Service serviceCustomer(ServiceFactory sf){
			return sf.getService() ;
		}

		public static void main(String args[]){
			ServiceFactory sf = Implementation1.factory ;
			serviceCustomer(sf).method1() ;
			serviceCustomer(sf).method2() ;
			sf = Implementation2.factory ;
			serviceCustomer(sf).method1() ;
			serviceCustomer(sf).method2() ;
		}
	}/* Output:
		Implementation1 method1
		Implementation1 method2
		Implementation2 method1
		Implementation2 method2
	*/

<h3 id="10.7">10.7 嵌套类</h3>

<h3 id="10.8">10.8 为什么要使用内部类</h3>

可以认为：内部类提供了某种进入其外围类的窗口

使用内部类最吸引人的原因是：每个内部类都能独立的继承一个(接口的)实现，所以无论外围类是否已经继承了(接口的)实现，对于内部类都没有影响。

内部类有效的实现了多重继承，也就是说，内部类允许继承多个非接口类型(类或者抽象类)

必须在一个类中以某种方式实现两个接口，有两种方法，使用单一类或者内部类

	package org.fmz.inner ;

	interface A{

	}

	interface B{

	}
	//单一类的模式
	class X implements A, B{

	}
	//内部类的模式
	class Y implements A{
		B b = new B(){};
	}

	public class MultiInterfaces{

	}

>上述实现的是两个接口，实际上并没有多大的区别

如果上面的接口是抽象的类或者具体的类，那么久只能使用内部类才能实现多重继承

	package org.fmz.inner ;

	class D{

	}

	abstract class E{

	}

	class Z extends D{
		E e = new E(){
		};
	}

	public class MultiImplementation{

	}

>从而解决了java中的多重继承的问题

<h3 id="10.9">10.9 内部类的继承</h3>

待续...

<h3 id="10.10">10.10 内部类可以被覆盖吗？</h3>

待续...

<h3 id="10.11">10.11 局部内部类</h3>

待续...

<h3 id="10.12">10.12 内部类的标识符</h3>

待续...

***

***

<h2 id="11">11 持有对象</h2>

如果一个程序只包含固定数量且声明周期已知的对象，那么这是一个非常简单的程序。

<h3 id="11.1">11.1 泛型和类型安全容器</h3>

第一个简单的泛型示例：

	package org.fmz.holding ;

	import java.util.* ;

	class Apple{
		private static long counter ;
		private final long id = counter ++ ;
		public long id(){
			return id ;
		}
	}

	public class AppleAndOrangeWithoutGenerics{
		public static void main(String args[]){
			ArrayList<Apple> apples = new ArrayList<Apple>() ;
			for(int i=0;i<10;i++){
				apples.add(new Apple()) ;
			}
			for(int i=0;i<apples.size();i++){
				System.out.println(apples.get(i).id()) ;
			}
			for(Apple apple : apples){
				System.out.println(apple.id()) ;
			}
		}
	}

>上述的代码能自动的生成一个计数器。

<h3 id="11.2">11.2 基本概念</h3>

1. Collection。一个独立元素的序列，这些元素都服从一条或者多条规则。List必须按照插入的顺序保存元素，而Set不能有重复元素。Queue按照排队规则来确定对象产生的顺序(通常与它被插入的顺序相同)。

2. Map。一组成对的"键值对"对象，允许使用键来查找值。ArrayList允许你使用数字来查找值，因此在某种意义上来讲，将数字与对象关联在一起。映射表允许我们使用另外一个对象来查找某个对象，它也被称为"关联数组"。

简单的一个Integer对象填充Collection接口：

	package org.fmz.holding ;

	import java.util.* ;

	public class SimpleCollection{
		public static void main(String args[]){
			Collection<Integer> c = new ArrayList<Integer>() ;
			for(int i=0;i<100;i++){
				c.add(i);//Autoboxing
			}
			for(Integer i : c){
				System.out.print(i + " ") ;
			}
		}
	}

<h3 id="11.3">11.3 添加一组元素</h3>

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class AddingGroups{
		public static void main(String args[]){
			Collection<Integer> collection = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5)) ;
			Integer[] moreInts = {5,6,7,8,9,10} ;
			collection.addAll(Arrays.asList(moreInts)) ;
			Collections.addAll(collection,11,12,13,14,15,16) ;
			Collections.addAll(collection,moreInts) ;
			List<Integer> list = Arrays.asList(16,17,18,19,20) ;
			list.set(1,99) ;
			//list.add(21) ;
			for(Integer i : collection){
				System.out.print(i + "，") ;
			}
			System.out.println("*******************") ;
			
			for(Integer i : list){
				System.out.print(i + "，") ;
			}
		}
	}

>Collection构造器可以接收另外一个Collection，用它来将自身初始化

>Collection.addAll()成员只能就收另外一个Collection对象作为参数，因此不如Arrays.asList()或Collections.addAll()灵活，这两个方法都使用的是可变参数列表。

>可以直接使用Arrays.asList()的输出，当其为List，但是在这种情况下，其底层表示的是数组，因此不能调整尺寸。

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	class Snow{
	}

	class Powder extends Snow{
	}

	class Light extends Powder{
	}

	class Heavy extends Powder{
	}

	class Crusty extends Snow{
	}

	class Slush extends Snow{
	}

	public class AsListInterface{
		public static void main(String args[]){
			List<Snow> snow1 = Arrays.asList(new Powder(), new Crusty(), new Slush()) ;
			List<Snow> snow2 = Arrays.asList(new Light(), new Heavy()) ;
		}
	}

<h3 id="11.4">11.4 容器的打印</h3>

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class PrintingContainers{
		static Collection fill(Collection<String> collection){
			collection.add("rat") ;
			collection.add("cat") ;
			collection.add("dog") ;
			collection.add("dog") ;
			return collection ;
		}

		static Map fill(Map<String, String> map){
			map.put("rat", "Fuzzy") ;
			map.put("cat", "Rags") ;
			map.put("dog", "Bosco") ;
			map.put("dog", "Spot") ;
			return map ;
		}

		public static void main(String args[]){
			System.out.println(fill(new ArrayList<String>())) ;
			System.out.println(fill(new LinkedList<String>())) ;
			System.out.println(fill(new HashSet<String>())) ;
			System.out.println(fill(new TreeSet<String>())) ;
			System.out.println(fill(new LinkedHashSet<String>())) ;
			System.out.println(fill(new HashMap<String, String>())) ;
			System.out.println(fill(new TreeMap<String, String>())) ;
			System.out.println(fill(new LinkedHashMap<String, String>())) ;
		}
	}

Collection在每个槽中只能保存一个元素。此类容器包括：List，以特定的顺序保存元素；Set，元素不能重复；Queue，只能在元素的一端插入元素，在容器的另一端移除元素。ArrayList和LinkedList都是按照插入元素的顺序保存元素，二者的不同是执行某种类型的操作时的性能的不同。而且LinkedList包含的操作也多于ArrayList。

HashSet、TreeSet和LinkedHashSet都是Set类型，每一个相同项只能保存一次。HashSet使用想当复杂的方式来储存元素；TreeSet按照特定的顺序来储存元素(比较结果的升序保存对象)；LinkHashSet是按照添加的顺序来保存对象。

Map也有三种风格：HashMap、TreeMap和LinkedHashMap，同Set接口类似，分别按照复杂技术、升序排列和插入顺序进行保存。

<h3 id="11.5">11.5 List</h3>

示例代码：

	package org.fmz.holding ;

	import java.util.* ;
	import typeinfo.pets.* ;

	public class ListFeatures{
		public static void main(String args[]){
			Random rand = new Random(47) ;
			List<Pet> pets = Pets.arrayList(7) ;
			System.out.println("1：" + pets) ;
			Hamster h = new Hamster() ;
			pets.add(h) ;//Automatically resizes
			System.out.println("2: " + pets) ;
			System.out.println("3: " + pets.contains(h)) ;
			pets.remove(h) ;
			Pet p = pets.get(2) ;
			System.out.println("4: " + p + " " + pets.indexOf(p)) ;
			Pet cymric = new Cymric() ;
			System.out.println("5: " + pets.indexOf(cymric)) ;
			System.out.println("6: " + pets.remove(cymric)) ;
			//必须是准确的对象
			System.out.println("7: " + pets.remove(p)) ;
			System.out.println("8: " + pets) ;
			pets.add(3, new Mouse()) ;
			System.out.println("9: " + pets) ;
			List<Pet> sub = pets.subList(1,4) ;
			System.out.println("Sublist: " + sub) ;
			System.out.println("10: " + pets.containsAll(sub)) ;
			Collections.sort(sub) ;
			System.out.println("sorted sublist: " + sub) ;
			System.out.println("11: " + pets.containsAll(sub)) ;//来说containsAll()，参数中元素的顺序不重要
			Collections.shuffle(sub,rand) ;
			System.out.println("Shuffled Sublist: " + sub) ;
			System.out.println("12: " + pets.containsAll(sub)) ;
			List<Pet> copy = new ArrayList<Pet>(pets) ;
			sub = Arrays.asList(pets.get(1), pets.get(4)) ;
			System.out.println("Sub: " + sub) ;
			copy.retainAll(sub) ;//交集的行为
			System.out.println("13: " + copy) ;
			copy = new ArrayList<Pet>(pets) ;
			copy.remove(2) ;
			System.out.println("14: " + copy) ;
			copy.removeAll(sub) ;//补集的行为
			System.out.println("15: " + copy) ;
			copy.set(1, new Mouse()) ;
			System.out.println("16: " + copy) ;
			copy.addAll(2, sub) ;
			System.out.println("17: " + copy) ;
			System.out.println("18: " + pets.isEmpty()) ;
			pets.clear() ;
			System.out.println("19: " + pets) ;
			System.out.println("20: " + pets.isEmpty()) ;
			pets.addAll(Pets.arrayList(4)) ;//并集的行为
			System.out.println("21: " + pets) ;
			Object[] o = pets.toArray() ;
			System.out.println("22: " + o[3]) ;
			Pet[] pa = pets.toArray(new Pet[0]) ;
			System.out.println("23: " + pa[3].id()) ;
		}
	}

<h3 id="11.6">11.6 迭代器</h3>

迭代器(是一种设计模式)是一个对象，它的工作是遍历并选择序列中的对象，而客户端程序员不必知道或者关心该程序底层的结构。此外，迭代器通产被称为轻量级对象；创建它的代价小。Java的Iterator只能单向移动。

简单的迭代器，示例代码：

	package org.fmz.holding ;

	import java.util.* ;
	import typeinfo.pets.* ;

	public class SimpleIteration{
		public static void main(String args[]){
			List<Pet> pets = Pets.arrayList(12) ;
			Iterator<Pet> it = pets.iterator() ;
			while(it.hasNext()){
				Pet p = it.next() ;
				System.out.print(p.id() + ":" + p + " ") ;
			}
			System.out.println() ;

			for(Pet p : pets){
				System.out.print(p.id() + ":" + p + " ") ;
			}
			System.out.println() ;
			it = pets.iterator() ;//必须要生成新的迭代器
			for(int i=0;i<6;i++){
				it.next() ;
				it.remove() ;//移除由next()产生的最后一个元素，在调用remove()之前必须先调用next()方法
			}
			System.out.println(pets) ;
		}
	}

>如果你只是向前遍历List，并不打算修改List对象本身，那么利用forEach会更加的简洁。

>每一个迭代器都是一个对象，如果对迭代器完成不同的操作(尤其是修改迭代器的时候)，要生成一个新的迭代器。

使用容器，必须对容器的确切类型进行编程。但是，如果原本是对List进行编码的，但是后来发现如果能够把相同的代码应用于Set，将会显得非常方便。如果我们一开始就编写的是通用编码，他们只是使用容器，而不关心容器的类型，这样就可以实现代码的复用了，这些是要通过迭代器来完成。

示例代码：

	package org.fmz.holding ;

	import java.util.* ;
	import typeinfo.pets.* ;

	public class CrossContainerIteration{
		public static void display(Iterator<Pet> it){
			while(it.hasNext()){
				Pet p = it.next() ;
				System.out.println(p.id() + ":" + p + " ") ;
			}
			System.out.println() ;
		}

		public static void main(String args[]){
			ArrayList<Pet> pets = Pets.arrayList(8) ;
			LinkedList<Pet> petsll = new LinkedList<Pet>(pets) ;
			HashSet<Pet> petshs = new HashSet<Pet>(pets) ;
			TreeSet<Pet> petsts = new TreeSet<Pet>(pets) ;
			display(pets.iterator()) ;
			display(petsll.iterator()) ;
			display(petshs.iterator()) ;
			display(petsts.iterator()) ;
		}
	}

>display()方法中不包含任何有关遍历序列的类型信息。从而也展示了Iterator的威力：能够将遍历序列的操作与序列底层的结构分离。

#### ListIterator

Iterator迭代器一旦创建，表示返回序列的第一个元素，并且是从第一个元素单向向前移动。

ListIterator是Iterator的子类型(接口)，它只能用于各种List类的访问。ListIterator可以双向移动，还可以产生相对于迭代器在列表中指向的当前位置的前一个和后一个元素的索引，可以使用set()方法，替换使用过的最后一个元素，可以利用listIterator()方法产生一开始就指向列表索引为n的元素处的Iterator。实例代码：

	package org.fmz.holding ;

	import java.util.* ;
	import typeinfo.pets.* ;

	public class ListIteration{
		public static void main(String args[]){
			List<Pet> pets = Pets.arrayList(8) ;
			ListIterator<Pet> it = pets.listIterator() ;
			while(it.hasNext()){
				System.out.print(it.next() + ", " + it.nextIndex() + ", " + it.previousIndex() + "; ") ;
			}
			System.out.println() ;

			while(it.hasPrevious()){//反序
				System.out.print(it.previousIndex() + ":" + it.previous() + "; ") ;
			}
			System.out.println() ;
			System.out.println(pets) ;
			it = pets.listIterator(3) ;
			while(it.hasNext()){
				it.next() ;
				it.set(Pets.randomPet()) ;//set()方法表示是是replace()的意思
			}
			System.out.println(pets) ;
		}
	}

<h3 id="11.7">11.7 LinkedList</h3>

LinkedList在执行中间插入和移除操作的时候，比ArrayList更加高效，但是随机访问操作方面要逊色一点。

示例代码：

	package org.fmz.holding ;

	import typeinfo.pets.* ;
	import java.util.* ;

	public class LinkedListFeatures{
		public static void main(String args[]){
			LinkedList<Pet> pets = new LinkedList<Pet>(Pets.arrayList(5)) ;
			System.out.println("pets: " + pets) ;
			System.out.println("pets.getFirst(): " + pets.getFirst()) ;//返回第一元素，列表为空时抛出NoSuchElementException
			System.out.println("pets.element(): " + pets.element()) ;//返回第一元素，列表为空时抛出NoSuchElementException
			System.out.println("pets.peek(): " + pets.peek()) ;//返回第一个元素，在列表为空时返回null
			System.out.println("pets: " + pets) ;
			System.out.println("pets.remove(): " + pets.remove()) ;//删除第一个元素(列表中的头)，列表为空时返回null
			System.out.println("pets: " + pets) ;
			System.out.println("pets.removeFirst(): " + pets.removeFirst()) ;//删除第一个元素，列表为空时抛出NoSuchElementException
			System.out.println("pets: " + pets) ;
			System.out.println("pets.poll(): " + pets.poll()) ;//返回并且删除第一个元素(列表中的头)
			System.out.println("pets: " + pets) ;
			pets.addFirst(new Rat()) ;//在列表中的头添加元素
			System.out.println("pets: " + pets) ;
			pets.offer(Pets.randomPet()) ;//在列表中的尾添加元素
			System.out.println("After offer(), pets: " + pets) ;
			pets.add(Pets.randomPet()) ;//在列表中的尾添加元素
			System.out.println("After add(), pets: " + pets) ;
			pets.addLast(Pets.randomPet()) ;//在列表中的尾添加元素
			System.out.println("After addLast(), pets: " + pets) ;
			System.out.println("pets.removeLast(): " + pets.removeLast()) ;//返回并且删除最后一个元素(列表中的尾)
			System.out.println("pets: " + pets) ;
			System.out.println("pets.remove(): " + pets.remove()) ;
			System.out.println("pets: " + pets) ;
		}
	}

>LinkedList默认的行为是：在列表的尾进行添加，add()、addLast()、offer()表示在列表的尾添加元素；在列表的头进行删除，remove()、removeFirst()表示在列表中的头进行删除；addFirst()表示在列头添加、removeLast()表示在类尾删除。

<h3 id="11.8">11.8 Stack</h3>

"栈"通常是一种后进先出(LIFO)的容器。定义自己的栈：

	package org.fmz.holding ;

	import java.util.* ;

	public class Stack<T>{
		private LinkedList<T> storage = new LinkedList<T>() ;
		public void push(T v){
			storage.addFirst(v) ;
		}
		public T peek(){
			return storage.getFirst() ;
		}

		public T pop(){
			return storage.removeFirst() ;
		}

		public boolean empty(){
			return storage.isEmpty() ;
		}

		public String toString(){
			return storage.toString() ;
		}
	}

>当要使用自己定义的栈的时候，需要指明包.类名称，因为会与java.util.Stack发生重名。

>在加载类的时候，如果没有具体的指定包.类名称，则先从本包中查找加载相应的类；如果有具体的import 包.类名称或者指定的包.类名称，则按照指定的加载。(当导入包.*时，还是优先从本包中查找可以加载的类)

	package org.fmz.holding ;

	public class StackTest{
		public static void main(String args[]){
			Stack<String> stack = new Stack<String>() ;
			for(String s : "My dog has fleas".split(" ")){
				stack.push(s) ;
			}
			while(!stack.empty()){
				System.out.print(stack.pop() + " ") ;
			}
		}
	}/* Output:
		*******************
		fleas *******************
		has *******************
		dog *******************
		My
	*/

>测试栈，从同一个包中查找，没有导入java.util包

	package org.fmz.holding ;

	import java.util.* ;

	public class StackTest{
		public static void main(String args[]){
			Stack<String> stack = new Stack<String>() ;
			for(String s : "My dog has fleas".split(" ")){
				stack.push(s) ;
			}
			while(!stack.empty()){
				System.out.print(stack.pop() + " ") ;
			}
		}
	}/* Output:
		*******************
		fleas *******************
		has *******************
		dog *******************
		My
	*/

>从输出结果上来看，还是寻找本包中的类加载(因为没有具体指定java.util.Stack)

	package org.fmz.holding ;

	import java.util.Stack ;

	public class StackTest{
		public static void main(String args[]){
			Stack<String> stack = new Stack<String>() ;
			for(String s : "My dog has fleas".split(" ")){
				stack.push(s) ;
			}
			while(!stack.empty()){
				System.out.print(stack.pop() + " ") ;

			}
		}
	}/* Output:
		fleas has dog My
	*/

>上述程序具体指定了java.util.Stack，则就加载具体的类。一句话概括，没有明确指明类型，则优先从本包中查找，然后从导入包中查找。

<h3 id="11.9">11.9 Set</h3>

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class SetOfInteger{
		public static void main(String args[]){
			Random rand = new Random(47) ;
			Set<Integer> intset = new HashSet<Integer>() ;
			for(int i=0;i<1000;i++){
				intset.add(rand.nextInt(30)) ;
			}
			System.out.println(intset) ;
		}
	}/* Output:
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
	*/

>此处HashSet也具有了排序的功能，输出的顺序有规律可循，但是书上的输出结果是没有规律可循的。

>HashSet使用了散列函数的元素储存方式；TreeSet将元素储存在红-黑树数据结构中；LinkedHashSet因为速度原因也使用了散列，但看起来像使用了链表来维护元素的插入顺序。

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class SetOperations{
		public static void main(String args[]){
			Set<String> set1 = new HashSet<String>() ;
			Collections.addAll(set1, "A B C D E F G H I J K L".split(" ")) ;
			set1.add("M") ;
			System.out.println("H: " + set1.contains("H")) ;
			System.out.println("N: " + set1.contains("N")) ;
			Set<String> set2 = new HashSet<String>() ;
			Collections.addAll(set2, "H I J K L".split(" ")) ;
			System.out.println("set2 in set1: " + set1.containsAll(set2)) ;
			set1.remove("H") ;
			System.out.println("Set1: " + set1) ;
			System.out.println("set2 in set1: " + set1.containsAll(set2)) ;
			set1.removeAll(set2) ;
			System.out.println("Set1: " + set1) ;
			Collections.addAll(set1, "X Y Z".split(" ")) ;
			System.out.println("Set1: " + set1) ;
		}
	}/* Output:
	H: true
	N: false
	set2 in set1: true
	Set1: [A, B, C, D, E, F, G, I, J, K, L, M]
	set2 in set1: false
	Set1: [A, B, C, D, E, F, G, M]
	Set1: [A, B, C, D, E, F, G, M, X, Y, Z]
	*/

>可以用来表示求出集合之间的交、并、补的关系

能够产生每一个元素都唯一的列表是相当有用的功能，示例代码：

	package org.fmz.holding ;

	import java.util.* ;
	import net.mindview.util.* ;

	public class UniqueWords{
		public static void main(String args[]){
			Set<String> words = new TreeSet<String>(new TextFile("SetOperations.java", "\\W+")) ;
			System.out.println(words) ;
		}
	}/* Output:
		[A, B, C, Collections, D, E, F, G, H, HashSet, I, J, K, L, M, N, Set, Set1, SetOperations, String, System, X, Y, Z, add,
		 addAll, args, class, contains, containsAll, fmz, holding, import, in, java, main, new, org, out, package, println, publ
		ic, remove, removeAll, set1, set2, split, static, util, void]
	*/

>这种排序是按照字典记性的，因此大小写被安排到了不同的组

	package org.fmz.holding ;

	import java.util.* ;
	import net.mindview.util.* ;

	public class UniqueWordsAlphabetic{
		public static void main(String args[]){
			Set<String> words = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) ;
			words.addAll(new TextFile("SetOperations.java", "\\W+")) ;
			System.out.println(words) ;
		}
	}/* Output:
		[A, add, addAll, args, B, C, class, Collections, contains, containsAll, D, E, F, fmz, G, H, HashSet, holding, I, import,
		 in, J, java, K, L, M, main, N, new, org, out, package, println, public, remove, removeAll, Set, set1, set2, SetOperatio
		ns, split, static, String, System, util, void, X, Y, Z]
	*/

>向构造器中传入了比较器，这样可以按照自定义的形式进行排序了。

<h3 id="11.10">11.10 Map</h3>

将对象映射到其他对象的能力是一种解决编程问题的杀手锏。

检验java的Random类的随机性，示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class Statistics{
		public static void main(String args[]){
			Random rand = new Random(47) ;
			Map<Integer, Integer> m = new HashMap<Integer, Integer>() ;
			for(int i=0;i<10000;i++){
				int r = rand.nextInt(20) ;
				Integer freq = m.get(r) ;
				m.put(r, freq == null ? 1 : freq+1) ;//这时候就显现出了抛出异常和值为null的不同之处了，如果这个时候抛出异常，则根本无法进行赋值
			}
			System.out.println(m) ;
		}
	}/* Output:
		{0=481, 1=502, 2=489, 3=508, 4=481, 5=503, 6=519, 7=471, 8=468, 9=549, 10=513, 11=531, 12=521, 13=506, 14=477, 15=497, 1
		6=533, 17=509, 18=478, 19=464}
	*/

使用containsKey()和containsValue()来测试一个Map，示例代码：

	package org.fmz.holding ;

	import typeinfo.pets.* ;
	import java.util.* ;

	public class PetMap{
		public static void main(String args[]){
			Map<String, Pet> petMap = new HashMap<String, Pet>() ;
			petMap.put("My Cat", new Cat("Molly")) ;
			petMap.put("My Dog", new Dog("Ginger")) ;
			petMap.put("My Hamster", new Hamster("Bosco")) ;
			System.out.println(petMap) ;
			Pet dog = petMap.get("My Dog") ;
			System.out.println(dog) ;
			System.out.println(petMap.containsKey("My Dog")) ;
			System.out.println(petMap.containsValue(dog)) ;//一定是一个对象的引用
		}
	}/* Output:
		{My Dog=Dog Ginger, My Cat=Cat Molly, My Hamster=Hamster Bosco}
		Dog Ginger
		true
		true
	*/

>将容器进行组合能够产生强大的数据结构。

<h3 id="11.11">11.11 Queue</h3>

队列是一种典型的先进先出(FIFO)的容器。即，从容器的一端放入事物，从容器的另一端取出，并且事物放入容器的顺序与事物取出顺序相同。

LinkedList类提供了方法，以支持队列的行为。并且LinkedList实现了Queue接口。

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class QueueDemo{
		public static void printQ(Queue queue){
			while(queue.peek() != null){
				System.out.print(queue.remove() + " ") ;
			}
			System.out.println() ;
		}

		public static void main(String args[]){
			Queue<Integer> queue = new LinkedList<Integer>() ;
			Random rand = new Random(47) ;
			for(int i=0;i<10;i++){
				int temp = rand.nextInt(i + 10) ;
				System.out.print(temp + " ") ;
				queue.offer(temp) ;
			}
			System.out.println() ;
			printQ(queue) ;
			Queue<Character> queuec = new LinkedList<Character>() ;
			for(char c : "zhonghuarenminggongheguo".toCharArray()){
				queuec.offer(c) ;
			}
			printQ(queuec) ;
		}
	}/* Output:
		8 1 1 1 5 14 3 1 0 1
		8 1 1 1 5 14 3 1 0 1
		z h o n g h u a r e n m i n g g o n g h e g u o
	*/

>队列数据结构是先进先出，因此一般添加元素都是在队尾(offer()、add())，删除元素是在队头(remove()、poll())，并且返回队尾元素；返回队头元素方法是：peek()、element()。

#### PriorityQueue

先进先出描述了最典型的队列规则。

优先队列声明了下一个弹出元素是最需要的元素(具有最高的优先级)。

当你在PriorityQueue上调用offer()方法来插入一个对象时，这个对象会在队列中排序。默认的排序将使用对象在队列中的自然顺序，但是你可以提供自己的Comparator来修改这个顺序。PriorityQueue可以保证在调用peek()、pool()、remove()时，获得的元素是队列中具有最高优先级的元素。

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class PriorityQueueDemo{
		public static void printQ(Queue queue){
			while(queue.peek() != null){
				System.out.print(queue.remove() + " ") ;
			}
			System.out.println() ;
		}

		public static void main(String args[]){
			Queue<Integer> queue = new PriorityQueue<Integer>() ;
			Random rand = new Random(47) ;
			for(int i=0;i<10;i++){
				int temp = rand.nextInt(i + 10) ;
				System.out.print(temp + " ") ;
				queue.offer(temp) ;
			}
			System.out.println() ;
			printQ(queue) ;
			Queue<Character> pq = new PriorityQueue<Character>() ;
			for(char c : "zhonghuarenminggongheguo".toCharArray()){
				pq.offer(c) ;
			}
			printQ(pq) ;
		}
	}/* Output:
		8 1 1 1 5 14 3 1 0 1
		0 1 1 1 1 1 3 5 8 14
		a e e g g g g g h h h i m n n n n o o o r u u z
	*/

<h3 id="11.12">11.12 Collection和Iterator</h3>

Collection是描述所有序列容器的共性根接口。

通过接口和Iterator完成的多态性实现

	package org.fmz.holding ;

	import typeinfo.pets.* ;
	import java.util.* ;

	public class InterfacesVsIterator{
		public static void display(Collection<Pet> pets){
			for(Pet p : pets){
				System.out.print(p.id() + ":" + p + " ") ;
			}
			System.out.println() ;
		}

		public static void display(Iterator<Pet> it){
			while(it.hasNext()){
				Pet p = it.next() ;
				System.out.print(p.id() + ":" + p + " ") ;
			}
			System.out.println() ;
		}

		public static void main(String args[]){
			List<Pet> pets = Pets.arrayList(8) ;
			Set<Pet> petshs = new HashSet<Pet>() ;
			for(Pet p : pets){
				petshs.add(p) ;//对象的添加不会重复
			}
			Map<String, Pet> petslhm = new LinkedHashMap<String, Pet>() ;
			String[] names = "A B C D E F G H".split(" ") ;
			for(int i=0;i<names.length;i++){
				petslhm.put(names[i], pets.get(i)) ;
			}
			System.out.println(pets) ;
			display(pets) ;
			display(pets.iterator()) ;
			System.out.println(petshs) ;
			display(petshs) ;
			display(petshs.iterator()) ;
			System.out.println(petslhm) ;
			System.out.println(petslhm.keySet()) ;
			System.out.println(petslhm.values()) ;
			display(petslhm.values()) ;
			display(petslhm.values().iterator()) ;
		}
	}/* Output:
		[Rat, Manx, Cymric, Mutt, Pug, Cymric, Pug, Manx]
		0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
		0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
		[Rat, Manx, Cymric, Mutt, Pug, Cymric, Pug, Manx]
		0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
		0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
		{A=Rat, B=Manx, C=Cymric, D=Mutt, E=Pug, F=Cymric, G=Pug, H=Manx}
		[A, B, C, D, E, F, G, H]
		[Rat, Manx, Cymric, Mutt, Pug, Cymric, Pug, Manx]
		0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
		0:Rat 1:Manx 2:Cymric 3:Mutt 4:Pug 5:Cymric 6:Pug 7:Manx
	*/

>两种方法都可以实现display()方法与底层容器的特定实现的解耦

当你要实现的不是一个Collection的外部类时，由于让它去实现Collection接口可能会非常困难或者麻烦，因此使用Iterator会非常吸引人。

外部类实现Collection接口，复用代码。代码示例：

	package org.fmz.holding ;

	import typeinfo.pets.* ;
	import java.util.* ;

	public class CollectionSequence extends AbstractCollection<Pet>{
		private Pet[] pets = Pets.createArray(8) ;
		public int size(){
			return pets.length ;
		}
		public Iterator<Pet> iterator(){//怎么可能有构造方法，明明是一个接口啊
			return new Iterator<Pet>(){//原来是个内部类
				private int index = 0 ;
				public boolean hasNext(){
					return index < pets.length ;
				}
				public Pet next(){
					return pets[index++] ;
				}
				public void remove(){
					throw new UnsupportedOperationException() ;
				}
			};	
		}
		public static void main(String args[]){
			CollectionSequence c = new CollectionSequence() ;
			InterfacesVsIterator.display(c) ;
			InterfacesVsIterator.display(c.iterator()) ;
		}
	}

这个时候，如果你的类已经继承了别的类，就不能实现Collection接口了，这时候只有实现了iterator()，这个时候会显得更加容易一些。

	package org.fmz.holding ;

	import typeinfo.pets.* ;
	import java.util.* ;

	class PetSequence{
		protected Pet[] pets = Pets.createArray(8) ;
	}

	public class NoCollectionSequence extends PetSequence{
		public Iterator<Pet> iterator(){//怎么可能有构造方法，明明是一个接口啊
			return new Iterator<Pet>(){//原来是个内部类
				private int index = 0 ;
				public boolean hasNext(){
					return index < pets.length ;
				}
				public Pet next(){
					return pets[index++] ;
				}
				public void remove(){
					throw new UnsupportedOperationException() ;
				}
			};	
		}
		public static void main(String args[]){
			NoCollectionSequence nc = new NoCollectionSequence() ;
			//InterfacesVsIterator.display(c) ;
			InterfacesVsIterator.display(nc.iterator()) ;
		}
	}

>这个时候就只能使用Iterator来进行输出了。

>生成Iterator是将队列与消费队列连接在一起的耦合度最小的方式，并且与实现Collection相比，它在序列上所施加的约束也更少。

<h3 id="11.13">11.13 Foreach迭代器</h3>

Foreach可以作用于数组，也可以作用于任何的Collection对象。

之所以能够工作，是因为JavaSE5引入了新的被称为Iterable的接口，该接口包含了一个能产生Iterator的iterator()方法，并且Iterable接口被foreach用来在序列中移动。因此，如果你创建了任何实现Iterable的类，都可以用于foreach语句中。示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class IterableClass implements Iterable<String>{
		protected String[] words = "And that is how we know the Earth to be banana-shaped.".split(" ") ;
		public Iterator<String> iterator(){
			return new Iterator<String>(){
				private int index = 0 ;
				public boolean hasNext(){
					return index < words.length ;
				}
				public String next(){
					return words[index++] ;
				}
				public void remove(){
					throw new UnsupportedOperationException() ;
				}
			};
		}

		public static void main(String args[]){
			for(String s : new IterableClass()){
				System.out.print(s + " ") ;
			}
		}
	}

>IterableClass.java

在JavaSE5中，大量的类都是Iterable类型，但是不包括各种Map。将你的类变成Iterable就能够运用foreach了。

显示所有操作系统的环境变量

	package org.fmz.holding ;

	import java.util.* ;

	public class EnvironmentVariables{
		public static void main(String args[]){
			for(Map.Entry entry : System.getenv().entrySet()){
				System.out.println(entry.getKey() + ": " + entry.getValue()) ;
			}
		}
	}

foreach语句可以作用于数组或者其他任何Iterable，但是并不意味着数组是Iterable，而且任何的自动包装也不会发生。示例代码：

package org.fmz.holding ;

	import java.util.* ;

	public class ArraysIsNotIterable{
		static <T> void test(Iterable<T> ib){
			for(T t : ib){
				System.out.print(t + " ") ;
			}
		}

		public static void main(String args[]){
			test(Arrays.asList(1, 2, 3)) ;
			String[] str = {"A", "B", "C"} ;
			//test(str); //数组不能装换为Iterable
			test(Arrays.asList(str)) ;//如果要复用代码，必须进行手动的转化
		}
	}

#### 适配器方法惯用法

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	class ReversibleArrayList<T> extends ArrayList<T>{
		public ReversibleArrayList(Collection<T> c){
			super(c) ;
		}
		public Iterable<T> reversed(){
			return new Iterable<T>(){
				public Iterator<T> iterator(){
					return new Iterator<T>(){
						int current = size() - 1 ;//size()方法返回的是集合中元素的个数，比最后一个元素的索引数大1
						public boolean hasNext(){
							return current > -1 ;
						}
						public T next(){
							return get(current--) ;
						}
						public void remove(){
							throw new UnsupportedOperationException() ;
						}
					};
				}
			};
		}
	}

	public class AdapterMethodIdiom{
		public static void main(String args[]){
			ReversibleArrayList<String> ral = new ReversibleArrayList<String>(Arrays.asList("To be or not to be".split(" "))) ;
			for(String s : ral){
				System.out.print(s + " ") ;
			}
			System.out.println() ;

			for(String s : ral.reversed()){
				System.out.print(s + " ") ;
			}
		}
	}/* Output:
		To be or not to be
		be to not or be To
	*/

在IterableClass中增加两个适配器

	package org.fmz.holding ;

	import java.util.* ;

	public class MultiIterableClass extends IterableClass{
		public Iterable<String> reversed(){
			return new Iterable<String>(){
				public Iterator<String> iterator(){
					return new Iterator<String>(){
						int current = words.length - 1 ;
						public boolean hasNext(){
							return current > -1 ;
						}
						public String next(){
							return words[current--] ;
						}
						public void remove(){
							throw new UnsupportedOperationException() ;
						}
					};
				}
			};
		}

		public Iterable<String> randomized(){
			return new Iterable<String>(){
					public Iterator<String> iterator(){
					List<String> shuffled = new ArrayList<String>(Arrays.asList(words)) ;
					Collections.shuffle(shuffled, new Random(47)) ;
					return shuffled.iterator() ;
				}
			};
		}

		public static void main(String args[]){
			MultiIterableClass mic = new MultiIterableClass() ;
			for(String s : mic){
				System.out.print(s + " ") ;
			}
			System.out.println() ;
			for(String s : mic.reversed()){
				System.out.print(s + " ") ;
			}
			System.out.println() ;
			for(String s : mic.randomized()){
				System.out.print(s + " ") ;
			}
			System.out.println() ;
		}
	}/* Output:
		And that is how we know the Earth to be banana-shaped.
		banana-shaped. be to Earth the know we how is that And
		is banana-shaped. Earth that how the be And we know to
	*/

#### Collections.shuffle()

示例代码：

	package org.fmz.holding ;

	import java.util.* ;

	public class ModifyingArraysAsList{
		public static void main(String args[]){
			Random rand = new Random(47) ;
			Integer[] ia = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} ;
			List<Integer> list1 = new ArrayList<Integer>(Arrays.asList(ia)) ;
			System.out.println("Before Shuffling: " + list1) ;
			Collections.shuffle(list1, rand) ;
			System.out.println("After Shuffling: " + list1) ;
			System.out.println("array: " + Arrays.toString(ia)) ;

			List<Integer> list2 = Arrays.asList(ia) ;
			System.out.println("Before Shuffling: " + list2) ;
			Collections.shuffle(list2, rand) ;
			System.out.println("After Shuffling: " + list2) ;
			System.out.println("array: " + Arrays.toString(ia)) ;
		}
	}/* Output:
		Before Shuffling: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		After Shuffling: [4, 6, 3, 1, 8, 7, 2, 5, 10, 9]
		array: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		Before Shuffling: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		After Shuffling: [9, 1, 6, 3, 7, 2, 5, 10, 4, 8]
		array: [9, 1, 6, 3, 7, 2, 5, 10, 4, 8]
	*/

>在第一种情况中，Arrays.asList()的输出被传递给了ArrayList()的构造器，这将创建一个引用ia的元素的ArrayList,因此打乱这些引用，不会修改数组；但是，如果直接使用Array.asList(ia)的结果，这种打乱就会修改ia的顺序。

>Arrays.asList()产生的List对象会使用底层数组作为其物理实现。

各种容器类的差异：

	package org.fmz.holding ;

	import net.mindview.util.* ;

	public class ContainerMethods{
		public static void main(String args[]){
			ContainerMethodDifferences.main(args) ;
		}
	}/* Output:
		Collection: [add, addAll, clear, contains, containsAll, equals, forEach, hashCode, isEmpty, iterator, parallelStream, remove, removeAll, removeIf, retainAll, size, spliterator, stream, toArray]
		Interfaces in Collection: [Iterable]
		Set extends Collection, adds: []
		Interfaces in Set: [Collection]
		HashSet extends Set, adds: []
		Interfaces in HashSet: [Set, Cloneable, Serializable]
		LinkedHashSet extends HashSet, adds: []
		Interfaces in LinkedHashSet: [Set, Cloneable, Serializable]
		TreeSet extends Set, adds: [headSet, descendingIterator, descendingSet, pollLast, subSet, floor, tailSet, ceiling, last,
		 lower, comparator, pollFirst, first, higher]
		Interfaces in TreeSet: [NavigableSet, Cloneable, Serializable]
		List extends Collection, adds: [replaceAll, get, indexOf, subList, set, sort, lastIndexOf, listIterator]
		Interfaces in List: [Collection]
		ArrayList extends List, adds: [trimToSize, ensureCapacity]
		Interfaces in ArrayList: [List, RandomAccess, Cloneable, Serializable]
		LinkedList extends List, adds: [offerFirst, poll, getLast, offer, getFirst, removeFirst, element, removeLastOccurrence,
		peekFirst, peekLast, push, pollFirst, removeFirstOccurrence, descendingIterator, pollLast, removeLast, pop, addLast, pee
		k, offerLast, addFirst]
		Interfaces in LinkedList: [List, Deque, Cloneable, Serializable]
		Queue extends Collection, adds: [poll, peek, offer, element]
		Interfaces in Queue: [Collection]
		PriorityQueue extends Queue, adds: [comparator]
		Interfaces in PriorityQueue: [Serializable]
		Map: [clear, compute, computeIfAbsent, computeIfPresent, containsKey, containsValue, entrySet, equals, forEach, get, get
		OrDefault, hashCode, isEmpty, keySet, merge, put, putAll, putIfAbsent, remove, replace, replaceAll, size, values]
		HashMap extends Map, adds: []
		Interfaces in HashMap: [Map, Cloneable, Serializable]
		LinkedHashMap extends HashMap, adds: []
		Interfaces in LinkedHashMap: [Map]
		SortedMap extends Map, adds: [lastKey, subMap, comparator, firstKey, headMap, tailMap]
		Interfaces in SortedMap: [Map]
		TreeMap extends Map, adds: [descendingKeySet, navigableKeySet, higherEntry, higherKey, floorKey, subMap, ceilingKey, pol
		lLastEntry, firstKey, lowerKey, headMap, tailMap, lowerEntry, ceilingEntry, descendingMap, pollFirstEntry, lastKey, firs
		tEntry, floorEntry, comparator, lastEntry]
		Interfaces in TreeMap: [NavigableMap, Cloneable, Serializable]
	*/

***

***

<h2 id="12">12 通过异常处理错误</h2>

Java的基本理论是：结构不佳的代码不能运行。

<h3 id="12.1">12.1 概念</h3>

如果使用异常，那就不必方法的调用处进行检查，因为异常机制将保证能够捕获这个错误，并且只需要在一个地方处理错误，也就是所谓的异常处理程序中。

<h3 id="12.2">12.2 基本异常</h3>

异常情形(exceptional condition)是阻止当前方法或者作用域继续执行的问题。异常情形与普通信息不同，所谓的普通问题是指：在当前的环境下能够得到足够的信息，总能够处理这个错误；而对于异常信息，就不能继续下去了，因为在当前的环境下无法获得必要的信息来解决问题。你所要做的就是从当前的环境中跳出，并且把问题提交给上一级环境。这就是抛出异常是发生的事情。

当抛出异常之后又几件事情要发生。首先，程序会在堆中创建一个异常对象。然后，当前执行的路径被终止，并且从当前的环境中弹出异常对象的引用。此时，异常处理机制接管程序，并开始寻找一个恰当的方法来继续执行程序。这个恰当的方法就是异常处理程序，它的任务是从错误的状态中恢复，以使程序要么换一种方式运行，要么继续运行下去。

异常允许我们强制程序停止运行，并且告诉我们出现了什么问题，或者(理想状态下)强制程序处理问题，并返回到稳定状态。

#### 异常参数

标准的异常都有两个构造器，一个是默认的构造器，另外一个是接收字符串参数的构造器，以便把相关信息放入异常对象的构造器中。

能够抛出任意Throwable对象，它使异常类型的根类。

<h3 id="12.3">12.3 捕获异常</h3>

监控区域(guarded region)：它是一段可能产生异常的代码，并且后面跟着处理这些异常的代码。

#### try块

如果在方法内部抛出了异常(或者在方法内部调用的其他方法抛出了异常)，这个方法将在抛出异常的过程中结束。要是不希望方法就此结束，可以在方法内设置一个特殊的块来捕获异常。代码：`try{//...}`

对于不支持异常处理的程序语言，要想仔细的检查错误，就要在每个方法的后面加上设置和错误检验的代码，甚至在每次调用同样的方法的时候都要这么做。有了异常处理机制，可以把所有的动作都放在try块中，然后只需要在一个地方就可以捕获所有异常。这意味着代码将更容易编写和阅读，因为完成任务的代码没有和错误检验的代码混在一起。

#### 异常处理程序

异常处理紧跟在try后面，以关键字catch表示：`try{//...}catch(Type1 id1){//Handle Excepiton of Type1}catch(Type2 id2){//Handle Excepiton of Type2}catch(Type3 id3){//Handle Excepiton of Type3}`

注意：在try块的内部，许多不同的方法调用可能会产生类型相同的异常，而你只要提供一个针对此类型的异常处理程序。

##### 终止与恢复

异常处理程序理论上有两种基本模式。java支持终止模式：在这种模型中，将假设错误非常关键，以至程序无法返回到异常发生的地方继续执行。一旦异常抛出，就表明错误已经无法挽回，也不能回来继续执行。

另一种称为恢复模式：异常处理程序的工作是修正错误，然后重新尝试调用出问题的方法，并认为第二次能成功。对于恢复模型，希望异常处理后程序能够继续执行。如果在java中实现类似的恢复功能，那么在遇见错误的时候就不能抛出异常，而是调用方法来修正这个错误。或者，把try块放在while循环中，这样就不断的进入try块，知道得到满意的结果。

>虽然恢复模型显得很吸引人，但是最终还是转向了终止模式，而且忽略了恢复行为。其中，重要的原因可能是它所导致的耦合：异常处理程序要依赖抛出位置的非通用性代码。

<h3 id="12.4">12.4 创建自定义异常</h3>

一个简单的异常

	package org.fmz.exception ;

	class SimpleException extends Exception{

	}

	public class InheritingException{
		public void f()throws SimpleException{
			System.out.println("Throw SimpleException from f()") ;
			throw new SimpleException() ;
		}

		public static void main(String args[]){
			InheritingException ie = new InheritingException() ;
			try{
				ie.f() ;
				}catch(SimpleException se){
					System.out.println("Catch it!") ;
				}
		}
	}/* Output:
	Throw SimpleException from f()
	Catch it!
	*/

>编译器创建了默认的构造器，它将自动调用基类中的构造器。本例总没有使用SimpleException(String)这样的构造器，这种构造器也不实用。对于异常来说，最重要的部分就是类名。

接收字符串的自定义异常类：

	package org.fmz.exception ;

	class MyException extends Exception{
		public MyException(){}
		public MyException(String msg){
			super(msg) ;
		}
	}

	public class FullConstructors{
		public static void f() throws MyException{
			System.out.println("Throwing MyException from f()") ;
			throw new MyException() ;
		}
		
		public static void g() throws MyException{
			System.out.println("Throwing MyException from g()") ;
			throw new MyException("Originated in g()") ;
		}

		public static void main(String args[]){
			try{
				f() ;
			}catch(MyException me){
				me.printStackTrace(System.out) ;
			}
			try{
				g() ;
			}catch(MyException me){
				me.printStackTrace(System.out) ;
			}
		}
	}/* Output:
	Throwing MyException from f()
	org.fmz.exception.MyException
		at org.fmz.exception.FullConstructors.f(FullConstructors.java:13)
		at org.fmz.exception.FullConstructors.main(FullConstructors.java:23)
	Throwing MyException from g()
	org.fmz.exception.MyException: Originated in g()
		at org.fmz.exception.FullConstructors.g(FullConstructors.java:18)
		at org.fmz.exception.FullConstructors.main(FullConstructors.java:28)
	*/

>在异常的处理程序中，调用了Throwable类声明的printStackTrace()方法。表示：打印从方法的调用处直到异常抛出处的方法调用序列。这里信息被发送到System.out，并自动地被捕获和显示在输出中。如果直接调用e.printStackTrace()，则信息将被输出到标准错误流当中。

#### 异常与记录日志

基本的日志记录功能

	package org.fmz.exception ;

	import java.util.logging.* ;
	import java.io.* ;

	class LoggingException extends Exception{
		private static Logger logger = Logger.getLogger("LoggeringException") ;
		public LoggingException(){
			StringWriter trace = new StringWriter() ;
			printStackTrace(new PrintWriter(trace)) ;
			logger.severe(trace.toString()) ;
		}
	}

	public class LoggingExceptions{
		public static void main(String args[]){
			try{
				throw new LoggingException() ;
			}catch(LoggingException e){
				System.err.println("Caught: " + e) ;
			}
		}
	}/* Output:
	十月 24, 2015 11:05:40 上午 org.fmz.exception.LoggingException <init>
	严重: org.fmz.exception.LoggingException
		at org.fmz.exception.LoggingExceptions.main(LoggingExceptions.java:18)

	Caught: org.fmz.exception.LoggingException
	*/

>静态的Logging.getLogging()方法创建一个String参数(通常与错误相关联的包名和类名)相关联的Logging对象。这个对象将其输出发送到System.err。

如果我们想要捕获和记录其他人编写的异常

	package org.fmz.exception ;

	import java.util.logging.* ;
	import java.io.* ;

	public class LoggingException2{
		private static Logger logger = Logger.getLogger("LoggeringException2") ;
		static void logException(Exception e){
			StringWriter trace = new StringWriter() ;
			e.printStackTrace(new PrintWriter(trace)) ;
			logger.severe(trace.toString()) ;
		}

		public static void main(String args[]){
			try{
				throw new NullPointerException() ;
			}catch(NullPointerException e){
				logException(e) ;
			}
		}
	}/* Output:
		十月 24, 2015 11:03:04 上午 org.fmz.exception.LoggingException2 logException
		严重: java.lang.NullPointerException
				at org.fmz.exception.LoggingException2.main(LoggingException2.java:16)

<h3 id="12.5">12.5 异常说明</h3>

Java鼓励人们把方法可能会抛出的异常告知使用此方法的客户端程序员。为了让客户端程序员知道什么样的程序抛出什么样的异常，java提供的响应的语法(并且强制执行这样的语法)，这样能以礼貌的方式告诉客户端程序员某个方法可能会抛出的异常类型，然后客户端程序员可以进行相应的处理。这就是异常说明，属于方法声明的一部分，紧跟在参数列表之后。

例如：`void f() throws TooBig, TooSmall, DivZero{//...}`表示会抛出多种异常；`void f(){//...}`表示不会抛出任何异常(除了从RuntimeException继承的异常，它们可以在没有异常声明的情况下被抛出)。

代码必须与异常的声明保持一致，如果方法代码中产生了异常却没有进行处理，编译器会发现这个问题，并提醒你：要么处理这个异常，要么就在异常说明中标明此方法将会产生异常。通过这种自顶向下强制执行的异常说明机制，java在编译时就可以就可以保证一定水平的异常正确性。

<h3 id="12.6">12.6 捕获所有的异常</h3>

捕获所有的异常：`catch(Exception e){//...}`，这将捕获所有的异常，所以最好把它放在处理程序的末尾，以防它抢在其他处理程序之前先把异常捕获了。

#### 栈轨迹

printStackTrace()方法所提供的信息可以通过getStackTrace()方法直接访问，这个方法将返回一个由栈轨迹中的所有元素构成的数组，其中每一个元素都表示栈中的一帧。元素0是栈顶元素，并且是调用序列中的最后一个方法的调用。数组中最后一个元素和栈底是调用序列中的第一个方法的调用。示例代码：

	package org.fmz.exception ;

	public class WhoCalled{
		static void f(){
			try{
				throw new Exception() ;
			}catch(Exception e){
				for(StackTraceElement ste : e.getStackTrace()){
					System.out.println(ste) ;
				}
			}
		}

		static void g(){f() ;}

		static void h(){g() ;}

		public static void main(String args[]){
			f() ;
			System.out.println("********************") ;
			g() ;
			System.out.println("********************") ;
			h() ;
			System.out.println("********************") ;
		}
	}/* Output:
		********************
		org.fmz.exception.WhoCalled.f(WhoCalled.java:6)
		org.fmz.exception.WhoCalled.g(WhoCalled.java:14)
		org.fmz.exception.WhoCalled.main(WhoCalled.java:21)
		********************
		org.fmz.exception.WhoCalled.f(WhoCalled.java:6)
		org.fmz.exception.WhoCalled.g(WhoCalled.java:14)
		org.fmz.exception.WhoCalled.h(WhoCalled.java:16)
		org.fmz.exception.WhoCalled.main(WhoCalled.java:23)
		********************
	*/

#### 重新抛出异常

待续...

#### 异常链

待续...

<h3 id="12.7">12.7 Java标准异常</h3>

Throwable这个被用来表示任何可以作为异常被抛出的类。Throwable对象可以被分为两种类型: Error用来表示编译时和系统错误(除特殊情况外，一般你不用担心)；Exception是可以被抛出的基本数据类型，在java的类库、用户方法以及运行时的故障都可抛出Exception异常。

异常的基本概念是用名称代表发生的问题，并且异常的名称通常可以望文生义。

#### 特例：RuntimeException

代码：

	if(t == null){
		throw  new NullPointerException() ;
	}

如果必须对传递给对方的每一个引用都检查其是否为null，这将是一件非常麻烦的事情。幸运的是，你不用这么做，它属于java标准运行时检测的一部分。

如果对null进行了调用，java会自动抛出NullPointerException。NullPointerException继承自RuntimeException，而RuntimeException是java会自动抛出的一种异常。

属于RuntimeException的类型非常多，它们会自动被java虚拟机抛出，所以不必在异常说明中把它们列出来，也不需要在异常说明中声明方法将抛出RuntimeException类型的异常。它们是不受检查的类型。

注意：只能在代码中忽略RuntimeException(或者任何从它继承的异常)，其他类型的异常的处理都是由编译器强制实施的。

<h3 id="12.8">12.8 使用finally进行清理</h3>

示例代码：

	package org.fmz.exception ;

	class ThreeException extends Exception{
	}

	public class FinalWorks{
		static int count = 0 ;
		public static void main(String args[]){
			while(true){
				try{
					if(count++ == 0){
						throw new ThreeException() ;
					}
					System.out.println("No Exception") ;
				}catch(ThreeException e){
					System.out.println("ThreeException") ;
				}finally{
					System.out.println("In Finally Case") ;
					if(count == 2)
						break ;
				}
			}
		}
	}/* Output:
		ThreeException
		In Finally Case
		No Exception
		In Finally Case
	*/

>无论异常是否被抛出，finally字句总能够执行。

#### finally用来做什么？

当要把内存之外的资源恢复到它们原始的状态时，就要用到finally字句。这种需要清理的资源包括：已经打开的问价或者网络连接，在屏幕上画的图形，甚至可以是外部世界的某个开关。

示例代码：

	package org.fmz.exception ;

	class Switch{
		private boolean state = false ;
		public boolean read(){
			return state ;
		}
		public void on(){
			state = true ;
			System.out.println(this) ;
		}
		public void off(){
			state = false ;
			System.out.println(this) ;
		}
		public String toString(){
			return state ? "on" : "off" ;
		}
	}

	class OnOffException1 extends Exception{
	}

	class OnOffException2 extends Exception{
	}

	public class OnOffSwitch{
		private static  Swith s = new Switch() ;
		public static void f() throws OnOffException1, OnOffException2{
		
		}

		public static void main(String args[]){
			try{
				s.on() ;
				f() ;
				s.off() ;
			}catch(OnOffException1 e){
				System.out.println("OnOffException1") ;
				s.off() ;
			}catch(OnOffException2 e){
				System.out.println("OnOffException2") ;
				s.off() ;
			}
		}
	}

>这个程序想要表达的意思是，当main()函数结束的时候开关必须是关闭的，但是当f()函数抛出了异常，没有处理，则关闭操作就无法执行。如果是有了finally，只要把try块中的清理代码移动到一处即可。、

	package org.fmz.exception ;

	public class WithFinally{
		public static void main(String args[]){
			try{
				s.on() ;
				f() ;
				s.off() ;
			}catch(OnOffException1 e){
				System.out.println("OnOffException1") ;
			}catch(OnOffException2 e){
				System.out.println("OnOffException2") ;
			}finally{
				s.off() ;
			}
		}
	}

>这样无论是否抛出异常，s.off()是否被调用，最后都能调用s.off()；还有一个好处是将清理代码归结到了一个地方，更加易于维护。

#### 在return中使用finally

示例代码：

	package org.fmz.exception ;

	public class MultipleReturns{
		public static void f(int i){
			System.out.println("Initialization that requires cleanup") ;
			try{
				System.out.println("Point 1") ;
				if( i == 1){
					return ;
				}
				System.out.println("Point 2") ;
				if( i == 2){
					return ;
				}
				System.out.println("Point 3") ;
				if( i == 3){
					return ;
				}
				System.out.println("End") ;
				return ;
			}finally{
				System.out.println("Performing clearup") ;
			}
		}

		public static void main(String args[]){
			for(int i=1;i<=4;i++){
				f(i) ;
				System.out.println("******************") ;
			}
		}
	}/* Output:
		Initialization that requires cleanup
		Point 1
		Performing clearup
		******************
		Initialization that requires cleanup
		Point 1
		Point 2
		Performing clearup
		******************
		Initialization that requires cleanup
		Point 1
		Point 2
		Point 3
		Performing clearup
		******************
		Initialization that requires cleanup
		Point 1
		Point 2
		Point 3
		End
		Performing clearup
		******************
	*/

#### 缺憾：异常缺失

<h3 id="12.9">12.9 异常的限制</h3>

当覆盖方法的时候，只能够抛出在基类方法的异常说明里列出的那些异常。

<h3 id="12.10">12.10 构造器</h3>

对于构造阶段可能会抛出异常，并且要求清理的类，最安全的使用方式是使用嵌套的try子句，示例代码：

	package org.fmz.exception ;

	public class CleanUp{
		public static void main(String args[]){
			try{
				InputFile in = new InputFile("CleanUp.java") ;
				try{
					String s ;
					int i = 1 ;
					while((s = in.getLine()) != null){
						;
					}catch(Exception e){
						System.out.println("Caught Exception in main") ;
						e.printStackTrace(System.out) ;
					}finally{
						in.dispose() ;
					}
				}
			}catch(Exception e){
				System.out.println("InputFile Constructor failed") ;
			}
		}
	}

>这种通用的清理惯用法在构造器不抛出任何异常是也应该运用，其基本的规则是：在构建需要清理的对象之后，立刻进入一个try-finally语句块。

<h3 id="12.11">12.11 异常匹配</h3>

在抛出异常的时候，异常处理系统会按照代码的书写顺序找出最近的处理程序。

查找的时候并不要求抛出的异常同处理程序所声明的异常完全匹配。派生类对象也可以匹配其基类的处理程序。

<h3 id="12.12">12.12 其他可选方式</h3>

***

***

<h2 id="13">13 字符串</h2>

字符串操作是计算机程序设计中最常见的行为。

<h3 id="13.1">13.1 不可变的String</h3>

示例代码：

	package org.fmz.string ;

	public class Immutable{
		public static String upcase(String s){
			return s.toUpperCase() ;
		}

		public static void main(String args[]){
			String q = "fengmengzhao" ;
			System.out.println(q) ;
			String qq = upcase(q) ;//传递的只是一个引用
			System.out.println(qq) ;
			System.out.println(q) ;
		}
	}/* Output:
		fengmengzhao
		FENGMENGZHAO
		fengmengzhao
	*/

>当把q传递给upcase()方法时，实际上传递的使引用一个拷贝。其实，每当把String对象作为方法的参数时，都会复制一份引用，而该引用所指向的对象一直呆在单一的物理位置上，从未动过。

对于一个方法而言，参数是为该方法提供信息的，而不是想让该方法改变自己的。

<h3 id="13.2">13.2 重载 "+" 与StringBuilder</h3>

String对象是不可变的。因为String对象只有只读性，所以指向它的任何引用都不会改变它的值。

不可变会带来效率的问题。

Java中有两个重载的操作符：`+`、`+=`，并且仅仅有这两个操作符，java并不允许程序员重载任何操作符。

两种方法完成字符串数组转化成字符串，示例代码：

	package org.fmz.string ;

	public class WhitherStringBuilder{
		public static  String implicit(String[] fields){
			String result = "" ;
			for(int i=0;i<fields.length;i++){
				result += fields[i] ;
			}
			return result ;
		}
		public static  String explicit(String[] fields){
			StringBuilder result = new StringBuilder() ;
			for(int i=0;i<fields.length;i++){
				result.append(fields[i]) ;
			}
			return result.toString() ;
		}

		public static void main(String args[]){
			String[] str = {"a", "b", "c", "d"} ;
			System.out.println(implicit(str)) ;
			System.out.println(explicit(str)) ;
		}
	}

>通过`javap -c WhitherStringBuilder`我们可以得到类的字节码，通过字节码的分析我们知道：通过使用重载的String操作符(implicit()方法)则会在循环之内创建StringBuffer，这意味着没经过一次循环就要创建一个新的StringBuilder对象。而通过显式的创建StringBuilder对象的方法(explitic()方法)，只会创建一次StringBuilder对象。

因此为一个类编写toString()方法的时候，如果字符串的操作比较简单，那么可以信赖编译器，它会为你构造合理的字符串最终结果。但是，如果要在toString()方法中使用循环，最好自己创建一个StringBuilder对象，用它来构造最终结果。

实例代码：

	package org.fmz.string ;

	import java.util.* ;

	public class UsingStringBuilder{
		public static Random rand = new Random(47) ;
		public String toString(){
			StringBuilder result = new StringBuilder("[") ;
			for(int i=0;i<25;i++){
				result.append(rand.nextInt(100)) ;
				result.append(", ") ;
			}
			result.delete(result.length() - 2, result.length()) ;
			result.append("]") ;
			return result.toString() ;
		}

		public static void main(String args[]){
			UsingStringBuilder usb = new UsingStringBuilder() ;
			System.out.println(usb) ;
		}
	}/* Output:
		[58, 55, 93, 61, 61, 29, 68, 0, 22, 7, 88, 28, 51, 89, 9, 78, 98, 61, 20, 58, 16, 40, 11, 22, 4]
	*/

>在编写对象的toString()方法的时候用到了循环，这个时候最好使用显式的StringBuilder来创建一个对象，这样做使得程序具有更高的性能。

<h3 id="13.3">13.3 无意识的递归</h3>

Java中的每个类从根本上都是继承自Object，标准容器自然也不例外。因此容器类都有toString()方法，并且覆写了此方法，使得它生成的String结果能够表达容器本身，以及容器所包含的对象。例如：ArrayList.toString()，它会遍历ArrayList中包含的所有对象，调用每个元素上的toString()方法。

实例代码：

	package org.fmz.string ;

	import java.util.* ;

	public class InfiniteRecursion{
		public String toString(){
			//return "InfiniteRecursion Address" + this ;//发生了递归调用
			return super.toString() ;
		}

		public static void main(String args[]){
			List<InfiniteRecursion> v = new ArrayList<InfiniteRecursion>() ;
			for(int i=0;i<10;i++){
				v.add(new InfiniteRecursion()) ;
			}
			System.out.println(v) ;
		}
	}/* Output:
		[org.fmz.string.InfiniteRecursion@659e0bfd, org.fmz.string.InfiniteRecursion@2a139a55, org.fmz.string.InfiniteRecursion@
		15db9742, org.fmz.string.InfiniteRecursion@6d06d69c, org.fmz.string.InfiniteRecursion@7852e922, org.fmz.string.InfiniteR
		ecursion@4e25154f, org.fmz.string.InfiniteRecursion@70dea4e, org.fmz.string.InfiniteRecursion@5c647e05, org.fmz.string.I
		nfiniteRecursion@33909752, org.fmz.string.InfiniteRecursion@55f96302]
	*/

>如果使用`return "InfiniteRecursion Address" + this ;`，则当调用InfiniteRecursion对象的时候会本类中的toString()方法(覆写Object类中的方法)，这样就会发生方法中对本方法的调用，也就是发生了递归，会抛出异常。

>解决办法是调用Object类中的toString()方法，这样就不会发生递归了。

<h3 id="13.4">13.4 String上的操作</h3>

| 方法 | 参数、重载版本 | 应用 |
|:-- |:--:| --:|
| 构造器 | 重载版本：默认版本，String，StringBuilder，StringBuffer，char数组，byte数组 | 创建String对象 |
| length() | | String中字符的个数 |
| charAt() | Int索引 | 取得String中该索引位置上的char |
| getChars()、getBytes() | 要复制部分的起点和终点的索引，复制的目标数组，目标数组的起始索引 | 复制char或则byte到一个目标数组中 |
| toCharArray() | | 生成一个char[]，包含String的所有字符 |
| equals()、equalsIgnoreCase() | 与之进行比较的String | 比较两个String是否相同 |
| compareTo() | 与之进行比较的String | 按字典书序比较String内容，比较结果为负数、0或者正数。注意：大小写并不等价 |、
| contains() | 要搜索的charSequence | 如果该String中包含参数内容，返回true |
| contentEquals() | 与之进行比较的charSequence或者StringBuffer | 如果该String与参数内容完全一致，则返回true |
| regionMatcher() | 该String的索引偏移量，另一个String及其索引偏移量，要比较的长度。重载版本中增加了忽略大小写的功能 | 返回Boolean型，以表示所比较的区域是否相等 |
| startsWith() | 可能的启示String，重载的版本中增加了偏移量 | 返回Boolean结果，表示字符串是否以指定参数开头 |
| endsWith() | 该String可能的后缀String | 返回Boolean结果，表示字符串是否以指定参数为后缀 |
| indexOf()、lastIndexOf() | 重载版本包括：char、char与起始索引、String、String与起始索引 | 如果该String并不包含指定参数，则返回-1；否则返回此参数在String中的起始索引。lastIndexOf()是从后向前搜索 |
| subString()(subSequence) | 重载版本：起始索引，起始索引+终点坐标 | 返回一个新的String，以包含参数指定的子字符串 |
| concat() | 要连接的String | 返回一个新的String对象，内容为原始String连接上参数String |
| replace() | 要替换掉的字符，用来进行替换的新字符。也可以用一个charSequence来替代另外一个charSequence | 返回替换字符串后的新String对象。如果没有替换发生，则返回原始的String对象 |
| toLowerCase()、toUpperCase() | | 将字符大小写改变后，返回一个新的Strig对象。如果没有放生改变，则返回原来的字符串 |
| trim() | | 将String两端的空白字符删除后返回一个新的String对象。如果没有发生，则返回原始的字符串 |
| valueOf() | 重载版本：Object；char[];char[],偏移量，与字符的个数；boolean；char；int；long；float；double | 返回一个表示参数内容的String |
| intern() | | 为每个唯一的字符序列生成一个且仅生成一个String引用 |

<h3 id="13.5">13.5 格式化的输出</h3>

#### System.out.format()

System.out.format()同C语言中的printf()等价。

#### Formatter类

可以将Formatter看做一个翻译器，它将你的格式化字符串和数据翻译成需要的结果。

当你创建一个Formatter对象的时候，需要向其构造器中传递一些信息，告诉它最终的结果将向哪里输出。

示例代码：

	package org.fmz.string ;

	import java.io.* ;
	import java.util.* ;

	public class Turtle{
		private String name ;
		private Formatter f ;
		public Turtle(String name,Formatter f){
			this.name = name ;
			this.f = f ;
		}
		public void move(int x, int y){
			f.format("%s The Turtle is at (%d, %d)\n", name, x, y) ;
		}

		public static void main(String args[]){
			PrintStream outAlias = System.out ;
			Turtle tommy = new Turtle("Tommy", new Formatter(System.out)) ;
			Turtle terry = new Turtle("Terry", new Formatter(outAlias)) ;
			tommy.move(0, 0) ;
			tommy.move(1, 2) ;
			tommy.move(3, 4) ;
			terry.move(5, 6) ;
			terry.move(7, 8) ;
			terry.move(9, 10) ;
		}
	}/* Output:
		Tommy The Turtle is at (0, 0)
		Tommy The Turtle is at (1, 2)
		Tommy The Turtle is at (3, 4)
		Terry The Turtle is at (5, 6)
		Terry The Turtle is at (7, 8)
		Terry The Turtle is at (9, 10)
	*/

#### 格式化说明符

示例代码：

	package org.fmz.string ;

	import java.util.* ;

	public class Receipt{
		private double total = 0 ;
		private Formatter f = new Formatter(System.out) ;
		public void printTitle(){
			f.format("%-25s %5s %10s\n", "Item", "Qty", "Price") ;
			f.format("%-25s %5s %10s\n", "----", "---", "-----") ;
		}
		public void print(String name, int qty, double price){
			f.format("%-25s %5s %10.2f\n", name, qty, price) ;
			total += price ;
		}

		public void printTotal(){
			f.format("%-25s %5s %10.2f\n", "Tax", "", total*0.06) ;
			f.format("%-25s %5s %10s\n", "", "", "-----") ;
			f.format("%-25s %5s %10.2f\n", "Total", "", total*1.06) ;
		}

		public static void main(String args[]){
			Receipt receipt = new Receipt() ;
			receipt.printTitle() ;
			receipt.print("Jack's Magic Beans", 4, 4.25) ;
			receipt.print("Princess Peas", 3, 5.1) ;
			receipt.print("Three Bears Porridge", 1, 14.29) ;
			receipt.printTotal() ;
		}
	}/* Output:
		Item                        Qty      Price
		----                        ---      -----
		Jack's Magic Beans            4       4.25
		Princess Peas                 3       5.10
		Three Bears Porridge          1      14.29
		Tax                                   1.42
						     -----
		Total                                25.06
	*/

#### Formatter转换

	d	整数型		c	Unicode字符
	b	Boolean值	s	String
	f	浮点数		e	浮点数(科学计数)
	x	整数(十六进制)	h	散列码(十六进制)
	%	字符"%"

#### String.format()

String.format()是一个static方法，它接受与Formatter.format()一样的参数，但是返回一个String对象。当你使用format()方法一次的时候，用这个方法比较方便。

<h3 id="13.6">13.6 正则表达式</h3>

#### 基础

在java的正则表达式语言中，`\\`的意思是我要插入一个正则表达式的反斜杠，所以其后面的字符具有了特殊的含义。如果要想插入一个普通的反斜杠，则：`\\\\`；换行和制表符之类的东西用单斜杠：`\r\n\t`；加好在正则表达式中具有特殊含义，需要转义：`\\+`

String.split()是正则表达式的一个工具，其功能是：将字符串从正则表达式匹配的地方切开。用split方法来测试正则表达式，示例代码：

	package org.fmz.string ;

	import java.util.* ;

	public class Splitting{
		public static String knights = "Then, when you have found the shrubbery, you must cut down the mightiest tree in the forest... with... a herring!" ;
		public static void split(String regx){
			System.out.println(Arrays.toString(knights.split(regx))) ;
		}
		public static void main(String args[]){
			split(" ") ;
			split("\\W+") ;//Non-word Character
			split("n\\W+") ;//"n" followed by Non-word Charcter
		}
	}/*
		[Then,, when, you, have, found, the, shrubbery,, you, must, cut, down, the, mightiest, tree, in, the, forest..., with...
		, a, herring!]
		[Then, when, you, have, found, the, shrubbery, you, must, cut, down, the, mightiest, tree, in, the, forest, with, a, her
		ring]
		[The, whe, you have found the shrubbery, you must cut dow, the mightiest tree i, the forest... with... a herring!]
	*/

>String.split()方法还有一个重载的版本，它允许你限制字符串分割的次数。

String类自带的最后一个正则表达式工具是：替换。示例代码：

	package org.fmz.string ;

	public class Replacing{
		static String s = Splitting.knights ;
		public static void main(String args[]){
			System.out.println(s) ;
			System.out.println(s.replaceFirst("f\\w", "located")) ;
			System.out.println(s.replaceAll("shrubbery|tree|herring", "banana")) ;
		}
	}/* Output:
		Then, when you have found the shrubbery, you must cut down the mightiest tree in the forest... with... a herring!
		Then, when you have locatedund the shrubbery, you must cut down the mightiest tree in the forest... with... a herring!
		Then, when you have found the banana, you must cut down the mightiest banana in the forest... with... a banana!
	*/

#### 创建正则表达式

参考JDK文档java.util.regex.Pattern类

#### 量词

量词是描述一个模式吸收输入文本的方式。

	x?	一个或者零个x
	x*	零个或者多个x
	x+	一个或者多个x
	x{n}	恰好n个x
	x{n,}	至少n个x
	x{n,m}	至少n个x，但是不超过m个

#### Pattern和Matcher

一般来说比起功能有限的String类，我们更愿意构造功能强大的正则表达式对象。只需要导入java.util.regex包，然后用static Pattern.compile()方法来编译你的正则表达式即可。它会根据你String类型的正则表达式来生成一个Pattern对象，接下来，把你想要检索的字符串传入Pattern对象的matcher方法中，matcher()方法会生成一个Matcher对象，它有很多的功能可以用。例如，它的replaceAll()方法能将所有匹配的部分都换成传入的参数。

实例代码：

	package org.fmz.string ;

	import java.util.regex.* ;

	public class TestRegularException{
		public static void main(String args[]){
			if(args.length < 2){
				System.out.println("Usage:\njava org.fmz.string.TestRegularException characterSequence regularExpression") ;
				System.exit(0) ;
			}
			System.out.println("Input: \"" + args[0] + "\"") ;
			for(String arg : args){
				System.out.println("Regular expression: \"" + arg + "\"") ;
				Pattern p = Pattern.compile(arg) ;
				Matcher m = p.matcher(args[0]) ;
				while(m.find()){
					System.out.println("Match \"" + m.group() + "\" at positions: " + m.start() + "-" + (m.end() - 1)) ;
				}
			}
		}
	}/* Output:
		Input: "abcabcabcdefabc"
		Regular expression: "abcabcabcdefabc"
		Match "abcabcabcdefabc" at positions: 0-14
		Regular expression: "abc+"
		Match "abc" at positions: 0-2
		Match "abc" at positions: 3-5
		Match "abc" at positions: 6-8
		Match "abc" at positions: 12-14
		Regular expression: "(abc)+"
		Match "abcabcabc" at positions: 0-8
		Match "abc" at positions: 12-14
		Regular expression: "(abc){2,}"
		Match "abcabcabc" at positions: 0-8
	*/

Pattern类还提供了一个static方法:`static boolean matches(String regex,CharSequence input)`，该方法用来检查regex是否匹配整个CharSequence类型的input参数；编译后的Pattern对象还提供了split()方法，它从匹配了regex的地方分割输入的字符串，放回分割后的字符串数组。

通过调用Pattern.matcher()方法，并且传入一个要匹配的字符序列参数，得到一个Matcher对象，使用Matcher对象的方法，我们能判断各种不同类型的匹配是否成功：`boolean matches()`、`boolean lookingAt()`、`boolean find()`、`boolean find(int start)`。其中matches()表示判断整个暑促字符是否匹配整个正则表达式模式，lookingAt()则用来判断该字符串(不必是整个字符串)的始部分是否能够匹配模式。

##### find()

Matcher.find()方法可以在charSequence中查找多个匹配，还可以从不同的起始位置进行查找匹配：Matcher.find(int start)。示例代码：

	package org.fmz.string ;

	import java.util.regex.* ;

	public class Finding{
		public static void main(String args[]){
			Matcher m = Pattern.compile("\\w+").matcher("Everything is full of the linnet's wings") ;
			while(m.find()){
				System.out.print(m.group() + " ") ;
			}
			System.out.println() ;
			int i = 0 ;
			while(m.find(i)){
				System.out.print(m.group() + " ") ;
				i ++ ;
			}
		}
	}/* Output:
		Everything is full of the linnet s wings
		Everything verything erything rything ything thing hing ing ng g is is s full full ull ll l of of f the the he e linnet
		linnet innet nnet net et t s s wings wings ings ngs gs s
	*/

##### 组(groups)

组是用括号划分的正则表达式。例如：A(B(C))D，则0表示ABCD组；1表示BC组；2表示C组。示例代码：

	package org.fmz.string ;

	import java.util.regex.* ;

	public class Groups{
		public static final String POEM = 
			"Twas brilling, and the slithy toves\n"+
			"Did gyre and gimble in the borogoves,\n" ;
		public static void main(String args[]){
			Matcher m = Pattern.compile("(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$").matcher(POEM) ;
			while(m.find()){
				for(int i=0;i<=m.groupCount();i++){
					System.out.print("[" + m.group(i) + " " + m.groupCount() +"]") ;
				}
			System.out.println() ;
			}
		}
	}/* Output:
		[the slithy toves 4][the 4][slithy toves 4][slithy 4][toves 4]
		[in the borogoves, 4][in 4][the borogoves, 4][the 4][borogoves, 4]
	*/

##### start()与end()

在匹配操作成功之后，start()放回先前匹配的位置的索引，而end()返回所匹配的最后字符的索引加一的值。

##### Pattern标记

Pattern类的compile()方法还有另外一个版本，它接受一个标记参数，以调整匹配的行为。

#### split()

split()将输入的字符串断开成字符串对象数组，断开的边界由正则表达式来确定：`String[] split(CharSequence input)`、`String[] split(CharSequence input, int limit)`

同String.split()方法类似：`String[] split(String regex)`、`String[] split(String regex, int limit)`。

示例代码：

	package org.fmz.string ;

	import java.util.* ;
	import java.util.regex.* ;

	public class SplitDemo{
		public static void main(String args[]){
			String input = "This!!unusual use!!of exclamation!!points" ;
			System.out.println(Arrays.toString(Pattern.compile("!!").split(input))) ;
			System.out.println(Arrays.toString(Pattern.compile("!!").split(input, 3))) ;//仅仅用于前三个split
			System.out.println(Arrays.toString(input.split("!!"))) ;
			System.out.println(Arrays.toString(input.split("!!", 3))) ;
		}
	}/* Output:
		[This, unusual use, of exclamation, points]
		[This, unusual use, of exclamation!!points]
		[This, unusual use, of exclamation, points]
		[This, unusual use, of exclamation!!points]
	*/

>一个java.util.Pattern.split()的静态方法，一个是java.lang.String.split()方法。前者需要编译正则表达式：`Static Pattern compile(String regex)`

#### 替换操作

待续...

#### reset()

待续...

#### 正则表达式与Java I/O

待续...

<h3 id="13.7">13.7 扫描输入</h3>

到目前为止，从文件或者标准输入读取数据还是一件相当痛苦的事情。一般的解决之道是读入一行文本，对其进行分词，然后使用各种解析方法来解析数据。示例代码：

	package org.fmz.string ;

	import java.io.* ;

	public class SimpleRead{
		public static BufferedReader input = new BufferedReader(new StringReader("Sir Robin of Camelot\n22 1.61803")) ;
		public static void main(String args[]){
			try{
				System.out.println("What's your name?") ;
				String name = input.readLine() ;
				System.out.println(name) ;
				System.out.println("How old are you? What is your favorite double?") ;
				System.out.println("(input: <age> <double>)") ;
				String numbers = input.readLine() ;
				System.out.println(numbers) ;
				String[] numberArray = numbers.split(" ") ;
				int age = Integer.parseInt(numberArray[0]) ;
				double favorite = Double.parseDouble(numberArray[1]) ;
				System.out.format("Hi %s.\n", name) ;
				System.out.format("In 5 years you will be %d.\n", age+5) ;
				System.out.format("My favorite double is %f.", favorite/2) ;
			}catch(IOException e){
				System.err.println("I/O exception") ;
			}
		}
	}

>StringBuffer将String转化为可读入的流对象，然后用这个流对象来构建StringBuffer对象。因为我们要使用StringBuffer中的readLine()方法。

>readLine()方法将一行输入转化为String对象。如果数据不再同一个行，那么我们还用对数据进行分解(split()方法)。

在Java SE5中增加了Scanner类，大大的减小了上述的负担。上述代码的更简洁的形式：

	package org.fmz.string ;

	import java.util.* ;

	public class BetterRead{
		public static void main(String args[]){
			Scanner scanner = new Scanner(SimpleRead.input) ;
			System.out.println("What's your name?") ;
			String name = scanner.nextLine() ;
			System.out.println(name) ;
			System.out.println("How old are you? What is your favorite double?") ;
			System.out.println("(input: <age> <double>)") ;
			int age = scanner.nextInt() ;
			double favorite = scanner.nextDouble() ;
			System.out.format("Hi %s.\n", name) ;
			System.out.format("In 5 years you will be %d.\n", age+5) ;
			System.out.format("My favorite double is %f.", favorite/2) ;
		}
	}/* Output:
		What's your name?
		Sir Robin of Camelot
		How old are you? What is your favorite double?
		(input: <age> <double>)
		Hi Sir Robin of Camelot.
		In 5 years you will be 27.
		My favorite double is 0.809015.
	*/
>Scanner构造器可以接收任何类型的输入对象，包括File对象、InputStream、String或者此例子中的Readable对象(javaSE5中新增加的一个对象，表示具有某种read()方法的东西)。

>Scanner普通的next()方法返回下一个字符串。所有的next()方法，只有在找到一个完整的分词之后才能够返回。Scanner还有相应的hasNext()方法，以判断下一个输入的分词是否所需的类型。

#### Scanner定界符

示例代码：

	package org.fmz.string ;

	import java.util.* ;

	public class ScannerDelimiter{
		public static void main(String args[]){
			Scanner scanner = new Scanner(" 13, 14, 18, 9, 44") ;
			scanner.useDelimiter("\\s*,\\s*") ;
			while(scanner.hasNext()){
				System.out.println(scanner.next()) ;
			}
			System.out.println("****************") ;
			scanner = new Scanner("13, 14, 18, 9, 44") ;
			//scanner.useDelimiter("\\s*,\\s*") ;
			while(scanner.hasNext()){
				System.out.println(scanner.next()) ;
			}
			System.out.println("****************") ;
			scanner = new Scanner("13, 14, 18, 9, 44") ;
			scanner.useDelimiter("\\s*,\\s*") ;
			while(scanner.hasNextInt()){
				System.out.println(scanner.nextInt()) ;
			}
			System.out.println("****************") ;
			scanner = new Scanner("13, 14, 18, 9, 44") ;
			scanner.useDelimiter("\\s*,\\s*") ;
			while(scanner.hasNext()){
				System.out.println(scanner.next()) ;
			}
		}
		}/* Output:
		 13
		14
		18
		9
		44
		****************
		13,
		14,
		18,
		9,
		44
		****************
		13
		14
		18
		9
		44
		****************
		13
		14
		18
		9
		44
	*/

>Scanner的默认分割符为空格`" "`；Scanner每一定界的时候就会把匹配分隔符的部分作为界(去掉，不作为待输出的一部分)；每次用Scanner都要声明分界符，不然就会是默认的空格分界符。

#### 用正则表达式扫描

除了能够扫描基本数据类型之外，你还可以用自定义的正则表达式进行扫描。示例代码：

	package org.fmz.string ;

	import java.util.* ;
	import java.util.regex.* ;

	public class ThreatAnalyzer{
		static String threatData = 
			"58.27.82.161@2015/10/21\n"+
			"23.27.82.161@2013/08/25\n"+
			"345.27.82.161@2012/10/25\n"+
			"22.27.82.161@2015/07/25\n"+
			"12.27.82.161@2011/10/22\n"+
			"[Next log selection with different data format]" ;

		public static void main(String args[]){
			Scanner scanner = new Scanner(threatData) ;
			String pattern = "(\\d+[.]\\d+[.]\\d+[.]+\\d+)@(\\d{4}/\\d{2}/\\d{2})" ;
			while(scanner.hasNext(pattern)){
				scanner.next(pattern) ;
				MatchResult mr = scanner.match() ;
				String ip = mr.group(1) ;
				String data = mr.group(2) ;
				System.out.format("Threats on %s from %s\n",data, ip) ;
			}
		}
	}/* Output:
		Threats on 2015/10/21 from 58.27.82.161
		Threats on 2013/08/25 from 23.27.82.161
		Threats on 2012/10/25 from 345.27.82.161
		Threats on 2015/07/25 from 22.27.82.161
		Threats on 2011/10/22 from 12.27.82.161
	*/

>next()方法配合指定的正则表达式使用的时候，将找到下一个匹配模式的输入部分。调用match()方法就可以获得匹配的结果。

>它仅仅针对下一个输入的分词进行匹配，如果你的正则表达式中含有定界符，永远都不可能匹配成功。

<h3 id="13.8">13.8 StringTokenizer</h3>

***

***

<h2 id="14">14 类型信息</h2>

运行时类型信息(RTTI)使得你可以在程序运行时发现和使用类型信息。Java是如何让我么在运行时识别对象和类型信息的。主要有两种方式：1，传统的RTTI，它假设我么在编译的时候已经知道了所有的类型；2，反射机制，它允许我们在运行时发现和使用类的信息。

<h3 id="14.1">14.1 为什么需要RTTI</h3>

基本的展示多态性的代码：

	package org.fmz.typeinfo ;

	import java.util.* ;

	abstract class Shape{
		void draw(){
			System.out.println(this + ".draw()") ;
		}
		abstract public String toString();//定义为抽象方法，可以强制继承者覆写该方法，并且可以防止对无格式的Shape的实例化(有抽象方法的类变成了抽象类)
	}

	class Circle extends Shape{
		public String toString(){
			return "Circle" ;
		}
	}

	class Square extends Shape{
		public String toString(){
			return "Square" ;
		}
	}

	class Triangle extends Shape{
		public String toString(){
			return "Triangle" ;
		}
	}

	public class Shapes {
		public static void main(String args[]){
			List<Shape> shapes = Arrays.asList(new Circle(), new Square(), new Triangle()) ;
			for(Shape shape : shapes){
				shape.draw() ;
			}
		}
	}/* Output:
	Circle.draw()
	Square.draw()
	Triangle.draw()
	*/

>RTTI：运行时，识别一个对象的类型。

>在Shape对象放入容器和遍历容器取出对象的时候，针对的只是Shape接口，而不知道具体的Shape类型。这一点是在Java编译的时候由容器和泛型来强制确保的。

>在程序运行的时候，由RTTI，才可以知道具体Shape类型，进而实现了多态性。

<h3 id="4.2">4.2 Class对象</h3>

要理解RTTI在java中工作原理，首先必须知道类型信息在运行时是如何表示的。这项工作由称为Class对象的特殊对象完成，它包含了与类有关的信息。

实际上Class对象就是用来创建类的常规对象的。Java使用Class对象那个来执行其RTTI，即使你正在执行的是类似转型这样的操作。

类是程序的一部分，每一个类都有一个Class对象。每当编写并编译了一个新的类，就会产生一个Class对象(被保存在同名的.class文件中)。为了生成这个类的对象，运行这个程序的java虚拟机(JVM)将使用被称为类加载器的子系统。

类加载器子系统实际上可以包含一条类加载器链，但是只有一个原生类加载器，它是JVM实现的一部分。原生类加载器加载的是所谓的可信类，包括Java API的类，它们通常是从本地盘中加载的。在这条链中，通常不需要添加额外的类加载器，但是如果你有特殊的要求(例如以某种特殊的方式加载类，以支持Web服务器的应用，或者在网络中下载的类)，那么有一种方式可以挂接额外的类加载器。

所有的类都是在对其第一使用的时候，动态加载到JVM中的。当程序第一次创建对类的静态成员的引用时，就会加载这个类。这证明构造器也是类的静态方法，即使在构造器之前没有显示的使用static关键字。因此使用new操作符创建类的新对象的时候也被当做对类的静态成员的引用。

因此，Java程序在它开始运行之前并非被完全加载，其各个部分是在必须的时候才加载的。这一点与许多传统的语言都不相同。

类加载器首先检查这个类的Class对象是否被加载。如果尚未被加载，默认的类加载器就会根据类名查找.class文件。在这个类的字节码被加载的时候，它们会接受验证，以确保其没有被破坏，并且不包含不良的Java代码(这是Java中用于安全防范目的的措施之一)

一旦某个类的Class对象别载入内存，它就被用来创建这个类的所有对象。示例代码：

	package org.fmz.typeinfo ;

	class Candy{
		static{
			System.out.println("Loading Candy") ;
		}
	}

	class Cookie{
		static{
			System.out.println("Loading Cookie") ;
		}
	}

	public class SweetShop{
		public static void main(String args[]){
			System.out.println("inside main") ;
			new Candy() ;
			System.out.println("After creating Candy") ;
			try{
				Class.forName("org.fmz.typeinfo.Gum") ;//此处必须说明具体的包和类(能从path路径上搜索到)
			}catch(ClassNotFoundException e){
				System.out.println("Couldn't find Gum") ;
			}
			System.out.println("After Class.forName()(\"Gum\")") ;
			new Cookie() ;
			System.out.println("After creating Cookie") ;
		}
	}/* Output:
		inside main
		Loading Candy
		After creating Candy
		Loading Gum
		After Class.forName()("Gum")
		Loading Cookie
		After creating Cookie
	*/

当Gum这个类如果加载之后，Class.forName()就不会在加载这个类了(只加载一次)。实例代码：

	package org.fmz.typeinfo ;

	class Candy{
		static{
			System.out.println("Loading Candy") ;
		}
	}

	class Cookie{
		static{
			System.out.println("Loading Cookie") ;
		}
	}

	public class SweetShop{
		public static void main(String args[]){
			System.out.println("inside main") ;
			new Candy() ;
			System.out.println("After creating Candy") ;
			new Gum() ;
			System.out.println("After creating Gum") ;
			//如果Gum类已经加载过了，则不会执行以下代码
			try{
				Class.forName("org.fmz.typeinfo.Gum") ;//此处必须说明具体的包和类(能从path路径上搜索到)
			}catch(ClassNotFoundException e){
				System.out.println("Couldn't find Gum") ;
			}
			System.out.println("After Class.forName()(\"Gum\")") ;
			new Cookie() ;
			System.out.println("After creating Cookie") ;
		}
	}/* Output:
		inside main
		Loading Candy
		After creating Candy
		Loading Gum
		After creating Gum
		After Class.forName()("Gum")
		Loading Cookie
		After creating Cookie
	*/

>从输出中可以看出来：Class对象仅在需要是时候才会被加载，static初始化是在类加载的时候进行的。

#### Class.forName()

forName()方法是Class类的一个static成员。Class对象(既然是一个类，肯定就有对象)和其他的对象一样，我么可以获取并操作它的引用(这也就是类加载器的工作)。forName()是取得Class对象应用的一种方法，它的参数是Sting的class文件的名称(不包括.class后缀)，返回的是一个Class对象的引用。对forName()的调用是为了它的副作用：如果Gum类没有被加载就加载它。在加载的过程中static域就会初始化。

示例代码：

	package org.fmz.typeinfo ;

	interface HasBatteries{}

	interface Waterproof{}

	interface  Shoots{}

	class Toy{
		Toy(){}
		Toy(int i){}
	}

	class FancyToy extends Toy implements HasBatteries, Waterproof, Shoots{
		FancyToy(){
			super(1) ;
		}
	}

	public class ToyTest{
		static void printInfo(Class cc){
			System.out.println("Class name: " + cc.getName() +
				" is Interface? [" + cc.isInterface() + "]") ;
			System.out.println("Simple name: " + cc.getSimpleName()) ;
			System.out.println("Canonical name: " + cc.getCanonicalName()) ;
		}

		public static void main(String args[]){
			Class c = null ;
			try{
				c = Class.forName("org.fmz.typeinfo.FancyToy") ;
			}catch(ClassNotFoundException e){
				System.out.println("Can't find FancyToy") ;
				System.exit(1) ;
			}
			printInfo(c) ;
			for(Class face : c.getInterfaces()){
				printInfo(face) ;
			}
			Class up = c.getSuperclass() ;
			Object obj = null ;
			try{
				obj = up.newInstance() ;
			}catch(InstantiationException e){
				System.out.println("Cannot instantiate") ;
				System.exit(1) ;
			}catch(IllegalAccessException e){
				System.out.println("Cannot access") ;
				System.exit(1) ;
			}
			printInfo(obj.getClass()) ;
		}
	}/* Output:
		Class name: org.fmz.typeinfo.FancyToy is Interface? [false]
		Simple name: FancyToy
		Canonical name: org.fmz.typeinfo.FancyToy
		Class name: org.fmz.typeinfo.HasBatteries is Interface? [true]
		Simple name: HasBatteries
		Canonical name: org.fmz.typeinfo.HasBatteries
		Class name: org.fmz.typeinfo.Waterproof is Interface? [true]
		Simple name: Waterproof
		Canonical name: org.fmz.typeinfo.Waterproof
		Class name: org.fmz.typeinfo.Shoots is Interface? [true]
		Simple name: Shoots
		Canonical name: org.fmz.typeinfo.Shoots
		Class name: org.fmz.typeinfo.Toy is Interface? [false]
		Simple name: Toy
		Canonical name: org.fmz.typeinfo.Toy
	*/

>Class的newInstance()方法是实现虚拟构造器的一种途径。虚拟构造器允许你声明：我不知道你的确切类型，但是无论如何要正确的创建你自己。

>在上例中，up仅仅是一个Class的引用，在编译器不具备更进一步的类型信息。当你创建新的实例的时候，会得到Object的引用，但是这个引用是执行Toy对象的。

>使用newInstance来创建的类，不许带有默认的构造器。

#### 类字面常量

java中还提供了另外的一种方法来生成对Class对象的引用，即用类字面常量。例如：`FancyToy.class`

当使用`.class`来创建对Class对象的引用时，不会自动的初始化该Class对象。

为了使用类而做的准备工作实际上包括三个步骤：

1. 加载，这是由类加载器执行的。该步骤将查找字节码(通常在claspath所指定的路径中查找，但这并非是必须的)，并从这些字节码中创建一个Class对象。

2. 链接，在链接阶段将验证类中的字节码，为静态域非配储存空间，并且如果必须的话，将解析这个类创建的对其他类的所有引用。

3. 初始化，如果该类具有超类，则对其进行初始化，执行静态初始化器和静态初始化块。

初始化被延迟到了对静态方法(构造器时隐式的静态方法)或者非常数静态域进行首次引用时才执行。

示例代码：

	package org.fmz.typeinfo ;

	import java.util.* ;

	class Initable{
		static final int staticFinal = 47 ;
		static final int staticFinal2 = ClassInitialization.rand.nextInt(1000) ;
		static{
			System.out.println("Initializing Initable") ;
		}
	}

	class Initable2{
		static int staticNoFinal = 147 ;
		static{
			System.out.println("Initializing Initable2") ;
		}
	}

	class Initable3{
		static int staticNoFinal = 74 ;
		static{
			System.out.println("Initializing Initable3") ;
		}
	}

	public class ClassInitialization{
		public static Random rand = new Random(47) ;
		public static void main(String args[])throws Exception{
			System.out.println("******\\*.class进行加载******") ;
			Class initable = Initable.class ;
			System.out.println("After creating Initable ref") ;
			System.out.println("******对static-final常量进行访问******") ;
			System.out.println(Initable.staticFinal) ;
			System.out.println("******对static-final，但是非常量记性访问******") ;
			System.out.println(Initable.staticFinal2) ;
			System.out.println("******对static，但是非final常量访问******") ;
			System.out.println(Initable2.staticNoFinal) ;
			System.out.println("******Class.forName()进行加载******") ;
			Class initable3 = Class.forName("org.fmz.typeinfo.Initable3") ;
			System.out.println("******对static，但是非final常量访问******") ;
			System.out.println("After creating Initable3 ref") ;
			System.out.println(Initable3.staticNoFinal) ;
		}
	}/* Output:
		******\*.class进行加载******
		After creating Initable ref
		******对static-final常量进行访问******
		47
		******对static-final，但是非常量记性访问******
		Initializing Initable
		258
		******对static，但是非final常量访问******
		Initializing Initable2
		147
		******Class.forName()进行加载******
		Initializing Initable3
		******对static，但是非final常量访问******
		After creating Initable3 ref
		74
	*/

>使用`.class`语法获得对类的引用不会发生初始化。但是为了获得类的引用，使用Class.forName()方法，就会立刻进行初始化。

>如果static final值是编译期常量，那么这个值不需要对Initable类进行初始化就能够读取；但是只是将一个域设置为static final，将强制进行类的初始化，因为它不是一个编译常量。

>如果是一个static但不是final的域，那么对它进行访问时，总是要求它在被读取之前，首先进行链接(为这个域分配储存空间)和初始化(初始化该储存空间)。

#### 泛化的Class引用

Class引用总是指向一个Class对象，它可以制造类的实例，并包含可作用于这些实例的所有方法代码。它还包含该类的静态成员，因此Class引用表示的就是它所指向的对象的确切类型，而该对象就是Class类的一个对象。

泛化的Class。示例代码：

	package org.fmz.typeinfo ;

	public class GenericClassReferences{
		public static void main(String args[]){
			Class intClass = int.class ;
			Class<Integer> genericIntClass = int.class ;
			genericIntClass = Integer.class ;
			intClass = double.class;
			genericIntClass = double.class ;
		}
	}/* Output:
	GenericClassReferences.java:9: 错误: 不兼容的类型: Class<Double>无法转换为Class<Integer>
			genericIntClass = double.class ;
						^
	1 个错误
	*/

解决办法：使用通配符`?`

	package org.fmz.typeinfo ;

	public class WildcardClassReferences{
		public static void main(String args[]){
			Class<?> intClass = int.class;
			intClass = double.class;
		}
	}

>Class<?>的好处是它表示你并非是碰巧或者疏忽，而使用了一个非具体类的引用，而是你选择了非具体的版本。

为了创建一个Class引用，它被限定为某种类型，或者该类型的子类型，你需要通配符与extends关键字结合。示例代码：

	package org.fmz.typeinfo ;

	public class BoundedClassReferences{
		public static void main(String args[]){
			Class<? extends Number> bounded = int.class;
			bounded = double.class;
			bounded = Number.class;
			//Or everything else derived form Number
		}
	}

泛型类语法。示例代码：

	package org.fmz.typeinfo ;

	import java.util.* ;

	class CountedInteger{
		private static long counter ;
		private final long id = counter++ ;
		public String toString(){
			return Long.toString(id) ;
		}
	}

	public class FilledList<T>{
		private Class<T> type ;
		public FilledList(Class<T> type){
			this.type = type ;
		}
		public List<T> create(int nElements){
			List<T> result = new ArrayList<T>() ;
			try{
				for(int i=0;i<nElements;i++){
					result.add(type.newInstance()) ;
				}
			}catch(Exception e){
				throw new RuntimeException() ;
			}
			return result ;
		}

		public static void main(String args[]){
			FilledList<CountedInteger> fl = new FilledList<CountedInteger>(CountedInteger.class) ;
			System.out.println(fl.create(15)) ;
		}
	}/* Output:
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
	*/

#### 新的转型语法：cast()方法

待续...

<h3 id="14.3">14.3 类型转换前先做检查</h3>

我们知道的RTTI形式包括：

1. 传统的类型转换。如Shape，由RTTI确保类型装换的正确性，如果执行了一条错误的类型转换，就会抛出一个`ClassCastException`异常

2. 代表对象类型的Class对象。通过查询Class对象可以获取运行时所需要的信息。

3. 关键字instanceof。它返回一个boolean值，告诉我们对象是不是某个特定类型的实例

应用instanceof完成一个Pet。示例代码：

	package org.fmz.typeinfo.pets;

	public class Individual implements Comparable<Individual> {
	  private static long counter = 0;
	  private final long id = counter++;
	  private String name;
	  public Individual(String name) { this.name = name; }
	  // 'name' is optional:
	  public Individual() {}
	  public String toString() {
	    return getClass().getSimpleName() +
	      (name == null ? "" : " " + name);
	  }
	  public long id() { return id; }
	  public boolean equals(Object o) {
	    return o instanceof Individual &&
	      id == ((Individual)o).id;
	  }
	  public int hashCode() {
	    int result = 17;
	    if(name != null)
	      result = 37 * result + name.hashCode();
	    result = 37 * result + (int)id;
	    return result;
	  }
	  public int compareTo(Individual arg) {
	    // Compare by class name first:
	    String first = getClass().getSimpleName();
	    String argFirst = arg.getClass().getSimpleName();
	    int firstCompare = first.compareTo(argFirst);
	    if(firstCompare != 0)
	    return firstCompare;
	    if(name != null && arg.name != null) {
	      int secondCompare = name.compareTo(arg.name);
	      if(secondCompare != 0)
		return secondCompare;
	    }
	    return (arg.id < id ? -1 : (arg.id == id ? 0 : 1));
	  }
	} ///:~

>Individual.java

	package org.fmz.typeinfo.pets ;

	public class Person extends Individual{
		public Person(String name){
			super(name) ;
		}
	}

>Person.java

	package org.fmz.typeinfo.pets ;

	public class Pet extends Individual{
		public Pet(String name){
			super(name) ;
		}
		public Pet(){
			super() ;
		}
	}

>Pet.java

	package org.fmz.typeinfo.pets ;

	public class Dog extends Pet{
		public Dog(String name){
			super(name) ;
		}
		public Dog(){
			super() ;
		}
	}

>Dog.java

	package org.fmz.typeinfo.pets ;

	public class Mutt extends Dog{
		public Mutt(String name){
			super(name) ;
		}
		public Mutt(){
			super() ;
		}
	}

>Mutt.java

	package org.fmz.typeinfo.pets ;

	public class Pug extends Dog{
		public Pug(String name){
			super(name) ;
		}
		public Pug(){
			super() ;
		}
	}

>Pug.java

	package org.fmz.typeinfo.pets ;

	public class Cat extends Pet{
		public Cat(String name){
			super(name) ;
		}
		public Cat(){
			super() ;
		}
	}

>Cat.java

	package org.fmz.typeinfo.pets ;

	public class EgyptianMau extends Cat{
		public EgyptianMau(String name){
			super(name) ;
		}
		public EgyptianMau(){
			super() ;
		}
	}

>EgyptianMau.java

	package org.fmz.typeinfo.pets ;

	public class Manx extends Cat{
		public Manx(String name){
			super(name) ;
		}
		public Manx(){
			super() ;
		}
	}

>Manx.java

	package org.fmz.typeinfo.pets ;

	public class Cymric extends Manx{
		public Cymric(String name){
			super(name) ;
		}
		public Cymric(){
			super() ;
		}
	}

>Cymric.java

	package org.fmz.typeinfo.pets ;

	public class Rodent extends Pet{
		public Rodent(String name){
			super(name) ;
		}
		public Rodent(){
			super() ;
		}
	}

>Rodent.java

	package org.fmz.typeinfo.pets ;

	public class Rat extends Rodent{
		public Rat(String name){
			super(name) ;
		}
		public Rat(){
			super() ;
		}
	}

>Rat.java

	package org.fmz.typeinfo.pets ;

	public class Mouse extends Rodent{
		public Mouse(String name){
			super(name) ;
		}
		public Mouse(){
			super() ;
		}
	}

>Mouse.java

	package org.fmz.typeinfo.pets ;

	public class Hamster extends Rodent{
		public Hamster(String name){
			super(name) ;
		}
		public Hamster(){
			super() ;
		}
	}

>Hamster.java

	package org.fmz.typeinfo.pets ;

	import java.util.* ;

	public abstract class PetCreator{
		private Random rand = new Random(47) ;
		public abstract List<Class<? extends Pet>> types() ;
		public Pet randomPet(){
			int n = rand.nextInt(types().size()) ;
			try{
				return types().get(n).newInstance() ;
			}catch(InstantiationException e){
				throw new RuntimeException(e) ;
			}catch(IllegalAccessException e){
				throw new RuntimeException(e) ;
			}
		}

		public Pet[] createArray(int size){
			Pet[] result = new Pet[size] ;
			for(int i=0;i<size;i++){
				result[i] = randomPet() ;
			}
			return result ;
		}

		public ArrayList<Pet> arrayList(int size){
			ArrayList<Pet> result = new ArrayList<Pet>() ;
			Collections.addAll(result, createArray(size)) ;
			return result ;
		}
	}

>PetCreator.java

	package org.fmz.typeinfo.pets ;

	import java.util.* ;

	public class ForNameCreator extends PetCreator{
		private static List<Class<? extends Pet>> types = new ArrayList<Class<? extends Pet>>() ;
		private static String[] typenames = {
			"org.fmz.typeinfo.pets.Mutt",
			"org.fmz.typeinfo.pets.Pug",
			"org.fmz.typeinfo.pets.EgyptianMau",
			"org.fmz.typeinfo.pets.Manx",
			"org.fmz.typeinfo.pets.Cymric",
			"org.fmz.typeinfo.pets.Rat",
			"org.fmz.typeinfo.pets.Mouse",
			"org.fmz.typeinfo.pets.Hamster",
		} ;
		@SuppressWarnings("unckecked")
		private static void loader(){
			try{
				for(String name : typenames){
					types.add(
						(Class<? extends Pet>)Class.forName(name)) ;
				}
			}catch(ClassNotFoundException e){
				throw new RuntimeException(e) ;
			}
		}

		static {
			loader() ;
		}

		public List<Class<? extends Pet>> types(){
			return types ;
		}
	}

>ForNameCreator.java

	package org.fmz.typeinfo.pets ;

	import java.util.* ;

	public class PetCount{
		static class PetCounter extends HashMap<String, Integer>{
			public void count(String type){
				Integer quantity = get(type) ;
				if(quantity == null){
					put(type, 1) ;
				}else{
					put(type, quantity + 1) ;
				}
			}
		}

		public static void countPets(PetCreator creator){
			PetCounter counter = new PetCounter() ;
			for(Pet pet : creator.createArray(20)){
				System.out.print(pet.getClass().getSimpleName() + " ") ;
				if(pet instanceof Pet){
					counter.count("Pet") ;
				}
				if(pet instanceof Dog){
					counter.count("Dog") ;
				}
				if(pet instanceof Mutt){
					counter.count("Mutt") ;
				}
				if(pet instanceof Pug){
					counter.count("Pug") ;
				}
				if(pet instanceof Cat){
					counter.count("Cat") ;
				}
				if(pet instanceof Manx){
					counter.count("Manx") ;
				}
				if(pet instanceof Cymric){
					counter.count("Cymric") ;
				}
				if(pet instanceof Rodent){
					counter.count("Rodent") ;
				}
				if(pet instanceof Rat){
					counter.count("Rat") ;
				}
				if(pet instanceof Mouse){
					counter.count("Mouse") ;
				}
				if(pet instanceof Hamster){
					counter.count("Hamster") ;
				}
			}
			System.out.println() ;
			System.out.println(counter) ;
		}

		public static void main(String args[]){
			countPets(new ForNameCreator()) ;
		}
	}/* Output:
		Rat Manx Cymric Mutt Pug Cymric Pug Manx Cymric Rat EgyptianMau Hamster EgyptianMau Mutt Mutt Cymric Mouse Pug Mouse Cym
		ric
		{Pug=3, Rat=2, Cymric=5, Mouse=2, Cat=9, Manx=7, Rodent=5, Mutt=3, Dog=6, Pet=20, Hamster=1}
	*/

>PetCount.java

#### 使用类字面常量

待续...

#### 动态的instanceof

待续...

#### 递归计数

待续...

<h3 id="14.4>14.4 注册工厂</h3>

待续...

<h3 id="14.5">14.5 instanceof 与 Class的等价性</h3>

<h3 id="14.6">14.6 反射：运行时的类型信息</h3>

待续...

<h3 id="14.7">14.7 动态代理</h3>

<h3 id="14.8">14.8 空对象</h3>

<h3 id="14.9">14.9 接口与类型信息</h3>

***

***

<h2 id="15">15 泛型</h2>

一般的类和方法，只能使用具体的类型：要么是基本类型，要么是自定义的类。如果要编写可以应用与多种类型的代码，这种刻板的限制对代码的束缚就会很大。

<h3 id="15.1">15.1 与C++比较</h3>

Java的设计者曾将说过，设计这门语言的灵感主要来自于C++。

<h3 id="5.2">5.2 简单泛型</h3>

示例代码：

	package org.fmz.generics ;

	class Automobile{

	}

	public class Holder1{
		private Automobile a ;
		public Holder1(Automobile a){
			this.a = a ;
		}
		Automobile get(){
			return a ;
		}
	}

>容器只能储存一种类型：Automobile

示例代码：

	package org.fmz.generics ;

	public class Holder2{
		private Object a ;
		public Holder2(Object a){
			this.a = a ;
		}
		public void set(Object a){
			this.a = a ;
		}
		public Object get(){
			return a ;
		}
		public static void main(String args[]){
			Holder2 h2 = new Holder2(new Automobile()) ;
			Automobile a = (Automobile)h2.get() ;
			h2.set("Not an Automobile") ;
			String s = (String)h2.get() ;
			h2.set(1) ;
			Integer x = (Integer)h2.get() ;
		}
	}

>容器先后储存了Automobile、String、Integer类型

有些情况下，我么确实希望容器能够同时持有多种类型的对象。但是，通常而言，我们只会使用容器来储存一种类型的对象。泛型的主要目的之一就是用来指定容器要持有什么类型的对象，而且由编译器来确保类型的正确性。

示例代码：

	package org.fmz.generics ;

	public class Holder3<T>{
		private T a ;
		public Holder3(T a){
			this.a = a ;
		}
		public void set(T a){
			this.a = a ;
		}
		public T get(){
			return a ;
		}

		public static void main(String args[]){
			Holder3<Automobile> h3 = new Holder3<Automobile>(new Automobile()) ;
			Automobile a = h3.get() ;
			//h3.set("Not a Automobile") ;//can not compile
		}
	}

>上述代码完成了：我们暂时不指定是什么样的类型，而是稍后再决定具体使用什么类型。为了达到这个目的要使用类型参数，用尖括号括住，放在类名的后面。然后在使用这个类的时候，再用实际的类型区替换此类型。

>上例中，`T`就是类型参数

Java泛型的核心概念：告诉编译器想要使用什么样的类型，然后编译器帮你处理一切细节。

#### 一个元组类库

元组(tuple)：将一组对象直接打包储存于其中的一个单个对象。这个容器对象允许你读取其中的元素，但是不允许向其中存放新的对象。

元组示例代码：

	package org.fmz.generics ;

	public class TwoTuple<A, B>{
		public final A first ;
		public final B second ;
		public TwoTuple(A a, B b){
			first = a ;
			second = b ;
		}
		public String toString(){
			return "(" + first + ", " + second + ")" ;
		}
	}

>TwoTuple.java

	package org.fmz.generics ;

	public class ThreeTuple<A, B, C> extends TwoTuple<A, B>{
		public final C third ;
		public ThreeTuple(A a, B b, C c){
			super(a, b) ;
			third = c ;
		}
		public String toString(){
			return "(" + first + ", " + second + ", " + third + ")" ;
		}
	}

>ThreeTuple.java

	package org.fmz.generics ;

	public class FourTuple<A, B, C, D> extends ThreeTuple<A, B, C>{
		public final D fourth ;
		public FourTuple(A a, B b, C c, D d){
			super(a, b, c) ;
			fourth = d ;
		}
		public String toString(){
			return "(" + first + ", " + second + ", " + third + ", " + fourth + ")" ;
		}
	}

>FourTuple.java

	package org.fmz.generics ;

	public class FiveTuple<A, B, C, D, E> extends FourTuple<A, B, C, D>{
		public final E fifth ;
		public FiveTuple(A a, B b, C c, D d, E e){
			super(a, b, c, d) ;
			fifth = e ;
		}
		public String toString(){
			return "(" + first + ", " + second + ", " + third + ", " + fourth + ", " + fifth + ")" ;
		}
	}

>FiveTuple.java

	package org.fmz.generics ;

	class Amphibian{
	}

	class Vehicle{
	}

	public class TupleTest{
		static TwoTuple<String, Integer> f(){
			return new TwoTuple<String, Integer>("hi", 47) ;
		}
		static ThreeTuple<Amphibian, String, Integer> g(){
			return new ThreeTuple<Amphibian, String, Integer>(new Amphibian(), "hi", 47) ;
		}
		static FourTuple<Vehicle, Amphibian, String, Integer> h(){
			return new FourTuple<Vehicle, Amphibian, String, Integer>(new Vehicle(), new Amphibian(), "hi", 47) ;
		}
		static FiveTuple<Vehicle, Amphibian, String, Integer, Double> k(){
			return new FiveTuple<Vehicle, Amphibian, String, Integer, Double>(new Vehicle(), new Amphibian(), "hi", 47, 11.1) ;
		}

		public static void main(String args[]){
			TwoTuple<String, Integer> ttsi = f() ;
			System.out.println(ttsi) ;
			System.out.println(g()) ;
			System.out.println(h()) ;
			System.out.println(k()) ;
		}
	}/* Output:
		(hi, 47)
		(org.fmz.generics.Amphibian@659e0bfd, hi, 47)
		(org.fmz.generics.Vehicle@2a139a55, org.fmz.generics.Amphibian@15db9742, hi, 47)
		(org.fmz.generics.Vehicle@6d06d69c, org.fmz.generics.Amphibian@7852e922, hi, 47, 11.1)
	*/

>TupleTest.java，元组的使用。

#### 一个堆栈类

实例代码：

	package org.fmz.generics ;

	public class LinkedStack<T>{
		private static class Node<U>{
			U item ;
			Node<U> next ;
			Node(){
				item = null ;
				next = null ;
			}
			Node(U item, Node<U> next){
				this.item = item ;
				this.next = next ;
			}
			boolean end(){
				return item == null && next == null ;
			}
		}
		private Node<T> top = new Node<T>() ;
		public void push(T item){
			top = new Node<T>(item, top) ;
		}
		public T pop(){
			T result = top.item ;
			if(!top.end()){
				top = top.next ;
			}
			return result ;
		}

		public static void main(String args[]){
			LinkedStack<String> lss = new LinkedStack<String>() ;
			for(String s : "Phasers on stun!".split(" ")){
				lss.push(s) ;
			}
			String s ;
			while((s = lss.pop()) != null){
				System.out.println(s) ;
			}
		}
	}

>这个例子使用了一个末端哨兵(end sentinel)来判断堆栈何时为空。这个末端哨兵是在构架LinkedStatic时创建的。每次调用push()方法就会创建一个Node<T>对象，并且将其链接到前一个Node对象。当调用pop()方法的时候，总是会返回top.item，然后丢弃当前top所指的Node<T>，并将top转移到下一个Node<T>，除非你已经碰到了末端哨兵，这时候就不再移动top了。如果已经到达了末端，客户端程序还继续调用pop()方法，只能得到null。说明这个栈已经空了。

#### RandomList

示例代码：

	package org.fmz.generics ;

	import java.util.* ;

	public class RandomList<T>{
		private ArrayList<T> storage = new ArrayList<T>() ;
		private Random rand = new Random(47) ;
		public void add(T item){
			storage.add(item) ;
		}
		public T select(){
			return storage.get(rand.nextInt(storage.size())) ;
		}

		public static void main(String args[]){
			RandomList<String> rls = new RandomList<String>() ;
			for(String s : "The quick brow fox jumped over the lazy brown dog".split(" ")){
				rls.add(s) ;
			}
			for(int i=0;i<11;i++){
				System.out.print(rls.select() + " ") ;
			}
		}
	}/* Output:
		brown over fox quick quick dog brown The brow lazy brown
	*/

<h3 id="15.3">15.3 泛型接口</h3>

泛型也可以应用于接口。例如生成器(generator)，这是一种转么负责创建对象的类。随机生成器的完整示例代码：

	package org.fmz.generics ;

	interface Generator<T>{
		T next() ;
	}

>Generator.java，泛型接口。

	package org.fmz.generics ;

	public class Coffee{
		private static long counter = 0 ;
		private final long id = counter ++ ;
		public String toString(){
			return getClass().getSimpleName() + " " + id ;
		}
	}

>Coffee.java，具有典型的计数功能

	package org.fmz.generics ;

	public class Latte extends Coffee{
	}

>Latte.java，Latte咖啡

	package org.fmz.generics ;

	public class Mocha extends Coffee{
	}

>Mocha.java，Mocha咖啡

	package org.fmz.generics ;

	public class Americano extends Coffee{
	}

>Americano.java，Americano咖啡

	package org.fmz.generics ;

	public class Breve extends Coffee{
	}

>Breve.java，Breve咖啡

	package org.fmz.generics ;

	public class Cappuccino extends Coffee{
	}

>Cappuccino.java，卡布奇诺咖啡

	package org.fmz.generics ;

	import java.util.* ;

	public class CoffeeGenerator implements Generator<Coffee>, Iterable<Coffee>{
		private Class[] types = {Latte.class, Mocha.class, Cappuccino.class, Americano.class, Breve.class} ;
		private static Random rand = new Random(47) ;
		public CoffeeGenerator(){}
		//for iteration
		private int size = 0 ;
		public CoffeeGenerator(int size){
			this.size = size ;
		}
		public Coffee next(){
			try{
				return (Coffee)types[rand.nextInt(types.length)].newInstance() ;
			}catch(Exception e){
				throw new RuntimeException(e) ;
			}
		}

		class CoffeeIterator implements Iterator<Coffee>{
			int count = size ;
			public boolean hasNext(){
				return count > 0 ;
			}
			public Coffee next(){
				count -- ;
				return CoffeeGenerator.this.next() ;
			}
			public void remove(){
				throw new UnsupportedOperationException() ;
			}
		};

		public Iterator<Coffee> iterator(){
				return new CoffeeIterator() ;
		}

		public static void main(String args[]){
			CoffeeGenerator gen = new CoffeeGenerator() ;
			for(int i=0;i<5;i++){
				System.out.println(gen.next()) ;
			}
			for(Coffee c : new CoffeeGenerator(5)){
				System.out.println(c) ;
			}
		}
	}/* Output:
		Americano 0
		Latte 1
		Americano 2
		Mocha 3
		Mocha 4
		Breve 5
		Americano 6
		Latte 7
		Cappuccino 8
		Cappuccino 9
	*/

>CoffeeGenerator.java，咖啡生成器

斐波那契(fibonacci)生成器示例代码：

	package org.fmz.generics ;

	public class Fibonacci implements Generator<Integer>{
		private int count = 0 ;
		public Integer next(){
			return fib(count++) ;
		}
		private int fib(int n){
			if(n < 2){
				return 1 ;
			}
			return fib(n-2) + fib(n-1) ;
		}

		public static void main(String args[]){
			Fibonacci gen = new Fibonacci() ;
			for(int i=0;i<18;i++){
				System.out.print(gen.next() + " ") ;
			}
		}
	}/* Output:
		1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584
	*/

编写一个实现Iterable的Fibonacci的生成器(适配器模式)

	package org.fmz.generics ;

	import java.util.* ;

	public class IterableFibonacci extends Fibonacci implements Iterable<Integer>{
		private int n ;
		public IterableFibonacci(int n){
			this.n = n ;
		}
		public Iterator<Integer> iterator(){
			return new Iterator<Integer>(){
				public boolean hasNext(){
					return n > 0 ;
				}
				public Integer next(){
					n -- ;
					return IterableFibonacci.this.next() ;
				}
				public void remove(){
					throw new UnsupportedOperationException() ;
				}
			};
		}

		public static void main(String args[]){
			for(Integer i : new IterableFibonacci(18)){
				System.out.print(i + " ") ;
			}
		}
	}/* Output:
		1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584
	*/

<h3 id="15.4">15.4 泛型方法</h3>

泛型可以应用于整个类上。同样也可以在类中包含参数化方法，而这个方法所在的类可以是泛型，也可以不是泛型。也就是说是否拥有泛型方法，与其所在的类是否是泛型没有关系。

泛型方法使得该方法能够独立于类而产生变化。

基本指导原则：无论何时，只要你能做到，就应该尽量使用泛型方法。也就是说，如果使用泛型方法可以取代将整个类的泛型化，那么就应该只使用泛型方法，因为它可以使事物更清楚明白。

简单的泛型方法示例：

	package org.fmz.generics ;

	public class GenericMethods{
		public <T> void f(T x){
			System.out.println(x.getClass().getName()) ;
		}
		public static void main(String args[]){
			GenericMethods gm = new GenericMethods() ;
			gm.f("") ;
			gm.f(1) ;
			gm.f(1.0) ;
			gm.f(1.0f) ;
			gm.f('c') ;
			gm.f(gm) ;
		}
	}/* Output:
		java.lang.String
		java.lang.Integer
		java.lang.Double
		java.lang.Float
		java.lang.Character
		org.fmz.generics.GenericMethods
	*/

>当使用泛型类的时候，必须在创建对象的时候指定类型参数的值，而使用泛型方法的时候，通常不必指明参数类型，因为编译器会为我们找出具体的类型。这称为：类型参数推断。

#### 杠杆利用类型参数推断

待续...

#### 可变参数与泛型方法

示例代码：

	package org.fmz.generics ;

	import java.util.* ;

	public class GenericVarargs{
		public static <T> List<T> makeList(T... args){
			List<T> result = new ArrayList<T>() ;
			for(T item : args){
				result.add(item) ;
			}
			return result ;
		}

		public static void main(String args[]){
			List<String> ls = makeList("A") ;
			System.out.println(ls) ;
			ls = makeList("ABCDEFGHIJKLMNOPQRST".split("")) ;
			System.out.println(ls) ;
		}
	}/* Output:
		[A]
		[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
	*/

#### 用于Generator的泛型方法

示例代码：

	package org.fmz.generics ;

	import java.util.* ;

	public class Generators{
		public static <T> Collection<T> fill(Collection<T> coll, Generator<T> gen, int n){
			for(int i=0;i<n;i++){
				coll.add(gen.next()) ;
			}
			return coll ;
		}
		public static void main(String args[]){
			Collection<Coffee> coll = fill(new ArrayList<Coffee>(), new CoffeeGenerator(), 4) ;
			for(Coffee c : coll){
				System.out.println(c) ;
			}
			Collection<Integer> fnumbers = fill(new ArrayList<Integer>(), new Fibonacci(), 12) ;
			for(int i : fnumbers){
				System.out.print(i + " ") ;
			}
		}
	}/* Output:
		Americano 0
		Latte 1
		Americano 2
		Mocha 3
		1 1 2 3 5 8 13 21 34 55 89 144
	*/

#### 一个通用的Generator

	package org.fmz.generics ;

	public class BasicGenerator<T> implements Generator<T>{
		private Class<T> type ;
		public BasicGenerator(Class<T> type){
			this.type = type ;
		}
		public T next(){
			try{
				return type.newInstance() ;
			}catch(Exception e){
				throw new RuntimeException(e) ;
			}
		}

		public static <T> Generator<T> create(Class<T> type){
			return new BasicGenerator<T>(type) ;
		}
	}

>这个类提供了一个基本实现，用于生成某个类的对象。这个类必须具备两个特点：1，必须声明为public；2，必须具有默认的构造器(无参数的构造器)。

简单类示例代码：

	package org.fmz.generics ;

	public class CountObject{
		private static long counter = 0 ;
		private final long id = counter ++ ;
		public long id(){
			return id ;
		}
		public String toString(){
			return "CountObject" + id ;
		}
	}

>CountObject能够记录下来创建了多少个CountObject实例，并通过toString()方法告诉我们其编号。

使用BasicGenerator很容易的为CountObject创建一个Generator，示例代码：

	package org.fmz.generics ;

	public class BasicGeneratorDemo{
		public static void main(String args[]){
			Generator<CountObject> gen = BasicGenerator.create(CountObject.class) ;
			for(int i=0;i<5;i++){
				System.out.println(gen.next()) ;
			}
		}
	}/* Output:
		CountObject0
		CountObject1
		CountObject2
		CountObject3
		CountObject4
	*/

#### 简化元组的使用

重载static方法，创建元组

	package org.fmz.generics ;

	public class Tuple{
		public static <A, B> TwoTuple<A, B> tuple(A a, B b){
			return new TwoTuple<A, B>(a, b) ;
		}
		public static <A, B, C> ThreeTuple<A, B, C> tuple(A a, B b, C c){
			return new ThreeTuple<A, B, C>(a, b, c) ;
		}
		public static <A, B, C, D> FourTuple<A, B, C, D> tuple(A a, B b, C c, D d){
			return new FourTuple<A, B, C, D>(a, b, c, d) ;
		}
		public static <A, B, C, D, E> FiveTuple<A, B, C, D, E> tuple(A a, B b, C c, D d, E e){
			return new FiveTuple<A, B, C, D, E>(a, b, c, d, e) ;
		}
	}

重新测试TupleTest2.java

	package org.fmz.generics ;

	public class TupleTest2{
		static TwoTuple<String, Integer> f(){
			return Tuple.tuple("hi", 47) ;
		}
		static ThreeTuple<Amphibian, String, Integer> g(){
			return Tuple.tuple(new Amphibian(), "hi", 47) ;
		}
		static FourTuple<Vehicle, Amphibian, String, Integer> h(){
			return Tuple.tuple(new Vehicle(), new Amphibian(), "hi", 47) ;
		}
		static FiveTuple<Vehicle, Amphibian, String, Integer, Double> k(){
			return Tuple.tuple(new Vehicle(), new Amphibian(), "hi", 47, 11.1) ;
		}

		public static void main(String args[]){
			TwoTuple<String, Integer> ttsi = f() ;
			System.out.println(ttsi) ;
			System.out.println(g()) ;
			System.out.println(h()) ;
			System.out.println(k()) ;
		}
	}/* Output:
		(hi, 47)
		(org.fmz.generics.Amphibian@659e0bfd, hi, 47)
		(org.fmz.generics.Vehicle@2a139a55, org.fmz.generics.Amphibian@15db9742, hi, 47)
		(org.fmz.generics.Vehicle@6d06d69c, org.fmz.generics.Amphibian@7852e922, hi, 47, 11.1)
	*/

#### 一个使用的set工具

示例代码：

	package org.fmz.generics ;

	import java.util.* ;

	public class Sets{
		public static <T> Set<T> union(Set<T> a, Set<T> b){//并集
			Set<T> result = new HashSet<T>(a) ;
			result.addAll(b) ;
			return result ;
		}
		public static <T> Set<T> intersection(Set<T> a, Set<T> b){//交集
			Set<T> result = new HashSet<T>(a) ;
			result.retainAll(b) ;
			return result ;
		}
		public static <T> Set<T> difference(Set<T> superset, Set<T> subset){//补集
			Set<T> result = new HashSet<T>(superset) ;
			result.removeAll(subset) ;
			return result ;
		}
		public static <T> Set<T> complement(Set<T> a, Set<T> b){//两个集合求并之后作为整个集合，求其交集的补集
			return difference(union(a, b), intersection(a, b)) ;
		}
	}

<h3 id="15.5">15.5 匿名内部类</h3>

示例代码：

	package org.fmz.generics ;

	import java.util.* ;

	class Customer{
		private static long counter = 0 ;
		private final long id = counter++ ;
		private Customer(){} ;
		public String toString(){
			return "Customer" + id ;
		}
		public static Generator<Customer> generator(){
			return new Generator<Customer>(){
				public Customer next(){
					return new Customer() ;
				}
			};
		}
	}

	class Teller{
		private static long counter = 0 ;
		private final long id = counter++ ;
		private Teller(){} ;
		public String toString(){
			return "Teller" + id ;
		}
		public static Generator<Teller> generator=
		new Generator<Teller>(){
			public Teller next(){
				return new Teller() ;
			}
		};
	}

	public class BankTeller{
		public static void serve(Teller t, Customer c){
			System.out.println(t + " serves " + c) ;
		}

		public static void main(String args[]){
			Random rand = new Random(47) ;
			Queue<Customer> line = new LinkedList<Customer>() ;
			Generators.fill(line, Customer.generator(), 15) ;
			List<Teller> tellers = new ArrayList<Teller>() ;
			Generators.fill(tellers, Teller.generator, 4) ;
			for(Customer c : line){
				serve(tellers.get(rand.nextInt(tellers.size())), c) ;
			}
		}
	}/* Output:
		Teller2 serves Customer0
		Teller1 serves Customer1
		Teller2 serves Customer2
		Teller0 serves Customer3
		Teller0 serves Customer4
		Teller2 serves Customer5
		Teller0 serves Customer6
		Teller1 serves Customer7
		Teller2 serves Customer8
		Teller2 serves Customer9
		Teller1 serves Customer10
		Teller3 serves Customer11
		Teller1 serves Customer12
		Teller0 serves Customer13
		Teller0 serves Customer14
	*/

<h3 id="15.6">15.6 构建复杂模型</h3>

待续...

<h3 id="15.7">15.7 擦除神秘之处</h3>

待续...

<h3 id="15.8">15.8 擦除的补偿</h3>

待续...

<h3 id="15.9">15.9 边界</h3>

待续...

<h3 id="15.10">15.10 通配符</h3>

待续...

<h3 id="15.11">15.11 问题</h3>

待续...

<h3 id="15.12">15.12 自限定类型</h3>

待续...

<h3 id="15.13">15.13 动态类型安全</h3>

待续...

<h3 id="15.14>15.14 异常</h3>

待续...

<h3 id="15.15">15.15 混型</h3>

待续...

<h3 id="15.16">15.16 潜在类型机制</h3>

待续...

<h3 id="15.17">15.17 对缺乏潜在类型机制的补偿</h3>

待续...

<h3 id="15.18">15.18 将函数对象用作策略</h3>

待续...

***

***

<h2 id="16">16 数组</h2>

<h3 id="16.1">16.1 数组为什么特殊</h3>

数组与其他类容器之间的区别有三个方面：效率、类型和保存基本数据类型的能力。

<h3 id="16.2">16.2 数组是第一级对象</h3>

无论使用哪一种类型的数组，数组标识符其实就是一个引用，指向在堆中创建的一个真实的对象，数组对象用以保存指向其他对象的引用。

<h3 id="16.3">16.3 返回一个数组</h3>

示例代码：

	package org.fmz.arrays ;

	import java.util.* ;

	public class IceCream{
		private static Random rand = new Random(47) ;
		static final String[] FLAVORS = {
			"A", "B", "C", "D", "E", "F", "G", "H"
		} ;
		public static String[] flavorSet(int n){
			if(n > FLAVORS.length){
				throw new IllegalArgumentException() ;
			}
			String[] results = new String[n] ;
			boolean[] picked = new boolean[FLAVORS.length] ;
			for(int i=0;i<n;i++){
				int t ;
				do
				{
					t = rand.nextInt(FLAVORS.length) ;
				}
				while (picked[t]) ;
				results[i] = FLAVORS[t] ;
				picked[t] = true ;
			}
			return results ;
		}

		public static void main(String args[]){
			for(int i=0;i<7;i++){
				System.out.println(Arrays.toString(flavorSet(4))) ;
			}
		}
	}/* Output:
		[F, D, E, A]
		[B, E, D, F]
		[C, H, A, E]
		[G, B, E, D]
		[D, B, G, A]
		[A, G, E, D]
		[H, B, D, F]
	*/

>此处运用了一个static的随机数，能够真正的获得随机的搭配，从输出中也可以看出来。如果不适用static，则不会产生随机的搭配，示例代码：

	package org.fmz.arrays ;

	import java.util.* ;

	public class IceCream2{
		static final String[] FLAVORS = {
			"A", "B", "C", "D", "E", "F", "G", "H"
		} ;
		public static String[] flavorSet(int n){
			Random rand = new Random(47) ;
			if(n > FLAVORS.length){
				throw new IllegalArgumentException() ;
			}
			String[] results = new String[n] ;
			boolean[] picked = new boolean[FLAVORS.length] ;
			for(int i=0;i<n;i++){
				int t ;
				do
				{
					t = rand.nextInt(FLAVORS.length) ;
				}
				while (picked[t]) ;
				results[i] = FLAVORS[t] ;
				picked[t] = true ;
			}
			return results ;
		}

		public static void main(String args[]){
			for(int i=0;i<7;i++){
				System.out.println(Arrays.toString(flavorSet(4))) ;
			}
		}
	}/* Output:
		[F, D, E, A]
		[F, D, E, A]
		[F, D, E, A]
		[F, D, E, A]
		[F, D, E, A]
		[F, D, E, A]
		[F, D, E, A]
	*/

>在这里每次调用flavorSet()方法都会产生一个新的Random，所以不会产生搭配。而上面的例子中我们公用了一个Random产生的就是随机的。

<h3 id="16.4">16.4 多维数组</h3>

简单的多维数组，示例代码：

	package org.fmz.arrays ;

	import java.util.* ;

	public class MultiDimensionalPrimitiveArray{
		public static void main(String args[]){
			int[][] a = {
				{1, 2, 3},
				{4, 5, 6}
			};
			System.out.println(Arrays.deepToString(a)) ;
		}
	}/* Output:
		[[1, 2, 3], [4, 5, 6]]
	*/

使用new来分配数组，示例代码：

	package org.fmz.arrays ;

	import java.util.* ;

	public class ThreeDWithNew{
		public static void main(String args[]){
			int[][][] a = new int[2][2][4] ;
			System.out.println(Arrays.deepToString(a)) ;
		}
	}/* Output:
		[[[0, 0, 0, 0], [0, 0, 0, 0]], [[0, 0, 0, 0], [0, 0, 0, 0]]]
	*/

>可以看得出来，基本类型的数组的值在不进行显式初始化的情况下，会被自动初始化。对象数组会被初始化为null。

数组中构成矩阵的每个向量可以具有任意长度(这被称为粗糙数组)，示例代码：

package org.fmz.arrays ;

	import java.util.* ;

	public class RaggedArray{
		public static void main(String args[]){
			Random rand = new Random(47) ;
			int[][][] a = new int[rand.nextInt(7)][][] ;
			for(int i=0;i<a.length;i++){
				a[i] = new int[rand.nextInt(5)][] ;
				for(int j=0;j<a[i].length;j++){
					a[i][j] = new int[rand.nextInt(5)] ;
				}
			}
			System.out.println(Arrays.deepToString(a)) ;
		}
	}/* Output:
		[[], [[0], [0], [0, 0, 0, 0]], [[], [0, 0], [0, 0]], [[0, 0, 0], [0], [0, 0, 0, 0]], [[0, 0, 0], [0, 0, 0], [0], []], [[
		0], [], [0]]]
	*/

>可以将三维数组想象成三个维度：页、行、列。每一页都是一个二维数组。

<h3 id="16.5">16.5 数组与泛型</h3>

待续...

<h3 id="16.6">16.6 创建测试数据</h3>

待续...

<h3 id="16.7">16.7 Arrays使用功能</h3>

Arrays中有六个基本的static实用方法：equals()用于比较两个数组是否相等(deepEquals()用于多维数组)；fill()只能用同一个值填充各个位置，而针对对象而言，就是复制同一个引用进行填充；sort()用于对数组进行排序；binarySearch()用于已经排序的数组中查找元素；toString()产生数组的String表示；hashCode()产生数组的散列码。

#### System.arraycopy()

示例代码：

	package org.fmz.arrays ;

	import java.util.* ;

	public class CopyingArrays{
		public static void main(String args[]){
			int[] i = new int[7] ;
			int[] j = new int[10] ;
			Arrays.fill(i, 47) ;
			Arrays.fill(j, 99) ;
			System.out.println("i: " + Arrays.toString(i)) ;
			System.out.println("j: " + Arrays.toString(j)) ;
			System.arraycopy(i, 0, j, 0, i.length) ;
			System.out.println("j: " + Arrays.toString(j)) ;
			int k[] = new int[5] ;
			Arrays.fill(k, 103) ;
			System.out.println("k: " + Arrays.toString(k)) ;
			System.arraycopy(i, 0, k, 0, k.length) ;
			System.out.println("k: " + Arrays.toString(k)) ;
			Arrays.fill(k, 103) ;
			System.out.println("k: " + Arrays.toString(k)) ;
			System.arraycopy(k, 0, i, 0, k.length) ;
			System.out.println("i: " + Arrays.toString(i)) ;
		}
	}/* Output:
		i: [47, 47, 47, 47, 47, 47, 47]
		j: [99, 99, 99, 99, 99, 99, 99, 99, 99, 99]
		j: [47, 47, 47, 47, 47, 47, 47, 99, 99, 99]
		k: [103, 103, 103, 103, 103]
		k: [47, 47, 47, 47, 47]
		k: [103, 103, 103, 103, 103]
		i: [103, 103, 103, 103, 103, 47, 47]
	*/

>`arraycopy(Object src, int srcPos, Object dest, int destPos, int length)`，参数分别为：源数组，从源数组中什么位置开始复制的偏移量，从目标数组中什么位置开始替换(复制过来要替换)的偏移量，需要复制的元素个数。

>基本数据类型和对象数组都可以进行复制。如果是复制对象数组，那么只是复制了对象的引用，而不是对象本身的拷贝。这种被称为浅复制(shallow copy)

#### 数组的比较：Arrays.equals()

数组相等的条件是：元素个数必须相等、对应位置上的元素必须相等。

#### 数组元素的比较

程序设计的基本目标是：将保持不变的事物与会发生改变的事物相分离。不变的是通用的排序算法，变化的是各种对象相互比较的方式。因此，不是将进行比较的代码编写成不同的子程序，而是使用策略设计模式。通过使用策略，可以将会发生变化的代码封装在单独的类中(策略对象)，你可以将策略对象传递给总是相同的代码，这些代码将使用策略来完成其算法。通过这种方式，你能够用不同的对象来表示不同的比价方式，然后将它们传递给相同的排序算法。

使用Java标准库的方法Arrays.sort()演示比较的效果：

	package org.fmz.arrays ;

	interface Generator<T>{
		T next() ;
	}

>Generator.java，工具类

	//: net/mindview/util/CollectionData.java
	// A Collection filled with data using a generator object.
	package org.fmz.arrays ;
	import java.util.*;

	public class CollectionData<T> extends ArrayList<T> {
	  public CollectionData(Generator<T> gen, int quantity) {
	    for(int i = 0; i < quantity; i++)
	      add(gen.next());
	  }
	  // A generic convenience method:
	  public static <T> CollectionData<T>
	  list(Generator<T> gen, int quantity) {
	    return new CollectionData<T>(gen, quantity);
	  }
	} ///:~

>CollectionDate.java，工具类

	package org.fmz.arrays ;

	import java.util.*;

	public class Generated {
	  // Fill an existing array:
	  public static <T> T[] array(T[] a, Generator<T> gen) {
	    return new CollectionData<T>(gen, a.length).toArray(a);
	  }
	  // Create a new array:
	  @SuppressWarnings("unchecked")
	  public static <T> T[] array(Class<T> type,
	      Generator<T> gen, int size) {
	    T[] a =
	      (T[])java.lang.reflect.Array.newInstance(type, size);
	    return new CollectionData<T>(gen, size).toArray(a);
	  }
	} ///:~

>Generated.java，工具类

	package org.fmz.arrays ;

	import java.util.* ;

	public class CompType implements Comparable<CompType>{
		int i ;
		int j ;
		private static int count = 1 ;
		public CompType(int i, int j){
			this.i = i ;
			this.j = j ;
		}
		public String toString(){
			String result = "[i= " + i + ", j= " + j + "]";
			if(count++ % 3 == 0){
				result += "\n" ;
			}
			return result ;
		}
		public int compareTo(CompType rv){
			return ( i < rv.i ? -1 : (i == rv.i ? 0 : 1)) ;
		}
		private static Random rand = new Random(47) ;
		public static Generator<CompType> generator(){
			return new Generator<CompType>(){
				public CompType next(){
					return new CompType(rand.nextInt(100), rand.nextInt(100)) ;
				}
			};
		}

		public static void main(String args[]){
			CompType[] a = Generated.array(new CompType[12], generator()) ;
			System.out.println("before sorting: ") ;
			System.out.println(Arrays.toString(a)) ;
			Arrays.sort(a) ;
			System.out.println("after sorting: ") ;
			System.out.println(Arrays.toString(a)) ;
		}
	}/* Output:
		before sorting:
		[[i= 58, j= 55], [i= 93, j= 61], [i= 61, j= 29]
		, [i= 68, j= 0], [i= 22, j= 7], [i= 88, j= 28]
		, [i= 51, j= 89], [i= 9, j= 78], [i= 98, j= 61]
		, [i= 20, j= 58], [i= 16, j= 40], [i= 11, j= 22]
		]
		after sorting:
		[[i= 9, j= 78], [i= 11, j= 22], [i= 16, j= 40]
		, [i= 20, j= 58], [i= 22, j= 7], [i= 51, j= 89]
		, [i= 58, j= 55], [i= 61, j= 29], [i= 68, j= 0]
		, [i= 88, j= 28], [i= 93, j= 61], [i= 98, j= 61]
		]
	*/

>CompType.java，排序实现类。

反转自然排序，示例代码：

	package org.fmz.arrays ;

	import java.util.* ;

	public class Reverse{
		public static void main(String args[]){
			CompType[] a = Generated.array(new CompType[12], CompType.generator()) ;
			System.out.println("before sorting: ") ;
			System.out.println(Arrays.toString(a)) ;
			Arrays.sort(a, Collections.reverseOrder()) ;
			System.out.println("after sorting: ") ;
			System.out.println(Arrays.toString(a)) ;
		}
	}/* Output:
		before sorting:
		[[i= 58, j= 55], [i= 93, j= 61], [i= 61, j= 29]
		, [i= 68, j= 0], [i= 22, j= 7], [i= 88, j= 28]
		, [i= 51, j= 89], [i= 9, j= 78], [i= 98, j= 61]
		, [i= 20, j= 58], [i= 16, j= 40], [i= 11, j= 22]
		]
		after sorting:
		[[i= 98, j= 61], [i= 93, j= 61], [i= 88, j= 28]
		, [i= 68, j= 0], [i= 61, j= 29], [i= 58, j= 55]
		, [i= 51, j= 89], [i= 22, j= 7], [i= 20, j= 58]
		, [i= 16, j= 40], [i= 11, j= 22], [i= 9, j= 78]
		]
	*/

也可以编写自己的Comparator。示例代码：

	package org.fmz.arrays ;

	import java.util.* ;

	class CompTypeComparator implements Comparator<CompType>{
		public int compare(CompType o1, CompType o2){
			return (o1.j < o2.j ? -1 : (o1.j == o2.j ? 0 : 1)) ;
		}
	}

	public class ComparatorTest{
		public static void main(String args[]){
			CompType[] a = Generated.array(new CompType[12], CompType.generator()) ;
			System.out.println("before sorting: ") ;
			System.out.println(Arrays.toString(a)) ;
			Arrays.sort(a, new CompTypeComparator()) ;
			System.out.println("after sorting: ") ;
			System.out.println(Arrays.toString(a)) ;
		}
	}/* Output:
		[[i= 58, j= 55], [i= 93, j= 61], [i= 61, j= 29]
		, [i= 68, j= 0], [i= 22, j= 7], [i= 88, j= 28]
		, [i= 51, j= 89], [i= 9, j= 78], [i= 98, j= 61]
		, [i= 20, j= 58], [i= 16, j= 40], [i= 11, j= 22]
		]
		after sorting:
		[[i= 68, j= 0], [i= 22, j= 7], [i= 11, j= 22]
		, [i= 88, j= 28], [i= 61, j= 29], [i= 16, j= 40]
		, [i= 58, j= 55], [i= 20, j= 58], [i= 93, j= 61]
		, [i= 98, j= 61], [i= 9, j= 78], [i= 51, j= 89]
		]
	*/

#### 数组排序

对字符串数组进行排序。示例代码：

	//: net/mindview/util/CountingGenerator.java
	// Simple generator implementations.
	package org.fmz.arrays ;

	public class CountingGenerator {
	  public static class
	  Boolean implements Generator<java.lang.Boolean> {
	    private boolean value = false;
	    public java.lang.Boolean next() {
	      value = !value; // Just flips back and forth
	      return value;
	    }
	  }
	  public static class
	  Byte implements Generator<java.lang.Byte> {
	    private byte value = 0;
	    public java.lang.Byte next() { return value++; }
	  }
	  static char[] chars = ("abcdefghijklmnopqrstuvwxyz" +
	    "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	  public static class
	  Character implements Generator<java.lang.Character> {
	    int index = -1;
	    public java.lang.Character next() {
	      index = (index + 1) % chars.length;
	      return chars[index];
	    }
	  }
	  public static class
	  String implements Generator<java.lang.String> {
	    private int length = 7;
	    Generator<java.lang.Character> cg = new Character();
	    public String() {}
	    public String(int length) { this.length = length; }
	    public java.lang.String next() {
	      char[] buf = new char[length];
	      for(int i = 0; i < length; i++)
		buf[i] = cg.next();
	      return new java.lang.String(buf);
	    }
	  }
	  public static class
	  Short implements Generator<java.lang.Short> {
	    private short value = 0;
	    public java.lang.Short next() { return value++; }
	  }
	  public static class
	  Integer implements Generator<java.lang.Integer> {
	    private int value = 0;
	    public java.lang.Integer next() { return value++; }
	  }
	  public static class
	  Long implements Generator<java.lang.Long> {
	    private long value = 0;
	    public java.lang.Long next() { return value++; }
	  }
	  public static class
	  Float implements Generator<java.lang.Float> {
	    private float value = 0;
	    public java.lang.Float next() {
	      float result = value;
	      value += 1.0;
	      return result;
	    }
	  }
	  public static class
	  Double implements Generator<java.lang.Double> {
	    private double value = 0.0;
	    public java.lang.Double next() {
	      double result = value;
	      value += 1.0;
	      return result;
	    }
	  }
	} ///:~

>CountingGenerator.java，工具类

	//: net/mindview/util/RandomGenerator.java
	// Generators that produce random values.
	package org.fmz.arrays ;
	import java.util.*;

	public class RandomGenerator {
	  private static Random r = new Random(47);
	  public static class
	  Boolean implements Generator<java.lang.Boolean> {
	    public java.lang.Boolean next() {
	      return r.nextBoolean();
	    }
	  }
	  public static class
	  Byte implements Generator<java.lang.Byte> {
	    public java.lang.Byte next() {
	      return (byte)r.nextInt();
	    }
	  }
	  public static class
	  Character implements Generator<java.lang.Character> {
	    public java.lang.Character next() {
	      return CountingGenerator.chars[
		r.nextInt(CountingGenerator.chars.length)];
	    }
	  }
	  public static class
	  String extends CountingGenerator.String {
	    // Plug in the random Character generator:
	    { cg = new Character(); } // Instance initializer
	    public String() {}
	    public String(int length) { super(length); }
	  }
	  public static class
	  Short implements Generator<java.lang.Short> {
	    public java.lang.Short next() {
	      return (short)r.nextInt();
	    }
	  }
	  public static class
	  Integer implements Generator<java.lang.Integer> {
	    private int mod = 10000;
	    public Integer() {}
	    public Integer(int modulo) { mod = modulo; }
	    public java.lang.Integer next() {
	      return r.nextInt(mod);
	    }
	  }
	  public static class
	  Long implements Generator<java.lang.Long> {
	    private int mod = 10000;
	    public Long() {}
	    public Long(int modulo) { mod = modulo; }
	    public java.lang.Long next() {
	      return new java.lang.Long(r.nextInt(mod));
	    }
	  }
	  public static class
	  Float implements Generator<java.lang.Float> {
	    public java.lang.Float next() {
	      // Trim all but the first two decimal places:
	      int trimmed = Math.round(r.nextFloat() * 100);
	      return ((float)trimmed) / 100;
	    }
	  }
	  public static class
	  Double implements Generator<java.lang.Double> {
	    public java.lang.Double next() {
	      long trimmed = Math.round(r.nextDouble() * 100);
	      return ((double)trimmed) / 100;
	    }
	  }
	} ///:~

>RandomGenerator.java，工具类。

	package org.fmz.arrays ;

	import java.util.* ;

	public class StringSorting{
		public static void main(String args[]){
			String[] sa = Generated.array(new String[20], new RandomGenerator.String(5)) ;
			System.out.println("before sorting: ") ;
			System.out.println(Arrays.toString(sa)) ;
			System.out.println("************************") ;
			Arrays.sort(sa) ;
			System.out.println("after sorting: ") ;
			System.out.println(Arrays.toString(sa)) ;
			System.out.println("************************") ;
			Arrays.sort(sa, Collections.reverseOrder()) ;
			System.out.println("reverse sorting: ") ;
			System.out.println(Arrays.toString(sa)) ;
			System.out.println("************************") ;
			Arrays.sort(sa, String.CASE_INSENSITIVE_ORDER) ;
			System.out.println("case insensitive sorting: ") ;
			System.out.println(Arrays.toString(sa)) ;
		}
	}/* Output:
		before sorting:
		[YNzbr, nyGcF, OWZnT, cQrGs, eGZMm, JMRoE, suEcU, OneOE, dLsmw, HLGEa, hKcxr, EqUCB, bkIna, Mesbt, WHkjU, rUkZP, gwsqP,
		zDyCy, RFJQA, HxxHv]
		************************
		after sorting:
		[EqUCB, HLGEa, HxxHv, JMRoE, Mesbt, OWZnT, OneOE, RFJQA, WHkjU, YNzbr, bkIna, cQrGs, dLsmw, eGZMm, gwsqP, hKcxr, nyGcF,
		rUkZP, suEcU, zDyCy]
		************************
		reverse sorting:
		[zDyCy, suEcU, rUkZP, nyGcF, hKcxr, gwsqP, eGZMm, dLsmw, cQrGs, bkIna, YNzbr, WHkjU, RFJQA, OneOE, OWZnT, Mesbt, JMRoE,
		HxxHv, HLGEa, EqUCB]
		************************
		case insensitive sorting:
		[bkIna, cQrGs, dLsmw, eGZMm, EqUCB, gwsqP, hKcxr, HLGEa, HxxHv, JMRoE, Mesbt, nyGcF, OneOE, OWZnT, RFJQA, rUkZP, suEcU,
		WHkjU, YNzbr, zDyCy]
	*/

>StringSorting，排序类。

#### 在已经排序的数组中进行查找

可以利用Arrays.binarySearch()对已经排序号的数组进行快速查找，如果数组没有排序就进行此方法的查找，则会产生不可预料的结果。

示例代码：

	//: net/mindview/util/ConvertTo.java
	package org.fmz.arrays ;

	public class ConvertTo {
	  public static boolean[] primitive(Boolean[] in) {
	    boolean[] result = new boolean[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i]; // Autounboxing
	    return result;
	  }
	  public static char[] primitive(Character[] in) {
	    char[] result = new char[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i];
	    return result;
	  }
	  public static byte[] primitive(Byte[] in) {
	    byte[] result = new byte[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i];
	    return result;
	  }
	  public static short[] primitive(Short[] in) {
	    short[] result = new short[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i];
	    return result;
	  }
	  public static int[] primitive(Integer[] in) {
	    int[] result = new int[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i];
	    return result;
	  }
	  public static long[] primitive(Long[] in) {
	    long[] result = new long[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i];
	    return result;
	  }
	  public static float[] primitive(Float[] in) {
	    float[] result = new float[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i];
	    return result;
	  }
	  public static double[] primitive(Double[] in) {
	    double[] result = new double[in.length];
	    for(int i = 0; i < in.length; i++)
	      result[i] = in[i];
	    return result;
	  }
	} ///:~

>ConvertTo.java，工具类

	package org.fmz.arrays ;

	import java.util.* ;

	public class ArraySearching{
		public static void main(String args[]){
			Generator<Integer> gen = new RandomGenerator.Integer(1000) ;
			int[] a = ConvertTo.primitive(Generated.array(new Integer[25], gen)) ;
			Arrays.sort(a) ;
			System.out.println("sorted array: " + Arrays.toString(a)) ;
			while(true){
				int r = gen.next() ;
				int location = Arrays.binarySearch(a, r) ;
				if(location >= 0){
					System.out.println("Location of" + r + "is" + location + ", a[" + location + "]= " + a[location]) ;
					break ;
				}
			}
		}
	}/* Output:
		sorted array: [128, 140, 200, 207, 258, 258, 278, 288, 322, 429, 511, 520, 522, 551, 555, 589, 693, 704, 809, 861, 861,
		868, 916, 961, 998]
		Location of322is8, a[8]= 322
	*/

>ArraySearching.java，binarySearch()方法进行查找

>如果找到了目标，Arrays.binarySearch()产生的返回这等于或者大于0；否则会产生负的返回值，表示如要保持数组的排序状态此目标元素所应该插入的位置(不懂)。

***

***

<h2 id="17">17 容器深入研究</h2>

待续...

***

***

<h2 id="18">18 Java I/O系统</h2>

对于程序语言的设计者来说，创建一个好的输入\输出(I/O)系统是一项艰巨的任务。

<h3 id="18.1">18.1 File类</h3>

与其说是File类，不如说是FilePath类。它能代表一个特定的文件名称，又能代表一个目录下的一组文件名称。

#### 目录列表器

示例代码：

	package org.fmz.io ;

	import java.util.regex.* ;
	import java.io.* ;
	import java.util.* ;

	public class DirList{
		public static void main(String args[]){
			File path = new File(".") ;
			String[] list ;
			if(args.length == 0){
				list = path.list() ;
			}else{
				list = path.list(new DirFilter(args[0])) ;
			}
			Arrays.sort(list, String.CASE_INSENSITIVE_ORDER) ;
			for(String s : list){
				System.out.println(s) ;
			}
		}
	}

	class DirFilter implements FilenameFilter{
		private Pattern pattern ;
		public DirFilter(String regex){
			pattern = Pattern.compile(regex) ;
		}
		public boolean accept(File dir, String name){
			return pattern.matcher(name).matches() ;
		}
	}/* Output:
		DirList.java
	*/

>`.`表示当前目录，`..`表示上一级目录，以此类推；`\`表示目录下

>正则表达式中，`.`表示除`\n`、`\r`、`\r\n`的行终止符；`+`表示一个或者多个；`*`表示0个或者多个；`?`表示0个或者1个

这个方法非常适合匿名内部类

	package org.fmz.io ;

	import java.util.regex.* ;
	import java.io.* ;
	import java.util.* ;

	public class DirList2{
		public static void main(String args[]){
			File path = new File(".") ;
			String[] list ;
			if(args.length == 0){
				list = path.list() ;
			}else{
				list = path.list(filter(args[0])) ;
			}
			Arrays.sort(list, String.CASE_INSENSITIVE_ORDER) ;
			for(String s : list){
				System.out.println(s) ;
			}
		}
		public static FilenameFilter filter(final String regex){
			return new FilenameFilter(){
				private Pattern pattern = Pattern.compile(regex) ;
				public boolean accept(File dir, String name){
					return pattern.matcher(name).matches() ;
				}
			};
		}
	}/* Output:
		DirList.DirList.java
		DirList2.java
	*/

>注意：传向filter()方法的参数必须是final的。这在匿名内部类中是必须的，这样它才能够使用来自该类范围之外的对象。

还可以改为代码：

	package org.fmz.io ;

	import java.util.regex.* ;
	import java.io.* ;
	import java.util.* ;

	public class DirList3{
		public static void main(String args[]){
			File path = new File(".") ;
			String[] list ;
			if(args.length == 0){
				list = path.list() ;
			}else{
				list = path.list(
					new FilenameFilter(){
						private Pattern pattern = Pattern.compile(args[0]) ;
						public boolean accept(File dir, String name){
							return pattern.matcher(name).matches() ;
						}
					}) ;
			}
			Arrays.sort(list, String.CASE_INSENSITIVE_ORDER) ;
			for(String s : list){
				System.out.println(s) ;
			}
		}
	}/* Output:
		DirList.java
		DirList2.java
		DirList3.java
	*/

>这个方法的优点是解决特定问题的代码隔离、聚拢在一起。另一个方面代码不易阅读，因此要谨慎使用。

#### 目录实用工具

示例代码：

	package org.fmz.io ;

	import java.util.regex.* ;
	import java.io.* ;
	import java.util.* ;

	public final class Directory{
		public static File[] local(File dir, final String regex){
			return dir.listFiles(new FilenameFilter(){
				private Pattern pattern = Pattern.compile(regex) ;
				public boolean accept(File dir, String name){
					return pattern.matcher(new File(name).getName()).matches() ;
					//return pattern.matcher(name).matches() ;
				}
			});
		}

		public static File[] local(String path, final String regex){
			return local(new File(path), regex) ;
		}

		public static class TreeInfo implements Iterable<File>{
			public List<File> files = new ArrayList<File>() ;
			public List<File> dirs = new ArrayList<File>() ;
			public Iterator<File> iterator(){
				return files.iterator() ;
			}
			void addAll(TreeInfo other){
				files.addAll(other.files) ;
				dirs.addAll(other.dirs) ;
			}

			public String toString(){
				return "dirs: " +PPrint.pformat(dirs) +
					"\n\nfiles: " + PPrint.pformat(files) ;
			}
		}

		public static TreeInfo walk(String start, String regex){
			return recurseDirs(new File(start), regex) ;
		}

		public static TreeInfo walk(File start, String regex){
			return recurseDirs(start, regex) ;
		}

		public static TreeInfo walk(File start){
			return recurseDirs(start, ".*") ;
		}

		public static TreeInfo walk(String start){
			return recurseDirs(new File(start), ".*") ;
		}

		static TreeInfo recurseDirs(File startDir, String regex){
			TreeInfo result = new TreeInfo() ;
			for(File item : startDir.listFiles()){
				if(item.isDirectory()){
					result.dirs.add(item) ;
					result.addAll(recurseDirs(item, regex)) ;
				}else{
					if(item.getName().matches(regex)){
						result.files.add(item) ;
					}
				}
			}
			return result ;
		}
		/*
		public static void main(String args[]){
			if(args.length == 0){
				System.out.println(walk(".")) ;
			}else{
				for(String arg : args){
					System.out.println(walk(arg)) ;
				}
			}
		}Output:
			无参数：输出当前目录及其子目录的所有文件夹及文件
			一个参数：输出指定参数路径下的文件夹及文件
			多个参数：循环输出指定参数路径下的文件夹及文件
	*/
		public static void main(String args[]){
			if(args.length == 0){
				System.out.println(walk(".")) ;
			}else if(args.length == 1){
				System.out.println(walk(args[0])) ;
			}else{
				System.out.println(walk(args[0],args[1])) ;
			}
		}
	}/* Output:
		无参数：输出当前目录及其子目录的所有文件夹及文件
		一个参数：输出指定参数路径下的文件夹及文件
		二个参数：输出与第一个参数匹配的路径下、文件和第二个参数正则表达式匹配的文件夹和文件
		{args: null}
			dirs: [
			 .\org
			 .\org\fmz
			 .\org\fmz\io
			 ]

			files: [
			 .\Directory.java
			 .\DirList.java
			 .\DirList2.java
			 .\DirList3.java
			 .\org\fmz\io\Directory$1.class
			 .\org\fmz\io\Directory$TreeInfo.class
			 .\org\fmz\io\Directory.class
			 .\org\fmz\io\DirFilter.class
			 .\org\fmz\io\DirList.class
			 .\org\fmz\io\DirList2$1.class
			 .\org\fmz\io\DirList2.class
			 .\org\fmz\io\DirList3$1.class
			 .\org\fmz\io\DirList3.class
			 .\org\fmz\io\PPrint.class
			 .\PPrint.java
			 ]

		 {args: ..\access}
			dirs: [
			 ..\access\org
			 ..\access\org\fmz
			 ..\access\org\fmz\access
			 ]

			files: [
			 ..\access\Apple.java
			 ..\access\org\fmz\access\PrivateAccess.class
			 ..\access\org\fmz\access\Sundae.class
			 ..\access\PrivateAccess.java
			 ..\access\SingletonPattern.java
			 ]

		{args: ..\access .*\.java}
			dirs: [
			 ..\access\org
			 ..\access\org\fmz
			 ..\access\org\fmz\access
			 ]

			files: [
			 ..\access\Apple.java
			 ..\access\PrivateAccess.java
			 ..\access\SingletonPattern.java
			 ]
	*/

>Directory.java，目录工具

	package org.fmz.io ;

	import java.util.* ;

	public class PPrint{
		public static String pformat(Collection<?> c){
			if(c.size() == 0 ){
				return "[]" ;
			}
			StringBuilder result = new StringBuilder("[") ;
			for(Object elem: c){
				if(c.size() != 1){
					result.append("\n ") ;
				}
				result.append(elem) ;
			}
			if(c.size() != 1){
				result.append("\n ") ;
			}
			result.append("]") ;
			return result.toString() ;
		}

		public static void pprint(Collection<?> c){
			System.out.println(pformat(c)) ;
		}
		public static void pprint(Object[] c){
			System.out.println(pformat(Arrays.asList(c))) ;
		}
	}

>PPrint.java，灵巧打印机工具。用来进行格式化操作

测试目录工具

	package org.fmz.io ;

	import java.io.* ;
	import java.util.* ;

	public class DirectoryDemo{
		public static void main(String args[]){
			System.out.println("打印出当前目录树下的所有文件夹") ;
			PPrint.pprint(Directory.walk(".").dirs) ;//打印出当前目录树下的所有文件夹
			System.out.println("*************************************************") ;
			System.out.println("打印处当前目录树下的所有文件") ;
			PPrint.pprint(Directory.walk(".").files) ;//打印处当前目录树下的所有文件
			System.out.println("*************************************************") ;
			System.out.println("打印出当前目录树下匹配正则表达式(\".*\\.java\")的所有文件") ;
			PPrint.pprint(Directory.walk(".", ".*\\.java").files) ;//打印出当前目录树下匹配正则表达式的所有文件
			System.out.println("*************************************************") ;


			System.out.println("打印出本地目录下的所有文件夹及文件") ;
			PPrint.pprint(Directory.local(".")) ;//打印出本地目录下的所有文件夹及文件
			System.out.println("*************************************************") ;
			System.out.println("打印出指定目录下的所有文件夹及文件") ;
			PPrint.pprint(Directory.local("..")) ;//打印出指定目录下的所有文件夹及文件
			System.out.println("*************************************************") ;
			System.out.println("打印出本地目录下匹配正则表达式(\"o.*\")的所有文件夹及文件：首字母为o") ;
			PPrint.pprint(Directory.local(".", "o.*")) ;//打印出本地目录下匹配正则表达式的所有文件夹及文件：首字母为o
			System.out.println("*************************************************") ;
			System.out.println("打印出本地目录下匹配正则表达式(\"D.*\\.java\")的所有文件夹及文件：后缀为.java") ;
			PPrint.pprint(Directory.local(".", "D.*\\.java")) ;
			System.out.println("*************************************************") ;
		}
	}/* Output:
		打印出当前目录树下的所有文件夹
		[
		 .\org
		 .\org\fmz
		 .\org\fmz\io
		 ]
		*************************************************
		打印处当前目录树下的所有文件
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 .\org\fmz\io\Directory$1.class
		 .\org\fmz\io\Directory$TreeInfo.class
		 .\org\fmz\io\Directory.class
		 .\org\fmz\io\Directory2$1.class
		 .\org\fmz\io\Directory2$TreeInfo.class
		 .\org\fmz\io\Directory2.class
		 .\org\fmz\io\DirectoryDemo.class
		 .\org\fmz\io\DirectoryDemo2.class
		 .\org\fmz\io\DirFilter.class
		 .\org\fmz\io\DirList.class
		 .\org\fmz\io\DirList2$1.class
		 .\org\fmz\io\DirList2.class
		 .\org\fmz\io\DirList3$1.class
		 .\org\fmz\io\DirList3.class
		 .\org\fmz\io\PPrint.class
		 .\PPrint.java
		 ]
		*************************************************
		打印出当前目录树下匹配正则表达式(".*\.java")的所有文件
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 .\PPrint.java
		 ]
		*************************************************
		打印出本地目录下的所有文件夹及文件
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 .\org
		 .\PPrint.java
		 ]
		*************************************************
		打印出指定目录下的所有文件夹及文件
		[
		 ..\access
		 ..\arrays
		 ..\exception
		 ..\generics
		 ..\holding
		 ..\init and clear
		 ..\innerclasses
		 ..\interface
		 ..\io
		 ..\polymorphism
		 ..\reusing
		 ..\source_code
		 ..\strings
		 ..\typeinfo
		 ]
		*************************************************
		打印出本地目录下匹配正则表达式("o.*")的所有文件夹及文件：首字母为o
		[.\org]
		*************************************************
		打印出本地目录下匹配正则表达式("D.*\.java")的所有文件夹及文件：后缀为.java
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 ]
		*************************************************
	*/

>DirectoryDemo.java，测试类。

改进的Directory，此工具能根据正则表达式打印出与之向匹配的目录

	package org.fmz.io ;

	import java.util.regex.* ;
	import java.io.* ;
	import java.util.* ;

	public final class Directory2{
		public static File[] local(File dir, final String regex){
			return dir.listFiles(new FilenameFilter(){
				private Pattern pattern = Pattern.compile(regex) ;
				public boolean accept(File dir, String name){
					return pattern.matcher(new File(name).getName()).matches() ;
					//return pattern.matcher(name).matches() ;
				}
			});
		}
		public static File[] local(String path, final String regex){//overloading
			return local(new File(path), regex) ;
		}
		public static File[] local(String path){//overloading
			return local(new File(path), ".*") ;
		}
		public static File[] local(File dir){//overloading
			return local(dir, ".*") ;
		}

		public static class TreeInfo implements Iterable<File>{
			public List<File> files = new ArrayList<File>() ;
			public List<File> dirs = new ArrayList<File>() ;
			public Iterator<File> iterator(){
				return files.iterator() ;
			}
			void addAll(TreeInfo other){
				files.addAll(other.files) ;
				dirs.addAll(other.dirs) ;
			}

			public String toString(){
				return "dirs: " +PPrint.pformat(dirs) +
					"\n\nfiles: " + PPrint.pformat(files) ;
			}
		}

		public static TreeInfo walk(String start, String regex){//overloading
			return recurseDirs(new File(start), regex) ;
		}
		public static TreeInfo walk(File start, String regex){//overloading
			return recurseDirs(start, regex) ;
		}
		public static TreeInfo walk(File start){//overloading
			return recurseDirs(start, ".*") ;
		}
		public static TreeInfo walk(String start){//overloading
			return recurseDirs(new File(start), ".*") ;
		}

		static TreeInfo recurseDirs(File startDir, String regex){
			TreeInfo result = new TreeInfo() ;
			for(File item : startDir.listFiles()){
				if(item.isDirectory()){
					if(item.getName().matches(regex)){
						result.dirs.add(item) ;
					}
					result.addAll(recurseDirs(item, regex)) ;
				}else{
					if(item.getName().matches(regex)){
						result.files.add(item) ;
					}
				}
			}
			return result ;
		}
	}

>Directory2.java

	package org.fmz.io ;

	import java.io.* ;
	import java.util.* ;

	public class DirectoryDemo2{
		public static void main(String args[]){
			System.out.println("打印出当前目录树下的所有文件夹") ;
			PPrint.pprint(Directory2.walk(".").dirs) ;//打印出当前目录树下的所有文件夹
			System.out.println("*************************************************") ;
			System.out.println("打印处当前目录树下的所有文件") ;
			PPrint.pprint(Directory2.walk(".").files) ;//打印处当前目录树下的所有文件
			System.out.println("*************************************************") ;
			System.out.println("打印出当前目录树下匹配正则表达式(\".*io\")的所有文件夹") ;
			PPrint.pprint(Directory2.walk(".", ".*io").dirs) ;//打印出当前目录树下匹配正则表达式的所有文件夹
			System.out.println("*************************************************") ;
			System.out.println("打印出当前目录树下匹配正则表达式(\".*\\.java\")的所有文件") ;
			PPrint.pprint(Directory2.walk(".", ".*\\.java").files) ;//打印出当前目录树下匹配正则表达式的所有文件
			System.out.println("*************************************************") ;


			System.out.println("打印出本地目录下的所有文件夹及文件") ;
			PPrint.pprint(Directory2.local(".")) ;//打印出本地目录下的所有文件夹及文件
			System.out.println("*************************************************") ;
			System.out.println("打印出指定目录下的所有文件夹及文件") ;
			PPrint.pprint(Directory2.local("..")) ;//打印出指定目录下的所有文件夹及文件
			System.out.println("*************************************************") ;
			System.out.println("打印出本地目录下匹配正则表达式(\"o.*\")的所有文件夹及文件：首字母为o") ;
			PPrint.pprint(Directory2.local(".", "o.*")) ;//打印出本地目录下匹配正则表达式的所有文件夹及文件：首字母为o
			System.out.println("*************************************************") ;
			System.out.println("打印出本地目录下匹配正则表达式(\"D.*\\.java\")的所有文件夹及文件：后缀为.java") ;
			PPrint.pprint(Directory2.local(".", "D.*\\.java")) ;
			System.out.println("*************************************************") ;
		}
	}/* Output:
		打印出当前目录树下的所有文件夹
		[
		 .\org
		 .\org\fmz
		 .\org\fmz\io
		 ]
		*************************************************
		打印处当前目录树下的所有文件
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 .\org\fmz\io\Directory$1.class
		 .\org\fmz\io\Directory$TreeInfo.class
		 .\org\fmz\io\Directory.class
		 .\org\fmz\io\Directory2$1.class
		 .\org\fmz\io\Directory2$TreeInfo.class
		 .\org\fmz\io\Directory2.class
		 .\org\fmz\io\DirectoryDemo.class
		 .\org\fmz\io\DirectoryDemo2.class
		 .\org\fmz\io\DirFilter.class
		 .\org\fmz\io\DirList.class
		 .\org\fmz\io\DirList2$1.class
		 .\org\fmz\io\DirList2.class
		 .\org\fmz\io\DirList3$1.class
		 .\org\fmz\io\DirList3.class
		 .\org\fmz\io\PPrint.class
		 .\PPrint.java
		 ]
		*************************************************
		打印出当前目录树下匹配正则表达式(".*io")的所有文件夹
		[.\org\fmz\io]
		*************************************************
		打印出当前目录树下匹配正则表达式(".*\.java")的所有文件
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 .\PPrint.java
		 ]
		*************************************************
		打印出本地目录下的所有文件夹及文件
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 .\org
		 .\PPrint.java
		 ]
		*************************************************
		打印出指定目录下的所有文件夹及文件
		[
		 ..\access
		 ..\arrays
		 ..\exception
		 ..\generics
		 ..\holding
		 ..\init and clear
		 ..\innerclasses
		 ..\interface
		 ..\io
		 ..\polymorphism
		 ..\reusing
		 ..\source_code
		 ..\strings
		 ..\typeinfo
		 ]
		*************************************************
		打印出本地目录下匹配正则表达式("o.*")的所有文件夹及文件：首字母为o
		[.\org]
		*************************************************
		打印出本地目录下匹配正则表达式("D.*\.java")的所有文件夹及文件：后缀为.java
		[
		 .\Directory.java
		 .\Directory2.java
		 .\DirectoryDemo.java
		 .\DirectoryDemo2.java
		 .\DirList.java
		 .\DirList2.java
		 .\DirList3.java
		 ]
		*************************************************
	*/

>DirectoryDemo2.java，测试类。

在目录中穿行的工具

	package org.fmz.io ;

	import java.io.* ;

	public class ProcessFiles{
		public interface Strategy{
			void process(File file) ;
		}
		private Strategy strategy ;
		private String ext ;
		public ProcessFiles(Strategy strategy, String ext){
			this.strategy = strategy ;
			this.ext = ext ;
		}
		public void start(String[] args){
			try{
				if(args.length == 0){
					processDirectoryTree(new File(".")) ;
				}else{
					for(String arg : args){
						File fileArg = new File(arg) ;
						if(fileArg.isDirectory()){
							processDirectoryTree(fileArg) ;
						}else{
							if(!arg.endsWith("." + ext)){
								arg += "." + ext ;
							}
							strategy.process(new File(arg).getCanonicalFile()) ;
						}
					}
				}
			}catch(IOException e){
					throw new RuntimeException(e) ;
			}
		}

		public void  processDirectoryTree(File root) throws IOException{
			for(File file : Directory.walk(root.getAbsolutePath(), ".*\\."+ext)){
				strategy.process(file.getCanonicalFile()) ;
			}
		}

		public static void main(String args[]){
			new ProcessFiles(new ProcessFiles.Strategy(){
				public void process(File file){
					System.out.println(file) ;
				}
			}, "java").start(args) ;
		}
	}/* Output:
		D:\Thinking_in_Java_Demo\io>java org.fmz.io.ProcessFiles
		D:\Thinking_in_Java_Demo\io\Directory.java
		D:\Thinking_in_Java_Demo\io\Directory2.java
		D:\Thinking_in_Java_Demo\io\DirectoryDemo.java
		D:\Thinking_in_Java_Demo\io\DirectoryDemo2.java
		D:\Thinking_in_Java_Demo\io\DirList.java
		D:\Thinking_in_Java_Demo\io\DirList2.java
		D:\Thinking_in_Java_Demo\io\DirList3.java
		D:\Thinking_in_Java_Demo\io\PPrint.java
		D:\Thinking_in_Java_Demo\io\ProcessFiles.java
	*/

#### 目录的检查及创建

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class MakeDirectories{
		private static void usage(){
			System.err.println(
				"Usage:MakeDirectories path1 ...\n" +
				"Creates eath path\n" +
				"Usage:MakeDirectories -d path1 ...\n" +
				"Deletes each path\n" +
				"Usage:MakeDirectories -r path1 path2\n" +
				"Rename from path1 to path2") ;
			System.exit(1) ;
		}

		public static void fileData(File f){
			System.out.println(
				"Absolute path: " + f.getAbsolutePath() +
				"\nCan read: " + f.canRead() +
				"\nCan write: " + f.canWrite() +
				"\ngetName: " + f.getName() +
				"\ngetParent: " + f.getParent() +
				"\ngetPath: " + f.getPath() +
				"\nlength: " + f.length() +
				"\nlastModified: " + f.lastModified()) ;
			if(f.isFile()){
				System.out.println("It is a file") ;
			}else if(f.isDirectory()){
				System.out.println("It is a directory") ;
			}
		}

		public static void main(String args[]){
			if(args.length < 1){
				usage() ;
			}
			if(args[0].equals("-r")){
				if(args.length != 3){
					usage() ;
				}
				File old = new File(args[1]) ;
				File rname = new File(args[2]) ;
				old.renameTo(rname) ;
				fileData(old) ;
				fileData(rname) ;
				return ;
			}
			int count = 0 ;
			boolean del = false ;
			if(args[0].equals("-d")){
				count++ ;
				del = true ;
			}
			count-- ;
			while(++count < args.length){
				File f = new File(args[count]) ;
				if(f.exists()){
					System.out.println(f + " exists") ;
					if(del){
						System.out.println("deleting..." + f) ;
						f.delete() ;
					}
				}else{
					if(!del){
						f.mkdirs() ;
						System.out.println("Created " + f) ;
					}
				}
				fileData(f) ;
			}
		}
	}

<h3 id="18.2">18.2 输入和输出</h3>

在编程的I/O类库中常常使用流这个概念，它代表任何有能力产生数据的数据源对象或者有能力接收数据的接收端对象。流屏蔽了实际的I/O设备中处理数据的细节。

Java类库中的I/O类分为输入和输出两个部分，通过继承任何自InputStream或者是Reader派生而来的类都含有名为read()的基本方法，用于读取单个字节或者字节数组。同样，任何自OutputStream或者Writer派生而来的类都含有Write()方法，用于写单个字节或者字节数组。但是我们通常不会用到上述方法，他们之所以存在是因为别的类可以使用它们，以便提供更有用的接口。

我们很少使用单一的类来创建流对象，而是叠加多个对象来提供所期望的功能(这是装饰器的设计模式)

#### InputStream类型

InputStream的作用是用来表示那些从不同数据源产生输入的类：

1. 字节数组(ByteArrayInputStream)。

2. String对象(StringBufferInputStream)，@Deprecated。

3. "管道"，工作方式与实际管道相似，即，从一端输入，从另一端输出。

4. 一个由其他种类的流组成的序列，以便我们可以将它们收集合并到一个流中。

5. 其他数据源，如Internet连接等

| 类 | 功能 | 构造器参数，如何使用 |
|---- |:-----:| -----:|
| ByteArrayInputStream | 允许经内存的缓存区当做InputStream使用 | 缓冲区，字节将从中取出；作为一个数据源：将其与FilterInputStream对象相连以提供有用的接口 |
| StringBufferInputStream | 将String装换为InputStream | 字符串，底层实现实际上使用java.lang.StringBuffer |
| FileInputStream | 用于从文件中读取信息 | 字符串，表示文件名或者文件或FileDescriptor对象；作为一种数据源：将其与FilterInputStream对象相连以提供有用的接口 |
| PipedInputStream | 产生用于写入相关PipedOutputStream的数据。实现管道化的概念 | PipedOutputStream；作为多线程中数据源：将其与FilterInputStream对象相连以提供有用的接口 |
| SequenceInputStream | 将两个或者多个InputStream对象装换成单一的InputStream | 两个InputStream对象后者一个容纳InputStream对象的容器Enumeration；将其与FilterInputStream对象相连以提供有用的接口 |
| FilterInputStream | 抽象类，作为装饰器的接口。其中，装饰器为其他的InputStream提供有用的功能 | |

<h3 id="18.3">18.3 添加属性和有用接口</h3>

<h3 id="18.4">18.4 Reader和Writer</h3>

<h3 id="18.5">18.5 自我独立的类：RandomAccessFile</h3>

<h3 id="18.6">18.6 I/O流经典实用方式</h3>

#### 缓冲输入文件

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class BufferedInputFile{
		public static String read(String filename)throws IOException{
			BufferedReader in = new BufferedReader(new FileReader(filename)) ;
			//BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename))) ;
			String s ;
			StringBuilder result = new StringBuilder() ;
			while((s = in.readLine()) != null){
				result.append(s + "\n") ;
			}
			in.close() ;
			return result.toString() ;
		}

		public static void main(String args[])throws IOException{
			System.out.println(read("D:\\Thinking_in_Java_Demo\\interface\\AdaptRandomDoubles.java")) ;
		}
	}/* Output:
		package org.fmz.interfaces ;

		import java.nio.* ;
		import java.util.* ;

		public class AdaptRandomDoubles extends RandomDoubles implements Readable{
				private int count ;
				public AdaptRandomDoubles(int count){
						this.count = count ;
				}
				public int read(CharBuffer cb){
						if(count-- == 0){
								return -1 ;
						}
						String result = Double.toString(next()) + " " ;
						cb.append(result) ;
						return result.length() ;
				}

				public static void main(String args[]){
						Scanner s = new Scanner(new AdaptRandomDoubles(20)) ;
						while(s.hasNextDouble()){
								System.out.print(s.nextDouble() + " ") ;
						}
				}
		}
	*/

>字符串result用来累计文件的全部内容(包括必须添加的换行符，因为readLine()方法已将它们删除。)，最后调用close()关闭文件。

#### 从内存输入

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class MemoryInput{
		public static void main(String args[])throws Exception{
			StringReader in = new StringReader(BufferedInputFile.read("MemoryInput.java")) ;
			int c ;
			while((c = in.read()) != -1){
				System.out.print((char)c) ;
			}
		}
	}/* Output:
		package org.fmz.io ;

		import java.io.* ;

		public class MemoryInput{
				public static void main(String args[])throws Exception{
						StringReader in = new StringReader(BufferedInputFile.read("MemoryInput.java")) ;
						int c ;
						while((c = in.read()) != -1){
								System.out.print((char)c) ;
						}
				}
		}
	*/

>read()方法是以int形式返回下一个字符，因此必须类型转换为char才能正确打印。

#### 格式化的内存输入

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class FormattedMemoryInput{
		public static void main(String args[])throws IOException{
			try{
				DataInputStream in = new DataInputStream(
					new ByteArrayInputStream(
						BufferedInputFile.read("FormattedMemoryInput.java").getBytes())) ;
				while(true){
					System.out.print((char)in.readByte()) ;
				}
			}catch(EOFException e){
				System.err.println("End of stream") ;
			}
		}
	}/* Output:
		package org.fmz.io ;

		import java.io.* ;

		public class FormattedMemoryInput{
				public static void main(String args[])throws IOException{
						try{
								DataInputStream in = new DataInputStream(
										new ByteArrayInputStream(
												BufferedInputFile.read("FormattedMemoryInput.java").getBytes())) ;
								while(true){
										System.out.print((char)in.readByte()) ;
								}
						}catch(EOFException e){
								System.err.println("End of stream") ;
						}
				}
		}
		End of stream
	*/

如果是一次只读一个字节，可以用下面的示例：

	package org.fmz.io ;

	import java.io.* ;

	public class TestEOF{
		public static void main(String args[])throws IOException{
			DataInputStream in = new DataInputStream(
				new BufferedInputStream(
					new FileInputStream("TestEOF.java"))) ;
			while(in.available() != 0){
				System.out.print((char)in.readByte()) ;
			}
		}
	}/* Output:
		package org.fmz.io ;

		import java.io.* ;

		public class TestEOF{
				public static void main(String args[])throws IOException{
						DataInputStream in = new DataInputStream(
								new BufferedInputStream(
										new FileInputStream("TestEOF.java"))) ;
						while(in.available() != 0){
								System.out.print((char)in.readByte()) ;
						}
				}
		}
	*/

>注意：available()方法的工作方式会随着读取的媒介类型的不同而有所不同；字面的意思是说在没有阻塞的情况下所能读取的字节数。

#### 基本文件的输出

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class BasicFileOutput{
		static String file = "BasicFileOutput.out" ;
		public static void main(String args[])throws IOException{
			BufferedReader in = new BufferedReader(
				new StringReader(
					BufferedInputFile.read("BasicFileOutput.java"))) ;
			/*BufferedReader in = new BufferedReader(
				new FileReader("BasicFileOutput.java")) ;*/
			PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter(file))) ;
			int linecount = 1 ;
			String s ;
			while((s = in.readLine()) != null){
				out.println(linecount++ + ":" + s) ;
			}
			out.close() ;
			System.out.println(BufferedInputFile.read(file)) ;
		}
	}/* Output:
		1:package org.fmz.io ;
		2:
		3:import java.io.* ;
		4:
		5:public class BasicFileOutput{
		6:      static String file = "BasicFileOutput.out" ;
		7:      public static void main(String args[])throws IOException{
		8:              BufferedReader in = new BufferedReader(
		9:                      new StringReader(
		10:                             BufferedInputFile.read("BasicFileOutput.java"))) ;
		11:             PrintWriter out = new PrintWriter(
		12:                     new BufferedWriter(new FileWriter(file))) ;
		13:             int linecount = 1 ;
		14:             String s ;
		15:             while((s = in.readLine()) != null){
		16:                     out.println(linecount++ + ":" + s) ;
		17:             }
		18:             out.close() ;
		19:             System.out.println(BufferedInputFile.read(file)) ;
		20:     }
		21:}
	*/

文本输出的快捷方式：

	package org.fmz.io ;

	import java.io.* ;

	public class FileOutputShortcut{
		static String file = "BasicFileOutput.out" ;
		public static void main(String args[])throws IOException{
			BufferedReader in = new BufferedReader(
				new StringReader(
					BufferedInputFile.read("FileOutputShortcut.java"))) ;
			PrintWriter out = new PrintWriter(file) ;
			int linecount = 1 ;
			String s ;
			while((s = in.readLine()) != null){
				out.println(linecount++ + ":" + s) ;
			}
			out.close() ;
			System.out.println(BufferedInputFile.read(file)) ;
		}
	}/* Output:
		1:package org.fmz.io ;
		2:
		3:import java.io.* ;
		4:
		5:public class FileOutputShortcut{
		6:      static String file = "BasicFileOutput.out" ;
		7:      public static void main(String args[])throws IOException{
		8:              BufferedReader in = new BufferedReader(
		9:                      new StringReader(
		10:                             BufferedInputFile.read("FileOutputShortcut.java"))) ;
		11:             PrintWriter out = new PrintWriter(file) ;
		12:             int linecount = 1 ;
		13:             String s ;
		14:             while((s = in.readLine()) != null){
		15:                     out.println(linecount++ + ":" + s) ;
		16:             }
		17:             out.close() ;
		18:             System.out.println(BufferedInputFile.read(file)) ;
		19:     }
		20:}
	*/

#### 储存和恢复数据

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class StoringAndRecoveringData{
		public static void main(String args[])throws IOException{
			DataOutputStream out = new DataOutputStream(
				new BufferedOutputStream(
					new FileOutputStream("Data.txt"))) ;
			out.writeDouble(3.14159) ;
			out.writeUTF("That is pi") ;
			out.writeDouble(1.1413) ;
			out.writeUTF("Square root of 2") ;
			out.close() ;
			DataInputStream in = new DataInputStream(
				new BufferedInputStream(
					new FileInputStream("Data.txt"))) ;
			System.out.println(in.readDouble()) ;
			System.out.println(in.readUTF()) ;
			System.out.println(in.readDouble()) ;
			System.out.println(in.readUTF()) ;
		}
	}/* Output:
		3.14159
		That is pi
		1.1413
		Square root of 2
	*/

#### 读写随机访问文件

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class UsingRandomAccessFile{
		static String file = "rtest.dat" ;
		static void display()throws IOException{
			RandomAccessFile rf = new RandomAccessFile(file, "r") ;
			for(int i=0;i<7;i++){
				System.out.println(
					"Value " + i + ": " + rf.readDouble()) ;
			}
			System.out.println(rf.readUTF()) ;
			rf.close() ;
		}

		public static void main(String args[])throws IOException{
			RandomAccessFile rf = new RandomAccessFile(file, "rw") ;
			for(int i=0;i<7;i++){
				rf.writeDouble(i*1.414) ;
			}
			rf.writeUTF("The end of this file") ;
			rf.close() ;
			display() ;
			rf = new RandomAccessFile(file, "rw") ;
			rf.seek(5*8) ;
			rf.writeDouble(47.0001) ;
			rf.close() ;
			display() ;
		}
	}/* Output:
		Value 0: 0.0
		Value 1: 1.414
		Value 2: 2.828
		Value 3: 4.242
		Value 4: 5.656
		Value 5: 7.069999999999999
		Value 6: 8.484
		The end of this file
		Value 0: 0.0
		Value 1: 1.414
		Value 2: 2.828
		Value 3: 4.242
		Value 4: 5.656
		Value 5: 47.0001
		Value 6: 8.484
		The end of this file
	*/

#### 管道流

PipedInputStream、PipedOutputStream、PipedReader、PipedWriter，它们的价值只有在我们开始理解多线程之后才会显现，因为管道流用于任务之间的通信。

<h3 id="18.7">18.7 文件读写的实用工具</h3>

示例代码：

	package org.fmz.io ;

	import java.io.* ;
	import java.util.* ;

	public class TextFile extends ArrayList<String>{
		//Read a file as a single String
		public static String read(String fileName){
			StringBuilder sb = new StringBuilder() ;
			try{
				BufferedReader in = new BufferedReader(
					new FileReader(
						new File(fileName).getAbsoluteFile())) ;
				try{
					String s ;
					while((s = in.readLine()) != null){
						sb.append(s) ;
						sb.append("\n") ;
					}
				}finally{
					in.close() ;
				}
			}catch(IOException e){
				throw new RuntimeException() ;
			}
			return sb.toString() ;
		}

		//Write a file in one method call
		public static void write(String fileName, String text){
			try{
				PrintWriter out = new PrintWriter(
					new File(fileName).getAbsoluteFile()) ;
				try{
					out.print(text) ;
				}finally{
					out.close() ;
				}
			}catch(IOException e){
				throw new RuntimeException(e) ;
			}
		}

		//Read a file, split by any regular expression
		public TextFile(String fileName, String splitter){
			super(Arrays.asList(read(fileName).split(splitter))) ;
			if(get(0).equals("")){
				remove(0) ;
			}
		}

		public TextFile(String fileName){
			this(fileName, "\n") ;
		}

		public void write(String fileName){
			try{
				PrintWriter out = new PrintWriter(
					new File(fileName).getAbsoluteFile()) ;
				try{
					for(String item : this){
						out.println(item) ;
					}
				}finally{
					out.close() ;
				}
			}catch(IOException e){
				throw new RuntimeException(e) ;
			}
		}

		public static void main(String args[]){
			String file = read("TextFile.java") ;
			write("test.txt", file) ;
			TextFile text = new TextFile("test.txt") ;
			text.write("text2.txt") ;
			TreeSet<String> words = new TreeSet<String>(new TextFile("TextFile.java", "\\W+")) ;
			System.out.println(words.headSet("a")) ;//返回的元素严格小于"a"(只返回大写)
		}
	}/* Output:
		[0, ArrayList, Arrays, BufferedReader, File, FileReader, IOException, PrintWriter, Read, RuntimeException, String, Strin
		gBuilder, System, TextFile, TreeSet, W, Write]
	*/

#### 读取二进制文件

示例代码：

	package org.fmz.io ;

	import java.io.* ;

	public class BinaryFile{
		public static byte[] read(File bFile)throws IOException{
			BufferedInputStream bf = new BufferedInputStream(
				new FileInputStream(bFile)) ;
			try{
				byte[] data = new byte[bf.available()] ;
				bf.read(data) ;
				return data ;
			}finally{
				bf.close() ;
			}
		}

		public static byte[] read(String bFile)throws IOException{
			return read(new File(bFile).getAbsoluteFile()) ;
		}

		public static void main(String args[])throws IOException{
			byte[] B = read("BinaryFile.java") ;
			for(byte b : B){
				System.out.print((char)b) ;
			}
		}
	}/* Output:
		package org.fmz.io ;

		import java.io.* ;

		public class BinaryFile{
				public static byte[] read(File bFile)throws IOException{
						BufferedInputStream bf = new BufferedInputStream(
								new FileInputStream(bFile)) ;
						try{
								byte[] data = new byte[bf.available()] ;
								bf.read(data) ;
								return data ;
						}finally{
								bf.close() ;
						}
				}

				public static byte[] read(String bFile)throws IOException{
						return read(new File(bFile).getAbsoluteFile()) ;
				}

				public static void main(String args[])throws IOException{
						byte[] B = read("BinaryFile.java") ;
						for(byte b : B){
								System.out.print((char)b) ;
						}
				}
		}
	*/

<h3 id="18.8">18.8 标准I/O</h3>

程序的所有输入都可以来自于标准输入，它所有的输出都可以发送大标准输出，以及所有的错误信息都都可以发送到标准错误。标准I/O的意义：我们可以很容易的将程序串联起来，一个程序的标准输出可以成为另一个程序的标准输入。

#### 从标准输入中读取

Java提供了System.in、System.out、System.err。其中，System.out和System.err已经事先包装成了PrintStream对象。这意味着我们可以立即使用System.out和System.err。但是在读取System.in之前必须对其进行包装：

	package org.fmz.io ;

	import java.io.* ;

	public class Echo{
		public static void main(String args[])throws IOException{
			BufferedReader stdin = new BufferedReader(
				new InputStreamReader(System.in)) ;
			String s ;
			while((s = stdin.readLine()) != null && s.length() != 0){
				System.out.println(s) ;
			}
		}
	}

#### 将System.out装换成PrintWriter

	package org.fmz.io ;

	import java.io.* ;

	public class ChangeSystemOut{
		public static void main(String args[]){
			PrintWriter out = new PrintWriter(
				System.out, true) ;
			out.println("Hello World!!!") ;
		}
	}/* Output:
		Hello World!!!
	*/

>在这里使用PrintWriter更加方便，但是PrintWriter的println()方法调用时，就会一次性的将字符串转化为字节写入文件中，这样的做法是效率很低。

>优点是：该类自动吞噬了IOException，当你不想频繁处理异常的时候可以用此类。

更有效率的方法是：

	package org.fmz.io ;

	import java.io.* ;

	public class ChangeSystemOut2{
		public static void main(String args[])throws IOException{
			BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(System.out)) ;
			out.write("Hello World!!!") ;
			out.close() ;
		}
	}/* Output:
		Hello World!!!
	*/

>凡是涉及到缓存(Buffer)，一定要关闭流，只有关闭流内存才能得到刷新，对应才会有流的输入或者输出。

更加完美的办法就是将PrintWriter和BufferWriter组合使用，Java I/O的装配器模式提供了这种灵活性，但是也同样增加了代码的复杂性(有时候创建一个简单的流需要创建很多核心类)：

	package org.fmz.io ;

	import java.io.* ;

	public class ChangeSystemOut3{
		public static void main(String args[]){
			PrintWriter out = new PrintWriter(
				new BufferedWriter(
					new OutputStreamWriter(System.out))) ;
			out.println("Hello World!!!") ;
			out.close() ;
		}
	}/* Output:
		Hello World!!!
	*/

#### 标准I/O的重定向

如果我们开始在显示器上创建大量的输出，而这些输出滚动的太快以至于无法阅读时，重定向输出就显得极为重要：

	package org.fmz.io ;

	import java.io.* ;

	public class Redirecting{
		public static void main(String args[])throws IOException{
			PrintStream console = System.out ;
			BufferedInputStream in = new BufferedInputStream(
				new FileInputStream("Redirecting.java")) ;
			PrintStream out = new PrintStream(
				new BufferedOutputStream(
					new FileOutputStream("test.out"))) ;
			System.setIn(in) ;
			System.setOut(out) ;
			System.setErr(out) ;
			BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in)) ;
			String s ;
			while((s = br.readLine()) != null){
				System.out.println(s) ;
			}
			out.close() ;
			System.setOut(console) ;
		}
	}

<h3 id="18.9">18.9 进程控制</h3>

package org.fmz.io ;

	import java.io.* ;

	public class OSExecute{
		public static void command(String command){
			boolean err = false ;
			try{
				Process process = new ProcessBuilder(command.split(" ")).start() ;
				BufferedReader result = new BufferedReader(
					new InputStreamReader(process.getInputStream())) ;
				String s ;
				while((s = result.readLine()) != null){
					System.out.println(s) ;
				}

				BufferedReader errors = new BufferedReader(
					new InputStreamReader(process.getErrorStream())) ;
				while((s = errors.readLine()) != null){
					System.err.println(s) ;
					err = true ;
				}
			}catch(Exception e){
				if(!command.startsWith("CMD /C")){
					command("CMD /C" + command) ;
				}else{
					throw new RuntimeException(e) ;
				}
			}
			if(err){
				throw new OSExecuteException("Errors executing " + command) ;
			}
		}

		public static void main(String args[]){
			command("javap java.util.ArrayList") ;
		}
	}/* Output:
		Compiled from "ArrayList.java"
		public class java.util.ArrayList<E> extends java.util.AbstractList<E> implements java.util.List<E>, java.util.RandomAccess, java.lang.Cloneable, java.io.Serializable {
		  transient java.lang.Object[] elementData;
		  public java.util.ArrayList(int);
		  public java.util.ArrayList();
		  public java.util.ArrayList(java.util.Collection<? extends E>);
		  public void trimToSize();
		  public void ensureCapacity(int);
		  public int size();
		  public boolean isEmpty();
		  public boolean contains(java.lang.Object);
		  public int indexOf(java.lang.Object);
		  public int lastIndexOf(java.lang.Object);
		  public java.lang.Object clone();
		  public java.lang.Object[] toArray();
		  public <T> T[] toArray(T[]);
		  E elementData(int);
		  public E get(int);
		  public E set(int, E);
		  public boolean add(E);
		  public void add(int, E);
		  public E remove(int);
		  public boolean remove(java.lang.Object);
		  public void clear();
		  public boolean addAll(java.util.Collection<? extends E>);
		  public boolean addAll(int, java.util.Collection<? extends E>);
		  protected void removeRange(int, int);
		  public boolean removeAll(java.util.Collection<?>);
		  public boolean retainAll(java.util.Collection<?>);
		  public java.util.ListIterator<E> listIterator(int);
		  public java.util.ListIterator<E> listIterator();
		  public java.util.Iterator<E> iterator();
		  public java.util.List<E> subList(int, int);
		  static void subListRangeCheck(int, int, int);
		  public void forEach(java.util.function.Consumer<? super E>);
		  public java.util.Spliterator<E> spliterator();
		  public boolean removeIf(java.util.function.Predicate<? super E>);
		  public void replaceAll(java.util.function.UnaryOperator<E>);
		  public void sort(java.util.Comparator<? super E>);
		  static int access$100(java.util.ArrayList);
		  static {};
		}
	*/

>这里使用了javap反汇编器来反汇编java.util.ArrayList这个类。

<h3 id="18.10">18.10 新I/O</h3>

JDK 1.4的java.nio.*包中引入的新的Java I/O类库，其目的在于提高速度。实际上，就的I/O包已经使用nio重新实现过，以便充分利用这种速度提高。

速度的提高来源于所使用的结构更接近与操作系统执行I/O的方式：通道和缓冲器。

唯一直接与通道进行交互的缓冲器是ByteBuffer

线面演示可写的(FileOutputStream)、可读的(FileInputStream)以及即可以写有可以读的(RandomAccessFile)的通道：

	package org.fmz.io ;

	import java.io.* ;
	import java.nio.* ;
	import java.nio.channels.* ;

	public class GetChannel{
		private static final int BSIZE = 1024 ;
		public static void main(String args[])throws IOException{
			//Write a file
			FileChannel fc = new FileOutputStream("data.txt").getChannel() ;
			fc.write(ByteBuffer.wrap("Some text ".getBytes())) ;
			fc.close() ;
			//Add to the end of the file
			fc = new RandomAccessFile("data.txt", "rw").getChannel() ;
			fc.position(fc.size()) ;//Move to the end
			fc.write(ByteBuffer.wrap("Some more".getBytes())) ;
			fc.close() ;
			//Read the file
			fc = new FileInputStream("data.txt").getChannel() ;
			ByteBuffer buff = ByteBuffer.allocate(BSIZE) ;
			fc.read(buff) ;
			buff.flip() ;
			while(buff.hasRemaining()){
				System.out.print((char)buff.get()) ;
			}
		}
	}/* Output:
		Some text Some more
	*/

简单的文件复制程序：

	package org.fmz.io ;

	import java.io.* ;
	import java.nio.* ;
	import java.nio.channels.* ;

	public class ChannelCopy{
		private static final int BSIZE = 1024 ;
		public static void main(String args[])throws Exception{
			if(args.length != 2){
				System.out.println("arguements: sourcefile destfile") ;
				System.exit(1) ;
			}
			FileChannel
				in = new FileInputStream(args[0]).getChannel(),
				out = new FileOutputStream(args[1]).getChannel() ;
			ByteBuffer buff = ByteBuffer.allocate(BSIZE) ;
			while(in.read(buff) != -1){
				buff.flip() ;//Prepare for writing
				out.write(buff) ;
				buff.clear() ;//Prepare for reading
			}
		}
	}

从一个通道转到另外一个通道是上述代码的完美方案：

	package org.fmz.io ;

	import java.io.* ;
	import java.nio.* ;
	import java.nio.channels.* ;

	public class ChannelCopy2{
		public static void main(String args[])throws Exception{
			if(args.length != 2){
				System.out.println("arguements: sourcefile destfile") ;
				System.exit(1) ;
			}
			FileChannel
				in = new FileInputStream(args[0]).getChannel(),
				out = new FileOutputStream(args[1]).getChannel() ;
			in.transferTo(0, in.size(), out) ;
			//or:
			//out.transferTo(in, 0, out.size())
		}
	}

#### 转换数据

待续...

<h3 id="18.11">18.11 压缩</h3>

压缩的类库是按字节的方式而不是字符的方式处理的。

#### 用GZIP进行简单压缩

示例代码：

	package org.fmz.io ;

	import java.io.* ;
	import java.util.zip.* ;

	public class GZIPCompress{
		public static void main(String args[])throws IOException{
			if(args.length == 0){
				System.out.println(
					"Usage: \nGZIPCompress file\n" + 
						"\tUses GZIP compression to compress the file to test.gz")  ;
				System.exit(0) ;
			}
			BufferedReader in = new BufferedReader(
				new FileReader(args[0])) ;
			BufferedOutputStream out = new BufferedOutputStream(
				new GZIPOutputStream(
					new FileOutputStream("test.gz"))) ;
			System.out.println("Writing file") ;
			int c ;
			while((c = in.read()) != -1){
				out.write(c) ;
			}
			in.close() ;
			out.close() ;
			System.out.println("Reading file") ;
			BufferedReader in2 = new BufferedReader(
				new InputStreamReader(
					new GZIPInputStream(
						new FileInputStream("test.gz")))) ;
			String s ;
			while((s = in2.readLine()) != null){
				System.out.println(s) ;
			}
		}
	}/* OutputStream
		Writing file
		Reading file
		package org.fmz.io ;

		import java.io.* ;
		import java.util.zip.* ;

		public class GZIPCompress{
				public static void main(String args[])throws IOException{
						if(args.length == 0){
								System.out.println(
										"Usage: \nGZIPCompress file\n" +
												"\tUses GZIP compression to compress the file to test.gz")  ;
								System.exit(0) ;
						}
						BufferedReader in = new BufferedReader(
								new FileReader(args[0])) ;
						BufferedOutputStream out = new BufferedOutputStream(
								new GZIPOutputStream(
										new FileOutputStream("test.gz"))) ;
						System.out.println("Writing file") ;
						int c ;
						while((c = in.read()) != -1){
								out.write(c) ;
						}
						in.close() ;
						out.close() ;
						System.out.println("Reading file") ;
						BufferedReader in2 = new BufferedReader(
								new InputStreamReader(
										new GZIPInputStream(
												new FileInputStream("test.gz")))) ;
						String s ;
						while((s = in2.readLine()) != null){
								System.out.println(s) ;
						}
				}
		}
	*/

>当写入文件的时候，如果用Stream流对象，则可以选择用`BufferedOutputStream`或者`PrintStream`或者二者结合使用。上述代码使用BufferedOutputStream，下面的代码分别使用PrintStream或者二者结合使用

	package org.fmz.io ;

	import java.io.* ;
	import java.util.zip.* ;

	public class GZIPCompress2{
		public static void main(String args[])throws IOException{
			if(args.length == 0){
				System.out.println(
					"Usage: \nGZIPCompress file\n" + 
						"\tUses GZIP compression to compress the file to test.gz")  ;
				System.exit(0) ;
			}
			BufferedReader in = new BufferedReader(
				new FileReader(args[0])) ;
			PrintStream out = new PrintStream(
				new GZIPOutputStream(
					new FileOutputStream("test.gz"))) ;
			System.out.println("Writing file") ;
			int c ;
			while((c = in.read()) != -1){
				out.write(c) ;
			}
			in.close() ;
			out.close() ;
			System.out.println("Reading file") ;
			BufferedReader in2 = new BufferedReader(
				new InputStreamReader(
					new GZIPInputStream(
						new FileInputStream("test.gz")))) ;
			String s ;
			while((s = in2.readLine()) != null){
				System.out.println(s) ;
			}
		}
	}

> 使用PrintStream

	package org.fmz.io ;

	import java.io.* ;
	import java.util.zip.* ;

	public class GZIPCompress3{
		public static void main(String args[])throws IOException{
			if(args.length == 0){
				System.out.println(
					"Usage: \nGZIPCompress file\n" + 
						"\tUses GZIP compression to compress the file to test.gz")  ;
				System.exit(0) ;
			}
			BufferedReader in = new BufferedReader(
				new FileReader(args[0])) ;
			PrintStream out = new PrintStream(
				new BufferedOutputStream(
					new GZIPOutputStream(
						new FileOutputStream("test.gz")))) ;
			System.out.println("Writing file") ;
			int c ;
			while((c = in.read()) != -1){
				out.write(c) ;
			}
			in.close() ;
			out.close() ;
			System.out.println("Reading file") ;
			BufferedReader in2 = new BufferedReader(
				new InputStreamReader(
					new GZIPInputStream(
						new FileInputStream("test.gz")))) ;
			String s ;
			while((s = in2.readLine()) != null){
				System.out.println(s) ;
			}
		}
	}

>PrintStream和BufferedOutputStream结合使用。

>上述的压缩方式适合对单个数据流(而不是一系列互异的数据)进行压缩

#### 使用zip进行多文件的压缩

示例代码：

	package org.fmz.io ;

	import java.io.* ;
	import java.util.* ;
	import java.util.zip.* ;

	public class ZipCompress{
		public static void main(String args[])throws IOException{
			FileOutputStream f = new FileOutputStream("test.zip") ;
			CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32()) ;
			ZipOutputStream zos = new ZipOutputStream(csum) ;
			BufferedOutputStream out = new BufferedOutputStream(zos) ;
			zos.setComment("A test of Java Zipping") ;
			for(String arg : args){
				System.out.println("Writing file " + arg) ;
				BufferedReader in = new BufferedReader(
					new FileReader(arg)) ;
				zos.putNextEntry(new ZipEntry(arg)) ;
				int c ;
				while((c = in.read()) != -1){
					out.write(c) ;
				}
				in.close() ;
				out.flush() ;
			}
			out.close() ;
			System.out.println("Checksum: " + csum.getChecksum().getValue()) ;
			System.out.println("Reading file") ;
			FileInputStream fi = new FileInputStream("test.zip") ;
			CheckedInputStream csumi = new CheckedInputStream(fi, new Adler32()) ;
			ZipInputStream in2 = new ZipInputStream(csumi) ;
			BufferedInputStream bis = new BufferedInputStream(in2) ;
			ZipEntry ze ;
			while((ze = in2.getNextEntry()) != null){
				System.out.println("Reading file " + ze) ;
			}
			int x ;
			while((x = bis.read()) != -1){
				System.out.write(x) ;
			}
			if(args.length ==1){
				System.out.println("Checkedsum: " + csumi.getChecksum().getValue()) ;
				bis.close() ;
				ZipFile zf = new ZipFile("test.zip") ;
				Enumeration e = zf.entries() ;
				while(e.hasMoreElements()){
					ZipEntry ze2 = (ZipEntry)e.nextElement() ;
					System.out.println("File: " + ze2) ;
				}
			}
		}
	}/* Output:
		args: ZipCompress.java GZIPCompress3.java GZIPCompress2.java GZIPCompress.java
			Writing file ZipCompress.java
			Writing file GZIPCompress3.java
			Writing file GZIPCompress2.java
			Writing file GZIPCompress.java
			Checksum: 181762027
			Reading file
			Reading file ZipCompress.java
			Reading file GZIPCompress3.java
			Reading file GZIPCompress2.java
			Reading file GZIPCompress.java
	*/

#### Java的档案文件

JAR(Java ARchive, Java档案文件)。

JAR文件语法：`jar [options] destination [manifest] inputfiles`

各个options的含义：

c：创建一个新的或者空的压缩文档；t：列出目录表；x：解压所有文件；xfile：解压该文件；f: 打算指定一个文件名，如果没有这个项，jar假设所有的输入都是来自于标准输入，或者创建对象时，输出也是标准输出；m: 表示第一个参数将是用户自建的清单文件的名字；v：产生详细输出，描述jar所做的工作；O: 只储存文件，不压缩文件；M：不自动创建文件清单。

典型用法：

`jar cf myJarFile.jar *.class`，支持\*.class，例如：`jar cf myJarFile.jar org/fmz/io/*.class`

`jar cmf myJarFile.jar myMainfestFile.mf *.class`，表示：添加一个名为myMainfestFile.mf的用户自建清单

`jar tf myJarFile.jar`，表示：列出myJarFile.jar内所有文件的一个目录表

`jar tvf myJarFile.jar`，表示：提供有关myJarFile.jar中文件的更详细的信息。

`jar cvf myJarFile.jar audio classes images`，表示：将子目录aduio、classes、images合并到myJarFile.jar文件中

`jar uvf myJarFile.jar file`，表示：更新指定JAR文件夹中的文件夹或文件内容

<h3 id="18.12">18.12 对象序列化</h3>

待续...

<h3 id="18.13">18.13 XML</h3>

待续...

<h3 id="18.14">18.14 Preference</h3>

待续...

---

<h2 id="19">19 枚举类型</h2>

关键字enum可以将一组具名的值的有限集合创建为一种新的类型，而这些具名的值可以作为常规的程序组件使用，只是一个非常有用的功能。

待续...

---

<h2 id="20">20 注解</h2>

待续...

<h2 id="21">21 并发</h2>

并行编程可以使程序执行速度极大的提高，或者为设计某种类型的程序提供了更易用的模型，或者两者兼有。

<h3 id="21.1">21.1 并发的多面性</h3>

- 更快的执行
- 改进代码设计

<h3 id="21.2">21.2 基本的线程机制</h3>

并发编程可以使我们将程序划分为多个分离的、独立运行的任务。通过使用多线程机制，这些独立的任务(也别称为子任务)中的每一个都将由执行线程来驱动。一个线程就是在进程中的一个单一的顺序控制流，因此，单个进程可以拥有多个并发执行的任务，但是你的程序使得每个任务都好像有其自己的CPU一样。其底层是切分CPU时间，通常不需要你考虑。

线程模型为编程带来了便利，它简化了在单一程序中同时交织在一起的多个操作处理。在使用线程时，CPU将轮流给每一个任务分配其占用时间。每个人物觉得自己一直占用CPU，但事实上CPU时间是划分成片段分配给所有的任务(例外的情况是程序确实运行在多个CPU之上)。

<h4 id="21.2.1">21.2.1 定义任务</h4>

#### 定义任务

示例代码：

	/*
	 * 标识符id可以用来区分任务的多个实例，它是final的，因为一旦被初始化之后就不希望被修改
	 * Thread.yield()是线程调度器，可以将CPU从一个线程转移到另外一个线程
	 */
	package org.fmz.concurrency ;

	public class LiftOff implements Runnable{
		protected int countDown = 10 ;
		private static int taskCount = 0 ;
		private int id = taskCount++ ;// if private not necessary declared final
		public LiftOff(){}
		public LiftOff(int countDown){
			this.countDown = countDown ;
		}
		public String status(){
			return "#" + id + "(" +
				(countDown > 0 ? countDown : "LiftOff!") + "). " ;
		}
		public void run(){
			while(countDown-- > 0){
				System.out.print(status()) ;
				Thread.yield() ;
			}
		}
	}

> 在run()方法中对静态方法Thread.yield()的调用是对线程调度器(Java线程机制的一部分，可以将CPU从一个线程转移到另外一个线程)的一种建议。

下面是示例run()不是由单独的线程驱动的，它是在main()方法中直接调用的，实际上这里仍然是一个线程，即总是分配各main()的那个线程。

	package org.fmz.concurrency ;

	public class MainThread{
		public static void main(String args[]){
			LiftOff launch = new LiftOff() ;
			launch.run() ;
		}
	}/* Output:
		#0(9). #0(8). #0(7). #0(6). #0(5). #0(4). #0(3). #0(2). #0(1). #0(LiftOff!).
	*/

#### Thread类

用Thread来驱动LiftOff对象。示例代码：

	package org.fmz.concurrency ;

	public class BasicThreads{
		public static void main(String args[]){
			Thread t = new Thread(new LiftOff(50)) ;
			t.start() ;
			System.out.println("Waiting for LiftOff...") ;
		}
	}/* Output:
		Waiting for LiftOff...
		#0(49). #0(48). #0(47). #0(46). #0(45). #0(44). #0(43). #0(42). #0(41). #0(40). #0(39). #0(38). #0(37). #0(36). #0(35).
		#0(34). #0(33). #0(32). #0(31). #0(30). #0(29). #0(28). #0(27). #0(26). #0(25). #0(24). #0(23). #0(22). #0(21). #0(20).
		#0(19). #0(18). #0(17). #0(16). #0(15). #0(14). #0(13). #0(12). #0(11). #0(10). #0(9). #0(8). #0(7). #0(6). #0(5). #0(4)
		. #0(3). #0(2). #0(1). #0(LiftOff!).
	*/

还可以添加更多的线程去执行更多的任务

	package org.fmz.concurrency ;

	public class MoreBasicThreads{
		public static void main(String args[]){
			for(int i=0;i<5;i++){
				new Thread(new LiftOff()).start() ;
			}
			System.out.println("Waiting for LiftOff...") ;
		}
	}/* Output:
		#1(9). #3(9). #3(8). #4(9). #4(8). #4(7). #4(6). #4(5). #4(4). #4(3). #4(2). #4(1). #4(LiftOff!). #0(9). Waiting for Lif
		tOff...
		#2(9). #0(8). #3(7). #1(8). #3(6). #0(7). #0(6). #0(5). #2(8). #0(4). #3(5). #1(7). #1(6). #1(5). #1(4). #1(3). #1(2). #
		1(1). #1(LiftOff!). #3(4). #0(3). #2(7). #2(6). #2(5). #2(4). #2(3). #0(2). #0(1). #0(LiftOff!). #3(3). #3(2). #3(1). #3
		(LiftOff!). #2(2). #2(1). #2(LiftOff!).
		Or
		#2(9). #4(9). #4(8). #4(7). #4(6). #4(5). #4(4). #4(3). #4(2). #4(1). #3(9). Waiting for LiftOff...
		#0(9). #1(9). #0(8). #3(8). #4(LiftOff!). #2(8). #3(7). #0(7). #1(8). #1(7). #0(6). #3(6). #3(5). #2(7). #3(4). #0(5). #
		1(6). #0(4). #3(3). #2(6). #2(5). #2(4). #2(3). #2(2). #2(1). #2(LiftOff!). #3(2). #0(3). #1(5). #0(2). #3(1). #0(1). #1
		(4). #0(LiftOff!). #3(LiftOff!). #1(3). #1(2). #1(1). #1(LiftOff!).
		Or
		Waiting for LiftOff...
		#3(9). #4(9). #4(8). #1(9). #2(9). #0(9). #0(8). #0(7). #0(6). #0(5). #2(8). #1(8). #4(7). #3(8). #4(6). #1(7). #2(7). #
		0(4). #0(3). #0(2). #0(1). #2(6). #1(6). #4(5). #3(7). #4(4). #1(5). #2(5). #0(LiftOff!). #2(4). #1(4). #4(3). #3(6). #4
		(2). #1(3). #2(3). #2(2). #2(1). #2(LiftOff!). #1(2). #1(1). #1(LiftOff!). #4(1). #4(LiftOff!). #3(5). #3(4). #3(3). #3(
		2). #3(1). #3(LiftOff!).
		每次执行都会有不同的情况，加上main()方法总共有六个线程，随机的在这六个线程之间进行切换
	*/

> 输出说明不同任务的执行在线程被换进换出时混在了一起。这种交换是由线程调度器自动控制的。

#### 使用Executor

多线程。示例代码：

	package org.fmz.concurrency;

	import java.util.concurrent.*;

	public class CachedThreadPool{
		public static void main(String args[]){
			ExecutorService exec = Executors.newCachedThreadPool();
			for(int i=0;i<5;i++){
				exec.execute(new LiftOff());
			}
			exec.shutdown();
		}
	}/* Output:
		#4(9). #2(9). #1(9). #1(8). #1(7). #1(6). #0(9). #3(9). #0(8). #1(5). #1(4). #1(3). #1(2). #2(8). #4(8). #2(7). #1(1). #
		0(7). #3(8). #3(7). #3(6). #3(5). #0(6). #1(LiftOff!). #2(6). #4(7). #2(5). #0(5). #3(4). #0(4). #0(3). #2(4). #4(6). #2
		(3). #0(2). #3(3). #0(1). #2(2). #4(5). #2(1). #0(LiftOff!). #3(2). #2(LiftOff!). #4(4). #3(1). #3(LiftOff!). #4(3). #4(
		2). #4(1). #4(LiftOff!).
	*/

单线程。示例代码：

	package org.fmz.concurrency ;

	import java.util.concurrent.* ;

	public class SingleThreadExecutor{
		public static void main(String args[]){
			ExecutorService exec = Executors.newSingleThreadExecutor() ;
			for(int i=0;i<5;i++){
				exec.execute(new LiftOff()) ;
			}
			exec.shutdown() ;
		}
	}/* Output:
		#0(9). #0(8). #0(7). #0(6). #0(5). #0(4). #0(3). #0(2). #0(1). #0(LiftOff!). #1(9). #1(8). #1(7). #1(6). #1(5). #1(4). #
		1(3). #1(2). #1(1). #1(LiftOff!). #2(9). #2(8). #2(7). #2(6). #2(5). #2(4). #2(3). #2(2). #2(1). #2(LiftOff!). #3(9). #3
		(8). #3(7). #3(6). #3(5). #3(4). #3(3). #3(2). #3(1). #3(LiftOff!). #4(9). #4(8). #4(7). #4(6). #4(5). #4(4). #4(3). #4(
		2). #4(1). #4(LiftOff!).
	*/

待续...

---

<h2 id="22">22 图形化用户界面</h2>

待续...

---
