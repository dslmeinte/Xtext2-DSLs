package nl.dslmeinte.test.performance.emf;

import nl.dslmeinte.test.performance.FileBasedTask;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * {@link FileBasedTask} implementation which loads all contents files into one
 * gargantuan EMF {@link ResourceSet} and also tries to resolve all proxies
 * after all files have been loaded - without success/failure consequence, but
 * it does count for performance.
 * 
 * @author Meinte Boersma
 */
public class LoadIntoOneResourceSetTask extends LoadTask {

	private final ResourceSet resourceSet = new ResourceSetImpl();

	@Override
	protected ResourceSet getResourceSet() {
		return resourceSet;
	}

	@Override
	public void finish() {
		EcoreUtil.resolveAll(resourceSet);
		for( Resource resource : resourceSet.getResources() ) {
			if( resource.getErrors().size() > 0 ) {
				System.err.format( 
						"errors (parsing + resolution of cross-references) for %s:\n", //$NON-NLS-1$
						resource.getURI()
					);
				printErrors(resource);
			}
		}
		super.finish();
	}

}