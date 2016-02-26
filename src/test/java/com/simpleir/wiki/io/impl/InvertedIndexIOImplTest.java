package com.simpleir.wiki.io.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.io.InvertedIndexIO;
import com.simpleir.wiki.io.impl.InvertedIndexIOImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class InvertedIndexIOImplTest
{
	@Autowired
	private InvertedIndexIO invertedIndexIO;

	private static Map<TermIndexPair, String> idxToStringMap = new HashMap<InvertedIndexIOImplTest.TermIndexPair, String>();
	static
	{
		idxToStringMap.put(new TermIndexPair("", Collections.emptyList()), ": ");
		idxToStringMap.put(new TermIndexPair("Term-inator", Arrays.asList(1L)), "Term-inator: 1");
		idxToStringMap.put(new TermIndexPair("", Arrays.asList(1L, 2L, 5L, 123456789L)), ": 1, 2, 5, 123456789");
		idxToStringMap.put(new TermIndexPair("sandwich", Collections.emptyList()), "sandwich: ");
		idxToStringMap.put(new TermIndexPair(":", Arrays.asList(1L, 2L, 5L, 123456789L)), ":: 1, 2, 5, 123456789");
		idxToStringMap.put(new TermIndexPair(":James", Arrays.asList(1L, 2L, 5L, 123456789L)), ":James: 1, 2, 5, 123456789");
		idxToStringMap.put(new TermIndexPair("Anita", Arrays.asList(1L, 2L, 5L, 123456789L, 0L, 92L)), "Anita: 1, 2, 5, 123456789, 0, 92");
	}

	private static Map<String, List<Long>> lineToIdxMap = new HashMap<String, List<Long>>();
	static
	{
		lineToIdxMap.put(": ", Collections.emptyList());
		lineToIdxMap.put(": 119691", Arrays.asList(119691L));
		lineToIdxMap.put("thunder: 119691, 119693, 120042", Arrays.asList(119691L, 119693L, 120042L));
		lineToIdxMap.put("cass: 119691, 119692, 119693, 119694, 119695, 119696, 119697, 119698, 119840, 119842, 120251", Arrays.asList(119691L, 119692L, 119693L, 119694L, 119695L, 119696L, 119697L, 119698L, 119840L, 119842L, 120251L));
		lineToIdxMap.put("talk:article:apple:dog: 123, 456", Arrays.asList(123L, 456L));
		lineToIdxMap.put("talk:article:apple:dog: ", Collections.emptyList());
	}

	private static Map<String, String> lineToTermMap = new HashMap<String, String>();
	static
	{
		lineToTermMap.put("thunder: 119691, 119693, 120042", "thunder");
		lineToTermMap.put(": 119691", "");
		lineToTermMap.put("cass: 119691, 119692, 119693, 119694, 119695, 119696, 119697, 119698, 119840, 119842, 120251", "cass");
		lineToTermMap.put("talk:article:apple:dog: 123, 456", "talk:article:apple:dog");
	}

	@Test
	public void testIdxToString()
	{
		assertTrue(invertedIndexIO instanceof InvertedIndexIOImpl);

		for(TermIndexPair input : idxToStringMap.keySet())
		{
			String output = invertedIndexIO.idxToString(input.term, input.index);
			String expectedOutput = idxToStringMap.get(input);
			assertEquals(output, expectedOutput);
		}
	}

	@Test
	public void testIdxFromString()
	{
		assertTrue(invertedIndexIO instanceof InvertedIndexIOImpl);

		for(String input : lineToIdxMap.keySet())
		{
			List<Long> output = invertedIndexIO.idxFromString(input);
			List<Long> expectedOutput = lineToIdxMap.get(input);

			assertTrue(output.size() == expectedOutput.size());
			for(int i=0; i<output.size(); i++)
			{
				assertTrue(output.get(i).longValue() == expectedOutput.get(i).longValue());
			}
		}
	}

	@Test
	public void testTermFromString()
	{
		assertTrue(invertedIndexIO instanceof InvertedIndexIOImpl);

		for(String input : lineToTermMap.keySet())
		{
			String output = invertedIndexIO.termFromString(input);
			String expectedOutput = lineToTermMap.get(input);
			assertEquals(output, expectedOutput);
		}
	}

	private static final class TermIndexPair
	{
		public final String term;
		public final List<Long> index;

		public TermIndexPair(String term, List<Long> index)
		{
			this.term = term;
			this.index = index;
		}
	}
}