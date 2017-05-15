package battle.CombatActions;

import battle.BattleConstants;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;

public class TakeDamageAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        shot.amount *= BattleConstants.damageLevelToFactorMap.get(shot.hullDamageLevel);
        ship.setCurrentHullStrength(ship.getCurrentHullStrength() - shot.amount);
        logger.takesHullDamage(ship, shot.amount, shot.hullDamageLevel, shot.hitStrength, shot.specificResistanceOfTarget,
                ship.getThresholdFor(shot.hullDamageLevel).getCalculatedValue());
        return ship.getCurrentHullStrength() > 0;
    }
};