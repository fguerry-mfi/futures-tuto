/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import fr.mfi.futures.tuto.async.AsyncWorker;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example of the basic Future usage with a job executor. Please note that the
 * thread pool must be stoped in order to the JVM to exit.
 *
 * @author fguerry
 */
public class V03FutureTest {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        final Future<JsonClientResponse<Map>> futureResult = AsyncWorker.submit(new Callable<JsonClientResponse<Map>>() {
            @Override
            public JsonClientResponse<Map> call() throws Exception {
                return base.runRequest();
            }
        });
        Logger.getLogger(V03FutureTest.class.getName()).log(Level.INFO, "waiting for job completion...");
        while (!futureResult.isDone()) {
            // this is bad : in fact we don't know when the job has really ended...
            Thread.sleep(100);
        }
        try {
            final JsonClientResponse<Map> result = futureResult.get();
            // it's not a good habit to use Future.get, but at least it's safe after we are sure that Future.isDone
            base.processResponse(result);
        } catch (Exception ex) {
            base.processError(ex);
        }
        AsyncWorker.stop();
    }
}
