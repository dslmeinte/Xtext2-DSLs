package nl.dslmeinte.examples

import com.google.inject.Inject
import nl.dslmeinte.examples.xtend.builders.dom.DomBuilder

class HtmlDocumentExample {

	@Inject extension DomBuilder

	def kotlinExample(String...args) {
		html [
			head [
				title [$("XML encoding with Xtend")]
			]
			body [
				h1 [$("XML encoding with Xtend")]
				p [$("this format can be used as an alternative markup to XML")]

				// an element with attributes and text content:
				a("http://www.eclipse.org/Xtext") [$("Xtend")]

				// mixed content:
				p [
					$("This is some")
					b[$("mixed")]
					$("text. For more see the ")
					a("http://www.eclipse.org/Xtext") [$("Xtext")]
					$("project.")
				]
				p [ $("some text") ]

				// generated content:
				p [
					for( arg : args ) {
						$(arg)
					}
				]
			]
		]
	}

}
