package nl.dslmeinte.xtext.express.tests;

import nl.dslmeinte.xtext.express.base.BaseStandaloneSetup;

import org.eclipse.xtext.junit.AbstractXtextTests;
import org.junit.Before;

public class BaseTests extends AbstractXtextTests {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		with(BaseStandaloneSetup.class);
	}

	public void test_integer_literal() throws Exception {
		getModelAndExpect("123456", 0);
	}

	public void test_real_literal() throws Exception {
		getModelAndExpect("1.E06", 0);
	}

}
