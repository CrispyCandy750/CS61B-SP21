package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        /*
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
        */
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        /*
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
        */
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        /*
        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
        */
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        /*
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

        */
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        /*
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

        */
    }

    @Test
    public void addFirst() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();
        deque.addFirst("1");
        deque.addFirst("2");
        deque.addFirst("3");
    }

    @Test
    public void addLast() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();
        deque.addLast("1");
        deque.addLast("2");
        deque.addLast("3");
    }

    @Test
    public void isEmpty() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();

        boolean condition1 = deque.isEmpty();
        assertTrue(condition1);

        deque.addLast("1");
        boolean condition2 = deque.isEmpty();
        assertFalse(condition2);
    }

    @Test
    public void printDeque() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();
        deque.addLast("1");
        deque.addLast("2");
        deque.addLast("3");

        deque.printDeque();
    }

    @Test
    public void removeLast() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();
        deque.addLast("1");
        deque.addLast("2");
        deque.addLast("3");

        String actual1 = deque.removeLast();
        String expect1 = "3";
        assertEquals(expect1, actual1);

        String actual2 = deque.removeLast();
        String expect2 = "2";
        assertEquals(expect2, actual2);

        String actual3 = deque.removeLast();
        String expect3 = "1";
        assertEquals(expect3, actual3);

        String object4 = deque.removeLast();
        assertNull(object4);
    }

    @Test
    public void removeFirst() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();
        deque.addLast("1");
        deque.addLast("2");
        deque.addLast("3");

        String actual1 = deque.removeFirst();
        String expect1 = "1";
        assertEquals(expect1, actual1);

        String actual2 = deque.removeFirst();
        String expect2 = "2";
        assertEquals(expect2, actual2);

        String actual3 = deque.removeFirst();
        String expect3 = "3";
        assertEquals(expect3, actual3);

        String object4 = deque.removeFirst();
        assertNull(object4);
    }
}
