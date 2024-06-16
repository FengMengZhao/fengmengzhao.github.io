---
layout: post
title: 用增强的for循环和Iterator输出
subtitle: 泛型理解示例
---

	import java.util.*;

	public class LoopEnhanced_LoopIterator {
		@SuppressWarnings("hiding")
		public static <String> void print1(Collection<String> coll){
			for(String str : coll){
				System.out.println(str) ;
			}
		}
		@SuppressWarnings("hiding")
		public static <String> void print2(Collection<String> coll){
			Iterator<String> itr = coll.iterator() ;
			while(itr.hasNext()){
				String str = itr.next() ;
				System.out.println(str) ;
			}
		}
		public static void main(String args[]){
			Collection<String> coll = new ArrayList<String>() ;
			coll.add("fmz") ;
			coll.add("cll") ;
			coll.add("hkj") ;
			
			print1(coll) ;
			System.out.println("-------------------------------------") ;
			print2(coll) ;
		}
	}

### 使用泛型`<>`增强的for循环和Iterator输出

	import java.util.*;

	public class LoopEnhanced_LoopIteratorForAnyType {
		public static  <T> void print1(Collection<T> coll){
			for(T str : coll){
				System.out.print(str+"\t") ;
			}
		}
		
		public static  <T> void print2(Collection<T> coll){//泛型任意类型的使用
			Iterator<T> itr = coll.iterator() ;
			while(itr.hasNext()){
				System.out.print(itr.next()+"\t") ;
			}
		}
		public static void makeList(List<Integer> lis, int n){
			lis.clear() ;
			for(int i=0;i<n;i++){
				lis.add(i) ;
			}
		}
		public static void main(String args[]){
			Collection<String> coll = new ArrayList<String>() ;
			coll.add("fmz") ;
			coll.add("cll") ;
			coll.add("hkj") ;
			
			print1(coll) ;
			System.out.println("\n-------------------------------------") ;
			print2(coll) ;
			System.out.println("\n-----------------------------------------") ;
			List<Integer> list = new ArrayList<Integer>() ;
			makeList(list,10) ;
			/*
			for(int l : list){
				System.out.print(l+"\t") ;
			}
			*/
			print1(list) ;
			System.out.println() ;
			print2(list) ;
		}
	}
