package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] deque;

    /* first always points the first item. */
    private int first;

    /* last always points the successor of the last item. */
    private int rear;

    private static final int INITIAL_SIZE = 8;
    private static final double MIN_USAGE_FACTOR = 0.25;
    private static final double EXPAND_FACTOR = 1.5;
    private static final double SHRINK_FACTOR = 0.5;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        deque = (T[]) new Object[INITIAL_SIZE];
        first = rear = 0;
    }

    /** Adds an item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        first = prevIndex(deque, first);
        deque[first] = item;
        if (isFull()) {
            resize((int) (deque.length * EXPAND_FACTOR));
        }
    }

    /** Adds an item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        // 1. addToArr  2. isFull  3. resize()
        deque[rear] = item;
        rear = nextIndex(deque, rear);
        if (isFull()) {
            resize((int) (deque.length * EXPAND_FACTOR));
        }
    }

    /** Returns true if deque is empty, false otherwise. */
    @Override
    public boolean isEmpty() {
        return first == rear;
    }

    /** Returns the number of items in the deque. */
    @Override
    public int size() {
        return (rear - first + deque.length) % deque.length;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        System.out.println(this.getString());
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeFirst() {

        if (isEmpty()) {
            return null;
        }

        T res = deque[first];
        first = nextIndex(deque, first);

        if (needShrink()) {
            resize((int) (deque.length * SHRINK_FACTOR));
        }

        return res;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        rear = prevIndex(deque, rear);
        T res = deque[rear];

        if (needShrink()) {
            resize((int) (deque.length * SHRINK_FACTOR));
        }

        return res;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    @Override
    public T get(int index) {

        if (index >= size()) {
            return null;
        }

        return deque[getIndex(deque, first, index)];
    }

    @Override
    public Iterator<T> iterator() {
        return new DequeIterator();
    }


    private String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<T> iterator = this.iterator();

        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next() + " ");
        }

        return stringBuilder.toString().trim();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof ArrayDeque)) {
            return false;
        }

        ArrayDeque<T> otherDeque = (ArrayDeque<T>) obj;

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

    /* --------------------------- private class & methods --------------------------- */

    private class DequeIterator implements Iterator<T> {
        /* Cur points the next elements */
        int cur;
        DequeIterator() {
            cur = first;
        }

        @Override
        public boolean hasNext() {
            return cur != rear;
        }

        @Override
        public T next() {
            T res = deque[cur];
            cur = nextIndex(deque, cur);
            return res;
        }
    }

    /** Returns true if the deque is full, false otherwise. */
    private boolean isFull() {
        return nextIndex(deque, rear) == first;
    }

    /** Returns true if the deque need shrinking, false otherwise. */
    private boolean needShrink() {
        return deque.length > 8 && size() < deque.length * MIN_USAGE_FACTOR;
    }

    /** Resize the deque array. */
    private void resize(int newSize) {

        T[] newDeque = (T[]) new Object[newSize];
        int newRear = 0;

        Iterator<T> iterator = this.iterator();
        while (iterator.hasNext()) {
            newDeque[newRear] = iterator.next();
            newRear = nextIndex(newDeque, newRear);
        }

        first = 0;
        rear = newRear;
        deque = newDeque;
    }

    /** Returns the previous index of the param index. */
    private int prevIndex(T[] arr, int index) {
        return (index - 1 + arr.length) % arr.length;
    }

    /** Returns the next index of the param index */
    private int nextIndex(T[] arr, int index) {
        return (index + 1) % arr.length;
    }

    /** Returns the index starting at start with offset */
    private int getIndex(T[] arr, int start, int offset) {
        return (start + offset) % arr.length;
    }
}
