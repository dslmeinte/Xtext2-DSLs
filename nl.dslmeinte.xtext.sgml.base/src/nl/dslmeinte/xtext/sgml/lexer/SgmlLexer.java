package nl.dslmeinte.xtext.sgml.lexer;

import static nl.dslmeinte.xtext.sgml.lexer.BaseTerminals.*;
import static nl.dslmeinte.xtext.sgml.lexer.SgmlLexer.LexicalState.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.dslmeinte.xtext.util.antlr.trie.CaseInsensitiveTrie;
import nl.dslmeinte.xtext.util.antlr.trie.MapBasedTrie;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;

import com.google.inject.Inject;


/**
 * Lexer to lexe SGML content, using the principle of lexical state and
 * governing that state itself.
 * <p>
 * The two lexical states are:
 * <dl>
 * 	<dt>{@code header or tag}</dt>
 * 		<dd>For the SGML header, including entities and up to the closing {@code >},
 * 			or inside tags, including opening {@code <} and closing {@code >}.</dd>
 * 	<dt>{@code content}</dt>
 * 		<dd>For content outside of the SGML header and tags.</dd>
 * </dl>
 * 
 * @author Meinte Boersma
 */
public class SgmlLexer {

	/*
	 * +--------+
	 * | Facade |
	 * +--------+
	 */

	private final TokenFacade facade;

	@Inject
	public SgmlLexer(TokenFacade facade) {
		this.facade = facade;
	}

	public TokenFacade getFacade() {
		return facade;
	}


	/*
	 * +---------+
	 * | Weaving |
	 * +---------+
	 */

	private WeavableAntlrLexer baseLexer;

	public void setBaseLexer(WeavableAntlrLexer baseLexer) {
		this.baseLexer = baseLexer;
		init();
	}

	private int LA(int i) {
		return input().LA(i);
	}

	private void consume() {
		input().consume();
	}

	private void setType(BaseTerminals baseTerminal) {
		setType(facade.map(baseTerminal));
	}

	private void setType(int type) {
//	(switch on for debugging...):
//		if( type == 0 ) {
//			throw new IllegalArgumentException("token type can't be 0");
//		}
		baseLexer.setType(type);
	}

	private void match(String string) throws MismatchedTokenException {
		baseLexer.match(string);
	}

	private CharStream input() {
		return baseLexer.input();
	}

	private void recover(RecognitionException re) {
		baseLexer.recover(re);
	}

	private void emit() {
		baseLexer.emit();
	}


	/*
	 * +-------------------+
	 * | Lexing entry point|
	 * +-------------------+
	 */

	public void init() {
		lexicalState = content;
	}

	// the following 2 features have protected visibility to allow per-state testing:

	protected enum LexicalState {
		header, tag, content;
	}

	private LexicalState lexicalState;

	public void mTokensDelegate() throws RecognitionException {
		switch( lexicalState ) {
			case header:
			case tag:
			{
				mTokensTagOrHeader();
				return;
			}
			case content: {
				mTokensContent();
				return;
			}
		}
	}


	/*
	 * +--------------------+
	 * | Lexing: tag/header |
	 * +--------------------+
	 */

	private boolean insideBrackets = false;

	private final static CaseInsensitiveTrie<BaseTerminals> headerKeywordsTrie;

	static {
		Map<String, BaseTerminals> map = new HashMap<String, BaseTerminals>();
		map.put("DOCTYPE", doctype);
		map.put("ENTITY", entity);
		map.put(public_.getKeyword(), public_);
		map.put(system.getKeyword(), system);
		headerKeywordsTrie = MapBasedTrie.of(map);
	}

