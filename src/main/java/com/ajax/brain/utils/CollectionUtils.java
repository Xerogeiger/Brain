package com.ajax.brain.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * A class for Collection utilities not in {@link java.util.Collections}
 */
public final class CollectionUtils {
    /**
     * Don't let anyone instantiate this class
     */
    private CollectionUtils() {}

    /**
     * Converts an collection of generic objects to a {@code String}
     *
     * @param collection the collection to convert
     * @param stringy the method of converting the elements to a {@code String}
     * @return A string of the collection
     */
    public static <T> String toString(Collection<T> collection, ToString<T> stringy) {
        if (collection == null)
            return "null";

        int iMax = collection.size() - 1;
        if (iMax == -1)
            return "[]";

        Iterator<T> itr = collection.iterator();

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(stringy.toString(itr.next()));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
