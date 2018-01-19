package fr.mfi.futures.tuto.simple;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example of a "classic" multithreading approach to run the request.
 *
 * @author fguerry
 */
public class V02ThreadTest {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JsonClientResponse<Map> response = base.runRequest();
                    base.processResponse(response);
                } catch (Throwable e) {
                    base.processError(e);
                }
            }
        });
        thread.start();
        Logger.getLogger(V02ThreadTest.class.getName()).log(Level.INFO, "waiting for job completion...");
    }
}
