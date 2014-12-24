package structures;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.System.out;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Cons<E> implements Iterable<E> {
	public Cons<E> next;
	public E value;
	
	public Cons(E val) {
		this.value = val;
	}
	
	public Cons(E val, Cons<E> n) {
		this.value = val;
		this.next = n;
	}
	
	public E value() {
		return value;
	}
	
	public Cons<E> next() {
		return next;
	}
	
	public E setValue(E newval) {
		E temp = value;
		value = newval;
		return temp;
	}
	
	public Cons<E> setNext(Cons<E> newnext) {
		Cons<E> temp = next;
		next = newnext;
		return temp;
	}
	
	public boolean exists(Predicate<E> pred) {
		for(E val : this)
			if(pred.test(val))
				return true;
		
		return false;
	}
	
	public boolean all(Predicate<E> pred) {
		for(E val : this)
			if(!pred.test(val))
				return false;
		
		return true;
	}
	
	public boolean contains(Object obj) {
		return existant(this, obj);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!consp(obj))
			return false;
		else {
			Cons o = (Cons) obj;
			return equals(this.value, o.value) && equals(this.next, o.next);
		}
	}
	
	public static boolean equals(Object a, Object b) {
		if(a == null ^ b == null)
			return false;
		else 
			return a == b || a.equals(b);
	}
	
	@Override
	public Cons<E> clone() {
		return nreverse(nreverse(this));
	}
	
	public static <E> Cons<E> clone(Cons<E> c) {
		if(c == null) return null;
		else return c.clone();
	}
	
	// convert a list to a string for printing
	@Override
	public String toString() {
		return ("(" + toStringb(this));
	}

	public static String toString(Cons lst) {
		return ("(" + toStringb(lst));
	}

	private static String toStringb(Cons lst) {
		return ((lst == null) ? ")" : (first(lst) == null ? "()" : first(lst)
				.toString())
				+ ((rest(lst) == null) ? ")" : " " + toStringb(rest(lst))));
	}
	
	/**
	 * Destructive reverse. Must set to desired reverse pointer or will be lost.
	 * @param lst
	 * @return
	 */
	public static <E> Cons<E> nreverse (Cons<E> lst) {
		Cons<E> last = null, next;
		while (lst != null) {
			next = rest(lst);
			setrest(lst, last);
			last = lst;
			lst = next; 
		}
		return last; 
	}
	
	public static <E> boolean exists(Cons<E> c, Predicate<E> p) {
		if(c == null || p == null) 
			return false;
		else
			return c.exists(p);
	}
	
	public static <E> boolean all(Cons<E> c, Predicate<E> p) {
		if(c == null || p == null)
			return true;
		else
			return c.all(p);
	}
	
	public static boolean existant(Cons c, Object o) {
		if(c == null) 
			return false;
		else 
			return equals(c.value, o) || existant(rest(c), o);
	}
	
	public static Cons cons(Object o, Cons n) {
		return new Cons(o, n);
	}
	
	public static boolean consp(Object o) {
		return (o != null && o instanceof Cons);
	}
	
	public static <E> Cons<E> next(Cons<E> n) {
		return rest(n);
	}
	
	public static <E> Cons<E> rest(Cons<E> n) {
		return (n == null ? null : n.next);
	}
	
	public static Object first(Cons c) {
		return (c == null ? null : c.value);
	}
	
	public static Object second(Cons c) {
		return first(rest(c));
	}
	
	public static Object third(Cons c) {
		return first(rest(rest(c)));
	}
	
	public static Object nth(Cons c, int n) {
		for(int i = 0; c != null && i < n; i++) {
			c = rest(c);
		}
		return first(c);
	}
	
	public static <E> E last(Cons<E> c) {
		Object o = first(lastc(c));
		return o == null ? null : (E) o;
	}
	
	public static <E> Cons<E> lastc(Cons<E> c) {
		Cons<E> temp;
		for(temp = c; next(temp) != null; temp = temp.next);
		return temp;
	}
	
	public static <E> E setfirst(Cons<E> c, E val) {
		if(c == null) return null;
		return c.setValue(val);
	}
	
	public static <E> Cons<E> setrest(Cons<E> a, Cons<E> b) {
		if(a == null) return null;
		return a.setNext(b);
	}
	
	public static Cons nthrest(Cons c, int n) {
		if(n < 0) 
			return null;
		for(int i = 0; c != null && i < n; i++) {
			c = rest(c);
		}
		return c;
	}
	
	public static <E> Cons<E> append(Cons<E> a, Cons<E> b) {
		if (a == null)
			return b;
		else
			return cons(first(a), append(rest(a), b));
	}
	
	public static <E> void appendd(Cons<E> a, Cons<E> b) {
		setrest(lastc(a), b);
	}
	
	public static Cons reverse(Cons c) {
		Cons lst;
		for(lst = null; c != null; c = rest(c)) {
			lst = cons(first(c), lst);
		}
		return lst;
	}
	
	public static <E> E value(Cons<E> n) {
		return (n == null ? null : n.value);
	}
	
	public static Cons list(Object... elements) {
		Cons list = null;
		for (int i = elements.length - 1; i >= 0; i--) {
			list = cons(elements[i], list);
		}
		return list;
	}
	
	public static boolean cyclic(Cons c) {
		if(c == null || c.next == null) 
			return false;
		Cons slow = c.next, fast = c.next.next;
		while(fast != null) {
			// Compare actual pointers
			if(slow == fast)
				// Cycle found
				return true;
			slow = slow.next;
			// next(...) can give null w/o throwing error.
			fast = next(next(fast));
		}
		return false;
	}
	
	public static boolean removeCycle(Cons c) {
		if(c == null || c.next == null) 
			return false;
		Cons slow = c.next, fast = c.next.next;
		while(fast != null) {
			// Compare actual pointers
			if(slow == fast) {
				// Cycle found
				removeCycle_helper(c, slow);
				return true;
			}
			slow = slow.next;
			// next(...) can give null w/o throwing error.
			fast = next(next(fast));
		}
		return false;
	}
	
	protected static void removeCycle_helper(Cons head, Cons slow) {
		int k = countNodesInCycle(slow);
		
		Cons kth = head;
		for(int i = 0; i < k; i++) {
			kth = head.next;
		}
		
		Cons temp = head;
		while(temp != kth) {
			kth = kth.next;
			temp = temp.next;
		}
		
		// Alternative (more ambiguous as to what nums are though):
		/*for(int i = 1; i < k; i++) {
			temp = temp.next;
		}*/
		
		temp = temp.next;
		while(temp.next != kth)
			temp = temp.next;
		
		temp.next = null;
	}
	
	/**
	 * 
	 * @param node - a node in the cycle
	 * @return
	 */
	protected static int countNodesInCycle(Cons node) {
		Cons temp = node.next;
		int k;
		for(k = 1; temp.next != node; k++) {
			if(temp == null)
				return -1;
			temp = temp.next;
		}
		return k;
	}
	
	public static boolean acyclic(Cons c) {
		return !cyclic(c);
	}
	
	
	
	
    // iterative version of length
	public static int length (Cons lst) {
		if(cyclic(lst)) // Avoid infinite loop. Potentially destructive
			removeCycle(lst);

		int n = 0;
		while ( lst != null ) {
			n++;
			lst = rest(lst); }
		return n; 
	}
	
	
	// member returns null if requested item not found
	public static Cons member (Object item, Cons lst) {
	  if ( lst == null )
	     return null;
	   else if ( item.equals(first(lst)) )
	           return lst;
	         else return member(item, rest(lst)); }
	
	public static Cons union (Cons x, Cons y) {
	  if ( x == null ) return y;
	  if ( member(first(x), y) != null )
	       return union(rest(x), y);
	  else return cons(first(x), union(rest(x), y)); }
	
	public static boolean subsetp (Cons x, Cons y) {
	    return ( (x == null) ? true
	             : ( ( member(first(x), y) != null ) &&
	                 subsetp(rest(x), y) ) ); }
	
	
	
	
	public static Cons WS = list(" ", "\t", "\r", "\n", "\f");
	public static Cons QUOTES = list("\"", "'");
	public static String INTEGER = "\\d+";
	public static String FLOAT = "\\d+\\.\\d+";
	
	public static Object read(String s) {
		StringTokenizer st = new StringTokenizer(s, "\n\t\r\f ()'\"", true);
		return readerb(st);
	}
	
	private static Object readerb(StringTokenizer st) {
		if(st.hasMoreTokens()) {
			String token = st.nextToken();
//			out.println("token:\t" + token);
			if(existant(WS, token))
				return readerb(st);
			else if(existant(QUOTES, token.charAt(0) + ""))
				return readstr(token, st);
			else if(token.charAt(0) == '(')
				return readerlist(st);
			else if(token.matches(FLOAT))
				return Double.parseDouble(token);
			else if(token.matches(INTEGER))
				return Integer.parseInt(token);
			else if(token.equals(")"))
				return ")";
			else
				return token;
		} else
			return null;
	}
	
	private static String readstr(String open, StringTokenizer st) {
		StringBuffer sb = new StringBuffer();
		String lastToken = "";
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if(token.equals(open) && (!lastToken.endsWith("\\") || lastToken.endsWith("\\\\")))
				break;
			else 
				sb.append(token);
			lastToken = token;
		}
		return sb.toString();
	}
	
	private static Cons readerlist(StringTokenizer st) {
		Cons cns = null;
		while(st.hasMoreTokens()) {
			Object o = readerb(st);
			if(o == null || o.equals(")"))
				break;
			cns = cons(o, cns);
		}
		if(cns == null)
			cns = list("");
		return nreverse(cns);
	}
	
	public static Object copy_tree(Object tree) {
		if (consp(tree))
			return cons(copy_tree(first((Cons) tree)),
					(Cons) copy_tree(rest((Cons) tree)));
		return tree;
	}

	/**
	 * Substitute <code>old</code> for <code>gnew</code> in the binary Cons <code>tree</code>.
	 * Constructive
	 * @param gnew
	 * @param old
	 * @param tree
	 * @see Cons#sublis(Cons, Object)
	 * @return <code>tree</code> with everything that <code>.equals(old)</code> replaced by <code>gnew</code>.
	 */
	public static Object subst(Object gnew, Object old, Object tree) {
		if (consp(tree))
			return cons(subst(gnew, old, first((Cons) tree)),
					(Cons) subst(gnew, old, rest((Cons) tree)));
		return (old == tree || old.equals(tree)) ? gnew : tree;
	}

	/**
	 * Replaces the binding values in <code>alist</code> from its keys found in <code>tree</code>.
	 * Constructive
	 * @param alist
	 * @param tree
	 * @see Cons#subst(Object, Object, Object)
	 * @return <code>tree</code> with everything that <code>.equals(first(assoc))</code> replaced
	 *  by <code>second(assoc)</code>, where assoc is a binding.
	 */
	public static Object sublis(Cons alist, Object tree) {
		if (consp(tree))
			return cons(sublis(alist, first((Cons) tree)),
					(Cons) sublis(alist, rest((Cons) tree)));
		if (tree == null)
			return null;
		Cons pair = assoc(tree, alist);
		return (pair == null) ? tree : second(pair);
	}
	
	/*// Filter for whole tree.
	// Stable (Keeps order) and In-Place (I think...)
	public static Object wheredepth(Object tree, Predicate p) {
		if (tree == null || p == null) 
			return null;
		
		 if (p.test(tree)) {
//			 out.println("RETURN:\t" + tree);
			return list(tree);
		 } else if (consp(tree)) {
			Object temp = wheredepth(first((Cons) tree), p);
//			out.println(temp);
			if(temp == null)
				return wheredepth(rest((Cons) tree), p);
			else 
				return append((Cons) temp, (Cons) wheredepth(rest((Cons) tree), p));
		} else
			return null;
	}
	
//	private static MapReduce mr = new MapReduce(, r);
	// Filter for whole tree.
		// Stable (Keeps order) and In-Place (I think...)
		public static Object where(Object tree, Predicate p) {
			Cons c = (Cons) wheredepth(tree, p);
			out.println("WHERE: " + c);
			for(Cons tmp = c; tmp != null; tmp = tmp.next) {
				while(!p.test(tmp.value))
					tmp.value = ((Cons) tmp.value).value;
			}
			return c;
		}*/
		
		/*return (consp(left) && consp(right)) ?
		(p.test(left) || p.test(right) ? 
				append(p.test(left) ? list(left) : (Cons) left, p.test(right) ? list(right) : (Cons) right) :
				cons(first((Cons) left), (Cons) right)) :
		(consp(left) ? 
				(p.test(left) ? cons((Cons) left, list(right)) : cons(first((Cons) left), list(right))) :
				(p.test(right) ? cons(left, list(right)) : cons(left, (Cons) right)));*/
	
	// Filters only top cons layer.
	public static <E> Cons<E> filter(Cons<E> tree, Predicate<E> p) {
		if(tree == null) return null;
		Cons<E> list = null;
		for(E o : tree)
			if(p.test(o))
				list = cons(o, list);
		return reverse(list);
	}
	
	public static Cons readc(String s) {
		return (Cons) read(s);
	}
	
	public static Cons readlist(Cons lst) {
		if (lst == null)
			return null;
		return cons(read((String) first(lst)), readlist(rest(lst)));
	}
	
	// look up key in an association list
	// (assoc 'two '((one 1) (two 2) (three 3))) = (two 2)
	public static Cons assoc(Object key, Cons lst) {
		if (lst == null)
			return null;
		else if (key.equals(first((Cons) first(lst))))
			return ((Cons) first(lst));
		else
			return assoc(key, rest(lst));
	}
	
	public static <A, B> Cons<B> mapcar(Cons<A> c, Function<A, B> f) {
		if(c == null) return null;
		Cons<B> result = null;
		for(A a: c) 
			result = cons(f.apply(a), result);
		return reverse(result);
	}
	
	public static <A, B> Cons<B> mapcan(Cons<A> c, Function<A, B> f) {
		if(c == null) return null;
		Cons<B> result = null;
		for(A a: c) {
			B temp = f.apply(a);
			if(temp != null)
				result = cons(temp, result);
		}
		return reverse(result);
	}
	
	public static Cons dummysub = list(list("t", "t"));

	public static Cons match(Object pattern, Object input) {
		// Get rid of dummysub in result
		return mapcar(rest(reverse(matchb(pattern, input, dummysub))), 
				bind -> splatvarp(first((Cons) bind)) ? 
						cons(first((Cons) bind), list(reverse((Cons) second((Cons) bind)))) : 
						bind);
	}

	protected static Cons BLANK_CONS_READ = readc("()");
	
	// ?a matches anything once.
	// ?b* matches anything any number of times >= 0 *greedy* 
	public static Cons matchb(Object pattern, Object input, Cons bindings) {
		out.println("pat:\t\t\t" + pattern);
		out.println("input:\t\t" + input);
		out.println("bindings:\t" + bindings);
		
		if (bindings == null /*|| (pattern == null ^ input == null)*/)
			return null;
		
		if(BLANK_CONS_READ.equals(pattern) && BLANK_CONS_READ.equals(input))
			return bindings;
		else if(BLANK_CONS_READ.equals(input) && consp(pattern) && !equals(first((Cons) pattern), ""))
			return null;
		
		if (consp(pattern)) {
			if (consp(input)) {
				out.println("\tCONSP>");
				// TODO: Check lengths left for patterns such as (?a ?b* ?c) against (1 2 3 4) => ((?a 1) (?b (2 3)) (?c 4))
				if(splatvarp(first((Cons) pattern))) { //!( first((Cons) input).equals(rest((Cons) pattern)) )
					Cons notSplats = filter(rest((Cons) pattern), pat -> !splatvarp(pat));
					Cons inpc = rest((Cons) input), temp = inpc;
					
					// iff inpc == null then we can try splatvarp.
					// unless there are no more patterns, in which case match all of the rest.
					boolean backup = rest((Cons) pattern) == null;
					if(!backup) {
						for(Object pat : notSplats) {
							if(matchb(pat, first(inpc), bindings) != null)
								inpc = rest(inpc);
						}
						
						backup = (inpc != null && length(notSplats) < length(temp));
					}
					
					if(backup)
						return matchb(
								pattern,
								rest((Cons) input),
								matchb(first((Cons) pattern), first((Cons) input),
										bindings));
					// Else If pattern exists that's needed to match right now, match by falling through
					
					//					if(rest((Cons) pattern) == null) { // Collect rest
					
					/*} else if(all(rest((Cons) input), o -> o != second((Cons) pattern))); */
				
				}  
				
				// Default case
				return matchb(
					rest((Cons) pattern),
					rest((Cons) input),
					matchb(first((Cons) pattern), first((Cons) input),
							bindings));
				
			} else { // pattern is a cons, but not input. (input is prob. null)
				if(splatvarp(first((Cons) pattern)) && input == null && rest((Cons) pattern) == null)
					return bindings;
				else
					return null;
			}
		}
		// I added this.
		if (splatvarp(pattern)) {
			out.print("\tS_VARP>");
			Cons binding = assoc(pattern, bindings);
			out.println(binding);
			if(input == null)
				return bindings;
			if (binding != null) {
//				if (((Cons) second(binding)).contains(input))
//					return bindings;
//				else {
					// TODO: Reverse when stop matching this binding.
					binding.next.value = cons(input, (Cons) second(binding));
					return bindings;
//				}
			} else {
				return cons(list(pattern, list(input)), bindings);
			}
		}
		if (varp(pattern)) {
			out.println("\tVARP>");
			Cons binding = assoc(pattern, bindings);
			if (binding != null)
				if (equals(input, second(binding)))
					return bindings;
				else
					return null;
			else
				return cons(list(pattern, input), bindings);
		}
//		out.println(pattern);
//		out.println(input);
		if(equals(pattern, input))
			return bindings;
		return null;
	}
	

	/*private static Cons matchsplat(Object pattern, Object input, Cons bindings) {
		
	}*/
	
	public static Object transform(Cons patpair, Cons input) {
		Cons bindings = match(first(patpair), input);
		if (bindings == null)
			return null;
		return sublis(bindings, second(patpair));
	}

	// Transform a list of arguments. If no change, returns original.
	public static Cons transformlst(Cons allpats, Cons input) {
		if (input == null)
			return null;
		Cons restt = transformlst(allpats, rest(input));
		Object thist = transformr(allpats, first(input));
		if (thist == first(input) && restt == rest(input))
			return input;
		return cons(thist, restt);
	}

	// Transform a single item. If no change, returns original.
	public static Object transformr(Cons allpats, Object input) {
		// System.out.println("transformr:  " + input.toString());
		if (consp(input)) {
			Cons listt = transformlst(allpats, (Cons) input);
			// System.out.println("   lst =  " + listt.toString());
			return transformrb(allpats, allpats, transformlst(allpats, listt));
		}
		Object res = transformrb(allpats, allpats, input);
		// System.out.println("   result =  " + res.toString());
		return res;
	}

	// Transform a single item. If no change, returns original.
	public static Object transformrb(Cons pats, Cons allpats, Object input) {
		if (pats == null)
			return input;
		if (input == null)
			return null;
		Cons bindings = match(first((Cons) first(pats)), input);
		if (bindings == null)
			return transformrb(rest(pats), allpats, input);
		return sublis(bindings, second((Cons) first(pats)));
	}

	// Transform a single item repeatedly, until fixpoint (no change).
	public static Object transformfp(Cons allpats, Object input) {
		// System.out.println("transformfp: " + input.toString());
		Object trans = transformr(allpats, input);
		if (trans == input)
			return input;
		// System.out.println("    result = " + trans.toString());
		return transformfp(allpats, trans);
	} // potential loop!
	
	public static boolean varp(Object x) {
		return (stringp(x) && (((String) x).charAt(0) == '?'));
	}
	
	public static boolean splatvarp(Object x) {
		return (varp(x) && ((String) x).endsWith("*"));
	}
	
	public static boolean numberp(Object x) {
		return ((x != null) && (x instanceof Integer || x instanceof Double));
	}

	public static boolean integerp(Object x) {
		return ((x != null) && (x instanceof Integer));
	}

	public static boolean floatp(Object x) {
		return ((x != null) && (x instanceof Double));
	}

	public static boolean stringp(Object x) {
		return ((x != null) && (x instanceof String));
	}
	
	public static Cons fromCollection(Collection col) {
		Cons cns = cons(null, null), temp = cns;
		for(Object e : col) {
			temp = temp.next = cons(e, null);
			// Recursively to a cons
			if(e instanceof Collection) {
				temp.value = fromCollection((Collection) e);
			}
		}
		return cns.next;
	}

	@Override
	public Iterator<E> iterator() {
		final Cons<E> temp = this;
		return new Iterator<E>() {
			Cons<E> next = temp;
			
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public E next() {
				if(next == null) 
					return null;
				E v = next.value;
				next = next.next;
				return v;
			}
		};
	}
}
