package ships.resourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import battle.WeaponSecondaryEffect;
import ships.blueprints.WeaponBlueprint;
import ships.shipHulls.ValueDurationPair;
import ships.shipHulls.WeaponSize;

public class WeaponResourceLoader {
    private static Map<String, WeaponBlueprint> standardWeaponBlueprints;

    public static Map<String, WeaponBlueprint> getStandardWeaponBlueprints() throws NumberFormatException, IOException, InstantiationException {
        if (standardWeaponBlueprints == null) {
            InputStream weaponBlueprintsResource = WeaponResourceLoader.class.getClassLoader()
                    .getResourceAsStream("main/resources/standardWeaponBlueprints.csv");
            standardWeaponBlueprints = new HashMap<String, WeaponBlueprint>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(weaponBlueprintsResource));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().equals("")) {
                    continue;
                }
                String[] elements = line.split(";");
                String shorthand = elements[0].trim();
                String name = elements[1].trim();
                int accuracy = new Integer(elements[2].trim());
                int damage = new Integer(elements[3].trim());
                int armorPenetration = new Integer(elements[4].trim());
                int timeCost = new Integer(elements[5].trim());
                WeaponSize size = WeaponSize.valueOf(elements[6].trim());
                int iniBonus = new Integer(elements[7].trim());
                int value = new Integer(elements[8].trim());

                String[] secondaryEffects = elements[9].trim().split(",");
                Map<WeaponSecondaryEffect, ValueDurationPair> weaponEffects = new HashMap<WeaponSecondaryEffect, ValueDurationPair>(); 
                for (String effect: secondaryEffects) {
                    String[] split = effect.split(":");
                    if (split.length == 1) {
                        continue;
                    }
                    WeaponSecondaryEffect type = WeaponSecondaryEffect.getEnumFromString(split[0].trim());
                    Integer amount = Integer.valueOf(split[1].replaceAll("%", "").trim());
                    if (split.length > 2) {
                        Integer duration = Integer.valueOf(split[2].trim());
                        weaponEffects.put(type, new ValueDurationPair(amount, duration));
                    } else {
                        weaponEffects.put(type, new ValueDurationPair(amount, 0));
                    }
                }
                standardWeaponBlueprints.put(shorthand, new WeaponBlueprint(name, size, accuracy, damage, armorPenetration, timeCost, iniBonus, value, weaponEffects));
            }
        }

        return standardWeaponBlueprints;
    }

    public static WeaponBlueprint getStandardWeaponBlueprint(String weaponShorthand) throws NumberFormatException, IOException, InstantiationException {
        return getStandardWeaponBlueprints().get(weaponShorthand);
    }
}
