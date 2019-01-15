package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieList;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 10.01.2019
 */
public class MovieListComposer extends AbstractImdbObjectComposer implements ImdbObjectComposer<MovieList> {

    private MovieComposer movieComposer;

    public MovieListComposer(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) throws ComposerNotFoundException {
        super(apiFactory, imdbObjectComposerFactory);

        this.movieComposer = (MovieComposer) this.imdbObjectComposerFactory.getComposer(Movie.class);
    }

    @Override
    public MovieList compose(Task task) {
        MovieList movieList = new MovieList();
        apiFactory.fillUpImdbObject(movieList, task);

        for (Task movieTask : task.getNestedTasks()) {
            try {
                movieList.getMovies().add(this.movieComposer.compose(movieTask));
            } catch (ImdbSpiderException e) {
                e.printStackTrace();
            }
        }
        return movieList;
    }

}
