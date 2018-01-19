/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

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
 * Asynchronous request to get 2 items, and use of allAsList to merge the
 * results.
 *
 * @author fguerry
 */
public class V02OperationTest {

    public static void main(String... args) {
        new V02OperationTest().run();
    }

    public void run() {
        final ListenableFuture<JsonClientResponse<Item>> futureItem2 = AsyncWorker.submitListenable(() -> getItem(2));
        final ListenableFuture<JsonClientResponse<Item>> futureItem5 = AsyncWorker.submitListenable(() -> getItem(5));
        final List<ListenableFuture<JsonClientResponse<Item>>> futures = new ArrayList<>();
        futures.add(futureItem2);
        futures.add(futureItem5);
        final ListenableFuture<List<JsonClientResponse<Item>>> uniqueFuture = Futures.allAsList(futures);
        FunctionnalCallback.addCallback(uniqueFuture, this::processItems, this::processError);
        AsyncWorker.stop();
    }

    private void processItems(List<JsonClientResponse<Item>> result) {
        for (JsonClientResponse<Item> item : result) {
            Logger.getLogger(V02OperationTest.class.getName()).log(Level.INFO, "Item : " + item.getBody());
        }
    }

    private void processError(Throwable ex) {
        Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process item request", ex);
    }

    private JsonClientResponse<Item> getItem(Integer id) throws MalformedURLException, IOException {
        final JsonClientRequest<Object> subReq = new JsonClientRequest<Object>(new URL("http://localhost:8080/items/" + id));
        final JsonClientResponse<Item> subResult = new JsonClient().processSyncRequest(subReq, Item.class);
        return subResult;
    }

}
