package ships.resourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ships.blueprints.WeaponBlueprint;
import ships.shipHulls.WeaponSize;

public class WeaponResourceLoader {
    private static Map<String, WeaponBlueprint> standardWeaponBlueprints;

    public static Map<String, WeaponBlueprint> getStandardWeaponBlueprints() throws NumberFormatException, IOException {
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
                int accuracy = new Integer(elements[2].trim()).intValue();
                int damage = new Integer(elements[3].trim()).intValue();
                int armorPenetration = new Integer(elements[4].trim()).intValue();
                int timeCost = new Integer(elements[5].trim()).intValue();
                WeaponSize size = WeaponSize.valueOf(elements[6].trim());
                int iniBonus = new Integer(elements[7].trim()).intValue();
                standardWeaponBlueprints.put(shorthand, new WeaponBlueprint(name, size, accuracy, damage, armorPenetration, timeCost, iniBonus));
            }
        }

        return standardWeaponBlueprints;
    }

    public static WeaponBlueprint getStandardWeaponBlueprint(String weaponShorthand) throws NumberFormatException, IOException {
        return getStandardWeaponBlueprints().get(weaponShorthand);
    }
}
