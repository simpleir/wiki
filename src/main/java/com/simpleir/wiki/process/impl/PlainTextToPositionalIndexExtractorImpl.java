package com.simpleir.wiki.process.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.ArticleExtractor;
import com.simpleir.wiki.io.PositionalIndexIO;
import com.simpleir.wiki.ir.PositionalIndexer;
import com.simpleir.wiki.model.Article;
import com.simpleir.wiki.nlp.TextNormalizer;
import com.simpleir.wiki.process.PlainTextToPositionalIndexExtractor;

@Component
public class PlainTextToPositionalIndexExtractorImpl implements PlainTextToPositionalIndexExtractor
{
	@Autowired
	private ArticleExtractor articleExtractor;

	@Autowired
	private PositionalIndexer positionalIndexer;

	@Autowired
	private PositionalIndexIO positionalIndexIO;

	@Autowired
	private TextNormalizer textNormalizer;

	@Value("${plaintext.location}")
	private String plainTextDirectory;

	@Value("${positional_index.location.raw}")
	private String rawPositionalIndexDirectory;

	@Value("${preferred_terms}")
	private String preferredTermsStr;

	@Value("${restrict_to_preferred_terms}")
	private Boolean restrictToPreferredTerms;

	@Value("${strictly_preserve_order}")
	private boolean preserveOrder;

	private Set<String> preferredTerms;

	private boolean isInitialized;

	@Value("${charset_long}")
	private String charsetLong;

	private Charset preferredCharset;

	private void init()
	{
		if(!isInitialized)
		{
			preferredCharset = Charset.forName(charsetLong);
			preferredTerms = new HashSet<String>();
			for(String preferredTerm : preferredTermsStr.split(","))
			{
				preferredTerms.add(textNormalizer.normalizeWord(preferredTerm));
			}
			
			isInitialized = true;
		}
	}

	@Override
	public void run() throws IOException
	{
		init();

		if(preserveOrder)
		{
			String[] filenames = new File(plainTextDirectory).list();
			for(int i=0; i<filenames.length; i++)
			{
				subRun(filenames[i]);
			}
		}
		else
		{
			for(String filename : new File(plainTextDirectory).list())
			{
				subRun(filename);
			}
		}
	}

	private void subRun(String filenameWithoutDir) throws IOException
	{
		String absoluteReadPath  = plainTextDirectory          + File.separator + filenameWithoutDir;
		String absoluteWritePath = rawPositionalIndexDirectory + File.separator + filenameWithoutDir;

		Iterator<String> lineIter = Files.readAllLines(Paths.get(absoluteReadPath), preferredCharset).iterator();
		Iterator<Article> articleIter = articleExtractor.getArticlesFromLines(lineIter).iterator();
		PrintWriter pw = new PrintWriter(absoluteWritePath);

		extractPositionalIndicesFromArticles(articleIter, pw);
	}

	void extractPositionalIndicesFromArticles(Iterator<Article> articleIter, Writer writer) throws IOException
	{
		init();
		Map<String, Map<Long, List<Long>>> positionalIndices = positionalIndexer.extractPositionalIndexFromArticles(articleIter);

		for(Iterator<String> termIter = positionalIndices.keySet().iterator(); termIter.hasNext(); )
		{
			String term = termIter.next();
			if(restrictToPreferredTerms && !preferredTerms.contains(term))
			{
				continue;
			}

			writer.write(positionalIndexIO.idxToString(term, positionalIndices.get(term)));
			writer.write(String.format("%n"));

			writer.flush();
		}

		writer.close();
	}
}