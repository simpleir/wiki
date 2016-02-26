package com.simpleir.wiki.process.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simpleir.wiki.TestConfiguration;
import com.simpleir.wiki.process.InvertedIndexCondenser;
import com.simpleir.wiki.process.impl.InvertedIndexCondenserImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
public class InvertedIndexCondenserImplTest
{
	@Autowired
	private InvertedIndexCondenser invertedIndexCondenser;

	private static List<String> lines = Arrays.asList(
			"escobar: 728, 1491, 1787, 2140, 2176, 2395",
			"escobar: 3865, 4068, 4157, 4391",
			"escobar: 5844, 5845, 5946",
			"escobar: 6652, 7661, 8219, 8356, 8357",
			"escobar: 14227",
			"escobar: 15846, 15996",
			"escobar: 16883",
			"escobar: 19660",
			"escobar: 20610, 21069, 21461",
			"escobar: 22345, 22523, 22568, 23333, 23421",
			"escobar: 24650, 25532",
			"escobar: 26714, 27259",
			"escobar: 27948, 28653",
			"escobar: 30085, 30847",
			"");

	private static String expectedOutput = "escobar: 728, 1491, 1787, 2140, 2176, 2395, 3865, 4068, 4157, 4391, 5844, 5845, 5946, 6652, 7661, 8219, 8356, 8357, 14227, 15846, 15996, 16883, 19660, 20610, 21069, 21461, 22345, 22523, 22568, 23333, 23421, 24650, 25532, 26714, 27259, 27948, 28653, 30085, 30847";

	@Test
	public void testCondenseFile() throws IOException
	{
		InvertedIndexCondenserImpl impl = (InvertedIndexCondenserImpl) invertedIndexCondenser;

		StringWriter writer = new StringWriter();
		impl.condenseFile(lines.iterator(), writer);

		String output = writer.toString();
		assertEquals(output.trim(), expectedOutput.trim());
	}
}