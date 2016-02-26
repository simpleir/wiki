package com.simpleir.wiki.ir.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.ir.PositionalIndexer;
import com.simpleir.wiki.model.Article;
import com.simpleir.wiki.nlp.TextNormalizer;

@Component
public class SimplePositionalIndexer implements PositionalIndexer
{
	@Autowired
	TextNormalizer textNormalizer;

	@Override
	public Map<String, Map<Long, List<Long>>> extractPositionalIndexFromArticles(Iterator<Article> articleIter)
	{
		Map<String, Map<Long, List<Long>>> map = new LinkedHashMap<String, Map<Long,List<Long>>>();
		while(articleIter.hasNext())
		{
			Article article = articleIter.next();

			long id = article.getId();

			Map<String, List<Long>> termToPositionMap = extractTermPositionsFromArticle(article);

			for(Iterator<String> termIter = termToPositionMap.keySet().iterator(); termIter.hasNext(); )
			{
				String term = termIter.next();
				if(!map.containsKey(term))
				{
					map.put(term, new LinkedHashMap<Long, List<Long>>());
				}

				map.get(term).put(id, termToPositionMap.get(term));
			}
		}
		
		return map;
	}

	private Map<String, List<Long>> extractTermPositionsFromArticle(Article article)
	{
		Map<String, List<Long>> termToPositionMap = new LinkedHashMap<String, List<Long>>();
		long position = 0;
		for(ArticleTermIterator iter = new ArticleTermIterator(article, textNormalizer); iter.hasNext(); position++)
		{
			String term = iter.next();

			if(!termToPositionMap.containsKey(term))
			{
				termToPositionMap.put(term, new LinkedList<Long>());
			}

			termToPositionMap.get(term).add(position);
		}

		return termToPositionMap;
	}
}