import java.util.Random ;
import org.fmz.container.Vector ;
import org.fmz.container.FixedVector ;
import org.fmz.container.MaxHeap ;

public class HeapSort{

    static void heapSort(Vector vec){
        MaxHeap temp = new MaxHeap(vec.size()) ;
        for(int i=0; i<vec.size(); i++)
            temp.insert((Comparable)vec.elementAt(i)) ;
        for(int i=vec.size()-1; i>=0; i--)
            vec.replace(i, temp.removeMax()) ;
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
        

        heapSort(fvec) ;
        for(int i=0; i<fvec.size(); i++)
            System.out.print(fvec.elementAt(i) + "\t") ;
    }
}
