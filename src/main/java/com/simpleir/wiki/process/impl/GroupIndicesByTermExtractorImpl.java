package com.simpleir.wiki.process.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.InvertedIndexIO;
import com.simpleir.wiki.nlp.TextNormalizer;
import com.simpleir.wiki.process.GroupIndicesByTermExtractor;
import com.simpleir.wiki.utils.StringUtils;

@Component
public class GroupIndicesByTermExtractorImpl implements GroupIndicesByTermExtractor
{
	@Autowired
	private InvertedIndexIO invertedIndexIo;

	@Value("${ungrouped_index.dir}")
	private String ungroupedIndexSourceDir;

	@Value("${grouped_index.dir}")
	private String groupedIndexDestDir;

	@Autowired
	private TextNormalizer textNormalizer;

	@Value("${preferred_terms}")
	private String preferredTermsStr;

	@Value("${restrict_to_preferred_terms}")
	private Boolean restrictToPreferredTerms;

	private Set<String> preferredTerms;

	@Value("${charset_short}")
	private String charsetShort;

	@Value("${charset_long}")
	private String charsetLong;

	@Value("${strictly_preserve_order}")
	private boolean preserveOrder;

	private Charset preferredCharset;

	private boolean isInitialized;

	private void init()
	{
		if(!isInitialized)
		{
			preferredTerms = new HashSet<String>();
			for(String preferredTerm : preferredTermsStr.split(","))
			{
				preferredTerms.add(textNormalizer.normalizeWord(preferredTerm));
			}
			preferredCharset = Charset.forName(charsetLong);

			isInitialized = true;
		}
	}

	@Override
	public void run() throws IOException
	{
		init();

		if(preserveOrder)
		{
			String[] filenames = new File(ungroupedIndexSourceDir).list();
			for(int i=0; i<filenames.length; i++)
			{
				subRun(filenames[i]);
			}
		}
		else
		{
			for(String filename : new File(ungroupedIndexSourceDir).list())
			{
				subRun(filename);
			}
		}
	}

	private void subRun(String filenameWithoutDir) throws IOException
	{
		System.out.println(filenameWithoutDir);
		String absoluteReadPath  = ungroupedIndexSourceDir + File.separator + filenameWithoutDir;

		Iterator<String> lineIter = Files.readAllLines(Paths.get(absoluteReadPath), preferredCharset).iterator();

		IOMap ioMap = new SingleDirIOMap(groupedIndexDestDir, preferredCharset);
		groupLinesInFile(lineIter, ioMap);
	}

	void groupLinesInFile(Iterator<String> lineIter, IOMap ioMap) throws IOException
	{
		init();
		while(lineIter.hasNext())
		{
			String line = lineIter.next();
			String term = invertedIndexIo.termFromString(line);
			if(restrictToPreferredTerms && !preferredTerms.contains(term))
			{
				continue;
			}

			String sanitizedTerm = StringUtils.replaceNonWCharsWithUnderscores(term);
			ioMap.write(line, sanitizedTerm);
		}
	}

	/** Writes lines (with following newlines) to the specific resource.  A single-level hierarchy. */
	public static interface IOMap
	{
		void write(String line, String resourceName);
	}

	/** Implements IOMap as a single-level file directory. */
	public static class SingleDirIOMap implements IOMap
	{
		private String basePath;
		private Charset charset;

		public SingleDirIOMap(String basePath, Charset charset)
		{
			this.basePath = basePath;
			this.charset = charset;
		}

		public void write(String line, String resourceName)
		{
			Path outPath = Paths.get(basePath + File.separator + resourceName + ".txt");
			StandardOpenOption option = Files.exists(outPath) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW;
			try{
				String linePlusNewline = String.format(line + "%n");
				Files.write(outPath, (linePlusNewline).getBytes(charset), option);
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}