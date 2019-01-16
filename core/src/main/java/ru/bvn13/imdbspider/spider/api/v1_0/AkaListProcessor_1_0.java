package ru.bvn13.imdbspider.spider.api.v1_0;

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
    Task taskByDataType(AkaListDataType dataType) {
        return null;
    }

    @Override
    public void fillUpImdbObject(AkaList imdbObject, Task task) {

    }

}
