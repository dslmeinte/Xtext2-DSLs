package nl.dslmeinte.xtext.xsd

import nl.dslmeinte.xtext.xsd.xsdLanguage.BuiltinType
import nl.dslmeinte.xtext.xsd.xsdLanguage.BuiltinTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.Facet
import nl.dslmeinte.xtext.xsd.xsdLanguage.Import
import nl.dslmeinte.xtext.xsd.xsdLanguage.MaxLengthFacet
import nl.dslmeinte.xtext.xsd.xsdLanguage.NonComplexTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.Restriction
import nl.dslmeinte.xtext.xsd.xsdLanguage.Schema
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelSimpleTypeReference
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.EcoreUtil2

class XsdExtensions {

	// TODO  JavaDoc
	def baseType(NonComplexTypeReference it) {
		switch it {
			BuiltinTypeReference:			it.builtin
			TopLevelSimpleTypeReference:	{
				it.ref.restriction?.typeRef.baseType
			}
			default:
				throw new IllegalArgumentException(
					"don't know how to compute 'baseType' for type "
					+ it.eClass.name
					+ " (language engineer error)"
				)
				
		}

	}

	// TODO  JavaDoc
	def restriction(Facet facet) {
		facet.eContainer as Restriction
	}

	// TODO  JavaDoc
	def baseType(Facet facet) {
		restriction(facet).typeRef.baseType
	}

	// TODO  JavaDoc
	def isBaseTypeString(Facet facet) {
		facet.baseType == BuiltinType::STRING
	}

	/**
	 * Computes the value of the (first) max-length restriction (among the
	 * facets), returning {@literal -1} in case there's no max-length
	 * restriction.
	 */
	def maxLength(Restriction restriction) {
		for( Facet facet : restriction.getFacets() ) {
			if( facet instanceof MaxLengthFacet ) {
				return (facet as MaxLengthFacet).maxLength
			}
		}

		return -1
	}

	/**
	 * Resolves the given schema import.
	 */
	def resolveImport(Import importElt) {
		resolveImport(importElt.eResource, importElt.importURI)
	}

	def resolveImport(Resource resource, String uri) {
		val importResource = EcoreUtil2::getResource(resource, uri)
		importResource.contents.head as Schema
	}

	/**
	 * Returns the schema imported using the given name space prefix.
	 * 
	 * @param eObject
	 *            - a model element in the current XSD representation
	 * @param prefix
	 *            - the name space prefix
	 */
	def schema(EObject eObject, String prefix) {
		val thisSchema = EcoreUtil::getRootContainer(eObject) as Schema
		for( Import importElt : thisSchema.getImports() ) {
			if( importElt.nsPrefix == prefix ) {
				return resolveImport(importElt)
			}
		}
	
		throw new IllegalArgumentException(
				"no import with prefix '" + prefix + "' in this schema"
			)
	}

}