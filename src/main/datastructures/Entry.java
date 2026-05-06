
package datastructures;

public class Entry<K, V> implements IEntry<K, V>
{
    private K key;
    private V value;

    public Entry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey()
    {
        return key;
    }

    @Override
    public V getValue()
    {
        return value;
    }

    @Override
    public void setValue(V value)
    {
        this.value = value;
    }

    @Override
    public void setKey(K key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return "(" + key + ", " + value + ")";
    }
}

