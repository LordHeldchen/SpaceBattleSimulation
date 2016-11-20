package ships.blueprints;

import java.util.HashMap;
import java.util.Map;

public class MutableBaseStat {
    private final int            baseValue;
    private Map<String, Integer> flatBonusList = new HashMap<String, Integer>();
    private Map<String, Double>  factorList    = new HashMap<String, Double>();
    private int                  calculatedValue;

    public MutableBaseStat(int baseValue) {
        this.baseValue = baseValue;
        this.calculatedValue = baseValue;
    }

    public void addFlatBonus(String id, int bonus) {
        flatBonusList.put(id, bonus);
        calculate();
    }

    public void removeFlatBonus(String id) {
        flatBonusList.remove(id);
        calculate();
    }

    public void addFactor(String id, double factor) {
        factorList.put(id, factor);
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
