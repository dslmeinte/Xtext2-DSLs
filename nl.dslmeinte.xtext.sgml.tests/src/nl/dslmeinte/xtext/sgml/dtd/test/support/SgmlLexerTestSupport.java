package nl.dslmeinte.xtext.sgml.dtd.test.support;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import nl.dslmeinte.xtext.sgml.lexer.SgmlLexerForParsing;
import nl.dslmeinte.xtext.sgml.lexer.TokenFacade;
import nl.dslmeinte.xtext.util.antlr.HtmlTokenVisualizer;
import nl.dslmeinte.xtext.util.antlr.HtmlTokenVisualizer.TokenToStyleMapper;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Token;

import com.google.inject.Injector;

/**
 * Support class to test a {@link Lexer} wrapped in a
 * {@link SgmlLexerForParsing} using {@link LexerTestSupport}, with some
 * convenience for Guice.
 * 
 * @author Meinte Boersma
 */
public abstract class SgmlLexerTestSupport extends LexerTestSupport {

	private final Injector injector;

	public SgmlLexerTestSupport(Injector injector) {
		super(injector.getInstance(TokenFacade.class));
		this.injector = injector;
	}
	
	@Override
	protected Lexer createLexer(CharStream input) {
		SgmlLexerForParsing baseLexer = injector.getInstance(SgmlLexerForParsing.class);
		baseLexer.setCharStream(input);
		return baseLexer;
	}

	protected void visualize_lexing(String fileName, String sourcePath, String destinationPath) throws IOException {
		initLexer(null);
		HtmlTokenVisualizer visualizer = new HtmlTokenVisualizer(getLexer(), new TokenToStyleMapper() {
			@Override
			public String cssClassName(Token token) {
				int type = token.getType();
				if( tokenFacade.isBase(type) ) {
					return tokenFacade.asBase(type).name();
				}
				return "tagKeyword";
			}
		}, "default-lexing-style.css");
		CharStream input = new ANTLRFileStream(sourcePath + fileName );
		OutputStream output = new FileOutputStream(destinationPath + fileName.substring(0, fileName.lastIndexOf('.')) + "-lexed.html");
		visualizer.visualize(input, output, fileName + ": token visualization");
		output.close();
	}

}
