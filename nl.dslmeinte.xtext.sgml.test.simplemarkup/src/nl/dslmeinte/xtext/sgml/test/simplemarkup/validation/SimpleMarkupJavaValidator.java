package nl.dslmeinte.xtext.sgml.test.simplemarkup.validation;

import java.io.StringReader;

import nl.dslmeinte.xtext.conditional.ConditionalStandaloneSetup;
import nl.dslmeinte.xtext.conditional.parser.antlr.ConditionalParser;
import nl.dslmeinte.xtext.sgml.test.simplemarkup.simplemarkup.Condition;
import nl.dslmeinte.xtext.sgml.test.simplemarkup.simplemarkup.SimplemarkupPackage;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;

public class SimpleMarkupJavaValidator extends AbstractSimpleMarkupJavaValidator {

	private static final ConditionalParser conditionalParser =
			new ConditionalStandaloneSetup().createInjector().getInstance(ConditionalParser.class);

		/**
		 * Checks {@link Condition}s by invoking the {@code Conditional} parser for
		 * it and propagating (currently: only parse) errors.
		 * <p>
		 * This makes for a relatively crude composition of conditional expressions
		 * in the SimpleMarkup language (i.e., no syntax highlighting, no content
		 * assist, etc.), but it is by far the easiest and cleanest method to do so.
		 */
		@Check(CheckType.FAST)
		public void parseCondition(Condition condition) {
			StringReader input = new StringReader(condition.getExpr());
			IParseResult parseResult = conditionalParser.parse(input);
			if( parseResult.getSyntaxErrors().iterator().hasNext() ) {
				StringBuilder message = new StringBuilder("error(s) while parsing condition:"); //$NON-NLS-1$
				for( INode node : parseResult.getSyntaxErrors() ) {
					message.append("\n\t").append(node.getSyntaxErrorMessage()); //$NON-NLS-1$
				}
				error(message.toString(), SimplemarkupPackage.eINSTANCE.getCondition_Expr());
			}
			// note: only _parse_ errors are reported (i.e., no semantic errors - use Diagnostician for that?)
		}

}
