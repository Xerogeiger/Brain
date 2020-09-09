package com.ajax.brain.utils.sorts;

/**
 * Represents an object as a {@code Integer}
 *
 * It is recommended that any value where calculating the {@code Integer} takes time to store it
 */
@FunctionalInterface
public interface Representor<T> {
    /**
     * Convert the provided object to an integer representing it
     * @param obj the object to convert
     * @return the integer representing it
     */
    int convert(T obj);
}
