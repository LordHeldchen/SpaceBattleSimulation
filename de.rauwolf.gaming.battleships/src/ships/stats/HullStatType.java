package ships.stats;

public enum HullStatType {
    ACCURACY("accuracy"), INITIATIVE("initiative"), DEFENSE("Defense"),  
    GLANCE_THRESHOLD("glance threshold"), HIT_THRESHOLD("hit threshold"), CRIT_THRESHOLD("crit threshold"),
    CONTAINMENT("containment"), EVASION("evasion"), SHIELD_HP("shield hitpoints"), SHIELD_REGEN("shield regeneration"),
    SHIELD_BREAK_DURATION("shield break duration"), EPM("electronic protection measures"),
    RESISTANCE_HEAT("heat resistance"), RESISTANCE_EM("em resistance"), RESISTANCE_KINETIC("kinetic resistance"),
    RESISTANCE_EXPLOSIVE("explosive resistance"), RESISTANCE_ANTIMATTER("antimatter resistance"),
    RESISTANCE_PARTICLE("particle resistance");
      
    private final String value;

    HullStatType(String value) {
        this.value = value;
    }
    
    public static HullStatType getEnumFromString(String val) throws InstantiationException {
        switch (val) {
        case "acc":
            return ACCURACY;
        case "ini":
            return INITIATIVE;
        case "glan":
            return GLANCE_THRESHOLD;
        case "hit":
            return HIT_THRESHOLD;
        case "crit":
            return CRIT_THRESHOLD;
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
        };
        throw new InstantiationException("Value cannot be translated to any ComponentType enum value: " + val);
    }

    @Override
    public String toString() {
        return value;
    }
}
