package nl.dslmeinte.xtext.examples.validation

import com.google.inject.Inject
import nl.dslmeinte.xtext.examples.DataModelExtensions
import nl.dslmeinte.xtext.examples.dataModelDsl.DataModelDslPackage
import nl.dslmeinte.xtext.examples.dataModelDsl.Field
import nl.dslmeinte.xtext.examples.dataModelDsl.Modifier
import org.eclipse.xtext.validation.Check

class DataModelDslXtendValidator extends AbstractDataModelDslJavaValidator {

	@Inject
	extension DataModelExtensions

	extension DataModelDslPackage ePackage = DataModelDslPackage::eINSTANCE

	@Check
	def void check_dataType_only_has_simple_fields(Field field) {
		if( field.isSimple && field.modifier == Modifier::REPEATED ) {
			error( "a primitive-typed field can't be 'repeated'", field_Modifier );
		}
	}

}
