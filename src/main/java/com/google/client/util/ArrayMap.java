package com.google.client.util;


import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K, V> implements Cloneable {

    int size;
    private Object[] data;

    public static <K, V> ArrayMap<K, V> create() {
        return new ArrayMap<K, V>();
    }

    public static <K, V> ArrayMap<K, V> create(int initialCapacity) {
        ArrayMap<K, V> result = create();
        result.ensureCapacity(initialCapacity);
        return result;
    }

    public static <K, V> ArrayMap<K, V> of(Object... keyValuePairs) {
        ArrayMap<K, V> result = create(1);
        int length = keyValuePairs.length;
        if (1 == length % 2) {
            throw new IllegalArgumentException(
                    "missing value for last key: " + keyValuePairs[length - 1]);
        }
        result.size = keyValuePairs.length / 2;
        Object[] data = result.data = new Object[length];
        System.arraycopy(keyValuePairs, 0, data, 0, length);
        return result;
    }

    @Override
    public final int size() {
        return this.size;
    }

    public final K getKey(int index) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        @SuppressWarnings("unchecked")
        K result = (K) this.data[index << 1];
        return result;
    }

    public final V getValue(int index) {
        if (index < 0 || index >= this.size) {
            return null;
        }
        return valueAtDataIndex(1 + (index << 1));
    }

    public final V set(int index, K key, V value) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int minSize = index + 1;
        ensureCapacity(minSize);
        int dataIndex = index << 1;
        V result = valueAtDataIndex(dataIndex + 1);
        setData(dataIndex, key, value);
        if (minSize > this.size) {
            this.size = minSize;
        }
        return result;
    }

    public final V set(int index, V value) {
        int size = this.size;
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        int valueDataIndex = 1 + (index << 1);
        V result = valueAtDataIndex(valueDataIndex);
        this.data[valueDataIndex] = value;
        return result;
    }

    public final void add(K key, V value) {

        set(this.size, key, value);
    }

    public final V remove(int index) {
        return removeFromDataIndexOfKey(index << 1);
    }

    @Override
    public final boolean containsKey(Object key) {
        return -2 != getDataIndexOfKey(key);
    }

    public final int getIndexOfKey(K key) {
        return getDataIndexOfKey(key) >> 1;
    }

    @Override
    public final V get(Object key) {
        return valueAtDataIndex(getDataIndexOfKey(key) + 1);
    }

    @Override
    public final V put(K key, V value) {
        int index = getIndexOfKey(key);
        if (index == -1) {
            index = this.size;
        }
        return set(index, key, value);
    }

    @Override
    public final V remove(Object key) {

        return removeFromDataIndexOfKey(getDataIndexOfKey(key));
    }

    public final void ensureCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new IndexOutOfBoundsException();
        }
        Object[] data = this.data;
        int minDataCapacity = minCapacity << 1;
        int oldDataCapacity = data == null ? 0 : data.length;
        if (minDataCapacity > oldDataCapacity) {
            int newDataCapacity = oldDataCapacity / 2 * 3 + 1;
            if (newDataCapacity % 2 != 0) {
                newDataCapacity++;
            }
            if (newDataCapacity < minDataCapacity) {
                newDataCapacity = minDataCapacity;
            }
            setDataCapacity(newDataCapacity);
        }
    }

    private void setDataCapacity(int newDataCapacity) {
        if (newDataCapacity == 0) {
            this.data = null;
            return;
        }
        int size = this.size;
        Object[] oldData = this.data;
        if (size == 0 || newDataCapacity != oldData.length) {
            Object[] newData = this.data = new Object[newDataCapacity];
            if (size != 0) {
                System.arraycopy(oldData, 0, newData, 0, size << 1);
            }
        }
    }

    private void setData(int dataIndexOfKey, K key, V value) {
        Object[] data = this.data;
        data[dataIndexOfKey] = key;
        data[dataIndexOfKey + 1] = value;
    }

    private V valueAtDataIndex(int dataIndex) {
        if (dataIndex < 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        V result = (V) this.data[dataIndex];
        return result;
    }

    private int getDataIndexOfKey(Object key) {
        int dataSize = this.size << 1;
        Object[] data = this.data;
        for (int i = 0; i < dataSize; i += 2) {
            Object k = data[i];
            if (key == null ? k == null : key.equals(k)) {
                return i;
            }
        }
        return -2;
    }

    private V removeFromDataIndexOfKey(int dataIndexOfKey) {
        int dataSize = this.size << 1;
        if (dataIndexOfKey < 0 || dataIndexOfKey >= dataSize) {
            return null;
        }
        V result = valueAtDataIndex(dataIndexOfKey + 1);
        Object[] data = this.data;
        int moved = dataSize - dataIndexOfKey - 2;
        if (moved != 0) {
            System.arraycopy(data, dataIndexOfKey + 2, data, dataIndexOfKey, moved);
        }
        this.size--;
        setData(dataSize - 2, null, null);
        return result;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.data = null;
    }


    @Override
    public final boolean containsValue(Object value) {
        int dataSize = this.size << 1;
        Object[] data = this.data;
        for (int i = 1; i < dataSize; i += 2) {
            Object v = data[i];
            if (value == null ? v == null : value.equals(v)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }


    @Override
    public ArrayMap<K, V> clone() {
        try {
            @SuppressWarnings("unchecked")
            ArrayMap<K, V> result = (ArrayMap<K, V>) super.clone();
            Object[] data = this.data;
            if (data != null) {
                int length = data.length;
                Object[] resultData = result.data = new Object[length];
                System.arraycopy(data, 0, resultData, 0, length);
            }
            return result;
        } catch (CloneNotSupportedException e) {
            // won't happen
            return null;
        }
    }

    final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return ArrayMap.this.size;
        }
    }

    final class EntryIterator implements Iterator<Map.Entry<K, V>> {
        private boolean removed;
        private int nextIndex;

        public boolean hasNext() {
            return this.nextIndex < ArrayMap.this.size;
        }

        public Map.Entry<K, V> next() {
            int index = this.nextIndex;
            if (index == ArrayMap.this.size) {
                throw new NoSuchElementException();
            }
            this.nextIndex++;
            this.removed = false;
            return new Entry(index);
        }

        public void remove() {
            int index = this.nextIndex - 1;
            if (this.removed || index < 0) {
                throw new IllegalArgumentException();
            }
            ArrayMap.this.remove(index);
            this.nextIndex--;
            this.removed = true;
        }
    }

    final class Entry implements Map.Entry<K, V> {

        private int index;

        Entry(int index) {
            this.index = index;
        }

        public K getKey() {
            return ArrayMap.this.getKey(this.index);
        }

        public V getValue() {
            return ArrayMap.this.getValue(this.index);
        }

        public V setValue(V value) {
            return ArrayMap.this.set(this.index, value);
        }

        @Override
        public int hashCode() {
            K key = getKey();
            V value = getValue();
            return (key != null ? key.hashCode() : 0) ^ (value != null ? value.hashCode() : 0);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Map.Entry<?, ?>)) {
                return false;
            }
            Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return Objects.equal(getKey(), other.getKey()) && Objects.equal(getValue(), other.getValue());
        }
    }
}