	private void mTokensTagOrHeader() throws RecognitionException {
		if( isQueuePopulated() ) {
			dequeue();
			return;
		}

		int ch = LA(1);
		if( handledOpenTagOrSgmlComments(ch) ) {
			return;
		}
		if( handledWhitespace(ch) ) {
			return;
		}
		if( handledQuotedString(ch) ) {
			return;
		}
		if( ch == '>' ) {
			setType(close_tag);
			consume();
			updateLexicalState(content);
			return;
		}
		if( ch == '=' ) {
			setType(equals);
			consume();
			return;
		}
		if( ch == '/' ) {
			setType(slash);
			consume();
			return;
		}
		if( lexicalState == header ) {
			if( ch == '[' ) {
				consume();
				insideBrackets = true;
				setType(open_bracket);
				return;
			}
			if( ch == ']' ) {
				consume();
				insideBrackets = false;
				setType(close_bracket);
				return;
			}
			if( handledHeaderComments(ch) ) {
				return;
			}
			BaseTerminals keyword = headerKeywordsTrie.match(input());
			if( keyword != null ) {
				setType(keyword);
				return;
			}
		}
		if( ch == '!' ) {
			consume();
			BaseTerminals keyword = headerKeywordsTrie.match(input());
			if( keyword == doctype ) {
				setType(doctype);
				lexicalState = header;
				return;
			}
			if( keyword == entity ) {
				setType(entity);
				return;
			}
			// TODO  provide more useful info than '0'
			MismatchedTokenException mte = new MismatchedTokenException(0, input());
			recover(mte);
			throw mte;
		}
		if( lexicalState == tag ) {		// i.e., != header
			Integer match = facade.nonBaseKeywordsTrie().match(input());
			if( match != null ) {
				setType(match);
				return;
			}
		}
		if( handledIdentifier(ch) ) {
			return;
		}
		lexicalState = content;
		mTokensContent();
	}


	/*
	 * +-----------------+
	 * | Lexing: content |
	 * +-----------------+
	 */

	/**
	 * Lexes the input/content <em>inside</em> tags until the first opening tag
	 * {@code <} (which is not part of SGML comments).
	 */
	private void mTokensContent() throws RecognitionException {
		if( isQueuePopulated() ) {
			dequeue();
			return;
		}

		int ch = LA(1);
		if( handledOpenTagOrSgmlComments(ch) ) {
			return;
		}
		whitespaceOnly = true;
		int charactersToConsume = 0;
		// walk over contents and deal with (potential) entity reference matches:

		while( true ) {
			while( ( ch = LA(charactersToConsume + 1) ) != CharStream.EOF && !( ch == '<' || ch == '&' ) ) {
				whitespaceOnly &= isWhitespace(ch);
				charactersToConsume++;
			}
			if( ch == '<' || ch == CharStream.EOF ) {
				enqueue(getLiteralContentsType(), charactersToConsume);
				dequeue();
				return;
			}
			// LA(1) == '&':
			if( !isIdentifierPart(LA(charactersToConsume + 2)) ) {
				charactersToConsume += 2;
				whitespaceOnly = false;
				continue;
			}
			int i = 2;
			while( ( ch = LA(charactersToConsume + i) ) != CharStream.EOF && isIdentifierPart(ch) ) {
				i++;
			}
			if( charactersToConsume > 0 ) {
				// emit literal_contents token for characters before '&':
				enqueue(getLiteralContentsType(), charactersToConsume);
			}
			// emit ampersand token for '&':
			enqueue(ampersand, 1);
			enqueue(identifier, i - 2);
			if( ch == ';' ) {	// entity reference found!
				// emit semicolon token ';':
				enqueue(semicolon, 1);
			}
			dequeue();
			return;
		}
	}

	private boolean whitespaceOnly;

	private BaseTerminals getLiteralContentsType() {
		return( whitespaceOnly ? whitespace : literal_contents );
	}


	/*
	 * +---------------+
	 * | Token queuing |
	 * +---------------+
	 */

	private class QueuedToken {
		private BaseTerminals type;
		private int length;
		public QueuedToken(BaseTerminals type, int length) {
			this.type = type;
			this.length = length;
		}
	}

	private final List<QueuedToken> queue = new ArrayList<QueuedToken>();
	private int queueIndex = 0;

	public boolean isQueuePopulated() {
		return queue.size() > 0;
	}

	public void enqueue(BaseTerminals type, int length) {
		queue.add(new QueuedToken(type, length));
	}

	public void dequeue() {
		QueuedToken queuedToken = queue.get(queueIndex);
		setType(queuedToken.type);
		for( int i = 0; i < queuedToken.length; i++ ) {
			consume();
		}
		emit();
		queueIndex++;
		if( queueIndex >= queue.size() ) {
			queue.clear();
			queueIndex = 0;
		}
	}


