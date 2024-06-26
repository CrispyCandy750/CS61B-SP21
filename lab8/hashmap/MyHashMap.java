package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), removeFromBucket(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon removeFromBucket().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void clear() {
        clearBuckets(buckets);
    }

    /**
     * Check if the HashMap contains the key
     */
    @Override
    public boolean containsKey(K key) {
        return containsKey(buckets, key);
    }

    @Override
    public V get(K key) {
        return get(buckets, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        putInMap(key, value);
    }

    /**
     * Returns a set which contains all key in MyHashMap
     */
    @Override
    public Set<K> keySet() {
        return keySet(buckets);
    }

    @Override
    public V remove(K key) {
        return removeFromMap(key, null, false);
    }

    @Override
    public V remove(K key, V value) {
        return removeFromMap(key, value, true);
    }

    /**
     * Returns the iterator which iterate all keys in MyHashMap
     */
    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /* ------------------------ private class & methods ------------------------ */

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private double maxLoad = 0.75;
    private int initialSize = 16;
    private int size = 0;
    private double factor = 1.1;

    /**
     * Constructors
     */
    public MyHashMap() {
        this.buckets = createTable(initialSize);
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        this.buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.maxLoad = maxLoad;
        this.buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`removeFromBucket` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] buckets = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            buckets[i] = createBucket();
        }
        return buckets;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private void putInMap(K key, V value) {
        if (putInBuckets(buckets, key, value)) {
            size++;
        }

        if (size >= buckets.length * maxLoad) {
            resize((int) (buckets.length * factor));
        }
    }

    /**
     * add the key-value to the Map if key doesn't exist, else change the value.
     */
    private boolean putInBuckets(Collection<Node>[] buckets, K key, V value) {

        int index = getIndex(buckets, key);

        Iterator<Node> iterator = buckets[index].iterator();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.key.equals(key)) {
                next.value = value;
                return false;
            }
        }

        buckets[index].add(createNode(key, value));
        // 这里仅仅是put的，size++是不能用于这里的
        return true;
    }

    /**
     * Compute the bucket index with key
     */
    private int getIndex(Collection<Node>[] buckets, K key) {
        int hashCode = key.hashCode();
        int index = hashCode % buckets.length;

        return index < 0 ? index + buckets.length : index;
    }

    /**
     * Check if the HashMap contains the key
     */
    private boolean containsKey(Collection<Node>[] buckets, K key) {
        int index = getIndex(buckets, key);

        Iterator<Node> iterator = buckets[index].iterator();
        while (iterator.hasNext()) {
            if (iterator.next().key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value which the key corresponds to
     */
    private V get(Collection<Node>[] buckets, K key) {
        int index = getIndex(buckets, key);
        Iterator<Node> iterator = buckets[index].iterator();

        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.key.equals(key)) {
                return next.value;
            }
        }

        return null;
    }

    /**
     * Clear the buckets
     */
    private void clearBuckets(Collection<Node>[] buckets) {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
        size = 0;
    }

    /**
     * Returns a set which contains all key in MyHashMap
     */
    private Set<K> keySet(Collection<Node>[] buckets) {
        HashSet<K> keySet = new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            Iterator<Node> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                keySet.add(iterator.next().key);
            }
        }
        return keySet;
    }

    /**
     * Remove items from Map (this.buckets).
     *
     * @param matchValue match the value if true, otherwise only match the key.
     * @return
     */
    private V removeFromMap(K key, V value, boolean matchValue) {
        V removedValue = removeFromBucket(buckets, key, value, matchValue);
        if (removedValue != null) {
            size--;
        }
        return removedValue;
    }

    /**
     * Removes the items which key corresponds to
     */
    private V removeFromBucket(Collection<Node>[] buckets, K key, V value, boolean matchValue) {
        int index = getIndex(buckets, key);
        Collection<Node> bucket = createBucket();
        Iterator<Node> iterator = buckets[index].iterator();
        V removedValue = null;

        while (iterator.hasNext()) {
            Node node = iterator.next();
            if ((matchValue && node.key.equals(key) && node.value.equals(value))
                    || (!matchValue && node.key.equals(key))) {
                removedValue = node.value;
            } else {
                bucket.add(node);
            }
        }

        buckets[index] = bucket;

        return removedValue;
    }

    /**
     * Resize the length of the buckets.
     */
    private void resize(int newSize) {
        Collection<Node>[] newBuckets = createTable(newSize);

        for (int i = 0; i < buckets.length; i++) {
            Iterator<Node> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                Node next = iterator.next();
                putInBuckets(newBuckets, next.key, next.value);
            }
        }

        this.buckets = newBuckets;
    }

}
