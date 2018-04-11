---
layout: post
title: java从文件中读取数据并赋值给数组
---

### 从txt文件中读取数据赋值给一维数组

1. 字节流读入字节数组中
2. 字节数组转化为字符串
3. 字符创解析为字符串数组
4. 字符串数组转化为数值，并赋值为数组	
5. 文本文件是以字节的形式储存的。

> 在txt文件中一个数字或者字母表示一个字节，比如0为（byte）48，9为（byte）57，空格为（byte）32，a为（byte）97，z为（byte）122，A为（byte）65，Z为（byte）90。

    public class ReadTxtFileToOneDimensionalArray {
		public static void main(String args[])throws Exception{
			File file = new File("G:\\文档文件\\临时文件\\时间表1.txt") ;
			InputStream input = new FileInputStream(file) ;
			byte[] b = new byte[(int)file.length()] ;
			input.read(b) ;
			String str = new String(b) ;
			System.out.println(str) ;
			String[] number = str.split("\t") ;
			for(int i=0;i<number.length;i++){
				System.out.print(number[i]+"\t") ;
			}
			int[] temp = new int[number.length] ;
			for(int i=0;i<number.length;i++){
				temp[i] = Integer.parseInt(number[i]) ;
				System.out.print(temp[i]+"\t") ;
			}
			input.close() ; 
		}
    }
		
### 从txt文件中读取数据赋值给二维数组

	public class ReadTxtFileToTwoDimensionalArray {
		public static void main(String args[])throws Exception{
			File file = new File("G:\\文档文件\\临时文件\\时间表.txt") ;
			InputStream input = new FileInputStream(file) ;
			byte[] b = new byte[(int)file.length()] ;
			input.read(b) ;
			
			String str = new String(b) ;
			String[] split = str.split("\r\n") ;
			String[][] array = new String[split.length][] ;
			for(int i=0;i<split.length;i++){
				array[i] = split[i].split(" ") ; 
			}
			int[][] temp = new int[split.length][array[0].length] ;
			for(int i=0;i<temp.length;i++){
				for(int j=0;j<temp[0].length;j++){
					temp[i][j] = Integer.parseInt(array[i][j]) ;
					System.out.print(temp[i][j]+"\t") ;
				}
				System.out.println() ;
			}
			input.close() ; 
		}
	}

### 用字符流`BufferedReader`读取数据到二维数组（目前只能做到在知道行和列的情况下，能准确额读入二维数组！！！）

	public class ReadTxtFileToArrayWithBufferedReader {
		public static void main(String args[])throws Exception{
			File file = new File("G:\\文档文件\\临时文件\\时间表.txt") ;
			Reader r = new FileReader(file) ;
			BufferedReader buf = new BufferedReader(r) ;
			String line = null ;
			int[][] temp = new int[10][9]  ;
			int row = 0 ;
			while((line = buf.readLine()) != null){
				String[] str = line.split(" ") ;
				for(int i=0;i<str.length;i++){
					temp[row][i] = Integer.parseInt(str[i]) ;
					System.out.print(temp[row][i]+"\t") ;
				}
				System.out.println() ;
				row ++ ;
			}
			buf.close() ;
		}
	}

### 从Excel文件中读取数据到一维数组

	public class ReadExcelToOneDimensionalArray {
		public static void main(String args[]){
			try{
				File file = new File("G:\\文档文件\\临时文件\\时间表1.xls") ;
				Workbook book = Workbook.getWorkbook(file) ;
				Sheet sheet = book.getSheet(0) ;
				int[][] temp = new int[sheet.getRows()][sheet.getColumns()] ;
				for(int i=0;i<sheet.getRows();i++){
					for(int j=0;j<sheet.getColumns();j++){
						Cell cell = sheet.getCell(j, i) ;
						temp[i][j] = Integer.parseInt(cell.getContents()) ;
						System.out.print(temp[i][j]+"\t") ;
					}
					System.out.println() ;
				}
				book.close() ;
			}catch(Exception e){
				System.out.println(e) ;
			}
		}
	}

### 从Excel文件中读取数据到二维数组

	public class ReadExcelToTwoDimensionalArray {
		public static void main(String args[]){
			try{
				File file = new File("G:\\文档文件\\临时文件\\时间表2.xls") ;
				Workbook book = Workbook.getWorkbook(file) ;
				Sheet sheet = book.getSheet(0) ;
				int[][] temp = new int[sheet.getRows()][sheet.getColumns()] ;
				for(int i=0;i<sheet.getRows();i++){
					for(int j=0;j<sheet.getColumns();j++){
						Cell cell = sheet.getCell(j, i) ;
						temp[i][j] = Integer.parseInt(cell.getContents()) ;
						System.out.print(temp[i][j]+"\t") ;
					}
					System.out.println() ;
				}
				book.close() ;
			}catch(Exception e){
				System.out.println(e) ;
			}
		}
	}
