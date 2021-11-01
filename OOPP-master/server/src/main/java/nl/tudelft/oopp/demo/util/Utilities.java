package nl.tudelft.oopp.demo.util;

import java.security.SecureRandom;

public class Utilities {


    /**
     * Method generates a random code by using ASCII codes. It automatically selects 10
     * random codes converts it into String and concatenates it.
     * @return randomly generated string
     */
    public static String generateRandomAlphaNumeric() {
        int leftLimit = 48; // ASCII code for 0
        int rightLimit = 122; // ASCII code for z
        int targetStringLength = 10;
        SecureRandom random = new SecureRandom();

        //removed special characters
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
