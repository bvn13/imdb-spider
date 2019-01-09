package ru.bvn13.imdbspider.spider.tasker;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.extractor.HtmlExtractorException;
import ru.bvn13.imdbspider.spider.extractor.HtmlExtractor;
import ru.bvn13.imdbspider.spider.processor.HtmlProcessor;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Worker implements Callable<List<Task>> {

    private final String url;
    private final List<Task> tasks;

    private final HtmlExtractor htmlExtractor;
    private final HtmlProcessor htmlProcessor;

    private final ExecutorService executor;

    public Worker(String url, List<Task> tasks) {
        this.url = url;
        this.tasks = tasks;

        this.htmlExtractor = new HtmlExtractor();
        this.htmlProcessor = new HtmlProcessor();

        this.executor = Executors.newCachedThreadPool();
    }


    @Override
    public List<Task> call() throws Exception {
        Future<String> result = executor.submit(() -> htmlExtractor.getHtml(url));
        while (!result.isDone()) {
            Thread.yield();
        }

        final String html;
        try {
            html = result.get();
        } catch (InterruptedException e) {
            throw new ImdbSpiderException("Interrupted", e);
        } catch (ExecutionException e) {
            throw new HtmlExtractorException("Exception has been occurred", e);
        }

        tasks.parallelStream().forEach(task -> {
            Future<String> taskResult = executor.submit(() -> htmlProcessor.process(html, task.getXpathPattern()));
            while (!taskResult.isDone()) {
                Thread.yield();
            }
            try {
                task.setResult(taskResult.get());
            } catch (InterruptedException e) {
                task.setException(new ImdbSpiderException("Interrupted", e));
            } catch (ExecutionException e) {
                task.setException(new ImdbSpiderException("Exception has been occurred", e));
            }
        });

        return tasks;
    }
}
