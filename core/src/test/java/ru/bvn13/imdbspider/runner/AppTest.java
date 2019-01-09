package ru.bvn13.imdbspider.runner;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.bvn13.imdbspider.ImdbSpider;
import ru.bvn13.imdbspider.imdb.Movie;

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
        List<Movie> result = spider.searchMovieByTitle("Терминатор", 5);
    }
}
