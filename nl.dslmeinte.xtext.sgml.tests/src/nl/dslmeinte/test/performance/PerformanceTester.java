package nl.dslmeinte.test.performance;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.dslmeinte.test.ContentsUtil;

/**
 * Util class to support doing performance tests by performing a
 * {@link FileBasedTask} on all files inside a given directory (recursively or
 * not) with any of the given extensions. One {@link FileBasedTask} is
 * instantiated per run.
 * 
 * @author Meinte Boersma
 */
public class PerformanceTester {

	public static <T extends FileBasedTask> void performAll(
			Class<T> performerClass, File contentDir, String extensionsString, boolean recurse, int runs)
		throws IOException
	{
		List<File> files = new ContentsUtil(extensionsString).contents(contentDir, recurse);
		long start = System.currentTimeMillis();
		for( int i = 0; i < runs; i++ ) {
			if( runs > 1 ) {
				System.out.format( "starting run #%d\n", (i+1) );
			}
			FileBasedTask performer = instantiate(performerClass);
			for( File file : files ) {
				performer.perform(file);
			}
			performer.finish();
		}
		long millisElapsedTotal = System.currentTimeMillis() - start;
		System.out.printf(
				"#millis-elapsed-total=%d, <millis elapsed/run>=%d, <millis elapsed/(run x file)>=%d\n",
				millisElapsedTotal, millisElapsedTotal/runs, millisElapsedTotal/(runs*files.size())
			);
	}

	public static <T extends FileBasedTask> void performAll(
			Class<T> performerClass, File contentDir, String extensionsString, boolean recurse)
		throws IOException
	{
		performAll(performerClass, contentDir, extensionsString, recurse, 1);
	}

	private static <T extends FileBasedTask> T instantiate(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch( Exception e ) {
			throw new RuntimeException( "couldn't instantiate performer class " + clazz.getName() + " because: " + e.getMessage() );
		}
	}

}

