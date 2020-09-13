package com.ajax.brain.linguist;

import java.io.*;
import java.util.*;

/**
 * Most languages have a structured format but lexing may still be necessary to fully understand a sentence
 */
public class Lexer implements Iterable<Token> {
    /**
     * Common 1-character {@code String}s to save time later
     */
    private static final HashMap<Byte, String> COMMON_TOKENS = new HashMap<>();

    static {
        //Insert all of the common 1-byte token values
        for(int x = Byte.MIN_VALUE; x < Byte.MAX_VALUE; x++) {
            byte b = (byte) x;

            COMMON_TOKENS.put(b, String.valueOf((char)x));
        }
    }

    /**
     * The data buffer for the lexer
     */
    private final byte[] buffer;

    /**
     * The matchers to use
     */
    private final MatcherList matchers;

    /**
     * The {@code InputStream} to read from
     */
    private final InputStream inputStream;

    /**
     * Matches whitespace for the lexer
     * This one is more statically defined because it dictates how the lexer separates words
     */
    private final Matcher whitespaceMatcher;

    /**
     * The list of tokens read by the lexer
     */
    private final ArrayList<Token> tokenList;

    /**
     * The current index of the lexer in the buffer
     */
    private int bufferIndex;

    /**
     * Creates a lexer for the provided {@code String}
     *
     * @param text the {@code String} for the lexer
     * @param matcherList the list of {@code Matcher}s to use for creating tokens
     */
    public Lexer(String text, MatcherList matcherList) {
        this.whitespaceMatcher = Matcher.getWhitespaceMatcher();
        this.buffer = text.getBytes();
        this.matchers = matcherList;
        this.tokenList = new ArrayList<>();

        this.inputStream = null;
        this.bufferIndex = 0;
    }

    /**
     * Creates a {@code Lexer} from the provided {@code InputStream} and {@code MatcherList}
     *
     * @param inputStream the {@code InputStream} to read data from
     * @param matcherList the list of {@code Matcher}s to use for creating tokens
     */
    public Lexer(InputStream inputStream, MatcherList matcherList) {
        this(inputStream, Matcher.getWhitespaceMatcher(), matcherList, 1000);
    }

    /**
     * Creates a {@code Lexer} from the provided {@code File} and {@code MatcherList}
     *
     * @param file the file to read from
     * @param matcherList the list of {@code Matcher}s to use for creating tokens
     * @throws FileNotFoundException if the file provided does not exist
     */
    public Lexer(File file, MatcherList matcherList) throws FileNotFoundException {
        this(new FileInputStream(file), Matcher.getWhitespaceMatcher(), matcherList, 1000);
    }

    /**
     * Creates a {@code Lexer} from the provided {@code File}, {@code MatcherList}, and buffer size
     *
     * @param inputStream the {@code InputStream} to read data from
     * @param matcherList the list of {@code Matcher}s to use for creating tokens
     * @param bufferSize the size to make the data buffer
     */
    public Lexer(InputStream inputStream, MatcherList matcherList, int bufferSize) {
        this(inputStream, Matcher.getWhitespaceMatcher(), matcherList, bufferSize);
    }

    /**
     * Creates a {@code Lexer} from the provided {@code File}, {@code MatcherList}, whitespace {@code Matcher}, and buffer size
     * The whitespace {@code Matcher} is a {@code Matcher} used for determining the end of relevant word tokens and ignoring certain characters
     *
     * @param inputStream the {@code InputStream} to read the data from
     * @param whitespaceMatcher the {@code Matcher} to use for matching whitespace
     * @param matcherList the list of {@code Matcher}s to use for creating tokens
     * @param bufferSize the size to make the data buffer
     * @throws IllegalArgumentException is the whitespace {@code Matcher} is not a character {@code Matcher}
     */
    public Lexer(InputStream inputStream, Matcher whitespaceMatcher, MatcherList matcherList, int bufferSize) {
        if(!whitespaceMatcher.isCharacterMatch())
            throw new IllegalArgumentException("The whitespace matcher should match characters");

        this.matchers = matcherList;
        this.inputStream = inputStream;
        this.buffer = new byte[bufferSize];
        this.whitespaceMatcher = whitespaceMatcher;
        this.tokenList = new ArrayList<>();

        this.bufferIndex = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Token> iterator() {
        return new LexerIterator();
    }

    /**
     * Closes the stored {@code InputStream}
     *
     * @throws IOException see {@link InputStream#close()}
     * @throws NullPointerException if the {@code InputStream} is {@code null}
     */
    public void dispose() throws IOException {
        inputStream.close();
    }

    /**
     * Used for lexing the data in the array
     */
    public void lex() throws IOException {
        byte[] wordBuffer = new byte[100];
        int wbIndex = 0;

        MainLoop:
        while(true) {
            if(buffer.length == bufferIndex)
                readMore();
            if(bufferIndex == -1)
                break;

            byte b = nextByte();
            String value = COMMON_TOKENS.get(b);
            if(value == null)
                throw new Error("Unexpected null value from list of every possible value");

            for(Matcher matcher: matchers) { //Look for character matches
                if(matcher.isCharacterMatch() && matcher.match(value)) {
                    createWordToken(wordBuffer, wbIndex);
                    wbIndex = 0;
                    createToken(matcher, value);
                    continue MainLoop;
                }
            }

            if(whitespaceMatcher.match(value)) {
                createWordToken(wordBuffer, wbIndex);
                wbIndex = 0;
                continue;
            }

            wordBuffer[wbIndex++] = b;
        }

        createWordToken(wordBuffer, wbIndex); //Make sure that no words are left behind
    }

    /**
     * Creates a token based on the provided buffer using the sored matchers
     *
     * @param buffer the buffer holding the word
     * @param len the length of the word in the buffer
     */
    private void createWordToken(byte[] buffer, int len) {
        if(len != 0) {
            String value = new String(buffer, 0, len);

            for(Matcher matcher: matchers) { //Look for character matches
                if(matcher.match(value)) {
                    tokenList.add(new Token(matcher.getTokenType(), value));
                    return;
                }
            }

            tokenList.add(new Token(TokenType.WORD, value));
        }
    }

    private void createToken(Matcher matcher, String value) {
        Token t = new Token(matcher.getTokenType(), value);

        tokenList.add(t);
    }

    /**
     * Reads the next byte in the buffer
     *
     * @return the next byte in the buffer
     */
    private byte nextByte() {
        return buffer[bufferIndex++];
    }

    /**
     * Refills the buffer with new data from the {@code InputStream}
     *
     * @throws IOException see {@link java.io.InputStream#read(byte[])}
     *
     * @see java.io.InputStream#read(byte[])
     */
    private void readMore() throws IOException {
        if(inputStream != null) {
            try {
                int bytesRead = inputStream.read(buffer);
                if(bytesRead != -1) {
                    bufferIndex = 0;
                }
            } catch (EOFException ignored) {
            }
        }
        bufferIndex = -1;
    }

    public class LexerIterator implements Iterator<Token> {
        /**
         * The current index of the iterator
         */
        private int index = 0;

        /**
         * The last element returned by next
         */
        private int removable = -1;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return index != tokenList.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Token next() {
            if(!hasNext())
                throw new NoSuchElementException("No elements left in the LexerIterator");
            removable = index;
            return tokenList.get(index++);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove() {
            if(removable == -1)
                throw new IllegalStateException("You must call next() before remove()");
            tokenList.remove(removable);
            index--;
        }
    }
}
