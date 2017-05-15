package battle.CombatActions;

import battle.BattleConstants;
import battle.HullDamageLevel;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;
import ships.stats.HullStatType;

public class DetermineHullDamageLevelAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();
    
    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        shot.hitStrength = shot.armorPenetration + (int) (BattleConstants.randomizer.nextFloat() * BattleConstants.penetrationRandomizerMaximum);
        final HullStatType resistanceType = BattleConstants.damageTypeToResistanceType.get(shot.damageType);
        shot.specificResistanceOfTarget = ship.getHullStats().get(resistanceType).getCalculatedValue();
        shot.hitStrength -= shot.specificResistanceOfTarget;
        
        if (ship.getThresholdFor(HullDamageLevel.CRIT).getCalculatedValue() < shot.hitStrength) {
            shot.hullDamageLevel = HullDamageLevel.CRIT;
        } else if (ship.getThresholdFor(HullDamageLevel.HIT).getCalculatedValue() < shot.hitStrength) {
            shot.hullDamageLevel = HullDamageLevel.HIT;
        } else if (ship.getThresholdFor(HullDamageLevel.GLANCE).getCalculatedValue() < shot.hitStrength) {
            shot.hullDamageLevel = HullDamageLevel.GLANCE;
        } else {
            logger.armorDeflectsAllDamage(ship, shot.hitStrength, ship.getThresholdFor(shot.hullDamageLevel).getCalculatedValue());
            return false;
        }
        
        return true;
    }

}
