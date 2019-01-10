package ru.bvn13.imdbspider.spider.tasker;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.extractor.HtmlExtractorException;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;
import ru.bvn13.imdbspider.spider.extractor.HtmlExtractor;
import ru.bvn13.imdbspider.spider.processor.HtmlProcessor;
import ru.bvn13.imdbspider.spider.processor.JsoupHtmlProcessor;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Worker {

    private final String url;
    private final List<Task> tasks;

    private final HtmlExtractor htmlExtractor;
    private final HtmlProcessor htmlProcessor;

    private final ExecutorService executor;

    public Worker(String url, List<Task> tasks) {
        this.url = url;
        this.tasks = tasks;

        this.htmlExtractor = new HtmlExtractor();
        this.htmlProcessor = new JsoupHtmlProcessor();

        this.executor = Executors.newCachedThreadPool();
    }


    public Boolean run() throws HtmlExtractorException {

        final String html = htmlExtractor.getHtml(url);

        tasks.parallelStream().forEach(task -> {

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
