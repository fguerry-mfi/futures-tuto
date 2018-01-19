/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.swing;

import fr.mfi.futures.tuto.async.FunctionnalCallback;

/**
 * Convert the previous example from naive (linear) approach to asynchroneous,
 * with the help of Futures (and, more specifically, the FunctionnalCallback we
 * already used in previous series). Please note the following problem : click
 * several times rapidly on the button, and show how the text and progress bar
 * are affected. Other problem : try to use it when server is stopped.
 *
 * @author fguerry
 */
public class V03ClickCountTest {

    public static void main(String... args) {
        final V02ClickCountTestBase base = new V02ClickCountTestBase();
        base.setListener(e -> {
            FunctionnalCallback.submit(base::runRequest, base::processResponse, base::processError);
        });
        base.showFrame();
    }

}
