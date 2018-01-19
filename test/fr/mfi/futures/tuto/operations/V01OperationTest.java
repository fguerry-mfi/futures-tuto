/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

import fr.mfi.futures.tuto.client.JsonClient;
import fr.mfi.futures.tuto.client.JsonClientRequest;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import fr.mfi.futures.tuto.simple.V01SimpleTest;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Synchronous call of the server to get several items.
 *
 * @author fguerry
 */
public class V01OperationTest {

    public static void main(String... args) {
        new V01OperationTest().run();
    }

    public void run() {
        try {
            long begin = System.currentTimeMillis();
            final JsonClientRequest<Object> req = new JsonClientRequest<Object>(new URL("http://localhost:8080/items"));
            final JsonClientResponse<ItemList> result = new JsonClient().processSyncRequest(req, ItemList.class);
            Logger.getLogger(V01SimpleTest.class.getName()).log(Level.INFO, "code : " + result.getCode());
            final List<Integer> ids = result.getBody().getItemIds();
            Logger.getLogger(V01SimpleTest.class.getName()).log(Level.INFO, "item ids : " + ids);
            for (Integer id : ids) {
                final JsonClientRequest<Object> subReq = new JsonClientRequest<Object>(new URL("http://localhost:8080/items/" + id));
                final JsonClientResponse<Item> subResult = new JsonClient().processSyncRequest(subReq, Item.class);
                Logger.getLogger(V01OperationTest.class.getName()).log(Level.INFO, "item : " + subResult.getBody());
            }
            long end = System.currentTimeMillis();
            Logger.getLogger(V01OperationTest.class.getName()).log(Level.INFO, "duration : " + (end - begin));
        } catch (Throwable ex) {
            Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process simple request", ex);
        }
    }
}
