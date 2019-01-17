package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import ru.bvn13.imdbspider.imdb.TaglineDataType;
import ru.bvn13.imdbspider.imdb.TaglineList;
import ru.bvn13.imdbspider.imdb.TaglineListDataType;
import ru.bvn13.imdbspider.spider.tasker.Task;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author boyko_vn at 15.01.2019
 */
public class TaglineListProcessor_1_0 extends AbstractApiProcessor_1_0<TaglineList, TaglineListDataType> {

    public TaglineListProcessor_1_0(ApiFactory_1_0 apiFactory) {
        super(apiFactory);
    }

    @Override
    Task taskByDataType(TaglineListDataType taglineListDataType, String imdbObjectParentId) {
        Task t = new Task();
        t.setImdbObjectParentId(imdbObjectParentId);
        t.setDataType(taglineListDataType);
        switch (taglineListDataType) {
            case ELEMENTS:
                t.setCssSelector("#taglines_content > div.soda");
                AtomicInteger i = new AtomicInteger(0);
                t.setPostprocess((task, s) -> {
                    for (Element element : task.getCssSelectorResult()) {
                        Task newTaskId = getApiFactory().getTaglineProcessor().taskByDataType(TaglineDataType.ID, task.getImdbObjectParentId())
                                .setParentTask(task)
                                .setUrl(task.getUrl())
                                .setResult(String.format("%d", i.getAndAdd(1)));
                        task.getNestedTasks().add(newTaskId);

                        Task newTaskText = getApiFactory().getTaglineProcessor().taskByDataType(TaglineDataType.TEXT, task.getImdbObjectParentId())
                                .setParentTask(task)
                                .setUrl(task.getUrl())
                                .setResult(element.text());
                        newTaskId.getNestedTasks().add(newTaskText);
                    }
                });
                break;
        }
        return t;
    }

    @Override
    void fillUpImdbObject(TaglineList taglineList, Task task) {
        switch ((TaglineListDataType) task.getDataType()) {
            case ELEMENTS:
                taglineList.setUrl(task.getUrl());
                taglineList.getRetrievedDataTypes().add((TaglineListDataType) task.getDataType());
                break;
        }
    }


}
