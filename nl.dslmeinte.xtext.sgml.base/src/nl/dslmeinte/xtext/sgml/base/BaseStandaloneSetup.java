
package nl.dslmeinte.xtext.sgml.base;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class BaseStandaloneSetup extends BaseStandaloneSetupGenerated{

	public static void doSetup() {
		new BaseStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

