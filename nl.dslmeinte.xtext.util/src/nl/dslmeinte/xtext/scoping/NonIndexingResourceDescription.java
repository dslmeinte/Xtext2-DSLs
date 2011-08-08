package nl.dslmeinte.xtext.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.util.IAcceptor;

/**
 * Implementation of {@link IDefaultResourceDescriptionStrategy} for the which
 * nothing from any resource is indexed(/exported). This effectively disables
 * all cross-referencing across file boundary.
 * 
 * @author Meinte Boersma
 */
public class NonIndexingResourceDescription implements IDefaultResourceDescriptionStrategy {

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		return false;
	}

	@Override
	public boolean createReferenceDescriptions(EObject eObject, URI exportedContainerURI, IAcceptor<IReferenceDescription> acceptor) {
		return false;
	}

}
