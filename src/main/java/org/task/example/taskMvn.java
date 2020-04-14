package org.task.example;


import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class taskMvn {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String[]> setup = new CompletableFuture<>();
        delayedFunction(setup);

        //do whatever you want
                System.out.println("foo");

        //once you are ready, complete setup to execute the delayed function
                setup.complete(null);

        System.out.println(Arrays.toString(setup.get()));
    }



    public static CompletableFuture<String[]> delayedFunction(CompletableFuture<String[]> setup) {
        return setup.
                thenCompose(v-> CompletableFuture.supplyAsync(() -> new String[]{"1","2","3"}));
    }
}
