package com.simpleir.wiki.preprocess.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.ArticleExtractor;
import com.simpleir.wiki.io.MediaWikiCleaner;
import com.simpleir.wiki.model.Article;
import com.simpleir.wiki.preprocess.MediaWikiToPlainTextExtractor;

@Component
public class MediaWikiToPlainTextExtractorImpl implements MediaWikiToPlainTextExtractor
{
	@Autowired
	private ArticleExtractor articleExtractor;

	@Autowired
	private MediaWikiCleaner mediaWikiCleaner;

	@Value("${mediawiki.parsed.location}")
	private String mediaWikiFileLocation;

	@Value("${plaintext.location}")
	private String plainTextFileLocation;

	@Value("${new_article_line}")
	private String newArticleLine;

	@Value("${article_prefixes_to_skip}")
	private String titlePrefixesToSkip;

	private List<String> prefixesToSkip;

	private boolean isInitialized;

	@Value("${charset_long}")
	private String charsetLong;

	@Value("${strictly_preserve_order}")
	private boolean preserveOrder;

	private Charset preferredCharset;

	private void init()
	{
		if(!isInitialized)
		{
			prefixesToSkip = Arrays.asList(titlePrefixesToSkip.split(","));
			preferredCharset = Charset.forName(charsetLong);
			isInitialized = true;
		}
	}

	public void run() throws IOException
	{
		init();

		if(preserveOrder)
		{
			String[] filenames = new File(mediaWikiFileLocation).list();
			for(int i=0; i<filenames.length; i++)
			{
				subRun(filenames[i]);
			}
		}
		else
		{
			for(String filename : new File(mediaWikiFileLocation).list())
			{
				subRun(filename);
			}
		}
		
	}

	private void subRun(String filenameWithoutDir) throws IOException
	{
		String absoluteReadPath  = mediaWikiFileLocation + File.separator + filenameWithoutDir;
		String absoluteWritePath = plainTextFileLocation + File.separator + filenameWithoutDir;

		Iterator<String> lineIter = Files.readAllLines(Paths.get(absoluteReadPath), preferredCharset).iterator();
		Iterator<Article> articleIter = articleExtractor.getArticlesFromLines(lineIter).iterator();
		PrintWriter pw = new PrintWriter(absoluteWritePath);

		extractPlainTextFromArticles(articleIter, pw);
	}

	void extractPlainTextFromArticles(Iterator<Article> articleIter, Writer writer) throws IOException
	{
		init();
		while(articleIter.hasNext())
		{
			Article article = articleIter.next();
			String title = article.getTitle();
			if(startsWithOneOf(title, prefixesToSkip))
			{
				continue;
			}

			String cleanedText = mediaWikiCleaner.cleanText(article.getText());
			article.setText(cleanedText);

			List<String> linesToPrint = formatArticleForPrinting(article, newArticleLine);
			for(int i=0; i<linesToPrint.size(); i++)
			{
				writer.write(linesToPrint.get(i));
				writer.write(String.format("%n"));
			}

			writer.flush();
		}

		writer.close();
	}

	

	private static List<String> formatArticleForPrinting(Article article, String newArticleLine)
	{
		String titleLine = article.getId() + ": " + article.getTitle();
		return Arrays.asList(newArticleLine, titleLine, article.getText());
	}

	private static final boolean startsWithOneOf(String str, Collection<String> prefixes)
	{
		for(String prefix : prefixes)
		{
			if(str.startsWith(prefix))
			{
				return true;
			}
		}

		return false;
	}
}