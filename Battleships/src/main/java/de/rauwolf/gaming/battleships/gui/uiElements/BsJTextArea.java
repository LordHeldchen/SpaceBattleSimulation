package de.rauwolf.gaming.battleships.gui.uiElements;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;

public class BsJTextArea extends JTextArea implements MouseListener {
    private static final long serialVersionUID = 5867693165613038972L;

    public BsJTextArea() {
        addMouseListener(this);
        setEditable(false);
        setFocusable(false);
        setRequestFocusEnabled(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        getParent().dispatchEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        getParent().dispatchEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        getParent().dispatchEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        getParent().dispatchEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        getParent().dispatchEvent(e);
    }
}
