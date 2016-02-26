package com.simpleir.wiki.process.impl;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.InvertedIndexIO;
import com.simpleir.wiki.process.InvertedIndexCondenser;

@Component
public class InvertedIndexCondenserImpl implements InvertedIndexCondenser
{
	@Autowired
	private InvertedIndexIO invertedIndexIo;

	@Value("${inverse_index.location.grouped}")
	private String groupedIndexDestDir;

	@Value("${inverse_index.location.done}")
	private String properInvertedIndexDir;

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
			preferredCharset = Charset.forName(charsetLong);
			isInitialized = true;
		}
	}

	public void run() throws IOException
	{
		init();

		if(preserveOrder)
		{
			String[] filenames = new File(groupedIndexDestDir).list();
			for(int i=0; i<filenames.length; i++)
			{
				subRun(filenames[i]);
			}
		}
		else
		{
			for(String filename : new File(groupedIndexDestDir).list())
			{
				subRun(filename);
			}
		}
	}

	private void subRun(String filenameWithoutDir) throws IOException
	{
		String absoluteReadPath  = groupedIndexDestDir + File.separator + filenameWithoutDir;
		String absoluteWritePath = properInvertedIndexDir + File.separator + filenameWithoutDir;

		Iterator<String> lineIter = Files.readAllLines(Paths.get(absoluteReadPath), preferredCharset).iterator();
		PrintWriter pw = new PrintWriter(absoluteWritePath);

		condenseFile(lineIter, pw);
	}

	void condenseFile(Iterator<String> lineIter, Writer writer) throws IOException
	{
		init();
		String mainTerm = null;
		List<Long> mainIndexList = new LinkedList<>();

		while(lineIter.hasNext())
		{
			String line = lineIter.next();
			if("".equals(line))
			{
				continue;
			}

			String term = invertedIndexIo.termFromString(line);
			if(mainTerm == null)
			{
				mainTerm = term;
			}

			List<Long> indices = invertedIndexIo.idxFromString(line);
			mainIndexList.addAll(indices);
			if(!term.equals(mainTerm))
			{
				System.err.println("Wrong term in file.  Expected: " + mainTerm + ", found: " + term + ".");
			}
		}

		writer.write(invertedIndexIo.idxToString(mainTerm, mainIndexList));
		writer.write(String.format("%n"));

		writer.close();
	}
}