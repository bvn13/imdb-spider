package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.Aka;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 17.01.2019
 */
public class AkaComposer extends AbstractImdbObjectComposer implements ImdbObjectComposer<Aka> {
    public AkaComposer(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) throws ComposerNotFoundException {
        super(apiFactory, imdbObjectComposerFactory);
    }

    @Override
    public Aka compose(Task task) throws ImdbSpiderException {
        Aka aka = new Aka();
        this.apiFactory.fillUpImdbObject(aka, task);
        for (Task nestedTask : task.getNestedTasks()) {
            this.apiFactory.fillUpImdbObject(aka, nestedTask);
        }
        return aka;
    }
}
