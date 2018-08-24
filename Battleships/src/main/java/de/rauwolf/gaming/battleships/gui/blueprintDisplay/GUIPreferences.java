package de.rauwolf.gaming.battleships.gui.blueprintDisplay;

public class GUIPreferences {
    private static final GUIPreferences guiPreferences = new GUIPreferences();

    private GUIPreferences() {
    }

    public static GUIPreferences getInstance() {
        return guiPreferences;
    }

    private int displayWidth;
    private int displayHeigth;

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeigth() {
        return displayHeigth;
    }

    public void setDisplayHeigth(int displayHeigth) {
        this.displayHeigth = displayHeigth;
    }
}
