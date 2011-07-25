package nl.dslmeinte.xtext.sgml.dtd.test.support;

import java.io.IOException;
import java.util.Collections;

import nl.dslmeinte.xtext.dtd.DtdStandaloneSetup;
import nl.dslmeinte.xtext.dtd.dtdModel.DocumentTypeDefinition;
import nl.dslmeinte.xtext.dtd.dtdModel.DtdModelPackage;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.mwe.utils.StandaloneSetup;

public class DTDTestSupport {

	private static final String MODELS_DIR = "nl.dslmeinte.xtext.sgml.tests/models/";

	static {
		if( !EPackage.Registry.INSTANCE.containsKey(DtdModelPackage.eNS_URI) ) {
			DtdStandaloneSetup.doSetup();
		}
		new StandaloneSetup().setPlatformUri("..");
	}

	protected URI createModelsURI(String path) {
		return URI.createPlatformResourceURI(MODELS_DIR + path, true);
	}

	protected DocumentTypeDefinition loadModel(URI uri) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(uri, true);
		EcoreUtil.resolveAll(resourceSet);
		return (DocumentTypeDefinition) resource.getContents().get(0);
	}

	protected void saveModel(EPackage ePackage, URI uri) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(ePackage);
		try {
			resource.save(Collections.emptyMap());
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}

}
