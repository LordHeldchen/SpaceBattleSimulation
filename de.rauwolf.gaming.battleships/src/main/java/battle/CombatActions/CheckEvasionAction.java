package main.java.battle.CombatActions;

import main.java.battle.BattleConstants;
import main.java.battle.ShipInstance;
import main.java.battle.Shot;
import main.java.logging.battleLoggers.BattleLogger;
import main.java.ships.stats.StatType;

public class CheckEvasionAction implements CombatAction {
    private static final BattleLogger logger = BattleLogger.getInstance();

    // Range in which misses are possible = max chance to miss * total hit range
    private static final int maxRangeToMiss = (int) (BattleConstants.accuracyRandomizer * (1 - BattleConstants.minChanceToHit));
    
    @Override
    public boolean execute(ShipInstance ship, Shot shot) {
        // man - (acc + accuracyRandomizerMaximum * (1 - minChanceToHit)) = addedAcc
        // man 150, acc 0 --> 150 - ( 0 + 95) = 55 --> acc 0 + 55 + random VS man 150
        // man 120, acc 10 --> 120 - ( 10 + 95) = 15 --> acc 10 + 15 + random VS man 120
        // man 80, acc 50 --> 80 - ( 50 + 95) = -65 --> acc 50 + random VS man 80
        // man 40, acc 80 --> 40 - ( 80 + 95) = -135 --> acc 80 + random VS man 40
        final int evasion = ship.getHullStat(StatType.MANEUVERABILITY);
        final int addedAccuracy = Math.max(evasion - shot.accuracy - maxRangeToMiss, 0);
        final int totalAccuracy = shot.accuracy + addedAccuracy + BattleConstants.randomizer.nextInt(BattleConstants.accuracyRandomizer + 1);
        final boolean targetIsHit = totalAccuracy > evasion;
        logger.isHit(ship, targetIsHit, totalAccuracy, evasion, addedAccuracy, shot.weaponName);
        return targetIsHit;
    }
}
