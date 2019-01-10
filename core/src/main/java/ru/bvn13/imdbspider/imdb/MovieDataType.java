package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;

/**
 * @author boyko_vn at 09.01.2019
 */
public enum MovieDataType implements DataType {

    ID("id"),
    TITLE("title"),
    YEAR("year"),
    AKAS("akas")

    ;

    private String value;

    MovieDataType(String v) {
        value = v;
    }

    public static final EnumSet<MovieDataType> ALL_DATA = EnumSet.allOf(MovieDataType.class);

    @Override
    public String get() {
        return value;
    }
}
