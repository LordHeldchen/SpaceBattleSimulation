package ships.shipHulls;

public enum HullSize {
    XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");
    
    private final String value;
    
    HullSize(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
