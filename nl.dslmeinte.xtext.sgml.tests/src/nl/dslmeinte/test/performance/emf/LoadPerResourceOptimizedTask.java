package nl.dslmeinte.test.performance.emf;

import java.io.File;

import nl.dslmeinte.test.performance.FileBasedTask;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * {@link FileBasedTask} implementation which loads each contents file into its
 * own EMF {@link ResourceSet} and provokes the JVM garbage collector after
 * {@link #FILE_INTERVAL} files. It then also prints info on the current heap
 * size in order to see how memory intensive loading actually is.
 * 
 * @author Meinte Boersma
 */
public class LoadPerResourceOptimizedTask extends LoadTask {

	private static final int FILE_INTERVAL = 100;

	private int count = 0;

	@Override
	public void perform(File file) {
		count++;
		super.perform(file);
		if( count%FILE_INTERVAL == 0 ) {
			System.gc();
			System.out.format( "heap-size=%dM\n", Runtime.getRuntime().totalMemory()/(1<<20) ); //$NON-NLS-1$
		}
	}

	@Override
	protected ResourceSet getResourceSet() {
		return new ResourceSetImpl();
	}

}
