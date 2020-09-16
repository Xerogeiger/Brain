package com.ajax.brain.utils.sorts;

import java.util.Comparator;

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

    /**
     * Creates a {@code Comparator} from the {@code Representor}
     *
     * @param representor the {@code Representor}
     * @return the new comparator
     */
    static <T> Comparator<T> toComparator(Representor<T> representor) {
        return Comparator.comparingInt(representor::convert);
    }
}
