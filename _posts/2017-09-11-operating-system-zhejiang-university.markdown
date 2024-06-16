---
layout: post
title: 操作系统_浙江大学
---

### 操作系统的结构

OS definition:A program that asts as an intermediary between a user of a computer an the computer hadware

操作系统的目标：

1) 使用户方便的使用计算机

2) 使计算机硬件高效率运行

硬件系统的组成：一个或者数个CPUS，加上一些控制设备，通过内部总线连接在一起，它们共享内存。这些CPUS和设备并行执行，并且竞争使用内存的访问周期。

计算机系统的四个层次：硬件(最底层)(cpu、内存、I/O) --> 操作系统 --> 应用程序 --> 用户(人、机器设备、网络上的其他计算机) 

> 操作系统将硬件包裹起来，用户不可能去操作硬件，如果要访问硬件，必须使用系统调用(system call)

#### 现代操作系统的特征

多程序(Multiprogramming)，表示两个或者两个以上的程序能够驻留到内存空间中

多任务(Multitasking)，分时系统(Timesharing)，表示及时响应

CPU提供Dual-mode机制，实现OS的自我保护。CPU的 Mode bit或者类似的手段，可以在内核态和用户态之间进行切换

#### 操作系统的结构

1) 简单结构。例如MS-OS的层次结构:

![MS-OS Structure](/source_htmls/images/MS-OS_Structure.jpg)

2) 层次化结构。例如Unix System Structure:

![Unix System Structure](/source_htmls/images/Unix_system_structure.jpg)

3) 微内核结构

4) 模块(Modules)

[更多关于操作系统结构信息](http://www.cs.odu.edu/~cs471w/spring11/lectures/OSStructures.htm)

---

---

### 进程概念、操作与进程通信

所谓的进程：

1) different data with same program

2) different program with same data

> 进程是一个动态的概念，是有生命周期的

进程有三个维度：程序、数据、状态

Process：A program in execution

#### 进程控制块(process control block)

> PCB是一个非常复杂的数据结构

进程控制块是一个数据结构，通常与下列的信息相关联：

- process data
- program counter
- CPU register
- CPU scheduling information
- Memory-management information
- Accounting information
- I/O status information

> 进程控制块所保存的信息是对应同一个进程的，不同的进程共享的信息不放在进程控制块中

#### 进程调度队列(Process scheduling queues)

程序的调度实际上是管理了许多程序调度队列

程序调度队列1

![程序调度队列](/source_htmls/images/process_scheduling_queue1.jpg)

程序调度队列2

![程序调度队列](/source_htmls/images/process_scheduling_queue2.jpg)

#### 进程上下文切换(Contex Switch)

当CPU转向为另一个进程服务时，由于CPU内部的资源有限，它必须保存原有(转换前)进程的状态，进入待服务(转换后)进程状态，也即"进程上下文切换"

"状态"指寄存器、标志位、堆栈等当前值

上下文切换的时间是一种额外的开销(overhead)，因为期间CPU不做对用户进程直接有益的事

上下文切换时间决定于CPU硬件的支持力度

> CPU任何时候只能为一个进程服务

#### 进程创建

父进程创建若干子进程；后者再创建子进程；以此类推，构成了反应传承关系的一颗进程树

子进程的资源根据操作系统设计的不同会有不同的方式

- 子进程共享父进程的所有资源
- 子进程共享父进程的部分资源
- 子进程不从父进程共享资源，重新独立申请

执行代码的执行顺序也分为不同的情况：

- 父进程和子进程并发执行
- 父进程在子进程执行期间等待，待子进程执行完毕后才恢复执行余下代码

地址空间中的image

- 子进程复制了(duplicate NOT copy)父进程的image
- 子进程全新装入一个程序，得到了不同于父进程的image

