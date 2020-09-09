package com.ajax.brain.utils;

/**
 * Used to convert objects to a string
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
