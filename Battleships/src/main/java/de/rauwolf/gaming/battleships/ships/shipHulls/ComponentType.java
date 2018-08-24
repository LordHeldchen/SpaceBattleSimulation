package de.rauwolf.gaming.battleships.ships.shipHulls;

public enum ComponentType {
    ALLROUND("Allround"), DEFENSE("Defense"), ELECTRONICS("Electronics"), PROPULSION("Propulsion"), EWAR("EWar");

    private final String value;

    ComponentType(String value) {
        this.value = value;
    }
    
    public static ComponentType getEnumFromString(String val) throws InstantiationException {
        switch (val) {
        case "A":
            return ALLROUND;
        case "D":
            return DEFENSE;
        case "E":
            return ELECTRONICS;
        case "P":
            return PROPULSION;
        case "W":
            return EWAR;
        };
        throw new InstantiationException("Value cannot be translated to any ComponentType enum value: " + val);
    }

    @Override
    public String toString() {
        return value;
    }
}
