package nl.dslmeinte.xtext.ambiguity

import com.google.inject.Inject
import nl.dslmeinte.xtext.ambiguity.myPL.Assignment
import nl.dslmeinte.xtext.ambiguity.myPL.Class
import nl.dslmeinte.xtext.ambiguity.myPL.Expression
import nl.dslmeinte.xtext.ambiguity.myPL.Feature
import nl.dslmeinte.xtext.ambiguity.myPL.FirstHeadType
import nl.dslmeinte.xtext.ambiguity.myPL.Head
import nl.dslmeinte.xtext.ambiguity.myPL.IntegerLiteral
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
		val intField = someInnerClass.features.head
		// TODO  assertions on that

		val statement1 = model.elements.get(1)
		switch statement1 {
			VariableDeclaration: {
				assertEquals("localVar", statement1.variable.varName)
				statement1.value.assertIntegerLiteral(37)
				statement1.typeRef.assertHead(someClass, someInnerClass)
			}
			default: fail("not a " + typeof(VariableDeclaration).simpleName)
		}

		switch statement2: model.elements.get(2) {
			Assignment: {
				statement2.value.assertIntegerLiteral(42)
				statement2.lhs.assertHead((statement1 as VariableDeclaration).variable, intField)
			}
			default: fail("not an " + typeof(Assignment).simpleName)
		}
	}

	private def void assertIntegerLiteral(Expression it, int value) {
		switch it {
			IntegerLiteral:	assertEquals(value, int)
			default:		fail("not an " + typeof(IntegerLiteral).simpleName)
		}
	}

	private def void assertHead(Object it, FirstHeadType firstReference, Feature...featureRefs) {
		switch it {
			Head: {
//				assertSame(firstReference, first)
				// FIXME  resolution hasn't happened yet
			}
			default: fail("not a " + typeof(Head).simpleName)
		}
	}

}
