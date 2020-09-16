import com.ajax.brain.utils.UnsortedArrayException;
import com.ajax.brain.utils.sorts.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class SortsTest {
    static final Integer[] ints = new Integer[10000];
    static final Integer[] semiSorted;

    static {
        var x = Integer.MAX_VALUE;
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ThreadLocalRandom.current().nextInt(x);
        }

        semiSorted = ints.clone();

        QuickSort.sort(semiSorted, Integer::intValue);

        Integer temp = semiSorted[0];
        semiSorted[0] = semiSorted[semiSorted.length-1];
        semiSorted[semiSorted.length-1] = temp;
    }

    @Test
    public void testSort() {
        timeFullArraySort(QuickSort::getInstance);
        timeFullArraySort(MergeSort::getInstance);
        timeFullArraySort(BubbleSort::getInstance);
        timeFullArraySort(InsertionSort::getInstance);
        timeFullArraySort(RadixSort::getInstance);
        timeFullArraySort(TreeSort::getInstance);

        System.out.println();

        timeSingleArraySort(QuickSort::getInstance);
        timeSingleArraySort(MergeSort::getInstance);
        timeSingleArraySort(BubbleSort::getInstance);
        timeSingleArraySort(InsertionSort::getInstance);
        timeSingleArraySort(RadixSort::getInstance);
        timeSingleArraySort(TreeSort::getInstance);
    }

    public static void timeFullArraySort(Function<Representor<Integer>, Sort<Integer>> makeSort) {
        Sort<Integer> sort = makeSort.apply(Integer::intValue);
        String sortName = sort.getClass().getSimpleName();

        Integer[] unsorted3 = ints.clone();
        long b4_3 = System.currentTimeMillis();
        sort.sort(unsorted3);
        long af_3 = System.currentTimeMillis();
        System.out.println(sortName + " time taken: " + (af_3-b4_3) + ", sorted? " + (checkIfSorted(Integer::intValue, unsorted3)?"yes":"no"));
    }

    public static void timeSingleArraySort(Function<Representor<Integer>, Sort<Integer>> makeSort) {
        Sort<Integer> sort = makeSort.apply(Integer::intValue);
        String sortName = sort.getClass().getSimpleName();

        long totalTime = 0;

        Integer[] semiTemp = semiSorted.clone();
        long b4 = System.currentTimeMillis();
        sort.sort(semiTemp);
        long af = System.currentTimeMillis();

        totalTime += af-b4;
        if(!checkIfSorted(Integer::intValue, semiTemp))
                throw new UnsortedArrayException("Array should be sorted");

        System.out.println("Single element sort, " + sortName + " time taken: " + totalTime);
    }

    public static <T> boolean checkIfSorted(Representor<T> representor, T[] arr) {
        for(int x = 1; x < arr.length; x++) {
            if(representor.convert(arr[x-1]) > representor.convert(arr[x])) {
                System.out.printf("Unsorted array: arr[%d]=%s > arr[%d]=%s\n", x-1, arr[x-1].toString(), x, arr[x].toString());
                System.out.print("Full array: ");
                System.out.println(Arrays.toString(arr));
                return false;
            }
        }
        return true;
    }
}
