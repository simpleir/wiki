package com.simpleir.wiki.io.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.ArticleExtractor;
import com.simpleir.wiki.model.Article;

@Component
public class ArticleExtractorImpl implements ArticleExtractor
{
	@Value("${new_article_line}")
	private String newArticleLine;

	@Override
	public List<Article> getArticlesFromLines(Iterator<String> lineIter)
	{
		List<Article> list = new ArrayList<>(1024);
		Article curArticle = null;

		StringBuilder sb = new StringBuilder();
		while(lineIter.hasNext())
		{
			String line = lineIter.next();
			if("".equals(line))
			{
				continue;
			}

			if(newArticleLine.equals(line))
			{
				if(curArticle != null)
				{
					String text = sb.toString().trim();
					if(text != null && !"".equals(text))
					{
						curArticle.setText(text);
						list.add(curArticle);
					}
					sb = new StringBuilder();
				}

				if(lineIter.hasNext())
				{
					line = lineIter.next();
					curArticle = new Article();
					int colonIndex = line.indexOf(':');
					curArticle.setId(Long.parseLong(line.substring(0, colonIndex)));
					curArticle.setTitle(line.substring(colonIndex + 1).trim());
				}
			}
			else
			{
				sb.append(' ');
				sb.append(line);
			}
		}

		String text = sb.toString().trim();
		if(text != null && !"".equals(text))
		{
			curArticle.setText(text);
			list.add(curArticle);
		}

		return list;
	}

}
