package nl.dslmeinte.xtext.sgml.base.ui.guice;

import nl.dslmeinte.xtext.sgml.base.ui.syntaxcoloring.BaseTokenToAttributeIdMapper;
import nl.dslmeinte.xtext.sgml.lexer.SgmlLexerForParsing;
import nl.dslmeinte.xtext.sgml.lexer.ui.SgmlLexerForContentAssist;

import org.eclipse.xtext.ui.LexerUIBindings;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Support class which you can call from the UI Guice module of the UI project
 * of your Xtext language which is based on the Base SGML base language, to do
 * the appropriate bindings, like so:
 * 
 * <pre>
 * public void configureHighlightingLexer(Binder binder) {
 * 	UiModuleSupport.configureHighlightingLexer(binder);
 * }
 * 
 * public void configureContentAssistLexer(Binder binder) {
 * 	UiModuleSupport.configureContentAssistLexer(binder);
 * }
 * 
 * public Class&lt;? extends AbstractAntlrTokenToAttributeIdMapper&gt; bindAbstractAntlrTokenToAttributeIdMapper() {
 * 	return UiModuleSupport.bindAbstractAntlrTokenToAttributeIdMapper();
 * }
 * 
 * public void configureIResourceDescriptionsBuilderScope(Binder binder) {
 * 	UiModuleSupport.configureIResourceDescriptionsBuilderScope(binder);
 * }
 * </pre>
 * 
 * Use the last configuration only if you're using the clustering Xtext builder,
 * configured in the UI project's {@code plugin.xml}.
 * 
 * @author Meinte Boersma
 */
public class UiModuleSupport {

	/**
	 * Binds the generic SGML lexer (instead of the generated one), for lexing
	 * for the parsing for the semantic highlighter.
	 */
	public static void configureHighlightingLexer(Binder binder) {
		binder
			.bind(org.eclipse.xtext.parser.antlr.Lexer.class)
			.annotatedWith(Names.named(LexerUIBindings.HIGHLIGHTING))
			.to(SgmlLexerForParsing.class);
	}

	/**
	 * Binds the generic SGML lexer (instead of the generated one), for lexing
	 * for a parse without model creation for content assist.
	 */
	public static void configureContentAssistLexer(Binder binder) {
		binder
			.bind(org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer.class)
			.annotatedWith(Names.named(LexerUIBindings.CONTENT_ASSIST))
			.to(SgmlLexerForContentAssist.class);
	}

	/**
	 * Binds the {@link AbstractAntlrTokenToAttributeIdMapper} which takes care
	 * of correct highlighting for Xtext languages based on the {@code Base}
	 * SGML language.
	 */
	public static Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
		return BaseTokenToAttributeIdMapper.class;
	}

}
