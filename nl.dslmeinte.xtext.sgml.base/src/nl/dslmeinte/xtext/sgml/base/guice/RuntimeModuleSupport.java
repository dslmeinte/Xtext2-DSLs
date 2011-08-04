package nl.dslmeinte.xtext.sgml.base.guice;

import nl.dslmeinte.xtext.sgml.base.services.BaseTerminalsConverter;
import nl.dslmeinte.xtext.sgml.lexer.SgmlLexerForParsing;

import org.eclipse.xtext.conversion.IValueConverterService;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Support class which you can call from the runtime Guice module of the
 * standalone project of your Xtext language which is based on the Base SGML
 * base language, to do the appropriate bindings, like so:
 * 
 * <pre>
 * public void configureRuntimeLexer(Binder binder) {
 * 	RuntimeModuleSupport.configureRuntimeLexer(binder);
 * }
 * 
 * public Class&lt;? extends IValueConverterService&gt; bindIValueConverterService() {
 * 	return RuntimeModuleSupport.bindIValueConverterService();
 * }
 * </pre>
 * 
 * @author Meinte Boersma
 */
public class RuntimeModuleSupport {

	/**
	 * Binds the generic SGML lexer (instead of the generated one), for lexing
	 * for a full parse including model creation.
	 */
	public static void configureRuntimeLexer(Binder binder) {
		binder
			.bind(org.eclipse.xtext.parser.antlr.Lexer.class)
			.annotatedWith(Names.named(org.eclipse.xtext.parser.antlr.LexerBindings.RUNTIME))
			.to(SgmlLexerForParsing.class);
	}

	/**
	 * Makes sure that instances of {@code QuotedString} have their surrounding
	 * quotation remarks removed from their syntax.
	 */
	public static Class<? extends IValueConverterService> bindIValueConverterService() {
		return BaseTerminalsConverter.class;
	}

}
