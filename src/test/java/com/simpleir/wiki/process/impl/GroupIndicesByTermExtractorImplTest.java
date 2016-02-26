package com.simpleir.wiki.process.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.process.GroupIndicesByTermExtractor;
import com.simpleir.wiki.process.impl.GroupIndicesByTermExtractorImpl;
import com.simpleir.wiki.process.impl.GroupIndicesByTermExtractorImpl.IOMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class GroupIndicesByTermExtractorImplTest
{
	private static String newline = String.format("%n");

	@Autowired
	private GroupIndicesByTermExtractor groupIndicesByTermExtractor;

	private static List<List<String>> inverseIndexlines = new LinkedList<List<String>>();
	static
	{
		inverseIndexlines.add(Arrays.asList(
				"2015: 1, 2, 3",
				"escobar: 2",
				"film: 4, 6"));
		inverseIndexlines.add(Arrays.asList(
				"2015: 7",
				"escobar: 8, 9",
				"furiou: 21"));
	}

	private static Map<String, String> expectedInverseIndexOutput = new HashMap<String, String>();
	static
	{
		expectedInverseIndexOutput.put("2015", "2015: 1, 2, 3" + newline + "2015: 7");
		expectedInverseIndexOutput.put("escobar", "escobar: 2" + newline + "escobar: 8, 9");
		expectedInverseIndexOutput.put("film", "film: 4, 6");
		expectedInverseIndexOutput.put("furiou", "furiou: 21");
	}

	private static List<List<String>> positionalIndexlines = new LinkedList<List<String>>();
	static
	{
		positionalIndexlines.add(Arrays.asList(
				"2015: 1 - 1, 4; 2 - 2; 3 - 1; ",
				"escobar: 2 - 5; ",
				"film: 3 - 29; 6 - 1; "));
		positionalIndexlines.add(Arrays.asList(
				"2015: 7 - 1; ",
				"escobar: 9 - 15; 10 - 1, 2, 14, 16, 19, 21; ",
				"furiou: 7 - 2; "));
	}

	private static Map<String, String> expectedPositionalIndexOutput = new HashMap<String, String>();
	static
	{
		expectedPositionalIndexOutput.put("2015", "2015: 1 - 1, 4; 2 - 2; 3 - 1; " + newline + "2015: 7 - 1; ");
		expectedPositionalIndexOutput.put("escobar", "escobar: 2 - 5; " + newline + "escobar: 9 - 15; 10 - 1, 2, 14, 16, 19, 21; ");
		expectedPositionalIndexOutput.put("film", "film: 3 - 29; 6 - 1; ");
		expectedPositionalIndexOutput.put("furiou", "furiou: 7 - 2; ");
	}
	

	@Test
	public void testGroupLinesInFileInverseIndices() throws IOException
	{
		HashMapIOMap ioMap = new HashMapIOMap();

		GroupIndicesByTermExtractorImpl impl = (GroupIndicesByTermExtractorImpl) groupIndicesByTermExtractor;

		for(Iterator<List<String>> fakeFileIter = inverseIndexlines.iterator(); fakeFileIter.hasNext(); )
		{
			List<String> lines = fakeFileIter.next();
			impl.groupLinesInFile(lines.iterator(), ioMap);
		}

		for(String input : expectedInverseIndexOutput.keySet())
		{
			assertTrue(ioMap.hasKey(input));
			String output = ioMap.get(input);
			String expectedOutput = expectedInverseIndexOutput.get(input);
			assertEquals(output.trim(), expectedOutput.trim());
		}
	}

	@Test
	public void testGroupLinesInFilePositionalIndices() throws IOException
	{
		HashMapIOMap ioMap = new HashMapIOMap();

		GroupIndicesByTermExtractorImpl impl = (GroupIndicesByTermExtractorImpl) groupIndicesByTermExtractor;

		for(Iterator<List<String>> fakeFileIter = positionalIndexlines.iterator(); fakeFileIter.hasNext(); )
		{
			List<String> lines = fakeFileIter.next();
			impl.groupLinesInFile(lines.iterator(), ioMap);
		}

		for(String input : expectedPositionalIndexOutput.keySet())
		{
			assertTrue(ioMap.hasKey(input));
			String output = ioMap.get(input);
			String expectedOutput = expectedPositionalIndexOutput.get(input);
			assertEquals(output.trim(), expectedOutput.trim());
		}
	}
	

	private static class HashMapIOMap implements IOMap
	{
		private Map<String, String> map = new HashMap<String, String>();

		@Override
		public void write(String line, String resourceName)
		{
			if(!map.containsKey(resourceName))
			{
				map.put(resourceName, "");
			}

			map.put(resourceName, map.get(resourceName) + line + newline);
		}

		public boolean hasKey(String key)
		{
			return map.containsKey(key);
		}

		public String get(String key)
		{
			return map.get(key);
		}
	}
}