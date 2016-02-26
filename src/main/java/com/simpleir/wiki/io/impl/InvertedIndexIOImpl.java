package com.simpleir.wiki.io.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.InvertedIndexIO;

@Component
public class InvertedIndexIOImpl implements InvertedIndexIO
{

	@Override
	public String idxToString(String term, List<Long> indices)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(term);
		sb.append(": ");
		for(Iterator<Long> idIter = indices.iterator(); idIter.hasNext(); )
		{
			sb.append(idIter.next());
			if(idIter.hasNext())
			{
				sb.append(", ");
			}
		}

		return sb.toString();
	}

	@Override
	public List<Long> idxFromString(String invertedIndexLine)
	{
		int start = invertedIndexLine.lastIndexOf(':') + 1;
		String indicesStr = invertedIndexLine.substring(start).trim();
		if(indicesStr.length() == 0)
		{
			return Collections.emptyList();
		}
		String[] individualIndexStrs = indicesStr.split(",");

		List<Long> indices = new LinkedList<Long>();
		for(int i=0; i<individualIndexStrs.length; i++)
		{
			indices.add(Long.parseLong(individualIndexStrs[i].trim()));
		}

		return indices;
	}

	@Override
	public String termFromString(String str)
	{
		int lastColonIndex = str.lastIndexOf(':');
		return lastColonIndex > 0 ? str.substring(0, str.lastIndexOf(':')).trim() : "";
	}
}