package com.ajax.brain.utils.sorts;

public abstract class Sort<T> {
    protected final Representor<T> representor;

    /**
     * Creates a new sort with the representor
     * @param representor the representor for converting objects to integers\
     * @throws NullPointerException if the representor is null
     */
    public Sort(Representor<T> representor) {
        if(representor == null) {
            throw new NullPointerException("Representor must not be null");
        }

        this.representor = representor;
    }

    /**
     * Sorts the provided array
     * @param arr the array to sort
     */
    public abstract void sort(T[] arr);

    /**
     * Returns the sored representor
     * @return the representor of the sort
     */
    public Representor<T> getRepresentor() {
        return representor;
    }
}
