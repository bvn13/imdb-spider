package ru.bvn13.imdbspider.spider.processor;

import org.jsoup.select.Elements;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;

/**
 * @author boyko_vn at 10.01.2019
 */
public interface HtmlProcessor {

    Elements process(final String html, final String pattern) throws HtmlProcessorException;

}
