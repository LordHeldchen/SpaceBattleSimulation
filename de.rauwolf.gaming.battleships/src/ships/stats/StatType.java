package ships.stats;

public enum StatType {
    ACCURACY("accuracy",  false),
    DAMAGE("damage",  false),
    AP("armor penetration",  false),
    
    INITIATIVE("initiative", false),
    TIMECOST("time cost", false),
    EVASION("evasion", false),
    
    THRESHOLD_GLANCE("glance threshold", false),
    THRESHOLD_HIT("hit threshold", false),
    THRESHOLD_CRIT("crit threshold", false),
    
    CONTAINMENT("containment", false),
    EPM("electronic protection measures", true),
    
    MAX_HULL_STRENGTH("max. hull strength", false),
    
    SHIELD_HP("shield hitpoints", true),
    SHIELD_REGEN("shield regeneration", true),
    SHIELD_BREAK_DURATION("shield break duration", true),
    
    RESISTANCE_HEAT("heat resistance", false),
    RESISTANCE_EM("em resistance", false),
    RESISTANCE_KINETIC("kinetic resistance", false),
    RESISTANCE_EXPLOSIVE("explosive resistance", false),
    RESISTANCE_ANTIMATTER("antimatter resistance", false),
    RESISTANCE_PARTICLE("particle resistance", false);
      
    private final String value;
    private final boolean scalesWithSize;

    StatType(String value, boolean scalesWithSize) {
        this.value = value;
        this.scalesWithSize = scalesWithSize;
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
            case "eva":
                return EVASION;
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
}
