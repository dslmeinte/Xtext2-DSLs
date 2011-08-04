package nl.dslmeinte.xtext.sgml.test.simplemarkup.dtd;

import nl.dslmeinte.xtext.dtd.DtdStandaloneSetup;
import nl.dslmeinte.xtext.dtd.dtdModel.DtdModelPackage;
import nl.dslmeinte.xtext.sgml.dtd.DTD2XtextTransformer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.mwe.utils.StandaloneSetup;

@Deprecated
@SuppressWarnings("nls")
public class GenerateSimpleMarkupGrammar {

	static {
		if( !EPackage.Registry.INSTANCE.containsKey(DtdModelPackage.eNS_URI) ) {
			DtdStandaloneSetup.doSetup();
		}
		new StandaloneSetup().setPlatformUri("..");
	}

	private final static String SIMPLE_MARKUP_BASE_PACKAGE_NAME = "nl.dslmeinte.xtext.sgml.test.simplemarkup";
	private final static String SIMPLE_MARKUP_LANGUAGE_fqName = SIMPLE_MARKUP_BASE_PACKAGE_NAME + ".SimpleMarkup";
	private final static String SIMPLE_MARKUP_nsUri = "http://dslmeinte.nl/Xtext/sgml/dtd/test/simpleMarkup";

	/**
	 * Creates a {@code platform:/resource/} {@link URI} into this project from
	 * a source folder (either {@code "src"} or {@code "src-gen"}) and a postfix
	 * path.
	 */
	private static URI platformResourceURI(String sourceFolder, String postfixPath) {
		return URI.createPlatformResourceURI(
				String.format("%s/%s/%s/%s", SIMPLE_MARKUP_BASE_PACKAGE_NAME, sourceFolder, SIMPLE_MARKUP_BASE_PACKAGE_NAME.replace('.', '/'), postfixPath),
				true
			);
	}

	private void run() {
		DTD2XtextTransformer.transform(
				platformResourceURI("src", "dtd/simpleMarkup.dtd"),
				SIMPLE_MARKUP_LANGUAGE_fqName,
				SIMPLE_MARKUP_nsUri,
				platformResourceURI("src", "SimpleMarkup.xtext"),
				xpandPackageQualifiedName() + "::FeatureMixin"
			);
	}

	private String xpandPackageQualifiedName() {
		return getClass().getPackage().getName().replaceAll("\\.", "::");
	}

	public static void main(String...args) {
		new GenerateSimpleMarkupGrammar().run();
	}

}
