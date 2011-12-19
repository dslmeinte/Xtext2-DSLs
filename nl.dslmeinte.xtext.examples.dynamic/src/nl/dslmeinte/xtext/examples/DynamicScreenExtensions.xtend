package nl.dslmeinte.xtext.examples

import java.util.ArrayList
import nl.dslmeinte.xtext.examples.dataModelDsl.Enumeration
import nl.dslmeinte.xtext.examples.dataModelDsl.MetaPrimitiveType
import nl.dslmeinte.xtext.examples.dataModelDsl.PrimitiveType
import nl.dslmeinte.xtext.examples.dataModelDsl.Type
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DirectEnumLiteralsCollection
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.DynamicScreen
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumComparison
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumList
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumListReference
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.EnumLiteralsCollection
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.ExternalVariable
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.Field
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.GroupElement
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.PathTail
import nl.dslmeinte.xtext.examples.dynamicScreenDsl.ReferenceTarget
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil

class DynamicScreenExtensions {

	/**
	 * Returns all fields defined within all blocks within the {@link DynamicScreen}.
	 */
	def allFields(DynamicScreen it) {
		eAllContents.filter(typeof(Field)).toIterable
	}

	/**
	 * Returns all group elements defined within all blocks within the {@link DynamicScreen}.
	 */
	def allGroupElements(DynamicScreen it) {
		eAllContents.filter(typeof(GroupElement)).toIterable
	}

	/**
	 * Returns the type of the field referred to by the leaf element of this
	 * path.
	 */
	def Type resultType(PathTail element) {
		return element.lastElement.field.type
	}

	/**
	 * Returns the leaf element of this path.
	 */
	def PathTail lastElement(PathTail it) {
		var element = it
		while( element.tail != null ) {
			element = element.tail
		}
		element
	}

	/**
	 * Returns the root model element for the current .dsd file.
	 */
	def root(EObject eObject) {
		EcoreUtil::getRootContainer(eObject) as DynamicScreen
	}

	/**
	 * Returns all aliases defined within this {@link DynamicScreen}.
	 */
	def externalVars(DynamicScreen it) {
		declarations.filter(typeof(ExternalVariable))
	}

	/**
	 * Returns all enumLists defined within this {@link DynamicScreen}.
	 */
	def enumLists(DynamicScreen it) {
		declarations.filter(typeof(EnumList))
	}


	// (from DynamicScreenExtensions.ext:)

	def isBooleanTyped(ReferenceTarget it) {
		val type = path.resultType
		switch(type) {
			PrimitiveType : type.boolean
			default : false
		}
	}

	def isBoolean(PrimitiveType it) {
		   realizationType == MetaPrimitiveType::BOOLEAN
		|| name.toLowerCase == "boolean"
	}

	def dispatch resolve(EnumLiteralsCollection it) {
		{}
	}

	def dispatch resolve(DirectEnumLiteralsCollection it) {
		literals
	}

	def dispatch resolve(EnumListReference it) {
		ref.literals
	}

	/*
	 * Cannot use 'enum' since that causes invalid generated Java code.
	 * "def foo(EnumComparison it) { enum }" is still valid, though,
	 * and targets this function.
	 */
	def getEnum(EnumComparison it) {
		ref.ref.path.resultType as Enumeration
	}

	def allObservables(DynamicScreen it) {
		val result = new ArrayList<ReferenceTarget>()
		result.addAll(externalVars)
		result.addAll(allFields.toList.sortBy[name])
		result
	}

}