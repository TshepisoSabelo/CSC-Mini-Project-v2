 
package datastructures;

import java.util.Iterator;
import datastructures.ArrayList;

public class Dictionary<K,V> implements IDictionary<K, V>
{
    private PositionList<IEntry<K,V>> arrayList = null;

    public Dictionary(PositionList<IEntry<K,V>> arrayList)
    {
        this.arrayList = arrayList;
    }

    public Dictionary(int size)
    {
        createArray(size);
    }

    private void createArray(int size)
    {   
        arrayList = new PositionList<IEntry<K,V>>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public IEntry<K, V> find(K key)
    {
        if(arrayList == null || arrayList.isEmpty())
        {
            return null;
        }

        Node<IEntry<K,V>> node = (Node<IEntry<K,V>>) arrayList.first();

        while(node != null && node.getElement() != null)
        {
            IEntry<K,V> entry = node.getElement();
            if(entry.getKey().equals(key))
            {
                return entry;
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public void insert(K key, V value)
    {
        IEntry<K, V> entry = new Entry<K,V>(key, value);
        arrayList.addLast(entry); 
    }

    @SuppressWarnings("unchecked")
    @Override
    public IEntry<K, V> remove(K key)
    {
        if(arrayList == null || arrayList.isEmpty())
        {
            return null;
        }
        IPosition<IEntry<K, V>> current = arrayList.first();
        if(current == null)
        {
            return null;
        }
        Node<IEntry<K, V>> node = (Node<IEntry<K, V>>) current;

        while(node != null && node.getElement() != null)
        {
            IEntry<K, V> entry = node.getElement();
            if(entry.getKey().equals(key))
            {
                arrayList.remove(node);
                return entry;
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public boolean isEmpty() 
    {
        return arrayList == null || arrayList.isEmpty();
    }

    @Override
    public int size() 
    {
        if(arrayList == null)
        {
            return 0;
        }

        return arrayList.getSize();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<K> FindAll(K key) 
    {
        ArrayList<K> keys = new ArrayList<>();
        if(arrayList == null || arrayList.isEmpty())
        {
            return (Iterator<K>) keys.iterator();
        }
        Node<IEntry<K, V>> node = (Node<IEntry<K, V>>) arrayList.first();
        
        while(node != null && node.getElement() != null)
        {
            IEntry<K, V> entry = node.getElement();
            if(entry.getKey().equals(key))
            {
                keys.addLast(entry.getKey());
            }
            node = node.getNext();
        }
        return (Iterator<K>) keys.iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<IEntry<K, V>> entries() 
    {
        ArrayList<IEntry<K, V>> entries = new ArrayList<>();

        if(arrayList == null || arrayList.isEmpty())
        {
            return (Iterator<IEntry<K, V>>) entries.iterator();
        }
        Node<IEntry<K, V>> node = (Node<IEntry<K, V>>) arrayList.first();

        while(node != null && node.getElement() != null)
        {
            entries.addLast(node.getElement());
            node = node.getNext();
        }
        return (Iterator<IEntry<K, V>>) entries.iterator();
    }

    @SuppressWarnings("unchecked")
    public void replace(K key, V value) 
    {
        if(arrayList == null || arrayList.isEmpty())
        {
            return;
        }
        IPosition<IEntry<K, V>> current = arrayList.first();
        if(current == null)
        {
            return;
        }
        Node<IEntry<K, V>> node = (Node<IEntry<K, V>>) current;
        while(node != null && node.getElement() != null)
        {
            IEntry<K, V> entry = node.getElement();
            if(entry.getKey().equals(key))
            {
                entry.setValue(value);
                return;
            }
            node = node.getNext();
        }
    }
}
