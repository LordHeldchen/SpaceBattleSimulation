package main.java.battle.CombatActions;

import java.util.List;

import main.java.battle.BattleConstants;
import main.java.battle.ShipInstance;
import main.java.battle.Shot;
import main.java.logging.battleLoggers.BattleLogger;
import main.java.ships.stats.StatType;
import main.java.ships.weapons.WeaponSecondaryEffect;

public class ApplyTicklossEvent implements CombatEvent {
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