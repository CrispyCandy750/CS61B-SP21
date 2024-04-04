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
}