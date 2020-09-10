package com.ajax.brain.linguist;

/**
 * Says if the string is a match
 */
public abstract class Matcher {
    private static final WhitespaceMatcher WHITESPACE_MATCHER_INSTANCE = new WhitespaceMatcher();

    /**
     * The priority of the match
     * lower priorities come first
     */
    private final int priority;

    /**
     * The type of token the match will be
     */
    private final TokenType type;

    /**
     * Whether the matcher matches a word or character
     * a word matcher would match - And, or, but
     * a character matcher would match - , : ; '
     */
    private final boolean characterMatch;

    /**
     * Creates a new matcher with the provided priority
     * @param priority the priority of the matcher
     * @param type the type of token this matcher finds
     * @param characterMatch whether or not this matcher matches characters
     */
    public Matcher(int priority, TokenType type, boolean characterMatch) {
        this.priority = priority;
        this.type = type;
        this.characterMatch = characterMatch;
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
     * Returns whether or not this matcher is a character matcher
     *
     * @return if this is a character matcher
     */
    public boolean isCharacterMatch() {
        return characterMatch;
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
    public static Matcher getSimpleMatcher(String text, int priority, TokenType type, boolean ignoreCase) {
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
     * Returns a whitespace character matcher
     *
     * @return a whitespace character matcher
     */
    public static Matcher getWhitespaceMatcher() {
        return WHITESPACE_MATCHER_INSTANCE;
    }

    /**
     * Creates a new {@code CharacterMatcher}
     *
     * @param c the character to match
     * @param priority the priority of the matcher
     * @param type the type of token the matcher finds
     * @return the new {@code CharacterMatcher}
     */
    public static Matcher getCharacterMatcher(char c, int priority, TokenType type) {
        return new CharacterMatcher(c, priority, type);
    }

    /**
     * Creates a new {@code CharacterMatcher}
     *
     * @param s the character to match as a {@code String}
     * @param priority the priority of the matcher
     * @param type the type of token the matcher finds
     * @return the new {@code CharacterMatcher}
     */
    public static Matcher getCharacterMatcher(String s, int priority, TokenType type) {
        return new CharacterMatcher(s, priority, type);
    }

    /**
     * Matches single characters
     */
    private static class CharacterMatcher extends Matcher {
        private final char match;

        /**
         * Creates a new {@code CharacterMatcher} with the {@code String} to match and priority
         *
         * @param match the {@code String} to form of a character to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public CharacterMatcher(String match, int priority, TokenType type) {
            super(priority, type, true);
            if(match.length() != 1)
                throw new IllegalArgumentException("The given text should be a character");
            this.match = match.charAt(0);
        }

        /**
         * Creates a new {@code CharacterMatcher} with the {@code String} to match and priority
         *
         * @param match the character to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public CharacterMatcher(char match, int priority, TokenType type) {
            super(priority, type, true);
            this.match = match;
        }

        /**
         * Matches the stored value to the provided text
         * @param text the value to check
         * @return if the value matched
         */
        @Override
        public boolean match(String text) {
            if(text.length() == 1) {
                if(text.charAt(0) == this.match)
                    return true;
            }
            return false;
        }
    }

    /**
     * Matches strings discriminately based on character case
     */
    private static class CaseMatcher extends Matcher {
        private final String match;

        /**
         * Creates a new {@code CaseMatcher} with the {@code String} to match and priority
         *
         * @param match the {@code String} to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public CaseMatcher(String match, int priority, TokenType type) {
            super(priority, type, false);
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
        private final String match;

        /**
         * Creates a new {@code IgnoreCaseMatcher} with the {@code String} to match and priority
         *
         * @param match the {@code String} to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public IgnoreCaseMatcher(String match, int priority, TokenType type) {
            super(priority, type, false);
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

    /**
     * {@code Matcher} for whitespace
     */
    private static class WhitespaceMatcher extends Matcher {
        /**
         * Creates a new {@code WhitespaceMatcher}
         * Priority is irrelevant because it matches whitespace
         */
        public WhitespaceMatcher() {
            super(-1, TokenType.WHITESPACE, true);
        }

        /**
         * Returns whether or not the provided {@code String} is whitespace
         *
         * @param text the text to match
         * @return {@code false} if the text length is 0 or not entirely whitespace
         */
        @Override
        public boolean match(String text) {
            if(text.length() == 0)
                return false;

            if(text.length() == 1) {
                char ws = text.charAt(0);

                return Character.isWhitespace(ws);
            }

            for(int x = 0; x < text.length(); x++) {
                if(!Character.isWhitespace(text.charAt(x)))
                    return false;
            }

            return true;
        }
    }
}
