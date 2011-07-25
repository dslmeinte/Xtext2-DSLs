package nl.dslmeinte.xtext.sgml.simplemarkup.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

import nl.dslmeinte.xtext.conditional.ConditionalStandaloneSetup;
import nl.dslmeinte.xtext.conditional.parser.antlr.ConditionalParser;
import nl.dslmeinte.xtext.sgml.test.simplemarkup.SimpleMarkupStandaloneSetup;
import nl.dslmeinte.xtext.sgml.test.simplemarkup.simplemarkup.Reference;
import nl.dslmeinte.xtext.sgml.test.simplemarkup.simplemarkup.Section;
import nl.dslmeinte.xtext.sgml.test.simplemarkup.simplemarkup.SgmlDocument;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.parser.IParseResult;
import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Injector;

public class SimpleMarkupParserTest {

	private final static Injector conditionalInjector = new ConditionalStandaloneSetup().createInjector();

	static {
		SimpleMarkupStandaloneSetup.doSetup();
		ConditionalStandaloneSetup.doSetup();
	}

	@Test
	public void test_resolution_of_internal_crossReference() {
		SgmlDocument document = load("models/example1.sm", new ResourceSetImpl());
		EcoreUtil.resolveAll(document);
		Section to = ((Reference) document.getRoot().getContents().get(0)).getReference_tagOpen().getAttributes().getTo();
		Assert.assertNotNull(to);
		Assert.assertFalse(to.eIsProxy());
	}

	@Test
	public void test_resolution_of_external_crossReference() {
		ResourceSet resourceSet = new ResourceSetImpl();
		load("models/example1.sm", resourceSet);
		SgmlDocument document2 = load("models/example2.sm", resourceSet);
		Section to = ((Reference) document2.getRoot().getContents().get(0)).getReference_tagOpen().getAttributes().getTo();
		Assert.assertNotNull(to);
		Assert.assertFalse(to.eIsProxy());	// proxy gets resolved because of access
	}

	@Test
	public void test_parsing_of_conditional_expression() {
		SgmlDocument document = load("models/example1.sm", new ResourceSetImpl());
		String condition = ((Section) document.getRoot().getContents().get(1)).getSection_tagOpen().getAttributes().getCondition().getExpr();
		Assert.assertNotNull(condition);
		ConditionalParser parser = conditionalInjector.getInstance(ConditionalParser.class);
		Assert.assertNotNull(parser);
		StringReader input = new StringReader(condition);
		IParseResult result = parser.parse(input);
		Assert.assertNotNull(result);
	}

	private SgmlDocument load(String fileUri, ResourceSet resourceSet) {
		Resource resource = resourceSet.createResource(URI.createFileURI(fileUri));
		try {
			resource.load(Collections.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
		if( resource.getErrors().size() > 0 ) {
			for( Diagnostic diagnostic : resource.getErrors() ) {
				System.err.println(diagnostic.getMessage() + " @L" + diagnostic.getLine() );
			}
			Assert.fail("parse errors (see error console)");
		}
		EObject root = resource.getContents().get(0);
		Assert.assertNotNull(root);
		Assert.assertTrue(root instanceof SgmlDocument);
		return (SgmlDocument) root;
	}

}
