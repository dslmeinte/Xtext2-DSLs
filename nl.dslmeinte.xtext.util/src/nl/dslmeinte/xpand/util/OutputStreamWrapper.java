package nl.dslmeinte.xpand.util;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.xpand2.output.Output;
import org.eclipse.xpand2.output.OutputImpl;

/**
 * Custom implementation of {@link OutputImpl} which redirects
 * the templates' output directly to a {@link OutputStream}.
 * 
 * @author Meinte Boersma  (adaptation)
 * @author Marc Schlienger (original; (c) 2009, see http://www.innoq.com/blog/mrs)
 */
public class OutputStreamWrapper extends OutputImpl implements Output {

	private OutputStream body;

	public OutputStreamWrapper(OutputStream body) {
		this.body = body;
	}

	/**
	 * {@see Output(Impl)#write(String)}
	 */
	@Override
	public void write(String text) {
		try {
			body.write(text.getBytes());
		} catch (IOException e) {
			throw new RuntimeException( "Couldn't write to wrapped " + OutputStream.class.getSimpleName() + " instance.", e );
		}
	}

	/**
	 * {@see Output(Impl)#openFile(String, String)}
	 */
	@Override
	public void openFile(String path, String outletName) {
		throw new UnsupportedOperationException( getClass().getName() + " can only be used for non-file templates." );
	}

}