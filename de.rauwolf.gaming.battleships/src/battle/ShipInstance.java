package battle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import logging.battleLogger.BattleLogger;
import logging.battleLogger.HullDamageLevel;
import ships.blueprints.Blueprint;
import ships.blueprints.ComponentBlueprint;
import ships.blueprints.MutableBaseStat;
import ships.blueprints.WeaponBlueprint;
import ships.shipHulls.HullStatType;
import ships.shipHulls.DamageType;
import ships.shipHulls.ValueDurationPair;

public class ShipInstance implements CombatTarget {
    protected final Blueprint                        blueprint;

    private ShieldInstance                           shieldInstance;

    private final MutableBaseStat                    maxHullStrength;
    private double                                   currentHullStrength;

    // Holding the threshold as MutableBaseStats again allows e.g. for temporary
    // armor lowering effects and the like.
    // TODO: Werte hier auf Map von "BaseHullStat" umstellen, machts vsl.
    // übersichtlicher.
    // Sollte auch init per loop ermöglichen.
    private final MutableBaseStat                    glanceThreshold;
    private final MutableBaseStat                    hitThreshold;
    private final MutableBaseStat                    critThreshold;
    private final MutableBaseStat                    containment;
    private final MutableBaseStat                    epm;
    private final MutableBaseStat                    evasion;

    private final ShipInstance                       mothership;

    private final int                                idOfOwningEmpire;
    private BattleLogger                             logger;

    private LinkedList<CombatActor>                  combatActorsOfShip;

    // Derzeit nur Resistances
    private final Map<HullStatType, MutableBaseStat> hullStats;

    private int calculateFinalValueFor(int baseValue, HullStatType value, boolean scalesWithSize) {
        List<Integer> flatModifiers = new LinkedList<Integer>();
        List<Double> factorModifiers = new LinkedList<Double>();
        for (ComponentBlueprint component : blueprint.getComponents()) {
            Integer flatBonus = component.getFlatBonusFor(value);
            if (flatBonus != null) {
                flatModifiers.add(flatBonus);
            }
            Double factor = component.getFactorFor(value);
            if (factor != null) {
                factorModifiers.add(factor);
            }
        }
        for (Integer flatModifier : flatModifiers) {
            baseValue += flatModifier;
        }
        for (Double factor : factorModifiers) {
            baseValue = (int) Math.round(baseValue * factor);
        }
        if (scalesWithSize) {
            baseValue *= BattleConstants.shipSizeScaling.get(blueprint.getHullType().getHullSize());
        }
        return baseValue;
    }

    public ShipInstance(int idOfOwningEmpire, ShipInstance mothership, Blueprint blueprint) {
        this.idOfOwningEmpire = idOfOwningEmpire;
        this.blueprint = blueprint;
        this.mothership = mothership;

        this.glanceThreshold = new MutableBaseStat(calculateFinalValueFor(blueprint.getArmorGlanceThreshold(), HullStatType.GLANCE_THRESHOLD, false));
        this.hitThreshold = new MutableBaseStat(calculateFinalValueFor(blueprint.getArmorHitThreshold(), HullStatType.HIT_THRESHOLD, false));
        this.critThreshold = new MutableBaseStat(calculateFinalValueFor(blueprint.getArmorCritThreshold(), HullStatType.CRIT_THRESHOLD, false));
        this.containment = new MutableBaseStat(calculateFinalValueFor(blueprint.getContainment(), HullStatType.CONTAINMENT, false));
        this.epm = new MutableBaseStat(calculateFinalValueFor(blueprint.getHullType().getBaseEPM(), HullStatType.EPM, true));
        this.evasion = new MutableBaseStat(calculateFinalValueFor(blueprint.getEvasion(), HullStatType.EVASION, false));

        this.hullStats = new HashMap<HullStatType, MutableBaseStat>();
        for (HullStatType hullStatType : HullStatType.values()) {
            hullStats.put(hullStatType, new MutableBaseStat(calculateFinalValueFor(0, hullStatType, false)));
        }

        this.maxHullStrength = new MutableBaseStat(blueprint.getMaxHullStrength());
        this.currentHullStrength = this.maxHullStrength.getCalculatedValue();
    }

    public final int getIdOfOwningEmpire() {
        return idOfOwningEmpire;
    }

