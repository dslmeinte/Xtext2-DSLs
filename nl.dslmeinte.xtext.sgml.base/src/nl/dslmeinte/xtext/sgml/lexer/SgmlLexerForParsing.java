package nl.dslmeinte.xtext.sgml.lexer;

import nl.dslmeinte.xtext.sgml.base.guice.RuntimeModuleSupport;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;

import com.google.inject.Inject;

/**
 * The {@link org.eclipse.xtext.parser.antlr.Lexer} implementation which uses
 * the {@link SgmlLexer} internally to do the lexing of the input, with the
 * intent to do a full parser including model creation.
 * <p>
 * Bind this class to the runtime of your language using
 * {@link RuntimeModuleSupport#configureRuntimeLexer(com.google.inject.Binder)}.
 * <p>
 * Note that {@link org.eclipse.xtext.parser.antlr.Lexer} already implements
 * several methods of {@link WeavableAntlrLexer} without ever knowing about that
 * interface, just by matching the methods' signatures.
 * 
 * @author Meinte Boersma
 */
public class SgmlLexerForParsing
	extends org.eclipse.xtext.parser.antlr.Lexer
	implements WeavableAntlrLexer
{

	@Inject
	public SgmlLexerForParsing(SgmlLexer sgmlLexer) {
		super(null);
		this.sgmlLexer = sgmlLexer;
	}

	private SgmlLexer sgmlLexer;

	@Override
	public CharStream input() {
		return super.input;
	}

	@Override
	public void setType(int type) {
		super.state.type = type;
	}

	@Override
	public void mTokens() throws RecognitionException {
		sgmlLexer.mTokensDelegate();
	}

	@Override
	public void reset() {
		super.reset();
		sgmlLexer.setBaseLexer(this);
	}

}
