package ru.bvn13.imdbspider.imdb;

/**
 * @author boyko_vn at 15.01.2019
 */
public enum TaglineListDataType implements DataType {

    ELEMENTS("elements")
    ;

    private String value;

    TaglineListDataType(String v) {
        value = v;
    }

    @Override
    public String get() {
        return value;
    }
}
