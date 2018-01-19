/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import fr.mfi.futures.tuto.async.AsyncWorker;
import fr.mfi.futures.tuto.async.FunctionnalCallback;
import fr.mfi.futures.tuto.client.JsonClient;
import fr.mfi.futures.tuto.client.JsonClientRequest;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import fr.mfi.futures.tuto.simple.V01SimpleTest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Refactor method to get Items from Ids.
 *
 * @author fguerry
 */
public class V05OperationTest {

    public static void main(String... args) {
        new V05OperationTest().run();
    }

    public void run() {
        final List<Integer> ids = Lists.newArrayList(2, 5);
        FunctionnalCallback.addCallback(getItems(ids), this::processItems, this::processError);
        AsyncWorker.stop();
    }

    private ListenableFuture<List<Item>> getItems(Iterable<Integer> ids) {
        final List<ListenableFuture<Item>> futures = new ArrayList<>();
        for (int id : ids) {
            futures.add(getItemAsync(id));
        }
        return Futures.allAsList(futures);
    }

    private void processItems(List<Item> result) {
        for (Item item : result) {
            Logger.getLogger(V05OperationTest.class.getName()).log(Level.INFO, "Item : " + item);
        }
    }

    private void processError(Throwable ex) {
        Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process item request", ex);
    }

    private ListenableFuture<Item> getItemAsync(Integer id) {
        final ListenableFuture<JsonClientResponse<Item>> future = AsyncWorker.submitListenable(() -> getItem(id));
        return Futures.transform(future, JsonClientResponse<Item>::getBody);
    }

    private JsonClientResponse<Item> getItem(Integer id) throws MalformedURLException, IOException {
        final JsonClientRequest<Object> subReq = new JsonClientRequest<Object>(new URL("http://localhost:8080/items/" + id));
        final JsonClientResponse<Item> subResult = new JsonClient().processSyncRequest(subReq, Item.class);
        return subResult;
    }

}
