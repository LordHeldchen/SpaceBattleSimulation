package de.rauwolf.gaming.battleships.battle;

import java.util.List;

import de.rauwolf.gaming.battleships.logging.battleLoggers.BattleLogger;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public class ShieldInstance extends CombatActor {
    private final int                 maxShield;
    private final double              regenerationAmount;
    private final ShipInstance        owningShipInstance;
    private final int                 breakDuration;

    private double                    currentShield;

    private static final BattleLogger logger = BattleLogger.getInstance();

    public ShieldInstance(ShipInstance owningShipInstance, int maxShield, int regenerationAmount, int shieldBreakDuration) {
        super(BattleConstants.MAX_INITIATIVE, 1);

        this.owningShipInstance = owningShipInstance;
        this.maxShield = maxShield;
        this.currentShield = maxShield;
        this.regenerationAmount = ((double) regenerationAmount / 10d);
        this.breakDuration = shieldBreakDuration;
    }

    public ShipInstance getOwner() {
        return owningShipInstance;
    }

    public int getCurrentShield() {
        return (int) currentShield;
    }

    public void takeShieldDamage(Shot shot) {
        if (currentShield > 0) {
            double penetratingDamage = 0;

            List<Integer> valueDurationPair = shot.secondaryEffects.get(WeaponSecondaryEffect.SHIELD_PIERCE_PERCENT);
            if (valueDurationPair != null) {
                int shieldPiercePercent = valueDurationPair.get(0);
                penetratingDamage += ((shot.amount * shieldPiercePercent) / 100);
                shot.amount -= Math.abs(penetratingDamage);
            }

            valueDurationPair = shot.secondaryEffects.get(WeaponSecondaryEffect.SHIELD_PIERCE_FLAT);
            if (valueDurationPair != null) {
                Integer shieldPierceFlat = valueDurationPair.get(0);
                penetratingDamage += shieldPierceFlat;
                shot.amount -= Math.abs(penetratingDamage);
            }

            penetratingDamage += (shot.amount < 0 ? shot.amount : 0);
            shot.amount = shot.amount < 0 ? 0 : shot.amount;

            final double shieldBefore = currentShield;
            currentShield = currentShield - shot.amount;

            if (currentShield <= 0) {
                shot.amount = -currentShield;
                if (shieldBefore > 0) {
                    rememberLostTicks(breakDuration);
                    logger.shieldBreaks(this, breakDuration);
                }
                currentShield = 0;
            } else {
                shot.amount = 0;
                logger.takesShieldDamage(this, (shieldBefore - currentShield), shot.weaponName);
            }

            shot.amount += Math.max(penetratingDamage, 0);

            if (shot.amount <= 0) {
                shot.hullDamageLevel = HullDamageLevel.DEFLECT;
                logger.shieldDeflectsAllDamage(owningShipInstance, shot.weaponName);
            }
        }
    }

    @Override
    public CombatTarget takeAction(SingleBattle currentBattle) {
        CombatTarget superTarget = super.takeAction(currentBattle);

        if (maxShield > 0 && currentShield < maxShield) {
            final double regeneration = currentShield + regenerationAmount > maxShield ? maxShield - currentShield : regenerationAmount;
            currentShield += regeneration;
            currentBattle.getBattleLogger().regeneratesShield(this, regeneration, currentShield);
        }
        return superTarget;
    }

    @Override
    public boolean isNoLongerActive() {
        return owningShipInstance.isDestroyed();
    }

    @Override
    final public String toString() {
        return "Shields of " + owningShipInstance + "(ini " + this.getCurrentInitiative() + ")";
    }
}
