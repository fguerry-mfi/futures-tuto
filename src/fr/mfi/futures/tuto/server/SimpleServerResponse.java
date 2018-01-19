/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.server;

/**
 * Simple class to represent a response from the server point of view.
 *
 * @author fguerry
 */
public class SimpleServerResponse {

    int code = 200;
    String type;

    public void type(String type) {
        this.type = type;
    }

    public void code(int code) {
        this.code = code;
    }

}
