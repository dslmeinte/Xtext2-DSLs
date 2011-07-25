package nl.dslmeinte.xtext.sgml.lexer;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;

/**
 * An interface intended to be able to decouple the actual lexing implementation
 * from the base class, which is going to be either
 * {@link org.eclipse.xtext.parser.antlr.Lexer} or
 * {@link org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer}.
 * 
 * @author Meinte Boersma
 */
public interface WeavableAntlrLexer {

	CharStream input();

	void setType(int type);

	void match(String string) throws MismatchedTokenException;

	Token emit();

	void recover(RecognitionException re);

	void setCharStream(CharStream input);

}
