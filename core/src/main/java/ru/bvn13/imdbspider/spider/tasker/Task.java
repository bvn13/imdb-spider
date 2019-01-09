package ru.bvn13.imdbspider.spider.tasker;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.DataType;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Task {

    private String url;

    private String xpathPattern;
    private DataType dataType;
    private String result;

    private ImdbSpiderException exception;

    public Task() {
    }

    public Task(String xpathPattern) {
        this.xpathPattern = xpathPattern;
    }

    public Task(String url, String xpathPattern) {
        this.url = url;
        this.xpathPattern = xpathPattern;
    }

    public Task(String url, String xpathPattern, DataType dataType) {
        this.url = url;
        this.xpathPattern = xpathPattern;
        this.dataType = dataType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXpathPattern() {
        return xpathPattern;
    }

    public void setXpathPattern(String xpathPattern) {
        this.xpathPattern = xpathPattern;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ImdbSpiderException getException() {
        return exception;
    }

    public void setException(ImdbSpiderException exception) {
        this.exception = exception;
    }
}
