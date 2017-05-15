package ships.blueprints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ships.hulls.HullType;
import ships.hulls.HullTypeResourceLoader;

public class StandardConfigurationResourceLoader {
    private static Map<String, Blueprint> standardConfigurations;

    private static Map<String, Blueprint> getStandardConfigurations() throws InstantiationException, NotEnoughtSlotsException, IOException {
        if (standardConfigurations == null) {
            InputStream weaponBlueprintsResource = StandardConfigurationResourceLoader.class.getClassLoader()
                    .getResourceAsStream("main/resources/standardConfigurations.csv");
            standardConfigurations = new HashMap<String, Blueprint>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(weaponBlueprintsResource));

            Map<Blueprint, String[]> baysToFill = new HashMap<Blueprint, String[]>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().equals("")) {
                    continue;
                }
                // shorthand; name; hullType-shorthand;
                // x times weapon shorthand (separator ",");
                // x times component shorthand (separator ",");
                // x times ships in bay (separator ",", format
                // "hullType-shorthand: num"); description
                String[] elements = line.split(";");
                String shorthand = elements[0].trim();
                String name = elements[1].trim();
                String hullTypeShorthand = elements[2].trim();

                HullType hullType = HullTypeResourceLoader.getBasicHullType(hullTypeShorthand);

                String[] weaponShorthands = elements[3].split(",");
                String[] componentShorthands = elements[4].split(",");
                String[] shipsInBay = elements[5].split(",");

                String description = elements[6].trim();

                Blueprint blueprint = new Blueprint(name, description, hullType);
                baysToFill.put(blueprint, shipsInBay);

                for (String weaponShorthand : weaponShorthands) {
                    if (!weaponShorthand.trim().equals("")) {
                        blueprint.addStandardWeapon(weaponShorthand.trim());
                    }
                }
                for (String componentShorthand : componentShorthands) {
                    if (!componentShorthand.trim().equals("")) {
                        blueprint.addStandardComponent(componentShorthand.trim());
                    }
                }
                standardConfigurations.put(shorthand, blueprint);
            }

            for (Blueprint blueprint : baysToFill.keySet()) {
                for (String shipsInBay : baysToFill.get(blueprint)) {
                    String[] split = shipsInBay.split(":");
                    if (split.length != 2) {
                        continue;
                    }
                    Blueprint fighterBlueprint = standardConfigurations.get(split[0].trim());
                    blueprint.addFightersToBay(fighterBlueprint, new Integer(split[1].trim()).intValue());
                }
            }
        }

        return standardConfigurations;
    }

    public static Blueprint getStandardConfiguration(String configShorthand) throws InstantiationException, NotEnoughtSlotsException, IOException {
        return getStandardConfigurations().get(configShorthand);
    }
}
