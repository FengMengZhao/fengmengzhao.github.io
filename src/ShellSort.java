import java.util.Random ;

public class ShellSort{

    static void shellSort(int[] array, int lo, int hi){
        int current ;
        int pos ;
        int insert_value ;
        int h = 1 ;
        while(h < hi)
            h = h * 3 + 1 ;

        while(h > 0){
            for(current = h; current < hi && current >= lo; current ++){
                insert_value = array[current] ;
                pos = current ;
                while(pos > h - 1 && insert_value < array[pos - h]){
                    array[pos] = array[pos - h] ;
                    pos -= h ;
                }
                array[pos] = insert_value ;
            }
            h = (h - 1) / 3 ;
        }
    }

    public static void main(String args[]){
        Random rnd = new Random() ;
        int[] array = new int[20] ;
        for(int i = 0; i < array.length; i ++)
            array[i] = rnd.nextInt(10) ;
        shellSort(array, 0, array.length) ;
        for(int i : array)
            System.out.print(i + ",") ;
    }

}
