package nl.dslmeinte.xtext.sgml.base.ui.syntaxcoloring;

import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;

import com.google.inject.Singleton;

/**
 * Maps token names to constants in {@link DefaultHighlightingConfiguration} to
 * do lexical highlighting of the input.
 * <p>
 * Bind it to the Guice UI binding to activate as follows:
 * <pre>
 * 	public static Class&lt;? extends AbstractAntlrTokenToAttributeIdMapper&gt; bindAbstractAntlrTokenToAttributeIdMapper() {
		return BaseTokenToAttributeIdMapper.class;
	}
 * </pre>
 * <p>
 * It uses the naming convention underlying {@code Base.xtext} and
 * {@code DTD2Xtext.xpt}.
 * 
 * @author Meinte Boersma
 */
@Singleton
public class BaseTokenToAttributeIdMapper extends AbstractAntlrTokenToAttributeIdMapper {

	@Override
	protected String calculateId(String tokenName, int tokenType) {
		if( tokenName.startsWith("'") && tokenName.endsWith("'") ) {
			if( ( tokenName.length() > 3 ) || ( Character.isLetter(tokenName.charAt(1)) ) ) {
				return DefaultHighlightingConfiguration.KEYWORD_ID;
			}
		}
		if( tokenName.equals("RULE_SGML_COMMENTS") || tokenName.equals("RULE_HEADER_COMMENTS") ) {
			return DefaultHighlightingConfiguration.COMMENT_ID;
		}
		if( tokenName.equals("RULE_QUOTED_STRING") ) {
			return DefaultHighlightingConfiguration.STRING_ID;
		}
		if( tokenName.equals("RULE_IDENTIFIER") ) {
			return DefaultHighlightingConfiguration.NUMBER_ID;	// grey
		}
		return DefaultHighlightingConfiguration.DEFAULT_ID;
	}

}