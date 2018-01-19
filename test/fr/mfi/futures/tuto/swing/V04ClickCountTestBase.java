/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.swing;

import fr.mfi.futures.tuto.client.JsonClient;
import fr.mfi.futures.tuto.client.JsonClientRequest;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Upgraded version of the {@link V02ClickCountTestBase} class to sovle some
 * problems : avoid multi-click, proper error recovery. Please note : we added
 * some log to trace in which thread the code is run. Some of the Swing related
 * operations are not in the EDT : this is bad we must do something about it.
 *
 * @author fguerry
 */
public class V04ClickCountTestBase {

    public JProgressBar bar;
    public JButton button;
    public JFrame frame;

    public V04ClickCountTestBase() {
        button = new JButton("Click me");
        bar = new JProgressBar();
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.SOUTH);
        frame = new JFrame("Mad clicker...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
    }

    public void showFrame() {
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setListener(ActionListener l) {
        button.addActionListener(l);
    }

    public JsonClientResponse<ClickCount> runRequest() throws MalformedURLException, IOException {
        // Request
        Logger.getLogger(V04ClickCountTestBase.class.getName()).log(Level.INFO, "run request in " + Thread.currentThread().getName());
        bar.setIndeterminate(true);
        button.setEnabled(false);
        final JsonClientRequest<Object> req = new JsonClientRequest<Object>(new URL("http://localhost:8080/click"));
        final JsonClientResponse<ClickCount> result = new JsonClient().processSyncRequest(req, ClickCount.class);
        return result;
    }

    public void processResponse(JsonClientResponse<ClickCount> result) {
        // Response
        Logger.getLogger(V04ClickCountTestBase.class.getName()).log(Level.INFO, "response received in " + Thread.currentThread().getName());
        final ClickCount count = result.getBody();
        button.setText(count.message + " You clicked " + count.clickCount + " times.");
        restoreState();
    }

    public void processError(Throwable ex) {
        // Error
        Logger.getLogger(V04ClickCountTestBase.class.getName()).log(Level.WARNING, "unable to process click count request", ex);
        restoreState();
    }

    private void restoreState() {
        bar.setIndeterminate(false);
        button.setEnabled(true);
    }

}
