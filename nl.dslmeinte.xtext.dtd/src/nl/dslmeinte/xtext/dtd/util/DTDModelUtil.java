package nl.dslmeinte.xtext.dtd.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.dslmeinte.xtext.dtd.dtdModel.Attribute;
import nl.dslmeinte.xtext.dtd.dtdModel.AttributeList;
import nl.dslmeinte.xtext.dtd.dtdModel.Definition;
import nl.dslmeinte.xtext.dtd.dtdModel.DocumentTypeDefinition;
import nl.dslmeinte.xtext.dtd.dtdModel.Element;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Util class to interrogate {@link DocumentTypeDefinition} instances/models and
 * some of their members.
 * 
 * @author Meinte Boersma
 */
public class DTDModelUtil {

	/**
	 * @return all {@link Attribute}s referencing the given {@link Element}.
	 */
	public static List<Attribute> attributes(Element element) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		DocumentTypeDefinition root = (DocumentTypeDefinition) EcoreUtil.getRootContainer(element);
		for( Definition def : root.getDefinitions() ) {
			if( def instanceof AttributeList ) {
				AttributeList attributeList = (AttributeList) def;
				if( attributeList.getElement() == element ) {
					attributes.addAll(attributeList.getAttributes());
				}
			}
		}
		return attributes;
	}

	/**
	 * @return the first {@link Element} in the given
	 *         {@link DocumentTypeDefinition}.
	 */
	public static Element firstElement(DocumentTypeDefinition dtd) {
		for( Definition definition : dtd.getDefinitions() ) {
			if( definition instanceof Element ) {
				return (Element) definition;
			}
		}
		throw new IllegalArgumentException("no elements in DTD"); //$NON-NLS-1$
	}

	/**
	 * @return a {@link Set} (of {@link String}s) of all keywords, i.e.: tag and
	 *         attribute names, in the given {@link DocumentTypeDefinition}.
	 */
	public static Set<String> keywords(DocumentTypeDefinition dtd) {
		Set<String> keywordsSet = new HashSet<String>();
		for( Definition definition : dtd.getDefinitions() ) {
			if( definition instanceof Element ) {
				keywordsSet.add(((Element) definition).getName());
			}
			if( definition instanceof AttributeList ) {
				for( Attribute attribute : ((AttributeList) definition).getAttributes() ) {
					keywordsSet.add(attribute.getName());
				}
			}
		}
		return keywordsSet;
	}

	/**
	 * @return the {@link Element} in the given {@link DocumentTypeDefinition}
	 *         with the given name or {@code null} if none could be found.
	 */
	public static Element findElement(String name, DocumentTypeDefinition dtd) {
		for( Definition definition : dtd.getDefinitions() ) {
			if(    definition instanceof Element
				&& (((Element) definition).getName().equalsIgnoreCase(name)) )
			{
				return( (Element) definition );
			}
		}
		return null;
	}

}
