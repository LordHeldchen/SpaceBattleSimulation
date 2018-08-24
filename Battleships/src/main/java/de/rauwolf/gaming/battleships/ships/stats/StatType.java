package de.rauwolf.gaming.battleships.ships.stats;

public enum StatType {
    ACCURACY("accuracy", false, NumberType.INTEGER),
    DAMAGE("damage",  false, NumberType.INTEGER),
    AP("armor penetration",  false, NumberType.INTEGER),
    
    INITIATIVE("initiative", false, NumberType.INTEGER),
    TIMECOST("time cost", false, NumberType.INTEGER),
    MANEUVERABILITY("maneuverability", false, NumberType.INTEGER),
    PREFERRED_TARGET_PROBABILITY("preferred target probability", false, NumberType.PERCENT),
    
    THRESHOLD_GLANCE("glance threshold", false, NumberType.INTEGER),
    THRESHOLD_HIT("hit threshold", false, NumberType.INTEGER),
    THRESHOLD_CRIT("crit threshold", false, NumberType.INTEGER),
    
    CONTAINMENT("containment", false, NumberType.INTEGER),
    EPM("electronic protection measures", true, NumberType.INTEGER),
    
    MAX_HULL_STRENGTH("max. hull strength", false, NumberType.INTEGER),
    
    SHIELD_HP("shield hitpoints", true, NumberType.INTEGER),
    SHIELD_REGEN("shield regeneration", true, NumberType.INTEGER),
    SHIELD_BREAK_DURATION("shield break duration", false, NumberType.INTEGER),
    
    RESISTANCE_HEAT("heat resistance", false, NumberType.INTEGER),
    RESISTANCE_EM("em resistance", false, NumberType.INTEGER),
    RESISTANCE_KINETIC("kinetic resistance", false, NumberType.INTEGER),
    RESISTANCE_EXPLOSIVE("explosive resistance", false, NumberType.INTEGER),
    RESISTANCE_ANTIMATTER("antimatter resistance", false, NumberType.INTEGER),
    RESISTANCE_PARTICLE("particle resistance", false, NumberType.INTEGER);
      
    private final String value;
    private final boolean scalesWithSize;
    private final NumberType numberType;

    StatType(String value, boolean scalesWithSize, NumberType numberType) {
        this.value = value;
        this.scalesWithSize = scalesWithSize;
        this.numberType = numberType;
    }
    
    public static StatType getEnumFromString(String val) throws InstantiationException {
        switch (val) {
            case "ap":
                return AP;
            case "acc":
                return ACCURACY;
            case "ini":
                return INITIATIVE;
            case "glan":
                return THRESHOLD_GLANCE;
            case "hit":
                return THRESHOLD_HIT;
            case "crit":
                return THRESHOLD_CRIT;
            case "cont":
                return CONTAINMENT;
            case "man":
                return MANEUVERABILITY;
            case "shp":
                return SHIELD_HP;
            case "sreg":
                return SHIELD_REGEN;
            case "sbrk":
                return SHIELD_BREAK_DURATION;
            case "epm":
                return EPM;
            case "r-heat":
                return RESISTANCE_HEAT;
            case "r-em":
                return RESISTANCE_EM;
            case "r-kin":
                return RESISTANCE_KINETIC;
            case "r-exp":
                return RESISTANCE_EXPLOSIVE;
            case "r-anti":
                return RESISTANCE_ANTIMATTER;
            case "r-part":
                return RESISTANCE_PARTICLE;
        }
        throw new InstantiationException("Value cannot be translated to any " + StatType.class.getCanonicalName() + " enum value: " + val);
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean doesScaleWithSize() {
        return scalesWithSize;
    }

    public NumberType getNumberType() {
        return numberType;
    }
}
