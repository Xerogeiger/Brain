package com.ajax.brain.utils;

/**
 * A class for Array utilities missing from {@link java.util.Arrays}
 */
public final class ArrayUtils {
    /**
     * Don't let anyone instantiate this class
     */
    private ArrayUtils() {}

    /**
     * Converts an array of generic objects to a {@code String}
     *
     * @param arr the array to convert
     * @param stringy the method of converting the elements to a {@code String}
     * @return A string of the array
     */
    public static <T> String toString(T[] arr, ToString<T> stringy) {
        if ( arr== null)
            return "null";

        int iMax = arr.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(stringy.toString(arr[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
