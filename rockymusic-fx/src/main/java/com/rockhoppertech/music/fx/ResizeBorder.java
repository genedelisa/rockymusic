package com.rockhoppertech.music.fx;

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


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeSupport;

import javafx.scene.layout.Region;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResizeBorder extends Region {

    /**
     * 
     */
    private static final long serialVersionUID = -3303737374216949092L;
    private static final Logger logger = LoggerFactory
            .getLogger(ResizeBorder.class);

    /**
     */
    private class ResizeHandleRect {
        int thickness;
        Color lineColor;
        int size;
        Rectangle r;

        ResizeHandleRect(int x, int y, int w, int h) {
            this.r = new Rectangle(x, y, w, h);
            this.size = 4;
            this.thickness = 1;
            this.lineColor = Color.red;
        }

        boolean contains(int x, int y) {
            return r.contains(x, y);
        }

        public String toString() {
            return "x " + r.x + " y " + r.y + " width " + r.width + " height "
                    + r.height;
        }
    }

    // ///////

    /**
     * Creates a line border with the specified color, thickness, and corner
     * shape.
     * 
     * @param color
     *            the color of the border
     * @param thickness
     *            the thickness of the border
     */
    public ResizeBorder(Color color, int thickness) {
        lineColor = color;
        this.thickness = thickness;
        // this.handles = new ResizeHandle[9];
        this.handleSize = 8;
        this.changes = new PropertyChangeSupport(this);
    }

    /**
     * Returns the color of the border.
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Returns the thickness of the border.
     */
    public int getThickness() {
        return thickness;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque() {
        return true;
    }

    // void makeHandles(int x, int y, int width, int height) {
    // this.handles = new ResizeHandle[8];
    // int sz = this.handleSize;
    // int hsz = sz / 2;
    //
    // // top
    // handles[0] = new ResizeHandle(x - hsz, y - hsz, sz, sz);
    // handles[1] = new ResizeHandle(x + width / 2, y - hsz, sz, sz);
    // handles[2] = new ResizeHandle(x + width - hsz, y - hsz, sz, sz);
    //
    // // middle
    // handles[3] = new ResizeHandle(x - hsz, y + height / 2 - hsz, sz, sz);
    // handles[4] = new ResizeHandle(x + width - hsz, y + height / 2 - hsz,
    // sz, sz);
    //
    // // bottom
    // handles[5] = new ResizeHandle(x - hsz, y + height - hsz, sz, sz);
    // handles[6] = new ResizeHandle(x + width / 2, y + height - hsz, sz, sz);
    // handles[7] = new ResizeHandle(x + width - hsz, y + height - hsz, sz, sz);
    // }

    // public int hitDetect(int x, int y) {
    // if (handles[0] == null)
    // return ResizeBorder.NONE;
    //
    // for (int i = 0; i < handles.length; i++) {
    // if (handles[i].contains(x, y)) {
    // this.selectedHandle = handles[i];
    // this.selectedHandleIndex = i;
    // if (i == 3 || i == 4) // just the sides. needs to be better than
    // // this
    // return i;
    // }
    // }
    // this.selectedHandle = null;
    // this.selectedHandleIndex = ResizeBorder.NONE;
    // return ResizeBorder.NONE;
    // }

    void setResizeCursor(JComponent component) {
        switch (this.selectedHandleIndex) {
        case ResizeBorder.UL:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
            break;
        case ResizeBorder.UM:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            break;
        case ResizeBorder.UR:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
            break;
        case ResizeBorder.CL:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            break;
        case ResizeBorder.CR:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            break;
        case ResizeBorder.BL:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
            break;
        case ResizeBorder.BM:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
            break;
        case ResizeBorder.BR:
            component.setCursor(Cursor
                    .getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            break;
        case ResizeBorder.NONE:
            break;
        }
    }

    private Rectangle r;
    private PropertyChangeSupport changes;
    public static final String RESIZE_CHANGING = "ResizeChanging";
    public static final String RESIZE_CHANGED = "ResizeChanged";
    private Graphics graphics;

    protected int thickness;
    protected Color lineColor;
    // protected ResizeHandle[] handles;
    // protected ResizeHandle selectedHandle;
    protected int selectedHandleIndex;
    private Rectangle selectedRect;
    private int handleSize;

    public static final int UL = 0;
    public static final int UM = 1;
    public static final int UR = 2;
    public static final int CL = 3;
    public static final int CR = 4;
    public static final int BL = 5;
    public static final int BM = 6;
    public static final int BR = 7;
    public static final int NONE = -1;
}
