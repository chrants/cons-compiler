package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeSet;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import structures.Cons;
import static structures.Cons.*;

public class ConsTest {
	
	@Test
	public void equalsTest() {
		assertTrue(Cons.equals(null, null));
		assertFalse(Cons.equals(null, readc("()")));
		assertFalse(Cons.equals(readc("()"), null));
		
		assertTrue(Cons.equals(1, 1));
		assertFalse(Cons.equals(1, null));
		assertFalse(Cons.equals(null, 1));
		assertFalse(Cons.equals(1, 2));
		assertFalse(Cons.equals("2", 2));
		assertTrue(Cons.equals(read("2"), 2));
//		assertTrue(Cons.equals(2.0, 2));
		assertTrue(Cons.equals(2, first(list(2, 3, 4))));
		
		assertEquals(list(1, 2, 3), first(list(list(1, 2, 3))));
		assertNotEquals(list(1, 2, 3), list(list(1, 2, 3)));
		
		assertNotEquals(list("a"), list(list("a")));
		assertNotEquals(list(list("a")), list("a"));
		
		assertFalse(list("a").equals("a"));
		assertFalse(list("a").equals("(a)"));
	}
	
	@Test
	public void consIterates() {
		Cons c = list(2, 3, 4, 5, 6);
		int a = 2;
		for(Object i : c) {
			assertEquals(a, i);
			a++;
		}
	}

	@Test
	public void reader() {
		assertEquals(read("(a b c)"), list("a", "b", "c"));
		assertEquals(read("(a)"), list("a"));
		assertEquals(read("()"), list(""));
		assertEquals(read("("), list(""));
		assertEquals(read("( 2\t"), list(2));
		assertEquals(read("('3')"), list("3"));
		assertEquals(read("('3 4 5')"), list("3 4 5"));
		assertEquals(read("(\"3\")"), list("3"));
		assertEquals(read("(\"( 3 '\")"), list("( 3 '"));
		assertEquals(read("'3'"), "3");
		assertEquals(read("('3' 4 5)"), list("3", 4, 5));
		assertEquals(read("('3'"), list("3"));
		assertEquals(read("(("), list(list("")));
		assertEquals(read("(()"), list(list("")));
//		assertEquals(read("('3\""), list("3"));
		assertEquals(read("     "), null);
		assertEquals(read(""), null);
		assertEquals(read("1"), 1);
		assertEquals(read("1.2345"), 1.2345);
		assertEquals(read("(())"), list(list("")));
		assertEquals(read("((()) 3)"), list(list(list("")), 3));
		assertEquals(read("( ( ) )"), list(list("")));
		assertEquals(read(")"), ")");
		assertEquals(read("o"), "o");
		assertEquals(read("((1) (2) (3))"), list(list(1), list(2), list(3)));
		assertEquals(read("\t(\t(\t)\t3\tb\t(\t)\t)\t"), list(list(""), 3, "b", list("")));
	}
	
	@Test
	public void readlistTest() {
		assertEquals(list(list("")), readlist(list("()")));
		assertEquals(list(list(3)), readlist(list("(3)")));
		assertEquals(list(list(3), 4), readlist(list("(3)", "4")));
	}
	
	@Test
	public void filterTest() {
		Cons tree = readc("( (3) (a b) () (1 2) (3 4 5) (() ()) ((a 1 2 3) (1 2)) 4 )");
		assertNull(filter(tree, obj -> obj.equals(29)));
		assertEquals(readc("( (3) (a b) () (1 2) (3 4 5) (() ()) ((a 1 2 3) (1 2)) )"), filter(tree, el -> consp(el)));
		assertEquals(readc("( (a b) (1 2) (() ()) ((a 1 2 3) (1 2)) )"), filter(tree, obj -> match(read("(?a ?b)"), obj) != null));
		assertEquals(readc("( (3) )"), filter(tree, el -> match(read("( ?a )"), el) != null));
	}
	
	@Test
	public void substTest() {
		Cons c = list(1, 2, 3);
		assertEquals(list(1, 2, 4), subst(4, 3, c));
		// Check for destructiveness
		assertEquals(list(1, 2, 3), c);
		
		assertEquals(read("(a b c)"), sublis(readc("((1 a) (2 b) (3 c))"), c));
		// Check for destructiveness
		assertEquals(list(1, 2, 3), c);
	}
	
/*	@Test
	public void whereTest() {
		Cons tree = readc("( ((1 2 3) ())		(a)		(())		(a b c)		(() (a b c) (1 2 3)) )");
		assertNull(where(tree, obj -> obj.equals(10)));
		assertEquals(read("(a a a)"), where(tree, obj -> obj.equals("a")));
		assertEquals(read("( (()) )"), where(tree, obj -> obj.equals(read("(())"))));
//		assertEquals(read("( (a) (()) )"), match(read("(?a)"), tree));
	}*/
	
