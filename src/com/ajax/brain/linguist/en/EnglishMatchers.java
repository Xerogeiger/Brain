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
    public final Matcher coordinatingConjunctionMatcher;
    public final Matcher commaMatcher;
    public final Matcher digitMatcher;

    public final Matcher[] allMatchers;

    private EnglishMatchers() {
        this(1, 3, 1);
    }

    public EnglishMatchers(int symbolPriority, int conjunctionPriority, int digitPriority) {
        colonMatcher = Matcher.getCharacterMatcher(':', symbolPriority, TokenType.SUMMARY_SEPARATOR);
        semiColonMatcher = Matcher.getCharacterMatcher(';', symbolPriority, TokenType.CLAUSE_SEPARATOR);
        commaMatcher = Matcher.getCharacterMatcher(',', symbolPriority, TokenType.LIST_SEPARATOR);
        coordinatingConjunctionMatcher = Matcher.getMultiWordMatcher(conjunctionPriority, TokenType.COORDINATING_CONJUNCTION, true, "and", "or", "but", "for", "so", "yet", "nor");
        digitMatcher = Matcher.getDigitMatcher(digitPriority, TokenType.NUMBER);

        allMatchers = new Matcher[]{colonMatcher, semiColonMatcher, commaMatcher, coordinatingConjunctionMatcher, digitMatcher};
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
