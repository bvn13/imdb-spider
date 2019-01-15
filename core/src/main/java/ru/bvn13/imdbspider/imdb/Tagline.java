package ru.bvn13.imdbspider.imdb;

import java.util.EnumSet;

/**
 * @author boyko_vn at 15.01.2019
 */
public class Tagline extends ImdbObject<TaglineDataType> {

    private String text;

    @Override
    protected void initRetrievedDataTypes() {
        this.retrievedDataTypes = EnumSet.noneOf(TaglineDataType.class);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
