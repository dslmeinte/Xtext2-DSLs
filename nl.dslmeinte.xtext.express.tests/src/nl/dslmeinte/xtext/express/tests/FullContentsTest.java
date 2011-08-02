package nl.dslmeinte.xtext.express.tests;

import java.io.InputStream;

import junit.framework.Assert;
import nl.dslmeinte.xtext.express.ExpressStandaloneSetup;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.mwe.utils.StandaloneSetup;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.junit.Before;

public class FullContentsTest extends AbstractXtextTests {

	static {
		new StandaloneSetup().setPlatformUri("..");
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		with(ExpressStandaloneSetup.class);
	}

	public void test_ifc2x3() throws Exception {
		URI uri = URI.createPlatformResourceURI("express-artifacts/ifc2x3.exp", true);
		InputStream inputStream = URIConverter.INSTANCE.createInputStream(uri);
		EObject model = getModel(inputStream);
		Assert.assertNotNull(model);
	}

}
