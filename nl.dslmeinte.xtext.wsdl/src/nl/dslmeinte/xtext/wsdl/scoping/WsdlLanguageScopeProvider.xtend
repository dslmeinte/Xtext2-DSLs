package nl.dslmeinte.xtext.wsdl.scoping

import com.google.inject.Inject
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Definitions
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.XsdImport
import nl.dslmeinte.xtext.xsd.XsdExtensions
import nl.dslmeinte.xtext.xsd.xsdLanguage.Schema
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

class WsdlLanguageScopeProvider extends AbstractDeclarativeScopeProvider {

	@Inject extension XsdExtensions
	@Inject extension IQualifiedNameConverter

	def IScope scope_Part_def(Definitions document, EReference ref) {
		val resolution = resolve(document.xsdImport)
		Scopes::scopeFor(
			resolution.definitions,
			[name.toQualifiedName],
			IScope::NULLSCOPE
		)
	}

	def private Schema resolve(XsdImport xsdImport) {
		resolveImport(xsdImport.eResource, xsdImport.importURI)
	}

}
