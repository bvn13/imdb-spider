package ru.bvn13.imdbspider.runner;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.bvn13.imdbspider.ImdbSpider;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieList;

import java.util.List;


public class AppTest
{
    private static ImdbSpider spider;

    @BeforeClass
    public static void initClass() {
        spider = ImdbSpider.withApi_1_0();
    }

    @Test
    public void searchTerminatorTest() {
        try {
            MovieList result = spider.searchMovieByTitle("test", 5);
        } catch (ImdbSpiderException e) {
            e.printStackTrace();
        }
    }
}
