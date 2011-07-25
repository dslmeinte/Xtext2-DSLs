package nl.dslmeinte.xtext.parser.util;

import java.lang.reflect.Method;

import nl.dslmeinte.reflect.ReflectionUtil;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.eclipse.xtext.nodemodel.impl.LeafNode;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;

/**
 * Util class to help with "hacking" into ANTLR parsers to do re-parsing.
 * 
 * @author Meinte Boersma
 */
public class ParsingOverrideUtil {

	private static final Method createLeafNodeMethod;
	private static final Method tokenConsumedMethod;

	static {
		Class<AbstractInternalAntlrParser> parserClass = AbstractInternalAntlrParser.class;
		try {
			createLeafNodeMethod = parserClass.getDeclaredMethod("createLeafNode", Token.class, boolean.class);
			createLeafNodeMethod.setAccessible(true);
			tokenConsumedMethod = parserClass.getDeclaredMethod("tokenConsumed", Token.class, LeafNode.class);
			tokenConsumedMethod.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static LeafNode createLeafNode(AbstractInternalAntlrParser parser, Token token, boolean hidden) {
		return (LeafNode) ReflectionUtil.invoke(createLeafNodeMethod, parser, token, hidden);
	}

	public static void tokenConsumed(AbstractInternalAntlrParser parser, Token token, LeafNode node) {
		ReflectionUtil.invoke(tokenConsumedMethod, parser, token, node);
	}

	public static Token createReplacementToken(CommonToken oldToken, int type, String text, int offset) {
		CommonToken newToken = new CommonToken(oldToken);
		newToken.setType(type);
		newToken.setText(text);
		newToken.setStartIndex(oldToken.getStartIndex() + offset);
		newToken.setStopIndex(oldToken.getStartIndex() + offset + text.length());
		newToken.setCharPositionInLine(oldToken.getCharPositionInLine() + offset);
		return newToken;
	}

}
