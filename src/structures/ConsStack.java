package structures;

import static structures.Cons.*;

import java.util.Iterator;

public class ConsStack<E> implements Iterable<E> {
	protected Cons<E> head;
	
	public void push(E value) {
		head = cons(value, head);
	}
	
	public E peek() {
		return first(head);
	}
	
	public E pop() {
		E tmp = peek();
		head = rest(head);
		return tmp;
	}

	@Override
	public Iterator<E> iterator() {
		if(head == null)
			return null;
		return head.iterator();
	}
}
