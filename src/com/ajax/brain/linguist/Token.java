package com.ajax.brain.linguist;

import java.util.Objects;

/**
 * The lexer produces tokens
 */
public class Token {
    public TokenType type;
    public String value;

    /**
     * Creates a token with the given type and value
     */
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Creates a token with the given type and value
     * The character is converted to a {@code String} using {@code String.valueOf(char)}
     */
    public Token(TokenType type, char value) {
        this.type = type;
        this.value = String.valueOf(value);
    }

    /**
     * Returns the token's type
     * @return the token's type
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Returns the stored token value
     * @return the token value
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns whether or not the token's value is an empty string
     * @return true if the token's value is either null or empty
     *
     * @see String#isEmpty()
     */
    public boolean emptyToken() {
        return value == null || value.isEmpty();
    }

    /**
     * Returns the first character in the token's stored value
     * Useful if it is {@code TokenType.OPERATOR}
     * @return the first value in the string as a character
     * @throws NullPointerException if the stored String is null
     */
    public char getFirstChar() {
        return value.charAt(0);
    }

    /**
     * Checks if the object o is equal to the token
     * @param o the object to check
     * @return true if the object matches false if it doesn't match or is null
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type &&
                Objects.equals(value, token.value);
    }

    /**
     * Returns a unique hashCode based on the type and value of the token
     * @return a hashCode for the Token
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    /**
     * Returns a string value representing the token which includes the type and value
     * @return the string
     */
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
