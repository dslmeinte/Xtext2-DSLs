package nl.dslmeinte.xtext.util.antlr.trie;

import java.util.Set;


/**
 * Implementation of {@link CaseInsensitiveTrie} which matches the given set of
 * {@link String}s, returning the matched {@link String} verbatim in case of a
 * match.
 * 
 * @author Meinte Boersma
 */
public class StringBasedTrie extends TrieSupport<String> {

	private StringBasedTrie(Set<String> keys) {
		for( String key : keys ) {
			register(key, key);
		}
	}

	/**
	 * Creates a {@link CaseInsensitiveTrie} from the given set of
	 * {@link String} keys.
	 */
	public static StringBasedTrie of(Set<String> keys) {
		return new StringBasedTrie(keys);
	}

}
