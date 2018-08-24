package de.rauwolf.gaming.battleships.ships.shipHulls;

public enum ComponentType {
    ALLROUND("Allround", true), DEFENSE("Defense", false), ELECTRONICS("Electronics", false), PROPULSION("Propulsion", false), EWAR("EWar", false);

    private final String  value;
    private final boolean ignoresTypeRestrictions;

    ComponentType(String value, boolean ignoresTypeRestrictions) {
        this.value = value;
        this.ignoresTypeRestrictions = ignoresTypeRestrictions;
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
        }
        ;
        throw new InstantiationException("Value cannot be translated to any ComponentType enum value: " + val);
    }

    public boolean isIgnoresTypeRestrictions() {
        return ignoresTypeRestrictions;
    }

    @Override
    public String toString() {
        return value;
    }
}
