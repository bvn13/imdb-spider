package ru.bvn13.imdbspider.spider.tasker;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.extractor.HtmlExtractorException;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;
import ru.bvn13.imdbspider.spider.extractor.HtmlExtractor;
import ru.bvn13.imdbspider.spider.processor.HtmlProcessor;
import ru.bvn13.imdbspider.spider.processor.JsoupHtmlProcessor;

import java.util.List;
import java.util.Map;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Worker {

    private boolean isDebug;

    private final String url;
    private final List<Task> tasks;

    private final HtmlExtractor htmlExtractor;
    private final HtmlProcessor htmlProcessor;

    public Worker(String url, List<Task> tasks) {
        this.url = url;
        this.tasks = tasks;

        this.htmlExtractor = new HtmlExtractor();
        this.htmlProcessor = new JsoupHtmlProcessor();
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public Boolean run(Map<String, String> httpRequestHeaders) throws HtmlExtractorException {

        final String html = htmlExtractor.getHtml(url, httpRequestHeaders);

        tasks.parallelStream().forEach(task -> {

            if (isDebug) {
                task.setHtml(html);
            }

            try {
                if (task.getCssSelector() != null && !task.getCssSelector().isEmpty()) {
                    task.setCssSelectorResult(htmlProcessor.process(html, task.getCssSelector()));
                }

                if (task.getPostprocess() != null) {
                    task.getPostprocess().accept(task, html);
                }
            } catch (HtmlProcessorException e) {
                task.setException(new ImdbSpiderException(e));
                e.printStackTrace();
            }

        });

        return true;
    }
}
