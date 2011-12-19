package nl.dslmeinte.xtext.examples.generator

import com.google.inject.Inject
import java.util.List
import nl.dslmeinte.xtext.examples.DynamicScreenExtensions
import nl.dslmeinte.xtext.examples.dataModelDsl.DataModelDslPackage
import nl.dslmeinte.xtext.examples.dataModelDsl.EnumLiteral
import nl.dslmeinte.xtext.examples.dataModelDsl.Enumeration
import nl.dslmeinte.xtext.examples.dataModelDsl.MetaPrimitiveType
import nl.dslmeinte.xtext.examples.dataModelDsl.PrimitiveType
import nl.dslmeinte.xtext.examples.dataModelDsl.Type
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.AndOperation
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.AtomicReferringBExpression
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.Attribute
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.AttributeClass
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.BComparison
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.BExpression
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.BLiteral
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DirectEnumLiteralsCollection
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DynamicScreen
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DynamicScreenDslPackage
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumList
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumListReference
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumLiteralsCollection
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumMembership
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.ExternalVariable
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.Field
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.FieldGroup
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.GroupElement
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.Label
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.MultiEnumComparison
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.NegationExpression
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.NormalAttribute
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.OrOperation
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.PresenceCheck
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.ReferenceTarget
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.SingleEnumComparison
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.StringValueComparison
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

class DynamicScreenDslGenerator implements IGenerator {

