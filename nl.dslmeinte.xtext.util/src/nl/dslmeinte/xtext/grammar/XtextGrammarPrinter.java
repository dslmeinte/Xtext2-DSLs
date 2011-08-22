package nl.dslmeinte.xtext.grammar;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.grammaranalysis.impl.GrammarElementTitleSwitch;
import org.eclipse.xtext.impl.AbstractElementImpl;

/**
 * Util class which prints more useful information on Xtext grammar elements
 * than the {@link AbstractElementImpl#toString()} implementation does.
 * 
 * @author Meinte Boersma
 */
@SuppressWarnings("restriction")
public class XtextGrammarPrinter {

	public static String toString(EObject object) {
		if( object == null ) {
			return "null"; // $NON-NLS-1$
		}
		// TODO  add check that object is an instance of an EClass in the XtexPackage
		return new GrammarElementTitleSwitch().doSwitch(object);
	}

}
