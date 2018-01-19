/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple to use implementation of an HTTP server that one can bind with routes
 * to handle requests. Inspired by the Spark framework.
 *
 * @author fguerry
 */
public class SimpleServer {

    public static interface Route {

        Object process(SimpleServerRequest request, SimpleServerResponse response) throws Exception;
    }

    private static final SimpleServer sharedInstance = new SimpleServer();

    public static void port(int port) {
        synchronized (sharedInstance) {
            sharedInstance.startPort = port;
            sharedInstance.mayStartServer();
        }
    }

    public static int port() {
        synchronized (sharedInstance) {
            return sharedInstance.getServerPort();
        }
    }

    public static void post(String path, Route route) {
        synchronized (sharedInstance) {
            sharedInstance.addRoute(path, "POST", route);
            sharedInstance.mayStartServer();
        }
    }

    public static void get(String path, Route route) {
        synchronized (sharedInstance) {
            sharedInstance.addRoute(path, "GET", route);
            sharedInstance.mayStartServer();
        }
    }

    public static void awaitInitialization() {
        synchronized (sharedInstance) {
            sharedInstance.mayStartServer();
        }
    }

    public static void stop() {
        synchronized (sharedInstance) {
            sharedInstance.stopServer();
        }
    }

    private static class RouteHandler {

        private String path;
        private String method;
        private Route route;

        public RouteHandler(String path, String method, Route route) {
            super();
            this.path = path;
            this.method = method;
            this.route = route;
        }

        public boolean matchPath(String path) {
            return path.startsWith(this.path);
        }

        public boolean matchMethod(String method) {
            return Objects.equals(method, this.method);
        }
    }

    private int startPort = 0;
    private HttpServer server;
    private final List<RouteHandler> routes;
    private ExecutorService pool;

    public SimpleServer() {
        routes = new ArrayList<>();
    }

    private synchronized void mayStartServer() {
        try {
            if (server == null) {
                server = HttpServer.create(new InetSocketAddress(startPort), 0);
                pool = Executors.newFixedThreadPool(50);
                server.setExecutor(pool);
                server.createContext("/", this::handle);
                server.start();
                Logger.getLogger(SimpleServer.class.getName()).log(Level.INFO, "Server started at " + server.getAddress());
            }
        } catch (IOException e) {
            throw new RuntimeException("unable to start server", e);
        }
    }

    private void handle(HttpExchange exch) throws IOException {
        try {
            final URI uri = exch.getRequestURI();
            final String method = exch.getRequestMethod();
            final String path = uri.getPath();
            Logger.getLogger(SimpleServer.class.getName()).log(Level.FINER, method + " on " + path);
            final Headers headers = exch.getRequestHeaders();
            headers.forEach((k, v) -> Logger.getLogger(SimpleServer.class.getName()).log(Level.FINER, " Header : " + k + " = " + v));
            RouteHandler selectedRoute = null;
            for (RouteHandler route : routes) {
                if (route.matchPath(path) && route.matchMethod(method)) {
                    selectedRoute = route;
                    break;
                }
            }
            if (selectedRoute != null) {
                handleWithRoute(exch, selectedRoute);
            } else {
                exch.sendResponseHeaders(404, -1);
            }
        } finally {
            exch.close();
        }
    }

    private void handleWithRoute(HttpExchange exch, RouteHandler handler) throws IOException {
        try {
            final SimpleServerResponse res = new SimpleServerResponse();
            final URI uri = exch.getRequestURI();
            final String rawQuery = uri.getQuery();
            final Map<String, String> query = new HashMap<>();
            if (rawQuery != null) {
                final String[] args = rawQuery.split("&");
                for (String arg : args) {
                    String key = null;
                    String value = null;
                    int cut = arg.indexOf("=");
                    if (cut >= 0) {
                        key = arg.substring(0, cut);
                        value = arg.substring(cut + 1);
                    } else {
                        key = arg;
                        value = "";
                    }
                    query.put(key, value);
                }
            }
            final Object result = handler.route.process(new SimpleServerRequest(uri.getPath(), query, exch.getRequestBody()), res);
            if (res.type != null) {
                exch.getResponseHeaders().put("Content-Type", Arrays.asList(res.type));
            }
            if (result != null) {
                exch.sendResponseHeaders(res.code, 0);
                serialize(result, exch.getResponseBody());
            } else {
                exch.sendResponseHeaders(res.code, -1);
            }
        } catch (Throwable e) {
            exch.sendResponseHeaders(500, 0);
            e.printStackTrace(new PrintStream(exch.getResponseBody()));
        }
    }

    private void serialize(Object result, OutputStream body) throws IOException {
        if (result instanceof String) {
            final String value = (String) result;
            body.write(value.getBytes("UTF8"));
        } else {
            throw new IllegalArgumentException("unmanaged data type : " + result.getClass());
        }
    }

    public synchronized void stopServer() {
        if (pool != null) {
            pool.submit(this::stopNow);
        } else {
            stopNow();
        }
    }

    public synchronized void stopNow() {
        if (server != null) {
            server.stop(1);
            server = null;
        }
        if (pool != null) {
            pool.shutdown();
        }
    }

    public void addRoute(String path, String method, Route route) {
        synchronized (routes) {
            routes.add(new RouteHandler(path, method, route));
        }
    }

    public int getServerPort() {
        if (server != null && server.getAddress() != null) {
            return server.getAddress().getPort();
        }
        return 0;
    }

}
