/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import fr.mfi.futures.tuto.async.AsyncWorker;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bad example of the processing of the result of a Future with a transform.
 * With such a solution, the transform function is called only if the Future
 * ends as a succes. For this reason, it's not recommanded to process the result
 * of a Future only in a transform without any callback. Try it withh offline
 * server...
 *
 * @author fguerry
 */
public class V06FutureTestBAD {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        final ListenableFuture<JsonClientResponse<Map>> futureResult = AsyncWorker.submitListenable(new Callable<JsonClientResponse<Map>>() {
            @Override
            public JsonClientResponse<Map> call() throws Exception {
                return base.runRequest();
            }
        });
        Futures.transform(futureResult, new Function<JsonClientResponse<Map>, Void>() {
            @Override
            public Void apply(JsonClientResponse<Map> result) {
                base.processResponse(result);
                return null;
            }
        });
        Logger.getLogger(V06FutureTestBAD.class.getName()).log(Level.INFO, "waiting for job completion...");
        AsyncWorker.stop();
    }
}
