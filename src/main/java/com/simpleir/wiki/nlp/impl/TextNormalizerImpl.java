package com.simpleir.wiki.nlp.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simpleir.wiki.nlp.TextNormalizer;
import com.simpleir.wiki.utils.StringUtils;

@Component
public class TextNormalizerImpl implements TextNormalizer
{
	@Value("${stop_words}")
	private String stopWordsStr;

	private Set<String> stopWords = null;

	@Value("${max_nonword_characters_per_term}")
	private int maxNonwordCharacters;

	@Value("${max_characters_per_term}")
	private int maxCharacters;

	@Value("${min_characters_per_term}")
	private int minCharacters;

	private boolean isInitialized;

	private void init()
	{
		if(!isInitialized)
		{
			stopWords = new HashSet<>();
			stopWords.addAll(Arrays.asList(stopWordsStr.split(",")));
			isInitialized = true;
		}
	}

	@Override
	public String normalizeWord(String word)
	{
		init();
		String temp = word.toLowerCase();
		temp = StringUtils.trimNonWCharsFromText(temp);

		if(temp.length() > maxCharacters || temp.length() < minCharacters)
		{
			return null;
		}

		if(hasTooManyNonwordCharacters(temp))
		{
			return null;
		}

		if(stopWords.contains(temp))
		{
			return null;
		}

		if(getAsLong(temp) != null)
		{
			Long l = getAsLong(temp);
			if(word.charAt(0) == '-')
			{
				l *= -1;
			}
			return convertLong(l);
		}

		if(getAsDouble(temp) != null)
		{
			Double d = getAsDouble(temp);
			if(word.charAt(0) == '-')
			{
				d *= -1;
			}
			return convertDouble(d);
		}

		return OriginalPorterStemmer.simpleStemmer(temp);
	}

	private static final String convertLong(Long l)
	{
		if(l != null && l < 10000 && l > -10000)
		{
			return "" + l;
		}

		return null;
	}

	private static final Long getAsLong(String word)
	{
		try
		{
			Long l = Long.parseLong(word);
			return l;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	private static final String convertDouble(Double d)
	{
		if(d != null)
		{
			double abs = Math.abs(d);
			if(abs < 10)
			{
				return String.format("%.3f", d);
			}
			if(abs < 100)
			{
				return String.format("%.2f", d);
			}
			if(abs < 1000)
			{
				return String.format("%.1f", d);
			}
			return null;
		}

		return null;
	}

	private static final Double getAsDouble(String word)
	{
		try
		{
			Double d = Double.parseDouble(word);
			return d;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	private boolean hasTooManyNonwordCharacters(String word)
	{
		int nonWCount = 0;
		for(char c : word.toCharArray())
		{
			nonWCount += StringUtils.isW(c) ? 0 : 1;
		}

		return nonWCount > maxNonwordCharacters;
	}
}