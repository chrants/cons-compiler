cons-compiler
=============
##Contents

*   Set of Data Structures
*   Cons Compiler

#Cons Compiler
Uses a Lisp-like `Cons` cell pattern for matching with a intermediary compiler to a lisp-esque language.

### `read(String s)` and `match(Object pattern, Object input)` 
#### `read(string)`
`Cons read(String s)` compiles a String into a lisp-esque cell in Java. <BR/>

#### `match(pattern, input)`
`Cons Cons.match(Object pattern, Object input)`  returns a list of `Cons` bindings. <BR/>
An unknown, which must match and consistently match the same value is labeled `?var_name`. A splat or variadic variable is denoted `?list_name*`, witch (at the moment) does not match consistently acts as kind of 1 or more things buffer until the next thing in the pattern is matched.<BR/>
**Note: Currently splat variables are greedy and have interesting behavior if not used at the end of a pattern match.**

	read("3"); // => 3
	read("abcd"); // => "abcd"
	read("(\"I am a string\" 'me too')"); // Java => list("I am a string", "me too") 
	read("(a b c)"); // Java => list("a", "b", "c")
		// Lisp => (list* 'a 'b 'c NIL)
	read("( (+ 3 5) )"); // Java => list(list("+", 3, 5))
		// Lisp => '((+ 3 5))
	
	Cons pattern = readc("(+ ?a ?b*)"); // Java => list("+", "?a", "?b*")
	match(pattern, readc("(% 3 2)"));
		// Java => null (NO MATCH)
	match(pattern, readc("(+ 1)")); 
		// Java => list(list("?a", 1), list("?b*", list("")))
	match(pattern, readc("(+ 1 2)")); 
		// Java => list(list("?a", 1), list("?b*", list(2)))
	match(pattern, readc("(+ 5 9 (- 4 5) 1)"));
		// Java => list( list("?a", 5), list("?b*", list( 9, list("-", 4, 5), 1 )) );
		// Lisp => '( (?a 5) (?b* '(9 (- 4 5) 1)) )

#Data Structures

###  Lisp-like Cons Cell
`Cons` cells are essentially singly-linked nodes.

#### Core components
Everything in a `Cons` cell involves three essential components.

	static <E> Cons<E> cons(E value, Cons<E> rest)
	static <E> E first(Cons<E> c)
	static <E> Cons<E> rest(Cons<E> c)

##### Use Case: Stack
These are all you need to start building other data structures right away! <BR/>
Here is a `Cons`-based `Stack` implementation. Look at how clean that is! 

	import static structures.Cons.*;
	
	public class Stack<E> {
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
	}

This may seem a little anti-OOP, but what you lose in OOP, you gain in not having to do pesky null-checks.
Consider the following implementation without a Cons-based cell:


	public class Stack<E> {
	
		protected class Node<T> {
			public T value;
			public Node<T> next;
		}
		
		protected Node<E> head;
		
		public void push(E value) {
			Node<E> tmp = head;
			head = new Node<>();
			head.value = value;
			head.next = tmp;
		}
		
		public E peek() {
			if(head == null)
				return null;
			
			return head.value;
		}
		
		public E pop() {
			if(head == null)
				return null;
				
			E tmp = head.value;
			head = head.next;
			return tmp;
		}
	}


#### Other Features


	// Instance Methods/Constructors
	public Cons(E value)
	public Cons(E value, Cons<E> rest)
	public E value()
	public Cons<E> next()
	public boolean all(Predicate<E> pred)
	public boolean exists(Predicate<E> pred)
	public boolean contains(Object o)
	@Override public String toString()
	@Override public boolean equals(Object o)
	@Override public Cons<E> clone()
	public Iterator<E> iterator()
	
	// Static Methods (Null-Checked)
	static <E> E first(Cons<E> c)
	static <E> E second(Cons<E> c)
	static <E> E third(Cons<E> c)
	static <E> E last(Cons<E> c)
	static <E> Cons<E> rest(Cons<E> c)
	static Cons list(Object... elements)
	static Cons read(String s)
	static int length(Cons c)
	static <A, B> Cons<B> mapcar(Cons<A> c, Function<A, B> f)
	static <A, B> Cons<B> mapcan(Cons<A> c, Function<A, B> f)
	static <E> Cons<E> filter(Cons<E> c, Predicate<E> p)


###  Trie

	