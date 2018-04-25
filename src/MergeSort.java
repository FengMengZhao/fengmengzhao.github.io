import java.util.Random ;

public class MergeSort{

    static void mergeSort(int[] array, int lo, int hi){
        if(lo+1 == hi)
            return ;
        int mid = (lo + hi)/ 2 ;
        mergeSort(array, lo, mid) ;
        mergeSort(array, mid, hi) ;
        merge(array, lo, mid, hi) ;
    }

    static void merge(int[] array, int lo, int mid, int hi){
        int[] array_lt = new int[mid - lo + 1] ;
        int[] array_rt = new int[hi - mid + 1] ;
        for(int i = 0; i < array_lt.length-1; i ++)
            array_lt[i] = array[lo + i] ;
        for(int i = 0; i < array_rt.length-1; i ++)
            array_rt[i] = array[mid + i] ;
        array_lt[array_lt.length-1] = Integer.MAX_VALUE ;
        array_rt[array_rt.length-1] = Integer.MAX_VALUE ;
        int i = 0 ;
        int j = 0 ;
        for(int k = lo; k < hi; k ++){
            if(array_lt[i] < array_rt[j]){
                array[k] = array_lt[i] ;
                i ++ ;
            }else{
                array[k] = array_rt[j] ;
                j ++ ;
            }
        }
    }

    public static void main(String args[]){
        Random rnd = new Random() ;
        int[] array = new int[10] ;
        for(int i = 0; i < array.length; i ++)
            array[i] = rnd.nextInt(10) ;
        mergeSort(array, 0, array.length) ;
        for(int i : array)
            System.out.print(i + "\t") ;
    }
}
