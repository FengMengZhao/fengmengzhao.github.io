import java.util.Random ;

public class QuickSort{

    protected static void swap(int[] array, int first, int last){
        int temp = array[first] ;
        array[first] = array[last] ;
        array[last] = temp ;
    }

    protected static void medianOf3(int[] array, int lo, int hi){
        int mid = (lo + hi) >> 1 ;
        if(array[lo] > array[mid])
            swap(array, lo, mid) ;
        if(array[mid] > array[hi-1])
            swap(array, mid, hi-1) ;
        if(array[lo] > array[mid])
            swap(array, lo, mid) ;
    }

    protected static int partition(int[] array, int lo, int hi){
        int mid = (lo + hi) / 2 ;
        int pivot = array[mid] ;
        while(true){
            while(array[++ lo] < pivot) ;
            while(array[-- hi - 1] > pivot) ;
            if(lo > hi - 1)
                break ;
            else
                swap(array, lo, hi - 1) ;
        }
        return lo ;
    }

    static void quickSort(int[] array, int lo, int hi){
        if(lo - hi <= 10)
            InsertionSort.insertionSort_v2(array, lo, hi) ;
        else{
            medianOf3(array, lo, hi) ;
            int left_prt = partition(array, lo, hi) ;
            quickSort(array, lo, left_prt) ;
            quickSort(array, left_prt + 1, hi - 1) ;
        }
    }

    public static void main(String args[]){
        Random rnd = new Random() ;
        int[] array = new int[50] ;
        for(int i = 0; i < array.length; i ++)
            array[i] = rnd.nextInt(10) ;
        quickSort(array, 0, array.length) ;
        for(int i : array)
            System.out.print(i + ",") ;
    }
}
