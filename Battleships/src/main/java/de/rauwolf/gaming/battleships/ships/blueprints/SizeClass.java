package de.rauwolf.gaming.battleships.ships.blueprints;

public enum SizeClass {
    XXS("XXS"), XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");

    private final String value;
    
    SizeClass(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
