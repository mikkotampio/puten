package fi.purkka.puten.parser;

import java.util.ArrayList;
import java.util.List;

import fi.purkka.puten.lexer.Token;
import fi.purkka.puten.lexer.Token.TokenType;

public class Parser {
	
	private final List<Token> tokens;
	private int index = 0;
	
	private Parser(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	private Body toBody() {
		Body body = new Body();
		while(index < tokens.size()) {
			body.append(next());
		}
		return body;
	}
	
	private Node next() {
		if(accept(TokenType.BACKSLASH)) {
			if(accept(TokenType.STRING)) {
				return nextCommand(tokens.get(index-1).content);
			}
			return new Text(Token.BACKSLASH);
		}
		
		if(atEnd()) throw new ParseException(TokenType.STRING, "<EOF>");
		return new Text(tokens.get(index++));
	}
	
	private boolean accept(TokenType type) {
		if(atEnd()) return false;
		if(tokens.get(index).type == type) {
			index++;
			return true;
		}
		return false;
	}
	
	private boolean atEnd() {
		return index >= tokens.size();
	}
	
	private Token expect(TokenType type) {
		if(atEnd()) throw new ParseException(type, "<EOF>");
		if(tokens.get(index).type != type) {
			throw new ParseException(type, tokens.get(index).type);
		}
		return tokens.get(index++);
	}
	
	private Node nextCommand(String name) {
		if(atEnd()) {
			return new VariableAccess(name);
		}
		
		if(accept(TokenType.WHITESPACE)) {
			index--;
			return new VariableAccess(name);
		}
		
		List<Node> args = new ArrayList<>();
		
		if(accept(TokenType.COLON)) {
			args.add(nextArg());
			while(!accept(TokenType.OPENING_BRACE) && !atEnd()) {
				expect(TokenType.COLON);
				args.add(nextArg());
			}
			index--;
		}
		
		Body body = Body.EMPTY;
		
		if(accept(TokenType.OPENING_BRACE)) {
			body = new Body();
			while(!accept(TokenType.CLOSING_BRACE)) {
				body.append(next());
			}
		} else if(args.isEmpty()) {
			return new VariableAccess(name);
		}
		
		return new CommandCall(name, args, body);
	}
	
	private Node nextArg() {
		if(accept(TokenType.OPENING_BRACE)) {
			Node arg = next();
			expect(TokenType.CLOSING_BRACE);
			return arg;
		}
		return next();
	}
	
	/** Parses some tokens into a {@link Body}. */
	public static Body parse(List<Token> tokens) {
		return new Parser(tokens).toBody();
	}
}