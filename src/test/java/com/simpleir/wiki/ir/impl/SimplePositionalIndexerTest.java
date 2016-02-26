package com.simpleir.wiki.ir.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import com.simpleir.wiki.ir.PositionalIndexer;
import com.simpleir.wiki.ir.impl.SimplePositionalIndexer;
import com.simpleir.wiki.model.Article;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class SimplePositionalIndexerTest
{
	@Autowired
	private PositionalIndexer positionalIndexer;

	//Quotes from Samuel Clemens (Mark Twain), from https://www.goodreads.com/author/quotes/1244.Mark_Twain , accessed 2016 February 22
	private static List<Article> articleList = Arrays.asList(
			new Article("Article a", 1L, "If you tell the truth, you don't have to remember anything."),
			new Article("Article b", 2L, "Good friends, good books, and a sleepy conscience: this is the ideal life."),
			new Article("Article c", 3L, "Never tell the truth to people who are not worthy of it."),
			new Article("Article d", 4L, "The man who does not read has no advantage over the man who cannot read."),
			new Article("Article e", 5L, "I have never let my schooling interfere with my education."));

	private static Map<String, Map<Long, List<Long>>> expectedOutput = new HashMap<String, Map<Long,List<Long>>>();

	static
	{
		expectedOutput.put("advantag", new HashMap<Long, List<Long>>());
		expectedOutput.put("anyth", new HashMap<Long, List<Long>>());
		expectedOutput.put("book", new HashMap<Long, List<Long>>());
		expectedOutput.put("conscienc", new HashMap<Long, List<Long>>());
		expectedOutput.put("educ", new HashMap<Long, List<Long>>());
		expectedOutput.put("friend", new HashMap<Long, List<Long>>());
		expectedOutput.put("good", new HashMap<Long, List<Long>>());
		expectedOutput.put("ideal", new HashMap<Long, List<Long>>());
		expectedOutput.put("interfer", new HashMap<Long, List<Long>>());
		expectedOutput.put("let", new HashMap<Long, List<Long>>());
		expectedOutput.put("life", new HashMap<Long, List<Long>>());
		expectedOutput.put("man", new HashMap<Long, List<Long>>());
		expectedOutput.put("never", new HashMap<Long, List<Long>>());
		expectedOutput.put("peopl", new HashMap<Long, List<Long>>());
		expectedOutput.put("read", new HashMap<Long, List<Long>>());
		expectedOutput.put("rememb", new HashMap<Long, List<Long>>());
		expectedOutput.put("school", new HashMap<Long, List<Long>>());
		expectedOutput.put("sleepi", new HashMap<Long, List<Long>>());
		expectedOutput.put("tell", new HashMap<Long, List<Long>>());
		expectedOutput.put("truth", new HashMap<Long, List<Long>>());
		expectedOutput.put("worthi", new HashMap<Long, List<Long>>());

		expectedOutput.get("advantag").put(4L, Arrays.asList(2L));
		expectedOutput.get("anyth").put(1L, Arrays.asList(3L));
		expectedOutput.get("book").put(2L, Arrays.asList(3L));
		expectedOutput.get("conscienc").put(2L, Arrays.asList(5L));
		expectedOutput.get("educ").put(5L, Arrays.asList(4L));
		expectedOutput.get("friend").put(2L, Arrays.asList(1L));
		expectedOutput.get("good").put(2L, Arrays.asList(0L, 2L));
		expectedOutput.get("ideal").put(2L, Arrays.asList(6L));
		expectedOutput.get("interfer").put(5L, Arrays.asList(3L));
		expectedOutput.get("let").put(5L, Arrays.asList(1L));
		expectedOutput.get("life").put(2L, Arrays.asList(7L));
		expectedOutput.get("man").put(4L, Arrays.asList(0L, 3L));
		expectedOutput.get("never").put(3L, Arrays.asList(0L));
		expectedOutput.get("never").put(5L, Arrays.asList(0L));
		expectedOutput.get("peopl").put(3L, Arrays.asList(3L));
		expectedOutput.get("read").put(4L, Arrays.asList(1L, 4L));
		expectedOutput.get("rememb").put(1L, Arrays.asList(2L));
		expectedOutput.get("school").put(5L, Arrays.asList(2L));
		expectedOutput.get("sleepi").put(2L, Arrays.asList(4L));
		expectedOutput.get("tell").put(1L, Arrays.asList(0L));
		expectedOutput.get("tell").put(3L, Arrays.asList(1L));
		expectedOutput.get("truth").put(1L, Arrays.asList(1L));
		expectedOutput.get("truth").put(3L, Arrays.asList(2L));
		expectedOutput.get("worthi").put(3L, Arrays.asList(4L));
	}

	@Test
	public void testExtractPositionalIndexFromArticles()
	{
		assertTrue(positionalIndexer instanceof SimplePositionalIndexer);

		SimplePositionalIndexer impl = (SimplePositionalIndexer) positionalIndexer;

		Map<String, Map<Long, List<Long>>> positionalIndex = impl.extractPositionalIndexFromArticles(articleList.iterator());

		assertTrue(expectedOutput.size() == positionalIndex.size());

		for(String key : expectedOutput.keySet())
		{
			assertTrue(positionalIndex.containsKey(key));

			Map<Long, List<Long>> expectedPositions = expectedOutput.get(key);
			Map<Long, List<Long>> retrievedPositions = positionalIndex.get(key);

			assertTrue(expectedPositions.size() == retrievedPositions.size());
			for(Long subKey : expectedPositions.keySet())
			{
				assertTrue(retrievedPositions.containsKey(subKey));
				List<Long> expectedList  = expectedPositions .get(subKey);
				List<Long> retrievedList = retrievedPositions.get(subKey);

				assertTrue(expectedList.size() == retrievedList.size());

				List<Long> sortedExpected = deepCopyAndSort(expectedList);
				List<Long> sortedRetrieved = deepCopyAndSort(retrievedList);
				for(int i=0; i<sortedExpected.size(); i++)
				{
					assertTrue(sortedExpected.get(i) == sortedRetrieved.get(i));
				}
			}
		}
	}

	private static List<Long> deepCopyAndSort(List<Long> list)
	{
		List<Long> copyList = new ArrayList<Long>(list.size()+1);
		for(Long l : list)
		{
			copyList.add(l);
		}

		Collections.sort(copyList);

		return copyList;
	}
}