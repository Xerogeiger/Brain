package com.ajax.brain.linguist.en;

import com.ajax.brain.linguist.Matcher;
import com.ajax.brain.linguist.TokenType;

/**
 *
 */
public final class EnglishMatchers {
    private static final EnglishMatchers DEFAULT_INSTANCE = new EnglishMatchers();

    public final Matcher colonMatcher;
    public final Matcher semiColonMatcher;

    public final Matcher[] allMatchers;

    private EnglishMatchers() {
        this(1);
    }

    public EnglishMatchers(int symbolPriority) {
        colonMatcher = Matcher.getCharacterMatcher(':', symbolPriority, TokenType.SUMMARY_SEPARATOR);
        semiColonMatcher = Matcher.getCharacterMatcher(';', symbolPriority, TokenType.CLAUSE_SEPARATOR);

        allMatchers = new Matcher[]{colonMatcher, semiColonMatcher};
    }

    public Matcher[] getMatchers() {
        return allMatchers;
    }

    /**
     * Returns the default instance of this class
     *
     * @return the default instance
     */
    public static EnglishMatchers getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }
}
