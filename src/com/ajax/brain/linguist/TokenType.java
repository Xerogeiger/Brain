package com.ajax.brain.linguist;

/**
 * The following tokens are all related to the original english implementation and I will summarize their purpose
 *
 * LIST_SEPARATOR - for commas in a list like apples, oranges, and bananas
 * WORD - Any english word in a sentence like apple
 * PUNCTUATION - . ? !
 * SUMMARY_SEPARATOR - for colons. eg. I took a trip to buy apples at the store and they were good: We were out of apples so I figured we needed some.
 * CLAUSE_SEPARATOR - for semi-colons. eg. The store was full of all kinds of produce; The store also had meats and soups.
 * COORDINATING_CONJUNCTION - And, or, for, so, but, yet, nor. Some of theses words may not be used as conjunctions put that is for the parser to figure out
 * CORRELATIVE_CONJUNCTION - Either, neither. These are always CORRELATIVE_CONJUNCTIONS but "not only... but also", "both and" are more difficult to detect and are left for the parser
 * HYPHEN - This can be one word like bottom-left or number like one-hundred
 *
 * Other tokens include
 * OPERATOR this one is for is the lexer can handler operators like '+' and '*'
 * WHITESPACE probably not going to be used but it is there
 */
public enum TokenType {
    OPERATOR, LIST_SEPARATOR, WORD, PUNCTUATION, SUMMARY_SEPARATOR, COORDINATING_CONJUNCTION, CORRELATIVE_CONJUNCTION, CLAUSE_SEPARATOR, HYPHEN, WHITESPACE,
    TEST //Signifies a test token
}
