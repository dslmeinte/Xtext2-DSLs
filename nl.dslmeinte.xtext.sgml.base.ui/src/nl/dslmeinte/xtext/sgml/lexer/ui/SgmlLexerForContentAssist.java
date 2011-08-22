package nl.dslmeinte.xtext.sgml.lexer.ui;

import nl.dslmeinte.xtext.sgml.base.ui.guice.UiModuleSupport;
import nl.dslmeinte.xtext.sgml.lexer.SgmlLexer;
import nl.dslmeinte.xtext.sgml.lexer.WeavableAntlrLexer;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;

import com.google.inject.Inject;

/**
 * The {@link org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer}
 * implementation which uses the {@link SgmlLexer} internally to do the lexing
 * of the input, with the intent to do enough parsing for content assist and
 * such, but not model creation.
 * <p>
 * Bind this class to the runtime of your language using
 * {@link UiModuleSupport#configureContentAssistLexer(com.google.inject.Binder)}.
 * <p>
 * Note that
 * {@link org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer}
 * already implements several methods of {@link WeavableAntlrLexer} without ever
 * knowing about that interface, just by matching the methods' signatures.
 * 
 * @author Meinte Boersma
 */
public class SgmlLexerForContentAssist
	extends org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer
	implements WeavableAntlrLexer
{

	@Inject
	public SgmlLexerForContentAssist(SgmlLexer sgmlLexer) {
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
