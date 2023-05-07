package Lexicographer.Util;

import java.util.Map;

/**
 * Simple pair class
 * @param <K> Key
 * @param <V> Value
 */
public class Pair<K,V> implements Map.Entry<K,V>{
    private final K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old_val = this.value;
        this.value = value;
        return old_val;
    }
}
