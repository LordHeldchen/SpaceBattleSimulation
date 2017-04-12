package ships.resourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ships.blueprints.Blueprint;
import ships.blueprints.NotEnoughtSlotsException;
import ships.shipHulls.HullType;

public class StandardConfigurationResourceLoader {
    private static Map<String, Blueprint> standardConfigurations;

    private static Map<String, Blueprint> getStandardConfigurations() throws InstantiationException, NotEnoughtSlotsException, IOException {
        if (standardConfigurations == null) {
            InputStream weaponBlueprintsResource = StandardConfigurationResourceLoader.class.getClassLoader()
                    .getResourceAsStream("main/resources/standardConfigurations.csv");
            standardConfigurations = new HashMap<String, Blueprint>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(weaponBlueprintsResource));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().equals("")) {
                    continue;
                }
                // #shorthand; name; hullType shorthand; x times weapon
                // shorthand (separator ","); x times component shorthand
                // (separator ","); description
                String[] elements = line.split(";");
                String shorthand = elements[0].trim();
                String name = elements[1].trim();
                String hullTypeShorthand = elements[2].trim();

                HullType hullType = HullTypeResourceLoader.getBasicHullType(hullTypeShorthand);

                String[] weaponShorthands = elements[3].split(",");
                String[] componentShorthands = elements[4].split(",");
                String description = elements[5].trim();

                Blueprint blueprint = new Blueprint(name, description, hullType);
                for (String weaponShorthand : weaponShorthands) {
                    blueprint.addStandardWeapon(weaponShorthand.trim());
                }
                for (String componentShorthand : componentShorthands) {
                    blueprint.addStandardComponent(componentShorthand.trim());
                }
                standardConfigurations.put(shorthand, blueprint);
            }
        }

        return standardConfigurations;
    }

    public static Blueprint getStandardConfiguration(String configShorthand) throws InstantiationException, NotEnoughtSlotsException, IOException {
        return getStandardConfigurations().get(configShorthand);
    }
}
