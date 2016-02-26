package com.simpleir.wiki.ir.impl;

import java.util.Iterator;
import java.util.Scanner;

import com.simpleir.wiki.model.Article;
import com.simpleir.wiki.nlp.TextNormalizer;

public class ArticleTermIterator implements Iterator<String>
{
	private TextNormalizer textNormalizer;

	private Scanner scanner;
	private String next = null;

	public ArticleTermIterator(Article article, TextNormalizer textNormalizer)
	{
		this.textNormalizer = textNormalizer;
		scanner = new Scanner(article.getText());
		updateNextTerm();
	}

	@Override
	public boolean hasNext()
	{
		return next != null;
	}

	@Override
	public String next()
	{
		String theNext = next;
		updateNextTerm();
		return theNext;
	}

	@Override
	public void remove()
	{
		//not used
	}

	private void updateNextTerm()
	{
		if(scanner.hasNext())
		{
			String next = scanner.next();
			String term = textNormalizer.normalizeWord(next);

			while(term == null && scanner.hasNext())
			{
				next = scanner.next();
				term = textNormalizer.normalizeWord(next);
			}

			if(term == null)
			{
				scanner.close();
			}

			this.next = term;			
		}
		else
		{
			next = null;
			scanner.close();
		}
	}
}