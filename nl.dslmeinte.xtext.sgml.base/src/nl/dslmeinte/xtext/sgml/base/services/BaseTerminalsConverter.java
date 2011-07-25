package nl.dslmeinte.xtext.sgml.base.services;

import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractDeclarativeValueConverterService;
import org.eclipse.xtext.nodemodel.INode;

public class BaseTerminalsConverter extends AbstractDeclarativeValueConverterService {

	@ValueConverter(rule = "QuotedString")
	public IValueConverter<String> QuotedString() {
		return new IValueConverter<String>(){
				@Override
				public String toString(String value) throws ValueConverterException {
					return "\"" + value + "\"";
				}
				@Override
				public String toValue(String string, INode node) throws ValueConverterException {
					return string.substring(1, string.length()-1);
					// Note that 'node' could be used to determine which characters to escape.
				}
			};
	}

}
