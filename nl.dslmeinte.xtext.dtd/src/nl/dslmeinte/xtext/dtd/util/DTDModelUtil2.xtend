package nl.dslmeinte.xtext.dtd.util

import org.eclipse.emf.ecore.util.EcoreUtil

import nl.dslmeinte.xtext.dtd.dtdModel.Element
import nl.dslmeinte.xtext.dtd.dtdModel.DocumentTypeDefinition
import nl.dslmeinte.xtext.dtd.dtdModel.AttributeList
import nl.dslmeinte.xtext.dtd.dtdModel.Attribute

import java.util.Collection
import java.util.List
import java.util.ArrayList

/**
 * Xtend2 version of {@link DTDModelUtil}.
 * 
 * @author Meinte Boersma
 */
class DTDModelUtil2 {

	def attributes(Element element) {
		val root = EcoreUtil::getRootContainer(element) as DocumentTypeDefinition
		val attributesList = root.definitions.filter(typeof(AttributeList)).filter( d | d.element == element )
		val List<Attribute> attributes = new ArrayList<Attribute>()
		for( attributeList : attributesList ) {
			attributes.addAll(attributeList.attributes)
		}
		// a nicer construction using .fold should exist!
		return attributes
	}

}
