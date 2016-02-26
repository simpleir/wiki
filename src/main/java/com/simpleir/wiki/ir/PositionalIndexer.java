package com.simpleir.wiki.ir;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.simpleir.wiki.model.Article;

public interface PositionalIndexer
{
	Map<String, Map<Long, List<Long>>> extractPositionalIndexFromArticles(Iterator<Article> articleIter);
}