package nl.dslmeinte.examples.xtend.builders.dom

import java.util.List
import java.util.Map
import org.eclipse.xtend.lib.Data

/**
 * Any HTML element.
 */
@Data abstract class Element {}

/**
 * Any HTML text - think of a single {@code span}.
 */
@Data class Text extends Element {
	CharSequence text
}

/**
 * An HTML element that has children and attributes.
 */
@Data abstract class CompositeElement extends Element {
	List<Element> children = newArrayList
	Map<String, String> attributes = newHashMap
}

@Data abstract class ContentTag extends CompositeElement {}

@Data class Html extends CompositeElement {}

