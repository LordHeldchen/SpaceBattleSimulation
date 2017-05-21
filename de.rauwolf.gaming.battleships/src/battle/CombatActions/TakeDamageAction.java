package battle.CombatActions;

import battle.BattleConstants;
import battle.HullDamageLevel;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;

public class TakeDamageAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        if (!shot.hullDamageLevel.equals(HullDamageLevel.DEFLECT)) {
            shot.amount *= BattleConstants.damageLevelToFactorMap.get(shot.hullDamageLevel);
            target.setCurrentHullStrength(target.getCurrentHullStrength() - shot.amount);
            logger.takesHullDamage(target, shot.amount, shot.hullDamageLevel, shot.hitStrength, shot.specificResistanceOfTarget,
                    target.getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()), shot.weaponName);
            return target.getCurrentHullStrength() > 0;
        }
        return true;
    }
};