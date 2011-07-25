package nl.dslmeinte.ecore.util;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * Abstract support class to help with transforming (from anything) to Ecore.
 * 
 * @author Meinte Boersma
 */
abstract public class ToEcoreTransformerSupport {

	private final EcoreFactory factory = EcoreFactory.eINSTANCE;
	private final EcorePackage ecorePackage = EcorePackage.eINSTANCE;

	/**
	 * The end result/destination.
	 * <p>
	 * It's a member field to make it possible for parts of the transformation
	 * to add {@link EClass}es to it.
	 */
	protected final EPackage ePackage = factory.createEPackage();

	/**
	 * @return A new {@link EAttribute} with the given name and of type
	 *         {@code EString[1]}.
	 */
	protected EAttribute createStringAttribute(String name) {
		EAttribute eAttribute = factory.createEAttribute();
		eAttribute.setName(name);
		eAttribute.setEType(ecorePackage.getEString());
		eAttribute.setLowerBound(1);
		eAttribute.setUpperBound(1);
		return eAttribute;
	}

	/**
	 * @return A new {@link EClass} with the given name, which is already
	 *         properly contained in the {@link EPackage}.
	 */
	protected EClass createClass(String name) {
		EClass eClass = factory.createEClass();
		eClass.setName(name);
		ePackage.getEClassifiers().add(eClass);
		return eClass;
	}

	/**
	 * @return A new, containing (but as-yet uncontained) {@link EReference}
	 *         with the given name and with (UML-style) cardinality 1.
	 */
	protected EReference createContainingReference(String name) {
		return createContainingReference(name, 1, 1);
	}

	/**
	 * @return A new, containing (but as-yet uncontained) {@link EReference}
	 *         with the given name and the indicated cardinality lower- and
	 *         upper bounds.
	 */
	private EReference createContainingReference(String name, int lowerBound,
			int upperBound) {
				return createContainingReference(name, null, lowerBound, upperBound);
			}

	/**
	 * @return A new, containing (but as-yet uncontained) {@link EReference}
	 *         with the given name, given type and with (UML-style) cardinality
	 *         1.
	 */
	protected EReference createContainingReference(String name, EClass type) {
		return createContainingReference(name, type, 1, 1);
	}

	/**
	 * @return A new, containing (but as-yet uncontained) {@link EReference}
	 *         with the given name, given type and the indicated cardinality
	 *         lower- and upper bounds.
	 */
	protected EReference createContainingReference(String name, EClass type, int lowerBound, int upperBound) {
		EReference eReference = factory.createEReference();
		eReference.setName(name);
		eReference.setContainment(true);
		eReference.setEType(type);
		eReference.setLowerBound(lowerBound);
		eReference.setUpperBound(upperBound);
		return eReference;
	}

}
