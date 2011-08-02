
package nl.dslmeinte.xtext.express.expression;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ExpressionStandaloneSetup extends ExpressionStandaloneSetupGenerated{

	public static void doSetup() {
		new ExpressionStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

