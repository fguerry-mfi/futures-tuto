/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import com.google.common.util.concurrent.ListenableFuture;
import fr.mfi.futures.tuto.async.AsyncWorker;
import fr.mfi.futures.tuto.async.FunctionnalCallback;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reduce the code once more with the help of FunctionnaCallback.
 *
 * @author fguerry
 */
public class V09FutureTest {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        final ListenableFuture<JsonClientResponse<Map>> futureResult = AsyncWorker.submitListenable(base::runRequest);
        FunctionnalCallback.addCallback(futureResult, base::processResponse, base::processError);
        Logger.getLogger(V09FutureTest.class.getName()).log(Level.INFO, "waiting for job completion...");
        AsyncWorker.stop();
    }

}
