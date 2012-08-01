package nl.dslmeinte.xtext.xsd.parsing

import org.eclipse.xtext.conversion.ValueConverterException
import org.eclipse.xtext.conversion.impl.AbstractLexerBasedConverter
import org.eclipse.xtext.nodemodel.INode

class PATTERN_STRINGConverter extends AbstractLexerBasedConverter<String> {

	override protected String toEscapedString(String value) {
		"/" + value.replaceAll("/", "\\/") + "/"
	}

	override String toValue(String string, INode node) {
		if( string == null ) return null

		try {
			string.substring(1, string.length() - 1).replaceAll("\\/", "/")
		} catch (IllegalArgumentException e) {
			throw new ValueConverterException(e.getMessage(), node, e);
		}
	}

}
