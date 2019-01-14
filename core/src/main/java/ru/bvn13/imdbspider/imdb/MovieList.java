package ru.bvn13.imdbspider.imdb;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author boyko_vn at 09.01.2019
 */
public class MovieList extends ImdbObject<MovieListDataType> {

    private List<Movie> movies;

    @Override
    protected void initRetrievedDataTypes() {
        this.retrievedDataTypes = EnumSet.noneOf(MovieListDataType.class);
    }

    public List<Movie> getMovies() {
        if (movies == null) {
            movies = new ArrayList<>();
        }
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

}
