package nl.dslmeinte.xtext.util.antlr.trie;

/**
 * Implementation of {@link CaseInsensitiveTrie} which matches the enum's
 * literal's names.
 * <p>
 * Note that you can work around Java reserved words by postfixing an enum
 * literal with an underscore, e.g. with {@code public_}.
 * 
 * @author Meinte Boersma
 * 
 * @param <T>
 *            The <b>enum</b> to return.
 */
public class EnumBasedTrie<T extends Enum<T>> extends TrieSupport<T> {

	private EnumBasedTrie(T...enumLiterals) {
		for( T enumLiteral : enumLiterals ) {
			register(escapeFromJava(enumLiteral.name()), enumLiteral);
		}
	}

	/**
	 * Static factory method for an {@link EnumBasedTrie}.
	 */
	public static <T extends Enum<T>> CaseInsensitiveTrie<T> of(T...enumLiterals) {
		return new EnumBasedTrie<T>(enumLiterals);
	}

	/**
	 * Static factory method for an {@link EnumBasedTrie}.
	 * <p>
	 * Usage:
	 * <pre>
	 * MyEnum matchedLiteral = EnumDrivenTrie.of(MyEnum.class).match(..);
	 * </pre>
	 */
	public static <T extends Enum<T>> CaseInsensitiveTrie<T> of(Class<T> enumClass) {
		return new EnumBasedTrie<T>(enumClass.getEnumConstants());
	}

	/**
	 * Removes a postfix underscore (if present) to work around reserved Java
	 * words. (Util method.)
	 */
	public static String escapeFromJava(String key) {
		if( key.endsWith("_") ) {
			key = key.substring(0, key.length() - 1);
		}

		return key;
	}

}
