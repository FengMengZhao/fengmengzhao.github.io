---
layout: post
title: 底层编程(汇编语言)
---

---

### Introduction

**What is kernel?**

The kernel is the core part of an operating system keeps track of everything.

The kernel is both and an fence and gate. 

As a gate, it allows programs to access hardware in uniform way. Without the kernel, you would have to write programs to deal with every device model ever made. The kernel handles all device-specific interacitons so you don't have to. It also handles file access and intraction between process. 

As a fence, the kernel prevents programs from accidentally overwriting each other's data and from accessing files and devices that they don't have permission to. It limits the amout of damage a poorly-written program can do to other running programs.

**计算机语言**

*机器语言(Machine Language)*

This is what computer actually sees and deals with. Every command the computer sees is given as a number or sequence of number.

*汇编语言(Assembly Language)*

This is the same as machine language, except the command numbers have been replaced by letter sequences which are easier to memorize. Other small things are done to make it easier as well.

*高级语言(High-Level Language)*

High-Level language are there to make programming easier. Assembly language requiers you to work with the machine itself. High-Level Language allow you to describe the program in a more natural language. A single command in a high-level language usually is equivalent to sevel commands in an assembly language.

---

### 计算机组成(Computer Architecture)

Modern computer architecture is based off of an architecture called the Von Neumann architecture. The Von Neumann architecture divides the computer up into two main parts - the CPU(for Central Processing Unit) and the memory.

**Data Accessing Methods(Addressing Modes)**

*immediate mode*

The data to access is embedded in the instruction itself.

*register addressing mode*

The instruction contains a register to access, rather than a memory location.

*direct addressing mode*

The instruction contains the memory address to access.

*index addressing mode*

The insturction contains a memory address to access, and also specific and index register to offset that address.

*indirect addressing mode*

The instruction contains a pointer to where the data should be accessed.

*base pointer addressing mode*

This is similar to indirect addressing, but you also include a number called offset to add to the register's value before using it to look up.

---

### Your first programs

     .section .data #follow the data

     .section .text #follow the code

     .globl _start #mark the location of the start of the program

    _start: #the program entrance function
     movl $1, %eax #the linux kernel command number(system call) for exiting a program
     movl $0, %ebx #the parameter of the exit command(exit status)
     int $0x80 #wake up the kernel to run the exit command, the int stands for interrupt

> 上面的程序是汇编源码，后缀为`.s`，源码要经过编译器编译为目标文件(object file)，后缀为`.o`，编译的命令为：`as -o exit.o eixt.s`；An objce file is code that is in the machine language, but has not been completely put together；目标文件要经过链接(link)，链接器将目标文件组合在一起，并加入一些信息让kernel知道怎么加载和运行，链接的命令为：`ld -o exit exit.o`。

> Anything start with a period isn't directly translated into machine instruction. Instead, it's an instruction to the assembler itself. These are called assembler directives or pseudo-operations because they are handled by the assembler and are not actually run by the computer.

**Addressing Model**

The general form of memory address references is like:

```
ADDRESS_OR_OFFSET(%BASE_OR_OFFSET, %INDEX, MULTIPLIER)

FINAL ADDRESS = ADDRESS_OR_OFFSET + %BASE_OR_OFFSET + MULTIPLIER * %INDEX
```

> ADDRESS_OR_OFFSET and MULTIPLIER must be contants, while %BASE_OR_OFFSET and %INDEX must be register

*direct addressing mode*

This is done by only using the ADDRESS_OR_OFFSET portion. e.g., `movl ADDRESS, %eax`

*index addressing mode*

This is done by using The ADDRESS_OR_OFFSET and the %INDEX protion.

*indirect addressing mode*

Indirect addressing mode loads value from address indicated by a register. e.g., `movl (%eax), %ebx`

*base pointer addressing mode*

This is similar to indirect addressing mode, except that adds a constant value to the address in the register. e.g., `movl 4($eax), %ebx`

*immediate mode*

Immediate mode is used to load direct values into registers or memory location. e.g., `movl $12, %eax`

*register addressing mode*

Register addressing mode simply moves data in or out of a register.

---

### All about functions

Programming can either be viewed as breaking a large program down into smaller pieces until you get to the primitive functions, or incrementally building functions on top of primitives until you get the large pitcure in focus.

> Functions are perhaps the most fundamental language feature for abstraction and code reuse.

**Inorder to understand function calls, you need to understand the stack!**

when a program starts executing, a certain contiguous section of memory is set aside for the program called stack.

