/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.async;

import java.util.concurrent.Executor;
import javax.swing.SwingUtilities;

/**
 * A simple utility class to provide an executor that run its jobs on the Swing
 * EDT.
 *
 * @author fguerry
 */
public class SwingExecutor implements Executor {

    private static final SwingExecutor instance = new SwingExecutor();

    public static SwingExecutor getInstance() {
        return instance;
    }

    private SwingExecutor() {
    }

    @Override
    public void execute(Runnable command) {
        if (SwingUtilities.isEventDispatchThread()) {
            command.run();
        } else {
            SwingUtilities.invokeLater(command);
        }
    }

}
