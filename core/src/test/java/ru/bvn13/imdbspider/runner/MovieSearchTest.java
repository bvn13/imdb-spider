package ru.bvn13.imdbspider.runner;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.bvn13.imdbspider.ImdbSpider;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.imdb.MovieList;
import ru.bvn13.imdbspider.imdb.Tagline;
import ru.bvn13.imdbspider.imdb.accessories.SoundMix;

import static org.junit.Assert.*;


public class MovieSearchTest {

    private static final String TERMINATOR_STORYLINE = "A cyborg is sent from the future on a deadly mission. He has to kill Sarah Connor, a young woman whose life will have a great significance in years to come. Sarah has only one protector - Kyle Reese - also sent from the future. The Terminator uses his exceptional intelligence and strength to find Sarah, but is there any way to stop the seemingly indestructible cyborg ?";
    private static final String TERMINATOR_POSTER_LINK = "https://m.media-amazon.com/images/M/MV5BYTViNzMxZjEtZGEwNy00MDNiLWIzNGQtZDY2MjQ1OWViZjFmXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_UX182_CR0,0,182,268_AL_.jpg";

    private static final String TERMINATOR_TAGLINE_1 = "I'll be back!";
    private static final String TERMINATOR_TAGLINE_2 = "In the Year of Darkness, 2029, the rulers of this planet devised the ultimate plan. They would reshape the Future by changing the Past. The plan required something that felt no pity. No pain. No fear. Something unstoppable. They created 'THE TERMINATOR'";
    private static final String TERMINATOR_TAGLINE_3 = "The thing that won't die, in the nightmare that won't end.";
    private static final String TERMINATOR_TAGLINE_4 = "Your future is in its hands.";
    private static final String TERMINATOR_TAGLINE_5 = "La sua missione e una sola: distruggere, uccidere... (His one and only mission: to destroy, to kill...) (Italian DVD)";

    private static ImdbSpider spider;

    @BeforeClass
    public static void initClass() {
        spider = ImdbSpider.withApi_1_0()
                .addHttpRequestHeader("Content-Language", "en-EN");
    }

