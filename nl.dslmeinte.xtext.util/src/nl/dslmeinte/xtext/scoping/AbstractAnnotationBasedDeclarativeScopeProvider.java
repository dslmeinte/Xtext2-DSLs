package nl.dslmeinte.xtext.scoping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;

import com.google.common.base.Predicate;

/**
 * Implementation of {@link AbstractDeclarativeScopeProvider} which relies on
 * Java annotations to resolve the polymorphic dispatching rather than on a
 * naming convention. By using the constants defined in the corresponding
 * {@link EPackage} the scoping provider implementation has some static (i.e.,
 * compile-time) checking.
 * <p>
 * For an example and some design decisions, check the <a href=
 * "http://dslmeinte.wordpress.com/2011/05/09/annotation-based-dispatch-for-scope-providers/"
 * >corresponding blog</a>.
 * 
 * @author Meinte Boersma (see my <a
 *         href="http://dslmeinte.wordpress.com/">blog</a> and my <a
 *         href="http://www.dslconsultancy.com/">company web site</a>).
 */
public class AbstractAnnotationBasedDeclarativeScopeProvider extends AbstractDeclarativeScopeProvider {

	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * Returns a {@link Predicate} which checks whether a {@link Method} is a
	 * correct target for scoping based on annotations it might have.
	 */
	@Override
	protected Predicate<Method> getPredicate(EObject context,
			final EReference reference) {
		final int classId = reference.getEContainingClass().getClassifierID();
		final int featureId = reference.getFeatureID();
		final Predicate<Method> nameBasedPredicate = super.getPredicate(context, reference);
		return new Predicate<Method>() {
			public boolean apply(Method method) {
				if( method.getParameterTypes().length != 2 ) {
					return false;
				}
				ScopeForReference annotation = method.getAnnotation(ScopeForReference.class);
				if( annotation == null ) {
					if( nameBasedPredicate.apply(method) ) {
						logger.warn(strategyWarning(method));
					}
					return false;
				}
				return( classId == annotation.classId() && featureId == annotation.featureId() );
			}
		};
	}

	/**
	 * Annotation for methods which provide scoping for specific
	 * {@link EReference}s. The {@link #classId()} is the classifier ID of the
	 * {@link EClass} containing the {@link EReference} to scope for. The
	 * {@link #featureId()} is the feature ID for the {@link EReference} to
	 * scope for.
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface ScopeForReference {

		/**
		 * The classifier ID of the {@link EClass} containing the {@link EReference} to scope for.
		 * @return
		 */
		int classId();

		/**
		 * The feature ID for the {@link EReference} to scope for.
		 */
		int featureId();

	}

	/**
	 * Returns a {@link Predicate} which checks whether a {@link Method} is a
	 * correct target for scoping based on annotations it might have.
	 */
	@Override
	protected Predicate<Method> getPredicate(EObject context, EClass type) {
		final int classId = type.getClassifierID();
		final Predicate<Method> nameBasedPredicate = super.getPredicate(context, type);
		return new Predicate<Method>() {
			@Override
			public boolean apply(Method method) {
				if( method.getParameterTypes().length != 2 ) {
					return false;
				}
				ScopeForType annotation = method.getAnnotation(ScopeForType.class);
				if( annotation == null ) {
					if( nameBasedPredicate.apply(method) ) {
						logger.warn(strategyWarning(method));
					}
					return false;
				}
				return( classId == annotation.classId() );
			}
		};
	}

	/**
	 * Annotation for methods which provide scoping for specific
	 * {@link EClass}es. The {@link #classId()} is the classifier ID of the
	 * {@link EClass} containing the {@link EReference} to scope for.
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface ScopeForType {

		/**
		 * The classifier ID of the {@link EClass} to scope for.
		 * @return
		 */
		int classId();

	}

	private String strategyWarning(Method method) {
		return String.format( "method %s complies to name-based declarative strategy while using annotation-based declarative strategy", method.getName() );
	}

}
