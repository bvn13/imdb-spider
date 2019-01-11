package ru.bvn13.imdbspider;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.imdb.MovieList;
import ru.bvn13.imdbspider.imdb.MovieListDataType;
import ru.bvn13.imdbspider.spider.api.v1_0.ApiFactory_1_0;
import ru.bvn13.imdbspider.spider.composer.ImdbObjectComposerFactory;
import ru.bvn13.imdbspider.spider.composer.MovieListComposer;
import ru.bvn13.imdbspider.spider.tasker.Manager;
import ru.bvn13.imdbspider.spider.tasker.Task;
import ru.bvn13.imdbspider.spider.api.ApiFactory;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author boyko_vn at 09.01.2019
 */
public class ImdbSpider {

    private static final String URL_SEARCH_TITLE = "https://www.imdb.com/find?ref_=nv_sr_fn&q={{title}}&s=tt";

    private Manager manager;

    private ApiFactory apiFactory;
    private ImdbObjectComposerFactory imdbObjectComposerFactory;

    public static ImdbSpider withApi_1_0() {
        ApiFactory apiFactory = new ApiFactory_1_0();
        return new ImdbSpider(apiFactory, new ImdbObjectComposerFactory(apiFactory));
    }


    public ImdbSpider(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) {
        this.apiFactory = apiFactory;
        this.imdbObjectComposerFactory = imdbObjectComposerFactory;

        manager = new Manager();
    }

    public MovieList searchMovieByTitle(String title) throws ImdbSpiderException {
        return searchMovieByTitle(title, 0);
    }

    public MovieList searchMovieByTitle(String title, int maxCount) throws ImdbSpiderException {
        return searchMovieByTitle(title, maxCount, EnumSet.of(MovieDataType.TITLE));
    }

    public MovieList searchMovieByTitle(String title, int maxCount, EnumSet<MovieDataType> dataTypes) throws ImdbSpiderException {

        String url = URL_SEARCH_TITLE.replace("{{title}}", URLEncoder.encode(title, Charset.forName("utf-8")));

        List<Task> tasks = new ArrayList<>();

        try {
            Task t1 = apiFactory.taskByDataType(MovieListDataType.ELEMENTS);
            t1.setUrl(url);
            if (maxCount > 0) {
                t1.setRestrictionByCount(maxCount);
            }
            tasks.add(t1);
        } catch (DataTypeNotSupportedException e) {
            throw e;
        }

        LocalDateTime dateStart = LocalDateTime.now();
        try {
            manager.processTasks(tasks);
        } catch (ExecutionException | InterruptedException e) {
            throw new ImdbSpiderException("Error has been occurred!", e);
        }
        LocalDateTime dateEnd = LocalDateTime.now();
        Duration diff = Duration.between(dateStart, dateEnd);
        System.out.println("TIME SPENT: "+(diff.toMillis())+" msec");


        MovieListComposer movieListComposer = (MovieListComposer) imdbObjectComposerFactory.getComposer(MovieList.class);
        MovieList movieList = movieListComposer.compose(tasks.get(0));

        return movieList;

    }

}
