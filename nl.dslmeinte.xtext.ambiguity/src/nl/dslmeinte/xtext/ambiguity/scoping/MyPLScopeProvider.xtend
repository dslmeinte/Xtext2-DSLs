package nl.dslmeinte.xtext.ambiguity.scoping

import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider

class MyPLScopeProvider extends AbstractDeclarativeScopeProvider {

//	def IScope scope_FeatureRefTail_feature(FeatureRefTail it, EReference eRef) {
//		scopeFor(switch parent: eContainer {
//			Head: {
//				switch first: parent.first {
//					Class:		first.features
////					Variable:	(first.eContainer as VariableDeclaration).typeRef.tailElement
//				}
//			}
//		})
//	}
//
//	def private tailElement(Head it) {
//		tail.tailElement ?: first
//	}
//
//	def private tailElement(FeatureRefTail it) {
//		tail.tailElement ?: feature
//	}
//
//	def private features(Head it) {
//		switch tail {
//			case null:	switch first:
//		}
//	}

}