Unix环境下fork一个进程

	int main()
	{
	Pid_t  pid;//在子进程并没有执行
		/- fork another process */
		pid = fork();//系统调用，创建子进程；在Linux中子进程完全继承类父进程的一切，返回用户态之后，子进程和父进程是完全相同的两个进程(除了ID号)；父进程返回PID号，子进程返回0
		if (pid < 0) { /- error occurred */
			fprintf(stderr, "Fork Failed");
			exit(-1);
		}
		else if (pid == 0) { /- child process */
			execlp("/bin/ls", "ls", NULL);
		}
		else { /- parent process */
			/- parent will wait for the child to complete */
			wait (NULL);//等待子进程完成之后再进行
			printf ("Child Complete");
			exit(0);
		}
	}

进程终止表示语义一：子进程执行完最后一条指令后，要求操作系统将自己杀出(exit)，语义动作包括：

- 子进程传递数据给父进程(通过父进程的wait操作)
- 子进程的资源被操作系统收回

进程终止表示语义二：父进程终止子进程的执行(abort)

#### 进程合作

独立：进程不会影响其他进程的执行，也不被影响

合作：进程影响其他进程，或者受其影响

进程合作好处：共享信息、加速(计算)执行任务、模块化、方便调用...

#### 进程间通信

进程通信(Interprocess Communication, IPC)，消息系统是进程之间通信比较常用的一种方式。

同步通信

- 发送操作Send，发送进程等待，直到接受进程确认收到消息
- 接受操作receive，接收进程表等待，直至有个消息到达

异步通信

- 发送操作send，发送进程发出消息后即返回，该干什么干什么，不理会消息是否会到达
- 接受操作receive，接收进程执行一次接收动作后，要么收到一条有效消息，要么收到空消息

---

---

### 线程

进程：程序的一次执行，有独立的内存空间

线程：CPU调度的最小单元，和其他线程共享内存空间

---

---

### CPU调度

CPU调度器(scheduler)的使命：

- 从内存中一堆准备就绪的进程中(就绪队列总的就绪进程)，选取一个进程
- 将CPU分配给该进程

CPU调度的操作时机：

- 某一个进程从执行状态转为等待状态(非抢占式)
- 某一个进程从执行状态转为就绪状态(抢占式)
- 某一个进程从等待状态转为就绪状态(抢占式)
- 某一个进程的终止(非抢占式)

...

> 所谓的非抢占式(nonpreemptive)是进程自愿交出CPU资源，这种调度称之为非抢占式调度；所谓的抢占式(preemptive)是非自愿的交出CPU资源，这种调度称之为抢占式调度

CPU调度器决定了将CPU分配给谁。后续的操作就是，CPU分配器将CPU控制权交给该进程，操作内容：

- 上下文切换(switching contex)
- 从内核态(kernel mode)转到用户态(user mode)
- 跳转至用户程序中PC寄存器所指示的位置

分配延迟(Dispatch latency)：CPU分配器暂停前一个进程，启动后一个进程所经历的时间

#### CPU调度算法

FCFS调度算法，先来先服务算法。这种算法的启示：短进程先于长进程，会得到意想不到的效果

SJF，Shortest-Job-First调度算法。SJF算法分为抢占式和非抢占式，非抢占式表示一旦CPU分配给某个进程，其他进程就不能够抢过来，而抢占式是可以抢过来CPU的，又叫做Shortest-Remaining-Time-First(SRTF)

> SJF算法的前提是：CPU要获得进程的预运行时间，而程序的预运行时间是无法预先准确的判断出来的，这是SJF算法的致命的缺陷。

HRN算法，Highest response Ration Next。HRN=(W + T)/T，W代表是等待的时间，T代表预估CPU的时间

优先权法(Priority Scheduling)：

每一个进程都有一个优先数(priority number)，通常是一个整形数，选取就绪队列中，优先权最高的进程具有最高的优先权。优先权法也同样分为抢占式和非抢占式的

轮转法(Round Robin, RR):

每一个就绪进程获得一小段CPU时间(时间片，time quantum)，通常10ms-100ms。时间片用完毕，这个进程别破交出CPU，重新回到就绪队列，重新参与竞争。

#### 多层队列(Multilevel Queue)

多层队列是把就绪队列分成几个队列，例如：

