package nl.dslmeinte.xtext.util.antlr.trie;

import org.antlr.runtime.CharStream;

/**
 * Interface for a <em>trie</em> which matches a fixed set of {@link String}
 * keys case-insensitively and destructively (i.e., the matching characters are
 * consumed) against an ANTLR {@link CharStream}. <em>Longest match wins</em>
 * (contract).
 * 
 * @author Meinte Boersma
 * 
 * @param <T>
 *            The run-time representation type corresponding to the keys.
 */
public interface CaseInsensitiveTrie<T> {

	/**
	 * Matches the {@link CharStream} passed from its current position to any of
	 * the registered keys, returning the <em>longest</em> one (and advancing
	 * the input to just after the match by consuming the matching tokens), or
	 * {@code null} if no match is found.
	 */
	public abstract T match(CharStream input);

}
