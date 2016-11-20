package battle;

import logging.battleLogger.BattleLogger;
import ships.blueprints.Blueprint;

public class ShieldInstance implements CombatActor {
    private final ShipInstance ship;
    private final Blueprint    shipBlueprint;
    private final int          maxShield;
    private final int          regenerationAmount;

    private final int          battleSpeedDecay;

    private int                currentBattleSpeed;
    private int                currentShield;

    private final BattleLogger logger;

    public ShieldInstance(ShipInstance ship, BattleLogger logger, int maxStrength,
                    int regenerationAmount, int battleSpeedDecay) {
        this.ship = ship;
        this.maxShield = maxStrength;
        this.regenerationAmount = regenerationAmount;
        this.battleSpeedDecay = battleSpeedDecay;
        this.shipBlueprint = ship.getBlueprint();
        this.logger = logger;

        currentBattleSpeed = shipBlueprint.getStartBattleSpeed().getCalculatedValue()
                        + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);
    }

    @Override
    public void takeAction() {
        currentBattleSpeed -= battleSpeedDecay;

        if (maxShield > 0) {
            int regeneration = currentShield + regenerationAmount > maxShield
                            ? maxShield - currentShield
                            : regenerationAmount;
            currentShield += regeneration;
            logger.regeneratesShield(this, regeneration, currentShield);
        }
    }

    public ShipInstance getOwner() {
        return ship;
    }
}