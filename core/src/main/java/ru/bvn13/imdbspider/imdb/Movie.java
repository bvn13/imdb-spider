package ru.bvn13.imdbspider.imdb;

import ru.bvn13.imdbspider.imdb.accessories.Link;
import ru.bvn13.imdbspider.imdb.accessories.SoundMix;

import java.util.EnumSet;
import java.util.List;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Movie extends ImdbObject<MovieDataType> {

    private String title;
    private String originalTitle;
    private Integer year;
    private String posterLink;
    private String storyline;
    private String randomTagline;
    private List<String> genres;
    private String certificate;
    private List<Link> officialSites;
    private List<String> countries;
    private List<String> languages;
    private String releaseDate;
    private String budget;
    private String cumulativeWorldwideGross;
    private String runtime;
    private List<SoundMix> soundMixes;
    private String color;
    private String aspectRatio;

    private TaglineList taglineList;
    private AkaList akaList;

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

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    public String getStoryline() {
        return storyline;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public String getRandomTagline() {
        return randomTagline;
    }

    public void setRandomTagline(String randomTagline) {
        this.randomTagline = randomTagline;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public List<Link> getOfficialSites() {
        return officialSites;
    }

    public void setOfficialSites(List<Link> officialSites) {
        this.officialSites = officialSites;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getCumulativeWorldwideGross() {
        return cumulativeWorldwideGross;
    }

    public void setCumulativeWorldwideGross(String cumulativeWorldwideGross) {
        this.cumulativeWorldwideGross = cumulativeWorldwideGross;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public List<SoundMix> getSoundMixes() {
        return soundMixes;
    }

    public void setSoundMixes(List<SoundMix> soundMixes) {
        this.soundMixes = soundMixes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public TaglineList getTaglineList() {
        return taglineList;
    }

    public void setTaglineList(TaglineList taglineList) {
        this.taglineList = taglineList;
    }

    public AkaList getAkaList() {
        return akaList;
    }

    public void setAkaList(AkaList akaList) {
        this.akaList = akaList;
    }
}
