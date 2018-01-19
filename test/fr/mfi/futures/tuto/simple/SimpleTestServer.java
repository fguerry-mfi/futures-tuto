/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.simple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import fr.mfi.futures.tuto.server.SimpleServer;
import fr.mfi.futures.tuto.server.SimpleServerRequest;
import fr.mfi.futures.tuto.server.SimpleServerResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Example of a simple HTTP server.
 *
 * @author fguerry
 */
public class SimpleTestServer {

    public static void main(String... args) {
        SimpleServer.port(8080);
        SimpleServer.get("/test1", SimpleTestServer::test1);
        SimpleServer.get("/stop", SimpleTestServer::stop);
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    private static Object stop(SimpleServerRequest req, SimpleServerResponse res) throws Exception {
        SimpleServer.stop();
        return "OK";
    }

    private static Object test1(SimpleServerRequest req, SimpleServerResponse res) throws Exception {
        Thread.sleep(500);
        res.type("application/json");
        final Map<String, Object> result = new HashMap<>();
        result.put("message", "Welcome !");
        final ArrayList<Integer> data = Lists.newArrayList(1,5, 7);
        result.put("data", data);
        return mapper.writeValueAsString(result);
    }

}
