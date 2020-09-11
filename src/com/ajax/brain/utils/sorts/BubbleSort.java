package com.ajax.brain.utils.sorts;

/**
 * Implementation of a BubbleSort algorithm
 *
 * Code <del>stolen</del> borrowed from <a href="https://www.geeksforgeeks.org/">https://www.geeksforgeeks.org/</a>
 * The page it was borrowed form is <a href="https://www.geeksforgeeks.org/bubble-sort/">https://www.geeksforgeeks.org/bubble-sort/</a>
 *
 * MergeSorts should only be used if memory is not a problem
 */
public final class BubbleSort<T> extends Sort<T>{
    /**
     * Creates a new sort with the representor
     *
     * @param representor the representor for converting objects to integers\
     * @throws NullPointerException if the representor is null
     */
    private BubbleSort(Representor<T> representor) {
        super(representor);
    }

    /**
     * Sorts the provided array with a BubbleSort
     * @param arr the array to sort
     */
    @Override
    public void sort(T[] arr) {
        int n = arr.length;

        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (representor.convert(arr[j]) > representor.convert(arr[j + 1])) {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Sorts the provided array by constructing a new {@code BubbleSort} Object then using it
     * @param arr the array to sort
     * @param representor the representor the the provided object type
     * @param <T> the object type of the array
     */
    public static <T> void sort(T[] arr, Representor<T> representor) {
        new BubbleSort<T>(representor).sort(arr);
    }

    /**
     * Returns a new {@code BubbleSort} Object to be used multiple times
     * @param representor the representor the the provided object type
     * @param <T> the object type of the array
     */
    public static <T> BubbleSort<T> getInstance(Representor<T> representor) {
        return new BubbleSort<>(representor);
    }
}
