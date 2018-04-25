import java.util.Random ;

public class BubbleSort{

    /*
     * This algorithm is not optimal, because we compare the element which has already ordered
     * when the sequence is ordered, it terminate. We know this from the boolean sorted
     */
    static void bubbleSort(int[] array){
        boolean sorted = false ;
        while(sorted = !sorted){
            for(int i=0; i<array.length-1; i++){
                if(array[i] > array[i+1]){
                   swap(array, i, i+1) ;
                   sorted = false ;
                }
            }
        }
    }

    /*
     * This algorithm for bubble sort is optimal, because we will not compare the element which has already ordered
     * The abrove method also can be revised by assign a new variable hi=array.length then hi --
     */
    static void bubbleSortRevised(int[] array, int lo, int hi){
        boolean sorted = false ;
        while(sorted = !sorted){
            for(int i=lo; i<hi-1; i++){
                if(array[i] > array[i+1]){
                    swap(array, i, i+1) ;
                    sorted = false ;
                }
            }
            hi -- ;
        }
    }
    
    /*
     * This is the revised version for method bubbleSort()
     */
    static void bubbleSort2(int[] array){
        boolean sorted = false ;
        int hi = array.length ;
        while(sorted = !sorted){
            for(int i=0; i<hi-1; i++){
                if(array[i] > array[i+1]){
                   swap(array, i, i+1) ;
                   sorted = false ;
                }
            }
            hi -- ;
        }
    }

    static void swap(int[] array, int first, int last){
        int temp = array[first] ;
        array[first] = array[last] ;
        array[last] = temp ;
    }

    public static void main(String args[]){
        Random rnd = new Random(26) ;
        int[] array = new int[10] ;
        for(int i=0; i<array.length; i++){
            array[i] = rnd.nextInt(10) ;
        }
        bubbleSort(array) ;
        //bubbleSortRevised(array, 0, array.length) ;
        for(int i : array)
            System.out.print(i + "\t") ;
    }

}