	@Inject
	extension DynamicScreenExtensions

	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		val dynamicScreen = resource.contents.head as DynamicScreen
		val path = resource.URI.trimSegments(1).toPlatformString(true) + "/" + dynamicScreen.label.en + ".html"
		fsa.generateFile(path, dynamicScreen.htmlFile)
	}

	def private htmlFile(DynamicScreen it) {
		'''
		<html>
		  <head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<link rel="stylesheet" type="text/css" media="all" href="http://localhost:8888/static/style.css">
			<script src="http://localhost:8888/static/prototype.js" type="text/javascript"></script>
			<script src="http://localhost:8888/static/inputUtils.js" type="text/javascript"></script>
			<script type="text/javascript">
			  «javascript»
			</script>
			<title>«label.display»</title>
		  </head>
		  <body>
		
			<div class="header">Dynamic mockup for «label.display» screen</div>
		
			<div class="screen">
			  «body»
			</div>
		
			«eventJS»
		
		  </body>
		</html>
		
		'''
	}

	def private display(Label it) {
		'''«en»«IF nl != null» (nl: «nl»)«ENDIF»'''
	}

	def private dispatch displayName(Field it) { label.en }
	def private dispatch displayName(ExternalVariable it) { name }

	def private javascript(DynamicScreen it) {
		'''
		«FOR enumList : enumLists»
			«enumList.javascript»
		«ENDFOR»
		/**
		 * Updates the entire screen, based on all the logic defined in the DSL instance, whenever _any_ field changes.
		 * (Implementation is simpler that way. Splitting update logic requires determining which values are used in
		 *  any Boolean expression.)
		 */
		function updateScreen()
		{
			«FOR groupElt : allGroupElements»
				«groupElt.attributes.map[it.attribute(groupElt)].join("\n")»
			«ENDFOR»
		}
		'''
	}

	def private javascript(EnumList it) {
		'''
		var «name» = «enum.jsIndices(enum.literals)»;
		'''
	}

	def private jsIndices(Enumeration enumeration, List<EnumLiteral> myLiterals) {
		'''[ «myLiterals.map[ literal | enumeration.literals.indexOf(literal) ].join(", ")» ]'''
	}

	def private dispatch attribute(Attribute it, GroupElement elt) {
		''' 
		// handling of Attribute of (sub) type «eClass.name» not implemented yet
		'''
	}

	def private dispatch attribute(NormalAttribute it, GroupElement elt) {
		if( classExpr.classes.contains(AttributeClass::DISPLAY) ) {
			'''InputUtils.display( "«elt.id»", «clause.bValue» );'''
		} else {
			'''
			// WARNING: handling of AttributeClass-es other than 'display' not implemented yet"
			'''
		}
	}

	def private dispatch 		id(ExternalVariable it) { "_external_" + name }
	def private dispatch 		id(Field it) { name }
	def private dispatch String id(AtomicReferringBExpression it) { ref.ref.id }
	def private dispatch 		id(FieldGroup it) { eResource.getURIFragment(it) }

	def private dispatch bValue(BExpression it)				{ '''alert( "sub type «eClass.name» of BExpression not handled" ), true''' }
	def private dispatch bValue(OrOperation it)				{ '''(«left.bValue») || («right.bValue»)''' }
	def private dispatch bValue(AndOperation it)			{ '''(«left.bValue») && («right.bValue»)''' }
	def private dispatch bValue(NegationExpression it)		{ '''!(«expr.bValue»)''' }
	def private dispatch bValue(BComparison it)				{ '''«IF BLiteral == BLiteral::FALSE»!«ENDIF»InputUtils.bGet( "«id»" )''' }
	def private dispatch bValue(SingleEnumComparison it)	{ '''InputUtils.eGet( "«id»" ) == "«enum.literals.indexOf(ELiteral)»"''' }
	def private dispatch bValue(StringValueComparison it)	{ '''InputUtils.rGet( "«id»" ) === "«targetValue»"''' }
	def private dispatch bValue(PresenceCheck it)			{ '''InputUtils.rGet( "«id» ") != null''' }

	def private dispatch bValue(MultiEnumComparison it) 	{
		'''
		«IF membership == EnumMembership::IS_NOT_IN»!«ENDIF»InputUtils.contains(«ELiterals.multiEnumComparison(it)», InputUtils.eGet("«id»"))
		'''
	}

	def private dispatch multiEnumComparison(EnumLiteralsCollection it, MultiEnumComparison expr) {
		'''alert( "sub type «eClass.name» of EnumLiteralsCollection not handled in multiEnumComparison" ), true'''
	}

	def private dispatch multiEnumComparison(EnumListReference it, MultiEnumComparison expr) {
		'''«ref.name»'''
	}

	def private dispatch multiEnumComparison(DirectEnumLiteralsCollection it, MultiEnumComparison expr) {
		'''«expr.enum.jsIndices(literals)»'''
	}

	def unhandledSubType(EObject it, EClass superType, String blockName) {
		'''
		<div class="error">
		unhandled sub type «eClass.name» of «superType.name» in DEFINE-block «blockName»
		(meta-programmer error)
		</div>
		'''
	}

	def private body(DynamicScreen it) {
		'''
		<div class="onecolumn">
			<div class="title">External variables</div>
			«FOR it : externalVars»
				«inputBlock»
			«ENDFOR»
		</div>
		«FOR it : groups»
			«topLevelFieldGroup»
		«ENDFOR»
		'''
	}

	def private eventJS(DynamicScreen it) {
		'''
		<script type="text/javascript">
			«FOR it : allObservables»
				«IF isBooleanTyped»
					InputUtils.listenToRadio("«id»_", updateScreen);
				«ELSE»
					Event.observe($("«id»").childElements()[2].childElements()[0], "onchange", updateScreen);
				«ENDIF»
			«ENDFOR»
			Event.observe(window, "load", updateScreen);
			</script>
		'''
	}

	def private topLevelFieldGroup(FieldGroup it) {
		'''
		<div id="«id»" class="onecolumn">

			<div class="title">«label.display»</div>

			«FOR it : elements»
				«groupElement»
			«ENDFOR»

		</div>
		'''
	}

	def private dispatch groupElement(GroupElement it) {
		unhandledSubType(DynamicScreenDslPackage::eINSTANCE.groupElement, "groupElement")
	}

	def private dispatch groupElement(Field it) {
		inputBlock
	}

	def private dispatch groupElement(FieldGroup it) {
		'''
		<div id="«id»">
			«FOR it : elements»
				«groupElement»
			«ENDFOR»
		</div>
		'''
	}

	def private dispatch inputField(Type it, ReferenceTarget target) {
		unhandledSubType(DataModelDslPackage::eINSTANCE.type, "inputField")
	}

	def private dispatch inputField(Enumeration it, ReferenceTarget target) {
		'''
		<select class="inputSelect" onchange="updateScreen();">
			<option selected="selected" value="">(none)</option>
			«literals.options»
		</select>
		'''
	}

	def private dispatch inputField(PrimitiveType it, ReferenceTarget target) {
		it.inputElement(target.id, false)
	}

	def private inputBlock(ReferenceTarget it) {
		'''
		<div id="«id»" class="field">
			<div class="label">«displayName()»</div>
			<div class="required"></div>
			<div class="value">
			«path.resultType.inputField(it)»
			</div>
		</div>
		'''
	}

	def private inputElement(PrimitiveType it, String id, boolean withId) {
		val numericTypes = newArrayList(MetaPrimitiveType::INT, MetaPrimitiveType::NUMBER)
		if( realizationType == MetaPrimitiveType::BOOLEAN ) {
			'''
			<input name="«id»" id="«id»_1" style="display: none;" value="" checked="checked" type="radio" />
			<input name="«id»" id="«id»_2" class="inputRadio" value="true" type="radio">Yes&nbsp;&nbsp;</input>
			<input name="«id»" id="«id»_3" class="inputRadio" value="false" type="radio">No</input>
			'''
		} else {
			'''
			<input name="«id»" «IF withId»id="«id»"«ENDIF»
			«IF numericTypes.contains(realizationType)»
				type="text" class="inputNumber" />
			«ELSEIF realizationType == MetaPrimitiveType::STRING »
				type="text" maxlength="255" class="inputText" />
			«ELSE»
				<div class="error">no inputField definition for &gt;PrimitiveType&lt;«name» («realizationType»)</div>
			«ENDIF»
			'''
		}
	}

	def private options(List<EnumLiteral> literals) {
		'''
		«FOR it : literals.sortBy[name]»
			<option value="«literals.indexOf(it)»">«name»</option>
		«ENDFOR»
		'''
	}

}
