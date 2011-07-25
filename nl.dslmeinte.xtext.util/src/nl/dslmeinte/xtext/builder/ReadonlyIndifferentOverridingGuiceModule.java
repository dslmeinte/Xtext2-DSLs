package nl.dslmeinte.xtext.builder;

import org.eclipse.xtext.builder.builderState.IMarkerUpdater;
import org.eclipse.xtext.builder.builderState.MarkerUpdaterImpl;
import org.eclipse.xtext.ui.shared.internal.SharedModule;

/**
 * Customization of {@link ClusteringModule} which also binds the
 * {@link ReadonlyIndifferentMarkerUpdater} instead of {@link MarkerUpdaterImpl}
 * : because the order in which Guice modules are allowed to bind is
 * indeterminate we can't rely on doing the binding in the UI Guice module.
 * <p>
 * Be sure to configure this class in the {@code plugin.xml} file of the
 * {@code .ui} project for your Xtext language, like so:
 * 
 * <pre>
 * </pre>
 * 
 * TODO  finish code example
 * 
 * @author Meinte Boersma
 */
@SuppressWarnings("restriction")
public class ReadonlyIndifferentOverridingGuiceModule extends SharedModule {

	@Override
	public void configure() {
		super.configure();
		bind(IMarkerUpdater.class).to(ReadonlyIndifferentMarkerUpdater.class);
	}

}
