package org.elapsed.lib.utils;

public class CharacterUtils {
    public static boolean isInteger(final char character) {
        return character >= '0' && character <= '9';
    }

    public static int getAsInteger(final char character) {
        return character - '0';
    }
}
