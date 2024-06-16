---
layout: post
title: 秋季校招笔面试
---

---

**string是不可变(immutable/final)的,如何理解?比较stringbuffer  stringBuiler他们之间区别**

```
String is immutable/final generally for two reasons:

1. String objct are cached in String pool. Since cached String literals are shared between multiple clients there is always a risk, where one client's action would affect another client; Caching of String objects is important for performance reason.

2. String has been widely used as parameter for many java classes, e.g. for opening network connection, you can pass hostname and port number as String; you can pass database URL as a String for opening database connection; you can open any file by passing the name of the file as arguement to File I/O classes.

3. String are very popular as HashMap key, it is important for them to be immutable so that they can be retrieve the value object which is stored in hashmap. HashMap works in the principle of hashing, which requires same value to function properly. Mutable String would product two different hashcodes at the time of insertion and retrieval if contents of String was modified after insertion, potentially losing the value object in the map.

4. The absolutely most important reason that String is immutable is that it is used by the class loader mechanism, and thus have profound and fundamental security aspects.

5. String is immutable, it can safely share between many threads which is very important for multithreaded programming and to avoid any synchronization issue in java. Immutablity also makes String instance thread-safe in java, means you don't need to synchronized String operation externally. 

Difference between String StringBuffer and StringBuilder:

1. Mutability Difference: String is immutable, if you want to change its value, another object will be created, whereas StringBuffer and StringBuilder is multable, so you can change its values.

2. Thread-Safety Difference: StringBuffer is thread-safe, whereas not for StringBuilder, and StringBuilder is more efficient.

So the solutions:

1) If String is not gong to change, use String. 

2) If String is going to change and always for single thread, use StringBuiler for performance.

3) If String is going to change and will be accessed from multiple threads, use a StringBuffer for the thread-safety synchronization
```

*An immutable class for example?*

```
public final class Student{
    private String name;// this is immutable(private)

    public Student(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
```

---

**什么是多线程，为什么要使用多线程，多线程与多进程的区别**

```
What is thread?

1. A thread is an independent set of values of the processor registers(for a single core). Since this includes the Instruction Pointer(aka Program Counter), which controls what executes in what order. It also includes the Stack Pointer, which had better point to a unique area of memory for each thread or else they will interfere with each other.

2. Threads are the software unit affected by control flow(function call, loop, switch), because those instructions operate on the Instruction Pointer, and that belongs to a paritcular thread. Threads are often scheduled arrording to some prioritization scheme(although it is possible to design a system with one thread per processor core, in which case every thread is always running and no shcheduling is needed).

3. In fact the value of the Instruction Pointer and the instruction stored at that location is sufficient to determine a new value for Instruction Pointer. For most instructions, this simply advances the IP by the size of the instruction, but control flow instructions change the IP in other, predicatable ways. The sequence of values the IP takes on forms a path of execution weaving through the program code, giving rise to the name "thread".

ps: The exact list of CPU registers depends on the architecture, but instruction pointer and stack pointer are pretty much universal. They define a thread insofar as when this thread(set of register values) is loaded in the processor core, the thread is running. The processor is fetching instructions demanded by the thread and updateing the thread registers. When a contex switch is needed, the processor saves this set of register values into memeory and loads a set belonging to defferent thread, typically as part of the interrupt servicing logic.

Why we need multi therad?

1. The main reason, in many applications, multiple activities are going on at once. Some of these may block from time to time. By decomposing such an application into multiple sequential threads that run in quasi-parallel, the programming model becomes simpler.

2. Thread is lighter weight than process, they are easier to creat and destroy than processes, which is about 10-100 times faster than create a process.

3. About performance, threads yield no performance gain when all of them are CPU bound, but when there is a substantial
computing and substantial I/O, having threads allows these activities to overlap, thus speeding up the application. 
```

---

**How final keyword works**

