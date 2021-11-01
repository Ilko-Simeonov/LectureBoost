package nl.tudelft.oopp.demo.util;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class UtilitiesTest {
    @Test
    void testRandomUrl() {
        String random1 = Utilities.generateRandomAlphaNumeric();
        String random2 = Utilities.generateRandomAlphaNumeric();
        assertNotEquals(random1, random2);
    }
}
