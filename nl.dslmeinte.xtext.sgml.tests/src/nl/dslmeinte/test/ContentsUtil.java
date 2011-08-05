package nl.dslmeinte.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Util class to gather all files with certain extensions residing in a
 * directory and (optionally) any of its subdirectories (recursively).
 * 
 * @author Meinte Boersma
 */
public class ContentsUtil {

	/**
	 * Constructs a {@link ContentsUtil} instance which scans for files with
	 * extensions coming from the provided comma-separated extensions list.
	 */
	public ContentsUtil(String extensions) {
		for( String ext : extensions.split(",\\s*") ) { //$NON-NLS-1$
			this.extensions.add(ext);
		}
	}

	private final Set<String> extensions = new HashSet<String>();

	/**
	 * Returns a {@link List} of all {@link File}s in the indicated directory
	 * which have one of the extensions configured through
	 * {@link #ContentsUtil(String)}.
	 * 
	 * @param dir
	 *            a {@link File} pointing to a directory
	 * @param recurse
	 *            indicates whether recursion into sub directories happens
	 * @throws FileNotFoundException
	 *             in case the directory passed does not actually exist
	 */
	public List<File> contents(File dir, boolean recurse) throws FileNotFoundException {
		List<File> files = new ArrayList<File>();
		if( !dir.exists() ) {
			throw new FileNotFoundException( "location does not exist: " + dir.getPath() ); //$NON-NLS-1$
		}
		for( File file : dir.listFiles() ) {
			if( recurse && file.isDirectory() ) {
				files.addAll(contents(file, recurse));
			}
			if( file.isFile() ) {
				String fileName = file.getName();
				String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
				if( extensions.contains(ext) ) {
					files.add(file);
				}
			}
		}
		return files;
	}

}
