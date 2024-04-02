package test;

import bstmap.BSTMap;
import org.junit.Test;

import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class BSTMapTest {

    @Test
    public void clear() {
        BSTMap<String, Integer> map = new BSTMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.clear();

        Integer actual1 = map.size();
        Integer expect1 = 0;
        assertEquals(expect1, actual1);

        Integer object2 = map.get("1");
        assertNull(object2);
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
        BSTMap<String, Integer> map = new BSTMap<>();
        map.put("1", 1);

        Integer actual1 = map.get("1");
        Integer expect1 = 1;
        assertEquals(expect1, actual1);

        map.put("2", 2);
        map.put("3", 3);

        Integer actual2 = map.get("3");
        Integer expect2 = 3;
        assertEquals(expect2, actual2);

        Integer object3 = map.get("5");
        assertNull(object3);
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
    public void printInOrder() {
        BSTMap<String, Integer> map = new BSTMap<>();
        map.put("1", 1);
        map.put("5", 5);
        map.put("3", 3);
        map.printInOrder();
    }

    @Test
    public void keySet() {
        BSTMap<String, Integer> map = new BSTMap<>();
        map.put("1", 1);
        map.put("5", 5);
        map.put("3", 3);

        Set<String> keySet = map.keySet();

        int actual1 = keySet.size();
        int expect1 = 3;
        assertEquals(expect1, actual1);

        boolean condition2 = keySet.containsAll(Arrays.asList("1", "5", "3"));
        assertTrue(condition2);

        boolean condition3 = keySet.contains("4");
        assertFalse(condition3);
    }

    @Test
    public void iterator() {
        BSTMap<String, Integer> map = new BSTMap<>();
        map.put("1", 1);
        map.put("5", 5);
        map.put("3", 3);
        map.put("4", 4);

        Iterator<String> iterator = map.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}