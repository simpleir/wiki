package com.simpleir.wiki.process;

import java.io.IOException;

/**
 * @author simpleir.com
 * Looks through all lines of all files in a directory (ignoring subdirectories),
 * and writes each line to a specific file in another directory,
 * based on the "term" with which that line starts,
 * so that each line in the resultant directory contains lines for a single term. 
 */
public interface GroupIndicesByTermExtractor
{
	void run() throws IOException;
}