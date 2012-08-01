package nl.dslmeinte.xtext.xsd.parsing;

import java.util.regex.Pattern;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.conversion.ValueConverter;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Custom {@link IValueConverterService} implementation taking of conversion of
 * the String format (concrete syntax) to the corresponding data type (and vice
 * versa).
 * <p>
 * For this language, this pertains to converting regexp patterns like
 * "/-?\d{1,11}.\d{3}/" into something that's usable as a Java regexp
 * {@link Pattern}.
 */
@Singleton
public class CustomConversions extends DefaultTerminalConverters {

	@Inject private PATTERN_STRINGConverter patternStringValueConverter;

	@ValueConverter(rule = "PATTERN_STRING")
	public IValueConverter<String> PATTERN_STRING() {
		return patternStringValueConverter;
	}

}