package nl.dslmeinte.xtext.sgml.dtd.test.support;

import java.io.IOException;

import nl.dslmeinte.xtext.sgml.lexer.BaseTerminals;
import nl.dslmeinte.xtext.sgml.lexer.TokenFacade;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Token;
import org.junit.Assert;

/**
 * Support class to test {@link Lexer}s implementations in conjunction with a
 * {@link TokenFacade}.
 * 
 * @author Meinte Boersma
 */
public abstract class LexerTestSupport {

	private Lexer lexer;

	protected Lexer getLexer() {
		if( lexer == null ) {
			throw new IllegalStateException( "lexer not initialized" );
		}
		return lexer;
	}

	abstract protected Lexer createLexer(CharStream input);

	protected void initLexer(CharStream input) {
		this.lexer = createLexer(input);
	}

	protected final TokenFacade tokenFacade;

	public LexerTestSupport(TokenFacade tokenFacade) {
		this.tokenFacade = tokenFacade;
	}

	protected void assertNextToken(BaseTerminals type) {
		Token token = lexer.nextToken();
//		System.out.format( "|%s| [actual=%s, expected=%s]\n", token.getText(), TokenType.values()[token.getType()], type );
		assertTokenType(type, token);
	}

	protected void assertNextToken(BaseTerminals type, String text) {
		Token token = lexer.nextToken();
//		System.out.format( "|%s| [actual=%s, expected=%s]\n", token.getText(), TokenType.values()[token.getType()], type );
		assertTokenType(type, token);
		Assert.assertEquals(text, token.getText());
	}

	protected void assertEOF() {
		Assert.assertTrue(lexer.nextToken().getType() == CharStream.EOF);
	}

	protected void assertTokenType(BaseTerminals type, Token token) {
		int actualType = token.getType();
		if( tokenFacade.map(type) != actualType ) {
			Assert.fail(
					"expected " + type.name() + " but was: " +
					( tokenFacade.isBase(actualType) ? tokenFacade.asBase(actualType) : ( "<" + actualType + ">" ) )
				);
		}
		Assert.assertEquals(tokenFacade.map(type), actualType);
	}

	private Token nextNonWhitespaceToken() {
		Token token = lexer.nextToken();
		while( ( token != null ) && ( token.getType() == tokenFacade.map(BaseTerminals.whitespace) ) ) {
			token = lexer.nextToken();
		}
		return token;
	}

	protected void assertNextNonWhitespaceToken(BaseTerminals baseTerminal) {
		assertTokenType(baseTerminal, nextNonWhitespaceToken());
	}

	protected void assertNextNonWhitespaceToken(BaseTerminals baseTerminal, String text) {
		Token token = nextNonWhitespaceToken();
		assertTokenType(baseTerminal, token);
		Assert.assertEquals(text, token.getText());
	}

	protected void lexe(CharStream input) throws IOException {
		this.lexer = createLexer(input);
		Token token = lexer.nextToken();
		while( token.getType() != CharStream.EOF ) {
			if( token.getType() == 0 ) {
				System.err.println( "encountered token of type 0 @L" + token.getLine() + ":" + token.getCharPositionInLine() );
			}
			token = lexer.nextToken();
		}
	}

}
