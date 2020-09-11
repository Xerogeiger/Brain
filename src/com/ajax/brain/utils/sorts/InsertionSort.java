package com.ajax.brain.utils.sorts;

/**
 * Implementation of a InsertionSort algorithm
 *
 * Code <del>stolen</del> borrowed from <a href="https://www.geeksforgeeks.org/">https://www.geeksforgeeks.org/</a>
 * The page it was borrowed form is <a href="https://www.geeksforgeeks.org/insertion-sort/">https://www.geeksforgeeks.org/insertion-sort/</a>
 */
public final class InsertionSort<T> extends Sort<T> {
    /**
     * Creates a new sort with the representor
     *
     * @param representor the representor for converting objects to integers\
     * @throws NullPointerException if the representor is null
     */
    private InsertionSort(Representor<T> representor) {
        super(representor);
    }

    /**
     * Sorts the provided array with an InsertionSort
     *
     * @param arr the array to sort
     */
    @Override
    public void sort(T[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            T temp = arr[i];
            int key = representor.convert(temp);
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && representor.convert(arr[j]) > key) {
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = temp;
        }
    }

    /**
     * Sorts the provided array by constructing a new {@code InsertionSort} Object then using it
     * @param arr the array to sort
     * @param representor the representor the the provided object type
     * @param <T> the object type of the array
     */
    public static <T> void sort(T[] arr, Representor<T> representor) {
        new InsertionSort<T>(representor).sort(arr);
    }

    /**
     * Returns a new {@code InsertionSort} Object to be used multiple times
     * @param representor the representor the the provided object type
     * @param <T> the object type of the array
     */
    public static <T> InsertionSort<T> getInstance(Representor<T> representor) {
        return new InsertionSort<>(representor);
    }
}
