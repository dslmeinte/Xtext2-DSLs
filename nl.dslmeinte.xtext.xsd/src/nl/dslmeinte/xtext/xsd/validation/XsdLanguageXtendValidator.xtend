package nl.dslmeinte.xtext.xsd.validation

import com.google.inject.Inject
import nl.dslmeinte.xtext.xsd.XsdExtensions
import nl.dslmeinte.xtext.xsd.xsdLanguage.MaxLengthFacet
import nl.dslmeinte.xtext.xsd.xsdLanguage.XsdLanguagePackage
import org.eclipse.xtext.validation.Check

class XsdLanguageXtendValidator extends AbstractXsdLanguageJavaValidator {

	@Inject extension XsdExtensions

	val ePackage = XsdLanguagePackage::eINSTANCE


	@Check
	def void check_maxLength_restriction(MaxLengthFacet facet) {
		if( !facet.baseTypeString ) {
			error("maxLength facet can only be applied to string-based types", ePackage.maxLengthFacet_MaxLength)
		}
	}

	// TODO  warn-validation on enumeration values having more string length greater than maxLength (if present)

}
