package com.ajax.brain.utils.sorts;

public abstract class Sort<T> {
    protected final Representor<T> representor;

    /**
     * Creates a new {@code Sort} with the {@code Representor}
     *
     * @param representor the {@code Representor} for converting objects to integers
     * @throws NullPointerException if the representor is null
     */
    protected Sort(Representor<T> representor) {
        if(representor == null) {
            throw new NullPointerException("Representor must not be null");
        }

        this.representor = representor;
    }

    /**
     * Sorts the provided array
     * Sometimes this is just an accessor for Sorts that use recursion
     *
     * @param arr the array to sort
     */
    public abstract void sort(T[] arr);

    /**
     * Returns the sored {@code Representor}
     *
     * @return the {@code Representor} of the sort
     */
    public Representor<T> getRepresentor() {
        return representor;
    }
}
