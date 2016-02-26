package com.simpleir.wiki.io.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.io.PositionalIndexIO;
import com.simpleir.wiki.io.impl.PositionalIndexIOImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class PositionalIndexIOImplTest
{
	@Autowired
	private PositionalIndexIO positionalIndexIO;

	private static Map<TermIndexPair, String> idxToStringMap = new HashMap<PositionalIndexIOImplTest.TermIndexPair, String>();
	static
	{
		Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
		idxToStringMap.put(new TermIndexPair("", map), ": ");

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		idxToStringMap.put(new TermIndexPair("", map), ": 45442609 - 56, 424; ");

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		idxToStringMap.put(new TermIndexPair(":", map), ":: 45442609 - 56, 424; ");

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		idxToStringMap.put(new TermIndexPair("Jerry", map), "Jerry: 45442609 - 56, 424; ");

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		idxToStringMap.put(new TermIndexPair("talk:article:apple:dog", map), "talk:article:apple:dog: 45442609 - 56, 424; ");

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		map.put(45444174L, Arrays.asList(67L));
		idxToStringMap.put(new TermIndexPair("talk:article:apple:dog", map), "talk:article:apple:dog: 45442609 - 56, 424; 45444174 - 67; ");
	}

	private static Map<String, String> lineToTermMap = new HashMap<String, String>();
	static
	{
		lineToTermMap.put(": ", "");
		lineToTermMap.put(": 45442609 - 56, 424;", "");
		lineToTermMap.put(":: 45442609 - 56, 424;", ":");
		lineToTermMap.put("talk:article:apple:dog: 123, 456", "talk:article:apple:dog");
		lineToTermMap.put("rousei: 45442609 - 56, 424;", "rousei");
		lineToTermMap.put("rousei: 45442609 - 56, 424; 45444174 - 67;", "rousei");
	}

	private static Map<String, Map<Long, List<Long>>> lineToIdxMap = new HashMap<String, Map<Long,List<Long>>>();
	static
	{
		Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
		lineToIdxMap.put(": ", map);

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		lineToIdxMap.put(": 45442609 - 56, 424;", map);

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		lineToIdxMap.put(":: 45442609 - 56, 424;", map);

		map = new HashMap<Long, List<Long>>();
		map.put(924L, Arrays.asList(123L, 456L));
		lineToIdxMap.put("talk:article:apple:dog: 924 - 123, 456;", map);

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		lineToIdxMap.put("rousei: 45442609 - 56, 424;", map);

		map = new HashMap<Long, List<Long>>();
		map.put(45442609L, Arrays.asList(56L, 424L));
		map.put(45444174L, Arrays.asList(67L));
		lineToIdxMap.put("rousei: 45442609 - 56, 424; 45444174 - 67;", map);
	}
	

	@Test
	public void testIdxToString()
	{
		assertTrue(positionalIndexIO instanceof PositionalIndexIOImpl);

		for(TermIndexPair pair : idxToStringMap.keySet())
		{
			String output = positionalIndexIO.idxToString(pair.term, pair.index);
			String expectedOutput = idxToStringMap.get(pair);
			assertEquals(output, expectedOutput);
		}
	}

	@Test
	public void testIdxFromString()
	{
		assertTrue(positionalIndexIO instanceof PositionalIndexIOImpl);

		for(String input : lineToIdxMap.keySet())
		{
			Map<Long, List<Long>> output = positionalIndexIO.idxFromString(input);
			Map<Long, List<Long>> expectedOutput = lineToIdxMap.get(input);

			assertTrue(output.size() == expectedOutput.size());
			for(Long docId : output.keySet())
			{
				assertTrue(expectedOutput.containsKey(docId));
				List<Long> indices = output.get(docId);
				List<Long> expectedIndices = expectedOutput.get(docId);

				assertTrue(indices.size() == expectedIndices.size());
				for(int i=0; i<indices.size(); i++)
				{
					assertEquals(indices.get(i), expectedIndices.get(i));
				}
			}
		}
	}

	@Test
	public void testTermFromString()
	{
		assertTrue(positionalIndexIO instanceof PositionalIndexIOImpl);

		for(String input : lineToTermMap.keySet())
		{
			String output = positionalIndexIO.termFromString(input);
			String expectedOutput = lineToTermMap.get(input);
			assertEquals(output, expectedOutput);
		}
	}

	private static final class TermIndexPair
	{
		public final String term;
		public final Map<Long, List<Long>> index;

		public TermIndexPair(String term, Map<Long, List<Long>> index)
		{
			this.term = term;
			this.index = index;
		}
	}
}