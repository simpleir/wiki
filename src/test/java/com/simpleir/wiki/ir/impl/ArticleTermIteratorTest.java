package com.simpleir.wiki.ir.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.model.Article;
import com.simpleir.wiki.nlp.TextNormalizer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class ArticleTermIteratorTest
{
	@Autowired
	private TextNormalizer textNormalizer;

	private static List<Article> articleList = Arrays.asList(
			new Article("Article a", 1L, "If you tell the truth, you don't have to remember anything."),
			new Article("Article b", 2L, "Good friends, good books, and a sleepy conscience: this is the ideal life."),
			new Article("Article c", 3L, "Never tell the truth to people who are not worthy of it."),
			new Article("Article d", 4L, "The man who does not read has no advantage over the man who cannot read."),
			new Article("Article e", 5L, "I have never let my schooling interfere with my education."));

	private static Map<Long, List<String>> articleIdToTermListMap = new HashMap<Long, List<String>>();
	static
	{
		articleIdToTermListMap.put(1L, Arrays.asList("tell", "truth", "rememb", "anyth"));
		articleIdToTermListMap.put(2L, Arrays.asList("good", "friend", "good", "book", "sleepi", "conscienc", "ideal", "life"));
		articleIdToTermListMap.put(3L, Arrays.asList("never", "tell", "truth", "peopl", "worthi"));
		articleIdToTermListMap.put(4L, Arrays.asList("man", "read", "advantag", "man", "read"));
		articleIdToTermListMap.put(5L, Arrays.asList("never", "let", "school", "interfer", "educ"));
	}

	@Test
	public void testSomething()
	{
		for(Article article : articleList)
		{
			Iterator<String> expectedIter = articleIdToTermListMap.get(article.getId()).iterator();
			ArticleTermIterator ati = new ArticleTermIterator(article, textNormalizer);

			while(expectedIter.hasNext())
			{
				assertTrue(ati.hasNext());
				assertEquals(ati.next(), expectedIter.next());
			}

			assertFalse(ati.hasNext());
		}
	}
}