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
}