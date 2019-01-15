package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.Tagline;
import ru.bvn13.imdbspider.imdb.TaglineList;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 15.01.2019
 */
public class TaglineListComposer extends AbstractImdbObjectComposer implements ImdbObjectComposer<TaglineList> {

    private TaglineComposer taglineComposer;

    public TaglineListComposer(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) throws ComposerNotFoundException {
        super(apiFactory, imdbObjectComposerFactory);

        this.taglineComposer = (TaglineComposer) this.imdbObjectComposerFactory.getComposer(Tagline.class);
    }

    @Override
    public TaglineList compose(Task task) {
        TaglineList taglineList = new TaglineList();
        if (task.getNestedTasks().size() > 0) {
            Task taglineListTag = task.getNestedTasks().get(0);

            this.apiFactory.fillUpImdbObject(taglineList, taglineListTag);

            for (Task nestedTask : taglineListTag.getNestedTasks()) {
                try {
                    taglineList.getTaglines().add(this.taglineComposer.compose(nestedTask));
                } catch (ImdbSpiderException e) {
                    e.printStackTrace();
                }
            }

        }
        return taglineList;
    }
}
