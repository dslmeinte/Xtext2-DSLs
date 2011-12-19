package nl.dslmeinte.xtext.examples.scoping

import com.google.inject.Inject
import java.util.ArrayList
import nl.dslmeinte.xtext.examples.DataModelExtensions
import nl.dslmeinte.xtext.examples.DynamicScreenExtensions
import nl.dslmeinte.xtext.examples.dataModelDsl.DataType
import nl.dslmeinte.xtext.examples.dataModelDsl.Entity
import nl.dslmeinte.xtext.examples.dataModelDsl.Enumeration
import nl.dslmeinte.xtext.examples.dataModelDsl.Type
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.AtomicReferringBExpression
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DynamicScreen
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumList
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.ExternalVariable
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.Field
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.FieldGroup
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.PathTail
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

import static org.eclipse.xtext.scoping.Scopes.*

class DynamicScreenDslScopeProvider extends AbstractDeclarativeScopeProvider {

	@Inject
	extension DataModelExtensions

	@Inject
	extension DynamicScreenExtensions

	/*
	 * +------------------------+
	 * | aspect: Infrastructure |
	 * +------------------------+
	 */

	def scope_PathTail_field(PathTail context, EReference reference) {
		val previous = context.eContainer

		val type = switch(previous) {
			PathTail	: {
							var previousType = previous.field.type
							if( previousType instanceof Entity && previous.asEntity != null ) previous.asEntity else previousType
						  }
			ExternalVariable	: (previous.eContainer as DynamicScreen).viewClass
			Field				: (previous.eContainer as FieldGroup).typeOfRelativePath
			FieldGroup			: previous.typeOfRelativePath(true)	// ~ determine scope for FieldGroup.with
		}

		scopeFor(
			switch(type) {
				DataType	: type.fields
				Entity		: type.allFields
				default		: emptyList
			}
		)
	}

	/**
	 * Determines the relative base type for this {@link FieldGroup}.
	 * <p>
	 * Example:
	 * <pre>
	 * dynamic-screen "Example" view-class = SomeEntity
	 *
	 * group "Group1" with /feature1 {
	 *
	 *   group "Group1.1" with /nestedFeature1 {
	 *   }
	 * }
	 * </pre>
	 * The effective path of "Group1.1" is
	 * {@code SomeEntity/feature1/nestedFeature1}.
	 * Its type is the type of the referred {@code nestedFeature1} element.
	 *
	 * @param group
	 * 			  The field group for which to compute the relative base type.
	 * 
	 * @param ignoreWith
	 *            Determines whether the <em>with</em> attribute of the group
	 *            will be ignored in determining the relative path.
	 *            This is necessary for the case where we're trying to determine
	 *            the scope of that very same <em>with</em> attribute: the value
	 *            of it is then non-null already, but since its scope is not yet
	 *            determined, {@code group.with.lastElement.field} is an
	 *            as-yet unresolved proxy. Resolving the proxy uses determining
	 *            the scope again, leading to an infinite loop...
	 */
	def private typeOfRelativePath(FieldGroup group, boolean ignoreWith) {
		if( group.with != null && !ignoreWith ) {
			return group.with.lastElement.field.type
		}

		val previous = group.eContainer
			// (a FieldGroup is contained either by another FieldGroup or DynamicScreen)
		switch(previous) {
			FieldGroup		: previous.typeOfRelativePath
			DynamicScreen	: previous.viewClass
			default			: throw new IllegalArgumentException(
									"can't handle a field group contained by "
									+ previous.^class.simpleName
								)
		}
	}

	def private typeOfRelativePath(FieldGroup group) {
		typeOfRelativePath(group, false)
	}

	/**
	 * Provides the scope for the down cast inside a path element, relying on
	 * that element being non-{@literal null} otherwise an {@link Exception}
	 * which is caught and ignored by Xtext's core engine.
	 */
	def scope_PathTail_asEntity(PathTail context, EReference reference) {
		val type = context.field.type
		scopeFor(
			switch(type) {
				Entity	: type.allSubEntities
				default	: emptyList
			}
		)
	}


	/*
	 * +----------------------+
	 * | aspect: Declarations |
	 * +----------------------+
	 */

	def scope_EnumList_literals(EnumList context, EReference reference) {
		scopeFor(context.enum.literals)
	}


	/*
	 * +--------------------+
	 * | aspect: References |
	 * +--------------------+
	 */

	def scope_Reference_ref(DynamicScreen it, EReference reference) {
		val targets = new ArrayList<EObject>()

		targets.addAll(externalVars)
		targets.addAll(allFields.toList)

		scopeFor(targets)
	}


	/*
	 * +--------------------------+
	 * | aspect: Field attributes |
	 * +--------------------------+
	 */

	def scope_DirectEnumLiteralsCollection_literals(Field field, EReference reference) {
		field.path.resultType.createEnumLiteralsScopeFor
	}

	def IScope scope_EnumListReference_ref(Field field, EReference reference) {
		field.path.resultType.createEnumListsScopeFor(field)
	}


	/*
	 * +-----------------------------+
	 * | aspect: Boolean expressions |
	 * +-----------------------------+
	 */

	def scope_SingleEnumComparison_eLiteral(AtomicReferringBExpression context, EReference reference) {
		createEnumLiteralsScopeFor(context.ref.ref.path.resultType)
	}

	def scope_DirectEnumLiteralsCollection_literals(AtomicReferringBExpression context, EReference reference) {
		createEnumLiteralsScopeFor(context.ref.ref.path.resultType)
	}

	def scope_EnumListReference_ref(AtomicReferringBExpression context, EReference reference) {
		createEnumListsScopeFor(context.ref.ref.path.resultType, context)
	}


	/*
	 * +--------------+
	 * | Helper stuff |
	 * +--------------+
	 */

	// enum-related:

	def private dispatch createEnumLiteralsScopeFor(Enumeration enumeration) {
		scopeFor(enumeration.literals)
	}

	def private dispatch createEnumLiteralsScopeFor(Type type) {
		IScope::NULLSCOPE
	}

	def private dispatch createEnumListsScopeFor(Enumeration enumeration, EObject context) {
		scopeFor( context.root.enumLists.filter[ el | el.enum == enumeration ] )
	}

	def private dispatch createEnumListsScopeFor(Type type, EObject context) {
		IScope::NULLSCOPE
	}

}