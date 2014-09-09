package nl.dslmeinte.xtext.ambiguity.scoping

import nl.dslmeinte.xtext.ambiguity.myPL.Attribute
import nl.dslmeinte.xtext.ambiguity.myPL.Class
import nl.dslmeinte.xtext.ambiguity.myPL.Feature
import nl.dslmeinte.xtext.ambiguity.myPL.FeatureRefTail
import nl.dslmeinte.xtext.ambiguity.myPL.Head
import nl.dslmeinte.xtext.ambiguity.myPL.HeadTarget
import nl.dslmeinte.xtext.ambiguity.myPL.SpecElement
import nl.dslmeinte.xtext.ambiguity.myPL.Variable
import nl.dslmeinte.xtext.ambiguity.myPL.VariableDeclaration
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

class MyPLScopeProvider extends AbstractDeclarativeScopeProvider {

//	override IScope getScope(EObject it, EReference eRef) {
//		println('''scope_«eRef.EContainingClass.name»_«eRef.name»(«eClass.name» it, EReference eRef)''')
//		super.getScope(it, eRef)
//	}

	def IScope scope_FeatureRefTail_feature(FeatureRefTail it, EReference eRef) {
		Scopes.scopeFor((eContainer as SpecElement).directFeatures)
	}

	private def SpecElement lastElement(Head it) {
		var SpecElement elt = it
		while( elt.tail !== null ) {
			elt = elt.tail
		}
		elt
	}

	/**
	 * Can only be called when the given {@link Head} is fully resolved!
	 */
	private def Iterable<Feature> features(Head it) {
		switch last: lastElement {
			Head:			last.target.directFeatures
			FeatureRefTail:	last.feature.directFeatures
		}
	}


	// "direct" meaning: without considering any tail/just looking at the information directly at hand

	private def Iterable<Feature> directFeatures(Feature it) {
		switch it {
			Attribute:	#[]
			Class:		features
		}
	}

	private def Iterable<Feature> directFeatures(HeadTarget it) {
		switch it {
			Class:		features
			Variable:	(eContainer as VariableDeclaration).typeRef.features
		}
	}

	private def Iterable<Feature> directFeatures(SpecElement it) {
		switch it {
			Head:			target.directFeatures
			FeatureRefTail: feature.directFeatures
		}
	}

}
