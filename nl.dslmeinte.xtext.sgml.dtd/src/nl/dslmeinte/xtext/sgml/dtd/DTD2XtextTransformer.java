package nl.dslmeinte.xtext.sgml.dtd;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import nl.dslmeinte.xpand.XpandCallConfiguration;
import nl.dslmeinte.xtext.dtd.DtdStandaloneSetup;
import nl.dslmeinte.xtext.dtd.dtdModel.DtdModelPackage;
import nl.dslmeinte.xtext.dtd.util.DTDManager;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

/**
 * Transforms a {@code .dtd} file into an Xtext grammar which is compatible with
 * the custom (but generic) SGML lexer.
 * 
 * TODO  document usage
 * 
 * @deprecated This class has to be re-based on Xtend2.
 * 
 * @author Meinte Boersma
 */
@Deprecated
@SuppressWarnings("nls")
public class DTD2XtextTransformer {

	static {
		if( !EPackage.Registry.INSTANCE.containsKey(DtdModelPackage.eNS_URI) ) {
			DtdStandaloneSetup.doSetup();
		}
	}

	/**
	 * Transforms the {@code DTD} file into a Xtext grammar definition and saves
	 * that to the {@link OutputStream} given.
	 */
	public static void transform(URI dtdUri, String fqLanguageName, String nsURI, OutputStream xtextOutput) {
		baseConfig(dtdUri, fqLanguageName, nsURI, xtextOutput).evaluate();
	}

	/**
	 * Transforms the {@code DTD} file into a Xtext grammar definition and saves
	 * that to the {@link URI} given.
	 */
	public static void transform(URI dtdUri, String fqLanguageName, String nsURI, URI xtextUri) {
		transform(dtdUri, fqLanguageName, nsURI, toOutputStream(xtextUri));
	}

	private static OutputStream toOutputStream(URI uri) {
		String path = "../" + ( uri.isPlatformResource() ? uri.toPlatformString(true) : uri.toFileString() );
		try {
			return new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Transforms the {@code DTD} file into a Xtext grammar definition and saves
	 * that to the {@link OutputStream} given.
	 */
	public static void transform(URI dtdUri, String fqLanguageName, String nsURI, OutputStream xtextOutput, String commaSeparatedAdvices) {
		XpandCallConfiguration config = baseConfig(dtdUri, fqLanguageName, nsURI, xtextOutput);
		String[] advices = commaSeparatedAdvices.split("\\s*,\\s*");
		for( String advice : advices ) {
			config.registerAdvice(advice);
		}
		config.evaluate();
	}

	/**
	 * Transforms the {@code DTD} file into a Xtext grammar definition and saves
	 * that to the {@link OutputStream} given.
	 */
	public static void transform(URI dtdUri, String fqLanguageName, String nsURI, URI xtextUri, String commaSeparatedAdvices) {
		transform(dtdUri, fqLanguageName, nsURI, toOutputStream(xtextUri), commaSeparatedAdvices);
	}

	private static XpandCallConfiguration baseConfig(URI dtdUri, String fqLanguageName, String nsURI, OutputStream xtextOutput) {
		return new XpandCallConfiguration()
			.withEmfRegistry()
			.call(packagePrefix() + "::DTD2Xtext::main")
			.on(new DTDManager(dtdUri).getDTD())
			.with(fqLanguageName, stringify(nsURI))
			.to(xtextOutput);
	}

	private static String packagePrefix() {
		return DTD2XtextTransformer.class.getPackage().getName().replaceAll("\\.", "::");
	}

	private static String stringify(String str) {
		return "\"" + str + "\"";
	}

}
