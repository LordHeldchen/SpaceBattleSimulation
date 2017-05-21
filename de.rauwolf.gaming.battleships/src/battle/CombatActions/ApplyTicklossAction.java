package battle.CombatActions;

import java.util.List;

import battle.BattleConstants;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;
import ships.stats.StatType;
import ships.weapons.WeaponSecondaryEffect;

public class ApplyTicklossAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance target, Shot shot) {
        List<Integer> ticklossEffect = shot.secondaryEffects.get(WeaponSecondaryEffect.TICKLOSS);
        if (ticklossEffect != null) {
            int strength = ticklossEffect.get(0);
            int chanceInPermill = (strength * 1000) / (target.getHullStat(StatType.EPM) + strength);
            if (chanceInPermill > BattleConstants.randomizer.nextInt(1000)) {
                int lostTicks = ticklossEffect.get(1);
                logger.shipLosesTicks(target, lostTicks, chanceInPermill);
                target.addLostTicks(lostTicks);
            }
        }
        return true;
    }
};