package nl.dslmeinte.xtext.sgml.lexer;

import nl.dslmeinte.xtext.antlr.trie.CaseInsensitiveTrie;

import com.google.inject.ImplementedBy;

/**
 * Implementations of this interface provide run-time efficient mapping from
 * {@link BaseTerminals} to their ANTLR token types (encoded as {@code int}) and
 * matching of non-base keywords.
 * 
 * @author Meinte Boersma
 */
@ImplementedBy(AntlrTokenFacade.class)
public interface TokenFacade {

	public abstract CaseInsensitiveTrie<Integer> nonBaseKeywordsTrie();

	public abstract int map(BaseTerminals baseTerminal);

	// TODO  move the following two methods to a separate interface as their use is limited to token visualization (so far)

	public abstract boolean isBase(int type);

	public abstract BaseTerminals asBase(int type);

}
