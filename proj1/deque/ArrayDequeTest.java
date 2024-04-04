package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addFirst() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addFirst("abc");
    }

    @Test
    public void addLast() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addLast("abc");
    }

    @Test
    public void isEmpty() {
        ArrayDeque<String> deque = new ArrayDeque<>();

        boolean condition1 = deque.isEmpty();
        assertTrue(condition1);

        deque.addFirst("abc");
        boolean condition2 = deque.isEmpty();
        assertFalse(condition2);
    }

    @Test
    public void size() {
        ArrayDeque<String> deque = new ArrayDeque<>();

        int actual1 = deque.size();
        int expect1 = 0;
        assertEquals(expect1, actual1);

        deque.addFirst("abc");
        int actual2 = deque.size();
        int expect2 = 1;
        assertEquals(expect2, actual2);

        deque.addLast("def");
        int actual3 = deque.size();
        int expect3 = 2;
        assertEquals(expect3, actual3);
    }

    @Test
    public void removeFirst() {
        ArrayDeque<String> deque = new ArrayDeque<>();

        String object1 = deque.removeFirst();
        assertNull(object1);

        deque.addFirst("str1");
        deque.addFirst("str2");
        deque.addLast("str3");

        String actual2 = deque.removeFirst();
        String expect2 = "str2";
        assertEquals(expect2, actual2);

        String actual3 = deque.removeFirst();
        String expect3 = "str1";
        assertEquals(expect3, actual3);

        String actual4 = deque.removeFirst();
        String expect4 = "str3";
        assertEquals(expect3, actual3);

        String object5 = deque.removeFirst();
        assertNull(object5);
    }
}