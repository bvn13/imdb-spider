package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;

/**
 * @author boyko_vn at 09.01.2019
 */
public enum MovieDataType implements DataType {

    ID("id"),
    TITLE("title"),
    ORIGINAL_TITLE("original_title"),
    YEAR("year"),
    POSTER("poster"),
    STORYLINE("storyline"),
    RANDOM_TAGLINE("random_tagline"),
    GENRES("genres"),
    CERTIFICATE("certificate"),
    OFFICIAL_SITES("official_sites"),
    COUNTRIES("countries"),
    LANGUAGES("languages"),
    RELEASE_DATE("release_date"),
    BUDGET("budget"),
    CUMULATIVE_WORLDWIDE_GROSS("cumulative worldwide gross"),
    RUNTIME("runtime"),
    SOUND_MIXES("sound_mixes"),
    COLOR("color"),
    ASPECT_RATIO("aspect_ratio"),
    TAGLINES("taglines"),
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
