
package nl.dslmeinte.xtext.examples;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class DynamicScreenDslStandaloneSetup extends DynamicScreenDslStandaloneSetupGenerated{

	public static void doSetup() {
		new DynamicScreenDslStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

