package nl.dslmeinte.xtext.sgml.lexer.test;

import nl.dslmeinte.xtext.sgml.lexer.BaseTerminals;
import nl.dslmeinte.xtext.sgml.lexer.TokenFacade;
import nl.dslmeinte.xtext.sgml.test.simplemarkup.SimpleMarkupStandaloneSetup;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Injector;

public class AntlrTokenFacadeTest {

	private final Injector injector = new SimpleMarkupStandaloneSetup().createInjectorAndDoEMFRegistration();

	@Test
	public void test_actual_implementation() {
		TokenFacade tokenFacade = injector.getInstance(TokenFacade.class);
		for( BaseTerminals baseTerminal : BaseTerminals.values() ) {
			try {
				int tokenType = tokenFacade.map(baseTerminal);
				System.out.format( "%s -> %d\n", baseTerminal.name(), tokenType ); //$NON-NLS-1$
				if( baseTerminal.isKeyword() ) {
					System.out.format( "\t'%s'\n", baseTerminal.getKeyword() ); //$NON-NLS-1$
				}
			} catch (NullPointerException e) {
				Assert.fail( "no mapping defined for " + BaseTerminals.class.getName() + "." + baseTerminal.name() ); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

}
