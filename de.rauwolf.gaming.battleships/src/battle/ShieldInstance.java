package battle;

import logging.battleLogger.BattleLogger;

public class ShieldInstance extends CombatActor {
    private final ShipInstance owningShipInstance;
    private final int          maxShield;
    private final int          regenerationAmount;

    private int                currentShield;

    public ShieldInstance(ShipInstance owningShipInstance, int maxStrength, int regenerationAmount,
                    int initiativeDecay, BattleLogger logger) {
        super(owningShipInstance.getBlueprint().getStartBattleSpeed().getCalculatedValue()
                        + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum),
                        initiativeDecay, logger);

        this.owningShipInstance = owningShipInstance;
        this.maxShield = maxStrength;
        this.regenerationAmount = regenerationAmount;
    }

    @Override
    public CombatTarget takeAction() {
        loseInitiative();

        if (maxShield > 0) {
            int regeneration = currentShield + regenerationAmount > maxShield
                            ? maxShield - currentShield
                            : regenerationAmount;
            currentShield += regeneration;
            logger.regeneratesShield(this, regeneration, currentShield);
        }

        return null;
    }

    public ShipInstance getOwner() {
        return owningShipInstance;
    }

    public int getCurrentShield() {
        return currentShield;
    }

    public int takeShieldDamage(int damage, int strength) {
        final int shieldBefore = currentShield;
        currentShield = currentShield - damage > 0 ? currentShield - damage : 0;
        damage = damage - shieldBefore > 0 ? damage - shieldBefore : 0;

        if (shieldBefore > 0) {
            if (currentShield <= 0) {
                logger.shieldBreaks(this);
            } else {
                logger.takesShieldDamage(this, (shieldBefore - currentShield));
            }
        }

        return damage;
    }
}
