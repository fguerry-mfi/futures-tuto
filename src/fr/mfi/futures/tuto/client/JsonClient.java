/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple implementation of a JSON compatible HTTP client : it can execute
 * HTTP request that contains an instance as body, serialize it into JSON,
 * receive the response from the server, then parse back the body as a JSON
 * object.
 *
 * @author fguerry
 */
public class JsonClient {

    private final ObjectMapper mapper;

    public JsonClient() {
        this.mapper = new ObjectMapper();
    }

    public <In, Out> JsonClientResponse<Out> processSyncRequest(JsonClientRequest<In> req, Class<Out> kls) throws IOException {
        final HttpURLConnection cnx = (HttpURLConnection) req.url.openConnection();
        if (req.method != null) {
            cnx.setRequestMethod(req.method);
        }
        if (req.body != null) {
            cnx.setDoOutput(true);
            final OutputStream out = cnx.getOutputStream();
            cnx.addRequestProperty("Content-Type", "application/json");
            mapper.writeValue(out, req.body);
        }
        cnx.connect();
        final JsonClientResponse resp = new JsonClientResponse();
        resp.code = cnx.getResponseCode();
        InputStream input = null;
        final StringBuilder buf = new StringBuilder();
        if (resp.code < 200 || resp.code > 299) {
            buf.append(resp.getCode());
            buf.append(" ");
            buf.append(cnx.getResponseMessage());
        }
        if (cnx.getDoInput()) {
            if (buf.length() > 0) {
                try {
                    resp.body = mapper.readValue(cnx.getErrorStream(), Map.class);
                } catch (IOException ex) {
                    Logger.getLogger(JsonClient.class.getName()).log(Level.WARNING, "HTTP error doesn't contain a valid JSON", ex);
                }
            } else {
                resp.body = mapper.readValue(cnx.getInputStream(), kls);
            }
        }
        cnx.disconnect();
        if (buf.length() > 0) {
            if (resp.body != null) {
                buf.append("\n");
                buf.append(resp.body);
            }
            throw new IOException(buf.toString());
        }
        return resp;
    }

}
