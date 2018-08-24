package de.rauwolf.gaming.battleships.gui.uiElements;

import java.awt.Font;

import javax.swing.JLabel;

public class BsJLabel extends JLabel {
    private static final long serialVersionUID   = 7268529557737467168L;

    private static final int  FONT_SIZE_TITLE    = 24;
    private static final int  FONT_SIZE_SUBTITLE = 18;

    public BsJLabel() {
    }

    public BsJLabel(String string) {
        this();
        setText(string);
    }

    private void changeFont(int newSize, int fontWeight) {
        final Font font = getFont();
        final String fontName = font.getFontName();
        setFont(new Font(fontName, fontWeight, newSize));
    }

    public void setTitleCase() {
        changeFont(FONT_SIZE_TITLE, Font.BOLD);
    }

    public void setSubtitleCase() {
        changeFont(FONT_SIZE_SUBTITLE, Font.PLAIN);
    }
}
