package org.task.example;

import org.junit.Assert;
import org.junit.Test;
import org.task.example.model.SharedTaskManager;
import org.task.example.model.SharedTaskResult;
import org.task.example.model.SharedTaskState;
import org.task.example.model.SharedTaskType;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class TestSequentialRunSharedTaskManager {

    @Test
    public void main() {
        String textFirstTask = "111";
        String textSecondTask = "222";

        Callable<SharedTaskResult> firstTask = () -> new SharedTaskResult(SharedTaskState.Success, textFirstTask);
        Callable<SharedTaskResult> secondTask = () -> new SharedTaskResult(SharedTaskState.Success, textSecondTask);

        try {
            SharedTaskResult firstResult = SharedTaskManager.getOrStartTask(SharedTaskType.ARCHIEVE_MOVING, firstTask);
            Thread.sleep(1000);
            //SharedTaskResult secondResult = SharedTaskManager.getOrStartTask(SharedTaskType.ARCHIEVE_MOVING, secondTask);

            Assert.assertEquals(textFirstTask, firstResult.getMessage());
            //Assert.assertEquals(secondResult.getMessage(), firstResult.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}