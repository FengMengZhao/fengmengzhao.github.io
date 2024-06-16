---
layout: post
title: 数据结构与算法
---

## 目录

- [1 绪论](#1)
- [2 Array-Based](#2)
	- [2.1 向量(Vector)](#2.1)
	- [2.2 堆(Heap)](#2.2)
- [3 sequence-based](#3)
	- [3.1 栈(Stack)](#3.1)
	- [3.2 队列(Queue)](#3.2)
	- [3.3 双向队列(Deque)](#3.3)
	- [3.4 通用二叉树(General Binary Tree)](#3.4)
	- [3.5 Binary Search Tree](#3.5)

---

---

<h2 id="1">1 绪论</h2>

**学习结构的本质**

1. 把数据结构图形化、视觉化、
2. 学习什么时候用什么样的数据结构和算法

> Computer science should be called computing science, for the same reason why surgery is not called knife science.

**HailStoneSequence算法**

	/*
	 - 基本的HailStone序列
	 - 当n=1时，算法停止
	 - 当n为大于1的偶数时，除以2，加入序列
	 - 当n为大于1的奇数时，n*3+1，加入序列
	 - 重复上面两部，知道算法停止(n=1) 
	 */
	import java.util.- ;

	public class HailStoneSquence{
		static List hailStoneSquence(int n){
			List list = new ArrayList() ;
			list.add(n) ;
			while(1 < n){
				n = (n % 2 != 0) ? 3*n+1 : n/2 ;
				list.add(n) ;
			}
			return list ;
		}

		public static void main(String args[]){
			for(int i=1;i<100;i++){
				List list = hailStoneSquence(i) ;
				System.out.println(list) ;
			}
		}
	}

**好算法：**

- 正确
	+ 能够正确处理简单的数据
	+ 能够正确处理大规模的输入
	+ 能够正确处理一般性的输入
	+ 能够正确处理退化的输入
	+ 能够正确处理任意合法的输入
- 健壮
	+ 能够辨别出不合法的输入并做恰当的处理，而不至于非正常退出
- 可读
	+ 结构化+准确命名+注释+...
- 效率
	+ Algorithm + Data Structure = Programs
	+ (Algorithm + Data Structure) X Efficiency = Computation

> To measure is to know, If you can not measure it, you can not improve it.

### 算法记号

> 通常考虑问题的规模足够大的情况下，问题的成本(执行的基本操作次数)如何增长

**级数**

算数级数：与末项平方同阶

T(n) = 1 + 2 + 3 + ... + n = n(n+1)/2 = O(n²)

幂方级数：比幂次高出一阶

T(n) = 1² + 2² + ... + n² = n(n+1)(2n+1)/6 = O(n³)

T(n) = 1³ + 2³ + 3³ + ... + n³ = n²(n+1)²/4 = O(n^4)

几何级数(a>1)：与末项同阶

T(n) = aº + a¹ + a² + ... + a^n = O(a^n)

特殊的：

h(n) = 1 + 1/2 + 1/3 + ... + 1/n = Θ(logn)，调和级数

h(n) = log1 + log2 + log3 + ... + logn = log(n!) = Θ(nlogn)，对数级数

**经典冒泡排序算法**
	
	/*
	 - 经典的冒泡排序算法
	 - 不断进行循环，进行第i次循环时将第i大的元素放在序列的倒数第i位
	 - 只要在循环内有元素的位置交换，用boolean变量记录，还需进行循环
	 - boolean标识是关键，当我们再进行循环的时候，如果发现没有可交换的元素，表明序列已经排列好，就不再进行循环，退出
	 */
	package org.fmz.dsa.first ;

	public class BubbleSort{

		void bubbleSort(int[] A, int n){
			for(boolean flag = false; flag = !flag; n--){
				for(int i=0; i<n-1; i++){
					if(A[i] > A[i+1]){
						int exchange = A[i] ;
						A[i] = A[i+1] ;
						A[i+1] = exchange ;
						flag = false ;
					}
				}
			}	
		}

		void bubbleSort2(int[] A, int n){
			boolean sorted = false ;
			while(sorted = !sorted){
				for(int i=0; i<n-1; i++){
					if(A[i] > A[i+1]){
						int temp = A[i] ;
						A[i] = A[i+1] ;
						A[i+1] = temp ;
						sorted = false ;
					}
				}
				n -- ;
			}
		}

		public static void main(String args[]){
			BubbleSort bs = new BubbleSort() ;
			int[] A = {2, 4, 3, 5, 1, 2, 4, 7, 5, 6} ;
			//bs.bubbleSort(A, A.length) ;
			bs.bubbleSort2(A, A.length) ;

			for(int a : A){
				System.out.print(a + "\t") ;
			}
		}
	}/- output:
	1	2	2	3	4	4	5	5	6	7	[Finished in 0.6s]
	*/

> 冒泡排序算法写法是在是太经典了，用两层for循环或者一层while循环一层for循环即可实现。

### 迭代和递归

> 迭代乃人工，递归方神通(To iterate is human, to recurse, divine)

> 凡治众如治寡，分数是也(The control of a large force is the principle as the control of a few men:it is merely a question of dividing up their numbers)

**Decrese-and-conquer，减而治之**

求解一个大规模的问题，可以将其分为两个子问题：其中一个"平凡"，另一个规模缩减，分别求解子问题，由子问题的解得到原问题的解。

减而治之——经典例子：求解一个数组的和

	/*
	 - 将求和问题分解为一个规模缩减的子问题和一个平凡的问题
	 */
	package org.fmz.dsa.first ;

	public class SumArray{

		int sum(int[] A, int n){
			return (n < 1) ?
				0 : sum(A, n-1) + A[n-1] ;
		}
		
		public static void main(String args[]){
			int[] A = {1, 2, 3, 4, 5} ;

			SumArray sa = new SumArray() ;
			int sum = sa.sum(A, A.length) ;
			System.out.println(sum) ;
		}
	}/- output:
	15
	*/

减而治之——经典例子：数组倒置

	/*
	 - 将问题分解成一个规模小的子问题和一个平凡的问题
	 */
	package org.fmz.dsa.first ;

	public class ReverseArray{
		private void swap(int[] A, int lo, int hi){
			int temp = A[lo] ;
			A[lo] = A[hi] ;
			A[hi] = temp ;
		}

		void reverse(int[] A, int lo, int hi){
			if(lo < hi){
				this.swap(A, lo, hi) ;
				reverse(A, lo+1, hi-1) ;
			}else{
				return ;
			}
		}

		public static void main(String args[]){
			int[] A = {1, 5, 2, 3, 7, 4, 5, 6} ;
			ReverseArray ra = new ReverseArray() ;
			ra.reverse(A, 0, A.length-1) ;

			for(int a : A){
				System.out.print(a + "\t") ;
			}
		}
	}/- output:
	6       5       4       7       3       2       5       1
	*/

**Divide-and-conquer，分而治之**

为求解一个大规模的问题，可以将其划分为若干个(通常两个)子问题，规模大体相当，分别求解子问题，由子问题的解得到原问题的解。

分而治之——经典例子：数组求和

	/*
	 - 将问题分解成两个规模相当的子问题
	 - 递归基是求解子问题的基础
	 - 子问题求解、合并
	 */
	package org.fmz.dsa.first ;

	public class SumArray2{
		
		int sum(int[] A, int lo, int hi){
			if(lo == hi){
				return A[lo] ;
			}else{
				int m = (lo + hi) / 2 ;
				return sum(A, lo, m) + sum(A, m+1, hi) ;
			}
		}

		public static void main(String args[]){
			int[] A = {1, 2, 3, 4, 5, 6} ;
			SumArray2 sa2 = new SumArray2() ;
			int sum = sa2.sum(A, 0, A.length-1) ;
			System.out.println(sum) ;
		}
	}/- output:
	21
	*/

分而治之——经典例子：从数组中找出最大的两个数

实现一：

	/*
	 - 先找出序列当中的最大数并记录max_index
	 - 从index将序列分为前后两部分
	 - 依次在其中找出sub_index
	 */
	package org.fmz.dsa.first ;

	class MyResult{
		private int max_index ;
		private int sub_max_index ;

		public MyResult(int max_index, int sub_max_index){
			this.max_index = max_index ;
			this.sub_max_index = sub_max_index ;
		}

		public int getMaxIndex(){
			return this.max_index ;
		}
		public int getSubMaxIndex(){
			return this.sub_max_index ;
		}
	}

	public class FindTwoMax{

		MyResult max(int[] A, int lo, int hi){
			int max_index = lo ;
			for(int i = lo+1; i<hi; i++){
				if(A[max_index] < A[i]){
					max_index = i ;
				}
			}
			int sub_max_index = lo;
			for(int i = lo+1; i<max_index; i++){
				if(A[sub_max_index] < A[i]){
					sub_max_index = i ;
				}
			}
			for(int i = max_index+1; i<hi; i++){
				if(A[sub_max_index] < A[i]){
					sub_max_index = i ;
				}
			}
			return new MyResult(max_index, sub_max_index) ;
		}

		public static void main(String args[]){
			FindTwoMax ftm = new FindTwoMax() ;
			int[] A = {1, 3, 7, 1, 5, 2, 6, 9} ;
			MyResult mr = ftm.max(A, 0, A.length) ;
			System.out.println("max_index: " + mr.getMaxIndex() + " sub_max_index: " + mr.getSubMaxIndex()) ;
		}
	}

> 当一个方法中要输出两个结果的时候，我们可以对这两个结果进行封装，封装成一个类，这样就可以返回多个值

实现二：

	/*
	 - 一个对象保存max_index和sub_index
	 - 对象遍历整个序列，对于序列中的每一个数，更新max_index和sub_index
	 */
	package org.fmz.dsa.first ;

	public class FindTwoMax2{
		MyResult max(int[] A, int lo, int hi){
			int max_index = lo;
			int sub_max_index = lo+1;
			if(A[lo] < A[lo+1]){
				max_index = lo+1 ;
				sub_max_index = lo ;
			}
			for(int i=lo+2; i<hi; i++){
				if(A[i] > A[sub_max_index]){
					sub_max_index = i ;
					if(A[i] > A[max_index]){
						sub_max_index = max_index ;
						max_index = i ;
					}
				}
			}

			return new MyResult(max_index, sub_max_index) ;
		}

		public static void main(String args[]){
			FindTwoMax2 ftm2 = new FindTwoMax2() ;
			int[] A = {1, 3, 7} ;
			MyResult mr = ftm2.max(A, 0, A.length) ;
			System.out.println("max_index: " + mr.getMaxIndex() + " sub_max_index: " + mr.getSubMaxIndex()) ;
		}
	}

实现三：

	/*
	 - 用分而治之的思想解决寻找序列当中的两个最大的数
	 - 列出基本的递归基情况
	 - 递归调用方法
	 */
	public  class FindTwoMax3{
		MyResult findTwoMax3(int[] A, int lo, int hi){
			int max_index ;
			int  sub_max_index ;

			if(A.length <= 1){
				System.exit(1) ;
			}
			if(lo+2 == hi){
				if(A[lo] > A[lo+1]){
					max_index = lo ;
					sub_max_index = lo+1 ;
				}else{
					max_index = lo+1 ;
					sub_max_index = lo ;
				}
				return new MyResult(max_index, sub_max_index) ;
			}
			if(lo+3 == hi){
				int max = A[lo] ;
				max_index = lo ;
				if(A[lo+1] > max){
					max = A[lo+1] ;
					max_index = lo+1 ;
				}
				if(A[lo+2] > max){
					max = A[lo+2] ;
					max_index = lo+2 ;
				}

				if(lo != max_index){
					int sub = A[lo] ;
					sub_max_index = lo ;
					if(lo+1 == max_index){
						if(A[lo+2] > sub){
							sub_max_index = lo+2 ;
						}
					}else{
						if(A[lo+1] > sub){
							sub_max_index = lo+1 ;
						}
					}
				}else{
					if(A[lo+1] > A[lo+2]){
						sub_max_index = lo+1 ;
					}else{
						sub_max_index = lo+2 ;
					}
				}
				return new MyResult(max_index, sub_max_index) ;
			}

			int mi = (lo+hi)/2 ;
			MyResult mrl = findTwoMax3(A, lo, mi) ;
			MyResult mrr = findTwoMax3(A, mi, hi) ;

			if(A[mrl.getMaxIndex()] > A[mrr.getMaxIndex()]){
				max_index = mrl.getMaxIndex() ;
				sub_max_index = (A[mrl.getSubMaxIndex()] > A[mrr.getMaxIndex()]) ? mrl.getSubMaxIndex() : mrr.getMaxIndex() ;
			}else{
				max_index = mrr.getMaxIndex() ;
				sub_max_index = (A[mrr.getSubMaxIndex()] > A[mrl.getMaxIndex()]) ? mrr.getSubMaxIndex() : mrl.getMaxIndex() ;
			}
			return new MyResult(max_index, sub_max_index) ;
		}

		public static void main(String args[]){
			int[] A = {5, 9, 6, 6, 7, 2, 13} ;
			FindTwoMax3 ftm3 = new FindTwoMax3() ;
			MyResult mr = ftm3.findTwoMax3(A, 0, A.length) ;
			System.out.println("Max index --> " + mr.getMaxIndex() + "\t Sub max index --> " + mr.getSubMaxIndex()) ;
		}
	}
	
> 解决算法的问题的时候，一定要在头脑里形成一个清晰的逻辑图，这样再上手写代码，不然事倍功半。

> 减而治之和分而治之是解决一些算法问题的重要思想，当问题复杂的时候，要考虑到这种思想。

**递归消除：尾递归**

递归算法易于理解和实现，但是在空间(甚至时间)效率低，在讲求效率时，应该将递归改写成等价的迭代形式

尾递归：最后一步是递归调用最简单的递归模式

	/*
	 - 第一个方法是尾递归的形式，时间的复杂度为O(n)+O(n) 
	 - 第二个方法是迭代的形式，时间的复杂度为O(n)+O(1)
	 - 在讲求效率的时候，应该将递归的形式改写为迭代的形式
	 */
	public class Factorial{
		static long factorial(int n){ //递归
			if(n < 0){
				System.exit(1) ;
			}
			if(n == 0){
				return 1 ;
			}else{
				return n*factorial(n-1) ;
			}
		}

		static long factorial2(int n){ //迭代
			if(n < 0){
				System.exit(1) ;
			}
			if(n == 0){
				return 1 ;
			}

			long fac = 1 ;
			while(n > 1){
				fac *= n-- ;
			}
			return fac ;
		}

		public static void main(String args[]){
			long fac = factorial(65) ;
			System.out.println(fac) ;
		}
	}

### 动态规划

> Make it work, Make it right, Make it fast.

**斐波那契数列**

递归算法一：

	/*
	 - 当n=39左右的时候，算法的时间就接近1秒
	 - 算法的时间复杂度是O(2^n)
	 */
	public class Fibonacci{
		private int n ;
		public Fibonacci(int n){
			this.n = n ;
		}
		int fib(int x){
			if(x < 2){
				return 1 ;
			}
			return fib(x-1) + fib(x-2) ;
		}

		public void printSquence(){
			for(int i=0; i<n; i++){
				System.out.print(fib(i) + " ") ;
			}
		}

		public static void main(String args[]){
			Fibonacci f = new Fibonacci(39) ;
			f.printSquence() ;
		}
	}/- output:
	1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584 4181 6765 10946 17711 28657 46368 75025 121393 196418 317811 514229 832040 1346269 2178309 3524578 5702887 9227465 14930352 24157817 39088169 63245986 [Finished in 1.0s]
	*/

动态规划迭代算法二：

	/*
	 - 此算法采用动态规划，自下而上的进行迭代求解
	 - 此算法的时间复杂度是O(n)
	 */
	public class Fibonacci2{
		private int n ;
		public Fibonacci2(int n){
			this.n = n ;
		}
		long fib(int x){
			long f = 0 ;
			long g = 1 ;		
			while(0 < x--){
				g = g+f ;
				f = g-f ;
			}
			return g ;
		}

		public void printSquence(){
			for(int i=0; i<n; i++){
				System.out.print(fib(i) + " ") ;
			}
		}

		public static void main(String args[]){
			Fibonacci2 f = new Fibonacci2(65) ;
			f.printSquence() ;
		}
	}/- output:
	1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584 4181 6765 10946 17711 28657 46368 75025 121393 196418 317811 514229 832040 1346269 2178309 3524578 5702887 9227465 14930352 24157817 39088169 63245986 102334155 165580141 267914296 433494437 701408733 1134903170 1836311903 2971215073 4807526976 7778742049 12586269025 20365011074 32951280099 53316291173 86267571272 139583862445 225851433717 365435296162 591286729879 956722026041 1548008755920 2504730781961 4052739537881 6557470319842 10610209857723 17167680177565 [Finished in 0.6s]
	*/

**《Introduction to algorithm》书中的钢条切割动态规划**

递归算法求解：
	
	/*
	 - 将钢条从左边切割下长度为i的一段，只对右边的n-i的一段继续进行切割(递归求解)，对左边的一段不再进行切割
	 - 由于产生了大量的重复递归计算，计算的时间复杂度为O(2^n) 
	 */
	public class CutRod{

		public static int cutRod(int[] p, int n){
			if(n == 0){
				return 0 ;
			}

			int q = Integer.MIN_VALUE ;

			for(int i=0; i<n; i++){
				q = Math.max(q, p[i]+cutRod(p, n-i-1)) ;
			}
			return q ;
		}

		public static void main(String args[]){
			int[] p = {1, 5, 8, 9, 10, 17, 17, 20, 24, 30, 32, 33, 36, 40, 42, 44, 47, 49, 52, 55, 56, 58, 60, 62, 68, 70, 75, 77, 80} ;
			int q = cutRod(p, p.length) ;
			System.out.println(p.length) ;
			System.out.println(q) ;
		}
	}/- output:
	29
	85
	[Finished in 2.5s]
	*/

动态规划算法求解一：
	
	/*
	 - 采用自上而下的递归算法
	 - 保存递归过程中的子最优解，避免重复计算
	 - 时间复杂度大大减小，达到了多项式时间复杂度 
	 */
	public class MemorizedCutRod{

		public static int memorizedCutRod(int[] p, int n){
			int[] r = new int[n] ;
			for(int i=0; i<n; i++){
				r[i] = Integer.MIN_VALUE ;
			}
			return memorizedCutRodAux(p, n, r) ;
		}

		public static int memorizedCutRodAux(int[] p, int n, int[] r){
			
			if(n == 0){
				return 0 ;
			}

			if(r[n-1] >= 0){
				return r[n-1] ;
			}

			int q = Integer.MIN_VALUE ;

			for(int i=0; i<n; i++){
				q = Math.max(q, p[i]+memorizedCutRodAux(p, n-i-1, r)) ;
			}

			r[n-1] = q ;

			return q ;
		}

		public static void main(String args[]){
			int[] p = {1, 5, 8, 9, 10, 17, 17, 20, 24, 30, 32, 33, 36, 40, 42, 44, 47, 49, 52, 55, 56, 58, 60, 62, 68, 70, 75, 77, 80} ;
			int q = memorizedCutRod(p, p.length) ;
			System.out.println(p.length) ;
			System.out.println(q) ;
		}
	}/- output:
	29
	85
	[Finished in 0.6s]
	*/

动态规划求解算法二：

	/*
	 - 采用自下而上的动态规划算法
	 - 先求解规模小的，利用求得并保存的结果求解规模大的
	 */
	public class BottomUpCutRod{
		public static int bottomUpCutRod(int[] p, int n){
			int[] r = new int[n+1] ;
			r[0] = 0 ;

			for(int j=1; j<=n; j++){
				int q = Integer.MIN_VALUE ;

				for(int i=1; i<=j; i++){
					q = Math.max(q, p[i-1]+r[j-i]) ;
				}
				r[j] = q ;
			}

			return r[n] ;
		}

		public static void main(String args[]){
			int[] p = {1, 5, 8, 9, 10, 17, 17, 20, 24, 30, 32, 33, 36, 40, 42, 44, 47, 49, 52, 55, 56, 58, 60, 62, 68, 70, 75, 77, 80} ;
			int q = bottomUpCutRod(p, p.length) ;
			System.out.println(p.length) ;
			System.out.println(q) ;
		}
	}/- output:
	29
	85
	[Finished in 0.6s]
	*/

扩展算法-将最优解表示出来：

	/*
	 - 用r数组保存每一个长度(1-n)最优结果的值
	 - 用s数组保存每一个长度下最优结果的第一段切割长度
	 - 通过动态规划子结构最优，能找出切割方案(方法：printCutRodSolution()实现) 
	 */
	class CutRodResult{
		private int[] r ;
		private int[] s ;

		public CutRodResult(int[] r, int[] s){
			this.r = r ;
			this.s = s ;
		}

		public int[] getR(){
			return r ;
		}
		public int[] getS(){
			return s ;
		}
	}

	public class ExtendedBottomUpCutRod{

		public static CutRodResult extendedBottomUpCutRod(int[] p, int n){
			int[] r = new int[n+1] ;
			int[] s = new int[n+1] ;
			r[0] = 0 ;

			for(int j=1; j<=n; j++){
				int q = Integer.MIN_VALUE ;
				for(int i=1; i<=j; i++){
					if(q < p[i-1]+r[j-i]){
						q = p[i-1]+r[j-i] ;
						s[j] = i ;
						r[j] = q ;
					}
				}
			}

			return new CutRodResult(r, s) ;
		}

		public static void printCutRodSolution(int[] s, int n){
			while(n > 0){
				System.out.print(s[n]+"\t") ;
				n = n-s[n] ;
			}
		}

		public static void main(String args[]){
			int[] p = {1, 5, 8, 9, 10, 17, 17, 20, 24, 30, 32, 33, 36, 40, 42, 44, 47, 49, 52, 55, 56, 58, 60, 62, 68, 70, 75, 77, 80} ;
			CutRodResult crr = extendedBottomUpCutRod(p, p.length) ;
			int[] r = crr.getR() ;
			int[] s = crr.getS() ;
			for(int ri : r){
				System.out.print(ri + "\t") ;
			}
			System.out.println() ;
			for(int si : s){
				System.out.print(si + "\t") ;
			}
			System.out.println() ;
			printCutRodSolution(s, p.length) ;
		}

	}/- output:
	0	1	5	8	10	13	17	18	22	25	30	32	35	38	40	43	47	49	52	55	60	62	65	68	70	73	77	79	82	85	
	0	1	2	3	2	2	6	1	2	3	10	11	2	3	2	2	6	6	2	3	10	10	2	3	2	2	6	6	2	3	
	3	6	10	10	[Finished in 0.7s]
	*/

**动态规划求解最长公共子序列**
	
	/*
	 - 在两个序列中找出最大的公共子序列
	 - lcs()递归方法只能找出公共子序列的长度，而不能找出相应公共子序列的内容
	 - 动态规划填表法可以找出公共子序列的大小和具体内容
	 - 自下而上的动态规划求出所有子问题的最优解
	 - 通过递归的方法打印出最优解
	 - 递归打印妙不可言
	 */
	public class LongestCommonSequence{

		public static int lcs(String[] A, int a, String[]B, int b){
			if(a == 0 || b == 0){
				return 0 ;
			}

			if(A[a-1].equalsIgnoreCase(B[b-1])){
				return lcs(A, a-1, B, b-1)+1 ;
			}else{
				return lcs(A, a-1, B, b) > lcs(A, a, B, b-1) ? lcs(A, a-1, B, b) : lcs(A, a, B, b-1) ;
			}
		}

		public static int[][] lcsDp(String[] A, String[] B){
			int a = A.length ;
			int b = B.length ;
			int[][] c = new int[a+1][b+1] ;
			for(int i=0,j=0; i<c.length&&j<c[0].length; i++,j++){
				c[i][0] = 0 ;
				c[0][j] = 0 ;
			}
			for(int i=1; i<c.length; i++){
				for(int j=1; j<c[0].length; j++){
					if(A[i-1].equalsIgnoreCase(B[j-1])){
						c[i][j] = c[i-1][j-1] + 1 ;
					}else if(c[i][j-1] >= c[i-1][j]){
							c[i][j] = c[i][j-1] ;
					}else{
						c[i][j] = c[i-1][j] ;
					}
				}
			}

			for(int i=0; i<a+1; i++){
				for(int j=0; j<b+1; j++){
					System.out.print(c[i][j] + "\t") ;
				}
				System.out.println() ;
			}
			return c ;
		}

		public static void print(int[][] arr, String[] A, String[] B, int a, int b){
			if(a==0 || b==0){
				return ;
			}
			if(A[a-1].equalsIgnoreCase(B[b-1])){
				System.out.print(A[a-1] + "\t") ;
				print(arr, A, B, a-1, b-1) ;
			}else if(arr[a-1][b] > arr[a][b-1]){
				print(arr, A, B, a-1, b) ;
			}else{
				print(arr, A, B, a, b-1) ;
			}
		}

		public static void main(String arggs[]){
			String[] A = {"e", "d", "u", "c", "a", "t", "i", "o", "n", "a", "l"} ;
			//String[] A = {"a", "d", "v", "a", "n", "t", "a", "g", "e"} ;
			//String[] A = {"d", "i", "d", "a", "c", "t", "i", "c", "a", "l"} ;
			String[] B = {"a", "d", "v", "a", "n", "t", "a", "g", "e"} ;

			int l = lcs(A, A.length, B, B.length) ;
			System.out.println(l) ;

			int[][] arr = lcsDp(A, B) ;

			print(arr, A, B, A.length, B.length) ;
		}
	}/- output:
	4
	0	0	0	0	0	0	0	0	0	0	
	0	0	0	0	0	0	0	0	0	1	
	0	0	1	1	1	1	1	1	1	1	
	0	0	1	1	1	1	1	1	1	1	
	0	0	1	1	1	1	1	1	1	1	
	0	1	1	1	2	2	2	2	2	2	
	0	1	1	1	2	2	3	3	3	3	
	0	1	1	1	2	2	3	3	3	3	
	0	1	1	1	2	2	3	3	3	3	
	0	1	1	1	2	3	3	3	3	3	
	0	1	1	1	2	3	3	4	4	4	
	0	1	1	1	2	3	3	4	4	4	
	a	n	a	d	[Finished in 0.7s]
	*/

**动态规划求解排列排序问题**

	/*
	 - 运筹学中经典的排列排序问题
	 - n件物品在m个机器上进行生产
	 - n件物品的生产工序相同(工序在机器上的流向相同)
	 - n件物品在同所有机器的生产顺序相同 
	 - t_{ij}表示在第i个machine上加工第j个物品需要的加工时间
	 - c_{ij}表示在第i个machine上加工完成第j个产品在的时刻(假设从时刻0开始生产)
	 - 动态规划方法进行求解
	 - 此处求解的是给定的各个物品顺序下最小的加工周期    
	 - 如果要求出最优的物品加工顺序，需要用分支定界法，是多项式时间不可解的问题
	 - 学术界提出了很启发式的算法(近似算法)
	 */
	public class PermutationSequencing{

		public static int permutationSequencing(int[][] t){
			int m = t.length ;
			int n = t[0].length ;
			int[][] c = new int[m][n] ;
			c[0][0] = t[0][0] ;

			for(int i=1; i<m; i++){
				c[i][0] = c[i-1][0] + t[i][0] ;

				for(int j=1; j<n; j++){
					c[0][j] =c[0][j-1] + t[0][j] ; 

					c[i][j] = Math.max(c[i-1][j], c[i][j-1])+t[i][j] ;
				}
			}
			return c[m-1][n-1] ;
		}

		public static void main(String args[]){

			int[][] t2 = {[2, 4, 4, 2, 1, 3], [5, 4, 4, 5, 7, 6],
							[5, 5, 5, 8, 5, 7], [1, 4, 3, 2, 3, 4]} ;
			int c = permutationSequencing(t2) ;
			System.out.println(c) ;
		}
	}/- output:
	46
	[Finished in 0.7s]
	*/

---

---

<h2 id="2">2 Array-Based</h2>

- 抽象数据类型(Abstract Data Type) = 数据模型 + 定义在该数据类型上的一组操作
- 数据结构(Data Structure) = 基于某种特定的语言，实现ADT的一整套算法

> 抽象的数据类型，比如：vector,list,stack,queue,priorityQueue，是站在logical level上说的；数据结构，比如linked list,double linked list...，是站在implementation level上说的

<h3 id="2.1">2.1 向量(Vector)</h3>

**向量ADT接口**

`size()`：报告向量当前的规模(元素总数)

`get(r)`：获取秩为r的元素

`put(r, e)`：用e替换秩为r元素的数值

`insert(r, e)`：e作为秩为r的元素插入，原后继依次后移

`remove(r)`：删除秩为r的元素，返回该元素的原值

`disorderd()`：判断所有的元素是否已经按照非降序的方式排列，如果是返回0，如果不是返回按降序排列的元素对数(2个作为一对)

`sort()`：调整各个元素的位置，使之按非降序排列

`find(e)`：查找目标元素e，找到返回相应的秩，找不到返回-1

`search(e)`：查找e，返回不大于e且最大的元素的秩

`deduplicate(),uniquify()`：删除重复元素

`traverse()`：遍历向量并统一处理所有的元素

**可扩容的向量**

- 扩容时使用容量加倍策略，这样具有更优的时间复杂度

**无序向量**

- 区间删除：`remove(Rank lo, Rank hi)`
- 单元素删除：`remove(r)`，可以视为`remove(r, r+1)`的一种特殊情况

> 如果基于`remove(r)`的接口，反复调用来实现`remove(lo, hi)`的区间删除，在效率上会明显的下降

> - 有序向量：T为可判等的基本类型，或者已经重载操作符"=="或者"!="
> - 无序向量：T为可比较的基本类型，或者已经重载操作符"<"或者">"

**有序向量：唯一化**

- 唯一化的低效算法思路：从前往后，逐一比对各对相邻元素，若雷同，删除后者；否则转至后一个元素。低效的根源在于：同一元素可作为被删除元素的后继多次移动
- 唯一化的高效算法思路：以重复区间为单位，成批删除雷同元素。用两个指针定位重复元素的个数，定位完成后，成批删除

**有序向量：二分查找**

`search(T e, Rank lo, Rank hi)`，语义定义：在有序向量v[lo, hi)中，确定不大于e的最后一个元素；若e比起始元素小，返回`lo-1`(左侧哨兵)；若e比v[hi-1]大，则返回`hi-1`(末元素=右侧哨兵左邻)。这里相当于在v[lo-1]的位置安置了一个左侧哨兵`-∞`，而在v[hi]的位置安置了一个右侧哨兵`+∞`

二分查找可以通过分而治之的思想实现。如果将分点取在区间的中间((lo+hi)/2的向下取整)，则称之为Binary查找：`binsearch(T e, Rank lo, Rank hi)`；如果将分点取在黄金分割的位置，也就是利用斐波那契序列确定分点，则称之为Fibonacci查找：`fibsearch(T e, Rank lo, Rank hi)`。斐波那契查找比Binary查找具有更加高效的复杂度。

**vector的应用**

作为更加复杂的ADT的基础

**Vector VS Array**

- Difference between a Vector and an Array?
	+ A vector is a dynamic array, whose size can be increased, where as an array size can not be changed
	+ Reserve space can be given for vector, where as for array can not
	+ A vector is a class as an array in not
	+ Vector can store any type of objects, where as an array can store only homogeneous values
- Advantages of Arrays
	+ Arrays support efficient random access for the members
	+ It is easy to sort an array
	+ They are more appropriate for storing fixed number of elements
- Disadvantages of Arrays
	+ Elements can not be deleted
	+ Dynamic creation of Array is not possible
	+ Multiple data types can not be stored
- Advantage of Vector
	+ Size of the vector can be changed
	+ Multiple objects can be stored
	+ Elements can be deleted from a Vector
- Disadvantage of Vector
	+ A vector is an object, memory consumption is more

**冒泡排序算法再改进**
	
	/*
	 - 此冒泡排序算法是在原来冒泡排序算法的进一步改进
	 - 基本思想是：已经排好的序列我们就不再进行元素间的计较了
	 */
	import java.util.- ;

	public class BubbleSortImpro{
		static void bubbleSortImpro(int[] A, int lo, int hi){
			boolean sorted = false ;
			while(sorted = !sorted){
				for(int i=lo; i<hi-1; i++){
					if(A[i] > A[i+1]){
						int temp = A[i] ;
						A[i] = A[i+1] ;
						A[i+1] = temp ;
						sorted = false ;
					}
				}
				hi -- ;
			}
		}

		public static void main(String args[]){
			int[] A = new int[10] ; 
			Random rand = new Random(26) ;
			for(int i=0; i<A.length; i++){
				A[i] = rand.nextInt(10) ;
			}
			//int[] A = {2, 8, 7, 6} ;
			bubbleSortImpro(A, 0, A.length) ;

			for(int a : A){
				System.out.print(a + "\t") ;
			}
		}
	}/- output
	2	3	3	4	4	4	7	7	7	9	[Finished in 0.6s]
	*/

> 冒泡排序算法虽然可以做一些改进，但是最坏情况的时间复杂度仍然是O(n^2)

**归并排序算法**

	/*
	 - 利用分而治之的思想进行递归排序
	 - 算法的时间复杂度是O(n*logn)
	 - 有返回值和无返回值是等价的，有返回值只不过是把计算的结果返回罢了 
	 - 有些时候“哨兵元素”在算法中相当重要，引入这样的元素能够让算法的设计更加简单
	 */
	import java.util.- ;
	public class MergeSort{
		static void mergeSort(int[] A, int lo, int hi){
			if(lo+1 == hi){
				return ;
			}

			if(lo+1 < hi){
				int mid = (lo+hi)/2 ;

				mergeSort(A, lo, mid) ;
				mergeSort(A, mid, hi) ;

				merge(A, lo, mid, hi) ;
			}
		}

		private static void merge(int[] A, int lo, int mid, int hi){
			int[] A_left = new int[mid-lo+1] ;
			int[] A_right = new int[hi-mid+1] ;

			for(int i=0; i<A_left.length-1; i++){
				A_left[i] = A[lo+i] ;
			}
			for(int j=0; j<A_right.length-1; j++){
				A_right[j] = A[mid+j] ;
			}

			A_left[A_left.length-1] = Integer.MAX_VALUE ; //哨兵元素
			A_right[A_right.length-1] = Integer.MAX_VALUE ; // 哨兵元素

			int i = 0 ;
			int j = 0 ;
			for(int k=lo; k<hi; k++){
				if(A_left[i] < A_right[j]){
					A[k] = A_left[i] ;
					i++ ;
				}else{
					A[k] = A_right[j] ;
					j++ ;
				}
			}
		}

		public static void main(String args[]){
			///*
			int[] A = new int[100000] ; 
			Random rand = new Random(26) ;
			for(int i=0; i<A.length; i++){
				A[i] = rand.nextInt(10) ;
			}
			//*/
			//int[] A = {2, 4, 6, 1, 7, 8, 9} ;
			for(int i : A){
				System.out.print(i + "\t") ;
			}
			System.out.println() ;
			//int[] A = {3, 2} ;
			mergeSort(A, 0, A.length) ;
			//merge(A, 0, A.length/2, A.length) ;
			for(int i : A){
				System.out.print(i + "\t") ;
			}
		}
	}/- output:
	4	4	9	3	2	7	7	7	3	4	
	2	3	3	4	4	4	7	7	7	9	[Finished in 0.6s]
	*/

有返回值的MergeSort算法：

	package org.fmz.algorithm ;

	import java.util.- ;

	public class MergeSort{
		private static void merge(Integer[] array ,int start, int middle, int end){
			int n1 = middle - start + 1 ;
			int n2 = end - middle ;
			Integer[] left = new Integer[n1 + 1] ;
			Integer[] right = new Integer[n2 + 1] ;
			for(int i=0;i<n1;i++){
				left[i] = array[start + i] ;
			}
			for(int j=0;j<n2;j++){
				right[j] = array[middle + j + 1] ;
			}
			left[n1] = Integer.MAX_VALUE ;
			right[n2] = Integer.MAX_VALUE ;
			int i = 0 ;
			int j = 0 ;
			for(int k=start;k<=end;k++){
				if(left[i] < right[j]){
					array[k] = left[i] ;
					i++ ;
				}else{
					array[k] = right[j] ;
					j++ ;
				}
			}
		}
		public static Integer[] mergeSort(Integer[] array, int start, int end){
			if(start < end){
				int middle = (start + end) / 2 ;
				mergeSort(array, start, middle) ;
				mergeSort(array, middle+1, end) ;
				merge(array, start, middle, end) ;
			}
			return array ;
		}

		public static void main(String args[]){
			Random random = new Random(25) ;
			Integer[] array = new Integer[1000000] ;
			for(int i=0;i<array.length;i++){
				array[i] = random.nextInt(1000) ;
			}
			long startTime = System.currentTimeMillis() ;
			System.out.println(Arrays.asList(mergeSort(array, 0, array.length-1))) ;
			long endTime = System.currentTimeMillis() ;
			System.out.println(endTime - startTime) ;

		}
	}/**
	   *对于插入排序算法，排序10,000个数的数组需要33毫秒；排序100,000个数的数组需要4098毫秒
	   *对于Arrays.sort()算法，排序10,000个数的数组需要21毫秒；排序100,000个数的数组需要60毫秒；排序1,000,000个数的数组需要504毫秒；排序10,000,000个数的数组需要2331毫秒
	   *对于分治排序算法，排序10,000个数的数组需要26毫秒；排序100,000个数的数组需要46毫秒；排序1,000,000个数的数组需要311毫秒；排序10,000,000个数的数组需要4728毫秒
	   */

**插入排序算法**

	package org.fmz.algorithm ;

	import java.util.- ;

	public class InsertionSort{
		public static Integer[] insertionSort(Integer[] array){
			for(int j=1;j<array.length;j++){
				Integer insertValue = array[j] ;
				int i = j - 1 ;
				while(i >= 0 && array[i] > insertValue){
					array[i+1] = array[i] ;
					i-- ;
				}
				array[i+1] = insertValue ;
			}
			return array ;
		}

		public static Double[] insertionSort(Double[] array){
			for(int j=1;j<array.length;j++){
				Double insertValue = array[j] ;
				int i = j - 1 ;
				while(i >= 0 && array[i] > insertValue){
					array[i+1] = array[i] ;
					i-- ;
				}
				array[i+1] = insertValue ;
			}
			return array ;
		}

		public static void main(String args[]){
			Random random = new Random(26) ;
			Integer[] array = new Integer[10000000] ;
			for(int i=0;i<array.length;i++){
				array[i] = random.nextInt(1000) ;
			}
			long startTime = System.currentTimeMillis() ;
			insertionSort(array) ;
			long endTime = System.currentTimeMillis() ;
			System.out.println(endTime - startTime) ;
		}
	}/**
	   *对于插入排序算法，排序10,000个数的数组需要33毫秒；排序100,000个数的数组需要4098毫秒
	   *对于Arrays.sort()算法，排序10,000个数的数组需要21毫秒；排序100,000个数的数组需要60毫秒；排序1,000,000个数的数组需要504毫秒；排序10,000,000个数的数组需要2331毫秒
	   *对于分治排序算法，排序10,000个数的数组需要26毫秒；排序100,000个数的数组需要46毫秒；排序1,000,000个数的数组需要311毫秒；排序10,000,000个数的数组需要4728毫秒
	   */

**Vector的自定义实现**

Vector ADT 的实现
	
抽象类 Vector.java

	/*
	 - Vector 抽象类
	 - 其子类有两个，一个是FixedVector，另一个是DynamicVector
	 - 抽象类中有两个抽象方法，在子类中有不同的实现，故定义为抽象方法
	 - 抽象方法是：append()、insertAt()
	 */
	package org.fmz.container;

	public abstract class Vector{

		protected Object[] data;
		protected static final int DEFAULT_CAPACITY = 100;
		protected int numItems;



		public void finalize() throws Throwable {
			super.finalize();
		}

		public Vector(){
			data = new Object[DEFAULT_CAPACITY] ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public abstract boolean append(Object element) ;

		public void clear(){
			for(int i=0; i<numItems; i++){
				data[i] = null ;
			}
			numItems = 0 ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public boolean contains(Object element){
			return indexOf(element) != -1 ;
		}

		/**
	 	- 
	 	- @param index
		 */
		public Object elementAt(int index){
			if(index < 0 || index > numItems-1)
				return null;
			return data[index] ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public int indexOf(Object element){
			for(int i=0; i<numItems; i++)
				if(element.equals(data[i]))
					return i ;
			return -1 ;
		}

		/**
	 	- 
	 	- @param index
	 	- @param element
		 */
		public abstract boolean insertAt(int index, Object element) ;

		public boolean isEmpty(){
			return numItems == 0 ;
		}

		public boolean isFull(){
			return numItems == data.length ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public boolean remove(Object element){
			int pos = indexOf(element) ;
			if(pos == -1)
				return false;
			removeAt(pos) ;
			return true ;
		}

		/**
	 	- 
	 	- @param index
		 */
		public Object removeAt(int index){
			if(index < 0 || index > numItems-1)
				return null ;
			Object temp = data[index] ;
			while(index < numItems-1)
				data[index] = data[index+1] ;
				index ++ ;
			data[--numItems] = null ;
			return temp ;
		}

		/**
	 	- 
	 	- @param index
	 	- @param element
		 */
		public boolean replace(int index, Object element){
			if(index < 0 || index > numItems-1)
				return false;
			data[index] = element ;
			return true ;
		}

		public int size(){
			return numItems ;
		}
	}

子类 FixedVector.java

	/*
	 - FixedVector继承Vector抽象类
	 - override抽象类中的两个抽象方法
	 */
	package org.fmz.container;

	public class FixedVector extends Vector {

		public FixedVector(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		/**
		-
	 	- @param element
		 */
		public boolean append(Object element){
			if(isFull())
				return false;
			data[numItems++] = element ;
			return true ;
		}

		/**
	 	- 
	 	- @param index
	 	- @param element
		 */
		public boolean insertAt(int index, Object element){
			if(index<0 || index>numItems-1 || isFull())
				return false;
			for(int i=numItems-1; i>=index; i--)
				data[i+1] = data[i] ;
			data[index] = element ;
			numItems ++ ;
			return true ;
		}
	}

子类 DynamicVector.java

	/*
	 - DynamicVector继承Vector抽象类
	 - override抽象类中的抽象方法
	 - / 
	package org.fmz.container;


	public class DynamicVector extends Vector {

		public DynamicVector(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		/**
	 	- 
	 	- @param initCapacity
		 */
		public DynamicVector(int initCapacity){
			if(initCapacity <= 0)
				data = new Object[DEFAULT_CAPACITY] ;
			else
				data = new Object[initCapacity] ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public boolean append(Object element){
			/*
			if(isFull()){
				Object[] newData = new Object[data.length*2] ;
				for(int i=0; i<numItems; i++){
					newData[i] = data[i] ;
				}
				data = newData ;
			}
			*/
			ensureCapacity(data.length*2) ;
			data[numItems++] = element ;
			return true;
		}

		/**
	 	- 
	 	- @param minCapacity
		 */
		protected void ensureCapacity(int minCapacity){
			if(minCapacity <= data.length)
				return ;
			Object[] newData = new Object[minCapacity] ;
			for(int i=0; i<numItems; i++)
				newData[i] = data[i] ;
			data = newData ;
		}

		/**
	 	- 
	 	- @param index
	 	- @param element
		 */
		public boolean insertAt(int index, Object element){
			if(index < 0 || index > numItems-1)
				return false;
			if(isFull()){
				Object[] newData = new Object[data.length*2] ;
				for(int i=0,j=0; j<numItems; i++,j++){
					if(j == index)
						i ++ ;
					newData[i] = data[j] ;
				}
				data = newData ;
				data[index] = element ;
				numItems ++ ;
				return true ;
			}

			for(int i=numItems-1; i>=index; i--)
				data[i+1] = data[i] ;
			data[index] = element ;
			numItems ++ ;
			return true ;
		}
	}

利用定义的Vector ADT记性排序(Sort)和查找(Search)

查找类 Search.java

	/*
	 - 将查找定义为一个类，类中提供静态方法，传递向量和查找对象进行查找操作
	 - 一共提供两种查找方法：二分查找(binarySearch())和线性查找(linearSearch())
	 - 两种查找方法的参数Vector，都是有序Vector
	 */
	package org.fmz.container ;

	import org.fmz.container.Vector ;
	import org.fmz.container.Comparable ;

	public class Search{

		private Search(){}

		public static int binSearch(Vector vec, Comparable target){
			int first = 0,
				last = vec.size(),
				middle ;
			while(last - first >= 0){
				middle = (first + last) / 2 ;
				if(target.compareTo(vec.elementAt(middle)) < 0)
					last = middle-1 ;
				else if(target.compareTo(vec.elementAt(middle)) > 0)
					first = middle+1 ;
				else
					return middle ;
			}
			return -1 ;
		}

		public static int linearSearchSort(Vector vec, Comparable target){
			int j,
				n = vec.size() ;
			for(j=0; j<n && target.compareTo(vec.elementAt(j))>0; j++) ;
			if(j<n && target.compareTo(vec.elementAt(j)) == 0)
				return j ;
			return -1 ;
		}
	}

> 辅助接口 Comparable.java
	
	/*
	 - 可比较的接口，任何实现了此接口的object，都是可比较的，要override compareTo(Object o)方法，以此来指定比较规则
	 */
	package org.fmz.container ;

	public interface Comparable{
		int compareTo(Object o) ;
	}

排序类 Sort.java

	/*
	 - 将排序定义为一个类，提供静态方法
	 - 两种排序方法: 冒泡排序(bubbleSort(Vector, vec))和选择排序(selectionSort(Vector, vec))
	 */
	package org.fmz.container ;

	import org.fmz.container.Vector ;
	import org.fmz.container.Comparable ;

	public class Sort{

		private Sort() {}

		public static void swap(Vector vec, int first, int second){
			Object temp = vec.elementAt(first) ;
			vec.replace(first, vec.elementAt(second)) ;
			vec.replace(second, temp) ;
		}

		public static void bubbleSort(Vector vec){
			int i,
				j,
				n=vec.size() ;
			Comparable first ;
			Comparable second ;
			for(i=n-1; i>0; i--){
				for(j=1; j<=i; j++){
					first = (Comparable)vec.elementAt(j-1) ;
					second = (Comparable)vec.elementAt(j) ;
					if(!(first.compareTo(second)<0)){
						swap(vec, j-1, j) ;
					}
				}	
			}
		}

		public static void selectionSort(Vector vec){
			int i,
				j,
				n = vec.size(), 
				small_pos ;
			Comparable smallest,
					   current ;
			for(i=0; i<n-1; i++){
				small_pos = i ;
				smallest = (Comparable)vec.elementAt(small_pos) ;
				for(j=i+1; j<n; j++){
					current = (Comparable)vec.elementAt(j) ;
					if(current.compareTo(smallest) > 0){
						small_pos = j ;
						smallest = (Comparable)vec.elementAt(small_pos) ;
					}
				}
				if(small_pos != i)
					swap(vec, i, small_pos) ;
			}
		}
	}

---

<h3 id="2.2">2.2 堆(Heap)</h3>

> A binary tree is collection of elements called nodes. The collection is either empty or it consists of a node called the root, along with two binary trees called the left and right subtrees. The roots of the left and right subtrees are children of the root; the root is the parent of these children. An edge connects each node of a binary tree to each of its children. A node with no children is called leaf. A node having at least one child is called an internal node.

> The depth of node n in binary tree is the number of edges that must be traversed when travelling from the root of the tree to n. The height of the tree is one more than the depth of the depth of the deepest node. All nodes with depth d are said to be at level d of the tree.

> A binary tree is called complete if it was built by filling its levels from left to right, starting at the root.

**堆的定义**

A heap is a partially ordered complete binary tree. That is to say partially ordered is to say that there is some relationship between the value of a node and the value of ots children. In a min-heap, the value of a node is less than or equal to the values of its children. In a max-heap, the value of a node is greater than or equals to the values of its children.

**heap的实现**

Heap.java
	
	/*
	 - heap抽象类定义了两个抽象方法 percolate()、sift()
	 - 抽象方法的覆写不同会导致MinHeap和MaxHeap中的insert和remove方法有不同的实现
	 */
	package org.fmz.container ;

	public abstract class Heap{
		protected Comparable[] data;
		protected static final int DEFAULT_CAPACITY = 100;
		protected int numItems;




		public void finalize() throws Throwable {
			super.finalize();
		}

		public Heap(){
			data = new Comparable[DEFAULT_CAPACITY] ;
		}

		/**
	 	- 
	 	- @param initCapacity
		 */
		public Heap(int initCapacity){
			if(initCapacity <= 0)
				data = new Comparable[DEFAULT_CAPACITY] ;
			else
				data = new Comparable[initCapacity] ;
		}

		public void clear(){
			for(int i=0; i<numItems; i++)
				data[i] = null ;
			numItems = 0 ;
		}

		public void contract(){
			if(numItems == data.length)
				return ;

			Comparable[] new_data = new Comparable[numItems] ;
			for(int i=0; i<new_data.length; i++)
				new_data[i] = data[i] ;
			data = new_data ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public void insert(Comparable element){
			if(isFull()){
				Comparable[] new_data = new Comparable[numItems << 1] ;
				for(int i=0; i<numItems; i++)
					new_data[i] = data[i] ;
				data = new_data ;
			}
			data[numItems++] = element ;
			percolate() ;
		}

		public boolean isFull(){
			return numItems == data.length ;
		}

		public boolean isEmpty(){
			return numItems == 0 ;
		}

		protected boolean isLeaf(int pos){
			return (pos << 1) + 1 >= numItems ;
		}

		/**
	 	- 
	 	- @param pos
		 */
		protected int leftChild(int pos){
			if(pos < 0)
				return -1;
			return (pos << 1) + 1 ;
		}

		/**
	 	- 
	 	- @param pos
		 */
		protected int parent(int pos){
			if(pos <= 0)
				return -1 ;
			return (pos - 1) >> 1 ;
		}

		protected Comparable peek(){
			return data[0] ;
		}

		protected abstract void percolate();

		protected Comparable remove(){
			if(isEmpty())
				return null ;
			Comparable root = data[0] ;
			swap(data, 0, numItems-1) ;
			data[--numItems] = null ;
			if(numItems > 0)
				sift() ;
			return root ;
		}

		/**
	 	- 
	 	- @param pos
		 */
		protected int rightChild(int pos){
			if(pos < 0)
				return -1;
			return (pos << 1) + 2 ;
		}

		protected abstract void sift();

		public int size(){
			return numItems ;
		}

		/**
	 	- 
	 	- @param arr
	 	- @param first
	 	- @param second
		 */
		protected void swap(Comparable[] arr, int first, int second){
			Comparable tmp = arr[first] ;
			arr[first] = arr[second] ;
			arr[second] = tmp ;
		}

	}

MinHeap.java

	/*
	 - 通过对父类中抽象方法的覆写，从而实现insert和remove方法的不同实现
	 */
	package org.fmz.container;

	public class MinHeap extends Heap {



		public void finalize() throws Throwable {
			super.finalize();
		}

		/**
	 	- 
	 	- @param initCapacity
		 */
		public MinHeap(int initCapacity){
			super(initCapacity) ;
		}

		public MinHeap(){

		}

		public Comparable peekMin(){
			return peek();
		}

		protected void percolate(){
			int pos = size() -1 ;

			while(pos != 0 &&
					data[pos].compareTo(data[parent(pos)]) < 0){
				swap(data, pos, parent(pos)) ;
				pos = parent(pos) ;
			}
		}

		public Comparable removeMin(){
			return remove() ;
		}

		protected void sift(){
			int pos = 0,
				i ,
				r_pos ;
			while(!isLeaf(pos)){
				i = leftChild(pos) ;
				r_pos = rightChild(pos) ;

				if(r_pos < size() && data[i].compareTo(data[r_pos]) > 0)
					i = r_pos ;

				if(data[pos].compareTo(data[i]) <= 0)
					return ;

				swap(data, pos, i) ;

				pos = i ;
			}
		}
	}

MaxHeap.java

	package org.fmz.container;

	public class MaxHeap extends Heap {



		public void finalize() throws Throwable {
			super.finalize();
		}

		protected MaxHeap(){

		}

		/**
	 	- 
	 	- @param initCapacity
		 */
		protected MaxHeap(int initCapacity){
			super(initCapacity) ;
		}

		public Comparable peekMax(){
			return peek() ;
		}

		protected void percolate(){
			int pos = size() - 1 ;
			while(pos != 0 &&
					data[pos].compareTo(data[parent(pos)]) > 0){
				swap(data, pos, parent(pos)) ;
				pos = parent(pos) ;
			}
		}

		public Comparable removeMax(){
			return remove() ;
		}

		protected void sift(){
			int pos = 0,
				i,
				r_pos ;
			while(!isLeaf(pos)){
				i = leftChild(pos) ;
				r_pos = rightChild(pos) ;

				if(r_pos < size() &&
						data[i].compareTo(data[r_pos]) < 0)
					i = r_pos ;

				if(data[pos].compareTo(data[i]) >= 0)
					return ;

				swap(data, pos, i) ;

				i = pos ;
			}
		}

	}

**堆排序(heap sort)的实现**

HeapSort.java

	/*
	 - 将可比较的对象插入堆中
	 - 从堆中不断删除root
	 - 即可得到从小到大的排序
	 */
	import java.util.- ;

	import org.fmz.container.MinHeap ;

	public class HeapSort{
		public static void heapSort(int[] array){
			int n = array.length ;
			MinHeap heap = new MinHeap(n) ;

			for(int i=0; i<n; i++)
				heap.insert((Comparable)array[i]) ;
			for(int j=0; j<n; j++)
				array[j] = (int)heap.removeMin() ; 
		}

		public static void main(String args[]){
			int[] array = new int[10] ;
			Random rand = new Random(26) ;
			for(int i=0; i<array.length; i++)
				array[i] = rand.nextInt(10) ;
			heapSort(array) ;
			for(int i : array)
				System.out.print(i + "\t") ;
		}
	}/- output
	2	3	3	4	4	4	7	7	7	9	[Finished in 0.6s]
	*/

**Quick Sort**

	/*
	 - Quick sort 和 Merge sort 都是采用递归的思想解决问题，使用 Divide-and-Conquer策略
	 - 将一个长的序列分解成很多年小的序列，在每个序列上递归调用，当序列足够小的时候，使用插入排序方法排序
	 - 核心点在于找到一个中间数(或者是随机的一个数)，使得partition之后的两个序列，其中一个序列的数都小于等于中间数，而另一个都大于等于中间数
	 */
	import java.util.- ;

	public class QuickSort{
		public static void swap(int[] array, int first, int second){
			int temp = array[first] ;
			array[first] = array[second] ;
			array[second] = temp ;
		}

		static void medianOf3(int[] array, int left, int right){
			int middle = (left + right) >> 1 ;	

			if(array[left] > array[middle])
				swap(array, left, middle) ;

			if(array[middle] > array[right])
				swap(array, middle, right) ;

			if(array[left] > array[middle])
				swap(array, left, middle) ;
		}

		static int partition(int[] array, int left, int right){
			int middle = (left + right) >> 1 ;
			int pivot = array[middle] ;
			while(true){
				while(array[left++] < pivot) ;
				while(array[right--] > pivot) ;
				if(left < right)
					swap(array, left-1, right+1) ;
				else
					return left-1 ;
					
			}
		}

		public static void insertionSort(int[] array, int left, int right){
			int start = left ;
			if(left == right)
				return ;
			while(left < right){
				int insert_value = array[++left] ;
				int walker = left-1 ;
				while(walker >= start && array[walker] > insert_value){
					array[walker+1] = array[walker] ;
					walker -- ;
				}
				array[walker+1] = insert_value ;
			}
		}

		public static void quickSort(int[] array, int left, int right){
			if(right-left + 1 <= 10)
				insertionSort(array, left, right) ;
			else{
				medianOf3(array, left, right) ;
				int left_part = partition(array, left, right) ;
				quickSort(array, left, left_part-1) ;
				quickSort(array, left_part, right) ;
			}
		}

		public static void main(String args[]){
			Random rand = new Random(24) ;
			int[] array = new int[20] ;
			for(int i=0; i<array.length; i++)
				array[i] = rand.nextInt(10) ;

			quickSort(array, 0, array.length-1) ;

			for(int i : array){
				System.out.print(i + "\t") ;
			}
		}
	}/- output
	0	0	1	2	3	0	2	4	4	4	4	4	5	6	7	7	8	8	8	9	[Finished in 0.7s]
	*/

**Shell Sort**

ShellSort.java

	/*
	 - Shell sort是Insertion sort的一个扩展
	 - 使用1 4 13 40 ... 这样的序列作为interval
	 - 从最大的interval开始值排序像个interval的数据
	 - 不断缩小interval
	 - 当interval为1时，即为插入排序，排序的速度会非常快，因为有前面许多interval为基础的序列已经排序好了
	 */
	import java.util.- ;

	public class ShellSort{

		public static void shellSort(int[] array){
			int inner,
				outer,
				target ;
			int h = 1 ;
			while(h <= array.length)
				h = h*3 + 1 ;

			while(h > 0){
				for(outer = h; outer < array.length; outer++){
					target = array[outer] ;
					inner = outer ;
					while(inner > h-1 && array[inner-h] > target){
						array[inner] = array[inner-h] ;
						inner -= h ;
					}
					array[inner] = target ;
				}
				h = (h-1) / 3 ;
			}
		}

		public static void main(String args[]){
			Random rand = new Random(24) ;
			int[] array = new int[20] ;
			for(int i=0; i<array.length; i++)
				array[i] = rand.nextInt(10) ;
			shellSort(array) ;

			for(int i : array){
				System.out.print(i + "\t") ;
			}
		}
	}/- output
	0	0	0	1	2	2	3	4	4	4	4	4	5	6	7	7	8	8	8	9	[Finished in 0.6s]
	*/

---

---

<h2 id="3">3 列表</h2>

> Linked list: a linear collection of nodes in which each node has, at a minimun, a data portion and a link portion; a node's link portion is a pointer or reference to the next node in the list, unless there is no next node, in which case the link portion is empty.

> The nodes of a linked list are not stored in contiguous blocks of memory, hence the links. Only the memory address of the first and last nodes are stored outside the list; the memory address of any internal node is stored in another node, namely the predecessor. This means that a given internal node can be accessed only by navigating to that node by way of the links, starting with head. Thus a linked list is a sequential-access structure, like a audio or video cassette.

<h3 id="3.1">3.1 栈(Stack)</h3>

**栈的典型应用**

- 逆序输出(conversion)，输出次序与处理的过程颠倒：递归深度和输出长度不易预知
- 递归嵌套(stack permutation + parenthesis)：具有自相似性的问题可递归描述，但分支位置和嵌套深度不固定、
- 延迟缓冲(evaliation)：线性扫描算法模式中，在预读足够长之后，方能确定可处理的前缀
- 栈式计算(RPN)：基于栈结构的特定计算模式

**括号匹配**

BrancketsMatch.java

	/*
	 - 从开始扫描可能需要匹配的符号，这是包括：() [] {} <>
	 - 将出现左边的符号 '( [ { <' 压入栈中
	 - 扫描的右边的符号且栈顶符号与之相匹配时，将栈顶弹出
	 - 如果右边的符号和栈顶的符号不符合，则直接返回false，匹配失败
	 */
	//package test ;

	import java.util.- ;

	public class BracketsMatch{

		public static boolean bracketsMatch(String str){
			char[] char_ary = str.toCharArray() ;
			Stack<Character> sta = new Stack<Character>() ; 

			for(int i=0; i<char_ary.length; i++){
				char c = char_ary[i] ;
				try{
					switch(c){
						case '(': sta.push('(') ;
							break ;
						case '[': sta.push('[') ;
							break ;
						case '{': sta.push('{') ;
							break ;
						case '<': sta.push('<') ;
							break ;
						case ')':
							if(sta.peek().equals('(')){
								sta.pop() ;
								break ;
							}else
								return false ;
						case ']':
							if(sta.peek().equals('[')){
								sta.pop() ;
								break ;
							}else
								return false ;
						case '}':
							if(sta.peek().equals('{')){
								sta.pop() ;
								break ;
							}else
								return false ;
						case '>':
							if(sta.peek().equals('<')){
								sta.pop() ;
								break ;
							}else
								return false ;
					}
	 			}catch(EmptyStackException ese){
	 				return false ;
				}
			}
			return sta.empty() ;
		}
		public static void main(String args[]){
			String str = "{()[][()][]}" ;
			//String str = "[(])" ;
			boolean result = bracketsMatch(str) ;
			System.out.println(result) ;
		}
	}

**栈混洗(stack permutation)**

将栈A的元素，通过一个中转的栈S，全部转移到栈B中的过程。将A中的元素不断push到S中，S可以选择在何时pop，从而将中转元素push到B中，这样的一个过程称之为栈混洗。

> 栈混洗的方式可能有多种，但是最多也不会超过全排列的总数。
> 判断任意给出一个全排列，是否是输入序列的栈混洗是一个问题。

> 栈混洗和括号匹配之间存在关系——n个元素的栈混洗，等价于n对括号的匹配。

**中缀表达式(infix expression)计算**

InfixExpressionEvaluate.java

	/*
	 - 中缀表达式的计算用于计算有优先级的符号计算
	 - 利用栈的数据结构来保存两个栈
	 - 运算符栈保存运算符(+-*/^!...)，运算符栈的起始加入一个哨兵元素和表达式中人为加入的哨兵元素想对应
	 - 操作数栈用于保存数据
	 - 遍历的将表达式字符入栈，如果运算符有更高的优先级，继续入栈，直到有低的优先级出现，则可以计算栈中的最高优先级，也即运算符栈顶元素
	 - 操作数栈的元素也随着运算符栈的元素变化而变化，变化发生在有可以计算的最高优先级，否则继续入栈
	 - 最后操作数栈只剩下一个元素，也即中缀表达式的值
	 */
	import java.util.- ;

	public class InfixExpressionEvaluate{

		public static int compare(char x, char y){
			switch(x){
				case '+':
					switch(y){
						case '+': return -1 ;
						case '-': return -1 ;
						case '*': return -1 ;
						case '/': return -1 ;
						case '^': return -1 ;
						case '!': return -1 ;
						case '(': return 1 ;
						case ')': return 2 ;
						case '$': return 1 ;
					}
				case '-':
					switch(y){
						case '+': return -1 ;
						case '-': return -1 ;
						case '*': return -1 ;
						case '/': return -1 ;
						case '^': return -1 ;
						case '!': return -1 ;
						case '(': return 1 ;
						case ')': return 2 ;
						case '$': return 1 ;
					}
				case '*':
					switch(y){
						case '+': return 1 ;
						case '-': return 1 ;
						case '*': return -1 ;
						case '/': return -1 ;
						case '^': return -1 ;
						case '!': return -1 ;
						case '(': return 1 ;
						case ')': return 2 ;
						case '$': return 1 ;
					}
				case '/':
					switch(y){
						case '+': return 1 ;
						case '-': return 1 ;
						case '*': return -1 ;
						case '/': return -1 ;
						case '^': return -1 ;
						case '!': return -1 ;
						case '(': return 1 ;
						case ')': return 2 ;
						case '$': return 1 ;
					}
				case '^':
					switch(y){
						case '+': return 1 ;
						case '-': return 1 ;
						case '*': return 1 ;
						case '/': return 1 ;
						case '^': return -1 ;
						case '!': return -1 ;
						case '(': return 1 ;
						case ')': return 2 ;
						case '$': return 1 ;
					}
				case '!':
					switch(y){
						case '+': return 1 ;
						case '-': return 1 ;
						case '*': return 1 ;
						case '/': return 1 ;
						case '^': return 1 ;
						case '!': return -1 ;
						case '(': return 1 ;
						case ')': return 2 ;
						case '$': return 1 ;
					}
				case '(':
					switch(y){
						case '+': return 1 ;
						case '-': return 1 ;
						case '*': return 1 ;
						case '/': return 1 ;
						case '^': return 1 ;
						case '!': return 2 ;
						case '(': return 1 ;
						case ')': return 2 ;
						case '$': return 1 ;
					}
				case ')':
					switch(y){
						case '+': return -1 ;
						case '-': return -1 ;
						case '*': return -1 ;
						case '/': return -1 ;
						case '^': return -1 ;
						case '!': return -1 ;
						case '(': return 0 ;
						case ')': return 2 ;
						case '$': return 2 ;
					}
				case '$':
					switch(y){
						case '+': return -1 ;
						case '-': return -1 ;
						case '*': return -1 ;
						case '/': return -1 ;
						case '^': return -1 ;
						case '!': return -1 ;
						case '(': return 2 ;
						case ')': return 2 ;
						case '$': return 0 ;
					}
			}
			return 3 ;
		}

		public static int factorial(int n){
			if(n < 0){
				System.exit(1) ;
			}
			if(n == 0){
				return 1 ;
			}

			int fac = 1 ;
			while(n > 1){
				fac *= n-- ;
			}
			return fac ;
		}

		public static int operate(char x, int first, int folloing){
			switch(x){
				case '+': return folloing + first ;
				case '-': return folloing - first ;
				case '*': return folloing - first ;
				case '/': return folloing / first ;
				case '^':
					int exponential = 1 ; 
					while(first > 0){
						exponential *= folloing ;
						first -- ;
					}
					return exponential ;
			}
			return -1 ;
		}

		public static int evaluate(char[] infix_expr){
			Stack<Character> operator_stack = new Stack<Character>() ;
			Stack<Integer> digital_stack = new Stack<Integer>() ;
			operator_stack.push('$') ;
			while(!operator_stack.empty()){
				for(int i=0; i<infix_expr.length; i++){
					if(Character.isDigit(infix_expr[i])){
						int read_number = Character.getNumericValue(infix_expr[i]) ;
						int j = i ;
						while(Character.isDigit(infix_expr[++j])){
							read_number = read_number*10 + Character.getNumericValue(infix_expr[j]) ;
						}
						i = j - 1 ;
						digital_stack.push(read_number) ;
					}else{
						if(compare(infix_expr[i], operator_stack.peek()) == 1)
							operator_stack.push(infix_expr[i]) ;
						else if(compare(infix_expr[i], operator_stack.peek()) == 0)
							operator_stack.pop() ;
						else if(compare(infix_expr[i], operator_stack.peek()) == -1){
							if(operator_stack.peek().equals('!')){
								operator_stack.pop() ;
								int digital_cal = digital_stack.pop() ;
								digital_stack.push(factorial(digital_cal)) ;
								
							}
							else{
								char operate_cal = operator_stack.pop() ;
								int digital_first = digital_stack.pop() ;
								int digital_following = digital_stack.pop() ;							
								digital_stack.push(operate(operate_cal, digital_first, digital_following)) ;
							}
							i -- ;
						}
						else{
							System.err.println("Something error.") ;
							return -1 ;
						}
							
					}
				}
			}
			return digital_stack.pop() ;
		}

		public static void main(String args[]){
			String infix_expression = "(1+2^3!-4)*(5!-(6-(7-(89-0!))))" ;
			int result = evaluate(infix_expression.concat("$").toCharArray()) ;
			System.out.println(result) ;
		}
	}/*output
	2013
	[Finished in 0.7s]
	*/

**利用逆波兰表达式(Reverse Polish Notation)进行中缀表达式的计算**

> 我们可以将中缀表达式表示成逆波兰表达式的形式，也称之为后缀表达式

将中缀表达式转化为后缀表达式的算法：

- Create an empt stack called opstack for keeping operators. Create an empty list for output.
- Convert the input infix string to a list by using string method split.
- Scan the token list from left to right.
	+ if the token is an operand, append it to the end of the output list.
	+ if the token is a left paranthesis, push it on the optstack.
	+ if the token is the right paranthesis, pop the opstack until the corresponding left parenthesis is removed. Append each operator to the end of the output list.
	+ if the token is an operator(+-^...), push it to the opstack. However, first remove any operator already on the opstack that have higher or equal precedence and append them to the output list.
- When the input expression has been completely processed, check the opstack. Any operators still on the stack can be removed and appended to the end of the output list.

算法实现：ReversePolishNotation.java

	/*
	 - 中缀表达式转化为后缀表达式时候要保存在一个字符串序列当中，将大于9的数转化为字符串，保存在字符序列中不可行
	 - 后缀表达式的计算简单，只需要保存一个整数栈即可
	 */
	import java.util.- ;

	public class ReversePolishNotation{

		public static List<String> infixToPostfix(char[] infix_expr){
			Stack<Character> operator_stack = new Stack<Character>() ;
			List<String> postfix = new ArrayList<String>() ;
			operator_stack.push('(') ;

			for(int i=0; i<infix_expr.length; i++){
				if(Character.isDigit(infix_expr[i])){
					String read_num = "" + infix_expr[i] ; 			
					int j = i ;
					while(Character.isDigit(infix_expr[++j])){
						read_num = read_num + infix_expr[j] ;
					}
					i = j - 1 ;
					postfix.add(read_num) ;
				}else{
					if(Character.compare(infix_expr[i], '(') == 0)
						operator_stack.push('(') ;
					else if(Character.compare(infix_expr[i], ')') == 0){
						while(!operator_stack.peek().equals('(')){
							postfix.add("" + operator_stack.pop()) ;
						}
						operator_stack.pop() ;
					}else{
						if(InfixExpressionEvaluate.compare(infix_expr[i], operator_stack.peek()) == 1){
							operator_stack.push(infix_expr[i]) ;
						}else{
							if(InfixExpressionEvaluate.compare(infix_expr[i], operator_stack.peek()) == -1){
								postfix.add("" + operator_stack.pop()) ;
								i -- ;
							}
						}
					}
				}
			}
			return postfix ;
		}


		public static int evaluate(String[] postfix){
			Stack<Integer> compute = new Stack<Integer>() ;
			for(int i=0; i<postfix.length; i++){
				if(isDigit(postfix[i])){
					compute.push(Integer.parseInt(postfix[i])) ;
				}else{
					switch(postfix[i]){
						case "!":
							int fac = compute.pop() ;
							compute.push(InfixExpressionEvaluate.factorial(fac)) ; 
							break ;
						default:  
							int first = compute.pop() ;
							int following = compute.pop() ;
							compute.push(InfixExpressionEvaluate.operate(postfix[i].toCharArray()[0], first, following)) ;
					}
				}
			}			
			return compute.pop() ;
		}

		public static boolean isDigit(String s){
			try{
				Integer.parseInt(s) ;
				return true ;
			}catch(Exception e){
				return false ;
			}
		}
		

		public static void main(String args[]){
			String infix_expression = "(1+2^3!-4)*(5!-(6-(7-(89-0!))))" ;
			char[] infix_char = infix_expression.concat(")").toCharArray() ;
			List<String> postfix = infixToPostfix(infix_char) ;
			System.out.println(postfix) ;

			String[] str = new String[postfix.size()] ;
			Iterator<String> iter = postfix.iterator() ;
			int i = 0 ;
			while(iter.hasNext()){
				str[i++] = iter.next() ;	
			}	
			System.out.println(evaluate(str)) ;
		}
	}/- output:
	[1, 2, 3, !, ^, +, 4, -, 5, !, 6, 7, 89, 0, !, -, -, -, -, *]
	2013
	[Finished in 0.7s]
	*/

**A Linked-List Stack Implementation**

LinearNode.java
	
	/*
	 - 作为Stack的field使用
	 - 包含一个data和preference
	 */
	package org.fmz.container ;

	public class LinearNode{
		public LinearNode next ;
		public Object data ;

		public LinearNode(Object o){
			data = o ;
		}

		public LinearNode(Object o, LinearNode nxt){
			data = o ;
			next = nxt ;
		}
	}

Stack.java

	/*
	 - 通过link-list的方式实现了Stack的数据结构
	 */
	package org.fmz.container;

	public class Stack{
		protected LinearNode head;
		protected int numItems = 0;


		public Stack(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		public void clear(){
			head = null ;
		}

		public boolean isEmpty(){
			return numItems == 0 ;
		}

		public Object pop(){
			if(isEmpty())
				return null;
			Object element = head.data ;
			head = head.next ;
			numItems -- ;
			return element ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public void push(Object element){
			if(isEmpty())
				head = new LinearNode(element) ;
			else
				head = new LinearNode(element, head) ;
			numItems ++ ;
		}

		public int size(){
			return numItems ;
		}

		public Object top(){
			if(isEmpty())
				return null;
			return head.data ;
		}

	}

**递归(Recursion)**

> A variable to any running program is stack called the run-time stack. If the program in question takes the form of machine code, i.e., if the program was written in a truly compiled language, then the run-time stack is part of the running program. Java is not a truly compiled language; a java class file contains not machine code but byte code. Byte code is a language intermediate between Java and machine language. Java byte code cannot be executed directly by the CPU; byte code must be interperted, i.e., executed by a machine-language program. The machine-language program that executes Java byte code is called the Java Virtual Machine(JVM). The run-time stack available to a Java program resides within the JVM.

> When a methon is called, the method's arguements and its return address are pushed onto the run-time stack before execution of the methon begins. A methon's return address marks the location at which program execution should continue after the methon has returned. The methon's arguements, its return address, and any other pertinent data that have been pushed onto the run-time stack are collectively refered to as the method call's stack frame, or call frame, or activation record.

Looping and recursion are equivalent, i.e., any algorithm that can be implemented with a loop can also be implemented with recursion, and vice versa.

The primary advantage of recursion is that it often means less work for the programmer. The drawback of it is that building and removing cll frames is time comsuming relative to the record keeping repuired by loops, and another is stack overflow.

---

<h3 id="3.2">3.2 队列(Queue)</h3>

> 我们经常使用单链表(singly linked list)来实现栈数据结构，我们也可以使用单链表来实现队列。但是我们经常使用双链表(double linked list)来实现队列。一个link用来储存node的前面一个node地址(predecessor)，另外一个link用来储存node的后面一个node地址(Successor)

DLNode.java

	package org.fmz.container;

	public class DLNode extends SLNode {

		DLNode prev;

		public DLNode(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		/**
	 	- 
	 	- @param dat
		 */
		public DLNode(Object dat){
			super(dat) ;
		}

		/**
	 	- 
	 	- @param dat
	 	- @param pre
	 	- @param nxt
		 */
		public DLNode(Object dat, DLNode pre, DLNode nxt){
			super(dat, nxt) ;
			prev = pre ;
		}

	}

SLNode.java

	package org.fmz.container;

	public class SLNode {

		public Object data;
		public SLNode next;

		public SLNode(){

		}

		public void finalize() throws Throwable {

		}

		/**
	 	- 
	 	- @param dat
		 */
		public SLNode(Object dat){
			data = dat ;
		}

		/**
	 	- 
	 	- @param dat
	 	- @param nxt
		 */
		public SLNode(Object dat, SLNode nxt){
			data = dat ;
			next = nxt ;
		}

	}

> 这里的 SLNode 指的是上文中的 LinearNode

Queue.java

	package org.fmz.container;

	public class Queue{ 

		DLNode head;
		DLNode tail;
		int numItems;

		public Queue(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		public void clear(){
			head = tail = null ;
		}

		public Object front(){
			if(isEmpty())
				return null;
			return head.data ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public void insertBack(Object element){
			if(isEmpty()){
				head = tail = new DLNode(element) ;
				numItems ++ ;
				return ;
			}
			tail.next = new DLNode(element) ;
			tail = (DLNode)tail.next ;
			numItems ++ ;
		}

		public boolean isEmpty(){
			return numItems == 0 ;
		}

		public Object removeFront(){
			Object tmp = front() ;
			if(tmp == null)
				return null;
			head = (DLNode)head.next ;
			if(head != null)
				((DLNode)head).prev = null ;
			else
				tail = null ;
			numItems -- ;
			return tmp ;
		}

		public int size(){
			return numItems ;
		}

	}

**队列的应用**

Queues are typically used to avoid loss in situations where congestion is possible, i.e., when request for some resource may arrive faster than they can be servied. A printer is an example of such resource; you may have heared the term print queue. A print queue holds pending print jobs in the order of the arrival. Queues are also used in computer simulations.

---

<h3 id="3.3">3.3 双向队列(Deque)</h3>

> A deque(pronounced "deck") is a double-ended queue.

Deque.java

	package org.fmz.container;


	public class Deque extends Queue {

		public Deque(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		public Object back(){
			if(isEmpty())
				return null ;
			return tail.data ;
		}

		/**
	 	- 
	 	- @param element
		 */
		public void insertFront(Object element){
			if(isEmpty()){
				insertBack(element) ;
				numItems ++ ;
				return ;
			}
			head = new DLNode(element, null, (DLNode)head) ;
			if(head.next != null)
				((DLNode)head.next).prev = (DLNode)head ;
			numItems ++ ;

		}

		public Object removeback(){
			Object temp = back() ;
			if(temp == null)
				return null ;
			tail = tail.prev ;
			if(tail == null)
				head = null ;
			else{
				((DLNode)tail.next).prev = null ;
				tail.next = null ;
			}
			numItems -- ;
			return temp ;
		}

	}

**Deque Application**

One application of deque is storing a web browser's history. Recently visited URLs are added to the front of the deque, and the URL at the back of the deque is removed after some specified number of insertions at the front.

---

<h3 id="3.4">3.4 通用二叉树(General Binary Tree)</h3>

> To traverse a binary tree is to visit each of the tree's node.

- A preorder traversal visits a given node before visiting either of the node's children(and their subtrees).
- An inorder traversal visits a given node's left child(ant the left child's subtree), then the node itself, then the node's right child(and the right child's subtree).
- A postorder traversal visits a given node's two subtree before visiting the node itself.

**Binary Tree Implementation**

TreeContainer.java

	package org.fmz.container;


	public abstract class TreeContainer extends LinkedContainer {

		protected Node root;

		public TreeContainer(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		public void clear(){
			root = null ;
		}

	}

BinaryTree.java

	package org.fmz.container;


	public abstract class BinaryTree extends TreeContainer {

		public static abstract class BinaryTreeNode extends Node {

			public BinaryTreeNode leftChild;
			public BinaryTreeNode parent;
			public BinaryTreeNode rightChild;

			
			/**
		 	- 
		 	- @param dat
			 */
			public BinaryTreeNode(Object dat){
				super(dat) ;
			}

			public void finalize() throws Throwable {
				super.finalize();
			}

			/**
		 	- 
		 	- @param dat
		 	- @param lc
		 	- @param rc
		 	- @param par
			 */
			public BinaryTreeNode(Object dat, BinaryTreeNode lc, BinaryTreeNode rc, BinaryTreeNode par){
				super(dat) ;
				leftChild = lc ;
				rightChild = rc ;
				parent = par ;	
			}

			public boolean isLeaf(){
				return leftChild==null && rightChild==null ;
			}


		}

		public interface NodeProcessor {

			/**
		 	- 
		 	- @param node
			 */
			void processNode(BinaryTreeNode node);

		}

		public BinaryTree(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		public int height(){
			return heightHelper((BinaryTreeNode)root, -1) ;
		}

		/**
	 	- 
	 	- @param current
	 	- @param ht
		 */
		protected int heightHelper(BinaryTreeNode current, int ht){
			if(current == null)
				return ht ;
			return Math.max(heightHelper(current.leftChild, ht+1),
							heightHelper(current.rightChild, ht+1)) ;
		}

		/**
	 	- 
	 	- @param node
	 	- @param processor
		 */
		protected void inorder(BinaryTreeNode node, NodeProcessor processor){
			if(node != null)
				inorder(node.leftChild, processor) ;
			processor.processNode(node) ;
			if(node != null)
				inorder(node.rightChild, processor) ;
		}

		/**
	 	- 
	 	- @param processor
		 */
		public void inorderTraverse(NodeProcessor processor){
			inorder((BinaryTreeNode)root, processor) ;
		}

		/**
	 	- 
	 	- @param node
	 	- @param processor
		 */
		protected void postorder(BinaryTreeNode node, NodeProcessor processor){
			if(node != null){
				postorder(node.leftChild, processor) ;
				postorder(node.rightChild, processor) ;
			}
			processor.processNode(node) ;
		}

		/**
	 	- 
	 	- @param processor
		 */
		public void postorderTraverse(NodeProcessor processor){
			postorder((BinaryTreeNode)root, processor) ;
		}

		/**
	 	- 
	 	- @param node
	 	- @param processor
		 */
		protected void preorder(BinaryTreeNode node, NodeProcessor processor){
			processor.processNode(node) ;
			if(node != null){
				preorder(node.leftChild, processor) ;
				preorder(node.rightChild, processor) ;
			}
		}

		/**
	 	- 
	 	- @param node
	 	- @param processor
		 */
		public void preorderTraverse(BinaryTreeNode node, NodeProcessor processor){
			preorder((BinaryTreeNode)root, processor) ;
		}

	}

> Class BinaryTree extends TreeContainer and also contains static member class BinaryTreeNode, which subclass Node, and member interface NodeProcessor. Any class that realize interface NodeProcessor must offer a public method called processNode, which accepts an arguement of type of BinaryTreeNode. This gives our traversals great versatility; we can process all the nodes of a BinaryTree in any way we choose simply by coding a class that fulfills the contract set forth by the interface.

**Binary Tree Application**

- Huffman coding, which is used in data compression
- The expression tree
- The binary search tree(BST)

---

<h3 id="3.5">3.5 Binary Search Tree</h3>

> A binary search tree(BST) is a totally ordered binary tree. The BST's total ordering does the heap's partial ordering one better; not only is there a relationship between heap's partial ordering one node and its children, but there is also a definite relationship between the children. In a BST, the 
value of a node's left child is less than the value of the node itself, and the value of a node's right child is greater than or equal to the value of the node.

**BST Implementation**

BinarySearchTree.java

	package org.fmz.container;


	public class BinarySearchTree extends BinaryTree {

		// A BST needs Comparable nodes because a BST is totally ordered

		public static class BSTNode extends BinaryTreeNode implements Comparable {


			public void finalize() throws Throwable {
				super.finalize();
			}

			/**
		 	- 
		 	- @param dat
			 */
			public BSTNode(Comparable dat){
				super(dat) ;
			}

			/**
		 	- 
		 	- @param dat
		 	- @param lc
		 	- @param rc
		 	- @param par
			 */
			public BSTNode(Comparable dat, BSTNode lc, BSTNode rc, BSTNode par){
				super(dat, lc, rc, par) ;
			}

			/**
		 	- 
		 	- @param o
			 */
			public int compareTo(Object o){
				return ((Comparable)data).compareTo(((BSTNode)o).data) ;
			}

		}

		public BinarySearchTree(){

		}

		public void finalize() throws Throwable {
			super.finalize();
		}

		/**
	 	- 
	 	- @param target
		 */
		public Comparable find(BSTNode target){
			BSTNode found = findHelper((BSTNode)root, new BSTNode(target)) ;

			if(found ==  null)
				return null ;
			return (Comparable)found.data ;
		}

		/**
	 	- 
	 	- @param current
	 	- @param target
		 */
		protected BSTNode findHelper(BSTNode current, BSTNode target){
			// If we have left the tree, then the target was not found
			// This method is recusive

			if(current == null)
				return null ;

			// If the target node is less than the current node, then turn left

			if(target.compareTo(current) < 0)
				return findHelper((BSTNode)current.leftChild, target) ;

			// If the target node is greater than the currnt node, then turn right

			if(target.compareTo(current) > 0)
				return findHelper((BSTNode)current.leftChild, target) ;

			// Otherwise the currnt node is the target node

			return current ;
		}

		/**
	 	- 
	 	- @param newItem
		 */
		public void insert(Comparable newItem){
			root = insertHelper((BSTNode)root, new BSTNode(newItem)) ;
		}

		/**
	 	- 
	 	- @param current
	 	- @param newNode
		 */
		protected BSTNode insertHelper(BSTNode current, BSTNode newNode){
			// If we have found the correct location for the node, then return the reference to the new node, linking it to the tree
			// This method is recusive

			if(current == null){
				numItems ++ ;
				return newNode ;
			}

			// If the new node is less than the current node, then go left
			// otherwise go right

			if(newNode.compareTo(current) < 0){
				current.leftChild = insertHelper((BSTNode)current.leftChild, newNode) ;
				current.leftChild.parent = current ;
			}else{
				current.rightChild = insertHelper((BSTNode)current.rightChild, newNode) ;
				current.rightChild.parent = current ;
			}
			return current ;
		}

		/**
	 	- 
	 	- @param current
		 */
		protected BSTNode maxHelper(BSTNode current){
			// This method is not recusive, because the maximum nodes of a BST is easy to find using a loop;
			// go right until the rightmost node if found

			if(current == null)
				return null ;
			while(current.rightChild != null)
				current = (BSTNode)current.rightChild ;
			return current ;
		}

		public Comparable maximum(){
			BSTNode max = maxHelper((BSTNode)root) ;
			if(max == null)
				return null ;
			return (Comparable)max.data ;
		}

		/**
	 	- 
	 	- @param current
		 */
		protected BSTNode minHelper(BSTNode current){
			// This minimum node of a BST is the leftmost node

			if(current == null)
				return null ;
			while(current.leftChild != null)
				current = (BSTNode)current.leftChild ;
			return current ;
		}

		public Comparable minimum(){
			BSTNode min = minHelper((BSTNode)root) ;
			if(min == null)
				return null ;
			return (Comparable)min.data ;
		}

		/*
	 	- The inorder predecessor of node n is
	 	- 1. the maximum node of n's left subtree, provided that n has a left subtree
	 	- 2. the first ancessor of n such that n is in the ancessor's right subtree
		 */ 

		/**
	 	- 
	 	- @param target
		 */
		public Comparable predecessor(Comparable target){
			BSTNode found = findHelper((BSTNode)root, new BSTNode(target)) ;
			if(found == null)
				return null ;
			if(found.leftChild != null)
				return (Comparable)maxHelper((BSTNode)found.leftChild).data ;
			BSTNode parent = (BSTNode)found.parent ;
			while(parent != null && parent.compareTo(found) > 0)
				parent = (BSTNode)parent.parent ;
			if(parent == null)
				return null ;
			return (Comparable)parent.data ;
		}

		/**
	 	- 
	 	- @param target
		 */
		public void remove(Comparable target){
			removeHelper((BSTNode)root, new BSTNode(target)) ;
		}

		/*
	 	- 1. Find the node to be removed. If no such node exists, terminate.
	 	- 2.  (1) if(the node to be removed is a leaf)
		 			If the target node has a parent, i.e., if the target node is not the root, then overwrite the parent's reference to the target node
		 			with null, unlinking the target node from the tree.
		 	   (2) else if(the target node has no left child)
		 	   		Overwirte the target node's data portion and the links to its children with those of its right child, effectively transforming the
		 	   		node into its right child.
		 	   (3) else if(the target node has no right child)
		 	   		Transform the target node into its left child.
		 	   (4) else // the target node has two childern
					Replace the target node's data portion with that of its successor, which is the minimum node of the target node's right subtree. Then
					delete the successor, to which case (1) or case (2) must apply.
		 */

		/**
	 	- 
	 	- @param current
	 	- @param target
		 */
		protected BSTNode removeHelper(BSTNode current, BSTNode target){
			if(current == null)
				return null ;
			if(target.compareTo(current) < 0)
				current.leftChild = removeHelper((BSTNode)current.leftChild, target) ;
			else if(target.compareTo(current) > 0)
				current.rightChild = removeHelper((BSTNode)current.rightChild, target) ;
			else{
				if(current.isLeaf()){
					numItems -- ;
					return null ;
				}
				BSTNode temp ;
				if(current.leftChild == null){
					temp = (BSTNode)current.rightChild ;
					current.data = temp.data ;
					current.leftChild = temp.leftChild ;
					if(current.leftChild != null)
						current.leftChild.parent = current ;
					current.rightChild = temp.rightChild ;
					if(current.rightChild != null)
						current.rightChild.parent = current ;
				}
				else if(current.rightChild == null){
					temp = (BSTNode)current.leftChild ;
					current.data = temp.data ;
					current.leftChild = temp.leftChild ;
					if(current.leftChild != null)
						current.leftChild.parent = current ;
					current.rightChild = temp.rightChild ;
					if(current.rightChild != null)
						current.rightChild.parent = current ;
				}
				else{
					temp = (BSTNode)current.rightChild ;
					if(temp.isLeaf()){
						current.data = temp.data ;
						current.rightChild = null ;
					}
					else if(temp.leftChild == null){
						current.data = temp.data ;
						current.rightChild = temp.rightChild ;
						if(current.rightChild != null)
							current.rightChild.parent = current ;
					}
					else{
						while(temp.leftChild.leftChild != null)
							temp = (BSTNode)temp.leftChild ;
						current.data = temp.leftChild.data ;
						removeHelper((BSTNode)temp, new BSTNode((Comparable)temp.leftChild.data)) ;
						numItems ++ ;
					}
				}
				numItems -- ;
			}
			return current ;
		}

		/*
	 	- The inorder successor of node n is
	 	- 1. the minimum node of n's right subtree, provided that n has a right subtree
	 	- 2. the first ancessor of n such that n is in the ancessor's left subtree
		 */ 

		/**
	 	- 
	 	- @param target
		 */
		public Comparable successor(Comparable target){
			BSTNode found = findHelper((BSTNode)root, new BSTNode(target)) ;
			if(found == null)
				return null ;
			if(found.rightChild != null)
				return (Comparable)minHelper((BSTNode)found.rightChild).data ;
			BSTNode parent = (BSTNode)found.parent ;
			while(parent != null && parent.compareTo(found) <= 0)
				parent = (BSTNode)parent.parent ;
			if(parent == null)
				return null ;
			return (Comparable)parent.data ;
		}

	}

**Time Analysis of the Fundamental BST Operations**

> A tree is said to be balanced, if the tree has the least possible height.

The height of a balenced binary tree with n nodes is approximately lg(n). The operatinos insert, find, and remove are all in O(lg(n))

In the worse case, however, operations insert, find, and remove are in O(n), for in the worse case a BST is simply a list.

**BST Application**

- dictiorary

---

---
