package nl.dslmeinte.xtext.sgml.dtd.test;

import nl.dslmeinte.xtext.dtd.dtdModel.DocumentTypeDefinition;
import nl.dslmeinte.xtext.sgml.dtd.DTD2EcoreTransformer;
import nl.dslmeinte.xtext.sgml.dtd.test.support.DTDTestSupport;

import org.eclipse.emf.ecore.EPackage;
import org.junit.Test;

public class DTD2EcoreTransformerTest extends DTDTestSupport {

	@Test
	public void test_transformation_of_trivial_dtd_file() {
		doTransformation("trivial");
	}

	private void doTransformation(String modelName) {
		DocumentTypeDefinition dtdDefinition = loadModel(createModelsURI(modelName + ".dtd"));
//		EPackage ePackage = new DTD2EcoreWithSyntheticsTransformer(dtdDefinition).transform();
		EPackage ePackage = new DTD2EcoreTransformer(dtdDefinition).transform();
		ePackage.setName(modelName);
		ePackage.setNsPrefix(modelName.toLowerCase());
		ePackage.setNsURI("http://dslmeinte.nl/Xtext/sgml/dtd/" + modelName );
		saveModel(ePackage, createModelsURI(modelName + "-gen.ecore"));
	}

}
