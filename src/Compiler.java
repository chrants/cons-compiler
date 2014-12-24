import java.util.StringTokenizer;

import structures.Cons;
import static structures.Cons.*;
import static java.lang.System.out;

public class Compiler {
	
	public Compiler() {
		
	}
	
	public static void main(String... args) {

	}
	
	Cons patterns = readlist(list(
			"(WS '\\s+')",
			"(BOOLEAN (or true false))",
			"(FLOAT '\\d+\\.\\d+')",
			"(INTEGER '\\d+')",
			"(LIST 'LPAREN ?x* RPAREN')",
			"(ID '[a-zA-Z][0-9a-zA-Z]*')"
	));
	
	Cons transformations = readlist(list(
			"( ( '(' ?x* ')' ) (LPAREN ?x* RPAREN) )",
			"( (token (?TokenType ?pattern) ?value) (token ?TokenType ?value) )",
			"( (#{ ?x* }) ?x*)"
	));
	
	public String tokenize(String s) {
		StringTokenizer tokenizer = new StringTokenizer(s, "\n\r\t.();", true);
		
		return "";
	}
	
	private String tokenizeh(StringTokenizer tokenizer) {
		return "";
	}

}
