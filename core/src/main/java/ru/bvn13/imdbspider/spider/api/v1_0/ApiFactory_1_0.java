package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.imdb.*;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author boyko_vn at 09.01.2019
 */
public class ApiFactory_1_0 implements ApiFactory {

    private static final String URL_MAIN = "https://www.imdb.com";

    private static final String URL_SEARCH_TITLE = "https://www.imdb.com/find?ref_=nv_sr_fn&q={{title}}&s=tt";

    private final Pattern PATTERN_MOVIE_ID_FROM_MOVIELIST = Pattern.compile("/title/tt(\\d+)/.*");

    private EnumSet<MovieDataType> defaultMovieDataTypeSet = EnumSet.of(MovieDataType.ID, MovieDataType.TITLE, MovieDataType.YEAR);
    private EnumSet<MovieDataType> movieDataTypeSet;

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
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    task.setResult(task.getCssSelectorResult().first().wholeText().trim());
                });
                break;
            case ORIGINAL_TITLE:
                t.setCssSelector("#title-overview-widget > div.vital > div.title_block > div > div.titleBar > div.title_wrapper > div.originalTitle");
                t.setPostprocess((task, s) -> {
                    task.setResultType(String.class);
                    if (task.getCssSelectorResult().size() > 0) {
                        task.setResult(task.getCssSelectorResult().first().ownText());
                    } else {
                        task.setResult("");
                    }
                });
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
        }

        if (isDone) {
            movie.getRetrievedDataTypes().add((MovieDataType) task.getDataType());
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
