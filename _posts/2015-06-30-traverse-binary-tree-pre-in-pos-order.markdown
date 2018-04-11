---
layout: post
title: 数据结构之二叉树前中后序遍历算法
subtitle: java实现二叉树遍历
---

	class BinaryTrees{
		int data ;
		BinaryTrees left ;
		BinaryTrees right ;
		public BinaryTrees(int data){
			this.data = data ;
			this.left = null ;
			this.right = null ;
		}

		public void insert(BinaryTrees root,int data){
			if(data > root.data){
				if(root.right == null){
					root.right = new BinaryTrees(data) ;			
				}else{
					this.insert(root.right, data);
				}
			}else{
				if(root.left == null){
					root.left = new BinaryTrees(data) ;
				}else{
					this.insert(root.left, data) ;
				}
			}
		}
	};

	public class BinaryTree_Pre_In_Post_Order_Traversal {
		public static void preOrder(BinaryTrees root){
			if(root != null){
				System.out.print(root.data+"\t") ;
				preOrder(root.left) ;
				preOrder(root.right) ;
			}
		};
		public static void inOrder(BinaryTrees root){
			if(root != null){
				inOrder(root.left) ;
				System.out.print(root.data+"\t") ;
				inOrder(root.right) ;
			}
		};
		public static void postOrder(BinaryTrees root){
			if(root != null){
				postOrder(root.left) ;
				postOrder(root.right) ;
				System.out.print(root.data+"\t") ;
			}
		};
		public static void main(String args[]){
			int[] array = {8,3,10,9,1,5} ;
			BinaryTrees root = new BinaryTrees(array[0]) ;
			for(int i=1;i<array.length;i++){
				root.insert(root, array[i]) ; 
			}
			preOrder(root) ;
			System.out.println("\n------------------------------------------") ;
			inOrder(root) ;
			System.out.println("\n------------------------------------------") ;
			postOrder(root) ;
			System.out.println("\n------------------------------------------") ;
		}
	}
