package byow.Core.test;

import org.junit.Test;

import static org.junit.Assert.*;

public class CustomTest {

    @Test
    public void toUpperCase() {
        char actual = Character.toUpperCase('1');
        System.out.println(actual);
    }

    @Test
    public void parseInt() {
        String intStr = "123456";
        int actual = Integer.parseInt(intStr.substring(0, intStr.length()));
        int expect = 123456;
        assertEquals(expect, actual);
    }
}
