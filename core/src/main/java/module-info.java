module imdb.spider.core {
    exports ru.bvn13.imdbspider;
    exports ru.bvn13.imdbspider.exceptions;
    exports ru.bvn13.imdbspider.exceptions.api;
    exports ru.bvn13.imdbspider.exceptions.composer;
    exports ru.bvn13.imdbspider.exceptions.extractor;
    exports ru.bvn13.imdbspider.exceptions.processor;
    exports ru.bvn13.imdbspider.imdb;
    exports ru.bvn13.imdbspider.imdb.accessories;

    requires java.xml;
    requires org.jsoup;

}
