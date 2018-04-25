import java.util.* ;
import org.fmz.container.Vector ;
import org.fmz.container.FixedVector ;

public class InsertionSort{
    
    static void insertionSort(Vector vec){
        int current ;
        int pos ;
        int n = vec.size() ;
        for(current=1; current<n; current++){
            pos = current ;
            while(pos>0 && ((Comparable)vec.elementAt(current)).compareTo(vec.elementAt(pos-1)) < 0)
                pos -- ;
            if(pos != current){
                vec.insertAt(pos, vec.elementAt(current)) ;
                vec.removeAt(current+1) ;
             }
        }
    }

    public static void insertionSort_v2(int[] array, int lo, int hi){
        int current ;
        int pos ;
        int insert_value ;
        for(current = lo + 1; current < hi; current ++){
            pos = current - 1 ;
            insert_value = array[current] ;
            while(pos >= lo && insert_value < array[pos]){
                array[pos + 1] = array[pos] ;
                pos -- ;
            }
            array[pos + 1]  = insert_value ;
        }
    }

    public static void main(String args[]){
        Random rnd = new Random() ;
        /*
        FixedVector fvec = new FixedVector() ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        fvec.append(rnd.nextInt(10)) ;
        */
        int[] array = new int[10] ;
        for(int i = 0; i<array.length; i++)
            array[i] = rnd.nextInt(10) ;
        //insertionSort(fvec) ;
        insertionSort_v2(array, 0, array.length) ;
        for(int i : array)
            System.out.print(i + "\t") ;
    }
}

