package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.imdb.*;
import ru.bvn13.imdbspider.imdb.accessories.Link;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.processor.HtmlProcessor;
import ru.bvn13.imdbspider.spider.tasker.Task;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/**
 * @author boyko_vn at 09.01.2019
 *
 * IMDB :: SPIDER :: API :: version 1.0 (started 09.01.2019)
 */
public class ApiFactory_1_0 implements ApiFactory {

    private boolean isDebug;

    static final String URL_MAIN = "https://www.imdb.com";

    private static final String URL_SEARCH_TITLE = "https://www.imdb.com/find?ref_=nv_sr_fn&q={{title}}&s=tt";
    static final String URL_AKAS = "https://www.imdb.com/title/tt{{movie_id}}/releaseinfo";

    static final Pattern PATTERN_MOVIE_ID_FROM_MOVIELIST = Pattern.compile("/title/tt(\\d+)/.*");

    private EnumSet<MovieDataType> defaultMovieDataTypeSet = EnumSet.of(MovieDataType.ID, MovieDataType.TITLE, MovieDataType.YEAR);
    private EnumSet<MovieDataType> movieDataTypeSet;

    private HtmlProcessor htmlProcessor;

    static class POSTPROCESS {

        static final BiConsumer<Task, String> GET_TEXT_OF_FIRST_ELEMENT = (task, s) -> {
            task.setResultType(String.class);
            if (task.getCssSelectorResult().size() > 0) {
                task.setResult(task.getCssSelectorResult().first().text().trim());
            } else {
                task.setResult("");
            }
        };

        static final BiConsumer<Task, String> GET_OWN_TEXT_OF_FIRST_ELEMENT = (task, s) -> {
            task.setResultType(String.class);
            if (task.getCssSelectorResult().size() > 0) {
                task.setResult(task.getCssSelectorResult().first().ownText().trim());
            } else {
                task.setResult("");
            }
        };

        static final BiConsumer<Task, String> GET_WHOLE_TEXT_OF_FIRST_ELEMENT = (task, s) -> {
            task.setResultType(String.class);
            if (task.getCssSelectorResult().size() > 0) {
                task.setResult(task.getCssSelectorResult().first().wholeText().trim());
            } else {
                task.setResult("");
            }
        };

        static final BiConsumer<Task, String> GET_OWN_TEXT_OF_PARENT_MODE = (task, s) -> {
            task.setResultType(String.class);
            if (task.getCssSelectorResult().size() > 0) {
                task.setResult(task.getCssSelectorResult().first().parent().ownText().trim());
            } else {
                task.setResult("");
            }
        };

        static final BiConsumer<Task, String> COLLECT_TITLES_OF_ALL_NESTED_LINKS_OF_PARENT_NODE = (task, s) -> {
            task.setResultType(List.class);
            List<String> titles = new ArrayList<>();
            if (task.getCssSelectorResult().size() > 0) {
                for (Element title : task.getCssSelectorResult().first().parent().select("a")) {
                    titles.add(title.text().trim());
                }
            }
            task.setResult(titles);
        };

        static final BiConsumer<Task, String> COLLECT_ALL_NESTED_LINKS_OF_PARENT_NODE = (task, s) -> {
            task.setResultType(List.class);
            List<Link> titles = new ArrayList<>();
            if (task.getCssSelectorResult().size() > 0) {
                for (Element link : task.getCssSelectorResult().first().parent().select("a")) {
                    final String url = link.attr("href").trim();
                    titles.add(new Link()
                            .setTitle(link.text().trim())
                            .setUrl((url.startsWith("/") ? String.format("%s%s", URL_MAIN, url) : url))
                    );
                }
            }
            task.setResult(titles);
        };

        static final BiConsumer<Task, String> GET_TITLE_OF_FIRST_LINK_IN_PARENT_MODE = (task, s) -> {
            task.setResultType(String.class);
            task.setResult("");
            if (task.getCssSelectorResult().size() > 0) {
                Elements links = task.getCssSelectorResult().first().parent().select("a");
                if (links.size() > 0) {
                    task.setResult(links.first().text().trim());
                }
            }
        };


    }

    private MovieListProcessor_1_0 movieListProcessor;
    private MovieProcessor_1_0 movieProcessor;
    private TaglineListProcessor_1_0 taglineListProcessor;
    private TaglineProcessor_1_0 taglineProcessor;
    private AkaListProcessor_1_0 akaListProcessor;
    private AkaProcessor_1_0 akaProcessor;

