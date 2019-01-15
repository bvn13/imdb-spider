package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.*;
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
    private MovieComposer movieComposer;
    private TaglineListComposer taglineListComposer;
    private TaglineComposer taglineComposer;

    public <C extends ImdbObject> ImdbObjectComposer getComposer(Class<C> clazz) throws ComposerNotFoundException {
        if (clazz.isAssignableFrom(MovieList.class)) {
            if (movieListComposer == null) {
                movieListComposer = new MovieListComposer(apiFactory, this);
            }
            return movieListComposer;
        } if (clazz.isAssignableFrom(Movie.class)) {
            if (movieComposer == null) {
                movieComposer = new MovieComposer(apiFactory, this);
            }
            return movieComposer;
        } if (clazz.isAssignableFrom(TaglineList.class)) {
            if (taglineListComposer == null) {
                taglineListComposer = new TaglineListComposer(apiFactory, this);
            }
            return taglineListComposer;
        } if (clazz.isAssignableFrom(Tagline.class)) {
            if (taglineComposer == null) {
                taglineComposer = new TaglineComposer(apiFactory, this);
            }
            return taglineComposer;
        }

        throw new ComposerNotFoundException(String.format("Composer not found: %s", clazz.getName()));
    }

}
