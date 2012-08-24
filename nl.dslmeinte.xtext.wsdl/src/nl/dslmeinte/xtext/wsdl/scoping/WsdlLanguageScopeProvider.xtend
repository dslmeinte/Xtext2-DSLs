package nl.dslmeinte.xtext.wsdl.scoping

import com.google.inject.Inject
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.Definitions
import nl.dslmeinte.xtext.wsdl.wsdlLanguage.XsdImport
import nl.dslmeinte.xtext.xsd.XsdExtensions
import nl.dslmeinte.xtext.xsd.xsdLanguage.Schema
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.mwe2.language.scoping.QualifiedNameProvider
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

class WsdlLanguageScopeProvider extends AbstractDeclarativeScopeProvider {

	@Inject extension XsdExtensions
	@Inject QualifiedNameProvider qualifiedNameProvider

	def IScope scope_Part_def(Definitions document, EReference ref) {
		Scopes::scopeFor(
			resolve(document.xsdImport).definitions,
			[qualifiedNameProvider.getFullyQualifiedName(it)],
			IScope::NULLSCOPE
		)
	}

	def private Schema resolve(XsdImport xsdImport) {
		resolveImport(xsdImport.eResource, xsdImport.importURI)
	}

	// TODO  we could use IResourceScopeCache here to do some (automatically evicted) caching...

}
