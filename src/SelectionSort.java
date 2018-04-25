import java.util.Random ;
import org.fmz.container.Vector ;
import org.fmz.container.FixedVector ;

public class SelectionSort{

    static void selectionSort(Vector vec){
        int current ; // point to which selection processing(we need process n-1 times)
        int pos ; // in each selection iteation, point to which element we will compare
        int small_pos ; // in each selection iteration, store the smallest element position
        Comparable smallest ; // in each selection iteration, store the smallest element 
        int n = vec.size() ;

        for(current=0; current<n-1; current++){
            small_pos = current ;
            smallest = (Comparable)vec.elementAt(small_pos) ;
            for(pos=current+1; pos<n; pos++){
                if(((Comparable)vec.elementAt(pos)).compareTo(smallest) < 0){
                    small_pos = pos ;
                    smallest = (Comparable)vec.elementAt(pos) ;
                }
            }
            if(small_pos != current)
                swap(vec, current, small_pos) ;
        }    
    }

    static void swap(Vector vec, int first, int last){
        Object temp = vec.elementAt(first) ;
        vec.replace(first, vec.elementAt(last)) ;
        vec.replace(last, temp) ;
    }

    public static void main(String args[]){
        Random rnd = new Random() ;
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

        selectionSort(fvec) ;
        for(int i=0; i<fvec.size(); i++)
            System.out.print(fvec.elementAt(i) + "\t") ;
    }
}
