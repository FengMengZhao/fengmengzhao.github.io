---
layout: post
title: 运筹学算法
subtitle: java程序实现经典的最短路问题、最速下降算法、斐波那契搜索、黄金分割搜素
---

### 最短路径问题

	import java.util.*;
	class Vertex implements Comparable<Vertex>{
		public String name ;
		public double minDistance = Double.POSITIVE_INFINITY ;
		public Edge[] adjacencies ;
		public Vertex(String name){
			this.name = name ;
		}
		public String toString(){
			return name ;
		}
		public int compareTo(Vertex other){//必须有方法的覆写；
			return Double.compare(minDistance, other.minDistance) ;
		}
		public Vertex previous ;
	}

	class Edge{
		public Vertex target ;
		public double weight ;
		public Edge(Vertex target,double weight){
			this.target = target ;
			this.weight = weight ;
		}
	}

	public class DijkstraAlgorithmExercise {
		
		public static void computePaths(Vertex source){
			source.minDistance = 0. ;
			PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>() ;
			vertexQueue.add(source) ;
			
			while(!vertexQueue.isEmpty()){
				Vertex first = vertexQueue.poll() ;
				
				for(Edge e : first.adjacencies){
					Vertex next = e.target ;
					double weight = e.weight ;
					double distanceThroughFirst = first.minDistance + weight ;
					if(distanceThroughFirst < next.minDistance){
						vertexQueue.remove(next) ;
						next.minDistance = distanceThroughFirst ;
						next.previous = first ;
						vertexQueue.add(next) ;
					}
				}
			}
		}
		
		public static  List<Vertex> getShortestPathTo(Vertex target){ 
			List<Vertex> path = new ArrayList<Vertex>() ;
			for(Vertex vertex = target;vertex != null;vertex = vertex.previous){
				path.add(vertex) ;
			}
			Collections.reverse(path) ;
			return path ;
			
		}
		public static void main(String args[]){
			Vertex A = new Vertex ("A") ;
			Vertex B1 = new Vertex ("B1") ;
			Vertex B2 = new Vertex ("B2") ;
			Vertex C1 = new Vertex ("C1") ;
			Vertex C2 = new Vertex ("C2") ;
			Vertex C3 = new Vertex ("C3") ;
			Vertex C4 = new Vertex ("C4") ;
			Vertex D1 = new Vertex ("D1") ;
			Vertex D2 = new Vertex ("D2") ;
			Vertex D3 = new Vertex ("D3") ;
			Vertex E1 = new Vertex ("E1") ;
			Vertex E2 = new Vertex ("E2") ;
			Vertex E3 = new Vertex ("E3") ;
			Vertex F1 = new Vertex ("F1") ;
			Vertex F2 = new Vertex ("F2") ;
			Vertex G = new Vertex ("G") ;
			
			A.adjacencies = new Edge[]{ new Edge(B1,5),
						    new Edge(B2,3)};
			B1.adjacencies = new Edge[]{new Edge(A,5),
						    new Edge(C1,1),
						    new Edge(C2,3),
						    new Edge(C3,6)};
			B2.adjacencies = new Edge[]{new Edge(A,3),
						    new Edge(C2,8),
						    new Edge(C3,7),
						    new Edge(C4,6)};
			C1.adjacencies = new Edge[]{new Edge(B1,1),
				          	    new Edge(D1,6),
						    new Edge(D2,8)};
			C2.adjacencies = new Edge[]{new Edge(B1,3),
						    new Edge(B2,8),
						    new Edge(D1,3),
						    new Edge(D2,5)};
			C3.adjacencies = new Edge[]{new Edge(B1,6),
						    new Edge(B2,7),
						    new Edge(D2,3),
						    new Edge(D3,3)};
			C4.adjacencies = new Edge[]{new Edge(B2,6),
						    new Edge(D2,8),
						    new Edge(D3,4)};
			D1.adjacencies = new Edge[]{new Edge(C1,6),
						    new Edge(C2,3),
						    new Edge(E1,2),
						    new Edge(E2,2)};
			D2.adjacencies = new Edge[]{new Edge(C1,8),
						    new Edge(C2,5),
						    new Edge(C3,3),
						    new Edge(C4,8),
						    new Edge(E2,1),
						    new Edge(E3,2)};
			D3.adjacencies = new Edge[]{new Edge(C3,3),
						    new Edge(C4,4),
						    new Edge(E2,3),
						    new Edge(E3,3)};
			E1.adjacencies = new Edge[]{new Edge(D1,2),
						    new Edge(F1,3),
						    new Edge(F2,5)};
			E2.adjacencies = new Edge[]{new Edge(D1,2),
						    new Edge(D2,1),
						    new Edge(D3,3),
						    new Edge(F1,5),
						    new Edge(F2,2)};
			E3.adjacencies = new Edge[]{new Edge(D2,2),
						    new Edge(D3,3),
						    new Edge(F1,6),
						    new Edge(F2,6)};
			F1.adjacencies = new Edge[]{new Edge(E1,3),
						    new Edge(E2,5),
						    new Edge(E3,6),
						    new Edge(G,4)};
			F2.adjacencies = new Edge[]{new Edge(E1,5),
						    new Edge(E2,2),
						    new Edge(E3,6),
						    new Edge(G,3)};
			G.adjacencies = new Edge[]{ new Edge(F1,4),
						    new Edge(F2,3)};
			Vertex[] vertexs ={A,B1,B2,C1,C2,C3,C4,D1,D2,D3,E1,E2,E3,F1,F2,G} ;
			computePaths(A) ;
			
			for(Vertex v : vertexs){
				System.out.println("MinDistance to v:"+v.minDistance) ;
				List<Vertex> path = getShortestPathTo(v) ;
				System.out.println("Path:"+path) ;
			}
		}
    }

