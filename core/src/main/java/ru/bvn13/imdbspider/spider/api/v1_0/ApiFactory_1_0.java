package ru.bvn13.imdbspider.spider.api.v1_0;

import ru.bvn13.imdbspider.exceptions.api.DataTypeNotSupportedException;
import ru.bvn13.imdbspider.imdb.DataType;
import ru.bvn13.imdbspider.imdb.ImdbObject;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 09.01.2019
 */
public class ApiFactory_1_0 implements ApiFactory {

    @Override
    public Task taskByDataType(DataType dataType) throws DataTypeNotSupportedException {
        if (dataType instanceof MovieDataType) {
            return taskByMovieDataType((MovieDataType) dataType);
        } else {
            throw new DataTypeNotSupportedException(String.format("DataType %s not supported by API v1_0!", dataType.getClass().getName()));
        }
    }

    @Override
    public void fulfillImdbObject(ImdbObject imdbObject, Task task) {
        if (imdbObject instanceof Movie) {
            if (task.getDataType() instanceof MovieDataType) {
                fulfillMovie((Movie) imdbObject, task);
            }
        }
    }

    private Task taskByMovieDataType(MovieDataType movieDataType) {
        switch (movieDataType) {
            case TITLE: return new Task();
            default: return null;
        }
    }

    private void fulfillMovie(Movie movie, Task task) {
        switch ((MovieDataType) task.getDataType()) {
            case TITLE: movie.setTitle(task.getResult()); break;
        }
    }

}
