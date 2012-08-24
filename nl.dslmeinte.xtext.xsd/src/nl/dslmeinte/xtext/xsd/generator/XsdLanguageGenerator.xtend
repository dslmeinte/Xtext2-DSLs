package nl.dslmeinte.xtext.xsd.generator

import com.google.inject.Inject
import nl.dslmeinte.xtext.xsd.XsdExtensions
import nl.dslmeinte.xtext.xsd.xsdLanguage.Attribute
import nl.dslmeinte.xtext.xsd.xsdLanguage.BuiltinTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.ComplexType
import nl.dslmeinte.xtext.xsd.xsdLanguage.DirectTopLevelComplexTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.DirectTopLevelSimpleTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.DirectTopLevelTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.Documentation
import nl.dslmeinte.xtext.xsd.xsdLanguage.Element
import nl.dslmeinte.xtext.xsd.xsdLanguage.EmbeddedComplexType
import nl.dslmeinte.xtext.xsd.xsdLanguage.EmbeddedSimpleType
import nl.dslmeinte.xtext.xsd.xsdLanguage.EnumerationFacet
import nl.dslmeinte.xtext.xsd.xsdLanguage.Import
import nl.dslmeinte.xtext.xsd.xsdLanguage.ImportedTopLevelComplexTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.ImportedTopLevelSimpleTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.ImportedTopLevelTypeReference
import nl.dslmeinte.xtext.xsd.xsdLanguage.LengthFacet
import nl.dslmeinte.xtext.xsd.xsdLanguage.MaxLengthFacet
import nl.dslmeinte.xtext.xsd.xsdLanguage.Multiplicity
import nl.dslmeinte.xtext.xsd.xsdLanguage.PatternFacet
import nl.dslmeinte.xtext.xsd.xsdLanguage.RangeFacet
import nl.dslmeinte.xtext.xsd.xsdLanguage.Restriction
import nl.dslmeinte.xtext.xsd.xsdLanguage.Schema
import nl.dslmeinte.xtext.xsd.xsdLanguage.SimpleType
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelComplexType
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelElement
import nl.dslmeinte.xtext.xsd.xsdLanguage.TopLevelSimpleType
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

class XsdLanguageGenerator implements IGenerator {

