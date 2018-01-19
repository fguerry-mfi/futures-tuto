/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.swing;

import fr.mfi.futures.tuto.async.FunctionnalCallback;

/**
 * Same as previous example, but we included some corrections in the test base
 * (see {@link V04ClickCountTestBase}) to address the known problems.
 *
 * @author fguerry
 */
public class V04ClickCountTest {

    public static void main(String... args) {
        final V04ClickCountTestBase base = new V04ClickCountTestBase();
        base.setListener(e -> {
            FunctionnalCallback.submit(base::runRequest, base::processResponse, base::processError);
        });
        base.showFrame();
    }

}
