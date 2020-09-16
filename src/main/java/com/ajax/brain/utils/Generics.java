package com.ajax.brain.utils;

import java.lang.reflect.Array;

/**
 * Utility class to take the warnings because @SupressWarnings() shouldn't be used for very much 
 * it mostly hides existing problems so directing them to this class allows easy ways of changing 
 * to methods that don't throw warnings or at least keep all of them here
 */
public final class Generics {
    /**
     * Don't let anyone instantiate this class
     */
    private Generics() {}

    /**
     * Creates new array based on the provided one
     *
     * A necessary evil for my generic sorts that use arrays to separate values
     *
     * @param arr the base array
     * @param length the length of the new one
     * @param <T> the type of the array
     * @return the new array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newGenericArray(T[] arr, int length) {
        return (T[]) Array.newInstance(arr.getClass().getComponentType(), length);
    }
}
