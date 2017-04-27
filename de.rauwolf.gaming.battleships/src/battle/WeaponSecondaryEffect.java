package battle;

public enum WeaponSecondaryEffect {
    SHIELD_PIERCE_FLAT, SHIELD_PIERCE_PERCENT, TICKLOSS;
    
    public static WeaponSecondaryEffect getEnumFromString(String val) throws InstantiationException {
        switch (val) {
        case "spierceflat":
            return SHIELD_PIERCE_FLAT;
        case "spiercepercent":
            return SHIELD_PIERCE_PERCENT;
        case "tickloss":
            return TICKLOSS;
        };
        throw new InstantiationException("Value cannot be translated to any WeaponSecondaryEffect enum value: " + val);
    }
}
