package nl.dslmeinte.xtext.sgml.contenttype;

/**
 * Token types used to recognize SGML documents using
 * {@link SimpleSgmlRecognizer}.
 * 
 * @author Meinte Boersma
 */
enum Terminals {

	/**
	 * A contiguous sequence of whitespace characters, as determined by
	 * {@link Character#isWhitespace(int)}.
	 */
	whitespace,
	/**
	 * {@code <!-- ... -->}
	 */
	sgml_comments,
	/**
	 * A single {@code '<'}.
	 */
	open_tag,
	/**
	 * End Of File.
	 */
	EOF,
	/**
	 * Anything other than the other token types.
	 */
	textual_contents,
	;

}
