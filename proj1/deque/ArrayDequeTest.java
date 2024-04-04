package deque;

import org.junit.Test;

public class ArrayDequeTest {

    @Test
    public void addFirst() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addFirst("abc");
    }
}