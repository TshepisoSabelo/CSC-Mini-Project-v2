package datastructures;

public class Node<E> implements IPosition<E>
{
    private E element;
    private Node<E> next;
    private Node<E> prev;

    public Node()
    {
        this.element = null;
        this.next = null;
        this.prev = null;
    }

    public Node(E element)
    {
        this.element = element;
        this.next = null;
        this.prev = null;
    }

    public Node(E element, Node<E> prev, Node<E> next)
    {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public E getElement()
    {
        return element;
    }

    public void setElement(E element)
    {
        this.element = element;
    }

    public Node<E> getNext()
    {
        return next;
    }

    public void setNext(Node<E> next)
    {
        this.next = next;
    }

    public Node<E> getPrev()
    {
        return prev;
    }

    public void setPrev(Node<E> prev)
    {
        this.prev = prev;
    }
}