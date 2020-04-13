package org.task.example.model;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class SharedTaskManager {
    private static final HashMap<SharedTaskType, CompletableFuture> futureCollections = new HashMap<>();

    public static SharedTaskResult getOrStartTask(SharedTaskType taskType, Supplier<SharedTaskResult> supplier) throws ExecutionException, InterruptedException {
        CompletableFuture cf;

        if (futureCollections.containsKey(taskType)) {
            cf = futureCollections.get(taskType);
        } else {
            if (supplier == null) return new SharedTaskResult();
            cf = CompletableFuture.supplyAsync(supplier)
                    .exceptionally((err) -> new SharedTaskResult(SharedTaskState.Failure, err.getMessage()));
            futureCollections.put(taskType, cf);
        }

        return (SharedTaskResult) cf.get();
    }
}
