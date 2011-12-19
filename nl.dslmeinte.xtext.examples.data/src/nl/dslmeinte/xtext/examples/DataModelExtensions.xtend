package nl.dslmeinte.xtext.examples

import java.util.Map
import java.util.Set
import nl.dslmeinte.xtext.examples.dataModelDsl.DataModel
import nl.dslmeinte.xtext.examples.dataModelDsl.Entity
import nl.dslmeinte.xtext.examples.dataModelDsl.Field
import nl.dslmeinte.xtext.examples.dataModelDsl.MetaPrimitiveType
import nl.dslmeinte.xtext.examples.dataModelDsl.PrimitiveType

class DataModelExtensions {

	/**
	 * Determines whether a {@link Field} can be considered to be "simple" (as
	 * opposed to "complex" -{@link DataType}s may only contain "simple"
	 * fields).
	 */
	def isSimple(Field it) {
		type instanceof PrimitiveType
	}

	/**
	 * Returns all {@link Entity}s in the model.
	 */
	def entities(DataModel it) {
		compoundTypes.filter(typeof(Entity))
	}

	/**
	 * Returns all fields of the entity given, directly including all inherited
	 * fields.
	 */
	def allFields(Entity it) {
		val result = fields
		if( superType != null ) {
			result.addAll(superType.allFields)
		}
//		if( result.size > 0 ) {
//			println(name + " ::= " + result.map[name].join(", "))
//		}
		result.unmodifiableView
	}

	def MetaPrimitiveType realizationType(PrimitiveType it) {
		if( superType == null ) realizationType else superType.realizationType
	}

	/**
	 * Returns all (proper) sub entities of the given {@link Entity}, recursively.
	 */
	def Set<Entity> allSubEntities(Entity superEntity) {
		// TODO  rewrite this into proper Xtend2-style
		val Map<Entity, Set<Entity>> subEntitiesMap = newHashMap()

		for( Entity entity : (superEntity.eContainer as DataModel).entities ) {
			var Entity superType = entity.superType
			while( superType != null ) {
				var Set<Entity> set = subEntitiesMap.get(superType)
				if( set == null ) {
					set = newHashSet()
					subEntitiesMap.put(superType, set)
				}
				set.add(entity)
				superType = superType.superType
			}
		}

		val result = subEntitiesMap.get(superEntity)
//		if( result.size > 0 ) {
//			println( superEntity.name + " -> [ " + subEntitiesMap.get(superEntity).map[name].join(", ") + " ]" )
//		}
		result.unmodifiableView
	}

}