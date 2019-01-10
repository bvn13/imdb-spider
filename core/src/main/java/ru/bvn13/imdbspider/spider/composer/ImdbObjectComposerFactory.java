package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.ImdbObject;
import ru.bvn13.imdbspider.imdb.MovieList;
import ru.bvn13.imdbspider.spider.api.ApiFactory;

/**
 * @author boyko_vn at 10.01.2019
 */
public class ImdbObjectComposerFactory {

    private ApiFactory apiFactory;

    public ImdbObjectComposerFactory(ApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    private MovieListComposer movieListComposer;

    public <C extends ImdbObject> ImdbObjectComposer getComposer(Class<C> clazz) throws ComposerNotFoundException {
        if (clazz.isAssignableFrom(MovieList.class)) {
            if (movieListComposer == null) {
                movieListComposer = new MovieListComposer(apiFactory);
                return movieListComposer;
            }
        }

        throw new ComposerNotFoundException(String.format("Composer not found: %s", clazz.getClass().getName()));
    }

}
