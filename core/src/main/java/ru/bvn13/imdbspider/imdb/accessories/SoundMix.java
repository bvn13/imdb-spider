package ru.bvn13.imdbspider.imdb.accessories;

/**
 * @author boyko_vn at 14.01.2019
 */
public class SoundMix {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public SoundMix setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SoundMix setDescription(String description) {
        this.description = description;
        return this;
    }
}
