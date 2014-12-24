package structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// Not really implemented as a StringTrie; a StringTrie lumps characters together as a form of compression and is ~O(1) time
public class CharSeqTrie {
	
	protected TrieCharNode root;
	
	public CharSeqTrie() {
		root = new TrieCharNode();
	}
	
	public boolean contains(String s) {
		return contains(s, root);
	}
	
	protected boolean contains(String s, TrieCharNode n) {
		if(s == null || n == null)
			return false;
		else
			return n.children.contains(TrieCharNode.tnode(s.charAt(0))) && 
				contains(s.substring(1), n.children.ceiling(TrieCharNode.tnode(s.charAt(0))));
	}
	
	public boolean containsPrefix(String s) {
		return containsPrefix(s, root);
	}
	
	protected boolean containsPrefix(String s, TrieCharNode n) {
		if(s == null)
			return false;
		else if(s.length() == 0)
			return true;
		else if(n == null)
			return false;
		else
			return n.children.contains(TrieCharNode.tnode(s.charAt(0))) && 
				containsPrefix(s.substring(1), n.children.ceiling(TrieCharNode.tnode(s.charAt(0))));
	}
	
	public boolean add(String s) {
		if(s == null)
			return false;
		if(root.prefix == 0)
			root = TrieCharNode.tnode(s.charAt(0));
		return root.add(s.substring(1));
	}
	
	// TODO: Add a way to add a value to the end of a certain thing.

}
