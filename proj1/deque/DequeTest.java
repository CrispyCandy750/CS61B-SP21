package deque;

import org.junit.Test;

import static org.junit.Assert.*;

public class DequeTest {
    @Test
    public void testEquals() {
        ArrayDeque<String> aDeque = new ArrayDeque<>();
        LinkedListDeque<String> lDeque = new LinkedListDeque<>();
        aDeque.addLast("abc");
        aDeque.addLast("def");
        aDeque.addLast("ghi");
        lDeque.addLast("abc");
        lDeque.addLast("def");
        lDeque.addLast("ghi");

        assertEquals("the arrayDeque and LinkedListDeque should be equivalent", aDeque, lDeque);
    }
}