- 要求交互的进程，放在前台队列，使用RR轮转调度算法
- 可以批量处理的进程，，放在后台队列，使用FCFS调度算法

---

---

### 进程同步

#### 进程同步之临界区问题

常识：

- 对共享数据(Shared Data)的并发访问(Concurrent Access)，可能导致数据不一致问题
- 确保数据的一致性(Data Consistency)，是一个合理的要求。它需要一种机制，以保证合作进程有序进行

> - 原子操作(Atomic operation)要求该操作完整地一次性完成，不允许中间被打断。
- 汇编指令执行一次任务是不会被中断的(i386cpu)

Race Condition(竞争)：The situation where serveral processes acess and manipulate shared data concurrenctly. The final value of the shared data depends upon which process finishes last.

解决竞争问题的方法，并发进程必须使用同步(synchronize)

临界区问题(The critical-section prblem)，是n个进程中至少一个以上的进程修改了共享数据，才构成临界区问题。临界区指的是代码。我们想要的是：任何时候只有一个进程在其临界区执行。

临界区问题解决方案必须满足3个条件：

- 互斥(Mutual Exclusion)，如果一个进程正在其临界区执行，那么其他任何进程不允许在他们的临界区执行
- 空闲让进(Process)，如果没有进程处于它的临界区，并且某些进程申请进入临界区，那么，只有个那些不在Remainder section的进程，才能参与是否进入临界区的选举，并且这个选举不允许被无限期的推迟。
- 有限等待(Bounded Waiting)，某一个进程从其提出申请，至它获准进入他们的临界区这段时间里，其他进程进入他们临界区的次数存在上界。

#### 临界区软件实现算法

算法1(二进程)

i进程

	do {
		while (turn != i) ;
		critical section
		turn = j;
		remainder section
	} while (1);

j进程

	do {
		while (turn != j) ;
		critical section
		turn = i;
		remainder section
	} while (1);

> 算法1满足Mutual Exclusion，但是不满足Process，所以算法不能用来处理临界区问题

算法2(二进程)

i进程

	do {
		flag[i] = true;		
		while (flag[j]) ;					
		critical section
		flag [i] = false;
		remainder section
	} while (1);

j进程

	do {
		flag[j] = true;		
		while (flag[i]) ;					
		critical section //临界区
		flag [j] = false;
		remainder section
	} while (1);

> 算法2同样只满足Mutual Exclusion，但是不满足Process，故不能用来处理临界区问题

Peterson算法(二进程)

i进程

	while (true) {
		flag[i] = TRUE;
		turn = j;
		while ( flag[j] && turn == j);
		CRITICAL SECTION
		flag[i] = FALSE;
		REMAINDER SECTION
	}

j进程

	while (true) {
		flag[j] = TRUE;
		turn = i;
		while ( flag[i] && turn == i);
		CRITICAL SECTION
		flag[j] = FALSE;
		REMAINDER SECTION
	}

> Peterson可以用来处理二进程的临界区问题

面包房算法(Bakery，处理N个进程临界区问题)

	do{
		choosing[i] = true ;
		number[i] = max(number[0],number[i],...,number[n-1]) + 1 ;
		choosing[i] = false ;
		for(j=0; j<n; j++){
			while(choosing[j]) ;
			while((number[j] != 0) && ((number[j], j) < (number[i], i))) ;
		}
		critical section
		number[i] = 0 ;
		remainder section
	}while(1) ;

#### 临界区问题的硬件指令解决方案

提供硬件指令来解决临界区问题

#### 信号量(Semaphore)

信号量可以解决无限多进程的临界区问题

	Semaphore S;    //  初始值为 1
	do{
	     wait (S);
		    Critical Section;
	     signal (S);
		    remainder section;
	} while(1);

---

---

### 虚拟存储思想

- 逻辑空间可以独立于物理空间
- 进程只需要一小部分代码(请求CPU执行的部分代码)驻留内存
- 进程的逻辑空间可以远远大于(分配给它的)物理空间
- 于是，物理空间被更多的进程共享
