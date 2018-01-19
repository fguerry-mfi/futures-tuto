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
 * Last version of {@link V02ClickCountTestBase} that solves all the issues we
 * have seen. All calls to Swing methods must be run in EDT.
 *
 * @author fguerry
 */
public class V05ClickCountTestBase {

    public JProgressBar bar;
    public JButton button;
    public JFrame frame;

    public V05ClickCountTestBase() {
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

    public void setWaitingState(boolean waiting) {
        Logger.getLogger(V05ClickCountTestBase.class.getName()).log(Level.INFO, "set waiting state in " + Thread.currentThread().getName());
        bar.setIndeterminate(waiting);
        button.setEnabled(!waiting);
    }

    public JsonClientResponse<ClickCount> runRequest() throws MalformedURLException, IOException {
        // Request
        Logger.getLogger(V05ClickCountTestBase.class.getName()).log(Level.INFO, "run request in " + Thread.currentThread().getName());
        final JsonClientRequest<Object> req = new JsonClientRequest<Object>(new URL("http://localhost:8080/click"));
        final JsonClientResponse<ClickCount> result = new JsonClient().processSyncRequest(req, ClickCount.class);
        return result;
    }

    public void processResponse(JsonClientResponse<ClickCount> result) {
        // Response
        Logger.getLogger(V05ClickCountTestBase.class.getName()).log(Level.INFO, "response received in " + Thread.currentThread().getName());
        final ClickCount count = result.getBody();
        button.setText(count.message + " You clicked " + count.clickCount + " times.");
        setWaitingState(false);
    }

    public void processError(Throwable ex) {
        // Error
        Logger.getLogger(V05ClickCountTestBase.class.getName()).log(Level.WARNING, "unable to process click count request", ex);
        setWaitingState(false);
    }

}
