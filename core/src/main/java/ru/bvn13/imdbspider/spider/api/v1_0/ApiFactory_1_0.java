package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.exceptions.processor.HtmlProcessorException;
import ru.bvn13.imdbspider.imdb.*;
import ru.bvn13.imdbspider.imdb.accessories.Link;
import ru.bvn13.imdbspider.imdb.accessories.SoundMix;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.processor.HtmlProcessor;
import ru.bvn13.imdbspider.spider.tasker.Task;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author boyko_vn at 09.01.2019
 *
 * IMDB :: SPIDER :: API :: version 1.0 (started 09.01.2019)
 */
public class ApiFactory_1_0 implements ApiFactory {

    public static final String URL_MAIN = "https://www.imdb.com";

    private static final String URL_SEARCH_TITLE = "https://www.imdb.com/find?ref_=nv_sr_fn&q={{title}}&s=tt";

    private final Pattern PATTERN_MOVIE_ID_FROM_MOVIELIST = Pattern.compile("/title/tt(\\d+)/.*");

    private EnumSet<MovieDataType> defaultMovieDataTypeSet = EnumSet.of(MovieDataType.ID, MovieDataType.TITLE, MovieDataType.YEAR);
    private EnumSet<MovieDataType> movieDataTypeSet;

    private HtmlProcessor htmlProcessor;

    private static class POSTPROCESS {

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

    public ApiFactory_1_0(HtmlProcessor htmlProcessor) {
        this.htmlProcessor = htmlProcessor;
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
            return taskByMovieDataType((MovieDataType) dataType);
        } else if (dataType instanceof MovieListDataType) {
            return taskByMovieListDataType((MovieListDataType) dataType);
        } else if (dataType instanceof TaglineListDataType) {
            return taskByTaglineListDataType((TaglineListDataType) dataType);
        } else if (dataType instanceof TaglineDataType) {
            return taskByTaglineDataType((TaglineDataType) dataType);
        } else {
            throw new DataTypeNotSupportedException(String.format("DataType %s is not supported by API v1_0!", dataType.getClass().getName()));
        }
    }

    @Override
    public void fillUpImdbObject(ImdbObject imdbObject, Task task) {
        if (imdbObject instanceof Movie) {
            if (task.getDataType() instanceof MovieDataType) {
                fillUpMovie((Movie) imdbObject, task);
            }
        } else if (imdbObject instanceof MovieList) {
            if (task.getDataType() instanceof MovieListDataType) {
                fillUpMovieList((MovieList) imdbObject, task);
            }
        } else if (imdbObject instanceof TaglineList) {
            if (task.getDataType() instanceof TaglineListDataType) {
                fillUpTaglineList((TaglineList) imdbObject, task);
            }
        } else if (imdbObject instanceof Tagline) {
            if (task.getDataType() instanceof TaglineDataType) {
                fillUpTagline((Tagline) imdbObject, task);
            }
        }
    }

