package ru.bvn13.imdbspider;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.imdb.MovieList;
import ru.bvn13.imdbspider.spider.api.v1_0.ApiFactory_1_0;
import ru.bvn13.imdbspider.spider.composer.ImdbObjectComposerFactory;
import ru.bvn13.imdbspider.spider.composer.MovieListComposer;
import ru.bvn13.imdbspider.spider.processor.JsoupHtmlProcessor;
import ru.bvn13.imdbspider.spider.tasker.Manager;
import ru.bvn13.imdbspider.spider.tasker.Task;
import ru.bvn13.imdbspider.spider.api.ApiFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author boyko_vn at 09.01.2019
 */
public class ImdbSpider {

    private boolean isDebug;

    private Manager manager;

    private ApiFactory apiFactory;
    private ImdbObjectComposerFactory imdbObjectComposerFactory;

    public static ImdbSpider withApi_1_0() {
        ApiFactory apiFactory = new ApiFactory_1_0(new JsoupHtmlProcessor());
        return new ImdbSpider(apiFactory, new ImdbObjectComposerFactory(apiFactory));
    }


    public ImdbSpider(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) {
        this.apiFactory = apiFactory;
        this.imdbObjectComposerFactory = imdbObjectComposerFactory;

        manager = new Manager();
    }

    public boolean isDebug() {
        return isDebug;
    }

    public ImdbSpider setDebug(boolean debug) {
        isDebug = debug;
        manager.setDebug(isDebug);
        apiFactory.setDebug(isDebug);
        return this;
    }

    public ImdbSpider addHttpRequestHeader(String key, String value) {
        manager.addHttpRequestHeader(key, value);
        return this;
    }

    public MovieList searchMovieByTitle(String title) throws ImdbSpiderException {
        return searchMovieByTitle(title, 0);
    }

    public MovieList searchMovieByTitle(String title, int maxCount) throws ImdbSpiderException {
        return searchMovieByTitle(title, maxCount, EnumSet.of(MovieDataType.TITLE));
    }

    public MovieList searchMovieByTitle(String title, int maxCount, MovieDataType... dataTypes) throws ImdbSpiderException {
        return searchMovieByTitle(title, maxCount, EnumSet.copyOf(Arrays.asList(dataTypes)));
    }

    public MovieList searchMovieByTitle(String title, int maxCount, EnumSet<MovieDataType> dataTypes) throws ImdbSpiderException {

        List<Task> tasks = apiFactory.createTasksForSearchMovieByTitle(title, maxCount, dataTypes);

        LocalDateTime dateStart = LocalDateTime.now();
        manager.processTasks(tasks);
        LocalDateTime dateEnd = LocalDateTime.now();
        Duration diff = Duration.between(dateStart, dateEnd);
        System.out.println("TIME SPENT: "+(diff.toMillis())+" msec");


        MovieListComposer movieListComposer = (MovieListComposer) imdbObjectComposerFactory.getComposer(MovieList.class);
        MovieList movieList = movieListComposer.compose(tasks.get(0));

        return movieList;

    }



}
