package com.simpleir.wiki.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.simpleir.wiki.utils.StringUtils;

import static org.junit.Assert.*;
import static com.simpleir.wiki.utils.StringUtils.isW;
import static com.simpleir.wiki.utils.StringUtils.replaceNonWCharsWithUnderscores;

public class StringUtilsTest
{
	private static final Set<Character> allWs = new HashSet<Character>(Arrays.asList(
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

	private static final Map<String, String> replacements = new HashMap<String, String>();
	static
	{
		replacements.put("And", "And");
		replacements.put("!", "_");
		replacements.put("_", "_");
		replacements.put("Jim@google.com", "Jim_google_com");
		replacements.put("", "");
		replacements.put("................", "________________");
		replacements.put(".this.", "_this_");
		replacements.put("\\\\\\\\\\", "_____");
	}

	@Test
	public void testIsW()
	{
		for(char c : allWs)
		{
			assertTrue(isW(c));
		}
		for(int i=0; i<65535; i++)
		{
			char c = (char) i;
			if(!allWs.contains(c))
			{
				assertFalse(isW(c));
			}
		}
	}

	@Test
	public void testTrimNonWCharsFromText()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("-", "");
		expectedInputOutputMap.put("-***", "");
		expectedInputOutputMap.put("-5", "5");
		expectedInputOutputMap.put("otherwise,", "otherwise");
		expectedInputOutputMap.put("none-too-smart", "none-too-smart");
		expectedInputOutputMap.put("none-too-smart,", "none-too-smart");
		expectedInputOutputMap.put(":701-777-7777.", "701-777-7777");
		expectedInputOutputMap.put(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,701-777-7777.", "701-777-7777");

		for(String input : expectedInputOutputMap.keySet())
		{
			String expectedOutput = expectedInputOutputMap.get(input);
			String output = StringUtils.trimNonWCharsFromText(input);
			assertEquals(expectedOutput, output);
		}
	}

	@Test
	public void testReplaceNonWCharsWithUnderscores()
	{
		for(String original : replacements.keySet())
		{
			assertEquals(replaceNonWCharsWithUnderscores(original), replacements.get(original));
		}
	}

	@Test
	public void testCollapseWhitespace()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("   ", "");
		expectedInputOutputMap.put("\t5\t", " 5 ");
		expectedInputOutputMap.put("I don't think that's a good idea", "I don't think that's a good idea");
		expectedInputOutputMap.put("I don't             \t                think that's a good idea", "I don't think that's a good idea");
		expectedInputOutputMap.put("Did you hear the one about the clown?\n\n\n\nNo, you didn't", "Did you hear the one about the clown?\nNo, you didn't");

		for(String input : expectedInputOutputMap.keySet())
		{
			String expectedOutput = expectedInputOutputMap.get(input);
			String output = StringUtils.collapseWhitespace(input);
			assertEquals(expectedOutput, output);
		}
	}

	@Test
	public void testRemovePairedSymbolDelineatedSections()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("{}", " ");
		expectedInputOutputMap.put("I am {not} great", "I am   great");
		expectedInputOutputMap.put("I am }not{ great", "I am }not{ great");
		expectedInputOutputMap.put("I {am {not}} great", "I    great");
		
		for(String input : expectedInputOutputMap.keySet())
		{
			String expectedOutput = expectedInputOutputMap.get(input);
			String output = StringUtils.removePairedSymbolDelineatedSections(input, '{', '}');
			assertEquals(expectedOutput, output);
			
		}
	}
}