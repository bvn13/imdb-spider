package ru.bvn13.imdbspider.spider.api.v1_0;

import ru.bvn13.imdbspider.imdb.Tagline;
import ru.bvn13.imdbspider.imdb.TaglineDataType;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 15.01.2019
 */
public class TaglineProcessor_1_0 extends AbstractApiProcessor_1_0<Tagline, TaglineDataType> {

    public TaglineProcessor_1_0(ApiFactory_1_0 apiFactory) {
        super(apiFactory);
    }

    @Override
    Task taskByDataType(TaglineDataType taglineDataType) {
        Task t = new Task();
        t.setDataType(taglineDataType);
        switch (taglineDataType) {
            case ID:
                //
                break;
            case TEXT:
                t.setPostprocess((task, s) -> {
                    task.setResult(((String)task.getResult()).trim());
                });
                break;
        }
        return t;
    }

    @Override
    void fillUpImdbObject(Tagline tagline, Task task) {
        switch ((TaglineDataType) task.getDataType()) {
            case ID:
                tagline.setUrl(task.getUrl());
                tagline.setId((String) task.getResult());
                tagline.getRetrievedDataTypes().add((TaglineDataType) task.getDataType());
                break;
            case TEXT:
                tagline.setUrl(task.getUrl());
                tagline.setText((String) task.getResult());
                tagline.getRetrievedDataTypes().add((TaglineDataType) task.getDataType());
                break;
        }
    }


}
