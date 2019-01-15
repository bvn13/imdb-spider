package ru.bvn13.imdbspider.imdb;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author boyko_vn at 15.01.2019
 */
public class TaglineList extends ImdbObject<TaglineListDataType> {

    private List<Tagline> taglines;

    @Override
    protected void initRetrievedDataTypes() {
        this.retrievedDataTypes = EnumSet.noneOf(TaglineListDataType.class);
    }

    public List<Tagline> getTaglines() {
        if (taglines == null) {
            taglines = new ArrayList<>();
        }
        return taglines;
    }

    public void setTaglines(List<Tagline> taglines) {
        this.taglines = taglines;
    }
}
