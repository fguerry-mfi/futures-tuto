/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.swing;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mfi.futures.tuto.server.SimpleServer;
import fr.mfi.futures.tuto.server.SimpleServerRequest;
import fr.mfi.futures.tuto.server.SimpleServerResponse;

/**
 * This HTTP server implementation provide an URL that keep track of its click
 * count (number of times it has been requested). We will use it to display the
 * click count value dynamically in a UI that calls for it.
 *
 * @author fguerry
 */
public class ClickTestServer {

    public static void main(String... args) {
        new ClickTestServer().start();
    }

    private final ObjectMapper mapper;
    private int clickCount;

    public ClickTestServer() {
        mapper = new ObjectMapper();
        clickCount = 0;
    }

    public void start() {
        SimpleServer.port(8080);
        SimpleServer.get("/click", this::click);
        SimpleServer.get("/stop", this::stop);
    }

    private Object stop(SimpleServerRequest req, SimpleServerResponse res) throws Exception {
        SimpleServer.stop();
        return "OK";
    }

    private Object click(SimpleServerRequest req, SimpleServerResponse res) throws Exception {
        Thread.sleep(2000);
        res.type("application/json");
        final ClickCount count = new ClickCount();
        count.clickCount = ++clickCount;
        count.message = "Welcome, mad clicker !";
        return mapper.writeValueAsString(count);
    }

}
