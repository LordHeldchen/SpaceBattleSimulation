package de.rauwolf.gaming.battleships.gui.uiElements;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class HighlightableBsJPanel extends BsJPanel implements MouseListener {
    private static final long   serialVersionUID   = 5867693165613038972L;

    private static final Border HIGHLIGHTED_BORDER = BorderFactory.createLineBorder(Color.RED, 2, false);
    private static final Border NO_BORDER          = BorderFactory.createEmptyBorder(2, 2, 2, 2);

    public HighlightableBsJPanel() {
        super();
        setBorder(NO_BORDER);
    }

    public void setHightlighted(boolean highlighted) {
        if (highlighted) {
            setBorder(HIGHLIGHTED_BORDER);
        } else {
            setBorder(NO_BORDER);
        }
    }
}
