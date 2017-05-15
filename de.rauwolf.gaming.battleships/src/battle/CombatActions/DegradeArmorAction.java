package battle.CombatActions;

import java.util.List;

import battle.HullDamageLevel;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;
import ships.weapons.WeaponSecondaryEffect;

public class DegradeArmorAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        List<Integer> degradeEffect = shot.secondaryEffects.get(WeaponSecondaryEffect.DEGRADE);
        if (degradeEffect != null && (shot.hullDamageLevel.equals(HullDamageLevel.HIT) || shot.hullDamageLevel.equals(HullDamageLevel.CRIT))) {
            String key = WeaponSecondaryEffect.DEGRADE.toString();
            int val = degradeEffect.get(0);
            ship.getThresholdFor(HullDamageLevel.GLANCE).addFlatBonus(key, val);
            ship.getThresholdFor(HullDamageLevel.HIT).addFlatBonus(key, val);
            ship.getThresholdFor(HullDamageLevel.CRIT).addFlatBonus(key, val);
            logger.shipArmorDegrades(ship, val);
        }
        return true;
    }
};