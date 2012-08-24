package nl.dslmeinte.xtext.wsdl

import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Definitions
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.HttpTransport
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.SoapTransport
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Transport
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.XsdImport
import com.google.inject.Inject
import nl.dslmeinte.xtext.xsd.XsdExtensions

class WsdlExtensions {

	@Inject extension XsdExtensions


	def xsdImport(EObject it) {
		(EcoreUtil::getRootContainer(it) as Definitions).xsdImport
	}

	def resolve(XsdImport it) {
		resolveImport(eResource, importURI)
	}

	def hasSoapBindings(Definitions it) {
		!bindings.map[transport].filter(typeof(SoapTransport)).empty
	}

	def hasHttpBindings(Definitions it) {
		!bindings.map[transport].filter(typeof(HttpTransport)).empty
	}

	def name(Transport it) {
		eClass.name.replaceFirst("Transport", "").toFirstLower
	}

	def typeName(HttpTransport it) {
		type.toString
	}

}
