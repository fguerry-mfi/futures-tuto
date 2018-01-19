/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import fr.mfi.futures.tuto.async.AsyncWorker;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example of a simple HTTP call with a ListenableFuture and a listener on it to
 * proccess the result.
 *
 * @author fguerry
 */
public class V04FutureTest {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        final ListenableFuture<JsonClientResponse<Map>> futureResult = AsyncWorker.submitListenable(new Callable<JsonClientResponse<Map>>() {
            @Override
            public JsonClientResponse<Map> call() throws Exception {
                return base.runRequest();
            }
        });
        futureResult.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    final JsonClientResponse<Map> result = futureResult.get();
                    base.processResponse(result);
                } catch (Exception ex) {
                    base.processError(ex);
                }
            }
        }, MoreExecutors.directExecutor());
        Logger.getLogger(V04FutureTest.class.getName()).log(Level.INFO, "waiting for job completion...");
        AsyncWorker.stop();
    }
}
