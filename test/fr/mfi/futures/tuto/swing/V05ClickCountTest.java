/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.swing;

import fr.mfi.futures.tuto.async.FunctionnalCallback;
import fr.mfi.futures.tuto.async.SwingExecutor;

/**
 * Final version : the Swing calls are all run in EDT, thanks to refactor of
 * class {@link V05ClickCountTestBase} and use of SwingExecutor.
 *
 * @author fguerry
 */
public class V05ClickCountTest {

    public static void main(String... args) {
        final V05ClickCountTestBase base = new V05ClickCountTestBase();
        base.setListener(e -> {
            base.setWaitingState(true);
            FunctionnalCallback.submit(base::runRequest, base::processResponse, base::processError, SwingExecutor.getInstance());
        });
        base.showFrame();
    }

}
