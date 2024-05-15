package byow.Core;

import java.util.Random;

public class Utils {

    /**
     * Returns a random integer uniformly in [min(a, b), max(a, b)]/).
     *
     * @param a the left endpoint
     * @param b the right endpoint
     * @param includeRightBound true if include the b
     * @return a random integer uniformly in [min(a, b), max(a, b)]/)
     * @throws IllegalArgumentException if {@code b < a}
     * @throws IllegalArgumentException if {@code b - a >= Integer.MAX_VALUE}
     */
    public static int uniform(Random random, int a, int b, boolean includeRightBound) {
        int leftBound = Math.min(a, b);
        int rightBound = Math.max(a, b);
        if (includeRightBound) {
            rightBound = rightBound + 1;
        }
        return RandomUtils.uniform(random, leftBound, rightBound);
    }
}
