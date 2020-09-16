package com.ajax.brain.utils;

public class UnsortedArrayException extends RuntimeException{

    /**
     * {@inheritDoc}
     */
    public UnsortedArrayException() {
    }

    /**
     * {@inheritDoc}
     */
    public UnsortedArrayException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public UnsortedArrayException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public UnsortedArrayException(Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    public UnsortedArrayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
