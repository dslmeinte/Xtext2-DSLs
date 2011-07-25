package nl.dslmeinte.xtext.sgml.dtd;

import java.util.HashMap;
import java.util.Map;

import nl.dslmeinte.ecore.util.ToEcoreTransformerSupport;
import nl.dslmeinte.xtext.dtd.dtdModel.Attribute;
import nl.dslmeinte.xtext.dtd.dtdModel.Definition;
import nl.dslmeinte.xtext.dtd.dtdModel.DocumentTypeDefinition;
import nl.dslmeinte.xtext.dtd.dtdModel.Element;
import nl.dslmeinte.xtext.dtd.dtdModel.EmptyContent;
import nl.dslmeinte.xtext.dtd.dtdModel.Expression;
import nl.dslmeinte.xtext.dtd.util.DTDModelUtil;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * Class to transform a DTD definition (file) to an Ecore model, representing
 * the same hierarchy.
 * <p>
 * I'm using Java instead of Xtend (which would be more convenient/comfortable)
 * because that would mean learning another language, the particulars of the
 * Xtend runtime engine and configuring a launch configuration (MWE2 file or
 * such).
 * 
 * @author Meinte Boersma
 */
public class DTD2EcoreTransformer extends ToEcoreTransformerSupport {

	/**
	 * The source.
	 */
	private final DocumentTypeDefinition dtdDefinition;

	public DTD2EcoreTransformer(DocumentTypeDefinition dtdDefinition) {
		this.dtdDefinition = dtdDefinition;
	}

	public EPackage transform() {
		boolean first = true;
		for( Definition definition : dtdDefinition.getDefinitions() ) {
			if( definition instanceof Element ) {
				Element element = (Element) definition;
				EClass eClass = map(element);
				if( first ) {
					sgmlDocument.getEStructuralFeatures().add(createContainingReference("root", eClass));
					first = false;
				}
			}
		}
		return ePackage;
	}

	private final Map<Element, EClass> element2eClassMap = new HashMap<Element, EClass>();

	/**
	 * Maps {@link Element} &rarr; top-level {@link EClass}, taking care of
	 * containment and memoisation.
	 */
	private EClass map(Element element) {
		if( element2eClassMap.containsKey(element) ) {
			return element2eClassMap.get(element);
		}

		EClass eClass = createClass(element.getName());
		element2eClassMap.put(element, eClass);
		handleContent(element, eClass);

		return eClass;
	}

	/*
	 * +-----------------------+
	 * | Actual transformation |
	 * +-----------------------+
	 */

	/**
	 * Handles the content of {@link Element} by mapping it and containing it in
	 * the given {@link EClass}.
	 */
	private void handleContent(Element element, EClass eClass) {
		for( Attribute attribute : DTDModelUtil.attributes(element) ) {
			eClass.getEStructuralFeatures().add(map(attribute));
		}

		Expression content = element.getContent();

		if( content instanceof EmptyContent ) {
			return;
		} else {
			eClass.getESuperTypes().add(container);
		}
	}


	/*
	 * +--------------+
	 * | Simple stuff |
	 * +--------------+
	 */

	private EAttribute map(Attribute attribute) {
		EAttribute eAttribute = createStringAttribute(attribute.getName());

		switch( attribute.getCardinality() ) {
			case IMPLIED :
				{
					eAttribute.setLowerBound(0);
					break;
				}
			case REQUIRED :
				{
					eAttribute.setLowerBound(1);
					break;
				}
		}

		return eAttribute;
	}

	private final EClass sgmlDocument = createClass("SgmlDocument");
	private final EClass entity = createClass("Entity");
	private final EClass container = createClass("Container");
	private final EClass pcDataContent = createClass("#PCDataContent");
	private final EClass pcDataTextContent = createClass("#PCDataTextContent");
	private final EClass pcDataEntityReference = createClass("#PCDataEntityReference");

	{
		pcDataTextContent.getEStructuralFeatures().add(createStringAttribute("text"));
		entity.getEStructuralFeatures().add(createStringAttribute("name"));

		EReference eReference = createContainingReference("entity", entity);
		eReference.setContainment(false);
		pcDataEntityReference.getEStructuralFeatures().add(eReference);

		pcDataTextContent.getESuperTypes().add(pcDataContent);
		pcDataEntityReference.getESuperTypes().add(pcDataContent);

		container.getEStructuralFeatures().add(
				createContainingReference("contents", EcorePackage.eINSTANCE.getEObject(), 0, -1)
			);
		container.setAbstract(true);
		sgmlDocument.getEStructuralFeatures().add(createContainingReference("entities", entity, 0, -1));
	}

}
