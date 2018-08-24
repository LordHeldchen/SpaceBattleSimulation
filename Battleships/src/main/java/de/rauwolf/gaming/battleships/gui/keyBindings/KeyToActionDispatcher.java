package de.rauwolf.gaming.battleships.gui.keyBindings;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import de.rauwolf.gaming.battleships.gui.blueprintDisplay.BlueprintDesignerGUI;

public class KeyToActionDispatcher implements KeyEventDispatcher {

    public BlueprintDesignerGUI                   clientgui       = null;

    protected Set<KeyToActionEnum>                keyCmdSet;

    protected Map<String, ArrayList<ActionEvent>> cmdActionMap;

    protected Timer                               keyRepeatTimer;

    /**
     * Map that keeps track of the tasks that are currently repeating
     */
    protected Map<KeyToActionEnum, TimerTask>     repeatingTasks;

    public KeyToActionDispatcher() {
        keyCmdSet = new HashSet<KeyToActionEnum>();
        cmdActionMap = new HashMap<String, ArrayList<ActionEvent>>();
        keyRepeatTimer = new Timer("Key Repeat Timer");
        repeatingTasks = new HashMap<KeyToActionEnum, TimerTask>();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent evt) {
        if (clientgui != null) {
            return false;
        }

        int keyCode = evt.getKeyCode();
        int modifiers = evt.getModifiers();
        // Get a collection of key/cmd binds that match the keycode/modifiers
        ArrayList<KeyToActionEnum> kcbs = KeyToActionEnum.getBindByKey(keyCode, modifiers);

        // If there's no action associated with this key bind, or the
        // current action is invalid, do not consume this event.
        boolean consumed = false;

        for (KeyToActionEnum kcb : kcbs) {
            if (!keyCmdSet.contains(kcb)) {
                continue;
            }

            // Get the actions associated with this key/cmd binding
            ArrayList<ActionEvent> actions = cmdActionMap.get(kcb.cmd);
            // Skip if we don't have an action vector
            if (actions == null) {
                continue;
            }
            for (ActionEvent action : actions) {
                if (action == null) {
                    continue;
                }
                consumed = true;

                if (evt.getID() == KeyEvent.KEY_PRESSED) {
                    if (kcb.isRepeatable) {
                        startRepeating(kcb, action);
                    } else {
                        // XXX Should do something
                    }
                }

                if (evt.getID() == KeyEvent.KEY_RELEASED) {
                    if (kcb.isRepeatable) {
                        stopRepeating(kcb);
                    }
                }
            }
        }
        // If we had a binding, this event should be considered consumed
        return consumed;
    }

    public synchronized void registerKeyCommandBind(KeyToActionEnum kcb) {
        keyCmdSet.add(kcb);
    }

    public synchronized void registerActionEvent(String cmd, ActionEvent action) {
        ArrayList<ActionEvent> actions = cmdActionMap.get(cmd);
        if (actions == null) {
            actions = new ArrayList<ActionEvent>();
            actions.add(action);
            cmdActionMap.put(cmd, actions);
        } else {
            actions.add(action);
        }
    }

    public synchronized void removeAllActions() {
        for (ArrayList<ActionEvent> actions : cmdActionMap.values()) {
            actions.clear();
        }
    }

    protected void startRepeating(KeyToActionEnum kcb, final ActionEvent action) {
        if (repeatingTasks.containsKey(kcb)) {
            return;
        }

        if (action == null) {
            return;
        }

        TimerTask tt = new TimerTask() {
            public void run() {
                Thread.yield();
            }
        };
        repeatingTasks.put(kcb, tt);
    }

    /**
     * Stops the repeat timer task for the given KeyCommandBind.
     * 
     * @param kcb
     */
    public void stopRepeating(KeyToActionEnum kcb) {
        // If we're not repeating, there's nothing to cancel
        if (!repeatingTasks.containsKey(kcb)) {
            return;
        }
        repeatingTasks.get(kcb).cancel();
        repeatingTasks.remove(kcb);
    }

    /**
     * Stop all repeat timers.
     */
    public void stopAllRepeating() {
        for (KeyToActionEnum kcb : repeatingTasks.keySet()) {
            repeatingTasks.get(kcb).cancel();
            repeatingTasks.remove(kcb);
        }
    }

}