    private List<CombatActor> getWeaponInstances(int startingInitiative) {
        List<CombatActor> weaponInstances = new LinkedList<CombatActor>();

        for (WeaponBlueprint weaponBlueprint : blueprint.getWeapons()) {
            int damage = weaponBlueprint.getDamage();
            int armorPenetration = weaponBlueprint.getArmorPenetration();
            int accuracy = calculateFinalValueFor(weaponBlueprint.getAccuracy(), HullStatType.ACCURACY, false);
            int timeCost = weaponBlueprint.getTimeCost();
            int weaponInitiative = startingInitiative + weaponBlueprint.getInitiativeBonus();
            DamageType damageType = weaponBlueprint.getDmgType();

            String name = weaponBlueprint.getName();

            Map<WeaponSecondaryEffect, ValueDurationPair> secondaryEffects = weaponBlueprint.getSecondaryEffects();

            WeaponInstance weaponInstance = new WeaponInstance(this, name, weaponInitiative, timeCost, damage, accuracy, armorPenetration, damageType,
                    secondaryEffects);

            weaponInstances.add(weaponInstance);
        }
        return weaponInstances;
    }

    private CombatActor getShieldInstance(int startingInitiative) {
        int shieldStrength = calculateFinalValueFor(blueprint.getMaxShieldStrength(), HullStatType.SHIELD_HP, true);
        int shieldRegPerTick = calculateFinalValueFor(blueprint.getShieldRegenerationAmount(), HullStatType.SHIELD_REGEN, true);
        int shieldBreakDuration = calculateFinalValueFor(blueprint.getShieldBreakDuration(), HullStatType.SHIELD_BREAK_DURATION, false);

        if (shieldStrength > 0) {
            double breakCount = 0;
            for (ComponentBlueprint component : blueprint.getComponents()) {
                breakCount += component.getFlatBonusFor(HullStatType.SHIELD_BREAK_DURATION) != null ? 1 : 0;
            }
            shieldBreakDuration = (int) Math.ceil(shieldBreakDuration / breakCount);
        }

        shieldInstance = new ShieldInstance(this, shieldStrength, shieldRegPerTick, shieldBreakDuration, startingInitiative);
        shieldInstance.addBattleLoger(logger);
        return shieldInstance;
    }

    private List<CombatActor> getEwarInstances(int startingInitiative) {
        // TODO
        return new LinkedList<CombatActor>();
    }

    @Override
    public List<CombatActor> getCombatActors() {
        int startingInitiative = calculateFinalValueFor(blueprint.getStartBattleSpeed(), HullStatType.INITIATIVE, false)
                + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);

