package nl.dslmeinte.xtext.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.validation.NamesAreUniqueValidator;

/**
 * Implementation of {@link IDefaultResourceDescriptionStrategy} which exports
 * nothing from any resource is indexed(/exported). This effectively disables
 * all cross-referencing across file boundary among a few other aspects which
 * rely on the names of exported objects, like unique names validation courtesy
 * of the {@link NamesAreUniqueValidator} composed check. This is useful in case
 * all your resources really do not depend on other resources and you do not
 * need the default names-are-unique validation.
 * 
 * @author Meinte Boersma
 */
public class NonIndexingResourceDescriptionStrategy implements IDefaultResourceDescriptionStrategy {

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		return false;
	}

	@Override
	public boolean createReferenceDescriptions(EObject eObject, URI exportedContainerURI, IAcceptor<IReferenceDescription> acceptor) {
		return false;
	}

}
