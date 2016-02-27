package com.simpleir.wiki.process;

import java.io.IOException;

/**
 * @author simpleir.com
 * For each file in a directory (ignoring subdirectories), for each article in that file,
 * for each term in that article, for each instance of that term in that article,
 * it adds that term to an positional index together with all positions in that article
 * to denote that that term is present in that article and in which places.
 * It subsequently writes the positional index generated from each file to a corresponding file
 * in another directory.
 */
public interface PlainTextToPositionalIndexExtractor
{
	void run() throws IOException;
}