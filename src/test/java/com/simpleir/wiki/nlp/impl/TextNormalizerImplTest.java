package com.simpleir.wiki.nlp.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.nlp.TextNormalizer;
import com.simpleir.wiki.nlp.impl.TextNormalizerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class TextNormalizerImplTest
{
	@Autowired
	private TextNormalizer textNormalizer;

	private static Set<String> longWords = new HashSet<String>(Arrays.asList("1234567890123456789012345678901", "abcdefghijklmnopqrstuvwxyz1234567890", "supercalifragilisticexpialidocious", "..supercalifragilisticexpialidocious.."));
	private static Set<String> wordsWithTooManyNonwordCharacters = new HashSet<String>(Arrays.asList("one-in-a-million-ish", "$$$$$a.b.c.d.e.f$$$$$"));
	private static Set<String> stopWords = new HashSet<String>(Arrays.asList("a", "an", "the", "of", "on", "in", "out"));

	private static Map<String, String> wordsThatArelongsMap = new HashMap<String, String>();
	static
	{
		wordsThatArelongsMap.put("123", "123");
		wordsThatArelongsMap.put("0", "0");
		wordsThatArelongsMap.put("-234", "-234");
		wordsThatArelongsMap.put("123456789", null);
	}

	private static Map<String, String> wordsThatAreDoublesMap = new HashMap<String, String>();
	static
	{
		wordsThatAreDoublesMap.put("0.002", "0.002");
		wordsThatAreDoublesMap.put("-0.002", "-0.002");
		wordsThatAreDoublesMap.put("0.0029", "0.003");
		wordsThatAreDoublesMap.put("-0.0029", "-0.003");

		wordsThatAreDoublesMap.put("0.02", "0.020");
		wordsThatAreDoublesMap.put("-0.02", "-0.020");
		wordsThatAreDoublesMap.put("0.025", "0.025");
		wordsThatAreDoublesMap.put("-0.025", "-0.025");
		wordsThatAreDoublesMap.put("0.0256", "0.026");
		wordsThatAreDoublesMap.put("-0.0256", "-0.026");

		wordsThatAreDoublesMap.put("0.1", "0.100");
		wordsThatAreDoublesMap.put("-0.1", "-0.100");
		wordsThatAreDoublesMap.put("0.12", "0.120");
		wordsThatAreDoublesMap.put("-0.12", "-0.120");
		wordsThatAreDoublesMap.put("0.123", "0.123");
		wordsThatAreDoublesMap.put("-0.123", "-0.123");
		wordsThatAreDoublesMap.put("0.1234", "0.123");
		wordsThatAreDoublesMap.put("-0.1234", "-0.123");

		wordsThatAreDoublesMap.put("9.1", "9.100");
		wordsThatAreDoublesMap.put("-9.1", "-9.100");
		wordsThatAreDoublesMap.put("5.56", "5.560");
		wordsThatAreDoublesMap.put("-5.56", "-5.560");
		wordsThatAreDoublesMap.put("5.561", "5.561");
		wordsThatAreDoublesMap.put("-5.561", "-5.561");
		wordsThatAreDoublesMap.put("5.5612", "5.561");
		wordsThatAreDoublesMap.put("-5.5612", "-5.561");

		wordsThatAreDoublesMap.put("55.6", "55.60");
		wordsThatAreDoublesMap.put("-55.6", "-55.60");
		wordsThatAreDoublesMap.put("55.67", "55.67");
		wordsThatAreDoublesMap.put("-55.67", "-55.67");
		wordsThatAreDoublesMap.put("55.678", "55.68");
		wordsThatAreDoublesMap.put("-55.678", "-55.68");

		wordsThatAreDoublesMap.put("556.7", "556.7");
		wordsThatAreDoublesMap.put("-556.7", "-556.7");
		wordsThatAreDoublesMap.put("556.78", "556.8");
		wordsThatAreDoublesMap.put("-556.78", "-556.8");

		wordsThatAreDoublesMap.put("1000.0", null);
		wordsThatAreDoublesMap.put("-1000.0", null);
	}

	@Test
	public void testNormalizeWordLongWords()
	{
		assertTrue(textNormalizer instanceof TextNormalizerImpl);

		for(String longWord : longWords)
		{
			assertTrue(textNormalizer.normalizeWord(longWord) == null);
		}
	}

	@Test
	public void testNormalizeWordNonwordCharacters()
	{
		assertTrue(textNormalizer instanceof TextNormalizerImpl);

		for(String wordWithTooManyNonwordCharacters : wordsWithTooManyNonwordCharacters)
		{
			assertTrue(textNormalizer.normalizeWord(wordWithTooManyNonwordCharacters) == null);
		}
	}

	@Test
	public void testNormalizeWordStopWords()
	{
		assertTrue(textNormalizer instanceof TextNormalizerImpl);

		for(String stopWord : stopWords)
		{
			assertTrue(textNormalizer.normalizeWord(stopWord) == null);
		}
	}

	@Test
	public void testNormalizeWordLongs()
	{
		assertTrue(textNormalizer instanceof TextNormalizerImpl);

		for(String wordThatIsALong : wordsThatArelongsMap.keySet())
		{
			String output = textNormalizer.normalizeWord(wordThatIsALong);
			String expectedOutput = wordsThatArelongsMap.get(wordThatIsALong);
			assertEquals(output, expectedOutput);
		}
	}

	@Test
	public void testNormalizeWordDoubles()
	{
		assertTrue(textNormalizer instanceof TextNormalizerImpl);

		for(String wordThatIsADouble : wordsThatAreDoublesMap.keySet())
		{
			String output = textNormalizer.normalizeWord(wordThatIsADouble);
			String expectedOutput = wordsThatAreDoublesMap.get(wordThatIsADouble);
			assertEquals(output, expectedOutput);
		}
	}

	
}