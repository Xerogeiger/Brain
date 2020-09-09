package com.ajax.brain.linguist;

/**
 * The type of plural a word is
 * a apple - SINGULAR
 * my apple - SINGULAR_POSSESSIVE
 * my apples - PLURAL_POSSESSIVE
 * apples - PLURAL
 * the students' apples - PLURAL_POSSESSIVE
 */
public enum PluralType {
    SINGULAR(false, false), SINGULAR_POSSESSIVE(false, true), PLURAL(true, false), PLURAL_POSSESSIVE(true, true);

    public final boolean plural;
    public final boolean possessive;

    PluralType(boolean plural, boolean possessive) {
        this.plural = plural;
        this.possessive = possessive;
    }

    /**
     * Returns whether or not the type is plural
     * @return {@code true} if the type is plural
     */
    public boolean isPlural() {
        return plural;
    }

    /**
     * Returns whether or not the type is possessive
     * @return {@code true} if the type is possessive
     */
    public boolean isPossessive() {
        return possessive;
    }

    /**
     * Returns whether or not the type is singular
     * @return {@code true} if the type is singular
     */
    public boolean isSingular() {
        return !plural;
    }
}
