package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;

/**
 * Created by bvn13 on 16.01.2019.
 */
public class Aka extends ImdbObject<AkaDataType> {

    private String name;
    private String title;

    @Override
    protected void initRetrievedDataTypes() {
        this.retrievedDataTypes = EnumSet.noneOf(AkaDataType.class);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
