package nl.dslmeinte.xtext.sgml.contenttype;

import static nl.dslmeinte.xtext.sgml.contenttype.Terminals.*;
import static nl.dslmeinte.xtext.sgml.contenttype.SimpleSgmlRecognizer.Result.*;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.RecognitionException;

/**
 * Class which can recognize SGML documents with a specific document type/root
 * identifier in a somewhat simplistic manner. It uses a lexer to scan through
 * the input as that's easier and probably more performant than some regular
 * expression.
 * <p>
 * The recognizer is decidedly simple in that it only does very partial lexing
 * of the input and doesn't do any error recovery whatsoever, which might cause
 * a more strict result to be returned.
 * 
 * @author Meinte Boersma
 */
public class SimpleSgmlRecognizer {

	private final String rootIdentifier;

	public SimpleSgmlRecognizer(String rootIdentifier) {
		this.rootIdentifier = rootIdentifier;
	}

	/**
	 * Checks whether the input can be recognized as SGML document, preferably
	 * having the right document type, and returns the corresponding
	 * {@link Result} literal.
	 */
	public Result recognize(CharStream newInput) {
		this.input = newInput;
		try {
			// proceed until first non-whitespace, non-SGML-comments token:
			Token token;
			do {
				token = nextToken();
			} while( token.getType() != EOF && ( token.getType() == whitespace || token.getType() == sgml_comments ) );

			if( token.getType() == open_tag ) {
				return handleOpenTag();
			}
			return token.getType() == EOF ? emptySgml : notSgml;
		} catch( RecognitionException e ) {
			return notSgml;
		}
	}

	private Result handleOpenTag() throws RecognitionException {
		Token token = nextToken();
		if( token.getType() == textual_contents ) {
			if( token.getText().equalsIgnoreCase("!DOCTYPE") ) { //$NON-NLS-1$
				token = nextToken();
				if( token.getType() != whitespace ) {
					return sgmlWithWrongDocType;
				}
				token = nextToken();
				if( token.getType() != textual_contents ) {
					return sgmlWithWrongDocType;
				} // else:
				return( token.getText().equalsIgnoreCase(rootIdentifier) ? sgmlWithRightDocType : sgmlWithWrongDocType );
			} // else:
			return sgmlWithoutDocType;
		} // else:
		return notSgml;
	}

	private CharStream input;

	private Token nextToken() throws RecognitionException {
		int ch = input.LA(1);
		if( ch == '<' ) {
			input.consume();
			if( input.LA(1) == '!' && input.LA(2) == '-' && input.LA(3) == '-' ) {
				input.consume();
				input.consume();
				input.consume();
				while( ( ch = input.LA(1) ) != CharStream.EOF && !( ch == '-' && input.LA(2) == '-' && input.LA(3) == '>' ) ) {
					input.consume();
				}
				if( ch != CharStream.EOF ) {
					input.consume();
					input.consume();
					input.consume();
					return new Token(sgml_comments);
				} // else:
				throw new MismatchedSetException();
			} // else:
			return new Token(open_tag);
		}
		if( Character.isWhitespace(ch) ) {
			input.consume();
			while( ( ch = input.LA(1) ) != CharStream.EOF && Character.isWhitespace(ch) ) {
				input.consume();
			}
			return new Token(whitespace);
		}
		if( ch == CharStream.EOF ) {
			return new Token(EOF);
		}
		StringBuilder text = new StringBuilder().append((char) ch);
		input.consume();
		while( ( ch = input.LA(1) ) != CharStream.EOF && !( ch == '<' || Character.isWhitespace(ch) ) ) {
			text.append((char) ch);
			input.consume();
		}
		return new Token(textual_contents, text.toString());
	}

	/**
	 * Enumeration used to encode the result of an SGML recognition.
	 */
	public enum Result {

		/**
		 * The document is a SGML document with a document type declaration with
		 * the right declared root element.
		 */
		sgmlWithRightDocType,
		/**
		 * The document is a SGML document with a document type declaration that
		 * either syntactically incorrect or doesn't declare the right declared
		 * root element.
		 */
		sgmlWithWrongDocType,
		/**
		 * The document is a SGML document without a document type declaration
		 * but with some SGML-like content.
		 */
		sgmlWithoutDocType,
		/**
		 * The document is not a SGML document - it starts (modulo whitespace
		 * and SGML comments) on general, textual content, not an open tag.
		 */
		notSgml,
		/**
		 * The document is empty modulo whitespace and SGML comments.
		 */
		emptySgml;

	}

}
