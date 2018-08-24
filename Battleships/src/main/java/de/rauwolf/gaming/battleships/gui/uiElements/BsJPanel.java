package de.rauwolf.gaming.battleships.gui.uiElements;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class BsJPanel extends JPanel implements MouseListener {
    private static final long serialVersionUID = 5867693165613038972L;

    public BsJPanel() {
        super(new MigLayout());

        addMouseListener(this);
        setFocusable(false);
        setRequestFocusEnabled(false);
    }

    public void wrapAndAdd(Component comp, String... constraints) {
        JPanel dummy = new JPanel();
        add(dummy, "wrap");
        add(comp, String.join(", ", constraints) + ", wrap");
    }

    public void addAndWrap(Component comp, String... constraints) {
        add(comp, String.join(", ", constraints) + ", wrap");
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
