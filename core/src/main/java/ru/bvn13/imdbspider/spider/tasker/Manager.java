package ru.bvn13.imdbspider.spider.tasker;

import ru.bvn13.imdbspider.exceptions.extractor.HtmlExtractorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Manager {

    private Map<String, String> httpRequestHeaders = new HashMap<>();

    public void addHttpRequestHeader(String key, String value) {
        this.httpRequestHeaders.put(key, value);
    }

    public void processTasks(List<Task> allTasks) {

        Map<String, List<Task>> groupedTasks = new ConcurrentHashMap<>(allTasks.size());

        for (Task task : allTasks) {
            List<Task> filteredTasks = null;

            if (groupedTasks.keySet().contains(task.getUrl())) {
                filteredTasks = groupedTasks.get(task.getUrl());
            } else {
                filteredTasks = new ArrayList<>();
                groupedTasks.put(task.getUrl(), filteredTasks);
            }

            filteredTasks.add(task);
        }

        groupedTasks.entrySet().parallelStream().forEach(stringListEntry -> {
            Worker w = new Worker(stringListEntry.getKey(), stringListEntry.getValue());
            try {
                w.run(httpRequestHeaders);
            } catch (HtmlExtractorException e) {
                e.printStackTrace();
            }
        });

        List<Task> nextTasks = new ArrayList<>();

        for (Task task : allTasks) {
            if (task.hasNextTasks()) {
                nextTasks.addAll(task.getNestedTasks());
            }
        }

        if (!nextTasks.isEmpty()) {
            processTasks(nextTasks);
        }

    }

}
