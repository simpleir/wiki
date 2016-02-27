package com.simpleir.wiki;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.simpleir.wiki.preprocess.MediaWikiToPlainTextExtractor;
import com.simpleir.wiki.preprocess.XmlToMediaWikiExtractor;
import com.simpleir.wiki.process.GroupIndicesByTermExtractor;
import com.simpleir.wiki.process.InvertedIndexCondenser;
import com.simpleir.wiki.process.PlainTextToInvertedIndexExtractor;
import com.simpleir.wiki.process.PlainTextToPositionalIndexExtractor;
import com.simpleir.wiki.process.PositionalIndexCondenser;

@SuppressWarnings("unused")
public class App 
{
	public static void main( String[] args ) throws Exception
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configurations.class);
//        context.getBean(XmlToMediaWikiExtractor.class).run();
//        context.getBean(MediaWikiToPlainTextExtractor.class).run();
//        context.getBean(PlainTextToInvertedIndexExtractor.class).run();
//        context.getBean(PlainTextToPositionalIndexExtractor.class).run();
//        context.getBean(GroupIndicesByTermExtractor.class).run();
//        context.getBean(InvertedIndexCondenser.class).run();
//        context.getBean(PositionalIndexCondenser.class).run();
		System.out.println("Installed");
        context.close();
    }
}
