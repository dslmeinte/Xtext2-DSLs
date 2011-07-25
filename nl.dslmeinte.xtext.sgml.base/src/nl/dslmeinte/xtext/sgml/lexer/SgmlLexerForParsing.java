package nl.dslmeinte.xtext.sgml.lexer;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;

import com.google.inject.Inject;

/**
 * TODO  provide Javadoc on how to use this class
 * 
 * @author meinte
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
