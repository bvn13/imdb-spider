package ru.bvn13.imdbspider.spider.tasker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author boyko_vn at 09.01.2019
 */
public class Manager {

    private ExecutorService executor;

    public Manager() {
        this.executor = Executors.newCachedThreadPool();
    }


    public List<Task> processTasks(List<Task> allTasks) throws ExecutionException, InterruptedException {

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

        List<Task> result = Collections.synchronizedList(new ArrayList<>());

        groupedTasks.entrySet().parallelStream().forEach(stringListEntry -> {
            Future<List<Task>> r = executor.submit(new Worker(stringListEntry.getKey(), stringListEntry.getValue()));
            while (!r.isDone()) {
                Thread.yield();
            }
            try {
                result.addAll(r.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });


        return result;

    }

}
