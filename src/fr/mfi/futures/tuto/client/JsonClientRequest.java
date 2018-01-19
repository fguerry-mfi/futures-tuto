/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.client;

import java.net.URL;

/**
 * Simple class to represent a request, with an optionnal body to be serialized
 * in JSON, from the client point of view.
 *
 * @author fguerry
 */
public class JsonClientRequest<T> {

    final URL url;
    String method;
    T body;

    public JsonClientRequest(URL url) {
        this.url = url;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T jsonBody) {
        this.body = jsonBody;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
