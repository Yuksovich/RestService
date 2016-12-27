package processors;

public interface CacheAdapter<K,V> {
    void put(K key,V value);
    V get(K key);

}
