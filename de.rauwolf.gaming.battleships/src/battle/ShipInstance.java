package battle;

import java.util.LinkedList;
import java.util.List;

import logging.battleLogger.BattleLogger;
import logging.battleLogger.HullDamageType;
import ships.blueprints.Blueprint;
import ships.blueprints.ComponentBlueprint;
import ships.blueprints.MutableBaseStat;
import ships.blueprints.WeaponBlueprint;

public class ShipInstance implements CombatTarget {
    protected final Blueprint blueprint;

    private ShieldInstance shieldInstance;

    private final MutableBaseStat maxHullStrength;
    private int currentHullStrength;

    // Holding the threshold as MutableBaseStats again allows e.g. for temporary
    // armor lowering effects and the like.
    private final MutableBaseStat glanceThreshold;
    private final MutableBaseStat hitThreshold;
    private final MutableBaseStat critThreshold;
    private final MutableBaseStat evasion;

    private final ShipInstance mothership;

    private final int idOfOwningEmpire;
    private BattleLogger logger;

    private LinkedList<CombatActor> combatActorsOfShip;


    private MutableBaseStat retrieveFinalValueFor(int baseValue, String value) {
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
        MutableBaseStat mutableValue = new MutableBaseStat(baseValue);
        for (Integer flatModifier : flatModifiers) {
            mutableValue.addFlatBonus("", flatModifier);
        }
        for (Double factor : factorModifiers) {
            mutableValue.addFactor("", factor);
        }
        return mutableValue;
    }

    public ShipInstance(int idOfOwningEmpire, ShipInstance mothership, Blueprint blueprint) {
        this.idOfOwningEmpire = idOfOwningEmpire;
        this.blueprint = blueprint;
        this.mothership = mothership;

        this.glanceThreshold = retrieveFinalValueFor(blueprint.getArmorGlanceThreshold(), "glan");
        this.hitThreshold = retrieveFinalValueFor(blueprint.getArmorHitThreshold(), "hit");
        this.critThreshold = retrieveFinalValueFor(blueprint.getArmorCritThreshold(), "crit");
        this.evasion = retrieveFinalValueFor(blueprint.getEvasion(), "eva");

        this.maxHullStrength = new MutableBaseStat(blueprint.getMaxHullStrength());
        this.currentHullStrength = this.maxHullStrength.getCalculatedValue();
    }

    public final int getIdOfOwningEmpire() {
        return idOfOwningEmpire;
    }

    private List<WeaponInstance> getWeaponInstances(int startingInitiative) {
        List<WeaponInstance> weaponInstances = new LinkedList<WeaponInstance>();

        for (WeaponBlueprint weaponBlueprint : blueprint.getWeapons()) {
            MutableBaseStat damage = new MutableBaseStat(weaponBlueprint.getDamage());
            MutableBaseStat armorPenetration = new MutableBaseStat(weaponBlueprint.getArmorPenetration());

            MutableBaseStat accuracy = retrieveFinalValueFor(weaponBlueprint.getAccuracy(), "acc");

            MutableBaseStat timeCost = new MutableBaseStat(weaponBlueprint.getTimeCost());

            String name = weaponBlueprint.getName();

            WeaponInstance weaponInstance = new WeaponInstance(this, name, startingInitiative, timeCost, damage, accuracy, armorPenetration);

            weaponInstances.add(weaponInstance);
        }
        return weaponInstances;
    }

    @Override
    public List<CombatActor> getCombatActors() {
        int startingInitiative = retrieveFinalValueFor(blueprint.getStartBattleSpeed(), "ini").getCalculatedValue()
                + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);

        if (combatActorsOfShip == null) {
            combatActorsOfShip = new LinkedList<CombatActor>();
            for (WeaponInstance weaponInstance : getWeaponInstances(startingInitiative)) {
                combatActorsOfShip.add(weaponInstance);
            }

            shieldInstance = new ShieldInstance(blueprint.getMaxShieldStrength(), blueprint.getShieldRegenerationAmount(), startingInitiative,
                    blueprint.getShieldInitiativeDecay());
            shieldInstance.addBattleLoger(logger);
        }
        return combatActorsOfShip;
    }

    public ShipInstance getMothership() {
        return mothership;
    }

    public LinkedList<ShipInstance> getFightersInBay() {
        List<? extends Blueprint> fighterTypesInBay = blueprint.getFighterTypesInBay();
        LinkedList<ShipInstance> fighters = new LinkedList<ShipInstance>();
        for (Blueprint fighterType : fighterTypesInBay) {
            ShipInstance fighter = new ShipInstance(idOfOwningEmpire, this, fighterType);
            fighters.add(fighter);
        }
        return fighters;
    }

    @Override
    public final boolean isDestroyed() {
        return currentHullStrength <= 0;
    }

    @Override
    public final void takeDamage(Shot shot) {
        final int evasion = this.evasion.getCalculatedValue();

        // TODO Add a minimum/maximum hit chance (<0,1 and >0,9 perhaps?)
        final int totalAccuracy = shot.accuracy + BattleConstants.randomizer.nextInt(BattleConstants.accuracyRandomizerMaximum);
        if (totalAccuracy > evasion) {
            logger.evades(this, false, totalAccuracy, evasion);
            int remainingDamage = shieldInstance.takeShieldDamage(shot.amount, shot.armorPenetration);
            takeHullDamage(remainingDamage, shot.armorPenetration);
        } else {
            logger.evades(this, true, totalAccuracy, evasion);
        }
    }

    private final int takeHullDamage(int damage, int armorPenetration) {
        int hitStrength = (int) (BattleConstants.randomizer.nextFloat() * BattleConstants.penetrationRandomizerMaximum) + armorPenetration;

        if (hitStrength > glanceThreshold.getCalculatedValue()) {
            if (hitStrength > hitThreshold.getCalculatedValue()) {
                if (hitStrength > critThreshold.getCalculatedValue()) {
                    damage *= BattleConstants.critMultiplier;
                    logger.takesHullDamage(this, damage, HullDamageType.CRIT, hitStrength, critThreshold.getCalculatedValue());
                } else {
                    damage *= BattleConstants.hitMultiplier;
                    logger.takesHullDamage(this, damage, HullDamageType.HIT, hitStrength, hitThreshold.getCalculatedValue());
                }
            } else {
                damage *= BattleConstants.glanceMultiplier;
                logger.takesHullDamage(this, damage, HullDamageType.GLANCING, hitStrength, glanceThreshold.getCalculatedValue());
            }
            currentHullStrength -= damage;
            return damage;

        } else {
            logger.armorDeflectsAllDamage(this, hitStrength, glanceThreshold.getCalculatedValue());
        }
        return 0;
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
