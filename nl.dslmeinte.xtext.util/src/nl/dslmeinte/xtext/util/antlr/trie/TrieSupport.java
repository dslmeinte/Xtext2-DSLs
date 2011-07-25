package nl.dslmeinte.xtext.util.antlr.trie;

import org.antlr.runtime.CharStream;

/**
 * Implementation of {@link CaseInsensitiveTrie} which allows sub implementors
 * to govern to way keys and their run-time representations are registered.
 * 
 * @author Meinte Boersma
 * 
 * @param <T>
 *            The run-time representation type corresponding to the keys.
 */
abstract public class TrieSupport<T> implements CaseInsensitiveTrie<T> {

	protected static class Node<T> {

		/**
		 * If the current {@link Node} can be considered to terminate on the
		 * last character, then {@link EnumBasedTrie#match(String)} is going to return
		 * this enum literal.
		 */
		private T t;

		@SuppressWarnings("unchecked")
		private Node<T>[] children = new Node[26];

	}

	private Node<T> root = new Node<T>();

	protected void register(String key, T t) {
		if( !isWord(key) ) {
			throw new IllegalArgumentException( "'" + key + "' is not a word so can't be registered with a character-based trie" );
		}
		Node<T> currentNode = root;
		for( int i = 0; i < key.length(); i++ ) {
			int charIndex = Character.toLowerCase(key.charAt(i)) - 'a';
			Node<T> nextNode = currentNode.children[charIndex];
			if( nextNode == null ) {
				nextNode = new Node<T>();
				currentNode.children[charIndex] = nextNode;
			}
			if( i == key.length() -1 ) {
				nextNode.t = t;
			}
			currentNode = nextNode;
		}
	}

	/**
	 * Returns whether the given {@link String} consists solely of letter
	 * characters.
	 */
	public static boolean isWord(String key) {
		for( int i = 0; i < key.length(); i++ ) {
			if( !Character.isLetter(key.charAt(i)) ) {
				return false;
			}
		}
		return true;
	}

	@Override
	public T match(CharStream input) {
		Node<T> currentNode = root;
		T currentMatch = null;
		input.mark();	// invariant: input is marked before matching and after each (potentially partial) match
		int marks = 1;
		int ch = 0;		// (appease compiler by initializing ch)
		// invariant: currentNode != null <==> last character read matched the trie
		while( currentNode != null && ( ch = input.LA(1) ) != CharStream.EOF ) {
			input.consume();
			int charIndex = Character.toLowerCase(ch) - 'a';
			if( charIndex < 0 || charIndex >= 26 ) {
				input.rewind();
				input.release(marks);
				return currentMatch;
			}
			Node<T> nextNode = currentNode.children[charIndex];
			if( nextNode == null ) {
				input.rewind();
				input.release(marks);
				return currentMatch;
			}
			if( nextNode.t != null ) {
				currentMatch = nextNode.t;
				input.mark();
				marks++;
			}
			currentNode = nextNode;
		}
		if( ch == CharStream.EOF ) {
			input.rewind();
			input.release(marks);
			return currentMatch;
		}
		input.rewind();
		return null;
	}

}
