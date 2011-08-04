package nl.dslmeinte.xtext.builder;

import org.eclipse.xtext.builder.builderState.IMarkerUpdater;
import org.eclipse.xtext.builder.builderState.MarkerUpdaterImpl;
import org.eclipse.xtext.ui.shared.internal.SharedModule;

/**
 * Customization of {@link SharedModule} which binds the
 * {@link ReadonlyIndifferentMarkerUpdater} instead of {@link MarkerUpdaterImpl}.
 * <p>
 * Be sure to configure this class in the {@code plugin.xml} file of the
 * {@code .ui} project for your Xtext language, like so:
 * 
 * <pre>
 *    <extension
 *          point="org.eclipse.xtext.ui.shared.overridingGuiceModule">
 *       <module
 *             class="nl.dslmeinte.xtext.builder.ReadonlyIndifferentOverridingGuiceModule">
 *       </module>
 *    </extension>
 * </pre>
 * <p>
 * Note that it relies on non-strict OSGi resolution (through the
 * {@link SuppressWarnings suppressWarnings-restriction} annotation), because of
 * Equinox (OSGi) access restrictions in the {@code org.eclipse.xtext.builder}
 * (through an {@code x-friends} declaration) and
 * {@code org.eclipse.xtext.ui.shared} (through an {@code x-internal}
 * declaration) plug-ins. Therefore, make sure to <em>not</em> run this with
 * {@code -Dosgi.resolverMode=strict}.
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
