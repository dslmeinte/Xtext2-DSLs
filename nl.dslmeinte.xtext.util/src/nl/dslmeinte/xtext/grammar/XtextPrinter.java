package nl.dslmeinte.xtext.grammar;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.impl.AbstractElementImpl;
import org.eclipse.xtext.util.XtextSwitch;

/**
 * Util class which prints more useful information on Xtext grammar elements
 * than the {@link AbstractElementImpl#toString()} implementation.
 * 
 * @author Meinte Boersma
 */
public class XtextPrinter {

	private final static Printer printer = new Printer();

	public static String toString(EObject object) {
		return printer.doSwitch(object);
	}

	private static class Printer extends XtextSwitch<String>  {

		@Override
		public String caseRuleCall(RuleCall object) {
			String inner = doSwitch(object.getRule());
			if( inner.contains(" ") ) {
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

		/**
		 * Use the default.
		 */
		@Override
		public String defaultCase(EObject object) {
			return object.toString();
		}

		/**
		 * Avoid printing {@code null} for the default (1-) cardinality.
		 */
		private String cardinalityDisplay(String cardinality) {
			return( cardinality == null ? "" : cardinality );
		}

	}

}
