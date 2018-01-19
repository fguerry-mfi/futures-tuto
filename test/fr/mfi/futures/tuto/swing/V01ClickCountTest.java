/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.swing;

import fr.mfi.futures.tuto.client.JsonClient;
import fr.mfi.futures.tuto.client.JsonClientRequest;
import fr.mfi.futures.tuto.client.JsonClientResponse;
import fr.mfi.futures.tuto.simple.V01SimpleTest;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Example of a Swing application that request the click count on a HTTP server,
 * and display it in a button. This is a naive approach, the server call will
 * freeze the Swing EDT.
 *
 * @author fguerry
 */
public class V01ClickCountTest {

    public static void main(String... args) {
        final JButton button = new JButton("Click me");
        final JProgressBar bar = new JProgressBar();
        final JPanel panel = new JPanel(new BorderLayout());
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Request
                    bar.setIndeterminate(true);
                    final JsonClientRequest<Object> req = new JsonClientRequest<>(new URL("http://localhost:8080/click"));
                    final JsonClientResponse<ClickCount> result = new JsonClient().processSyncRequest(req, ClickCount.class);
                    // Response
                    final ClickCount count = result.getBody();
                    button.setText(count.message + " You clicked " + count.clickCount + " times.");
                    bar.setIndeterminate(false);
                } catch (Throwable ex) {
                    // Error
                    Logger.getLogger(V01SimpleTest.class.getName()).log(Level.WARNING, "unable to process click count request", ex);
                }
            }
        });
        panel.add(button, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.SOUTH);
        final JFrame frame = new JFrame("Mad clicker...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
