---
title: Python Essential Reference
layout: post
---

---

<center style="color: green">待续...</center>

---

## 目录

- [1. Python语言特性](#1)
- [2. Python基础(fundamentals)](#2)
- [3. Python类型(Type)和对象(Object)](#3)

---

<h3 id = '1'>1. pthon语言特性</h3>

Python是动态类型语言(dynamically typed language),在程序的执行过程中,变量的名字可以与不同类型的值进行绑定.语言的特性和c语言有很大的不同,c语言中一个变量名是代表固定的类型,大小,内存地址.

> Python是动态类型(Dynamically typed)和强类型(Strong typed)语言.

    #!/usr/bin python

    principal = 1000
    rate = 0.05
    numyears = 10
    year = 1
    while year <= numyears:
        principal = principal * (1 + rate)
    #    print year, principal
    #    print "%3d %0.2f" % (year, principal)
    #    print format(year, "2d"), format(principal, "0.2f")
        print "{0:2d} {1:0.2f}".format(year, principal)
        year += 1

> 代码中的变量principal最开始bound是integer(principal = 1000),执行过程中bound float(principal = principal * (1 + rate)).

<h3 id = '2'>2. Python基础(fundamentals)</h3>

#### python语法

Python换行符表示新的statement开始,也可用用`;`将多个statement放在同一行中.

Python用缩进(indentation)解释body关系,没有规定缩进的大小,只要保持一致性即可,但是通常用四个空格的缩进表示一个缩进的级别.

`%d %s %f`是special formatting-character,用来定义特定类型数据的格式,例如整数、字符串和浮点数,special formatting-character可以包含修饰符(modifiers)用来修饰宽度(width)和precision(精度),例如`%3d %0.2f`.默认对其的方式是左对齐,修饰符的width表示向右对其width个字符.

Python没有特殊的switch语句,用来测试value,为了处理多种测试用例,使用`elif`.

#### 文件相关操作

打开文件并读取:

    #!/usr/bin python

    f = open("open_file.py")
    line = f.readline()
    while line: 
        print line, #trailing ',' omits newline character
    #    print(line, end='') #use in Python 3
        line = f.readline()

    for line in open("open_file.py"):
        print line,
    f.close()

写入文件并写入：

    #!/usr/bin python

    principal = 1000
    rate = 0.05
    numberyears = 15
    year = 1

    f = open("out", "w")
    while year <= numberyears:
        principal = principal * (1 + rate)
        print >> f, "%3d %0.3f" % (year, principal)
        year += 1
    f.close()

标准输入与输出读写
    #!/usr/bin python

    import sys
    #print "Enter your name:", 
    #name = sys.stdin.readline()
    name = raw_input("Enter your name:")
    print "Welcome",name,"!"

#### Pyton字符串(String)

    a = "Hello World"
    b = 'Python is groovy'
    c = """Computer says 'NO'"""

> Triple-quoted strings are useful when the contents of a string literal span multiple lines of text.

    x = 3.4
    s = "The value of x is" + str(x)
    s = "The value of x is" + repr(x)
    s = "The value of x is" + format(x, "0.4f")

> str() 和 repr() 都可以产生字符串,但两者的结果略有不同,前者是输出结果的样子,而后者是在计算机中的实际表示.

#### python数组(lists)

Lists  are sequences of arbitrary objects.

把文件中内容读入list

    #!/usr/bin python

    import sys

    if len(sys.argv) != 2:
        print "Please supply a filename"
        raise SystemExit(1)
    f = open(sys.argv[1])
    lines = f.readlines()
    f.close()

    fvalues = [float(line) for line in lines]

    print "The minimum value is ", min(fvalues)
    print "The maximum value is ", max(fvalues)
    print sys.argv[0]

> `fvalues = [float(line) for line in lines]`可以简写成`fvalues = [float(line) for line in open(sys.argv[1])]`,这个表达式通过遍历list中的所有Strings构造了一个新的list,同时对所有的item应用了函数`float()`,这种强有力重构list的方法称为*list comprehension*.

#### Python元组(Tuples)

To create simple data structures, you can pack a collection of values together into a single object using a tuple.

Note: the contents of a tuple cannot be modified after creation. This reflects the fact that a tuple is best viewed as a single object consisting of several parts, not as a collection of distinct objects to which you might insert or remove items. Tuples are immutable.

Tuple和List混合使用,读取csc文件中的数据并转化格式后构建Tuple

    #!/usr/bin python

    filename = "portfolio.csv"
    portfolio = []
    for line in open(filename):
        fields = line.split(",")
        name = fields[0]
        shares = int(fields[1])
        price = float(fields[2])
        stock = (name, shares, price)
        portfolio.append(stock)

    print portfolio[1]

#### Python集合(sets)

A set is used to contain an unordered collection of objects.

与List和Tuple不同,sets是无序的,并且不能用数字进行索引.

Sets支持标准的集合运算：

    a = t | s #并集 Union of t and s
    b = t & s #交集 Intersection of t and s
    c = t - s #补集 Set difference (items in t, but not in s)
    d = t ^ s #异或 Symmetric difference(items in t or s, but not both)

#### Python关联数组Map(directories)

A directory is an associate array or hash table that comntains indexed by keys.

例如：

    stodk = {
        "name"   : "GOOG"
        "shares" : 100
        "price"  : 490.10
    }

通常使用字符串作为directory的key,但是也可以使用其他Python对象,比如数字、tuple,像list和directory对象不能被用作key,因为不是immutable.

空directory可以通过两种方式创建：

    prices = {} # An empty dict
    prices = dicr() # An enmty dict

#### Pyton循环(looping)和迭代(iteration)

`range(i, j, [,stride])`函数创造了一个list对象,代表从i到j-1的value,如果i省略,则从0开始.

#### python函数

You can use the `def` statement to create a function

当有多个值返回的时候,可以用一个返回一个Tuple

可以对函数的参数设置默认值,当设置默认值调用的时候,默认值参数可以不写,也可以任意的顺序参数来调用函数,但是这样调用的时候必须写出来参数的名称.

#### Python数字generator

函数不仅仅能产生一个或者几个值,还可以产生一个generator.

    #!/usr/bin python

    def countdown(n):
        print "Counting down!"
        while n > 0:
            yield n # thd statement create generator
            n -= 1

    c = countdown(9)

    #print c.next()

    for i in countdown(5):
        print i

> Specially, when you write a statement such as for item in s, s could represent `a list of items`, `the lines of a file`, `the result of a generator function`, or `any number of other objects that support iteration`.

#### Python批处理(Coroutines)

Normally, functions operate on a sigle set of input arguements. However, a function can also be written to operate as a task that process a sequence of inputs send to it. This type of function is known as a coroutine and is created by using the yieled statement as an expression `(yield)`.

    #!/usr/bin python

    def print_matches(matchtext):
        print "Looking for", matchtext
        while True:
            line = (yield)
            if matchtext in line:
                print line

    matcher = print_matches("python")
    matcher.next()

    matcher.send("Hello, world!")
    matcher.send("python is cool!")
    matcher.send("cool!")
    matcher.send("I love python!")
    matcher.close()

#### Python 模块(modules)

    impourt div as foo
    from div import divide
    from div import *

#### Python惯例

可以使用Python的命令提示符(prompt)进行计算功能,`_`表示一个特殊变量,用来保存上一次的计算结果.

`\` 是 line-continue character.但是,当遇到truple-quoted string,list,Tuple,directory跨越多行时,不需要line-contine符号.

Python的结构是由缩进来决定的,例如：方法体(bodies of functions),条件语句,循环语句和类的定义,如果这些结构中只包含一个语句,可以将它们放在同一行中.

Python中定义的变量名不能与build-in重复

    str = '4'
    str(4) #报错,str函数已经被上面的定义给破坏了.

---

<h3 id = '3'>3. Python类型(Type)和对象(Object)</h3>

Python中的数据都是以对象的形式而存在的,每一个对象都有一个标识(identity),一个类型(type or called class)和一个值(Value).

如果一个对象的值能够被改变,称这个对象为可改变的(mutable);如果一个对象的值不能被改变,称这个值为不可变的(immutable).

属性就是和对象相关联的值;方法就是在一个对象上可以执行的某种操作.

#### (内置函数)build-in function

`id()` returns the identity of an object as an integer, this integer usually corresponds to the object's location.

`type()` returns the type of an object.

Example of compare two objects:

    def compare(a, b):
        if a is b:
            # a and b is the same object
            statement
        if a == b:
            # a and b have the same value
            statement
        if type(a) is type(b):
            # a and b has the same type
            statement

`isInstance(object ,type)` return whether the object is an object of the type.

Example for type checking:

    if isinstance(s, list):
        s.append(item)

    if isinstance(d, dict):
        d.update(t)

#### 引用计数(Reference Counting)和垃圾收集(Garbage Collection)

所有的对象都是会被引用计数的,不论是重新定义一个新的名字或者是在一个容器中增加一个引用.

通过`del`语句或者是超出对象的作用域时,可以减少引用计数

`sys.getrefcount()`可以得出当前对象的引用计数.

当引用计数变为0时,对象会被回收(garbage-collected);但是有些时候循环引用(circular dependency)会出现在集合的使用中,在程序执行的过程中,cycle-detection algorithm会定期的执行来处理这个问题.

Example for circular dependency:

    a = {}
    b = {}
    a['b'] = b
    b['a'] = a
    del a
    del b

#### 引用和复制

When program makes an assignment a = b, a new reference to b is created.

如果b是像stirngs或者numbers等不可变对象,则上面的赋值实际上是copy了一个b对象, a和b指向的是不同的对象;如果是可变的对象,则直接将引用赋值给a, a和b指向的是相同的对象.

针对集合类的copy有两种形式: shallow copy and deep copy. A shallow copy creates a new object but populate it with reference to the items contained in the origin object; A deep copy creates a new object an recursively copies all the objects it contains.

Example for reference and copy:

    a = [1, 2, 3, 5]
    b = a
    b is a #return True
    b[2] = -100 # a = [1, 2, -100, 5]
    
Example for shallow copy:

    a = [1, 2, [3, 4]];
    b = list(a)
    b is a #return False
    b.append(100) #b = [1, 2, [3, 4], 100]; a = [1, 2, [3, 4]]
    b[2, 0] = -100 # b = [1, 2, [-100, 4], 100]; a = [1, 2, [-100, 4]]

Example for deep copy:

    import copy
    a = [1, 2, [3, 4]]
    b = copy.deepcopy(a)
    b[2, 0] = -100 #b = [1, 2, [-100, 4]]; a = [1, 2, [3, 4]]

#### First-class Object

first-class object表示对对象的使用没有限制,和其他的对象没有区别.

A first-class is an entity that can be dynamically created, destroyed, passed to a function, return as a value, and have all the rights as other variables in the programming language have.

All objects in Python are said to be "first class". This means that all objects that can be named by identifier have equal status. It also means that all objects that can be named can be treated as data.

    #!/user/bin/ python

    #the first class nature can be seen by adding some more unusual items
    items = {'number': 42, 'text': "Hello World"}
    items["func"] = abs #add the abs() function
    import math
    items['mod'] = math #add an module
    items['error'] = ValueError #add an exception type
    items['append'] = nums.append #add a method of another object

Convert a line to a list

    line = "Google,100,490.10"
    field_types = [str, int, float]
    raw_fields = line.split(',')
    fields = [ty(val) for ty, val in zip(field_types, raw_fields)]

#### Build-in Types for Representing Data

Python提供了一个null对象(an object with no value),在程序中写成: `None`.

None经常用在可选参数的默认值,因此函数能够检测出是否调用者真的传递了一个值给参数.

None在Boolean表达式中被认为是False.

##### Numeric Types

Python提供5中数值类型: Booleans, integer, long integer(Pyton3中已经没有区分integer和long integer), floating-point numbers and complex numbers.

Python值提供了双精度浮点数(64-bit),没有提供单精度浮点数(32-bit).

Python的Boolean变量是: True, False. True和False分别mapping为1和0.

##### Sequence Types(strings, lists, tuples)

Sequences represents

strings and tuples are immutable; all sequences support iteration.

The build-in function list(s) converts any iterable type to a list. If s is already a list, this funciton constructs a new list that is a shallow copy of s.

---

<center style="color: green">待续...</center>

---
