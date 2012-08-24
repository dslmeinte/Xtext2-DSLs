
package nl.dslmeinte.xtext.wsdl;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class WsdlLanguageStandaloneSetup extends WsdlLanguageStandaloneSetupGenerated{

	public static void doSetup() {
		new WsdlLanguageStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