    public ApiFactory_1_0(HtmlProcessor htmlProcessor) {
        this.htmlProcessor = htmlProcessor;

        this.movieListProcessor = new MovieListProcessor_1_0(this);
        this.movieProcessor = new MovieProcessor_1_0(this);
        this.taglineListProcessor = new TaglineListProcessor_1_0(this);
        this.taglineProcessor = new TaglineProcessor_1_0(this);
        this.akaListProcessor = new AkaListProcessor_1_0(this);
        this.akaProcessor = new AkaProcessor_1_0(this);
    }

    @Override
    public void setDebug(boolean debug) {
        isDebug = debug;
        this.movieProcessor.setDebug(isDebug);
    }

    @Override
    public List<Task> createTasksForSearchMovieByTitle(String title, int maxCount, EnumSet<MovieDataType> dataTypes) throws ImdbSpiderException {

        setMovieDataTypeSet(dataTypes);

        String url = URL_SEARCH_TITLE.replace("{{title}}", URLEncoder.encode(title, Charset.forName("utf-8")));

        List<Task> tasks = new ArrayList<>();

        try {
            Task t1 = taskByDataType(MovieListDataType.ELEMENTS);
            t1.setUrl(url);
            if (maxCount > 0) {
                t1.setRestrictionByCount(maxCount);
            }
            tasks.add(t1);
        } catch (DataTypeNotSupportedException e) {
            throw e;
        }

        return tasks;

    }

    @Override
    public Task taskByDataType(DataType dataType) throws DataTypeNotSupportedException {
        if (dataType instanceof MovieDataType) {
            return movieProcessor.taskByDataType((MovieDataType) dataType);
        } else if (dataType instanceof MovieListDataType) {
            return movieListProcessor.taskByDataType((MovieListDataType) dataType);
        } else if (dataType instanceof TaglineListDataType) {
            return taglineListProcessor.taskByDataType((TaglineListDataType) dataType);
        } else if (dataType instanceof TaglineDataType) {
            return taglineProcessor.taskByDataType((TaglineDataType) dataType);
        } else if (dataType instanceof AkaListDataType) {
            return akaListProcessor.taskByDataType((AkaListDataType) dataType);
        } else if (dataType instanceof AkaDataType) {
            return akaProcessor.taskByDataType((AkaDataType) dataType);
        } else {
            throw new DataTypeNotSupportedException(String.format("DataType %s is not supported by API v1_0!", dataType.getClass().getName()));
        }
    }

    @Override
    public void fillUpImdbObject(ImdbObject imdbObject, Task task) {
        if (imdbObject instanceof Movie) {
            if (task.getDataType() instanceof MovieDataType) {
                movieProcessor.fillUpImdbObject((Movie) imdbObject, task);
            }
        } else if (imdbObject instanceof MovieList) {
            if (task.getDataType() instanceof MovieListDataType) {
                movieListProcessor.fillUpImdbObject((MovieList) imdbObject, task);
            }
        } else if (imdbObject instanceof TaglineList) {
            if (task.getDataType() instanceof TaglineListDataType) {
                taglineListProcessor.fillUpImdbObject((TaglineList) imdbObject, task);
            }
        } else if (imdbObject instanceof Tagline) {
            if (task.getDataType() instanceof TaglineDataType) {
                taglineProcessor.fillUpImdbObject((Tagline) imdbObject, task);
            }
        } else if (imdbObject instanceof AkaList) {
            if (task.getDataType() instanceof AkaListDataType) {
                akaListProcessor.fillUpImdbObject((AkaList) imdbObject, task);
            }
        } else if (imdbObject instanceof Aka) {
            if (task.getDataType() instanceof AkaDataType) {
                akaProcessor.fillUpImdbObject((Aka) imdbObject, task);
            }
        }
    }



    @Override
    public EnumSet<MovieDataType> getDefaultMovieDataTypeSet() {
        return defaultMovieDataTypeSet;
    }

    @Override
    public void setMovieDataTypeSet(EnumSet<MovieDataType> movieDataTypeSet) {
        this.movieDataTypeSet = movieDataTypeSet;
    }

    @Override
    public EnumSet<MovieDataType> getMovieDataTypeSet() {
        return movieDataTypeSet;
    }



    HtmlProcessor getHtmlProcessor() {
        return htmlProcessor;
    }

    MovieProcessor_1_0 getMovieProcessor() {
        return movieProcessor;
    }

    TaglineProcessor_1_0 getTaglineProcessor() {
        return taglineProcessor;
    }

    TaglineListProcessor_1_0 getTaglineListProcessor() {
        return taglineListProcessor;
    }

    AkaListProcessor_1_0 getAkaListProcessor() {
        return akaListProcessor;
    }

    AkaProcessor_1_0 getAkaProcessor() {
        return akaProcessor;
    }
}
