package tests;

import com.ajax.brain.linguist.Matcher;
import com.ajax.brain.linguist.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatcherTest {

    @Test
    void getSimpleMatcher() {
        Matcher m = Matcher.getSimpleMatcher("test", 1, TokenType.WORD, true);
        Matcher c_m = Matcher.getSimpleMatcher("test", 1, TokenType.WORD, false);

        assertTrue(m.match("TeSt"));
        assertTrue(m.match("TEST"));
        assertFalse(m.match("TEST "));
        assertFalse(c_m.match("TEST"));
        assertFalse(c_m.match("TeSt"));
        assertTrue(c_m.match("test"));
    }
}