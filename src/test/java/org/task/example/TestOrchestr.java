package org.task.example;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.task.example.model.SharedTaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestOrchestr {

    @Test
    public void executeTests() throws ClassNotFoundException, ExecutionException, InterruptedException {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));

        List<CompletableFuture> results = new ArrayList<>();

        String[] classes = new String[]{"org.task.example.TestConcurrentRunSharedTaskManager"
                , "org.task.example.TestSequentialRunSharedTaskManager"};

        SharedTaskManager.setCountTask(classes.length+1);
        for (String className : classes) {
            CompletableFuture test = CompletableFuture.supplyAsync(() -> {
                try {
                    return junit.run(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            });
            results.add(test);
        }

        for (CompletableFuture completableFuture : results) {
            System.out.println(completableFuture.get());
        }
    }

}
