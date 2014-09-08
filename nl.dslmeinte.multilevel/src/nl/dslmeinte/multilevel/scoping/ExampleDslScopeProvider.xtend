package nl.dslmeinte.multilevel.scoping

import nl.dslmeinte.multilevel.exampleDsl.EntityInstance
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

class ExampleDslScopeProvider extends AbstractDeclarativeScopeProvider {

	def scope_FeatureValue_feature(EntityInstance context, EReference eRef) {
		Scopes.scopeFor(context.entity?.features)
	}

}
