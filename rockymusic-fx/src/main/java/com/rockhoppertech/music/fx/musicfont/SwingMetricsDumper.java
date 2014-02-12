package com.rockhoppertech.music.fx.musicfont;

/*
 * #%L
 * Rocky Music FX
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.rockhoppertech.music.fx.musicfont.model.SymbolFactory;

public class SwingMetricsDumper extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gc = (Graphics2D) g;
        InputStream is = SwingMetricsDumper.class
                .getResourceAsStream("/fonts/Bravura.otf");

        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        font = font.deriveFont(48f);
        FontMetrics fm = gc.getFontMetrics(font);
        int i = 0xE050;
        String s = SymbolFactory.unicodeToString(0xE050);
        Rectangle2D b = fm.getStringBounds(s, gc);
        System.err.println(b);

    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("PianoRoll Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        SwingMetricsDumper demo = new SwingMetricsDumper();
        frame.setContentPane(demo);

        frame.pack();
        frame.setLocationByPlatform(true); // since JDK 1.5
        frame.setAlwaysOnTop(false); // since JDK 1.5
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
