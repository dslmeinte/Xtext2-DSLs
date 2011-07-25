package nl.dslmeinte.xtext.util.antlr.trie.test;

import nl.dslmeinte.xtext.util.antlr.trie.CaseInsensitiveTrie;
import nl.dslmeinte.xtext.util.antlr.trie.EnumBasedTrie;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.junit.Assert;
import org.junit.Test;

public class EnumBasedTrieTest {

	private enum SimpleKeywords {
		a, aa;
	}

	@Test
	public void test_all_simple_keywords() {
		Assert.assertEquals(SimpleKeywords.a, match("a"));
		Assert.assertEquals(SimpleKeywords.aa, match("aa"));
	}

	@Test
	public void test_no_match() {
		Assert.assertEquals(null, match("b"));
	}

	@Test
	public void test_no_match_against_EOF() {
		Assert.assertEquals(null, match(""));
	}

	@Test
	public void test_longer_input() {
		Assert.assertEquals(SimpleKeywords.a, match("ax"));
		Assert.assertEquals(SimpleKeywords.aa, match("aaX"));
	}

	@Test
	public void test_antlr_CharStream_consumption() {
		ANTLRStringStream input = new ANTLRStringStream("aax");
		Assert.assertEquals(SimpleKeywords.aa, trie.match(input));
		Assert.assertEquals('x', input.LA(1));
	}

	@Test
	public void test_repeated_match() {
		ANTLRStringStream input = new ANTLRStringStream("aaaaa");
		Assert.assertEquals(SimpleKeywords.aa, trie.match(input));
		Assert.assertEquals(SimpleKeywords.aa, trie.match(input));
		Assert.assertEquals(SimpleKeywords.a, trie.match(input));
		Assert.assertEquals(CharStream.EOF, input.LA(1));
	}

	private final CaseInsensitiveTrie<SimpleKeywords> trie = EnumBasedTrie.of(SimpleKeywords.class);

	private SimpleKeywords match(String input) {
		return trie.match(new ANTLRStringStream(input));
	}

}
