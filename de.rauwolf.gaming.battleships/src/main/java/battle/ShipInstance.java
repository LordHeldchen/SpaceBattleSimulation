package main.java.battle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.java.battle.CombatActions.ApplyTicklossAction;
import main.java.battle.CombatActions.CheckEvasionAction;
import main.java.battle.CombatActions.CheckExplodeAction;
import main.java.battle.CombatActions.CombatAction;
import main.java.battle.CombatActions.DegradeArmorAction;
import main.java.battle.CombatActions.DetermineHullDamageLevelAction;
import main.java.battle.CombatActions.TakeDamageAction;
import main.java.battle.CombatActions.TakeShieldDamageAction;
import main.java.ships.blueprints.Blueprint;
import main.java.ships.blueprints.ShipBlueprint;
import main.java.ships.blueprints.SizeClass;
import main.java.ships.components.ComponentBlueprint;
import main.java.ships.shipHulls.DamageType;
import main.java.ships.stats.MutableStat;
import main.java.ships.stats.StatType;
import main.java.ships.weapons.WeaponBlueprint;
import main.java.ships.weapons.WeaponSecondaryEffect;

public class ShipInstance implements CombatTarget {
    private final int                        idOfOwningEmpire;
    protected final ShipBlueprint            shipBlueprint;
    private final ShipInstance               mothership;

    // Holding the values as MutableBaseStats again allows e.g. for temporary
    // armor lowering effects and the like.
    private final Map<StatType, MutableStat> hullStats;
    private double                           currentHullStrength;

    private LinkedList<CombatActor>          combatActorsOfShip;
    private LinkedList<CombatActor>          weaponInstances;
    private ShieldInstance                   shieldInstance;

    private int                              startingInitiative;

    private static final List<CombatAction>  actionList;
    private String                           instanceId;

    static {
        actionList = new LinkedList<CombatAction>();
        actionList.add(new CheckEvasionAction());
        actionList.add(new TakeShieldDamageAction());
        actionList.add(new DetermineHullDamageLevelAction());
        actionList.add(new CheckExplodeAction());
        actionList.add(new TakeDamageAction());
        actionList.add(new ApplyTicklossAction());
        actionList.add(new DegradeArmorAction());
    }

    private int calculateFinalValueFor(StatType statType, Blueprint blueprint) {
        int baseValue = blueprint.getStatFor(statType);
        List<Integer> flatModifiers = new LinkedList<Integer>();
        List<Double> factorModifiers = new LinkedList<Double>();
        for (ComponentBlueprint component : blueprint.getComponents()) {
            Integer flatBonus = component.getFlatBonusFor(statType);
            if (flatBonus != null) {
                flatModifiers.add(flatBonus);
            }
            Double factor = component.getFactorFor(statType);
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
        if (statType.doesScaleWithSize()) {
            baseValue *= BattleConstants.shipSizeScaling.get(blueprint.getSize());
        }
        return baseValue;
    }

    public ShipInstance(int idOfOwningEmpire, ShipInstance mothership, ShipBlueprint blueprint, String instanceId) {
        this.idOfOwningEmpire = idOfOwningEmpire;
        this.shipBlueprint = blueprint;
        this.mothership = mothership;
        this.instanceId = instanceId;

        this.hullStats = new HashMap<StatType, MutableStat>();
        for (StatType hullStatType : StatType.values()) {
            hullStats.put(hullStatType, new MutableStat(calculateFinalValueFor(hullStatType, shipBlueprint)));
        }

        this.setCurrentHullStrength(hullStats.get(StatType.MAX_HULL_STRENGTH).getCalculatedValue());
        this.startingInitiative = hullStats.get(StatType.INITIATIVE).getCalculatedValue()
                + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizer);
    }

    public final int getIdOfOwningEmpire() {
        return idOfOwningEmpire;
    }

    private List<CombatActor> getWeaponInstances() {
        if (weaponInstances == null) {
            weaponInstances = new LinkedList<CombatActor>();

            for (WeaponBlueprint weaponBlueprint : shipBlueprint.getWeapons()) {
                int damage = weaponBlueprint.getStatFor(StatType.DAMAGE);
                int armorPenetration = weaponBlueprint.getStatFor(StatType.AP);
                int accuracy = calculateFinalValueFor(StatType.ACCURACY, weaponBlueprint);
                int timeCost = calculateFinalValueFor(StatType.TIMECOST, weaponBlueprint);
                int weaponInitiative = calculateFinalValueFor(StatType.INITIATIVE, weaponBlueprint) + startingInitiative;
                int preferredTargetProbability = calculateFinalValueFor(StatType.PREFERRED_TARGET_PROBABILITY, weaponBlueprint)
                        + hullStats.get(StatType.MANEUVERABILITY).getCalculatedValue();

                DamageType damageType = weaponBlueprint.getDmgType();

                String name = weaponBlueprint.getName();
                List<SizeClass> preferredTargets = weaponBlueprint.getPreferredTargets();

                Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects = weaponBlueprint.getSecondaryEffects();

                WeaponInstance weaponInstance = new WeaponInstance(this, name, weaponInitiative, timeCost, damage, accuracy, armorPenetration, damageType,
                        preferredTargets, preferredTargetProbability, secondaryEffects);

                weaponInstances.add(weaponInstance);
            }
        }
        return weaponInstances;
    }

