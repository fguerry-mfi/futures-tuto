package fr.mfi.futures.tuto.simple;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import fr.mfi.futures.tuto.client.JsonClient;
import fr.mfi.futures.tuto.client.JsonClientRequest;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple intuitive and synchronous call to the server.
 *
 * @author fguerry
 */
public class V01SimpleTest {

    public static void main(String... args) {
        try {
            // Request
            final JsonClientRequest<Object> req = new JsonClientRequest<>(new URL("http://localhost:8080/test1"));
            long begin = System.currentTimeMillis();
            final JsonClientResponse<Map> result = new JsonClient().processSyncRequest(req, Map.class);
            // Response
            long end = System.currentTimeMillis();
            Logger.getLogger(V01SimpleTest.class.getName()).log(Level.INFO, "code : " + result.getCode());
            Logger.getLogger(V01SimpleTest.class.getName()).log(Level.INFO, "body : " + result.getBody());
            Logger.getLogger(V01SimpleTest.class.getName()).log(Level.INFO, "duration : " + (end - begin));
        } catch (Throwable ex) {
            // Error
            Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process simple request", ex);
        }
    }
}