### 最速下降算法

	class MultiArray{
		public double[][] multiArray(double[][] a,double[][] b,double[][] c){
			double[][] temp1 = new double[a.length][b[0].length] ;
			double[][] temp2 = new double[a.length][c[0].length] ;
			if(a[0].length != b.length){
				System.out.println("输入矩阵不能相乘！") ;
				System.exit(1) ;
			}
			else{
				for(int i=0;i<a.length;i++){
					for(int j=0;j<a[0].length;j++){
						for(int k=0;k<b[0].length;k++){
							temp1[i][k] += a[i][j]*b[j][k] ;
						}
					}
				}
			}
			
			if(temp1[0].length != c.length){
				System.out.println("输入矩阵不能相乘！") ;
				System.exit(1) ;
			}
			else{
				for(int i=0;i<temp1.length;i++){
					for(int j=0;j<temp1[0].length;j++){
						for(int k=0;k<c[0].length;k++){
							temp2[i][k] += temp1[i][j]*c[j][k] ;
						}
					}
				}
			}
			return temp2 ;
		}
		
		public double[][] multiArray(double[][] a,double[][] b){
			double[][] temp = new double[a.length][b[0].length] ;
			if(a[0].length != b.length){
				System.out.println("输入矩阵不能相乘！") ;
				System.exit(1) ;
			}
			else{
				for(int i=0;i<a.length;i++){
					for(int j=0;j<a[0].length;j++){
						for(int k=0;k<b[0].length;k++){
							temp[i][k] += a[i][j]*b[j][k] ;
						}
					}
				}
			}
			return temp ;
		}
	}

	public class GradientMethod {
		public static void main(String args[]){
		
			//double[][] H = {2,0;0,50} ;
			double[][] H = {3,-1;-1,1} ;
			double x1 = 2 ;
			double x2 = 2 ;
			double λ = 0 ;
			double[][] derivative = {2;0} ;//注意数组的静态初始化与动态初始化的区别，另外循环一定要有三条件：初始条件；终止条件；循环条件！！！
			MultiArray ma = new MultiArray() ;
			double[][] IDerivative = {2;0} ;
			/*
			do{
				//derivative =new double[][] {2*x1;50*x2} ;//列向量梯度,注意数组动态初始化和静态初始化的区别！！！！！
				derivative =new double[][] {3*x1-x2-2;x2-x1} ;
				//IDerivative =new double[][]{2*x1;50*x2} ;//转置行向量梯度
				IDerivative =new double[][]{3*x1-x2-2;x2-x1} ;
				double[][] temp1 = ma.multiArray(IDerivative,derivative) ;
				double[][] temp2 = ma.multiArray(IDerivative,H,derivative) ;
				λ = temp1[0][0]/temp2[0][0] ;
				
				x1 = x1 - λ*derivative[0][0] ;
				x2 = x2 - λ*derivative[1][0] ;
				
				
				System.out.println(λ) ;
				System.out.println("x1= "+x1+"\tx2="+x2) ;
				System.out.println(1.5*x1*x1+0.5*x2*x2-x1*x2-2*x1+"\n") ;
			}while((derivative[0][0]*derivative[0][0]+derivative[1][0]*derivative[1][0])>0.0001) ;//do while 循环和while循环的另外区别是do while 可以用循环内的变量来限制循环的终止。
			*/
			
			while((derivative[0][0]*derivative[0][0]+derivative[1][0]*derivative[1][0])>0.0001){
				derivative =new double[][] {3*x1-x2-2;x2-x1} ;
				IDerivative =new double[][]{3*x1-x2-2;x2-x1} ;
				double[][] temp1 = ma.multiArray(IDerivative,derivative) ;
				double[][] temp2 = ma.multiArray(IDerivative,H,derivative) ;
				λ = temp1[0][0]/temp2[0][0] ;
				
				x1 = x1 - λ*derivative[0][0] ;
				x2 = x2 - λ*derivative[1][0] ;
				
				
				System.out.println(λ) ;
				System.out.println("x1= "+x1+"\tx2="+x2) ;
				System.out.println(1.5*x1*x1+0.5*x2*x2-x1*x2-2*x1+"\n") ;
			}
		}
	}	