    private Task taskByMovieDataType(MovieDataType movieDataType) {
        Task t = new Task();
        t.setDataType(movieDataType);
        switch (movieDataType) {
            case ID:
                t.setPostprocess((task, s) -> {
                    Matcher matcher = PATTERN_MOVIE_ID_FROM_MOVIELIST.matcher(task.getUrl());
                    if (matcher.find()) {
                        task.setResultType(String.class);
                        task.setResult(matcher.group(1));
                    }
                });
                break;
            case TITLE:
                t.setCssSelector("#title-overview-widget > div.vital > div.title_block > div > div.titleBar > div.title_wrapper > h1");
                t.setPostprocess(POSTPROCESS.GET_WHOLE_TEXT_OF_FIRST_ELEMENT);
                break;
            case ORIGINAL_TITLE:
                t.setCssSelector("#title-overview-widget > div.vital > div.title_block > div > div.titleBar > div.title_wrapper > div.originalTitle");
                t.setPostprocess(POSTPROCESS.GET_OWN_TEXT_OF_FIRST_ELEMENT);
                break;
            case YEAR:
                t.setCssSelector("#titleYear > a");
                t.setPostprocess((task, s) -> {
                    task.setResultType(Integer.class);
                    if (task.getCssSelectorResult().size() > 0) {
                        try {
                            task.setResult(Integer.parseInt(task.getCssSelectorResult().first().text().trim()));
                        } catch (NumberFormatException e) {
                            task.setResult(-1);
                        }
                    } else {
                        task.setResult(-1);
                    }
                });
                break;
            case POSTER:
                t.setCssSelector("#title-overview-widget > div.vital > div.slate_wrapper > div.poster > a > img");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().attr("src"));
                    } else {
                        task.setResult("");
                    }
                });
                break;
            case STORYLINE:
                t.setCssSelector("#titleStoryLine > div:nth-child(3) > p > span");
                t.setPostprocess(POSTPROCESS.GET_TEXT_OF_FIRST_ELEMENT);
                break;
            case RANDOM_TAGLINE:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Taglines)"); //#titleStoryLine > div:nth-child(8) > h4
                t.setPostprocess(POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case GENRES:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Genres)");
                t.setPostprocess(POSTPROCESS.COLLECT_TITLES_OF_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case CERTIFICATE:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Certificate)");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().parent().select("span:nth-child(2)").first().text().trim());
                    }
                });
                break;
            case OFFICIAL_SITES:
                t.setCssSelector("#titleDetails > div > h4:contains(Official Sites)");
                t.setPostprocess(POSTPROCESS.COLLECT_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case COUNTRIES:
                t.setCssSelector("#titleDetails > div > h4:contains(Country)");
                t.setPostprocess(POSTPROCESS.COLLECT_TITLES_OF_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case LANGUAGES:
                t.setCssSelector("#titleDetails > div > h4:contains(Language)");
                t.setPostprocess(POSTPROCESS.COLLECT_TITLES_OF_ALL_NESTED_LINKS_OF_PARENT_NODE);
                break;
            case RELEASE_DATE:
                t.setCssSelector("#titleDetails > div > h4:contains(Release Date)");
                t.setPostprocess(POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case BUDGET:
                t.setCssSelector("#titleDetails > div > h4:contains(Budget)");
                t.setPostprocess(POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case CUMULATIVE_WORLDWIDE_GROSS:
                t.setCssSelector("#titleDetails > div > h4:contains(Cumulative Worldwide Gross)");
                t.setPostprocess(POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case RUNTIME:
                t.setCssSelector("#titleDetails > div > h4:contains(Runtime)");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    task.setResult("");
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().parent().text().replace("Runtime:", "").trim());
                    }
                });
                break;
            case SOUND_MIXES:
                t.setCssSelector("#titleDetails > div > h4:contains(Sound Mix)");
                t.setPostprocess((task, s) -> {
                    task.setResultType(List.class);
                    List<SoundMix> titles = new ArrayList<>();
                    if (task.getCssSelectorResult().size() > 0) {
                        String html = task.getCssSelectorResult().first().parent().html();
                        html = html.replace("\r", "");
                        html = html.replace("\n", "");
                        html = html.replace("<span class=\"ghost\">|</span>", "|");

                        // remove header: <h4 class="inline">Sound Mix:</h4>
                        html = html.replaceAll("(<h4.+\\/h4>)", "");

                        String[] lines = html.split("\\|");

                        for (int i=0; i<lines.length; i++) {
                            try {
                                Elements els = htmlProcessor.process(String.format("<div>%s</div>", lines[i]), "div");
                                if (els.size() > 0) {
                                    Element div = els.first();
                                    Element link = div.selectFirst("a");
                                    titles.add(new SoundMix()
                                            .setName(link.text().trim())
                                            .setDescription(div.ownText())
                                    );
                                }
                            } catch (HtmlProcessorException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    task.setResult(titles);
                });
                break;
            case COLOR:
                t.setCssSelector("#titleDetails > div > h4:contains(Color)");
                t.setPostprocess(POSTPROCESS.GET_TITLE_OF_FIRST_LINK_IN_PARENT_MODE);
                break;
            case ASPECT_RATIO:
                t.setCssSelector("#titleDetails > div > h4:contains(Aspect Ratio)");
                t.setPostprocess(POSTPROCESS.GET_OWN_TEXT_OF_PARENT_MODE);
                break;
            case TAGLINES:
                t.setCssSelector("#titleStoryLine > div > h4:contains(Taglines)");
                t.setPostprocess((task, s) -> {
                    if (task.getCssSelectorResult().size() > 0) {
                        Elements links = task.getCssSelectorResult().first().parent().select("span > a:contains(See more)");
                        if (links.size() > 0) {
                            Task newTask = this.taskByTaglineListDataType(TaglineListDataType.ELEMENTS)
                                    .setParentTask(task)
                                    .setUrl(String.format("%s%s", URL_MAIN, links.first().attr("href")));
                            task.getNestedTasks().add(newTask);
                        }
                    }
                });
                break;
        }
        return t;
    }

    private Task taskByTaglineListDataType(TaglineListDataType taglineListDataType) {
        Task t = new Task();
        t.setDataType(taglineListDataType);
        switch (taglineListDataType) {
            case ELEMENTS:
                t.setCssSelector("#taglines_content > div.soda");
                AtomicInteger i = new AtomicInteger(0);
                t.setPostprocess((task, s) -> {
                    for (Element element : task.getCssSelectorResult()) {
                        Task newTaskId = taskByTaglineDataType(TaglineDataType.ID)
                                .setParentTask(task)
                                .setUrl(task.getUrl())
                                .setResult(String.format("%d", i.getAndAdd(1)));
                        task.getNestedTasks().add(newTaskId);

                        Task newTaskText = taskByTaglineDataType(TaglineDataType.TEXT)
                                .setParentTask(task)
                                .setUrl(task.getUrl())
                                .setResult(element.text());
                        newTaskId.getNestedTasks().add(newTaskText);
                    }
                });
                break;
        }
        return t;
    }

    private Task taskByTaglineDataType(TaglineDataType taglineDataType) {
        Task t = new Task();
        t.setDataType(taglineDataType);
        switch (taglineDataType) {
            case ID:
                //
                break;
            case TEXT:
                t.setPostprocess((task, s) -> {
                    task.setResult(((String)task.getResult()).trim());
                });
                break;
        }
        return t;
    }

    private Task taskByMovieListDataType(MovieListDataType movieListDataType) {
        Task t = new Task();
        t.setDataType(movieListDataType);
        switch (movieListDataType) {
            case ELEMENTS:
                t.setCssSelector("#main > div > div.findSection > table > tbody > tr > td.result_text");
                t.setResultType(List.class);
                t.setPostprocess((task, s) -> {
                    int count = 0;
                    for (Element element : task.getCssSelectorResult()) {
                        count++;
                        if (task.getRestrictionByCount() != null) {
                            if (count > task.getRestrictionByCount()) {
                                break;
                            }
                        }
                        Element link = element.select("a").first();
                        if (movieDataTypeSet == null) {
                            movieDataTypeSet = defaultMovieDataTypeSet;
                        }
                        if (!movieDataTypeSet.contains(MovieDataType.ID)) {
                            movieDataTypeSet.add(MovieDataType.ID);
                        }
                        Task movieTask = this.taskByMovieDataType(MovieDataType.ID)
                                .setParentTask(task)
                                .setUrl(String.format("%s%s", URL_MAIN, link.attr("href")));
                        task.getNestedTasks().add(movieTask);
                        movieDataTypeSet.forEach(movieDataType -> movieTask.getNestedTasks().add(this.taskByMovieDataType(movieDataType)
                                .setParentTask(movieTask)
                                .setUrl(String.format("%s%s", URL_MAIN, link.attr("href")))));
                    }
                });
                break;
        }
        return t;
    }

    private void fillUpMovie(Movie movie, Task task) {
        boolean isDone = false;
        switch ((MovieDataType) task.getDataType()) {
            case ID:
                movie.setUrl(task.getUrl());
                movie.setId((String) task.getResult());
                isDone = true;
                break;
            case TITLE:
                movie.setTitle((String) task.getResult());
                isDone = true;
                break;
            case ORIGINAL_TITLE:
                movie.setOriginalTitle((String) task.getResult());
                isDone = true;
                break;
            case YEAR:
                movie.setYear((Integer) task.getResult());
                isDone = true;
                break;
            case POSTER:
                movie.setPosterLink((String) task.getResult());
                isDone = true;
                break;
            case STORYLINE:
                movie.setStoryline((String) task.getResult());
                isDone = true;
                break;
            case RANDOM_TAGLINE:
                movie.setRandomTagline((String) task.getResult());
                isDone = true;
                break;
            case GENRES:
                movie.setGenres((List<String>) task.getResult());
                isDone = true;
                break;
            case CERTIFICATE:
                movie.setCertificate((String) task.getResult());
                isDone = true;
                break;
            case OFFICIAL_SITES:
                movie.setOfficialSites((List<Link>) task.getResult());
                isDone = true;
                break;
            case COUNTRIES:
                movie.setCountries((List<String>) task.getResult());
                isDone = true;
                break;
            case LANGUAGES:
                movie.setLanguages((List<String>) task.getResult());
                isDone = true;
                break;
            case RELEASE_DATE:
                movie.setReleaseDate((String) task.getResult());
                isDone = true;
                break;
            case BUDGET:
                movie.setBudget((String) task.getResult());
                isDone = true;
                break;
            case CUMULATIVE_WORLDWIDE_GROSS:
                movie.setCumulativeWorldwideGross((String) task.getResult());
                isDone = true;
                break;
            case RUNTIME:
                movie.setRuntime((String) task.getResult());
                isDone = true;
                break;
            case SOUND_MIXES:
                movie.setSoundMixes((List<SoundMix>) task.getResult());
                isDone = true;
                break;
            case COLOR:
                movie.setColor((String) task.getResult());
                isDone = true;
                break;
            case ASPECT_RATIO:
                movie.setAspectRatio((String) task.getResult());
                isDone = true;
                break;
            case TAGLINES:
                isDone = true;
        }

        if (isDone) {
            movie.getRetrievedDataTypes().add((MovieDataType) task.getDataType());
        }
    }

    private void fillUpTaglineList(TaglineList taglineList, Task task) {
        switch ((TaglineListDataType) task.getDataType()) {
            case ELEMENTS:
                taglineList.setUrl(task.getUrl());
                taglineList.getRetrievedDataTypes().add((TaglineListDataType) task.getDataType());
                break;
        }
    }

    private void fillUpTagline(Tagline tagline, Task task) {
        switch ((TaglineDataType) task.getDataType()) {
            case ID:
                tagline.setUrl(task.getUrl());
                tagline.setId((String) task.getResult());
                tagline.getRetrievedDataTypes().add((TaglineDataType) task.getDataType());
                break;
            case TEXT:
                tagline.setUrl(task.getUrl());
                tagline.setText((String) task.getResult());
                tagline.getRetrievedDataTypes().add((TaglineDataType) task.getDataType());
                break;
        }
    }

    private void fillUpMovieList(MovieList movieList, Task task) {
        switch ((MovieListDataType) task.getDataType()) {
            case ELEMENTS:
                movieList.setUrl(task.getUrl());
                movieList.getRetrievedDataTypes().add((MovieListDataType) task.getDataType());
                break;
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
}
