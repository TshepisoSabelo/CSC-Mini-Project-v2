package datastructures;

import java.util.Iterator;

public interface IDictionary<K,V>
{
    public IEntry<K,V> find(K key);

    public void insert(K key,V value);

    public IEntry<K, V> remove(K key);

    public boolean isEmpty();

    public int size();

    public Iterator<K> FindAll(K key);

    public Iterator<IEntry<K,V>> entries();
}