    @Test
    public void testSearchTerminator() throws ImdbSpiderException {
        MovieList result = spider.searchMovieByTitle("Terminator", 5,
                MovieDataType.ID,
                MovieDataType.TITLE,
                MovieDataType.ORIGINAL_TITLE,
                MovieDataType.YEAR,
                MovieDataType.STORYLINE,
                MovieDataType.RANDOM_TAGLINE,
                MovieDataType.GENRES,
                MovieDataType.CERTIFICATE,
                MovieDataType.OFFICIAL_SITES,
                MovieDataType.COUNTRIES,
                MovieDataType.LANGUAGES,
                MovieDataType.RELEASE_DATE,
                MovieDataType.BUDGET,
                MovieDataType.CUMULATIVE_WORLDWIDE_GROSS,
                MovieDataType.RUNTIME,
                MovieDataType.SOUND_MIXES,
                MovieDataType.COLOR,
                MovieDataType.ASPECT_RATIO,
                MovieDataType.POSTER,
                MovieDataType.TAGLINES
        );


        assertTrue("Got "+result.getMovies().size()+" movies but more than 0 expected", result.getMovies().size() > 0);
        Movie movie = result.getMovies().get(0);

        assertTrue("Expected ID field presence", movie.isDataTypeRetrieved(MovieDataType.ID));
        assertTrue("Expected TITLE field presence", movie.isDataTypeRetrieved(MovieDataType.TITLE));
        assertTrue("Expected ORIGINAL_TITLE field presence", movie.isDataTypeRetrieved(MovieDataType.ORIGINAL_TITLE));
        assertTrue("Expected YEAR field presence", movie.isDataTypeRetrieved(MovieDataType.YEAR));
        assertTrue("Expected STORYLINE field presence", movie.isDataTypeRetrieved(MovieDataType.STORYLINE));
        assertTrue("Expected RANDOM_TAGLINE field presence", movie.isDataTypeRetrieved(MovieDataType.RANDOM_TAGLINE));
        assertTrue("Expected GENRES field presence", movie.isDataTypeRetrieved(MovieDataType.GENRES));
        assertTrue("Expected CERTIFICATE field presence", movie.isDataTypeRetrieved(MovieDataType.CERTIFICATE));
        assertTrue("Expected OFFICIAL_SITES field presence", movie.isDataTypeRetrieved(MovieDataType.OFFICIAL_SITES));
        assertTrue("Expected COUNTRIES field presence", movie.isDataTypeRetrieved(MovieDataType.COUNTRIES));
        assertTrue("Expected LANGUAGES field presence", movie.isDataTypeRetrieved(MovieDataType.LANGUAGES));
        assertTrue("Expected RELEASE_DATE field presence", movie.isDataTypeRetrieved(MovieDataType.RELEASE_DATE));
        assertTrue("Expected BUDGET field presence", movie.isDataTypeRetrieved(MovieDataType.BUDGET));
        assertTrue("Expected CUMULATIVE_WORLDWIDE_GROSS field presence", movie.isDataTypeRetrieved(MovieDataType.CUMULATIVE_WORLDWIDE_GROSS));
        assertTrue("Expected RUNTIME field presence", movie.isDataTypeRetrieved(MovieDataType.RUNTIME));
        assertTrue("Expected SOUND_MIXES field presence", movie.isDataTypeRetrieved(MovieDataType.SOUND_MIXES));
        assertTrue("Expected COLOR field presence", movie.isDataTypeRetrieved(MovieDataType.COLOR));
        assertTrue("Expected ASPECT_RATIO field presence", movie.isDataTypeRetrieved(MovieDataType.ASPECT_RATIO));
        assertTrue("Expected POSTER field presence", movie.isDataTypeRetrieved(MovieDataType.POSTER));
        assertTrue("Expected TAGLINES field presence", movie.isDataTypeRetrieved(MovieDataType.TAGLINES));

        assertEquals("Expected that first in search result has ID = 0088247, but given: "+movie.getId(), "0088247", movie.getId());
        assertEquals("Expected movie name: The Terminator, but given: "+movie.getOriginalTitle(), "The Terminator", movie.getOriginalTitle());
        assertEquals("Expected year = "+movie.getYear(), Integer.valueOf(1984), movie.getYear());

        assertEquals("Invalid storyline", TERMINATOR_STORYLINE, movie.getStoryline());
        //assertEquals(TERMINATOR_TAGLINES, movie.getRandomTagline());
        assertTrue("Expected that genre Action is present", movie.getGenres().contains("Action"));
        assertTrue("Expected that genre Sci-Fi is present", movie.getGenres().contains("Sci-Fi"));

        assertEquals("Expected that certificate is 16+ but given: "+movie.getCertificate(), "16+", movie.getCertificate());

        //assertTrue(movie.getOfficialSites().contains("Facebook"));
        assertTrue("Expected than at least one site is present", movie.getOfficialSites().size() > 0);
        assertEquals("Expected that first site is Facebook", "Facebook", movie.getOfficialSites().get(0).getTitle());
        //assertEquals(TERMINATOR_FACEBOOK_URL, movie.getOfficialSites().get(0).getUrl()); //not comparable, dynamic link

        assertTrue("Expected at least one country is present", movie.getCountries().size() > 0);
        assertTrue("Expected country UK", movie.getCountries().contains("UK"));
        assertTrue("Expected country USA", movie.getCountries().contains("USA"));

        assertTrue("Expected at least one language is present", movie.getLanguages().size() > 0);
        assertTrue("Expected language English", movie.getLanguages().contains("English"));
        assertTrue("Expected language Spanish", movie.getLanguages().contains("Spanish"));

        assertEquals("26 October 1984 (USA)", movie.getReleaseDate());

        assertEquals("Expected budget: $6,400,000", "$6,400,000", movie.getBudget());
        assertEquals("Expected Cumulative Worldwide Gross: $40,400,000", "$40,000,000", movie.getCumulativeWorldwideGross());

        assertEquals("Expected runtime: 107 min", "107 min", movie.getRuntime());
        assertEquals("Expected color: Color", "Color", movie.getColor());
        assertEquals("Expected aspect ratio: 1.85 : 1","1.85 : 1", movie.getAspectRatio());

        assertEquals("Poster link is not valid", TERMINATOR_POSTER_LINK, movie.getPosterLink());


        //sound mixes
        assertTrue("Expected at least one sound mix", movie.getSoundMixes().size() > 0);
        boolean hasMono=false, hasDolby=false, hasDTS=false;
        String descrMono="", descrDolby="", descrDTS="";
        for (SoundMix soundMix : movie.getSoundMixes()) {
            switch (soundMix.getName()) {
                case "Mono" : hasMono = true; descrMono = soundMix.getDescription(); break;
                case "Dolby" : hasDolby = true; descrDolby = soundMix.getDescription();  break;
                case "DTS" : hasDTS = true; descrDTS = soundMix.getDescription();  break;
            }
        }
        assertTrue("Expected sound mix: Mono", hasMono);
        assertTrue("Expected sound mix: Dolby", hasDolby);
        assertTrue("Expected sound mix: DTS", hasDTS);
        assertEquals("Expected Mono sound mix description: (original release), but given: "+descrMono, "(original release)", descrMono);
        assertEquals("Expected Dolby sound mix description: (DVD Re-Release), but given: "+descrDolby, "(DVD Re-Release)", descrDolby);
        assertEquals("Expected DTS sound mix description: (DTS HD Master Audio), but given: "+descrDTS, "(DTS HD Master Audio)", descrDTS);


        // taglines
        assertNotNull("Expected that tagline list presence", movie.getTaglineList());
        assertEquals("Expected 5 taglines but given: "+movie.getTaglineList().getTaglines().size(), 5, movie.getTaglineList().getTaglines().size());

        boolean hasTagline1 = false, hasTagline2 = false, hasTagline3 = false, hasTagline4 = false, hasTagline5 = false;
        for (Tagline tagline : movie.getTaglineList().getTaglines()) {
            switch (tagline.getText()) {
                case TERMINATOR_TAGLINE_1 : hasTagline1 = true; break;
                case TERMINATOR_TAGLINE_2 : hasTagline2 = true; break;
                case TERMINATOR_TAGLINE_3 : hasTagline3 = true; break;
                case TERMINATOR_TAGLINE_4 : hasTagline4 = true; break;
                case TERMINATOR_TAGLINE_5 : hasTagline5 = true; break;
            }
        }

        assertTrue("Expected Tagline 1 presence", hasTagline1);
        assertTrue("Expected Tagline 2 presence", hasTagline2);
        assertTrue("Expected Tagline 3 presence", hasTagline3);
        assertTrue("Expected Tagline 4 presence", hasTagline4);
        assertTrue("Expected Tagline 5 presence", hasTagline5);
    }
}
