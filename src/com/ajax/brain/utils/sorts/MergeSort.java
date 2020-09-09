package com.ajax.brain.utils.sorts;

import java.lang.reflect.Array;

/**
 * Implementation of a MergeSort algorithm
 *
 * Code <del>stolen</del> borrowed from <a href="https://www.geeksforgeeks.org/">https://www.geeksforgeeks.org/</a>
 * The page it was borrowed form is <a href="https://www.geeksforgeeks.org/merge-sort/">https://www.geeksforgeeks.org/merge-sort/</a>
 *
 * MergeSorts should only be used if memory is not a problem
 */
public final class MergeSort<T> extends Sort<T>{
    /**
     * Creates a new MergeSort with the representor
     *
     * @param representor the representor for converting objects to integers\
     * @throws NullPointerException if the representor is null
     */
    private MergeSort(Representor<T> representor) {
        super(representor);
    }

    /**
     * Sorts the provided array with a merge sort
     * @param arr the array to sort
     */
    @Override
    public void sort(T[] arr) {
        sort(arr, 0, arr.length-1);
    }

    /**
     * Separates the array, sorts it, then merges them
     *
     * @param arr the array to sort
     * @param l the left index
     * @param m the middle index
     * @param r the right index
     */
    private void merge(T[] arr, int l, int m, int r) {
        // Find sizes of two sub-arrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        T[] L = newArr(arr, n1);
        T[] R= newArr(arr, n2);

        /*Copy data to temp arrays*/
        if (n1 >= 0) System.arraycopy(arr, l, L, 0, n1);
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays */

        // Initial indexes of first and second sub-arrays
        int i = 0, j = 0;

        // Initial index of merged sub-array array
        int k = l;
        while (i < n1 && j < n2) {
            if (representor.convert(L[i]) <= representor.convert(R[j])) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    /**
     * Creates new array based on the provided one
     * @param arr the base array
     * @param length the length of the new one
     * @param <T> the type of the array
     * @return the new array
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] newArr(T[] arr, int length) {
        return (T[]) Array.newInstance(arr.getClass().getComponentType(), length);
    }

    /**
     * Sorts the provided array from left to right
     * @param arr the array to sort
     * @param l the left index
     * @param r the right index
     */
    private void sort(T[] arr, int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    /**
     * Sorts the provided array by constructing a new QuickSort Object then using it
     * @param arr the array to sort
     * @param representor the representor the the provided object type
     * @param <T> the object type of the array
     */
    public static <T> void sort(T[] arr, Representor<T> representor) {
        new MergeSort<T>(representor).sort(arr);
    }

    /**
     * Returns a new QuickSort Object to be used multiple times
     * @param representor the representor the the provided object type
     * @param <T> the object type of the array
     */
    public static <T> MergeSort<T> getInstance(Representor<T> representor) {
        return new MergeSort<>(representor);
    }
}
