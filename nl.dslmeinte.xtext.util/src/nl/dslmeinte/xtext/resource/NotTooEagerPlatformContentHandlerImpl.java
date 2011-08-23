package nl.dslmeinte.xtext.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.impl.PlatformContentHandlerImpl;
import org.eclipse.xtext.resource.impl.ResourceServiceProviderRegistryImpl;

/**
 * Sub implementation of {@link PlatformContentHandlerImpl} which defeats the
 * "too eager"-check in {@link ResourceServiceProviderRegistryImpl.InternalData}
 * in order to be able to make use of the content handling and content type
 * support provided by various Eclipse plugins.
 * <p>
 * The {@link PlatformContentHandlerImpl} implements a {@link ContentHandler}
 * which is aware of the fact that we're running "in a platform", and more to
 * the point: it is aware of the workspace and the {@code platform:/resource/}
 * URI scheme/protocol. Unfortunately, it doesn't pass the "too eager"-check.
 * <p>
 * This functionality is provided by the {@code org.eclipse.emf.ecore} bundle.
 * Look at the {@code plugin.xml} of that plug-in, and the classes in
 * {@code org.eclipse.emf.ecore.plugin} to get a better idea of the inner
 * workings of this.
 * <p>
 * <em>Important</em>: to make use of this class, register an extension point in
 * at least one of your plugins like so - which one doesn't really matter.
 * 
 * <pre>
 *    <extension point="org.eclipse.emf.ecore.content_handler">
 *       <contentHandler
 *             class="nl.dslmeinte.xtext.resource.NotTooEagerPlatformContentHandlerImpl"
 *             priority="3000">
 *       </contentHandler>
 *    </extension>
 * </pre>
 * 
 * This ensures that the not too eager {@link ContentHandler} gets priority over
 * the default one (which fails the "too eager" check by stating that it can
 * handle {@code null} as well)..
 * 
 * @author Meinte Boersma
 */
public class NotTooEagerPlatformContentHandlerImpl extends PlatformContentHandlerImpl {

	/**
	 * Handle all {@link URI}s except a {@code null} one.
	 */
	@Override
	public boolean canHandle(URI uri) {
		return ( uri == null ) ? false : true;
	}

}
