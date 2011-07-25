package nl.dslmeinte.xtext.dtd.util;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.dslmeinte.xtext.dtd.DtdStandaloneSetup;
import nl.dslmeinte.xtext.dtd.dtdModel.DocumentTypeDefinition;
import nl.dslmeinte.xtext.dtd.dtdModel.DtdModelPackage;
import nl.dslmeinte.xtext.dtd.dtdModel.Element;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Class to manage and provide util functionality on DTD files.
 * 
 * @author Meinte Boersma
 */
public class DTDManager {

	static {
		if( !EPackage.Registry.INSTANCE.containsKey(DtdModelPackage.eNS_URI) ) {
			DtdStandaloneSetup.doSetup();
		}
	}

	private DocumentTypeDefinition dtd;

	public DocumentTypeDefinition getDTD() {
		return dtd;
	}

	/**
	 * Tries to load the DTD file specified by the {@link URI} while
	 * constructing an instance. Throws an {@link IllegalArgumentException} in
	 * case the DTD file had errors.
	 */
	public DTDManager(URI uri) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		try {
			resource.load(Collections.emptyMap());
			EcoreUtil.resolveAll(resource);
			dtd = (DocumentTypeDefinition) resource.getContents().get(0);
			if( resource.getErrors().size() > 0 ) {
				throw new IllegalArgumentException("errors while parsing DTD file");
			}
		} catch (IOException e) {
			System.err.println(e);
			throw new RuntimeException("could not load DTD file", e);
		}
	}

	private Map<String, Element> elementCache = new HashMap<String, Element>();

	/**
	 * @return the first (and only) {@link Element} in the DTD.
	 */
	public Element findElement(String name) {
		Element element = elementCache.get(name);
		if( element == null ) {
			element = DTDModelUtil.findElement(name, dtd);
			elementCache.put(name, element);
		}
		return element;
	}

	private Set<String> keywords = null;

	/**
	 * @return all the tag keywords in the DTD.
	 */
	public Set<String> tagKeywords() {
		if( keywords != null ) {
			return keywords;
		}

		keywords = new HashSet<String>(DTDModelUtil.keywords(dtd));
		return keywords;
	}

}
