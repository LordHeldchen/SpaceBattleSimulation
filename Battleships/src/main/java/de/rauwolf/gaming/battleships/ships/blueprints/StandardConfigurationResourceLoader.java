package de.rauwolf.gaming.battleships.ships.blueprints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongSizeForSlotException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongTypeForSlotException;
import de.rauwolf.gaming.battleships.ships.components.ComponentBlueprint;
import de.rauwolf.gaming.battleships.ships.components.ComponentResourceLoader;
import de.rauwolf.gaming.battleships.ships.hulls.HullType;
import de.rauwolf.gaming.battleships.ships.hulls.HullTypeResourceLoader;
import de.rauwolf.gaming.battleships.ships.resourceLoader.WeaponResourceLoader;
import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponBlueprint;

public class StandardConfigurationResourceLoader {
    private static Map<String, ShipBlueprint> standardConfigurations;

    private static final String               STANDARD_CONFIGURATIONS_FILE = "de/rauwolf/gaming/battleships/ships/blueprints/standardConfigurations.csv";

    private enum Position {
        POS_SHORTHAND(0), POS_NAME(1), POS_HULL_TYPE_SHORTHAND(2), POS_WEAPON_SHORTHAND_LIST(3), POS_COMPONENT_SHORTHAND_LIST(4), POS_SHIPS_IN_BAY(5),
        POS_DESCRIPTION(6);

        private int pos;

        private Position(int pos) {
            this.pos = pos;
        }

        private int getPosition() {
            return pos;
        }
    }

    private static Map<String, ShipBlueprint> getStandardConfigurations()
            throws NumberFormatException, InstantiationException, IOException, WrongSizeForSlotException, NotEnoughtSlotsException, WrongTypeForSlotException {
        if (standardConfigurations == null) {
            InputStream standardConfigurationsResource = StandardConfigurationResourceLoader.class.getClassLoader()
                    .getResourceAsStream(STANDARD_CONFIGURATIONS_FILE);
            standardConfigurations = new HashMap<String, ShipBlueprint>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(standardConfigurationsResource));

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
                        WeaponBlueprint weaponBlueprint = WeaponResourceLoader.getStandardWeaponBlueprint(weaponShorthand.trim());
                        SizeClass size = weaponBlueprint.getSize();
                        List<SizeLimitedSlot<WeaponBlueprint>> weaponSlotsForSize = blueprint.getWeaponSlotsForSize(size);
                        for (SizeLimitedSlot<WeaponBlueprint> slot : weaponSlotsForSize) {
                            if (slot.isEmpty()) {
                                slot.setSlottedElement(weaponBlueprint);
                                break;
                            }
                        }
                    }
                }
                for (String componentShorthand : componentShorthands) {
                    if (!componentShorthand.trim().equals("")) {
                        ComponentBlueprint componentBlueprint = ComponentResourceLoader.getStandardComponentBlueprint(componentShorthand.trim());
                        ComponentType type = componentBlueprint.getType();
                        List<TypeLimitedSlot<ComponentBlueprint>> componentSlotsForSize = blueprint.getComponentSlotsForType(type);
                        for (TypeLimitedSlot<ComponentBlueprint> slot : componentSlotsForSize) {
                            if (slot.isEmpty()) {
                                slot.setSlottedElement(componentBlueprint);
                                break;
                            }
                        }
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

    public static ShipBlueprint getBlueprint(String configShorthand)
            throws NumberFormatException, InstantiationException, IOException, WrongSizeForSlotException, NotEnoughtSlotsException, WrongTypeForSlotException {

        return getStandardConfigurations().get(configShorthand);
    }

    public static String[] getAvailableBlueprintShorthands()
            throws InstantiationException, NotEnoughtSlotsException, IOException, NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {

        Collection<String> keySet = getStandardConfigurations().keySet();
        String[] ret = new String[keySet.size()];
        return keySet.toArray(ret);
    }

    public static ShipBlueprint[] getAvailableBlueprints()
            throws InstantiationException, NotEnoughtSlotsException, IOException, NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {

        Collection<ShipBlueprint> shipBlueprints = getStandardConfigurations().values().stream().sorted().collect(Collectors.toList());
        ShipBlueprint[] ret = new ShipBlueprint[shipBlueprints.size()];
        return shipBlueprints.toArray(ret);
    }

    private static void appendAttribute(StringBuilder saveStringBuilder, ShipBlueprint blueprint, Position position) {
        switch (position) {
            case POS_SHORTHAND:
                saveStringBuilder.append(blueprint.getId());
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
                saveStringBuilder.append(blueprint.getFighterTypesInBay().entrySet().stream().map(entry -> entry.getKey().getId() + ": " + entry.getValue())
                        .collect(Collectors.joining(", ")));
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
        for (Position pos : Position.values()) {
            if (!first) {
                saveStringBuilder.append("; ");
            }
            first = false;
            appendAttribute(saveStringBuilder, blueprint, pos);
        }
        System.out.println("String that would have been saved: " + saveStringBuilder.toString());
    }
}
