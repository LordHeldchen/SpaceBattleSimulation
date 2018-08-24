package de.rauwolf.gaming.battleships.gui.uiElements;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;

public interface ClickableElement extends PaintableElement {
    public static final String MOUSE_CLICK_LEFT   = "mouse_click_left";
    public static final String MOUSE_CLICK_RIGHT  = "mouse_click_right";
    public static final String MOUSE_DOUBLE_CLICK = "mouse_double_click";
    public static final String MOUSE_OVER         = "mouse_over";
    public static final String MOUSE_EXIT         = "mouse_exit";
    public static final String MOUSE_UP           = "mouse_up";
    public static final String MOUSE_DOWN         = "mouse_down";

    Cursor getCursor();

    void setCursor(Cursor c);

    Shape getElementShape();

    void onMouseClick(MouseEvent e);

    void onMouseOver(MouseEvent e);

    void onMouseExit(MouseEvent e);

    void onMouseDown(MouseEvent e);

    void onMouseUp(MouseEvent e);
}