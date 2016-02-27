package com.simpleir.wiki.preprocess;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.simpleir.wiki.model.Article;

public class WikiDumpSaxHandler extends DefaultHandler
{
	private String newArticleLine;
	private boolean skipRedirects;
	private boolean axeRefs;
	private final int numArticlesPerFile;
	private final String fileNameFormatStr;
	private int articleNumber;
	private PrintWriter pw;

	/**
	 * @param numArticlesPerFile How many articles to put in each file
	 * @param fileNameFormatStr the format String for filename %s where it can put a descriptor of each chunk.  
	 * @throws FileNotFoundException 
	 */
	public WikiDumpSaxHandler(int numArticlesPerFile, boolean skipRedirects, String fileNameFormatStr, boolean axeRefs, String newArticleLine) throws FileNotFoundException
	{
		this.numArticlesPerFile = numArticlesPerFile;
		this.skipRedirects = skipRedirects;
		this.fileNameFormatStr = fileNameFormatStr;
		this.pw = new PrintWriter(String.format(fileNameFormatStr, articleNumber));
		this.axeRefs = axeRefs;
		this.newArticleLine = newArticleLine;
	}

	private Article curArticle = null;
	private StringBuilder builder = new StringBuilder();

	private boolean startedArticleButNotStartedIdYet = false;
	private boolean grabId = false;
	private boolean inTextElement = false;
	private boolean inTitle = false;
	private boolean inRef = false;
	private boolean articleIsJustRedirect = false;

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if("text".equalsIgnoreCase(qName))
		{
			inTextElement = true;
		}
		else if("Page".equalsIgnoreCase(qName))
		{
			curArticle = new Article();
			startedArticleButNotStartedIdYet = true;
		}
		else if("Id".equalsIgnoreCase(qName))
		{
			if(startedArticleButNotStartedIdYet)
			{
				grabId = true;
				startedArticleButNotStartedIdYet = false;
			}
		}
		else if("title".equalsIgnoreCase(qName))
		{
			inTitle = true;
			titleStringBuilder = new StringBuilder();
		}
		else if("ref".equalsIgnoreCase(qName))
		{
			inRef = true;
		}
		else if("redirect".equalsIgnoreCase(qName))
		{
			articleIsJustRedirect = true;
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if("text".equalsIgnoreCase(qName))
		{
			curArticle.setText(builder.toString().trim());
			builder = new StringBuilder();
			inTextElement = false;
		}
		else if("Id".equalsIgnoreCase(qName))
		{
			grabId = false;
		}
		else if("title".equalsIgnoreCase(qName))
		{
			curArticle.setTitle(titleStringBuilder.toString().trim());
			inTitle = false;
		}
		else if("ref".equalsIgnoreCase(qName))
		{
			inRef = false;
		}
		else if("Page".equalsIgnoreCase(qName))
		{
			if(!skipRedirects || !articleIsJustRedirect)
			{
				pw.println();
				pw.println(newArticleLine);
				pw.println(curArticle.getId() + ": " + curArticle.getTitle());
				pw.println(curArticle.getText());
				pw.flush();

				articleNumber++;
			}

			articleIsJustRedirect = false;
			if(articleNumber > 0 && articleNumber % numArticlesPerFile == 0)
			{
				pw.close();
				try
				{
					pw = new PrintWriter(String.format(fileNameFormatStr, articleNumber));
				}
				catch(Exception e)
				{
				}
			}
		}
	}

	private StringBuilder titleStringBuilder = null;

	public void characters(char ch[], int start, int length) throws SAXException
	{
		String text = new String(ch, start, length);
		String trimmed = text.trim();

		if(inTextElement)
		{
			if(!axeRefs || !inRef)
			{
				builder.append(text);
			}
		}
		else if(inTitle)
		{
			titleStringBuilder.append(text);
		}

		else if(trimmed.length() > 0)
		{
			if(grabId)
			{
				curArticle.setId(Long.parseLong(trimmed));
			}
		}
	}
}
