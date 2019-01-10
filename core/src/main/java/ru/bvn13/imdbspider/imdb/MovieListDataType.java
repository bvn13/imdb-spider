package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;

/**
 * @author boyko_vn at 10.01.2019
 */
public enum MovieListDataType implements DataType {

    ELEMENTS("element")
    ;

    private String value;

    MovieListDataType(String v) {
        value = v;
    }

    public static final EnumSet<MovieListDataType> ALL_DATA = EnumSet.allOf(MovieListDataType.class);

    @Override
    public String get() {
        return value;
    }

}
