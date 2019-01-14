package ru.bvn13.imdbspider.runner;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.bvn13.imdbspider.ImdbSpider;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.imdb.MovieList;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MovieSearchTest
{
    private static ImdbSpider spider;

    @BeforeClass
    public static void initClass() {
        spider = ImdbSpider.withApi_1_0();
    }

    @Test
    public void testSearchTerminator() throws ImdbSpiderException {
        MovieList result = spider.searchMovieByTitle("Терминатор", 5, EnumSet.of(MovieDataType.ID, MovieDataType.TITLE, MovieDataType.ORIGINAL_TITLE, MovieDataType.YEAR));
        assertTrue(result.getMovies().size() > 0);
        Movie movie = result.getMovies().get(0);
        assertTrue(movie.isDataTypeRetrieved(MovieDataType.ID));
        assertTrue(movie.isDataTypeRetrieved(MovieDataType.TITLE));
        assertTrue(movie.isDataTypeRetrieved(MovieDataType.ORIGINAL_TITLE));
        assertTrue(movie.isDataTypeRetrieved(MovieDataType.YEAR));
        assertEquals("0088247", movie.getId());
        assertEquals("The Terminator", movie.getOriginalTitle());
        assertEquals(Integer.valueOf(1984), movie.getYear());
    }
}
