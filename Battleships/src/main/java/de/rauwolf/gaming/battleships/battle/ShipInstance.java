package de.rauwolf.gaming.battleships.battle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.rauwolf.gaming.battleships.logging.battleLoggers.BattleLogger;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.components.ComponentBlueprint;
import de.rauwolf.gaming.battleships.ships.shipHulls.DamageType;
import de.rauwolf.gaming.battleships.ships.stats.MutableStat;
import de.rauwolf.gaming.battleships.ships.stats.StatType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponBlueprint;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public class ShipInstance extends CombatTarget {
    private static final BattleLogger        logger = BattleLogger.getInstance();

    private final int                        idOfOwningEmpire;
    protected final ShipBlueprint            shipBlueprint;
    private final ShipInstance               mothership;

    private LinkedList<CombatActor>          combatActorsOfShip;
    private LinkedList<CombatActor>          weaponInstances;
    private ShieldInstance                   shieldInstance;

    private String                           instanceId;

    // Holding the values as MutableBaseStats again allows e.g. for temporary
    // armor lowering effects and the like.
    private final Map<StatType, MutableStat> hullStats;

    public ShipInstance(int idOfOwningEmpire, ShipInstance mothership, ShipBlueprint blueprint, String instanceId) {
        super();
        this.idOfOwningEmpire = idOfOwningEmpire;
        this.shipBlueprint = blueprint;
        this.mothership = mothership;
        this.instanceId = idOfOwningEmpire + "-" + instanceId;

        this.hullStats = new HashMap<StatType, MutableStat>();
        for (StatType statType : StatType.values()) {
            hullStats.put(statType, new MutableStat(shipBlueprint.calculateFinalValueFor(statType)));
        }
    }

    public final int getIdOfOwningEmpire() {
        return idOfOwningEmpire;
    }

    private List<CombatActor> getWeaponInstances() {
        final int startingInitiative = getHullStat(StatType.INITIATIVE) + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizer);
        if (weaponInstances == null) {
            weaponInstances = new LinkedList<CombatActor>();

            for (WeaponBlueprint weaponBlueprint : shipBlueprint.getWeapons()) {
                int damage = weaponBlueprint.getStatFor(StatType.DAMAGE);
                int armorPenetration = weaponBlueprint.getStatFor(StatType.AP);
                int accuracy = weaponBlueprint.calculateFinalValueFor(StatType.ACCURACY);
                int timeCost = weaponBlueprint.calculateFinalValueFor(StatType.TIMECOST);
                int weaponInitiative = weaponBlueprint.calculateFinalValueFor(StatType.INITIATIVE) + startingInitiative;
                int preferredTargetProbability = weaponBlueprint.calculateFinalValueFor(StatType.PREFERRED_TARGET_PROBABILITY)
                        + getHullStat(StatType.MANEUVERABILITY);

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

    private List<CombatActor> getEwarInstances() {
        // TODO
        return new LinkedList<CombatActor>();
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

    public String getDescription() {
        return shipBlueprint.getDescription();
    }

    public ShipBlueprint getBlueprint() {
        return shipBlueprint;
    }

    public SizeClass getSizeClass() {
        return shipBlueprint.getHullType().getHullSize();
    }

    @Override
    public int getHullStat(StatType statType) {
        return hullStats.get(statType).getCalculatedValue();
    }

    @Override
    public void addFlatBonusFor(StatType statType, String key, int value) {
        hullStats.get(statType).addFlatBonus(key, value);
    }

    @Override
    public void addFactorFor(StatType statType, String key, double value) {
        hullStats.get(statType).addFactor(key, value);
    }

    @Override
    public void removeFlatBonusFor(StatType statType, String key) {
        hullStats.get(statType).removeFlatBonus(key);
    }

    @Override
    public void removeFactorFor(StatType statType, String key) {
        hullStats.get(statType).removeFactor(key);
    }

    @Override
    public String getName() {
        return shipBlueprint.getName();
    }

    @Override
    public ShieldInstance getShieldInstance() {
        if (shieldInstance == null) {
            int shieldStrength = shipBlueprint.calculateFinalValueFor(StatType.SHIELD_HP);
            int shieldRegPerTick = shipBlueprint.calculateFinalValueFor(StatType.SHIELD_REGEN);
            int shieldBreakDuration = shipBlueprint.calculateFinalValueFor(StatType.SHIELD_BREAK_DURATION);

            if (shieldStrength > 0) {
                double breakCount = 0;
                for (ComponentBlueprint component : shipBlueprint.getComponents()) {
                    breakCount += component.getFlatBonusFor(StatType.SHIELD_BREAK_DURATION) != null ? 1 : 0;
                }
                shieldBreakDuration = (int) Math.ceil(shieldBreakDuration / breakCount);
            }

            shieldInstance = new ShieldInstance(this, shieldStrength, shieldRegPerTick, shieldBreakDuration);
        }
        return shieldInstance;
    }

    @Override
    protected void applyTickloss(Shot shot) {
        super.applyTickloss(shot);
        List<Integer> ticklossEffect = shot.secondaryEffects.get(WeaponSecondaryEffect.TICKLOSS);
        if (ticklossEffect != null) {
            int strength = ticklossEffect.get(0);
            int chanceInPermill = (strength * 1000) / (getHullStat(StatType.EPM) + strength);
            if (chanceInPermill > BattleConstants.randomizer.nextInt(1000)) {
                int lostTicks = ticklossEffect.get(1);
                logger.targetLosesTicks(this, lostTicks, chanceInPermill);
                for (CombatActor actor : getCombatActors()) {
                    actor.rememberLostTicks(lostTicks);
                }
            }
        }
    }

    @Override
    final public String toString() {
        return shipBlueprint.getName() + " " + instanceId + " ";
    }
}
