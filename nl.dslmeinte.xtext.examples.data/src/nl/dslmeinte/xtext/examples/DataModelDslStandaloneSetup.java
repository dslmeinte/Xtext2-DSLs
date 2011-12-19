
package nl.dslmeinte.xtext.examples;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class DataModelDslStandaloneSetup extends DataModelDslStandaloneSetupGenerated{

	public static void doSetup() {
		new DataModelDslStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

