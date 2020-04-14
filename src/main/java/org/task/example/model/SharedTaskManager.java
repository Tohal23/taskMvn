package org.task.example.model;

import java.util.HashMap;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class SharedTaskManager {
    private static final HashMap<SharedTaskType, FutureTask<SharedTaskResult>> futureCollections = new HashMap<>();
    private static int counterTask;
    private static final Object locker = new Object();

    public static SharedTaskResult getOrStartTask(SharedTaskType taskType, Callable<SharedTaskResult> supplier) throws ExecutionException, InterruptedException {
        FutureTask<SharedTaskResult> future;

        if (futureCollections.containsKey(taskType)) {
            future = futureCollections.get(taskType);
        } else {
            if (supplier == null) return new SharedTaskResult();
            future = new FutureTask<>(supplier);
            futureCollections.put(taskType, future);
        }

        synchronized (locker) {
            counterTask--;
            if (counterTask == 0) {
                locker.notifyAll();
                for (SharedTaskType type : SharedTaskType.values()) {
                    FutureTask<SharedTaskResult> futureStart = futureCollections.get(type);
                    new Thread(futureStart).start();;
                }
            } else {
                locker.wait();
            }
        }

        return future.get();
    }

    public static void setCountTask(int counterTask) {
        SharedTaskManager.counterTask = counterTask;
    }
}
