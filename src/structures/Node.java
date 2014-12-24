package structures;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Node<E> implements Nodal<E> {
	
	public E value;
	public List<Node<E>> children = new ArrayList<Node<E>>();
	
	public Node(E val) {
		this.value = val;
	}
	
	public Node(E val, List<Node<E>> children) {
		this.value = val;
		children.addAll(children);
	}
	
	public void add(E n) {
		children.add(node(n));
	}
	
	public void add(Node<E> n) {
		children.add(n);
	}
	
	public static <E> Node<E> node(E val) {
		return new Node<E>(val);
	}

	@Override
	public E value() {
		return value;
	}

	@Override
	public Collection<? extends Nodal<E>> children() {
		return children;
	}

}
