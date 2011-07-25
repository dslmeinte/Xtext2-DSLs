
package nl.dslmeinte.xtext.dtd;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class DtdStandaloneSetup extends DtdStandaloneSetupGenerated{

	public static void doSetup() {
		new DtdStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

