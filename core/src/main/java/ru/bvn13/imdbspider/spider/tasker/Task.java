package ru.bvn13.imdbspider.spider.tasker;

import org.jsoup.select.Elements;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Task {

    private String imdbObjectParentId;
    private String url;
    private String html;

    public enum SOURCE_TYPE {
        URL,
        HTML
    }

    private SOURCE_TYPE sourceType = SOURCE_TYPE.URL;

    private String sourceHtml;

    private DataType dataType;
    private String cssSelector;
    private Elements cssSelectorResult;

    private Integer restrictionByCount;

    private Class resultType;
    private Object result;

    private ImdbSpiderException exception;

    private BiConsumer<Task, String> postprocess;

    private Task parentTask;
    private List<Task> nestedTasks;

    public Task() {
    }

    public Task(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public Task(String url, String cssSelector) {
        this.url = url;
        this.cssSelector = cssSelector;
    }

    public Task(String url, String cssSelector, DataType dataType) {
        this.url = url;
        this.cssSelector = cssSelector;
        this.dataType = dataType;
    }

    public String getImdbObjectParentId() {
        return imdbObjectParentId;
    }

    public void setImdbObjectParentId(String imdbObjectParentId) {
        this.imdbObjectParentId = imdbObjectParentId;
    }

    public SOURCE_TYPE getSourceType() {
        return sourceType;
    }

    public Task setSourceType(SOURCE_TYPE sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public String getSourceHtml() {
        return sourceHtml;
    }

    public Task setSourceHtml(String sourceHtml) {
        this.sourceHtml = sourceHtml;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Task setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getHtml() {
        return html;
    }

    public Task setHtml(String html) {
        this.html = html;
        return this;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public Task setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
        return this;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Task setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public Elements getCssSelectorResult() {
        return cssSelectorResult;
    }

    public Task setCssSelectorResult(Elements cssSelectorResult) {
        this.cssSelectorResult = cssSelectorResult;
        return this;
    }

    public ImdbSpiderException getException() {
        return exception;
    }

    public Task setException(ImdbSpiderException exception) {
        this.exception = exception;
        return this;
    }

    public BiConsumer<Task, String> getPostprocess() {
        return postprocess;
    }

    public Task setPostprocess(BiConsumer<Task, String> postprocess) {
        this.postprocess = postprocess;
        return this;
    }

    public Class getResultType() {
        return resultType;
    }

    public Task setResultType(Class resultType) {
        this.resultType = resultType;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public Task setResult(Object result) {
        this.result = result;
        return this;
    }

    public boolean hasNextTasks() {
        return (nestedTasks != null && !nestedTasks.isEmpty());
    }

    public List<Task> getNestedTasks() {
        if (nestedTasks == null) {
            nestedTasks = new ArrayList<>();
        }
        return nestedTasks;
    }

    public Task setNestedTasks(List<Task> nestedTasks) {
        this.nestedTasks = nestedTasks;
        return this;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public Task setParentTask(Task parentTask) {
        this.parentTask = parentTask;
        return this;
    }

    public Integer getRestrictionByCount() {
        return restrictionByCount;
    }

    public Task setRestrictionByCount(Integer restrictionByCount) {
        this.restrictionByCount = restrictionByCount;
        return this;
    }
}
