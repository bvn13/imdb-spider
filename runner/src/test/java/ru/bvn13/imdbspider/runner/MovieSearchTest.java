package ru.bvn13.imdbspider.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    //@Test
    public void testSearchTerminatorReturningJson() throws ImdbSpiderException, JsonProcessingException {
        MovieList result = spider.searchMovieByTitle("Терминатор", 5, EnumSet.of(MovieDataType.ID, MovieDataType.TITLE, MovieDataType.ORIGINAL_TITLE, MovieDataType.YEAR));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);
        //System.out.println("JSON = " + json);
        //assertEquals("{\"retrievedDataTypes\":[\"ELEMENTS\"],\"id\":null,\"url\":\"https://www.imdb.com/find?ref_=nv_sr_fn&q=%D0%A2%D0%B5%D1%80%D0%BC%D0%B8%D0%BD%D0%B0%D1%82%D0%BE%D1%80&s=tt\",\"movies\":[{\"retrievedDataTypes\":[\"ID\",\"TITLE\",\"ORIGINAL_TITLE\",\"YEAR\"],\"id\":\"0088247\",\"url\":\"https://www.imdb.com/title/tt0088247/?ref_=fn_tt_tt_1\",\"title\":\"Терминатор (1984)\",\"originalTitle\":\"The Terminator\",\"year\":1984,\"akas\":{}}]}", json);
    }
}
