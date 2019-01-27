package ru.bvn13.imdbspider.collection.composer;

import ru.bvn13.imdbspider.imdb.Movie;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bvn13 on 28.01.2019.
 */
public class FileTreeElement {
    enum TYPE {
        UNKNOWN, FILE, DIRECTORY
    }

    private TYPE type = TYPE.UNKNOWN;

    private Path path;
    private String fullPath;
    private FileTreeElement parent;
    private List<FileTreeElement> nestedElements = new ArrayList<>();
    private int depth = 0;

    private Movie movie;

    //#region _GETTERS_SETTERS_

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public FileTreeElement getParent() {
        return parent;
    }

    public void setParent(FileTreeElement parent) {
        this.parent = parent;
        this.depth = parent.depth + 1;
    }

    public List<FileTreeElement> getNestedElements() {
        return nestedElements;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public boolean isDirectory() {
        return type.equals(TYPE.DIRECTORY);
    }

    public boolean isFile() {
        return type.equals(TYPE.FILE);
    }

    public int getDepth() {
        return depth;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    //#endregion

    public void print() {
        StringBuilder value = new StringBuilder();
        for (int i=0; i<depth; i++) {
            value.append(" ");
        }
        value.append("-> ");
        value.append(fullPath);
        System.out.println(value);
        for (FileTreeElement nested : nestedElements) {
            nested.print();
        }
    }
}