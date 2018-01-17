package main.java.characters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.java.ships.blueprints.SizeClass;
import main.java.ships.stats.StatType;

public class Admiral extends Character {
    private Map<SizeClass, Map<StatType, Integer>> statBoniFlat;

    public Admiral(String name) {
        super(name);
        statBoniFlat = new HashMap<SizeClass, Map<StatType, Integer>>();
        for (SizeClass size : SizeClass.values()) {
            statBoniFlat.put(size, new HashMap<StatType, Integer>());
        }
    }

    public Integer getFlatBonusAmountOfTypeForSize(SizeClass size, StatType type) {
        if (statBoniFlat.containsKey(size) && statBoniFlat.get(size).containsKey(type)) {
            return statBoniFlat.get(size).get(type);
        } else {
            return 0;
        }
    }

    public Map<SizeClass, Map<StatType, Integer>> getAllFlatBoni() {
        return statBoniFlat;
    }

    public void setFlatBonusAmountOfTypeForSize(SizeClass size, StatType type, Integer value) {
        statBoniFlat.get(size).put(type, value);
    }

    @Override
    public String toString() {
        String ret = "Admiral Boni of " + getName() + ":\n";
        for (Entry<SizeClass, Map<StatType, Integer>> admiralBonus : getAllFlatBoni().entrySet()) {
            if (admiralBonus.getValue().size() > 0) {
                ret += "# For ships of size " + admiralBonus.getKey() + "\n";
                for (Entry<StatType, Integer> admiralSizeBonus : admiralBonus.getValue().entrySet()) {
                    ret += "  # " + admiralSizeBonus.getKey() + ": " + admiralSizeBonus.getValue() + "\n";
                }
            }
        }
        return ret;
    }
}
