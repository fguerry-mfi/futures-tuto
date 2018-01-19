/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

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
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Error management : manage each error with a fallback, so we can trace the
 * errors and provide a default value.
 *
 * @author fguerry
 */
public class V09OperationTest {

    public static void main(String... args) {
        new V09OperationTest().run();
    }

    long begin;
    long end;

    public void run() {
        begin = System.currentTimeMillis();
        final ListenableFuture<JsonClientResponse<ItemList>> futureResponse = AsyncWorker.submitListenable(this::getItems);
        final ListenableFuture<ItemList> futureList = Futures.transform(futureResponse, JsonClientResponse<ItemList>::getBody);
        final ListenableFuture<List<Integer>> futureIds = Futures.transform(futureList, ItemList::getItemIds);
        final ListenableFuture<List<Item>> futureItems = Futures.transform(futureIds, (AsyncFunction<List<Integer>, List<Item>>) this::getItems);
        FunctionnalCallback.addCallback(futureItems, this::processItems, this::processError);
        AsyncWorker.await();
    }

    private ListenableFuture<List<Item>> getItems(Collection<Integer> ids) {
        Logger.getLogger(V09OperationTest.class.getName()).log(Level.INFO, "item ids : " + ids);
        final List<ListenableFuture<Item>> futures = ids.stream().map(this::getItemAsync).collect(Collectors.toList());
        return Futures.allAsList(futures);
    }

    private void processItems(List<Item> result) {
        for (Item item : result) {
            Logger.getLogger(V09OperationTest.class.getName()).log(Level.INFO, "Item : " + item);
        }
        end = System.currentTimeMillis();
        Logger.getLogger(V09OperationTest.class.getName()).log(Level.INFO, "duration : " + (end - begin));
        AsyncWorker.shutdown();
    }

    private void processError(Throwable ex) {
        Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process item request", ex);
        AsyncWorker.shutdown();
    }

    private ListenableFuture<Item> getItemAsync(Integer id) {
        final ListenableFuture<JsonClientResponse<Item>> futureResponse = AsyncWorker.submitListenable(() -> getItem(id));
        final ListenableFuture<Item> futureItem = Futures.transform(futureResponse, JsonClientResponse<Item>::getBody);
        return Futures.withFallback(futureItem, (Throwable t) -> {
            Logger.getLogger(V09OperationTest.class.getName()).log(Level.WARNING, "received a corrupted item", t);
            return Futures.immediateFuture(new Item(id, "this item is corrupted !"));
        });
    }

    private JsonClientResponse<Item> getItem(Integer id) throws MalformedURLException, IOException {
        final JsonClientRequest<Object> subReq = new JsonClientRequest<Object>(new URL("http://localhost:8080/items/" + id));
        final JsonClientResponse<Item> subResult = new JsonClient().processSyncRequest(subReq, Item.class);
        return subResult;
    }

    private JsonClientResponse<ItemList> getItems() throws MalformedURLException, IOException {
        final JsonClientRequest<Object> req = new JsonClientRequest<Object>(new URL("http://localhost:8080/items?error"));
        final JsonClientResponse<ItemList> result = new JsonClient().processSyncRequest(req, ItemList.class);
        return result;
    }
}
