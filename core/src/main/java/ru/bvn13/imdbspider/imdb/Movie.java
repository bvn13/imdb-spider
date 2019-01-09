package ru.bvn13.imdbspider.imdb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Movie extends ImdbObject {

    private String title;
    private Map<String, String> akas = new ConcurrentHashMap<>(50);


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getAkas() {
        return akas;
    }

    public void setAkas(Map<String, String> akas) {
        this.akas = akas;
    }
}
