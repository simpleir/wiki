package com.simpleir.wiki.io;

import java.util.Iterator;
import java.util.List;

import com.simpleir.wiki.model.Article;

public interface ArticleExtractor
{
	List<Article> getArticlesFromLines(Iterator<String> lineIter);
}
