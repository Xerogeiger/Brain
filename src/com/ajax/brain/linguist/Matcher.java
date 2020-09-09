package com.ajax.brain.linguist;

/**
 * Says if the string is a match
 */
public abstract class Matcher {
    /**
     * The priority of the match
     * lower priorities come first
     */
    private int priority;

    /**
     * The type of token the match will be
     */
    private TokenType type;

    /**
     * Creates a new matcher with the provided priority
     * @param priority the priority of the matcher
     */
    public Matcher(int priority, TokenType type) {
        this.priority = priority;
        this.type = type;
    }

    /**
     * Returns the priority of the matcher
     * @return the priority of the matcher
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Returns the type of token the match would be
     * @return the type of token the match would be
     */
    public TokenType getTokenType() {
        return type;
    }

    /**
     * The method any matcher should override
     * Determines if the provided String was a match
     * @param text the text to match
     * @return if the text matched
     */
    public abstract boolean match(String text);

    /**
     * Returns a simple matcher to avoid unnecessary lambda expressions everywhere for 1 word cases
     *
     * @param text the text to match
     * @param priority the priority of the matcher
     * @param ignoreCase whether or not the ignore the case of the string to match
     * @return the matcher based on the provided values
     * @throws NullPointerException if text is null
     */
    public static final Matcher getSimpleMatcher(String text, int priority, TokenType type, boolean ignoreCase) {
        if(text == null) {
            throw new NullPointerException("The text must not be null");
        }

        if(ignoreCase) {
            return new IgnoreCaseMatcher(text, priority, type);
        } else {
            return new CaseMatcher(text, priority, type);
        }
    }

    /**
     * Matches strings discriminately based on character case
     */
    private static class CaseMatcher extends Matcher {
        private String match;

        /**
         * Creates a new CaseMatcher with the String to match and priority
         *
         * @param match the String to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public CaseMatcher(String match, int priority, TokenType type) {
            super(priority, type);
            this.match = match;
        }

        /**
         * Matches the stored value to the provided text
         * @param text the value to check
         * @return if the value matched
         */
        @Override
        public boolean match(String text) {
            return match.equals(text);
        }
    }

    /**
     * Matches strings indiscriminately based on character case
     */
    private static class IgnoreCaseMatcher extends Matcher {
        private String match;

        /**
         * Creates a new IgnoreCaseMatcher with the String to match and priority
         *
         * @param match the String to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public IgnoreCaseMatcher(String match, int priority, TokenType type) {
            super(priority, type);
            this.match = match;
        }

        /**
         * Matches the stored value to the provided text
         * @param text the value to check
         * @return if the value matched
         */
        @Override
        public boolean match(String text) {
            return match.equalsIgnoreCase(text);
        }
    }
}
