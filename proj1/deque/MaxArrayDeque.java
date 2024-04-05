package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> implements MaxDeque<T> {

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public T max() {
        return max(this.comparator);
    }


    /**
     * returns the maximum element in the deque as governed by the parameter Comparator c.
     * If the MaxArrayDeque is empty, simply return null.
     */
    @Override
    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }

        Iterator<T> iterator = this.iterator();
        T maxItem = iterator.next();

        while (iterator.hasNext()) {
            T item = iterator.next();
            if (c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }

        return maxItem;
    }
}
