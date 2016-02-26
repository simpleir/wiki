package com.simpleir.wiki.preprocess.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.preprocess.XmlToMediaWikiExtractor;
import com.simpleir.wiki.process.WikiDumpSaxHandler;

@Component
public class XmlToMediaWikiExtractorImpl implements XmlToMediaWikiExtractor
{
	@Value("${mediawikidump.location}")
	private String xmlDumpLocation;

	@Value("${mediawiki.parsed.location}")
	private String mediaWikiFileLocation;

	@Value("${num_articles_per_file}")
	private int articlesPerFile;

	@Value("${new_article_line}")
	private String newArticleLine;

	@Value("${mediawiki.parsed.formatstr}")
	private String formatStr;

    @Value("${skip_redirects}")
    private Boolean skipRedirectArticles;

	public void run() throws Exception
	{
		String outputFormatStr = mediaWikiFileLocation + File.separator + formatStr;
		InputStream is = new FileInputStream(xmlDumpLocation);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		WikiDumpSaxHandler handler = new WikiDumpSaxHandler(articlesPerFile, skipRedirectArticles, outputFormatStr, true, newArticleLine);
		parser.parse(is, handler);
	}
}