### 斐波那契（Fibonacci）一维搜索算法

	import java.text.*;
	public class FibonacciOneDimensionalSearch {
		public static void main(String args[]){
		double a0 = -1 ;
		double b0 = 3 ;
		double δ = 0.08 ;
		double a1 = 0 ;
		double b1 = 0 ;
		double f = 0 ;
		
		double[] fiInt = Fibonacci(15) ;
		/*
		for(int i=0;i<fiInt.length;i++){
				System.out.println(fiInt[i]) ;
			}
		}
		*/
		f = (int)(1/δ) ;
		double[] temp = new double[fiInt.length] ;
		for(int i=0;i<fiInt.length;i++){
			temp[i] = Math.abs(f-fiInt[i]) ;
			//System.out.println(temp[i]) ;
		}
		
		double minIndex = 0 ;
		double min = temp[0] ;
		for(int index=0;index<fiInt.length;index++){
			if(temp[index]<min){
				min = temp[index] ;
				minIndex = index ;
			}
		}
		//System.out.println(min+"\t"+minIndex) ;
		
		//f = fiInt[(int)minIndex] ;
		//System.out.println(f) ;
		System.out.println("迭代次数："+((int)(minIndex)-1)) ;
		
		DecimalFormat df = new DecimalFormat(".###") ;
		
		for(int i=(int)minIndex;i>=2;i--){
			a1 = a0 +(fiInt[i-2]/fiInt[i])*(b0-a0) ;
			b1 = a0 + (fiInt[i-1]/fiInt[i])*(b0-a0) ;
			/*
			t = a1 ;
			double y1 = y ;
			t = b1 ;
			double y2 = y ;
			*/
			if(fun(a1) > fun(b1)){
				a0 = a1 ;
			}
			else{
				b0 = b1 ;
			}
			
			
			//System.out.println("a1= "+a1+"\t"+"b1= "+b1) ;
			System.out.println("a0= "+df.format(a0)+"\tb0= "+df.format(b0)) ;
			System.out.println("a1= "+df.format(a1)+"\tb1= "+df.format(b1)) ;
			System.out.println("ya1= "+df.format(fun(a1))+"\tyb1= "+df.format(fun(b1))) ;
			//System.out.println("a0= "+a0+"\tb0= "+b0) ;
			//System.out.println("y1= "+fun(a1)+"\t"+"y2= "+fun(b1)) ;
			/*
			System.out.println(a1) ;
			System.out.println(fiInt[i-2]) ;
			System.out.println(fiInt[i-1]) ;
			System.out.println(fiInt[i]) ;
			System.out.println(a1) ;
			*/
			System.out.println() ;
		}
		//System.out.println(fun(0.5)) ;
	}
		
		public static double[] Fibonacci(int n){
			double[] fiInt = new double[n+1] ;
			fiInt[0] = fiInt[1] = 1 ;
			for(int i=2;i<fiInt.length;i++){
				fiInt[i] = fiInt[i-1] +fiInt[i-2] ;
			}
			return fiInt ;
		}
		public static double fun(double t){
			return t*t -t + 2 ;
		}
	}

### 黄金分割一维搜索算法

	import java.text.*;
	public class GoldenOneDimensionalSearch {
		public static void main(String args[]){
			double a0 = -1 ;
			double b0 = 3 ;
			double δ = 0.08 ;
			double a1 = 0 ;
			double b1 = 0 ;
			
			DecimalFormat df = new DecimalFormat(".###") ;
			
			do{
				a1 = a0 + (1-0.618)*(b0-a0) ;
				b1 = a0 + 0.618*(b0-a0) ;
				if(fun(a1) > fun(b1)){
					a0 = a1 ;
				}
				else{
					b0 = b1 ;
				}
				System.out.println("a0= "+df.format(a0)+"\tb0= "+df.format(b0)) ;
				//System.out.println("a1= "+df.format(a1)+"\tb1= "+df.format(b1)) ;
				System.out.println("ya1= "+df.format(fun(a1))+"\tyb1= "+df.format(fun(b1))) ;
				System.out.println() ;
			}while(((b0-a0)/4) > δ) ;
			
		}
		public static double fun(double t){
			return t*t -t +2 ;
		}
	}
