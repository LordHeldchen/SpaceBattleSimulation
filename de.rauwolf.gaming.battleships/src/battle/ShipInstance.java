package battle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import battle.CombatActions.ApplyTicklossAction;
import battle.CombatActions.CheckEvasionAction;
import battle.CombatActions.CheckExplodeAction;
import battle.CombatActions.DegradeArmorAction;
import battle.CombatActions.DetermineHullDamageLevelAction;
import battle.CombatActions.CombatAction;
import battle.CombatActions.TakeDamageAction;
import battle.CombatActions.TakeShieldDamageAction;
import ships.blueprints.Blueprint;
import ships.components.ComponentBlueprint;
import ships.hulls.HullSize;
import ships.shipHulls.DamageType;
import ships.stats.HullStatType;
import ships.stats.MutableBaseStat;
import ships.weapons.WeaponBlueprint;
import ships.weapons.WeaponSecondaryEffect;

public class ShipInstance implements CombatTarget {
    protected final Blueprint                        blueprint;

    private ShieldInstance                           shieldInstance;

    private final MutableBaseStat                    maxHullStrength;
    private double                                   currentHullStrength;

    // Holding the threshold as MutableBaseStats again allows e.g. for temporary
    // armor lowering effects and the like.
    // TODO: Werte hier auf Map von "BaseHullStat" umstellen, machts vsl. übersichtlicher.
    // Sollte auch init per loop ermöglichen.
    private final MutableBaseStat                    glanceThreshold;
    private final MutableBaseStat                    hitThreshold;
    private final MutableBaseStat                    critThreshold;
    private final MutableBaseStat                    containment;
    private final MutableBaseStat                    epm;
    private final MutableBaseStat                    evasion;

    private final ShipInstance                       mothership;

    private final int                                idOfOwningEmpire;

    private LinkedList<CombatActor>                  combatActorsOfShip;

    // Derzeit nur Widerstände
    private final Map<HullStatType, MutableBaseStat> hullStats;

    private final int                                startingInitiative;

    private LinkedList<CombatActor> weaponInstances;

    private int lostTicks = 0;

    private static final List<CombatAction> actionList;

    static {
        actionList = new LinkedList<CombatAction>();
        actionList.add(new CheckEvasionAction());
        actionList.add(new ApplyTicklossAction());
        actionList.add(new TakeShieldDamageAction());
        actionList.add(new DetermineHullDamageLevelAction());
        actionList.add(new CheckExplodeAction());
        actionList.add(new TakeDamageAction());
        actionList.add(new DegradeArmorAction());
    }

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
        this.setCurrentHullStrength(this.maxHullStrength.getCalculatedValue());
        
        this.startingInitiative = calculateFinalValueFor(blueprint.getStartBattleSpeed(), HullStatType.INITIATIVE, false)
                + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);
    }

    public final int getIdOfOwningEmpire() {
        return idOfOwningEmpire;
    }

    private List<CombatActor> getWeaponInstances() {
        if (weaponInstances == null) {
            weaponInstances = new LinkedList<CombatActor>();

            for (WeaponBlueprint weaponBlueprint : blueprint.getWeapons()) {
                int damage = weaponBlueprint.getDamage();
                int armorPenetration = weaponBlueprint.getArmorPenetration();
                int accuracy = calculateFinalValueFor(weaponBlueprint.getAccuracy(), HullStatType.ACCURACY, false);
                int timeCost = weaponBlueprint.getTimeCost();
                int weaponInitiative = startingInitiative + weaponBlueprint.getInitiativeBonus();
                DamageType damageType = weaponBlueprint.getDmgType();

                String name = weaponBlueprint.getName();
                List<HullSize> preferredTargets = weaponBlueprint.getPreferredTargets();

                Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects = weaponBlueprint.getSecondaryEffects();

                WeaponInstance weaponInstance = new WeaponInstance(this, name, weaponInitiative, timeCost, damage, accuracy, armorPenetration, damageType,
                        preferredTargets, secondaryEffects);

                weaponInstances.add(weaponInstance);
            }
        }
        return weaponInstances;
    }

    public ShieldInstance getShieldInstance() {
        if (shieldInstance == null) {
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
        return getCurrentHullStrength() <= 0;
    }

    @Override
    public final void receiveAttack(Shot shot) {
        for (CombatAction action: actionList) {
            if (!action.execute(this, shot)) {
                break;
            }
        }
    }

    public boolean reactBeforeAttacker(ShipInstance attacker) {
        // TODO: Common solution dependent on setups. E.g. for ECM or point defense.
        // Other active defense mechanisms that need to be added elsewhere? Active defense of relevance anyways?
        int rand = BattleConstants.randomizer.nextInt(BattleConstants.cloakingRandomizerMaximum);
        return false;
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
    
    public HullSize getSizeClass() {
        return blueprint.getHullType().getHullSize();
    }

    public MutableBaseStat getContainment() {
        return containment;
    }
    
    public MutableBaseStat getThresholdFor(HullDamageLevel damageLevel) {
        switch (damageLevel) {
            case CRIT:
                return critThreshold;
            case HIT:
                return hitThreshold;
            case GLANCE:
                return glanceThreshold;
            case DEFLECT:
                return glanceThreshold;
            default:
                break;
        }
        return null;
    }

    public double getCurrentHullStrength() {
        return currentHullStrength;
    }

    public void setCurrentHullStrength(double currentHullStrength) {
        this.currentHullStrength = currentHullStrength;
    }

    public MutableBaseStat getEvasion() {
        return this.evasion;
    }

    public Map<HullStatType, MutableBaseStat> getHullStats() {
        return hullStats;
    }

    public MutableBaseStat getEpm() {
        return epm;
    }

    @Override
    final public String toString() {
        return blueprint.getName() + " (" + idOfOwningEmpire + ")";
    }

    @Override
    public void endCurrentBattle() {
        // TODO: Anything to do here?
    }

    public void addLostTicks(int lostTicks) {
        for (CombatActor actor : getCombatActors()) {
            actor.rememberLostTicks(lostTicks);
        }
    }
}
