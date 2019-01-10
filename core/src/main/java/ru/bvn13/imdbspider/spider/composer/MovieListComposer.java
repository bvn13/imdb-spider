package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieList;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 10.01.2019
 */
public class MovieListComposer implements ImdbObjectComposer<MovieList> {

    private ApiFactory apiFactory;

    public MovieListComposer(ApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    @Override
    public MovieList compose(Task task) {
        MovieList movieList = new MovieList();
        apiFactory.fillUpImdbObject(movieList, task);

        for (Task movieTask : task.getNestedTasks()) {
            Movie movie = new Movie();
            movieList.getMovies().add(movie);
            apiFactory.fillUpImdbObject(movie, movieTask);
            for (Task nestedTask : movieTask.getNestedTasks()) {
                apiFactory.fillUpImdbObject(movie, nestedTask);
            }
        }
        return movieList;
    }

}