	@Inject extension XsdExtensions


	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		val model = resource.contents.head as Schema
		fsa.generateFile(model.name + ".xsd", model.xsdFile)
	}

	def private xsdFile(Schema it)
		'''
		<?xml version="1.0" encoding="UTF-8" standalone="no"?>
		<xs:schema
		  xmlns:tns="«nsURI»"
		  targetNamespace="«nsURI»"
		  xmlns:xs="http://www.w3.org/2001/XMLSchema"
		«FOR it : it.imports»
		  xmlns:«nsPrefix»="«resolveImport.nsURI»"
		«ENDFOR»
		«IF it.imports.size > 0 »  elementFormDefault="unqualified"«ENDIF»
		>

			«inner»

		</xs:schema>
		'''

	def private inner(Schema it)
		'''
		«FOR it : it.imports»
			«import_»
		«ENDFOR»
		«FOR it : it.definitions»
			«definition»
		«ENDFOR»
		'''

	def private import_(Import it)
		'''
		  <xs:import namespace="«resolveImport.nsURI»" schemaLocation="«resolveImport.name».xsd" />
		'''

	def private dispatch definition(TopLevelElement it)
		'''
		<xs:element name="«name»"«IF typeRef != null» type="«typeRef.typeRef»" />«ELSE»>
			«embeddedTypeDef.definition»
		</xs:element>
		«ENDIF»
		'''

	def private dispatch definition(TopLevelComplexType it)
		'''
		«IF documentation != null»  <!-- «documentation» -->«ENDIF»
		<xs:complexType name="«name»"«IF ^abstract» abstract="true"«ENDIF»>
			«innerDefinition»
		</xs:complexType>
		'''

	def private dispatch definition(EmbeddedComplexType it)
		'''
		<xs:complexType>
			«innerDefinition»
		</xs:complexType>
		'''

	def private dispatch definition(TopLevelSimpleType it)
		'''
		<xs:simpleType name="«name»">
			«documentation.documentation»
			«innerDefinition»
		</xs:simpleType>
		'''

	def private dispatch definition(EmbeddedSimpleType it)
		'''
		<xs:simpleType>
			«documentation.documentation»
			«innerDefinition»
		</xs:simpleType>
		'''

	def private dispatch innerDefinition(ComplexType it)
		'''
		«IF ^extends != null»
			<xs:complexContent>
			<xs:extension base="«^extends.typeRef»">
		«ENDIF»
			<xs:sequence>
			«FOR property : properties.filter(typeof(Element))»
				«property.property»
			«ENDFOR»
			</xs:sequence>
		«FOR property : properties.filter(typeof(Attribute))»
			«property.property»
		«ENDFOR»
		«IF ^extends != null»
		      </xs:extension>
		    </xs:complexContent>
		«ENDIF»
		'''
		
	def private dispatch innerDefinition(SimpleType it) {
		restriction?.restriction
	}

	def private restriction(Restriction it)
		'''
	    <xs:restriction base="«typeRef.typeRef»">
	    	«FOR facet : facets»
	    		«facet.restrictionVariant»
	    	«ENDFOR»
	    </xs:restriction>
		'''

	def private dispatch restrictionVariant(EnumerationFacet it)
		'''
		«FOR literal : literals»
			<xs:enumeration value="«literal.value»"«literal.annotation?.documentation»>
			</xs:enumeration>
		«ENDFOR»
		'''

	def private dispatch restrictionVariant(MaxLengthFacet it)
		'''<xs:maxLength value="«maxLength»" />'''

	def private dispatch restrictionVariant(PatternFacet it)
		'''<xs:pattern value="«pattern»" />'''

	def private dispatch restrictionVariant(RangeFacet it)
		'''
		<xs:minInclusive value="«lower»" />
		<xs:maxInclusive value="«upper»" />
		'''
		
	def private dispatch restrictionVariant(LengthFacet it)
		'''<xs:length value="«length»" />'''

	def private dispatch property(Element it)
		'''
		<xs:element name="«name»"«multiplicity.multiplicity»«IF typeRef != null» type="«typeRef.typeRef»" />
		«ELSE»>
			«embeddedTypeDef.definition»
		</xs:element>
		«ENDIF»
		'''

	def private dispatch property(Attribute it)
		'''<xs:attribute name="«name»" type="«typeRef.typeRef»" «IF required»use="required"«ENDIF»/>'''

	def private dispatch multiplicity(Multiplicity it)
		''' minOccurs="«lower»" maxOccurs="«IF unbounded»unbounded«ELSE»«upper»«ENDIF»" '''
	def private dispatch multiplicity(Void it)
		''''''
		// Note: a bit of hack to be able to do away with ugly 'if != null' checks

	def private dispatch typeRef(BuiltinTypeReference it)					'''xs:«builtin»'''
	def private dispatch typeRef(DirectTopLevelTypeReference it)			'''tns:«ref.name»'''
	def private dispatch typeRef(DirectTopLevelSimpleTypeReference it)		'''tns:«ref.name»'''
	def private dispatch typeRef(DirectTopLevelComplexTypeReference it)		'''tns:«ref.name»'''
	def private dispatch typeRef(ImportedTopLevelTypeReference it)			'''«^import.nsPrefix»:«ref.name»'''
	def private dispatch typeRef(ImportedTopLevelSimpleTypeReference it)	'''«^import.nsPrefix»:«ref.name»'''
	def private dispatch typeRef(ImportedTopLevelComplexTypeReference it)	'''«^import.nsPrefix»:«ref.name»'''
	// TODO  introduce convenience types to simplify generation

	def private dispatch documentation(Documentation it)	{ text.documentation }
	def private dispatch documentation(Void it)				''''''
	def private dispatch documentation(String str)
		'''
		<xs:annotation>
			<xs:documentation>«this»</xs:documentation>
		</xs:annotation>
		'''

}
