package ru.bvn13.imdbspider.spider.api;

import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.imdb.DataType;
import ru.bvn13.imdbspider.imdb.ImdbObject;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 09.01.2019
 */
public interface ApiFactory {

    Task taskByDataType(DataType dataType) throws DataTypeNotSupportedException;

    void fulfillImdbObject(ImdbObject imdbObject, Task task);

}
