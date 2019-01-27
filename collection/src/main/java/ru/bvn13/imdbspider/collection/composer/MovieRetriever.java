package ru.bvn13.imdbspider.collection.composer;

import ru.bvn13.imdbspider.ImdbSpider;
import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;
import ru.bvn13.imdbspider.imdb.MovieList;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by bvn13 on 28.01.2019.
 */
public class MovieRetriever implements Runnable {

    private FileTreeElement treeElement;
    private AtomicInteger completedCount;
    private Consumer<String> callback;

    public MovieRetriever(FileTreeElement treeElement, AtomicInteger completedCount, Consumer<String> callback) {
        this.treeElement = treeElement;
        this.completedCount = completedCount;
        this.callback = callback;
    }

    private String prepareFileName(String roughFileName) {
        String filename = "";

        return roughFileName;
    }


    @Override
    public void run() {

        String roughFilename = treeElement.getPath().getFileName().toString();
        String filename = prepareFileName(roughFilename);

        ImdbSpider spider = ImdbSpider.withApi_1_0();
        try {
            MovieList result = spider.searchMovieByTitle(filename);



        } catch (ImdbSpiderException e) {
            e.printStackTrace();
        }

        completedCount.incrementAndGet();

        callback.accept(roughFilename);
    }


}
