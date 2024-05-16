package byow.lab13.test;

import byow.lab13.MemoryGame;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryGameTest {

    private static final MemoryGame MEMORY_GAME = new MemoryGame(80, 48, System.currentTimeMillis());

    /** The sleep time after draw the canvas. */
    private static final long SLEEP_TIME = 500000;

    @Test
    public void generateRandomString() {
        String randomString = MEMORY_GAME.generateRandomString(10);

        System.out.println(randomString);

        int actual = randomString.length();
        int expect = 10;
        assertEquals(expect, actual);
    }

    @Test
    public void drawFrame() throws InterruptedException {
        MEMORY_GAME.drawFrame("This is the test content.");

        Thread.sleep(SLEEP_TIME);
    }
}
