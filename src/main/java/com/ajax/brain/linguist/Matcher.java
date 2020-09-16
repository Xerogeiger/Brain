package com.ajax.brain.linguist;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Says if the string is a match
 */
public abstract class Matcher {
    private static WhitespaceMatcher WHITESPACE_MATCHER_INSTANCE;

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
        if(WHITESPACE_MATCHER_INSTANCE == null)
            WHITESPACE_MATCHER_INSTANCE = new WhitespaceMatcher();

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
     * Creates a new {@code MultiWordMatcher} from the provided values
     *
     * @param priority the priority of the {@code Matcher}
     * @param type the type of token this {@code Matcher} finds
     * @param ignoreCase whether of not this {@code Matcher} should ignore the case of words
     * @param strings the {@code String}s this {@code Matcher} should match
     * @return the new {@code MultiWordMatcher}
     */
    public static Matcher getMultiWordMatcher(int priority, TokenType type, boolean ignoreCase, String... strings) {
        return new MultiWordMatcher(priority, strings, type, ignoreCase);
    }

    /**
     * Creates a new {@code MultiCharMatcher} from the provided values
     *
     * @param priority the priority of the {@code Matcher}
     * @param type the type of token this {@code Matcher} finds
     * @param chars the {@code String}s of characters this {@code Matcher} should match
     * @return the new {@code MultiCharacterMatcher}
     */
    public static Matcher getMultiCharacterMatcher(int priority, TokenType type, String chars) {
        return new MultiCharacterMatcher(priority, type, chars);
    }

    /**
     * Creates a new {@code MultiCharMatcher} from the provided values
     *
     * @param priority the priority of the {@code Matcher}
     * @param type the type of token this {@code Matcher} finds
     * @param chars the characters this {@code Matcher} should match
     * @return the new {@code MultiCharacterMatcher}
     */
    public static Matcher getMultiCharacterMatcher(int priority, TokenType type, char... chars) {
        return new MultiCharacterMatcher(priority, type, chars);
    }

    /**
     * Creates a new {@code RegexMatcher} from the provided values
     *
     * @param priority the priority of the {@code Matcher}
     * @param type the type of token this {@code Matcher} finds
     * @param characterMatch whether or not this {@code Matcher} matches characters
     * @param pattern the Regex pattern this {@code Matcher} should match with
     * @return the new {@code RegexMatcher}
     */
    public static Matcher getRegexMatcher(int priority, TokenType type, boolean characterMatch, String pattern) {
        return new RegexMatcher(priority, type, characterMatch, pattern);
    }

    /**
     * Creates a new {@code RegexMatcher} from the provided values
     *
     * @param priority the priority of the {@code Matcher}
     * @param type the type of token this {@code Matcher} finds
     * @param characterMatch whether or not this {@code Matcher} matches characters
     * @param pattern the Regex pattern this {@code Matcher} should match with
     * @return the new {@code RegexMatcher}
     */
    public static Matcher getRegexMatcher(int priority, TokenType type, boolean characterMatch, Pattern pattern) {
        return new RegexMatcher(priority, type, characterMatch, pattern);
    }

    /**
     * Creates a new {@code DigitMatcher} from the provided values
     *
     * @param priority the priority of the {@code Matcher}
     * @param type the type of token this {@code Matcher} finds
     * @return the new {@code DigitMatcher}
     */
    public static Matcher getDigitMatcher(int priority, TokenType type) {
        return new DigitMatcher(priority, type);
    }

    /**
     * Matcher that uses Java Regex
     */
    private static class RegexMatcher extends Matcher {
        private final Pattern pattern;

        /**
         * Creates a new {@code RegexMatcher} with the provided values
         *
         * @param priority       the priority of the matcher
         * @param type           the type of token this matcher finds
         * @param characterMatch whether or not this matcher matches characters
         * @param pattern the regex pattern to match with
         */
        public RegexMatcher(int priority, TokenType type, boolean characterMatch, String pattern) {
            this(priority, type, characterMatch, Pattern.compile(pattern));
        }

