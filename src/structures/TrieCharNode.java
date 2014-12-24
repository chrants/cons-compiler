package structures;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import structures.TrieNode;

public class TrieCharNode extends TrieNode<Character> {

		public char prefix;
		public TreeSet<TrieCharNode> children = new TreeSet<TrieCharNode>((a, b) -> {
			return a.prefix - b.prefix;
		});
		
		protected TrieCharNode() {
			
		}
		
		public TrieCharNode(char val) {
			this.prefix = val;
		}
		
		public TrieCharNode(char val, Set<TrieCharNode> children) {
			this.prefix = val;
			children.addAll(children);
		}
		
		public boolean add(String s) {
			if(s == null)
				return false;
			else if(s.length() == 0) {
				return children.add(new TrieCharNode());
			} else {
				char first = s.charAt(0);
				String rest = s.substring(1);
				TrieCharNode next = tnode(first);
				if(children.contains(next)) {
					next = children.ceiling(next);
				} else {
					children.add(next);
				}
				return next.add(rest);
			}
		}

		@Override
		public void add(Character val) {
			children.add(tnode(val));
		}

		@Override
		public Character value() {
			return prefix;
		}
		
		public static TrieCharNode tnode(char v) {
			return new TrieCharNode(v);
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj != null && 
					(
							(obj instanceof Character && ((char) obj) == this.prefix) || 
							(obj instanceof TrieCharNode && this.prefix == ((TrieCharNode) obj).prefix)
					)
			;
		}

		@Override
		public Collection<? extends Nodal<Character>> children() {
			// TODO Auto-generated method stub
			return children;
		}

}
