package com.simpleir.wiki.utils;

public class StringUtils
{
	public static final boolean isW(int i)
	{
		return (i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122);
	}

	public static final String trimNonWCharsFromText(String word)
	{
		int start = 0;
		int end = word.length()-1;

		while(start <= end && !isW(word.charAt(start))) { start++; }
		while(start <= end && !isW(word.charAt(end)))   { end--;   }

		return (start <= end) ? word.substring(start, end+1) : "";
	}

	public static final String replaceNonWCharsWithUnderscores(String term)
	{
		char[] charArray = term.toCharArray();
		for(int i=0; i<charArray.length; i++)
		{
			if(!isW(charArray[i]))
			{
				charArray[i] = '_';
			}
		}

		return new String(charArray);
	}

	public static final String collapseWhitespace(String text)
	{
		String temp = text.replaceAll("[\\t ]+", " ")
		                  .replaceAll("^ $", "")
		                  .replaceAll("\r", "");

		while(temp.indexOf("\n\n") >= 0)
		{
			temp = temp.replaceAll("\n\n", "\n");
		}
		return temp;
	}

	private static final boolean isProperlyDelineated(String text, char startingSymbol, char endingSymbol)
	{
		char[] chars = text.toCharArray();
		int level = 0;
		for(int i=0; i<chars.length; i++)
		{
			if(chars[i] == startingSymbol) { level++; }
			if(chars[i] == endingSymbol) { level--; }
			if(level < 0) { return false; }
		}

		return level == 0;
	}

	private static final String removePairedSymbolDelineatedSectionsProper(String text, char startingSymbol, char endingSymbol)
	{
		char[] chars = text.toCharArray();
		int length = chars.length;

		char[] copiedChars = new char[length];

		int targetIndex = 0;
		int sourceIndex = 0;
		int level = 0;

		while(sourceIndex < length)
		{
			char c = chars[sourceIndex++];
			if(startingSymbol == c)
			{
				level++;
			}
			else if(endingSymbol == c)
			{
				level--;
				copiedChars[targetIndex++] = ' ';
			}
			else if(level == 0)
			{
				copiedChars[targetIndex++] = c;
			}
		}

		return new String(copiedChars, 0, targetIndex);
	}

	public static final String removePairedSymbolDelineatedSections(String text, char startingSymbol, char endingSymbol)
	{
		if(isProperlyDelineated(text, startingSymbol, endingSymbol))
		{
			return removePairedSymbolDelineatedSectionsProper(text, startingSymbol, endingSymbol);
		}

		return text;
	}
}