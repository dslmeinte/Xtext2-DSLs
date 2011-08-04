package nl.dslmeinte.xtext.sgml.base;

import nl.dslmeinte.xtext.sgml.base.base.BasePackage;

import org.eclipse.emf.ecore.EPackage;

import com.google.inject.Injector;

/**
 * Initialization support for running Xtext languages without Equinox extension
 * registry.
 */
public class BaseStandaloneSetup extends BaseStandaloneSetupGenerated {

	public static void doSetup() {
		new BaseStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	/**
	 * Don't register an extension to avoid unnecessary overlap with other EMF
	 * languages.
	 */
	@Override
	public void register(@SuppressWarnings("unused") Injector injector) {
		if( !EPackage.Registry.INSTANCE.containsKey(BasePackage.eNS_URI) ) {
			EPackage.Registry.INSTANCE.put(BasePackage.eNS_URI, BasePackage.eINSTANCE);
		}
	}

}

