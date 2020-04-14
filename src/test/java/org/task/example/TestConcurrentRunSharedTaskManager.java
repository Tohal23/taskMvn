package org.task.example;

import org.junit.Assert;
import org.junit.Test;
import org.task.example.model.SharedTaskManager;
import org.task.example.model.SharedTaskResult;
import org.task.example.model.SharedTaskState;
import org.task.example.model.SharedTaskType;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class TestConcurrentRunSharedTaskManager {

    @Test
    public void main() {
        String textFirstTask = "111";
        String textSecondTask = "222";

        Callable<SharedTaskResult> firstTask = () -> {
            SharedTaskResult result = new SharedTaskResult(SharedTaskState.Success, textFirstTask);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return result;
        };
        Callable<SharedTaskResult> secondTask = () -> new SharedTaskResult(SharedTaskState.Success, textSecondTask);

        try {
            CompletableFuture firstRequest = CompletableFuture.supplyAsync(() -> {
                try {
                    return SharedTaskManager.getOrStartTask(SharedTaskType.DOCUMENT_MOVING, firstTask);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            });
            Thread.sleep(1000);
            CompletableFuture secondRequest = CompletableFuture.supplyAsync(() -> {
                try {
                    return SharedTaskManager.getOrStartTask(SharedTaskType.DOCUMENT_MOVING, secondTask);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            });

            SharedTaskResult firstResult = (SharedTaskResult) firstRequest.get();
            SharedTaskResult secondResult = (SharedTaskResult) secondRequest.get();

            Assert.assertEquals(textFirstTask, firstResult.getMessage());
            Assert.assertEquals(secondResult.getMessage(), firstResult.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
