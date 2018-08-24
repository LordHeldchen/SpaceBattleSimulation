package de.rauwolf.gaming.battleships.gui.menubars;

import java.awt.event.ActionListener;

public class ActionCommand {

    private final String label;
    private final ActionListener actionListener;

    public ActionCommand(String label, ActionListener actionListener) {
        this.label = label;
        this.actionListener = actionListener;
    }

    public String getLabel() {
        return label;
    }

    public ActionListener getActionListener() {
        return actionListener;
    }
}
