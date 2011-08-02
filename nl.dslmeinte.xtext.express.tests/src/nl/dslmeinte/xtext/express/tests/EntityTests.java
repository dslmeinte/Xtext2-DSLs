package nl.dslmeinte.xtext.express.tests;

import nl.dslmeinte.xtext.express.ExpressStandaloneSetup;

import org.eclipse.xtext.junit.AbstractXtextTests;
import org.junit.Before;
import org.junit.Test;

public class EntityTests extends AbstractXtextTests {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		with(ExpressStandaloneSetup.class);
	}

	@Test
	public void test_9_1() throws Exception {
		getModelAndExpect(inSchema("TYPE person_name = STRING;\nEND_TYPE;"), 0);
	}

	@Test
	public void test_9_1_where_clause() throws Exception {
		getModelAndExpect(inSchema("TYPE positive = INTEGER;\nWHERE\n\tnot_negative : SELF > 0;\nEND_TYPE;"), 0);
	}

	@Test
	public void test_9_2_1_1() throws Exception {
		getModelAndExpect(inSchema("ENTITY point;\n\tx, y, z : REAL;\nEND_ENTITY;"), 0);
	}

	private String inSchema(String text) {
		return "SCHEMA mySchema;\n" + text + "\nEND_SCHEMA;";
	}

}
