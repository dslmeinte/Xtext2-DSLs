
package nl.dslmeinte.xtext.conditional;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ConditionalStandaloneSetup extends ConditionalStandaloneSetupGenerated{

	public static void doSetup() {
		new ConditionalStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

