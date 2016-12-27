package util;

import processors.CacheAdapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum Cache implements CacheAdapter<String, Map<String, String>> {

    INSTANCE {
        public synchronized
        Map<String, String> get(final String key) {
            if (key == null) {
                return null;
            }
            return cache.get(key);
        }

        public synchronized void put(final String key, final Map<String, String> value) {
            if (key == null) {
                return;
            }
            cache.put(key, (HashMap<String, String>) value);
        }
    };

    private final static int CACHE_SIZE = 16;
    private static final SimpleCache<String, HashMap<String, String>> cache = new SimpleCache<>();

    private static final class SimpleCache<K, V> extends LinkedHashMap<K, V> {
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > CACHE_SIZE;
        }
    }

    public static CacheAdapter<String, Map<String, String>> getInstance() {
        return INSTANCE;
    }
}
