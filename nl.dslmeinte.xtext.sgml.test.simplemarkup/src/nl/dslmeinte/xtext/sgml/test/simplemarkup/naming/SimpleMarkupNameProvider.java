package nl.dslmeinte.xtext.sgml.test.simplemarkup.naming;

import nl.dslmeinte.xtext.sgml.test.simplemarkup.simplemarkup.Section;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.naming.SimpleNameProvider;

public class SimpleMarkupNameProvider extends SimpleNameProvider {

	/**
	 * The qualified name of {@link Section} is
	 * {@code this.Section_tagOpen.attributes.name} without surrounding quotes -
	 * all other {@link EObject}s are delegated to {@link SimpleNameProvider}.
	 */
	@Override
	public QualifiedName getFullyQualifiedName(EObject object) {
		if( object instanceof Section ) {
			String name = ((Section) object).getSection_tagOpen().getAttributes().getName();
			return name != null ? QualifiedName.create(name) : null;
		}
		return super.getFullyQualifiedName(object);
	}

}
