package com.simpleir.wiki.process.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.InvertedIndexIO;
import com.simpleir.wiki.io.PositionalIndexIO;
import com.simpleir.wiki.process.PositionalIndexCondenser;

@Component
public class PositionalIndexCondenserImpl implements PositionalIndexCondenser
{
	@Autowired
	private InvertedIndexIO invertedIndexIo;

	@Autowired
	private PositionalIndexIO positionalIndexIO;

	@Value("${positional_index.location.grouped}")
	private String groupedIndexDestDir;

	@Value("${positional_index.location.done}")
	private String properPositionalIndexDir;

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

	@Override
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
		String absoluteReadPath  = groupedIndexDestDir      + File.separator + filenameWithoutDir;
		String absoluteWritePath = properPositionalIndexDir + File.separator + filenameWithoutDir;

		Iterator<String> lineIter = Files.readAllLines(Paths.get(absoluteReadPath), preferredCharset).iterator();
		PrintWriter pw = new PrintWriter(absoluteWritePath);

		condenseFile(lineIter, pw);
	}

	void condenseFile(Iterator<String> lineIter, Writer writer) throws IOException
	{
		init();
		String mainTerm = null;
		Map<Long, List<Long>> mainIndex = new LinkedHashMap<Long, List<Long>>();

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
			
			Map<Long, List<Long>> positionalIndex = positionalIndexIO.idxFromString(line);
			for(Iterator<Long> docIdIter = positionalIndex.keySet().iterator(); docIdIter.hasNext(); )
			{
				long docId = docIdIter.next();
				mainIndex.put(docId, positionalIndex.get(docId));

				if(!term.equals(mainTerm))
				{
					System.err.println("Wrong term in file.  Expected: " + mainTerm + ", found: " + term + ".");
				}
			}
		}

		writer.write(positionalIndexIO.idxToString(mainTerm, mainIndex));
		writer.write(String.format("%n"));
		writer.close();
	}
}