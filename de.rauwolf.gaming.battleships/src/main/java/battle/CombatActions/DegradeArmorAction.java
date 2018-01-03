package main.java.battle.CombatActions;

import java.util.List;

import main.java.battle.HullDamageLevel;
import main.java.battle.ShipInstance;
import main.java.battle.Shot;
import main.java.logging.battleLoggers.BattleLogger;
import main.java.ships.weapons.WeaponSecondaryEffect;

public class DegradeArmorAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        List<Integer> degradeEffect = shot.secondaryEffects.get(WeaponSecondaryEffect.DEGRADE);
        if (degradeEffect != null && (shot.hullDamageLevel.equals(HullDamageLevel.HIT) || shot.hullDamageLevel.equals(HullDamageLevel.CRIT))) {
            String key = WeaponSecondaryEffect.DEGRADE.toString();
            int val = degradeEffect.get(0);
            target.addFlatBonusFor(HullDamageLevel.GLANCE.getDefenseStatAgainstDamageLevel(), key, -val);
            target.addFlatBonusFor(HullDamageLevel.HIT.getDefenseStatAgainstDamageLevel(), key, -val);
            target.addFlatBonusFor(HullDamageLevel.CRIT.getDefenseStatAgainstDamageLevel(), key, -val);
            logger.shipArmorDegrades(target, val);
        }
        return true;
    }
};