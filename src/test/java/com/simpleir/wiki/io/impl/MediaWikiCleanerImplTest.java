package com.simpleir.wiki.io.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.io.MediaWikiCleaner;
import com.simpleir.wiki.io.impl.MediaWikiCleanerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class MediaWikiCleanerImplTest
{
	@Autowired
	private MediaWikiCleaner mediaWikiCleaner;

	@Test
	public void testCleanText()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("Hello", "Hello");
		expectedInputOutputMap.put("'''Dustin Long''' (born December 17, 1968) is a [[NASCAR]] reporter for [[Motor Racing Network]]. He is currently the senior staff writer for the organization.  Long is a 1991 graduate of the [[Indiana University Bloomington]] with a degree in Journalism.  Long worked 13 years for the [[The Virginian-Pilot|Virginian Pilot]], covering motorsport. He held the post he held until 2011, when he moved on to MRN. According to Long, he has \"covered more than 400 Cup races\" since 1999.  Long is also a published author, with another book, NASCAR Racing (Inside the Speedway), set to release September 1.  ==Notes and References==  <ref>http://hamptonroads.com/blogs/dustin-long</ref> <ref>http://dustinlong01.wordpress.com/about/</ref>  ==References== {{Reflist}}  {{DEFAULTSORT:Long, Dustin}} [[Category:American male journalists]] [[Category:NASCAR people]] [[Category:1968 births]] [[Category:Living people]]   {{US-journalist-stub}}",
				                   "Dustin Long born December 17 1968 is a NASCAR reporter for Motor Racing Network. He is currently the senior staff writer for the organization. Long is a 1991 graduate of the Indiana University Bloomington with a degree in Journalism. Long worked 13 years for the Virginian Pilot covering motorsport. He held the post he held until 2011 when he moved on to MRN. According to Long he has covered more than 400 Cup races since 1999. Long is also a published author with another book NASCAR Racing Inside the Speedway set to release September 1. Notes and References");

		assertTrue(mediaWikiCleaner instanceof MediaWikiCleanerImpl);
		for(String input : expectedInputOutputMap.keySet())
		{
			String output = mediaWikiCleaner.cleanText(input);
			assertEquals(output.trim(), expectedInputOutputMap.get(input).trim());
		}
	}

	@Test
	public void testStripEncodedCharacters()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("&mdash;", " ");
		expectedInputOutputMap.put("&mdash", "&mdash");
		expectedInputOutputMap.put("Hello", "Hello");
		expectedInputOutputMap.put("</ref>&nbsp;Hello", "</ref> Hello");
		expectedInputOutputMap.put("</ref>nbsp;Hello", "</ref>nbsp;Hello");
		expectedInputOutputMap.put("Big&mash;Hello", "Big Hello");
		expectedInputOutputMap.put("&larr;Big&mdash;Hello", " Big Hello");
		expectedInputOutputMap.put("Jeremy&abc;", "Jeremy ");
		expectedInputOutputMap.put("Wikip&#198;dia;", "Wikip dia;");
		expectedInputOutputMap.put("Wikip&#abcdefghijklmnop;dia;", "Wikip&#abcdefghijklmnop;dia;");
		
		assertTrue(mediaWikiCleaner instanceof MediaWikiCleanerImpl);
		MediaWikiCleanerImpl impl = (MediaWikiCleanerImpl) mediaWikiCleaner;

		for(String input : expectedInputOutputMap.keySet())
		{
			String output = impl.stripEncodedCharacters(input);
			assertEquals(output, expectedInputOutputMap.get(input));
		}
	}

	@Test
	public void testStripURLsFromText()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("Hello", "Hello");
		expectedInputOutputMap.put("What's going on, www peeps?", "What's going on, www peeps?");
		expectedInputOutputMap.put("What's going on, http:/peeps?", "What's going on, http:/peeps?");
		expectedInputOutputMap.put("The website www.theonion is good", "The website www.theonion is good");
		expectedInputOutputMap.put("The website www.theonion.com is good", "The website  is good");
		expectedInputOutputMap.put("http://", "http://");
		expectedInputOutputMap.put("http://a", "http://a");
		expectedInputOutputMap.put("http://a.", "http://a.");
		expectedInputOutputMap.put("http://a.b", "");
		

		assertTrue(mediaWikiCleaner instanceof MediaWikiCleanerImpl);
		MediaWikiCleanerImpl impl = (MediaWikiCleanerImpl) mediaWikiCleaner;

		for(String input : expectedInputOutputMap.keySet())
		{
			String output = impl.stripURLsFromText(input);
			assertEquals(output, expectedInputOutputMap.get(input));
			
		}
	}

	@Test
	public void testReplaceUnnecessaryCharsWithSpaces()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put(".-_/[]|:'", ".-_/[]|:'");
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("a", "a");
		expectedInputOutputMap.put("once-in-a-lifetime", "once-in-a-lifetime");
		expectedInputOutputMap.put("once+in+a+lifetime", "once in a lifetime");
		expectedInputOutputMap.put(",!@#$%^&*()=+`~{}\\;\"<>?#~±”“…„™ƒ°¶«»®¬©§£¢€¡",
				                   "                                            ");

		assertTrue(mediaWikiCleaner instanceof MediaWikiCleanerImpl);
		MediaWikiCleanerImpl impl = (MediaWikiCleanerImpl) mediaWikiCleaner;

		for(String input : expectedInputOutputMap.keySet())
		{
			String output = impl.replaceUnnecessaryCharsWithSpaces(input);
			assertEquals(output, expectedInputOutputMap.get(input));
		}
	}

	@Test
	public void testRemoveReferencesSection()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("Hello", "Hello");
		expectedInputOutputMap.put("==References==", "");
		expectedInputOutputMap.put(" ==References==", " ");
		expectedInputOutputMap.put("'''Dustin Long''' (born December 17, 1968) is a [[NASCAR]] reporter for [[Motor Racing Network]]. He is currently the senior staff writer for the organization.  Long is a 1991 graduate of the [[Indiana University Bloomington]] with a degree in Journalism.  Long worked 13 years for the [[The Virginian-Pilot|Virginian Pilot]], covering motorsport. He held the post he held until 2011, when he moved on to MRN. According to Long, he has \"covered more than 400 Cup races\" since 1999.  Long is also a published author, with another book, NASCAR Racing (Inside the Speedway), set to release September 1.  ==Notes and References==  <ref>http://hamptonroads.com/blogs/dustin-long</ref> <ref>http://dustinlong01.wordpress.com/about/</ref>  ==References== {{Reflist}}  {{DEFAULTSORT:Long, Dustin}} [[Category:American male journalists]] [[Category:NASCAR people]] [[Category:1968 births]] [[Category:Living people]]   {{US-journalist-stub}}  ",
				"'''Dustin Long''' (born December 17, 1968) is a [[NASCAR]] reporter for [[Motor Racing Network]]. He is currently the senior staff writer for the organization.  Long is a 1991 graduate of the [[Indiana University Bloomington]] with a degree in Journalism.  Long worked 13 years for the [[The Virginian-Pilot|Virginian Pilot]], covering motorsport. He held the post he held until 2011, when he moved on to MRN. According to Long, he has \"covered more than 400 Cup races\" since 1999.  Long is also a published author, with another book, NASCAR Racing (Inside the Speedway), set to release September 1.  ==Notes and References==  <ref>http://hamptonroads.com/blogs/dustin-long</ref> <ref>http://dustinlong01.wordpress.com/about/</ref>  ");

		for(String input : expectedInputOutputMap.keySet())
		{
			String output = MediaWikiCleanerImpl.removeReferencesSection(input);
			assertEquals(output, expectedInputOutputMap.get(input));
		}
	}

	@Test
	public void testAddSpaceAroundRefs()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("How<ref>are</ref>you", "How <ref>are</ref> you");
		expectedInputOutputMap.put("How<ref a=\"\">are</ref>you", "How <ref a=\"\">are</ref> you");
		expectedInputOutputMap.put("How are you", "How are you");
		expectedInputOutputMap.put("How ref are you", "How ref are you");
		expectedInputOutputMap.put("How<REF>are</ref>you", "How<REF>are</ref> you");
		expectedInputOutputMap.put("How< ref>are</ ref>you", "How< ref>are</ ref>you");

		for(String input : expectedInputOutputMap.keySet())
		{
			String output = MediaWikiCleanerImpl.addSpaceAroundRefs(input);
			assertEquals(output, expectedInputOutputMap.get(input));
		}
	}

	@Test
	public void testCleanWithBliki()
	{
		Map<String, String> expectedInputOutputMap = new HashMap<String, String>();
		expectedInputOutputMap.put("", "");
		expectedInputOutputMap.put("Hello", "Hello");
		expectedInputOutputMap.put("'''Terry Zwigoff''' has a great name", "Terry Zwigoff has a great name");
		expectedInputOutputMap.put("Axe these <ref> stupid </ref> refs" ,"Axe these  refs");
		expectedInputOutputMap.put("[[square braces]] are easy" ,"square braces are easy");
		expectedInputOutputMap.put("[[square braces | square braces with renames]] are harder" ,"square braces with renames are harder");
		expectedInputOutputMap.put("Long worked 13 years for the [[The Virginian-Pilot|Virginian Pilot]], covering motorsport. He held the post he held until 2011, when he moved on to MRN. According to Long, he has \"covered more than 400 Cup races\" since 1999.", "Long worked 13 years for the Virginian Pilot, covering motorsport. He held the post he held until 2011, when he moved on to MRN. According to Long, he has \"covered more than 400 Cup races\" since 1999.");
		expectedInputOutputMap.put("'''Aggravation de l'Espace''' is a public artwork by [[French people|French]] sculptor [[Jean Boutellis]] (born 1937), located on a median on Central Parkway in [[Cincinnati, Ohio]], [[United States]]. This sculpture was surveyed in 1994 as part of the [[Smithsonian Institution|Smithsonian's]] [[Save Outdoor Sculpture!]] program.<ref name=\"SOS\">{{cite web | author=Smithsonian | year=1993 | title=Aggravation de l'Espace, (sculpture). | work=Save Outdoor Sculpture | publisher=Smithsonian | url=http://siris-artinventories.si.edu/ipac20/ipac.jsp?&profile=all&source=~!siartinventories&uri=full=3100001~!302364~!0#focus | accessdate= 2 January 2010}}</ref>  ==Description==  This abstract steel sculpture has two large \"leg-like\" prongs on both sides of a triangular center piece. The sculpture is placed on small concrete disks which rest in a circular garden in the middle of the median along a busy street-way. The sculpture is painted [[red]].<ref name=\"SOS\"/>  ==Acquisition==  \"Aggravation de l'Espace\" was donated to the city by the [[Robert A. Taft]] family.<ref name=\"ArtInNature\">{{cite web | author= | year= | title=Art In Nature | work=Art and Architecture in Cincinnati Parks | publisher=Cincinnati Parks | url=http://www.cincinnatiparks.com/bm~doc/full-story-art-in-nature.pdf | accessdate= 2 January 2010}}</ref> The sculpture was originally installed in front of [[Cincinnati City Hall]] but after September 1984 it was moved to its current location.<ref name=\"SOS\"/> According to \"Cincinnati Parks and Parkways\" the piece was moved to its new location because it \"was so annoying to pedestrians.\"<ref name=Recchie>Darbee, Jeffrey T. & Nancy A. Recchie. ''Cincinnati Parks and Parkways''. Arcadia Publishing, 2010, p 100.</ref>  ==Information==  Locals have been known to call the sculpture \"Cootie\" because the sculpture's leggy design looks like a \"bug\" from the game [[Cootie (game)|Cootie]].<ref name=\"SOS\"/>  ==Condition==  This sculpture was surveyed by [[Save Outdoor Sculpture!]] in 1994 and was described as needing treatment. The sculpture is frequently repainted due to the frequent fading of the color. The paint is also known for flaking and rust is known to form underneath the structure. At the time of the survey it was in need of repainting.<ref name=\"SOS\"/>", "Aggravation de l'Espace is a public artwork by French sculptor Jean Boutellis (born 1937), located on a median on Central Parkway in Cincinnati, Ohio, United States. This sculpture was surveyed in 1994 as part of the Smithsonian's Save Outdoor Sculpture! program.  ==Description==  This abstract steel sculpture has two large \"leg-like\" prongs on both sides of a triangular center piece. The sculpture is placed on small concrete disks which rest in a circular garden in the middle of the median along a busy street-way. The sculpture is painted red.  ==Acquisition==  \"Aggravation de l'Espace\" was donated to the city by the Robert A. Taft family. The sculpture was originally installed in front of Cincinnati City Hall but after September 1984 it was moved to its current location. According to \"Cincinnati Parks and Parkways\" the piece was moved to its new location because it \"was so annoying to pedestrians.\"  ==Information==  Locals have been known to call the sculpture \"Cootie\" because the sculpture's leggy design looks like a \"bug\" from the game Cootie.  ==Condition==  This sculpture was surveyed by Save Outdoor Sculpture! in 1994 and was described as needing treatment. The sculpture is frequently repainted due to the frequent fading of the color. The paint is also known for flaking and rust is known to form underneath the structure. At the time of the survey it was in need of repainting.");

		for(String input : expectedInputOutputMap.keySet())
		{
			String output = MediaWikiCleanerImpl.cleanWithBliki(input);
			assertEquals(output, expectedInputOutputMap.get(input));
		}
	}
}