package ships.resourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ships.blueprints.SizeClass;
import ships.shipHulls.DamageType;
import ships.weapons.WeaponBlueprint;
import ships.weapons.WeaponSecondaryEffect;

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
                SizeClass size = SizeClass.valueOf(elements[6].trim());
                int iniBonus = new Integer(elements[7].trim());
                int value = new Integer(elements[8].trim());
                DamageType dmgType = DamageType.valueOf(elements[9].trim());

                List<SizeClass> preferredTargetSizes = new LinkedList<SizeClass>();
                String[] preferredTargets = elements[10].trim().split(",");
                for (String preferredTarget : preferredTargets) {
                    if (preferredTarget.equals("")) {
                        continue;
                    }
                    preferredTargetSizes.add(SizeClass.valueOf(preferredTarget.trim()));
                }

                Map<WeaponSecondaryEffect, List<Integer>> weaponEffects = new HashMap<WeaponSecondaryEffect, List<Integer>>();
                if (elements.length > 11) {
                    String[] secondaryEffects = elements[11].trim().split(",");
                    for (String effect : secondaryEffects) {
                        String[] split = effect.split(":");
                        if (split.length == 1) {
                            continue;
                        }
                        WeaponSecondaryEffect type = WeaponSecondaryEffect.getEnumFromString(split[0].trim());
                        Integer amount = Integer.valueOf(split[1].replaceAll("%", "").trim());
                        List<Integer> values = new LinkedList<Integer>();
                        values.add(amount);
                        if (split.length > 2) {
                            Integer duration = Integer.valueOf(split[2].trim());
                            values.add(duration);
                        }
                        weaponEffects.put(type, values);
                    }
                }
                standardWeaponBlueprints.put(shorthand, new WeaponBlueprint(shorthand, name, size, accuracy, damage, armorPenetration, timeCost, iniBonus,
                        value, dmgType, preferredTargetSizes, weaponEffects));
            }
        }

        return standardWeaponBlueprints;
    }

    public static WeaponBlueprint getStandardWeaponBlueprint(String weaponShorthand) throws NumberFormatException, IOException, InstantiationException {
        return getStandardWeaponBlueprints().get(weaponShorthand);
    }
}