	/*
	 * +---------------------+
	 * | Convenience methods |
	 * +---------------------+
	 */

	private boolean isIdentifierPart(int ch) {
		return( Character.isLetterOrDigit(ch) || ch == '_' );
	}

	private boolean handledIdentifier(int ch) {
		if( isIdentifierPart(ch) ) {
			setType(identifier);
			consume();
			while( isIdentifierPart(LA(1)) ) {
				consume();
			}
			return true;
		}
		return false;
	}

	private boolean isQuoteChar(int ch) {
		return( ch == '"' || ch == '\'' );
	}

	private BaseTerminals mapQuoteChar(int ch) {
		if( ch == '"' ) {
			return double_quote;
		}
		if( ch == '\'' ) {
			return single_quote;
		}
		throw new IllegalArgumentException( ((char) ch) + " is not a quote character" );
	}

	private boolean handledQuotedString(int ch) throws RecognitionException {
		if( isQuoteChar(ch) ) {
			handleQuotedString(ch);
			return true;
		}
		return false;
	}

	/**
	 * Assertion: only called in case {@code Character.isQuoteChar( LA(1) )}.
	 * 
	 * @param quoteChar
	 *            the value of {@code LA(1)}
	 */
	private void handleQuotedString(int quoteChar) throws RecognitionException {
		BaseTerminals quoteTerminal = mapQuoteChar(quoteChar);
		enqueue(quoteTerminal, 1);

		int i = 2;
		int ch;
		// TODO  add correct handling of escaping
		while( ( ch = LA(i) ) != quoteChar && ch != CharStream.EOF ) {
			i++;
		}
		enqueue(quoted_string, i-2);

		if( ch != CharStream.EOF ) {
			enqueue(quoteTerminal, 1);
			dequeue();
			return;
		}

		for( int j = 0; j < i; j++ ) {
			consume();
		}
		NoViableAltException nvae = new NoViableAltException();
		recover(nvae);
		throw nvae;
	}

	private boolean isWhitespace(int ch) {
		return Character.isWhitespace(ch);
	}

	private boolean handledWhitespace(int ch) {
		if( isWhitespace(ch) ) {
			setType(whitespace);
			consume();
			while( isWhitespace( LA(1) ) ) {
				consume();
			}
			return true;
		}
		return false;
	}

	private boolean handledOpenTagOrSgmlComments(int ch) throws RecognitionException {
		if( ch == '<' ) {
			if( LA(2) == '!' && LA(3) == '-' && LA(4) == '-' ) {
				match("<!--");
				while( ( ch = LA(1) ) != CharStream.EOF && !( ch == '-' && LA(2) == '-' && LA(3) == '>' ) ) {
					consume();
				}
				if( ch != CharStream.EOF ) {
					match("-->");
					setType(sgml_comments);
					return true;
				} else {
					setType(0);
					// TODO  actually use a sensible BitSet instead of null
					MismatchedSetException mse = new MismatchedSetException(null, input());
			        recover(mse);
			        throw mse;
				}
			}
			// no comments, only an open tag symbol:
			consume();
			setType(open_tag);
			updateLexicalState(tag);
			return true;
		}
		return false;
	}

	/**
	 * Updates the {@link #lexicalState} for the case that we've just
	 * encountered an open or close tag, taking into account the current state
	 * and how the header behaves.
	 */
	private void updateLexicalState(LexicalState nonHeaderState) {
		lexicalState = ( lexicalState == header && insideBrackets ) ? header : nonHeaderState;
	}

	private boolean handledHeaderComments(int ch) throws RecognitionException {
		if( ch == '-' && LA(2) == '-' ) {
			consume();
			consume();
			setType(header_comments);
			while( true ) {
				while( ( ch = LA(1) ) != CharStream.EOF && ch != '-' ) {
					consume();
				}
				if( ch == '-' ) {
					consume();
					if( LA(1) == '-' ) {
						consume();
						return true;
					} else {
						continue;
					}
				}
				if( ch == CharStream.EOF ) {
					// TODO  actually use a sensible BitSet instead of null
					MismatchedSetException mse = new MismatchedSetException(null, input());
					recover(mse);
					throw mse;
				}
			}
		}
		return false;
	}

}

