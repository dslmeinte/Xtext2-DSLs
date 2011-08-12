package nl.dslmeinte.xtext.sgml.lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.dslmeinte.xtext.antlr.trie.CaseInsensitiveTrie;
import nl.dslmeinte.xtext.antlr.trie.MapBasedTrie;
import nl.dslmeinte.xtext.antlr.trie.TrieSupport;
import nl.dslmeinte.xtext.sgml.lexer.SgmlTokenHelper.TokenType;

import org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider;

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
	public AntlrTokenFacade(SgmlTokenHelper tokenHelper) {
		final Map<String, Integer> nonBaseKeywordsMap = new HashMap<String, Integer>();
		for( TokenType tokenType : tokenHelper.getKeywordTokens() ) {
			String keyword = tokenType.getDescription();
			BaseTerminals baseTerminal = BaseTerminals.fromKeyword(keyword);
			if( baseTerminal == null && TrieSupport.isWord(keyword) ) {
				nonBaseKeywordsMap.put(keyword, tokenType.getId());
			} else {
				baseTerminalsMap.put(baseTerminal, tokenType.getId());
			}
		}
		for( TokenType tokenType : tokenHelper.getLexerRuleNames() ) {
			BaseTerminals baseTerminal = BaseTerminals.fromName(tokenType.getDescription());
			if( baseTerminal != null ) {
				baseTerminalsMap.put(baseTerminal, tokenType.getId());
			}
		}
		nonBaseKeywordsTrie = MapBasedTrie.of(nonBaseKeywordsMap);
		for( BaseTerminals baseTerminal : BaseTerminals.values() ) {
			if( !baseTerminalsMap.containsKey(baseTerminal) ) {
				throw new IllegalArgumentException("BaseTerminals('" + baseTerminal.name() + "') not mapped" );
			}
		}
	}

	private final Map<BaseTerminals, Integer> baseTerminalsMap = new HashMap<BaseTerminals, Integer>();

	@Override
	public int map(BaseTerminals baseTerminal) {
		return baseTerminalsMap.get(baseTerminal).intValue();
	}

	@Override
	public BaseTerminals asBase(int type) {
		for( Entry<BaseTerminals, Integer> entry : baseTerminalsMap.entrySet() ) {
			if( entry.getValue().intValue() == type ) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public boolean isBase(int type) {
		return baseTerminalsMap. containsValue(type);
	}

	private final CaseInsensitiveTrie<Integer> nonBaseKeywordsTrie;

	@Override
	public CaseInsensitiveTrie<Integer> nonBaseKeywordsTrie() {
		return nonBaseKeywordsTrie;
	}

}
