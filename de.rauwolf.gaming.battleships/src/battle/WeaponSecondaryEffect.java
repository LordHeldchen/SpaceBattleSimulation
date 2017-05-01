package battle;

public enum WeaponSecondaryEffect {
    SHIELD_PIERCE_FLAT("spierceflat"), SHIELD_PIERCE_PERCENT("spiercepercent"), TICKLOSS("tickloss"), DEGRADE("degrade");

    private final String value;

    WeaponSecondaryEffect(String value) {
        this.value = value;
    }

    public static WeaponSecondaryEffect getEnumFromString(String val) throws InstantiationException {
        switch (val) {
        case "spierceflat":
            return SHIELD_PIERCE_FLAT;
        case "spiercepercent":
            return SHIELD_PIERCE_PERCENT;
        case "tickloss":
            return TICKLOSS;
        case "degrade":
            return DEGRADE;
        }
        throw new InstantiationException("Value cannot be translated to any WeaponSecondaryEffect enum value: " + val);
    }

    @Override
    public String toString() {
        return value;
    }
}
