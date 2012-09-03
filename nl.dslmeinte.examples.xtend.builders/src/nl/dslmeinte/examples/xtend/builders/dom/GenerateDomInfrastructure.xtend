package nl.dslmeinte.examples.xtend.builders.dom

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

import static nl.dslmeinte.examples.xtend.builders.dom.DomElementLiteral.*

class GenerateDomInfrastructure {

	def static void main(String...args) {
		new GenerateDomInfrastructure().run()
	}

	def private run() {
		val classQName = ^class.name
		packageName = classQName.substring(0, classQName.lastIndexOf('.'))
		genDir = new File("src/" + packageName.replaceAll('\\.', '/'))

		val elements = mkElements
		elements.generateDomElements.dumpTo("GeneratedDomElements.xtend")
		elements.generateDomBuilder.dumpTo("DomBuilder.xtend")
	}


	private String packageName
	private File genDir

	def private dumpTo(CharSequence contents, String fileName) {
		val file = new File(genDir.absolutePath + "/" + fileName)
		val writer = new PrintWriter(new FileOutputStream(file))
		writer.println(contents)
		writer.close
	}


	def private mkElements() {
		val compositeType = typeof(CompositeElement)
		val contentType = typeof(ContentTag)
		val htmlType = typeof(Html)

		newArrayList(
			createElement("head", compositeType, htmlType)
			// bootstrap below with 1 line above
			,createElement("title", compositeType, typeof(Head))
			,createElement("body", contentType, htmlType)
			,createElement("b", contentType, contentType)
			,createElement("p", contentType, contentType)
			,createElement("h1", contentType, contentType)
			,createElement("a", contentType, contentType).addAttributeNames("href")
		)
	}


	def private generateDomElements(Iterable<DomElementLiteral> elements)
		'''
		package «packageName»

		«FOR it : elements»
			@Data class «elementName.toFirstUpper» extends «superType.simpleName» {}
		«ENDFOR»
		'''

	def private generateDomBuilder(Iterable<DomElementLiteral> elements)
		'''
		package «packageName»

		class DomBuilder extends BaseDomBuilder {

		«FOR e : elements»
			«e.generateXtension»
		«ENDFOR»

		}
		'''

	def private generateXtension(DomElementLiteral it) {
		val ElementName = elementName.toFirstUpper
		'''
		def «elementName»(«containerElementType.simpleName» parent, «FOR attributeName : attributeNames»String «attributeName», «ENDFOR»(«ElementName»)=>void subBuilder) {
			val child = new «ElementName»().subBuildWith(subBuilder)
			«FOR attributeName : attributeNames»
				child.attributes.put("«attributeName»", «attributeName»)
			«ENDFOR»
			child.appendTo(parent)
		}
		'''
	}

}