**When calling function, how stack works?**

Before executing a function, a program pushes all of parameters for the function onto the stack in the reverse order that the are documented. Then the program issue a `call` instruction indicating which function it wishes to start. The call instruction does two things(the caller's action before executing the callee function):

First, it pushes the address of the next indtruction, which is the return address, onto the stack;

Then, it modifies the instruction pointer(%eip) to point the start of the function. So, at the time the function starts;

```
Parameter #N
...
Parameter 2
Parameter 1
Return Address <-- (%esp)
```

Now the function itself has some work to do(the callee action while executing):

First, save the current base pointer register %ebp, by doing `pushl %ebp`;

Next, it copy the stack pointer to %ebp by doing `movl %esp, %ebp`, this allows you to be able to access the function parameter(and local variables too) as fixed indexes from the base pointer.

```
Parameter N*4+4(%ebp)
...
Parameter 12(%ebp)
Parameter 8(%ebp)
Return Address <-- 4(%ebp)
old %ebp <-- (%ebp) and (%esp)
```

Next, the function reserves space on the stack for any local variable it needs by simply moving the stack pointer out of the way. e.g., we are going to need two words(remember, a word is four byte long) of memory to run a function:`subl $8, %esp`


```
Parameter N*4+4(%ebp)
...
Parameter 12(%ebp)
Parameter 8(%ebp)
Return Address <-- 4(%ebp)
old %ebp <-- (%esp) and (%ebp)
Local Variable 1 <-- -4(%ebp)
Local Variable 2 <-- -8(%ebp) and (%esp)
```

When the function is done executing, it does three things(the caller's action after the callee is done):

1. It stores its return value in %eax
2. It resets the stack to what it was when it was called(it gets rid of the current stack frame and puts the stack frame of the calling code back into effect)
3. It returns control back to wherever it was called from. This is done using the `ret` instruction, which pops whatever value is at the top of the stack, and sets the instruction pointer, %eip, to the value.

```
movl %ebp, %esp
popl %ebp
ret
```
After the calling, the result is:

```
Parameter N*4+4(%ebp)
...
Parameter 12(%ebp)
Parameter 8(%ebp) <-- (%esp)
```

**一个例子(2^0 + 3^3)**

     .section .data

     .section .text

     .global _start
    _start:
     pushl $0 #第二个参数入栈
     pushl $2 #第一个参数入栈 (见栈图1)
     call power #调用函数power(见栈图2)
     addl $8, %esp #栈指针回到栈顶(清空参数parameters)(见栈图3)

     pushl %eax #第二次调用函数前，将前一个结果保存至栈顶(见栈图4)


     pushl $3 #第二个参数入栈
     pushl $3 #第一个参数入栈(见栈图5)
     call power #调用函数power(见栈图6)
     addl $8, %esp #清空栈顶参数parameters(见栈图7)
     popl %ebx #将上次结果保存至%ebx中(见栈图8)

     addl %eax, %ebx #第二次计算结果加入到第一次计算结果中

     movl $1, %eax #system call exit
     int $0x80 #interrupt control return to kernel

     .type power, @function #定义函数
    power:
     pushl %ebp #保存_start函数%ebp
     movl %esp, %ebp #将栈顶指针保存在%ebp
     subl $4, %esp #为Local Variable预留空间

     movl 8(%ebp), %ebx #将参数一移入register中
     movl 12(%ebp), %ecx #将参数二移入register中

     movl $1, -4(%ebp) #保存1到Local Variable
     cmpl $0, %ecx  #参数二是否为0
     je end_power #如果是，退出函数

     movl %ebx, -4(%ebp) #将结果保存到Local Variable

    power_loop_start:
     cmpl $1, %ecx #循环条件判断
     je end_power #结束循环
     movl -4(%ebp), %eax #上一次结果保存到%eax中
     imull %ebx, %eax #进行一次乘法

     movl %eax, -4(%ebp) #结果保存到Local Variable

     decl %ecx #减小参数值
     jmp power_loop_start #进行新的循环

    end_power:
     movl -4(%ebp), %eax #将返回结果保存在%eax中
     movl %ebp, %esp #清空Local Variable
     popl %ebp #保存_start函数的%ebp
     ret #交还函数的控制权，pop %eip

栈图说明:

```
栈图1:

...
... <-- (%ebp)(_start函数)
...
#parameter2 $0
#parameter1 $2 <--(%esp)

栈图2:

...
... <-- (%ebp)(_start函数)
...
#parameter2 $0
#parameter1 $2 <--(%esp)

栈图3:

...
... <-- (%ebp)(_start函数)
... <-- (%esp)

栈图4:

...
... <-- (%ebp)(_start函数)
... 
%eax <--(%esp)

栈图5:

...
... <-- (%ebp)(_start函数)
... 
%eax
#parameter2 $3
#parameter1 $3 <--(%esp)

栈图6:

...
... <-- (%ebp)(_start函数)
... 
%eax
#parameter2 $3
#parameter1 $3 <--(%esp)

栈图7:

...
... <-- (%ebp)(_start函数)
... 
%eax <-- (%esp)

栈图8:

...
... <-- (%ebp)(_start函数)
... <-- (%esp)
```

**阶乘递归函数(factorial)**

     .section .data

     .section .text

     .global _start

    _start:
     pushl $5

     call factorial
     addl $4, %esp

     movl %eax, %ebx

     movl $1, %eax
     int $0x80

    factorial:
     pushl %ebp
     movl %esp, %ebp
     movl 8(%ebp), %eax
     cmpl $1, %eax
     je end_factorial

     decl %eax
     pushl %eax
     call factorial
     movl 8(%ebp), %ebx
     imull %ebx, %eax

    end_factorial:
     movl %ebp, %esp
     popl %ebp
     ret

**递归的Iteration模式**

     .section .data

     .section .text

     .global _start
    _start:
     movl $3, %ebx
     movl %ebx, %eax

    start_loop:
     cmpl $1, %ebx
     je loop_end

     decl %ebx
     imull %ebx, %eax
     jmp start_loop

    loop_end:
     movl %eax, %ebx
     movl $1, %eax
     int $0x80

---

### Dealing with files

**The Unix File concept**

Unix files, no matter what program created them, can all be accessed as a sequential stream of bytes. When you access a file, you start opening it by name. The operating system then gives you a number, called *file descriptor*, which you use to refer to the file until you are through with it. You can the read and write to the file using its file descriptor. When you are done reading and writing, you then close the file, which then makes the file descriptor useless.

In our programs we will deal with files in the following ways:

1. Tell the name of the file to open, and in what mode you want it opend(read, write, both read and write, create it if it doesn't exist, etc.). This is handled with the `open` system call, which takes a file name, a number representing the mode, and a permission set as its parameters. %eax will hold the system call number, which is 5. Tha address of the first character of file name should be stored in %ebx. The read/write intentions, represented as a number, should be stored in %ecx. Finally, the permission set should be stored as a number in %edx.
2. Linux will return to you a file descriptor in %eax, Remember, this is a number that you use to refer to this file throughout you program.
3. Next you will operate on the file doing reads and/or writes, each time giving linux the file descriptor you want to use. `read` is system call 3, and to call it you need to have the file descriptor in %ebx, the address of a buffer for storing data that is read in %ecx, and the size of buffer in %edx. Read will return the number of characters read from the file, or error code. `write` is system call 4, and it requires the same parameters as the read system call, except that the buffer should already be filed with the data to write out. The write system call will give back the number of bytes written in %eax or an error code.
4. When you are through with your files, you can the tell Linux to close them. Afterwards, you file descriptor is no longer valid. This is done by using `close`, system call 6. The only parameter to close is the file descriptor, which is placed in %ebx.

**Buffers and .bss**

A buffer is a continuous block of bytes used for bulk data transfer. Usually buffers are only used to store data temporarily, and it is then read from buffers and converted to a form that is easier for programs to handle.

Buffers are a fixed size, set by programmer.

`.section .bss` is another section just as `.section .data`, however, it doesn't take up space in the executable. This section can reverse storage, but it can't initialize it. It is useful for buffers:

```
.section .bss
 .lcomm my_buffer, 500
```

> This directive will create a symbol, my_buffer, that refers to a 500-byte storage location that we can use as a buffer.

**Standard and Special Files**

STDIN

> This is standard input. It is a read-only file, and usually represents your keyboard. This is always file descriptor 0.

STDOUT

> This is standard out. It is a write-only file, and usually represents you screen display. This is always file descriptor 1.

STDERR

> This is standard error. It is a write-only file, and usually represents you screen display. This is always file descriptor 2.

`.equ` directive allows you to assign names to numbers. e.g., if you did `.equ LINUX_SYSCALL, 0x80`, any time after that you wrote LINUX_SYSCALL, the assembler would substitue 0x80 for that. You can write:

In assembly language, we declare contants with the `.equ` directive.

`int LINUX_SYSCALL`

---

### Developing Robust Programs
