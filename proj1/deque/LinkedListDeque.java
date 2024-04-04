package deque;

public class LinkedListDeque<T> implements Deque<T> {

    public LinkedListDeque() {
        this.sentinel = new Node(null);
        this.size = 0;
    }

    /** Adds an item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        addAfter(sentinel, item);
    }

    /** Adds an item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        addBefore(sentinel, item);
    }

    /** Returns true if deque is empty, false otherwise. */
    @Override
    public boolean isEmpty() {
        return isPointingSelf(sentinel);
    }

    /** Returns the number of items in the deque. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        System.out.println(getCycleStringStartingAtExclusively(sentinel));
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeFirst() {
        return null;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeLast() {
        return removePrevious(sentinel);
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    @Override
    public T get(int index) {
        return null;
    }

    /* --------------------------- private class & methods --------------------------- */

    private Node sentinel;
    private int size;

    private class Node {
        Node prev;
        T item;
        Node next;

        Node(T item) {
            this.item = item;
            prev = next = this;
        }
    }

    /** Add the value after the predecessor. */
    private void addAfter(Node predecessor, T item) {
        Node node = new Node(item);
        predecessor.next.prev = node;
        node.next = predecessor.next;
        predecessor.next = node;
        node.prev = predecessor;
        size++;
    }

    /** Add the value before the successor. */
    private void addBefore(Node successor, T item) {
        Node node = new Node(item);
        successor.prev.next = node;
        node.prev = successor.prev;
        successor.prev = node;
        node.next = successor;
        size++;
    }

    /** Returns true if the `prev` and `node` points the node itself, false otherwise. */
    private boolean isPointingSelf(Node node) {
        return node.next == node && node.prev == node;
    }

    /** Prints the cycle-list starting at the specified node exclusively. */
    private String getCycleStringStartingAtExclusively(Node starter) {
        StringBuilder stringBuilder = new StringBuilder();
        Node cur = starter.next;
        while (cur != starter) {
            stringBuilder.append(cur.item + " ");
            cur = cur.next;
        }
        return stringBuilder.toString();
    }

    /**
     * Removes and returns the item at the front of the node.
     * If no such item exists, returns null.
     */
    private T removePrevious(Node node) {
        T res = node.prev.item;

        node.prev = node.prev.prev;
        node.prev.next = node;
        size--;

        return res;
    }


}
