package ru.bvn13.imdbspider.spider.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.exceptions.composer.ComposerNotFoundException;
import ru.bvn13.imdbspider.imdb.Movie;
import ru.bvn13.imdbspider.imdb.MovieDataType;
import ru.bvn13.imdbspider.imdb.TaglineList;
import ru.bvn13.imdbspider.spider.api.ApiFactory;
import ru.bvn13.imdbspider.spider.tasker.Task;

/**
 * @author boyko_vn at 15.01.2019
 */
public class MovieComposer extends AbstractImdbObjectComposer implements ImdbObjectComposer<Movie> {

    private TaglineListComposer taglineListComposer;

    public MovieComposer(ApiFactory apiFactory, ImdbObjectComposerFactory imdbObjectComposerFactory) throws ComposerNotFoundException {
        super(apiFactory, imdbObjectComposerFactory);

        taglineListComposer = (TaglineListComposer) this.imdbObjectComposerFactory.getComposer(TaglineList.class);
    }

    @Override
    public Movie compose(Task task) throws ImdbSpiderException {
        Movie movie = new Movie();
        apiFactory.fillUpImdbObject(movie, task);
        for (Task nestedTask : task.getNestedTasks()) {
            apiFactory.fillUpImdbObject(movie, nestedTask);
            if (nestedTask.getDataType().equals(MovieDataType.TAGLINES)) {
                movie.setTaglineList(taglineListComposer.compose(nestedTask));
            }
        }
        return movie;
    }
}
