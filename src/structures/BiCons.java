package structures;

public class BiCons<E> extends Cons<E> {

	public Cons<E> prev;
	
	public BiCons(E val) {
		super(val);
	}
	
	public BiCons(E val, Cons<E> next) {
		super(val, next);
	}
	
	public BiCons(Cons<E> prev, E val) {
		super(val);
		this.prev = prev;
	}
	
	public Cons<E> prev() {
		return prev;
	}
	
	public static <E> Cons<E> prev(BiCons<E> c) {
		return (c == null ? null : c.prev);
	}
	
}
