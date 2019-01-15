package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.spider.api.ApiFactory;

/**
 * @author boyko_vn at 15.01.2019
 */
abstract public class AbstractImdbObjectComposer {

    protected ApiFactory apiFactory;
    protected ImdbObjectComposerFactory imdbObjectComposerFactory;

    public AbstractImdbObjectComposer(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) throws ComposerNotFoundException {
        this.apiFactory = apiFactory;
        this.imdbObjectComposerFactory = imdbObjectComposerFactory;
    }


}
