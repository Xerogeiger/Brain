package tests;

import com.ajax.brain.linguist.*;
import com.ajax.brain.linguist.en.EnglishMatchers;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    @Test
    public void lexerTest() {
        MatcherList matchers = new MatcherList();

        matchers.add(Matcher.getSimpleMatcher("TEST", 1, TokenType.TEST, false));
        matchers.addAll(EnglishMatchers.getDefaultInstance().getMatchers());

        Lexer lexer = new Lexer("TEST word yet Test", matchers);

        try {
            lexer.lex();
        } catch (Exception e) {
            fail(e);
        }

        for(Token t: lexer) {
            System.out.println(t);
        }
    }
}