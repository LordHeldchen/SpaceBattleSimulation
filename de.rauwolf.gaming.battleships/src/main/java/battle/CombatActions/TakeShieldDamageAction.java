package main.java.battle.CombatActions;

import main.java.battle.ShipInstance;
import main.java.battle.Shot;
import main.java.logging.battleLoggers.BattleLogger;

public class TakeShieldDamageAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        target.getShieldInstance().takeShieldDamage(shot, logger);
        return true;
    }
}
