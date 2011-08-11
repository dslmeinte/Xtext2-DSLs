package nl.dslmeinte.xtext.contentassist;

import nl.dslmeinte.xtext.grammar.XtextGrammarPrinter;

import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider;

/**
 * Util class to help with the trickier parts of implementing content assist.
 * Use this in your own implementation of {@link IContentProposalProvider}.
 * 
 * @author Meinte Boersma
 */
public class ProposalProviderUtil {

	/**
	 * Creates a message detailing the most important information in the
	 * {@link ContentAssistContext} object pertaining to proposal
	 * provision/content assist.
	 */
	public static String info(ContentAssistContext context) {
		StringBuilder builder =
			new StringBuilder()
				.append("prefix=\"") // $NON-NLS-1$
				.append(context.getPrefix())
				.append("\" -- last complete node: "); // $NON-NLS-1$
		addNodeDetails(context.getLastCompleteNode(), builder);
		builder.append(" -- current node: "); // $NON-NLS-1$
		addNodeDetails(context.getCurrentNode(), builder);
		builder.append(" -- FIRST: ["); // $NON-NLS-1$
		boolean comma = false;
		for( AbstractElement element : context.getFirstSetGrammarElements() ) {
			if( comma ) {
				builder.append(", "); // $NON-NLS-1$
			}
			builder
				.append("<") // $NON-NLS-1$
				.append(XtextGrammarPrinter.toString(element))
				.append(">"); // $NON-NLS-1$
			comma = true;
		}
		builder.append("]"); // $NON-NLS-1$
		return builder.toString();
	}

    private static void addNodeDetails(INode node, StringBuilder builder) {
    	builder
    		.append("\"")
    		.append(node.getText())
    		.append("\" <")
    		.append(XtextGrammarPrinter.toString(node.getGrammarElement()))
    		.append(">");
    }

}
