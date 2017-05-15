package battle.CombatActions;

import battle.BattleConstants;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;

public class CheckExplodeAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        double explodeChance = ((shot.amount - ship.getContainment().getCalculatedValue()) / ship.getCurrentHullStrength()) * BattleConstants.maxChanceExplodeOnCrit;

        if (explodeChance > BattleConstants.randomizer.nextDouble()) {
            ship.setCurrentHullStrength(0);
            logger.explodes(ship, explodeChance, shot.hitStrength, ship.getThresholdFor(shot.hullDamageLevel).getCalculatedValue(),
                    ship.getContainment().getCalculatedValue());
            return false;
        } else {
            return true;
        }
    }
};