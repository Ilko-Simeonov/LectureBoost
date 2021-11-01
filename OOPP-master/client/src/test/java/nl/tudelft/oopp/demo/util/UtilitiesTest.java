package nl.tudelft.oopp.demo.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UtilitiesTest {

    @Test
    void urlGenerator_test() {
        String testUrl = Utilities.generateRandomAlphaNumeric();
        assertNotNull(testUrl);
    }

}