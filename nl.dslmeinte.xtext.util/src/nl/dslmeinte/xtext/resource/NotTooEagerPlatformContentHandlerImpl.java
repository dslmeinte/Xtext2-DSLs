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
 * the default one.
 * 
 * @author Meinte Boersma
 */
public class NotTooEagerPlatformContentHandlerImpl extends PlatformContentHandlerImpl {

	@Override
	public boolean canHandle(URI uri) {
		return ( uri == null ) ? false : true;
	}

}
