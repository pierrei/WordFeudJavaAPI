package nu.mrpi.util;

/**
 * @author Pierre Ingmansson
 */
public class ArrayUtil {
    public static char[] convertToCharArray(String[] stringArrayWithOnlyChars) {
        char[] chars = new char[stringArrayWithOnlyChars.length];
        for (int i = 0, length = stringArrayWithOnlyChars.length; i < length; i++) {
            String currentChar = stringArrayWithOnlyChars[i];
            if ("".equals(currentChar)) {
                chars[i] = '*';
            } else {
                chars[i] = currentChar.charAt(0);
            }
        }
        return chars;
    }
}
