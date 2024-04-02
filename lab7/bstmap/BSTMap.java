package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private BSTNode root;
    private int size;

    @Override
    public void clear() {
        size = 0;
    }

    /**
     * Check if the BST contains a given key.
     * @param key The key to search for.
     * @return True if the key is found in the BST, false otherwise.
     */
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Insert a key-value pair into the BST or update the value if the key already exists.
     *
     * @param key   The key to be inserted or updated.
     * @param value The value associated with the key.
     * @return The modified node after insertion or update.
     */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private class BSTNode {
        K key;
        V value;
        BSTNode lChild;
        BSTNode rChild;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Check if the BST contains a given key.
     *
     * @param node The current node being checked.
     * @param key  The key to search for.
     * @return True if the key is found in the BST, false otherwise.
     */
    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        } else if (node.key.compareTo(key) > 0) {
            return containsKey(node.lChild, key);
        } else if (node.key.compareTo(key) < 0) {
            return containsKey(node.rChild, key);
        } else {
            return true;
        }
    }

    /**
     * Insert a key-value pair into the BST or update the value if the key already exists.
     *
     * @param node  The current node being considered for insertion or update.
     * @param key   The key to be inserted or updated.
     * @param value The value associated with the key.
     * @return The modified node after insertion or update.
     */
    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            size++;
            return new BSTNode(key, value);
        }

        int cmp = node.key.compareTo(key);

        if (cmp > 0) {
            node.lChild = put(node.lChild, key, value);
        } else if (cmp < 0) {
            node.rChild = put(node.rChild, key, value);
        } else {
            node.value = value;
        }

        return node;
    }
}
