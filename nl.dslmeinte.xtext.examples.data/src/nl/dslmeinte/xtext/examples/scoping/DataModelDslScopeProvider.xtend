package nl.dslmeinte.xtext.examples.scoping

import com.google.inject.Inject
import nl.dslmeinte.xtext.examples.DataModelExtensions
import nl.dslmeinte.xtext.examples.dataModelDsl.DataModel
import nl.dslmeinte.xtext.examples.dataModelDsl.DataType
import nl.dslmeinte.xtext.examples.dataModelDsl.Entity
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

import static org.eclipse.xtext.scoping.Scopes.*

class DataModelDslScopeProvider extends AbstractDeclarativeScopeProvider {

	@Inject
	extension DataModelExtensions

	def scope_Field_type(DataType dataType, EReference eRef) {
		val dataModel = dataType.eContainer as DataModel
		scopeFor( dataModel.primitives.primitiveTypes )
	}

	def scope_Usage_field(Entity entity, EReference eRef) {
		scopeFor( entity.allFields )
	}

	def scope_Usage_field(DataType dataType, EReference eRef) {
		scopeFor( dataType.fields )
	}

}