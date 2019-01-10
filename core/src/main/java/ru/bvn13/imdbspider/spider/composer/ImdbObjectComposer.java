package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.ImdbObject;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 10.01.2019
 */
public interface ImdbObjectComposer<C extends ImdbObject> {

    C compose(Task task) throws ImdbSpiderException;

}
