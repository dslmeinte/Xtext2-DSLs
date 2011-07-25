package nl.dslmeinte.xtext.sgml.lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.dslmeinte.xtext.util.antlr.trie.CaseInsensitiveTrie;
import nl.dslmeinte.xtext.util.antlr.trie.MapBasedTrie;
import nl.dslmeinte.xtext.util.antlr.trie.TrieSupport;

import org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider;
import org.eclipse.xtext.parser.antlr.TokenTool;
import org.eclipse.xtext.util.Strings;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to map the token types obtained through {@link AntlrTokenDefProvider}
 * to instances of {@link BaseTerminals} where applicable.
 * 
 * @author Meinte Boersma
 */
@Singleton
public class AntlrTokenFacade implements TokenFacade {

	@Inject
	public AntlrTokenFacade(AntlrTokenDefProvider tokenDefProvider) {
		final Map<String, Integer> nonBaseKeywordsMap = new HashMap<String, Integer>();
		for( Entry<Integer, String> entry : tokenDefProvider.getTokenDefMap().entrySet() ) {
			String tokenDescription = entry.getValue();
			int id = entry.getKey();
			if( tokenDescription.startsWith("'") && tokenDescription.endsWith("'") ) {
				String keyword = tokenDescription.substring(1, tokenDescription.length()-1);
				keyword = Strings.convertFromJavaString(keyword, true);
				keyword = keyword.intern();	// optimization attempt: avoid duplicate String-s
				BaseTerminals baseTerminal = BaseTerminals.fromKeyword(keyword);
				if( baseTerminal == null ) {
					if( TrieSupport.isWord(keyword) ) {
						nonBaseKeywordsMap.put(keyword, id);
					}
				} else {
					baseTerminalsMap.put(baseTerminal, id);
				}
			} else if( TokenTool.isLexerRule(tokenDescription) ) {
				String ruleName = TokenTool.getLexerRuleName(tokenDescription);
				BaseTerminals baseTerminal = BaseTerminals.fromName(ruleName);
				if( baseTerminal != null ) {
					baseTerminalsMap.put(baseTerminal, id);
				}
			}
		}
		nonBaseKeywordsTrie = MapBasedTrie.of(nonBaseKeywordsMap);
	}

	private final Map<BaseTerminals, Integer> baseTerminalsMap = new HashMap<BaseTerminals, Integer>();

	@Override
	public int map(BaseTerminals baseTerminal) {
		return baseTerminalsMap.get(baseTerminal).intValue();
	}

	private final CaseInsensitiveTrie<Integer> nonBaseKeywordsTrie;

	@Override
	public CaseInsensitiveTrie<Integer> nonBaseKeywordsTrie() {
		return nonBaseKeywordsTrie;
	}

	// TODO  add a check on whether all BaseTerminals enum literals are mapped (other than through a unit test)

}