```
import java.util.*;
public class TestFinal{
    static final List<Integer> ls1;
    final List<Integer> ls2; 
    public TestFinal(){
        ls1 = new ArrayList<>();// compile error, because static instance is visible to all objects
        ls2 = new ArrayList<>();// correct, because one object can only init once
        ls2.add(1);
        ls2.add(2);
    }

    // Not correct, because the method will be invoked multi times
    public void set(List<Integer> ls){
        ls2 = ls;
    }

    public void add(Integer I){
        ls1.add(I);// correct, because we change the content, not the reference object itself
        ls2.add(I);// same as above
    }
}

About consturctor: Constructor can be invoked only one time per object by useing new keyword. Programmer cann't invoke constructor many times because consturctor is design so.

About method: Method can be invoked as many times as programmer warnts and compiler knows programmer can invoke method multi times.

Final class cannot be subclass

Final method cannot be overridden(this is in baseclass)

Final method can be a override method(this is in subclass)

Final keyword can decorate: class, field(static field), local variable, parameter, method
```

---

**对设计模式的设计原则是如何理解的**

```
设计模式的主要原则有：

1. 开闭原则(OCP, Open-Close Principle)

1) Software entries should be open for extension, but close for modification

2) 通过扩展已有的软件系统，可以通过新的行为，以满足对软件的新需求，使变化中的的软件系统有一定的适应性和灵活性；已有的软件模块，特别是最终要抽象层模块不能再修改，这样使变化中的系统有一点的稳定性和延续性

3) 要做到开闭原则：抽象化是关键；对可变性的封装原则(EVP, Priciple Of Encapsulation of Variation)

2. 里氏替换原则(LSP, Liskov Substitution Priciple)

1) 如果对每一个类型T1的对象o1，都有类型为T2的对象o2，使得T1定义的所有程序P在所有对象o1都替换成o2时，程序P的行为没有发生改变，那么类型T2是类型T1的子类型。也即是说：一个软件实体如果使用的一个基类的话，那么一定适用其子类，而且他根本不能觉察出基类对象和子类对象的区别

2) 里氏替换原则是复用的基石，只有当衍生类可以替换掉基类，软件单位的功能不会受到影响时，基类才能真正的被复用，而衍生类也能够在基类的基础上增加新的行为

3. 依赖倒转原则(DIP, Dependence Inversion Principle)

1) 依赖倒转原则讲的是：要依赖于抽象，不要依赖于具体

2) 抽象层次包含的是应用系统的商务逻辑和宏观的、对整体系统来说重要的战略决定，是必然性的体现；而具体层次则是一些次要的与实现有关的算法和逻辑，以及战术性从决定，带有相当大的偶然性的选择。具体层次的代码使经常会变动的，不能避免出现错误。抽象层次依赖于具体层次使得许多具体层次的细节的算法立刻影响到抽象层次的的宏观商务逻辑，导致微观决定宏观，战术决定战略，这样使很荒唐的事情。依赖倒转原则就是将这种很荒唐的事情反过来

3) 依赖倒转原则的另外一种说法是：要针对接口编程，不要针对具体编程。针对接口编程的意思是：应当使用Java的接口和抽象Java类进行变量的类型声明、参量类型声明、方法的放回类型声明、以及数据的类型转换等

4) 要做到依赖倒转原则的关键是：抽象耦合

4. 接口隔离原则(ISP, Interface Segregation Principle)

1) 几口隔离原则讲的是：使用多个专门的接口，总比是用单一的总接口要好。换言之：一个类对另一个类的依赖性应当是建立在最小的接口上的

2) For example:

// When the robot extends the interface, it must implements eat method, but it can't eat
// We call such interface fat interface or pollute interface

// interface segregation principle - bad example
interface IWorker {
    void work();
    void eat();
}

class Worker implements IWorker{
    public void work(){...}
    public void eat(){...}
}

class SuperWorker implements IWorker{
    public void work(){...}
    public void eat(){...}
}

class Manager{
    IWorker worker;
    
    public void setWorker(IWorker w){
        worker = w;
    }

    public void manage(){
        worker.work();
    }
}

// interface segregation priciple - good example
interface IWorkerable {
    void eat();
}

interface IFeedable{
    public void eat();
}

class Worker implements IWorkable, IFeedable{
    public void work(){...}
    public void eat(){...}
}

class SuperWorker implements IWorkable, IFeedable{
    public void work(){...}
    public void eat(){...}
}

class Robot implements IWorkable{
    public void work(){...}
}

5. 合成/聚合复用原则(CARP, Composite/Aggregate Reuse Priciple)

1) 合成/聚合复用原则也经常叫做合成复用原则(CRP, Compsite Reuse Priciple)。是在一个新的对象里面使用已有的对象，使之成为新对象的一部分；新的对象通过委派达到复用已有功能的目的

2) 尽量使用合成/聚合，尽量不要使用继承

3) 合成(Composition)和聚合(Aggregation)均是关联(Association)的特殊种类。聚合是用来表示"拥有"关系或者整体与部分的关系；而合成则表示一种强的多的"拥有"关系。在一个合成关系里，部分和整体的声明周期是一样的。一个合成的很对象完全拥有对其组成部分的支配权，包括它的创建和泯灭等。程序语言来说：组合而成的新对象组成部分的内存分配、内存释放有绝对责任。合成通常是值的聚合(Aggregation by Value)，而通常所说的聚合是引用聚合(Aggregation by Reference)

4) 继承复用的优点：新的实现比较容易，因为超类中的大部分功能可以通过继承关系自动进入子类；修改或扩展继承而来的实现较为容易

5) 为什么建议使用组合/聚合复用而不建议使用继承：继承复用破坏包装，因为继承将超类中的细节暴露给了子类，这种复用称之为透明复用或者白箱复用；如果超类的实现发生了改变，那么子类的实现也不得不发生改变。因此当基类发生改变的时候，这种改变就像水中投入石子引起的水波，将一圈又一圈的传递给一个又一个的子类当中，使得设计者不得不改变子类以适应这种变化

6. 迪米特法则(LoD, Law of Demeter), 也称之为最少知识原则(LKP, Least Knowledge Principle)
```

