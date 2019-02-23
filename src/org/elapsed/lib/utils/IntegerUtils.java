package org.elapsed.lib.utils;

public class IntegerUtils {
    private static final IntegerParseResult FAILURE = new IntegerParseResult(false, 0);

    public static IntegerParseResult parseInt(final String string) {
        if (string == null) {
            return IntegerUtils.FAILURE;
        }
        final int length = string.length();

        if (length == 0) {
            return IntegerUtils.FAILURE;
        }
        int previous, number = 0;

        if (string.charAt(0) == '-') {
            for (int i = 1; i < length; ++i) {
                final char character = string.charAt(i);

                if (!CharacterUtils.isInteger(character)) {
                    return IntegerUtils.FAILURE;
                }
                previous = number;
                number *= 10;
                number -= CharacterUtils.getAsInteger(character);

                if (previous < number) {
                    return IntegerUtils.FAILURE;
                }
            }
        } else {
            for (int i = 0; i < length; ++i) {
                final char character = string.charAt(i);

                if (!CharacterUtils.isInteger(character)) {
                    return IntegerUtils.FAILURE;
                }
                previous = number;
                number *= 10;
                number += CharacterUtils.getAsInteger(character);

                if (previous > number) {
                    return IntegerUtils.FAILURE;
                }
            }
        }
        return new IntegerParseResult(true, number);
    }

    public static int unsafelyParseInt(final String string) {
        final int length = string.length();

        int number = 0;

        if (string.charAt(0) == '-') {
            for (int i = 1; i < length; ++i) {
                number *= 10;
                number -= CharacterUtils.getAsInteger(string.charAt(i));
            }
        } else {
            for (int i = 0; i < length; ++i) {
                number *= 10;
                number += CharacterUtils.getAsInteger(string.charAt(i));
            }
        }
        return number;
    }

    public static class IntegerParseResult {
        private final boolean successful;
        private final int value;

        public IntegerParseResult(final boolean successful, final int value) {
            this.successful = successful;
            this.value = value;
        }

        public boolean wasSuccessful() {
            return this.successful;
        }

        public int getValue() {
            return this.value;
        }
    }
}