package fr.mfi.futures.tuto.simple;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.util.Map;

/**
 * Same as previous version (see {@link V01SimpleTest}) but with each code block
 * refactored into a helper class (see {@link V02SimpleTestBase}).
 *
 * @author fguerry
 */
public class V02SimpleTest {

    public static void main(String... args) throws Exception {
        final V02SimpleTestBase base = new V02SimpleTestBase();
        try {
            final JsonClientResponse<Map> response = base.runRequest();
            base.processResponse(response);
        } catch (Throwable e) {
            base.processError(e);
        }
    }
}