---

**类在什么情况下会触发加载**

```
1) A new instance of a class is created(in bytecode, the execution of a new instruction. Alternatively, via implicit creation: reflection, cloning, or deserialization)

2) The invocation of a static method decleared by a class(int bytecode, the execution of an invokestatic instruction)

3) The use or assignment of a static field declared by a class or interface, except for static fields that are final and initialized by a compile-time constant expression(in byte code, the execution of a getstatic or putstatic instruction)

4) The invocation of certain reflective methods in Java API, such as methods in class Class or in classes in the java.lang.reflect package

5) The initialization of a subclass of a class(Initialization of a class requires prior initialization of its superclass)

6) The designation of a class as the initial class(with the main() method) when Java Virtual Machine starts up
```

---

**Java的类加载机制**

```
加载 --> 验证 --> 准备 --> 解析 --> 类初始化 --> 对象实例化

加载、验证、初始化是按照严格的顺序开始的，但并不是按照严格顺序完成的(通常是互相交叉混合进行的)；解析可能发生在初始化之前，也可能发生在初始化之后(为了支持Java语言的运行时绑定)。

Java当中的绑定：将一个方法的调用与一个方法的主体关联起来，分为静态绑定和动态绑定。静态绑定是编译时绑定(compile-time binding)，也叫前期绑定。Java 当中只有final、static、private和constructor(构造方法)是前期绑定的；动态绑定又称之为后期绑定或者运行时绑定，运行时根据对象的类型进行绑定，是Java语言默认的方法绑定方式，几乎所有的方法绑定都是运行是绑定。

一、加载阶段

加载是通过一个类的全权限定名获取其二级制的字节流的过程。将这个字节流所代表的静态存储结构转化为方法去的运行时数据结构，并且在Java的堆上生成一个java.lang.Class对象，作为这些方法区中这些数据的访问入口。

类加载器:

类的加载可以使用系统自带的或者自己定义的类加载完成。

启动类加载器(Bootstrap ClassLoader)：加载JDK/jre/lib/rt.jar，主要用来加载Java的核心类库；如法被Java程序直接引用

扩展类加载器(Extension ClassLoader)：加载JDK/jre/lib/ext目录下的类文件；可以在Java程序中直接引用

应用程序类加载器(Application ClassLoader)：加载用户自定义的ClassPath路径中的类文件；可以在Java程序中直接引用

自定义类加载器(Customer ClassLoader)

启动类加载器 <-- 扩展类加载其 <-- 应用程序加载器 <-- 自定义加载器：称之为双亲委托模型，上一个层次是下一个层次的父加载器，但是它们之间的关系并不是通过继承来完成的，而是使用composition来复用父加载器的代码；双亲委托模型的工作方式是：当前类加载器收到类加载请求 --> 把请求委托给父类加载器完成 --> 依次向上直到启动类加载器 --> 父类无法加载(无法找到相应的类文件) --> 往子类加载器传递 --> 依次向下

二、验证阶段

验证的目的是确保Class文件的字节流包含的信息符合当前虚拟机的要求，而不包含危害虚拟机自身的安全，包括：文件格式、元数据、字节码和符号引用等

三、准备阶段

准别阶段主要是：为类变量分配内存并设置类变量的初始值，这些内存都将在方法区中进行分配；这时候的内存分配仅仅包含类变量(static)，不包含实例变量，实例变量会在对象实例化的过程中随对象分配在堆中，这里的初始化为默认的初始值，而不是Java代码中assign的值

Note:

1. 对于primitive data type对应的类变量和全局变量，不显式的assign值，会用默认的初始值；局部变量不assign值，编译不通过

2. static final修饰的变量或者在声明的时候显式赋值，或者在类初始化的时候赋值，否则编译不通过；只有final修饰的变量或者在声明的时候赋值，或者在对象实例化的过程中赋值，否则编译不通过；总之，final类变量和final实例变量在使用之前都必须赋值，系统不会为其assign默认值

3. 对于引用数据类型(数组或者对象引用)，如果没有显示的为其赋值，则系统赋值为null

4. 数组在初始化时没有为其赋值，则默认为零

5. static final变量在编译的阶段就会将其结果放入调用它的类的常量池中

四、解析阶段

解析阶段是：将常量池中的符号引用转化为直接引用的过程。解析可能发生在初始化前或者初始化后，分别为静态解析或者动态解析，static发生在静态解析阶段

五、初始化

初始化：这一个阶段才真正的执行类中定义的Java代码

六、对象实例化

对象实例化：带Java代码中需要在堆上分配对象的时候，执行对象实例化的代码

```

