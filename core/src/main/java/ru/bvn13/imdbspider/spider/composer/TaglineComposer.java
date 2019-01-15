package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.Tagline;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 15.01.2019
 */
public class TaglineComposer extends AbstractImdbObjectComposer implements ImdbObjectComposer<Tagline> {

    public TaglineComposer(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) throws ComposerNotFoundException {
        super(apiFactory, imdbObjectComposerFactory);

    }

    @Override
    public Tagline compose(Task task) throws ImdbSpiderException {
        Tagline tagline = new Tagline();
        this.apiFactory.fillUpImdbObject(tagline, task);
        for (Task nestedTask : task.getNestedTasks()) {
            this.apiFactory.fillUpImdbObject(tagline, nestedTask);
        }
        return tagline;
    }
}
