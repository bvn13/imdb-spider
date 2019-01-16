package ru.bvn13.imdbspider.spider.api.v1_0;

/**
 * @author boyko_vn at 15.01.2019
 */
abstract public class AbstractApiProcessor_1_0 {

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
}
