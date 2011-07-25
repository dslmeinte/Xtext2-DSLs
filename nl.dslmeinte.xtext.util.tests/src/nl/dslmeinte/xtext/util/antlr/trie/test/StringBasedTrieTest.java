package nl.dslmeinte.xtext.util.antlr.trie.test;

import java.util.Arrays;
import java.util.HashSet;

import nl.dslmeinte.xtext.util.antlr.trie.CaseInsensitiveTrie;
import nl.dslmeinte.xtext.util.antlr.trie.StringBasedTrie;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.junit.Assert;
import org.junit.Test;

public class StringBasedTrieTest {

	@Test
	public void test_all_simple_keywords() {
		Assert.assertEquals("a", match("a"));
		Assert.assertEquals("aa", match("aa"));
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
		Assert.assertEquals("a", match("ax"));
		Assert.assertEquals("aa", match("aaX"));
	}

	@Test
	public void test_antlr_CharStream_consumption() {
		ANTLRStringStream input = new ANTLRStringStream("aax");
		Assert.assertEquals("aa", trie.match(input));
		Assert.assertEquals('x', input.LA(1));
	}

	@Test
	public void test_repeated_match() {
		ANTLRStringStream input = new ANTLRStringStream("aaaaa");
		Assert.assertEquals("aa", trie.match(input));
		Assert.assertEquals("aa", trie.match(input));
		Assert.assertEquals("a", trie.match(input));
		Assert.assertEquals(CharStream.EOF, input.LA(1));
	}

	private final CaseInsensitiveTrie<String> trie = StringBasedTrie.of(new HashSet<String>(Arrays.asList("a", "aa")));

	private String match(String input) {
		return trie.match(new ANTLRStringStream(input));
	}

}
