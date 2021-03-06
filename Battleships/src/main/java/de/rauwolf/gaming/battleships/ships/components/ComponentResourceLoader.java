package de.rauwolf.gaming.battleships.ships.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

public class ComponentResourceLoader {
    private static Map<String, ComponentBlueprint> standardComponentBlueprints;

    private static final String                    COMPONENT_DEFINITION_FILE = "de/rauwolf/gaming/battleships/ships/components/standardComponentBlueprints.csv";

    public static Map<String, ComponentBlueprint> getStandardComponentBlueprints() throws InstantiationException, NumberFormatException, IOException {
        if (standardComponentBlueprints == null) {
            InputStream standardComponentBlueprintsResource = ComponentResourceLoader.class.getClassLoader().getResourceAsStream(COMPONENT_DEFINITION_FILE);
            standardComponentBlueprints = new HashMap<String, ComponentBlueprint>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(standardComponentBlueprintsResource));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int elementNr = 0;
                if (line.startsWith("#") || line.trim().equals("")) {
                    continue;
                }
                String[] elements = line.split(";");

                // # shorthand; name; slot size; x times: (modified stat: modifier)
                String shorthand = elements[elementNr++].trim();
                String name = elements[elementNr++].trim();
                ComponentType type = ComponentType.getEnumFromString(elements[elementNr++].trim());
                int value = new Integer(elements[elementNr++].trim());

                Map<StatType, Double> factors = new HashMap<StatType, Double>();
                Map<StatType, Integer> flatBonuses = new HashMap<StatType, Integer>();

                while (elementNr < elements.length) {
                    String[] modifier = elements[elementNr++].split(":");
                    StatType modifierType = StatType.getEnumFromString(modifier[0].trim());
                    if (modifier[1].trim().endsWith("%")) {
                        factors.put(modifierType, (new Double(modifier[1].replaceAll("%", "").trim()) / 100) + 1);
                    } else {
                        flatBonuses.put(modifierType, new Integer(modifier[1].trim()));
                    }
                }

                standardComponentBlueprints.put(shorthand, new ComponentBlueprint(shorthand, name, type, value, factors, flatBonuses));
            }
        }

        return standardComponentBlueprints;
    }

    public static ComponentBlueprint getStandardComponentBlueprint(String componentShorthand)
            throws NumberFormatException, InstantiationException, IOException {
        return getStandardComponentBlueprints().get(componentShorthand);
    }
}
