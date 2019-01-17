package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import ru.bvn13.imdbspider.imdb.AkaDataType;
import ru.bvn13.imdbspider.imdb.AkaList;
import ru.bvn13.imdbspider.imdb.AkaListDataType;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * Created by bvn13 on 16.01.2019.
 */
public class AkaListProcessor_1_0 extends AbstractApiProcessor_1_0<AkaList, AkaListDataType> {

    public AkaListProcessor_1_0(ApiFactory_1_0 apiFactory) {
        super(apiFactory);
    }

    @Override
    Task taskByDataType(AkaListDataType dataType, String imdbObjectParentId) {
        Task t = new Task();
        t.setImdbObjectParentId(imdbObjectParentId);
        t.setDataType(dataType);
        switch (dataType) {
            case ELEMENTS:
                t.setCssSelector("#releaseinfo_content > table.ipl-zebra-list.akas-table-test-only > tbody > tr");
                t.setPostprocess((task, s) -> {
                    int i = 0;
                    for (Element element : task.getCssSelectorResult()) {
                        Task akaTask = getApiFactory().getAkaProcessor().taskByDataType(AkaDataType.ID, task.getImdbObjectParentId())
                                .setUrl(task.getUrl())
                                .setResultType(String.class)
                                .setResult(String.format("%d", (i++)))
                                .setParentTask(task);
                        task.getNestedTasks().add(akaTask);

                        for (AkaDataType value : AkaDataType.values()) {
                            if (!value.equals(AkaDataType.ID)) {
                                Task newTask = getApiFactory().getAkaProcessor().taskByDataType(value, task.getImdbObjectParentId())
                                        .setSourceType(Task.SOURCE_TYPE.HTML)
                                        .setUrl(task.getUrl())
                                        .setSourceHtml(element.html())
                                        .setParentTask(akaTask);
                                akaTask.getNestedTasks().add(newTask);
                            }
                        }
                    }
                });
                break;
        }
        return t;
    }

    @Override
    public void fillUpImdbObject(AkaList akaList, Task task) {
        switch ((AkaListDataType) task.getDataType()) {
            case ELEMENTS:
                akaList.setUrl(task.getUrl());
                akaList.getRetrievedDataTypes().add((AkaListDataType) task.getDataType());
                break;
        }
    }

}