        /**
         * Creates a new {@code RegexMatcher} with the provided values
         *
         * @param priority       the priority of the matcher
         * @param type           the type of token this matcher finds
         * @param characterMatch whether or not this matcher matches characters
         * @param pattern the regex pattern to match with
         */
        public RegexMatcher(int priority, TokenType type, boolean characterMatch, Pattern pattern) {
            super(priority, type, characterMatch);
            this.pattern = pattern;
        }

        /**
         * Matches the stored regex value to the provided text
         *
         * @param text the value to check
         * @return if the value matched
         */
        @Override
        public boolean match(String text) {
            return pattern.matcher(text).find();
        }
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
         *
         * @param text the value to check
         * @return if the value matched
         */
        @Override
        public boolean match(String text) {
            if(text.length() == 1) {
                return text.charAt(0) == this.match;
            }
            return false;
        }
    }

    /**
     * Matches multiple characters
     */
    private static class MultiCharacterMatcher extends Matcher {
        private final char[] match;

        /**
         * Creates a new {@code MultiCharacterMatcher} with the {@code String} to match and priority
         *
         * @param chars the {@code String} of characters to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public MultiCharacterMatcher(int priority, TokenType type, String chars) {
            super(priority, type, true);
            this.match = chars.toCharArray();
            Arrays.sort(this.match);
        }

        /**
         * Creates a new {@code MultiCharacterMatcher} with the {@code String} to match and priority
         *
         * @param match the characters to match
         * @param priority the match priority
         * @param type the type of token the match would be
         */
        public MultiCharacterMatcher(int priority, TokenType type, char... match) {
            super(priority, type, true);
            this.match = match.clone();
            Arrays.sort(this.match);
        }

        /**
         * Matches the stored value to the provided text
         * @param text the value to check
         * @return if the value matched
         */
        @Override
        public boolean match(String text) {
            if(text.length() == 1) {
                return Arrays.binarySearch(this.match, text.charAt(0)) > -1;
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
     * A {@code Matcher} capable of matching multiple words against a value
     */
    private static class MultiWordMatcher extends Matcher{
        /**
         * The array of strings to match
         */
        private final String[] match;

        /**
         * If the matcher should ignore the case of the strings when matching
         */
        private final boolean ignoreCase;

        /**
         * Constructs a new {@code MultiWordMatcher} with the provided priority, strings to match, and token type
         *
         * @param priority the priority of this {@code Matcher}
         * @param match the strings to match
         * @param type the token type of this {@code Matcher}
         * @param ignoreCase whether of not to ignore case of this na
         */
        public MultiWordMatcher(int priority, String[] match, TokenType type, boolean ignoreCase) {
            super(priority, type, false);
            this.match = match;
            this.ignoreCase = ignoreCase;
        }

        /**
         * Matches the provided text against the internal array of words
         *
         * @param text the text to match
         * @return if the text matched any of the words
         */
        @Override
        public boolean match(String text) {
            if(text == null) //Save some time
                return false;

            if(ignoreCase) {
                for (String str : match) {
                    if (str.equalsIgnoreCase(text))
                        return true;
                }
            } else {
                for (String str : match) {
                    if (str.equals(text))
                        return true;
                }
            }
            return false;
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

    /**
     * {@code Matcher} for digits
     */
    private static class DigitMatcher extends Matcher {
        /**
         * Creates a new {@code DigitMatcher}
         * @param priority the priority of the digit {@code DigitMatcher}
         * @param tokenType the token type of the matched digits
         */
        public DigitMatcher(int priority, TokenType tokenType) {
            super(priority, tokenType, false);
        }

        /**
         * Returns whether or not the provided {@code String} is only digits
         *
         * @param text the text to match
         * @return {@code false} if the text length is 0 or not entirely digits
         */
        @Override
        public boolean match(String text) {
            if(text.length() == 0)
                return false;

            if(text.length() == 1) {
                char ws = text.charAt(0);

                return Character.isDigit(ws);
            }

            for(int x = 0; x < text.length(); x++) {
                if(!Character.isDigit(text.charAt(x)))
                    return false;
            }

            return true;
        }
    }
}
