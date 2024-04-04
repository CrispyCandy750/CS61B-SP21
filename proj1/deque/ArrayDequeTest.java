package deque;

import org.junit.Test;

import java.net.Socket;
import java.util.Iterator;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addFirst() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addFirst("str1");
        deque.addFirst("str2");
        deque.addFirst("str3");
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

    @Test
    public void removeLast() {
        ArrayDeque<String> deque = new ArrayDeque<>();

        String object1 = deque.removeLast();
        assertNull(object1);

        deque.addFirst("str1");
        deque.addFirst("str2");
        deque.addLast("str3");

        String actual2 = deque.removeLast();
        String expect2 = "str3";
        assertEquals(expect2, actual2);

        String actual3 = deque.removeLast();
        String expect3 = "str1";
        assertEquals(expect3, actual3);

        String actual4 = deque.removeLast();
        String expect4 = "str2";
        assertEquals(expect3, actual3);

        String object5 = deque.removeLast();
        assertNull(object5);
    }

    @Test
    public void get() {
        ArrayDeque<String> deque = new ArrayDeque<>();

        String object1 = deque.get(0);
        assertNull(object1);

        deque.addFirst("str1");
        deque.addFirst("str2");
        deque.addLast("str3");
        /* The deque is ["str2", "str1", "str3"] */

        String actual2 = deque.get(0);
        String expect2 = "str2";
        assertEquals(expect2, actual2);

        String actual3 = deque.get(1);
        String expect3 = "str1";
        assertEquals(expect3, actual3);

        String actual4 = deque.get(2);
        String expect4 = "str3";
        assertEquals(expect3, actual3);

        String object5 = deque.get(3);
        assertNull(object5);
    }

    @Test
    public void iterator() {
        ArrayDeque<String> deque = new ArrayDeque<>();

        deque.addFirst("str1");
        deque.addFirst("str2");
        deque.addLast("str3");
        deque.addLast("str4");

        Iterator<String> iterator = deque.iterator();
        String[] expectStr = new String[]{"str2", "str1", "str3", "str4"};
        int i = 0;

        while (iterator.hasNext()) {
            assertEquals(expectStr[i++], iterator.next());
        }
    }
}