package nl.dslmeinte.xtext.sgml.lexer;

import nl.dslmeinte.xtext.util.antlr.trie.EnumBasedTrie;

/**
 * Enumeration of all the terminals in the base grammar ({@code Base.xtext}).
 * <p>
 * This enumeration is used to conveniently and safely map these terminals to
 * generated {@code int} identifiers.
 * 
 * @author Meinte Boersma
 */
public enum BaseTerminals {

	// TODO  add enough meta data so Base.xtext could be generated from this description (or vice versa ~ SingleSourcePrinciple)

	public_("PUBLIC"), system("SYSTEM"),
	open_tag("<"), close_tag(">"), slash("/"), equals("="),
	doctype("!DOCTYPE"), entity("!ENTITY"),
	open_bracket("["), close_bracket("]"),
	ampersand("&"), semicolon(";"),
	single_quote("'"), double_quote("\""),
	identifier, quoted_string,
	sgml_comments,
	header_comments,
	whitespace,
	literal_contents,
	;

	private String keyword;

	private BaseTerminals() {
		this.keyword = null;
	}

	private BaseTerminals(String keyword) {
		this.keyword = keyword;
	}

	public boolean isKeyword() {
		return keyword != null;
	}

	public String getKeyword() {
		return keyword;
	}

	public static BaseTerminals fromKeyword(String keyword) {
		for( BaseTerminals terminal : values() ) {
			if( terminal.isKeyword() && terminal.getKeyword().equalsIgnoreCase(keyword) ) {
				return terminal;
			}
		}

		return null;
	}

	/**
	 * Maps a {@link String} to a {@link BaseTerminals} literal, based on name
	 * alone. <em>Note</em> that you can postfix enum literal names with an
	 * underscore to work around Java reserved words.
	 */
	public static BaseTerminals fromName(String name) {
		for( BaseTerminals terminal : values() ) {
			String key = terminal.ruleName();
			if( key.equals(name) ) {
				return terminal;
			}
		}

		return null;
	}

	/**
	 * @return the name of the generated rule (identifier constant in the
	 *         generated ANTLR parser class) <em>without</em> the '{@code RULE_}
	 *         ' prefix.
	 */
	public String ruleName() {
		return EnumBasedTrie.escapeFromJava(name().toUpperCase());
	}

}
