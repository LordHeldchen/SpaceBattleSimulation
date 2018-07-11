package main.java.battle.CombatActions;

import main.java.battle.ShipInstance;
import main.java.battle.Shot;

public interface CombatEvent {
    public boolean execute(ShipInstance target, Shot shot);
}
