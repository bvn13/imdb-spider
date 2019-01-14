package ru.bvn13.imdbspider.imdb.accessories;

/**
 * @author boyko_vn at 14.01.2019
 */
public class Link {

    private String url;
    private String title;

    public String getUrl() {
        return url;
    }

    public Link setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Link setTitle(String title) {
        this.title = title;
        return this;
    }
}
