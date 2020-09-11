package com.ajax.brain.utils.sorts;

/**
 * Implementation of a QuickSort algorithm
 *
 * Code <del>stolen</del> borrowed from <a href="https://www.geeksforgeeks.org/">https://www.geeksforgeeks.org/</a>
 * The page it was borrowed form is <a href="https://www.geeksforgeeks.org/quick-sort/">https://www.geeksforgeeks.org/quick-sort/</a>
 *
 * QuickSorts should be used for smaller data-sets not gigantic ones
 */
public final class QuickSort<T> extends Sort<T>{
    /**
     * Creates a new {@code QuickSort} object with the {@code Representor}
     * @param representor the {@code Representor} for the objects
     * @throws NullPointerException if the representor is null
     */
    private QuickSort(Representor<T> representor) {
        super(representor);
    }

    /**
     * Sorts from the low to the high based on the pivot
     *
     * @param arr the array of values
     * @param low the low index
     * @param high the high index
     * @return the partitioning index
     */
    private int partition(T[] arr, int low, int high) {
        int pivot = representor.convert(arr[high]);
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++) {
            // If current element is smaller than the pivot
            if (representor.convert(arr[j]) < pivot)
            {
                i++;

                // swap arr[i] and arr[j]
                T temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        T temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }

    /**
     * Partitions the array to sort it
     *
     * @param arr the array of values
     * @param low the low of the array
     * @param high the high of the array
     */
    private void sort(T[] arr, int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }

    /**
     * Sorts the given array using a QuickSort
     *
     * @param arr the array to sort
     * @throws NullPointerException if the array is null
     */
    @Override
    public void sort(T[] arr) {
        if(arr == null) {
            throw new NullPointerException("The array can not be null");
        }

        sort(arr, 0, arr.length-1);
    }

    /**
     * Sorts the provided array by constructing a new {@code QuickSort} object then using it
     * @param arr the array to sort
     * @param representor the {@code Representor} for the provided object type
     * @param <T> the object type of the array
     */
    public static <T> void sort(T[] arr, Representor<T> representor) {
        new QuickSort<T>(representor).sort(arr);
    }

    /**
     * Returns a new {@code QuickSort} object to be used multiple times
     * @param representor the {@code Representor} for the provided object type
     * @param <T> the object type of the array
     */
    public static <T> QuickSort<T> getInstance(Representor<T> representor) {
        return new QuickSort<>(representor);
    }
}
