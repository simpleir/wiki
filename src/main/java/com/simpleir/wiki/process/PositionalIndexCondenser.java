package com.simpleir.wiki.process;

import java.io.IOException;

/**
 * @author simpleir.com
 * For each file in a directory (ignoring subdirectories),
 * condenses the various positional index lines into a single positional index line,
 * and writes the result to another file in another directory.
 */
public interface PositionalIndexCondenser
{
	void run() throws IOException;
}