package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] deque;

    /* first always points the first item. */
    private int first;

    /* last always points the successor of the last item. */
    private int rear;

    private static final int INITIAL_SIZE = 8;
    private static final double MIN_USAGE_FACTOR = 0.25;
    private static final double EXPAND_FACTOR = 1.25;
    private static final double SHRINK_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        deque = (T[]) new Object[INITIAL_SIZE];
        first = rear = 0;
    }

    /** Adds an item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        first = prev(deque, first);
        addToArr(deque, first, item);
        if (isFull()) {
            // TODO: implement the iterable and implements the resize.
            resize((int) (deque.length * EXPAND_FACTOR));
        }
    }

    /** Adds an item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        // 1. addToArr  2. isFull  3. resize()
        addToArr(deque, rear, item);
        rear = next(deque, rear);
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
        // TODO: implement the iterable and implements the printDeque.
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
        first = next(deque, first);

        return res;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    @Override
    public T removeLast() {
        return null;
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

    /** Returns true if the deque is full, false otherwise. */
    private boolean isFull() {
        return next(deque, rear) == first;
    }

    private boolean resize(int newSize) {
        return false;
    }

    /* --------------------------- abstraction barrier --------------------------- */
    /** Returns the previous index of the param index. */
    private int prev(T[] deque, int index) {
        return (index - 1 + deque.length) % deque.length;
    }

    /** Returns the next index of the param index */
    private int next(T[] deque, int index) {
        return (index + 1) % deque.length;
    }

    /** Add before the index-th item. */
    private void addToArr(T[] deque, int index, T item) {
        deque[index] = item;
    }
}
