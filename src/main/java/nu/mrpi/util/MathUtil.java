package nu.mrpi.util;

/**
 * @author Pierre Ingmansson
 */
public class MathUtil {
    /**
     * Returns a random integer in the interval of the two parameters (including both from and to)
     *
     * @param from The lower integer in the interval
     * @param to   The higher integer in the interval
     * @return A random integer between (and including) the two parameters
     */
    public static int random(final int from, final int to) {
        return from + (int) (Math.random() * ((to - from) + 1));
    }
}
