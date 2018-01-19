/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

import com.google.common.base.Function;
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
 * Since previous example, we refactor the transformation of the
 * JsonClientResponse into an Item as a Future.transform and create a method to
 * get the item asynchronously. The signature of methods is more readable and
 * close to a real API.
 *
 * @author fguerry
 */
public class V03OperationTest {

    public static void main(String... args) {
        new V03OperationTest().run();
    }

    public void run() {
        final ListenableFuture<Item> futureItem2 = getItemAsync(2);
        final ListenableFuture<Item> futureItem5 = getItemAsync(5);
        final List<ListenableFuture<Item>> futures = new ArrayList<>();
        futures.add(futureItem2);
        futures.add(futureItem5);
        final ListenableFuture<List<Item>> uniqueFuture = Futures.allAsList(futures);
        FunctionnalCallback.addCallback(uniqueFuture, this::processItems, this::processError);
        AsyncWorker.stop();
    }

    private void processItems(List<Item> result) {
        for (Item item : result) {
            Logger.getLogger(V03OperationTest.class.getName()).log(Level.INFO, "Item : " + item);
        }
    }

    private void processError(Throwable ex) {
        Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process item request", ex);
    }

    private ListenableFuture<Item> getItemAsync(Integer id) {
        final ListenableFuture<JsonClientResponse<Item>> future = AsyncWorker.submitListenable(() -> getItem(id));
        return Futures.transform(future, new Function<JsonClientResponse<Item>, Item>() {
            @Override
            public Item apply(JsonClientResponse<Item> res) {
                return res.getBody();
            }
        });
    }

    private JsonClientResponse<Item> getItem(Integer id) throws MalformedURLException, IOException {
        final JsonClientRequest<Object> subReq = new JsonClientRequest<Object>(new URL("http://localhost:8080/items/" + id));
        final JsonClientResponse<Item> subResult = new JsonClient().processSyncRequest(subReq, Item.class);
        return subResult;
    }

}
