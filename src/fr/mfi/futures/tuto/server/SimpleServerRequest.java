/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.server;

import java.io.InputStream;
import java.util.Map;

/**
 * Simple class to represent a request from the server point of view.
 *
 * @author fguerry
 */
public class SimpleServerRequest {

    private final String path;
    private final InputStream input;
    private final Map<String, String> query;

    public SimpleServerRequest(String path, Map<String, String> query, InputStream input) {
        this.path = path;
        this.input = input;
        this.query = query;
    }

    public InputStream body() {
        return input;
    }

    public String path() {
        return path;
    }

    public Map<String, String> query() {
        return query;
    }

}
