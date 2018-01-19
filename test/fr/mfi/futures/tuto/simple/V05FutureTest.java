/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import fr.mfi.futures.tuto.async.AsyncWorker;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example of a HTTP request with a ListenableFuture and a FutureCallback.
 *
 * @author fguerry
 */
public class V05FutureTest {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        final ListenableFuture<JsonClientResponse<Map>> futureResult = AsyncWorker.submitListenable(new Callable<JsonClientResponse<Map>>() {
            @Override
            public JsonClientResponse<Map> call() throws Exception {
                return base.runRequest();
            }
        });
        Futures.addCallback(futureResult, new FutureCallback<JsonClientResponse<Map>>() {
            @Override
            public void onSuccess(JsonClientResponse<Map> result) {
                base.processResponse(result);
            }

            @Override
            public void onFailure(Throwable thrwbl) {
                base.processError(thrwbl);
            }
        });
        Logger.getLogger(V05FutureTest.class.getName()).log(Level.INFO, "waiting for job completion...");
        AsyncWorker.stop();
    }
}
