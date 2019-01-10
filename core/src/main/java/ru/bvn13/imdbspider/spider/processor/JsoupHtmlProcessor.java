package ru.bvn13.imdbspider.spider.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;

/**
 * @author boyko_vn at 10.01.2019
 */
public class JsoupHtmlProcessor implements HtmlProcessor {

    @Override
    public Elements process(String html, String pattern) throws HtmlProcessorException {
        Document doc = Jsoup.parse(html, "UTF-8");
        Elements result = doc.select(pattern);
        return result;
    }

}
