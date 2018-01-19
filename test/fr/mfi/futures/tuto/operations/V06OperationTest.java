/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
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
 * Add getItems query, and use it (after transform) to provide ID list : after a
 * first Future, if we need more futures, we can transform with AsyncFunction.
 *
 * @author fguerry
 */
public class V06OperationTest {

    public static void main(String... args) {
        new V06OperationTest().run();
    }

    public void run() {
        final ListenableFuture<JsonClientResponse<ItemList>> futureResponse = AsyncWorker.submitListenable(this::getItems);
        final ListenableFuture<List<Integer>> futureIds = Futures.transform(futureResponse, new Function<JsonClientResponse<ItemList>, List<Integer>>() {
            @Override
            public List<Integer> apply(JsonClientResponse<ItemList> res) {
                return res.getBody().getItemIds();
            }
        });
        final ListenableFuture<List<Item>> futureItems = Futures.transform(futureIds, new AsyncFunction<List<Integer>, List<Item>>() {
            @Override
            public ListenableFuture<List<Item>> apply(List<Integer> ids) throws Exception {
                return getItems(ids);
            }
        });
        FunctionnalCallback.addCallback(futureItems, this::processItems, this::processError);
        AsyncWorker.await();
    }

    private JsonClientResponse<ItemList> getItems() throws MalformedURLException, IOException {
        final JsonClientRequest<Object> req = new JsonClientRequest<Object>(new URL("http://localhost:8080/items"));
        final JsonClientResponse<ItemList> result = new JsonClient().processSyncRequest(req, ItemList.class);
        return result;
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
            Logger.getLogger(V06OperationTest.class.getName()).log(Level.INFO, "Item : " + item);
        }
        AsyncWorker.shutdown();
    }

    private void processError(Throwable ex) {
        Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process item request", ex);
        AsyncWorker.shutdown();
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
