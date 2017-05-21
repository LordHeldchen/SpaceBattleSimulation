package battle.CombatActions;

import battle.BattleConstants;
import battle.HullDamageLevel;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;
import ships.stats.StatType;

public class CheckExplodeAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        if (shot.hullDamageLevel.equals(HullDamageLevel.CRIT)) {
            int containment = target.getHullStat(StatType.CONTAINMENT);
            double explodeChance = ((shot.amount - containment) / target.getCurrentHullStrength())
                    * BattleConstants.maxChanceExplodeOnCrit;

            if (explodeChance > BattleConstants.randomizer.nextDouble()) {
                target.setCurrentHullStrength(0);
                logger.explodes(target, explodeChance, shot.hitStrength,
                        target.getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()), containment, shot.weaponName);
                return false;
            }
        }
        return true;
    }
};