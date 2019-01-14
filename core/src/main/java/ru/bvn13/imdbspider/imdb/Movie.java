package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Movie extends ImdbObject<MovieDataType> {

    private String title;
    private String originalTitle;
    private Integer year;
    private Map<String, String> akas = new ConcurrentHashMap<>(50);

    @Override
    protected void initRetrievedDataTypes() {
        this.retrievedDataTypes = EnumSet.noneOf(MovieDataType.class);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Map<String, String> getAkas() {
        return akas;
    }

    public void setAkas(Map<String, String> akas) {
        this.akas = akas;
    }

}
