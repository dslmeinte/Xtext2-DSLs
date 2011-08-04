package nl.dslmeinte.xtext.antlr;

import org.antlr.runtime.CharStream;

/**
 * Wraps a {@link CharStream} and gathers some simple statistical data on the
 * invocation of its instance<b>s</b>, meaning that the data is gathered during
 * the whole lifetime of the class as loaded in the current {@link ClassLoader}.
 * <p>
 * Use {@link #reset()} to reset counters to 0.
 * 
 * @author Meinte Boersma
 */
public class StatisticCharStream implements CharStream {

	private final CharStream wrappedCharStream;

	public StatisticCharStream(CharStream wrappedCharStream) {
		this.wrappedCharStream = wrappedCharStream;
	}

	@Override
	public void consume() {
		wrappedCharStream.consume();
	}

	@Override
	public int getCharPositionInLine() {
		return wrappedCharStream.getCharPositionInLine();
	}

	@Override
	public int getLine() {
		return wrappedCharStream.getLine();
	}

	@Override
	public int index() {
		return wrappedCharStream.index();
	}

	private static long lookaheadCalls = 0;
	private static long sumLookaheadCalls = 0;

	public static long lookaheadCalls() {
		return lookaheadCalls;
	}

	public static long sumLookaheadCalls() {
		return sumLookaheadCalls;
	}

	public static void reset() {
		lookaheadCalls = 0;
		sumLookaheadCalls = 0;
	}

	public static String status() {
		return String.format(
				"#lookahead-calls=%d, ∑lookahead=%d, <lookahead>=%f", //$NON-NLS-1$
				lookaheadCalls, sumLookaheadCalls,
				((double) sumLookaheadCalls/lookaheadCalls)
			);
	}

	@Override
	public int LA(int i) {
		lookaheadCalls++;
		sumLookaheadCalls += i;
		return wrappedCharStream.LA(i);
	}

	@Override
	public int LT(int i) {
		lookaheadCalls++;
		sumLookaheadCalls += i;
		return wrappedCharStream.LT(i);
	}

	@Override
	public int mark() {
		return wrappedCharStream.mark();
	}

	@Override
	public void release(int marker) {
		wrappedCharStream.release(marker);
	}

	@Override
	public void rewind() {
		wrappedCharStream.rewind();
	}

	@Override
	public void rewind(int marker) {
		wrappedCharStream.rewind(marker);
	}

	@Override
	public void seek(int index) {
		wrappedCharStream.seek(index);
	}

	@Override
	public void setCharPositionInLine(int pos) {
		wrappedCharStream.setCharPositionInLine(pos);
	}

	@Override
	public void setLine(int line) {
		wrappedCharStream.setLine(line);
	}

	@Override
	public int size() {
		return wrappedCharStream.size();
	}

	@Override
	public String substring(int start, int stop) {
		return wrappedCharStream.substring(start, stop);
	}

	@Override
	public String getSourceName() {
		return wrappedCharStream.getSourceName();
	}

}
