package battle.CombatActions;

import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;

public class TakeShieldDamageAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        target.getShieldInstance().takeShieldDamage(shot, logger);
        return true;
    }
}
