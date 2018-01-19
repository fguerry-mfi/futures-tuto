/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.async;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to create and use a thread pool as an executor for submitting
 * jobs.
 *
 * @author fguerry
 */
public class AsyncWorker {

    private static final ExecutorService executor;
    private static final ListeningExecutorService listeningExecutor;

    static {
        executor = Executors.newFixedThreadPool(10);
        listeningExecutor = MoreExecutors.listeningDecorator(executor);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        return executor.submit(callable);
    }

    public static <T> ListenableFuture<T> submitListenable(Callable<T> callable) {
        return listeningExecutor.submit(callable);
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static ListeningExecutorService getListeningExecutor() {
        return listeningExecutor;
    }

    public static void stop() {
        shutdown();
        await();
    }

    public static void await() {
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException ex) {
            Logger.getLogger(AsyncWorker.class.getName()).log(Level.SEVERE, "don't stop well...", ex);
        }
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
