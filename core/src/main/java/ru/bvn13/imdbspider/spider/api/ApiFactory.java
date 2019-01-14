package ru.bvn13.imdbspider.spider.api;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.imdb.DataType;
import ru.bvn13.imdbspider.imdb.ImdbObject;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.spider.tasker.Task;

import java.util.EnumSet;
import java.util.List;

/**
 * @author boyko_vn at 09.01.2019
 */
public interface ApiFactory {

    List<Task> createTasksForSearchMovieByTitle(String title, int maxCount, EnumSet<MovieDataType> dataTypes) throws ImdbSpiderException;

    Task taskByDataType(DataType dataType) throws DataTypeNotSupportedException;

    void fillUpImdbObject(ImdbObject imdbObject, Task task);

    EnumSet<MovieDataType> getDefaultMovieDataTypeSet();

    void setMovieDataTypeSet(EnumSet<MovieDataType> movieDataTypeSet);

    EnumSet<MovieDataType> getMovieDataTypeSet();

}
