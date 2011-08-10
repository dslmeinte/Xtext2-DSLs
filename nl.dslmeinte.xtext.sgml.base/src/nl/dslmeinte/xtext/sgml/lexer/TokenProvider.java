package nl.dslmeinte.xtext.sgml.lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider;
import org.eclipse.xtext.util.Strings;

import com.google.inject.Inject;

/**
 * Class to compute a list of (currently only keyword) tokens.
 * 
 * TODO  create a util class to provide all kinds of information on "SGML-type" grammars
 * 
 * @author Meinte Boersma
 */
public class TokenProvider {

	private final List<String> keywordTokens = new ArrayList<String>();

	/**
	 * <em>WARNING</em>: code duplicated from {@link AntlrTokenFacade}. (No time
	 * for proper Refactoring.)
	 */
	@Inject
	public TokenProvider(AntlrTokenDefProvider tokenDefProvider) {
		for( Entry<Integer, String> entry : tokenDefProvider.getTokenDefMap().entrySet() ) {
			String tokenDescription = entry.getValue();
			if( tokenDescription.startsWith("'") && tokenDescription.endsWith("'") ) { //$NON-NLS-1$ //$NON-NLS-2$
				String keyword = tokenDescription.substring(1, tokenDescription.length()-1);
				keyword = Strings.convertFromJavaString(keyword, true);
				keyword = keyword.intern();	// optimization attempt: avoid duplicate String-s
				keywordTokens.add(keyword);
			}
		}
	}

	public List<String> getKeywordTokens() {
		return Collections.unmodifiableList(keywordTokens);
	}

}
