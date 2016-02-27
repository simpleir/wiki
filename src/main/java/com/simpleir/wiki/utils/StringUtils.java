package com.simpleir.wiki.utils;

/**
 * @author simpleir.com
 * A class containing basic string manipulation methods with a potential for reuse.
 */
public class StringUtils
{
	/**
	 * Determines whether the character matches [a-zA-Z0-9]
	 * @param i the character in question
	 * @return whether the character matches [a-zA-Z0-9]
	 */
	public static final boolean isW(int i)
	{
		return (i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122);
	}

	/**
	 * Analogous to String.trim(), where it removes an unwanted prefix and suffix,
	 * except that instead of removing characters that match \s, it removes those matching \W
	 * @param word the word from which to trim \W prefix and suffix
	 * @return
	 */
	public static final String trimNonWCharsFromText(String word)
	{
		int start = 0;
		int end = word.length()-1;

		while(start <= end && !isW(word.charAt(start))) { start++; }
		while(start <= end && !isW(word.charAt(end)))   { end--;   }

		return (start <= end) ? word.substring(start, end+1) : "";
	}

	/**
	 * Replaces all characters that fail isW with '_' (the underscore).
	 * @param term the term to clean
	 * @return the term with all \W characters replaced with '_'
	 */
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

	/**
	 * Attempts to replace all sequences matching \s+ with a single space.
	 * Intended largely for compression, and programmed conservatively,
	 * so it errs on the side of not replacing some \s+ sequences, lest it cause other problems.
	 * @param text the text in which to collapse the whitespace
	 * @return the text with whitespace collapsed from \s+ to a single space
	 */
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

	/**
	 * Checks whether the text has properly-matched starting and ending symbols, a. la. proper bracket nesting
	 * @param text the text to check
	 * @param startingSymbol the symbol starting a grouping
	 * @param endingSymbol the symbol ending a grouping
	 * @return whether the text has properly-matched starting and ending symbols
	 */
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

	/**
	 * Removes characters contained in a properly-delineated nested grouping scheme.
	 * E.g. for parentheses, "This is (just (a) test) of functionality" -> "This is    of functionality"
	 * Precise whitespace handling subject to change.
	 * @param text the text from which to remove the nested groupings
	 * @param startingSymbol the symbol starting a grouping
	 * @param endingSymbol the symbol ending a grouping
	 * @return the text with characters in groupings stripped out. 
	 */
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

	/**
	 * Removes characters contained in a nested grouping scheme IF they're properly delineated.
	 * Behavior is undefined if they are not properly delineated, though it may make a best effort to complete the job
	 * @param text the text from which to remove the nested groupings
	 * @param startingSymbol the symbol starting a grouping
	 * @param endingSymbol the symbol ending a grouping
	 * @return the text with characters in groupings stripped out IF they're properly delineated
	 */
	public static final String removePairedSymbolDelineatedSections(String text, char startingSymbol, char endingSymbol)
	{
		if(isProperlyDelineated(text, startingSymbol, endingSymbol))
		{
			return removePairedSymbolDelineatedSectionsProper(text, startingSymbol, endingSymbol);
		}

		return text;
	}
}