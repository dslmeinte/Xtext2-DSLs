package nl.dslmeinte.xtext.xsd;

import nl.dslmeinte.xtext.xsd.parsing.CustomConversions;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;

public class XsdLanguageRuntimeModule extends nl.dslmeinte.xtext.xsd.AbstractXsdLanguageRuntimeModule {

	/**
	 * Bind some extra {@link IValueConverter}s on top of the {@link DefaultTerminalConverters}.
	 */
	public Class<? extends org.eclipse.xtext.conversion.IValueConverterService> bindIValueConverterService() {
		return CustomConversions.class;
	}

}
