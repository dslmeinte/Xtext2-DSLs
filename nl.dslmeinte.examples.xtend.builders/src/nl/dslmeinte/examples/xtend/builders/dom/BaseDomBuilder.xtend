package nl.dslmeinte.examples.xtend.builders.dom

abstract class BaseDomBuilder {

	/**
	 * Builds a {@link TextElement} with the given text, appending it
	 * to a {@link CompositeElement}.
	 */
	def void $(CompositeElement container, CharSequence text) {
		new Text(text).appendTo(container)
	}

	/*
	 * For each non-abstract CompositeElement, an Xtension will be provided which can be used as a builder.
	 * 
	 * Note the following:
	 * 		- the function(al) signature (T)=>void essentially corresponds to a method/procedure operating on an instance of T;
	 * 		- the code pattern for U as child of T is:
	 * 
	 * def u(T parent, (U)=>void subBuilder) {
	 *   new U().subBuildWith(subBuilder).appendTo(parent)
	 * }
	 * 
	 * 			where 'u' is the literal for U with first character in lower case.
	 */

	/**
	 * Creates the HTML root.
	 * <p>
	 * (Note that this is a special, non-generateable case.)
	 */
	def html((Html)=>void subBuilder) {
		new Html().subBuildWith(subBuilder)
	}

	/**
	 * Executes the given sub builder on the given object, returning the augmented object.
	 * Essentially, this is the fluent version of {@link Procedure1#apply(..)}.
	 */
	def protected <T> T subBuildWith(T object, (T)=>void subBuilder) {
		subBuilder.apply(object)
		object
	}

	/**
	 * Appends the given {@link Element child element} to the given
	 * {@link CompositeElement composite element}.
	 */
	def protected void appendTo(Element child, CompositeElement parent) {
		parent.children += child
	}

}
