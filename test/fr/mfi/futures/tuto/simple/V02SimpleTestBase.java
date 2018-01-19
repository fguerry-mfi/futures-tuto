/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import fr.mfi.futures.tuto.client.JsonClient;
import fr.mfi.futures.tuto.client.JsonClientRequest;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This simple class cut the process of running a HTTP request into 3 methods
 * with simple roles..
 *
 * @author fguerry
 */
public class V02SimpleTestBase {

    private long begin;

    public JsonClientResponse<Map> runRequest() throws Exception {
        final JsonClientRequest<Object> req = new JsonClientRequest<>(new URL("http://localhost:8080/test1"));
        begin = System.currentTimeMillis();
        final JsonClientResponse<Map> result = new JsonClient().processSyncRequest(req, Map.class);
        return result;
    }

    public void processResponse(JsonClientResponse<Map> result) {
        long end = System.currentTimeMillis();
        Logger.getLogger(V02SimpleTestBase.class.getName()).log(Level.INFO, "code : " + result.getCode());
        Logger.getLogger(V02SimpleTestBase.class.getName()).log(Level.INFO, "body : " + result.getBody());
        Logger.getLogger(V02SimpleTestBase.class.getName()).log(Level.INFO, "duration : " + (end - begin));
    }

    public void processError(Throwable t) {
        Logger.getLogger(V02SimpleTestBase.class.getName()).log(Level.WARNING, "unable to process simple request", t);
    }
}
