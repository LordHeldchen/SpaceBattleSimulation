package ships.blueprints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import ships.hulls.HullType;
import ships.hulls.HullTypeResourceLoader;

public class StandardConfigurationResourceLoader {
    private static Map<String, ShipBlueprint> standardConfigurations;

    private enum Position {
        POS_SHORTHAND(0), POS_NAME(1), POS_HULL_TYPE_SHORTHAND(2), POS_WEAPON_SHORTHAND_LIST(3), POS_COMPONENT_SHORTHAND_LIST(4), POS_SHIPS_IN_BAY(
                5), POS_DESCRIPTION(6);

        private int pos;

        private Position(int pos) {
            this.pos = pos;
        }

        private int getPosition() {
            return pos;
        }
    }

    private static Map<String, ShipBlueprint> getStandardConfigurations() throws InstantiationException, NotEnoughtSlotsException, IOException {
        if (standardConfigurations == null) {
            InputStream weaponBlueprintsResource = StandardConfigurationResourceLoader.class.getClassLoader()
                    .getResourceAsStream("main/resources/standardConfigurations.csv");
            standardConfigurations = new HashMap<String, ShipBlueprint>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(weaponBlueprintsResource));

            Map<ShipBlueprint, String[]> baysToFill = new HashMap<ShipBlueprint, String[]>();
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
                String shorthand = elements[Position.POS_SHORTHAND.getPosition()].trim();
                String name = elements[Position.POS_NAME.getPosition()].trim();
                String hullTypeShorthand = elements[Position.POS_HULL_TYPE_SHORTHAND.getPosition()].trim();

                HullType hullType = HullTypeResourceLoader.getBasicHullType(hullTypeShorthand);

                String[] weaponShorthands = elements[Position.POS_WEAPON_SHORTHAND_LIST.getPosition()].split(",");
                String[] componentShorthands = elements[Position.POS_COMPONENT_SHORTHAND_LIST.getPosition()].split(",");
                String[] shipsInBay = elements[Position.POS_SHIPS_IN_BAY.getPosition()].split(",");

                String description = elements[Position.POS_DESCRIPTION.getPosition()].trim();

                ShipBlueprint blueprint = new ShipBlueprint(shorthand, name, description, hullType);
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

            for (ShipBlueprint blueprint : baysToFill.keySet()) {
                for (String shipsInBay : baysToFill.get(blueprint)) {
                    String[] split = shipsInBay.split(":");
                    if (split.length != 2) {
                        continue;
                    }
                    ShipBlueprint fighterBlueprint = standardConfigurations.get(split[0].trim());
                    blueprint.addFightersToBay(fighterBlueprint, new Integer(split[1].trim()).intValue());
                }
            }
        }

        return standardConfigurations;
    }

    public static ShipBlueprint getBlueprint(String configShorthand) throws InstantiationException, NotEnoughtSlotsException, IOException {
        return getStandardConfigurations().get(configShorthand);
    }

    public static String[] getAvailableBlueprintShorthands() throws InstantiationException, NotEnoughtSlotsException, IOException {
        Collection<String> keySet = getStandardConfigurations().keySet();
        String[] ret = new String[keySet.size()];
        return keySet.toArray(ret);
    }

    public static ShipBlueprint[] getAvailableBlueprints() throws InstantiationException, NotEnoughtSlotsException, IOException {
        Collection<ShipBlueprint> shipBlueprints = getStandardConfigurations().values().stream().sorted().collect(Collectors.toList());
        ShipBlueprint[] ret = new ShipBlueprint[shipBlueprints.size()];
        return shipBlueprints.toArray(ret);
    }

    private static void appendAttribute(StringBuilder saveStringBuilder, ShipBlueprint blueprint, Position position) {
        switch (position) {
            case POS_SHORTHAND:
                saveStringBuilder.append(blueprint.getShorthand());
                break;
            case POS_NAME:
                saveStringBuilder.append(blueprint.getName());
                break;
            case POS_COMPONENT_SHORTHAND_LIST:
                saveStringBuilder.append(blueprint.getComponents().stream().map(comp -> comp.getShorthand()).collect(Collectors.joining(",")));
                break;
            case POS_DESCRIPTION:
                saveStringBuilder.append(blueprint.getDescription());
                break;
            case POS_HULL_TYPE_SHORTHAND:
                saveStringBuilder.append(blueprint.getHullType().getShorthand());
                break;
            case POS_SHIPS_IN_BAY:
                saveStringBuilder.append(blueprint.getFighterTypesInBay().entrySet().stream()
                        .map(entry -> entry.getKey().getShorthand() + ": " + entry.getValue()).collect(Collectors.joining(", ")));
                break;
            case POS_WEAPON_SHORTHAND_LIST:
                saveStringBuilder.append(blueprint.getWeapons().stream().map(weap -> weap.getShorthand()).collect(Collectors.joining(", ")));
                break;
            default:
                throw new RuntimeException("Error while saving blueprint, got position " + position);
        }
    }

    public static void saveBlueprint(ShipBlueprint blueprint) {
        StringBuilder saveStringBuilder = new StringBuilder();
        boolean first = true;
        for (Position pos: Position.values()) {
            if (!first) {
                saveStringBuilder.append("; ");
            }
            first = false;
            appendAttribute(saveStringBuilder, blueprint, pos);
        }
        System.out.println("String that would have been saved: " + saveStringBuilder.toString());
    }
}
