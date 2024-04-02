package test;

import bstmap.BSTMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class BSTMapTest {

    @Test
    public void clear() {
    }

    @Test
    public void containsKey() {
        BSTMap<String, Integer> map = new BSTMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        boolean condition1 = map.containsKey("1");
        assertTrue(condition1);

        boolean condition2 = map.containsKey("2");
        assertTrue(condition2);

        boolean condition3 = map.containsKey("5");
        assertFalse(condition3);
    }

    @Test
    public void get() {
    }

    @Test
    public void size() {
        BSTMap<String, Integer> map = new BSTMap<>();

        int actual1 = map.size();
        int expect1 = 0;
        assertEquals(expect1, actual1);

        map.put("1", 1);
        map.put("2", 2);
        int actual2 = map.size();
        int expect2 = 2;
        assertEquals(expect2, actual2);

        map.put("3", 3);
        int actual3 = map.size();
        int expect3 = 3;
        assertEquals(expect3, actual3);
    }

    @Test
    public void put() {
        BSTMap<String, Integer> map = new BSTMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
    }

    @Test
    public void keySet() {
    }
}