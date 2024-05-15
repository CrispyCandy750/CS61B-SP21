package byow.Utils;

import byow.Core.RandomUtils;

import java.util.Random;

public class Utils {
    /** Returns the max value of the ints. */
    public static int getMax(int[] ints) {
        int max = ints[0];
        for (int i = 1; i < ints.length; i++) {
            if (ints[i] > max) {
                max = ints[i];
            }
        }
        return max;
    }
}
