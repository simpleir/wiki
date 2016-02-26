package com.simpleir.wiki.io;

import java.util.List;
import java.util.Map;

public interface PositionalIndexIO
{
	String idxToString(String term, Map<Long, List<Long>> positionalIndex);

	String termFromString(String generalIndexLine);

	Map<Long, List<Long>> idxFromString(String positionalIndexLine);
}