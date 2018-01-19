/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.async;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Utility class to factorize some common operations with Future Callbacks, so
 * that we can use easilly functionnal interfaces and lambda expressions into
 * calling code.
 *
 * @author fguerry
 */
public class FunctionnalCallback<T> implements FutureCallback<T> {

    private final Consumer<T> succes;
    private final Consumer<Throwable> error;

    public FunctionnalCallback(Consumer<T> succes, Consumer<Throwable> error) {
        this.succes = succes;
        this.error = error;
    }

    @Override
    public void onSuccess(T v) {
        succes.accept(v);
    }

    @Override
    public void onFailure(Throwable thrwbl) {
        error.accept(thrwbl);
    }

    public static <V> void addCallback(ListenableFuture<V> future, Consumer<V> succes, Consumer<Throwable> error) {
        Futures.addCallback(future, new FunctionnalCallback<V>(succes, error));
    }

    public static <V> void submit(Callable<V> callable, Consumer<V> succes, Consumer<Throwable> error) {
        addCallback(AsyncWorker.submitListenable(callable), succes, error);
    }

    public static <V> void addCallback(ListenableFuture<V> future, Consumer<V> succes, Consumer<Throwable> error, Executor executor) {
        Futures.addCallback(future, new FunctionnalCallback<V>(succes, error), executor);
    }

    public static <V> void submit(Callable<V> callable, Consumer<V> succes, Consumer<Throwable> error, Executor executor) {
        addCallback(AsyncWorker.submitListenable(callable), succes, error, executor);
    }

}
