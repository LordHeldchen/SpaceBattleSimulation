package ships.resourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ships.blueprints.ComponentBlueprint;
import ships.shipHulls.ComponentType;

public class ComponentResourceLoader {
    private static Map<String, ComponentBlueprint> standardComponentBlueprints;

    public static Map<String, ComponentBlueprint> getStandardComponentBlueprints() throws InstantiationException, NumberFormatException, IOException {
        if (standardComponentBlueprints == null) {
            InputStream standardComponentBlueprintsResource = ComponentResourceLoader.class.getClassLoader()
                    .getResourceAsStream("main/resources/standardComponentBlueprints.csv");
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

                Map<String, Double> factors = new HashMap<String, Double>();
                Map<String, Integer> flatBonuses = new HashMap<String, Integer>();

                while (elementNr < elements.length) {
                    String[] modifier = elements[elementNr++].split(":");
                    if (modifier[1].trim().endsWith("%")) {
                        factors.put(modifier[0].trim(), (new Double(modifier[1].replaceAll("%", "").trim()) / 100) + 1);
                    } else {
                        flatBonuses.put(modifier[0].trim(), new Integer(modifier[1].trim()));
                    }
                }

                standardComponentBlueprints.put(shorthand, new ComponentBlueprint(name, type, value, factors, flatBonuses));
            }
        }

        return standardComponentBlueprints;
    }

    public static ComponentBlueprint getStandardComponentBlueprint(String componentShorthand) throws NumberFormatException, InstantiationException, IOException {
        return getStandardComponentBlueprints().get(componentShorthand);
    }
}
