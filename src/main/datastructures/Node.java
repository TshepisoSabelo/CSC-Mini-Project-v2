package datastructures;

public class Node<E> {

	private E element;
	private Node<E> next;
	private Node<E> prev;

	/**
	 * This is a default constructor that sets all attributes of the Node to null
	 */
	public Node() {
		this.element = null;
		this.next = null;
		this.prev = null;
	}
	
	/**
	 * This is a constructor that sets the element with the given variable while next and prev are set to null
	 * @param element - The element of the node to set
	 */
	
	public Node(E element) {
		this.element = element;
		this.next = null;
		this.prev = null;
		
	}
	
	/**
	 * This is a constructor that sets all the attributes with the given variables
	 * @param element - The element of the Node to set
	 * @param prev - The previous Node 
	 * @param next - The next Node
	 */
	public Node(E element, Node<E> prev, Node<E> next)
	{
		this.element = element;
		this.prev = prev;
		this.next = next;
	}

	/**
	 * This is a getter that returns the element of the Node
	 * @return The element of the Node
	 */
	public E getElement() {
		return element;
	}
	/**
	 * This is a setter that sets the element of the Node
	 * @param element - The element to set
	 */

	public void setElement(E element) {
		this.element = element;
	}

	/**
	 * This is a getter that returns the next Node
	 * @return The next Node
	 */
	public Node<E> getNext() {
		return next;
	}

	/**
	 * This is a setter that returns the next Node
	 * @param next - The next Node
	 */
	public void setNext(Node<E> next) {
		this.next = next;
	}

	/**
	 * This is a getter that returns the previous Node
	 * @return The previous Node
	 */
	public Node<E> getPrev() {
		return prev;
	}

	/**
	 * This is a setter that returns the previous Node
	 * @param prev - The previous Node
	 */
	public void setPrev(Node<E> prev) {
		this.prev = prev;
	}

}