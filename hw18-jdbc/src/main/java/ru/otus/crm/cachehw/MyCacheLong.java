package ru.otus.crm.cachehw;

import java.util.*;

public class MyCacheLong<V> implements HwCache<Long, V> {
    private final Map<Key, V> cache = new WeakHashMap<>();
    private final Set<HwListener<Long, V>> listeners = new HashSet<>();

    private record Key(long key) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Key key1 = (Key) o;
            return Objects.equals(key, key1.key);
        }
    }

    public void put(Long key, V value) {
        cache.put(new Key(key), value);
        listeners.forEach(el -> el.notify(key, value, "put"));
    }

    @Override
    public void remove(Long key) {
        V v = cache.remove(new Key(key));
        listeners.forEach(el -> el.notify(key, v, "remove"));
    }

    @Override
    public V get(Long key) {
        V v = cache.get(new Key(key));
        listeners.forEach(el -> el.notify(key, v, "get"));
        return v;
    }

    @Override
    public void addListener(HwListener<Long, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<Long, V> listener) {
        listeners.remove(listener);
    }
}
