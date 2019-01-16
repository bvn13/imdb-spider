package ru.bvn13.imdbspider.spider.api.v1_0;

import ru.bvn13.imdbspider.imdb.Aka;
import ru.bvn13.imdbspider.imdb.AkaDataType;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * Created by bvn13 on 16.01.2019.
 */
public class AkaProcessor_1_0 extends AbstractApiProcessor_1_0<Aka, AkaDataType> {
    public AkaProcessor_1_0(ApiFactory_1_0 apiFactory) {
        super(apiFactory);
    }

    @Override
    Task taskByDataType(AkaDataType dataType) {
        return null;
    }

    @Override
    void fillUpImdbObject(Aka imdbObject, Task task) {

    }
}
