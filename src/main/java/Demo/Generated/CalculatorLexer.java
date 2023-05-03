package Demo.Generated;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Lexicographer.FrontEnd.Lexer.*;
public class CalculatorLexer implements Lexer {
	//Sorted by priority
	private final String[] types = {null,"string","(",")","float","print","end","{","}","minus","mul","integer","plus","div","equals","variable"};
	private final Pattern[] patterns = {Pattern.compile("\\/\\/.*\\n"),Pattern.compile("\"([^\"]+)\""),Pattern.compile("\\("),Pattern.compile("\\)"),Pattern.compile("(?<!\\w)(\\d+\\.\\d+)(?!\\w)"),Pattern.compile("(?<!\\w)print(?!\\w)"),Pattern.compile(";"),Pattern.compile("\\{"),Pattern.compile("\\}"),Pattern.compile("\\-"),Pattern.compile("\\*"),Pattern.compile("(?<!\\w)(\\d+)[^\\.\\d]{1}(?!\\w)"),Pattern.compile("\\+"),Pattern.compile("\\/"),Pattern.compile("\\="),Pattern.compile("(\\w+)")};
	public ArrayList<Token> tokenize(String file){
		ArrayList<Token> tokens = new ArrayList<>();
		for(int i = 0; i < patterns.length; i++){
			Matcher matcher = patterns[i].matcher(file);
			String type = types[i];
			//Find, add, and remove
			file = matcher.replaceAll(match_result -> {
			if(type != null) { tokens.add(new Token(type, match_result.groupCount() == 1 ?   match_result.group(1) : null, matcher.start())); }
			return String.valueOf("\00".repeat(Math.max(0, matcher.end() - matcher.start()))); //fill string of same size with NUL to keep position.
			});
		}
		//Sort tokens by locations
		tokens.sort(Comparator.comparingInt(Token::getLocation));
		return tokens;
	}
}
