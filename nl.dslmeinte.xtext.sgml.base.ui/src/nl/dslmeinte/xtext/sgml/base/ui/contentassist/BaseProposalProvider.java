package nl.dslmeinte.xtext.sgml.base.ui.contentassist;

import nl.dslmeinte.xtext.sgml.base.base.BasePackage;
import nl.dslmeinte.xtext.sgml.base.base.Entity;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

/**
 * Implementation/customization of content assist.
 */
@SuppressWarnings("nls")	// (only "technical" String-s)
public class BaseProposalProvider extends AbstractBaseProposalProvider {

	/**
	 * Provides content assist for entity references, by directly creating the
	 * proposals from what the scope provider returns and taking care to close
	 * the proposal text (replacement string) with the semi-colon ({@code ';'})
	 * required for valid entity references.
	 * <p>
	 * Note that
	 * {@link #complete_PCDATAEntityReference_Entity(EObject, Assignment, ContentAssistContext, ICompletionProposalAcceptor)}
	 * is never called because of the way the grammar is (necessarily) set up.
	 */
	@Override
	public void complete__PCDATAEntityReference(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		proposeEntities(context, acceptor);
	}

    protected void proposeEntities(ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		IScope scope = getScopeProvider().getScope(context.getCurrentModel(), BasePackage.Literals._PCDATA_ENTITY_REFERENCE__ENTITY);
		Iterable<IEObjectDescription> candidates = scope.getAllElements();
		for( IEObjectDescription candidate: candidates ) {
			if( !acceptor.canAcceptMoreProposals() ) {
				return;
			}
			// copied and adapted from AbstractJavaBasedContentProposalProvider.DefaultProposalCreator#apply:
			String name = candidate.getName().toString();
			EObject objectOrProxy = candidate.getEObjectOrProxy();
			StyledString displayString = getStyledDisplayString(objectOrProxy, candidate.getQualifiedName().toString(),name);
			Image image = getImage(objectOrProxy);

			String oldPrefix = context.getPrefix();
			ContentAssistContext newContext = changePrefix(context, "&");
			ICompletionProposal result = createCompletionProposal("&" + name + ";", displayString, image, newContext);
			// correct replacement offset so it doesn't start right at the beginning of the preceding token:
			((ConfigurableCompletionProposal) result).setReplacementOffset(context.getOffset() - ( oldPrefix.endsWith("&") ? 1 : 0 ) );
			// getPriorityHelper().adjustCrossReferencePriority(result, context.getPrefix());
			//		(don't bump priority, as we could have many entity references and they're not that important)

			acceptor.accept(result);
		}
    }

    /**
	 * Changes the prefix of the given {@link ContentAssistContext}.
	 */
    protected ContentAssistContext changePrefix(ContentAssistContext context, String prefix) {
    	return context.copy().setPrefix(prefix).toContext();
    }

	/**
	 * Provides tag completion for a {@link Entity}.
	 */
	@Override
    public void complete_Entity(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    	super.complete_Entity(model, ruleCall, context, acceptor);
    	proposeEntity(model, context, acceptor);
    }

    /**
     * Provides tag completion for a {@link Entity} which is already partially started.
     */
    @Override
    public void completeEntity_Path(EObject model, Assignment assignment,
    		ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
    	// TODO  document scenario!
    	INode previousSibling = context.getLastCompleteNode().getPreviousSibling();
    	if( previousSibling != null ) {
    		String prefix = previousSibling.getText() + ((Entity) model).getName();
    		if( "!ENTITY".startsWith(prefix) ) {
    	    	proposeEntity(model, context, acceptor);
    		}
    	}
    	super.completeEntity_Path(model, assignment, context, acceptor);
    }

	/**
	 * Proposes the replacement "{@code <!ENTITY SYSTEM "">}" as "{@code !ENTITY}
	 * " and places the cursor one space after the {@code !ENTITY} keyword.
	 */
	private void proposeEntity(EObject model, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		ICompletionProposal completionProposal = createCompletionProposal("<!ENTITY  SYSTEM \"\">", new StyledString("!ENTITY"), getImage(model), getPriorityHelper().getDefaultPriority(), context.getPrefix(), context);
		((ConfigurableCompletionProposal) completionProposal).setCursorPosition("<!ENTITY ".length());
		acceptor.accept(completionProposal);
	}

}
