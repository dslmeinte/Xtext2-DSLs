package nl.dslmeinte.test.performance;

import java.io.File;

/**
 * Abstract implementation of {@link FileBasedTask} which keeps track of the
 * number of successes ({@link #performInternal(File)} returns {@code true})
 * and failures ({@link #performInternal(File)} returns {@code false} or an
 * exception is thrown), and printing info on that after each run.
 * 
 * @author Meinte Boersma
 */
public abstract class CountingFileBasedTask implements FileBasedTask {

	abstract protected boolean performInternal(File file) throws Exception;

	private int nSuccess = 0;
	private int nFailure = 0;

	@Override
	public void perform(File file) {
		try {
			boolean result = performInternal(file);
			if( result ) {
				nSuccess++;
				return;
			} else {
				printError(file);
			}
		} catch( Exception e ) {
			printError(file, "; cause: %s", e.getMessage());
		}
		nFailure++;
	}

	private void printError(File file) {
		printError(file, "");
	}

	private void printError(File file, String appendFormatString, Object...args) {
		System.err.format( "could not successfully perform task for file '%s'" + appendFormatString + "\n", file.getPath(), args );
	}

	@Override
	public void finish() {
		System.out.printf( "#success=%d, #failure=%d\n", nSuccess, nFailure );
	}

}
