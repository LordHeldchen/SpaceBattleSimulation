package main.java.battle.CombatActions;

import main.java.battle.BattleConstants;
import main.java.battle.HullDamageLevel;
import main.java.battle.ShipInstance;
import main.java.battle.Shot;
import main.java.logging.battleLoggers.BattleLogger;

public class TakeDamageEvent implements CombatEvent {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        if (!shot.hullDamageLevel.equals(HullDamageLevel.DEFLECT)) {
            shot.amount *= BattleConstants.DAMAGE_LEVEL_TO_FACTOR_MAP.get(shot.hullDamageLevel);
            target.setCurrentHullStrength(target.getCurrentHullStrength() - shot.amount);
            logger.takesHullDamage(target, shot.amount, shot.hullDamageLevel, shot.hitStrength, shot.specificResistanceOfTarget,
                    target.getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()), shot.weaponName);
            return target.getCurrentHullStrength() > 0;
        }
        return true;
    }
};