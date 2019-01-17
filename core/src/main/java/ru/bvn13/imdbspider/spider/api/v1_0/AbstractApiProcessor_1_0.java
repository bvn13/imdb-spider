package ru.bvn13.imdbspider.spider.api.v1_0;

import ru.bvn13.imdbspider.imdb.DataType;
import ru.bvn13.imdbspider.imdb.ImdbObject;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 15.01.2019
 */
abstract public class AbstractApiProcessor_1_0<C extends ImdbObject, D extends Enum<?> & DataType> {

    protected boolean isDebug;

    private ApiFactory_1_0 apiFactory;

    public AbstractApiProcessor_1_0(ApiFactory_1_0 apiFactory) {
        this.apiFactory = apiFactory;
    }

    public ApiFactory_1_0 getApiFactory() {
        return apiFactory;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    abstract void fillUpImdbObject(C imdbObject, Task task);

    Task taskByDataType(D dataType) {
        return this.taskByDataType(dataType, null);
    }

    abstract Task taskByDataType(D dataType, String imdbObjectParentId);

    void initializeNestedImdbObjectParentId(Task task, String parentId) {
        if (task != null) {
            task.getNestedTasks().forEach(nestedTask -> {
                nestedTask.setImdbObjectParentId(parentId);
            });
        }
    }
}
