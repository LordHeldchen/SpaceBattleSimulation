package de.rauwolf.gaming.battleships.ships.stats;

import java.util.HashMap;
import java.util.Map;

public class MutableStat {
    private final int            baseValue;
    private Map<String, Integer> flatBonusList = new HashMap<String, Integer>();
    private Map<String, Double>  factorList    = new HashMap<String, Double>();
    private int                  calculatedValue;

    public MutableStat(int baseValue) {
        this.baseValue = baseValue;
        this.calculatedValue = baseValue;
    }

    public void addFlatBonus(String id, int bonus) {
        if (flatBonusList.containsKey(id)) {
            flatBonusList.put(id, flatBonusList.get(id) + bonus);
        } else {
            flatBonusList.put(id, bonus);
        }
        calculate();
    }

    public void removeFlatBonus(String id) {
        flatBonusList.remove(id);
        calculate();
    }

    public void addFactor(String id, double factor) {
        if (factorList.containsKey(id)) {
            factorList.put(id, factorList.get(id) + factor);
        } else {
            factorList.put(id, factor);
        }
        calculate();
        calculate();
    }

    public void removeFactor(String id) {
        factorList.remove(id);
        calculate();
    }

    private void calculate() {
        calculatedValue = baseValue;
        for (Integer value : flatBonusList.values()) {
            calculatedValue += value;
        }
        for (Double factor : factorList.values()) {
            calculatedValue *= factor;
        }
    }

    public int getCalculatedValue() {
        return calculatedValue;
    }
}