---

**Java的内存模型**

```
Java的内存主要分为：Java堆、方法区、Java虚拟机栈、本地方法栈、程序计数器

一、Java虚拟机栈

Java的虚拟机栈是Java执行方法的内存模型；Java的每一个方法在执行的时候都会创建一个栈帧，栈帧当中存放着局部变量表、操作数栈、动态链接和方法返回地址;

Java的虚拟机栈在编译的时候确定内存的大小。如果线程的请求深度大于虚拟机所需要的深度，将会抛出StackOverFlowError；如果虚拟机在动态动态扩展栈时无法申请到足够的内存空间，将会抛出OutofMemoryError

1. 局部变量表中存放的是方法的参数和方法内部的局部变量，包括各种基本的数据类型、对象引用和ReturnAddress类型

2. 操作数栈用于保存中间变量

3. 动态链接指向方法去中的运行时的常量池

二、Java堆

Java堆用于存放Java对象和数组，又叫GC堆，物理上可以不连续，逻辑上必须连续

三、方法区

方法去存放已经被虚拟机加载的类信息、常量、静态变量、JIT编译后的代码等；方法去又称之为永久代，是堆的一个逻辑部分；

运行时的常量池是方法区的一部分

Class文件除了类的版本、字段、方法、接口等描述信息外，还有一项是Class文件常量池，这一个部分在加载之后将放在方法去中的运行时常量池当中，运行时常量池相对于Class文件常量池的另一个特点是动态性，运行期间可以将新的常量放入池中，使用较多的是String类的intern()方法。
```

---

**Java的垃圾回收**

```
强引用：Object o = new Object()，只要强引用还存在，gc就永远不会回收它

软引用：可能还有用，但是并非必须的对象；声明周期为第二次gc回收

弱引用：弱引用是比软引用更弱的一种引用；引用关联的对象只能存活到紧接着的一次gc回收

虚引用：虚引用是最弱的一种引用关系，目的是在这个对象被收集器回收的时候得到一个系统通知

垃圾对象的判定方法：

1. 引用计数器算法: 每一次引用，计数器加1，引用无效，计数器减1，计数为0时即可回收

2. 根搜索算法：定义GC root，从GC root开始向下搜索，当搜索不可达时，证明此对象不可用。gc root：栈帧中局部变量的引用对象、方法区静态属性的引用变量、方法区中常量的引用变量、本地方法栈中Native引用的对象

gc回收算法：

1. 复制算法

2. 标记-清除算法

3. 标记-整理算法

4. 分代收集算法(新生代、老生代)
```

---
