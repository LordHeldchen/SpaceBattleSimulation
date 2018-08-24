package de.rauwolf.gaming.battleships.ships.blueprints;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.rauwolf.gaming.battleships.battle.BattleConstants;
import de.rauwolf.gaming.battleships.battle.ShipInstance;
import de.rauwolf.gaming.battleships.ships.components.ComponentBlueprint;
import de.rauwolf.gaming.battleships.ships.hulls.HullType;
import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponBlueprint;

public class ShipBlueprint implements BlueprintWithSubComponents, Comparable<ShipBlueprint> {
    private final HullType                                                      hullType;

    private final Map<StatType, Integer>                                        statMap;

    private final Map<SizeClass, List<SizeLimitedSlot<WeaponBlueprint>>>        weapons;
    private final Map<ComponentType, List<TypeLimitedSlot<ComponentBlueprint>>> components;
    private final Map<ShipBlueprint, Integer>                                   fightersInBay;

    private final String                                                        id;
    private final String                                                        name;
    private final String                                                        description;

    public ShipBlueprint(String shorthand, String name, String description, HullType hullType) {
        this.id = shorthand;
        this.name = name;
        this.description = description;
        this.hullType = hullType;
        this.statMap = new HashMap<StatType, Integer>();

        for (StatType stat : StatType.values()) {
            statMap.put(stat, hullType.getBaseStatFor(stat));
        }

        Map<SizeClass, List<SizeLimitedSlot<WeaponBlueprint>>> weaponsTmp = Maps.newHashMap();
        for (SizeClass size : SizeClass.values()) {
            LinkedList<SizeLimitedSlot<WeaponBlueprint>> weaponListForSizeTmp = Lists.newLinkedList();
            for (int i = 0; i < this.hullType.getAvailableWeaponSlotsForSize(size); i++) {
                weaponListForSizeTmp.add(new SizeLimitedSlot<WeaponBlueprint>(size));
            }
            weaponsTmp.put(size, Collections.unmodifiableList(weaponListForSizeTmp));
        }
        weapons = Collections.unmodifiableMap(weaponsTmp);

        HashMap<ComponentType, List<TypeLimitedSlot<ComponentBlueprint>>> componentsTmp = Maps.newHashMap();
        for (ComponentType type : ComponentType.values()) {
            LinkedList<TypeLimitedSlot<ComponentBlueprint>> componentListForTypeTmp = Lists.newLinkedList();
            for (int i = 0; i < this.hullType.getAvailableComponentSlotsForType(type); i++) {
                componentListForTypeTmp.add(new TypeLimitedSlot<ComponentBlueprint>(type));
            }
            componentsTmp.put(type, Collections.unmodifiableList(componentListForTypeTmp));
        }
        components = Collections.unmodifiableMap(componentsTmp);

        fightersInBay = new HashMap<ShipBlueprint, Integer>();
    }

    public HullType getHullType() {
        return hullType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getStatFor(StatType stat) {
        return statMap.get(stat);
    }

    public Map<ShipBlueprint, Integer> getFighterTypesInBay() {
        return fightersInBay;
    }

    public int getValueOfShipInstance() {
        int value = hullType.getValue();
        value += getWeapons().stream().collect(Collectors.summarizingInt(e -> e.getValue())).getSum();
        value += (getComponents().stream().collect(Collectors.summarizingInt(e -> e.getValue(hullType.getHullSize()))).getSum());
        return value;
    }

    public void addFightersToBay(ShipBlueprint fighter, int num) throws InstantiationException {
        int availableShipBaysForSize = hullType.getAvailableShipBaysForSize(fighter.getHullSize());
        for (ShipBlueprint fighterInBay : fightersInBay.keySet()) {
            if (fighterInBay.getHullSize().equals(fighter.getHullSize())) {
                availableShipBaysForSize -= fightersInBay.get(fighterInBay);
            }
        }
        if (num > availableShipBaysForSize) {
            throw new InstantiationException("Too many fighters specified for " + this);
        }
        fightersInBay.put(fighter, fightersInBay.keySet().contains(fighter) ? fightersInBay.get(fighter) + num : num);
    }

    private SizeClass getHullSize() {
        return this.hullType.getHullSize();
    }

    public ShipInstance createInstance(int owningEmpire, String instanceId) {
        return new ShipInstance(owningEmpire, null, this, instanceId);
    }

    public Map<SizeClass, List<SizeLimitedSlot<WeaponBlueprint>>> getWeaponSlots() {
        return this.weapons;
    }

    public List<SizeLimitedSlot<WeaponBlueprint>> getWeaponSlotsForSize(SizeClass size) {
        return this.weapons.get(size);
    }

    public List<WeaponBlueprint> getWeapons() {
        return this.weapons.values().stream().flatMap(List::stream).map(w -> w.getSlottedElement()).collect(Collectors.toList());
    }

    public Map<ComponentType, List<TypeLimitedSlot<ComponentBlueprint>>> getComponentSlots() {
        return this.components;
    }

    public List<TypeLimitedSlot<ComponentBlueprint>> getComponentSlotsForType(ComponentType type) {
        return this.components.get(type);
    }

    public List<ComponentBlueprint> getComponents() {
        return components.values().stream().flatMap(List::stream).map(c -> c.getSlottedElement()).filter(c -> c != null).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name + " (" + hullType + ")";
    }

    public SizeClass getSize() {
        return hullType.getHullSize();
    }

    @Override
    public boolean isDesignValid() {
        return true;
    }

    @Override
    public int compareTo(ShipBlueprint o) {
        int res = hullType.getHullSize().compareTo(o.hullType.getHullSize());
        if (res == 0) {
            res = hullType.getName().compareTo(o.hullType.getName());
        }
        return res;
    }

    public int calculateFinalValueFor(StatType statType) {
        int baseValue = getStatFor(statType);
        List<Integer> flatModifiers = new LinkedList<Integer>();
        List<Double> factorModifiers = new LinkedList<Double>();
        for (ComponentBlueprint component : getComponents()) {
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
            baseValue *= BattleConstants.shipSizeScaling.get(getSize());
        }
        return baseValue;
    }
}
