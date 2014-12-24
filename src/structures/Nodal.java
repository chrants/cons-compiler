package structures;

import java.util.Collection;

public interface Nodal<E> {
	
	public void add(E val);
	public Collection<? extends Nodal<E>> children();
	public E value();
	
	public static boolean nodep(Object o) {
		return (o != null && o instanceof Nodal);
	}
	
//	public default boolean equals(Object obj) {
//		// TODO Auto-generated method stub
//		return (obj == null ? false : (obj instanceof Nodal ? this.value().equals(((Nodal) obj).value()) : false));
//	}
	
}