        if (combatActorsOfShip == null) {
            combatActorsOfShip = new LinkedList<CombatActor>();
            for (CombatActor weaponInstance : getWeaponInstances(startingInitiative)) {
                combatActorsOfShip.add(weaponInstance);
            }
            combatActorsOfShip.add(getShieldInstance(startingInitiative));
            combatActorsOfShip.addAll(getEwarInstances(startingInitiative));
        }
        return combatActorsOfShip;
    }

    public ShipInstance getMothership() {
        return mothership;
    }

    public LinkedList<ShipInstance> getFightersInBay() {
        Map<Blueprint, Integer> fighterTypesInBay = blueprint.getFighterTypesInBay();
        LinkedList<ShipInstance> fighters = new LinkedList<ShipInstance>();
        for (Map.Entry<Blueprint, Integer> fighterType : fighterTypesInBay.entrySet()) {
            for (int i = 0; i < fighterType.getValue(); i++) {
                ShipInstance fighterInstance = new ShipInstance(idOfOwningEmpire, this, fighterType.getKey());
                fighters.add(fighterInstance);
            }
        }
        return fighters;
    }

    @Override
    public final boolean isDestroyed() {
        return currentHullStrength <= 0;
    }

    // Range in which misses are possible = max chance to miss * total hit range
    private static final int maxRangeToMiss = (int) (BattleConstants.accuracyRandomizerMaximum * (1 - BattleConstants.minChanceToHit));

    @Override
    public final void receiveAttack(Shot shot) {
        final int evasion = this.evasion.getCalculatedValue();

        // eva - (acc + accuracyRandomizerMaximum * (1 - minChanceToHit)) =
        // addedAcc
        // eva 150, acc 0 --> 150 - ( 0 + 95) = 55 --> acc 0 + 55 + random VS
        // eva 150
        // eva 120, acc 10 --> 120 - ( 10 + 95) = 15 --> acc 10 + 15 + random VS
        // eva 120
        // eva 80, acc 50 --> 80 - ( 50 + 95) = -65 --> acc 50 + random VS eva
        // 80
        // eva 40, acc 80 --> 40 - ( 80 + 95) = -135 --> acc 80 + random VS eva
        // 40
        final int addedAccuracy = Math.max(evasion - shot.accuracy - maxRangeToMiss, 0);
        final int totalAccuracy = shot.accuracy + addedAccuracy + BattleConstants.randomizer.nextInt(BattleConstants.accuracyRandomizerMaximum + 1);

        if (totalAccuracy > evasion) {
            logger.evades(this, false, totalAccuracy, evasion, addedAccuracy);
            double remainingDamage = shieldInstance.takeShieldDamage(shot);
            if (remainingDamage > 0) {
                takeHullDamage(remainingDamage, shot.armorPenetration, shot.damageType, shot.secondaryEffects);
            }
            if (!isDestroyed()) {
                checkAndApplyTickloss(shot.secondaryEffects.get(WeaponSecondaryEffect.TICKLOSS));
            }
        } else {
            logger.evades(this, true, totalAccuracy, evasion, addedAccuracy);
        }
    }

    private final double takeHullDamage(double remainingDamage, int armorPenetration, DamageType damageType,
            Map<WeaponSecondaryEffect, ValueDurationPair> secondaryEffects) {
        int hitStrength = (int) (BattleConstants.randomizer.nextFloat() * BattleConstants.penetrationRandomizerMaximum) + armorPenetration;
        final HullStatType resistanceType = BattleConstants.damageTypeToResistanceType.get(damageType);
        hitStrength -= hullStats.get(resistanceType).getCalculatedValue();

        if (hitStrength > glanceThreshold.getCalculatedValue()) {
            if (hitStrength > hitThreshold.getCalculatedValue()) {
                if (hitStrength > critThreshold.getCalculatedValue()) {
                    remainingDamage *= BattleConstants.critMultiplier;
                    double explodeChance = remainingDamage < currentHullStrength
                            ? ((double) (remainingDamage - containment.getCalculatedValue()) / (double) currentHullStrength)
                                    * BattleConstants.maxChanceExplodeOnCrit
                            : -1;
                    double rand = BattleConstants.randomizer.nextDouble();
                    if (explodeChance > rand) {
                        remainingDamage = currentHullStrength;
                        logger.explodes(this, explodeChance, hitStrength, critThreshold.getCalculatedValue(), containment.getCalculatedValue());
                    } else {
                        logger.takesHullDamage(this, remainingDamage, HullDamageLevel.CRIT, hitStrength, critThreshold.getCalculatedValue());
                    }
                } else {
                    remainingDamage *= BattleConstants.hitMultiplier;
                    logger.takesHullDamage(this, remainingDamage, HullDamageLevel.HIT, hitStrength, hitThreshold.getCalculatedValue());
                }

                checkAndApplyDegrade(secondaryEffects.get(WeaponSecondaryEffect.DEGRADE));
            } else {
                remainingDamage *= BattleConstants.glanceMultiplier;
                logger.takesHullDamage(this, remainingDamage, HullDamageLevel.GLANCING, hitStrength, glanceThreshold.getCalculatedValue());
            }
            currentHullStrength -= remainingDamage;
            return remainingDamage;

        } else {
            logger.armorDeflectsAllDamage(this, hitStrength, glanceThreshold.getCalculatedValue());
        }
        return 0;
    }

    private void checkAndApplyTickloss(ValueDurationPair ticklossEffect) {
        if (ticklossEffect != null) {
            int strength = ticklossEffect.getValue();
            int chanceInPermill = (strength * 1000) / (epm.getCalculatedValue() + strength);
            if (chanceInPermill > BattleConstants.randomizer.nextInt(1000)) {
                int lostTicks = ticklossEffect.getDuration();
                logger.shipLosesTicks(this, lostTicks, chanceInPermill);
                for (CombatActor actor : combatActorsOfShip) {
                    actor.loseInitiative(lostTicks);
                }
            }
        }
    }

    private void checkAndApplyDegrade(ValueDurationPair degradeEffect) {
        if (degradeEffect != null) {
            String key = WeaponSecondaryEffect.DEGRADE.toString();
            int val = degradeEffect.getValue();
            glanceThreshold.addFlatBonus(key, val);
            hitThreshold.addFlatBonus(key, val);
            critThreshold.addFlatBonus(key, val);
            logger.shipArmorDegrades(this, val);
        }
    }

    public boolean reactBeforeAttacker(ShipInstance attacker) {
        // TODO: Common solution dependent on setups. E.g. for ECM or point
        // defense.
        // Other active defense mechanisms that need to be added elsewhere?
        int rand = BattleConstants.randomizer.nextInt(BattleConstants.cloakingRandomizerMaximum);
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void addBattleLog(BattleLogger logger) {
        this.logger = logger;
    }

    public void removeBattleLogger() {
        this.logger = null;
    }

    public String getName() {
        return blueprint.getName();
    }

    public String getDescription() {
        return blueprint.getDescription();
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    @Override
    final public String toString() {
        return blueprint.getName() + " (" + idOfOwningEmpire + ")";
    }

    @Override
    public void endCurrentBattle() {
        // TODO: Anything to do here?
    }
}
