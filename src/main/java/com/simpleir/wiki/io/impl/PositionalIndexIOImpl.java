package com.simpleir.wiki.io.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.simpleir.wiki.io.PositionalIndexIO;

@Component
public class PositionalIndexIOImpl implements PositionalIndexIO
{

	@Override
	public String idxToString(String term, Map<Long, List<Long>> positionalIndex)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(term);
		sb.append(": ");

		for(Long docId : positionalIndex.keySet())
		{
			sb.append(docId);
			sb.append(" - ");

			List<Long> positions = positionalIndex.get(docId);
			for(int i=0; i<positions.size(); i++)
			{
				sb.append(positions.get(i));
				if(i < positions.size()-1)
				{
					sb.append(", ");
				}
			}

			sb.append("; ");
		}

		return sb.toString();
	}

	@Override
	public String termFromString(String str)
	{
		int lastColonIndex = str.lastIndexOf(':');
		return lastColonIndex > 0 ? str.substring(0, str.lastIndexOf(':')).trim() : "";
	}

	@Override
	public Map<Long, List<Long>> idxFromString(String positionalIndexLine)
	{
		int start = positionalIndexLine.lastIndexOf(':')+1;
		String positionalIndicesStr = positionalIndexLine.substring(start).trim();
		if(positionalIndicesStr.length() == 0)
		{
			return new HashMap<Long, List<Long>>();
		}
		
		String[] documentIndices = positionalIndicesStr.split(";");

		Map<Long, List<Long>> positionalIndex = new LinkedHashMap<Long, List<Long>>();
		for(int i=0; i<documentIndices.length; i++)
		{
			String documentIndexStr = documentIndices[i].trim();
			if("".equals(documentIndexStr))
			{
				continue;
			}

			int dash = documentIndexStr.indexOf('-');
			long docId = Long.parseLong(documentIndexStr.substring(0, dash).trim());

			String[] positionStrs = documentIndexStr.substring(dash+1).trim().split(",");
			List<Long> positions = new LinkedList<Long>();

			for(int j=0; j<positionStrs.length; j++)
			{
				positions.add(Long.parseLong(positionStrs[j].trim()));
			}

			positionalIndex.put(docId, positions);
		}

		return positionalIndex;
	}

}
