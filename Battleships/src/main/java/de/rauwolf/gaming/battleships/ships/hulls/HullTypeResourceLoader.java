package de.rauwolf.gaming.battleships.ships.hulls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;

public class HullTypeResourceLoader {
    private static Map<String, HullType> basicHullTypes;

    private static final String          HULL_TYPE_DEFINITION_FILE = "de/rauwolf/gaming/battleships/ships/hulls/hullTypes.csv";

    @FunctionalInterface
    private interface setterInterface<X> {
        void fillSlot(X type, int num);
    }

    private static <X> void fillSlots(String shorthand, String[] slots, setterInterface<X> setter, X[] values) throws InstantiationException {
        int i = 0;
        int valueCount = 0;
        for (final X type : values) {
            while (i < slots.length && slots[i].trim().equals("")) {
                i++;
            }
            if (i >= slots.length) {
                throw new InstantiationException("Too many slots speficied for hull type '" + shorthand + "'");
            }
            setter.fillSlot(type, new Integer(slots[i]).intValue());
            valueCount++;
            i++;
        }
        if (valueCount != values.length) {
            throw new InstantiationException("Too few slots speficied for hull type '" + shorthand + "'");
        }
    }

    public static Map<String, HullType> getBasicHullTypes() throws InstantiationException {
        if (basicHullTypes == null) {
            InputStream basicHullTypesResource = HullTypeResourceLoader.class.getClassLoader().getResourceAsStream(HULL_TYPE_DEFINITION_FILE);
            basicHullTypes = new HashMap<String, HullType>();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(basicHullTypesResource));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    int elementNr = 0;
                    if (line.startsWith("#") || line.trim().equals("")) {
                        continue;
                    }
                    // shorthand; name; baseStartInitiative; baseManeuverability;
                    // baseHullStrength; baseGlanceThreshold; baseHitThreshold;
                    // baseCritThreshold; description
                    String[] elements = line.split(";");
                    String shorthand = elements[elementNr++].trim();
                    String name = elements[elementNr++].trim();
                    int baseStartInitiative = new Integer(elements[elementNr++].trim());
                    int baseManeuverability = new Integer(elements[elementNr++].trim());
                    int baseHullStrength = new Integer(elements[elementNr++].trim());
                    int baseGlanceThreshold = new Integer(elements[elementNr++].trim());
                    int baseHitThreshold = new Integer(elements[elementNr++].trim());
                    int baseCritThreshold = new Integer(elements[elementNr++].trim());
                    int baseContainment = new Integer(elements[elementNr++].trim());
                    int baseEPM = new Integer(elements[elementNr++].trim());

                    String[] weaponSlots = elements[elementNr++].split(" ");
                    String[] componentSlots = elements[elementNr++].split(" ");

                    SizeClass sizeCategory = SizeClass.valueOf(elements[elementNr++].trim());
                    String[] bays = elements[elementNr++].split(",");

                    int value = new Integer(elements[elementNr++].trim());
                    String description = elements[elementNr++].trim();
                    HullType hullType = new HullType(shorthand, name, sizeCategory, baseGlanceThreshold, baseHitThreshold, baseCritThreshold, baseHullStrength,
                            baseContainment, baseEPM, baseStartInitiative, baseManeuverability, value, description);

                    fillSlots(shorthand, weaponSlots, hullType::setAvailableWeaponSlotsForSize, SizeClass.values());
                    fillSlots(shorthand, componentSlots, hullType::setAvailableComponentSlotsForType, ComponentType.values());

                    for (String bay : bays) {
                        if (bay.trim().equals("")) {
                            continue;
                        }
                        String[] bayDefinition = bay.trim().split(":");
                        SizeClass baySizeCategory = SizeClass.valueOf(bayDefinition[0].trim());
                        int bayAmount = new Integer(bayDefinition[1].trim()).intValue();

                        hullType.setAvailableShipBaysForSize(baySizeCategory, bayAmount);
                    }

                    basicHullTypes.put(shorthand, hullType);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return basicHullTypes;
    }

    public static HullType getBasicHullType(String shorthand) throws InstantiationException {
        return getBasicHullTypes().get(shorthand);
    }
}
