package ru.bvn13.imdbspider.spider.api.v1_0;

import org.jsoup.nodes.Element;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.imdb.MovieList;
import ru.bvn13.imdbspider.imdb.MovieListDataType;
import ru.bvn13.imdbspider.spider.tasker.Task;

import java.util.List;

/**
 * @author boyko_vn at 15.01.2019
 */
public class MovieListProcessor_1_0 extends AbstractApiProcessor_1_0<MovieList, MovieListDataType> {

    public MovieListProcessor_1_0(ApiFactory_1_0 apiFactory) {
        super(apiFactory);
    }

    @Override
    Task taskByDataType(MovieListDataType movieListDataType) {
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
                        if (getApiFactory().getMovieDataTypeSet() == null) {
                            getApiFactory().setMovieDataTypeSet(getApiFactory().getDefaultMovieDataTypeSet());
                        }
                        if (!getApiFactory().getMovieDataTypeSet().contains(MovieDataType.ID)) {
                            getApiFactory().getMovieDataTypeSet().add(MovieDataType.ID);
                        }
                        Task movieTask = getApiFactory().getMovieProcessor().taskByDataType(MovieDataType.ID)
                                .setParentTask(task)
                                .setUrl(String.format("%s%s", ApiFactory_1_0.URL_MAIN, link.attr("href")));
                        task.getNestedTasks().add(movieTask);
                        getApiFactory().getMovieDataTypeSet().forEach(movieDataType ->
                                movieTask.getNestedTasks().add(getApiFactory().getMovieProcessor().taskByDataType(movieDataType)
                                .setParentTask(movieTask)
                                .setUrl(String.format("%s%s", ApiFactory_1_0.URL_MAIN, link.attr("href")))));
                    }
                });
                break;
        }
        return t;
    }

    @Override
    void fillUpImdbObject(MovieList movieList, Task task) {
        switch ((MovieListDataType) task.getDataType()) {
            case ELEMENTS:
                movieList.setUrl(task.getUrl());
                movieList.getRetrievedDataTypes().add((MovieListDataType) task.getDataType());
                break;
        }
    }



}
