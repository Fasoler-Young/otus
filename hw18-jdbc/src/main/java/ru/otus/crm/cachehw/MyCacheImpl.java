package ru.otus.crm.cachehw;

import java.util.*;

public class MyCacheImpl<T, V> implements HwCache<T, V> {
    private final Map<Key<T>, V> cache = new WeakHashMap<>();
    private final Set<HwListener<T, V>> listeners = new HashSet<>();

    private record Key<T>(T key) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Key<T> key1 = (Key<T>) o;
            return Objects.equals(key, key1.key);
        }
    }

    public void put(T key, V value) {
        cache.put(new Key<T>(key), value);
        notifyAllListener(key, value, "put");
    }

    @Override
    public void remove(T key) {
        V v = cache.remove(new Key<T>(key));
        notifyAllListener(key, v, "remove");
    }

    private void notifyAllListener(T key, V v, String action) {
        try {
            listeners.forEach(el -> el.notify(key, v, action));
        } catch (Exception ignored) {

        }
    }

    @Override
    public V get(T key) {
        V v = cache.get(new Key<T>(key));
        notifyAllListener(key, v, "get");
        return v;
    }

    @Override
    public void addListener(HwListener<T, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<T, V> listener) {
        listeners.remove(listener);
    }
}
