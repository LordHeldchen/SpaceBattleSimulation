package de.rauwolf.gaming.battleships.gui.uiElements;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface PaintableElement {
    public void translate(int x, int y);

    public void setVisible(boolean v);

    public void drawInto(Graphics g);

    public Rectangle getBounds();
}