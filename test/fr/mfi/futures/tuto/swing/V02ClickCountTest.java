/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.swing;

import fr.mfi.futures.tuto.client.JsonClientResponse;

/**
 * Refactor of the previous example to extract relevant steps into a helper
 * class (see {@link V02ClickCountTestBase}).
 *
 * @author fguerry
 */
public class V02ClickCountTest {

    public static void main(String... args) {
        final V02ClickCountTestBase base = new V02ClickCountTestBase();
        base.setListener(e -> {
            try {
                JsonClientResponse<ClickCount> result = base.runRequest();
                base.processResponse(result);
            } catch (Throwable ex) {
                base.processError(ex);
            }
        });
        base.showFrame();
    }

}
