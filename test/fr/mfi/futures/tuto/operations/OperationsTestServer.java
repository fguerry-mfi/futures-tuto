/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mfi.futures.tuto.server.SimpleServer;
import fr.mfi.futures.tuto.server.SimpleServerRequest;
import fr.mfi.futures.tuto.server.SimpleServerResponse;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Test server that serves a REST-full resource : GET /items for the list, GET
 * /items/{id} for a specific item.
 *
 * @author fguerry
 */
public class OperationsTestServer {

    public static void main(String... args) {
        SimpleServer.port(8080);
        SimpleServer.get("/items", OperationsTestServer::items);
        SimpleServer.get("/stop", OperationsTestServer::stop);
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    private static Object stop(SimpleServerRequest req, SimpleServerResponse res) throws Exception {
        SimpleServer.stop();
        return "OK";
    }

    private static Object items(SimpleServerRequest req, SimpleServerResponse res) throws Exception {
        int delay = 1500;
        if (req.query().containsKey("delay")) {
            try {
                delay = Integer.parseInt(req.query().get("delay"));
            } catch (Exception e) {
            }
        }
        if (delay > 0) {
            Thread.sleep(delay);
        }
        res.type("application/json");
        if (req.path().equals("/items")) {
            final ItemList items = new ItemList();
            items.setItemIds(new ArrayList<>());
            items.getItemIds().add(2);
            items.getItemIds().add(5);
            if (req.query().containsKey("error")) {
                items.getItemIds().add(7);
            }
            if (req.query().containsKey("many")) {
                items.getItemIds().add(8);
                items.getItemIds().add(13);
                items.getItemIds().add(20);
                items.getItemIds().add(23);
                items.getItemIds().add(45);
                items.getItemIds().add(56);
                items.getItemIds().add(67);
                items.getItemIds().add(73);
                items.getItemIds().add(88);
                items.getItemIds().add(91);
                items.getItemIds().add(105);
                items.getItemIds().add(113);
            }
            return mapper.writeValueAsString(items);
        } else if (req.path().startsWith("/items/")) {
            final String[] split = req.path().split("/");
            if (split.length > 2) {
                final int id = Integer.parseInt(split[2]);
                if (id == 7) {
                    res.code(500);
                    final HashMap<String, String> message = new HashMap<String, String>();
                    message.put("message", "item 7 is corrupted");
                    return mapper.writeValueAsString(message);
                }
                return mapper.writeValueAsString(new Item(id, "Item n°" + id));
            }
            res.code(404);
            return mapper.writeValueAsString("Item not found : " + req.path());
        } else {
            res.code(400);
            return null;
        }
    }

}
