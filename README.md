cons-compiler
=============
##Contents

*   Set of Data Structures
*   Cons Compiler

#Cons Compiler

#Data Structures

###  Lisp-like Cons Cell
`Cons` cells are essentially singly-linked nodes.

#### Core components
Everything in a `Cons` cell involves three essential components.

	<E> Cons<E> cons(E value, Cons<E> rest)
	<E> E first(Cons<E> c)
	<E> Cons<E> rest(Cons<E> c)

##### Use Case: Stack
These are all you need to start building other data structures right away! <BR/>
Here is a Cons-based Stack implementation. Look at how clean that is! 

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

This may seem a little anti-OOP, but what you lose in OOP, you gain in not having to do pesky null-checks.<BR/>
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

###  Trie

	