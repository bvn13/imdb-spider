package ru.bvn13.imdbspider.collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import ru.bvn13.imdbspider.collection.composer.CollectionComposer;
import ru.bvn13.imdbspider.collection.exceptions.CollectionComposerException;

/**
 * Hello world!
 *
 */
public class Runner {

    public static final class Settings {

        @Option(name = "--library-path", aliases = {"-l"}, usage = "Library path", required = true)
        private String libraryPath;

        @Option(name = "--output-path", aliases = {"-o"}, usage = "Output path", required = true)
        private String outputPath;

        @Option(name = "--recursive", aliases = {"-r"}, usage = "Recursive")
        private boolean isRecursive;

        @Option(name = "--collection-template", aliases = {"-ct"}, usage = "HTML as Collection template")
        private String collectionTemplatePath;

        @Option(name = "--movie-template", aliases = {"-mt"}, usage = "HTML as Movie template")
        private String movieTemplatePath;

        @Option(name = "--row-template", aliases = {"-rt"}, usage = "HTML as row template")
        private String rowTemplatePath;

        @Option(name = "--movies-per-row", aliases = {"-mr"}, usage = "Number, count of movies into one row (default = 3)")
        private int moviesPerRow;

        @Option(name = "--collection-identifier", aliases = {"-ci"}, usage = "Substring in Collection template to be replaced with composed Collection block (default = '{{collection}}')")
        private String collectionIdentifier;

        @Option(name = "--row-identifier", aliases = {"-ri"}, usage = "Substring in Row template to be replaced with composed Row block (default = '{{row]]')")
        private String rowIdentigier;

        @Option(name = "--movie-title-identifier", aliases = {"-mti"}, usage = "Substring in Movie template to be replaced with movie title (default '{{title}}')")
        private String movieTitleIdentifier;

        @Option(name = "--movie-poster-identifier", aliases = {"-mpi"}, usage = "Substring in Movie template to be replaced with movie poster (default '{{poster}}')")
        private String moviePosterIdentifier;

        @Option(name = "--movie-filename-identifier", aliases = {"-mfi"}, usage = "Substring in Movie template to be replaced with movie filename (default '{{filename}}')")
        private String movieFilenameIdentifier;

        @Option(name = "--categorized", aliases = {"-c"}, usage = "Use first level directory into Library path as category")
        private boolean isCategorized;

        @Option(name = "--encoding", aliases = {"-e"}, usage = "Encoding (default = utf-8)")
        private String encoding;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }

        //#region _GETTERS_

        public String getLibraryPath() {
            return libraryPath;
        }

        public String getOutputPath() {
            return outputPath;
        }

        public boolean isRecursive() {
            return isRecursive;
        }

        public String getCollectionTemplatePath() {
            return collectionTemplatePath;
        }

        public String getMovieTemplatePath() {
            return movieTemplatePath;
        }

        public String getCollectionIdentifier() {
            return collectionIdentifier;
        }

        public boolean isCategorized() {
            return isCategorized;
        }

        public String getEncoding() {
            return encoding;
        }

        public String getMovieTitleIdentifier() {
            return movieTitleIdentifier;
        }

        public String getMoviePosterIdentifier() {
            return moviePosterIdentifier;
        }

        public String getRowTemplatePath() {
            return rowTemplatePath;
        }

        public int getMoviesPerRow() {
            return moviesPerRow;
        }

        public String getRowIdentigier() {
            return rowIdentigier;
        }

        //#endregion
    }

    private void fillUpWithDefaults(Settings settings) {

        if (settings.encoding == null || settings.encoding.trim().isEmpty()) {
            settings.encoding = "utf-8";
        }

        if (settings.collectionTemplatePath == null || settings.collectionTemplatePath.isEmpty()) {
            settings.collectionTemplatePath = getClass().getResource("/templates/collection.html").getPath();
        }

        if (settings.movieTemplatePath == null || settings.movieTemplatePath.isEmpty()) {
            settings.movieTemplatePath = getClass().getResource("/templates/movie.html").getPath();
        }

        if (settings.rowTemplatePath == null || settings.rowTemplatePath.isEmpty()) {
            settings.rowTemplatePath = getClass().getResource("/templates/row.html").getPath();
        }

        if (settings.collectionIdentifier == null || settings.collectionIdentifier.trim().isEmpty()) {
            settings.collectionIdentifier = "{{collection}}";
        }

        if (settings.rowIdentigier == null || settings.rowIdentigier.isEmpty()) {
            settings.rowIdentigier = "{{row}}";
        }

        if (settings.movieTitleIdentifier == null || settings.movieTitleIdentifier.isEmpty()) {
            settings.movieTitleIdentifier = "{{title}}";
        }

        if (settings.moviePosterIdentifier == null || settings.moviePosterIdentifier.isEmpty()) {
            settings.moviePosterIdentifier = "{{poster}}";
        }

        if (settings.movieFilenameIdentifier == null || settings.movieFilenameIdentifier.isEmpty()) {
            settings.movieFilenameIdentifier = "{{filename}}";
        }

        if (settings.moviesPerRow <= 0) {
            settings.moviesPerRow = 3;
        }

    }


    private void start(String[] args) throws CollectionComposerException {
        Settings settings = new Settings();
        CmdLineParser parser = new CmdLineParser(settings);

        try {
            parser.parseArgument(args);
            System.out.println("settings = " + settings);
        } catch (CmdLineException e) {
            System.err.println("e = " + e.toString());
            parser.printUsage(System.out);
            return;
        }

        fillUpWithDefaults(settings);

        CollectionComposer composer = new CollectionComposer();

        composer.compose(settings);
    }


    public static void main(String[] args) throws CollectionComposerException {

        Runner runner = new Runner();
        runner.start(args);


    }



}
