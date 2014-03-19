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


import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * add these to a component at compass points. The component will be resized in
 * that direction.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class ResizeHandle extends Region {

    private static final Logger logger = LoggerFactory
            .getLogger(ResizeHandle.class);

    public enum ResizeMode {
        TOP, BOTTOM, LEFT, RIGHT, NW, NE, SW, SE
    }

    private ResizeMode resizeMode = ResizeMode.RIGHT;

    private boolean isInside;
    private DragContext dragContext;

    public ResizeHandle() {
        this(ResizeMode.RIGHT);
    }

    public ResizeHandle(ResizeMode mode) {
        getStyleClass().setAll("resizehandle-control");
        this.setStyle("-fx-background-color: red;");
        this.setMinHeight(6);
        this.setMinHeight(6);
        this.setPrefWidth(8);
        this.setPrefHeight(8);
        this.setResizeMode(mode);
        this.dragContext = new DragContext();

        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("resize r x {}", e.getX());
                double xx = dragContext.initialTranslateX
                        + e.getX()
                        - dragContext.mouseAnchorX;
                System.err.println("xx " + xx);
                // xx -= xx % snapX;
                if (xx < 0) {
                    xx = 0;
                }
                System.err.println("sx " + xx);
                Region parent = (Region) ResizeHandle.this.getParent();
                switch (resizeMode) {
                case RIGHT:
                    double w = parent.getWidth() + xx;
                    parent.setPrefWidth(w);
                    break;
                default:
                    break;
                }
                e.consume();

                // ResizeHandle.this.setTranslateX(xx);
                // ResizeHandle.this.setTranslateY(
                // dragContext.initialTranslateY
                // + e.getY()
                // - dragContext.mouseAnchorY);
            }
        });
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("resize r x {}", e.getX());

                dragContext.mouseAnchorX = e.getX();
                dragContext.mouseAnchorY = e.getY();
                dragContext.initialTranslateX =
                        ResizeHandle.this.getTranslateX();
                dragContext.initialTranslateY =
                        ResizeHandle.this.getTranslateY();
                //ResizeHandle.this.setCursor(Cursor.MOVE);
                e.consume();
            }
        });
        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                logger.debug("resize r x {}", e.getX());
                double xx = dragContext.initialTranslateX
                        + e.getX()
                        - dragContext.mouseAnchorX;
                // xx -= xx % 25;
                // xx -= xx % snapX;
                if (xx < 0) {
                    xx = 0;
                }
                e.consume();
                // ResizeHandle.this.setTranslateX(xx);
                // ResizeHandle.this.setTranslateY(
                // dragContext.initialTranslateY
                // + e.getY()
                // - dragContext.mouseAnchorY);

                // logger.debug("released at beat {}", getBeatForX(xx));
                // if (ResizeHandle.this instanceof TrackNode) {
                // TrackNode tn = (TrackNode) node;
                // tn.getTrack().setStartBeat(getBeatForX(xx));
                // }
               // ResizeHandle.this.setCursor(Cursor.DEFAULT);
            }
        });
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                e.consume();
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                e.consume();
            }
        });
    }

    // protected void processMouseMotionEvent(MouseEvent e) {
    // Parent parent = this.getParent();
    // // The mouse coords are in the component's space. The location
    // // we are going to set needs to be in the parent's parent space.
    // Point mpoint = e.getPoint();
    // Point point = SwingUtilities.convertPoint(this, mpoint, parent
    // .getParent());
    // Dimension dim = parent.getSize();
    // Point location = parent.getLocation();
    // Dimension min = parent.getMinimumSize();
    //
    // switch (e.getID()) {
    // case MouseEvent.MOUSE_DRAGGED:
    // logger.debug("dragged " + e);
    //
    // if (this.resizeMode == ResizeMode.RIGHT) {
    // if (point.x - location.x > min.width) {
    // Dimension d = new Dimension((point.x - location.x),
    // dim.height);
    // parent.setSize(d);
    // }
    // }

    // if (this.resizeMode == ResizeMode.LEFT) {
    // if (location.x - point.x + dim.width > min.width) {
    // Dimension d = new Dimension(
    // (location.x - point.x + dim.width), dim.height);
    // logger.debug("new size " + d);
    // parent.setSize(d);
    // parent.setLocation(new Point(point.x, location.y));
    // }
    // }
    //
    // if (this.resizeMode == ResizeMode.TOP) {
    // if (location.y - point.y + dim.height > min.height) {
    // Dimension d = new Dimension(dim.width, (location.y
    // - point.y + dim.height));
    // logger.debug("new size " + d);
    // parent.setSize(d);
    // parent.setLocation(new Point(location.x, point.y));
    // }
    // }
    //
    // if (this.resizeMode == ResizeMode.BOTTOM) {
    // if (point.y - location.y > min.height) {
    // Dimension d = new Dimension(dim.width,
    // (point.y - location.y));
    // logger.debug("new size " + d);
    // parent.setSize(d);
    // }
    // }
    //
    // // width and height
    // if (this.resizeMode == ResizeMode.SE) {
    // if (point.y - location.y > min.height
    // && point.x - location.x > min.width) {
    // Dimension d = new Dimension((point.x - location.x),
    // (point.y - location.y));
    // logger.debug("new size " + d);
    // parent.setSize(d);
    // }
    // }
    //
    // // width and height and location
    // if (this.resizeMode == ResizeMode.SW) {
    // if (true) {
    // Dimension d = new Dimension(location.x - point.x
    // + dim.width, point.y - location.y);
    // logger.debug("new size " + d);
    // parent.setSize(d);
    // parent.setLocation(new Point(point.x, point.y - d.height));
    // }
    // }
    //
    // // if (location.x - point.x + dim.width > min.width) }
    //
    // // width and height and location
    // if (this.resizeMode == ResizeMode.NE) {
    // int deltax = Math.abs(point.x - location.x);
    // int deltay = Math.abs(point.y - location.y);
    // Dimension d = null;
    //
    // // get bigger
    // if (point.y < location.y) {
    // logger.debug("<deltay " + deltay);
    // d = new Dimension(parent.getSize().width,
    // parent.getSize().height + deltay);
    // parent.setSize(d);
    // parent.setLocation(new Point(location.x, point.y));
    // }
    //
    // if (point.x > location.x) {
    // deltax -= parent.getSize().width;
    // d = new Dimension(parent.getSize().width + deltax, parent
    // .getSize().height);
    // parent.setSize(d);
    // parent.setLocation(new Point(location.x, point.y));
    // }
    //
    // // get smaller
    //
    // if (point.y > location.y) {
    // logger.debug(">deltay " + deltay);
    // d = new Dimension(parent.getSize().width,
    // parent.getSize().height - deltay);
    // parent.setSize(d);
    // parent.setLocation(new Point(location.x, point.y));
    // }
    //
    // logger.debug("new size " + d);
    //
    // }
    //
    // // width and height and location
    // if (this.resizeMode == ResizeMode.NW) {
    // Dimension d = null;
    // if (point.x > location.x || point.y > location.y) {
    // int deltax = point.x - location.x;
    // int deltay = point.y - location.y;
    // d = new Dimension(dim.width - deltax, dim.height - deltay);
    // } else if (point.x < location.x || point.y < location.y) {
    // d = new Dimension(location.x - point.x + dim.width,
    // location.y - point.y + dim.height);
    // }
    // logger.debug("new size " + d);
    // parent.setSize(d);
    // parent.setLocation(new Point(point.x, point.y));
    //
    // }

    // break;
    // }
    //
    // }

    // protected void processMouseEvent(MouseEvent e) {
    // switch (e.getID()) {
    // case MouseEvent.MOUSE_PRESSED:
    // // Point p = this.getLocation();
    // // this.dragOffsetX = e.getX() - p.x;
    // // this.dragOffsetY = e.getY() - p.y;
    // break;
    // case MouseEvent.MOUSE_RELEASED:
    //
    // break;
    // case MouseEvent.MOUSE_CLICKED:
    // // this.notifyListeners();
    //
    // break;
    // case MouseEvent.MOUSE_ENTERED:
    // this.requestFocus();
    // this.isInside = true;
    //
    // break;
    // case MouseEvent.MOUSE_EXITED:
    // this.isInside = false;
    //
    // break;
    // }
    //
    // }

    /**
     * @return the resizeMode
     */
    public ResizeMode getResizeMode() {
        return this.resizeMode;
    }

    /**
     * @param resizeMode
     *            the resizeMode to set
     */
    public void setResizeMode(ResizeMode resizeMode) {
        this.resizeMode = resizeMode;
        switch (resizeMode) {
        case RIGHT:
            this.setCursor(Cursor.E_RESIZE);
            break;
        case LEFT:
            this.setCursor(Cursor.W_RESIZE);
            break;
        case TOP:
            this.setCursor(Cursor.N_RESIZE);
            break;
        case BOTTOM:
            this.setCursor(Cursor.S_RESIZE);
            break;
        case NE:
            this.setCursor(Cursor.NE_RESIZE);
            break;
        case NW:
            this.setCursor(Cursor.NW_RESIZE);
            break;
        case SE:
            this.setCursor(Cursor.SE_RESIZE);
            break;
        case SW:
            this.setCursor(Cursor.SW_RESIZE);
            break;
        }
    }

}
