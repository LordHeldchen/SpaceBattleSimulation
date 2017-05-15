package battle.CombatActions;

import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;

public class TakeShieldDamageAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        ship.getShieldInstance().takeShieldDamage(shot, logger);
        return shot.amount > 0;
    }
}
