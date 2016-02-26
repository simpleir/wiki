package com.simpleir.wiki.ir.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.ir.InvertedIndexer;
import com.simpleir.wiki.ir.impl.SimpleInvertedIndexer;
import com.simpleir.wiki.model.Article;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class SimpleInvertedIndexerTest
{
	@Autowired
	private InvertedIndexer invertedIndexer;

	//Quotes from Samuel Clemens (Mark Twain), from https://www.goodreads.com/author/quotes/1244.Mark_Twain , accessed 2016 February 22
	private static List<Article> articleList = Arrays.asList(
			new Article("Article a", 1L, "If you tell the truth, you don't have to remember anything."),
			new Article("Article b", 2L, "Good friends, good books, and a sleepy conscience: this is the ideal life."),
			new Article("Article c", 3L, "Never tell the truth to people who are not worthy of it."),
			new Article("Article d", 4L, "The man who does not read has no advantage over the man who cannot read."),
			new Article("Article e", 5L, "I have never let my schooling interfere with my education."));

	private static Map<String, List<Long>> expectedOutput = new HashMap<String, List<Long>>();

	static
	{
		expectedOutput.put("advantag", Arrays.asList(4L));
		expectedOutput.put("anyth", Arrays.asList(1L));
		expectedOutput.put("book", Arrays.asList(2L));
		expectedOutput.put("conscienc", Arrays.asList(2L));
		expectedOutput.put("educ", Arrays.asList(5L));
		expectedOutput.put("friend", Arrays.asList(2L));
		expectedOutput.put("good", Arrays.asList(2L));
		expectedOutput.put("ideal", Arrays.asList(2L));
		expectedOutput.put("interfer", Arrays.asList(5L));
		expectedOutput.put("let", Arrays.asList(5L));
		expectedOutput.put("life", Arrays.asList(2L));
		expectedOutput.put("man", Arrays.asList(4L));
		expectedOutput.put("never", Arrays.asList(3L, 5L));
		expectedOutput.put("peopl", Arrays.asList(3L));
		expectedOutput.put("read", Arrays.asList(4L));
		expectedOutput.put("rememb", Arrays.asList(1L));
		expectedOutput.put("school", Arrays.asList(5L));
		expectedOutput.put("sleepi", Arrays.asList(2L));
		expectedOutput.put("tell", Arrays.asList(1L, 3L));
		expectedOutput.put("truth", Arrays.asList(1L, 3L));
		expectedOutput.put("worthi", Arrays.asList(3L));
	}

	@Test
	public void testExtractInvertedIndexFromArticles() throws IOException
	{
		assertTrue(invertedIndexer instanceof SimpleInvertedIndexer);
		SimpleInvertedIndexer impl = (SimpleInvertedIndexer) invertedIndexer;

		Iterator<Article> articleIter = articleList.iterator();
		Map<String, List<Long>> invertedIndex = impl.extractInvertedIndexFromArticles(articleIter);

		assertTrue(expectedOutput.size() == invertedIndex.size());

		for(String key : expectedOutput.keySet())
		{
			assertTrue(invertedIndex.containsKey(key));
			List<Long> expectedLongs = expectedOutput.get(key);
			List<Long> retrievedLongs = invertedIndex.get(key);
			assertTrue(expectedLongs.size() == retrievedLongs.size());

			List<Long> sortedExpected = deepCopyAndSort(expectedLongs);
			List<Long> sortedRetrieved = deepCopyAndSort(retrievedLongs);
			for(int i=0; i<sortedExpected.size(); i++)
			{
				assertTrue(sortedExpected.get(i) == sortedRetrieved.get(i));
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