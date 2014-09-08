package nl.dslmeinte.multilevel.validation

import java.util.Map
import nl.dslmeinte.multilevel.exampleDsl.Attribute
import nl.dslmeinte.multilevel.exampleDsl.DataType
import nl.dslmeinte.multilevel.exampleDsl.EntityInstance
import nl.dslmeinte.multilevel.exampleDsl.ExampleDslPackage
import nl.dslmeinte.multilevel.exampleDsl.Feature
import nl.dslmeinte.multilevel.exampleDsl.FeatureValue
import nl.dslmeinte.multilevel.exampleDsl.IntegerLiteral
import nl.dslmeinte.multilevel.exampleDsl.Reference
import nl.dslmeinte.multilevel.exampleDsl.StringLiteral
import org.eclipse.xtext.validation.Check

class ExampleDslValidator extends AbstractExampleDslValidator {

	val ePackage = ExampleDslPackage.eINSTANCE

	@Check
	def check_features_do_not_receive_no_or_multiple_values(EntityInstance instance) {
		val Map<Feature, Integer> feature2numberOfValues = newHashMap()
		instance.values.map[feature].forEach[ f |
			var count = feature2numberOfValues.get(f) ?: 0
			count++
			feature2numberOfValues.put(f, count)
		]
		instance.entity.features.forEach[ f |
			val count = feature2numberOfValues.get(f) ?: 0
			if( count == 0 ) {
				warning('''no value given for feature «f.name»''', ePackage.entityInstance_Values)
			}
			if( count > 1 ) {
				error('''multiple values given for feature «f.name»''', ePackage.entityInstance_Values)
			}
		]
		// Note: because of scoping, only features of the indicated entity can be referenced
	}

	@Check
	def check_feature_values_have_the_correct_type(FeatureValue it) {
		switch( value ) {
			StringLiteral: {
				switch( feature ) {
					Attribute:	if( (feature as Attribute).dataType !== DataType.STRING ) {
						error('''attribute value must be of type «(feature as Attribute).dataType.literal», not a string''', ePackage.featureValue_Value)
					}
					Reference:	error('''feature value must an entity «(feature as Reference).entity.name», not a string''', ePackage.featureValue_Value)
				}
			}
			IntegerLiteral:	{
				switch( feature ) {
					Attribute:	if( (feature as Attribute).dataType !== DataType.INTEGER ) {
						error('''attribute value must be of type «(feature as Attribute).dataType.literal», not an integer''', ePackage.featureValue_Value)
					}
					Reference:	error('''feature value must be an entity «(feature as Reference).entity.name», not an integer''', ePackage.featureValue_Value)
				}
			}
			EntityInstance: {
				switch( feature ) {
					Attribute:	error('''feature value must be an entity «(value as EntityInstance).entity.name», not of data type «(feature as Attribute).dataType.name()»''', ePackage.featureValue_Value)
					Reference:	if( (value as EntityInstance).entity !== (feature as Reference).entity ) {
						error('''feature value must be an entity «(feature as Reference).entity.name», not an entity of type «(value as EntityInstance).entity.name»''', ePackage.featureValue_Value)
					}
				}
			}
		}
	}

}
