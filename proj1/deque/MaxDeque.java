package deque;

import java.util.Comparator;

public interface MaxDeque<T> extends Deque<T> {

    /**
     * returns the maximum element in the deque as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, simply return null.
     */
    T max();

    /**
     * returns the maximum element in the deque as governed by the parameter Comparator c.
     * If the MaxArrayDeque is empty, simply return null.
     */
    T max(Comparator<T> c);

}
