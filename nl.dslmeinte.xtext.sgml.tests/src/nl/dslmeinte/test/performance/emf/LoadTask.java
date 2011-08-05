package nl.dslmeinte.test.performance.emf;

import java.io.File;
import java.util.Collections;

import nl.dslmeinte.test.performance.CountingFileBasedTask;
import nl.dslmeinte.test.performance.FileBasedTask;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Abstract implementation of {@link FileBasedTask} (actually,
 * {@link CountingFileBasedTask}) to load contents files as EMF {@link Resource}s
 * in such a way that the implementation can choose how to provide a
 * {@link ResourceSet}.
 * 
 * @author Meinte Boersma
 */
public abstract class LoadTask extends CountingFileBasedTask {

	protected abstract ResourceSet getResourceSet();

	@Override
	protected boolean performInternal(File file) throws Exception {
		ResourceSet resourceSet = getResourceSet();
		Resource resource = resourceSet.createResource(URI.createFileURI(file.getPath()));
		resource.load(Collections.emptyMap());
		printErrors(resource);
		return( resource.getErrors().size() == 0 );
	}

	protected void printErrors(Resource resource) {
		for( Diagnostic error : resource.getErrors() ) {
			System.err.format( "L%d: %s\n", error.getLine(), error.getMessage() ); //$NON-NLS-1$
		}
	}

}
