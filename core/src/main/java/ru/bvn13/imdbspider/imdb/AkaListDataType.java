package ru.bvn13.imdbspider.imdb;

/**
 * Created by bvn13 on 16.01.2019.
 */
public enum AkaListDataType implements DataType {

    ELEMENTS("elements")
    ;

    private String value;

    AkaListDataType(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }
}
