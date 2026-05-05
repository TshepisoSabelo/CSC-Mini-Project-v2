package datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<T> implements IList<T>, Iterable<T> {
	// variables
	private T[] array;
	public static final int CAPACITY = 2000;
	private int size;

	// constructor:
	/**
	 * Default constructor
	 */
	public ArrayList() {
		createArray(CAPACITY);
		size = 0;
	}

	/**
	 * Overloaded Constructor
	 * 
	 * @param size the initial capacity of the list
	 */
	public ArrayList(int size) {
		createArray(size);
		this.size = 0;
	}

	// iterator class:
	/**
	 * Private iterator class for traversing the ArrayList
	 * 
	 * @param <T>
	 */
	private class ArrayListIterator implements Iterator<T> {
		private int j = 0;
		private boolean removable = false;

		@SuppressWarnings("unchecked")
		@Override
		public T next() throws NoSuchElementException {
			if (j >= size)
				throw new NoSuchElementException("No next element");
			// TODO Auto-generated method stub
			removable = true;
			return (T) array[j++];
		}

		@Override
		public boolean hasNext() {
			// need to do
			return j < size;
		}

	}

	/**
	 * Returns an iterator over elements in this list.
	 * 
	 * @return an iterator
	 */
	@Override
	public Iterator<T> iterator() {
		return new ArrayListIterator();
	}

	// functions:
	/**
	 * Returns the number of elements in the list.
	 * 
	 * @return the current size of the list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Checks whether the list is empty.
	 * 
	 * @return true if the list contains no elements, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the element at the specified index.
	 * 
	 * @param i the index of the element to return
	 * @return the element at index i
	 */
	@Override
	public T get(int i) {
		checkIndex(i);
		return array[i];
	}

	/**
	 * Replaces the element at the specified index with the given item.
	 * 
	 * @param i    the index of the element to replace
	 * @param item the new element
	 * @return the updated element
	 */
	@Override
	public T set(int i, T item) {
		checkIndex(i);
		array[i] = item;
		return array[i];
	}

	/**
	 * Inserts an element at the specified index and shifts subsequent elements to
	 * the right. If the array is full, its capacity is doubled.
	 * 
	 * @param i    the index where the element should be inserted
	 * @param item the element to insert
	 * @throws IndexOutOfBoundsException if the index is invalid
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void add(int i, T item) throws IndexOutOfBoundsException
	{
		//check the index that was added:
		if(i<0 || i>size)
			   throw new IndexOutOfBoundsException("Index is out of bounds");
		//check if the array is full or not:
		if(size==array.length)
		{
			//increase the size of the array:Doubling strategy 
			T[] copy=(T[]) new Object[array.length*2];
			//copy all items into the new array:
			for(int c=0;c<array.length;c++)
			{
				copy[c]=array[c];
			}
			array=copy;
		}
		// shift elements to the right .
		for (int k = size - 1; k >= i; k--) {
			array[k + 1] = array[k];
		}
		array[i] = item;
		size++;

	}

	/**
	 * Removes and returns the element at the specified index.
	 * 
	 * @param i the index of the element to remove
	 * @return the removed element
	 * @throws IllegalStateException if the list is empty
	 */
	@Override
	public T remove(int i) {
		if (isEmpty()) {
			throw new IllegalStateException("Cannot remove from empty array");
		}
		checkIndex(i);
		T itemtoRemoveT = array[i];
		shiftElementsLeft(i);
		return itemtoRemoveT;
	}

	/**
	 * Removes and returns the element in the array.
	 * 
	 * @param T the object to remove
	 * @return the removed element
	 * @throws IllegalStateException if the list is empty
	 */
	public T remove(T obj) {
		if (isEmpty()) {
			throw new IllegalStateException("Cannot remove from empty array");
		}

		T removed = null;

		for (int i = 0; i < this.size; i++) {
			if (obj.equals(array[i])) {
				removed = array[i];
				shiftElementsLeft(i);
				break;
			}
		}

		return removed;
	}

	/**
	 * This method sorts the elements in the array using the bubble sort algorithm
	 */
	public void sort() {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size - 1; j++) {
            Comparable current = (Comparable) array[j];
            Comparable next = (Comparable) array[j + 1];

            if (current.compareTo(next) > 0) {
                T temp = array[j];
                array[j] = array[j + 1];
                array[j + 1] = temp;
            }
         }
      }
   }

   /**
	 * This method reverses the order of elements in the array 
	 */

	public void reverse()
	{
		for (int i = 0; i < size / 2; i++) {
			T temp = array[i];
            array[i] = array[size - 1 - i];
            array[size - 1 - i] = temp;
		}
    
	}
	
	 /**
	    * Method to add an item to the end of the list 
	    * @param item The item that will be added to the end of the list 
	    */
	    public void addLast(T item)
	    {
	    	//increase size 
	        if(size==array.length)
	        {
	        	increaseSizeOfArray();
	        }
	        array[size]=item ;
	        size++;
	        
	    }

	@Override
	public String toString() {
		return "List [array=" + Arrays.toString(array) + ", size=" + size + "]";
	}
	


	//helper functions
	/**
	 * Creates the internal array with the specified size.
	 * 
	 * @param Size the capacity of the array
	 */
	@SuppressWarnings("unchecked")
	private void createArray(int Size) {
		this.array = (T[]) new Object[Size];
	}

	/**
	 * Checks whether the given index is within valid bounds.
	 * 
	 * @param i the index to check
	 * @throws IndexOutOfBoundsException if the index is invalid
	 */
	private void checkIndex(int i) {
		if (i < 0 || i >= size)
			throw new IndexOutOfBoundsException("Index is out of bounds");

	}

	private void shiftElementsLeft(int i) {
		// shift elements to the left :
		for (int r = i; r < size - 1; r++) {
			array[r] = array[r + 1];
		}
		array[size - 1] = null;

		size--;
	}
	
	/**
	 * Method uses a doubling strategy to increase the size of the array
	 */
	private void increaseSizeOfArray()
	{
		//increase the size of the array:Doubling strategy 
				@SuppressWarnings("unchecked")
				T[] copy=(T[]) new Object[array.length*2];
				//copy all items into the new array:
				for(int c=0; c<size; c++)
				{
					copy[c]=array[c];
				}
				array=copy;
	}


}