	@Test
	public void fromCollectionTest() {
		Collection col = new LinkedList();
		col.add(1); col.add(2); col.add(5);
		assertEquals(list(1, 2, 5), fromCollection(col));
		
		col = new TreeSet();
		col.add(1); col.add(1); col.add(4); col.add((int) 'a');
		assertEquals(list(1, 4, (int) 'a'), fromCollection(col));
		
		col = new ArrayList();
		Collection tmp = new ArrayList();
		tmp.add(1); tmp.add("b");
		col.add(tmp);
		assertEquals(list(1, "b"), fromCollection(tmp));
		assertEquals(list(list(1, "b")), fromCollection(col));
		
		tmp = new TreeSet();
		tmp.add("abcd"); tmp.add("z");
		col.add(tmp);
		assertEquals(read("(abcd z)"), fromCollection(tmp));
		assertEquals(read("((1 b) (abcd z))"), fromCollection(col)); 
	}
	
	@Test
	public void matchTest() {
		// Simple precondition null checks.
		assertNull(match(null, readc("(1 2 3)")));
		assertNull(match(null, null));
		assertNull(match(readc("(?a)"), null));
		
		Cons pat = readc("(?a ?b ?c)");
//		System.out.println(pat);
//		System.out.println(read("( (?a (1)) (?b (2)) (?c (3)) )"));
//		System.out.println(match(pat, list(list(1), list(2), list(3))));
//		System.out.println(match(pat, list(1, 2, 3)));
		assertEquals(read("( (?a 1) (?b 2) (?c 3) )"), match(pat, list(1, 2, 3)));
		assertEquals(read("( (?a (1)) (?b (2)) (?c (3)) )"), match(pat, list(list(1), list(2), list(3))));
		
		pat = readc("(+ ?a ?b)");
		assertEquals(read("((?a 0) (?b 1))"), match(pat, read("(+ 0 1)")));
		assertEquals(read("( (?a (* 3 4)) (?b (exp (- 5))) )"), match(pat, read("(+ (* 3 4) (exp (- 5)))")));
		
		pat = readc("(?a*)");
		assertEquals(read("( (?a* (1)) )"), match(pat, read("(1)")));
		assertEquals(read("( (?a* (5 4 3 2 1)) )"), match(pat, read("(5 4 3 2 1)")));
		
		pat = readc("(+ ?first ?rest*)");
		assertEquals(read("( (?first 0) (?rest* (1)) )"), match(pat, read("(+ 0 1)")));
		assertEquals(read("( (?first 0) (?rest* (1 2)) )"), match(pat, read("(+ 0 1 2)")));
		
		pat = readc("(* ?a ?b* ?c");
		assertEquals(read("( (?a 1) (?b* ()) (?c 2) )"), match(pat, read("(* 1 2)")));
		assertEquals(read("( (?a 1) (?b* (2)) (?c 3))"), match(pat, read("(* 1 2 3)")));
		assertEquals(read("( (?a 1) (?b* (2 5)) (?c 3))"), match(pat, read("(* 1 2 5 3)")));
		assertEquals(read("( (?a 1) (?b* ((a b c d))) (?c 3))"), match(pat, read("(* 1 (a b c d) 3)")));
		assertEquals(read("( (?a 1) (?b* ((a b c d) 2 4)) (?c 3))"), match(pat, read("(* 1 (a b c d) 2 4 3)")));
	}
	
	@Test
	public void appender() {
		assertEquals(list(1, 2), append(list(1), list(2)));
		assertEquals(list(2, 3, 4), append(list(2), list(3, 4)));
//		assertEquals(list(list(), list()), append(list(), list()));
	}
	
	@Test
	public void lister() {
		assertEquals(list(3), cons(3, null));
		assertEquals(list(4, 5), cons(4, cons(5, null)));
		assertEquals(list(), null);
		assertEquals(list(""), list(new String()));
		assertThat(list(list(2), 3), is(
				allOf(
						equalTo(cons(list(2), cons(3, null))),
						equalTo(cons(cons(2, null), cons(3, null)))
				)
		));
	}
	
	@Test
	@Ignore
	public void cyclicTest() {
		Cons c = list(1);
		c.next = c;
		
		assertTrue(cyclic(c));
		assertFalse(acyclic(c));
		
		assertTrue(removeCycle(c));
		assertNull(c.next);
		assertEquals(c, list(1));
		
		c = list(1, 2, 3, 4, 5);
		lastc(c).next = c;
		
		assertTrue(cyclic(c));
		assertFalse(acyclic(c));
		
		assertTrue(removeCycle(c));
		assertNull(lastc(c).next);
		assertEquals(c, list(1, 2, 3, 4, 5));
	}
	
}
