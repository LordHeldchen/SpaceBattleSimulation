package battle.CombatActions;

import java.util.List;

import battle.BattleConstants;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;
import ships.weapons.WeaponSecondaryEffect;

public class ApplyTicklossAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        List<Integer> ticklossEffect = shot.secondaryEffects.get(WeaponSecondaryEffect.TICKLOSS);
        if (ticklossEffect != null) {
            int strength = ticklossEffect.get(0);
            int chanceInPermill = (strength * 1000) / (ship.getEpm().getCalculatedValue() + strength);
            if (chanceInPermill > BattleConstants.randomizer.nextInt(1000)) {
                int lostTicks = ticklossEffect.get(1);
                logger.shipLosesTicks(ship, lostTicks, chanceInPermill);
                ship.addLostTicks(lostTicks);
            }
        }
        return true;
    }
};