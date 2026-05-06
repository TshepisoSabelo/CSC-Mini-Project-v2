package datastructures;

public class PositionsList<E> implements Iterator{

	private Node<E> header;
	private Node<E> trailer;
	private int size = 0;

	/**
	 * This is the default constructor that initialises the header and trailer node
	 */
	public PositionsList() {
		header = new Node<>();
		trailer = new Node<>(null, header, null);
		header.setNext(trailer);
	}

	/**
	 * This is a getter method that returns the size of the list
	 * 
	 * @return an integer representing the size of the array
	 */
	public int getSize() {
		return size;
	}

	/**
	 * This method returns if the list is empty or not
	 * 
	 * @return a boolean representing if the list is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * This method returns the first Element of the first Node of the list
	 * 
	 * @return the first Element of the list
	 */
	public IPosition<E> first() {
		if (this.isEmpty())
			return null;

		IPosition<E> first = header.getNext();
		
		return first;
	}
	
	/**
	 * This method returns the last Element of the last Node of the list
	 * 
	 * @return the last Element of the list
	 */
	public IPosition<E> last() {
		if (this.isEmpty())
			return null;

		Node<E> last = trailer.getPrev();

		return last;
	}

	/**
	 * This method adds an element to the front of the list
	 * 
	 * @param e - The element to add first
	 */
	public void addFirst(E e) {
		addBetween(e, this.header, this.header.getNext());
	}

	/**
	 * This method adds an element to the end of the list
	 * 
	 * @param e - The element to add last
	 */
	public void addLast(E e) {
		addBetween(e, this.trailer.getPrev(), this.trailer);
	}

	/**
	 * This method adds an element in between two Nodes
	 * 
	 * @param e        - The element to add
	 * @param previous - the previous Node
	 * @param next     - the next Node
	 */
	public void addBetween(E e, Node<E> previous, Node<E> next) {
		Node<E> node = new Node<>(e, previous, next);

		previous.setNext(node);
		next.setPrev(node);
		size++;
	}

	/**
	 * This method removes the first element
	 * 
	 * @return the element that was removed
	 */
	public IPosition<E> removeFirst() {
		return remove(this.header.getNext());
	}

	/**
	 * This method removes the last element
	 * 
	 * @return the element that was removed
	 */
	public IPosition<E> removeLast() {
		return remove(this.trailer.getPrev());
	}

	/**
	 * This method removes a specified Node
	 * 
	 * @param node - The node to be removed
	 * @return the element that was removed
	 */
	public IPosition<E> remove(Node<E> node) {
		E element = node.getElement();

		Node<E> prev = node.getPrev();
		Node<E> next = node.getNext();

		prev.setNext(next);
		next.setPrev(prev);

		size--;

		return node;
	}
	
	/**
	 * This method finds a specific element in the list
	 * @param elem - The element to be found
	 * @return the Node containing the element
	 */
	public IPosition<E> find(E elem)
	{
		Node<E> node = null;
		Node<E> current = this.header.getNext();
		for(int i = 0; i < size; i++)
		{
			if(current.getElement().equals(elem))
			{
				node= current;
				break;
			}
			
			current = current.getNext();
		}
		
		return node;
		
	}

	public java.util.Iterator<E> iterator()
	{
	    return new java.util.Iterator<E>()
	    {
	        private Node<E> current = header.getNext();

	        @Override
	        public boolean hasNext()
	        {
	            return current != trailer;
	        }

	        @Override
	        public E next()
	        {
	            E data = current.getElement();
	            current = current.getNext();
	            return data;
	        }
	    };
	}

	@Override
	public Object next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

}