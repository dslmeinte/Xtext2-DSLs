package nl.dslmeinte.test.performance;

import java.io.File;

/**
 * Implementors are be used by
 * {@link PerformanceTester#performAll(Class, File, String, boolean, int)}
 * to run performance (load) tests.
 * 
 * @author Meinte Boersma
 */
public interface FileBasedTask {

	/**
	 * Called on each contents file.
	 */
	void perform(File file);

	/**
	 * Called after each run, to allow run-wide destructions and reporting.
	 */
	void finish();

}