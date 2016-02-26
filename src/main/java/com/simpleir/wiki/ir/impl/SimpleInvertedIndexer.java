package com.simpleir.wiki.ir.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.ir.InvertedIndexer;
import com.simpleir.wiki.model.Article;
import com.simpleir.wiki.nlp.TextNormalizer;

@Component
public class SimpleInvertedIndexer implements InvertedIndexer
{
	@Autowired
	TextNormalizer textNormalizer;

	@Override
	public Map<String, List<Long>> extractInvertedIndexFromArticles(Iterator<Article> articleIter)
	{
		Map<String, List<Long>> map = new LinkedHashMap<>();

		while(articleIter.hasNext())
		{
			Article article = articleIter.next();
			long id = article.getId();

			Set<String> terms = extractTermsFromArticle(article);
			for(Iterator<String> termIter = terms.iterator(); termIter.hasNext(); )
			{
				String term = termIter.next();
				if(!map.containsKey(term))
				{
					map.put(term, new LinkedList<Long>());
				}

				map.get(term).add(id);
			}
		}

		return map;
	}

	private Set<String> extractTermsFromArticle(Article article)
	{
		Set<String> terms = new LinkedHashSet<>();

		ArticleTermIterator termIter = new ArticleTermIterator(article, textNormalizer);
		while(termIter.hasNext())
		{
			String term = termIter.next();
			if(!terms.contains(term))
			{
				terms.add(term);
			}
		}

		return terms;
	}
}
