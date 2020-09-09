package com.ajax.brain.linguist;

import java.io.*;
import java.util.Iterator;

/**
 * Most languages have a structured format but lexing may still be necessary to fully understand a sentence
 */
public class Lexer implements Iterable<Token>{

    private final byte[] buffer;
    private final MatcherList matchers;
    private final InputStream inputStream;
    private int bytesLeft;

    public Lexer(String text, MatcherList matcherList) {
        this.matchers = matcherList;
        this.buffer = text.getBytes();
        this.inputStream = null;
    }

    public Lexer(InputStream inputStream, MatcherList matcherList) {
        this(inputStream, matcherList, 1000);
    }

    public Lexer(File file, MatcherList matcherList) throws FileNotFoundException {
        this(new FileInputStream(file), matcherList, 1000);
    }

    public Lexer(InputStream inputStream, MatcherList matcherList, int bufferSize) {
        this.matchers = matcherList;
        this.inputStream = inputStream;
        this.buffer = new byte[bufferSize];
    }

    @Override
    public Iterator<Token> iterator() {
        return null;
    }

    /**
     * Used for lexing the data in the array
     */
    private void lex() {

    }

    /**
     * Refills the buffer with new data from the {@code InputStream}
     * @return {@code true} if any data was read in
     * @throws IOException see {@link java.io.InputStream#read(byte[])}
     *
     * @see java.io.InputStream#read(byte[])
     */
    private boolean readMore() throws IOException {
        if(inputStream != null) {
            try {
                bytesLeft = inputStream.read(buffer);
                if(bytesLeft != -1)
                    return true;
            } catch (EOFException e) {
                return false;
            }
        }
        return false;
    }
}
