---
layout: post
title: 令人吃惊的java语法
---

### Use for-each syntax to output two-dimensional array

	package org.fmz.awesome ;

	import java.util.* ;

	public class ForEachOutputTwoDimensionalArray{
		public static void main(String args[]){
			int[][] A = new int[10][10] ;
			Random random = new Random(25) ;
			for(int i=0;i<A.length;i++){
				Arrays.fill(A[i], random.nextInt(10)) ;
			}

			for(int[] i : A){
				for(int j: i){
					System.out.print(j + "\t") ;
				}
				System.out.println() ;
			}
		}
	}/* Output:
		1       1       1       1       1       1       1       1       1       1
		8       8       8       8       8       8       8       8       8       8
		7       7       7       7       7       7       7       7       7       7
		8       8       8       8       8       8       8       8       8       8
		7       7       7       7       7       7       7       7       7       7
		8       8       8       8       8       8       8       8       8       8
		5       5       5       5       5       5       5       5       5       5
		7       7       7       7       7       7       7       7       7       7
		6       6       6       6       6       6       6       6       6       6
		4       4       4       4       4       4       4       4       4       4
	*/

---

---

### Thinking from Object-Oriented to use `this`

	package org.fmz.learning ;

	public class Cal{
		int number ;
		public Cal(int number){
			this.number = number ;
		}
		public Cal add(int i){
			number += i ;
			return this ;
		}

		public static void main(String args[]){
			Cal c = new Cal(100).add(1).add(2).add(3).add(4) ;//this is called fluent API
			System.out.println(c.number) ;
		}
	}/* Output:
		110
	*/

> Sometimes we don't know to use return or void, especially, we can use both. You can return yout kind of Object, which is very convenient, and this can show your understanding of Object-Oriented thinking.

---

---
