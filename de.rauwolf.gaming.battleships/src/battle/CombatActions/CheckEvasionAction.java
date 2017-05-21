package battle.CombatActions;

import battle.BattleConstants;
import battle.ShipInstance;
import battle.Shot;
import logging.battleLogger.BattleLogger;
import ships.stats.StatType;

public class CheckEvasionAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    // Range in which misses are possible = max chance to miss * total hit range
    private static final int maxRangeToMiss = (int) (BattleConstants.accuracyRandomizerMaximum * (1 - BattleConstants.minChanceToHit));
    
    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        // eva - (acc + accuracyRandomizerMaximum * (1 - minChanceToHit)) = addedAcc
        // eva 150, acc 0 --> 150 - ( 0 + 95) = 55 --> acc 0 + 55 + random VS eva 150
        // eva 120, acc 10 --> 120 - ( 10 + 95) = 15 --> acc 10 + 15 + random VS eva 120
        // eva 80, acc 50 --> 80 - ( 50 + 95) = -65 --> acc 50 + random VS eva 80
        // eva 40, acc 80 --> 40 - ( 80 + 95) = -135 --> acc 80 + random VS eva 40
        final int evasion = ship.getHullStat(StatType.EVASION);
        final int addedAccuracy = Math.max(evasion - shot.accuracy - maxRangeToMiss, 0);
        final int totalAccuracy = shot.accuracy + addedAccuracy + BattleConstants.randomizer.nextInt(BattleConstants.accuracyRandomizerMaximum + 1);
        final boolean targetIsHit = totalAccuracy > evasion;
        logger.isHit(ship, targetIsHit, totalAccuracy, evasion, addedAccuracy);
        return targetIsHit;
    }
}
