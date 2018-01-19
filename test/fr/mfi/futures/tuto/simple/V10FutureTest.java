/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import fr.mfi.futures.tuto.async.AsyncWorker;
import fr.mfi.futures.tuto.async.FunctionnalCallback;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reduce the code once more with the help of FunctionnaCallback.
 *
 * @author fguerry
 */
public class V10FutureTest {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        FunctionnalCallback.submit(base::runRequest, base::processResponse, base::processError);
        Logger.getLogger(V10FutureTest.class.getName()).log(Level.INFO, "waiting for job completion...");
        AsyncWorker.stop();
    }

}
