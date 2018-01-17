package main.java.battle.CombatActions;

import main.java.battle.BattleConstants;
import main.java.battle.HullDamageLevel;
import main.java.battle.ShipInstance;
import main.java.battle.Shot;
import main.java.logging.battleLoggers.BattleLogger;
import main.java.ships.stats.StatType;

public class CheckExplodeAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        if (shot.hullDamageLevel.equals(HullDamageLevel.CRIT)) {
            int containment = target.getHullStat(StatType.CONTAINMENT);
            double explodeChance = ((shot.amount - containment) / target.getCurrentHullStrength())
                    * BattleConstants.maxChanceExplodeOnCrit;

            if (explodeChance > BattleConstants.randomizer.nextDouble()) {
                logger.explodes(target, explodeChance, shot.hitStrength, shot.amount,
                        target.getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()), containment, shot.weaponName);
                target.setCurrentHullStrength(0);
                return false;
            }
        }
        return true;
    }
};