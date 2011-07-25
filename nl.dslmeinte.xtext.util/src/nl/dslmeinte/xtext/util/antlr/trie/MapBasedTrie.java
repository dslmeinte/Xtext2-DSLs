package nl.dslmeinte.xtext.util.antlr.trie;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of a {@link CaseInsensitiveTrie} which matches keys in the
 * given map and returns the corresponding value.
 * 
 * @author Meinte Boersma
 * 
 * @param <T>
 */
public class MapBasedTrie<T> extends TrieSupport<T> {

	private MapBasedTrie(Map<String, T> map) {
		for( Entry<String, T> entry : map.entrySet() ) {
			register(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Creates a {@link CaseInsensitiveTrie} from the given map of
	 * {@link String} keys &rarr; {@link T} values.
	 */
	public static <T> MapBasedTrie<T> of(Map<String, T> map) {
		return new MapBasedTrie<T>(map);
	}

}
