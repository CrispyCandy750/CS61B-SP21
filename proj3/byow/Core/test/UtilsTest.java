package byow.Core.test;

import byow.Core.Utils;
import static org.junit.Assert.*;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import javax.sound.midi.Track;
import java.net.Socket;
import java.util.Random;

public class UtilsTest {

    private static final Random RANDOM = new Random(123);

    /** Test the uniform method randomly. */
    @Test
    public void uniform1() {
        int actual = Utils.uniform(RANDOM, 1, -3, true);
        System.out.println(actual);
        assertTrue(actual >= -3 && actual <= 1);
    }

    /** Test the uniform method randomly. */
    @Test
    public void uniform2() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Utils.uniform(RANDOM,1, -3, true));
        }
    }
}