    public ShieldInstance getShieldInstance() {
        if (shieldInstance == null) {
            int shieldStrength = calculateFinalValueFor(StatType.SHIELD_HP, shipBlueprint);
            int shieldRegPerTick = calculateFinalValueFor(StatType.SHIELD_REGEN, shipBlueprint);
            int shieldBreakDuration = calculateFinalValueFor(StatType.SHIELD_BREAK_DURATION, shipBlueprint);

            if (shieldStrength > 0) {
                double breakCount = 0;
                for (ComponentBlueprint component : shipBlueprint.getComponents()) {
                    breakCount += component.getFlatBonusFor(StatType.SHIELD_BREAK_DURATION) != null ? 1 : 0;
                }
                shieldBreakDuration = (int) Math.ceil(shieldBreakDuration / breakCount);
            }

            shieldInstance = new ShieldInstance(this, shieldStrength, shieldRegPerTick, shieldBreakDuration, startingInitiative);
        }
        return shieldInstance;
    }

    private List<CombatActor> getEwarInstances() {
        // TODO
        return new LinkedList<CombatActor>();
    }

    @Override
    public List<CombatActor> getCombatActors() {
        if (combatActorsOfShip == null) {
            combatActorsOfShip = new LinkedList<CombatActor>();
            for (CombatActor weaponInstance : getWeaponInstances()) {
                combatActorsOfShip.add(weaponInstance);
            }
            combatActorsOfShip.add(getShieldInstance());
            combatActorsOfShip.addAll(getEwarInstances());
        }
        return combatActorsOfShip;
    }

    public ShipInstance getMothership() {
        return mothership;
    }

    public LinkedList<ShipInstance> getFightersInBay() {
        int instanceNum = 0;

        Map<ShipBlueprint, Integer> fighterTypesInBay = shipBlueprint.getFighterTypesInBay();
        LinkedList<ShipInstance> fighters = new LinkedList<ShipInstance>();
        for (Map.Entry<ShipBlueprint, Integer> fighterType : fighterTypesInBay.entrySet()) {
            for (int i = 0; i < fighterType.getValue(); i++) {
                ShipInstance fighterInstance = new ShipInstance(idOfOwningEmpire, this, fighterType.getKey(), instanceId + "-" + instanceNum);
                fighters.add(fighterInstance);
            }
        }
        return fighters;
    }

    @Override
    public final boolean isDestroyed() {
        return getCurrentHullStrength() <= 0;
    }

    @Override
    public final void receiveAttack(Shot shot) {
        for (CombatAction action : actionList) {
            if (!action.execute(this, shot)) {
                break;
            }
        }
    }

    public boolean reactBeforeAttacker(ShipInstance attacker) {
        // TODO: Common solution dependent on setups. E.g. for ECM or point
        // defense.
        // Other active defense mechanisms that need to be added elsewhere?
        // Active defense of relevance anyways?
        int rand = BattleConstants.randomizer.nextInt(BattleConstants.CLOAKING_RANDOMIZER_MAXIMUM);
        return false;
    }

    public String getName() {
        return shipBlueprint.getName();
    }

    public String getDescription() {
        return shipBlueprint.getDescription();
    }

    public ShipBlueprint getBlueprint() {
        return shipBlueprint;
    }

    public SizeClass getSizeClass() {
        return shipBlueprint.getHullType().getHullSize();
    }

    public double getCurrentHullStrength() {
        return currentHullStrength;
    }

    public void setCurrentHullStrength(double currentHullStrength) {
        this.currentHullStrength = currentHullStrength;
    }

    public int getHullStat(StatType statType) {
        return hullStats.get(statType).getCalculatedValue();
    }

    @Override
    final public String toString() {
        return shipBlueprint.getName() + " " + instanceId + " (empire " + idOfOwningEmpire + ")";
    }

    @Override
    public void endCurrentBattle() {
        // TODO: Anything to do here?
    }

    @Override
    public void startCurrentBattle() {
    }

    public void addLostTicks(int lostTicks) {
        for (CombatActor actor : getCombatActors()) {
            actor.rememberLostTicks(lostTicks);
        }
    }

    public void addFlatBonusFor(StatType statType, String key, int value) {
        hullStats.get(statType).addFlatBonus(key, value);
    }

    public void addFactorFor(StatType statType, String key, double value) {
        hullStats.get(statType).addFactor(key, value);
    }

    public void removeFlatBonusFor(StatType statType, String key) {
        hullStats.get(statType).removeFlatBonus(key);
    }

    public void removeFactorFor(StatType statType, String key) {
        hullStats.get(statType).removeFactor(key);
    }
}
