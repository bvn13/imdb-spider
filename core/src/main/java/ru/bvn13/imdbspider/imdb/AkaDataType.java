package ru.bvn13.imdbspider.imdb;

/**
 * Created by bvn13 on 16.01.2019.
 */
public enum AkaDataType implements DataType {

    ID("id"),
    TITLE("title")
    ;

    private String value;

    AkaDataType(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }
}
