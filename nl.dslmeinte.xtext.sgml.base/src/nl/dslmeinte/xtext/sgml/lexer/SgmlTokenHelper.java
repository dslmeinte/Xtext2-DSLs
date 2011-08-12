package nl.dslmeinte.xtext.sgml.lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider;
import org.eclipse.xtext.parser.antlr.TokenTool;
import org.eclipse.xtext.util.Strings;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Util class to help with the tokens in an Xtext language.
 * 
 * @author Meinte Boersma
 */
@Singleton
public class SgmlTokenHelper {

	/**
	 * Initializes the singleton.
	 */
	@Inject
	public SgmlTokenHelper(AntlrTokenDefProvider tokenDefProvider) {
		for( Entry<Integer, String> entry : tokenDefProvider.getTokenDefMap().entrySet() ) {
			String tokenDescription = entry.getValue();
			int id = entry.getKey();
			if( tokenDescription.startsWith("'") && tokenDescription.endsWith("'") ) { //$NON-NLS-1$ //$NON-NLS-2$
				String keyword = tokenDescription.substring(1, tokenDescription.length()-1);
				keyword = Strings.convertFromJavaString(keyword, true);
				/*
				 * Slight optimization: avoid duplicate String-s to be created.
				 * Note that the set of keywords in not very large.
				 */
				keyword = keyword.intern();
				keywordTokens.add(new TokenType(keyword, id));
				continue;
			}
			if( TokenTool.isLexerRule(tokenDescription) ) {
				String ruleName = TokenTool.getLexerRuleName(tokenDescription);
				lexerRuleNames.add(new TokenType(ruleName, id));
				continue;
			}
		}
	}

	private final List<TokenType> keywordTokens = new ArrayList<TokenType>();

	/**
	 * @return A list of all the keywords in the grammar, i.e. all token types
	 *         which are a fixed string (as opposed to a terminal rule).
	 */
	public List<TokenType> getKeywordTokens() {
		return Collections.unmodifiableList(keywordTokens);
	}

	/**
	 * Helper class to encapsulate a token type as an integer id and a
	 * description.
	 * 
	 * @author Meinte Boersma
	 */
	public static class TokenType {
		private String description;
		private int id;
		public TokenType(String description, int id) {
			this.description = description;
			this.id = id;
		}
		public String getDescription() {
			return description;
		}
		public int getId() {
			return id;
		}
	}

	private final List<TokenType> lexerRuleNames = new ArrayList<TokenType>();

	/**
	 * @return A list of all the lexer rule (typically a terminal rule) in the
	 *         grammar, with the (upper-cased) lexer rule name for a
	 *         description.
	 */
	public List<TokenType> getLexerRuleNames() {
		return lexerRuleNames;
	}


}
