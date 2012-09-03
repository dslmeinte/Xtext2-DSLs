package nl.dslmeinte.examples.xtend.builders.dom

import org.eclipse.xtend.lib.Property

class DomElementLiteral {

	@Property String elementName
	@Property Class<? extends CompositeElement> superType
	@Property Class<? extends CompositeElement> containerElementType
	val attributeNames = <String>newArrayList

	def static createElement(String elementName, Class<? extends CompositeElement> superType, Class<? extends CompositeElement> containerElementType) {
		val literal = new DomElementLiteral()

		literal.elementName = elementName
		literal.superType = superType
		literal.containerElementType = containerElementType

		literal
	}

	private new() {}

	def addAttributeNames(String...attributeNames) {
		this.attributeNames.addAll(attributeNames.toList)
		this
	}

	def getAttributeNames() {
		attributeNames
	}

}
