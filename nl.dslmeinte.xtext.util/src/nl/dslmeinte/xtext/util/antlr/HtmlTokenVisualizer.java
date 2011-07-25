package nl.dslmeinte.xtext.util.antlr;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Token;

/**
 * Simple class to visualize the tokenization of a file by an ANTLR
 * {@link Lexer} as HTML.
 * <p>
 * Typical usage looks as follows:
 * <pre>
 * TokenVisualizer visualizer = new TokenVisualizer(lexer, tokenToStyleMapper, "lexing-style.css");
 * visualizer.visualize(input, output, "tokenization visualization");
 * </pre>
 * where {@code lexer} is a {@link Lexer} instance, {@code tokenToStyleMapper}
 * is a {@link TokenToStyleMapper} instance, {@code input} a {@link CharStream}
 * instance and {@link output} an {@link OutputStream} instance.
 * 
 * @author Meinte Boersma
 */
public class HtmlTokenVisualizer {

	/**
	 * Implementations of this interface map {@link Token}s to CSS class names
	 * (i.e., style).
	 * 
	 * @author Meinte Boersma
	 */
	public interface TokenToStyleMapper {

		/**
		 * Returns the CSS class name for the {@link Token} passed.
		 * (Usually involves only {@link Token#getType()}).
		 */
		String cssClassName(Token token);

	}

	private final Lexer lexer;
	private final TokenToStyleMapper mapper;
	private final String cssFilePath;

	/**
	 * @param cssFilePath
	 *            - the path to the CSS file with the styles (i.e., CSS classes)
	 *            as named by the {@link TokenToStyleMapper}, relative to the
	 *            destination location of the output.
	 */
	public HtmlTokenVisualizer(Lexer lexer, TokenToStyleMapper mapper, String cssFilePath) {
		this.lexer = lexer;
		this.mapper = mapper;
		this.cssFilePath = cssFilePath;
	}

	/**
	 * Visualizes the tokenization of the {@code input} {@link CharStream} by
	 * the ANTLR {@link Lexer} passed to the constructor and prints the
	 * resulting HTML to the {@link OutputStream}.
	 * <p>
	 * (The implementation is as statefull/-less as the {@link Lexer} is.)
	 * 
	 * @param outputStream
	 *            - the {@link OutputStream} to print the visualization to.
	 * @param title
	 *            - the title for the HTML page.
	 */
	public void visualize(CharStream input, OutputStream outputStream, String title) {
		PrintWriter output = new PrintWriter(outputStream);

		htmlHeader(title, outputStream);

		output.println("<div class=\"tokenVisualization\">");

		lexer.setCharStream(input);
		Token token = lexer.nextToken();
		while( token.getType() != CharStream.EOF ) {
			output.printf("<span class='%s'>%s</span>", mapper.cssClassName(token), escapeHtml(token.getText()));
			token = lexer.nextToken();
		}

		output.println("</div>");

		htmlFooter(outputStream);

		output.flush();
	}

	private String escapeHtml(String text) {
		text = text.replaceAll("&", "&amp;");
		text = text.replaceAll("<", "&lt;");
		text = text.replaceAll(">", "&gt;");
		text = text.replaceAll("\\n", "&para;<br/>\n");
		int previousSize = text.length();
		text = text.replaceAll("\\n(\\s+)", "\n&nbsp;$1");
		while( text.length() != previousSize ) {
			previousSize = text.length();
		}
		return text;
	}

	private void htmlHeader(String title, OutputStream outputStream) {
		PrintWriter output = new PrintWriter(outputStream);

		output.println("<html>");
		output.println("\t<head>");
		output.print("\t\t<link href=\"");
		output.print(cssFilePath);
		output.println("\" rel=\"stylesheet\" type=\"text/css\" />");
		output.println("\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		output.print("\t\t<title>");
		output.print(title);
		output.println("</title>");
		output.println("\t</head>");
		output.println("<body>");
		output.println();
		output.println("<p>");

		output.flush();
	}

	private void htmlFooter(OutputStream outputStream) {
		PrintWriter output = new PrintWriter(outputStream);

		output.println();
		output.println("</body>");
		output.println("</html>");

		output.flush();
	}

}
