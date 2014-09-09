package nl.dslmeinte.xtext.ambiguity

import com.google.inject.Inject
import nl.dslmeinte.xtext.ambiguity.myPL.Assignment
import nl.dslmeinte.xtext.ambiguity.myPL.Attribute
import nl.dslmeinte.xtext.ambiguity.myPL.Class
import nl.dslmeinte.xtext.ambiguity.myPL.Expression
import nl.dslmeinte.xtext.ambiguity.myPL.Feature
import nl.dslmeinte.xtext.ambiguity.myPL.FirstHeadType
import nl.dslmeinte.xtext.ambiguity.myPL.Head
import nl.dslmeinte.xtext.ambiguity.myPL.IntegerLiteral
import nl.dslmeinte.xtext.ambiguity.myPL.MyPLPackage
import nl.dslmeinte.xtext.ambiguity.myPL.Program
import nl.dslmeinte.xtext.ambiguity.myPL.VariableDeclaration
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@InjectWith(MyPLInjectorProvider)
@RunWith(XtextRunner)
class MyPLTest {

	@Inject ParseHelper<Program> parser

	val ePackage = MyPLPackage.eINSTANCE

	@Test
	def void parse_example() {
		val model = parser.parse('''
			class SomeClass {
				class SomeInnerClass {
					intField : integer
				}
			}

			SomeClass.SomeInnerClass localVar := 37  // ID-Keyword('.')-ID-[WS]-ID-[WS]-Keyword(':=')-[WS]-INT(42)
			localVar.intField := 42                  // ID-Keyword('.')-ID-[WS]-Keyword(':=')-INT(42)
		''')

		EcoreUtil2.resolveAll(model.eResource)

		val someClass = model.elements.get(0) as Class
		val someInnerClass = someClass.features.head as Class
		val intField = someInnerClass.features.head.assertType(typeof(Attribute)) [ assertEquals("integer", type) ]

		val statement1 = model.elements.get(1).assertType(typeof(VariableDeclaration)) [
			assertEquals("localVar", variable.varName)
			value.assertIntegerLiteral(37)
			typeRef.assertHead(someClass, someInnerClass)
		]

		model.elements.get(2).assertType(typeof(Assignment)) [
			value.assertIntegerLiteral(42)
			lhs.assertHead((statement1 as VariableDeclaration).variable, intField)
		]
	}

	private def void assertIntegerLiteral(Expression it, int value) {
		assertType(typeof(IntegerLiteral)) [ assertEquals(value, int) ]
	}

	private def void assertHead(Object it, FirstHeadType firstReference, Feature...featureRefs) {
		assertType(typeof(Head)) [
			val first_ = eGet(ePackage.head_First)
			assertSame(firstReference, first_)
		]
	}

	private def <T> T assertType(Object object, java.lang.Class<T> clazz, (T)=>void assertion) {
		if( clazz.isAssignableFrom(object.^class) ) {
			val t = clazz.cast(object)
			assertion.apply(t)
			t
		} else {
			fail("not a " + clazz.simpleName)
			null
		}
	}

}
