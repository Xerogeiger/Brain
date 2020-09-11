package com.ajax.brain.utils.sorts;

import com.ajax.brain.utils.Generics;

/**
 * Implementation of a RadixSort algorithm
 *
 * Code <del>stolen</del> borrowed from <a href="https://www.geeksforgeeks.org/">https://www.geeksforgeeks.org/</a>
 * The page it was borrowed form is <a href="https://www.geeksforgeeks.org/radix-sort/">https://www.geeksforgeeks.org/radix-sort/</a>
 */
public final class RadixSort<T> extends Sort<T>{
    /**
     * Creates a new {@code RadixSort} with the {@code Representor}
     *
     * @param representor the {@code Representor} for converting objects to integers
     * @throws NullPointerException if the representor is null
     */
    private RadixSort(Representor<T> representor) {
        super(representor);
    }

    /**
     * RadixSort utility method
     *
     * Returns the largest value from the array
     * @param arr the array that is being sorted
     * @return the largest value in the array
     */
    private int getMax(T[] arr) {
        int mx = representor.convert(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            int val = representor.convert(arr[i]);
            if (val > mx) {
                mx = val;
            }
        }
        return mx;
    }

    /**
     * RadixSort utility method
     * Does the actual sorting
     *
     * @param arr the array to sort
     * @param exp the current exponent 10^exp
     */
    private void countSort(T[] arr, int exp) {
        T[] output = Generics.newGenericArray(arr, arr.length);
        int[] count = new int[10];

        for (T t : arr)
            count[(representor.convert(t) / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i-1];

        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[(representor.convert(arr[i])/exp)%10] - 1] = arr[i];
            count[(representor.convert(arr[i])/exp)%10]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    /**
     * Sorts the provided array with a RadixSort
     *
     * @param arr the array to sort
     */
    @Override
    public void sort(T[] arr) {
        int m = getMax(arr);

        for (int exp = 1; m/exp > 0; exp *= 10) {
            countSort(arr, exp);
        }
    }

    /**
     * Sorts the provided array by constructing a new {@code RadixSort} Object then using it
     * @param arr the array to sort
     * @param representor the representor for the provided object type
     * @param <T> the object type of the array
     */
    public static <T> void sort(T[] arr, Representor<T> representor) {
        new RadixSort<T>(representor).sort(arr);
    }

    /**
     * Returns a new {@code RadixSort} Object to be used multiple times
     * @param representor the representor for the provided object type
     * @param <T> the object type of the array
     */
    public static <T> RadixSort<T> getInstance(Representor<T> representor) {
        return new RadixSort<>(representor);
    }
}
