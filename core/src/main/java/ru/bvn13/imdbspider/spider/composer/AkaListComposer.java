package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.Aka;
import ru.bvn13.imdbspider.imdb.AkaList;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 17.01.2019
 */
public class AkaListComposer extends AbstractImdbObjectComposer implements ImdbObjectComposer<AkaList> {

    private AkaComposer akaComposer;

    public AkaListComposer(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) throws ComposerNotFoundException {
        super(apiFactory, imdbObjectComposerFactory);

        akaComposer = (AkaComposer) this.imdbObjectComposerFactory.getComposer(Aka.class);
    }

    @Override
    public AkaList compose(Task task) throws ImdbSpiderException {
        AkaList akaList = new AkaList();
        if (task.getNestedTasks().size() > 0) {
            Task akaListTask = task.getNestedTasks().get(0);

            this.apiFactory.fillUpImdbObject(akaList, task);
            for (Task nestedTask : akaListTask.getNestedTasks()) {
                akaList.getAkas().add(akaComposer.compose(nestedTask));
            }
        }
        return akaList;
    }
}
