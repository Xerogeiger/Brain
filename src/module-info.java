module Brain {
    requires Sphinx4.core;
    requires junit;
    requires org.junit.jupiter.api;

    exports com.ajax.brain.linguist.en;
    exports com.ajax.brain.linguist;
    exports com.ajax.brain.utils;
    exports com.ajax.brain.utils.sorts;

    //Tests
    exports tests;
    exports tests.sphinx;
}