package com.simpleir.wiki.process;

import java.io.IOException;

/**
 * @author simpleir.com
 * For each file in a directory (ignoring subdirectories), for each article in that file,
 * for each term in that article, it adds that term to an inverted index to denote that
 * that term is present in that article.
 * It subsequently writes the inverted index generated from each file to a corresponding file
 * in another directory.
 */
public interface PlainTextToInvertedIndexExtractor
{
	void run() throws IOException;
}
