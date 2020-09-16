package com.ajax.brain.linguist;

/**
 * When an exception occurs during lexing
 */
public class WordLexException extends Exception{

    /**
     * {@inheritDoc}
     */
    public WordLexException() {
    }

    /**
     * {@inheritDoc}
     */
    public WordLexException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public WordLexException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public WordLexException(Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    public WordLexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
