package ru.bvn13.imdbspider.imdb;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by bvn13 on 16.01.2019.
 */
public class AkaList extends ImdbObject<AkaListDataType> {

    private List<Aka> akas;

    @Override
    protected void initRetrievedDataTypes() {
        this.retrievedDataTypes = EnumSet.noneOf(AkaListDataType.class);
    }

    public List<Aka> getAkas() {
        if (akas == null) {
            akas = new ArrayList<>();
        }
        return akas;
    }

    public void setAkas(List<Aka> akas) {
        this.akas = akas;
    }
}
