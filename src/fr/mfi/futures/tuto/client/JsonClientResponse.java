/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.client;

/**
 * Simple class to represent a response containing a parsed JSON body, from the
 * client point of view.
 *
 * @author fguerry
 */
public class JsonClientResponse<T> {

    int code;
    T body;

    public int getCode() {
        return code;
    }

    public T getBody() {
        return body;
    }

}
