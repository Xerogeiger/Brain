package com.ajax.brain.utils;

/**
 * Used to convert objects to a string
 *
 * the normal toString method isn't always useful this interface isn't used for printing it's used for converting
 */
@FunctionalInterface
public interface ToString<T> {
    /**
     * Convert the passed object to a string
     *
     * @param t the object to convert
     * @return the string from the object
     */
    String toString(T t);

    @Deprecated
    String toString(); //You probably mean to call toString(T) because this one does nothing
}
