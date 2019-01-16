package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;

/**
 * @author boyko_vn at 09.01.2019
 */
public abstract class ImdbObject<DT extends Enum<DT> & DataType> {

    protected String html;

    protected EnumSet<DT> retrievedDataTypes;

    protected String id;
    protected String url;

    public ImdbObject() {
        this.initRetrievedDataTypes();
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    protected abstract void initRetrievedDataTypes();

    public boolean isDataTypeRetrieved(DT dataType) {
        return (retrievedDataTypes != null && retrievedDataTypes.contains(dataType));
    }

    public EnumSet<DT> getRetrievedDataTypes() {
        return retrievedDataTypes;
    }

    public void setRetrievedDataTypes(EnumSet<DT> retrievedDataTypes) {
        this.retrievedDataTypes = retrievedDataTypes;
    }

    public void addDataType(DT movieDataType) {
        if (!this.retrievedDataTypes.contains(movieDataType)) {
            this.retrievedDataTypes.add(movieDataType);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
