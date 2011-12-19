package nl.dslmeinte.xtext.examples.validation

import com.google.inject.Inject
import java.util.EnumSet
import java.util.Set
import nl.dslmeinte.xtext.examples.DynamicScreenExtensions
import nl.dslmeinte.xtext.examples.dataModelDsl.Entity
import nl.dslmeinte.xtext.examples.dataModelDsl.Enumeration
import nl.dslmeinte.xtext.examples.dataModelDsl.Modifier
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.AttributeClass
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.AttributeClassExpression
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DirectEnumLiteralsCollection
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DynamicScreen
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DynamicScreenDslPackage
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.Field
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.FieldGroup
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.PathTail
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.Restriction
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.RestrictionGroupAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.validation.Check


class DynamicScreenDslXtendValidator extends AbstractDynamicScreenDslJavaValidator {

	@Inject
	extension DynamicScreenExtensions

	extension DynamicScreenDslPackage ePackage = DynamicScreenDslPackage::eINSTANCE

	@Check
	def void pathResultType_is_primitive_or_data_type(PathTail it) {
		if( tail != null ) {
			return
		}

		var EObject head = it
		while( head instanceof PathTail ) {
			head = head.eContainer
		}
		if( !(head instanceof Field) ) {
			return
		}

		val type = field.type
		if( type instanceof Entity ) {
			error( "type of last element of a data path must be a DataType, a PrimitiveType or Enumeration, not an Entity as " + (type as Entity).name + " is", pathTail_Field )
		}
	}

	@Check
	def void unique_fieldNames(Field it) {
		for( otherField : root.allFields ) {
			if( otherField != it && name == otherField.name ) {
				error( "duplicate field name '" + name + "'", dummyForExternalVariable_Name)
			}
		}
	}

	@Check
	def void unique_attributeClasses(AttributeClassExpression it) {
		val Set<AttributeClass> set = EnumSet::noneOf(typeof(AttributeClass))

		var unique = true
		for( attributeClass : classes ) {
			if( !set.add(attributeClass) ) {
				unique = false	// (could break now...)
			}
		}

		if( !unique ) {
			error( "left hand side expression contains duplicate attribute classes", attributeClassExpression_Classes )
		}
	}

	@Check
	def void topLevel_fieldGroup_must_have_label(FieldGroup it) {
		if(    ( eContainer instanceof DynamicScreen )
			&& ( label == null ) ) {
			error( "top-level field group must have a label", groupElement_Label )
		}
	}

	@Check
	def void restriction_must_apply_to_enum_field(Restriction it) {
		if( !(eContainer instanceof Field) ) {
			error( "an enumeration restriction attribute can only be used with a field", groupElement_Attributes )
			return
		}

		val type = (eContainer as Field).path.resultType
		if( !( type instanceof Enumeration ) ) {
			error( "an enumeration restriction attribute can only be used with a field which has an Enumeration as type", groupElement_Attributes )
		}
	}

	@Check
	def void warn_for_empty_list_of_Enumeration_literals(DirectEnumLiteralsCollection it) {
		if( literals.size == 0 ) {
			warning( "enumeration literals list is empty", directEnumLiteralsCollection_Literals )
		}
	}

	/**
	 * This is already checked since the grammar is defined here with a +, but
	 * this message is a lot clearer.
	 */
	@Check
	def void restrictionGroupAttribute_must_have_at_least_one_rule(RestrictionGroupAttribute it) {
		if( rules.size == 0 ) {
			error( "enumeration restriction group attribute must have at least 1 rule", groupElement_Attributes )
		}
	}

	@Check
	def void warn_for_multiValued_properties_inside_paths(PathTail element) {
		if( element.field.modifier == Modifier::REPEATED ) {
			warning( "field is multi-valued ==> the path might need extra selection logic (currently not implemented)", pathTail_Field )
		}
	}

	@Check
	def void warn_for_empty_field_groups(FieldGroup it) {
		if( elements.size == 0 ) {
			warning( "field group is empty", fieldGroup_Elements )
		}
	}

	// TODO  add constraints on type of ExternalVariable.path?

}
