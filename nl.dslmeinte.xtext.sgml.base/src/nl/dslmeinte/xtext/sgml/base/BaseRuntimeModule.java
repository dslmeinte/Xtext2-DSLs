package nl.dslmeinte.xtext.sgml.base;

import nl.dslmeinte.xtext.sgml.base.services.BaseTerminalsConverter;

import org.eclipse.xtext.conversion.IValueConverterService;

/**
 * Use this class to register components to be used at runtime / without the
 * Equinox extension registry.
 */
public class BaseRuntimeModule extends nl.dslmeinte.xtext.sgml.base.AbstractBaseRuntimeModule {

	@Override
	public Class<? extends IValueConverterService> bindIValueConverterService() {
		return BaseTerminalsConverter.class;
	}

}
