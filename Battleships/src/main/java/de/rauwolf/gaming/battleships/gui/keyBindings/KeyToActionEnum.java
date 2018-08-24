package de.rauwolf.gaming.battleships.gui.keyBindings;

import java.util.ArrayList;

public enum KeyToActionEnum {
    TMP("tmp", false);

    public String  cmd;
    public int     key;
    public int     modifiers;
    public boolean isRepeatable;

    private KeyToActionEnum(String cmd, boolean repeatable) {
        this.cmd = cmd;
        this.key = -1;
        this.modifiers = 0;
        this.isRepeatable = repeatable;
    }

    public static ArrayList<KeyToActionEnum> getBindByKey(int keycode, int modifiers) {
        ArrayList<KeyToActionEnum> binds = new ArrayList<KeyToActionEnum>();
        for (KeyToActionEnum bind : values()) {
            if (bind.key == keycode && bind.modifiers == modifiers) {
                binds.add(bind);
            }
        }
        return binds;
    }

    public static KeyToActionEnum getBindByCmd(String cmd) {
        for (KeyToActionEnum bind : values()) {
            if (bind.cmd.equals(cmd)) {
                return bind;
            }
        }
        return null;
    }

}
