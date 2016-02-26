package com.simpleir.wiki.io;

import java.util.List;

public interface InvertedIndexIO
{
	String idxToString(String term, List<Long> indices);

	List<Long> idxFromString(String invertedIndexLine);

	String termFromString(String generalIndexLine);
}