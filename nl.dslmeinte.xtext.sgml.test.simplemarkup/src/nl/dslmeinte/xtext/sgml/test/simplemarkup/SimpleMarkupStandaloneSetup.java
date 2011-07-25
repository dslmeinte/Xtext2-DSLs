
package nl.dslmeinte.xtext.sgml.test.simplemarkup;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class SimpleMarkupStandaloneSetup extends SimpleMarkupStandaloneSetupGenerated{

	public static void doSetup() {
		new SimpleMarkupStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

