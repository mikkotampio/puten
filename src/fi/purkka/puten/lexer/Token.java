package fi.purkka.puten.lexer;

/** Represents a Puten token. Each instance has an associated <i>type</i>
 * (see {@link TokenType) and <i>content</i>, the string value it represents.
 * They are accessible via the public fields of instances of this class.
 *
 * <p>For some types of tokens, the content is a constant. For constant
 * tokens, the static fields of this class may be used as instances. For
 * non-constant tokens, refer to the static methods of this class.</p> */
public class Token {
	
	public final static Token OPENING_BRACE = new Token(TokenType.OPENING_BRACE);
	public final static Token CLOSING_BRACE = new Token(TokenType.CLOSING_BRACE);
	public final static Token COLON = new Token(TokenType.COLON);
	public final static Token BACKSLASH = new Token(TokenType.BACKSLASH);
	
	public static Token string(String content) {
		return new Token(TokenType.STRING, content);
	}
	
	public final TokenType type;
	public final String content;
	
	private Token(TokenType type, String content) {
		this.type = type;
		this.content = content;
	}
	
	private Token(TokenType type) {
		this.type = type;
		this.content = type.content;
	}
	
	public Token append(String newContent) {
		if(type.isConstant())
			throw new UnsupportedOperationException("Cannot append to constant token");
		return new Token(type, content + newContent);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Token)) {
			return false;
		}
		Token other = (Token) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return type.isConstant() ? type.toString()
				: type + "(" + content + ")";
	}

	/** The different types of tokens. */
	public static enum TokenType {
		
		/** Represents the character <code>{</code> */
		OPENING_BRACE("{"),
		
		/** Represents the character <code>}</code> */
		CLOSING_BRACE("}"),
		
		/** Represents the character <code>:</code> */
		COLON(":"),
		
		/** Represents the character <code>\</code> */
		BACKSLASH("\\"),
		
		/** Represents an arbitrary string. */
		STRING;
		
		private final String content;
		
		TokenType() {
			content = null;
		}
		
		TokenType(String content) {
			this.content = content;
		}
		
		/** Returns whether the content of this token is always
		 * the same string. */
		public boolean isConstant() {
			return content != null;
		}
	}
}