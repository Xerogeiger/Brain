import com.ajax.brain.utils.sorts.MergeSort;
import com.ajax.brain.utils.sorts.QuickSort;
import com.ajax.brain.utils.sorts.Representor;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Is worrying about a quicksort actually worth it
 */
public class SortTimeTest {
    public static void main(String[] args) {
        int high = 1000;
        int low = -1000;
        int iterations = 200;
        Integer[] unsorted = new Integer[4000];

        for(int x = 0; x < unsorted.length; x++) {
            unsorted[x] = ThreadLocalRandom.current().nextInt(low, high + 1);
        }

        testQuickSort(iterations, unsorted);
        testMergeSort(iterations, unsorted);
    }

    public static void testQuickSort(int iterations, Integer[] array) {
        Integer[] toSort = null;
        long totalTimeTaken = 0;
        for(int x = 0; x < iterations; x++) {
            toSort = Arrays.copyOf(array, array.length);
            long b4 = System.currentTimeMillis();
            QuickSort.sort(toSort, i -> i);
            totalTimeTaken += (System.currentTimeMillis()-b4);
        }

        assert toSort != null;
        checkIfSorted(i -> i,toSort);

        System.out.println("Array before QuickSort: " + Arrays.toString(array));
        System.out.println("Array after QuickSort: " + Arrays.toString(toSort));
        System.out.println("Time for QuickSort: " + totalTimeTaken);
    }

    public static void testMergeSort(int iterations, Integer[] array) {
        Integer[] toSort = null;
        long totalTimeTaken = 0;
        for(int x = 0; x < iterations; x++) {
            toSort = Arrays.copyOf(array, array.length);
            long b4 = System.currentTimeMillis();
            MergeSort.sort(toSort, i -> i);
            totalTimeTaken += (System.currentTimeMillis()-b4);
        }

        assert toSort != null;
        checkIfSorted(i -> i,toSort);

        System.out.println("Array before MergeSort: " + Arrays.toString(array));
        System.out.println("Array after MergeSort: " + Arrays.toString(toSort));
        System.out.println("Time for MergeSort: " + totalTimeTaken);
    }

    public static <T> void checkIfSorted(Representor<T> representor, T[] arr) {
        for(int x = 1; x < arr.length; x++) {
            if(representor.convert(arr[x-1]) > representor.convert(arr[x])) {
                throw new RuntimeException("Array not sorted");
            }
        }
    }
}
