package byow.Core.test;

import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import org.junit.Test;

import java.util.Random;
import java.util.Scanner;

public class EngineTest {
    private static final int WORLD_WIDTH = 100;
    private static final int WORLD_HEIGHT = 50;

    private static final Random RANDOM = Engine.RANDOM;
    //    public static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final long SLEEP_TIME = 500000;

    //    @Test
    //    public void generateWorldFilledWithRooms() throws InterruptedException {
    //        TETile[][] world = Engine.generateWorldFilledWithRooms(RANDOM, WORLD_WIDTH,
    //        WORLD_HEIGHT);
    //        TERenderer ter = new TERenderer();
    //        ter.initialize(world.length, world[0].length);
    //        ter.renderFrame(world);
    //
    //        Thread.sleep(SLEEP_TIME);
    //    }

    @Test
    public void interactWithInputString() throws InterruptedException {
//        9,223,372,036,854,775,807
//        String input = "n92233720112375804S";
        String input = "n" + System.currentTimeMillis() + "s";
        Engine engine = new Engine();
        TETile[][] world = engine.interactWithInputString(input);
        TERenderer ter = new TERenderer();
        ter.initialize(world.length, world[0].length);

        ter.renderFrame(world);
        Thread.sleep(SLEEP_TIME);
    }
}
