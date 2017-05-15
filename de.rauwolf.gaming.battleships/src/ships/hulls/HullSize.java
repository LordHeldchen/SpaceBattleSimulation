package ships.hulls;

public enum HullSize {
    XXS("XXS"), XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");
    
    private final String value;
    
    HullSize(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
