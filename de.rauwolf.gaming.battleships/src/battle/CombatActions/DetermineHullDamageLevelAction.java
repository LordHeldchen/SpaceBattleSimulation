package battle.CombatActions;

import battle.BattleConstants;
import battle.HullDamageLevel;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;
import ships.stats.StatType;

public class DetermineHullDamageLevelAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();
    
    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        if (!(shot.hullDamageLevel != null && shot.hullDamageLevel.equals(HullDamageLevel.DEFLECT))) {

            shot.hitStrength = shot.armorPenetration + (int) (BattleConstants.randomizer.nextFloat() * BattleConstants.penetrationRandomizerMaximum);
            final StatType resistanceType = BattleConstants.damageTypeToResistanceType.get(shot.damageType);
            shot.specificResistanceOfTarget = target.getHullStat(resistanceType);
            shot.hitStrength -= shot.specificResistanceOfTarget;

            if (target.getHullStat(HullDamageLevel.CRIT.getDefenseStatAgainstDamageLevel()) < shot.hitStrength) {
                shot.hullDamageLevel = HullDamageLevel.CRIT;
            } else if (target.getHullStat(HullDamageLevel.HIT.getDefenseStatAgainstDamageLevel()) < shot.hitStrength) {
                shot.hullDamageLevel = HullDamageLevel.HIT;
            } else if (target.getHullStat(HullDamageLevel.GLANCE.getDefenseStatAgainstDamageLevel()) < shot.hitStrength) {
                shot.hullDamageLevel = HullDamageLevel.GLANCE;
            } else {
                shot.hullDamageLevel = HullDamageLevel.DEFLECT;
                logger.armorDeflectsAllDamage(target, shot.hitStrength, target.getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()), shot.weaponName);
            }
        }
        
        return true;
    }

}
