package nl.dslmeinte.xtext.sgml.test.simplemarkup.ui;

import nl.dslmeinte.xtext.sgml.base.ui.syntaxcoloring.BaseTokenToAttributeIdMapper;
import nl.dslmeinte.xtext.sgml.lexer.SgmlLexerForParsing;
import nl.dsmeinte.xtext.sgml.base.lexer.ui.SgmlLexerForContentAssist;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Use this class to register components to be used within the IDE.
 */
public class SimpleMarkupUiModule extends nl.dslmeinte.xtext.sgml.test.simplemarkup.ui.AbstractSimpleMarkupUiModule {

	public SimpleMarkupUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	public void configureHighlightingLexer(Binder binder) {
		binder
			.bind(org.eclipse.xtext.parser.antlr.Lexer.class)
			.annotatedWith(Names.named(org.eclipse.xtext.ui.LexerUIBindings.HIGHLIGHTING))
			.to(SgmlLexerForParsing.class);
	}

	public void configureContentAssistLexer(Binder binder) {
		binder
			.bind(org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer.class)
			.annotatedWith(Names.named(org.eclipse.xtext.ui.LexerUIBindings.CONTENT_ASSIST))
			.to(SgmlLexerForContentAssist.class);
	}

	public void configureTokenToAttributeIDMapper(Binder binder) {
        binder
        	.bind(AbstractAntlrTokenToAttributeIdMapper.class)
        	.to(BaseTokenToAttributeIdMapper.class);
    }

}
