package com.simpleir.wiki.process;

import java.io.IOException;

/**
 * @author simpleir.com
 * For each file in a directory (ignoring subdirectories),
 * condenses the various inverted index lines into a single inverted index line,
 * and writes the result to another file in another directory.
 */
public interface InvertedIndexCondenser
{
	void run() throws IOException;
}