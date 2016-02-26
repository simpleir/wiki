package com.simpleir.wiki.io.impl;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModel;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.MediaWikiCleaner;
import com.simpleir.wiki.utils.StringUtils;

@Component
public class MediaWikiCleanerImpl implements MediaWikiCleaner
{
	@Value("${url_regex}")
	private String urlRegex;

	@Value("${www_regex}")
	private String wwwRegex;

	@Value("${bad_chars}")
	private String badCharStrs;

	@Value("${encoded_chars_to_skip}")
	private String encodedCharToSkipStrs;

	@Value("${remove_commas}")
	private Boolean removeCommas;

	private char[] badChars;

	private String[] encodedCharsToSkip;

	private Pattern urlPattern;
	private Pattern wwwPattern;

	private boolean isInitialized;

	private void init()
	{
		if(!isInitialized)
		{
			urlPattern = Pattern.compile(urlRegex);
			wwwPattern = Pattern.compile(wwwRegex);

			String[] badCharStringArray = badCharStrs.split(",");
			int length = badCharStringArray.length + (removeCommas ? 1 : 0);
			badChars = new char[length];

			for(int i=0; i<badCharStringArray.length; i++)
			{
				badChars[i] = badCharStringArray[i].charAt(0);
			}

			if(removeCommas)
			{
				badChars[length-1] = ',';
			}

			encodedCharsToSkip = encodedCharToSkipStrs.split(",");
		}
	}

	@Override
	public String cleanText(String text)
	{
		init();

		try
		{
			String temp = removeReferencesSection(text);
			temp = temp.replaceAll("==\n", "==\n\n");
			temp = addSpaceAroundRefs(temp);
			temp = cleanWithBliki(temp);
			temp = StringUtils.removePairedSymbolDelineatedSections(temp, '{', '}');
			temp = stripEncodedCharacters(temp);
			temp = stripURLsFromText(temp);
			temp = replaceUnnecessaryCharsWithSpaces(temp);
			temp = StringUtils.collapseWhitespace(temp);
			return temp;
		}
		catch(Exception e)
		{
			return StringUtils.collapseWhitespace(StringUtils.removePairedSymbolDelineatedSections(text, '{', '}'));
		}
	}

	String stripEncodedCharacters(String text)
	{
		init();
		String temp = text;
		for(String encodedChar : encodedCharsToSkip)
		{
			temp = temp.replaceAll(encodedChar, " ");
		}

		return temp;
	}

	/** @return everything up to the ==References== part */
	static final String removeReferencesSection(String text)
	{
		int refIndex = text.indexOf("==References==");
		if(refIndex >= 0)
		{
			return text.substring(0, refIndex);
		}
		return text;
	}

	static final String addSpaceAroundRefs(String text)
	{
		return text.replaceAll("<ref", " <ref")
				   .replaceAll("/ref>", "/ref> ");
	}

	String stripURLsFromText(String text)
	{
		init();
		StringBuilder b = new StringBuilder();
		String[] strs = urlPattern.split(text);

		for(int i=0; i<strs.length; i++)
		{
			b.append(strs[i]);
		}

		String noHttp = b.toString();
		strs = wwwPattern.split(noHttp);

		b = new StringBuilder();
		for(int i=0; i<strs.length; i++)
		{
			b.append(strs[i]);
		}

		return b.toString();
	}

	String replaceUnnecessaryCharsWithSpaces(String text)
	{
		init();
		char[] sourceChars = text.toCharArray();
		char[] chars = new char[text.length()];

outer:	for(int i=0; i<sourceChars.length; i++)
		{
			if(StringUtils.isW(sourceChars[i]))
			{
				chars[i] = sourceChars[i];
				continue outer;
			}
			for(int j=0; j<badChars.length; j++)
			{
				if(sourceChars[i] == badChars[j])
				{
					chars[i] = ' ';
					continue outer;
				}
			}
			chars[i] = sourceChars[i];
		}

		return new String(chars);
	}

	static String cleanWithBliki(String text)
	{
		IWikiModel model = new WikiModel("http://www.simpleir.com", "http://www.simpleir.com");
		ITextConverter converter = new PlainTextConverter();
		Appendable resultBuffer = new StringBuilder();

		try
		{
			WikiModel.toText(model, converter, text, resultBuffer, false, false);
		}
		catch(Exception e)
		{
			int len = Math.min(1000, text.length());
			String intro = (len == 0) ? "" : text.substring(0, len);
			System.out.println("Issue with text starting with " + intro);
		}
		return resultBuffer.toString();
	}
}
