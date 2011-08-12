package nl.dslmeinte.xtext.grammar;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.impl.AbstractElementImpl;
import org.eclipse.xtext.util.XtextSwitch;

/**
 * Util class which prints more useful information on Xtext grammar elements
 * than the {@link AbstractElementImpl#toString()} implementation does.
 * 
 * @author Meinte Boersma
 */
public class XtextGrammarPrinter {

	private final static Printer printer = new Printer();

	public static String toString(EObject object) {
		if( object == null ) {
			return "null"; // $NON-NLS-1$
		}
		// TODO  add check that object is an instance of an EClass in the XtexPackage
		return printer.doSwitch(object);
	}

	private static class Printer extends XtextSwitch<String>  {

		// TODO  add cases as you find the need for them

		@Override
		public String caseRuleCall(RuleCall object) {
			String inner = doSwitch(object.getRule());
			if( inner.contains(" ") || inner.contains("=") ) {
				inner = "(" + inner + ")";
			}
			return inner + cardinalityDisplay(object.getCardinality());
		}

		@Override
		public String caseParserRule(ParserRule object) {
			return object.getName();
		}

		@Override
		public String caseKeyword(Keyword object) {
			return "'" + object.getValue() + "'" + cardinalityDisplay(object.getCardinality());
		}

		/**
		 * Prints just the name of the terminal rule, not the structure.
		 */
		@Override
		public String caseTerminalRule(TerminalRule object) {
			return object.getName();
		}

		@Override
		public String caseAssignment(Assignment object) {
			return cardinalityDisplay(object.getOperator()) + object.getFeature() + cardinalityDisplay(object.getCardinality());
		}

		/**
		 * Use the default for everything else, adding {@code " default!"} to
		 * the {@link String} returned to provide some incentive to add the
		 * case.
		 */
		@Override
		public String defaultCase(EObject object) {
			return object.toString() + " default!";
		}

		/**
		 * Avoid printing {@code null} for the default (1-) cardinality.
		 */
		private String cardinalityDisplay(String cardinality) {
			return( cardinality == null ? "" : cardinality );
		}

	}

}
