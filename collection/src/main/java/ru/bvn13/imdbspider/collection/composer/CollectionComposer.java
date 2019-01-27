package ru.bvn13.imdbspider.collection.composer;

import ru.bvn13.imdbspider.collection.Runner;
import ru.bvn13.imdbspider.collection.exceptions.CollectionComposerException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by bvn13 on 27.01.2019.
 */
public class CollectionComposer {

    private static final class ProcessFile extends SimpleFileVisitor<Path> {
        private FileTreeElement treeElement;
        private List<String> filters = new ArrayList<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            boolean isApropriate = false;
            for (String filter : filters) {
                if (file.getFileName().toString().endsWith(filter) || file.getFileName().toString().matches(filter)) {
                    isApropriate = true;
                    break;
                }
            }
            if (isApropriate) {
                //System.out.println("Processing file:" + file);
                FileTreeElement treeFile = new FileTreeElement();
                treeFile.setParent(treeElement);
                treeFile.setPath(file);
                treeFile.setFullPath(file.toString());
                treeFile.setType(FileTreeElement.TYPE.FILE);
                treeElement.getNestedElements().add(treeFile);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            //System.out.println("Processing directory:" + dir);
            if (treeElement == null) {
                treeElement = new FileTreeElement();
            } else if (!treeElement.getFullPath().equals(dir.toString())) {
                FileTreeElement subTreeElement = new FileTreeElement();
                subTreeElement.setParent(treeElement);
                treeElement.getNestedElements().add(subTreeElement);
                treeElement = subTreeElement;
            }
            treeElement.setPath(dir);
            treeElement.setFullPath(dir.toString());
            treeElement.setType(FileTreeElement.TYPE.DIRECTORY);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
            return FileVisitResult.SKIP_SUBTREE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            FileTreeElement current = treeElement;
            treeElement = treeElement.getParent();
            if (current.getNestedElements().size() == 0) {
                treeElement.getNestedElements().remove(current);
                current = null;
            }
            return FileVisitResult.CONTINUE;
        }

        public List<String> getFilters() {
            return filters;
        }
    }

    private FileTreeElement tree;
    private String collectionTemplate;
    private String movieTemplate;
    private String rowTemplate;

    private ExecutorService executorService;

    public void compose(Runner.Settings settings) throws CollectionComposerException {

        findAllVideoFiles(settings);

        Charset charset = Charset.forName(settings.getEncoding());

        try {
            collectionTemplate = readFile(settings.getCollectionTemplatePath(), charset);
            movieTemplate = readFile(settings.getMovieTemplatePath(), charset);
            rowTemplate = readFile(settings.getRowTemplatePath(), charset);
        } catch (IOException e) {
            throw new CollectionComposerException("Error reading file", e);
        }

        AtomicInteger threadsCount = new AtomicInteger(0);
        AtomicInteger completedCount = new AtomicInteger(0);

        Object object = new Object();

        executorService = Executors.newCachedThreadPool();

        startMovieRetriever(tree, threadsCount, completedCount, fname -> {
            synchronized (object) {
                System.out.print(String.format("Completed: [%d/%d]\r", completedCount.get(), threadsCount.get()));
            }
        });

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new CollectionComposerException("Could not retrieve movie data", e);
        }

        System.out.println("\n");



    }

    private void findAllVideoFiles(Runner.Settings settings) {
        tree = new FileTreeElement();
        tree.setFullPath(settings.getLibraryPath());

        ProcessFile fileProcessor = new ProcessFile();
        fileProcessor.treeElement = tree;

        fileProcessor.getFilters().add(".+\\.3g2$");
        fileProcessor.getFilters().add(".+\\.3gp$");
        fileProcessor.getFilters().add(".+\\.3gp2$");
        fileProcessor.getFilters().add(".+\\.3gpp$");
        fileProcessor.getFilters().add(".+\\.3gpp2$");
        fileProcessor.getFilters().add(".+\\.asf$");
        fileProcessor.getFilters().add(".+\\.asx$");
        fileProcessor.getFilters().add(".+\\.avi$");
        fileProcessor.getFilters().add(".+\\.bin$");
        fileProcessor.getFilters().add(".+\\.dat$");
        fileProcessor.getFilters().add(".+\\.drv$");
        fileProcessor.getFilters().add(".+\\.f4v$");
        fileProcessor.getFilters().add(".+\\.flv$");
        fileProcessor.getFilters().add(".+\\.gtp$");
        fileProcessor.getFilters().add(".+\\.h264$");
        fileProcessor.getFilters().add(".+\\.m4v$");
        fileProcessor.getFilters().add(".+\\.mkv$");
        fileProcessor.getFilters().add(".+\\.mod$");
        fileProcessor.getFilters().add(".+\\.moov$");
        fileProcessor.getFilters().add(".+\\.mov$");
        fileProcessor.getFilters().add(".+\\.mp4$");
        fileProcessor.getFilters().add(".+\\.mpeg$");
        fileProcessor.getFilters().add(".+\\.mpg$");
        fileProcessor.getFilters().add(".+\\.mts$");
        fileProcessor.getFilters().add(".+\\.rm$");
        fileProcessor.getFilters().add(".+\\.rmvb$");
        fileProcessor.getFilters().add(".+\\.spl$");
        fileProcessor.getFilters().add(".+\\.srt$");
        fileProcessor.getFilters().add(".+\\.stl$");
        fileProcessor.getFilters().add(".+\\.swf$");
        fileProcessor.getFilters().add(".+\\.ts$");
        fileProcessor.getFilters().add(".+\\.vcd$");
        fileProcessor.getFilters().add(".+\\.vid$");
        fileProcessor.getFilters().add(".+\\.vid$");
        fileProcessor.getFilters().add(".+\\.vid$");
        fileProcessor.getFilters().add(".+\\.vob$");
        fileProcessor.getFilters().add(".+\\.webm$");
        fileProcessor.getFilters().add(".+\\.wm$");
        fileProcessor.getFilters().add(".+\\.wmv$");
        fileProcessor.getFilters().add(".+\\.yuv$");

        try {
            Files.walkFileTree(Paths.get(settings.getLibraryPath()), fileProcessor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tree.print();
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private void startMovieRetriever(FileTreeElement treeElement, AtomicInteger threadsCount, AtomicInteger completedCount, Consumer<String> callback) {

        if (treeElement.isFile()) {
            threadsCount.incrementAndGet();
            executorService.submit(new MovieRetriever(treeElement, completedCount, callback));
        } else if (treeElement.isDirectory()) {
            for (FileTreeElement nested : treeElement.getNestedElements()) {
                startMovieRetriever(nested, threadsCount, completedCount, callback);
            }
        }

    }

}
