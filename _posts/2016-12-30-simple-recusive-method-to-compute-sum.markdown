---
layout: post
title: 简单的递归求最大数的方法
---

1. 递归真是一个神奇的东西！！！
2. 自己的理解：递归其实是一种内循环（迭代）！！！

代码实例：

    public class RecursiveMax {
		public static int recursiveMax(int[] A,int n){ if(n >= A.length){
				System.out.println("数组中没有足够的数") ;
				System.exit(0) ;//在这里的作用是不让运行报错！！！
			}
			if(n == 1){
				return A[0] ;
			}else{
				return Math.max(recursiveMax(A,n-1), A[n]) ;
			}
		}
		public static void main(String args[]){
			int[] A = {2,4,5,3,9,4,6,3,8,12,3,7} ;
			System.out.println(recursiveMax(A,11)) ;
		}
    }

---

### 导致递归的两个基本原则

1. 基本情形(base case)。必须要有一些基本的情形，它们不用递归就能够求解
2. 不断推进(making progress)。对于那些要递归求解的情形，递归调用必须总朝着一个基准的情形推进
