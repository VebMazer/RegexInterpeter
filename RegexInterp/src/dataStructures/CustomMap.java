
package dataStructures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import dataStructures.CustomSet;
import java.util.Iterator;
import java.util.Objects;


public class CustomMap<K, V> implements Map {
    
    private class Pair {
        K key;
        V value;
        
        public Pair(K k, V v) {
            key = k;
            value = v;
        }
        
        @Override
        public boolean equals(Object o) {
            //if(this.getClass() != o.getClass()) return false;
            if(this == null || this != o) return false;
            Pair p = (Pair) o;
            return this.key == p.key;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.key);
            return hash;
        }
    }
    
    private Set<Pair> pairs;
    
    public CustomMap() {
        pairs = new CustomSet();
    }
    
    @Override
    public int size() {
        return pairs.size();
    }

    @Override
    public boolean isEmpty() {
        return pairs.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        Iterator<Pair> iterator = pairs.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().key.equals(key)) return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        Iterator<Pair> iterator = pairs.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().value.equals(value)) return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Iterator<Pair> iterator = pairs.iterator();
        while(iterator.hasNext()) {
            Pair pair = iterator.next();
            if(pair.key.equals(key)) return pair.value;
        }
        return null;
    }
    
    @Override
    public Object put(Object key, Object value) {
        V lastValue = this.get(key);
        remove(key);
        pairs.add(new Pair((K) key, (V) value));
        return lastValue;
    }
    
    @Override
    public Object remove(Object key) {
        Iterator<Pair> iterator = pairs.iterator();
        while(iterator.hasNext()) {
            Pair pair = iterator.next();
            if(pair.key.equals(key)) {
                pairs.remove(pair);
                return pair.value;
            }
        }
        return false;
    }

    @Override
    public void putAll(Map m) {
        Iterator<K> iterator = m.keySet().iterator();
        while(iterator.hasNext()) {
            K key = iterator.next();
            this.put(key, m.get(key));
        }
    }

    @Override
    public void clear() {
        pairs = new CustomSet();
    }

    @Override
    public Set keySet() {
        Set keySet = new CustomSet();
        Iterator<Pair> iterator = pairs.iterator();
        while(iterator.hasNext()) {
            keySet.add(iterator.next().key);
        }
        return keySet;
    }

    @Override
    public Collection values() {
        Set values = new CustomSet();
        Iterator<Pair> iterator = pairs.iterator();
        while(iterator.hasNext()) {
            values.add(iterator.next().value);
        }
        return values;
    }

    @Override
    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
