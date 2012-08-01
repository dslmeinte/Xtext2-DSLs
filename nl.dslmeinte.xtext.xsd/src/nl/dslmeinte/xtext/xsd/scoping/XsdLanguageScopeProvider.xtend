package nl.dslmeinte.xtext.xsd.scoping

import com.google.inject.Inject
import nl.dslmeinte.xtext.xsd.XsdExtensions
import nl.dslmeinte.xtext.xsd.xsdLanguage.ImportedReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.ImportedTopLevelComplexTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.ImportedTopLevelSimpleTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.ImportedTopLevelTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.Schema
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelComplexType
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelSimpleType
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelType
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

class XsdLanguageScopeProvider extends AbstractDeclarativeScopeProvider {

	@Inject extension XsdExtensions


	/**
	 * Returns a scope for all imports in the current schema.
	 * <p>
	 * The effective name of an import is the 'nsPrefix' attribute and not the
	 * name of the schema referenced. This is done to be able to avoid name
	 * collisions (and it mimics the actual XSD construct as well).
	 */
	def scope_Import(Schema schema, EReference eRef) {
		Scopes::scopeFor(schema.imports, [QualifiedName::create(it.nsPrefix)], IScope::NULLSCOPE)
	}

	def scope_TopLevelTypeReference_ref(ImportedTopLevelTypeReference ref, EReference eRef) {
		createTopLevelTypeScope(ref, typeof(TopLevelType))
	}

	def scope_TopLevelSimpleTypeReference_ref(ImportedTopLevelSimpleTypeReference ref, EReference eRef) {
		createTopLevelTypeScope(ref, typeof(TopLevelSimpleType))
	}

	def scope_TopLevelComplexTypeReference_ref(ImportedTopLevelComplexTypeReference ref, EReference eRef) {
		createTopLevelTypeScope(ref, typeof(TopLevelComplexType))
	}

	/**
	 * Returns a scope for a the {@code ref} cross-reference in a
	 * TopLevel<i>x</i>Reference object.
	 * 
	 * @param clazz
	 *            - the {@link Class} object corresponding to <i>x</i>; must
	 *            extend {@link TopLevelType}.
	 */
	def private createTopLevelTypeScope(ImportedReference importRef, Class<? extends TopLevelType> clazz) {
		val schema = schema(importRef, importRef.^import.nsPrefix)
		Scopes::scopeFor(schema.definitions.filter(clazz))
	}

	// TODO  use IResourceScopeCache here to do some (automatically evicted) caching...

}
