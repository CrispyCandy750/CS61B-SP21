package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        this.sentinel = new Node(null);
        this.size = 0;
    }

    /** Adds an item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        addAfter(sentinel, item);
        size++;
    }

    /** Adds an item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        addBefore(sentinel, item);
        size++;
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
        T res = removeNext(sentinel);
        if (res != null) {
            size--;
        }
        return res;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeLast() {
        T res = removePrevious(sentinel);
        if (res != null) {
            size--;
        }
        return res;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    @Override
    public T get(int index) {
        if (index < 0) {
            return null;
        }
        return getIthItemStartingAt(sentinel, index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof LinkedListDeque)) {
            return false;
        }

        LinkedListDeque<T> otherDeque = (LinkedListDeque<T>) obj;

        if (otherDeque.size() != this.size()) {
            return false;
        }

        Iterator<T> iterator = this.iterator();
        Iterator<T> otherIterator = otherDeque.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() != otherIterator.next()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<T> {
        Node cur;
        DequeIterator() {
            cur = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return cur != sentinel;
        }

        @Override
        public T next() {
            T item = cur.item;
            cur = cur.next;
            return item;
        }
    }

    /* --------------------------- private class & methods --------------------------- */

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
        // size++;
    }

    /** Add the value before the successor. */
    private void addBefore(Node successor, T item) {
        Node node = new Node(item);
        successor.prev.next = node;
        node.prev = successor.prev;
        successor.prev = node;
        node.next = successor;
        // size++;
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
        // size--;

        return res;
    }

    /**
     * Removes and returns the item at the back of the node.
     * If no such item exists, returns null.
     */
    private T removeNext(Node node) {
        T res = node.next.item;

        node.next = node.next.next;
        node.next.prev = node;

        return res;
    }

    /** Returns the i-th item starting at node exclusively, returns null if index out of size. */
    private T getIthItemStartingAt(Node node, int index) {
        int i = 0;
        Node cur = node.next;

        while (i < index && cur != node) {
            cur = cur.next;
            i++;
        }

        // if cur == node, returns null
        return cur.item;
    }
}
