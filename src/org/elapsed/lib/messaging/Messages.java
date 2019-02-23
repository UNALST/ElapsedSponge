package org.elapsed.lib.messaging;

import org.bukkit.command.CommandSender;

public class Messages {
    private static final int BITS_NEEDED = 7;

    private static final int ARRAY_SIZE = 1 << Messages.BITS_NEEDED;
    private static final char BOUNDS_COMPARATOR = (char) (Character.MAX_VALUE << Messages.BITS_NEEDED);

    private static final boolean[] COLOR_CODE = new boolean[Messages.ARRAY_SIZE];
    private static final char[] LOWERCASE_CHARACTERS = new char[Messages.ARRAY_SIZE];

    private static final char COLOR_CODE_CHARACTER = (char) 167;

    public static final String BLACK = Messages.getColor('0');
    public static final String DARK_BLUE = Messages.getColor('1');
    public static final String DARK_GREEN = Messages.getColor('2');
    public static final String DARK_AQUA = Messages.getColor('3');
    public static final String DARK_RED = Messages.getColor('4');
    public static final String DARK_PURPLE = Messages.getColor('5');
    public static final String GOLD = Messages.getColor('6');
    public static final String GRAY = Messages.getColor('7');
    public static final String DARK_GRAY = Messages.getColor('8');
    public static final String BLUE = Messages.getColor('9');
    public static final String GREEN = Messages.getColor('a');
    public static final String AQUA = Messages.getColor('b');
    public static final String RED = Messages.getColor('c');
    public static final String LIGHT_PURPLE = Messages.getColor('d');
    public static final String YELLOW = Messages.getColor('e');
    public static final String WHITE = Messages.getColor('f');
    public static final String MAGIC = Messages.getColor('k');
    public static final String BOLD = Messages.getColor('l');
    public static final String STRIKETHROUGH = Messages.getColor('m');
    public static final String UNDERLINE = Messages.getColor('n');
    public static final String ITALIC = Messages.getColor('o');
    public static final String RESET = Messages.getColor('r');

    static {
        final String codes = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
        final int length = codes.length();

        char character;

        for (int i = 0; i < length; ++i) {
            character = codes.charAt(i);

            Messages.COLOR_CODE[character] = true;
            Messages.LOWERCASE_CHARACTERS[character] = character >= 'A' && character <= 'Z' ? (char) (character + 32) : character;
        }
    }

    private static String getColor(char color) {
        return new String(new char[] { Messages.COLOR_CODE_CHARACTER, color } );
    }

    public static String color(final char code, final String text) {
        final char[] characters = text.toCharArray();
        final int iterations = characters.length - 1;

        char read;

        for (int i = 0; i < iterations;) {
            if (characters[i++] == code && ((read = characters[i]) & Messages.BOUNDS_COMPARATOR) == 0 && Messages.COLOR_CODE[read]) {
                characters[i - 1] = Messages.COLOR_CODE_CHARACTER;
                characters[i] = Messages.LOWERCASE_CHARACTERS[read];
            }
        }
        return new String(characters);
    }

    public static String color(final String text) {
        return Messages.color('&', text);
    }

    public static String format(final char code, final String text, final Object... args) {
        final String[] arguments = new String[args.length];
        int needed = 0;

        int i;

        for (i = 0; i < args.length; ++i) {
            needed += (arguments[i] = args[i].toString()).length();
        }
        final int length = text.length();
        final char[] characters = new char[length - 2 * args.length + needed];

        int index = 0;
        char read, next;

        String replacement;
        int replacementLength;

        int arg = 0;

        for (i = 0; i < length;) {
            if ((read = text.charAt(i++)) == code && ((next = text.charAt(i)) & Messages.BOUNDS_COMPARATOR) == 0 && Messages.COLOR_CODE[next]) {
                characters[index++] = Messages.COLOR_CODE_CHARACTER;
                characters[index++] = Messages.LOWERCASE_CHARACTERS[next];

                ++i;
            } else if (read == '%' && text.charAt(i) == 's') {
                replacementLength = (replacement = arguments[arg++]).length();

                for (int j = 0; j < replacementLength; ++j) {
                    characters[index + j] = replacement.charAt(j);
                }
                index += replacementLength;
                ++i;
            } else  {
                characters[index++] = read;
            }
        }
        return new String(characters);
    }

    public static boolean send(final CommandSender sender, final String text, final Object... args) {
        sender.sendMessage(Messages.format('&', text, args));
        return true;
    }

    public static boolean error(final CommandSender sender, final String text, final Object... args) {
        final String[] arguments = new String[args.length];
        int needed = 0;

        int i;

        for (i = 0; i < args.length; ++i) {
            needed += (arguments[i] = args[i].toString()).length();
        }
        final int length = text.length();

        final char[] characters = new char[length - 2 * args.length + needed + 2];
        characters[0] = Messages.COLOR_CODE_CHARACTER;
        characters[1] = 'c';

        int index = 0;
        char read;

        String replacement;
        int replacementLength;

        int arg = 0;

        for (i = 0; i < length;) {
            if ((read = text.charAt(i++)) == '%' && text.charAt(i) == 's') {
                replacementLength = (replacement = arguments[arg++]).length();

                for (int j = 0; j < replacementLength; ++j) {
                    characters[index + j + 2] = replacement.charAt(j);
                }
                index += replacementLength;
                ++i;
            } else  {
                characters[index++ + 2] = read;
            }
        }
        sender.sendMessage(new String(characters));
        return false;
    }
}