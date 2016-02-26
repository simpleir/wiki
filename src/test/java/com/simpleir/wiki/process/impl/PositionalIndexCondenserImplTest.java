package com.simpleir.wiki.process.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.process.PositionalIndexCondenser;
import com.simpleir.wiki.process.impl.PositionalIndexCondenserImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class PositionalIndexCondenserImplTest
{
	@Autowired
	private PositionalIndexCondenser positionalIndexCondenser;

	private static List<String> lines = Arrays.asList(
			"abc: 30272385 - 572; ",
			"abc: 30272395 - 159; ",
			"abc: 30272432 - 150; 30273218 - 107, 112; 30273304 - 127, 131, 134; 30275584 - 223; 30275656 - 3991; 30275677 - 70; ",
			"abc: 30272638 - 1573; 30272727 - 41; 30272832 - 2; ",
			"abc: 30272816 - 488; 30274637 - 230; ",
			"abc: 30272859 - 915; ",
			"abc: 30273312 - 353; ",
			"abc: 30273553 - 3096; ",
			"");

	private String expectedOutput= "abc: 30272385 - 572; 30272395 - 159; 30272432 - 150; 30273218 - 107, 112; 30273304 - 127, 131, 134; 30275584 - 223; 30275656 - 3991; 30275677 - 70; 30272638 - 1573; 30272727 - 41; 30272832 - 2; 30272816 - 488; 30274637 - 230; 30272859 - 915; 30273312 - 353; 30273553 - 3096; ";

	@Test
	public void testCondenseFile() throws IOException
	{
		PositionalIndexCondenserImpl impl = (PositionalIndexCondenserImpl) positionalIndexCondenser;

		StringWriter writer = new StringWriter();
		impl.condenseFile(lines.iterator(), writer);

		String output = writer.toString();
		assertEquals(expectedOutput.trim(), output.trim());
	}
}