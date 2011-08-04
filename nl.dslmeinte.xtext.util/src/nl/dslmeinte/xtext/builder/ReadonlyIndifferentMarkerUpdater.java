package nl.dslmeinte.xtext.builder;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.builder.builderState.MarkerUpdaterImpl;
import org.eclipse.xtext.builder.builderState.Messages;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.ui.MarkerTypes;
import org.eclipse.xtext.ui.resource.IStorage2UriMapper;
import org.eclipse.xtext.util.Pair;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

/**
 * Re-implementation of {@link MarkerUpdaterImpl} which promotes parse and
 * validation error to error markers regardless of the files being read-only.
 * <p>
 * The implementation re-implements {@link MarkerUpdaterImpl} by copying the
 * relevant parts of the source and commenting out the lines with the {@code if}
 * -statement concerning read-onliness. This class needs to be bound in the
 * Guice configuration for the {@code .ui} project of an Xtext language using
 * {@link ReadonlyIndifferentOverridingGuiceModule}.
 * <p>
 * Note that it relies on non-strict OSGi resolution (through the
 * {@link SuppressWarnings suppressWarnings-restriction} annotation), because of
 * Equinox (OSGi) access restrictions in the {@code org.eclipse.xtext.builder}
 * plug-in, through an {@code x-friends} declaration. Therefore, make sure to
 * <em>not</em> run this with {@code -Dosgi.resolverMode=strict}.
 * 
 * @author Meinte Boersma
 */
@SuppressWarnings("restriction")
public class ReadonlyIndifferentMarkerUpdater extends MarkerUpdaterImpl {

	@Inject
	private IStorage2UriMapper mapper;

	private final Logger log = Logger.getLogger(getClass());

	@Override
	public void updateMarker(ResourceSet resourceSet, ImmutableList<Delta> resourceDescriptionDeltas,
			IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, Messages.MarkerUpdaterImpl_ValidateResources,
				resourceDescriptionDeltas.size());
		subMonitor.subTask(Messages.MarkerUpdaterImpl_ValidateResources);
		for (Delta delta : resourceDescriptionDeltas) {
			if (subMonitor.isCanceled())
				throw new OperationCanceledException();
			if (delta.getNew() != null) {
				Iterable<Pair<IStorage, IProject>> storages = mapper.getStorages(delta.getNew().getURI());
				SubMonitor child = subMonitor.newChild(1);
				child.setWorkRemaining(3);
				for (Pair<IStorage, IProject> pair : storages) {
					child.setWorkRemaining(3);
					if (pair.getFirst() instanceof IFile) {
						IFile file = (IFile) pair.getFirst();
//						if (!file.isReadOnly() && !pair.getSecond().isHidden()) {
						Resource resource = resourceSet.getResource(delta.getNew().getURI(), true);
						addMarkers(file, resource, child.newChild(2));
//						}
					} else {
						child.worked(1);
					}
				}
			} else {
				Iterable<Pair<IStorage, IProject>> storages = mapper.getStorages(delta.getOld().getURI());
				for (Pair<IStorage, IProject> pair : storages) {
					if (pair.getFirst() instanceof IFile) {
						IFile file = (IFile) pair.getFirst();
//						if (!file.isReadOnly() && file.isAccessible()) {
						try {
							file.deleteMarkers(MarkerTypes.FAST_VALIDATION, true, IResource.DEPTH_ZERO);
							file.deleteMarkers(MarkerTypes.NORMAL_VALIDATION, true, IResource.DEPTH_ZERO);
						} catch (CoreException ex) {
							log.error(ex.getMessage(), ex);
						}
//						}
					}
				}
				subMonitor.worked(1);
			}
		}
	}

}
