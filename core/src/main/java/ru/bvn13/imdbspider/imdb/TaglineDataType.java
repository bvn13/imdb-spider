package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;

/**
 * @author boyko_vn at 15.01.2019
 */
public enum TaglineDataType implements DataType {

    ID("id"),
    TEXT("text")
    ;

    private String value;

    TaglineDataType(String v) {
        value = v;
    }

    public static final EnumSet<TaglineDataType> ALL_DATA = EnumSet.allOf(TaglineDataType.class);

    @Override
    public String get() {
        return null;
    }
}
