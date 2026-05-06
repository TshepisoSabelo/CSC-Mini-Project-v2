package datastructures;

public interface IEntry<K, V> 
{
    K getKey();

    V getValue();

    void setValue(V value);

    void setKey(K key);
}

