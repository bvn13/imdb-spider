package ru.bvn13.imdbspider.spider.processor;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;
import ru.bvn13.imdbspider.exceptions.processor.HtmlToXmlConvertionException;
import ru.bvn13.imdbspider.exceptions.processor.PatternEvaluationException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author boyko_vn at 09.01.2019
 */
public class HtmlProcessor {

    public String process(final String html, final String pattern) throws HtmlProcessorException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new HtmlProcessorException(e);
        }
        Document xml = null;
        try {
            xml = db.parse(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
        } catch (SAXException e) {
            throw new HtmlToXmlConvertionException("Html parsing exception", e);
        } catch (IOException e) {
            throw new HtmlToXmlConvertionException("Html reading exception", e);
        }

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        String result = null;
        try {
            result = (String) xpath.evaluate(pattern, xml, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new PatternEvaluationException(String.format("Could not evaluate pattern: %s", pattern), e);
        }

        return result;
    }

}
