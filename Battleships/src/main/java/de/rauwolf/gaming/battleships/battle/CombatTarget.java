package de.rauwolf.gaming.battleships.battle;

import java.util.List;

import de.rauwolf.gaming.battleships.logging.battleLoggers.BattleLogger;
import de.rauwolf.gaming.battleships.ships.stats.StatType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public abstract class CombatTarget {
    private static final BattleLogger        logger         = BattleLogger.getInstance();

    // Range in which misses are possible = max chance to miss * total hit range
    private static final int                 maxRangeToMiss = (int) (BattleConstants.accuracyRandomizer * (1 - BattleConstants.minChanceToHit));

    private double                           currentHullStrength;

    public CombatTarget() {
    }

    public boolean reactBeforeAttacker(ShipInstance attacker) {
        // TODO: Common solution dependent on setups. E.g. for ECM or point defense.
        // Other active defense mechanisms that need to be added elsewhere? Active defense of relevance at all?
        return false;
    }

    public void startBattle() {
        this.setCurrentHullStrength(getHullStat(StatType.MAX_HULL_STRENGTH));
    }

    public void endBattle() {
    }

    public abstract String getName();

    public abstract ShieldInstance getShieldInstance();

    public boolean isDestroyed() {
        return getCurrentHullStrength() <= 0;
    }

    public abstract int getHullStat(StatType statType);

    public abstract void addFlatBonusFor(StatType statType, String key, int value);

    public abstract void addFactorFor(StatType statType, String key, double value);

    public abstract void removeFlatBonusFor(StatType statType, String key);

    public abstract void removeFactorFor(StatType statType, String key);

    public double getCurrentHullStrength() {
        return currentHullStrength;
    }

    public void setCurrentHullStrength(double currentHullStrength) {
        this.currentHullStrength = currentHullStrength;
    }

    public void receiveAttack() {
        Shot shot = ShotBuffer.readShot();
        if (tryEvade(shot)) {
            getShieldInstance().takeShieldDamage(shot);
            determineHullDamageLevel(shot);
            if (checkExplosion(shot) && takeDamage(shot)) {
                applyTickloss(shot);
                degradeArmor(shot);
            }
        }
    }

    private boolean tryEvade(Shot shot) {
        // man - (acc + accuracyRandomizerMaximum * (1 - minChanceToHit)) = addedAcc
        // man 150, acc 0 --> 150 - ( 0 + 95) = 55 --> acc 0 + 55 + random VS man 150
        // man 120, acc 10 --> 120 - ( 10 + 95) = 15 --> acc 10 + 15 + random VS man 120
        // man 80, acc 50 --> 80 - ( 50 + 95) = -65 --> acc 50 + random VS man 80
        // man 40, acc 80 --> 40 - ( 80 + 95) = -135 --> acc 80 + random VS man 40
        final int evasion = getHullStat(StatType.MANEUVERABILITY);
        final int addedAccuracy = Math.max(evasion - shot.accuracy - maxRangeToMiss, 0);
        final int totalAccuracy = shot.accuracy + addedAccuracy + BattleConstants.randomizer.nextInt(BattleConstants.accuracyRandomizer + 1);
        final boolean targetIsHit = totalAccuracy > evasion;
        logger.isHit(this, targetIsHit, totalAccuracy, evasion, addedAccuracy, shot.weaponName);
        return targetIsHit;
    }

    private void determineHullDamageLevel(Shot shot) {
        if (shot.hullDamageLevel == null || !shot.hullDamageLevel.equals(HullDamageLevel.DEFLECT)) {
            shot.hitStrength = shot.armorPenetration + (int) (BattleConstants.randomizer.nextFloat() * BattleConstants.PENETRATION_RANDOMIZER_MAXIMUM);
            final StatType resistanceType = BattleConstants.damageTypeToResistanceType.get(shot.damageType);
            shot.specificResistanceOfTarget = getHullStat(resistanceType);
            shot.hitStrength -= shot.specificResistanceOfTarget;

            if (getHullStat(HullDamageLevel.CRIT.getDefenseStatAgainstDamageLevel()) < shot.hitStrength) {
                shot.hullDamageLevel = HullDamageLevel.CRIT;
            } else if (getHullStat(HullDamageLevel.HIT.getDefenseStatAgainstDamageLevel()) < shot.hitStrength) {
                shot.hullDamageLevel = HullDamageLevel.HIT;
            } else if (getHullStat(HullDamageLevel.GLANCE.getDefenseStatAgainstDamageLevel()) < shot.hitStrength) {
                shot.hullDamageLevel = HullDamageLevel.GLANCE;
            } else {
                shot.hullDamageLevel = HullDamageLevel.DEFLECT;
                logger.armorDeflectsAllDamage(this, shot.hitStrength, getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()), shot.weaponName);
            }
        }
    }

    private boolean checkExplosion(Shot shot) {
        if (shot.hullDamageLevel.equals(HullDamageLevel.CRIT)) {
            int containment = getHullStat(StatType.CONTAINMENT);
            double explodeChance = ((shot.amount - containment) / getCurrentHullStrength()) * BattleConstants.MAX_CHANCE_EXPLODE_ON_CRIT;

            if (explodeChance > BattleConstants.randomizer.nextDouble()) {
                logger.explodes(this, explodeChance, shot.hitStrength, shot.amount, getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()),
                        containment, shot.weaponName);
                setCurrentHullStrength(0);
                return false;
            }
        }
        return true;
    }

    private boolean takeDamage(Shot shot) {
        if (!shot.hullDamageLevel.equals(HullDamageLevel.DEFLECT)) {
            shot.amount *= BattleConstants.DAMAGE_LEVEL_TO_FACTOR_MAP.get(shot.hullDamageLevel);
            setCurrentHullStrength(getCurrentHullStrength() - shot.amount);
            logger.takesHullDamage(this, shot.amount, shot.hullDamageLevel, shot.hitStrength, shot.specificResistanceOfTarget,
                    getHullStat(shot.hullDamageLevel.getDefenseStatAgainstDamageLevel()), shot.weaponName);
            return getCurrentHullStrength() > 0;
        }
        return true;
    }

    protected void applyTickloss(Shot shot) {
    }

    private void degradeArmor(Shot shot) {
        List<Integer> degradeEffect = shot.secondaryEffects.get(WeaponSecondaryEffect.DEGRADE);
        if (degradeEffect != null && (shot.hullDamageLevel.equals(HullDamageLevel.HIT) || shot.hullDamageLevel.equals(HullDamageLevel.CRIT))) {
            String key = WeaponSecondaryEffect.DEGRADE.toString();
            int val = degradeEffect.get(0);
            addFlatBonusFor(HullDamageLevel.GLANCE.getDefenseStatAgainstDamageLevel(), key, -val);
            addFlatBonusFor(HullDamageLevel.HIT.getDefenseStatAgainstDamageLevel(), key, -val);
            addFlatBonusFor(HullDamageLevel.CRIT.getDefenseStatAgainstDamageLevel(), key, -val);
            logger.targetArmorDegrades(this, val);
        }
    }
}
