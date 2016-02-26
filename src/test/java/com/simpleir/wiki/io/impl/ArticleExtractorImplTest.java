package com.simpleir.wiki.io.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.io.ArticleExtractor;
import com.simpleir.wiki.io.impl.ArticleExtractorImpl;
import com.simpleir.wiki.model.Article;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class ArticleExtractorImplTest
{
	@Autowired
	private ArticleExtractor articleExtractor;

	private List<String> lines = Arrays.asList("",
			"+------------ NEW ARTICLE ------------+",
			"1: Article a",
			"If you tell the truth, you don't have to remember anything.",
			"",
			"+------------ NEW ARTICLE ------------+",
			"2: Article b",
			"Good friends, good books, and a sleepy conscience: this is the ideal life.",
			"",
			"+------------ NEW ARTICLE ------------+",
			"3: Article c",
			"Never tell the truth to people who are not worthy of it.",
			"",
			"+------------ NEW ARTICLE ------------+",
			"4: Article d",
			"The man who does not read has no advantage over the man who cannot read.",
			"",
			"+------------ NEW ARTICLE ------------+",
			"5: Article e",
			"I have never let my schooling interfere with my education.",
			"",
			"+------------ NEW ARTICLE ------------+",
			"37352292: Big Apple (Colborne, Ontario)",
			"[[File:Colborne Big Apple.jpg|thumb|A tourist with the Big Apple]]",
			"The '''Big Apple''' is a bakery, restaurant and [[roadside attraction]] in the community of [[Colborne, Ontario|Colborne]], part of the municipality of [[Cramahe, Ontario|Cramahe]], [[Northumberland County, Ontario|Northumberland County]] in [[Central Ontario|Central]] [[Ontario]], [[Canada]]. It just off [[Ontario Highway 401]] at interchange 497 (Northumberland County Road 25/Percy Street) and is recognizable from the highway because of what it claims is the world's largest apple.<ref>{{cite book|last=Culbert|first=Terry|title=County Roads: Around Ontario with Global Television's Terry Culbert|url=http://books.google.ca/books?id=ZXpNR-QzZSwC&pg=PA17&lpg=PA17&dq=Big+Apple+Colborne+Ontario&source=bl&ots=cHgt7FKVh2&sig=VMNJdXo9YoONxtQ4Iok7mc4RYkY&hl=en&sa=X&ei=PAp-UP7QEKaR0QH68IDoDA&ved=0CC8Q6AEwAA#v=onepage&q=Big%20Apple%20Colborne%20Ontario&f=false|year=1995|publisher=General Store Publishing House|location=Burnstown, Ontario|isbn=1896182216|page=17|chapter=Northumberland Country Apples|authormask=|trans_title=|format=|origyear=|oclc=|doi=|bibcode=|id=|trans_chapter=|chapterurl=|quote=|laysummary=|laydate=}}</ref>",
			"",
			"With a height of {{convert|10.7|m}} and diameter of {{convert|11.6|m}}, the Big Apple features an observation deck.",
			"",
			"The site also features a petting zoo and other amenities.",
			"",
			"==See also==",
			"* [[List of bakeries]]",
			"",
			"==References==",
			"{{reflist}}",
			"",
			"==External links==",
			"*{{Official website|http://web.archive.org/web/20121230024936/http://www.visitcramahe.ca/cramahe/tourism/apple.asp}}",
			"*[http://www.cbc.ca/archives/categories/lifestyle/travel/supersized-sights-of-canada/big-apple-dreams.html ''Big apple dreams in Colborne''], CBC Digital Archives, ''[[Venture (TV series)|Venture]]''; Broadcast Date: May 22, 1988;  Guests: George Boycott, Suzie Boycott, Henry Mensen, Doug Rutherford; Host: [[Robert Guy Scully]] ",
			"",
			"{{coord|44|01|20|N|77|54|20|W|region:CA-ON_type:landmark_source:GoogleEarth|display=title}}",
			"",
			"[[Category:Roadside attractions in Canada]]",
			"[[Category:Restaurants in Ontario]]",
			"[[Category:Bakeries of Canada]]",
			"[[Category:Buildings and structures in Northumberland County, Ontario]]",
			"[[Category:Novelty buildings in Canada]]",
			"[[Category:Visitor attractions in Northumberland County, Ontario]]",
			"",
			"{{Ontario-struct-stub}}",
			"");

	private static List<Article> expectedArticleList = Arrays.asList(
			new Article("Article a", 1L, "If you tell the truth, you don't have to remember anything."),
			new Article("Article b", 2L, "Good friends, good books, and a sleepy conscience: this is the ideal life."),
			new Article("Article c", 3L, "Never tell the truth to people who are not worthy of it."),
			new Article("Article d", 4L, "The man who does not read has no advantage over the man who cannot read."),
			new Article("Article e", 5L, "I have never let my schooling interfere with my education."),
			new Article("Big Apple (Colborne, Ontario)", 37352292L, "[[File:Colborne Big Apple.jpg|thumb|A tourist with the Big Apple]] The '''Big Apple''' is a bakery, restaurant and [[roadside attraction]] in the community of [[Colborne, Ontario|Colborne]], part of the municipality of [[Cramahe, Ontario|Cramahe]], [[Northumberland County, Ontario|Northumberland County]] in [[Central Ontario|Central]] [[Ontario]], [[Canada]]. It just off [[Ontario Highway 401]] at interchange 497 (Northumberland County Road 25/Percy Street) and is recognizable from the highway because of what it claims is the world's largest apple.<ref>{{cite book|last=Culbert|first=Terry|title=County Roads: Around Ontario with Global Television's Terry Culbert|url=http://books.google.ca/books?id=ZXpNR-QzZSwC&pg=PA17&lpg=PA17&dq=Big+Apple+Colborne+Ontario&source=bl&ots=cHgt7FKVh2&sig=VMNJdXo9YoONxtQ4Iok7mc4RYkY&hl=en&sa=X&ei=PAp-UP7QEKaR0QH68IDoDA&ved=0CC8Q6AEwAA#v=onepage&q=Big%20Apple%20Colborne%20Ontario&f=false|year=1995|publisher=General Store Publishing House|location=Burnstown, Ontario|isbn=1896182216|page=17|chapter=Northumberland Country Apples|authormask=|trans_title=|format=|origyear=|oclc=|doi=|bibcode=|id=|trans_chapter=|chapterurl=|quote=|laysummary=|laydate=}}</ref> With a height of {{convert|10.7|m}} and diameter of {{convert|11.6|m}}, the Big Apple features an observation deck. The site also features a petting zoo and other amenities. ==See also== * [[List of bakeries]] ==References== {{reflist}} ==External links== *{{Official website|http://web.archive.org/web/20121230024936/http://www.visitcramahe.ca/cramahe/tourism/apple.asp}} *[http://www.cbc.ca/archives/categories/lifestyle/travel/supersized-sights-of-canada/big-apple-dreams.html ''Big apple dreams in Colborne''], CBC Digital Archives, ''[[Venture (TV series)|Venture]]''; Broadcast Date: May 22, 1988;  Guests: George Boycott, Suzie Boycott, Henry Mensen, Doug Rutherford; Host: [[Robert Guy Scully]]  {{coord|44|01|20|N|77|54|20|W|region:CA-ON_type:landmark_source:GoogleEarth|display=title}} [[Category:Roadside attractions in Canada]] [[Category:Restaurants in Ontario]] [[Category:Bakeries of Canada]] [[Category:Buildings and structures in Northumberland County, Ontario]] [[Category:Novelty buildings in Canada]] [[Category:Visitor attractions in Northumberland County, Ontario]] {{Ontario-struct-stub}}"));

	@Test
	public void testGetArticlesFromLines()
	{
		assertTrue(articleExtractor instanceof ArticleExtractorImpl);
		List<Article> retrievedArticleList = articleExtractor.getArticlesFromLines(lines.iterator());

		assertTrue(retrievedArticleList.size() == expectedArticleList.size());
		for(int i=0; i<expectedArticleList.size(); i++)
		{
			Article expectedArticle = expectedArticleList.get(i);
			Article retrievedArticle = retrievedArticleList.get(i);
			assertEquals(expectedArticle, retrievedArticle);
		}
	}
}