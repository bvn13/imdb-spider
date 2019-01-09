package ru.bvn13.imdbspider;

import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.spider.api.v1_0.ApiFactory_1_0;
import ru.bvn13.imdbspider.spider.tasker.Manager;
import ru.bvn13.imdbspider.spider.tasker.Task;
import ru.bvn13.imdbspider.spider.api.ApiFactory;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author boyko_vn at 09.01.2019
 */
public class ImdbSpider {

    private static final String URL_MAIN = "https://www.imdb.com/";
    private static final String URL_SEARCH_TITLE = "https://www.imdb.com/find?ref_=nv_sr_fn&q={{title}}&s=tt";

    private Manager manager;

    private ApiFactory apiFactory;

    public static ImdbSpider withApi_1_0() {
        return new ImdbSpider(new ApiFactory_1_0());
    }


    public ImdbSpider(ApiFactory apiFactory) {

        manager = new Manager();

    }

    public List<Movie> searchMovieByTitle(String title) {
        return searchMovieByTitle(title, 10);
    }

    public List<Movie> searchMovieByTitle(String title, int maxCount) {
        return searchMovieByTitle(title, maxCount, EnumSet.of(MovieDataType.TITLE));
    }

    public List<Movie> searchMovieByTitle(String title, int maxCount, EnumSet<MovieDataType> dataTypes) {

        String url = URL_SEARCH_TITLE.replace("{{title}}", URLEncoder.encode(title, Charset.forName("utf-8")));

        List<Task> tasks = new ArrayList<>();

        for (MovieDataType mdt : MovieDataType.values()) {
            if (dataTypes.contains(mdt)) {
                try {
                    tasks.add(apiFactory.taskByDataType(mdt));
                } catch (DataTypeNotSupportedException e) {
                    //do nothing
                    e.printStackTrace();
                }
            }
        }

        try {
            tasks = manager.processTasks(tasks);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

}
