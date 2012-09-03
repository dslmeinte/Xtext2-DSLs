package nl.dslmeinte.examples.xtend.builders.dom

import org.eclipse.xtend.lib.Property

/**
 * Representation of a HTML DOM element (type).
 * 
 * @author Meinte Boersma (c) 2012
 */
class DomElementLiteral {

	@Property String elementName
	@Property Class<? extends CompositeElement> superType

	private Class<? extends CompositeElement> containerElementType
	private DomElementLiteral containerElementLiteral

	def static createElement(String elementName, Class<? extends CompositeElement> superType, DomElementLiteral containerElementLiteral) {
		val literal = new DomElementLiteral()

		literal.elementName = elementName
		literal.superType = superType
		literal.containerElementLiteral = containerElementLiteral

		literal
	}

	def static createElement(String elementName, Class<? extends CompositeElement> superType, Class<? extends CompositeElement> containerElementType) {
		val literal = new DomElementLiteral()

		literal.elementName = elementName
		literal.superType = superType
		literal.containerElementType = containerElementType

		literal
	}

	private new() {}	// private constructor to prevent direct (mutable) instantiation


	// (prevent generation of getter for attributeNames)

	val attributeNames = <String>newArrayList

	def addAttributeNames(String...attributeNames) {
		this.attributeNames.addAll(attributeNames.toList)
		this
	}

	def getAttributeNames() {
		attributeNames
	}


	/**
	 * @returns The name (which is not guaranteed to start on an uppercase character) of the container element.
	 */
	def containerElementName() {
		if( containerElementType != null ) containerElementType.simpleName else containerElementLiteral.elementName
	}

}
