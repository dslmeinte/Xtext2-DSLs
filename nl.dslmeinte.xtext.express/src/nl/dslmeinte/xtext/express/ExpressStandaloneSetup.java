
package nl.dslmeinte.xtext.express;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ExpressStandaloneSetup extends ExpressStandaloneSetupGenerated{

	public static void doSetup() {
		new ExpressStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

