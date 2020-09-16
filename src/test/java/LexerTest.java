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

        Lexer lexer = new Lexer("If I had to choose 1 favorite fruit it would be either apples or oranges.", matchers);

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